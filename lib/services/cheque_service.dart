import 'dart:async';
import 'dart:convert';
import 'dart:math';

import 'package:http/http.dart' as http;

import '../models/check_in_location_preset.dart';

class ChequeInfo {
  const ChequeInfo({
    required this.cheque,
    required this.debugInfo,
    required this.ticket,
  });

  final String cheque;
  final ChequeDebugInfo debugInfo;
  final String ticket;
}

class ChequeDebugInfo {
  const ChequeDebugInfo({
    required this.requestBody,
    required this.requestHeaders,
    this.errorMessage,
    this.responseBody,
    this.statusCode,
  });

  final String requestBody;
  final Map<String, String> requestHeaders;
  final String? errorMessage;
  final String? responseBody;
  final int? statusCode;

  String toMultilineText() {
    final StringBuffer buffer = StringBuffer()
      ..writeln('Request Headers:')
      ..writeln(const JsonEncoder.withIndent('  ').convert(requestHeaders))
      ..writeln('')
      ..writeln('Request Body:')
      ..writeln(requestBody);

    if (statusCode != null) {
      buffer
        ..writeln('')
        ..writeln('Response Status: $statusCode');
    }
    if (responseBody != null && responseBody!.isNotEmpty) {
      buffer
        ..writeln('')
        ..writeln('Response Body:')
        ..writeln(responseBody);
    }
    if (errorMessage != null && errorMessage!.isNotEmpty) {
      buffer
        ..writeln('')
        ..writeln('Error:')
        ..writeln(errorMessage);
    }

    return buffer.toString().trim();
  }
}

class ChequeService {
  ChequeService({
    http.Client? client,
  }) : _client = client ?? http.Client();

  static const String _endpoint =
      'https://appsy.jbysoft.com/tyrz/cheque/bind.service';

  final http.Client _client;
  ChequeDebugInfo? lastDebugInfo;

  Future<ChequeInfo> fetchCheque(CheckInLocationPreset preset) async {
    final loginInfo = preset.loginInfo;
    final String headerLoginToken = loginInfo.loginToken;
    final String headerChannel = loginInfo.blqd;
    final String headerJgbh =
        loginInfo.zxbm.isNotEmpty ? loginInfo.zxbm : loginInfo.jgbh;
    final String headerZzjgdmz =
        loginInfo.zzjgdmz.isNotEmpty ? loginInfo.zzjgdmz : loginInfo.qycode;
    final Map<String, String> headers = <String, String>{
      'channel': headerChannel,
      'jgbh': headerJgbh,
      'login-token': headerLoginToken,
      'zzbs': headerJgbh,
      'zzjgdmz': headerZzjgdmz,
      'Content-Type': 'application/json',
    };
    final String requestBody = jsonEncode(<String, dynamic>{
      'client_id': 'bbPro',
      'forward_client_id': 'bbPro',
      'timestamp': _buildTimestamp(),
      'userinfo': jsonEncode(_buildUserInfo(preset)),
      'sign': _buildRandomSign(),
      'loginToken': loginInfo.loginToken,
    });

    lastDebugInfo = ChequeDebugInfo(
      requestBody: requestBody,
      requestHeaders: headers,
    );

    final response = await _client
        .post(
          Uri.parse(_endpoint),
          headers: headers,
          body: requestBody,
        )
        .timeout(const Duration(seconds: 10));

    lastDebugInfo = ChequeDebugInfo(
      requestBody: requestBody,
      requestHeaders: headers,
      responseBody: response.body,
      statusCode: response.statusCode,
    );

    if (response.statusCode != 200) {
      throw ChequeServiceException(
        'ticket/cheque 查询失败，HTTP ${response.statusCode}',
      );
    }

    final Map<String, dynamic> payload =
        jsonDecode(response.body) as Map<String, dynamic>;
    if (payload['success'] != true) {
      throw ChequeServiceException(
        (payload['msg'] ?? 'ticket/cheque 查询失败').toString(),
      );
    }

    final String ticket = (payload['ticket'] ?? '').toString();
    final String cheque = (payload['cheque'] ?? ticket).toString();
    if (ticket.isEmpty && cheque.isEmpty) {
      throw const ChequeServiceException('ticket/cheque 响应为空。');
    }

    return ChequeInfo(
      cheque: cheque,
      debugInfo: lastDebugInfo!,
      ticket: ticket,
    );
  }

