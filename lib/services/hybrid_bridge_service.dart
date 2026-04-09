import 'dart:collection';
import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter_inappwebview/flutter_inappwebview.dart';

import '../models/hybrid_runtime_context.dart';

class HybridBridgeService {
  const HybridBridgeService();

  Future<void> bootstrapCookies(
    HybridRuntimeContext context, {
    void Function(String message)? onLog,
  }) async {
    final CookieManager cookieManager = CookieManager.instance();
    final WebUri webUri = WebUri(context.resolvedUri.toString());

    await cookieManager.deleteCookies(url: webUri);
    onLog?.call('Cleared old cookies for ${context.resolvedUri.host}.');

    for (final HybridCookieSeed cookie in context.cookieSeeds) {
      await cookieManager.setCookie(
        url: webUri,
        name: cookie.name,
        value: cookie.value,
        path: '/',
        isSecure: cookie.isSecure,
      );
    }

    onLog?.call(
      'Bootstrapped ${context.cookieSeeds.length} cookies for ${context.resolvedUri.host}.',
    );
  }

  UnmodifiableListView<UserScript> buildInitialUserScripts(
    HybridRuntimeContext context,
  ) {
    return UnmodifiableListView<UserScript>(
      <UserScript>[
        UserScript(
          groupName: 'bbtotal-hybrid',
          injectionTime: UserScriptInjectionTime.AT_DOCUMENT_START,
          source: _buildDocumentStartScript(context),
        ),
      ],
    );
  }

  void registerHandlers({
    required InAppWebViewController controller,
    required void Function(String message) onLog,
    required void Function(String payload) onPostMessage,
    required void Function(String url) onOpenUrl,
    required VoidCallback onClosePage,
  }) {
    controller.addJavaScriptHandler(
      handlerName: 'nativeLog',
      callback: (List<dynamic> arguments) {
        final String message = arguments.isEmpty ? '' : arguments.first.toString();
        onLog(message.isEmpty ? 'empty nativeLog payload' : message);
        return null;
      },
    );

    controller.addJavaScriptHandler(
      handlerName: 'nativePostMessage',
      callback: (List<dynamic> arguments) {
        final String payload = arguments.isEmpty ? '' : arguments.first.toString();
        onPostMessage(payload);
        return null;
      },
    );

    controller.addJavaScriptHandler(
      handlerName: 'nativeOpenUrl',
      callback: (List<dynamic> arguments) {
        final String url = arguments.isEmpty ? '' : arguments.first.toString();
        onOpenUrl(url);
        return null;
      },
    );

    controller.addJavaScriptHandler(
      handlerName: 'nativeClosePage',
      callback: (List<dynamic> arguments) {
        onClosePage();
        return null;
      },
    );
  }

