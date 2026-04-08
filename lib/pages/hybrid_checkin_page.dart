import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_inappwebview/flutter_inappwebview.dart';

import '../models/check_in_history_location.dart';
import '../models/check_in_location_preset.dart';
import '../models/hybrid_runtime_context.dart';
import '../models/user_login_info.dart';
import '../services/check_in_history_service.dart';
import '../services/cheque_service.dart';
import '../services/hybrid_bridge_service.dart';
import '../services/hybrid_diagnostics_service.dart';
import '../services/location_service.dart';
import '../services/pending_todo_service.dart';
import '../services/user_login_info_service.dart';

class HybridCheckInPage extends StatefulWidget {
  const HybridCheckInPage({super.key});

  @override
  State<HybridCheckInPage> createState() => _HybridCheckInPageState();
}

class _HybridCheckInPageState extends State<HybridCheckInPage> {
  late final TextEditingController _usernameController = TextEditingController();
  late final TextEditingController _addressController =
      TextEditingController(text: '河北省石家庄市鹿泉区御园路71号靠近光谷科技园');
  late final TextEditingController _longitudeController =
      TextEditingController(text: '114.347315');
  late final TextEditingController _latitudeController =
      TextEditingController(text: '38.050979');

  final LocationService _locationService = LocationService();
  final ChequeService _chequeService = ChequeService();
  final CheckInHistoryService _checkInHistoryService = CheckInHistoryService();
  final PendingTodoService _pendingTodoService = PendingTodoService();
  final UserLoginInfoService _userLoginInfoService = UserLoginInfoService();
  final HybridBridgeService _hybridBridgeService = const HybridBridgeService();
  final HybridDiagnosticsService _diagnostics = HybridDiagnosticsService();

  static const String _initiateBaseUrl =
      'https://appsy.jbysoft.com/dxgl-app/dxslgl/#/dxslglComp/dxslglfq/index';
  static const String _approvalBaseUrl =
      'https://appsy.jbysoft.com/dxgl-app/dxslgl/#/dxslglComp/dxslglsp/index';
  static const String _initiateZtConfig =
      '7171a46ae52cd9c7c0278a72c65492b0fd234129620126cd57ac2fccaaec061b0abed44bcb02786c4057648caf5b99b6c4e7488567e1d5fa7399b6cf5fdeb7ea09b27db192ee5320e008c8c888a156ab41012aa0eea91e2a034cd260ee71bc6e6c20b42e49633af74bfc77a9e1142c02c45548a3e3e89cc6a93eda4ca371a379';
  static const String _approvalZtConfig =
      '7171a46ae52cd9c7c0278a72c65492b0fd234129620126cd57ac2fccaaec061b0abed44bcb02786c4057648caf5b99b6a47cd85e2d21008f7399b6cf5fdeb7ea09b27db192ee5320e008c8c888a156ab41012aa0eea91e2a034cd260ee71bc6e6c20b42e49633af74bfc77a9e1142c02c45548a3e3e89cc6a93eda4ca371a379';

  List<CheckInHistoryLocation> _historyLocations = <CheckInHistoryLocation>[];
  UserLoginInfo? _loginInfo;
  CheckInLocationPreset? _preparedPreset;
  HybridRuntimeContext? _runtimeContext;
  InAppWebViewController? _webViewController;
  Key _webViewKey = UniqueKey();
  bool _isLoadingHistory = false;
  bool _isLoadingCheque = false;
  bool _isLoadingPendingTodos = false;
  bool _isResolvingLoginInfo = false;
  bool _isLoadingWebView = false;
  double _webProgress = 0;
  String _status = '准备一个最小 hybrid 容器：先配置 URL、登录信息和位置，再打开内嵌页面。';

  @override
  void initState() {
    super.initState();
    _locationService.lastError.addListener(_syncErrorState);
  }

  void _syncErrorState() {
    final String? error = _locationService.lastError.value;
    if (!mounted || error == null || error.isEmpty) {
      return;
    }

    setState(() {
      _status = error;
    });
  }

  void _log(String message) {
    _diagnostics.add(message);
  }

