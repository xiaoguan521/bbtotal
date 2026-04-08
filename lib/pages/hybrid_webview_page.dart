import 'dart:convert';
import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_inappwebview/flutter_inappwebview.dart';

import '../models/hybrid_runtime_context.dart';
import '../services/hybrid_bridge_service.dart';
import '../services/hybrid_diagnostics_service.dart';

class HybridWebViewPage extends StatefulWidget {
  const HybridWebViewPage({
    required this.runtimeContext,
    required this.title,
    super.key,
  });

  final HybridRuntimeContext runtimeContext;
  final String title;

  @override
  State<HybridWebViewPage> createState() => _HybridWebViewPageState();
}

class _HybridWebViewPageState extends State<HybridWebViewPage> {
  final HybridBridgeService _hybridBridgeService = const HybridBridgeService();
  final HybridDiagnosticsService _diagnostics = HybridDiagnosticsService();
  late final Map<String, String> _appCache =
      Map<String, String>.from(widget.runtimeContext.storageSeed);

  InAppWebViewController? _webViewController;
  bool _isLoading = true;
  double _webProgress = 0;

  void _log(String message) {
    _diagnostics.add(message);
  }

  void _showMessage(String message) {
    if (!mounted) {
      return;
    }

    ScaffoldMessenger.of(context)
      ..hideCurrentSnackBar()
      ..showSnackBar(SnackBar(content: Text(message)));
  }

