import 'dart:async';
import 'dart:developer' as developer;

import 'package:flutter/foundation.dart';
import 'package:geolocator/geolocator.dart';
import 'package:permission_handler/permission_handler.dart';

import 'mock_location_service.dart';

class LocationService {
  LocationService({
    MockLocationService? mockLocationService,
    LocationSettings? singleLocationSettings,
    LocationSettings? streamLocationSettings,
  })  : _mockLocationService = mockLocationService ?? MockLocationService(),
        _singleLocationSettings = singleLocationSettings ??
            const LocationSettings(
              accuracy: LocationAccuracy.high,
              timeLimit: Duration(seconds: 15),
            ),
        _streamLocationSettings = streamLocationSettings ??
            const LocationSettings(
              accuracy: LocationAccuracy.high,
              distanceFilter: 10,
            );

  final MockLocationService _mockLocationService;
  final LocationSettings _singleLocationSettings;
  final LocationSettings _streamLocationSettings;

  final ValueNotifier<bool> isLoading = ValueNotifier<bool>(false);
  final ValueNotifier<String?> lastError = ValueNotifier<String?>(null);

  StreamSubscription<Position>? _subscription;

  Future<bool> requestPermission() async {
    _setLoading(true);
    _clearError();

    try {
      await _ensurePermission(requestAlways: true);
      return true;
    } catch (error, stackTrace) {
      _recordError(error);
      _log('requestPermission failed', error: error, stackTrace: stackTrace);
      rethrow;
    } finally {
      _setLoading(false);
    }
  }

  Future<Position> getCurrentPosition() async {
    _setLoading(true);
    _clearError();

    try {
      final mockPosition = await _mockLocationService.getMockPosition();
      if (mockPosition != null) {
        _log(
          'Returning mock position: '
          '${mockPosition.latitude}, ${mockPosition.longitude}',
        );
        return mockPosition;
      }

      await _ensurePermission(requestAlways: false);

      final position = await Geolocator.getCurrentPosition(
        locationSettings: _singleLocationSettings,
      );
      _log('Current position: ${position.latitude}, ${position.longitude}');
      return position;
    } on TimeoutException catch (error, stackTrace) {
      final fallback = await Geolocator.getLastKnownPosition();
      if (fallback != null) {
        _log(
          'Current position timeout, fallback to last known position: '
          '${fallback.latitude}, ${fallback.longitude}',
          error: error,
          stackTrace: stackTrace,
        );
        return fallback;
      }

      const failure = LocationServiceException(
        '定位超时，且没有可用的上次定位缓存。',
      );
      _recordError(failure);
      _log('getCurrentPosition timed out', error: error, stackTrace: stackTrace);
      throw failure;
    } catch (error, stackTrace) {
      _recordError(error);
      _log('getCurrentPosition failed', error: error, stackTrace: stackTrace);
      rethrow;
    } finally {
      _setLoading(false);
    }
  }

  Stream<Position> getPositionStream() async* {
    _setLoading(true);
    _clearError();

    try {
      final mockPosition = await _mockLocationService.getMockPosition();
      if (mockPosition != null) {
        _log('Using mock location stream.');
        yield* _mockLocationService.getPositionStream();
        return;
      }

      await _ensurePermission(requestAlways: false);
    } catch (error, stackTrace) {
      _recordError(error);
      _log('getPositionStream setup failed', error: error, stackTrace: stackTrace);
      rethrow;
    } finally {
      _setLoading(false);
    }

    yield* Geolocator.getPositionStream(
      locationSettings: _streamLocationSettings,
    ).handleError((Object error, StackTrace stackTrace) {
      _recordError(error);
      _log(
        'Position stream emitted an error',
        error: error,
        stackTrace: stackTrace,
      );
    });
  }

  Future<StreamSubscription<Position>> startListening({
    required void Function(Position position) onPositionChanged,
    void Function(Object error)? onError,
  }) async {
    await stopListening();
    _subscription = getPositionStream().listen(
      onPositionChanged,
      onError: (Object error) {
        _recordError(error);
        if (onError != null) {
          onError(error);
        }
      },
    );
    return _subscription!;
  }

  Future<void> stopListening() async {
    final subscription = _subscription;
    _subscription = null;
    await subscription?.cancel();
  }

  Future<void> _ensurePermission({required bool requestAlways}) async {
    final serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if (!serviceEnabled) {
      throw const LocationServiceDisabledException(
        '定位服务未开启，请先打开系统定位开关。',
      );
    }

    final foregroundStatus = await _requestForegroundPermission();
    if (!_isGranted(foregroundStatus)) {
      if (foregroundStatus.isPermanentlyDenied) {
        throw const LocationPermissionPermanentlyDeniedException(
          '定位权限被永久拒绝，请前往系统设置手动开启。',
        );
      }

      throw const LocationPermissionDeniedException(
        '定位权限被拒绝，无法继续获取位置信息。',
      );
    }

    if (!requestAlways) {
      return;
    }

    final backgroundStatus = await _requestAlwaysPermission();
    if (backgroundStatus == null || _isGranted(backgroundStatus)) {
      return;
    }

    if (backgroundStatus.isPermanentlyDenied) {
      throw const LocationPermissionPermanentlyDeniedException(
        '后台定位权限被永久拒绝，请前往系统设置手动开启。',
      );
    }

    throw const LocationPermissionDeniedException(
      '后台定位权限被拒绝，持续定位功能不可用。',
    );
  }

  Future<PermissionStatus> _requestForegroundPermission() async {
    var status = await Permission.locationWhenInUse.status;
    if (_isGranted(status)) {
      return status;
    }

    status = await Permission.locationWhenInUse.request();
    if (_isGranted(status) || status.isPermanentlyDenied) {
      return status;
    }

    return Permission.location.request();
  }

  Future<PermissionStatus?> _requestAlwaysPermission() async {
    var status = await Permission.locationAlways.status;
    if (_isGranted(status) || status.isPermanentlyDenied) {
      return status;
    }

    status = await Permission.locationAlways.request();
    if (!_isGranted(status) && status.isPermanentlyDenied) {
      _log('Background location permission is permanently denied.');
    }
    return status;
  }

  bool _isGranted(PermissionStatus status) {
    return status.isGranted || status.isLimited;
  }

  void _setLoading(bool value) {
    isLoading.value = value;
  }

  void _clearError() {
    lastError.value = null;
  }

  void _recordError(Object error) {
    if (error is LocationServiceException) {
      lastError.value = error.message;
      return;
    }

    lastError.value = error.toString();
  }

  void _log(
    String message, {
    Object? error,
    StackTrace? stackTrace,
  }) {
    developer.log(
      message,
      name: 'LocationService',
      error: error,
      stackTrace: stackTrace,
    );
  }

  void dispose() {
    final subscription = _subscription;
    _subscription = null;
    subscription?.cancel();
    isLoading.dispose();
    lastError.dispose();
  }
}

class LocationServiceException implements Exception {
  const LocationServiceException(this.message);

  final String message;

  @override
  String toString() => 'LocationServiceException: $message';
}

class LocationServiceDisabledException extends LocationServiceException {
  const LocationServiceDisabledException(super.message);
}

class LocationPermissionDeniedException extends LocationServiceException {
  const LocationPermissionDeniedException(super.message);
}

class LocationPermissionPermanentlyDeniedException
    extends LocationServiceException {
  const LocationPermissionPermanentlyDeniedException(super.message);
}
