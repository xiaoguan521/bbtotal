import 'user_login_info.dart';

class CheckInLocationPreset {
  const CheckInLocationPreset({
    required this.address,
    required this.latitude,
    required this.loginInfo,
    required this.longitude,
    this.cheque = '',
    this.province = '',
    this.city = '',
    this.district = '',
    this.street = '',
    this.cityCode = '',
    this.provinceCode = '',
    this.adCode = '',
    this.provinceReferred = '',
    this.isVirtualLocation = false,
    this.ticket = '',
  });

  final String address;
  final String cheque;
  final double latitude;
  final UserLoginInfo loginInfo;
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
  final String ticket;

  String get coordinateText => '$longitude, $latitude';

  CheckInLocationPreset copyWith({
    String? address,
    String? cheque,
    double? latitude,
    UserLoginInfo? loginInfo,
    double? longitude,
    String? province,
    String? city,
    String? district,
    String? street,
    String? cityCode,
    String? provinceCode,
    String? adCode,
    String? provinceReferred,
    bool? isVirtualLocation,
    String? ticket,
  }) {
    return CheckInLocationPreset(
      address: address ?? this.address,
      cheque: cheque ?? this.cheque,
      latitude: latitude ?? this.latitude,
      loginInfo: loginInfo ?? this.loginInfo,
      longitude: longitude ?? this.longitude,
      province: province ?? this.province,
      city: city ?? this.city,
      district: district ?? this.district,
      street: street ?? this.street,
      cityCode: cityCode ?? this.cityCode,
      provinceCode: provinceCode ?? this.provinceCode,
      adCode: adCode ?? this.adCode,
      provinceReferred: provinceReferred ?? this.provinceReferred,
      isVirtualLocation: isVirtualLocation ?? this.isVirtualLocation,
      ticket: ticket ?? this.ticket,
    );
  }

  Uri buildCheckInUri(String baseUrl) {
    final int hashIndex = baseUrl.indexOf('#');
    if (hashIndex == -1) {
      final Uri baseUri = Uri.parse(baseUrl);
      final Map<String, String> queryParameters =
          Map<String, String>.from(baseUri.queryParameters);
      _applyDynamicParameters(queryParameters);
      return baseUri.replace(queryParameters: queryParameters);
    }

    final String prefix = baseUrl.substring(0, hashIndex + 1);
    final String fragment = baseUrl.substring(hashIndex + 1);
    final Uri fragmentUri = Uri.parse('https://placeholder$fragment');
    final Map<String, String> queryParameters =
        Map<String, String>.from(fragmentUri.queryParameters);

    _applyDynamicParameters(queryParameters);

    final Uri updatedFragmentUri = fragmentUri.replace(
      queryParameters: queryParameters,
    );
    final String rebuilt = '$prefix${updatedFragmentUri.path}'
        '${updatedFragmentUri.hasQuery ? '?${updatedFragmentUri.query}' : ''}';
    return Uri.parse(rebuilt);
  }

  void _applyDynamicParameters(Map<String, String> queryParameters) {
    queryParameters['tyLoginToken'] = loginInfo.loginToken;
    queryParameters['userid'] = loginInfo.userId.toString();
    queryParameters['dx_29_sjdxsl'] = loginInfo.userId.toString();
    queryParameters['khbh'] = loginInfo.grbh;
    queryParameters['zjhm'] = loginInfo.idCard;

    if (loginInfo.jgbm.isNotEmpty) {
      queryParameters['jgbm'] = loginInfo.jgbm;
    }
    if (loginInfo.zxbm.isNotEmpty) {
      queryParameters['zxbm'] = loginInfo.zxbm;
    } else {
      queryParameters['zxbm'] = loginInfo.jgbh;
    }
    if (loginInfo.qycode.isNotEmpty) {
      queryParameters['qycode'] = loginInfo.qycode;
    }
    if (loginInfo.zzjgdmz.isNotEmpty) {
      queryParameters['zzjgdmz'] = loginInfo.zzjgdmz;
    }
    if (cheque.isNotEmpty) {
      queryParameters['cheque'] = cheque;
    }
  }

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
