class CheckInHistoryLocation {
  const CheckInHistoryLocation({
    required this.address,
    required this.coordinateText,
    required this.deviceIdentifier,
    required this.deviceName,
    required this.recordId,
    required this.recordNumber,
    required this.timestamp,
    required this.longitude,
    required this.latitude,
  });

  factory CheckInHistoryLocation.fromJson(Map<String, dynamic> json) {
    final String coordinateText = (json['dx_29_dkzb'] ?? '').toString();
    final List<String> parts =
        coordinateText.split(',').map((String item) => item.trim()).toList();

    double? longitude;
    double? latitude;
    if (parts.length >= 2) {
      longitude = double.tryParse(parts[0]);
      latitude = double.tryParse(parts[1]);
    }

    final int? timestampMs = (json['dx_29_shijian'] as num?)?.toInt();
    return CheckInHistoryLocation(
      address: (json['dx_29_dkwz'] ?? '').toString(),
      coordinateText: coordinateText,
      deviceIdentifier: (json['dx_29_sbsbm'] ?? '').toString(),
      deviceName: (json['dx_29_sjbz'] ?? '').toString(),
      recordId: (json['dx_29_id'] ?? '').toString(),
      recordNumber: (json['dx_29_dxbh'] ?? '').toString(),
      timestamp: timestampMs == null
          ? null
          : DateTime.fromMillisecondsSinceEpoch(timestampMs),
      longitude: longitude,
      latitude: latitude,
    );
  }

  final String address;
  final String coordinateText;
  final String deviceIdentifier;
  final String deviceName;
  final String recordId;
  final String recordNumber;
  final DateTime? timestamp;
  final double? longitude;
  final double? latitude;

  bool get hasUsableLocation =>
      address.isNotEmpty && longitude != null && latitude != null;

  String get uniqueKey => '$address|$coordinateText';

  String get timeText {
    final DateTime? value = timestamp;
    if (value == null) {
      return '未知时间';
    }

    String twoDigits(int number) => number.toString().padLeft(2, '0');
    return '${value.year}-${twoDigits(value.month)}-${twoDigits(value.day)} '
        '${twoDigits(value.hour)}:${twoDigits(value.minute)}:${twoDigits(value.second)}';
  }
}
