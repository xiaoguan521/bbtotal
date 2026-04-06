import 'dart:async';

import 'package:flutter/services.dart';
import 'package:geolocator/geolocator.dart';

class MockLocationService {
  MockLocationService({
    MethodChannel? channel,
    Duration streamInterval = const Duration(seconds: 1),
  })  : _channel = channel ?? const MethodChannel(_channelName),
        _streamInterval = streamInterval;

  static const String _channelName = 'bbtotal/mock_location';

  final MethodChannel _channel;
  final Duration _streamInterval;

  Future<void> setMockLocation({
    required double latitude,
    required double longitude,
    double accuracy = 5,
    double altitude = 0,
    double altitudeAccuracy = 0,
    double heading = 0,
    double headingAccuracy = 0,
    double speed = 0,
    double speedAccuracy = 0,
    int? floor,
  }) {
    return _channel.invokeMethod<void>(
      'setMockLocation',
      <String, dynamic>{
        'enabled': true,
        'latitude': latitude,
        'longitude': longitude,
        'accuracy': accuracy,
        'altitude': altitude,
        'altitudeAccuracy': altitudeAccuracy,
        'heading': heading,
        'headingAccuracy': headingAccuracy,
        'speed': speed,
        'speedAccuracy': speedAccuracy,
        'floor': floor,
        'timestampMs': DateTime.now().millisecondsSinceEpoch,
      },
    );
  }

  Future<void> clearMockLocation() {
    return _channel.invokeMethod<void>('clearMockLocation');
  }

  Future<Position?> getMockPosition() async {
    final result = await _channel.invokeMapMethod<String, dynamic>(
      'getMockLocation',
    );

    if (result == null || result['enabled'] != true) {
      return null;
    }

    return Position(
      latitude: (result['latitude'] as num).toDouble(),
      longitude: (result['longitude'] as num).toDouble(),
      timestamp: DateTime.fromMillisecondsSinceEpoch(
        (result['timestampMs'] as num?)?.toInt() ??
            DateTime.now().millisecondsSinceEpoch,
      ),
      accuracy: (result['accuracy'] as num?)?.toDouble() ?? 5,
      altitude: (result['altitude'] as num?)?.toDouble() ?? 0,
      altitudeAccuracy: (result['altitudeAccuracy'] as num?)?.toDouble() ?? 0,
      heading: (result['heading'] as num?)?.toDouble() ?? 0,
      headingAccuracy: (result['headingAccuracy'] as num?)?.toDouble() ?? 0,
      speed: (result['speed'] as num?)?.toDouble() ?? 0,
      speedAccuracy: (result['speedAccuracy'] as num?)?.toDouble() ?? 0,
      floor: (result['floor'] as num?)?.toInt(),
      isMocked: true,
    );
  }

  Stream<Position> getPositionStream() async* {
    while (true) {
      final position = await getMockPosition();
      if (position == null) {
        return;
      }

      yield position;
      await Future<void>.delayed(_streamInterval);
    }
  }
}
