import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../models/check_in_history_location.dart';
import '../models/check_in_location_preset.dart';
import '../models/hybrid_runtime_context.dart';
import '../models/user_login_info.dart';
import '../services/check_in_history_service.dart';
import '../services/cheque_service.dart';
import '../services/hybrid_workflow_service.dart';
import '../services/pending_todo_service.dart';
import '../services/user_login_info_service.dart';
import 'hybrid_webview_page.dart';

class HybridCheckInPage extends StatefulWidget {
  const HybridCheckInPage({super.key});

  @override
  State<HybridCheckInPage> createState() => _HybridCheckInPageState();
}

class _HybridCheckInPageState extends State<HybridCheckInPage> {
  final TextEditingController _usernameController = TextEditingController();
  final CheckInHistoryService _checkInHistoryService = CheckInHistoryService();
  final ChequeService _chequeService = ChequeService();
  final HybridWorkflowService _workflowService = const HybridWorkflowService();
  final PendingTodoService _pendingTodoService = PendingTodoService();
  final UserLoginInfoService _userLoginInfoService = UserLoginInfoService();

  List<CheckInHistoryLocation> _historyLocations = <CheckInHistoryLocation>[];
  UserLoginInfo? _loginInfo;
  CheckInLocationPreset? _preparedPreset;
  String? _selectedHistoryKey;
  String? _resolvedUsername;
  bool _isPreparingUserContext = false;
  bool _isLaunchingInitiate = false;
  bool _isLaunchingPending = false;

  bool get _isBusy =>
      _isPreparingUserContext || _isLaunchingInitiate || _isLaunchingPending;

  CheckInHistoryLocation? get _selectedHistoryLocation {
    final String? selectedHistoryKey = _selectedHistoryKey;
    if (selectedHistoryKey == null) {
      return _historyLocations.isEmpty ? null : _historyLocations.first;
    }

    for (final CheckInHistoryLocation item in _historyLocations) {
      if (item.uniqueKey == selectedHistoryKey) {
        return item;
      }
    }
    return _historyLocations.isEmpty ? null : _historyLocations.first;
  }

  void _showMessage(String message) {
    if (!mounted) {
      return;
    }

    ScaffoldMessenger.of(context)
      ..hideCurrentSnackBar()
      ..showSnackBar(SnackBar(content: Text(message)));
  }

  void _handleUsernameChanged(String value) {
    final String trimmed = value.trim();
    if (_resolvedUsername == null || trimmed == _resolvedUsername) {
      return;
    }

    setState(() {
      _resolvedUsername = null;
      _loginInfo = null;
      _historyLocations = <CheckInHistoryLocation>[];
      _selectedHistoryKey = null;
      _preparedPreset = null;
    });
  }

  Future<UserLoginInfo?> _ensureUserContext({
    bool forceRefresh = false,
  }) async {
    final String username = _usernameController.text.trim();
    if (username.isEmpty) {
      _showMessage('请先输入姓名。');
      return null;
    }

    if (!forceRefresh &&
        _loginInfo != null &&
        _resolvedUsername == username &&
        _historyLocations.isNotEmpty) {
      return _loginInfo;
    }

    setState(() {
      _isPreparingUserContext = true;
    });

    try {
      final UserLoginInfo loginInfo =
          await _userLoginInfoService.fetchMobileLoginInfo(username);
      final List<CheckInHistoryLocation> historyLocations =
          await _checkInHistoryService.fetchRecentLocations(loginInfo);

      if (!mounted) {
        return null;
      }

      setState(() {
        _loginInfo = loginInfo;
        _resolvedUsername = username;
        _historyLocations = historyLocations;
        _selectedHistoryKey =
            historyLocations.isEmpty ? null : historyLocations.first.uniqueKey;
        _preparedPreset = null;
      });

      if (historyLocations.isEmpty) {
        _showMessage('最近 5 天没有查询到可用的打卡位置。');
      }

      return loginInfo;
    } on UserLoginInfoServiceException catch (error) {
      _showMessage(error.message);
    } on CheckInHistoryServiceException catch (error) {
      _showMessage(error.message);
    } on TimeoutException {
      _showMessage('请求超时，请稍后重试。');
    } catch (error) {
      _showMessage('准备用户信息失败：$error');
    } finally {
      if (mounted) {
        setState(() {
          _isPreparingUserContext = false;
        });
      }
    }

    return null;
  }

