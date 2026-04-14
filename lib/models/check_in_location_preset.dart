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
    this.deviceIdentifier = '',
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
  final String deviceIdentifier;
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
    String? deviceIdentifier,
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
      deviceIdentifier: deviceIdentifier ?? this.deviceIdentifier,
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
      _applyDynamicParameters(queryParameters, routePath: baseUri.path);
      return baseUri.replace(queryParameters: queryParameters);
    }

    final String prefix = baseUrl.substring(0, hashIndex + 1);
    final String fragment = baseUrl.substring(hashIndex + 1);
    final Uri fragmentUri = Uri.parse('https://placeholder$fragment');
    final Map<String, String> queryParameters =
        Map<String, String>.from(fragmentUri.queryParameters);

    _applyDynamicParameters(queryParameters, routePath: fragmentUri.path);

    final Uri updatedFragmentUri = fragmentUri.replace(
      queryParameters: queryParameters,
    );
    final String rebuilt = '$prefix${updatedFragmentUri.path}'
        '${updatedFragmentUri.hasQuery ? '?${updatedFragmentUri.query}' : ''}';
    return Uri.parse(rebuilt);
  }

  void _applyDynamicParameters(
    Map<String, String> queryParameters, {
    required String routePath,
  }) {
    final String orgNumber = loginInfo.zxbm.isNotEmpty
        ? loginInfo.zxbm
        : loginInfo.jgbh;
    final bool isApprovalRoute = routePath.contains('/dxslglComp/dxslglsp/index');

    _putIfMissing(queryParameters, 'tyLoginToken', loginInfo.loginToken);
    _putIfMissing(queryParameters, 'userid', loginInfo.userId.toString());
    _putIfMissing(queryParameters, 'khbh', loginInfo.grbh);
    _putIfMissing(queryParameters, 'zjhm', loginInfo.idCard);

    if (orgNumber.isNotEmpty) {
      _putIfMissing(queryParameters, 'jgbm', orgNumber);
    }
    if (loginInfo.zxbm.isNotEmpty) {
      _putIfMissing(queryParameters, 'zxbm', loginInfo.zxbm);
    } else {
      _putIfMissing(queryParameters, 'zxbm', loginInfo.jgbh);
    }
    if (loginInfo.qycode.isNotEmpty) {
      _putIfMissing(queryParameters, 'qycode', loginInfo.qycode);
    }
    if (loginInfo.zzjgdmz.isNotEmpty) {
      _putIfMissing(queryParameters, 'zzjgdmz', loginInfo.zzjgdmz);
    }
    if (!isApprovalRoute) {
      _putIfMissing(queryParameters, 'dx_29_sjdxsl', loginInfo.userId.toString());
      if (deviceIdentifier.isNotEmpty) {
        _putIfMissing(queryParameters, 'dx_29_sbsbm', deviceIdentifier);
        _putIfMissing(queryParameters, 'sbsbm', deviceIdentifier);
        _putIfMissing(queryParameters, 'deviceId', deviceIdentifier);
      }
    }
    if (cheque.isNotEmpty) {
      _putIfMissing(queryParameters, 'cheque', cheque);
    }
  }

  void _putIfMissing(
    Map<String, String> queryParameters,
    String key,
    String value,
  ) {
    if (value.isEmpty) {
      return;
    }
    final String? existing = queryParameters[key];
    if (existing != null && existing.isNotEmpty) {
      return;
    }
    queryParameters[key] = value;
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