  Future<void> _fillWithCurrentCoordinates() async {
    try {
      final position = await _locationService.getCurrentPosition();
      if (!mounted) {
        return;
      }

      _longitudeController.text = position.longitude.toStringAsFixed(6);
      _latitudeController.text = position.latitude.toStringAsFixed(6);
      setState(() {
        _status = '已使用设备当前坐标更新桥接位置。';
      });
      _log(
        'Location updated from device: ${position.longitude.toStringAsFixed(6)}, '
        '${position.latitude.toStringAsFixed(6)}',
      );
    } on LocationServiceException catch (error) {
      if (!mounted) {
        return;
      }

      setState(() {
        _status = error.message;
      });
    }
  }

  CheckInLocationPreset? _buildPresetFromInputs(UserLoginInfo loginInfo) {
    final String address = _addressController.text.trim();
    final double? longitude = double.tryParse(_longitudeController.text.trim());
    final double? latitude = double.tryParse(_latitudeController.text.trim());

    if (address.isEmpty || longitude == null || latitude == null) {
      setState(() {
        _status = '请先填写或选择有效的地址、经度和纬度。';
      });
      return null;
    }

    return CheckInLocationPreset(
      address: address,
      longitude: longitude,
      latitude: latitude,
      loginInfo: loginInfo,
      province: '河北省',
      city: '石家庄市',
      district: '鹿泉区',
      street: '御园路',
      cityCode: '0311',
      provinceCode: '130000',
      adCode: '130110',
      provinceReferred: '冀',
    );
  }

  Future<UserLoginInfo?> _fetchMobileLoginInfo() async {
    final String username = _usernameController.text.trim();
    if (username.isEmpty) {
      setState(() {
        _status = '请输入姓名后再查询移动端登录 token。';
      });
      return null;
    }

    setState(() {
      _isResolvingLoginInfo = true;
      _status = '正在查询移动端 app_02 登录 token...';
    });

    try {
      final UserLoginInfo loginInfo =
          await _userLoginInfoService.fetchMobileLoginInfo(username);
      if (!mounted) {
        return null;
      }

      setState(() {
        _loginInfo = loginInfo;
        _status = '已获取 ${loginInfo.qdmc ?? loginInfo.blqd} 的登录 token。';
      });
      _log('Fetched login info for ${loginInfo.username}.');
      unawaited(_fetchRecentHistoryLocations(loginInfo: loginInfo));
      return loginInfo;
    } on UserLoginInfoServiceException catch (error) {
      if (!mounted) {
        return null;
      }

      setState(() {
        _status = error.message;
      });
      return null;
    } on TimeoutException {
      if (!mounted) {
        return null;
      }

      setState(() {
        _status = '登录 token 查询超时，请稍后重试。';
      });
      return null;
    } catch (error) {
      if (!mounted) {
        return null;
      }

      setState(() {
        _status = '登录 token 查询失败：$error';
      });
      return null;
    } finally {
      if (mounted) {
        setState(() {
          _isResolvingLoginInfo = false;
        });
      }
    }
  }

  Future<void> _fetchRecentHistoryLocations({
    UserLoginInfo? loginInfo,
  }) async {
    final UserLoginInfo? resolvedLoginInfo = loginInfo ?? _loginInfo;
    if (resolvedLoginInfo == null) {
      setState(() {
        _status = '请先查询移动端 Token，再加载历史打卡位置。';
      });
      return;
    }

    setState(() {
      _isLoadingHistory = true;
      _status = '正在查询最近5天的历史打卡位置...';
    });

    try {
      final List<CheckInHistoryLocation> items = await _checkInHistoryService
          .fetchRecentLocations(resolvedLoginInfo);
      if (!mounted) {
        return;
      }
      setState(() {
        _historyLocations = items;
        _status = items.isEmpty
            ? '最近5天没有查询到可用的历史打卡位置。'
            : '已加载最近5天内的 ${items.length} 个历史打卡位置。';
      });
      _log('Loaded ${items.length} recent history locations.');
    } on CheckInHistoryServiceException catch (error) {
      if (!mounted) {
        return;
      }
      setState(() {
        _status = error.message;
      });
    } on TimeoutException {
      if (!mounted) {
        return;
      }
      setState(() {
        _status = '历史打卡位置查询超时，请稍后重试。';
      });
    } catch (error) {
      if (!mounted) {
        return;
      }
      setState(() {
        _status = '历史打卡位置查询失败：$error';
      });
    } finally {
      if (mounted) {
        setState(() {
          _isLoadingHistory = false;
        });
      }
    }
  }

