import 'package:bbtotal/models/check_in_location_preset.dart';
import 'package:bbtotal/models/hybrid_runtime_context.dart';
import 'package:bbtotal/models/user_login_info.dart';
import 'package:bbtotal/services/hybrid_bridge_service.dart';
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

  test('document start script does not contain malformed duplicate catch', () {
    final HybridRuntimeContext context = HybridRuntimeContext.fromInputs(
      baseUrl: 'https://appsy.jbysoft.com/example/#/page/index',
      loginInfo: loginInfo,
      preset: preset,
    );

    const HybridBridgeService service = HybridBridgeService();
    final String source = service.buildInitialUserScripts(context).first.source;

    expect(
      source,
      contains('window.bbgrxx = normalizeDeviceFields(runtime.bbgrxx || {});'),
    );
    expect(
      source,
      isNot(
        contains(
          'window.bbgrxx = normalizeDeviceFields(runtime.bbgrxx || {});\n  } catch (_) {}',
        ),
      ),
    );
    expect(source, contains('syncBbgrxxFromUserinfo(envelope.userinfo);'));
    expect(
      source,
      contains(
        'window.__bbtotalSyncBbgrxxFromUserinfo = syncBbgrxxFromUserinfo;',
      ),
    );
    expect(source, contains('mobileUtilsPatchAttempts >= 120'));
    expect(
      source,
      contains('normalized.locationMsg = normalizeLocationPayload'),
    );
    expect(
      source,
      contains('normalized.updatingLocationMsg = normalizeLocationPayload'),
    );
    expect(
      source,
      contains(
        'window.I = Object.assign({}, window.I || {}, bridge, compatBridge);',
      ),
    );
    expect(
      source,
      contains(
        'window.iapp = Object.assign({}, window.iapp || {}, iappCompat);',
      ),
    );
    expect(source, contains("simei: createSyncBridgeMethod('I.simei'"));
    expect(source, contains("fn2: createSyncBridgeMethod('iapp.fn2'"));
    expect(source, contains('decodeURIComponent(raw)'));
    expect(source, contains("headerKey === 'login-token'"));
    expect(source, contains("headerKey === 'tylogintoken'"));
  });

  test(
    'document start scripts include remote bootstrap override when provided',
    () {
      final HybridRuntimeContext context = HybridRuntimeContext.fromInputs(
        baseUrl: 'https://appsy.jbysoft.com/example/#/page/index',
        loginInfo: loginInfo,
        preset: preset,
      );

      const HybridBridgeService service = HybridBridgeService();
      final scripts = service.buildInitialUserScripts(
        context,
        extraBootstrapScript: 'window.__remoteBridgeLoaded = true;',
      );

      expect(scripts, hasLength(2));
      expect(
        scripts.first.source,
        contains('window.__bbtotalRuntime = runtime;'),
      );
      expect(
        scripts.last.source,
        contains('window.__remoteBridgeLoaded = true;'),
      );
    },
  );
}
