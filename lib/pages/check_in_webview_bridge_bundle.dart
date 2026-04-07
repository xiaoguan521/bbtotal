import 'dart:convert';

import 'package:flutter/foundation.dart';

import '../models/check_in_location_preset.dart';

class CheckInWebViewBridgeBundle {
  CheckInWebViewBridgeBundle._({
    required this.allowedOriginRule,
    required this.androidStartupScript,
    required this.bridgeContext,
    required this.bridgeContextJson,
    required this.iosStartupScript,
    required this.locationPayload,
    required this.locationPayloadJson,
    required this.resolvedUri,
    required this.userinfoResult,
    required this.userinfoResultJson,
    required this.wifiInfo,
    required this.wifiInfoJson,
  });

  factory CheckInWebViewBridgeBundle.fromPreset(CheckInLocationPreset preset) {
    final Uri resolvedUri = preset.buildCheckInUri(
      CheckInWebViewPageBridgeDefaults.inspectedCheckInUrl,
    );
    final Map<String, dynamic> locationPayload =
        _normalizeMap(preset.toBridgePayload());
    final Map<String, dynamic> bridgeContext = <String, dynamic>{
      'username': preset.loginInfo.username,
      'userid': preset.loginInfo.userId,
      'grbh': preset.loginInfo.grbh,
      'zjhm': preset.loginInfo.idCard,
      'loginToken': preset.loginInfo.loginToken,
      'blqd': preset.loginInfo.blqd,
      'jgbh': preset.loginInfo.jgbh,
      'jgbm': preset.loginInfo.jgbm,
      'zxbm': preset.loginInfo.zxbm,
      'qycode': preset.loginInfo.qycode,
      'zzjgdmz': preset.loginInfo.zzjgdmz,
      'ticket': preset.ticket,
      'cheque': preset.cheque,
      'account': preset.loginInfo.account,
    };
    final Map<String, dynamic> userinfoResult =
        _buildUserinfoResult(preset, bridgeContext);
    final Map<String, dynamic> wifiInfo = <String, dynamic>{
      'ssid': '',
      'bssid': '',
      'wifiName': '',
      'wifiMac': '',
      'isWifi': false,
      'device': 'flutter-native-webview',
    };

    final String locationPayloadJson = jsonEncode(locationPayload);
    final String bridgeContextJson = jsonEncode(bridgeContext);
    final String userinfoResultJson = jsonEncode(userinfoResult);
    final String wifiInfoJson = jsonEncode(wifiInfo);

    return CheckInWebViewBridgeBundle._(
      allowedOriginRule: _buildAllowedOriginRule(resolvedUri),
      androidStartupScript: _buildAndroidStartupScript(
        bridgeContextJson: bridgeContextJson,
        locationPayloadJson: locationPayloadJson,
        userinfoResultJson: userinfoResultJson,
        wifiInfoJson: wifiInfoJson,
      ),
      bridgeContext: bridgeContext,
      bridgeContextJson: bridgeContextJson,
      iosStartupScript: _buildIosStartupScript(
        bridgeContextJson: bridgeContextJson,
        locationPayloadJson: locationPayloadJson,
        userinfoResultJson: userinfoResultJson,
        wifiInfoJson: wifiInfoJson,
      ),
      locationPayload: locationPayload,
      locationPayloadJson: locationPayloadJson,
      resolvedUri: resolvedUri,
      userinfoResult: userinfoResult,
      userinfoResultJson: userinfoResultJson,
      wifiInfo: wifiInfo,
      wifiInfoJson: wifiInfoJson,
    );
  }

  final String allowedOriginRule;
  final String androidStartupScript;
  final Map<String, dynamic> bridgeContext;
  final String bridgeContextJson;
  final String iosStartupScript;
  final Map<String, dynamic> locationPayload;
  final String locationPayloadJson;
  final Uri resolvedUri;
  final Map<String, dynamic> userinfoResult;
  final String userinfoResultJson;
  final Map<String, dynamic> wifiInfo;
  final String wifiInfoJson;

  Map<String, dynamic> creationParamsFor(TargetPlatform platform) {
    final bool isAndroid = platform == TargetPlatform.android;
    return <String, dynamic>{
      'allowedOriginRule': allowedOriginRule,
      'bridgeContextJson': bridgeContextJson,
      'locationPayloadJson': locationPayloadJson,
      'startupScript': isAndroid ? androidStartupScript : iosStartupScript,
      'url': resolvedUri.toString(),
      'userinfoResultJson': userinfoResultJson,
      'wifiInfoJson': wifiInfoJson,
    };
  }

