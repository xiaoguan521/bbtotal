import 'package:bbtotal/models/user_login_info.dart';
import 'package:bbtotal/services/hybrid_workflow_service.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  const HybridWorkflowService workflowService = HybridWorkflowService();

  const UserLoginInfo loginInfo = UserLoginInfo(
    account: 'liu',
    username: '刘晓晨',
    userId: 3517,
    grbh: '01000000009000026129',
    idCard: '130528198704157217',
    loginToken: 'token:app_02',
    blqd: 'app_02',
    jgbh: '1301100001',
    jgbm: '130110000102a705',
    zxbm: '1301100001',
    qycode: 'shineyue',
    zzjgdmz: 'shineyue',
  );

  test('pickCheckInTodo returns item whose zdyRwmc contains 打卡', () {
    final List<Map<String, dynamic>> items = <Map<String, dynamic>>[
      <String, dynamic>{
        'ggxx': <String, dynamic>{'zdyRwmc': '刘晓晨提交的需求填报'},
      },
      <String, dynamic>{
        'ggxx': <String, dynamic>{'zdyRwmc': '刘晓晨提交的打卡'},
      },
    ];

    final Map<String, dynamic>? matched = workflowService.pickCheckInTodo(
      items,
    );

    expect(matched, isNotNull);
    expect(
      (matched!['ggxx'] as Map<String, dynamic>)['zdyRwmc'],
      contains('打卡'),
    );
  });

  test('buildInitiatePageUrl carries fixed workflow params', () {
    final Uri uri = Uri.parse(workflowService.buildInitiatePageUrl(loginInfo));
    final Uri fragmentUri = Uri.parse('https://placeholder${uri.fragment}');

    expect(fragmentUri.path, '/dxslglComp/dxslglfq/index');
    expect(fragmentUri.queryParameters['bm'], '03060');
    expect(fragmentUri.queryParameters['cpbs'], 'bbPro');
    expect(fragmentUri.queryParameters['userid'], '3517');
    expect(fragmentUri.queryParameters['khbh'], '01000000009000026129');
    expect(fragmentUri.queryParameters['jgbm'], '1301100001');
  });

  test('buildApprovalPageUrl carries process identifiers when available', () {
    final Map<String, dynamic> todo = <String, dynamic>{
      'ggxx': <String, dynamic>{'bpmid': '1012789848258'},
      'bpmxq': <String, dynamic>{
        'businessKey': '1012789848258',
        'processKey': 'k_1684813228059',
        'taskName': '审批',
        'taskId': 'd98fca19-22b5-11f1-9a69-faaf32788154',
        'processInstanceId': '7d17cceb-2291-11f1-9a69-faaf32788154',
        'taskDefinitionKey': 'a8137b2f6314b6',
        'processDefinitionId':
            'k_1684813228059:17:e6c9a1a0-5c9d-11f0-ab1b-6ea4c76c13d7',
      },
    };

    final Uri uri = Uri.parse(
      workflowService.buildApprovalPageUrl(
        loginInfo: loginInfo,
        todo: todo,
        cheque: 'cheque-123',
      ),
    );
    final Uri fragmentUri = Uri.parse('https://placeholder${uri.fragment}');

    expect(fragmentUri.path, '/dxslglComp/dxslglsp/index');
    expect(fragmentUri.queryParameters['businessKey'], '1012789848258');
    expect(fragmentUri.queryParameters['bpmid'], '1012789848258');
    expect(fragmentUri.queryParameters['jgbm'], '1301100001');
    expect(
      fragmentUri.queryParameters['processInstanceId'],
      '7d17cceb-2291-11f1-9a69-faaf32788154',
    );
    expect(fragmentUri.queryParameters['taskDefinitionKey'], 'a8137b2f6314b6');
    expect(
      fragmentUri.queryParameters['processDefinitionId'],
      'k_1684813228059:17:e6c9a1a0-5c9d-11f0-ab1b-6ea4c76c13d7',
    );
  });

  test('buildApprovalLaunchPayload mirrors task context for params2', () {
    final Map<String, dynamic> todo = <String, dynamic>{
      'ggxx': <String, dynamic>{'bpmid': '1012789848258'},
      'bpmxq': <String, dynamic>{
        'businessKey': '1012789848258',
        'processKey': 'k_1684813228059',
        'taskName': '审批',
        'taskId': 'd98fca19-22b5-11f1-9a69-faaf32788154',
        'processInstanceId': '7d17cceb-2291-11f1-9a69-faaf32788154',
        'taskDefinitionKey': 'a8137b2f6314b6',
        'processDefinitionId':
            'k_1684813228059:17:e6c9a1a0-5c9d-11f0-ab1b-6ea4c76c13d7',
      },
    };

    final Map<String, dynamic> launchPayload = workflowService
        .buildApprovalLaunchPayload(todo: todo);

    expect(launchPayload['bpmid'], '1012789848258');
    expect(launchPayload['businessKey'], '1012789848258');
    expect(launchPayload['processKey'], 'k_1684813228059');
    expect(launchPayload['bpmparam'], contains('"taskId"'));
    expect(launchPayload['taskid'], 'd98fca19-22b5-11f1-9a69-faaf32788154');
    expect(launchPayload['taskId'], 'd98fca19-22b5-11f1-9a69-faaf32788154');
    expect(launchPayload['taskDefinitionKey'], 'a8137b2f6314b6');
    expect(
      launchPayload['processInstanceId'],
      '7d17cceb-2291-11f1-9a69-faaf32788154',
    );
    expect(
      launchPayload['processDefinitionId'],
      'k_1684813228059:17:e6c9a1a0-5c9d-11f0-ab1b-6ea4c76c13d7',
    );
    expect(launchPayload['taskName'], '审批');
    expect(launchPayload['flowtype'], 'db');
    expect(launchPayload['newdaiban'], 'db');
    expect(launchPayload['nodeType'], 'check');
    expect(launchPayload['processDefinitionKey'], 'k_1684813228059');
  });

  test('buildApprovalLaunchPayload falls back to root and ggxx fields', () {
    final Map<String, dynamic> todo = <String, dynamic>{
      'bpmid': '1012867450330',
      'businessKey': '1012867450330',
      'processDefinitionKey': 'k_1734318719996',
      'taskid': 'bd836987-37aa-11f1-bb80-3e64db595ffa',
      'taskName': '推送发起人',
      'processInstanceId': 'bd82a635-37aa-11f1-bb80-3e64db595ffa',
      'processDefinitionId': 'k_1734318719996:6:1781bb04-2d99-11f1-b68e-6e03c0170726',
      'ggxx': <String, dynamic>{
        'taskDefinitionKey': 'a407480927d991',
      },
    };

    final Map<String, dynamic> launchPayload = workflowService
        .buildApprovalLaunchPayload(todo: todo);

    expect(launchPayload['bpmid'], '1012867450330');
    expect(launchPayload['businessKey'], '1012867450330');
    expect(launchPayload['processKey'], 'k_1734318719996');
    expect(launchPayload['processDefinitionKey'], 'k_1734318719996');
    expect(launchPayload['taskid'], 'bd836987-37aa-11f1-bb80-3e64db595ffa');
    expect(launchPayload['taskDefinitionKey'], 'a407480927d991');
    expect(
      launchPayload['processInstanceId'],
      'bd82a635-37aa-11f1-bb80-3e64db595ffa',
    );
    expect(
      launchPayload['processDefinitionId'],
      'k_1734318719996:6:1781bb04-2d99-11f1-b68e-6e03c0170726',
    );
  });

  test('buildApprovalPageUrl keeps fallback identifiers in query', () {
    final Map<String, dynamic> todo = <String, dynamic>{
      'ggxx': <String, dynamic>{
        'bpmid': '1012867450330',
        'taskDefinitionKey': 'a407480927d991',
      },
      'businessKey': '1012867450330',
      'processDefinitionKey': 'k_1734318719996',
      'taskid': 'bd836987-37aa-11f1-bb80-3e64db595ffa',
      'taskName': '推送发起人',
      'processInstanceId': 'bd82a635-37aa-11f1-bb80-3e64db595ffa',
      'processDefinitionId': 'k_1734318719996:6:1781bb04-2d99-11f1-b68e-6e03c0170726',
    };

    final Uri uri = Uri.parse(
      workflowService.buildApprovalPageUrl(
        loginInfo: loginInfo,
        todo: todo,
        cheque: 'cheque-123',
      ),
    );
    final Uri fragmentUri = Uri.parse('https://placeholder${uri.fragment}');

    expect(fragmentUri.queryParameters['jgbm'], '1301100001');
    expect(fragmentUri.queryParameters['processDefinitionKey'], 'k_1734318719996');
    expect(
      fragmentUri.queryParameters['processInstanceId'],
      'bd82a635-37aa-11f1-bb80-3e64db595ffa',
    );
    expect(fragmentUri.queryParameters['taskDefinitionKey'], 'a407480927d991');
    expect(
      fragmentUri.queryParameters['processDefinitionId'],
      'k_1734318719996:6:1781bb04-2d99-11f1-b68e-6e03c0170726',
    );
  });
}