  Future<void> _fetchChequeInfo() async {
    final UserLoginInfo? loginInfo = _loginInfo ?? await _fetchMobileLoginInfo();
    if (!mounted || loginInfo == null) {
      return;
    }

    final CheckInLocationPreset? preset = _buildPresetFromInputs(loginInfo);
    if (preset == null) {
      return;
    }

    setState(() {
      _isLoadingCheque = true;
      _status = '正在查询 cheque / ticket...';
    });

    try {
      final chequeInfo = await _chequeService.fetchCheque(preset);
      if (!mounted) {
        return;
      }

      setState(() {
        _preparedPreset = preset.copyWith(
          cheque: chequeInfo.cheque,
          ticket: chequeInfo.ticket,
        );
        _status = '已获取 cheque / ticket，可用于内嵌页会话启动。';
      });
      _log('Fetched cheque/ticket for hybrid bootstrap.');
    } on ChequeServiceException catch (error) {
      if (!mounted) {
        return;
      }
      setState(() {
        final String debugText =
            _chequeService.lastDebugInfo?.toMultilineText() ?? '';
        _status = 'cheque / ticket 查询失败：${error.message}'
            '${debugText.isEmpty ? '' : '\n\n$debugText'}';
      });
    } on TimeoutException {
      if (!mounted) {
        return;
      }
      setState(() {
        final String debugText =
            _chequeService.lastDebugInfo?.toMultilineText() ?? '';
        _status = 'cheque / ticket 查询超时。'
            '${debugText.isEmpty ? '' : '\n\n$debugText'}';
      });
    } catch (error) {
      if (!mounted) {
        return;
      }
      setState(() {
        final String debugText =
            _chequeService.lastDebugInfo?.toMultilineText() ?? '';
        _status = 'cheque / ticket 查询失败：$error'
            '${debugText.isEmpty ? '' : '\n\n$debugText'}';
      });
    } finally {
      if (mounted) {
        setState(() {
          _isLoadingCheque = false;
        });
      }
    }
  }

  Future<void> _openPendingTodoPage() async {
    final UserLoginInfo? loginInfo = _loginInfo ?? await _fetchMobileLoginInfo();
    if (!mounted || loginInfo == null) {
      return;
    }

    setState(() {
      _isLoadingPendingTodos = true;
      _status = '正在查询待办...';
    });

    try {
      final Map<String, dynamic> payload =
          await _pendingTodoService.fetchPendingTodos(loginInfo);
      if (!mounted) {
        return;
      }

      final List<Map<String, dynamic>> items = _extractTodoItems(payload);
      if (items.isEmpty) {
        setState(() {
          _status = '没有查询到可用待办。';
        });
        _log('Fetched pending todos: 0 items.');
        return;
      }

      final CheckInLocationPreset? preset = await _ensurePreparedPreset(loginInfo);
      if (!mounted) {
        return;
      }

      final String approvalUrl = _buildApprovalPageUrl(
        loginInfo: loginInfo,
        todo: items.first,
        cheque: preset?.cheque ?? '',
      );
      _log(
        'Fetched pending todos: ${items.length} items, opening first businessKey='
        '${_readNestedString(items.first, 'bpmxq', 'businessKey')}.',
      );
      _openEmbeddedPage(
        approvalUrl,
        preset: preset,
        statusMessage: '正在打开待办处理页面...',
      );
    } on PendingTodoServiceException catch (error) {
      if (!mounted) {
        return;
      }
      setState(() {
        _status = error.message;
      });
    } on TimeoutException {
      if (!mounted) {
        return;
      }
      setState(() {
        _status = '待办查询超时，请稍后重试。';
      });
    } catch (error) {
      if (!mounted) {
        return;
      }
      setState(() {
        _status = '待办查询失败：$error';
      });
    } finally {
      if (mounted) {
        setState(() {
          _isLoadingPendingTodos = false;
        });
      }
    }
  }

