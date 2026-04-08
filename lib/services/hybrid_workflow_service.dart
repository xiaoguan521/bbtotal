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
    final String orgNumber =
        loginInfo.zxbm.isNotEmpty ? loginInfo.zxbm : loginInfo.jgbh;
    return _buildHashRouteUrl(
      initiateBaseUrl,
      <String, String>{
        'bm': '03060',
        'ticket': 'nothing',
        'cpbs': 'bbPro',
        'ztConfig': initiateZtConfig,
        'cysxFlag': 'false',
        'dx_29_sjdxsl': loginInfo.userId.toString(),
        'qycode': loginInfo.qycode,
        'zzjgdmz': loginInfo.zzjgdmz,
        'jgbm': loginInfo.jgbm,
        'zxbm': orgNumber,
        'khbh': loginInfo.grbh,
        'userid': loginInfo.userId.toString(),
        'zjhm': loginInfo.idCard,
      },
    );
  }

  String buildApprovalPageUrl({
    required UserLoginInfo loginInfo,
    required Map<String, dynamic> todo,
    required String cheque,
  }) {
    final String orgNumber =
        loginInfo.zxbm.isNotEmpty ? loginInfo.zxbm : loginInfo.jgbh;
    final Map<String, String> queryParameters = <String, String>{
      'businessKey': _readNestedString(todo, 'bpmxq', 'businessKey'),
      'bpmid': _readNestedString(todo, 'ggxx', 'bpmid'),
      'newdaiban': 'db',
      'ticket': 'nothing',
      'qycode': loginInfo.qycode,
      'ztConfig': approvalZtConfig,
      'flowable': 'db',
      'flowtype': 'db',
      'processDefinitionKey': _readNestedString(todo, 'bpmxq', 'processKey'),
      'taskName': _readNestedString(todo, 'bpmxq', 'taskName'),
      'taskid': _readNestedString(todo, 'bpmxq', 'taskId'),
      'nodeType': 'check',
      'cheque': cheque,
      'zxbm': orgNumber,
      'jgbm': loginInfo.jgbm,
      'khbh': loginInfo.grbh,
      'userid': loginInfo.userId.toString(),
      'zjhm': loginInfo.idCard,
      'zzjgdmz': loginInfo.zzjgdmz,
      'openTab': '*',
      'tyLoginToken': loginInfo.loginToken,
      'cpbs': 'gjj',
    };

    final String processInstanceId = _readNestedString(
      todo,
      'bpmxq',
      'processInstanceId',
    );
    final String taskDefinitionKey = _readNestedString(
      todo,
      'bpmxq',
      'taskDefinitionKey',
    );
    final String processDefinitionId = _readNestedString(
      todo,
      'bpmxq',
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
