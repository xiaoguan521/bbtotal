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

  applyStorage(window.localStorage, runtime.storageSeed);
  applyStorage(window.sessionStorage, runtime.storageSeed);

  const bridge = {
    getToken() {
      return runtime.bridgeContext.loginToken || '';
    },
    getUserinfo() {
      return runtime.userInfoJson || stringify(runtime.userInfo || {});
    },
    getUserInfo() {
      return runtime.userInfoJson || stringify(runtime.userInfo || {});
    },
    getLocation() {
      return stringify(runtime.locationPayload || {});
    },
    getUpdatingLocation() {
      return stringify(runtime.locationPayload || {});
    },
    getWifiinfo() {
      return stringify(runtime.wifiInfo || {});
    },
    postMessage(message) {
      callHandler('nativePostMessage', String(message || ''));
      return '';
    },
    openUrl(url) {
      callHandler('nativeOpenUrl', String(url || ''));
      return '';
    },
    closePage() {
      callHandler('nativeClosePage');
      return '';
    },
    hiddenTitle() {
      callHandler('nativeLog', 'hiddenTitle requested');
      return '';
    },
  };

  window.__bbtotalRuntime = runtime;
  window.__bbtotalApplySession = () => {
    applyStorage(window.localStorage, runtime.storageSeed);
    applyStorage(window.sessionStorage, runtime.storageSeed);
    return runtime;
  };

  window.BBTotalHybrid = bridge;
  window.SYAppModel = Object.assign({}, window.SYAppModel || {}, bridge);
  window.android = window.SYAppModel;
  window.Android = window.SYAppModel;
  window.appModel = window.SYAppModel;
  window.AppModel = window.SYAppModel;
  window.getToken = bridge.getToken;
  window.getUserinfo = bridge.getUserinfo;
  window.getUserInfo = bridge.getUserInfo;

  window.webkit = window.webkit || {};
  window.webkit.messageHandlers = window.webkit.messageHandlers || {};
  window.webkit.messageHandlers.SYAppModel = {
    postMessage(message) {
      const payload =
        typeof message === 'string' ? message : stringify(message || {});
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

  const originalFetch = window.fetch ? window.fetch.bind(window) : null;
  if (originalFetch) {
    window.fetch = (input, init = {}) => {
      const requestUrl =
        typeof input === 'string'
          ? input
          : (input && typeof input === 'object' && 'url' in input ? input.url : '');
      if (!shouldPatchUrl(requestUrl)) {
        return originalFetch(input, init);
      }

      const sourceHeaders =
        init.headers ||
        (input && typeof input === 'object' && 'headers' in input
          ? input.headers
          : undefined);
      return originalFetch(input, {
        ...init,
        headers: mergeHeaders(sourceHeaders),
      });
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
      this.__bbtotalHeaders = this.__bbtotalHeaders || {};
      return originalOpen.call(this, method, url, ...rest);
    };

    xhrPrototype.send = function(body) {
      const requestUrl = String(this.__bbtotalUrl || '');
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
      }
      return originalSend.call(this, body);
    };
  }

  callHandler('nativeLog', 'document-start hybrid bridge installed');
  window.__bbtotalHybridInstalled = true;
})();
''';
  }
}