  List<Map<String, dynamic>> _extractTodoItems(Map<String, dynamic> payload) {
    final Object? results = payload['results'];
    if (results is List) {
      return results
          .whereType<Map>()
          .map(
            (Map item) => item.map<String, dynamic>(
              (dynamic key, dynamic value) => MapEntry(key.toString(), value),
            ),
          )
          .toList();
    }

    return <Map<String, dynamic>>[];
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

  Future<CheckInLocationPreset?> _ensurePreparedPreset(
    UserLoginInfo loginInfo,
  ) async {
    final CheckInLocationPreset? current = _preparedPreset;
    if (current != null && current.cheque.isNotEmpty) {
      return current;
    }

    await _fetchChequeInfo();
    return _preparedPreset ?? _buildPresetFromInputs(loginInfo);
  }

  void _applyHistoryLocation(CheckInHistoryLocation item) {
    if (item.longitude == null || item.latitude == null) {
      return;
    }

    _addressController.text = item.address;
    _longitudeController.text = item.longitude!.toString();
    _latitudeController.text = item.latitude!.toString();

    setState(() {
      _status = '已使用历史打卡位置：${item.address}';
      _preparedPreset = null;
    });
    _log('Applied history location ${item.address}.');
  }

  void _clearDiagnostics() {
    _diagnostics.clear();
    setState(() {
      _status = '已清空诊断日志。';
    });
  }

  void _openEmbeddedPage(
    String rawUrl, {
    CheckInLocationPreset? preset,
    String statusMessage = '正在打开内嵌页面...',
  }) {
    try {
      final HybridRuntimeContext runtimeContext =
          HybridRuntimeContext.fromInputs(
        baseUrl: rawUrl,
        loginInfo: _loginInfo,
        preset: preset,
      );

      _diagnostics.clear();
      _log('Prepared hybrid runtime for ${runtimeContext.resolvedUri}.');
      setState(() {
        _runtimeContext = runtimeContext;
        _webViewController = null;
        _webViewKey = UniqueKey();
        _webProgress = 0;
        _isLoadingWebView = true;
        _status = statusMessage;
      });
    } catch (error) {
      setState(() {
        _status = 'URL 无效：$error';
      });
    }
  }

  Future<void> _openInitiatePage() async {
    final UserLoginInfo? loginInfo = _loginInfo ?? await _fetchMobileLoginInfo();
    if (!mounted || loginInfo == null) {
      return;
    }

    final CheckInLocationPreset? preset = await _ensurePreparedPreset(loginInfo);
    if (!mounted) {
      return;
    }

    final String initiateUrl = _buildInitiatePageUrl(loginInfo);
    _openEmbeddedPage(
      initiateUrl,
      preset: preset,
      statusMessage: '正在打开主动发起页面...',
    );
  }

  String _buildInitiatePageUrl(UserLoginInfo loginInfo) {
    final String orgNumber =
        loginInfo.zxbm.isNotEmpty ? loginInfo.zxbm : loginInfo.jgbh;
    return _buildHashRouteUrl(
      _initiateBaseUrl,
      <String, String>{
        'bm': '03060',
        'ticket': 'nothing',
        'cpbs': 'bbPro',
        'ztConfig': _initiateZtConfig,
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

  String _buildApprovalPageUrl({
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
      'ztConfig': _approvalZtConfig,
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

    return _buildHashRouteUrl(_approvalBaseUrl, queryParameters);
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

  Future<void> _initializeWebView(InAppWebViewController controller) async {
    final HybridRuntimeContext? runtimeContext = _runtimeContext;
    if (runtimeContext == null) {
      return;
    }

    _webViewController = controller;
    _hybridBridgeService.registerHandlers(
      controller: controller,
      onLog: (String message) => _log('js: $message'),
      onPostMessage: (String payload) => _log('postMessage: $payload'),
      onOpenUrl: (String url) {
        _log('openUrl requested: $url');
        if (!mounted) {
          return;
        }
        setState(() {
          _status = '页面请求打开新地址：$url';
        });
      },
      onClosePage: () {
        _log('closePage requested');
        if (!mounted) {
          return;
        }
        setState(() {
          _status = '页面请求关闭当前容器。';
          _runtimeContext = null;
          _webViewController = null;
          _isLoadingWebView = false;
          _webProgress = 0;
        });
      },
    );

    await _hybridBridgeService.bootstrapCookies(
      runtimeContext,
      onLog: _log,
    );

    await controller.loadUrl(
      urlRequest: URLRequest(
        url: WebUri(runtimeContext.resolvedUri.toString()),
      ),
    );
  }

  Future<void> _reloadEmbeddedPage() async {
    await _webViewController?.reload();
  }

  @override
  void dispose() {
    _locationService.lastError.removeListener(_syncErrorState);
    _chequeService.dispose();
    _checkInHistoryService.dispose();
    _locationService.dispose();
    _pendingTodoService.dispose();
    _userLoginInfoService.dispose();
    _usernameController.dispose();
    _addressController.dispose();
    _longitudeController.dispose();
    _latitudeController.dispose();
    _diagnostics.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final ThemeData theme = Theme.of(context);
    final HybridRuntimeContext? runtimeContext = _runtimeContext;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Hybrid 容器 MVP'),
        actions: <Widget>[
          IconButton(
            tooltip: '清空日志',
            onPressed: _clearDiagnostics,
            icon: const Icon(Icons.clear_all),
          ),
          IconButton(
            tooltip: '刷新内嵌页',
            onPressed: runtimeContext == null ? null : _reloadEmbeddedPage,
            icon: const Icon(Icons.refresh),
          ),
        ],
      ),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: <Widget>[
          Card(
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Text('状态', style: theme.textTheme.titleMedium),
                  const SizedBox(height: 8),
                  Text(_status),
                  const SizedBox(height: 8),
                  Text(
                    runtimeContext == null
                        ? '尚未打开内嵌页。'
                        : '当前容器 URL: ${runtimeContext.resolvedUri}',
                    style: theme.textTheme.bodySmall,
                  ),
                ],
              ),
            ),
          ),
          const SizedBox(height: 12),
          Card(
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Text('页面入口', style: theme.textTheme.titleMedium),
                  const SizedBox(height: 12),
                  Text(
                    '请选择要进入的业务入口。主动发起沿用原有发起逻辑；待办处理会先查询待办，再打开第一条审批页面。',
                    style: theme.textTheme.bodyMedium,
                  ),
                  const SizedBox(height: 12),
                  Wrap(
                    spacing: 12,
                    runSpacing: 12,
                    children: <Widget>[
                      FilledButton.icon(
                        onPressed: _openInitiatePage,
                        icon: const Icon(Icons.playlist_add_circle_outlined),
                        label: const Text('主动发起'),
                      ),
                      FilledButton.icon(
                        onPressed: _isLoadingPendingTodos ? null : _openPendingTodoPage,
                        icon: const Icon(Icons.assignment_turned_in_outlined),
                        label: Text(_isLoadingPendingTodos ? '查询中...' : '待办处理'),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ),
          const SizedBox(height: 12),
          Card(
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Text('用户身份', style: theme.textTheme.titleMedium),
                  const SizedBox(height: 12),
                  TextField(
                    controller: _usernameController,
                    decoration: const InputDecoration(
                      labelText: '姓名',
                      hintText: '请输入姓名',
                      border: OutlineInputBorder(),
                    ),
                  ),
                  const SizedBox(height: 12),
                  FilledButton.icon(
                    onPressed: _isResolvingLoginInfo ? null : _fetchMobileLoginInfo,
                    icon: const Icon(Icons.badge_outlined),
                    label: Text(_isResolvingLoginInfo ? '查询中...' : '查询移动端 Token'),
                  ),
                  if (_loginInfo != null) ...<Widget>[
                    const SizedBox(height: 12),
                    Container(
                      width: double.infinity,
                      padding: const EdgeInsets.all(12),
                      decoration: BoxDecoration(
                        color: const Color(0xFFEFF5FF),
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: Text(
                        '账号: ${_loginInfo!.account}\n'
                        'Token: ${_loginInfo!.tokenPreview}\n'
                        '渠道: ${_loginInfo!.blqd} / ${_loginInfo!.qdmc ?? '未知'}',
                      ),
                    ),
                  ],
                ],
              ),
            ),
          ),
          const SizedBox(height: 12),
          Card(
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Text('桥接位置', style: theme.textTheme.titleMedium),
                  const SizedBox(height: 12),
                  TextField(
                    controller: _addressController,
                    maxLines: 3,
                    decoration: const InputDecoration(
                      labelText: '地址',
                      border: OutlineInputBorder(),
                    ),
                  ),
                  const SizedBox(height: 12),
                  TextField(
                    controller: _longitudeController,
                    keyboardType: const TextInputType.numberWithOptions(
                      decimal: true,
                      signed: true,
                    ),
                    decoration: const InputDecoration(
                      labelText: '经度',
                      border: OutlineInputBorder(),
                    ),
                  ),
                  const SizedBox(height: 12),
                  TextField(
                    controller: _latitudeController,
                    keyboardType: const TextInputType.numberWithOptions(
                      decimal: true,
                      signed: true,
                    ),
                    decoration: const InputDecoration(
                      labelText: '纬度',
                      border: OutlineInputBorder(),
                    ),
                  ),
                  const SizedBox(height: 12),
                  Wrap(
                    spacing: 12,
                    runSpacing: 12,
                    children: <Widget>[
                      FilledButton.icon(
                        onPressed: _fillWithCurrentCoordinates,
                        icon: const Icon(Icons.gps_fixed),
                        label: const Text('使用当前坐标'),
                      ),
                      FilledButton.icon(
                        onPressed: _isLoadingHistory
                            ? null
                            : () => _fetchRecentHistoryLocations(),
                        icon: const Icon(Icons.history),
                        label: Text(_isLoadingHistory ? '查询中...' : '查询历史位置'),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ),
          const SizedBox(height: 12),
          Card(
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Text('会话辅助', style: theme.textTheme.titleMedium),
                  const SizedBox(height: 12),
                  FilledButton.icon(
                    onPressed: _isLoadingCheque ? null : _fetchChequeInfo,
                    icon: const Icon(Icons.verified_outlined),
                    label: Text(_isLoadingCheque ? '查询中...' : '查询 cheque / ticket'),
                  ),
                  if (_preparedPreset != null) ...<Widget>[
                    const SizedBox(height: 12),
                    Container(
                      width: double.infinity,
                      padding: const EdgeInsets.all(12),
                      decoration: BoxDecoration(
                        color: const Color(0xFFEFF7EE),
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: Text(
                        'cheque: ${_preparedPreset!.cheque}\n'
                        'ticket: ${_preparedPreset!.ticket}',
                      ),
                    ),
                  ],
                ],
              ),
            ),
          ),
          if (_historyLocations.isNotEmpty) ...<Widget>[
            const SizedBox(height: 12),
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    Text('最近历史位置', style: theme.textTheme.titleMedium),
                    const SizedBox(height: 12),
                    ..._historyLocations.map(
                      (CheckInHistoryLocation item) => Column(
                        children: <Widget>[
                          ListTile(
                            contentPadding: EdgeInsets.zero,
                            title: Text(item.address),
                            subtitle: Text(
                              '${item.timeText}\n'
                              '${item.coordinateText}\n'
                              '${item.deviceName.isEmpty ? '未知设备' : item.deviceName}',
                            ),
                            isThreeLine: true,
                            trailing: FilledButton.tonal(
                              onPressed: () => _applyHistoryLocation(item),
                              child: const Text('使用'),
                            ),
                          ),
                          if (item != _historyLocations.last) const Divider(),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ],
          const SizedBox(height: 12),
          Card(
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Text('运行时预览', style: theme.textTheme.titleMedium),
                  const SizedBox(height: 8),
                  Text(
                    runtimeContext == null
                        ? '尚未生成运行时上下文。'
                        : 'Origin: ${runtimeContext.origin}\n'
                            'Headers: ${runtimeContext.authHeaders.keys.join(', ')}\n'
                            'Storage Keys: ${runtimeContext.storageSeed.keys.join(', ')}',
                  ),
                  const SizedBox(height: 8),
                  Text(
                    '说明：同步 bridge 数据在 document-start 注入；异步回调仅用于日志、openUrl 和 closePage，避免再次回到 runJavaScript 竞态。',
                    style: theme.textTheme.bodySmall,
                  ),
                ],
              ),
            ),
          ),
          Card(
            clipBehavior: Clip.antiAlias,
            child: Padding(
              padding: const EdgeInsets.all(12),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Row(
                    children: <Widget>[
                      Text('内嵌页', style: theme.textTheme.titleMedium),
                      const Spacer(),
                      if (_isLoadingWebView)
                        Text('${(_webProgress * 100).round()}%'),
                    ],
                  ),
                  const SizedBox(height: 8),
                  if (_isLoadingWebView && _webProgress < 1)
                    LinearProgressIndicator(value: _webProgress),
                  const SizedBox(height: 8),
                  SizedBox(
                    height: 520,
                    child: runtimeContext == null
                        ? const DecoratedBox(
                            decoration: BoxDecoration(
                              color: Color(0xFFF6F8FB),
                            ),
                            child: Center(
                              child: Text('配置 URL 后点击“打开内嵌页”。'),
                            ),
                          )
                        : InAppWebView(
                            key: _webViewKey,
                            initialSettings: InAppWebViewSettings(
                              isInspectable: kDebugMode,
                              mediaPlaybackRequiresUserGesture: false,
                              allowsInlineMediaPlayback: true,
                              useShouldOverrideUrlLoading: true,
                            ),
                            initialUserScripts: _hybridBridgeService
                                .buildInitialUserScripts(runtimeContext),
                            onWebViewCreated: _initializeWebView,
                            onLoadStart: (controller, url) {
                              _log('loadStart: ${url ?? runtimeContext.resolvedUri}');
                              if (!mounted) {
                                return;
                              }
                              setState(() {
                                _isLoadingWebView = true;
                                _status = '页面开始加载：${url ?? runtimeContext.resolvedUri}';
                              });
                            },
                            onLoadStop: (controller, url) {
                              _log('loadStop: ${url ?? runtimeContext.resolvedUri}');
                              if (!mounted) {
                                return;
                              }
                              setState(() {
                                _isLoadingWebView = false;
                                _webProgress = 1;
                                _status = '页面加载完成。';
                              });
                            },
                            onProgressChanged: (controller, progress) {
                              if (!mounted) {
                                return;
                              }
                              setState(() {
                                _webProgress = progress / 100;
                                _isLoadingWebView = progress < 100;
                              });
                            },
                            onConsoleMessage: (controller, consoleMessage) {
                              _log(
                                'console[${consoleMessage.messageLevel}]: ${consoleMessage.message}',
                              );
                            },
                            onReceivedError: (controller, request, error) {
                              _log(
                                'error: ${request.url} -> ${error.description}',
                              );
                              if (!mounted) {
                                return;
                              }
                              setState(() {
                                _isLoadingWebView = false;
                                _status = '页面错误：${error.description}';
                              });
                            },
                            shouldOverrideUrlLoading:
                                (controller, navigationAction) async {
                              final WebUri? uri = navigationAction.request.url;
                              if (uri == null) {
                                return NavigationActionPolicy.ALLOW;
                              }
                              if (<String>{
                                'http',
                                'https',
                                'file',
                                'about',
                                'data',
                                'javascript',
                              }.contains(uri.scheme)) {
                                return NavigationActionPolicy.ALLOW;
                              }
                              _log('Blocked non-http(s) navigation: $uri');
                              return NavigationActionPolicy.CANCEL;
                            },
                          ),
                  ),
                ],
              ),
            ),
          ),
          const SizedBox(height: 12),
          Card(
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Text('诊断日志', style: theme.textTheme.titleMedium),
                  const SizedBox(height: 8),
                  AnimatedBuilder(
                    animation: _diagnostics,
                    builder: (BuildContext context, Widget? child) {
                      final String logText = _diagnostics.entries.isEmpty
                          ? 'No bridge events yet.'
                          : _diagnostics.entries.join('\n');
                      return SelectableText(logText);
                    },
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