  void _showLogsSheet() {
    showModalBottomSheet<void>(
      context: context,
      backgroundColor: const Color(0xFFF2F2F7),
      isScrollControlled: true,
      builder: (BuildContext context) {
        return SafeArea(
          top: false,
          child: Padding(
            padding: const EdgeInsets.fromLTRB(16, 12, 16, 24),
            child: SizedBox(
              height: MediaQuery.of(context).size.height * 0.58,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Center(
                    child: Container(
                      width: 40,
                      height: 5,
                      decoration: BoxDecoration(
                        color: const Color(0xFFD1D1D6),
                        borderRadius: BorderRadius.circular(999),
                      ),
                    ),
                  ),
                  const SizedBox(height: 18),
                  Row(
                    children: <Widget>[
                      const Text(
                        '诊断日志',
                        style: TextStyle(
                          fontSize: 22,
                          fontWeight: FontWeight.w700,
                        ),
                      ),
                      const Spacer(),
                      TextButton(
                        onPressed: _diagnostics.clear,
                        child: const Text('清空'),
                      ),
                    ],
                  ),
                  const SizedBox(height: 12),
                  Expanded(
                    child: Container(
                      width: double.infinity,
                      padding: const EdgeInsets.all(16),
                      decoration: BoxDecoration(
                        color: Colors.white,
                        borderRadius: BorderRadius.circular(24),
                      ),
                      child: AnimatedBuilder(
                        animation: _diagnostics,
                        builder: (BuildContext context, Widget? child) {
                          final String logText = _diagnostics.entries.isEmpty
                              ? 'No bridge events yet.'
                              : _diagnostics.entries.join('\n');
                          return SingleChildScrollView(
                            child: SelectableText(
                              logText,
                              style: const TextStyle(
                                fontSize: 13,
                                height: 1.45,
                                color: Color(0xFF3A3A3C),
                              ),
                            ),
                          );
                        },
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
        );
      },
    );
  }

  Future<void> _initializeWebView(InAppWebViewController controller) async {
    _webViewController = controller;
    _log('initializeWebView start: ${widget.runtimeContext.resolvedUri}');

    try {
      _hybridBridgeService.registerHandlers(
        controller: controller,
        onLog: (String message) => _log('js: $message'),
        onPostMessage: (String payload) => _log('postMessage: $payload'),
        onOpenUrl: (String url) {
          _log('openUrl requested: $url');
          _showMessage('页面请求打开新地址：$url');
        },
        onClosePage: () {
          _log('closePage requested');
          if (mounted) {
            Navigator.of(context).maybePop();
          }
        },
      );
      _log('initializeWebView handlers registered');

      await _hybridBridgeService.bootstrapCookies(
        widget.runtimeContext,
        onLog: _log,
      );

      final URLRequest request = URLRequest(
        url: WebUri(widget.runtimeContext.resolvedUri.toString()),
      );
      _log('initializeWebView loading url: ${request.url}');
      await controller.loadUrl(urlRequest: request);
      _log('initializeWebView loadUrl invoked');
    } catch (error, stackTrace) {
      _log('initializeWebView failed: $error');
      _log(stackTrace.toString());
      _showMessage('页面初始化失败：$error');
      if (mounted) {
        setState(() {
          _isLoading = false;
        });
      }
    }
  }

  Future<void> _reloadEmbeddedPage() async {
    await _webViewController?.reload();
  }

  Future<JsPromptResponse?> _handleJsPrompt(
    InAppWebViewController controller,
    JsPromptRequest jsPromptRequest,
  ) async {
    final String message = jsPromptRequest.message ?? '';
    _log('jsPrompt: $message');

    final Map<String, dynamic>? payload = _decodePromptPayload(message);
    if (payload == null) {
      return null;
    }

    final String functionName = (payload['function'] ?? '').toString();
    if (functionName.isEmpty) {
      return null;
    }

    final String result = await _handlePromptFunction(controller, payload);
    _log('jsPrompt handled: $functionName => $result');
    return JsPromptResponse(
      handledByClient: true,
      action: JsPromptResponseAction.CONFIRM,
      value: result,
    );
  }

  Map<String, dynamic>? _decodePromptPayload(String message) {
    try {
      final Object? decoded = jsonDecode(message);
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

  Future<String> _handlePromptFunction(
    InAppWebViewController controller,
    Map<String, dynamic> payload,
  ) async {
    final String functionName = (payload['function'] ?? '').toString();
    switch (functionName) {
      case 'getAppCache':
        return await _getAppCache(controller, payload);
      case 'setAppCache':
        return await _setAppCache(controller, payload);
      case 'removeAppCache':
        return await _removeAppCache(controller, payload);
      case 'clearAppCache':
        return await _clearAppCache(controller);
      default:
        _log('jsPrompt unsupported function: $functionName');
        return '';
    }
  }

  Future<String> _getAppCache(
    InAppWebViewController controller,
    Map<String, dynamic> payload,
  ) async {
    final String key = (payload['key'] ?? '').toString();
    if (key.isEmpty) {
      return '';
    }

    final String value = _appCache[key] ?? '';
    final String callbackScript = '''
(() => {
  const payload = ${jsonEncode(<String, dynamic>{
      'code': 0,
      'getAppCache': key,
      key: value,
    })};
  if (typeof appCacheResult === 'function') {
    appCacheResult(payload);
    return;
  }
  if (typeof window.appCacheResult === 'function') {
    window.appCacheResult(payload);
  }
})()
''';
    unawaited(
      Future<void>.delayed(const Duration(milliseconds: 1), () async {
        try {
          await controller.evaluateJavascript(source: callbackScript);
        } catch (error) {
          _log('getAppCache callback failed: $error');
        }
      }),
    );
    return '';
  }

  Future<String> _setAppCache(
    InAppWebViewController controller,
    Map<String, dynamic> payload,
  ) async {
    final String key = (payload['key'] ?? '').toString();
    final Object? rawValue = payload['value'] ?? payload['data'];
    final String value = rawValue == null ? '' : rawValue.toString();
    if (key.isEmpty) {
      return '';
    }

    _appCache[key] = value;
    unawaited(_syncAppCacheToPage(controller, key: key, value: value));
    return '';
  }

  Future<String> _removeAppCache(
    InAppWebViewController controller,
    Map<String, dynamic> payload,
  ) async {
    final String key = (payload['key'] ?? '').toString();
    if (key.isEmpty) {
      return '';
    }

    _appCache.remove(key);
    unawaited(_syncAppCacheToPage(controller, key: key, remove: true));
    return '';
  }

  Future<String> _clearAppCache(InAppWebViewController controller) async {
    _appCache.clear();
    unawaited(_syncAppCacheToPage(controller, clearAll: true));
    return '';
  }

  Future<void> _syncAppCacheToPage(
    InAppWebViewController controller, {
    String? key,
    String? value,
    bool remove = false,
    bool clearAll = false,
  }) async {
    final String script;
    if (clearAll) {
      script = '''
(() => {
  try {
    if (window.localStorage) window.localStorage.clear();
    if (window.sessionStorage) window.sessionStorage.clear();
  } catch (_) {}
})()
''';
    } else if (remove && key != null) {
      script = '''
(() => {
  const key = ${jsonEncode(key)};
  try {
    if (window.localStorage) window.localStorage.removeItem(key);
    if (window.sessionStorage) window.sessionStorage.removeItem(key);
  } catch (_) {}
})()
''';
    } else if (key != null) {
      script = '''
(() => {
  const key = ${jsonEncode(key)};
  const value = ${jsonEncode(value ?? '')};
  try {
    if (window.localStorage) window.localStorage.setItem(key, value);
    if (window.sessionStorage) window.sessionStorage.setItem(key, value);
  } catch (_) {}
})()
''';
    } else {
      return;
    }

    try {
      await controller.evaluateJavascript(source: script);
    } catch (error) {
      _log('syncAppCacheToPage failed: $error');
    }
  }

  @override
  void dispose() {
    _diagnostics.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFF2F2F7),
      appBar: AppBar(
        title: Text(widget.title),
        actions: <Widget>[
          IconButton(
            tooltip: '诊断日志',
            onPressed: _showLogsSheet,
            icon: const Icon(Icons.subject_rounded),
          ),
          IconButton(
            tooltip: '刷新',
            onPressed: _reloadEmbeddedPage,
            icon: const Icon(Icons.refresh_rounded),
          ),
        ],
      ),
      body: SafeArea(
        top: false,
        child: Column(
          children: <Widget>[
            if (_isLoading)
              LinearProgressIndicator(
                minHeight: 2,
                value:
                    _webProgress <= 0 || _webProgress >= 1 ? null : _webProgress,
                backgroundColor: const Color(0xFFE5E5EA),
                valueColor: const AlwaysStoppedAnimation<Color>(
                  Color(0xFF0A84FF),
                ),
              ),
            Expanded(
              child: InAppWebView(
                initialSettings: InAppWebViewSettings(
                  isInspectable: kDebugMode,
                  mediaPlaybackRequiresUserGesture: false,
                  allowsInlineMediaPlayback: true,
                  useShouldOverrideUrlLoading: true,
                ),
                initialUserScripts:
                    _hybridBridgeService.buildInitialUserScripts(
                  widget.runtimeContext,
                ),
                onWebViewCreated: _initializeWebView,
                onLoadStart: (controller, url) {
                  _log('loadStart: ${url ?? widget.runtimeContext.resolvedUri}');
                  if (!mounted) {
                    return;
                  }
                  setState(() {
                    _isLoading = true;
                  });
                },
                onLoadStop: (controller, url) {
                  _log('loadStop: ${url ?? widget.runtimeContext.resolvedUri}');
                  if (!mounted) {
                    return;
                  }
                  setState(() {
                    _isLoading = false;
                    _webProgress = 1;
                  });
                },
                onProgressChanged: (controller, progress) {
                  if (!mounted) {
                    return;
                  }
                  setState(() {
                    _webProgress = progress / 100;
                    _isLoading = progress < 100;
                  });
                },
                onConsoleMessage: (controller, consoleMessage) {
                  _log(
                    'console[${consoleMessage.messageLevel}]: ${consoleMessage.message}',
                  );
                },
                onJsPrompt: _handleJsPrompt,
                onReceivedError: (controller, request, error) {
                  _log('error: ${request.url} -> ${error.description}');
                  _showMessage('页面错误：${error.description}');
                  if (!mounted) {
                    return;
                  }
                  setState(() {
                    _isLoading = false;
                  });
                },
                shouldOverrideUrlLoading: (controller, navigationAction) async {
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
    );
  }
}
