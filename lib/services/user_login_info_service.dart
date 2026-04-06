import 'dart:async';
import 'dart:convert';

import 'package:http/http.dart' as http;

import '../models/user_login_info.dart';

class UserLoginInfoService {
  UserLoginInfoService({
    http.Client? client,
  }) : _client = client ?? http.Client();

  static const String _endpoint =
      'https://appsy.jbysoft.com/PT/business/token/userLoginInfo';
  static const String _detailEndpoint =
      'https://appsy.jbysoft.com/PT/business/token/getUserInfoViaToken';

  final http.Client _client;

  Future<UserLoginInfo> fetchMobileLoginInfo(String username) async {
    final Uri uri = Uri.parse(_endpoint);
    final response = await _client
        .post(
          uri,
          headers: const <String, String>{
            'Content-Type': 'application/json',
          },
          body: jsonEncode(<String, dynamic>{
            'page': 1,
            'size': 10,
            'username': username,
            'blqd': 'app_02',
          }),
        )
        .timeout(const Duration(seconds: 10));

    if (response.statusCode != 200) {
      throw UserLoginInfoServiceException(
        '登录信息查询失败，HTTP ${response.statusCode}',
      );
    }

    final Map<String, dynamic> payload =
        jsonDecode(response.body) as Map<String, dynamic>;
    final bool success = payload['success'] == true;
    if (!success) {
      throw UserLoginInfoServiceException(
        (payload['msg'] ?? '登录信息查询失败').toString(),
      );
    }

    final List<dynamic> items = (payload['data'] as List<dynamic>?) ?? <dynamic>[];
    final List<UserLoginInfo> loginInfos = items
        .whereType<Map<String, dynamic>>()
        .map(UserLoginInfo.fromJson)
        .where((UserLoginInfo item) => item.username.isNotEmpty)
        .toList();

    if (loginInfos.isEmpty) {
      throw const UserLoginInfoServiceException('没有查询到该用户的登录信息。');
    }

    for (final UserLoginInfo item in loginInfos) {
      if (item.isMobileApp) {
        return _fetchUserInfoViaToken(item);
      }
    }

    throw const UserLoginInfoServiceException(
      '没有查询到移动端 app_02 的登录信息。',
    );
  }

  void dispose() {
    _client.close();
  }

  Future<UserLoginInfo> _fetchUserInfoViaToken(UserLoginInfo loginInfo) async {
    final Uri uri = Uri.parse(
      '$_detailEndpoint?token=${Uri.encodeQueryComponent(loginInfo.loginToken)}',
    );

    final response = await _client.get(uri).timeout(const Duration(seconds: 10));
    if (response.statusCode != 200) {
      throw UserLoginInfoServiceException(
        '登录详情查询失败，HTTP ${response.statusCode}',
      );
    }

    final Map<String, dynamic> payload =
        jsonDecode(response.body) as Map<String, dynamic>;
    final Map<String, dynamic> userInfo =
        _decodeJsonObject(payload['userinfo']) ?? <String, dynamic>{};
    final Map<String, dynamic> zzjgxx =
        _asMap(userInfo['zzjgxx']) ?? <String, dynamic>{};
    final Map<String, dynamic> gjmsg =
        _asMap(zzjgxx['gjmsg']) ?? <String, dynamic>{};
    final Map<String, dynamic> compmsg =
        _asMap(zzjgxx['compmsg']) ?? <String, dynamic>{};

    final String qycode =
        (userInfo['qycode'] ?? payload['qycode'] ?? '').toString();
    final String zzjgdmz = (gjmsg['zjbzxbm'] ??
            compmsg['zjbzxbm'] ??
            userInfo['qycode'] ??
            payload['zzjgdmz'] ??
            '')
        .toString();

    return loginInfo.copyWith(
      jgbm: (payload['jgbm'] ?? userInfo['bmbh'] ?? '').toString(),
      zxbm: (payload['zxbm'] ?? userInfo['zxbm'] ?? '').toString(),
      qycode: qycode,
      zzjgdmz: zzjgdmz,
      ticket: (payload['ticket'] ?? '').toString(),
    );
  }

  Map<String, dynamic>? _decodeJsonObject(Object? value) {
    if (value is Map<String, dynamic>) {
      return value;
    }

    if (value is String && value.isNotEmpty) {
      final Object decoded = jsonDecode(value);
      if (decoded is Map<String, dynamic>) {
        return decoded;
      }
    }

    return null;
  }

  Map<String, dynamic>? _asMap(Object? value) {
    if (value is Map<String, dynamic>) {
      return value;
    }

    if (value is Map) {
      return value.map(
        (key, dynamic mapValue) => MapEntry(key.toString(), mapValue),
      );
    }

    return null;
  }
}

class UserLoginInfoServiceException implements Exception {
  const UserLoginInfoServiceException(this.message);

  final String message;

  @override
  String toString() => 'UserLoginInfoServiceException: $message';
}