  Future<CheckInLocationPreset?> _ensurePreparedPreset(
    UserLoginInfo loginInfo,
  ) async {
    final CheckInHistoryLocation? historyLocation = _selectedHistoryLocation;
    if (historyLocation == null) {
      _showMessage('请先选择一个最近打卡位置。');
      return null;
    }

    final CheckInLocationPreset basePreset = _buildPresetFromHistory(
      loginInfo: loginInfo,
      item: historyLocation,
    );
    final CheckInLocationPreset? currentPreparedPreset = _preparedPreset;
    if (currentPreparedPreset != null &&
        currentPreparedPreset.cheque.isNotEmpty &&
        currentPreparedPreset.address == basePreset.address &&
        currentPreparedPreset.coordinateText == basePreset.coordinateText &&
        currentPreparedPreset.loginInfo.loginToken == loginInfo.loginToken) {
      return currentPreparedPreset;
    }

    try {
      final ChequeInfo chequeInfo = await _chequeService.fetchCheque(basePreset);
      final CheckInLocationPreset preparedPreset = basePreset.copyWith(
        cheque: chequeInfo.cheque,
        ticket: chequeInfo.ticket,
      );

      if (!mounted) {
        return preparedPreset;
      }

      setState(() {
        _preparedPreset = preparedPreset;
      });
      return preparedPreset;
    } on ChequeServiceException catch (error) {
      _showMessage(error.message);
    } on TimeoutException {
      _showMessage('会话信息查询超时，请稍后重试。');
    } catch (error) {
      _showMessage('会话信息查询失败：$error');
    }

    return null;
  }

  CheckInLocationPreset _buildPresetFromHistory({
    required UserLoginInfo loginInfo,
    required CheckInHistoryLocation item,
  }) {
    final _LocationMetadata metadata = _inferLocationMetadata(item.address);
    return CheckInLocationPreset(
      address: item.address,
      latitude: item.latitude!,
      loginInfo: loginInfo,
      longitude: item.longitude!,
      province: metadata.province,
      city: metadata.city,
      district: metadata.district,
      street: metadata.street,
      deviceIdentifier: item.deviceIdentifier,
      cityCode: metadata.cityCode,
      provinceCode: metadata.provinceCode,
      adCode: metadata.adCode,
      provinceReferred: metadata.provinceReferred,
    );
  }

  Future<void> _openInitiatePage() async {
    setState(() {
      _isLaunchingInitiate = true;
    });

    try {
      final UserLoginInfo? loginInfo = await _ensureUserContext();
      if (!mounted || loginInfo == null) {
        return;
      }

      final CheckInLocationPreset? preset = await _ensurePreparedPreset(loginInfo);
      if (!mounted || preset == null) {
        return;
      }

      final String url = _workflowService.buildInitiatePageUrl(loginInfo);
      _navigateToWebView(
        rawUrl: url,
        preset: preset,
        title: '主动发起',
      );
    } finally {
      if (mounted) {
        setState(() {
          _isLaunchingInitiate = false;
        });
      }
    }
  }

  Future<void> _openPendingTodoPage() async {
    setState(() {
      _isLaunchingPending = true;
    });

    try {
      final UserLoginInfo? loginInfo = await _ensureUserContext();
      if (!mounted || loginInfo == null) {
        return;
      }

      final CheckInLocationPreset? preset = await _ensurePreparedPreset(loginInfo);
      if (!mounted || preset == null) {
        return;
      }

      final Map<String, dynamic>? matchedTodo = await _findCheckInPendingTodo(
        loginInfo,
      );
      if (!mounted) {
        return;
      }

      if (matchedTodo == null) {
        _showMessage('没有查询到打卡待办。');
        return;
      }

      final String url = _workflowService.buildApprovalPageUrl(
        loginInfo: loginInfo,
        todo: matchedTodo,
        cheque: preset.cheque,
      );
      _navigateToWebView(
        rawUrl: url,
        preset: preset,
        title: '待办处理',
      );
    } on PendingTodoServiceException catch (error) {
      _showMessage(error.message);
    } on TimeoutException {
      _showMessage('待办查询超时，请稍后重试。');
    } catch (error) {
      _showMessage('待办查询失败：$error');
    } finally {
      if (mounted) {
        setState(() {
          _isLaunchingPending = false;
        });
      }
    }
  }

