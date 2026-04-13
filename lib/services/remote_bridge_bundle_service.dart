import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:flutter/services.dart';
import 'package:http/http.dart' as http;
import 'package:path_provider/path_provider.dart';

import '../models/remote_bridge_bundle.dart';

class RemoteBridgeBundleService {
  RemoteBridgeBundleService({http.Client? client})
    : _client = client ?? http.Client();

  static const String _embeddedConfigAsset =
      'assets/bootstrap/remote_bridge_config.json';
  static const String _embeddedManifestAsset =
      'assets/bootstrap/remote_bridge_manifest.json';
  static const String _embeddedScriptAsset =
      'assets/bootstrap/remote_bridge_bundle.js';
  static const String _cacheFileName = 'remote_bridge_bundle_cache.json';

  final http.Client _client;

  Future<RemoteBridgeBundle> loadActiveBundle({
    Duration remoteTimeout = const Duration(seconds: 2),
    void Function(String message)? onLog,
  }) async {
    final RemoteBridgeBundle embeddedBundle = await _loadEmbeddedBundle();
    final RemoteBridgeBundle activeFallback =
        await _loadCachedBundle() ?? embeddedBundle;
    final String manifestUrl = await _loadRemoteManifestUrl();

    if (manifestUrl.isEmpty) {
      onLog?.call(
        'remote bridge manifest url is empty, using ${activeFallback.source} ${activeFallback.version}',
      );
      return activeFallback;
    }

    try {
      final RemoteBridgeBundle remoteBundle = await _fetchRemoteBundle(
        manifestUrl,
      ).timeout(remoteTimeout);
      await _persistBundle(remoteBundle);
      onLog?.call(
        'remote bridge bundle updated to ${remoteBundle.version} from ${remoteBundle.source}',
      );
      return remoteBundle;
    } catch (error) {
      onLog?.call(
        'remote bridge refresh failed, fallback to ${activeFallback.source} ${activeFallback.version}: $error',
      );
      return activeFallback;
    }
  }

  Future<RemoteBridgeBundle> _loadEmbeddedBundle() async {
    try {
      final Map<String, dynamic> manifest = _decodeJsonObject(
        await rootBundle.loadString(_embeddedManifestAsset),
      );
      final String scriptSource = await rootBundle.loadString(
        _embeddedScriptAsset,
      );
      final String version = (manifest['version'] ?? '').toString();
      return RemoteBridgeBundle(
        version: version.isEmpty
            ? RemoteBridgeBundle.embeddedFallback.version
            : version,
        scriptSource: scriptSource,
        source: 'embedded',
      );
    } catch (_) {
      return RemoteBridgeBundle.embeddedFallback;
    }
  }

  Future<String> _loadRemoteManifestUrl() async {
    try {
      final Map<String, dynamic> config = _decodeJsonObject(
        await rootBundle.loadString(_embeddedConfigAsset),
      );
      return (config['manifestUrl'] ?? '').toString().trim();
    } catch (_) {
      return '';
    }
  }

  Future<RemoteBridgeBundle?> _loadCachedBundle() async {
    try {
      final File cacheFile = await _cacheFile();
      if (!await cacheFile.exists()) {
        return null;
      }
      final Map<String, dynamic> payload = _decodeJsonObject(
        await cacheFile.readAsString(),
      );
      final RemoteBridgeBundle bundle = RemoteBridgeBundle.fromJson(payload);
      if (bundle.version.isEmpty || bundle.scriptSource.trim().isEmpty) {
        return null;
      }
      return bundle.copyWith(source: 'cached');
    } catch (_) {
      return null;
    }
  }

  Future<void> _persistBundle(RemoteBridgeBundle bundle) async {
    final File cacheFile = await _cacheFile();
    await cacheFile.parent.create(recursive: true);
    await cacheFile.writeAsString(jsonEncode(bundle.toJson()));
  }

  Future<RemoteBridgeBundle> _fetchRemoteBundle(String manifestUrl) async {
    final Uri manifestUri = Uri.parse(manifestUrl);
    final http.Response manifestResponse = await _client.get(manifestUri);
    if (manifestResponse.statusCode != 200) {
      throw RemoteBridgeBundleException(
        'manifest request failed: HTTP ${manifestResponse.statusCode}',
      );
    }

    final Map<String, dynamic> manifest = _decodeJsonObject(
      manifestResponse.body,
    );
    final String version = (manifest['version'] ?? '').toString().trim();
    if (version.isEmpty) {
      throw const RemoteBridgeBundleException('manifest version is empty');
    }

    final String inlineScript = (manifest['script'] ?? '').toString();
    if (inlineScript.trim().isNotEmpty) {
      return RemoteBridgeBundle(
        version: version,
        scriptSource: inlineScript,
        source: 'remote',
      );
    }

    final String scriptUrl = (manifest['scriptUrl'] ?? '').toString().trim();
    if (scriptUrl.isEmpty) {
      throw const RemoteBridgeBundleException(
        'manifest script and scriptUrl are both empty',
      );
    }

    final Uri scriptUri = manifestUri.resolve(scriptUrl);
    final http.Response scriptResponse = await _client.get(scriptUri);
    if (scriptResponse.statusCode != 200) {
      throw RemoteBridgeBundleException(
        'script request failed: HTTP ${scriptResponse.statusCode}',
      );
    }

    return RemoteBridgeBundle(
      version: version,
      scriptSource: scriptResponse.body,
      source: 'remote',
    );
  }

  Future<File> _cacheFile() async {
    final Directory directory = await getApplicationSupportDirectory();
    return File('${directory.path}/$_cacheFileName');
  }

  Map<String, dynamic> _decodeJsonObject(String raw) {
    final Object decoded = jsonDecode(raw);
    if (decoded is Map<String, dynamic>) {
      return decoded;
    }
    if (decoded is Map) {
      return decoded.map<String, dynamic>(
        (dynamic key, dynamic value) => MapEntry(key.toString(), value),
      );
    }
    throw const RemoteBridgeBundleException('json payload is not an object');
  }
}

class RemoteBridgeBundleException implements Exception {
  const RemoteBridgeBundleException(this.message);

  final String message;

  @override
  String toString() => 'RemoteBridgeBundleException: $message';
}
