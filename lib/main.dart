import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_inappwebview/flutter_inappwebview.dart';

import 'pages/hybrid_checkin_page.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();

  if (!kIsWeb && defaultTargetPlatform == TargetPlatform.android) {
    await InAppWebViewController.setWebContentsDebuggingEnabled(true);
  }

  runApp(const BbtotalApp());
}

class BbtotalApp extends StatelessWidget {
  const BbtotalApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'BBTotal',
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
      home: const HybridCheckInPage(),
    );
  }
}
