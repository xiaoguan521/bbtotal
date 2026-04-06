import 'dart:async';
import 'dart:convert';

import 'package:http/http.dart' as http;

import '../models/check_in_history_location.dart';
import '../models/user_login_info.dart';

class CheckInHistoryService {
  CheckInHistoryService({
    http.Client? client,
  }) : _client = client ?? http.Client();

  static const String _endpoint =
      'https://appsy.jbysoft.com/V2/TBGL/business/common/Instance2\$m=query.service';

  final http.Client _client;

  Future<List<CheckInHistoryLocation>> fetchRecentLocations(
    UserLoginInfo loginInfo,
  ) async {
    final String orgNumber =
        loginInfo.zxbm.isNotEmpty ? loginInfo.zxbm : loginInfo.jgbh;
    final response = await _client
        .post(
          Uri.parse(_endpoint),
          headers: <String, String>{
            'jgbh': orgNumber,
            'login-token': loginInfo.loginToken,
            'zzbs': orgNumber,
            'blqd': loginInfo.blqd,
            'channel': loginInfo.blqd,
            'Content-Type': 'application/json',
          },
          body: jsonEncode(_buildRequestBody(loginInfo, orgNumber)),
        )
        .timeout(const Duration(seconds: 10));

    if (response.statusCode != 200) {
      throw CheckInHistoryServiceException(
        '历史打卡位置查询失败，HTTP ${response.statusCode}',
      );
    }

    final Map<String, dynamic> payload =
        jsonDecode(response.body) as Map<String, dynamic>;
    if (payload['success'] != true) {
      throw CheckInHistoryServiceException(
        (payload['msg'] ?? '历史打卡位置查询失败').toString(),
      );
    }

    final List<dynamic> results =
        (payload['results'] as List<dynamic>?) ?? <dynamic>[];
    final List<CheckInHistoryLocation> allLocations = results
        .whereType<Map<String, dynamic>>()
        .map(CheckInHistoryLocation.fromJson)
        .where((CheckInHistoryLocation item) => item.hasUsableLocation)
        .toList()
      ..sort((a, b) {
        final DateTime? left = a.timestamp;
        final DateTime? right = b.timestamp;
        if (left == null && right == null) {
          return 0;
        }
        if (left == null) {
          return 1;
        }
        if (right == null) {
          return -1;
        }
        return right.compareTo(left);
      });

    final Set<String> seen = <String>{};
    final List<CheckInHistoryLocation> recentUniqueLocations =
        <CheckInHistoryLocation>[];
    for (final CheckInHistoryLocation item in allLocations) {
      if (seen.add(item.uniqueKey)) {
        recentUniqueLocations.add(item);
      }
      if (recentUniqueLocations.length == 5) {
        break;
      }
    }

    return recentUniqueLocations;
  }