  String get flutterRuntimeScript {
    return _buildBaseStartupScript(
      bridgeContextJson: bridgeContextJson,
      bridgeInstallScript: '''
  postDebug = (message) => {
    try {
      window.bbtotalDebug.postMessage(String(message));
    } catch (_) {}
  };

  dispatchNativeCommand = (command) => {
    const raw =
      typeof command === 'string' ? command : JSON.stringify(command || {});
    try {
      window.bbtotalBridgeControl.postMessage(raw);
    } catch (_) {}
    return '';
  };

  resolveLocationResult = () => JSON.stringify(payload);
  resolveUpdatingLocationResult = () => JSON.stringify(payload);
  resolveUserinfoResult = () => JSON.stringify(userinfoResult);
  resolveWifiInfoResult = () => JSON.stringify(wifiInfo);
''',
      locationPayloadJson: locationPayloadJson,
      userinfoResultJson: userinfoResultJson,
      wifiInfoJson: wifiInfoJson,
    );
  }

  static String _buildAllowedOriginRule(Uri uri) {
    final String portSuffix = uri.hasPort ? ':${uri.port}' : '';
    return '${uri.scheme}://${uri.host}$portSuffix';
  }

  static Map<String, dynamic> _buildUserinfoResult(
    CheckInLocationPreset preset,
    Map<String, dynamic> bridgeContext,
  ) {
    final Map<String, dynamic> rawUserinfo = _parseJsonMap(
      preset.loginInfo.rawUserInfoJson,
    );
    final Map<String, dynamic> rawZzjgxx = _parseJsonMap(
      preset.loginInfo.rawZzjgxxJson,
    );
    final Map<String, dynamic> payload = _normalizeMap(preset.toBridgePayload());

    final Map<String, dynamic> result = <String, dynamic>{
      ...rawUserinfo,
      'gztMoudleMsg': rawUserinfo['gztMoudleMsg'] ??
          <String, dynamic>{
            'ryly': 'xgzt',
            'flag': true,
            'xmmc': null,
            'classifyNumber': '03001',
            'formUrl':
                '/dxgl-app/dxslgl/#/dxslglComp/dxslglfq/index?bm=03060&dx_29_sjdxsl=${preset.loginInfo.userId}',
            'matterClassNumber': '03060:task',
            'xmid': null,
            'dx_29_sjdxsl': preset.loginInfo.userId.toString(),
            'dx_29_dxmc': preset.loginInfo.username,
            'classifyName': null,
            'matterClassName': '打卡',
          },
      'ptdlzh': _firstNonEmpty(rawUserinfo['ptdlzh'], preset.loginInfo.account),
      'client': _firstNonEmpty(
        rawUserinfo['client'],
        preset.loginInfo.client,
        '4',
      ),
      'username': _firstNonEmpty(
        bridgeContext['username'],
        rawUserinfo['username'],
        rawUserinfo['xingming'],
      ),
      'xingming': _firstNonEmpty(
        bridgeContext['username'],
        rawUserinfo['xingming'],
        rawUserinfo['username'],
      ),
      'userid': _firstNonEmpty(
        bridgeContext['userid'],
        rawUserinfo['userid'],
      ),
      'grbh': _firstNonEmpty(bridgeContext['grbh'], rawUserinfo['grbh']),
      'zjhm': _firstNonEmpty(bridgeContext['zjhm'], rawUserinfo['zjhm']),
      'loginToken': _firstNonEmpty(
        bridgeContext['loginToken'],
        rawUserinfo['loginToken'],
      ),
      'tyLoginToken': _firstNonEmpty(
        bridgeContext['loginToken'],
        rawUserinfo['tyLoginToken'],
      ),
      'blqd': _firstNonEmpty(
        bridgeContext['blqd'],
        rawUserinfo['blqd'],
        'app_02',
      ),
      'jgbh': _firstNonEmpty(bridgeContext['jgbh'], rawUserinfo['jgbh']),
      'jgbm': _firstNonEmpty(bridgeContext['jgbm'], rawUserinfo['jgbm']),
      'zxbm': _firstNonEmpty(bridgeContext['zxbm'], rawUserinfo['zxbm']),
      'qycode': _firstNonEmpty(
        bridgeContext['qycode'],
        rawUserinfo['qycode'],
      ),
      'zzjgdmz': _firstNonEmpty(
        bridgeContext['zzjgdmz'],
        rawUserinfo['zzjgdmz'],
      ),
      'cheque': _firstNonEmpty(
        bridgeContext['cheque'],
        rawUserinfo['cheque'],
      ),
      'ticket': _firstNonEmpty(
        bridgeContext['ticket'],
        rawUserinfo['ticket'],
      ),
      'bmbh': _firstNonEmpty(rawUserinfo['bmbh'], bridgeContext['jgbm']),
      'f_dept_id': _firstNonEmpty(
        rawUserinfo['f_dept_id'],
        bridgeContext['jgbm'],
      ),
      'deptid': _firstNonEmpty(rawUserinfo['deptid'], bridgeContext['jgbm']),
      'locationMsg': rawUserinfo['locationMsg'] ?? payload,
      'updatingLocationMsg': rawUserinfo['updatingLocationMsg'] ?? payload,
      'notificationRuleParams':
          rawUserinfo['notificationRuleParams'] ?? <String, dynamic>{},
      'loadState': rawUserinfo['loadState'] ?? true,
    };

    final Map<String, dynamic> zzjgxx = rawZzjgxx.isNotEmpty
        ? rawZzjgxx
        : _normalizeMap(result['zzjgxx']);
    final Map<String, dynamic> results = _normalizeMap(zzjgxx['results'])
      ..putIfAbsent('userData', () => <String, dynamic>{});
    final Map<String, dynamic> userData = _normalizeMap(results['userData']);
    final Map<String, dynamic> gjmsg = _normalizeMap(results['gjmsg']);
    final Map<String, dynamic> zxjgInfo = _normalizeMap(results['zxjgInfo']);

    userData['name'] = _firstNonEmpty(userData['name'], result['username']);
    userData['personId'] = _firstNonEmpty(userData['personId'], result['userid']);
    userData['personalNo'] = _firstNonEmpty(
      userData['personalNo'],
      result['grbh'],
    );
    userData['zbmbh'] = _firstNonEmpty(userData['zbmbh'], result['jgbm']);

    gjmsg['jgbm'] = _firstNonEmpty(gjmsg['jgbm'], result['jgbm']);
    gjmsg['jgbh'] = _firstNonEmpty(gjmsg['jgbh'], result['jgbh']);
    gjmsg['zjbzxbm'] = _firstNonEmpty(
      gjmsg['zjbzxbm'],
      result['zzjgdmz'],
      result['qycode'],
    );

    zxjgInfo['jgbh'] = _firstNonEmpty(zxjgInfo['jgbh'], result['jgbh']);

    results['userData'] = userData;
    results['gjmsg'] = gjmsg;
    results['zxjgInfo'] = zxjgInfo;
    zzjgxx['results'] = results;
    result['zzjgxx'] = zzjgxx;

    return result;
  }

