import 'dart:convert';

import 'check_in_location_preset.dart';
import 'user_login_info.dart';

class HybridRuntimeContext {
  const HybridRuntimeContext._({
    required this.baseUrl,
    required this.resolvedUri,
    required this.loginInfo,
    required this.preset,
    required this.userInfoObject,
  });

  factory HybridRuntimeContext.fromInputs({
    required String baseUrl,
    UserLoginInfo? loginInfo,
    CheckInLocationPreset? preset,
  }) {
    final Uri resolvedUri = preset != null
        ? preset.buildCheckInUri(baseUrl)
        : Uri.parse(baseUrl);

    return HybridRuntimeContext._(
      baseUrl: baseUrl,
      resolvedUri: resolvedUri,
      loginInfo: loginInfo,
      preset: preset,
      userInfoObject: _buildUserInfoObject(loginInfo),
    );
  }

  final String baseUrl;
  final Uri resolvedUri;
  final UserLoginInfo? loginInfo;
  final CheckInLocationPreset? preset;
  final Map<String, dynamic> userInfoObject;

  String get origin {
    final String port = resolvedUri.hasPort ? ':${resolvedUri.port}' : '';
    return '${resolvedUri.scheme}://${resolvedUri.host}$port';
  }

  String get userInfoJson => jsonEncode(userInfoObject);

  Map<String, dynamic> get locationPayload {
    if (preset != null) {
      return preset!.toBridgePayload();
    }
    return <String, dynamic>{
      'data': <String, dynamic>{},
      'errorCode': 1,
      'msg': '定位尚未准备',
    };
  }

  Map<String, dynamic> get wifiInfo => <String, dynamic>{
        'ssid': '',
        'bssid': '',
        'wifiName': '',
        'wifiMac': '',
        'isWifi': false,
        'device': 'flutter_inappwebview',
      };

  Map<String, dynamic> get bridgeContext => <String, dynamic>{
        'loginToken': loginInfo?.loginToken ?? '',
        'tyLoginToken': loginInfo?.loginToken ?? '',
        'username': loginInfo?.username ?? '',
        'userid': loginInfo?.userId.toString() ?? '',
        'grbh': loginInfo?.grbh ?? '',
        'idCard': loginInfo?.idCard ?? '',
        'blqd': loginInfo?.blqd ?? '',
        'jgbh': loginInfo?.jgbh ?? '',
        'jgbm': loginInfo?.jgbm ?? '',
        'zxbm': loginInfo?.zxbm ?? '',
        'qycode': loginInfo?.qycode ?? '',
        'zzjgdmz': loginInfo?.zzjgdmz ?? '',
        'ticket': preset?.ticket ?? loginInfo?.ticket ?? '',
        'cheque': preset?.cheque ?? '',
      };

  Map<String, String> get authHeaders {
    if (loginInfo == null) {
      return <String, String>{};
    }

    final String orgNo =
        loginInfo!.zxbm.isNotEmpty ? loginInfo!.zxbm : loginInfo!.jgbh;
    final String orgCode = loginInfo!.zzjgdmz.isNotEmpty
        ? loginInfo!.zzjgdmz
        : loginInfo!.qycode;

    return <String, String>{
      if (loginInfo!.loginToken.isNotEmpty) 'login-token': loginInfo!.loginToken,
      if (loginInfo!.loginToken.isNotEmpty) 'tyLoginToken': loginInfo!.loginToken,
      if (loginInfo!.blqd.isNotEmpty) 'channel': loginInfo!.blqd,
      if (loginInfo!.blqd.isNotEmpty) 'blqd': loginInfo!.blqd,
      if (orgNo.isNotEmpty) 'jgbh': orgNo,
      if (orgNo.isNotEmpty) 'zzbs': orgNo,
      if (orgCode.isNotEmpty) 'zzjgdmz': orgCode,
    };
  }

  Map<String, String> get storageSeed => <String, String>{
        if ((loginInfo?.loginToken ?? '').isNotEmpty)
          'loginToken': loginInfo!.loginToken,
        if ((loginInfo?.loginToken ?? '').isNotEmpty)
          'tyLoginToken': loginInfo!.loginToken,
        if ((loginInfo?.blqd ?? '').isNotEmpty) 'blqd': loginInfo!.blqd,
        if (loginInfo != null) 'userid': loginInfo!.userId.toString(),
        if ((preset?.ticket ?? loginInfo?.ticket ?? '').isNotEmpty)
          'ticket': preset?.ticket ?? loginInfo?.ticket ?? '',
        if ((preset?.cheque ?? '').isNotEmpty) 'cheque': preset!.cheque,
        if (userInfoObject.isNotEmpty) 'userinfo': userInfoJson,
      };

  List<HybridCookieSeed> get cookieSeeds => storageSeed.entries
      .map(
        (entry) => HybridCookieSeed(
          name: entry.key,
          value: entry.value,
          isSecure: resolvedUri.scheme == 'https',
        ),
      )
      .toList();

  Map<String, dynamic> toScriptRuntime() => <String, dynamic>{
        'origin': origin,
        'host': resolvedUri.host,
        'resolvedUrl': resolvedUri.toString(),
        'bridgeContext': bridgeContext,
        'authHeaders': authHeaders,
        'storageSeed': storageSeed,
        'userInfo': userInfoObject,
        'userInfoJson': userInfoJson,
        'locationPayload': locationPayload,
        'wifiInfo': wifiInfo,
      };

  static Map<String, dynamic> _buildUserInfoObject(UserLoginInfo? loginInfo) {
    if (loginInfo == null) {
      return <String, dynamic>{};
    }

    final Map<String, dynamic> raw = _decodeJsonMap(loginInfo.rawUserInfoJson);
    return <String, dynamic>{
      ...raw,
      'username': raw['username'] ?? raw['xingming'] ?? loginInfo.username,
      'xingming': raw['xingming'] ?? raw['username'] ?? loginInfo.username,
      'userid': raw['userid'] ?? loginInfo.userId,
      'grbh': raw['grbh'] ?? loginInfo.grbh,
      'zjhm': raw['zjhm'] ?? loginInfo.idCard,
      'loginToken': raw['loginToken'] ?? loginInfo.loginToken,
      'tyLoginToken': raw['tyLoginToken'] ?? loginInfo.loginToken,
      'blqd': raw['blqd'] ?? loginInfo.blqd,
      'jgbh': raw['jgbh'] ?? loginInfo.jgbh,
      'jgbm': raw['jgbm'] ?? loginInfo.jgbm,
      'zxbm': raw['zxbm'] ?? loginInfo.zxbm,
      'qycode': raw['qycode'] ?? loginInfo.qycode,
      'zzjgdmz': raw['zzjgdmz'] ?? loginInfo.zzjgdmz,
      'ticket': raw['ticket'] ?? loginInfo.ticket,
    };
  }

  static Map<String, dynamic> _decodeJsonMap(String raw) {
    if (raw.isEmpty) {
      return <String, dynamic>{};
    }

    try {
      final Object decoded = jsonDecode(raw);
      if (decoded is Map<String, dynamic>) {
        return decoded;
      }
      if (decoded is Map) {
        return decoded.map<String, dynamic>(
          (dynamic key, dynamic value) => MapEntry(key.toString(), value),
        );
      }
    } catch (_) {}

    return <String, dynamic>{};
  }
}

class HybridCookieSeed {
  const HybridCookieSeed({
    required this.name,
    required this.value,
    required this.isSecure,
  });

  final String name;
  final String value;
  final bool isSecure;
}
