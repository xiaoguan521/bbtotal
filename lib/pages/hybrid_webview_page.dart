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

  bool get _showDiagnosticsControls => kDebugMode;

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
    if (!_showDiagnosticsControls) {
      return;
    }
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
        onPostMessage: (String payload) {
          _log('postMessage: $payload');
          unawaited(_handleOpenUrlRequest(payload, source: 'postMessage'));
        },
        onOpenUrl: (String url) {
          _log('openUrl requested: $url');
          unawaited(_handleOpenUrlRequest(url, source: 'bridge'));
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

  Future<void> _handleOpenUrlRequest(
    String rawPayload, {
    required String source,
  }) async {
    final _HybridNavigationTarget? target = _resolveNavigationTarget(rawPayload);
    if (target == null) {
      _log('openUrl ignored ($source): empty or unrecognized payload');
      return;
    }

    _log('openUrl resolved ($source): ${target.uri}');

    if (!target.preferExternal && _isEmbeddableUri(target.uri)) {
      await _openChildPage(target);
      return;
    }

    if (target.uri.scheme == 'tel') {
      _showMessage('当前容器暂不支持直接拨号：${target.uri.path}');
      return;
    }

    _showMessage('页面请求打开外部地址：${target.uri}');
  }

  Future<void> _openChildPage(_HybridNavigationTarget target) async {
    if (!mounted) {
      return;
    }

    final HybridRuntimeContext childContext = HybridRuntimeContext.fromInputs(
      baseUrl: target.uri.toString(),
      loginInfo: widget.runtimeContext.loginInfo,
      preset: widget.runtimeContext.preset,
      launchPayload: target.launchPayload,
    );

    final MaterialPageRoute<void> route = MaterialPageRoute<void>(
      builder: (BuildContext context) => HybridWebViewPage(
        runtimeContext: childContext,
        title: target.title,
      ),
    );

    if (target.replaceCurrent) {
      await Navigator.of(context).pushReplacement(route);
      return;
    }

    await Navigator.of(context).push(route);
  }

  bool _isEmbeddableUri(Uri uri) {
    return <String>{'http', 'https', 'file', 'about', 'data'}.contains(uri.scheme);
  }

  _HybridNavigationTarget? _resolveNavigationTarget(String rawPayload) {
    final String payload = rawPayload.trim();
    if (payload.isEmpty) {
      return null;
    }

    final Map<String, dynamic>? decoded = _decodePromptPayload(payload);
    if (decoded != null) {
      final String? pushUrl = _firstNonEmptyString(<Object?>[decoded['pushURL']]);
      final String? embeddedUrl = _firstNonEmptyString(<Object?>[
        pushUrl,
        decoded['url'],
        decoded['href'],
      ]);
      final String? externalUrl = _firstNonEmptyString(<Object?>[
        decoded['openUrl'],
      ]);
      final Uri? uri = embeddedUrl != null
          ? _parseTargetUri(embeddedUrl)
          : _parseExternalUri(externalUrl);
      if (uri == null) {
        return null;
      }
      return _HybridNavigationTarget(
        uri: uri,
        preferExternal: embeddedUrl == null && externalUrl != null,
        replaceCurrent: (decoded['push']?.toString() == '1') &&
            (decoded['hidden']?.toString() == '1'),
        launchPayload: decoded,
        title:
            _firstNonEmptyString(<Object?>[
              decoded['title'],
              decoded['hqsxmc'],
              decoded['name'],
            ]) ??
            _deriveTitleFromUri(uri),
      );
    }

    final Uri? uri = _parseTargetUri(payload);
    if (uri == null) {
      return null;
    }
    return _HybridNavigationTarget(
      uri: uri,
      title: _deriveTitleFromUri(uri),
    );
  }

  String? _firstNonEmptyString(List<Object?> values) {
    for (final Object? value in values) {
      final String text = value?.toString().trim() ?? '';
      if (text.isNotEmpty) {
        return text;
      }
    }
    return null;
  }

  Uri? _parseTargetUri(String? rawUrl) {
    final String value = rawUrl?.trim() ?? '';
    if (value.isEmpty) {
      return null;
    }

    Uri? uri = Uri.tryParse(value);
    uri ??= Uri.tryParse(Uri.encodeFull(value));
    if (uri == null) {
      return null;
    }
    if (uri.hasScheme) {
      return uri;
    }
    return widget.runtimeContext.resolvedUri.resolveUri(uri);
  }

  Uri? _parseExternalUri(String? rawUrl) {
    final String value = rawUrl?.trim() ?? '';
    if (value.isEmpty) {
      return null;
    }

    if (value.startsWith('http://') || value.startsWith('https://')) {
      return Uri.tryParse(value);
    }

    return Uri.tryParse('http://$value');
  }

  String _deriveTitleFromUri(Uri uri) {
    final String? directTitle = uri.queryParameters['title'];
    if (directTitle != null && directTitle.trim().isNotEmpty) {
      return directTitle;
    }

    final String fragment = uri.fragment;
    if (fragment.isNotEmpty) {
      final Uri? fragmentUri = Uri.tryParse('https://placeholder$fragment');
      final String? fragmentTitle = fragmentUri?.queryParameters['title'];
      if (fragmentTitle != null && fragmentTitle.trim().isNotEmpty) {
        return fragmentTitle;
      }
    }

    return widget.title;
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
          if (_showDiagnosticsControls)
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
                  javaScriptCanOpenWindowsAutomatically: true,
                  supportMultipleWindows: true,
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
                onCreateWindow: (controller, createWindowAction) async {
                  final WebUri? webUri = createWindowAction.request.url;
                  final String rawTarget = webUri?.toString() ?? '';
                  _log('createWindow requested: $rawTarget');
                  if (rawTarget.isNotEmpty) {
                    unawaited(
                      _handleOpenUrlRequest(rawTarget, source: 'window.open'),
                    );
                  }
                  return false;
                },
                onJsPrompt: _handleJsPrompt,
                onReceivedError: (controller, request, error) {
                  _log('error: ${request.url} -> ${error.description}');
                  if (request.isForMainFrame ?? true) {
                    _showMessage('页面错误：${error.description}');
                  }
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

class _HybridNavigationTarget {
  const _HybridNavigationTarget({
    required this.uri,
    required this.title,
    this.preferExternal = false,
    this.replaceCurrent = false,
    this.launchPayload = const <String, dynamic>{},
  });

  final Uri uri;
  final String title;
  final bool preferExternal;
  final bool replaceCurrent;
  final Map<String, dynamic> launchPayload;
}
