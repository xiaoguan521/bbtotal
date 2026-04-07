import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class NativeCheckInWebViewController {
  MethodChannel? _channel;

  void attach(
    int viewId,
    Future<dynamic> Function(MethodCall call) handler,
  ) {
    final MethodChannel nextChannel = MethodChannel(
      'bbtotal/native_check_in_webview/$viewId',
    );
    nextChannel.setMethodCallHandler(handler);
    _channel = nextChannel;
  }

  void detach() {
    _channel?.setMethodCallHandler(null);
    _channel = null;
  }

  Future<void> applyPreset() async {
    await _channel?.invokeMethod<void>('applyPreset');
  }

  Future<void> reload() async {
    await _channel?.invokeMethod<void>('reload');
  }
}

class NativeCheckInWebView extends StatefulWidget {
  const NativeCheckInWebView({
    super.key,
    required this.controller,
    required this.creationParams,
    this.onEvent,
  });

  final NativeCheckInWebViewController controller;
  final Map<String, dynamic> creationParams;
  final ValueChanged<Map<String, dynamic>>? onEvent;

  static const String viewType = 'bbtotal/native_check_in_webview';

  @override
  State<NativeCheckInWebView> createState() => _NativeCheckInWebViewState();
}

class _NativeCheckInWebViewState extends State<NativeCheckInWebView> {
  Future<dynamic> _handleNativeCall(MethodCall call) async {
    if (call.method != 'onEvent') {
      return null;
    }

    final Object? rawArguments = call.arguments;
    if (rawArguments is Map) {
      widget.onEvent?.call(
        rawArguments.map<String, dynamic>(
          (dynamic key, dynamic value) =>
              MapEntry(key.toString(), value),
        ),
      );
    }
    return null;
  }

  void _onPlatformViewCreated(int viewId) {
    widget.controller.attach(viewId, _handleNativeCall);
  }

  @override
  void dispose() {
    widget.controller.detach();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    switch (Theme.of(context).platform) {
      case TargetPlatform.android:
        return AndroidView(
          viewType: NativeCheckInWebView.viewType,
          creationParams: widget.creationParams,
          creationParamsCodec: const StandardMessageCodec(),
          onPlatformViewCreated: _onPlatformViewCreated,
        );
      case TargetPlatform.iOS:
        return UiKitView(
          viewType: NativeCheckInWebView.viewType,
          creationParams: widget.creationParams,
          creationParamsCodec: const StandardMessageCodec(),
          onPlatformViewCreated: _onPlatformViewCreated,
        );
      default:
        return const Center(
          child: Text('Only Android and iOS are supported.'),
        );
    }
  }
}
