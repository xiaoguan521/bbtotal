import 'package:flutter/material.dart';

import '../models/check_in_location_preset.dart';
import 'check_in_webview_page.dart';
import '../services/location_service.dart';

class CheckInLocationSetupPage extends StatefulWidget {
  const CheckInLocationSetupPage({super.key});

  @override
  State<CheckInLocationSetupPage> createState() =>
      _CheckInLocationSetupPageState();
}

class _CheckInLocationSetupPageState extends State<CheckInLocationSetupPage> {
  late final TextEditingController _addressController =
      TextEditingController(text: '河北省石家庄市鹿泉区御园路71号靠近光谷科技园');
  late final TextEditingController _longitudeController =
      TextEditingController(text: '114.347315');
  late final TextEditingController _latitudeController =
      TextEditingController(text: '38.050979');

  final LocationService _locationService = LocationService();

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

  void _openCheckInPage() {
    final String address = _addressController.text.trim();
    final double? longitude = double.tryParse(_longitudeController.text.trim());
    final double? latitude = double.tryParse(_latitudeController.text.trim());

    if (address.isEmpty || longitude == null || latitude == null) {
      setState(() {
        _status = 'Address, longitude and latitude are all required.';
      });
      return;
    }

    final preset = CheckInLocationPreset(
      address: address,
      longitude: longitude,
      latitude: latitude,
      province: '河北省',
      city: '石家庄市',
      district: '鹿泉区',
      street: '御园路',
      cityCode: '0311',
      provinceCode: '130000',
      adCode: '130110',
      provinceReferred: '冀',
    );

    Navigator.of(context).push(
      MaterialPageRoute<void>(
        builder: (_) => CheckInWebViewPage(preset: preset),
      ),
    );
  }

  @override
  void dispose() {
    _locationService.lastError.removeListener(_syncErrorState);
    _locationService.dispose();
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
                onPressed: _fillWithCurrentCoordinates,
                icon: const Icon(Icons.gps_fixed),
                label: const Text('使用当前坐标'),
              ),
              FilledButton.icon(
                onPressed: _openCheckInPage,
                icon: const Icon(Icons.open_in_browser),
                label: const Text('进入打卡页'),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