  Future<Map<String, dynamic>?> _findCheckInPendingTodo(
    UserLoginInfo loginInfo,
  ) async {
    for (final int ywzt in HybridWorkflowService.pendingQueryOrder) {
      final Map<String, dynamic> payload = await _pendingTodoService
          .fetchPendingTodos(loginInfo, ywzt: ywzt);
      final List<Map<String, dynamic>> items = _workflowService.extractTodoItems(
        payload,
      );
      final Map<String, dynamic>? matched = _workflowService.pickCheckInTodo(
        items,
      );
      if (matched != null) {
        return matched;
      }
    }

    return null;
  }

  void _navigateToWebView({
    required String rawUrl,
    required CheckInLocationPreset preset,
    required String title,
  }) {
    try {
      final HybridRuntimeContext runtimeContext = HybridRuntimeContext.fromInputs(
        baseUrl: rawUrl,
        loginInfo: _loginInfo,
        preset: preset,
      );

      Navigator.of(context).push(
        MaterialPageRoute<void>(
          builder: (BuildContext context) => HybridWebViewPage(
            runtimeContext: runtimeContext,
            title: title,
          ),
        ),
      );
    } catch (error) {
      _showMessage('页面地址生成失败：$error');
    }
  }

  _LocationMetadata _inferLocationMetadata(String address) {
    final String province = address.contains('河北') ? '河北省' : '';
    final String city = address.contains('石家庄') ? '石家庄市' : '';
    String district = '';
    String adCode = '';

    if (address.contains('鹿泉区')) {
      district = '鹿泉区';
      adCode = '130110';
    } else if (address.contains('新华区')) {
      district = '新华区';
      adCode = '130105';
    } else if (address.contains('桥西区')) {
      district = '桥西区';
      adCode = '130104';
    } else if (address.contains('长安区')) {
      district = '长安区';
      adCode = '130102';
    }

    String street = '';
    final RegExp streetExp = RegExp(r'([\u4e00-\u9fa5A-Za-z0-9]+(?:路|街|大道))');
    final RegExpMatch? streetMatch = streetExp.firstMatch(address);
    if (streetMatch != null) {
      street = streetMatch.group(1) ?? '';
    }

    return _LocationMetadata(
      province: province,
      city: city,
      district: district,
      street: street,
      cityCode: city.isEmpty ? '' : '0311',
      provinceCode: province.isEmpty ? '' : '130000',
      adCode: adCode,
      provinceReferred: province.isEmpty ? '' : '冀',
    );
  }