  static Map<String, dynamic> _parseJsonMap(String raw) {
    if (raw.trim().isEmpty) {
      return <String, dynamic>{};
    }

    try {
      return _normalizeMap(jsonDecode(raw));
    } catch (_) {
      return <String, dynamic>{};
    }
  }

  static Map<String, dynamic> _normalizeMap(Object? raw) {
    if (raw is Map<String, dynamic>) {
      return raw.map<String, dynamic>(
        (String key, dynamic value) => MapEntry(key, _normalizeValue(value)),
      );
    }
    if (raw is Map) {
      return raw.map<String, dynamic>(
        (dynamic key, dynamic value) =>
            MapEntry(key.toString(), _normalizeValue(value)),
      );
    }
    return <String, dynamic>{};
  }

  static dynamic _normalizeValue(dynamic value) {
    if (value is Map) {
      return _normalizeMap(value);
    }
    if (value is List) {
      return value.map<dynamic>(_normalizeValue).toList();
    }
    return value;
  }

  static dynamic _firstNonEmpty(Object? first, Object? second, [Object? third]) {
    for (final Object? candidate in <Object?>[first, second, third]) {
      if (candidate == null) {
        continue;
      }
      if (candidate is String && candidate.trim().isEmpty) {
        continue;
      }
      return candidate;
    }
    return '';
  }