  String _buildDocumentStartScript(HybridRuntimeContext context) {
    final String runtimeJson = jsonEncode(context.toScriptRuntime());
    return '''
(() => {
  if (window.__bbtotalHybridInstalled) {
    return;
  }

  const runtime = $runtimeJson;
  const pendingCalls = [];
  let platformReady =
    !!(window.flutter_inappwebview && window.flutter_inappwebview.callHandler);

  const callHandler = (name, ...args) => {
    if (
      platformReady &&
      window.flutter_inappwebview &&
      typeof window.flutter_inappwebview.callHandler === 'function'
    ) {
      return window.flutter_inappwebview.callHandler(name, ...args);
    }
    pendingCalls.push([name, args]);
    return Promise.resolve(null);
  };

  const flushPendingCalls = () => {
    if (
      !window.flutter_inappwebview ||
      typeof window.flutter_inappwebview.callHandler !== 'function'
    ) {
      return;
    }
    while (pendingCalls.length > 0) {
      const [name, args] = pendingCalls.shift();
      try {
        window.flutter_inappwebview.callHandler(name, ...args);
      } catch (_) {}
    }
  };

  window.addEventListener('flutterInAppWebViewPlatformReady', () => {
    platformReady = true;
    flushPendingCalls();
  });

  const preview = (value, max = 240) => {
    const raw =
      typeof value === 'string'
        ? value
        : (() => {
            try {
              const serialized = JSON.stringify(value);
              return serialized === undefined ? String(value) : serialized;
            } catch (_) {
              return String(value);
            }
          })();
    const normalized = raw == null ? '' : String(raw);
    return normalized.length > max
      ? normalized.slice(0, max) + ' ...truncated'
      : normalized;
  };

  const report = (message) => {
    callHandler('nativeLog', String(message || ''));
  };

  const stringify = (value) => {
    try {
      return JSON.stringify(value);
    } catch (_) {
      return '{}';
    }
  };

  const applyStorage = (store, payload) => {
    if (!store || !payload) {
      return;
    }
    Object.entries(payload).forEach(([key, value]) => {
      if (value == null || value === '') {
        return;
      }
      try {
        store.setItem(key, String(value));
      } catch (_) {}
    });
  };

  const mergeDefined = (...sources) => {
    const target = {};
    sources.forEach((source) => {
      if (!source || typeof source !== 'object') {
        return;
      }
      Object.entries(source).forEach(([key, value]) => {
        if (value !== undefined) {
          target[key] = value;
        }
      });
    });
    return target;
  };

  const parseObjectPayload = (value) => {
    const parsed = parseJsonLikePayload(value);
    if (parsed && typeof parsed === 'object' && !Array.isArray(parsed)) {
      return parsed;
    }
    return null;
  };

  const normalizeLocationPayload = (value) => {
    const fallback =
      runtime.locationPayload && typeof runtime.locationPayload === 'object'
        ? runtime.locationPayload
        : { data: {}, errorCode: 1, msg: '定位尚未准备' };
    const parsed = parseObjectPayload(value);
    if (!parsed) {
      return fallback;
    }

    if (parsed.data && typeof parsed.data === 'object' && !Array.isArray(parsed.data)) {
      return mergeDefined(fallback, parsed, {
        data: mergeDefined(fallback.data || {}, parsed.data || {}),
      });
    }

    const locationKeys = [
      'latitude',
      'longitude',
      'address',
      'province',
      'city',
      'district',
      'street',
      'cityCode',
      'provinceCode',
      'adCode',
      'provinceReferred',
      'isVirtuallocation',
    ];
    const looksLikeLocation = locationKeys.some((key) =>
      Object.prototype.hasOwnProperty.call(parsed, key),
    );
    if (!looksLikeLocation) {
      return mergeDefined(fallback, parsed);
    }

    return mergeDefined(fallback, parsed, {
      data: mergeDefined(fallback.data || {}, parsed),
    });
  };

  const normalizeDeviceFields = (payload) => {
    const next =
      payload && typeof payload === 'object' && !Array.isArray(payload)
        ? mergeDefined(payload)
        : {};
    const deviceCandidate = [
      next.sbsbm,
      next.dx_29_sbsbm,
      next.deviceuuid,
      next.deviceId,
      runtime.pageParams && runtime.pageParams.sbsbm,
      runtime.pageParams && runtime.pageParams.dx_29_sbsbm,
      runtime.pageParams && runtime.pageParams.deviceuuid,
      runtime.pageParams && runtime.pageParams.deviceId,
      runtime.bbgrxx && runtime.bbgrxx.sbsbm,
      runtime.bbgrxx && runtime.bbgrxx.dx_29_sbsbm,
      runtime.bbgrxx && runtime.bbgrxx.deviceuuid,
      runtime.bbgrxx && runtime.bbgrxx.deviceId,
    ].find((value) => typeof value === 'string' && value.trim());

    if (deviceCandidate) {
      if (!next.sbsbm) {
        next.sbsbm = deviceCandidate;
      }
      if (!next.dx_29_sbsbm) {
        next.dx_29_sbsbm = deviceCandidate;
      }
      if (!next.deviceuuid) {
        next.deviceuuid = deviceCandidate;
      }
      if (!next.deviceId) {
        next.deviceId = deviceCandidate;
      }
    }

    return next;
  };

  const syncBbgrxx = (payload) => {
    const normalized = normalizeDeviceFields(
      mergeDefined(runtime.bbgrxx || {}, window.bbgrxx || {}, payload || {}),
    );
    normalized.locationMsg = normalizeLocationPayload(normalized.locationMsg);
    normalized.updatingLocationMsg = normalizeLocationPayload(
      normalized.updatingLocationMsg || normalized.locationMsg,
    );
    window.bbgrxx = normalized;
    const serialized = stringify(normalized);
    try {
      if (window.localStorage) {
        window.localStorage.setItem('bbgrxx', serialized);
      }
      if (window.sessionStorage) {
        window.sessionStorage.setItem('bbgrxx', serialized);
      }
    } catch (_) {}
    return normalized;
  };

  applyStorage(window.localStorage, runtime.storageSeed);
  applyStorage(window.sessionStorage, runtime.storageSeed);

  try {
    syncBbgrxx(runtime.bbgrxx || {});
  } catch (_) {
    window.bbgrxx = normalizeDeviceFields(runtime.bbgrxx || {});
  }

  window.__bbtotalPageParams = runtime.pageParams || {};
  window.__bbtotalPageParamsJson = runtime.pageParamsJson || '{}';
  window.__bbtotalLaunchPayload = runtime.launchPayload || {};
  window.__bbtotalLaunchPayloadJson = runtime.launchPayloadJson || '';
  if (!window.params && runtime.pageParamsJson) {
    window.params = runtime.pageParamsJson;
  }
  if (!window.params2 && runtime.launchPayloadJson) {
    window.params2 = runtime.launchPayloadJson;
  }

  const locationPayload = runtime.locationPayload || {};
  const wifiPayload = runtime.wifiInfo || {};
  const resolvedUrl = (() => {
    try {
      return new URL(runtime.resolvedUrl || window.location.href, window.location.href);
    } catch (_) {
      return null;
    }
  })();
  const cpbs =
    (resolvedUrl && resolvedUrl.searchParams && resolvedUrl.searchParams.get('cpbs')) ||
    runtime.bridgeContext.cpbs ||
    'gjj';
  const clientId = 'bbPro';
  const appSecret = 'SY6EE1BFA49CE3482EA003DD5C87CBFF';
  const appUrl = 'https://app.jbysoft.com';

  const buildTimestamp = () => {
    const now = new Date();
    const two = (value) => String(value).padStart(2, '0');
    return [
      now.getFullYear(),
      two(now.getMonth() + 1),
      two(now.getDate()),
      two(now.getHours()),
      two(now.getMinutes()),
      two(now.getSeconds()),
    ].join('');
  };

  const md5 = (input) => {
    const text = String(input ?? '');

    const rotateLeft = (value, shift) => (value << shift) | (value >>> (32 - shift));
    const addUnsigned = (x, y) => {
      const x4 = x & 0x40000000;
      const y4 = y & 0x40000000;
      const x8 = x & 0x80000000;
      const y8 = y & 0x80000000;
      const result = (x & 0x3fffffff) + (y & 0x3fffffff);
      if (x4 & y4) {
        return result ^ 0x80000000 ^ x8 ^ y8;
      }
      if (x4 | y4) {
        if (result & 0x40000000) {
          return result ^ 0xc0000000 ^ x8 ^ y8;
        }
        return result ^ 0x40000000 ^ x8 ^ y8;
      }
      return result ^ x8 ^ y8;
    };

    const f = (x, y, z) => (x & y) | (~x & z);
    const g = (x, y, z) => (x & z) | (y & ~z);
    const h = (x, y, z) => x ^ y ^ z;
    const i = (x, y, z) => y ^ (x | ~z);

    const ff = (a, b, c, d, x, s, ac) => {
      a = addUnsigned(a, addUnsigned(addUnsigned(f(b, c, d), x), ac));
      return addUnsigned(rotateLeft(a, s), b);
    };
    const gg = (a, b, c, d, x, s, ac) => {
      a = addUnsigned(a, addUnsigned(addUnsigned(g(b, c, d), x), ac));
      return addUnsigned(rotateLeft(a, s), b);
    };
    const hh = (a, b, c, d, x, s, ac) => {
      a = addUnsigned(a, addUnsigned(addUnsigned(h(b, c, d), x), ac));
      return addUnsigned(rotateLeft(a, s), b);
    };
    const ii = (a, b, c, d, x, s, ac) => {
      a = addUnsigned(a, addUnsigned(addUnsigned(i(b, c, d), x), ac));
      return addUnsigned(rotateLeft(a, s), b);
    };

    const convertToWordArray = (value) => {
      const messageLength = value.length;
      const numberOfWordsTemp1 = messageLength + 8;
      const numberOfWordsTemp2 =
        (numberOfWordsTemp1 - (numberOfWordsTemp1 % 64)) / 64;
      const numberOfWords = (numberOfWordsTemp2 + 1) * 16;
      const wordArray = new Array(numberOfWords - 1);
      let bytePosition = 0;
      let byteCount = 0;

      while (byteCount < messageLength) {
        const wordCount = (byteCount - (byteCount % 4)) / 4;
        bytePosition = (byteCount % 4) * 8;
        wordArray[wordCount] =
          wordArray[wordCount] | (value.charCodeAt(byteCount) << bytePosition);
        byteCount += 1;
      }

      const wordCount = (byteCount - (byteCount % 4)) / 4;
      bytePosition = (byteCount % 4) * 8;
      wordArray[wordCount] = wordArray[wordCount] | (0x80 << bytePosition);
      wordArray[numberOfWords - 2] = messageLength << 3;
      wordArray[numberOfWords - 1] = messageLength >>> 29;
      return wordArray;
    };

    const wordToHex = (value) => {
      let hex = '';
      for (let count = 0; count <= 3; count += 1) {
        const byte = (value >>> (count * 8)) & 255;
        hex += ('0' + byte.toString(16)).slice(-2);
      }
      return hex;
    };

    const utf8Encode = (value) => unescape(encodeURIComponent(value));
    const x = convertToWordArray(utf8Encode(text));
    let a = 0x67452301;
    let b = 0xefcdab89;
    let c = 0x98badcfe;
    let d = 0x10325476;

    for (let k = 0; k < x.length; k += 16) {
      const aa = a;
      const bb = b;
      const cc = c;
      const dd = d;

      a = ff(a, b, c, d, x[k + 0], 7, 0xd76aa478);
      d = ff(d, a, b, c, x[k + 1], 12, 0xe8c7b756);
      c = ff(c, d, a, b, x[k + 2], 17, 0x242070db);
      b = ff(b, c, d, a, x[k + 3], 22, 0xc1bdceee);
      a = ff(a, b, c, d, x[k + 4], 7, 0xf57c0faf);
      d = ff(d, a, b, c, x[k + 5], 12, 0x4787c62a);
      c = ff(c, d, a, b, x[k + 6], 17, 0xa8304613);
      b = ff(b, c, d, a, x[k + 7], 22, 0xfd469501);
      a = ff(a, b, c, d, x[k + 8], 7, 0x698098d8);
      d = ff(d, a, b, c, x[k + 9], 12, 0x8b44f7af);
      c = ff(c, d, a, b, x[k + 10], 17, 0xffff5bb1);
      b = ff(b, c, d, a, x[k + 11], 22, 0x895cd7be);
      a = ff(a, b, c, d, x[k + 12], 7, 0x6b901122);
      d = ff(d, a, b, c, x[k + 13], 12, 0xfd987193);
      c = ff(c, d, a, b, x[k + 14], 17, 0xa679438e);
      b = ff(b, c, d, a, x[k + 15], 22, 0x49b40821);

      a = gg(a, b, c, d, x[k + 1], 5, 0xf61e2562);
      d = gg(d, a, b, c, x[k + 6], 9, 0xc040b340);
      c = gg(c, d, a, b, x[k + 11], 14, 0x265e5a51);
      b = gg(b, c, d, a, x[k + 0], 20, 0xe9b6c7aa);
      a = gg(a, b, c, d, x[k + 5], 5, 0xd62f105d);
      d = gg(d, a, b, c, x[k + 10], 9, 0x02441453);
      c = gg(c, d, a, b, x[k + 15], 14, 0xd8a1e681);
      b = gg(b, c, d, a, x[k + 4], 20, 0xe7d3fbc8);
      a = gg(a, b, c, d, x[k + 9], 5, 0x21e1cde6);
      d = gg(d, a, b, c, x[k + 14], 9, 0xc33707d6);
      c = gg(c, d, a, b, x[k + 3], 14, 0xf4d50d87);
      b = gg(b, c, d, a, x[k + 8], 20, 0x455a14ed);
      a = gg(a, b, c, d, x[k + 13], 5, 0xa9e3e905);
      d = gg(d, a, b, c, x[k + 2], 9, 0xfcefa3f8);
      c = gg(c, d, a, b, x[k + 7], 14, 0x676f02d9);
      b = gg(b, c, d, a, x[k + 12], 20, 0x8d2a4c8a);

      a = hh(a, b, c, d, x[k + 5], 4, 0xfffa3942);
      d = hh(d, a, b, c, x[k + 8], 11, 0x8771f681);
      c = hh(c, d, a, b, x[k + 11], 16, 0x6d9d6122);
      b = hh(b, c, d, a, x[k + 14], 23, 0xfde5380c);
      a = hh(a, b, c, d, x[k + 1], 4, 0xa4beea44);
      d = hh(d, a, b, c, x[k + 4], 11, 0x4bdecfa9);
      c = hh(c, d, a, b, x[k + 7], 16, 0xf6bb4b60);
      b = hh(b, c, d, a, x[k + 10], 23, 0xbebfbc70);
      a = hh(a, b, c, d, x[k + 13], 4, 0x289b7ec6);
      d = hh(d, a, b, c, x[k + 0], 11, 0xeaa127fa);
      c = hh(c, d, a, b, x[k + 3], 16, 0xd4ef3085);
      b = hh(b, c, d, a, x[k + 6], 23, 0x04881d05);
      a = hh(a, b, c, d, x[k + 9], 4, 0xd9d4d039);
      d = hh(d, a, b, c, x[k + 12], 11, 0xe6db99e5);
      c = hh(c, d, a, b, x[k + 15], 16, 0x1fa27cf8);
      b = hh(b, c, d, a, x[k + 2], 23, 0xc4ac5665);

      a = ii(a, b, c, d, x[k + 0], 6, 0xf4292244);
      d = ii(d, a, b, c, x[k + 7], 10, 0x432aff97);
      c = ii(c, d, a, b, x[k + 14], 15, 0xab9423a7);
      b = ii(b, c, d, a, x[k + 5], 21, 0xfc93a039);
      a = ii(a, b, c, d, x[k + 12], 6, 0x655b59c3);
      d = ii(d, a, b, c, x[k + 3], 10, 0x8f0ccc92);
      c = ii(c, d, a, b, x[k + 10], 15, 0xffeff47d);
      b = ii(b, c, d, a, x[k + 1], 21, 0x85845dd1);
      a = ii(a, b, c, d, x[k + 8], 6, 0x6fa87e4f);
      d = ii(d, a, b, c, x[k + 15], 10, 0xfe2ce6e0);
      c = ii(c, d, a, b, x[k + 6], 15, 0xa3014314);
      b = ii(b, c, d, a, x[k + 13], 21, 0x4e0811a1);
      a = ii(a, b, c, d, x[k + 4], 6, 0xf7537e82);
      d = ii(d, a, b, c, x[k + 11], 10, 0xbd3af235);
      c = ii(c, d, a, b, x[k + 2], 15, 0x2ad7d2bb);
      b = ii(b, c, d, a, x[k + 9], 21, 0xeb86d391);

      a = addUnsigned(a, aa);
      b = addUnsigned(b, bb);
      c = addUnsigned(c, cc);
      d = addUnsigned(d, dd);
    }

    return (wordToHex(a) + wordToHex(b) + wordToHex(c) + wordToHex(d)).toLowerCase();
  };

  const invokeNamedCallback = (name, payload, source) => {
    if (typeof name !== 'string' || !name.trim()) {
      return false;
    }

    const callbackName = name.trim();
    const candidate =
      typeof window[callbackName] === 'function'
        ? window[callbackName]
        : callbackName.split('.').reduce((current, part) => {
            if (!current || typeof current !== 'object') {
              return undefined;
            }
            return current[part];
          }, window);

    if (typeof candidate !== 'function') {
      report('[bridge] ' + source + ' callback not found: ' + callbackName);
      return false;
    }

    try {
      candidate(payload);
      report('[bridge] ' + source + ' callback delivered: ' + callbackName);
      return true;
    } catch (error) {
      const errorText =
        error && error.stack ? String(error.stack) : preview(error);
      report('[bridge] ' + source + ' callback error ' + callbackName + ' ' + errorText);
      return false;
    }
  };

  const invokeGlobalBridgeCallback = (callbackName, payload, source) => {
    if (typeof callbackName === 'string' && callbackName.trim()) {
      return invokeNamedCallback(callbackName, payload, source);
    }

    return invokeNamedCallback(callbackName, payload, source);
  };

  const buildUserinfoEnvelope = () => {
    const rawUserinfo = runtime.userInfoJson || stringify(runtime.userInfo || {});
    const timestamp = buildTimestamp();
    const signSource =
      'client_id' + clientId +
      'forward_client_id' + cpbs +
      'timestamp' + timestamp +
      'userinfo' + rawUserinfo +
      appSecret;
    return {
      client_id: clientId,
      forward_client_id: cpbs,
      sign: md5(signSource),
      timestamp,
      userinfo: rawUserinfo,
    };
  };

  const parseJsonLikePayload = (value) => {
    if (typeof value === 'string') {
      try {
        return JSON.parse(value);
      } catch (_) {
        return value;
      }
    }
    return value;
  };

  const asCompletionHandler = (value) => {
    if (value && typeof value.complete === 'function') {
      return value;
    }
    return null;
  };

  const completeHandler = (handler, payload) => {
    if (handler && typeof handler.complete === 'function') {
      handler.complete(payload);
    }
  };

  const parseUserinfoObject = (value) => {
    const parsed = parseJsonLikePayload(value);
    if (parsed && typeof parsed === 'object' && !Array.isArray(parsed)) {
      return parsed;
    }
    return {};
  };

  const syncBbgrxxFromUserinfo = (value) => {
    const parsed = parseUserinfoObject(value);
    return syncBbgrxx(parsed);
  };

  const resolveRequestUrl = (url, { prependOrigin = false } = {}) => {
    if (typeof url !== 'string' || !url.trim()) {
      return '';
    }

    const rawUrl = url.trim();
    if (/^https?:\\/\\//i.test(rawUrl)) {
      return rawUrl;
    }

    if (prependOrigin) {
      try {
        return new URL(rawUrl, appUrl).toString();
      } catch (_) {
        return rawUrl;
      }
    }

    if (rawUrl.startsWith('/')) {
      try {
        return new URL(rawUrl, runtime.origin).toString();
      } catch (_) {
        return rawUrl;
      }
    }

    return rawUrl;
  };

  const normalizeRequestPayload = (rawPayload, { prependOrigin = false } = {}) => {
    const payload = parseJsonLikePayload(rawPayload);
    if (!payload || typeof payload !== 'object' || Array.isArray(payload)) {
      throw new Error('入参格式不正确');
    }

    const url = resolveRequestUrl(payload.url, { prependOrigin });
    if (!url) {
      throw new Error('缺少请求地址');
    }

    const method = String(
      payload.method || payload.httpMethod || payload.requestMethod || 'GET',
    ).toUpperCase();
    const headers = {
      ...(payload.headers && typeof payload.headers === 'object' ? payload.headers : {}),
    };
    const bodyCandidate =
      payload.body !== undefined
        ? payload.body
        : payload.data !== undefined
          ? payload.data
          : payload.params !== undefined
            ? payload.params
            : undefined;

    return {
      payload,
      url,
      method,
      headers,
      bodyCandidate,
    };
  };

  const appendQueryParams = (url, params) => {
    try {
      const target = new URL(url);
      Object.entries(params || {}).forEach(([key, value]) => {
        if (value === undefined || value === null) {
          return;
        }
        target.searchParams.set(key, String(value));
      });
      return target.toString();
    } catch (_) {
      return url;
    }
  };

  const buildBridgeResult = (data, errorCode, msg) => {
    return stringify({
      data,
      errorCode,
      msg,
    });
  };

  const buildNestedStringResult = (key, value, msg) => {
    return buildBridgeResult(
      stringify({ [key]: value }),
      0,
      msg,
    );
  };

  const createDsBridgeAsyncMethod = (name, handler) => {
    return (payload, maybeHandler) => {
      report(
        '[dsBridge] ' + name + ' called payload=' + preview(payload),
      );

      const completionHandler = asCompletionHandler(maybeHandler);

      const task = Promise.resolve()
        .then(() => handler(payload))
        .then((result) => {
          const normalized =
            typeof result === 'string' ? result : stringify(result);
          completeHandler(completionHandler, normalized);
          report('[dsBridge] ' + name + ' completed ' + preview(normalized));
          return normalized;
        })
        .catch((error) => {
          const errorText =
            error && error.message ? String(error.message) : preview(error);
          completeHandler(completionHandler, errorText);
          report('[dsBridge] ' + name + ' error ' + errorText);
          throw error;
        });

      return completionHandler ? undefined : task;
    };
  };

  const dsBridgeMethods = {
    request: createDsBridgeAsyncMethod('request', async (rawPayload) => {
      const normalized = normalizeRequestPayload(rawPayload);
      const headers = mergeHeaders(normalized.headers);
      const contentType = String(
        normalized.payload.contentType || headers.get('Content-Type') || 'application/json',
      );
      const init = {
        method: normalized.method,
        headers,
      };
      const isSync = normalized.payload.isSync === true;

      let requestUrl = normalized.url;
      if (
        normalized.method === 'GET' &&
        normalized.bodyCandidate &&
        typeof normalized.bodyCandidate === 'object'
      ) {
        requestUrl = appendQueryParams(requestUrl, normalized.bodyCandidate);
      } else if (normalized.bodyCandidate !== undefined) {
        if (contentType.includes('application/json')) {
          const requestData =
            normalized.bodyCandidate &&
            typeof normalized.bodyCandidate === 'object' &&
            !Array.isArray(normalized.bodyCandidate)
              ? normalized.bodyCandidate.data ?? normalized.bodyCandidate
              : normalized.bodyCandidate;
          init.body =
            typeof requestData === 'string' ? requestData : JSON.stringify(requestData);
          if (!headers.has('Content-Type')) {
            headers.set('Content-Type', 'application/json');
          }
        } else {
          init.body =
            typeof normalized.bodyCandidate === 'string'
              ? normalized.bodyCandidate
              : JSON.stringify(normalized.bodyCandidate);
        }
      }

      if (isSync) {
        report('[dsBridge] request requested sync mode');
      }

      const response = await fetch(requestUrl, init);
      return await response.text();
    }),
    requestBeiBei: createDsBridgeAsyncMethod('requestBeiBei', async (rawPayload) => {
      const normalized = normalizeRequestPayload(rawPayload, { prependOrigin: true });
      return await dsBridgeMethods.request(stringify({
        ...normalized.payload,
        url: normalized.url,
      }));
    }),
    encrypt: createDsBridgeAsyncMethod('encrypt', async (rawPayload) => {
      const payload = parseJsonLikePayload(rawPayload);
      if (!payload || typeof payload !== 'object') {
        return buildBridgeResult('', 1, '入参格式不正确');
      }
      if (!payload.data) {
        return buildBridgeResult('', -1, '请传入要加密的内容');
      }
      return buildNestedStringResult('result', payload.data, '加密数据成功');
    }),
    decrypt: createDsBridgeAsyncMethod('decrypt', async (rawPayload) => {
      const payload = parseJsonLikePayload(rawPayload);
      if (!payload || typeof payload !== 'object') {
        return buildBridgeResult('', 1, '入参格式不正确');
      }
      return buildNestedStringResult('result', payload.data ?? '', '解密数据成功');
    }),
    getDepts: createDsBridgeAsyncMethod('getDepts', async () => {
      return buildNestedStringResult('depts', 0, '获取步数成功');
    }),
  };

  const deliverLocationPayload = (source) => {
    const payload = locationPayload;
    report('[bridge] ' + source + ' delivering locationResult ' + preview(payload));

    window.setTimeout(() => {
      try {
        if (typeof locationResult === 'function') {
          locationResult(payload);
          return;
        }

        if (typeof window.locationResult === 'function') {
          window.locationResult(payload);
          return;
        }

        if (window.bbgrxx && typeof window.bbgrxx === 'object') {
          window.bbgrxx.locationMsg = payload;
          window.bbgrxx.updatingLocationMsg = payload;
          report('[bridge] location payload applied via window.bbgrxx fallback');
          return;
        }

        window.__bbtotalLastLocationResult = payload;
        report('[bridge] location payload stored at window.__bbtotalLastLocationResult fallback');
      } catch (error) {
        const errorText =
          error && error.stack ? String(error.stack) : preview(error);
        report('[bridge] locationResult delivery error ' + errorText);
      }
    }, 0);
  };

  const deliverWifiPayload = (source) => {
    const payload = wifiPayload;
    report('[bridge] ' + source + ' delivering wifiInfoResult ' + preview(payload));

    window.setTimeout(() => {
      try {
        if (typeof wifiInfoResult === 'function') {
          wifiInfoResult(payload);
          return;
        }

        if (typeof window.wifiInfoResult === 'function') {
          window.wifiInfoResult(payload);
          return;
        }

        window.__bbtotalLastWifiInfoResult = payload;
        report('[bridge] wifi payload stored at window.__bbtotalLastWifiInfoResult fallback');
      } catch (error) {
        const errorText =
          error && error.stack ? String(error.stack) : preview(error);
        report('[bridge] wifiInfoResult delivery error ' + errorText);
      }
    }, 0);
  };

  const createSyncBridgeMethod = (name, producer) => {
    return (...args) => {
      report(
        '[bridge] ' + name + ' called args=' +
          args.map((item) => preview(item)).join(' | '),
      );
      try {
        const result = producer(...args);
        report('[bridge] ' + name + ' returned ' + preview(result));
        return result;
      } catch (error) {
        const errorText =
          error && error.stack ? String(error.stack) : preview(error);
        report('[bridge] ' + name + ' error ' + errorText);
        throw error;
      }
    };
  };

  const createAsyncBridgeMethod = (name, action) => {
    return (...args) => {
      report(
        '[bridge] ' + name + ' called args=' +
          args.map((item) => preview(item)).join(' | '),
      );
      try {
        const result = action(...args);
        report('[bridge] ' + name + ' completed');
        return result;
      } catch (error) {
        const errorText =
          error && error.stack ? String(error.stack) : preview(error);
        report('[bridge] ' + name + ' error ' + errorText);
        throw error;
      }
    };
  };

  const bridge = {
    getToken: createSyncBridgeMethod('getToken', () => {
      return runtime.bridgeContext.loginToken || '';
    }),
    getLoginToken: createSyncBridgeMethod('getLoginToken', (callbackName) => {
      const token = runtime.bridgeContext.loginToken || '';
      if (typeof callbackName === 'string' && callbackName.trim()) {
        invokeGlobalBridgeCallback(callbackName, token, 'getLoginToken');
        return undefined;
      }
      return token;
    }),
    getBjys: createSyncBridgeMethod('getBjys', (callbackName) => {
      const mode = 'bottom';
      if (typeof callbackName === 'string' && callbackName.trim()) {
        invokeGlobalBridgeCallback(callbackName, mode, 'getBjys');
        return undefined;
      }
      return mode;
    }),
    getUserinfo: createSyncBridgeMethod('getUserinfo', (callbackName) => {
      const envelope = buildUserinfoEnvelope();
      syncBbgrxxFromUserinfo(envelope.userinfo);
      if (typeof callbackName === 'string' && callbackName.trim()) {
        invokeGlobalBridgeCallback(callbackName, envelope, 'getUserinfo');
        return undefined;
      }
      return runtime.userInfoJson || stringify(runtime.userInfo || {});
    }),
    getUserInfo: createSyncBridgeMethod('getUserInfo', () => {
      return runtime.userInfoJson || stringify(runtime.userInfo || {});
    }),
    getLocation: createAsyncBridgeMethod('getLocation', () => {
      deliverLocationPayload('getLocation');
    }),
    getUpdatingLocation: createAsyncBridgeMethod('getUpdatingLocation', () => {
      deliverLocationPayload('getUpdatingLocation');
    }),
    getWifiinfo: createSyncBridgeMethod('getWifiinfo', () => {
      deliverWifiPayload('getWifiinfo');
      return stringify(runtime.wifiInfo || {});
    }),
    postMessage: createAsyncBridgeMethod('postMessage', (message) => {
      callHandler('nativePostMessage', String(message || ''));
      return '';
    }),
    outApp: createAsyncBridgeMethod('outApp', () => {
      callHandler('nativeClosePage');
      return '';
    }),
    logintokeninvalid: createAsyncBridgeMethod('logintokeninvalid', () => {
      report('logintokeninvalid requested');
      return '';
    }),
    stopPush: createAsyncBridgeMethod('stopPush', () => ''),
    startPush: createAsyncBridgeMethod('startPush', () => ''),
    refreshNavicationCount: createAsyncBridgeMethod('refreshNavicationCount', () => ''),
    onlyrefreshBottomCount: createAsyncBridgeMethod('onlyrefreshBottomCount', () => ''),
    clearWebCache: createAsyncBridgeMethod('clearWebCache', () => ''),
    cleanWebCache: createAsyncBridgeMethod('cleanWebCache', () => ''),
    hideSharebar: createAsyncBridgeMethod('hideSharebar', () => ''),
    refushHome: createAsyncBridgeMethod('refushHome', () => ''),
    refushMine: createAsyncBridgeMethod('refushMine', () => ''),
    returnHome: createAsyncBridgeMethod('returnHome', () => ''),
    finishForResult: createAsyncBridgeMethod('finishForResult', () => ''),
    openUrl: createAsyncBridgeMethod('openUrl', (url) => {
      callHandler('nativeOpenUrl', String(url || ''));
      return '';
    }),
    closePage: createAsyncBridgeMethod('closePage', () => {
      callHandler('nativeClosePage');
      return '';
    }),
    hiddenTitle: createAsyncBridgeMethod('hiddenTitle', () => {
      report('hiddenTitle requested');
      return '';
    }),
    HtmlJc: createAsyncBridgeMethod('HtmlJc', (message) => {
      callHandler('nativeLog', 'HtmlJc:' + String(message || ''));
      return '';
    }),
    Scan: createAsyncBridgeMethod('Scan', () => {
      report('Scan requested');
      return '';
    }),
    startQrCode: createAsyncBridgeMethod('startQrCode', () => {
      report('startQrCode requested');
      return '';
    }),
    callPhone: createAsyncBridgeMethod('callPhone', (message) => {
      callHandler('nativeOpenUrl', 'tel:' + String(message || ''));
      return '';
    }),
    applicationBecomeActive: createAsyncBridgeMethod('applicationBecomeActive', () => ''),
    changeScreenbrightness: createAsyncBridgeMethod('changeScreenbrightness', () => ''),
    request: createAsyncBridgeMethod('request', (payload, handler) => {
      return dsBridgeMethods.request(payload, handler);
    }),
    requestBeiBei: createAsyncBridgeMethod('requestBeiBei', (payload, handler) => {
      return dsBridgeMethods.requestBeiBei(payload, handler);
    }),
    encrypt: createAsyncBridgeMethod('encrypt', (payload, handler) => {
      return dsBridgeMethods.encrypt(payload, handler);
    }),
    decrypt: createAsyncBridgeMethod('decrypt', (payload, handler) => {
      return dsBridgeMethods.decrypt(payload, handler);
    }),
    getDepts: createAsyncBridgeMethod('getDepts', (payload, handler) => {
      return dsBridgeMethods.getDepts(payload, handler);
    }),
  };

  window.__bbtotalRuntime = runtime;
  window.__bbtotalApplySession = () => {
    applyStorage(window.localStorage, runtime.storageSeed);
    applyStorage(window.sessionStorage, runtime.storageSeed);
    syncBbgrxx(runtime.bbgrxx || {});
    syncBbgrxxFromUserinfo(runtime.userInfoJson || runtime.userInfo || {});
    return runtime;
  };

  window.__bbtotalSyncBbgrxx = syncBbgrxx;
  window.__bbtotalSyncBbgrxxFromUserinfo = syncBbgrxxFromUserinfo;

  const installMobileUtilsPatch = () => {
    const utils = window.ptPublicMethodMobileUtils;
    if (!utils || typeof utils !== 'object') {
      return false;
    }
    if (utils.__bbtotalGetUserMsgMobilePatched) {
      return true;
    }
    if (typeof utils.getUserMsgMobile !== 'function') {
      return false;
    }

    const original = utils.getUserMsgMobile.bind(utils);
    utils.getUserMsgMobile = async function (...args) {
      const result = await original(...args);
      try {
        const originalUserinfo =
          result && typeof result === 'object' ? result.userinfo : undefined;
        const mergedUserinfo = syncBbgrxxFromUserinfo(originalUserinfo);
        if (result && typeof result === 'object' && !Array.isArray(result)) {
          if (result.userinfoRaw === undefined) {
            result.userinfoRaw = originalUserinfo;
          }
          result.userinfo = mergedUserinfo;
        }
      } catch (error) {
        report('[bridge] getUserMsgMobile normalize error ' + preview(error));
      }
      return result;
    };
    utils.__bbtotalGetUserMsgMobilePatched = true;
    report('[bridge] patched ptPublicMethodMobileUtils.getUserMsgMobile');
    return true;
  };

  let mobileUtilsPatchAttempts = 0;
  let lastPatchedMobileUtils = null;
  installMobileUtilsPatch();
  const mobileUtilsPatchTimer = window.setInterval(() => {
    mobileUtilsPatchAttempts += 1;
    const currentMobileUtils = window.ptPublicMethodMobileUtils;
    if (
      currentMobileUtils &&
      currentMobileUtils !== lastPatchedMobileUtils &&
      installMobileUtilsPatch()
    ) {
      lastPatchedMobileUtils = currentMobileUtils;
    } else if (
      currentMobileUtils &&
      currentMobileUtils.__bbtotalGetUserMsgMobilePatched
    ) {
      lastPatchedMobileUtils = currentMobileUtils;
    } else {
      installMobileUtilsPatch();
    }
    if (mobileUtilsPatchAttempts >= 120) {
      window.clearInterval(mobileUtilsPatchTimer);
    }
  }, 250);

  window.BBTotalHybrid = bridge;
  window.SYAppModel = Object.assign({}, window.SYAppModel || {}, bridge);
  window.android = window.SYAppModel;
  window.Android = window.SYAppModel;
  window.appModel = window.SYAppModel;
  window.AppModel = window.SYAppModel;
  window.getToken = bridge.getToken;
  window.getLoginToken = bridge.getLoginToken;
  window.getUserinfo = bridge.getUserinfo;
  window.getUserInfo = bridge.getUserInfo;

  window.webkit = window.webkit || {};
  window.webkit.messageHandlers = window.webkit.messageHandlers || {};
  window.webkit.messageHandlers.SYAppModel = {
    postMessage(message) {
      const payload =
        typeof message === 'string' ? message : stringify(message || {});
      let type = '';
      try {
        const parsed = typeof message === 'string' ? JSON.parse(message) : message;
        type = parsed && typeof parsed === 'object' ? String(parsed.type || '') : '';
      } catch (_) {}

      if (type === 'getLocation' || type === 'getUpdatingLocation') {
        report('[bridge] webkit postMessage type=' + type);
        deliverLocationPayload('webkit:' + type);
      }

      callHandler('nativePostMessage', payload);
    },
  };

  const authHeaders = runtime.authHeaders || {};
  const shouldPatchUrl = (url) => {
    if (typeof url !== 'string') {
      return false;
    }
    return url.startsWith('/') || (runtime.host && url.includes(runtime.host));
  };

  const mergeHeaders = (source) => {
    const headers = new Headers(source || {});
    Object.entries(authHeaders).forEach(([key, value]) => {
      if (!value || headers.has(key)) {
        return;
      }
      headers.set(key, value);
    });
    return headers;
  };

  const installDsBridgeShim = (target) => {
    if (!target || typeof target !== 'object') {
      return target;
    }

    const originalCall =
      typeof target.call === 'function' ? target.call.bind(target) : null;

    target.call = (methodName, payload, callback) => {
      const method = String(methodName || '');
      if (dsBridgeMethods[method]) {
        if (typeof callback === 'function') {
          return dsBridgeMethods[method](payload, {
            complete(value) {
              callback(value);
            },
          });
        }
        return dsBridgeMethods[method](payload);
      }

      report('[dsBridge] unknown method ' + method);
      return originalCall ? originalCall(methodName, payload, callback) : Promise.resolve('');
    };

    target.hasNativeMethod = (methodName) => {
      return !!dsBridgeMethods[String(methodName || '')];
    };

    target.register = target.register || ((name, fn) => {
      if (typeof name === 'string' && typeof fn === 'function') {
        target[name] = fn;
      }
    });

    target.registerAsyn = target.registerAsyn || target.register;
    return target;
  };

  window.request = dsBridgeMethods.request;
  window.requestBeiBei = dsBridgeMethods.requestBeiBei;
  window.encrypt = dsBridgeMethods.encrypt;
  window.decrypt = dsBridgeMethods.decrypt;
  window.getDepts = dsBridgeMethods.getDepts;
  window.dsBridge = installDsBridgeShim(window.dsBridge || {});

  const originalFetch = window.fetch ? window.fetch.bind(window) : null;
  if (originalFetch) {
    window.fetch = async (input, init = {}) => {
      const requestUrl =
        typeof input === 'string'
          ? input
          : (input && typeof input === 'object' && 'url' in input ? input.url : '');
      if (!shouldPatchUrl(requestUrl)) {
        return originalFetch(input, init);
      }

      report('[network] fetch -> ' + requestUrl);
      const sourceHeaders =
        init.headers ||
        (input && typeof input === 'object' && 'headers' in input
          ? input.headers
          : undefined);
      try {
        const response = await originalFetch(input, {
          ...init,
          headers: mergeHeaders(sourceHeaders),
        });
        report(
          '[network] fetch <- ' + requestUrl + ' status=' + String(response.status),
        );
        return response;
      } catch (error) {
        report(
          '[network] fetch !! ' + requestUrl + ' error=' +
            (error && error.message ? error.message : preview(error)),
        );
        throw error;
      }
    };
  }

  const xhrPrototype = window.XMLHttpRequest && window.XMLHttpRequest.prototype;
  if (xhrPrototype && !xhrPrototype.__bbtotalHybridPatched) {
    xhrPrototype.__bbtotalHybridPatched = true;

    const originalOpen = xhrPrototype.open;
    const originalSend = xhrPrototype.send;
    const originalSetRequestHeader = xhrPrototype.setRequestHeader;

    xhrPrototype.setRequestHeader = function(name, value) {
      this.__bbtotalHeaders = this.__bbtotalHeaders || {};
      this.__bbtotalHeaders[name.toLowerCase()] = value;
      return originalSetRequestHeader.call(this, name, value);
    };

    xhrPrototype.open = function(method, url, ...rest) {
      this.__bbtotalUrl = url;
      this.__bbtotalMethod = method;
      this.__bbtotalHeaders = this.__bbtotalHeaders || {};
      return originalOpen.call(this, method, url, ...rest);
    };

    xhrPrototype.send = function(body) {
      const requestUrl = String(this.__bbtotalUrl || '');
      if (shouldPatchUrl(requestUrl)) {
        report(
          '[network] xhr -> ' +
            String(this.__bbtotalMethod || 'GET') + ' ' + requestUrl,
        );
      }
      if (shouldPatchUrl(requestUrl)) {
        Object.entries(authHeaders).forEach(([key, value]) => {
          if (!value || this.__bbtotalHeaders[key.toLowerCase()] === value) {
            return;
          }
          this.__bbtotalHeaders[key.toLowerCase()] = value;
          try {
            originalSetRequestHeader.call(this, key, value);
          } catch (_) {}
        });
        this.addEventListener('loadend', () => {
          report(
            '[network] xhr <- ' +
              String(this.__bbtotalMethod || 'GET') + ' ' + requestUrl +
              ' status=' + String(this.status),
          );
        }, { once: true });
      }
      return originalSend.call(this, body);
    };
  }

  report('document-start hybrid bridge installed');
  window.__bbtotalHybridInstalled = true;
})();
''';
  }
}
