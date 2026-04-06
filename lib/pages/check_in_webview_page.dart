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
  bool _hideNativeChrome = false;
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
              _appendDiagnostic(message.message);
            });
          },
        )
        ..addJavaScriptChannel(
          'bbtotalBridgeControl',
          onMessageReceived: (JavaScriptMessage message) async {
            if (!mounted) {
              return;
            }

            final Map<String, dynamic>? command = _decodeCommand(message.message);
            if (command == null) {
              return;
            }

            final String type = (command['type'] ?? '').toString();
            if (type == 'hiddenTitle') {
              setState(() {
                _hideNativeChrome = true;
                _status = 'Native title hidden by H5 bridge request.';
              });
            } else if (type == 'showTitle') {
              setState(() {
                _hideNativeChrome = false;
                _status = 'Native title shown by H5 bridge request.';
              });
            } else if (type == 'openUrl') {
              final String url = (command['url'] ?? '').toString();
              setState(() {
                _status = 'H5 requested openUrl: $url';
              });
            } else if (type == 'pop') {
              if (Navigator.of(context).canPop()) {
                Navigator.of(context).pop();
              }
            } else if (type == 'reload') {
              await _controller.reload();
            }
            _appendDiagnostic('bridge-control: ${message.message}');
            setState(() {
              _pageDiagnostics = _pageDiagnostics;
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

  void _appendDiagnostic(String message) {
    final String current = _pageDiagnostics == 'No page diagnostics yet.'
        ? ''
        : _pageDiagnostics;
    final String next = current.isEmpty ? message : '$current\n$message';
    _pageDiagnostics = next;
  }

  Map<String, dynamic>? _decodeCommand(String raw) {
    try {
      final Object decoded = jsonDecode(raw);
      if (decoded is Map<String, dynamic>) {
        return decoded;
      }
      if (decoded is Map) {
        return decoded.map(
          (key, dynamic value) => MapEntry(key.toString(), value),
        );
      }
    } catch (_) {}
    return null;
  }

  Future<void> _injectPresetIntoPage() async {
    try {
      await _controller.runJavaScript(_buildInjectionScript(widget.preset));
      if (!mounted) {
        return;
      }

      setState(() {
        _isLoading = false;
        _status = 'Location bridge installed. Waiting for the H5 page to request location.';
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
  const postDebug = (message) => {
    try {
      window.bbtotalDebug.postMessage(message);
    } catch (_) {}
  };
  const postBridgeControl = (command) => {
    try {
      window.bbtotalBridgeControl.postMessage(JSON.stringify(command));
    } catch (_) {}
  };

  const installConsoleHooks = () => {
    const wrap = (level) => {
      const original = console[level];
      console[level] = (...args) => {
        postDebug('console.' + level + ': ' + args.map((item) => {
          try {
            return typeof item === 'string' ? item : JSON.stringify(item);
          } catch (_) {
            return String(item);
          }
        }).join(' '));
        if (original) {
          original.apply(console, args);
        }
      };
    };

    wrap('log');
    wrap('warn');
    wrap('error');

    window.addEventListener('error', (event) => {
      postDebug('window.onerror: ' + (event.message || 'unknown error'));
    });

    window.addEventListener('unhandledrejection', (event) => {
      postDebug('unhandledrejection: ' + String(event.reason || 'unknown reason'));
    });
  };

  const buildWifiInfo = () => ({
    ssid: '',
    bssid: '',
    wifiName: '',
    wifiMac: '',
    isWifi: false,
    device: 'flutter-webview',
  });

  const handleCommand = (input) => {
    let command = input;
    try {
      if (typeof input === 'string') {
        command = JSON.parse(input);
      }
    } catch (_) {}

    if (!command || typeof command !== 'object') {
      postDebug('bbtotal bridge -> unsupported command: ' + String(input));
      return '';
    }

    const type = command.type || command.function || '';
    if (type === 'getLocation' || type === 'getUpdatingLocation') {
      postDebug('bbtotal bridge -> command ' + type);
      applyPayload();
      return JSON.stringify(payload);
    }

    if (type === 'hiddenTitle') {
      postDebug('bbtotal bridge -> hiddenTitle()');
      postBridgeControl({ type: 'hiddenTitle' });
      return '';
    }

    if (type === 'config') {
      postDebug('bbtotal bridge -> config ' + JSON.stringify(command));
      if (command.hideNav === 'YES' || command.hideNav === true) {
        postBridgeControl({ type: 'hiddenTitle' });
      } else if (command.hideNav === 'NO' || command.hideNav === false) {
        postBridgeControl({ type: 'showTitle' });
      }
      return '';
    }

    if (type === 'getWifiinfo') {
      const wifiInfo = buildWifiInfo();
      postDebug('bbtotal bridge -> getWifiinfo() ' + JSON.stringify(wifiInfo));
      if (typeof window.getWifiinfoResult === 'function') {
        window.getWifiinfoResult(JSON.stringify(wifiInfo));
      }
      return JSON.stringify(wifiInfo);
    }

    if (command.push || command.pushURL) {
      postDebug('bbtotal bridge -> push ' + JSON.stringify(command));
      return '';
    }

    if (command.pop) {
      postDebug('bbtotal bridge -> pop ' + JSON.stringify(command));
      postBridgeControl({ type: 'pop' });
      return '';
    }

    if (command.openUrl) {
      postDebug('bbtotal bridge -> openUrl ' + command.openUrl);
      postBridgeControl({ type: 'openUrl', url: command.openUrl });
      return '';
    }

    if (type === 'reloadData') {
      postDebug('bbtotal bridge -> reloadData');
      postBridgeControl({ type: 'reload' });
      return '';
    }

    if (type === 'launchMiniProgram') {
      postDebug('bbtotal bridge -> launchMiniProgram ' + JSON.stringify(command));
      return '';
    }

    if (type === 'yuyueMeeting' ||
        type === 'endMeeting' ||
        type === 'joinyuyueMeeting' ||
        type === 'joinyuyueZhibo') {
      postDebug('bbtotal bridge -> native meeting action ' + JSON.stringify(command));
      return '';
    }

    postDebug('bbtotal bridge -> passthrough command ' + JSON.stringify(command));
    return '';
  };

  const applyPayload = () => {
    try {
      if (!window.bbgrxx || typeof window.bbgrxx !== 'object') {
        window.bbgrxx = {};
      }

      window.bbgrxx.blqd = window.bbgrxx.blqd || 'app_02';
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
        postDebug('bbtotal applyPayload -> window.locationResult(payload)');
      } else if (
        window.onBasicFormRef &&
        typeof window.onBasicFormRef.updateGhsxLoaction === 'function'
      ) {
        window.onBasicFormRef.updateGhsxLoaction();
        postDebug('bbtotal applyPayload -> updateGhsxLoaction()');
      }

      return true;
    } catch (error) {
      postDebug('bbtotal applyPayload error: ' + String(error));
      return false;
    }
  };

  const installBridge = () => {
    window.__bbtotalPreset = payload;
    window.__bbtotalApplyPreset = applyPayload;
    window.__bbtotalHandleNativeCommand = handleCommand;

    window.SYAppModel = window.SYAppModel || {};
    window.SYAppModel.getLocation = () => {
      postDebug('bbtotal bridge -> SYAppModel.getLocation()');
      applyPayload();
      return JSON.stringify(payload);
    };
    window.SYAppModel.getUpdatingLocation = () => {
      postDebug('bbtotal bridge -> SYAppModel.getUpdatingLocation()');
      applyPayload();
      return JSON.stringify(payload);
    };
    window.SYAppModel.postMessage = (message) => handleCommand(message);
    window.SYAppModel.hiddenTitle = () => handleCommand({ type: 'hiddenTitle' });
    window.SYAppModel.hideNav = (hidden) =>
      handleCommand({ function: 'config', hideNav: hidden ? 'YES' : 'NO' });
    window.SYAppModel.getWifiinfo = () => handleCommand({ type: 'getWifiinfo' });
    window.SYAppModel.openUrl = (url) => handleCommand({ openUrl: url });
    window.SYAppModel.reloadData = () => handleCommand({ function: 'reloadData' });
    window.SYAppModel.yuyueMeeting = (message) => handleCommand({ type: 'yuyueMeeting', params: message });
    window.SYAppModel.endMeeting = (message) => handleCommand({ type: 'endMeeting', params: message });
    window.SYAppModel.joinyuyueMeeting = (message) => handleCommand({ type: 'joinyuyueMeeting', params: message });
    window.SYAppModel.joinyuyueZhibo = (message) => handleCommand({ type: 'joinyuyueZhibo', params: message });

    window.webkit = window.webkit || {};
    window.webkit.messageHandlers = window.webkit.messageHandlers || {};
    window.webkit.messageHandlers.SYAppModel = {
      postMessage: (message) => handleCommand(message),
    };

    const originalPrompt = window.prompt ? window.prompt.bind(window) : null;
    window.prompt = (message, defaultValue) => {
      if (typeof message === 'string' && message.includes('launchMiniProgram')) {
        handleCommand(message);
        return '';
      }
      return originalPrompt ? originalPrompt(message, defaultValue) : '';
    };
  };

  installConsoleHooks();
  window.__bbtotalPreset = payload;
  window.__bbtotalApplyPreset = applyPayload;
  installBridge();
  postDebug('bbtotal bridge installed');
})();
''';
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
                  onPressed: () async {
                    await _controller.runJavaScript(
                      'window.__bbtotalApplyPreset && window.__bbtotalApplyPreset();',
                    );
                    if (!mounted) {
                      return;
                    }
                    setState(() {
                      _status = 'Manual location injection triggered.';
                    });
                  },
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