  static String _buildBaseStartupScript({
    required String bridgeContextJson,
    required String bridgeInstallScript,
    required String locationPayloadJson,
    required String userinfoResultJson,
    required String wifiInfoJson,
  }) {
    return '''
(() => {
  if (window.__bbtotalBridgeInstalled) {
    return;
  }

  const payload = $locationPayloadJson;
  const userinfoResult = $userinfoResultJson;
  const wifiInfo = $wifiInfoJson;
  const bridgeContext = $bridgeContextJson;
  const trackedFrames = new WeakSet();
  const vConsoleSources = [
    'https://unpkg.com/vconsole@latest/dist/vconsole.min.js',
    'https://cdn.jsdelivr.net/npm/vconsole@latest/dist/vconsole.min.js',
  ];

  let postDebug = (_) => {};
  let dispatchNativeCommand = () => '';
  let resolveLocationResult = () => JSON.stringify(payload);
  let resolveUpdatingLocationResult = () => JSON.stringify(payload);
  let resolveUserinfoResult = () => JSON.stringify(userinfoResult);
  let resolveWifiInfoResult = () => JSON.stringify(wifiInfo);

  const stringifyForLog = (value) => {
    if (value == null) {
      return 'null';
    }
    if (typeof value === 'string') {
      return value;
    }
    try {
      return JSON.stringify(value);
    } catch (_) {
      return String(value);
    }
  };

  const deepClone = (value) => {
    try {
      return JSON.parse(JSON.stringify(value));
    } catch (_) {
      return value;
    }
  };

  const frameLabel = (targetWindow, fallbackLabel) => {
    if (targetWindow === window) {
      return 'main';
    }
    try {
      const href = targetWindow.location && targetWindow.location.href;
      if (href) {
        return fallbackLabel + ':' + href;
      }
    } catch (_) {}
    return fallbackLabel;
  };

  const notifyUserinfo = (targetWindow = window, contextValue = userinfoResult) => {
    if (typeof targetWindow.getUserinfoResult === 'function') {
      targetWindow.getUserinfoResult(JSON.stringify(contextValue));
    }
    if (typeof targetWindow.getUserInfoResult === 'function') {
      targetWindow.getUserInfoResult(JSON.stringify(contextValue));
    }
  };

  const notifyWifiinfo = (targetWindow = window) => {
    if (typeof targetWindow.getWifiinfoResult === 'function') {
      targetWindow.getWifiinfoResult(JSON.stringify(wifiInfo));
    }
  };

  const applyPayload = (targetWindow = window) => {
    const targetDocument = targetWindow.document;
    const nextBbgrxx = ensureUserContext(targetWindow);
    nextBbgrxx.locationMsg = payload;
    nextBbgrxx.updatingLocationMsg = payload;
    targetWindow.bbgrxx = nextBbgrxx;

    const nextSe =
      targetWindow.Se && typeof targetWindow.Se === 'object'
        ? targetWindow.Se
        : {};
    nextSe.locationMsg = payload;
    targetWindow.Se = nextSe;

    if (targetDocument && typeof targetDocument.querySelector === 'function') {
      const textarea = targetDocument.querySelector('#textarea-dx_29_dkwz');
      if (textarea) {
        textarea.value = payload.data.address || '';
        textarea.innerText = payload.data.address || '';
        ['input', 'change', 'blur'].forEach((eventName) => {
          textarea.dispatchEvent(
            new targetWindow.Event(eventName, { bubbles: true }),
          );
        });
      }
    }

    if (typeof targetWindow.locationResult === 'function') {
      targetWindow.locationResult(payload);
    } else if (
      targetWindow.onBasicFormRef &&
      typeof targetWindow.onBasicFormRef.updateGhsxLoaction === 'function'
    ) {
      targetWindow.onBasicFormRef.updateGhsxLoaction();
    }

    return JSON.stringify(payload);
  };

  const mergeUserContextObject = (currentValue) => {
    const current =
      currentValue && typeof currentValue === 'object' ? currentValue : {};
    const base = deepClone(userinfoResult) || {};
    const next = {
      ...base,
      ...current,
    };

    const baseZzjgxx =
      base.zzjgxx && typeof base.zzjgxx === 'object' ? base.zzjgxx : {};
    const nextZzjgxx =
      next.zzjgxx && typeof next.zzjgxx === 'object' ? next.zzjgxx : {};
    const baseResults =
      baseZzjgxx.results && typeof baseZzjgxx.results === 'object'
        ? baseZzjgxx.results
        : {};
    const nextResults =
      nextZzjgxx.results && typeof nextZzjgxx.results === 'object'
        ? nextZzjgxx.results
        : {};

    next.zzjgxx = {
      ...baseZzjgxx,
      ...nextZzjgxx,
      results: {
        ...baseResults,
        ...nextResults,
        userData: {
          ...(baseResults.userData || {}),
          ...(nextResults.userData || {}),
        },
        gjmsg: {
          ...(baseResults.gjmsg || {}),
          ...(nextResults.gjmsg || {}),
        },
        zxjgInfo: {
          ...(baseResults.zxjgInfo || {}),
          ...(nextResults.zxjgInfo || {}),
        },
      },
    };

    next.locationMsg = payload;
    next.updatingLocationMsg = payload;
    next.notificationRuleParams =
      next.notificationRuleParams && typeof next.notificationRuleParams === 'object'
        ? next.notificationRuleParams
        : {};
    next.loadState = next.loadState ?? true;

    return next;
  };

  const ensureUserContext = (targetWindow, options = {}) => {
    const label = options.label || frameLabel(targetWindow, 'main');
    const next = mergeUserContextObject(targetWindow.bbgrxx);

    if (typeof targetWindow.__bbtotalSyncUserContext === 'function') {
      const synced = targetWindow.__bbtotalSyncUserContext(
        next,
        'ensureUserContext',
      );
      if (!targetWindow.__bbtotalUserContextLogged) {
        targetWindow.__bbtotalUserContextLogged = true;
        postDebug(
          '[' + label + '] user context ready zzjgxx.results=' +
            String(!!(synced.zzjgxx && synced.zzjgxx.results)),
        );
      }
      return synced;
    }

    targetWindow.bbgrxx = next;
    targetWindow.userinfo = next;
    try {
      targetWindow.bbgrxx2 = JSON.stringify(next);
    } catch (_) {}

    if (!targetWindow.__bbtotalUserContextLogged) {
      targetWindow.__bbtotalUserContextLogged = true;
      postDebug(
        '[' + label + '] user context ready zzjgxx.results=' +
          String(!!(next.zzjgxx && next.zzjgxx.results)),
      );
    }

    return next;
  };

  const installUserContextHooks = (targetWindow, label) => {
    if (targetWindow.__bbtotalUserContextHooked) {
      return;
    }

    let bbgrxxValue = mergeUserContextObject(targetWindow.bbgrxx);
    let userinfoValue = mergeUserContextObject(targetWindow.userinfo || bbgrxxValue);
    let bbgrxx2Value = (() => {
      try {
        return JSON.stringify(bbgrxxValue);
      } catch (_) {
        return '';
      }
    })();

    const syncAll = (sourceValue, sourceName) => {
      const merged = mergeUserContextObject(sourceValue);
      bbgrxxValue = merged;
      userinfoValue = merged;
      try {
        bbgrxx2Value = JSON.stringify(merged);
      } catch (_) {
        bbgrxx2Value = '';
      }
      postDebug(
        '[' + label + '] user context synced from ' + sourceName +
          ' zzjgxx.results=' + String(!!(merged.zzjgxx && merged.zzjgxx.results)),
      );
      return merged;
    };

    try {
      targetWindow.__bbtotalSyncUserContext = syncAll;

      Object.defineProperty(targetWindow, 'bbgrxx', {
        configurable: true,
        enumerable: true,
        get() {
          return bbgrxxValue;
        },
        set(value) {
          syncAll(value, 'bbgrxx');
        },
      });

      Object.defineProperty(targetWindow, 'userinfo', {
        configurable: true,
        enumerable: true,
        get() {
          return userinfoValue;
        },
        set(value) {
          syncAll(value, 'userinfo');
        },
      });

      Object.defineProperty(targetWindow, 'bbgrxx2', {
        configurable: true,
        enumerable: true,
        get() {
          return bbgrxx2Value;
        },
        set(value) {
          if (typeof value === 'string') {
            try {
              syncAll(JSON.parse(value), 'bbgrxx2');
              return;
            } catch (_) {}
          }
          syncAll(value, 'bbgrxx2');
        },
      });

      targetWindow.__bbtotalUserContextHooked = true;
      syncAll(bbgrxxValue, 'bootstrap');
    } catch (error) {
      postDebug(
        '[' + label + '] user context hook failed: ' + stringifyForLog(error),
      );
    }
  };

  const installConsoleHooks = (targetWindow, label) => {
    if (
      !targetWindow.console ||
      typeof targetWindow.console !== 'object' ||
      targetWindow.__bbtotalConsoleHooked
    ) {
      return;
    }

    targetWindow.__bbtotalConsoleHooked = true;
    const levels = ['log', 'info', 'warn', 'error', 'debug'];
    levels.forEach((level) => {
      const original = targetWindow.console[level];
      if (typeof original !== 'function') {
        return;
      }

      targetWindow.console[level] = (...args) => {
        postDebug(
          '[' + label + '] console.' + level + ': ' +
            args.map((item) => stringifyForLog(item)).join(' '),
        );
        return original.apply(targetWindow.console, args);
      };
    });

    try {
      targetWindow.addEventListener('error', (event) => {
        postDebug(
          '[' + label + '] window.onerror: ' +
            String(event.message || 'unknown error'),
        );
      });
      targetWindow.addEventListener('unhandledrejection', (event) => {
        postDebug(
          '[' + label + '] unhandledrejection: ' +
            stringifyForLog(event.reason || 'unknown reason'),
        );
      });
    } catch (_) {}
  };

  const installVConsole = (targetWindow, label) => {
    const targetDocument = targetWindow.document;
    if (
      !targetDocument ||
      !targetDocument.documentElement ||
      targetWindow.__bbtotalVConsoleDisabled
    ) {
      return;
    }

    const createInstance = () => {
      if (targetWindow.VConsole && !targetWindow.__bbtotalVConsole) {
        try {
          targetWindow.__bbtotalVConsole = new targetWindow.VConsole();
          postDebug('[' + label + '] vConsole installed');
        } catch (error) {
          postDebug(
            '[' + label + '] vConsole init failed: ' + stringifyForLog(error),
          );
        }
      }
    };

    if (targetWindow.VConsole) {
      createInstance();
      return;
    }

    if (targetWindow.__bbtotalVConsoleLoading) {
      return;
    }

    targetWindow.__bbtotalVConsoleLoading = true;
    const script = targetDocument.createElement('script');
    script.id = 'bbtotal-vconsole-script';
    let sourceIndex = 0;

    const loadNext = () => {
      if (sourceIndex >= vConsoleSources.length) {
        targetWindow.__bbtotalVConsoleDisabled = true;
        postDebug('[' + label + '] vConsole load failed for all sources');
        return;
      }
      script.src = vConsoleSources[sourceIndex];
      sourceIndex += 1;
    };

    script.onload = () => {
      createInstance();
    };
    script.onerror = () => {
      postDebug('[' + label + '] vConsole source failed: ' + script.src);
      loadNext();
    };

    loadNext();
    targetDocument.documentElement.appendChild(script);
  };

  const installNetworkHooks = (targetWindow, label) => {
    if (targetWindow.__bbtotalNetworkPatched) {
      return;
    }
    targetWindow.__bbtotalNetworkPatched = true;

    const authHeaders = {
      'login-token': bridgeContext.loginToken || '',
      'tyLoginToken': bridgeContext.loginToken || '',
      channel: bridgeContext.blqd || '',
      blqd: bridgeContext.blqd || '',
      jgbh: bridgeContext.zxbm || bridgeContext.jgbh || '',
      zzbs: bridgeContext.zxbm || bridgeContext.jgbh || '',
      zzjgdmz: bridgeContext.zzjgdmz || bridgeContext.qycode || '',
      qycode: bridgeContext.qycode || '',
    };

    const shouldPatchUrl = (url) =>
      typeof url === 'string' &&
      (url.startsWith('/') || url.includes('appsy.jbysoft.com'));

    const originalFetch =
      targetWindow.fetch ? targetWindow.fetch.bind(targetWindow) : null;
    if (originalFetch) {
      targetWindow.fetch = async (input, init = {}) => {
        const requestUrl =
          typeof input === 'string'
            ? input
            : (input && typeof input === 'object' && 'url' in input ? input.url : '');
        if (!shouldPatchUrl(requestUrl)) {
          return originalFetch(input, init);
        }

        const headers = new Headers(
          init.headers ||
            (input && typeof input === 'object' && 'headers' in input ? input.headers : undefined),
        );
        Object.entries(authHeaders).forEach(([key, value]) => {
          if (!value) {
            return;
          }
          if (!headers.has(key) || headers.get(key) !== value) {
            headers.set(key, value);
          }
        });
        return originalFetch(input, { ...init, headers });
      };
    }

    const xhrPrototype = targetWindow.XMLHttpRequest &&
      targetWindow.XMLHttpRequest.prototype;
    if (!xhrPrototype) {
      return;
    }

    const originalOpen = xhrPrototype.open;
    const originalSend = xhrPrototype.send;
    const originalSetRequestHeader = xhrPrototype.setRequestHeader;

    xhrPrototype.setRequestHeader = function(name, value) {
      this.__bbtotalHeaders = this.__bbtotalHeaders || {};
      this.__bbtotalHeaders[name.toLowerCase()] = value;
      return originalSetRequestHeader.call(this, name, value);
    };

    xhrPrototype.open = function(method, url, ...rest) {
      this.__bbtotalMethod = method;
      this.__bbtotalUrl = url;
      this.__bbtotalHeaders = this.__bbtotalHeaders || {};
      return originalOpen.call(this, method, url, ...rest);
    };

    xhrPrototype.send = function(body) {
      const requestUrl = String(this.__bbtotalUrl || '');
      if (shouldPatchUrl(requestUrl)) {
        const requestHeaders = this.__bbtotalHeaders || {};
        Object.entries(authHeaders).forEach(([key, value]) => {
          if (!value) {
            return;
          }
          if (requestHeaders[key.toLowerCase()] === value) {
            return;
          }
          requestHeaders[key.toLowerCase()] = value;
          try {
            originalSetRequestHeader.call(this, key, value);
          } catch (_) {}
        });
      }

      return originalSend.call(this, body);
    };

    postDebug('[' + label + '] network hooks installed');
  };

  const installPromptProxy = (targetWindow) => {
    if (targetWindow.__bbtotalPromptPatched) {
      return;
    }
    targetWindow.__bbtotalPromptPatched = true;

    const originalPrompt =
      targetWindow.prompt ? targetWindow.prompt.bind(targetWindow) : null;
    targetWindow.prompt = (message, defaultValue) => {
      if (typeof message === 'string' && message.includes('launchMiniProgram')) {
        dispatchNativeCommand(message);
        return '';
      }
      return originalPrompt ? originalPrompt(message, defaultValue) : '';
    };
  };

  const buildBridgeForWindow = (targetWindow, label) => {
    const wrapBridgeMethod = (methodName, handler) => (...args) => {
      postDebug(
        '[' + label + '] SYAppModel.' + methodName + ' called args=' +
          args.map((item) => stringifyForLog(item)).join(' | '),
      );
      try {
        const result = handler(...args);
        postDebug(
          '[' + label + '] SYAppModel.' + methodName + ' ok result=' +
            stringifyForLog(result).slice(0, 800),
        );
        return result;
      } catch (error) {
        const errorText =
          error && error.stack
            ? String(error.stack)
            : stringifyForLog(error);
        postDebug(
          '[' + label + '] SYAppModel.' + methodName + ' error: ' + errorText,
        );
        throw error;
      }
    };

    return {
      getLocation: wrapBridgeMethod('getLocation', () => {
        applyPayload(targetWindow);
        return resolveLocationResult();
      }),
      getUpdatingLocation: wrapBridgeMethod('getUpdatingLocation', () => {
        applyPayload(targetWindow);
        return resolveUpdatingLocationResult();
      }),
      getUserinfo: wrapBridgeMethod('getUserinfo', () => {
        const contextValue = ensureUserContext(targetWindow, { label });
        notifyUserinfo(targetWindow, contextValue);
        return JSON.stringify(contextValue);
      }),
      getUserInfo: wrapBridgeMethod('getUserInfo', () => {
        const contextValue = ensureUserContext(targetWindow, { label });
        notifyUserinfo(targetWindow, contextValue);
        return JSON.stringify(contextValue);
      }),
      getWifiinfo: wrapBridgeMethod('getWifiinfo', () => {
        notifyWifiinfo(targetWindow);
        return resolveWifiInfoResult();
      }),
      postMessage: wrapBridgeMethod(
        'postMessage',
        (message) => dispatchNativeCommand(message),
      ),
      hiddenTitle: wrapBridgeMethod(
        'hiddenTitle',
        () => dispatchNativeCommand({ type: 'hiddenTitle' }),
      ),
      hideNav: wrapBridgeMethod(
        'hideNav',
        (hidden) =>
          dispatchNativeCommand({
            function: 'config',
            hideNav: hidden ? 'YES' : 'NO',
          }),
      ),
      openUrl: wrapBridgeMethod(
        'openUrl',
        (url) => dispatchNativeCommand({ openUrl: url }),
      ),
      reloadData: wrapBridgeMethod(
        'reloadData',
        () => dispatchNativeCommand({ function: 'reloadData' }),
      ),
      yuyueMeeting: wrapBridgeMethod(
        'yuyueMeeting',
        (message) =>
          dispatchNativeCommand({ type: 'yuyueMeeting', params: message }),
      ),
      endMeeting: wrapBridgeMethod(
        'endMeeting',
        (message) =>
          dispatchNativeCommand({ type: 'endMeeting', params: message }),
      ),
      joinyuyueMeeting: wrapBridgeMethod(
        'joinyuyueMeeting',
        (message) =>
          dispatchNativeCommand({ type: 'joinyuyueMeeting', params: message }),
      ),
      joinyuyueZhibo: wrapBridgeMethod(
        'joinyuyueZhibo',
        (message) =>
          dispatchNativeCommand({ type: 'joinyuyueZhibo', params: message }),
      ),
    };
  };

  const installIntoWindow = (targetWindow, rawLabel = 'main') => {
    if (!targetWindow || targetWindow.__bbtotalWindowBridgeInstalled) {
      return;
    }

    const label = frameLabel(targetWindow, rawLabel);
    targetWindow.__bbtotalPreset = payload;
    targetWindow.__bbtotalApplyPreset = () => applyPayload(targetWindow);
    targetWindow.__bbtotalNotifyUserinfo = () => notifyUserinfo(targetWindow);
    targetWindow.__bbtotalNotifyWifiinfo = () => notifyWifiinfo(targetWindow);

    targetWindow.SYAppModel = targetWindow.SYAppModel || {};
    Object.assign(
      targetWindow.SYAppModel,
      buildBridgeForWindow(targetWindow, label),
    );

    targetWindow.webkit = targetWindow.webkit || {};
    targetWindow.webkit.messageHandlers =
      targetWindow.webkit.messageHandlers || {};
    targetWindow.webkit.messageHandlers.SYAppModel = {
      postMessage: (message) => dispatchNativeCommand(message),
    };

    targetWindow.android = targetWindow.SYAppModel;
    targetWindow.Android = targetWindow.SYAppModel;
    targetWindow.appModel = targetWindow.SYAppModel;
    targetWindow.AppModel = targetWindow.SYAppModel;

    installConsoleHooks(targetWindow, label);
    installNetworkHooks(targetWindow, label);
    installPromptProxy(targetWindow);
    installVConsole(targetWindow, label);
    installUserContextHooks(targetWindow, label);
    ensureUserContext(targetWindow, { label });
    setTimeout(() => ensureUserContext(targetWindow, { label }), 0);
    setTimeout(() => ensureUserContext(targetWindow, { label }), 150);
    setTimeout(() => ensureUserContext(targetWindow, { label }), 800);
    setTimeout(() => ensureUserContext(targetWindow, { label }), 2000);

    targetWindow.__bbtotalWindowBridgeInstalled = true;
    postDebug('[' + label + '] bridge installed');
  };

  const tryInstallIntoFrame = (frameElement, index) => {
    if (trackedFrames.has(frameElement)) {
      return;
    }
    trackedFrames.add(frameElement);

    const frameName =
      frameElement.getAttribute('name') ||
      frameElement.id ||
      'frame-' + index;

    const install = () => {
      try {
        if (!frameElement.contentWindow) {
          return;
        }
        installIntoWindow(frameElement.contentWindow, frameName);
      } catch (error) {
        postDebug(
          '[' + frameName + '] frame injection skipped: ' +
            stringifyForLog(error),
        );
      }
    };

    frameElement.addEventListener('load', install);
    install();
  };

  const installIntoFrames = () => {
    const frames = Array.from(document.querySelectorAll('iframe, frame'));
    frames.forEach((frameElement, index) => {
      tryInstallIntoFrame(frameElement, index);
    });
  };

  const watchFrames = () => {
    if (!document.documentElement || window.__bbtotalFrameObserverInstalled) {
      return;
    }
    window.__bbtotalFrameObserverInstalled = true;

    const observer = new MutationObserver((mutations) => {
      mutations.forEach((mutation) => {
        mutation.addedNodes.forEach((node) => {
          if (!node || node.nodeType !== Node.ELEMENT_NODE) {
            return;
          }

          if (node.matches && node.matches('iframe, frame')) {
            tryInstallIntoFrame(node, Date.now());
          }
          if (node.querySelectorAll) {
            node
              .querySelectorAll('iframe, frame')
              .forEach((frameElement, index) => {
                tryInstallIntoFrame(frameElement, index);
              });
          }
        });
      });
    });

    observer.observe(document.documentElement, {
      childList: true,
      subtree: true,
    });
  };

  window.__bbtotalPreset = payload;
  window.__bbtotalApplyPreset = () => applyPayload(window);
  window.__bbtotalNotifyUserinfo = () => notifyUserinfo(window);
  window.__bbtotalNotifyWifiinfo = () => notifyWifiinfo(window);

  $bridgeInstallScript
  installIntoWindow(window, 'main');
  installIntoFrames();
  watchFrames();
  setTimeout(installIntoFrames, 600);
  setTimeout(installIntoFrames, 1800);
  window.__bbtotalBridgeInstalled = true;
})();
''';
  }

