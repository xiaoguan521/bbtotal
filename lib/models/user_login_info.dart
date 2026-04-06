class UserLoginInfo {
  const UserLoginInfo({
    required this.account,
    required this.username,
    required this.userId,
    required this.grbh,
    required this.idCard,
    required this.loginToken,
    required this.blqd,
    required this.jgbh,
    this.client = '',
    this.desktopChannel = '',
    this.desktopJgbh = '',
    this.desktopJgbm = '',
    this.desktopLoginToken = '',
    this.desktopQycode = '',
    this.desktopZxbm = '',
    this.desktopZzjgdmz = '',
    this.jgbm = '',
    this.rawUserInfoJson = '',
    this.zxbm = '',
    this.qycode = '',
    this.zzjgdmz = '',
    this.ticket = '',
    this.qdmc,
  });

  factory UserLoginInfo.fromJson(Map<String, dynamic> json) {
    return UserLoginInfo(
      account: (json['account'] ?? '') as String,
      username: (json['username'] ?? '') as String,
      userId: (json['userid'] as num?)?.toInt() ?? 0,
      grbh: (json['grbh'] ?? '') as String,
      idCard: (json['idCard'] ?? '') as String,
      loginToken: (json['loginToken'] ?? '') as String,
      blqd: (json['blqd'] ?? '') as String,
      jgbh: (json['jgbh'] ?? '') as String,
      client: (json['client'] ?? '') as String,
      jgbm: (json['jgbm'] ?? '') as String,
      rawUserInfoJson: (json['userinfo'] ?? '') as String,
      zxbm: (json['zxbm'] ?? '') as String,
      qycode: (json['qycode'] ?? '') as String,
      zzjgdmz: (json['zzjgdmz'] ?? '') as String,
      ticket: (json['ticket'] ?? '') as String,
      qdmc: json['qdmc'] as String?,
    );
  }

  final String account;
  final String username;
  final int userId;
  final String grbh;
  final String idCard;
  final String loginToken;
  final String blqd;
  final String jgbh;
  final String client;
  final String desktopChannel;
  final String desktopJgbh;
  final String desktopJgbm;
  final String desktopLoginToken;
  final String desktopQycode;
  final String desktopZxbm;
  final String desktopZzjgdmz;
  final String jgbm;
  final String rawUserInfoJson;
  final String zxbm;
  final String qycode;
  final String zzjgdmz;
  final String ticket;
  final String? qdmc;

  bool get isMobileApp => blqd == 'app_02';

  String get tokenPreview {
    if (loginToken.length <= 18) {
      return loginToken;
    }
    return '${loginToken.substring(0, 12)}...${loginToken.substring(loginToken.length - 6)}';
  }

  UserLoginInfo copyWith({
    String? account,
    String? username,
    int? userId,
    String? grbh,
    String? idCard,
    String? loginToken,
    String? blqd,
    String? jgbh,
    String? client,
    String? desktopChannel,
    String? desktopJgbh,
    String? desktopJgbm,
    String? desktopLoginToken,
    String? desktopQycode,
    String? desktopZxbm,
    String? desktopZzjgdmz,
    String? jgbm,
    String? rawUserInfoJson,
    String? zxbm,
    String? qycode,
    String? zzjgdmz,
    String? ticket,
    String? qdmc,
  }) {
    return UserLoginInfo(
      account: account ?? this.account,
      username: username ?? this.username,
      userId: userId ?? this.userId,
      grbh: grbh ?? this.grbh,
      idCard: idCard ?? this.idCard,
      loginToken: loginToken ?? this.loginToken,
      blqd: blqd ?? this.blqd,
      jgbh: jgbh ?? this.jgbh,
      client: client ?? this.client,
      desktopChannel: desktopChannel ?? this.desktopChannel,
      desktopJgbh: desktopJgbh ?? this.desktopJgbh,
      desktopJgbm: desktopJgbm ?? this.desktopJgbm,
      desktopLoginToken: desktopLoginToken ?? this.desktopLoginToken,
      desktopQycode: desktopQycode ?? this.desktopQycode,
      desktopZxbm: desktopZxbm ?? this.desktopZxbm,
      desktopZzjgdmz: desktopZzjgdmz ?? this.desktopZzjgdmz,
      jgbm: jgbm ?? this.jgbm,
      rawUserInfoJson: rawUserInfoJson ?? this.rawUserInfoJson,
      zxbm: zxbm ?? this.zxbm,
      qycode: qycode ?? this.qycode,
      zzjgdmz: zzjgdmz ?? this.zzjgdmz,
      ticket: ticket ?? this.ticket,
      qdmc: qdmc ?? this.qdmc,
    );
  }
}
