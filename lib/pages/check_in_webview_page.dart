import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:webview_flutter/webview_flutter.dart';

import '../models/check_in_location_preset.dart';

class CheckInWebViewPage extends StatefulWidget {
  const CheckInWebViewPage({
    super.key,
    required this.preset,
  });

  static const String inspectedCheckInUrl =
      'https://appsy.jbysoft.com/dxgl-app/dxslgl/#/dxslglComp/dxslglfq/index?bm=03060&dx_29_sjdxsl=3517&tyLoginToken=6d77b5d26961f35f56e827e81558da4710:app_02&ticket=nothing&cheque=tyrz029860a4855f44e5a5899641ac483483&zzjgdmz=shineyue&cpbs=bbPro&qycode=shineyue&ztConfig=7171a46ae52cd9c7c0278a72c65492b0fd234129620126cd57ac2fccaaec061b0abed44bcb02786c4057648caf5b99b6c4e7488567e1d5fa7399b6cf5fdeb7ea09b27db192ee5320e008c8c888a156ab41012aa0eea91e2a034cd260ee71bc6e6c20b42e49633af74bfc77a9e1142c02c45548a3e3e89cc6a93eda4ca371a379&zjhm=130528198704157217&khbh=01000000009000026129&jgbm=130110000102a705&userid=3517&zxbm=1301100001&cysxFlag=false';

  final CheckInLocationPreset preset;

  @override
  State<CheckInWebViewPage> createState() => _CheckInWebViewPageState();
}

class _CheckInWebViewPageState extends State<CheckInWebViewPage> {
  late final Uri _resolvedUri =
      widget.preset.buildCheckInUri(CheckInWebViewPage.inspectedCheckInUrl);
  late final WebViewController _controller =
      WebViewController()
        ..setJavaScriptMode(JavaScriptMode.unrestricted)
        ..addJavaScriptChannel(
          'bbtotalDebug',
          onMessageReceived: (JavaScriptMessage message) {
            if (!mounted) {
              return;
            }
            setState(() {
              _pageDiagnostics = message.message;
            });
          },
        )
        ..setNavigationDelegate(
          NavigationDelegate(
            onPageStarted: (String url) {
              if (!mounted) {
                return;
              }
              setState(() {
                _isLoading = true;
                _status = 'Loading check-in page...';
              });
            },
            onPageFinished: (String url) async {
              await _injectPresetIntoPage();
              await Future<void>.delayed(const Duration(seconds: 1));
              await _collectPageDiagnostics();
            },
            onWebResourceError: (WebResourceError error) {
              if (!mounted) {
                return;
              }
              setState(() {
                _isLoading = false;
                _status = 'WebView error: ${error.description}';
              });
            },
          ),
        )
        ..loadRequest(_resolvedUri);

  bool _isLoading = true;
  String _pageDiagnostics = 'No page diagnostics yet.';
  String _status = 'Opening check-in page...';

  Future<void> _injectPresetIntoPage() async {
    try {
      await _controller.runJavaScript(_buildInjectionScript(widget.preset));
      if (!mounted) {
        return;
      }

      setState(() {
        _isLoading = false;
        _status = 'Preset location injected into the H5 check-in page.';
      });
    } catch (error) {
      if (!mounted) {
        return;
      }

      setState(() {
        _isLoading = false;
        _status = 'Injection failed: $error';
      });
    }
  }

  Future<void> _collectPageDiagnostics() async {
    try {
      final Object result = await _controller.runJavaScriptReturningResult('''
(() => {
  const bodyText = document.body ? document.body.innerText : '';
  const candidates = Array.from(document.querySelectorAll('div, span, p, button, a'))
    .map((el) => (el.innerText || '').trim())
    .filter((text) => text && (
      text.includes('失败') ||
      text.includes('错误') ||
      text.includes('重试') ||
      text.includes('异常')
    ));

  return JSON.stringify({
    href: location.href,
    title: document.title,
    bodyText: bodyText.slice(0, 1200),
    candidateErrors: candidates.slice(0, 12),
  });
})();
''');

      final String raw = result.toString();
      final String normalized = raw.startsWith('"') ? jsonDecode(raw) as String : raw;
      if (!mounted) {
        return;
      }
      setState(() {
        _pageDiagnostics = normalized;
      });
    } catch (error) {
      if (!mounted) {
        return;
      }
      setState(() {
        _pageDiagnostics = 'Collect diagnostics failed: $error';
      });
    }
  }

