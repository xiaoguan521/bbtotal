import 'dart:async';

import 'package:flutter/material.dart';

import '../models/check_in_location_preset.dart';
import '../models/check_in_history_location.dart';
import '../models/user_login_info.dart';
import 'check_in_webview_page.dart';
import '../services/cheque_service.dart';
import '../services/check_in_history_service.dart';
import '../services/location_service.dart';
import '../services/user_login_info_service.dart';

class CheckInLocationSetupPage extends StatefulWidget {
  const CheckInLocationSetupPage({super.key});

  @override
  State<CheckInLocationSetupPage> createState() =>
      _CheckInLocationSetupPageState();
}

class _CheckInLocationSetupPageState extends State<CheckInLocationSetupPage> {
  late final TextEditingController _usernameController =
      TextEditingController();
  late final TextEditingController _addressController =
      TextEditingController(text: '河北省石家庄市鹿泉区御园路71号靠近光谷科技园');
  late final TextEditingController _longitudeController =
      TextEditingController(text: '114.347315');
  late final TextEditingController _latitudeController =
      TextEditingController(text: '38.050979');

  final LocationService _locationService = LocationService();
  final ChequeService _chequeService = ChequeService();
  final CheckInHistoryService _checkInHistoryService = CheckInHistoryService();
  final UserLoginInfoService _userLoginInfoService = UserLoginInfoService();

  List<CheckInHistoryLocation> _historyLocations = <CheckInHistoryLocation>[];
  UserLoginInfo? _loginInfo;
  CheckInLocationPreset? _preparedPreset;
  bool _isLoadingHistory = false;
  bool _isLoadingCheque = false;
  bool _isResolvingLoginInfo = false;
  String _status = 'Ready to configure the clock-in location.';

  @override
  void initState() {
    super.initState();
    _locationService.lastError.addListener(_syncErrorState);
  }

  void _syncErrorState() {
    final error = _locationService.lastError.value;
    if (!mounted || error == null || error.isEmpty) {
      return;
    }

    setState(() {
      _status = error;
    });
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
        _status = 'Filled the form with the device coordinates.';
      });
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
        _status = '已获取 cheque / ticket，可以进入打卡页。';
      });
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
  }

  Future<void> _openCheckInPage() async {
    final CheckInLocationPreset? resolvedPreset = _preparedPreset;
    if (resolvedPreset == null) {
      setState(() {
        _status = '请先手动完成 Token 和 cheque 查询，再进入打卡页。';
      });
      return;
    }

    Navigator.of(context).push(
      MaterialPageRoute<void>(
        builder: (_) => CheckInWebViewPage(preset: resolvedPreset),
      ),
    );
  }

  @override
  void dispose() {
    _locationService.lastError.removeListener(_syncErrorState);
    _chequeService.dispose();
    _checkInHistoryService.dispose();
    _locationService.dispose();
    _userLoginInfoService.dispose();
    _usernameController.dispose();
    _addressController.dispose();
    _longitudeController.dispose();
    _latitudeController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Scaffold(
      appBar: AppBar(
        title: const Text('打卡位置设置'),
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
                  Text('Status', style: theme.textTheme.titleMedium),
                  const SizedBox(height: 8),
                  Text(_status),
                  const SizedBox(height: 12),
                  ValueListenableBuilder<bool>(
                    valueListenable: _locationService.isLoading,
                    builder: (context, isLoading, _) {
                      return Text(
                        isLoading ? 'Loading location...' : 'Idle',
                        style: theme.textTheme.bodySmall,
                      );
                    },
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
                  if (_loginInfo != null)
                    Container(
                      width: double.infinity,
                      padding: const EdgeInsets.all(12),
                      decoration: BoxDecoration(
                        color: const Color(0xFFEFF5FF),
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: Text(
                        '移动端 token: ${_loginInfo!.tokenPreview}\n'
                        '渠道: ${_loginInfo!.blqd} / ${_loginInfo!.qdmc ?? '未知'}',
                      ),
                    ),
                  if (_loginInfo != null) const SizedBox(height: 16),
                  if (_preparedPreset != null)
                    Container(
                      width: double.infinity,
                      padding: const EdgeInsets.all(12),
                      decoration: BoxDecoration(
                        color: const Color(0xFFEFF7EE),
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: Text(
                        '已准备 cheque: ${_preparedPreset!.cheque}\n'
                        'ticket: ${_preparedPreset!.ticket}',
                      ),
                    ),
                  if (_preparedPreset != null) const SizedBox(height: 16),
                  Text('打卡页来源', style: theme.textTheme.titleMedium),
                  const SizedBox(height: 8),
                  const Text(
                    '这个 URL 来自本次 chrome://inspect/#devices 抓到的真实 WebView 打卡页。',
                  ),
                  const SizedBox(height: 8),
                  Text(
                    CheckInWebViewPage.inspectedCheckInUrl,
                    maxLines: 3,
                    overflow: TextOverflow.ellipsis,
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
                  Text('打卡位置', style: theme.textTheme.titleMedium),
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
                ],
              ),
            ),
          ),
          const SizedBox(height: 12),
          Wrap(
            spacing: 12,
            runSpacing: 12,
            children: <Widget>[
              FilledButton.icon(
                onPressed: _isResolvingLoginInfo ? null : _fetchMobileLoginInfo,
                icon: const Icon(Icons.badge_outlined),
                label: Text(_isResolvingLoginInfo ? '查询中...' : '1. 查询移动端Token'),
              ),
              FilledButton.icon(
                onPressed: _isLoadingCheque ? null : _fetchChequeInfo,
                icon: const Icon(Icons.verified_outlined),
                label: Text(_isLoadingCheque ? '查询中...' : '2. 查询Cheque'),
              ),
              FilledButton.icon(
                onPressed: _isLoadingHistory
                    ? null
                    : () => _fetchRecentHistoryLocations(),
                icon: const Icon(Icons.history),
                label: Text(_isLoadingHistory ? '查询中...' : '3. 查询历史位置'),
              ),
              FilledButton.icon(
                onPressed: _fillWithCurrentCoordinates,
                icon: const Icon(Icons.gps_fixed),
                label: const Text('使用当前坐标'),
              ),
              FilledButton.icon(
                onPressed: _isResolvingLoginInfo || _isLoadingCheque
                    ? null
                    : _openCheckInPage,
                icon: const Icon(Icons.open_in_browser),
                label: const Text('4. 进入打卡页'),
              ),
            ],
          ),
          if (_historyLocations.isNotEmpty) ...<Widget>[
            const SizedBox(height: 12),
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    Text('最近5天打卡位置', style: theme.textTheme.titleMedium),
                    const SizedBox(height: 4),
                    Text(
                      '仅展示当前用户最近5天内的可用打卡位置',
                      style: theme.textTheme.bodySmall,
                    ),
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
        ],
      ),
    );
  }
}
