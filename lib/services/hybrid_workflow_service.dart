import 'dart:convert';

import '../models/user_login_info.dart';

class HybridWorkflowService {
  const HybridWorkflowService();

  static const String initiateBaseUrl =
      'https://appsy.jbysoft.com/dxgl-app/dxslgl/#/dxslglComp/dxslglfq/index';
  static const String approvalBaseUrl =
      'https://appsy.jbysoft.com/dxgl-app/dxslgl/#/dxslglComp/dxslglsp/index';
  static const String initiateZtConfig =
      '7171a46ae52cd9c7c0278a72c65492b0fd234129620126cd57ac2fccaaec061b0abed44bcb02786c4057648caf5b99b6c4e7488567e1d5fa7399b6cf5fdeb7ea09b27db192ee5320e008c8c888a156ab41012aa0eea91e2a034cd260ee71bc6e6c20b42e49633af74bfc77a9e1142c02c45548a3e3e89cc6a93eda4ca371a379';
  static const String approvalZtConfig =
      '7171a46ae52cd9c7c0278a72c65492b0fd234129620126cd57ac2fccaaec061b0abed44bcb02786c4057648caf5b99b6a47cd85e2d21008f7399b6cf5fdeb7ea09b27db192ee5320e008c8c888a156ab41012aa0eea91e2a034cd260ee71bc6e6c20b42e49633af74bfc77a9e1142c02c45548a3e3e89cc6a93eda4ca371a379';
  static const List<int> pendingQueryOrder = <int>[0, 1];

  List<Map<String, dynamic>> extractTodoItems(Map<String, dynamic> payload) {
    final Object? results = payload['results'];
    if (results is! List) {
      return <Map<String, dynamic>>[];
    }

    return results
        .whereType<Map>()
        .map(
          (Map item) => item.map<String, dynamic>(
            (dynamic key, dynamic value) => MapEntry(key.toString(), value),
          ),
        )
        .toList();
  }

  Map<String, dynamic>? pickCheckInTodo(List<Map<String, dynamic>> items) {
    for (final Map<String, dynamic> item in items) {
      if (_readNestedString(item, 'ggxx', 'zdyRwmc').contains('打卡')) {
        return item;
      }
    }
    return null;
  }

  String buildInitiatePageUrl(UserLoginInfo loginInfo) {
    final String orgNumber = _resolveWorkflowOrgNumber(loginInfo);
    return _buildHashRouteUrl(initiateBaseUrl, <String, String>{
      'bm': '03060',
      'ticket': 'nothing',
      'cpbs': 'bbPro',
      'ztConfig': initiateZtConfig,
      'cysxFlag': 'false',
      'dx_29_sjdxsl': loginInfo.userId.toString(),
      'qycode': loginInfo.qycode,
      'zzjgdmz': loginInfo.zzjgdmz,
      'jgbm': orgNumber,
      'zxbm': orgNumber,
      'khbh': loginInfo.grbh,
      'userid': loginInfo.userId.toString(),
      'zjhm': loginInfo.idCard,
    });
  }

  String buildApprovalPageUrl({
    required UserLoginInfo loginInfo,
    required Map<String, dynamic> todo,
    required String cheque,
  }) {
    final Map<String, dynamic> launchPayload = buildApprovalLaunchPayload(
      todo: todo,
    );
    final String orgNumber = _resolveWorkflowOrgNumber(loginInfo);
    final String flowtype = _readLaunchPayloadString(launchPayload, 'flowtype');
    final String processDefinitionKey = _readLaunchPayloadString(
      launchPayload,
      'processDefinitionKey',
    );
    final String processKey = _readLaunchPayloadString(
      launchPayload,
      'processKey',
    );
    final Map<String, String> queryParameters = <String, String>{
      'businessKey': _readLaunchPayloadString(launchPayload, 'businessKey'),
      'bpmid': _readLaunchPayloadString(launchPayload, 'bpmid'),
      'newdaiban': 'db',
      'ticket': 'nothing',
      'qycode': loginInfo.qycode,
      'ztConfig': approvalZtConfig,
      'flowable': flowtype,
      'flowtype': flowtype,
      'processDefinitionKey': processDefinitionKey.isNotEmpty
          ? processDefinitionKey
          : processKey,
      'taskName': _readLaunchPayloadString(launchPayload, 'taskName'),
      'taskid': _readLaunchPayloadString(launchPayload, 'taskid'),
      'nodeType': 'check',
      'cheque': cheque,
      'zxbm': orgNumber,
      'jgbm': orgNumber,
      'khbh': loginInfo.grbh,
      'userid': loginInfo.userId.toString(),
      'zjhm': loginInfo.idCard,
      'zzjgdmz': loginInfo.zzjgdmz,
      'openTab': '*',
      'tyLoginToken': loginInfo.loginToken,
      'cpbs': 'gjj',
    };

    final String processInstanceId = _readLaunchPayloadString(
      launchPayload,
      'processInstanceId',
    );
    final String taskDefinitionKey = _readLaunchPayloadString(
      launchPayload,
      'taskDefinitionKey',
    );
    final String processDefinitionId = _readLaunchPayloadString(
      launchPayload,
      'processDefinitionId',
    );

    if (processInstanceId.isNotEmpty) {
      queryParameters['processInstanceId'] = processInstanceId;
    }
    if (taskDefinitionKey.isNotEmpty) {
      queryParameters['taskDefinitionKey'] = taskDefinitionKey;
    }
    if (processDefinitionId.isNotEmpty) {
      queryParameters['processDefinitionId'] = processDefinitionId;
    }

    return _buildHashRouteUrl(approvalBaseUrl, queryParameters);
  }