  @override
  void dispose() {
    _checkInHistoryService.dispose();
    _chequeService.dispose();
    _pendingTodoService.dispose();
    _userLoginInfoService.dispose();
    _usernameController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final CheckInHistoryLocation? selectedHistoryLocation = _selectedHistoryLocation;

    return Scaffold(
      backgroundColor: const Color(0xFFF2F2F7),
      body: SafeArea(
        child: ListView(
          padding: const EdgeInsets.fromLTRB(16, 12, 16, 24),
          children: <Widget>[
            const _IosHeroHeader(
              title: '打卡入口',
              subtitle: '自动准备身份、位置和会话参数，再进入业务页面。',
            ),
            const SizedBox(height: 18),
            const _SectionLabel('用户身份'),
            const SizedBox(height: 8),
            _IosSection(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  _IosInputRow(
                    child: TextField(
                      controller: _usernameController,
                      enabled: !_isBusy,
                      textInputAction: TextInputAction.done,
                      onChanged: _handleUsernameChanged,
                      onSubmitted: (_) => _ensureUserContext(forceRefresh: true),
                      decoration: InputDecoration(
                        hintText: '输入姓名后按回车自动加载',
                        border: InputBorder.none,
                        suffixIcon: _isPreparingUserContext
                            ? const Padding(
                                padding: EdgeInsets.all(12),
                                child: SizedBox(
                                  width: 18,
                                  height: 18,
                                  child: CupertinoActivityIndicator(radius: 9),
                                ),
                              )
                            : null,
                      ),
                    ),
                  ),
                  if (_loginInfo != null) ...<Widget>[
                    const SizedBox(height: 14),
                    _InfoPill(
                      title: _loginInfo!.username,
                      subtitle:
                          '${_loginInfo!.account}  ·  ${_loginInfo!.tokenPreview}',
                    ),
                  ],
                ],
              ),
            ),
            const SizedBox(height: 20),
            const _SectionLabel('最近 5 次打卡位置'),
            const SizedBox(height: 8),
            if (_usernameController.text.trim().isEmpty)
              const _HintPanel(
                message: '先输入姓名，再从最近的打卡位置里选一个作为本次桥接定位。',
              )
            else if (_isPreparingUserContext)
              const _HintPanel(message: '正在查询最近打卡位置...')
            else if (_historyLocations.isEmpty)
              _HintPanel(
                message: _resolvedUsername == null
                    ? '输入姓名后按回车，或直接点击下方按钮自动准备。'
                    : '最近 5 天没有查询到可用打卡位置。',
              )
            else
              _IosSection(
                child: Column(
                  children: _historyLocations.map((CheckInHistoryLocation item) {
                    final bool selected =
                        item.uniqueKey == selectedHistoryLocation?.uniqueKey;
                    final bool isLast = identical(item, _historyLocations.last);
                    return Column(
                      children: <Widget>[
                        InkWell(
                          onTap: _isBusy
                              ? null
                              : () {
                                  setState(() {
                                    _selectedHistoryKey = item.uniqueKey;
                                    _preparedPreset = null;
                                  });
                                },
                          borderRadius: BorderRadius.circular(20),
                          child: Padding(
                            padding: const EdgeInsets.symmetric(
                              horizontal: 4,
                              vertical: 14,
                            ),
                            child: Row(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: <Widget>[
                                Container(
                                  width: 28,
                                  height: 28,
                                  margin: const EdgeInsets.only(top: 2),
                                  decoration: BoxDecoration(
                                    color: selected
                                        ? const Color(0xFF0A84FF)
                                        : const Color(0xFFE5E5EA),
                                    shape: BoxShape.circle,
                                  ),
                                  child: Icon(
                                    selected
                                        ? Icons.check_rounded
                                        : Icons.place_outlined,
                                    size: 16,
                                    color: selected
                                        ? Colors.white
                                        : const Color(0xFF8E8E93),
                                  ),
                                ),
                                const SizedBox(width: 12),
                                Expanded(
                                  child: Column(
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: <Widget>[
                                      Text(
                                        item.address,
                                        style: const TextStyle(
                                          fontSize: 16,
                                          fontWeight: FontWeight.w600,
                                          color: Color(0xFF111111),
                                        ),
                                      ),
                                      const SizedBox(height: 6),
                                      Text(
                                        item.timeText,
                                        style: const TextStyle(
                                          fontSize: 13,
                                          color: Color(0xFF6E6E73),
                                        ),
                                      ),
                                      const SizedBox(height: 2),
                                      Text(
                                        item.coordinateText,
                                        style: const TextStyle(
                                          fontSize: 13,
                                          color: Color(0xFF6E6E73),
                                        ),
                                      ),
                                      if (item.deviceName.isNotEmpty) ...<Widget>[
                                        const SizedBox(height: 2),
                                        Text(
                                          item.deviceName,
                                          style: const TextStyle(
                                            fontSize: 13,
                                            color: Color(0xFF6E6E73),
                                          ),
                                        ),
                                      ],
                                    ],
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ),
                        if (!isLast)
                          const Divider(
                            height: 1,
                            indent: 40,
                            color: Color(0xFFE5E5EA),
                          ),
                      ],
                    );
                  }).toList(),
                ),
              ),
            const SizedBox(height: 24),
            _ActionButton(
              label: _isLaunchingInitiate ? '处理中...' : '主动发起',
              onPressed: _isBusy ? null : _openInitiatePage,
              filled: true,
            ),
            const SizedBox(height: 12),
            _ActionButton(
              label: _isLaunchingPending ? '处理中...' : '待办处理',
              onPressed: _isBusy ? null : _openPendingTodoPage,
              filled: false,
            ),
          ],
        ),
      ),
    );
  }
}

class _HintPanel extends StatelessWidget {
  const _HintPanel({required this.message});

  final String message;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(24),
      ),
      child: Text(
        message,
        style: const TextStyle(
          fontSize: 14,
          height: 1.45,
          color: Color(0xFF6E6E73),
        ),
      ),
    );
  }
}