  Map<String, dynamic> _buildUserInfo(CheckInLocationPreset preset) {
    final loginInfo = preset.loginInfo;
    final Map<String, dynamic> userinfo = Map<String, dynamic>.from(
      _decodeJsonObject(loginInfo.rawUserInfoJson) ?? <String, dynamic>{},
    );

    if (userinfo['zzjgxx'] == null && loginInfo.rawZzjgxxJson.isNotEmpty) {
      userinfo['zzjgxx'] =
          _decodeJsonObject(loginInfo.rawZzjgxxJson) ?? <String, dynamic>{};
    }

    userinfo['gztMoudleMsg'] = <String, dynamic>{
      'ryly': 'xgzt',
      'flag': true,
      'xmmc': null,
      'classifyNumber': '03001',
      'formUrl':
          '/dxgl-app/dxslgl/#/dxslglComp/dxslglfq/index?bm=03060&dx_29_sjdxsl=${loginInfo.userId}',
      'matterClassNumber': '03060:task',
      'xmid': null,
      'dx_29_sjdxsl': loginInfo.userId.toString(),
      'dx_29_dxmc': loginInfo.username,
      'classifyName': null,
      'matterClassName': '打卡',
    };

    userinfo['ptdlzh'] = userinfo['ptdlzh'] ?? loginInfo.account;
    userinfo['client'] =
        userinfo['client'] ?? (loginInfo.client.isNotEmpty ? loginInfo.client : '4');
    userinfo['username'] = userinfo['username'] ?? loginInfo.username;
    userinfo['xingming'] = userinfo['xingming'] ?? loginInfo.username;
    userinfo['loginToken'] = userinfo['loginToken'] ?? loginInfo.loginToken;
    userinfo['blqd'] = userinfo['blqd'] ?? loginInfo.blqd;
    userinfo['userid'] = userinfo['userid'] ?? loginInfo.userId;
    userinfo['grbh'] = userinfo['grbh'] ?? loginInfo.grbh;
    userinfo['zjhm'] = userinfo['zjhm'] ?? loginInfo.idCard;
    userinfo['qycode'] = userinfo['qycode'] ?? loginInfo.qycode;
    userinfo['zxbm'] = userinfo['zxbm'] ?? loginInfo.zxbm;
    userinfo['jgbm'] = userinfo['jgbm'] ?? loginInfo.jgbm;
    userinfo['bmbh'] = userinfo['bmbh'] ?? loginInfo.jgbm;
    userinfo['f_dept_id'] = userinfo['f_dept_id'] ?? loginInfo.jgbm;
    userinfo['deptid'] = userinfo['deptid'] ?? loginInfo.jgbm;

    userinfo['locationMsg'] = jsonEncode(preset.toBridgePayload());
    return userinfo;
  }

  Map<String, dynamic>? _decodeJsonObject(String value) {
    if (value.isEmpty) {
      return null;
    }

    final Object decoded = jsonDecode(value);
    if (decoded is Map<String, dynamic>) {
      return decoded;
    }

    if (decoded is Map) {
      return decoded.map(
        (key, dynamic mapValue) => MapEntry(key.toString(), mapValue),
      );
    }

    return null;
  }

  String _buildTimestamp() {
    final now = DateTime.now();
    String twoDigits(int value) => value.toString().padLeft(2, '0');
    return '${now.year}'
        '${twoDigits(now.month)}'
        '${twoDigits(now.day)}'
        '${twoDigits(now.hour)}'
        '${twoDigits(now.minute)}'
        '${twoDigits(now.second)}';
  }

  String _buildRandomSign() {
    const String chars = '0123456789abcdef';
    final Random random = Random.secure();
    return List<String>.generate(
      32,
      (_) => chars[random.nextInt(chars.length)],
    ).join();
  }

  void dispose() {
    _client.close();
  }
}

class ChequeServiceException implements Exception {
  const ChequeServiceException(this.message);

  final String message;

  @override
  String toString() => 'ChequeServiceException: $message';
}