  static String _buildAndroidStartupScript({
    required String bridgeContextJson,
    required String locationPayloadJson,
    required String userinfoResultJson,
    required String wifiInfoJson,
  }) {
    return _buildBaseStartupScript(
      bridgeContextJson: bridgeContextJson,
      bridgeInstallScript: '''
  const nativeBridge = window.bbtotalNativeBridge || null;
  dispatchNativeCommand = (command) => {
    if (!nativeBridge || typeof nativeBridge.postMessage !== 'function') {
      return '';
    }

    const raw =
      typeof command === 'string' ? command : JSON.stringify(command || {});
    try {
      nativeBridge.postMessage(raw);
    } catch (_) {}
    return '';
  };

  postDebug = (message) => {
    if (!nativeBridge || typeof nativeBridge.log !== 'function') {
      return;
    }
    try {
      nativeBridge.log(String(message));
    } catch (_) {}
  };

  const callNativeString = (methodName, fallback) => {
    if (!nativeBridge || typeof nativeBridge[methodName] !== 'function') {
      return fallback;
    }
    try {
      const result = nativeBridge[methodName]();
      return typeof result === 'string' ? result : fallback;
    } catch (_) {
      return fallback;
    }
  };

  resolveLocationResult = () =>
    callNativeString('getLocation', JSON.stringify(payload));
  resolveUpdatingLocationResult = () =>
    callNativeString('getUpdatingLocation', JSON.stringify(payload));
  resolveUserinfoResult = () =>
    callNativeString('getUserinfo', JSON.stringify(userinfoResult));
  resolveWifiInfoResult = () =>
    callNativeString('getWifiinfo', JSON.stringify(wifiInfo));
''',
      locationPayloadJson: locationPayloadJson,
      userinfoResultJson: userinfoResultJson,
      wifiInfoJson: wifiInfoJson,
    );
  }