class _IosHeroHeader extends StatelessWidget {
  const _IosHeroHeader({
    required this.title,
    required this.subtitle,
  });

  final String title;
  final String subtitle;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.fromLTRB(6, 8, 6, 0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text(
            title,
            style: const TextStyle(
              fontSize: 34,
              fontWeight: FontWeight.w700,
              color: Color(0xFF111111),
              letterSpacing: -0.8,
            ),
          ),
          const SizedBox(height: 6),
          Text(
            subtitle,
            style: const TextStyle(
              fontSize: 15,
              height: 1.4,
              color: Color(0xFF6E6E73),
            ),
          ),
        ],
      ),
    );
  }
}

class _SectionLabel extends StatelessWidget {
  const _SectionLabel(this.label);

  final String label;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 8),
      child: Text(
        label.toUpperCase(),
        style: const TextStyle(
          fontSize: 12,
          fontWeight: FontWeight.w600,
          letterSpacing: 0.6,
          color: Color(0xFF8E8E93),
        ),
      ),
    );
  }
}

class _IosSection extends StatelessWidget {
  const _IosSection({required this.child});

  final Widget child;

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(28),
        boxShadow: const <BoxShadow>[
          BoxShadow(
            color: Color(0x14000000),
            blurRadius: 24,
            offset: Offset(0, 10),
          ),
        ],
      ),
      padding: const EdgeInsets.all(16),
      child: child,
    );
  }
}

class _IosInputRow extends StatelessWidget {
  const _IosInputRow({required this.child});

  final Widget child;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 2),
      decoration: BoxDecoration(
        color: const Color(0xFFF5F5F7),
        borderRadius: BorderRadius.circular(18),
      ),
      child: child,
    );
  }
}

class _InfoPill extends StatelessWidget {
  const _InfoPill({
    required this.title,
    required this.subtitle,
  });

  final String title;
  final String subtitle;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        color: const Color(0xFFEAF3FF),
        borderRadius: BorderRadius.circular(20),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text(
            title,
            style: const TextStyle(
              fontSize: 15,
              fontWeight: FontWeight.w600,
              color: Color(0xFF111111),
            ),
          ),
          const SizedBox(height: 2),
          Text(
            subtitle,
            style: const TextStyle(
              fontSize: 12,
              color: Color(0xFF6E6E73),
            ),
          ),
        ],
      ),
    );
  }
}

class _ActionButton extends StatelessWidget {
  const _ActionButton({
    required this.label,
    required this.onPressed,
    required this.filled,
  });

  final String label;
  final VoidCallback? onPressed;
  final bool filled;

  @override
  Widget build(BuildContext context) {
    final bool enabled = onPressed != null;
    final Color backgroundColor = filled
        ? (enabled ? const Color(0xFF0A84FF) : const Color(0x660A84FF))
        : (enabled ? Colors.white : const Color(0xFFF0F0F3));
    final Color foregroundColor =
        filled ? Colors.white : const Color(0xFF111111);

    return SizedBox(
      width: double.infinity,
      child: CupertinoButton(
        padding: EdgeInsets.zero,
        onPressed: onPressed,
        child: AnimatedContainer(
          duration: const Duration(milliseconds: 180),
          height: 58,
          decoration: BoxDecoration(
            color: backgroundColor,
            borderRadius: BorderRadius.circular(20),
            border: filled
                ? null
                : Border.all(color: const Color(0xFFE5E5EA)),
            boxShadow: filled
                ? const <BoxShadow>[
                    BoxShadow(
                      color: Color(0x290A84FF),
                      blurRadius: 20,
                      offset: Offset(0, 10),
                    ),
                  ]
                : null,
          ),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Text(
                label,
                style: TextStyle(
                  fontSize: 17,
                  fontWeight: FontWeight.w600,
                  color: foregroundColor,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _LocationMetadata {
  const _LocationMetadata({
    required this.province,
    required this.city,
    required this.district,
    required this.street,
    required this.cityCode,
    required this.provinceCode,
    required this.adCode,
    required this.provinceReferred,
  });

  final String province;
  final String city;
  final String district;
  final String street;
  final String cityCode;
  final String provinceCode;
  final String adCode;
  final String provinceReferred;
}
