import 'package:flutter/material.dart';

import '../models/check_in_location_preset.dart';
import 'check_in_webview_bridge_bundle.dart';
import 'native_check_in_webview.dart';

class CheckInWebViewPage extends StatefulWidget {
  const CheckInWebViewPage({
    super.key,
    required this.preset,
  });

  static const String inspectedCheckInUrl =
      CheckInWebViewPageBridgeDefaults.inspectedCheckInUrl;

  final CheckInLocationPreset preset;

  @override
  State<CheckInWebViewPage> createState() => _CheckInWebViewPageState();
}

class _CheckInWebViewPageState extends State<CheckInWebViewPage> {
  late final CheckInWebViewBridgeBundle _bundle =
      CheckInWebViewBridgeBundle.fromPreset(widget.preset);
  final NativeCheckInWebViewController _nativeController =
      NativeCheckInWebViewController();

  bool _hideNativeChrome = false;
  bool _isLoading = true;
  String _pageDiagnostics = 'No bridge events yet.';
  String _status = 'Opening check-in page with the native bridge...';

  void _appendDiagnostic(String message) {
    final String current = _pageDiagnostics == 'No bridge events yet.'
        ? ''
        : _pageDiagnostics;
    _pageDiagnostics = current.isEmpty ? message : '$current\n$message';
  }

  void _handleBridgeControl(Map<String, dynamic> command) {
    final String type = (command['type'] ?? '').toString();
    if (type == 'hiddenTitle') {
      setState(() {
        _hideNativeChrome = true;
        _status = 'Native title hidden by bridge request.';
      });
      return;
    }
    if (type == 'showTitle') {
      setState(() {
        _hideNativeChrome = false;
        _status = 'Native title shown by bridge request.';
      });
      return;
    }
    if (type == 'openUrl') {
      final String url = (command['url'] ?? '').toString();
      setState(() {
        _status = 'H5 requested openUrl: $url';
      });
      return;
    }
    if (type == 'pop') {
      if (Navigator.of(context).canPop()) {
        Navigator.of(context).pop();
      }
      return;
    }
    if (type == 'reload') {
      _nativeController.reload();
    }
  }

  void _handleNativeEvent(Map<String, dynamic> event) {
    if (!mounted) {
      return;
    }

    final String type = (event['type'] ?? '').toString();
    switch (type) {
      case 'pageStarted':
        setState(() {
          _isLoading = true;
          _status = 'Loading check-in page...';
          _appendDiagnostic('pageStarted: ${event['url'] ?? ''}');
        });
        break;
      case 'pageFinished':
        setState(() {
          _isLoading = false;
          _status = 'Native bridge is ready before page scripts run.';
          _appendDiagnostic('pageFinished: ${event['url'] ?? ''}');
        });
        break;
      case 'bridgeControl':
        final Object? rawCommand = event['command'];
        if (rawCommand is Map) {
          final Map<String, dynamic> command = rawCommand.map<String, dynamic>(
            (dynamic key, dynamic value) => MapEntry(key.toString(), value),
          );
          _appendDiagnostic('bridgeControl: $command');
          _handleBridgeControl(command);
        }
        break;
      case 'log':
        setState(() {
          _appendDiagnostic('log: ${event['message'] ?? ''}');
        });
        break;
      case 'error':
        setState(() {
          _isLoading = false;
          _status = 'WebView error: ${event['description'] ?? 'unknown'}';
          _appendDiagnostic('error: ${event['description'] ?? ''}');
        });
        break;
      default:
        setState(() {
          _appendDiagnostic('event[$type]: $event');
        });
        break;
    }
  }

  @override
  Widget build(BuildContext context) {
    final TargetPlatform platform = Theme.of(context).platform;

    return Scaffold(
      appBar: _hideNativeChrome
          ? null
          : AppBar(
              title: const Text('打卡页面'),
              actions: <Widget>[
                IconButton(
                  tooltip: '手动注入定位',
                  onPressed: () {
                    _nativeController.applyPreset();
                  },
                  icon: const Icon(Icons.my_location_outlined),
                ),
                IconButton(
                  tooltip: '刷新页面',
                  onPressed: () {
                    _nativeController.reload();
                  },
                  icon: const Icon(Icons.refresh),
                ),
              ],
            ),
      body: Column(
        children: <Widget>[
          Material(
            color: const Color(0xFFF6F8FB),
            child: ListTile(
              dense: true,
              leading: const Icon(Icons.place_outlined),
              title: Text(widget.preset.address),
              subtitle: Text(
                '${widget.preset.loginInfo.username} | '
                '${widget.preset.coordinateText}',
              ),
              trailing: _isLoading
                  ? const SizedBox(
                      width: 20,
                      height: 20,
                      child: CircularProgressIndicator(strokeWidth: 2),
                    )
                  : const Icon(Icons.check_circle, color: Colors.green),
            ),
          ),
          Material(
            color: const Color(0xFFEFF5FF),
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
              child: Row(
                children: <Widget>[
                  const Icon(Icons.info_outline, size: 18),
                  const SizedBox(width: 8),
                  Expanded(child: Text(_status)),
                ],
              ),
            ),
          ),
          ExpansionTile(
            title: const Text('桥接状态'),
            subtitle: const Text('查看事件流和最终 URL'),
            childrenPadding: const EdgeInsets.fromLTRB(16, 0, 16, 16),
            children: <Widget>[
              SelectableText(
                'Final URL:\n${_bundle.resolvedUri}\n\n'
                'Origin Rule:\n${_bundle.allowedOriginRule}\n\n'
                'Events:\n$_pageDiagnostics',
              ),
            ],
          ),
          Expanded(
            child: NativeCheckInWebView(
              controller: _nativeController,
              creationParams: _bundle.creationParamsFor(platform),
              onEvent: _handleNativeEvent,
            ),
          ),
        ],
      ),
    );
  }
}
