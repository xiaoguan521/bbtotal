class CheckInLocationPreset {
  const CheckInLocationPreset({
    required this.address,
    required this.latitude,
    required this.longitude,
    this.province = '',
    this.city = '',
    this.district = '',
    this.street = '',
    this.cityCode = '',
    this.provinceCode = '',
    this.adCode = '',
    this.provinceReferred = '',
    this.isVirtualLocation = false,
  });

  final String address;
  final double latitude;
  final double longitude;
  final String province;
  final String city;
  final String district;
  final String street;
  final String cityCode;
  final String provinceCode;
  final String adCode;
  final String provinceReferred;
  final bool isVirtualLocation;

  String get coordinateText => '$longitude, $latitude';

  Map<String, dynamic> toBridgePayload() {
    return <String, dynamic>{
      'data': <String, dynamic>{
        'latitude': latitude,
        'longitude': longitude,
        'address': address,
        'province': province,
        'city': city,
        'district': district,
        'street': street,
        'cityCode': cityCode,
        'provinceCode': provinceCode,
        'adCode': adCode,
        'provinceReferred': provinceReferred,
        'isVirtuallocation': isVirtualLocation,
      },
      'errorCode': 0,
      'cityCode': cityCode,
      'provinceCode': provinceCode,
      'adCode': adCode,
      'provinceReferred': provinceReferred,
      'msg': '定位成功',
    };
  }
}
