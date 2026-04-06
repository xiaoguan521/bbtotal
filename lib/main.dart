import 'package:flutter/material.dart';
import 'pages/location_test_page.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const BbtotalApp());
}

class BbtotalApp extends StatelessWidget {
  const BbtotalApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Location Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xFF1F6FEB)),
        useMaterial3: true,
      ),
      home: const CheckInLocationSetupPage(),
    );
  }
}
