import 'package:bbtotal/models/check_in_location_preset.dart';
import 'package:bbtotal/models/hybrid_runtime_context.dart';
import 'package:bbtotal/models/user_login_info.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
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
    ticket: 'ticket-1',
  );

  final CheckInLocationPreset preset = CheckInLocationPreset(
    address: '河北省石家庄市鹿泉区御园路71号靠近光谷科技园',
    latitude: 38.0,
    longitude: 114.0,
    loginInfo: loginInfo,
    deviceIdentifier: 'device-123',
    cheque: 'cheque-123',
    ticket: 'ticket-123',
  );

  test('launch payload is propagated into storage seed and runtime map', () {
    final HybridRuntimeContext context = HybridRuntimeContext.fromInputs(
      baseUrl: 'https://appsy.jbysoft.com/example/#/page/index',
      loginInfo: loginInfo,
      preset: preset,
      launchPayload: const <String, dynamic>{
        'push': '1',
        'pushURL': 'https://appsy.jbysoft.com/next',
        'hidden': '1',
      },
    );

    expect(context.storageSeed['params2'], isNotEmpty);
    expect(context.storageSeed['params'], isNotEmpty);
    expect(context.launchPayload['push'], '1');
    expect(context.pageParams['deviceuuid'], 'device-123');
    expect(context.pageParams['dx_29_sbsbm'], 'device-123');
    expect(context.bbgrxx['deviceuuid'], 'device-123');
    expect(context.bbgrxx['dx_29_sbsbm'], 'device-123');
    expect(context.bbgrxx['blqd'], 'app_02');
    expect(context.userInfoObject['deviceuuid'], 'device-123');
    expect(context.userInfoObject['locationMsg'], contains('"errorCode":0'));
    expect(context.userInfoObject['client'], '4');
    expect(
      context.toScriptRuntime()['launchPayload'],
      isA<Map<String, dynamic>>(),
    );
    expect(
      context.toScriptRuntime()['launchPayloadJson'],
      contains('"push":"1"'),
    );
    expect(
      context.toScriptRuntime()['pageParamsJson'],
      contains('"deviceuuid":"device-123"'),
    );
    expect(
      context.toScriptRuntime()['bbgrxxJson'],
      contains('"deviceuuid":"device-123"'),
    );
  });

  test(
    'approval launch payload is exposed to page storage without becoming cookies',
    () {
      final HybridRuntimeContext context = HybridRuntimeContext.fromInputs(
        baseUrl: 'https://appsy.jbysoft.com/example/#/page/index',
        loginInfo: loginInfo,
        preset: preset,
        launchPayload: const <String, dynamic>{
          'bpmid': '1012789848258',
          'taskid': 'd98fca19-22b5-11f1-9a69-faaf32788154',
          'taskId': 'd98fca19-22b5-11f1-9a69-faaf32788154',
          'flowtype': 'db',
          'taskDefinitionKey': 'a8137b2f6314b6',
          'processInstanceId': '7d17cceb-2291-11f1-9a69-faaf32788154',
        },
      );

      expect(context.storageSeed.containsKey('taskid'), isFalse);
      expect(
        context.pageStorageSeed['taskid'],
        'd98fca19-22b5-11f1-9a69-faaf32788154',
      );
      expect(
        context.pageStorageSeed['taskId'],
        'd98fca19-22b5-11f1-9a69-faaf32788154',
      );
      expect(context.pageStorageSeed['flowtype'], 'db');
      expect(
        context.toScriptRuntime()['pageStorageSeed'],
        isA<Map<String, String>>(),
      );
    },
  );
}