  String _buildInjectionScript(CheckInLocationPreset preset) {
    final String payloadJson = jsonEncode(preset.toBridgePayload());
    return '''
(() => {
  const payload = $payloadJson;

  const applyPayload = () => {
    try {
      if (!window.bbgrxx || typeof window.bbgrxx !== 'object') {
        window.bbgrxx = {};
      }

      window.bbgrxx.locationMsg = payload;
      window.bbgrxx.updatingLocationMsg = payload;

      if (window.Se && typeof window.Se === 'object') {
        window.Se.locationMsg = payload;
      }

      const textarea = document.querySelector('#textarea-dx_29_dkwz');
      if (textarea) {
        textarea.value = payload.data.address || '';
        textarea.innerText = payload.data.address || '';
        ['input', 'change', 'blur'].forEach((eventName) => {
          textarea.dispatchEvent(new Event(eventName, { bubbles: true }));
        });
      }

      if (typeof window.locationResult === 'function') {
        window.locationResult(payload);
      } else if (
        window.onBasicFormRef &&
        typeof window.onBasicFormRef.updateGhsxLoaction === 'function'
      ) {
        window.onBasicFormRef.updateGhsxLoaction();
      }

      return true;
    } catch (error) {
      console.log('bbtotal applyPayload error', error);
      return false;
    }
  };

  const installBridge = () => {
    window.__bbtotalPreset = payload;
    window.__bbtotalApplyPreset = applyPayload;

    window.SYAppModel = window.SYAppModel || {};
    window.SYAppModel.getLocation = () => {
      applyPayload();
      return JSON.stringify(payload);
    };
    window.SYAppModel.getUpdatingLocation = () => {
      applyPayload();
      return JSON.stringify(payload);
    };

    window.webkit = window.webkit || {};
    window.webkit.messageHandlers = window.webkit.messageHandlers || {};
    window.webkit.messageHandlers.SYAppModel = {
      postMessage: (message) => {
        let type = '';
        try {
          if (typeof message === 'string') {
            type = JSON.parse(message).type || '';
          } else if (message && typeof message === 'object') {
            type = message.type || '';
          }
        } catch (_) {}

        if (!type || type === 'getLocation' || type === 'getUpdatingLocation') {
          applyPayload();
        }
      },
    };
  };

  installBridge();
  applyPayload();

  let attempts = 0;
  const timer = setInterval(() => {
    attempts += 1;
    const done = applyPayload();
    if (done || attempts >= 20) {
      clearInterval(timer);
    }
  }, 600);
})();
''';
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('打卡页面'),
        actions: <Widget>[
          IconButton(
            tooltip: '重新注入定位',
            onPressed: _injectPresetIntoPage,
            icon: const Icon(Icons.my_location_outlined),
          ),
          IconButton(
            tooltip: '刷新页面',
            onPressed: () {
              _controller.reload();
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
            title: const Text('诊断信息'),
            subtitle: const Text('查看最终 URL 和页面错误内容'),
            childrenPadding: const EdgeInsets.fromLTRB(16, 0, 16, 16),
            children: <Widget>[
              SelectableText(
                'Final URL:\n${_resolvedUri.toString()}\n\n'
                'Diagnostics:\n$_pageDiagnostics',
              ),
            ],
          ),
          Expanded(
            child: WebViewWidget(controller: _controller),
          ),
        ],
      ),
    );
  }
}
