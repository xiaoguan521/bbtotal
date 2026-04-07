import 'dart:async';
import 'dart:developer' as developer;

import 'package:flutter/foundation.dart';
import 'package:geolocator/geolocator.dart';
import 'package:permission_handler/permission_handler.dart';

class LocationService {
  LocationService({
    LocationSettings? singleLocationSettings,
  }) : _singleLocationSettings = singleLocationSettings ??
           const LocationSettings(
             accuracy: LocationAccuracy.high,
             timeLimit: Duration(seconds: 15),
           );

  final LocationSettings _singleLocationSettings;

  final ValueNotifier<bool> isLoading = ValueNotifier<bool>(false);
  final ValueNotifier<String?> lastError = ValueNotifier<String?>(null);

  Future<Position> getCurrentPosition() async {
    _setLoading(true);
    _clearError();

    try {
      await _ensurePermission();

      final position = await Geolocator.getCurrentPosition(
        locationSettings: _singleLocationSettings,
      );
      _log('Current position: ${position.latitude}, ${position.longitude}');
      return position;
    } on TimeoutException catch (error, stackTrace) {
      final Position? fallback = await Geolocator.getLastKnownPosition();
      if (fallback != null) {
        _log(
          'Current position timeout, fallback to last known position: '
          '${fallback.latitude}, ${fallback.longitude}',
          error: error,
          stackTrace: stackTrace,
        );
        return fallback;
      }

      const LocationServiceException failure = LocationServiceException(
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

  Future<void> _ensurePermission() async {
    final bool serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if (!serviceEnabled) {
      throw const LocationServiceDisabledException(
        '定位服务未开启，请先打开系统定位开关。',
      );
    }

    final PermissionStatus foregroundStatus = await _requestForegroundPermission();
    if (_isGranted(foregroundStatus)) {
      return;
    }

    if (foregroundStatus.isPermanentlyDenied) {
      throw const LocationPermissionPermanentlyDeniedException(
        '定位权限被永久拒绝，请前往系统设置手动开启。',
      );
    }

    throw const LocationPermissionDeniedException(
      '定位权限被拒绝，无法继续获取位置信息。',
    );
  }

  Future<PermissionStatus> _requestForegroundPermission() async {
    PermissionStatus status = await Permission.locationWhenInUse.status;
    if (_isGranted(status)) {
      return status;
    }

    status = await Permission.locationWhenInUse.request();
    if (_isGranted(status) || status.isPermanentlyDenied) {
      return status;
    }

    return Permission.location.request();
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
