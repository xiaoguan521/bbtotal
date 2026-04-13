class RemoteBridgeBundle {
  const RemoteBridgeBundle({
    required this.version,
    required this.scriptSource,
    required this.source,
  });

  static const RemoteBridgeBundle embeddedFallback = RemoteBridgeBundle(
    version: 'embedded-1.0.0',
    scriptSource: '',
    source: 'embedded',
  );

  final String version;
  final String scriptSource;
  final String source;

  bool get hasScript => scriptSource.trim().isNotEmpty;

  RemoteBridgeBundle copyWith({
    String? version,
    String? scriptSource,
    String? source,
  }) {
    return RemoteBridgeBundle(
      version: version ?? this.version,
      scriptSource: scriptSource ?? this.scriptSource,
      source: source ?? this.source,
    );
  }

  Map<String, dynamic> toJson() => <String, dynamic>{
    'version': version,
    'scriptSource': scriptSource,
    'source': source,
  };

  static RemoteBridgeBundle fromJson(Map<String, dynamic> json) {
    return RemoteBridgeBundle(
      version: (json['version'] ?? '').toString(),
      scriptSource: (json['scriptSource'] ?? '').toString(),
      source: (json['source'] ?? '').toString(),
    );
  }
}