  Map<String, dynamic> buildApprovalLaunchPayload({
    required Map<String, dynamic> todo,
  }) {
    final String bpmid = _readTodoString(
      todo,
      keys: const <String>['bpmid'],
      sections: const <String>['ggxx', 'bpmxq'],
    );
    final String businessKey = _readTodoString(
      todo,
      keys: const <String>['businessKey'],
      sections: const <String>['bpmxq', 'ggxx'],
    );
    final String processDefinitionKey = _readTodoString(
      todo,
      keys: const <String>['processDefinitionKey', 'processKey'],
      sections: const <String>['bpmxq', 'ggxx'],
    );
    final String processKey = _readTodoString(
      todo,
      keys: const <String>['processKey', 'processDefinitionKey'],
      sections: const <String>['bpmxq', 'ggxx'],
    );
    final String taskId = _readTodoString(
      todo,
      keys: const <String>['taskId', 'taskid'],
      sections: const <String>['bpmxq', 'ggxx'],
    );
    final String taskName = _readTodoString(
      todo,
      keys: const <String>['taskName'],
      sections: const <String>['bpmxq', 'ggxx'],
    );
    final String taskDefinitionKey = _readTodoString(
      todo,
      keys: const <String>['taskDefinitionKey'],
      sections: const <String>['bpmxq', 'ggxx'],
    );
    final String processInstanceId = _readTodoString(
      todo,
      keys: const <String>['processInstanceId'],
      sections: const <String>['bpmxq', 'ggxx'],
    );
    final String processDefinitionId = _readTodoString(
      todo,
      keys: const <String>['processDefinitionId'],
      sections: const <String>['bpmxq', 'ggxx'],
    );

    return <String, dynamic>{
      if (bpmid.isNotEmpty) 'bpmid': bpmid,
      if (businessKey.isNotEmpty) 'businessKey': businessKey,
      if (processKey.isNotEmpty) 'processKey': processKey,
      if (processDefinitionKey.isNotEmpty)
        'processDefinitionKey': processDefinitionKey,
      'bpmparam': jsonEncode(todo),
      if (taskId.isNotEmpty) 'taskid': taskId,
      if (taskId.isNotEmpty) 'taskId': taskId,
      'flowtype': 'db',
      'newdaiban': 'db',
      'nodeType': 'check',
      if (taskDefinitionKey.isNotEmpty) 'taskDefinitionKey': taskDefinitionKey,
      if (processInstanceId.isNotEmpty) 'processInstanceId': processInstanceId,
      if (processDefinitionId.isNotEmpty)
        'processDefinitionId': processDefinitionId,
      if (taskName.isNotEmpty) 'taskName': taskName,
    };
  }

  String _readNestedString(
    Map<String, dynamic> payload,
    String section,
    String key,
  ) {
    final Object? group = payload[section];
    if (group is Map<String, dynamic>) {
      return (group[key] ?? '').toString();
    }
    if (group is Map) {
      return (group[key] ?? '').toString();
    }
    return '';
  }

  String _readTodoString(
    Map<String, dynamic> todo, {
    required List<String> keys,
    required List<String> sections,
  }) {
    for (final String section in sections) {
      for (final String key in keys) {
        final String value = _readNestedString(todo, section, key);
        if (value.isNotEmpty) {
          return value;
        }
      }
    }

    for (final String key in keys) {
      final String value = (todo[key] ?? '').toString();
      if (value.isNotEmpty) {
        return value;
      }
    }

    return '';
  }

  String _readLaunchPayloadString(Map<String, dynamic> payload, String key) {
    return (payload[key] ?? '').toString();
  }

  String _resolveWorkflowOrgNumber(UserLoginInfo loginInfo) {
    if (loginInfo.zxbm.isNotEmpty) {
      return loginInfo.zxbm;
    }
    if (loginInfo.jgbh.isNotEmpty) {
      return loginInfo.jgbh;
    }
    return loginInfo.jgbm;
  }

  String _buildHashRouteUrl(
    String baseUrl,
    Map<String, String> queryParameters,
  ) {
    final String query = queryParameters.entries
        .where((MapEntry<String, String> entry) => entry.value.isNotEmpty)
        .map(
          (MapEntry<String, String> entry) =>
              '${Uri.encodeQueryComponent(entry.key)}='
              '${Uri.encodeQueryComponent(entry.value)}',
        )
        .join('&');

    return query.isEmpty ? baseUrl : '$baseUrl?$query';
  }
}
