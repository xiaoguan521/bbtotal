import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_inappwebview/flutter_inappwebview.dart';

import 'models/remote_bridge_bundle.dart';
import 'pages/hybrid_checkin_page.dart';
import 'services/remote_bridge_bundle_service.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();

  if (!kIsWeb && defaultTargetPlatform == TargetPlatform.android) {
    await InAppWebViewController.setWebContentsDebuggingEnabled(true);
  }

  final RemoteBridgeBundle remoteBridgeBundle =
      await RemoteBridgeBundleService().loadActiveBundle(
        onLog: (String message) => debugPrint('[remote-bridge] $message'),
      );

  runApp(BbtotalApp(remoteBridgeBundle: remoteBridgeBundle));
}

class BbtotalApp extends StatelessWidget {
  const BbtotalApp({
    super.key,
    this.remoteBridgeBundle = RemoteBridgeBundle.embeddedFallback,
  });

  final RemoteBridgeBundle remoteBridgeBundle;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '打卡入口',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: const ColorScheme.light(
          primary: Color(0xFF0A84FF),
          secondary: Color(0xFF5AC8FA),
          surface: Color(0xFFFFFFFF),
          error: Color(0xFFFF453A),
          onPrimary: Color(0xFFFFFFFF),
          onSecondary: Color(0xFFFFFFFF),
          onSurface: Color(0xFF111111),
          onError: Color(0xFFFFFFFF),
        ),
        scaffoldBackgroundColor: const Color(0xFFF2F2F7),
        splashFactory: NoSplash.splashFactory,
        useMaterial3: true,
        pageTransitionsTheme: const PageTransitionsTheme(
          builders: <TargetPlatform, PageTransitionsBuilder>{
            TargetPlatform.android: CupertinoPageTransitionsBuilder(),
            TargetPlatform.iOS: CupertinoPageTransitionsBuilder(),
            TargetPlatform.macOS: CupertinoPageTransitionsBuilder(),
          },
        ),
        appBarTheme: const AppBarTheme(
          backgroundColor: Color(0xFFF2F2F7),
          foregroundColor: Color(0xFF111111),
          elevation: 0,
          scrolledUnderElevation: 0,
          centerTitle: true,
        ),
        textTheme: Typography.blackCupertino,
      ),
      home: HybridCheckInPage(remoteBridgeBundle: remoteBridgeBundle),
    );
  }
}