  Map<String, dynamic> _buildRequestBody(
    UserLoginInfo loginInfo,
    String orgNumber,
  ) {
    return <String, dynamic>{
      'page': 1,
      'size': 20,
      'syObjectNumber': '29',
      'versionNum': '2.0',
      'isQueryGcData': '',
      'syObjectClass': '01',
      'queryType': '2',
      'isLsDx': '0',
      'dxslbh': ' ',
      'dateParams': '{"dx_29_shijian":"timestamp_date"}',
      'queryCriteria': <Map<String, dynamic>>[
        <String, dynamic>{
          'associatedObjectNumber': ' ',
          'fieldIdentification': 'dx_29_shijian',
          'syobjectfrom': ' ',
          'dataSource': 1,
          'syobjectnumber': '29',
          'value': _buildDateRange(),
          'valueType': 'yyyy-mm-dd',
          'dxkey': 'dx_29_shijian',
          'isShow': true,
        },
        <String, dynamic>{
          'associatedObjectNumber': '03',
          'dataSource': 1,
          'dxkey': 'dx_29_sjdxsl',
          'fieldIdentification': 'dx_29_sjdxsl',
          'subType': '',
          'syobjectfrom': ' ',
          'syobjectnumber': '29',
          'value': loginInfo.userId.toString(),
          'valueType': 'varchar',
          'isShow': true,
        },
      ],
      'queryContent': <Map<String, dynamic>>[
        <String, dynamic>{
          'fieldIdentification': 'dx_29_dkzb',
          'fieldIdentificationname': 'dx_29_dkzb',
          'syobjectfrom': ' ',
          'dataSource': 1,
          'syobjectnumber': '29',
          'associatedObjectNumber': ' ',
          'isKzShuxing': '0',
          'groupType': 'max',
          'secretScope': '',
          'qff': 0,
          'precision': ' ',
        },
        <String, dynamic>{
          'fieldIdentification': 'dx_29_dkwz',
          'fieldIdentificationname': 'dx_29_dkwz',
          'syobjectfrom': ' ',
          'dataSource': 1,
          'syobjectnumber': '29',
          'associatedObjectNumber': ' ',
          'isKzShuxing': '0',
          'groupType': 'max',
          'secretScope': '',
          'qff': 0,
          'precision': ' ',
        },
        <String, dynamic>{
          'fieldIdentification': 'dx_29_sjbz',
          'fieldIdentificationname': 'dx_29_sjbz',
          'syobjectfrom': ' ',
          'dataSource': 1,
          'syobjectnumber': '29',
          'associatedObjectNumber': ' ',
          'isKzShuxing': '0',
          'groupType': 'max',
          'secretScope': '',
          'qff': 0,
          'precision': ' ',
        },
        <String, dynamic>{
          'fieldIdentification': 'dx_29_id',
          'fieldIdentificationname': 'dx_29_id',
          'syobjectfrom': '',
          'syobjectnumber': '29',
          'associatedObjectNumber': '',
          'groupType': ' ',
          'isKzShuxing': '0',
          'sortType': ' ',
        },
        <String, dynamic>{
          'fieldIdentification': 'dx_29_dxbh',
          'fieldIdentificationname': 'dx_29_dxbh',
          'syobjectfrom': '',
          'syobjectnumber': '29',
          'associatedObjectNumber': '',
          'groupType': ' ',
          'isKzShuxing': '0',
          'sortType': ' ',
        },
        <String, dynamic>{
          'fieldIdentification': 'dx_29_shijian',
          'fieldIdentificationname': 'dx_29_shijian',
          'syobjectfrom': ' ',
          'dataSource': 1,
          'syobjectnumber': '29',
          'associatedObjectNumber': ' ',
          'isKzShuxing': '0',
          'groupType': 'max',
          'secretScope': '',
          'qff': 0,
          'precision': ' ',
          'sortPriority': 1,
          'sortType': 'desc',
        },
      ],
      'gcDataDxbh': '',
      'sjzsbbid': 1000713725971,
      'zsxDxbh': '29',
      'subDataShowType': 'below',
      'organizationNumber': orgNumber,
      'userid': loginInfo.userId,
      'temporaryStorage': '2',
      'fixedCriteria': <dynamic>[],
      'queryby': '【打卡记录清册（工作台）】【1000713725971】【清册查询】',
      'querybyType': '3',
      'himhconfidentiality': '1',
      'noteSql': ' and dxsl.dx_29_sjdxsl in(${loginInfo.userId}) ',
      'mergeAttribute': <dynamic>[],
      'totalSummary': <dynamic>[],
    };
  }

  String _buildDateRange() {
    final DateTime now = DateTime.now();
    final DateTime startOfToday = DateTime(now.year, now.month, now.day);
    final DateTime start = startOfToday.subtract(const Duration(days: 4));
    String format(DateTime value) {
      String twoDigits(int number) => number.toString().padLeft(2, '0');
      return '${value.year}-${twoDigits(value.month)}-${twoDigits(value.day)}';
    }

    return '${format(start)},${format(now)}';
  }

  void dispose() {
    _client.close();
  }
}

class CheckInHistoryServiceException implements Exception {
  const CheckInHistoryServiceException(this.message);

  final String message;

  @override
  String toString() => 'CheckInHistoryServiceException: $message';
}
