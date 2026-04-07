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

    final Map<String, dynamic> result = <String, dynamic>{
      ...rawUserinfo,
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
    };

    final Map<String, dynamic> zzjgxx = rawZzjgxx.isNotEmpty
        ? rawZzjgxx
        : _normalizeMap(result['zzjgxx']);
    final Map<String, dynamic> results =
        _normalizeMap(zzjgxx['results'])..putIfAbsent('userData', () => <String, dynamic>{});
    final Map<String, dynamic> userData =
        _normalizeMap(results['userData']);
    final Map<String, dynamic> gjmsg =
        _normalizeMap(results['gjmsg']);
    final Map<String, dynamic> zxjgInfo =
        _normalizeMap(results['zxjgInfo']);

    userData['name'] = _firstNonEmpty(userData['name'], result['username']);
    userData['personId'] = _firstNonEmpty(userData['personId'], result['userid']);
    userData['personalNo'] = _firstNonEmpty(userData['personalNo'], result['grbh']);
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

  const notifyUserinfo = () => {
    if (typeof window.getUserinfoResult === 'function') {
      window.getUserinfoResult(JSON.stringify(userinfoResult));
    }
    if (typeof window.getUserInfoResult === 'function') {
      window.getUserInfoResult(JSON.stringify(userinfoResult));
    }
  };

  const notifyWifiinfo = () => {
    if (typeof window.getWifiinfoResult === 'function') {
      window.getWifiinfoResult(JSON.stringify(wifiInfo));
    }
  };

  const applyPayload = () => {
    const nextBbgrxx =
      window.bbgrxx && typeof window.bbgrxx === 'object' ? window.bbgrxx : {};
    nextBbgrxx.locationMsg = payload;
    nextBbgrxx.updatingLocationMsg = payload;
    window.bbgrxx = nextBbgrxx;

    const nextSe =
      window.Se && typeof window.Se === 'object' ? window.Se : {};
    nextSe.locationMsg = payload;
    window.Se = nextSe;

    const textarea = document.querySelector('#textarea-dx_29_dkwz');
    if (textarea) {
      textarea.value = payload.data.address || '';
      textarea.innerText = payload.data.address || '';
      ['input', 'change', 'blur'].forEach((eventName) => {
        textarea.dispatchEvent(new Event(eventName, { bubbles: true }));
      });
    }

    if (typeof window.locationResult === 'function') {
      window.locationResult(payload);
    } else if (
      window.onBasicFormRef &&
      typeof window.onBasicFormRef.updateGhsxLoaction === 'function'
    ) {
      window.onBasicFormRef.updateGhsxLoaction();
    }

    return JSON.stringify(payload);
  };

  const installNetworkHooks = () => {
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

    const originalOpen = XMLHttpRequest.prototype.open;
    const originalSend = XMLHttpRequest.prototype.send;
    const originalSetRequestHeader = XMLHttpRequest.prototype.setRequestHeader;

    XMLHttpRequest.prototype.setRequestHeader = function(name, value) {
      this.__bbtotalHeaders = this.__bbtotalHeaders || {};
      this.__bbtotalHeaders[name.toLowerCase()] = value;
      return originalSetRequestHeader.call(this, name, value);
    };

    XMLHttpRequest.prototype.open = function(method, url, ...rest) {
      this.__bbtotalMethod = method;
      this.__bbtotalUrl = url;
      this.__bbtotalHeaders = this.__bbtotalHeaders || {};
      return originalOpen.call(this, method, url, ...rest);
    };

    XMLHttpRequest.prototype.send = function(body) {
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
  };

  const installPromptProxy = (dispatchCommand) => {
    const originalPrompt = window.prompt ? window.prompt.bind(window) : null;
    window.prompt = (message, defaultValue) => {
      if (typeof message === 'string' && message.includes('launchMiniProgram')) {
        dispatchCommand(message);
        return '';
      }
      return originalPrompt ? originalPrompt(message, defaultValue) : '';
    };
  };

  window.__bbtotalPreset = payload;
  window.__bbtotalApplyPreset = applyPayload;
  window.__bbtotalNotifyUserinfo = notifyUserinfo;
  window.__bbtotalNotifyWifiinfo = notifyWifiinfo;

  installNetworkHooks();
  $bridgeInstallScript
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
  const postNativeCommand = (command) => {
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

  window.SYAppModel = {
    getLocation: () => {
      applyPayload();
      return callNativeString('getLocation', JSON.stringify(payload));
    },
    getUpdatingLocation: () => {
      applyPayload();
      return callNativeString('getUpdatingLocation', JSON.stringify(payload));
    },
    getUserinfo: () => {
      notifyUserinfo();
      return callNativeString('getUserinfo', JSON.stringify(userinfoResult));
    },
    getUserInfo: () => {
      notifyUserinfo();
      return callNativeString('getUserInfo', JSON.stringify(userinfoResult));
    },
    getWifiinfo: () => {
      notifyWifiinfo();
      return callNativeString('getWifiinfo', JSON.stringify(wifiInfo));
    },
    postMessage: (message) => postNativeCommand(message),
    hiddenTitle: () => postNativeCommand({ type: 'hiddenTitle' }),
    hideNav: (hidden) =>
      postNativeCommand({
        function: 'config',
        hideNav: hidden ? 'YES' : 'NO',
      }),
    openUrl: (url) => postNativeCommand({ openUrl: url }),
    reloadData: () => postNativeCommand({ function: 'reloadData' }),
    yuyueMeeting: (message) =>
      postNativeCommand({ type: 'yuyueMeeting', params: message }),
    endMeeting: (message) =>
      postNativeCommand({ type: 'endMeeting', params: message }),
    joinyuyueMeeting: (message) =>
      postNativeCommand({ type: 'joinyuyueMeeting', params: message }),
    joinyuyueZhibo: (message) =>
      postNativeCommand({ type: 'joinyuyueZhibo', params: message }),
  };

  window.webkit = window.webkit || {};
  window.webkit.messageHandlers = window.webkit.messageHandlers || {};
  window.webkit.messageHandlers.SYAppModel = {
    postMessage: (message) => postNativeCommand(message),
  };

  window.android = window.SYAppModel;
  window.Android = window.SYAppModel;
  window.appModel = window.SYAppModel;
  window.AppModel = window.SYAppModel;

  installPromptProxy(postNativeCommand);
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
  const postNativeCommand = (command) => {
    const raw =
      typeof command === 'string' ? command : JSON.stringify(command || {});
    try {
      window.webkit.messageHandlers.bbtotalNativeBridge.postMessage(raw);
    } catch (_) {}
    return '';
  };

  window.SYAppModel = {
    getLocation: () => {
      applyPayload();
      return JSON.stringify(payload);
    },
    getUpdatingLocation: () => {
      applyPayload();
      return JSON.stringify(payload);
    },
    getUserinfo: () => {
      notifyUserinfo();
      return JSON.stringify(userinfoResult);
    },
    getUserInfo: () => {
      notifyUserinfo();
      return JSON.stringify(userinfoResult);
    },
    getWifiinfo: () => {
      notifyWifiinfo();
      return JSON.stringify(wifiInfo);
    },
    postMessage: (message) => postNativeCommand(message),
    hiddenTitle: () => postNativeCommand({ type: 'hiddenTitle' }),
    hideNav: (hidden) =>
      postNativeCommand({
        function: 'config',
        hideNav: hidden ? 'YES' : 'NO',
      }),
    openUrl: (url) => postNativeCommand({ openUrl: url }),
    reloadData: () => postNativeCommand({ function: 'reloadData' }),
    yuyueMeeting: (message) =>
      postNativeCommand({ type: 'yuyueMeeting', params: message }),
    endMeeting: (message) =>
      postNativeCommand({ type: 'endMeeting', params: message }),
    joinyuyueMeeting: (message) =>
      postNativeCommand({ type: 'joinyuyueMeeting', params: message }),
    joinyuyueZhibo: (message) =>
      postNativeCommand({ type: 'joinyuyueZhibo', params: message }),
  };

  window.android = window.SYAppModel;
  window.Android = window.SYAppModel;
  window.appModel = window.SYAppModel;
  window.AppModel = window.SYAppModel;

  installPromptProxy(postNativeCommand);
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
