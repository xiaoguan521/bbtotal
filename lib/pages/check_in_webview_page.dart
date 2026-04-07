import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:webview_flutter_android/webview_flutter_android.dart';
import 'package:webview_flutter_wkwebview/webview_flutter_wkwebview.dart';

import '../models/check_in_location_preset.dart';
import 'check_in_webview_bridge_bundle.dart';

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
  static const String _androidViewType = 'bbtotal/native_checkin_webview';
  static const String _androidChannelPrefix = 'bbtotal/native_checkin_webview';

  late final CheckInWebViewBridgeBundle _bundle =
      CheckInWebViewBridgeBundle.fromPreset(widget.preset);
  late final bool _useNativeAndroidWebView =
      defaultTargetPlatform == TargetPlatform.android;

  WebViewController? _controller;
  MethodChannel? _androidChannel;

  bool _hideNativeChrome = false;
  bool _isLoading = true;
  bool _bridgeInjectedForCurrentPage = false;
  String _pageDiagnostics = 'No bridge events yet.';
  String _status = 'Opening check-in page...';

  @override
  void initState() {
    super.initState();
    if (_useNativeAndroidWebView) {
      _status = 'Opening check-in page with native Android WebView...';
      _appendDiagnostic(
        'log: Android native WebView path selected for document-start bridge.',
      );
    } else {
      _status = 'Opening check-in page with webview_flutter...';
      _controller = _createController();
    }
  }

  @override
  void dispose() {
    _androidChannel?.setMethodCallHandler(null);
    super.dispose();
  }

  void _appendDiagnostic(String message) {
    final String current = _pageDiagnostics == 'No bridge events yet.'
        ? ''
        : _pageDiagnostics;
    _pageDiagnostics = current.isEmpty ? message : '$current\n$message';
  }

  Map<String, dynamic>? _decodeCommand(String raw) {
    try {
      final Object decoded = jsonDecode(raw);
      if (decoded is Map<String, dynamic>) {
        return decoded;
      }
      if (decoded is Map) {
        return decoded.map<String, dynamic>(
          (dynamic key, dynamic value) => MapEntry(key.toString(), value),
        );
      }
    } catch (_) {}
    return null;
  }

  Map<String, dynamic> _normalizeMap(Object? value) {
    if (value is Map<String, dynamic>) {
      return value;
    }
    if (value is Map) {
      return value.map<String, dynamic>(
        (dynamic key, dynamic mapValue) => MapEntry(key.toString(), mapValue),
      );
    }
    return <String, dynamic>{};
  }

  WebViewController _createController() {
    late final PlatformWebViewControllerCreationParams params;
    if (WebViewPlatform.instance is WebKitWebViewPlatform) {
      params = WebKitWebViewControllerCreationParams(
        allowsInlineMediaPlayback: true,
        mediaTypesRequiringUserAction: const <PlaybackMediaTypes>{},
      );
    } else {
      params = const PlatformWebViewControllerCreationParams();
    }

    final WebViewController controller = WebViewController
        .fromPlatformCreationParams(params)
      ..setJavaScriptMode(JavaScriptMode.unrestricted)
      ..setBackgroundColor(const Color(0x00000000))
      ..addJavaScriptChannel(
        'bbtotalDebug',
        onMessageReceived: (JavaScriptMessage message) {
          if (!mounted) {
            return;
          }
          setState(() {
            _appendDiagnostic('log: ${message.message}');
          });
        },
      )
      ..addJavaScriptChannel(
        'bbtotalBridgeControl',
        onMessageReceived: (JavaScriptMessage message) {
          final Map<String, dynamic>? command = _decodeCommand(message.message);
          if (command == null) {
            return;
          }
          _handleBridgeControl(command);
        },
      )
      ..setNavigationDelegate(
        NavigationDelegate(
          onPageStarted: (String url) {
            _bridgeInjectedForCurrentPage = false;
            if (!mounted) {
              return;
            }
            setState(() {
              _isLoading = true;
              _status = 'Loading check-in page...';
              _appendDiagnostic('pageStarted: $url');
            });
            _injectBridge(reason: 'pageStarted');
          },
          onProgress: (int progress) {
            if (!_bridgeInjectedForCurrentPage && progress >= 5) {
              _injectBridge(reason: 'progress=$progress');
            }
          },
          onPageFinished: (String url) {
            _injectBridge(reason: 'pageFinished');
            if (!mounted) {
              return;
            }
            setState(() {
              _isLoading = false;
              _status =
                  'webview_flutter bridge installed. Android can be inspected via chrome://inspect.';
              _appendDiagnostic('pageFinished: $url');
            });
          },
          onWebResourceError: (WebResourceError error) {
            if (!mounted) {
              return;
            }
            setState(() {
              _isLoading = false;
              _status = 'WebView error: ${error.description}';
              _appendDiagnostic('error: ${error.description}');
            });
          },
        ),
      )
      ..loadRequest(_bundle.resolvedUri);

    if (controller.platform is AndroidWebViewController) {
      AndroidWebViewController.enableDebugging(true);
      final AndroidWebViewController androidController =
          controller.platform as AndroidWebViewController;
      androidController.setMediaPlaybackRequiresUserGesture(false);
      _appendDiagnostic(
        'log: Android WebView debugging enabled. Use chrome://inspect.',
      );
    }

    return controller;
  }

  Future<void> _handleAndroidViewCreated(int viewId) async {
    final MethodChannel channel =
        MethodChannel('$_androidChannelPrefix' '_$viewId');
    _androidChannel = channel;
    channel.setMethodCallHandler(_handleAndroidMethodCall);

    if (!mounted) {
      return;
    }
    setState(() {
      _appendDiagnostic('log: Android native WebView created (viewId=$viewId).');
    });

    try {
      await channel.invokeMethod<void>('initialize');
      if (!mounted) {
        return;
      }
      setState(() {
        _appendDiagnostic('log: Android native WebView initialized.');
      });
    } catch (error) {
      if (!mounted) {
        return;
      }
      setState(() {
        _isLoading = false;
        _status = 'Native Android WebView initialization failed: $error';
        _appendDiagnostic('error: nativeInitFailed: $error');
      });
    }
  }

  Future<Object?> _handleAndroidMethodCall(MethodCall call) async {
    if (!mounted) {
      return null;
    }

    switch (call.method) {
      case 'log':
        final String message = (call.arguments ?? '').toString();
        setState(() {
          _appendDiagnostic('log: $message');
        });
        return null;
      case 'pageStarted':
        final String url = (call.arguments ?? '').toString();
        _bridgeInjectedForCurrentPage = true;
        setState(() {
          _isLoading = true;
          _status = 'Loading check-in page...';
          _appendDiagnostic('pageStarted: $url');
        });
        return null;
      case 'pageFinished':
        final String url = (call.arguments ?? '').toString();
        setState(() {
          _isLoading = false;
          _status =
              'Android native bridge ready. WebView can be inspected via chrome://inspect.';
          _appendDiagnostic('pageFinished: $url');
        });
        return null;
      case 'progress':
        final int progress = switch (call.arguments) {
          final int value => value,
          final num value => value.toInt(),
          _ => 0,
        };
        if (progress == 100 || progress == 5) {
          setState(() {
            _appendDiagnostic('progress: $progress');
          });
        }
        return null;
      case 'error':
        final Map<String, dynamic> error = _normalizeMap(call.arguments);
        final String description = (error['description'] ?? 'unknown').toString();
        setState(() {
          _isLoading = false;
          _status = 'WebView error: $description';
          _appendDiagnostic('error: $description');
        });
        return null;
      case 'bridgeControl':
        final String raw = (call.arguments ?? '').toString();
        final Map<String, dynamic>? command = _decodeCommand(raw);
        if (command == null) {
          setState(() {
            _appendDiagnostic('bridgeControlRaw: $raw');
          });
          return null;
        }
        _handleBridgeControl(command);
        return null;
      default:
        setState(() {
          _appendDiagnostic('nativeCall: ${call.method} ${call.arguments}');
        });
        return null;
    }
  }

  Future<void> _reloadPage() async {
    if (_useNativeAndroidWebView) {
      final MethodChannel? channel = _androidChannel;
      if (channel == null) {
        if (!mounted) {
          return;
        }
        setState(() {
          _appendDiagnostic('reloadSkipped: Android native channel not ready yet.');
        });
        return;
      }
      await channel.invokeMethod<void>('reload');
      return;
    }

    await _controller?.reload();
  }

  Future<void> _injectBridge({required String reason}) async {
    if (_useNativeAndroidWebView) {
      final MethodChannel? channel = _androidChannel;
      if (channel == null) {
        if (!mounted) {
          return;
        }
        setState(() {
          _appendDiagnostic(
            'bridgeApplySkipped[$reason]: Android native channel not ready yet.',
          );
        });
        return;
      }

      try {
        await channel.invokeMethod<void>('applyPreset');
        _bridgeInjectedForCurrentPage = true;

        if (!mounted) {
          return;
        }
        setState(() {
          _appendDiagnostic('bridgePresetApplied: $reason');
        });
      } catch (error) {
        if (!mounted) {
          return;
        }
        setState(() {
          _appendDiagnostic('bridgePresetApplyFailed[$reason]: $error');
        });
      }
      return;
    }

    try {
      await _controller!.runJavaScript(_bundle.flutterRuntimeScript);
      await _controller!.runJavaScript(
        'window.__bbtotalApplyPreset && window.__bbtotalApplyPreset();',
      );
      _bridgeInjectedForCurrentPage = true;

      if (!mounted) {
        return;
      }
      setState(() {
        _appendDiagnostic('bridgeInjected: $reason');
      });
    } catch (error) {
      if (!mounted) {
        return;
      }
      setState(() {
        _appendDiagnostic('bridgeInjectionFailed[$reason]: $error');
      });
    }
  }

  void _handleBridgeControl(Map<String, dynamic> command) {
    final String type = (command['type'] ?? '').toString();
    if (type == 'hiddenTitle') {
      setState(() {
        _hideNativeChrome = true;
        _status = 'Native title hidden by bridge request.';
        _appendDiagnostic('bridgeControl: $command');
      });
      return;
    }
    if (type == 'showTitle') {
      setState(() {
        _hideNativeChrome = false;
        _status = 'Native title shown by bridge request.';
        _appendDiagnostic('bridgeControl: $command');
      });
      return;
    }
    if (type == 'openUrl') {
      final String url = (command['url'] ?? command['openUrl'] ?? '').toString();
      setState(() {
        _status = 'H5 requested openUrl: $url';
        _appendDiagnostic('bridgeControl: $command');
      });
      return;
    }
    if (type == 'pop') {
      if (Navigator.of(context).canPop()) {
        Navigator.of(context).pop();
      }
      return;
    }
    if (type == 'reload' || command['function'] == 'reloadData') {
      _reloadPage();
    }
    setState(() {
      _appendDiagnostic('bridgeControl: $command');
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: _hideNativeChrome
          ? null
          : AppBar(
              title: const Text('打卡页面'),
              actions: <Widget>[
                IconButton(
                  tooltip: '手动注入定位',
                  onPressed: () {
                    _injectBridge(reason: 'manual');
                  },
                  icon: const Icon(Icons.my_location_outlined),
                ),
                IconButton(
                  tooltip: '刷新页面',
                  onPressed: () {
                    _reloadPage();
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
            child: _useNativeAndroidWebView
                ? AndroidView(
                    viewType: _androidViewType,
                    creationParams: _bundle.creationParamsFor(
                      TargetPlatform.android,
                    ),
                    creationParamsCodec: const StandardMessageCodec(),
                    onPlatformViewCreated: _handleAndroidViewCreated,
                  )
                : WebViewWidget(controller: _controller!),
          ),
        ],
      ),
    );
  }
}
