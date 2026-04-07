import 'dart:collection';

import 'package:flutter/foundation.dart';

class HybridDiagnosticsService extends ChangeNotifier {
  HybridDiagnosticsService({
    this.maxEntries = 200,
  });

  final int maxEntries;
  final List<String> _entries = <String>[];

  UnmodifiableListView<String> get entries => UnmodifiableListView(_entries);

  void add(String message) {
    final DateTime now = DateTime.now();
    final String timestamp =
        '${now.hour.toString().padLeft(2, '0')}:'
        '${now.minute.toString().padLeft(2, '0')}:'
        '${now.second.toString().padLeft(2, '0')}';
    _entries.add('[$timestamp] $message');
    if (_entries.length > maxEntries) {
      _entries.removeRange(0, _entries.length - maxEntries);
    }
    notifyListeners();
  }

  void clear() {
    if (_entries.isEmpty) {
      return;
    }
    _entries.clear();
    notifyListeners();
  }
}
