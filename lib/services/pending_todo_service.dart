import 'dart:async';
import 'dart:convert';

import 'package:http/http.dart' as http;

import '../models/user_login_info.dart';

class PendingTodoService {
  PendingTodoService({
    http.Client? client,
  }) : _client = client ?? http.Client();

  static const String _endpoint =
      'https://appsy.jbysoft.com/V2/PT/business/dealt/dbTableV2\$m=query.service';
  static const String _appId = '2019082217092434';
  static const String _service = 'shineyue';
  static const String _version = '1.0.1';
  static const String _zzbm = 'bbPro';

  final http.Client _client;

  Future<Map<String, dynamic>> fetchPendingTodos(
    UserLoginInfo loginInfo, {
    int page = 1,
    int size = 40,
  }) async {
    final String orgNumber =
        loginInfo.zxbm.isNotEmpty ? loginInfo.zxbm : loginInfo.jgbh;
    final String qycode =
        loginInfo.qycode.isNotEmpty ? loginInfo.qycode : _service;
    final String zzjgdmz =
        loginInfo.zzjgdmz.isNotEmpty ? loginInfo.zzjgdmz : qycode;
    final String client = loginInfo.client.isNotEmpty ? loginInfo.client : '4';

    final response = await _client
        .post(
          Uri.parse(_endpoint),
          headers: <String, String>{
            'blqd': loginInfo.blqd,
            'channel': loginInfo.blqd,
            'client': client,
            'jgbh': orgNumber,
            'login-token': loginInfo.loginToken,
            'm-sy-appid': _appId,
            'm-sy-service': _service,
            'm-sy-version': _version,
            'qycode': qycode,
            'zzbm': _zzbm,
            'zzbs': orgNumber,
            'zzjgdmz': zzjgdmz,
            'Content-Type': 'application/json',
          },
          body: jsonEncode(
            _buildRequestBody(
              loginInfo: loginInfo,
              organizationNumber: orgNumber,
              page: page,
              size: size,
            ),
          ),
        )
        .timeout(const Duration(seconds: 10));

    if (response.statusCode != 200) {
      throw PendingTodoServiceException(
        '待办查询失败，HTTP ${response.statusCode}',
      );
    }

    final Object decoded = jsonDecode(response.body);
    if (decoded is Map<String, dynamic>) {
      return decoded;
    }

    if (decoded is Map) {
      return decoded.map<String, dynamic>(
        (dynamic key, dynamic value) => MapEntry(key.toString(), value),
      );
    }

    throw const PendingTodoServiceException('待办查询响应不是有效的 JSON 对象。');
  }

  Map<String, dynamic> _buildRequestBody({
    required UserLoginInfo loginInfo,
    required String organizationNumber,
    required int page,
    required int size,
  }) {
    final DateTime now = DateTime.now();
    final DateTime start = DateTime(now.year, now.month - 6, now.day);

    String formatDate(DateTime value) {
      String twoDigits(int number) => number.toString().padLeft(2, '0');
      return '${value.year}-${twoDigits(value.month)}-${twoDigits(value.day)}';
    }

    return <String, dynamic>{
      'organizationNumber': organizationNumber,
      'grbh': loginInfo.grbh,
      'cxfl': 'db',
      'size': size,
      'page': page,
      'blqd': loginInfo.blqd,
      'startTime': formatDate(start),
      'endTime': formatDate(now),
      'sort': '0',
      'userId': loginInfo.userId,
      'dbms': null,
      'xsrwms': '1',
      'isComplex': false,
      'pushDeal': '1',
      'dxslid': loginInfo.userId.toString(),
      'available': '1',
      'ptSettings': <String, dynamic>{},
      'queryCriteria': <dynamic>[],
      'ywzt': 0,
    };
  }

  void dispose() {
    _client.close();
  }
}

class PendingTodoServiceException implements Exception {
  const PendingTodoServiceException(this.message);

  final String message;

  @override
  String toString() => 'PendingTodoServiceException: $message';
}