  static String _buildIosStartupScript({
    required String bridgeContextJson,
    required String locationPayloadJson,
    required String userinfoResultJson,
    required String wifiInfoJson,
  }) {
    return _buildBaseStartupScript(
      bridgeContextJson: bridgeContextJson,
      bridgeInstallScript: '''
  dispatchNativeCommand = (command) => {
    const raw =
      typeof command === 'string' ? command : JSON.stringify(command || {});
    try {
      window.webkit.messageHandlers.bbtotalNativeBridge.postMessage(raw);
    } catch (_) {}
    return '';
  };

  postDebug = (message) => {
    try {
      window.webkit.messageHandlers.bbtotalNativeLog.postMessage(String(message));
    } catch (_) {}
  };
''',
      locationPayloadJson: locationPayloadJson,
      userinfoResultJson: userinfoResultJson,
      wifiInfoJson: wifiInfoJson,
    );
  }
}

class CheckInWebViewPageBridgeDefaults {
  static const String inspectedCheckInUrl =
      'https://appsy.jbysoft.com/dxgl-app/dxslgl/#/dxslglComp/dxslglfq/index?bm=03060&dx_29_sjdxsl=3517&tyLoginToken=6d77b5d26961f35f56e827e81558da4710:app_02&ticket=nothing&cheque=tyrz029860a4855f44e5a5899641ac483483&zzjgdmz=shineyue&cpbs=bbPro&qycode=shineyue&ztConfig=7171a46ae52cd9c7c0278a72c65492b0fd234129620126cd57ac2fccaaec061b0abed44bcb02786c4057648caf5b99b6c4e7488567e1d5fa7399b6cf5fdeb7ea09b27db192ee5320e008c8c888a156ab41012aa0eea91e2a034cd260ee71bc6e6c20b42e49633af74bfc77a9e1142c02c45548a3e3e89cc6a93eda4ca371a379&zjhm=130528198704157217&khbh=01000000009000026129&jgbm=130110000102a705&userid=3517&zxbm=1301100001&cysxFlag=false';
}
