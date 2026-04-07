import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_inappwebview/flutter_inappwebview.dart';

import 'pages/hybrid_checkin_page.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();

  if (!kIsWeb && defaultTargetPlatform == TargetPlatform.android) {
    await InAppWebViewController.setWebContentsDebuggingEnabled(kDebugMode);
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
        colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xFF1F6FEB)),
        useMaterial3: true,
      ),
      home: const HybridCheckInPage(),
    );
  }
}
