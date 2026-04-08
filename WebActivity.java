package com.shineyue.pm.web;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ClientCertRequest;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.android.local.JPushConstants;
import com.alibaba.fastjson.JSON;
import com.alipay.android.phone.mobilecommon.multimediabiz.biz.file.impl.upload.HttpFileUploader;
import com.alipay.android.phone.mobilecommon.multimediabiz.biz.utils.LogUnAvailbleItem;
import com.alipay.mobile.android.verify.sdk.BizCode;
import com.alipay.mobile.android.verify.sdk.ServiceFactory;
import com.alipay.mobile.android.verify.sdk.interfaces.ICallback;
import com.alipay.mobile.android.verify.sdk.interfaces.IService;
import com.alipay.mobile.common.transport.config.DtnConfigItem;
import com.alipay.mobile.common.transport.monitor.RPCDataItems;
import com.alipay.mobile.common.transport.utils.TransportConstants;
import com.alipay.mobile.mrtc.api.wwj.StreamerConstants;
import com.alipay.sdk.cons.c;
import com.alipay.sdk.util.g;
import com.alipay.sdk.util.j;
import com.alivc.rtc.AliRtcEngine;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.district.DistrictSearchQuery;
import com.blankj.utilcode.constant.PermissionConstants;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.mlkit.common.sdkinternal.OptionalModuleUtils;
import com.gs.sm.SM3Util;
import com.gs.sm.SM4Util;
import com.heytap.mcssdk.constant.IntentConstant;
import com.huawei.hms.push.AttributionReporter;
import com.huawei.hms.push.constant.RemoteMessageConst;
import com.luck.picture.lib.config.CustomIntentKey;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okserver.download.DownloadInfo;
import com.mabeijianxi.jianxiexpression.ExpressionGridFragment;
import com.mabeijianxi.jianxiexpression.ExpressionShowFragment;
import com.mabeijianxi.jianxiexpression.widget.ExpressionEditText;
import com.meizu.cloud.pushsdk.constants.PushConstants;
import com.shineyue.pm.BaseTimeWebActivity;
import com.shineyue.pm.BasicActivity;
import com.shineyue.pm.Dao.NewLoginModule;
import com.shineyue.pm.Dao.NoSpeakingDaoManager;
import com.shineyue.pm.MainActivity;
import com.shineyue.pm.OaApplication;
import com.shineyue.pm.activity.AliPayActivity;
import com.shineyue.pm.activity.ChangePasswordActivity;
import com.shineyue.pm.activity.NoSpeakingActivity;
import com.shineyue.pm.activity.QrCodeActivity;
import com.shineyue.pm.aliyun_rtc.activity.AliRtcChatActivity;
import com.shineyue.pm.aliyun_rtc.bean.RTCAuthInfo;
import com.shineyue.pm.aliyun_rtc.contract.AliRtcLoginContract;
import com.shineyue.pm.aliyun_rtc.presenter.AliRtcLoginPresenter;
import com.shineyue.pm.aliyun_rtc.utils.AliRtcConstants;
import com.shineyue.pm.aliyun_rtc.utils.PermissionUtils;
import com.shineyue.pm.android_ocr.camera.CameraScanActivity;
import com.shineyue.pm.android_ocr.camera.CameraTakeActivity;
import com.shineyue.pm.bean.ChangeUserBean;
import com.shineyue.pm.dialog.BottomAnimDialog;
import com.shineyue.pm.dialog.NewUpdateDialog;
import com.shineyue.pm.dialog.PermissionDialog;
import com.shineyue.pm.dialog.QuoteDialog;
import com.shineyue.pm.dkplayer.activity.list.tiktok.TikTok2Activity;
import com.shineyue.pm.fragment.MainFragmentMessageNewVersion;
import com.shineyue.pm.modle_chat.BusinessCardActivity;
import com.shineyue.pm.modle_chat.ChatActivity;
import com.shineyue.pm.modle_chat.ForwardHistoryActivity;
import com.shineyue.pm.modle_chat.HuaTiDetailActivity;
import com.shineyue.pm.modle_chat.RepeatSendSelectActivity;
import com.shineyue.pm.modle_chat.adapter.ChatGroupUserPopupAdapter;
import com.shineyue.pm.modle_chat.adapter.ChatRvAdapter;
import com.shineyue.pm.modle_chat.bean.ChatWindowItem;
import com.shineyue.pm.modle_chat.bean.GroupUser;
import com.shineyue.pm.modle_chat.bean.HotIssuesBean;
import com.shineyue.pm.modle_chat.bean.HuaTiDetailBean;
import com.shineyue.pm.modle_chat.bean.Message;
import com.shineyue.pm.modle_chat.bean.MessageDing;
import com.shineyue.pm.modle_chat.bean.QunXiangQing;
import com.shineyue.pm.modle_chat.bean.QunZu;
import com.shineyue.pm.modle_chat.bean.QunZuItemInfoBean;
import com.shineyue.pm.modle_chat.chat_ding.DingPopupWindow;
import com.shineyue.pm.modle_chat.chat_history.SingleChatDetailActivity;
import com.shineyue.pm.modle_chat.chat_history.dao.OnParmsCallBack;
import com.shineyue.pm.modle_chat.dao.MessageDao;
import com.shineyue.pm.modle_chat.dao.helper.GreenDaoHelper;
import com.shineyue.pm.modle_chat.function_view.AddShowFragment;
import com.shineyue.pm.modle_chat.manager.CustomLinearLayoutManager;
import com.shineyue.pm.modle_chat.presenter.chat_window_list.ChatWindowListDaoManager;
import com.shineyue.pm.modle_chat.presenter.chat_window_list.ChatWindowListPresenter;
import com.shineyue.pm.modle_chat.presenter.chat_window_list.ChatWindowListView;
import com.shineyue.pm.modle_chat.presenter.chatview.AitManager;
import com.shineyue.pm.modle_chat.presenter.chatview.ChatAitPresenter;
import com.shineyue.pm.modle_chat.presenter.grouplist.GroupListPresenter;
import com.shineyue.pm.modle_chat.presenter.grouplist.GroupListView;
import com.shineyue.pm.modle_chat.preview.PreViewActivity;
import com.shineyue.pm.modle_chat.preview.PreViewMediaActivity;
import com.shineyue.pm.modle_chat.preview.imagepicker.ImageGridActivity;
import com.shineyue.pm.modle_chat.qunzu.activity.QunZuDetailActivity;
import com.shineyue.pm.modle_chat.qunzu.activity.QunZuDetailOldActivity;
import com.shineyue.pm.modle_chat.qunzu.dao.QunZuItemInfoDaoManager;
import com.shineyue.pm.modle_chat.utiles.AudioRecorderButton;
import com.shineyue.pm.modle_chat.utiles.ChatQxModule;
import com.shineyue.pm.modle_chat.utiles.MediaManager;
import com.shineyue.pm.modle_chat_new.ChatServiceNew;
import com.shineyue.pm.modle_circle.CircleTrendsDetailNewActivity;
import com.shineyue.pm.modle_circle.bean.CircleTrendsBean;
import com.shineyue.pm.modle_email.bean.ToolsBean;
import com.shineyue.pm.modle_email.dialog.PromptsDialog;
import com.shineyue.pm.modle_maillist.activity.OrganizationalStructureNewActivity;
import com.shineyue.pm.modle_maillist.dao.MailListDaoManager;
import com.shineyue.pm.modle_maillist.dao.MailListPrimaryDepartmentDaoManager;
import com.shineyue.pm.modle_maillist.dao.bean.MailItem;
import com.shineyue.pm.modle_maillist.presenter.MailPresenter;
import com.shineyue.pm.modle_maillist.presenter.MailView;
import com.shineyue.pm.modle_page_news.bean.ShareTypeBean;
import com.shineyue.pm.modle_page_work.activity.AttendanceTongJiActivity;
import com.shineyue.pm.modle_page_work.activity.SignWorkingActivity;
import com.shineyue.pm.modlepagefile.DocumentNewInterface;
import com.shineyue.pm.modlepagefile.activity.FileChatCheckActivity;
import com.shineyue.pm.modlepagefile.activity.FileChatCheckNewActivity;
import com.shineyue.pm.modlepagefile.activity.FileMainActivity;
import com.shineyue.pm.network.OkGoNetManager;
import com.shineyue.pm.network.callback.MyStringCallback;
import com.shineyue.pm.network.callback.MyStringCallbackNew;
import com.shineyue.pm.new_utils.FileTypeUtils;
import com.shineyue.pm.scancod.notify.Notify;
import com.shineyue.pm.server.AppUpgradeService;
import com.shineyue.pm.service.TTSService;
import com.shineyue.pm.tencent_rtc.CallingActivity;
import com.shineyue.pm.tencent_rtc.CallingFinishActivity;
import com.shineyue.pm.tencent_rtc.bean.TRTCOrderBean;
import com.shineyue.pm.tencent_rtc.bean.TRTCUserBean;
import com.shineyue.pm.tencent_rtc.util.TRTCSetSocketUtil;
import com.shineyue.pm.tencent_rtc_live.LiveAnchorActivity;
import com.shineyue.pm.tencent_rtc_live.LiveAudienceActivity;
import com.shineyue.pm.tencent_rtc_live.LiveFinishActivity;
import com.shineyue.pm.utils.ACache;
import com.shineyue.pm.utils.BrightnessController;
import com.shineyue.pm.utils.CachePzthConfigs;
import com.shineyue.pm.utils.CertUtils;
import com.shineyue.pm.utils.Constants;
import com.shineyue.pm.utils.DateUtils;
import com.shineyue.pm.utils.DavikActivityUtils;
import com.shineyue.pm.utils.DeviceUtils;
import com.shineyue.pm.utils.DownLoadUtil;
import com.shineyue.pm.utils.EncUtils;
import com.shineyue.pm.utils.ExitDialogUtil;
import com.shineyue.pm.utils.FileUtil;
import com.shineyue.pm.utils.GPS;
import com.shineyue.pm.utils.KeyBoardListener;
import com.shineyue.pm.utils.LocationUtils;
import com.shineyue.pm.utils.MD5Util;
import com.shineyue.pm.utils.MyPhotoUtils;
import com.shineyue.pm.utils.NavicationCountUtils;
import com.shineyue.pm.utils.NetUtils;
import com.shineyue.pm.utils.OutPutLogUtils;
import com.shineyue.pm.utils.PermissionsUtils;
import com.shineyue.pm.utils.PinYin4j;
import com.shineyue.pm.utils.TTSPlayerManager;
import com.shineyue.pm.utils.ToastInfo;
import com.shineyue.pm.utils.ToastType;
import com.shineyue.pm.utils.ToastUtil;
import com.shineyue.pm.utils.UtilTips;
import com.shineyue.pm.utils.Utils;
import com.shineyue.pm.utils.UtilsChatTopic;
import com.shineyue.pm.utils.async.Callback;
import com.shineyue.pm.utils.async.Result;
import com.shineyue.pm.utils.async.ThreadPoolManager;
import com.shineyue.pm.utils.autosize.utils.LogUtils;
import com.shineyue.pm.video.play.PlayView;
import com.shineyue.pm.view.MorePopQunZu;
import com.shineyue.pm.view.SharePop;
import com.shineyue.pm.view.loadingview.CustomLoadingView;
import com.shineyue.pm.web.webevent.EventDispatchUtil;
import com.shineyue.pm.web.webevent.WebActivityApi;
import com.shineyue.pm.websocket.Base64Utils;
import com.shineyue.pm.websocket.RSAUtils;
import com.shineyue.pmcs.R;
import com.socks.library.KLog;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.smtt.sdk.TbsListener;
import com.tencent.smtt.sdk.TbsVideoCacheTask;
import com.tencent.smtt.sdk.WebView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.pro.ak;
import com.xiaoleilu.hutool.util.StrUtil;
import com.yanzhenjie.permission.Permission;
import com.ypy.eventbus.EventBus;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.greenrobot.greendao.query.WhereCondition;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.xmlpull.v1.XmlPullParserException;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;
import tv.danmaku.ijk.media.encode.FFmpegSessionConfig;
import wendu.dsbridge.CompletionHandler;
import wendu.dsbridge.DWebView;

/* JADX INFO: loaded from: classes4.dex */
public class WebActivity extends BaseTimeWebActivity implements AliRtcLoginContract.view, View.OnClickListener, MailView, ChatWindowListView, GroupListView, ActivityCompat.OnRequestPermissionsResultCallback, ExpressionGridFragment.ExpressionClickListener, ExpressionGridFragment.ExpressionDeleteClickListener {
    private boolean BINDINGACTIVITY;
    private boolean ISLOADING;
    public final String NOT_SAME_PERSON;
    private final String PERF_KEY_REFRESH_SIGN_SESSION_TIME;
    private final int TAKE_PICTURE;
    private final int TAKE_PICTURE_CHAT;
    public final String UNABLE_GET_IMAGE;
    private ImageView abc_title_back;
    private TextView abc_title_txt_close;
    private String addInfo;
    private AddShowFragment addShowFragment;
    private AitManager aitManager;
    private AlertDialog alertDialog;
    private boolean applicationBecomeActive;
    private String bitmapName;
    private BottomAnimDialog bottomAnimDialog;
    private int brightness;
    CompletionHandler brushHandler;
    private JSONArray btnList;
    private ImageView btn_add;
    private Button btn_retry;
    private TextView btn_send;
    String cameraPath;
    private boolean changeScreenbrightness;
    private ChatRvAdapter chatAdapter;
    private ChatAitPresenter chatAitPresenter;
    private Animation chatFullCloseAnimation;
    private Animation chatFullOpenAnimation;
    private Animation chatHuaTiCloseAnimation;
    private Animation chatHuaTiOpenAnimation;
    private boolean chatPrepare;
    private ChatQxModule chatQxModule;
    private String chatType;
    private ChatWindowListPresenter chatWindowListPresenter;
    private int cheque;
    private int currPosition;
    private CustomLoadingView customLoadingView;
    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private String dingId;
    private long dingTime;
    private DocumentNewInterface documentNewInterface;
    private int duration;
    private ExpressionEditText editEmojicon;
    private long endTime;
    private EditText et_search;
    private EventDispatchUtil eventDispatchUtil;
    private ExpressionShowFragment expressionShowFragment;
    private String filePath;
    private long firstlimitTime;
    private FrameLayout fl_add;
    private FrameLayout fl_container;
    private FrameLayout fl_emogi;
    private String flag;
    private long fromLastReadTime;
    private FrameLayout fullscreenContainer;
    private ExpressionEditText getmExpressionEditTextFull;
    private GPS gps;
    private GroupListPresenter groupListPresenter;
    private int groupNum;
    private ChatGroupUserPopupAdapter groupUserAdapter;
    private List<GroupUser.DataBean> group_user_list;
    private String gwmc;
    private Handler handler;
    private long heartTime;
    private PermissionDialog huaTiDialog;
    private IService iService;
    private AudioRecorderButton id_recorder_button;
    private Uri imageUri;
    private Intent intent;
    private boolean isAddShow;
    private boolean isDelete;
    private boolean isDeleteFull;
    private boolean isEditSet;
    private boolean isEmogiShow;
    private boolean isEmogiShowFull;
    private boolean isHave;
    private boolean isHello;
    private boolean isInBottom;
    private boolean isInBottom_1;
    private boolean isKeyboardShow;
    private boolean isNeedSjc;
    private String isOnBack;
    private boolean isOpenPop;
    private boolean isOpenSideslip;
    private boolean isPlayer;
    private boolean isPrepare;
    private boolean isPrimaryLocalization;
    private boolean isQuote;
    private boolean isSearchOrDing;
    private boolean isShow;
    private boolean isShowChat;
    private boolean isShowLocationEnable;
    private boolean isShowQxDialog;
    private boolean isShutUp;
    private boolean isTicketLocation;
    private boolean isVideo;
    private boolean isshowFull;
    private boolean isttsPlay;
    private ImageView iv_back_user;
    private ImageView iv_chat_person;
    private ImageView iv_chat_qun_person;
    private ImageView iv_close_full_screen;
    private ImageView iv_del_quote;
    private ImageView iv_icon;
    private ImageView iv_more;
    private ImageView iv_qun_call;
    private ImageView iv_qun_more;
    private ImageView iv_rengong;
    private ImageView iv_robot_keyboard;
    private ImageView iv_speech;
    private ImageView iv_text_to_speech;
    private ImageView iv_title_close;
    private ImageView iv_voice;
    private final String[][] jianCheng;
    private JsPromptResult jsPromptResult;
    private JSONObject jsonObject_post;
    private boolean keyBoardShow;
    private double latitude;
    private List<Message> list;
    private RelativeLayout ll;
    private CustomLinearLayoutManager llLayoutManager;
    private LinearLayout ll_chat;
    private LinearLayout ll_company;
    private LinearLayout ll_content;
    private LinearLayout ll_content_view;
    private LinearLayout ll_no_speaking;
    private LinearLayout ll_norobot_button_root;
    private LinearLayout ll_robot_1;
    private LinearLayout ll_robot_2;
    private LinearLayout ll_robot_3;
    private LinearLayout ll_robot_back_root;
    private LinearLayout ll_robot_button_root;
    private LinearLayout ll_sendmessage;
    private RelativeLayout ll_web;
    private TTSService.LocalBinder localBinder;
    private GPS.LocationCallBack locationCallBack;
    private double longtitude;
    private AliRtcEngine mAliRtcEngine;
    private ACache mCache;
    private PopupWindow mCareButtonMorePopup;
    private ExpressionEditText mEetHuaTi;
    private String mEtChannelId;
    private PermissionUtils.PermissionGrant mGrant;
    private int mHeight100;
    private int mHeight30;
    private HorizontalScrollView mHsvMain;
    private LinearLayout mHsvMainContent;
    private String mId;
    private ImageView mIvEmojFull;
    private ImageView mIvForwardItem;
    private ImageView mIvForwardMerge;
    private ImageView mIvFullScreen;
    private ImageView mIvOpenOrClose;
    private ImageView mIvOpenOrCloseHuaTi;
    private LinearLayout mLlForwardContent;
    private LinearLayout mLlForwardItem;
    private LinearLayout mLlForwardMerge;
    private LinearLayout mLlHuaTiLaiYuanContext;
    private LinearLayout mLlTitleRightContent;
    public AMapLocationClientOption mLocationOption;
    private AliRtcLoginPresenter mLoginPresenter;
    private String mName;
    List<String> mPermissionList;
    private PermissionsUtils.IPermissionsResult mPermissionsResult;
    private ProgressDialog mProgressDialog;
    private final int mRequestCode;
    private RelativeLayout mRlHuaTiContent;
    private RelativeLayout mRvEditEmojiconContent;
    private RelativeLayout mRvFullScreen;
    private TextView mTvForwardItem;
    private TextView mTvForwardMerge;
    private TextView mTvHuaTiLaiYuan;
    private TextView mTvMultipleChoiceCancle;
    private LinearLayout mTvNewMessageDialog;
    private TextView mTvSendFull;
    private TextView mTvSendHuaTi;
    public int mType;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<String> mUploadStringCallback;
    private View mViewMask;
    private MailListDaoManager mailListDaoManager;
    private MailListPrimaryDepartmentDaoManager mailListPrimaryDepartmentDaoManager;
    private MailPresenter mailPresenter;
    private AMapLocationClient mapLocationClient;
    private AMapLocationClientOption mapLocationClientOption;
    private String mc;
    File mediaFile;
    private String meetingParams;
    private Message message1;
    private String mettingId;
    private boolean mkfFlag;
    public AMapLocationClient mlocationClient;
    public Handler myHandler;
    private MyThread myThread;
    private NavicationCountUtils navicationCountUtils;
    private String newAiPayData;
    private NewLoginModule newLoginModule;
    private NoSpeakingDaoManager noSpeakingDaoManager;
    private LinearLayout no_network;
    private int oldBrightness;
    private String otherObj;
    private String params;
    private String params2;
    private String path;
    String[] permissions;
    PermissionsUtils.IPermissionsResult permissionsResult_audio;
    PermissionsUtils.IPermissionsResult permissionsResult_audio2;
    PermissionsUtils.IPermissionsResult permissionsResult_audio_chat;
    PermissionsUtils.IPermissionsResult permissionsResult_camera;
    PermissionsUtils.IPermissionsResult permissionsResult_location;
    PermissionsUtils.IPermissionsResult permissionsResult_location_new;
    PermissionsUtils.IPermissionsResult permissionsResult_phone;
    PermissionsUtils.IPermissionsResult permissionsResult_pic;
    PermissionsUtils.IPermissionsResult permissionsResult_video;
    PermissionsUtils.IPermissionsResult permissionsResult_video_chat;
    PermissionsUtils.IPermissionsResult permissionsResult_write_external_storage;
    PermissionsUtils.IPermissionsResult permissionsResult_yinpin;
    String[] permissions_audio;
    String[] permissions_location;
    String[] permissions_phone;
    String[] permissions_video;
    String[] permissions_write_external_storage;
    PermissionsUtils.IPermissionsResult permissionssave;
    private boolean phoneBz;
    private PlayerService playerService;
    private boolean popIsShow;
    private PopupWindow popupWindow;
    private MorePopQunZu popwindows;
    private int preRefresh;
    private String private_url;
    private ProgressBar progressBar;
    private QunZuItemInfoDaoManager qunZuItemInfoDaoManager;
    private QuoteDialog quoteDialog;
    private HashMap<String, String> quoteMap;
    private Message quoteMessage;
    private long refreshTime;
    private RestTemplate restTemplate;
    String rightBarIcon;
    String rightBarTitle;
    RelativeLayout rl_back;
    RelativeLayout rl_call;
    private RelativeLayout rl_content;
    private RelativeLayout rl_full_screen_content;
    private RelativeLayout rl_notFound;
    private RelativeLayout rl_quote;
    private RelativeLayout rl_qx_content;
    RelativeLayout rl_shipin;
    private RelativeLayout rl_text_to_speech;
    private RelativeLayout rl_title;
    RelativeLayout rl_yuyin;
    private RecyclerView rv_msg;
    private RecyclerView rv_user;
    private Bitmap saveBitmap;
    private Bitmap saveBitmapBp;
    private Runnable saveBitmapRunnable;
    private ScrollView scroll;
    private String search;
    private SeekBar seekbar;
    private ServiceConnection serviceConnection;
    private boolean sfgly;
    private boolean sfjy;
    private boolean sfqz;
    private int sphyType;
    private long startTimeChat;
    private int start_ait;
    private int supportSoftInputHeight;
    private SwipeRefreshLayout sv_msg;
    private boolean sxtFlag;
    private TabLayout tabLayoutCurr;
    private String text;
    private PopupWindow textToSpeechPopwWindow;
    private String textToSpeechTitle;
    private String title;
    private String titleParms;
    private long toLastReadTime;
    private String toName;
    private String toUser;
    private String toUserName;
    private int tryAgainTimes;
    private TTSPlayerManager ttsManager;
    private TextView tv_company;
    private TextView tv_dept;
    private TextView tv_detail;
    private TextView tv_no_net;
    private TextView tv_permission_detail;
    private TextView tv_permission_name;
    private TextView tv_quote;
    TextView tv_quxiao;
    private TextView tv_robot_1;
    private TextView tv_robot_2;
    private TextView tv_robot_3;
    private TextView tv_time;
    private TextView tv_title;
    private TextView tv_title_chat;
    private TextView tv_title_more;
    private TextView tv_title_num;
    private TextView tv_total_num;
    private TextView tv_total_num_new;
    private String type;
    private NewUpdateDialog updateDialog;
    private String url;
    private final int video_mRequestCode;
    String[] video_permissions;
    private View view;
    private WebActivityApi webActivityApi;
    private List<String> webButtonName;
    private DWebView webView;
    private Stack<Activity> webactivityStack;
    String webtitle;
    private PopupWindow wenjianPopWindow;
    private String xmbh;
    private String xmmc;
    private PopupWindow yinShiPinPopupWindow;
    private String yqrIds;
    private String yqrNames;
    private String zfChatMsgResultName;
    private Context mContext = this;
    private String TAG = "WebActivity";
    private final int FILECHOOSER_RESULTCODE = 1;
    private final int BACK_LAST = 2;
    private final int BACK_MAIN = 3;
    private final int TAKE_PHOTO = 4;
    private int secence = 1;
    private boolean CAN_CLEAR_BASIC = true;
    private int position = -1;
    private long newsId = 0;
    protected final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(-1, -1);
    private final String APP_CACHE_DIRNAME = "/webcache";
    public Handler mHandler = new Handler() { // from class: com.shineyue.pm.web.WebActivity.1
        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            int i = msg.what;
            if (i != 2) {
                if (i == 3) {
                    WebActivity.this.removeAllActivity();
                }
            } else if (WebActivity.this.isOpenPop) {
                WebActivity.this.webView.loadUrl("javascript:onOpenPopListener()");
            } else {
                WebActivity.this.finish();
            }
            super.handleMessage(msg);
        }
    };
    private String user_info = "";
    private String cpbs = "";
    private String cpbm = "";
    private String typeFlag = "";
    long startTime = 0;
    public boolean isLoading = false;
    long time = System.currentTimeMillis();
    public final String SM3_KEY = "0123456789abcdeffedcba9876543210";
    private String SM4_KEY = "0123456789abcdeffedcba9876543210";
    private final String SM4_IV = "fedcba98765432100123456789abcdef";
    public final int TYPE_GENERAL_V = 1;
    public final int TYPE_IDCARD_HEAD_SCAN = 2;
    public final int TYPE_IDCARD_NATION_SCAN = 3;
    public final int TYPE_BANKCARD_SCAN = 4;
    public final int TYPE_TRAIN_TICKET_SCAN = 5;
    public final int TYPE_INVOICE_TAKING = 6;
    public final int TYPE_MARRIAGE_CERTIFICATE_TAKING = 7;
    public final int TYPE_BUSINESS_LICENSE_TAKING = 8;
    public final int TYPE_ESTATE_CERTIFICATE_TAKING = 9;
    public final int TYPE_ESTATE_REGISTRATION_TAKING = 10;
    public final int TYPE_PRC_TAKING = 11;
    public final int REQUEST_CODE = 17;
    public final int RESULT_CODE = 18;
    public final int PERMISSION_CODE_FIRST = 19;
    public final String TAKE_TYPE = "take_type";
    public final String IMAGE_PATH = "image_path";
    private boolean zfChatMsgNeedResult = false;
    private String saveImageUrl = "";
    private String saveImageName = "";
    private Runnable saveImageRunnable = new Runnable() { // from class: com.shineyue.pm.web.WebActivity.19
        @Override // java.lang.Runnable
        public void run() {
            try {
                KLog.i(WebActivity.this.TAG, WebActivity.this.saveImageUrl);
                String fileUrl = WebActivity.this.saveImageUrl;
                KLog.i(WebActivity.this.TAG, fileUrl);
                Bitmap bmp = Glide.with(WebActivity.this.mContext).asBitmap().load(fileUrl).into(Integer.MIN_VALUE, Integer.MIN_VALUE).get();
                KLog.i(WebActivity.this.TAG, bmp);
                if (bmp == null) {
                    return;
                }
                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();
                String str = "数字工商联图片" + System.currentTimeMillis() + ".jpg";
                File currentFile = new File(file, WebActivity.this.saveImageName);
                FileOutputStream fos = null;
                try {
                    try {
                        try {
                            fos = new FileOutputStream(currentFile);
                            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.flush();
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e = e;
                                e.printStackTrace();
                            }
                        } catch (IOException e2) {
                            e2.printStackTrace();
                            if (fos != null) {
                                try {
                                    fos.close();
                                } catch (IOException e3) {
                                    e = e3;
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (FileNotFoundException e4) {
                        e4.printStackTrace();
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e5) {
                                e = e5;
                                e.printStackTrace();
                            }
                        }
                    }
                    WebActivity.this.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(new File(currentFile.getPath()))));
                    WebActivity.this.runOnUiThread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.19.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ToastUtil.toast(WebActivity.this.mContext, "msg", "已保存至相册", ToastType.SUCCESS);
                        }
                    });
                } catch (Throwable th) {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e6) {
                            e6.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Exception e7) {
                KLog.e("PreViewMediaActivity", e7.getMessage());
            }
        }
    };
    protected CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    private final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 2323;
    private final int CAPTURE_PHOTO_ACTIVITY_REQUEST_CODE = 2324;
    private final int SELECT_ORIGINAL_PIC = 2325;
    String fileCache = CachePzthConfigs.getIns().getFilesDir(true, "image") + "/shineyuepm" + File.separator + TransportConstants.VALUE_UP_MEDIA_TYPE_IMG;
    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    public WebActivity() {
        File file = new File(this.fileCache + File.separator + "IMG_" + this.timeStamp + ".jpg");
        this.mediaFile = file;
        this.cameraPath = file.getAbsolutePath();
        this.TAKE_PICTURE = 520;
        this.permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        this.video_permissions = new String[]{"android.permission.CAMERA"};
        this.mPermissionList = new ArrayList();
        this.video_mRequestCode = 101;
        this.mRequestCode = 102;
        this.webactivityStack = new Stack<>();
        this.PERF_KEY_REFRESH_SIGN_SESSION_TIME = WebActivity.class.getName() + ".PERF_KEY_REFRESH_SIGN_SESSION_TIME";
        this.changeScreenbrightness = false;
        this.brightness = 191;
        this.oldBrightness = 0;
        this.mGrant = new PermissionUtils.PermissionGrant() { // from class: com.shineyue.pm.web.WebActivity.31
            @Override // com.shineyue.pm.aliyun_rtc.utils.PermissionUtils.PermissionGrant
            public void onPermissionGranted(int requestCode) {
            }

            @Override // com.shineyue.pm.aliyun_rtc.utils.PermissionUtils.PermissionGrant
            public void onPermissionCancel() {
                Toast.makeText(WebActivity.this.mContext, WebActivity.this.getString(R.string.alirtc_permission), 0).show();
                WebActivity.this.finish();
            }
        };
        this.mLocationOption = null;
        this.isPrimaryLocalization = false;
        this.jianCheng = new String[][]{new String[]{"11", "京"}, new String[]{"12", "津"}, new String[]{"13", "冀"}, new String[]{"14", "晋"}, new String[]{"15", "内蒙古"}, new String[]{FFmpegSessionConfig.CRF_21, "辽"}, new String[]{FFmpegSessionConfig.CRF_22, "吉"}, new String[]{FFmpegSessionConfig.CRF_23, "黑"}, new String[]{"31", "沪"}, new String[]{"32", "苏"}, new String[]{"33", "浙"}, new String[]{"34", "皖"}, new String[]{"35", "闽"}, new String[]{"36", "赣"}, new String[]{"37", "鲁"}, new String[]{"41", "豫"}, new String[]{"42", "鄂"}, new String[]{"43", "湘"}, new String[]{"44", "粤"}, new String[]{"45", "桂"}, new String[]{"46", "琼"}, new String[]{"50", "渝"}, new String[]{"51", "川"}, new String[]{"52", "贵"}, new String[]{"53", "云"}, new String[]{"54", "藏"}, new String[]{"61", "陕"}, new String[]{"62", "干"}, new String[]{"63", "青"}, new String[]{"64", "宁"}, new String[]{"65", "新"}, new String[]{"81", "港"}, new String[]{"82", "澳"}, new String[]{"71", "台"}};
        this.permissions_location = new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};
        this.permissionsResult_location = new PermissionsUtils.IPermissionsResult() { // from class: com.shineyue.pm.web.WebActivity.37
            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void passPermissons() {
                KLog.i(WebActivity.this.TAG, "sucess");
                WebActivity.this.startLongLocation();
                WebActivity.this.rl_qx_content.setVisibility(8);
                WebActivity.this.isShowQxDialog = false;
                PermissionsUtils.getInstance().dissmissPermissionDialogWebLocation();
            }

            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void forbitPermissons() {
                KLog.i(WebActivity.this.TAG, StreamerConstants.FALSE);
                WebActivity.this.startLongLocation();
                if (!PermissionsUtils.getInstance().isShowLocationWeb) {
                    KLog.i(WebActivity.this.TAG, "111");
                    PermissionsUtils.getInstance().showPermissionDialogWebLocation(WebActivity.this.mContext, 0, WebActivity.this.rl_qx_content);
                }
            }
        };
        this.permissionsResult_location_new = new PermissionsUtils.IPermissionsResult() { // from class: com.shineyue.pm.web.WebActivity.38
            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void passPermissons() {
                KLog.i(WebActivity.this.TAG, "sucess");
                WebActivity.this.initLocationNew();
                WebActivity.this.rl_qx_content.setVisibility(8);
                WebActivity.this.isShowQxDialog = false;
                PermissionsUtils.getInstance().dissmissPermissionDialogWebLocation();
            }

            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void forbitPermissons() {
                KLog.i(WebActivity.this.TAG, StreamerConstants.FALSE);
                WebActivity.this.initLocationNew();
                if (!PermissionsUtils.getInstance().isShowLocationWeb) {
                    KLog.i(WebActivity.this.TAG, "111");
                    PermissionsUtils.getInstance().showPermissionDialogWebLocation(WebActivity.this.mContext, 0, WebActivity.this.rl_qx_content);
                }
            }
        };
        this.NOT_SAME_PERSON = "NOT_SAME_PERSON";
        this.UNABLE_GET_IMAGE = "UNABLE_GET_IMAGE";
        this.permissionssave = new PermissionsUtils.IPermissionsResult() { // from class: com.shineyue.pm.web.WebActivity.42
            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void passPermissons() {
                WebActivity webActivity = WebActivity.this;
                webActivity.saveBitmap(webActivity.saveBitmap, WebActivity.this.bitmapName);
            }

            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void forbitPermissons() {
                PermissionsUtils.getInstance().showPermissionDialog(WebActivity.this.mContext, 1);
            }
        };
        this.saveBitmapRunnable = new Runnable() { // from class: com.shineyue.pm.web.WebActivity.43
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r4v0 */
            /* JADX WARN: Type inference failed for: r4v5, types: [android.content.Context] */
            /* JADX WARN: Type inference failed for: r4v6 */
            @Override // java.lang.Runnable
            public void run() {
                Log.d(WebActivity.this.TAG, "Build.BRAND============" + Build.BRAND);
                File file2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile(), System.currentTimeMillis() + ".jpg");
                FileOutputStream fileOutputStream = null;
                Bitmap bitmap = 0;
                bitmap = 0;
                try {
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    try {
                        fileOutputStream = new FileOutputStream(file2);
                        WebActivity.this.saveBitmapBp.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        fileOutputStream.flush();
                        WebActivity.this.runOnUiThread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.43.1
                            @Override // java.lang.Runnable
                            public void run() {
                                ToastUtil.toast(WebActivity.this.mContext, ToastInfo.btnSuccess, "保存", ToastType.SUCCESS);
                                WebActivity.this.saveBitmap = null;
                                WebActivity.this.bitmapName = null;
                            }
                        });
                        fileOutputStream.close();
                        WebActivity.this.saveBitmapBp = null;
                    } catch (Exception e2) {
                        KLog.i(WebActivity.this.TAG, e2.toString());
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                            WebActivity.this.saveBitmapBp = null;
                        }
                        bitmap = WebActivity.this.mContext;
                        bitmap.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(new File(file2.getPath()))));
                    }
                    bitmap = WebActivity.this.mContext;
                    bitmap.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(new File(file2.getPath()))));
                } catch (Throwable th) {
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                            WebActivity.this.saveBitmapBp = bitmap;
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    throw th;
                }
            }
        };
        this.mettingId = "";
        this.toName = "";
        this.yqrIds = "";
        this.yqrNames = "";
        this.permissions_video = new String[]{"android.permission.CAMERA"};
        this.permissions_audio = new String[]{"android.permission.RECORD_AUDIO"};
        this.permissionsResult_video = new PermissionsUtils.IPermissionsResult() { // from class: com.shineyue.pm.web.WebActivity.47
            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void passPermissons() {
                WebActivity.this.checkAudioPermission();
            }

            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void forbitPermissons() {
                PermissionsUtils.getInstance().showPermissionDialog(WebActivity.this.mContext, 2);
                WebActivity.this.mettingId = "";
                WebActivity.this.toName = "";
                WebActivity.this.yqrIds = "";
                WebActivity.this.yqrNames = "";
                WebActivity.this.sxtFlag = false;
                WebActivity.this.mkfFlag = false;
                WebActivity.this.sphyType = 0;
            }
        };
        this.permissionsResult_audio = new PermissionsUtils.IPermissionsResult() { // from class: com.shineyue.pm.web.WebActivity.48
            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void passPermissons() {
                if (WebActivity.this.sphyType == 1) {
                    WebActivity.this.startMeeting();
                    return;
                }
                if (WebActivity.this.sphyType == 2) {
                    WebActivity.this.joinMeeting();
                } else if (WebActivity.this.sphyType == 3) {
                    WebActivity.this.joinMeeting();
                } else if (WebActivity.this.sphyType == 4) {
                    WebActivity.this.joinLive();
                }
            }

            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void forbitPermissons() {
                PermissionsUtils.getInstance().showPermissionDialog(WebActivity.this.mContext, 3);
                WebActivity.this.mettingId = "";
                WebActivity.this.toName = "";
                WebActivity.this.yqrIds = "";
                WebActivity.this.yqrNames = "";
                WebActivity.this.sxtFlag = false;
                WebActivity.this.mkfFlag = false;
                WebActivity.this.sphyType = 0;
            }
        };
        this.textToSpeechTitle = "";
        this.text = "";
        this.isttsPlay = true;
        this.BINDINGACTIVITY = false;
        this.serviceConnection = null;
        this.list = new ArrayList();
        this.supportSoftInputHeight = 0;
        this.myThread = new MyThread();
        this.startTimeChat = 0L;
        this.endTime = 0L;
        this.heartTime = 0L;
        this.isShowChat = false;
        this.fromLastReadTime = 0L;
        this.toLastReadTime = 0L;
        this.tryAgainTimes = 0;
        this.isHave = false;
        this.isInBottom = true;
        this.isInBottom_1 = true;
        this.firstlimitTime = -1L;
        this.isEditSet = false;
        this.quoteMap = new HashMap<>();
        this.myHandler = new Handler() { // from class: com.shineyue.pm.web.WebActivity.66
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                int i = msg.what;
                if (i == 291) {
                    KLog.i(WebActivity.this.TAG, 318);
                    WebActivity.this.reTrySendMsg(msg);
                    return;
                }
                if (i != 564) {
                    if (i == 4660) {
                        KLog.i(WebActivity.this.TAG, Integer.valueOf(TbsListener.ErrorCode.ERROR_TBSINSTALLER_ISTBSCORELEGAL_02));
                        WebActivity.this.testIsServiceOpen(msg);
                        return;
                    }
                    return;
                }
                String uqIdentNo = msg.getData().getString("uqIdentNo");
                List<Message> msgs = GreenDaoHelper.getDaoSession().getMessageDao().queryBuilder().where(MessageDao.Properties.UqIdentNo.eq(uqIdentNo), new WhereCondition[0]).build().list();
                if (msgs.size() != 0 && msgs.get(0).sendStatus == 9) {
                    OutPutLogUtils.uMengLog("消息发送超时：" + msgs.get(0).uqIdentNo);
                    msgs.get(0).sendStatus = 2;
                    GreenDaoHelper.getDaoSession().getMessageDao().update(msgs.get(0));
                    EventBus.getDefault().post(new Notify.Message(1201, msgs.get(0)));
                    if (ChatServiceNew.loadMessages != null) {
                        Iterator<Message> iterator = ChatServiceNew.loadMessages.iterator();
                        while (iterator.hasNext()) {
                            Message next = iterator.next();
                            if (msgs.get(0).uqIdentNo.equals(next.uqIdentNo)) {
                                iterator.remove();
                                return;
                            }
                        }
                    }
                }
            }
        };
        this.permissions_write_external_storage = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        this.permissions_phone = new String[]{"android.permission.READ_PHONE_STATE"};
        this.groupNum = -1;
        this.TAKE_PICTURE_CHAT = 521;
        this.permissionsResult_camera = new PermissionsUtils.IPermissionsResult() { // from class: com.shineyue.pm.web.WebActivity.124
            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void passPermissons() throws XmlPullParserException, IOException {
                Intent openCameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                Uri imageUri = MyPhotoUtils.getOutputMediaFileUri(WebActivity.this.getApplicationContext(), WebActivity.this.fileCache, WebActivity.this.timeStamp, WebActivity.this.mediaFile);
                openCameraIntent.putExtra("output", imageUri);
                WebActivity.this.startActivityForResult(openCameraIntent, 521);
            }

            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void forbitPermissons() {
                PermissionsUtils.getInstance().showPermissionDialog(WebActivity.this.mContext, 2);
            }
        };
        this.permissionsResult_pic = new PermissionsUtils.IPermissionsResult() { // from class: com.shineyue.pm.web.WebActivity.125
            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void passPermissons() {
                Intent intent = new Intent(WebActivity.this.mContext, (Class<?>) ImageGridActivity.class);
                WebActivity.this.startActivityForResult(intent, 291);
            }

            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void forbitPermissons() {
                PermissionsUtils.getInstance().showPermissionDialog(WebActivity.this.mContext, 1);
            }
        };
        this.handler = new Handler() { // from class: com.shineyue.pm.web.WebActivity.129
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                Bitmap bitMap;
                super.handleMessage(msg);
                if (msg.what == 100) {
                    ((LinearLayout.LayoutParams) WebActivity.this.rl_content.getLayoutParams()).weight = 1.0f;
                } else {
                    if (msg.what != 200 || (bitMap = MyPhotoUtils.getSmallBitmap(WebActivity.this.cameraPath)) == null) {
                        return;
                    }
                    MyPhotoUtils.bitmapToByte(bitMap);
                }
            }
        };
        this.ISLOADING = false;
        this.permissionsResult_audio_chat = new PermissionsUtils.IPermissionsResult() { // from class: com.shineyue.pm.web.WebActivity.143
            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void passPermissons() {
                WebActivity.this.isVideo = true;
                WebActivity.this.editEmojicon.setVisibility(8);
                WebActivity.this.mRvEditEmojiconContent.setVisibility(8);
                WebActivity.this.id_recorder_button.setVisibility(0);
                WebActivity.this.iv_voice.setImageResource(R.mipmap.chat_key);
                WebActivity.this.hideAddPanel();
                WebActivity webActivity = WebActivity.this;
                webActivity.hideKeyboard(webActivity);
                WebActivity.this.hideEmogiPanel();
            }

            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void forbitPermissons() {
                PermissionsUtils.getInstance().showPermissionDialog(WebActivity.this.mContext, 3);
            }
        };
        this.permissionsResult_yinpin = new PermissionsUtils.IPermissionsResult() { // from class: com.shineyue.pm.web.WebActivity.144
            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void passPermissons() {
                if (CallingActivity.IS_LIVE) {
                    ToastUtil.toast(WebActivity.this.mContext, "msg", "当前正在会议中，请稍后", ToastType.WARNING);
                } else {
                    TRTCSetSocketUtil.sendStartMsg(1, WebActivity.this.toUser, WebActivity.this.toUserName, WebActivity.this.toUser, WebActivity.this.toUserName, 1);
                }
            }

            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void forbitPermissons() {
                PermissionsUtils.getInstance().showPermissionDialog(WebActivity.this.mContext, 3);
            }
        };
        this.permissionsResult_video_chat = new PermissionsUtils.IPermissionsResult() { // from class: com.shineyue.pm.web.WebActivity.145
            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void passPermissons() {
                WebActivity.this.checkAudcioPermissionChat();
            }

            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void forbitPermissons() {
                PermissionsUtils.getInstance().showPermissionDialog(WebActivity.this.mContext, 2);
            }
        };
        this.permissionsResult_audio2 = new PermissionsUtils.IPermissionsResult() { // from class: com.shineyue.pm.web.WebActivity.146
            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void passPermissons() {
                if (!CallingActivity.IS_LIVE) {
                    if (WebActivity.this.chatType.equals("3") || WebActivity.this.chatType.equals("6")) {
                        TRTCSetSocketUtil.sendStartMsg(1, "single" + WebActivity.this.toUser, WebActivity.this.mName + "的视频通话", WebActivity.this.toUser, WebActivity.this.toUserName, 2);
                        return;
                    }
                    CallingActivity.UserInfo userInfo = new CallingActivity.UserInfo();
                    userInfo.userId = WebActivity.this.mId;
                    userInfo.userName = WebActivity.this.mName;
                    CallingActivity.startGroupMeeting(DavikActivityUtils.getScreenManager().getCurrentActivity(), userInfo, WebActivity.this.toUser, WebActivity.this.toUserName);
                    return;
                }
                PermissionsUtils.getInstance().showPermissionDialog(WebActivity.this.mContext, 2);
            }

            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void forbitPermissons() {
                PermissionsUtils.getInstance().showPermissionDialog(WebActivity.this.mContext, 3);
            }
        };
        this.permissionsResult_phone = new PermissionsUtils.IPermissionsResult() { // from class: com.shineyue.pm.web.WebActivity.147
            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void passPermissons() {
                WebActivity.this.mailListDaoManager.getMailInfoById(WebActivity.this.toUser, new MailListDaoManager.OnMailListItem() { // from class: com.shineyue.pm.web.WebActivity.147.1
                    @Override // com.shineyue.pm.modle_maillist.dao.MailListDaoManager.OnMailListItem
                    public void onMailItemResult(MailItem mailItem) {
                        if (mailItem == null) {
                            ToastUtil.toast(WebActivity.this.mContext, "msg", "暂无获取此用户手机号码", ToastType.FAIL);
                            return;
                        }
                        String phone = mailItem.sjhm;
                        if (TextUtils.isEmpty(phone)) {
                            ToastUtil.toast(WebActivity.this.mContext, "msg", "暂无获取此用户手机号码", ToastType.FAIL);
                            return;
                        }
                        Intent intentCall = new Intent("android.intent.action.DIAL");
                        Uri data = Uri.parse(WebView.SCHEME_TEL + phone);
                        intentCall.setData(data);
                        if (intentCall.resolveActivity(WebActivity.this.mContext.getPackageManager()) != null) {
                            WebActivity.this.mContext.startActivity(intentCall);
                        } else {
                            ToastUtil.toast(WebActivity.this.mContext, "msg", "暂无获取此用户手机号码", ToastType.FAIL);
                        }
                    }
                });
            }

            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void forbitPermissons() {
                PermissionsUtils.getInstance().showPermissionDialog(WebActivity.this.mContext, 4);
            }
        };
        this.permissionsResult_write_external_storage = new PermissionsUtils.IPermissionsResult() { // from class: com.shineyue.pm.web.WebActivity.148
            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void passPermissons() {
                Intent intentWenJian = new Intent("android.intent.action.OPEN_DOCUMENT");
                intentWenJian.setType(MediaType.ALL_VALUE);
                intentWenJian.addCategory("android.intent.category.OPENABLE");
                intentWenJian.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
                WebActivity.this.startActivityForResult(intentWenJian, 1208);
            }

            @Override // com.shineyue.pm.utils.PermissionsUtils.IPermissionsResult
            public void forbitPermissons() {
                PermissionsUtils.getInstance().showPermissionDialog(WebActivity.this.mContext, 1);
            }
        };
        this.popIsShow = false;
        this.rl_back = null;
        this.rl_call = null;
        this.rl_yuyin = null;
        this.rl_shipin = null;
        this.tv_quxiao = null;
    }

    static /* synthetic */ int access$20208(WebActivity x0) {
        int i = x0.tryAgainTimes;
        x0.tryAgainTimes = i + 1;
        return i;
    }

    @Override // com.shineyue.pm.BaseTimeWebActivity, com.shineyue.pm.BaseWebWithAdapterActivity, com.shineyue.pm.BasicActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webv1);
        EventBus.getDefault().register(this);
        this.cheque = Utils.getIntMsg(this.mContext, "cheque");
        initView();
        if (!"1".equals(Utils.getMsg(this.mContext, "initMap"))) {
            KLog.i(this.TAG, "map");
            AMapLocationClient.updatePrivacyAgree(this, true);
            AMapLocationClient.updatePrivacyShow(this, true, true);
            Utils.saveMsg(this.mContext, "initMap", "1");
        }
        this.eventDispatchUtil = new EventDispatchUtil(this.mContext);
        this.webActivityApi = new WebActivityApi(this.mContext);
        this.CAN_CLEAR_BASIC = true;
        getWindow().addFlags(16777216);
        this.mCache = OaApplication.getmACache();
        Intent intent = getIntent();
        intent.getStringExtra("flag");
        try {
            this.mlocationClient = new AMapLocationClient(getApplicationContext());
        } catch (Exception e) {
            KLog.i(this.TAG, e.toString());
        }
        initData();
    }

    private void initView() {
        this.tv_title = (TextView) findViewById(R.id.abc_title_txt);
        this.abc_title_back = (ImageView) findViewById(R.id.abc_title_back);
        this.iv_title_close = (ImageView) findViewById(R.id.abc_title_close);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.tv_title_more = (TextView) findViewById(R.id.textView_more);
        this.iv_more = (ImageView) findViewById(R.id.iv_more);
        this.ll = (RelativeLayout) findViewById(R.id.ll);
        this.ll_web = (RelativeLayout) findViewById(R.id.ll);
        this.rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        this.rl_text_to_speech = (RelativeLayout) findViewById(R.id.rl_text_to_speech);
        this.iv_text_to_speech = (ImageView) findViewById(R.id.iv_text_to_speech);
        this.iv_speech = (ImageView) findViewById(R.id.iv_speech);
        this.rl_notFound = (RelativeLayout) findViewById(R.id.rl_notFound);
        this.no_network = (LinearLayout) findViewById(R.id.no_network);
        this.fl_container = (FrameLayout) findViewById(R.id.fl_container);
        CustomLoadingView customLoadingView = (CustomLoadingView) findViewById(R.id.custom_loading);
        this.customLoadingView = customLoadingView;
        customLoadingView.setVisibility(8);
        this.rl_title.setVisibility(0);
        this.tv_no_net = (TextView) findViewById(R.id.tv_no_net);
        this.btn_retry = (Button) findViewById(R.id.btn_retry);
        this.abc_title_txt_close = (TextView) findViewById(R.id.abc_title_txt_close);
        this.rl_qx_content = (RelativeLayout) findViewById(R.id.rl_qx_content);
        this.tv_permission_name = (TextView) findViewById(R.id.tv_permission_name);
        this.tv_permission_detail = (TextView) findViewById(R.id.tv_permission_detail);
        this.ll_chat = (LinearLayout) findViewById(R.id.ll_chat);
        findViewById(R.id.btn).setOnClickListener(this);
        initLinister();
    }

    private void initLinister() {
        this.abc_title_back.setOnClickListener(this);
        this.iv_title_close.setOnClickListener(this);
        this.rl_notFound.setOnClickListener(this);
        this.btn_retry.setOnClickListener(this);
        this.abc_title_txt_close.setOnClickListener(this);
        this.rl_text_to_speech.setOnClickListener(this);
        this.iv_speech.setOnClickListener(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void noNetShow(boolean show) {
        if (show) {
            this.no_network.setVisibility(0);
            if (NetUtils.checkWifi(this.mContext).intValue() == 3) {
                this.tv_no_net.setText("暂无网络");
                return;
            } else {
                this.tv_no_net.setText("网络不给力");
                return;
            }
        }
        this.no_network.setVisibility(8);
    }

    public void initData() {
        char c;
        this.SM4_KEY = Utils.getMsg(this.mContext, "sm4key");
        this.intent = getIntent();
        this.title = getIntent().getStringExtra("web_title");
        this.typeFlag = getIntent().getStringExtra("typeFlag");
        this.titleParms = getIntent().getStringExtra("titleParms");
        this.otherObj = getIntent().getStringExtra("otherObj");
        this.tv_title.setText(this.title);
        KLog.d(this.TAG, "title==" + this.title);
        String stringExtra = getIntent().getStringExtra("type");
        this.type = stringExtra;
        if (TextUtils.isEmpty(stringExtra)) {
            this.type = "";
        }
        String stringExtra2 = getIntent().getStringExtra("web_url");
        this.private_url = stringExtra2;
        this.url = stringExtra2;
        if (getIntent().hasExtra("position")) {
            this.position = getIntent().getIntExtra("position", -1);
        }
        if (getIntent().hasExtra("newsId")) {
            this.newsId = getIntent().getLongExtra("newsId", 0L);
        }
        if (Utils.getBooleanMsg(this.mContext, "newsModule") && this.newsId != 0) {
            this.tv_title.setText("详情");
        }
        KLog.i(this.TAG, this.private_url);
        try {
            JSONObject jsonObject = new JSONObject();
            if (this.url.contains("register.html")) {
                jsonObject.put("xingming", Utils.getMsg(this.mContext, "zgxm").toString());
                jsonObject.put("zjhm", Utils.getMsg(this.mContext, "zjhm").toString());
                jsonObject.put("zfbzh", Utils.getMsg(this.mContext, "zfbzh").toString());
            } else {
                JSONObject result = new JSONObject(Utils.getMsg(this.mContext, "UserXx"));
                KLog.i("用户信息:" + result.toString());
                jsonObject.put("blqd", "app_02");
                jsonObject.put("userid", result.optString("userid"));
                jsonObject.put("zxbm", result.optString("zxjgbm"));
                jsonObject.put("client", Utils.getMsg(this.mContext, "client"));
                jsonObject.put("jgbm", result.optString("jgbm"));
                jsonObject.put("khbh", result.optString("grbh"));
                jsonObject.put("username", result.optString("username"));
                jsonObject.put("deptid", result.optString("deptid"));
                jsonObject.put("deptname", result.optString("deptname"));
                jsonObject.put(c.e, result.optString("username"));
                jsonObject.put("tokenid", result.optString("tokenid"));
                jsonObject.put("sjlx", "android");
                jsonObject.put("zjhm", result.optString("zjhm"));
                jsonObject.put("qycode", result.optString("qycode"));
                if (this.type.equals("0013")) {
                    jsonObject.put("grbh", result.optString("grbh"));
                    jsonObject.put("czlx", "add");
                }
                if (result.optString("zxjgbm") != null && result.optString("zxjgbm").length() >= 2) {
                    String zxjgbm = result.optString("zxjgbm").substring(0, 2);
                    KLog.i(this.TAG, zxjgbm);
                    if (zxjgbm.equals("09")) {
                        String bmjb = Utils.getMsg(this.mContext, "bmjb");
                        jsonObject.put("bmjb", Utils.getMsg(this.mContext, "bmjb"));
                        if (bmjb.equals(GroupListPresenter.CREATE_ITEM)) {
                            jsonObject.put("jgjgbm", Utils.getMsg(this.mContext, "jgjgbm"));
                            jsonObject.put("jgzjhm", "");
                            jsonObject.put("jgzxjgbm", Utils.getMsg(this.mContext, "jgzxjgbm"));
                            jsonObject.put("jgusername", "");
                            jsonObject.put("jguserid", "");
                            jsonObject.put("jggrbh", "");
                            jsonObject.put("jgdeptid", "");
                            jsonObject.put("jgdeptname", "");
                            jsonObject.put("jgjgmc", "");
                            jsonObject.put("zjbzxbmstr", Utils.getMsg(this.mContext, "zjbzxbmstr"));
                            jsonObject.put("zjbzxmc", Utils.getMsg(this.mContext, "zjbzxmc"));
                        } else {
                            jsonObject.put("jgjgbm", Utils.getMsg(this.mContext, "jgjgbm"));
                            jsonObject.put("jgzjhm", Utils.getMsg(this.mContext, "jgzjhm"));
                            jsonObject.put("jgzxjgbm", Utils.getMsg(this.mContext, "jgzxjgbm"));
                            jsonObject.put("jgusername", Utils.getMsg(this.mContext, "jgusername"));
                            jsonObject.put("jguserid", Utils.getMsg(this.mContext, "jguserid"));
                            jsonObject.put("jggrbh", Utils.getMsg(this.mContext, "jggrbh"));
                            jsonObject.put("jgdeptid", Utils.getMsg(this.mContext, "jgdeptid"));
                            jsonObject.put("jgdeptname", Utils.getMsg(this.mContext, "jgdeptname"));
                            jsonObject.put("jgjgmc", Utils.getMsg(this.mContext, "jgjgmc"));
                            jsonObject.put("zjbzxbmstr", Utils.getMsg(this.mContext, "zjbzxbmstr"));
                            jsonObject.put("zjbzxmc", Utils.getMsg(this.mContext, "zjbzxmc"));
                        }
                    }
                }
            }
            Map json = new HashMap();
            try {
                String version = getVersionName(this.mContext);
                jsonObject.put(AttributionReporter.APP_VERSION, version);
                jsonObject.put("version", version);
                jsonObject.put("appVersionCode", getVersionCode(this.mContext));
                jsonObject.put("deviceuuid", DeviceUtils.getUniqueId(this.mContext));
                if (NetUtils.checkWifi(this.mContext).intValue() == 2) {
                    jsonObject.put("isWifi", true);
                    jsonObject.put("wifiName", "");
                    jsonObject.put("wifiMac", "");
                } else {
                    jsonObject.put("isWifi", false);
                    jsonObject.put("wifiName", "");
                    jsonObject.put("wifiMac", "");
                }
                jsonObject.put("settings", new JSONObject(Utils.getMsg(this.mContext, "settings")));
            } catch (Exception e) {
                try {
                    KLog.i(this.TAG, e.toString());
                } catch (Exception e2) {
                    KLog.i(this.TAG, e2.toString());
                }
            }
            jsonObject.put("headphoto", this.mCache.getAsString("headimage"));
            jsonObject.put("blqd", Utils.getMsg(this.mContext, "blqd"));
            jsonObject.put("channel", Utils.getMsg(this.mContext, "blqd"));
            jsonObject.put("zzbs", Utils.getMsg(OaApplication.application, "zxjgbm"));
            jsonObject.put("loginToken", Utils.getMsg(OaApplication.application, "accessToken"));
            jsonObject.put("client", Utils.getMsg(this.mContext, "client"));
            jsonObject.put("qycode", Utils.getQyCode());
            jsonObject.put("zzbm", "bbPro");
            if (Utils.getBooleanMsg(this.mContext, "CAREMODE")) {
                jsonObject.put(TtmlNode.ATTR_TTS_FONT_SIZE, "1");
            } else {
                jsonObject.put(TtmlNode.ATTR_TTS_FONT_SIZE, "0");
            }
            if (!TextUtils.isEmpty(this.otherObj)) {
                addOtherObj(jsonObject);
                jsonObject.put("bpmparam", this.otherObj);
            }
            jsonObject.put("client", Utils.getMsg(this.mContext, "client"));
            jsonObject.put("loginToken", Utils.getMsg(this.mContext, "accessToken"));
            json = (Map) JSON.parse(jsonObject.toString());
            String userInfo = Utils.getMsg(OaApplication.application, "UserXx");
            JSONObject jsonObject1 = new JSONObject(userInfo).optJSONObject("zzjgxx");
            json.put("zzjgxx", com.alibaba.fastjson.JSONObject.toJSON((Map) JSON.parse(jsonObject1.toString())));
            this.params = com.alibaba.fastjson.JSONObject.toJSON(json).toString();
            KLog.d(this.TAG, "params==" + this.params);
            JSONObject jsonObject2 = new JSONObject();
            if (this.type.equals("0001") || this.type.equals("0099")) {
                this.params2 = jsonObject2.toString();
            } else if (this.type.equals("0002") || this.type.equals("0003")) {
                jsonObject2.put("bm", getIntent().getStringExtra("bm"));
                if (getIntent().getStringExtra("flbm") != null) {
                    jsonObject2.put("flbm", getIntent().getStringExtra("flbm"));
                }
                this.params2 = jsonObject2.toString();
            } else if (this.type.equals("0004") || this.type.equals("0005")) {
                jsonObject2.put("bpmid", getIntent().getStringExtra("bpmid"));
                this.params2 = jsonObject2.toString();
            } else if (this.type.equals("0006")) {
                jsonObject2.put("id", getIntent().getStringExtra("dwbh"));
                this.params2 = jsonObject2.toString();
            } else if (this.type.equals("0007")) {
                jsonObject2.put("deptid", getIntent().getStringExtra("deptid"));
                jsonObject2.put("deptdw", getIntent().getStringExtra("deptdw"));
                this.params2 = jsonObject2.toString();
            } else if (this.type.equals("0008") || this.type.equals("0009")) {
                String stringExtra3 = getIntent().getStringExtra("params2");
                this.params2 = stringExtra3;
                KLog.d(this.TAG, stringExtra3);
            } else if (this.type.equals("0010")) {
                jsonObject2.put("bpmid", getIntent().getStringExtra("bpmid"));
                jsonObject2.put("taskid", getIntent().getStringExtra("taskId"));
                jsonObject2.put("flowtype", "db");
                jsonObject2.put("taskDefinitionKey", getIntent().getStringExtra("taskDefinitionKey"));
                jsonObject2.put("processInstanceId", getIntent().getStringExtra("processInstanceId"));
                this.params2 = jsonObject2.toString();
            } else if (this.type.equals("0011")) {
                jsonObject2.put("bpmid", values(getIntent().getStringExtra("bpmid")));
                jsonObject2.put("taskid", values(getIntent().getStringExtra("taskid")));
                jsonObject2.put("taskDefinitionKey", values(getIntent().getStringExtra("taskDefinitionKey")));
                jsonObject2.put("flowtype", values(getIntent().getStringExtra("flowtype")));
                jsonObject2.put("processInstanceId", values(getIntent().getStringExtra("processInstanceId")));
                jsonObject2.put("taskName", values(getIntent().getStringExtra("taskName")));
                this.params2 = jsonObject2.toString();
                KLog.d(this.TAG, jsonObject2.toString());
            } else if (this.type.equals("0012")) {
                this.params2 = getIntent().getStringExtra("params2");
            } else if (this.type.equals("0013")) {
                jsonObject2.put("ispg", values("0"));
                this.params2 = jsonObject2.toString();
            }
            KLog.d(this.TAG, "params2==" + this.params2);
            c = 0;
        } catch (Exception e3) {
            c = 0;
            KLog.i(this.TAG, e3.toString());
        }
        String stringExtra4 = getIntent().getStringExtra("flag");
        this.flag = stringExtra4;
        String str = this.TAG;
        Object[] objArr = new Object[1];
        objArr[c] = stringExtra4;
        KLog.i(str, objArr);
        if ("1".equals(this.flag) || this.newsId != 0) {
            initWebView();
            return;
        }
        if (this.url.contains("ticket") || this.url.contains("cheque")) {
            ticketType();
            initWebView();
            return;
        }
        ticketType();
        if (this.gps != null || this.mlocationClient != null) {
            this.isTicketLocation = true;
        }
        initLocation();
    }

    private void addOtherObj(JSONObject jsonObject) {
        try {
            JSONObject jsonObject_other = new JSONObject(this.otherObj);
            Iterator<String> keys = jsonObject_other.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object value = jsonObject_other.opt(key);
                jsonObject.put(key, value);
            }
        } catch (JSONException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTextToSpeech() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.isttsPlay) {
                if (!this.BINDINGACTIVITY) {
                    bindTTSService();
                }
                this.iv_speech.setVisibility(0);
            } else {
                TTSPlayerManager tTSPlayerManager = TTSPlayerManager.getInstance(getApplicationContext());
                this.ttsManager = tTSPlayerManager;
                tTSPlayerManager.setOnPrepare(new TTSPlayerManager.OnPrepare() { // from class: com.shineyue.pm.web.WebActivity.2
                    @Override // com.shineyue.pm.utils.TTSPlayerManager.OnPrepare
                    public void prepare() {
                        WebActivity.this.ttsManager.speak(WebActivity.this.text);
                    }

                    @Override // com.shineyue.pm.utils.TTSPlayerManager.OnPrepare
                    public void playerPrepare(int d) {
                        WebActivity.this.duration = d;
                        WebActivity.this.isPrepare = true;
                        WebActivity.this.iv_speech.setVisibility(0);
                        WebActivity.this.ttsManager.getPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.shineyue.pm.web.WebActivity.2.1
                            @Override // android.media.MediaPlayer.OnCompletionListener
                            public void onCompletion(MediaPlayer mp) {
                                WebActivity.this.iv_text_to_speech.setImageResource(R.mipmap.news_text_to_speech_false);
                                WebActivity.this.iv_speech.setImageResource(R.mipmap.news_text_to_speech_false);
                                WebActivity.this.isPlayer = true;
                            }
                        });
                    }
                });
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0088  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void ticketType() {
        /*
            Method dump skipped, instruction units count: 770
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shineyue.pm.web.WebActivity.ticketType():void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initWebView() {
        try {
            if (this.webView == null) {
                DWebView dWebView = new DWebView(this.mContext);
                this.webView = dWebView;
                this.fl_container.addView(dWebView, 0);
            }
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.ll_chat.getLayoutParams();
            layoutParams.topMargin = TbsListener.ErrorCode.RENAME_SUCCESS;
            this.ll_chat.setLayoutParams(layoutParams);
            try {
                final WebSettings webSettings = this.webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
                if (Utils.isNetworkAvailable(this.mContext)) {
                    webSettings.setCacheMode(-1);
                } else {
                    webSettings.setCacheMode(1);
                }
                webSettings.setDomStorageEnabled(true);
                webSettings.setDatabaseEnabled(true);
                webSettings.setSupportZoom(true);
                webSettings.setBuiltInZoomControls(true);
                webSettings.setDisplayZoomControls(false);
                webSettings.setAppCacheEnabled(true);
                webSettings.setBlockNetworkImage(false);
                String userAgent = webSettings.getUserAgentString();
                webSettings.setUserAgentString(userAgent + g.b + Constants.DEVICEINFO.USER_AGENT);
                if (Build.VERSION.SDK_INT >= 21) {
                    webSettings.setMixedContentMode(0);
                }
                this.webView.addJavascriptInterface(this.mContext, "SYAppModel");
                this.webView.addJavascriptObject(new WebActivityApi(this.mContext), null);
                this.webView.addJavascriptObject(this, null);
                if (Build.VERSION.SDK_INT >= 21) {
                    this.webView.getSettings().setMixedContentMode(0);
                }
                if (Build.VERSION.SDK_INT >= 19) {
                    android.webkit.WebView.setWebContentsDebuggingEnabled(true);
                }
                this.webView.setDownloadListener(new DownloadListener() { // from class: com.shineyue.pm.web.WebActivity.3
                    @Override // android.webkit.DownloadListener
                    public void onDownloadStart(String url, String userAgent2, String contentDisposition, String mimetype, long contentLength) {
                        if (url.startsWith("blob:")) {
                            Utils.showShort(WebActivity.this.mContext, "暂不支持该类型下载");
                            return;
                        }
                        KLog.i(WebActivity.this.TAG, "onDownloadStart==>" + url);
                        try {
                            Intent intent = new Intent("android.intent.action.VIEW");
                            intent.setData(Uri.parse(url));
                            WebActivity.this.startActivity(intent);
                        } catch (Exception e) {
                            KLog.i(WebActivity.this.TAG, e.toString());
                        }
                    }
                });
                this.webView.setWebViewClient(new WebViewClient() { // from class: com.shineyue.pm.web.WebActivity.4
                    @Override // android.webkit.WebViewClient
                    public void onReceivedSslError(android.webkit.WebView view, SslErrorHandler handler, SslError error) {
                        handler.proceed();
                        super.onReceivedSslError(view, handler, error);
                        KLog.d(WebActivity.this.TAG, "onReceivedSslError");
                        KLog.d(WebActivity.this.TAG, "错误描述" + error.getPrimaryError());
                    }

                    @Override // android.webkit.WebViewClient
                    public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        KLog.d(WebActivity.this.TAG, "onPageStarted==>url:" + url);
                        WebActivity.this.isLoading = true;
                        WebActivity.this.startTime = System.currentTimeMillis();
                        WebActivity.this.time = System.currentTimeMillis();
                        WebActivity.this.addTotal("页面开始加载", 0L);
                    }

                    @Override // android.webkit.WebViewClient
                    public void onPageFinished(final android.webkit.WebView view, String url) {
                        super.onPageFinished(view, url);
                        if (!TextUtils.isEmpty(WebActivity.this.title) && WebActivity.this.title.contains("404 Not Found")) {
                            WebActivity.this.noNetShow(true);
                            Utils.errorUpload(WebActivity.this.mContext, "webview", "页面title有404:  " + WebActivity.this.title);
                        }
                        webSettings.setBlockNetworkImage(false);
                        KLog.e(WebActivity.this.TAG, "页面加载总耗时：" + (System.currentTimeMillis() - WebActivity.this.time) + url);
                        if (WebActivity.this.isLoading) {
                            WebActivity.this.addTotal("页面加载完成", System.currentTimeMillis() - WebActivity.this.time);
                        }
                        KLog.d(WebActivity.this.TAG, "onPageFinished==>url:" + url);
                        WebActivity.this.isLoading = false;
                        try {
                            if (!Utils.haveValue(WebActivity.this.params2)) {
                                WebActivity.this.params2 = StrUtil.EMPTY_JSON;
                            }
                            JSONObject jsonObject = new JSONObject(WebActivity.this.params2);
                            jsonObject.put(ak.A, WebActivity.this.titleParms);
                            WebActivity.this.params2 = jsonObject.toString();
                        } catch (Exception e) {
                            KLog.e(e.getMessage());
                        }
                        if (WebActivity.this.newsId != 0) {
                            Notify.Message message = new Notify.Message(4001);
                            message.position = WebActivity.this.position;
                            message.newsId = WebActivity.this.newsId;
                            WebActivity webActivity = WebActivity.this;
                            webActivity.setRead(webActivity.newsId);
                            EventBus.getDefault().post(message);
                        }
                        KLog.i(WebActivity.this.TAG, "addDataSouce");
                        if (!Utils.haveValue(WebActivity.this.params2.trim())) {
                            WebActivity.this.params2 = StrUtil.EMPTY_JSON;
                        }
                        KLog.i(WebActivity.this.TAG, WebActivity.this.params);
                        KLog.i(WebActivity.this.TAG, WebActivity.this.params2);
                        if (Utils.haveValue(WebActivity.this.isOnBack)) {
                            view.loadUrl("javascript:lcbl('" + WebActivity.this.isOnBack + "')");
                        }
                        WebActivity.this.execJsMethod("addDataSouce", new OnParmsCallBack<Boolean>() { // from class: com.shineyue.pm.web.WebActivity.4.1
                            @Override // com.shineyue.pm.modle_chat.chat_history.dao.OnParmsCallBack
                            public void onParmsResult(Boolean result) {
                                if (result.booleanValue()) {
                                    view.loadUrl("javascript:addDataSouce('" + WebActivity.this.params + "','" + WebActivity.this.params2 + "')");
                                }
                            }
                        });
                        KLog.e(WebActivity.this.TAG, "页面加载完成" + WebActivity.this.CAN_CLEAR_BASIC);
                        if (WebActivity.this.CAN_CLEAR_BASIC) {
                            WebActivity.this.CAN_CLEAR_BASIC = false;
                            WebActivity.this.webView.clearHistory();
                            KLog.e(WebActivity.this.TAG, "清空了webview历史记录");
                        }
                        WebActivity.this.customLoadingView.setVisibility(8);
                    }

                    @Override // android.webkit.WebViewClient
                    public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                        super.shouldOverrideUrlLoading(view, url);
                        KLog.i(WebActivity.this.TAG, "shouldOverrideUrlLoading==>url:" + url);
                        if (!url.startsWith(JPushConstants.HTTP_PRE) && !url.startsWith(JPushConstants.HTTPS_PRE)) {
                            try {
                                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                                intent.addCategory("android.intent.category.BROWSABLE");
                                intent.setFlags(268435456);
                                WebActivity.this.startActivity(intent);
                            } catch (Exception e) {
                            }
                            return true;
                        }
                        WebView.HitTestResult hitTestResult = view.getHitTestResult();
                        if (TextUtils.isEmpty(hitTestResult.getExtra()) || hitTestResult.getType() == 0) {
                            KLog.i(WebActivity.this.TAG, "重定向: " + hitTestResult.getType() + " && EXTRA（）" + hitTestResult.getExtra() + "------");
                            KLog.i(WebActivity.this.TAG, "GetURL: " + view.getUrl() + "\ngetOriginalUrl()" + view.getOriginalUrl());
                            String str = WebActivity.this.TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("URL: ");
                            sb.append(url);
                            KLog.i(str, sb.toString());
                            return false;
                        }
                        if (url.startsWith(JPushConstants.HTTP_PRE) || url.startsWith(JPushConstants.HTTPS_PRE)) {
                            view.loadUrl(url);
                            return false;
                        }
                        if (url.contains("tel") && !url.contains(OkGoNetManager.SHINEYUEEWS)) {
                            if (ActivityCompat.checkSelfPermission(WebActivity.this, "android.permission.CALL_PHONE") != 0) {
                                new com.shineyue.pm.utils.PermissionUtils(WebActivity.this.mContext, "android.permission.CALL_PHONE");
                            } else {
                                String mobile = url.substring(url.lastIndexOf(":") + 1);
                                Uri uri = Uri.parse(com.tencent.smtt.sdk.WebView.SCHEME_TEL + mobile);
                                Intent intent2 = new Intent("android.intent.action.CALL", uri);
                                WebActivity.this.startActivity(intent2);
                            }
                            return true;
                        }
                        try {
                            Intent intent3 = new Intent("android.intent.action.VIEW", Uri.parse(url));
                            WebActivity.this.mContext.startActivity(intent3);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        return true;
                    }

                    @Override // android.webkit.WebViewClient
                    public WebResourceResponse shouldInterceptRequest(android.webkit.WebView view, String url) {
                        WebResourceResponse webResourceResponse = WebActivity.this.shouldUrlInterupt(url);
                        if (webResourceResponse != null) {
                            return webResourceResponse;
                        }
                        return super.shouldInterceptRequest(view, url);
                    }

                    @Override // android.webkit.WebViewClient
                    public WebResourceResponse shouldInterceptRequest(android.webkit.WebView view, WebResourceRequest request) {
                        WebActivity webActivity = WebActivity.this;
                        WebResourceResponse webResourceResponse = webActivity.shouldUrlInterupt(webActivity.url);
                        if (webResourceResponse != null) {
                            return webResourceResponse;
                        }
                        return super.shouldInterceptRequest(view, request);
                    }

                    @Override // android.webkit.WebViewClient
                    public void onReceivedError(android.webkit.WebView view, int errorCode, String description, String failingUrl) {
                        super.onReceivedError(view, errorCode, description, failingUrl);
                        KLog.i(WebActivity.this.TAG, Integer.valueOf(errorCode));
                        KLog.i(WebActivity.this.TAG, description);
                        KLog.i(WebActivity.this.TAG, failingUrl);
                        Utils.errorUpload(WebActivity.this.mContext, "webview", "errorCode:  " + errorCode + "; failingUrl:  " + failingUrl + "; msg;  " + description);
                        WebActivity.this.noNetShow(true);
                        if (WebActivity.this.CAN_CLEAR_BASIC) {
                            WebActivity.this.CAN_CLEAR_BASIC = false;
                            WebActivity.this.webView.clearHistory();
                            KLog.e(WebActivity.this.TAG, "清空了webview历史记录");
                        }
                    }

                    @Override // android.webkit.WebViewClient
                    public void onReceivedHttpError(android.webkit.WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                        super.onReceivedHttpError(view, request, errorResponse);
                        KLog.i(WebActivity.this.TAG, Integer.valueOf(errorResponse.getStatusCode()));
                        KLog.i(WebActivity.this.TAG, request.getUrl());
                    }

                    @Override // android.webkit.WebViewClient
                    public void onReceivedError(android.webkit.WebView view, WebResourceRequest request, WebResourceError error) {
                        super.onReceivedError(view, request, error);
                        KLog.i(WebActivity.this.TAG, Integer.valueOf(error.getErrorCode()));
                        KLog.i(WebActivity.this.TAG, request.getUrl());
                        Utils.errorUpload(WebActivity.this.mContext, "webview", "2errorCode:  " + error.getErrorCode() + "; failingUrl:  " + request.getUrl() + "; msg;  " + ((Object) error.getDescription()));
                        try {
                            if (WebActivity.this.url.equals(request.getUrl())) {
                                WebActivity.this.noNetShow(true);
                            }
                        } catch (Exception e) {
                            e.toString();
                        }
                        if (WebActivity.this.CAN_CLEAR_BASIC) {
                            WebActivity.this.CAN_CLEAR_BASIC = false;
                            WebActivity.this.webView.clearHistory();
                            KLog.e(WebActivity.this.TAG, "清空了webview历史记录");
                        }
                    }

                    @Override // android.webkit.WebViewClient
                    public void onReceivedClientCertRequest(android.webkit.WebView view, ClientCertRequest request) {
                        super.onReceivedClientCertRequest(view, request);
                        KLog.e("请求到了带证书链接");
                        if (CertUtils.getIns().getmCertificates() == null || CertUtils.getIns().getmPrivateKey() == null) {
                            CertUtils.getIns().loadCertificateAndPrivateKey();
                        }
                        if (Build.VERSION.SDK_INT >= 21) {
                            request.proceed(CertUtils.getIns().getmPrivateKey(), CertUtils.getIns().getmCertificates());
                        }
                    }

                    @Override // android.webkit.WebViewClient
                    public void onLoadResource(android.webkit.WebView view, String url) {
                        super.onLoadResource(view, url);
                        if (WebActivity.this.isLoading) {
                            WebActivity.this.addItem(url, System.currentTimeMillis() - WebActivity.this.startTime);
                        }
                        WebActivity.this.startTime = System.currentTimeMillis();
                    }
                });
                this.webView.setWebChromeClient(new WebChromeClient() { // from class: com.shineyue.pm.web.WebActivity.5
                    @Override // android.webkit.WebChromeClient
                    public void onProgressChanged(android.webkit.WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);
                        KLog.i("当前进度:" + newProgress);
                        if (newProgress != 100) {
                            if (WebActivity.this.progressBar != null) {
                                WebActivity.this.progressBar.setVisibility(0);
                                WebActivity.this.progressBar.setProgress(newProgress);
                                return;
                            }
                            return;
                        }
                        String youmeng_info = "加载进度100% 时间:" + DateUtils.dateToString(new Date()) + "---" + System.currentTimeMillis() + ";  userid:" + Utils.getMsg(OaApplication.application, "userid") + ";  userName:" + Utils.getMsg(OaApplication.application, c.e) + ";  url:" + WebActivity.this.url;
                        if (Utils.isStringOver128Bytes(youmeng_info) && youmeng_info.length() > 128) {
                            youmeng_info = youmeng_info.substring(0, 128);
                        }
                        MobclickAgent.onEvent(OaApplication.application, "webLoadFinish", youmeng_info);
                        if (WebActivity.this.webView.canGoBack()) {
                            WebActivity.this.abc_title_txt_close.setVisibility(8);
                        } else {
                            WebActivity.this.abc_title_txt_close.setVisibility(8);
                        }
                        if (WebActivity.this.progressBar != null) {
                            WebActivity.this.progressBar.setVisibility(8);
                        }
                    }

                    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
                    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
                    /* JADX WARN: Removed duplicated region for block: B:7:0x003e  */
                    @Override // android.webkit.WebChromeClient
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                        To view partially-correct code enable 'Show inconsistent code' option in preferences
                    */
                    public boolean onJsPrompt(android.webkit.WebView r17, java.lang.String r18, java.lang.String r19, java.lang.String r20, android.webkit.JsPromptResult r21) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
                        /*
                            Method dump skipped, instruction units count: 528
                            To view this dump change 'Code comments level' option to 'DEBUG'
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.shineyue.pm.web.WebActivity.AnonymousClass5.onJsPrompt(android.webkit.WebView, java.lang.String, java.lang.String, java.lang.String, android.webkit.JsPromptResult):boolean");
                    }

                    @Override // android.webkit.WebChromeClient
                    public void onReceivedTitle(android.webkit.WebView view, String titleReceive) {
                        super.onReceivedTitle(view, titleReceive);
                        KLog.i(WebActivity.this.TAG, titleReceive);
                        if (!TextUtils.isEmpty(titleReceive) && PinYin4j.isContainChinese(titleReceive) && !titleReceive.contains("考勤历史") && !"BILLBOARD".equals(WebActivity.this.typeFlag)) {
                            WebActivity.this.tv_title.setText(titleReceive);
                        }
                        if (!TextUtils.isEmpty(titleReceive) && titleReceive.contains("404 Not Found")) {
                            WebActivity.this.title = titleReceive;
                            WebActivity.this.noNetShow(true);
                            Utils.errorUpload(WebActivity.this.mContext, "webview", "页面title有404 2:  " + WebActivity.this.title);
                        }
                    }

                    @Override // android.webkit.WebChromeClient
                    public boolean onShowFileChooser(android.webkit.WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                        super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
                        WebActivity.this.mUploadCallbackAboveL = filePathCallback;
                        if (Build.VERSION.SDK_INT >= 21) {
                            KLog.i(WebActivity.this.TAG, fileChooserParams.getTitle());
                            KLog.i(WebActivity.this.TAG, Integer.valueOf(fileChooserParams.getMode()));
                            KLog.i(WebActivity.this.TAG, fileChooserParams.getFilenameHint());
                            KLog.i(WebActivity.this.TAG, Boolean.valueOf(fileChooserParams.isCaptureEnabled()));
                            KLog.e(WebActivity.this.TAG, "onShowFileChooser");
                            if (Utils.judgeClient().intValue() != 2 || !fileChooserParams.isCaptureEnabled()) {
                                WebActivity.this.take();
                            } else {
                                WebActivity.this.timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                                WebActivity webActivity = WebActivity.this;
                                webActivity.imageUri = MyPhotoUtils.getOutputMediaFileUri(webActivity.getApplicationContext(), WebActivity.this.fileCache, WebActivity.this.timeStamp, WebActivity.this.mediaFile);
                                Intent openCameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                                openCameraIntent.putExtra("output", WebActivity.this.imageUri);
                                WebActivity.this.startActivityForResult(openCameraIntent, 1);
                            }
                        } else {
                            WebActivity.this.take();
                        }
                        return true;
                    }

                    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                        WebActivity.this.mUploadMessage = uploadMsg;
                        KLog.e(WebActivity.this.TAG, "onShowFileChooser==1==");
                        WebActivity.this.take();
                    }

                    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                        WebActivity.this.mUploadMessage = uploadMsg;
                        KLog.e(WebActivity.this.TAG, "onShowFileChooser==2==" + acceptType);
                        WebActivity.this.take();
                    }

                    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                        WebActivity.this.mUploadMessage = uploadMsg;
                        KLog.e(WebActivity.this.TAG, "onShowFileChooser==3==" + acceptType + " " + capture);
                        WebActivity.this.take();
                    }

                    @Override // android.webkit.WebChromeClient
                    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                        callback.invoke(origin, true, false);
                        super.onGeolocationPermissionsShowPrompt(origin, callback);
                        KLog.d(WebActivity.this.TAG, "错误描述");
                    }

                    @Override // android.webkit.WebChromeClient
                    public View getVideoLoadingProgressView() {
                        FrameLayout frameLayout = new FrameLayout(WebActivity.this);
                        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
                        return frameLayout;
                    }

                    @Override // android.webkit.WebChromeClient
                    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
                        WebActivity.this.showCustomView(view, callback);
                        WebActivity.this.setRequestedOrientation(0);
                    }

                    @Override // android.webkit.WebChromeClient
                    public void onHideCustomView() {
                        WebActivity.this.hideCustomView();
                        WebActivity.this.setRequestedOrientation(1);
                    }
                });
                loadUrl();
            } catch (Exception e) {
                e.toString();
            }
        } catch (Exception e2) {
            KLog.i(this.TAG, e2.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        if (this.customView != null) {
            callback.onCustomViewHidden();
            return;
        }
        getWindow().getDecorView();
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        FullscreenHolder fullscreenHolder = new FullscreenHolder(this);
        this.fullscreenContainer = fullscreenHolder;
        fullscreenHolder.addView(view, this.COVER_SCREEN_PARAMS);
        decor.addView(this.fullscreenContainer, this.COVER_SCREEN_PARAMS);
        this.customView = view;
        setStatusBarVisibility(false);
        this.customViewCallback = callback;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideCustomView() {
        if (this.customView == null) {
            return;
        }
        setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        decor.removeView(this.fullscreenContainer);
        this.fullscreenContainer = null;
        this.customView = null;
        this.customViewCallback.onCustomViewHidden();
        this.webView.setVisibility(0);
    }

    public class FullscreenHolder extends FrameLayout {
        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : 1024;
        getWindow().setFlags(flag, 1024);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void setRead(long id) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            jsonObject.put("userid", Utils.getMsg(OaApplication.application, "userid"));
            jsonObject.put("grbh", Utils.getMsg(OaApplication.application, "khbh"));
            jsonObject.put("devicetype", "Android");
            jsonObject.put("operationtype", "资讯列表已读");
            jsonObject.put("isblockchain", Utils.getBooleanMsg(OaApplication.application, "isblockchain"));
            KLog.i(this.TAG, jsonObject.toString());
            OkGoNetManager.getInstance();
            String url = OkGoNetManager.APP_URL + OkGoNetManager.NEWSUPDATEREAD;
            ((PostRequest) OkGo.post(url).tag(this)).upJson(jsonObject.toString()).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.6
                @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
                public void onSuccess(String s, Call call, Response response) {
                    KLog.i(WebActivity.this.TAG, s);
                }

                @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    KLog.i(WebActivity.this.TAG, e.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUrl() {
        if (!"1".equals(this.flag)) {
            HashMap<String, String> map = new HashMap<>();
            String key = OkGoNetManager.WEB_FILTER;
            String urlPath = this.url;
            try {
                urlPath = new URL(this.url).getPath();
            } catch (Exception e) {
                e.printStackTrace();
            }
            map.put("sign", MD5Util.string2MD5(MD5Util.string2MD5("SHINEYUE" + urlPath + key)));
            if (!this.url.contains("qycode")) {
                if (this.url.contains("?")) {
                    this.url += "&qycode=" + Utils.getQyCode();
                } else {
                    this.url += "?qycode=" + Utils.getQyCode();
                }
            }
            if (!this.url.contains("&zzjgdmz=")) {
                this.url += "&zzjgdmz=" + Utils.getQyCode();
            }
            if (!this.url.contains("&cpbs=")) {
                this.url += "&cpbs=" + this.cpbs;
            }
            if (!this.url.contains("zzjgdmz=")) {
                this.url += "&zzjgdmz=" + Utils.getQyCode();
            }
            if (!this.url.contains("cpbs=") && Utils.haveValue(this.cpbs)) {
                this.url += "&cpbs=" + this.cpbs;
            }
            String zxbm = Utils.getMsg(this.mContext, "zxjgbm");
            String jgbm = Utils.getMsg(this.mContext, "jgbm");
            String khbh = Utils.getMsg(this.mContext, "khbh");
            String userid = Utils.getMsg(this.mContext, "userid");
            String zjhm = Utils.getMsg(this.mContext, "zjhm");
            Utils.getMsg(this.mContext, "client");
            String accessToken = Utils.getMsg(this.mContext, "accessToken");
            if (!this.url.contains("zxbm=") && Utils.haveValue(zxbm)) {
                this.url += "&zxbm=" + zxbm;
            }
            if (!this.url.contains("jgbm=") && Utils.haveValue(jgbm)) {
                this.url += "&jgbm=" + jgbm;
            }
            if (!this.url.contains("khbh=") && Utils.haveValue(khbh)) {
                this.url += "&khbh=" + khbh;
            }
            if (!this.url.contains("userid=") && Utils.haveValue(userid)) {
                this.url += "&userid=" + userid;
            }
            if (!this.url.contains("zjhm=") && Utils.haveValue(zjhm)) {
                this.url += "&zjhm=" + zjhm;
            }
            if (!this.url.contains("tyLoginToken=") && Utils.haveValue(accessToken)) {
                this.url += "&tyLoginToken=" + accessToken;
            }
            KLog.i(this.TAG, this.url);
            KLog.i(this.TAG, Utils.getMsg(this.mContext, "login_environment"));
            KLog.i(this.TAG, Utils.getMsg(this.mContext, "web_url"));
            String str = this.url;
            if (str == null || !str.startsWith("http")) {
                this.url = OkGoNetManager.APP_URL + this.url;
            }
            this.webView.loadUrl(this.url, map);
        } else {
            String str2 = this.url;
            if (str2 == null || !str2.startsWith("http")) {
                this.url = OkGoNetManager.APP_URL + this.url;
            }
            this.webView.loadUrl(this.url);
        }
        this.startTime = System.currentTimeMillis();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            if (this.isOpenSideslip) {
                this.webView.loadUrl("javascript:onSideslipListener()");
                return true;
            }
            DWebView dWebView = this.webView;
            if (dWebView != null && dWebView.canGoBack()) {
                this.webView.goBack();
                return true;
            }
            if (this.preRefresh == 1) {
                Intent resultIntent = new Intent(this.mContext, (Class<?>) WebActivity.class);
                resultIntent.putExtra("PREREFRESH", this.preRefresh);
                setResult(1656, resultIntent);
                finish();
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0079  */
    @Override // com.shineyue.pm.BasicActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void onResume() {
        /*
            r7 = this;
            super.onResume()
            r7.addActivity(r7)
            android.content.Context r0 = r7.mContext
            java.lang.String r1 = "isGroundReturn"
            java.lang.String r0 = com.shineyue.pm.utils.Utils.getMsg(r0, r1)
            java.lang.String r1 = r7.TAG
            r2 = 1
            java.lang.Object[] r3 = new java.lang.Object[r2]
            r4 = 0
            r3[r4] = r0
            com.socks.library.KLog.i(r1, r3)
            java.lang.String r1 = "viewWillAppear"
            r3 = 0
            r7.execJsMethod(r1, r3)     // Catch: java.lang.Exception -> L20
            goto L24
        L20:
            r1 = move-exception
            r1.toString()
        L24:
            java.lang.String r1 = r7.TAG
            java.lang.Object[] r2 = new java.lang.Object[r2]
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "close_on==>"
            r3.append(r5)
            android.content.Context r5 = r7.mContext
            java.lang.String r6 = "close_on"
            java.lang.String r5 = com.shineyue.pm.utils.Utils.getMsg(r5, r6)
            r3.append(r5)
            java.lang.String r3 = r3.toString()
            r2[r4] = r3
            com.socks.library.KLog.d(r1, r2)
            android.content.Context r1 = r7.mContext
            java.lang.String r1 = com.shineyue.pm.utils.Utils.getMsg(r1, r6)
            java.lang.String r2 = "true"
            boolean r1 = r1.equals(r2)
            r2 = 8
            if (r1 == 0) goto L5c
            android.widget.ImageView r1 = r7.iv_title_close
            r1.setVisibility(r2)
            goto L61
        L5c:
            android.widget.ImageView r1 = r7.iv_title_close
            r1.setVisibility(r2)
        L61:
            boolean r1 = r7.chatPrepare
            if (r1 == 0) goto La0
            com.shineyue.pm.modle_chat_new.ChatServiceNew r1 = com.shineyue.pm.OaApplication.chatService
            if (r1 == 0) goto L79
            com.shineyue.pm.modle_chat_new.ChatServiceNew r1 = com.shineyue.pm.OaApplication.chatService
            com.neovisionaries.ws.client.WebSocket r1 = com.shineyue.pm.modle_chat_new.ChatServiceNew.ws
            if (r1 == 0) goto L79
            com.shineyue.pm.modle_chat_new.ChatServiceNew r1 = com.shineyue.pm.OaApplication.chatService
            com.neovisionaries.ws.client.WebSocket r1 = com.shineyue.pm.modle_chat_new.ChatServiceNew.ws
            boolean r1 = r1.isOpen()
            if (r1 != 0) goto L7e
        L79:
            android.content.Context r1 = r7.mContext
            com.shineyue.pm.modle_chat_new.ChatServiceNew.startChatService(r1)
        L7e:
            com.mabeijianxi.jianxiexpression.widget.ExpressionEditText r1 = r7.editEmojicon
            r1.clearFocus()
            r7.hideKeyboard(r7)
            r7.hideEmogiPanel()
            r7.hideAddPanel()
            r7.sendReceive()
            android.widget.ImageView r1 = r7.iv_icon
            r2 = 2131624037(0x7f0e0065, float:1.8875242E38)
            r1.setImageResource(r2)
            android.widget.ImageView r1 = r7.btn_add
            if (r1 == 0) goto La0
            r1.clearAnimation()
            r7.isAddShow = r4
        La0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shineyue.pm.web.WebActivity.onResume():void");
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.abc_title_back /* 2131296289 */:
                if (this.isOpenPop) {
                    this.webView.loadUrl("javascript:onOpenPopListener()");
                } else {
                    Utils.saveMsg(this.mContext, "close_on", StreamerConstants.TRUE);
                    DWebView dWebView = this.webView;
                    if (dWebView != null && dWebView.canGoBack()) {
                        this.webView.goBack();
                    } else {
                        finish();
                    }
                }
                break;
            case R.id.abc_title_close /* 2131296292 */:
            case R.id.abc_title_txt_close /* 2131296295 */:
                Utils.saveMsg(this.mContext, "close_on", StreamerConstants.FALSE);
                removeAllActivity();
                break;
            case R.id.btn /* 2131296441 */:
                if (this.ll_chat.getVisibility() == 0) {
                    this.ll_chat.setVisibility(8);
                } else {
                    this.ll_chat.setVisibility(0);
                    if (!this.chatPrepare) {
                        this.chatType = "4";
                        this.toUser = "79422841";
                        this.toUserName = "时代大厦";
                        initChat();
                        this.chatPrepare = true;
                    }
                }
                break;
            case R.id.btn_retry /* 2131296544 */:
                if (NetUtils.checkWifi(this.mContext).intValue() == 3) {
                    ToastUtil.toast(this.mContext, "msg", "网络错误，请检查网络设置", ToastType.WARNING);
                } else {
                    if ("1".equals(this.flag)) {
                        initWebView();
                    } else if (this.url.contains("ticket") || this.url.contains("cheque")) {
                        ticketType();
                        initWebView();
                    } else {
                        ticketType();
                        if (this.gps != null || this.mlocationClient != null) {
                            this.isTicketLocation = true;
                        }
                        initLocation();
                    }
                    this.no_network.setVisibility(8);
                }
                break;
            case R.id.eetself_chat_buttom_input /* 2131296778 */:
                hideEmogiPanel();
                hideAddPanel();
                this.editEmojicon.requestFocus();
                break;
            case R.id.eetself_chat_buttom_input_full /* 2131296779 */:
                hideEmogiPanel();
                this.getmExpressionEditTextFull.requestFocus();
                break;
            case R.id.iv_chat_buttom_add /* 2131297135 */:
                this.editEmojicon.setVisibility(0);
                this.mRvEditEmojiconContent.setVisibility(0);
                this.id_recorder_button.setVisibility(8);
                this.iv_voice.setImageResource(R.mipmap.chat_voice);
                showAdd();
                break;
            case R.id.iv_chat_buttom_emoj /* 2131297136 */:
                this.isVideo = false;
                this.editEmojicon.setVisibility(0);
                this.mRvEditEmojiconContent.setVisibility(0);
                this.id_recorder_button.setVisibility(8);
                this.iv_voice.setImageResource(R.mipmap.chat_voice);
                this.editEmojicon.setVisibility(0);
                this.mRvEditEmojiconContent.setVisibility(0);
                this.id_recorder_button.setVisibility(8);
                showEmoj();
                break;
            case R.id.iv_chat_buttom_emoj_full /* 2131297137 */:
                this.editEmojicon.setVisibility(0);
                this.mRvEditEmojiconContent.setVisibility(0);
                showEmojFull();
                replaceFullImg();
                break;
            case R.id.iv_chat_buttom_rengong /* 2131297138 */:
                sendRenGongMsg();
                break;
            case R.id.iv_chat_buttom_voice /* 2131297139 */:
                if (this.isVideo) {
                    this.isVideo = false;
                    this.editEmojicon.setVisibility(0);
                    this.mRvEditEmojiconContent.setVisibility(0);
                    this.id_recorder_button.setVisibility(8);
                    this.iv_voice.setImageResource(R.mipmap.chat_voice);
                } else {
                    checkPermissions_audio();
                }
                break;
            case R.id.iv_chat_full_screen /* 2131297142 */:
            case R.id.rl_chat_full_screen /* 2131298391 */:
                this.ll.setBackgroundColor(Color.parseColor("#cc000000"));
                this.mViewMask.setVisibility(0);
                if (isKeyboardShown(this.ll)) {
                    hideKeyboard((Activity) this.mContext);
                }
                if (this.chatFullOpenAnimation == null) {
                    TranslateAnimation translateAnimation = (TranslateAnimation) AnimationUtils.loadAnimation(this.mContext, R.anim.chat_full_screen_open);
                    this.chatFullOpenAnimation = translateAnimation;
                    translateAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.shineyue.pm.web.WebActivity.7
                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationStart(Animation animation) {
                            if (WebActivity.this.isEmogiShow) {
                                WebActivity.this.showEmoj();
                            }
                            if (WebActivity.this.isAddShow) {
                                WebActivity.this.showAdd();
                            }
                            WebActivity.this.editEmojicon.clearFocus();
                            WebActivity.this.getmExpressionEditTextFull.requestFocus();
                            WebActivity.this.getmExpressionEditTextFull.setText(WebActivity.this.editEmojicon.getText());
                            WebActivity.this.getmExpressionEditTextFull.setSelection(WebActivity.this.editEmojicon.getSelectionStart());
                        }

                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationEnd(Animation animation) {
                            WebActivity.this.rl_full_screen_content.setVisibility(0);
                            WebActivity.this.isshowFull = true;
                            WebActivity webActivity = WebActivity.this;
                            webActivity.showKeyboardFull((Activity) webActivity.mContext);
                        }

                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                }
                this.rl_full_screen_content.setVisibility(0);
                this.rl_full_screen_content.startAnimation(this.chatFullOpenAnimation);
                break;
            case R.id.iv_chat_person /* 2131297150 */:
                if (Utils.judgeClient().intValue() == 2) {
                    Intent intent2 = new Intent(this.mContext, (Class<?>) BusinessCardActivity.class);
                    intent2.putExtra("id", this.toUser);
                    intent2.putExtra(c.e, this.toUserName);
                    this.mContext.startActivity(intent2);
                } else {
                    Object grbm = Utils.getMsg(this.mContext, "khbh" + this.toUser);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("userid", this.toUser + "");
                    map.put("khbh", grbm);
                    String params2 = new JSONObject(map).toString();
                    String qycode = Utils.getQyCode();
                    Intent intent22 = new Intent(this.mContext, (Class<?>) WebActivity.class);
                    intent22.putExtra("web_url", OkGoNetManager.WEB_URL + OkGoNetManager.DANGANNEW + "?organizationcode=" + qycode);
                    intent22.putExtra("web_title", "个人信息");
                    intent22.putExtra("params2", params2);
                    intent22.putExtra("type", "0008");
                    this.mContext.startActivity(intent22);
                }
                break;
            case R.id.iv_chat_qun_person /* 2131297151 */:
            case R.id.tv_chat_title_detail /* 2131299095 */:
                KLog.e(this.TAG, "id==" + this.toUser);
                Intent intent = new Intent(this, (Class<?>) QunZuDetailActivity.class);
                intent.putExtra("id", this.toUser);
                intent.putExtra(c.e, this.toUserName);
                intent.putExtra("hidden", true);
                startActivityForResult(intent, AppUpgradeService.mNotificationId);
                break;
            case R.id.iv_chat_title_back /* 2131297158 */:
                hideKeyboard(this);
                break;
            case R.id.iv_close_full_screen /* 2131297177 */:
                this.getmExpressionEditTextFull.clearFocus();
                this.editEmojicon.requestFocus();
                this.editEmojicon.setText(this.getmExpressionEditTextFull.getText());
                this.editEmojicon.setSelection(this.getmExpressionEditTextFull.getSelectionStart());
                closeFullScreen();
                break;
            case R.id.iv_del_quote /* 2131297195 */:
                delQuote();
                break;
            case R.id.iv_open_or_close /* 2131297329 */:
                boolean z = this.keyBoardShow;
                if (!z && !this.isEmogiShowFull) {
                    showKeyboardFull((Activity) this.mContext);
                } else if (z && !this.isEmogiShowFull) {
                    hideKeyboardFull((Activity) this.mContext);
                } else if (!z && this.isEmogiShowFull) {
                    hideEmogiPanelFull();
                }
                replaceFullImg();
                break;
            case R.id.iv_open_or_close_huati /* 2131297330 */:
                if (!this.keyBoardShow) {
                    showKeyboardHuaTi((Activity) this.mContext);
                    this.mIvOpenOrCloseHuaTi.setImageResource(R.mipmap.full_screen_down);
                } else {
                    hideKeyboardHuaTi((Activity) this.mContext);
                    this.mIvOpenOrCloseHuaTi.setImageResource(R.mipmap.full_screen_up);
                }
                break;
            case R.id.iv_qun_call /* 2131297375 */:
                if (!Utils.getBooleanMsg(this.mContext, "yspFlag")) {
                    checkPermissions_phone();
                } else if (this.chatType.equals("3") || this.chatType.equals("6")) {
                    showYinShiPinPopWindow();
                } else {
                    checkVideoPermissionChat();
                }
                break;
            case R.id.iv_qun_more /* 2131297376 */:
                if (UtilsChatTopic.isHuati(this.toUser)) {
                    Intent intent3 = new Intent(this.mContext, (Class<?>) HuaTiDetailActivity.class);
                    intent3.putExtra("ID", this.toUser);
                    this.mContext.startActivity(intent3);
                } else if (Utils.judgeClient().intValue() == 1) {
                    if (this.chatType.equals("3") || this.chatType.equals("6")) {
                        Intent intent4 = new Intent(this.mContext, (Class<?>) SingleChatDetailActivity.class);
                        intent4.putExtra("ID", this.toUser);
                        intent4.putExtra("fName", Utils.getMsg(this.mContext, "username"));
                        intent4.putExtra("tName", this.toUserName);
                        this.mContext.startActivity(intent4);
                    } else {
                        Intent intent5 = new Intent(this.mContext, (Class<?>) QunZuDetailOldActivity.class);
                        intent5.putExtra("id", this.toUser);
                        intent5.putExtra(c.e, this.toUserName);
                        this.mContext.startActivity(intent5);
                    }
                } else if (this.chatType.equals("3") || this.chatType.equals("6")) {
                    Intent intent6 = new Intent(this.mContext, (Class<?>) SingleChatDetailActivity.class);
                    intent6.putExtra("ID", this.toUser);
                    intent6.putExtra("fName", Utils.getMsg(this.mContext, "username"));
                    intent6.putExtra("tName", this.toUserName);
                    this.mContext.startActivity(intent6);
                } else {
                    Intent intent7 = new Intent(this.mContext, (Class<?>) QunZuDetailActivity.class);
                    intent7.putExtra("id", this.toUser);
                    intent7.putExtra(c.e, this.toUserName);
                    intent7.putExtra("hidden", true);
                    startActivityForResult(intent7, AppUpgradeService.mNotificationId);
                }
                break;
            case R.id.iv_robot_keyboard /* 2131297394 */:
                this.ll_norobot_button_root.setVisibility(0);
                this.ll_robot_button_root.setVisibility(8);
                break;
            case R.id.iv_speech /* 2131297438 */:
                if (!Utils.isFastClick(v, 500) && Build.VERSION.SDK_INT >= 23) {
                    if (this.isttsPlay) {
                        if (this.isPlayer) {
                            if (this.BINDINGACTIVITY) {
                                this.localBinder.pause();
                            } else {
                                bindTTSService();
                            }
                        } else if (this.BINDINGACTIVITY) {
                            this.localBinder.resume();
                        } else {
                            bindTTSService();
                        }
                    } else if (this.isPrepare) {
                        KLog.i(this.TAG, Boolean.valueOf(this.isPlayer));
                        if (this.isPlayer) {
                            if (this.isttsPlay) {
                                if (this.BINDINGACTIVITY) {
                                    this.localBinder.pause();
                                } else {
                                    bindTTSService();
                                }
                            } else {
                                this.ttsManager.pause();
                                this.iv_text_to_speech.setImageResource(R.mipmap.news_text_to_speech_false);
                                this.iv_speech.setImageResource(R.mipmap.news_text_to_speech_false);
                                this.isPlayer = false;
                            }
                        } else if (this.isttsPlay) {
                            if (this.BINDINGACTIVITY) {
                                this.localBinder.resume();
                            } else {
                                bindTTSService();
                            }
                        } else {
                            this.ttsManager.resume();
                            this.iv_text_to_speech.setImageResource(R.mipmap.news_text_to_speech_true);
                            this.iv_speech.setImageResource(R.mipmap.news_text_to_speech_true);
                            this.isPlayer = true;
                        }
                    }
                    break;
                }
                break;
            case R.id.ll_forward_item /* 2131297751 */:
                ToastUtil.toast(this.mContext, "msg", "逐条转发", ToastType.WARNING);
                break;
            case R.id.ll_forward_merge /* 2131297752 */:
                List<Message> checkedList = this.chatAdapter.getCheckedList();
                if (checkedList != null && checkedList.size() > 0) {
                    Intent forwardIntent = new Intent(this.mContext, (Class<?>) RepeatSendSelectActivity.class);
                    forwardIntent.putExtra("message", createMergeMessage());
                    this.chatAdapter.replacementStatus(false);
                    Context context = this.mContext;
                    Activity activity = (Activity) context;
                    context.startActivity(forwardIntent);
                    activity.overridePendingTransition(R.anim.delete_select_email_open_in, R.anim.delete_select_email_open_out);
                } else {
                    ToastUtil.toast(this.mContext, ToastInfo.selectRequired, "要转发的消息", ToastType.WARNING);
                }
                break;
            case R.id.ll_new_message_dialog /* 2131297859 */:
                this.isSearchOrDing = false;
                this.dingTime = 0L;
                this.dingId = "";
                this.list.clear();
                this.dingTime = 0L;
                this.mTvNewMessageDialog.setVisibility(8);
                loadDate();
                break;
            case R.id.ll_robot_back_root /* 2131297929 */:
                hideEmogiPanel();
                hideAddPanel();
                this.ll_robot_button_root.setVisibility(0);
                this.ll_norobot_button_root.setVisibility(8);
                break;
            case R.id.rl_notFound /* 2131298500 */:
                loadUrl();
                break;
            case R.id.rl_text_to_speech /* 2131298545 */:
                showTextToSpeechPopupWindow();
                break;
            case R.id.tv_chat_buttom_send /* 2131299065 */:
                if (this.dingTime > 0) {
                    this.dingTime = 0L;
                    loadDate();
                }
                if (this.editEmojicon.getText().toString().trim().length() > 0) {
                    if (this.editEmojicon.getText().toString().trim().length() <= 2000) {
                        sendMsg(this.toUser, this.editEmojicon.getText().toString().trim(), null, null);
                    } else {
                        ToastUtil.toast(this.mContext, ToastInfo.outOfRange, "文本内容", "2000字", ToastType.WARNING);
                    }
                } else if (!OaApplication.JIQIRENID.equals(this.toUser)) {
                    ToastUtil.toast(this.mContext, ToastInfo.inputRequired, "内容", ToastType.WARNING);
                }
                break;
            case R.id.tv_chat_buttom_send_full /* 2131299066 */:
                if (!TextUtils.isEmpty(this.getmExpressionEditTextFull.getText().toString())) {
                    this.editEmojicon.setText("");
                    if (this.dingTime > 0) {
                        this.dingTime = 0L;
                        loadDate();
                    }
                    if (this.getmExpressionEditTextFull.getText().toString().trim().length() > 0) {
                        if (this.getmExpressionEditTextFull.getText().toString().trim().length() <= 2000) {
                            sendMsg(this.toUser, this.getmExpressionEditTextFull.getText().toString().trim(), null, null);
                        } else {
                            ToastUtil.toast(this.mContext, ToastInfo.outOfRange, "文本内容", "2000字", ToastType.WARNING);
                        }
                    } else {
                        ToastUtil.toast(this.mContext, ToastInfo.inputRequired, "内容", ToastType.WARNING);
                    }
                    closeFullScreen();
                    break;
                }
                break;
            case R.id.tv_chat_buttom_send_full_huati /* 2131299067 */:
                createHuaTi(this.mEetHuaTi.getText().toString().trim(), "1", null);
                break;
            case R.id.tv_multiple_choice_cancle /* 2131299456 */:
                this.chatAdapter.replacementStatus(false);
                break;
            case R.id.view_mask /* 2131299940 */:
                if (this.isshowFull) {
                    this.getmExpressionEditTextFull.clearFocus();
                    this.editEmojicon.requestFocus();
                    this.editEmojicon.setText(this.getmExpressionEditTextFull.getText());
                    this.editEmojicon.setSelection(this.getmExpressionEditTextFull.getSelectionStart());
                    closeFullScreen();
                } else {
                    closeHuaTi();
                }
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getDencData(String data) {
        try {
            String strSM4Decrypt = SM4Util.decryptEcb(this.SM4_KEY, data);
            JSONObject json = new JSONObject();
            json.put("code", 0);
            json.put("message", "成功");
            json.put("data", strSM4Decrypt);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getEncDataPOST(String url, String jgbm, String grbh, String data) {
        try {
            String S = SM3Util.digest(data + this.SM4_KEY);
            String B = SM4Util.encryptEcb(this.SM4_KEY, data);
            String timestampM = String.valueOf(System.currentTimeMillis());
            String T = SM3Util.digest(timestampM + this.SM4_KEY);
            String uniqueidM = jgbm + grbh + url + timestampM;
            String U = SM3Util.digest(uniqueidM + this.SM4_KEY);
            JSONObject json = new JSONObject();
            json.put("code", 0);
            json.put("message", "成功");
            json.put("timestampM", timestampM);
            json.put("uniqueidM", uniqueidM);
            json.put("timestamp", T);
            json.put("uniqueid", U);
            json.put("datasign", S);
            json.put("data", B);
            return json.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getEncDataGET(String url, String jgbm, String grbh, String data) {
        try {
            String S = SM3Util.digest(data + this.SM4_KEY);
            String B = SM4Util.encryptEcb(this.SM4_KEY, data);
            String timestampM = String.valueOf(System.currentTimeMillis());
            String T = SM3Util.digest(timestampM + this.SM4_KEY);
            String uniqueidM = jgbm + grbh + url + timestampM;
            String U = SM3Util.digest(uniqueidM + this.SM4_KEY);
            JSONObject json = new JSONObject();
            json.put("code", 0);
            json.put("message", "成功");
            json.put("timestampM", timestampM);
            json.put("uniqueidM", uniqueidM);
            json.put("timestamp", T);
            json.put("uniqueid", U);
            json.put("datasign", S);
            json.put("data", B);
            return json.toString();
        } catch (Exception e) {
            return "";
        }
    }

    @JavascriptInterface
    public void getEncDataGET() {
    }

    @JavascriptInterface
    public void getWifiinfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (NetUtils.checkWifi(this.mContext).intValue() == 2) {
                jsonObject.put("isWifi", true);
                jsonObject.put("wifiName", NetUtils.getWifiName(this.mContext));
                jsonObject.put("wifiMac", NetUtils.getLocalMacAddress(this.mContext));
            } else {
                jsonObject.put("isWifi", false);
                jsonObject.put("wifiName", "");
                jsonObject.put("wifiMac", "");
            }
        } catch (JSONException e) {
        }
        execJsMethod("javascript:wifiInfoResult(" + jsonObject + ")");
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void getTicket(final String user_info, String cpbs) {
        int i = this.cheque;
        if (i == 0 || i == 1) {
            getTicketOld(user_info, cpbs);
            return;
        }
        if (i == 2) {
            getCheque(user_info);
            return;
        }
        long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(Long.valueOf(time));
        String sign = "client_id" + Constants.CLIENT_ID + "forward_client_id" + cpbs + "timestamp" + date + "userinfo" + user_info + Constants.APPSECRET;
        KLog.i(this.TAG, "前：" + sign);
        String sign2 = MD5Util.getMD5(sign);
        KLog.i(this.TAG, "client_id:  " + Constants.CLIENT_ID);
        KLog.i(this.TAG, "forward_client_id:  " + cpbs);
        KLog.i(this.TAG, "sign:  " + sign2);
        KLog.i(this.TAG, "timestamp:  " + date);
        KLog.i(this.TAG, "user_info:  " + user_info);
        KLog.i(this.TAG, OkGoNetManager.TICKET_COMMENT_URL + OkGoNetManager.GET_TICKET_NEW);
        KLog.i(this.TAG, Utils.getQyCode());
        PostRequest post = OkGo.post(OkGoNetManager.TICKET_COMMENT_URL + OkGoNetManager.GET_TICKET_NEW);
        post.connTimeOut(1000L);
        post.readTimeOut(3000L);
        post.writeTimeOut(1000L);
        ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) post.tag(this)).headers("M-Sy-AppId", Constants.M_SY_APPID)).headers("M-Sy-Version", Constants.M_SY_VERSION)).headers("M-Sy-Service", Utils.getQyCode())).headers("Content-Type", "application/json")).headers("data", "")).params("client_id", Constants.CLIENT_ID, new boolean[0])).params("forward_client_id", cpbs, new boolean[0])).params("sign", sign2, new boolean[0])).params("timestamp", date, new boolean[0])).params("userinfo", user_info, new boolean[0])).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.9
            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onSuccess(String s, Call call, Response response) {
                KLog.i(WebActivity.this.TAG, s);
                try {
                    JSONObject js = new JSONObject(s);
                    String ticket = js.get("ticket").toString();
                    Utils.saveMsg(WebActivity.this.mContext, "ticket", ticket);
                    if (!WebActivity.this.url.contains("ticket=")) {
                        if (WebActivity.this.url.contains("?")) {
                            WebActivity.this.url = WebActivity.this.url + "&ticket=" + ticket;
                        } else {
                            WebActivity.this.url = WebActivity.this.url + "?ticket=" + ticket;
                        }
                    }
                    WebActivity.this.getCheque(user_info);
                    KLog.i(WebActivity.this.TAG, WebActivity.this.url);
                } catch (JSONException e) {
                    WebActivity.this.initWebView();
                    e.printStackTrace();
                }
            }

            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                WebActivity.this.getCheque(user_info);
                if (response != null) {
                    ToastUtil.toast(WebActivity.this.mContext, ToastInfo.btnError, "ticket获取", ToastType.FAIL);
                    KLog.i(WebActivity.this.TAG, response.message());
                    KLog.i(WebActivity.this.TAG, Integer.valueOf(response.code()));
                    KLog.i(WebActivity.this.TAG, e.toString());
                }
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void getTicketOld(String user_info, String cpbs) {
        long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(Long.valueOf(time));
        String sign = "client_id" + Constants.CLIENT_ID + "forward_client_id" + cpbs + "timestamp" + date + "userinfo" + user_info + Constants.APPSECRET;
        KLog.i(this.TAG, "前：" + sign);
        String sign2 = MD5Util.getMD5(sign);
        KLog.i(this.TAG, "client_id:  " + Constants.CLIENT_ID);
        KLog.i(this.TAG, "forward_client_id:  " + cpbs);
        KLog.i(this.TAG, "sign:  " + sign2);
        KLog.i(this.TAG, "timestamp:  " + date);
        KLog.i(this.TAG, "user_info:  " + user_info);
        KLog.i(this.TAG, OkGoNetManager.TICKET_COMMENT_URL + OkGoNetManager.GET_TICKET_NEW);
        KLog.i(this.TAG, Utils.getQyCode());
        PostRequest post = OkGo.post(OkGoNetManager.TICKET_COMMENT_URL + OkGoNetManager.GET_TICKET_NEW);
        post.connTimeOut(1000L);
        post.readTimeOut(3000L);
        post.writeTimeOut(1000L);
        ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) post.tag(this)).headers("M-Sy-AppId", Constants.M_SY_APPID)).headers("M-Sy-Version", Constants.M_SY_VERSION)).headers("M-Sy-Service", Utils.getQyCode())).headers("Content-Type", "application/json")).headers("data", "")).params("client_id", Constants.CLIENT_ID, new boolean[0])).params("forward_client_id", cpbs, new boolean[0])).params("sign", sign2, new boolean[0])).params("timestamp", date, new boolean[0])).params("userinfo", user_info, new boolean[0])).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.11
            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onSuccess(String s, Call call, Response response) {
                KLog.i(WebActivity.this.TAG, s);
                try {
                    JSONObject js = new JSONObject(s);
                    String ticket = js.get("ticket").toString();
                    Utils.saveMsg(WebActivity.this.mContext, "ticket", ticket);
                    if (!WebActivity.this.url.contains("ticket=")) {
                        if (WebActivity.this.url.contains("?")) {
                            WebActivity.this.url = WebActivity.this.url + "&ticket=" + ticket;
                        } else {
                            WebActivity.this.url = WebActivity.this.url + "?ticket=" + ticket;
                        }
                    }
                    KLog.i(WebActivity.this.TAG, WebActivity.this.url);
                    WebActivity.this.initWebView();
                } catch (JSONException e) {
                    WebActivity.this.initWebView();
                    e.printStackTrace();
                }
            }

            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                WebActivity.this.initWebView();
                if (response != null) {
                    ToastUtil.toast(WebActivity.this.mContext, ToastInfo.btnError, "ticket获取", ToastType.FAIL);
                    KLog.i(WebActivity.this.TAG, response.message());
                    KLog.i(WebActivity.this.TAG, Integer.valueOf(response.code()));
                    KLog.i(WebActivity.this.TAG, e.toString());
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void getCheque(String user_info) {
        long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(Long.valueOf(time));
        KLog.i(this.TAG, user_info);
        String sign = "client_id" + Constants.CLIENT_ID + "forward_client_id" + this.cpbs + "loginToken" + Utils.getMsg(this.mContext, "accessToken") + "timestamp" + date + "userinfo" + user_info + Constants.APPSECRET;
        KLog.i(this.TAG, "前：" + sign);
        String sign2 = MD5Util.getMD5(sign);
        KLog.i(this.TAG, "client_id:  " + Constants.CLIENT_ID);
        KLog.i(this.TAG, "forward_client_id:  " + this.cpbs);
        KLog.i(this.TAG, "sign:  " + sign2);
        KLog.i(this.TAG, "timestamp:  " + date);
        KLog.i(this.TAG, "user_info:  " + user_info);
        KLog.i(this.TAG, OkGoNetManager.APP_URL + OkGoNetManager.GET_CHEQUE_NEW);
        KLog.i(this.TAG, Utils.getQyCode());
        KLog.i(this.TAG, "调用了GET_CHEQUE_NEW");
        PostRequest post = OkGo.post(OkGoNetManager.APP_URL + OkGoNetManager.GET_CHEQUE_NEW);
        post.connTimeOut(1000L);
        post.readTimeOut(3000L);
        post.writeTimeOut(1000L);
        ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) post.tag(this)).headers("M-Sy-AppId", Constants.M_SY_APPID)).headers("M-Sy-Version", Constants.M_SY_VERSION)).headers("M-Sy-Service", Utils.getQyCode())).headers("Content-Type", "application/json")).headers("data", "")).params("client_id", Constants.CLIENT_ID, new boolean[0])).params("forward_client_id", this.cpbs, new boolean[0])).params("sign", sign2, new boolean[0])).params("loginToken", Utils.getMsg(this.mContext, "accessToken"), new boolean[0])).params("timestamp", date, new boolean[0])).params("userinfo", user_info, new boolean[0])).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.13
            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onSuccess(String s, Call call, Response response) {
                KLog.i(WebActivity.this.TAG, s);
                try {
                    JSONObject js = new JSONObject(s);
                    String ticket = js.optString("ticket");
                    if (TextUtils.isEmpty(ticket)) {
                        if (TextUtils.isEmpty(js.optString("msg"))) {
                            ToastUtil.toast(WebActivity.this.mContext, "msg", "身份票据获取失败，请重试", ToastType.FAIL);
                        } else {
                            ToastUtil.toast(WebActivity.this.mContext, "msg", js.optString("msg"), ToastType.FAIL);
                        }
                    }
                    if (!WebActivity.this.url.contains("cheque=")) {
                        if (WebActivity.this.url.contains("?")) {
                            WebActivity.this.url = WebActivity.this.url + "&cheque=" + ticket;
                        } else {
                            WebActivity.this.url = WebActivity.this.url + "?cheque=" + ticket;
                        }
                    }
                    KLog.i(WebActivity.this.TAG, WebActivity.this.url);
                    WebActivity.this.initWebView();
                } catch (JSONException e) {
                    WebActivity.this.initWebView();
                    e.printStackTrace();
                }
            }

            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                ToastUtil.toast(WebActivity.this.mContext, "msg", "身份票据获取失败，请重试", ToastType.FAIL);
                WebActivity.this.initWebView();
                if (response != null) {
                    try {
                        KLog.i(WebActivity.this.TAG, response.message());
                        KLog.i(WebActivity.this.TAG, Integer.valueOf(response.code()));
                        KLog.i(WebActivity.this.TAG, e.toString());
                    } catch (Exception e2) {
                    }
                }
            }
        });
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    @JavascriptInterface
    public void postMessage(String str) {
        int i;
        char c;
        String rotateScreen;
        String togroup;
        String gid;
        String str2;
        String groupname;
        String hidden;
        String str_toUserName;
        String function;
        String str_toUser;
        String openUrl;
        String str3;
        String str_toUserName2;
        String str_toUser2;
        String groupname2;
        String gid2;
        KLog.i(this.TAG, "js");
        try {
            KLog.i(this.TAG, "str:" + str);
            JSONObject jSONObject = new JSONObject(str);
            this.jsonObject_post = jSONObject;
            String str_webtitle = jSONObject.optString("title");
            String openUrl2 = this.jsonObject_post.optString("openUrl");
            if (Utils.haveValue(str_webtitle) && str_webtitle.length() > 1) {
                this.webtitle = str_webtitle;
            }
            KLog.d(this.TAG, "webtitle==" + this.webtitle);
            String str_rightBarTitle = this.jsonObject_post.optString("rightBarTitle");
            if (!this.jsonObject_post.isNull("rightBarTitle")) {
                if ("".equals(str_rightBarTitle)) {
                    this.rightBarTitle = "";
                } else if (Utils.haveValue(str_rightBarTitle)) {
                    this.rightBarTitle = str_rightBarTitle;
                }
            }
            String tzyswd = "";
            if (this.jsonObject_post.has("type")) {
                tzyswd = this.jsonObject_post.optString("type");
            }
            if ("tzyswd".equals(tzyswd)) {
                startActivityForResult(new Intent(this.mContext, (Class<?>) FileMainActivity.class), 1638);
                return;
            }
            String str_rightBarIcon = this.jsonObject_post.optString("rightBarIcon");
            if (!this.jsonObject_post.isNull("rightBarIcon")) {
                if ("".equals(str_rightBarIcon)) {
                    this.rightBarIcon = "";
                } else if (Utils.haveValue(str_rightBarIcon)) {
                    this.rightBarIcon = str_rightBarIcon;
                }
            }
            String str_pop = this.jsonObject_post.optString("pop");
            if (str_pop.equals("1")) {
                KLog.i(this.TAG, str_pop);
                this.mHandler.sendEmptyMessage(2);
            } else if (str_pop.equals("2")) {
                KLog.i(this.TAG, str_pop);
                this.mHandler.sendEmptyMessage(3);
            }
            String str_copy = this.jsonObject_post.optString("fuzhistr");
            if (Utils.haveValue(str_copy) && str_copy.length() > 1) {
                ClipboardManager cm = (ClipboardManager) getSystemService("clipboard");
                ClipData mClipData = ClipData.newPlainText("Label", str_copy);
                cm.setPrimaryClip(mClipData);
            }
            String str_private_url = this.jsonObject_post.optString("pushURL");
            String hidden2 = this.jsonObject_post.optString("hidden");
            Log.e(this.TAG, "privateUrl" + this.private_url);
            if (Utils.haveValue(str_private_url) && str_private_url.length() > 1) {
                this.private_url = str_private_url;
            }
            final String push = this.jsonObject_post.optString("push");
            String rotateScreen2 = this.jsonObject_post.optString("rotateScreen");
            String str_singleChat = this.jsonObject_post.optString("singleChat");
            String str_toUser3 = this.jsonObject_post.optString("userid");
            String str_toUserName3 = this.jsonObject_post.optString("username");
            String togroup2 = this.jsonObject_post.optString("togroup");
            String gid3 = this.jsonObject_post.optString("gid");
            String groupname3 = this.jsonObject_post.optString("groupname");
            String function2 = this.jsonObject_post.optString("function");
            if (!"setAppCache".equals(function2)) {
                rotateScreen = rotateScreen2;
            } else {
                String key = this.jsonObject_post.optString("key");
                rotateScreen = rotateScreen2;
                setAppCache(key, this.jsonObject_post.optString("data"));
            }
            if ("removeAppCache".equals(function2)) {
                String key2 = this.jsonObject_post.optString("key");
                removeAppCache(key2);
            }
            if ("getAppCache".equals(function2)) {
                String key3 = this.jsonObject_post.optString("key");
                getAppCache(key3);
            }
            JSONObject jsonObject = new JSONObject(str);
            if (!jsonObject.has("function")) {
                togroup = togroup2;
                gid = gid3;
                str2 = "type";
                groupname = groupname3;
                hidden = hidden2;
                str_toUserName = str_toUserName3;
                function = function2;
                str_toUser = str_toUser3;
                openUrl = openUrl2;
            } else {
                try {
                    if (TextUtils.isEmpty(function2)) {
                        togroup = togroup2;
                        gid = gid3;
                        str2 = "type";
                        groupname = groupname3;
                        hidden = hidden2;
                        str_toUserName = str_toUserName3;
                        function = function2;
                        str_toUser = str_toUser3;
                        openUrl = openUrl2;
                    } else {
                        byte b = -1;
                        switch (function2.hashCode()) {
                            case -1906222979:
                                if (function2.equals("getSignData")) {
                                    b = 3;
                                }
                                break;
                            case -1343376330:
                                if (function2.equals("getDencData")) {
                                    b = 2;
                                }
                                break;
                            case -1263208537:
                                if (function2.equals("openPop")) {
                                    b = 5;
                                }
                                break;
                            case -369882872:
                                if (function2.equals("getEncDataGET")) {
                                    b = 0;
                                }
                                break;
                            case 218055447:
                                if (function2.equals("sideslip")) {
                                    b = 4;
                                }
                                break;
                            case 1418810638:
                                if (function2.equals("getEncDataPOST")) {
                                    b = 1;
                                }
                                break;
                        }
                        function = function2;
                        hidden = hidden2;
                        openUrl = openUrl2;
                        groupname = groupname3;
                        gid = gid3;
                        togroup = togroup2;
                        str2 = "type";
                        str_toUserName = str_toUserName3;
                        if (b != 0) {
                            str_toUser = str_toUser3;
                            if (b == 1) {
                                String bb_post_url = jsonObject.optString("url", "");
                                String bb_post_jgbm = jsonObject.optString("jgbm", "");
                                String bb_post_grbh = jsonObject.optString("grbh", "");
                                String bb_post_data = jsonObject.optString("data", "");
                                String bb_post_callback = jsonObject.optString("callback", "");
                                KLog.i(bb_post_url);
                                KLog.i(bb_post_jgbm);
                                KLog.i(bb_post_grbh);
                                KLog.i(bb_post_data);
                                String encPostData = getEncDataPOST(bb_post_url, bb_post_jgbm, bb_post_grbh, bb_post_data);
                                if (!TextUtils.isEmpty(encPostData)) {
                                    execJsMethod("javascript:" + bb_post_callback + "(" + encPostData + ")");
                                } else {
                                    JSONObject jsonEncPost = new JSONObject();
                                    jsonEncPost.put("code", 99);
                                    jsonEncPost.put("message", "失败");
                                    execJsMethod("javascript:" + bb_post_callback + "(" + encPostData + ")");
                                }
                            } else if (b == 2) {
                                String bb_enc_data = jsonObject.optString("data", "");
                                String bb_denc_callback = jsonObject.optString("callback", "");
                                String bb_denc_data = getDencData(bb_enc_data);
                                String str4 = this.TAG;
                                Object[] objArr = new Object[1];
                                objArr[0] = bb_denc_data;
                                KLog.i(str4, objArr);
                                if (!TextUtils.isEmpty(bb_denc_data)) {
                                    execJsMethod("javascript:" + bb_denc_callback + "(" + bb_denc_data + ")");
                                } else {
                                    JSONObject jsonDenc = new JSONObject();
                                    jsonDenc.put("code", 99);
                                    jsonDenc.put("message", "失败");
                                    execJsMethod("javascript:" + bb_denc_callback + "(" + bb_denc_data + ")");
                                }
                            } else if (b == 3) {
                                String funName = jsonObject.optString("callback");
                                if (TextUtils.isEmpty(funName)) {
                                    return;
                                }
                                String bb_sign_url = jsonObject.optString("url", "");
                                String bb_sign_jgbm = jsonObject.optString("jgbm", "");
                                String bb_sign_grbh = jsonObject.optString("grbh", "");
                                String bb_sign_data = jsonObject.optString("data", "");
                                JSONObject requestData = EncUtils.getSignData(bb_sign_url, bb_sign_jgbm, bb_sign_grbh, bb_sign_data);
                                if (requestData != null) {
                                    this.webView.loadUrl("javascript:" + funName + "(" + requestData + ")");
                                } else {
                                    this.webView.loadUrl("javascript:" + funName + "(" + EncUtils.getErrorReport() + ")");
                                }
                            } else if (b == 4) {
                                String open = jsonObject.optString(TtmlNode.TEXT_EMPHASIS_MARK_OPEN);
                                if (!Utils.haveValue(open) || !"YES".equals(open)) {
                                    this.isOpenSideslip = false;
                                } else {
                                    this.isOpenSideslip = true;
                                }
                            } else if (b == 5) {
                                String openPop = jsonObject.optString(TtmlNode.TEXT_EMPHASIS_MARK_OPEN);
                                if (!Utils.haveValue(openPop) || !"YES".equals(openPop)) {
                                    this.isOpenPop = false;
                                } else {
                                    this.isOpenPop = true;
                                }
                            }
                        } else {
                            str_toUser = str_toUser3;
                            String bb_get_url = jsonObject.optString("url", "");
                            String bb_get_jgbm = jsonObject.optString("jgbm", "");
                            String bb_get_grbh = jsonObject.optString("grbh", "");
                            String bb_get_data = jsonObject.optString("data", "");
                            String bb_get_callback = jsonObject.optString("callback", "");
                            String encGetData = getEncDataGET(bb_get_url, bb_get_jgbm, bb_get_grbh, bb_get_data);
                            if (!TextUtils.isEmpty(encGetData)) {
                                execJsMethod("javascript:" + bb_get_callback + "(" + encGetData + ")");
                            } else {
                                JSONObject jsonEncGet = new JSONObject();
                                jsonEncGet.put("code", 99);
                                jsonEncGet.put("message", "失败");
                                execJsMethod("javascript:" + bb_get_callback + "(" + encGetData + ")");
                            }
                        }
                    }
                } catch (Exception e) {
                    e = e;
                    i = 1;
                    c = 0;
                    String str5 = this.TAG;
                    Object[] objArr2 = new Object[i];
                    objArr2[c] = e.toString();
                    KLog.i(str5, objArr2);
                }
            }
            if (Utils.haveValue(str_singleChat)) {
                Intent intent = new Intent(this, (Class<?>) ChatActivity.class);
                if (!TextUtils.isEmpty(str_toUser)) {
                    str_toUser2 = str_toUser;
                    if (str_toUser2.equals(Utils.getMsg(this.mContext, "userid"))) {
                        ToastUtil.toast(this.mContext, ToastInfo.notEnabled, "", ToastType.FAIL);
                        return;
                    }
                } else {
                    str_toUser2 = str_toUser;
                }
                if (TextUtils.isEmpty(str_toUser2) || "undefined".equals(str_toUser2)) {
                    ToastUtil.toast(this.mContext, "msg", "userId不能为空", ToastType.FAIL);
                    return;
                }
                intent.putExtra("toUser", str_toUser2);
                str_toUserName2 = str_toUserName;
                intent.putExtra("toUserName", str_toUserName2);
                str3 = str2;
                intent.putExtra(str3, "3");
                startActivity(intent);
            } else {
                str3 = str2;
                str_toUserName2 = str_toUserName;
                str_toUser2 = str_toUser;
            }
            if (Utils.haveValue(togroup)) {
                Intent intent2 = new Intent(this, (Class<?>) ChatActivity.class);
                if (!TextUtils.isEmpty(gid)) {
                    gid2 = gid;
                    if (!"undefined".equals(gid2)) {
                        intent2.putExtra("toUser", gid2);
                        groupname2 = groupname;
                        intent2.putExtra("toUserName", groupname2);
                        intent2.putExtra(str3, "4");
                        startActivity(intent2);
                    }
                }
                ToastUtil.toast(this.mContext, "msg", "群组信息不能为空", ToastType.FAIL);
                return;
            }
            groupname2 = groupname;
            gid2 = gid;
            if (this.jsonObject_post.has(str3)) {
                String type = this.jsonObject_post.optString(str3);
                if ("qiantiaoLt".equals(type)) {
                    final JSONObject data = this.jsonObject_post.optJSONObject("data");
                    final boolean type1 = data.optBoolean(str3);
                    runOnUiThread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.14
                        @Override // java.lang.Runnable
                        public void run() {
                            if (type1) {
                                WebActivity.this.ll_chat.setVisibility(0);
                                if (!WebActivity.this.chatPrepare) {
                                    WebActivity.this.chatType = "4";
                                    WebActivity.this.toUser = data.optString("qzid");
                                    WebActivity.this.toUserName = data.optString("qzname");
                                    WebActivity.this.initChat();
                                    WebActivity.this.chatPrepare = true;
                                    return;
                                }
                                return;
                            }
                            WebActivity.this.ll_chat.setVisibility(8);
                            WebActivity webActivity = WebActivity.this;
                            if (!webActivity.isKeyboardShown(webActivity.ll)) {
                                if (WebActivity.this.isEmogiShow) {
                                    WebActivity.this.editEmojicon.clearFocus();
                                    WebActivity.this.hideEmogiPanel();
                                    return;
                                } else {
                                    if (WebActivity.this.isAddShow) {
                                        WebActivity.this.editEmojicon.clearFocus();
                                        WebActivity.this.hideAddPanel();
                                        return;
                                    }
                                    return;
                                }
                            }
                            WebActivity webActivity2 = WebActivity.this;
                            webActivity2.hideKeyboard(webActivity2);
                        }
                    });
                }
            }
            final String str_pay = this.jsonObject_post.optString("pay");
            final String str_subject = this.jsonObject_post.optString("subject");
            final String str_body = this.jsonObject_post.optString(TtmlNode.TAG_BODY);
            final String str_price = this.jsonObject_post.optString("price");
            final String str_ywlsh = this.jsonObject_post.optString("ywlsh");
            c = 0;
            try {
                final String str_sign = this.jsonObject_post.optString("sign");
                final String loadPDF = this.jsonObject_post.optString("loadPDF");
                this.params2 = this.jsonObject_post.toString();
                final String hidden3 = hidden;
                final String openUrl3 = openUrl;
                final String rotateScreen3 = rotateScreen;
                i = 1;
                try {
                    runOnUiThread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.15
                        @Override // java.lang.Runnable
                        public void run() {
                            Intent intent3;
                            String cpbs;
                            if (Utils.haveValue(WebActivity.this.webtitle) && !WebActivity.this.webtitle.contains("undefined") && WebActivity.this.webtitle.length() > 1) {
                                WebActivity.this.tv_title.setText(WebActivity.this.webtitle);
                            }
                            if (!Utils.haveValue(WebActivity.this.rightBarTitle) || WebActivity.this.rightBarTitle.length() <= 1) {
                                WebActivity.this.tv_title_more.setVisibility(8);
                            } else {
                                KLog.d(WebActivity.this.TAG, "跳转文字");
                                WebActivity.this.tv_title_more.setVisibility(0);
                                WebActivity.this.iv_more.setVisibility(8);
                                WebActivity.this.tv_title_more.setText(WebActivity.this.rightBarTitle);
                                WebActivity.this.tv_title_more.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.15.1
                                    @Override // android.view.View.OnClickListener
                                    public void onClick(View view) {
                                        WebActivity.this.webView.loadUrl("javascript:rightItemClick()");
                                    }
                                });
                            }
                            if (!Utils.haveValue(WebActivity.this.rightBarIcon) || WebActivity.this.rightBarIcon.length() <= 1) {
                                WebActivity.this.iv_more.setVisibility(8);
                            } else {
                                KLog.d(WebActivity.this.TAG, "跳转图片");
                                WebActivity.this.tv_title_more.setVisibility(8);
                                WebActivity.this.iv_more.setVisibility(0);
                                if (!WebActivity.this.rightBarIcon.startsWith("http")) {
                                    WebActivity.this.rightBarIcon = OkGoNetManager.WEB_URL_TRUE + WebActivity.this.rightBarIcon;
                                }
                                WebActivity webActivity = WebActivity.this;
                                webActivity.rightBarIcon = Utils.replaceIpAndPort(webActivity.rightBarIcon, OkGoNetManager.APP_URL);
                                Glide.with(WebActivity.this.mContext).load(WebActivity.this.rightBarIcon).into(WebActivity.this.iv_more);
                                WebActivity.this.iv_more.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.15.2
                                    @Override // android.view.View.OnClickListener
                                    public void onClick(View view) {
                                        WebActivity.this.webView.loadUrl("javascript:rightItemClick()");
                                    }
                                });
                            }
                            if (Utils.haveValue(loadPDF)) {
                                WebActivity.this.jsonObject_post.optString("title");
                                String data2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                                String str6 = loadPDF;
                                String fileName = str6 == null ? "" : str6.substring(str6.lastIndexOf(StrUtil.SLASH) + 1);
                                Intent intent4 = new Intent(WebActivity.this.mContext, (Class<?>) PreViewActivity.class);
                                intent4.putExtra("FILEURL", loadPDF);
                                intent4.putExtra("FILENAME", fileName);
                                intent4.putExtra("DATA", data2);
                                if ("0".equals(WebActivity.this.jsonObject_post.optString("isShowrightItem"))) {
                                    intent4.putExtra("SHOWBOTTOM", false);
                                } else {
                                    intent4.putExtra("SHOWBOTTOM", true);
                                }
                                intent4.putExtra("FILETYPE", FileUtil.getMIMEType(fileName));
                                intent4.putExtra("USERID", "htmlpreview");
                                WebActivity.this.mContext.startActivity(intent4);
                            }
                            String title = push;
                            if (Utils.haveValue(title) && push.equals("1")) {
                                if (rotateScreen3.equals("1")) {
                                    intent3 = new Intent(WebActivity.this.mContext, (Class<?>) WebActivity_Heng.class);
                                } else {
                                    intent3 = new Intent(WebActivity.this.mContext, (Class<?>) WebActivity.class);
                                }
                                intent3.putExtra("type", "0008");
                                intent3.putExtra("params2", WebActivity.this.params2);
                                if (WebActivity.this.private_url.contains("http")) {
                                    Utils.getMsg(WebActivity.this.mContext, "zxjgbm");
                                    Utils.getMsg(WebActivity.this.mContext, "jgbm");
                                    Utils.getMsg(WebActivity.this.mContext, "khbh");
                                    Utils.getMsg(WebActivity.this.mContext, "userid");
                                    Utils.getMsg(WebActivity.this.mContext, "zjhm");
                                    if (WebActivity.this.jsonObject_post.has("cpbs")) {
                                        cpbs = WebActivity.this.jsonObject_post.optString("cpbs");
                                    } else {
                                        cpbs = Constants.CPBS;
                                    }
                                    KLog.i(WebActivity.this.TAG, "cpbs:" + cpbs);
                                    intent3.putExtra("web_url", WebActivity.this.private_url);
                                    intent3.putExtra("type", "99999");
                                    intent3.putExtra("cpbs", cpbs);
                                    String result = Utils.getMsg(WebActivity.this.mContext, "UserXx");
                                    Object grxx = Utils.getMsg(WebActivity.this.mContext, "grxx");
                                    try {
                                        JSONObject js = new JSONObject(result);
                                        js.put("grxx", grxx);
                                        if (WebActivity.this.jsonObject_post.has("ticket_param_key") && WebActivity.this.jsonObject_post.has("ticket_param_value")) {
                                            String key4 = WebActivity.this.jsonObject_post.optString("ticket_param_key");
                                            String value = WebActivity.this.jsonObject_post.optString("ticket_param_value");
                                            if (Utils.haveValue(key4) && Utils.haveValue(value)) {
                                                js.put(key4, value);
                                            }
                                        }
                                        result = js.toString();
                                    } catch (JSONException e2) {
                                        e2.printStackTrace();
                                    }
                                    intent3.putExtra("ticket", result);
                                    intent3.putExtra("tztype", "0008");
                                    WebActivity.this.startActivityForResult(intent3, 1638);
                                    if ("1".equals(hidden3)) {
                                        WebActivity.this.finish();
                                    }
                                } else if (WebActivity.this.private_url.contains("http")) {
                                    intent3.putExtra("web_url", WebActivity.this.private_url);
                                    WebActivity.this.startActivityForResult(intent3, 1638);
                                    if ("1".equals(hidden3)) {
                                        WebActivity.this.finish();
                                    }
                                } else {
                                    intent3.putExtra("web_url", OkGoNetManager.WEB_URL + WebActivity.this.private_url);
                                    WebActivity.this.startActivityForResult(intent3, 1638);
                                    if ("1".equals(hidden3)) {
                                        WebActivity.this.finish();
                                    }
                                }
                            }
                            if (Utils.haveValue(str_pay) && str_pay.equals("alipay")) {
                                Intent intent5 = new Intent(WebActivity.this.mContext, (Class<?>) AliPayActivity.class);
                                intent5.putExtra("ywlsh", str_ywlsh);
                                intent5.putExtra("price", str_price);
                                intent5.putExtra(TtmlNode.TAG_BODY, str_body);
                                intent5.putExtra("subject", str_subject);
                                intent5.putExtra("sign", str_sign);
                                WebActivity.this.startActivity(intent5);
                            }
                            String openUrlcurr = openUrl3;
                            if (!TextUtils.isEmpty(openUrlcurr)) {
                                if (!openUrlcurr.startsWith(JPushConstants.HTTP_PRE) && !openUrlcurr.startsWith(JPushConstants.HTTPS_PRE)) {
                                    openUrlcurr = JPushConstants.HTTP_PRE + openUrlcurr;
                                }
                                Intent intent6 = new Intent();
                                intent6.setAction("android.intent.action.VIEW");
                                intent6.setData(Uri.parse(openUrlcurr));
                                WebActivity.this.startActivity(intent6);
                            }
                        }
                    });
                } catch (Exception e2) {
                    e = e2;
                    String str52 = this.TAG;
                    Object[] objArr22 = new Object[i];
                    objArr22[c] = e.toString();
                    KLog.i(str52, objArr22);
                }
            } catch (Exception e3) {
                e = e3;
                i = 1;
            }
        } catch (Exception e4) {
            e = e4;
        }
    }

    @JavascriptInterface
    public void brushFaceauthorization(Object msg, CompletionHandler<String> handler) {
        KLog.i(this.TAG, "brushFaceauthorization");
        this.brushHandler = handler;
        String name = Utils.getMsg(this.mContext, c.e);
        String zjhm = Utils.getMsg(this.mContext, "zjhm");
        KLog.i(this.TAG, name + zjhm);
        antCloudFaceCertifyNew(name, zjhm);
    }

    @JavascriptInterface
    public void switchScreens(String bs) {
        if ("1".equals(bs)) {
            setRequestedOrientation(1);
        } else if ("2".equals(bs)) {
            setRequestedOrientation(0);
        }
    }

    @JavascriptInterface
    public void jumptoAddressbook(String deptid, String deptname) {
        Intent intent = new Intent(this.mContext, (Class<?>) OrganizationalStructureNewActivity.class);
        intent.putExtra("departmentId", deptid);
        intent.putExtra("departmentName", deptname + "_" + deptid + ",");
        startActivity(intent);
    }

    @JavascriptInterface
    public void brushFaceauthorization() {
        KLog.i(this.TAG, "brushFaceauthorization");
        String name = Utils.getMsg(this.mContext, c.e);
        String zjhm = Utils.getMsg(this.mContext, "zjhm");
        KLog.i(this.TAG, name + zjhm);
        antCloudFaceCertifyNew(name, zjhm);
    }

    @JavascriptInterface
    public void brushFaceauthorization(String name, String zjhm, String msg) {
        KLog.i(this.TAG, "brushFaceauthorization");
        KLog.i(this.TAG, name + zjhm);
        antCloudFaceCertifyNew(name, zjhm);
    }

    private void setAppCache(String key, String value) {
        Utils.saveMsg(this.mContext, key, value);
        JSONObject jsonResult = new JSONObject();
        try {
            jsonResult.put("code", 0);
            jsonResult.put("setAppCache", key);
            execJsMethod("");
        } catch (Exception e) {
            KLog.i(this.TAG, e.toString());
        }
    }

    private void removeAppCache(String key) {
        Utils.deleteMsg(this.mContext, key);
        JSONObject jsonResult = new JSONObject();
        try {
            jsonResult.put("code", 0);
            jsonResult.put("removeAppCache", key);
            execJsMethod("");
        } catch (Exception e) {
            KLog.i(this.TAG, e.toString());
        }
    }

    private void getAppCache(String key) {
        String value = Utils.getMsg(this.mContext, key);
        JSONObject jsonResult = new JSONObject();
        try {
            jsonResult.put("code", 0);
            jsonResult.put("getAppCache", key);
            jsonResult.put(key, value);
            execJsMethod("javascript:appCacheResult(" + jsonResult.toString() + ")");
        } catch (Exception e) {
            KLog.i(this.TAG, e.toString());
        }
    }

    @JavascriptInterface
    public void takePhotoForBase64(Object msg, CompletionHandler<String> handler) throws XmlPullParserException, IOException {
        this.brushHandler = handler;
        openCamare();
    }

    @JavascriptInterface
    public void takePhotoForBase64(ValueCallback<String> callback) throws XmlPullParserException, IOException {
        this.mUploadStringCallback = callback;
        openCamare();
    }

    @JavascriptInterface
    public void cleanWebCache(String type) {
        if (Utils.haveValue(type) && type.equals("1")) {
            refreshSignSessionEvery30min(this.mContext);
            this.mContext.deleteDatabase("webview.db");
            this.mContext.deleteDatabase("webviewCache.db");
        }
    }

    @JavascriptInterface
    public void startOcr(Object msg, CompletionHandler<String> handler) {
        try {
            JSONObject innerParms = new JSONObject((String) msg);
            String ocrtype = innerParms.getString("ocrtype");
            if (!TextUtils.isEmpty(ocrtype)) {
                this.brushHandler = handler;
                if (ocrtype.equals("generalV")) {
                    CameraTakeActivity.toCameraActivity(this, 1);
                } else if (ocrtype.equals("idcardHeadScan")) {
                    CameraScanActivity.toCameraActivity(this, 2);
                } else if (ocrtype.equals("idcardNationScan")) {
                    CameraScanActivity.toCameraActivity(this, 3);
                } else if (ocrtype.equals("bankcardScan")) {
                    CameraScanActivity.toCameraActivity(this, 4);
                } else if (ocrtype.equals("trainTicketScan")) {
                    CameraScanActivity.toCameraActivity(this, 5);
                } else if (ocrtype.equals("invoiceTaking")) {
                    CameraTakeActivity.toCameraActivity(this, 6);
                } else if (ocrtype.equals("marriageTaking")) {
                    CameraTakeActivity.toCameraActivity(this, 7);
                } else if (ocrtype.equals("businessLicenseTaking")) {
                    CameraTakeActivity.toCameraActivity(this, 8);
                } else if (ocrtype.equals("estateCTaking")) {
                    CameraTakeActivity.toCameraActivity(this, 9);
                } else if (ocrtype.equals("estateRegTaking")) {
                    CameraTakeActivity.toCameraActivity(this, 10);
                } else if (ocrtype.equals("prcTaking")) {
                    CameraTakeActivity.toCameraActivity(this, 11);
                } else {
                    this.brushHandler = null;
                    JSONObject jsonResult = new JSONObject();
                    try {
                        jsonResult.put("data", "");
                        jsonResult.put(MyLocationStyle.ERROR_CODE, 1);
                        jsonResult.put("msg", "未找到执行方法标识");
                        handler.complete(jsonResult.toString());
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            JSONObject jsonResult2 = new JSONObject();
            try {
                jsonResult2.put("data", "");
                jsonResult2.put(MyLocationStyle.ERROR_CODE, 1);
                jsonResult2.put("msg", "入参格式不正确" + e.getMessage());
                handler.complete(jsonResult2.toString());
            } catch (JSONException ex2) {
                ex2.printStackTrace();
            }
        }
    }

    public void ocrSubmit() {
        String str = this.filePath;
        if (str != null && !"".equals(str)) {
            Toast.makeText(this.mContext, "开始识别", 0).show();
            new Thread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.16
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        String serverUrl = "http://61.240.128.22:8911/";
                        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
                        switch (WebActivity.this.mType) {
                            case 1:
                                serverUrl = "http://61.240.128.22:8911/ocr_general_v";
                                parts.add("image", new FileSystemResource(WebActivity.this.filePath));
                                break;
                            case 2:
                                serverUrl = "http://61.240.128.22:8911/ocr_idcard";
                                parts.add("idcard_image", new FileSystemResource(WebActivity.this.filePath));
                                parts.add("type", "0");
                                break;
                            case 3:
                                serverUrl = "http://61.240.128.22:8911/ocr_idcard";
                                parts.add("idcard_image", new FileSystemResource(WebActivity.this.filePath));
                                parts.add("type", "1");
                                break;
                            case 4:
                                serverUrl = "http://61.240.128.22:8911/ocr_bankcard";
                                parts.add("bankcard_image", new FileSystemResource(WebActivity.this.filePath));
                                break;
                            case 5:
                                serverUrl = "http://61.240.128.22:8911/ocr_train_ticket";
                                parts.add("train_ticket_image", new FileSystemResource(WebActivity.this.filePath));
                                break;
                            case 6:
                                serverUrl = "http://61.240.128.22:8911/ocr_invoice";
                                parts.add("invoice_image", new FileSystemResource(WebActivity.this.filePath));
                                break;
                            case 7:
                                serverUrl = "http://61.240.128.22:8911/ocr_marriage_certificate";
                                parts.add("marriage_certificate_image", new FileSystemResource(WebActivity.this.filePath));
                                break;
                            case 8:
                                serverUrl = "http://61.240.128.22:8911/ocr_business_license";
                                parts.add("business_license_image", new FileSystemResource(WebActivity.this.filePath));
                                break;
                            case 9:
                                serverUrl = "http://61.240.128.22:8911/ocr_estate_certificate";
                                parts.add("estate_certificate_image", new FileSystemResource(WebActivity.this.filePath));
                                break;
                            case 10:
                                serverUrl = "http://61.240.128.22:8911/ocr_estate_registration";
                                parts.add("estate_registration_image", new FileSystemResource(WebActivity.this.filePath));
                                break;
                            case 11:
                                serverUrl = "http://61.240.128.22:8911/ocr_property_right_certificate";
                                parts.add("prc_image", new FileSystemResource(WebActivity.this.filePath));
                                break;
                        }
                        String result = WebActivity.this.ocr(serverUrl, parts);
                        android.os.Message msg = new android.os.Message();
                        if (result == "" || result == null) {
                            JSONObject jsonResult = new JSONObject();
                            try {
                                jsonResult.put("data", "");
                                jsonResult.put(MyLocationStyle.ERROR_CODE, 1);
                                jsonResult.put("msg", "识别失败");
                                WebActivity.this.brushHandler.complete(jsonResult.toString());
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                            WebActivity.this.brushHandler = null;
                        } else {
                            JSONObject jsonResult2 = new JSONObject();
                            JSONObject data = new JSONObject();
                            try {
                                data.put("result", result);
                                jsonResult2.put("data", data.toString());
                                jsonResult2.put(MyLocationStyle.ERROR_CODE, 0);
                                jsonResult2.put("msg", "识别成功");
                                WebActivity.this.brushHandler.complete(jsonResult2.toString());
                            } catch (JSONException ex2) {
                                ex2.printStackTrace();
                            }
                            WebActivity.this.brushHandler = null;
                        }
                        WebActivity.this.mHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void back(View view) {
        finish();
    }

    public String ocr(String serverUrl, MultiValueMap<String, Object> parts) {
        try {
            FormHttpMessageConverter converter = new FormHttpMessageConverter();
            converter.addPartConverter(new ResourceHttpMessageConverter());
            this.restTemplate.getMessageConverters().add(converter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpHeaders imageHeaders = new HttpHeaders();
        imageHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        imageHeaders.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<MultiValueMap<String, Object>> imageEntity = new HttpEntity<>(parts, imageHeaders);
        try {
            String response = (String) this.restTemplate.postForObject(serverUrl, imageEntity, String.class, new Object[0]);
            return response;
        } catch (Exception e2) {
            Log.e(OptionalModuleUtils.OCR, e2.getLocalizedMessage() == null ? e2.getMessage() : "");
            return "";
        }
    }

    @JavascriptInterface
    public void outApp() {
        removeAllActivity();
        Utils.mySelfOut(this.mContext, true, 0);
    }

    @JavascriptInterface
    public void logintokeninvalid() {
        ExitDialogUtil.getInstance().showExitDialog();
    }

    @JavascriptInterface
    public void stopPush() {
        JPushInterface.stopPush(getApplicationContext());
    }

    @JavascriptInterface
    public void startPush() {
        JPushInterface.resumePush(getApplicationContext());
    }

    @JavascriptInterface
    public void refreshNavicationCount() {
        if (this.navicationCountUtils == null) {
            this.navicationCountUtils = new NavicationCountUtils();
        }
        this.navicationCountUtils.refreshNavicationCount();
    }

    @JavascriptInterface
    public void onlyrefreshBottomCount() {
        if (this.navicationCountUtils == null) {
            this.navicationCountUtils = new NavicationCountUtils();
        }
        this.navicationCountUtils.refreshNavicationCount();
    }

    @JavascriptInterface
    public void clearWebCache() {
        runOnUiThread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.17
            @Override // java.lang.Runnable
            public void run() {
                new android.webkit.WebView(WebActivity.this.mContext).clearCache(true);
            }
        });
    }

    @JavascriptInterface
    public void hideSharebar(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject(str);
        this.jsonObject_post = jSONObject;
        String sffx = jSONObject.optString("share");
        "0".equals(sffx);
    }

    @JavascriptInterface
    public void refushHome() {
        EventBus.getDefault().post(new Notify.Message(Notify.UpTuiSong));
    }

    @JavascriptInterface
    public void refushMine() {
        EventBus.getDefault().post(new Notify.Message(1010));
    }

    @JavascriptInterface
    public void returnHome() {
        removeAllActivity();
    }

    @JavascriptInterface
    public void finishForResult(String result) {
        KLog.i(this.TAG, result);
        Intent intent = new Intent();
        intent.putExtra("result", result);
        setResult(1638, intent);
        finish();
    }

    @JavascriptInterface
    public void hiddenTitle() {
        runOnUiThread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.18
            @Override // java.lang.Runnable
            public void run() {
                WebActivity.this.rl_title.setVisibility(8);
                if (WebActivity.this.ll != null) {
                    WebActivity.this.ll.requestLayout();
                    WebActivity.this.ll.invalidate();
                }
            }
        });
    }

    @JavascriptInterface
    public void filePreview(String message) {
        try {
            KLog.i(this.TAG, message);
            JSONObject js = new JSONObject(message);
            String fileUrl = js.get("fileUrl").toString();
            String fileName = js.get(DownloadInfo.FILE_NAME).toString();
            String parms = js.has("data") ? js.get("data").toString() : "";
            Intent intent = new Intent(this.mContext, (Class<?>) PreViewActivity.class);
            intent.putExtra("FILEURL", fileUrl);
            intent.putExtra("FILENAME", fileName);
            intent.putExtra("DATA", "");
            intent.putExtra("FILETYPE", FileUtil.getMIMEType(fileName));
            intent.putExtra("USERID", "htmlpreview");
            intent.putExtra("PARMS", parms);
            if ("0".equals(js.optString("isShowrightItem"))) {
                intent.putExtra("SHOWBOTTOM", false);
            } else {
                intent.putExtra("SHOWBOTTOM", true);
            }
            if (js.has("ylService")) {
                intent.putExtra("appUrl", js.get("ylService").toString());
            }
            if (js.has("xgnotes")) {
                intent.putExtra("xgnotes", js.optBoolean("xgnotes"));
            }
            if (js.has("shownotes")) {
                intent.putExtra("shownotes", js.optBoolean("shownotes"));
            }
            startActivityForResult(intent, 0);
        } catch (Exception e) {
            e.toString();
            KLog.i(this.TAG, e.toString());
            KLog.e(this.TAG, e.getMessage());
        }
    }

    @JavascriptInterface
    public void openykjdetails(String jsonObject) {
        try {
            JSONObject result = new JSONObject(jsonObject);
            long id = result.optLong("id");
            int circleType = result.optInt("circleType");
            if (circleType == 3 || circleType == 43) {
                CircleTrendsBean.ResultsBean.DataBean bean = new CircleTrendsBean.ResultsBean.DataBean();
                bean.setCircleType(circleType);
                bean.setId(id);
                TikTok2Activity.start(this.mContext, bean);
            } else {
                Intent intent = new Intent(this.mContext, (Class<?>) CircleTrendsDetailNewActivity.class);
                intent.putExtra("dataId", id);
                this.mContext.startActivity(intent);
            }
        } catch (JSONException e) {
        }
    }

    @JavascriptInterface
    public void downloadImage(String message) {
        try {
            KLog.i(this.TAG, message);
            JSONObject js = new JSONObject(message);
            String fileUrl = js.get("fileUrl").toString();
            String fileName = js.get(DownloadInfo.FILE_NAME).toString();
            saveImageToGallery(fileUrl, fileName);
        } catch (Exception e) {
            e.toString();
            KLog.i(this.TAG, e.toString());
            KLog.e(this.TAG, e.getMessage());
        }
    }

    @JavascriptInterface
    public void getLoginToken(String methedName) {
        KLog.i(this.TAG, methedName);
        String loginToken = Utils.getMsg(this.mContext, "accessToken");
        KLog.i(this.TAG, loginToken);
        KLog.i(this.TAG, "javascript:" + methedName + "(" + loginToken + ")");
        execJsMethod("javascript:" + methedName + "('" + loginToken + "')");
    }

    @JavascriptInterface
    public void getBjys(String methedName) {
        String mode;
        KLog.i(this.TAG, methedName);
        if (Utils.isGonggeNavigationStyle(this.mContext)) {
            mode = TtmlNode.CENTER;
        } else {
            mode = "bottom";
        }
        KLog.i(this.TAG, "javascript:" + methedName + "(" + mode + ")");
        execJsMethod("javascript:" + methedName + "('" + mode + "')");
    }

    @JavascriptInterface
    public void getUserinfo(String methedName) {
        KLog.i(this.TAG, methedName);
        long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(Long.valueOf(time));
        String sign = "client_id" + Constants.CLIENT_ID + "forward_client_id" + this.cpbs + "timestamp" + date + "userinfo" + this.user_info + Constants.APPSECRET;
        KLog.i(this.TAG, "前：" + sign);
        String sign2 = MD5Util.getMD5(sign);
        Map<Object, String> map = new HashMap<>();
        map.put("client_id", Constants.CLIENT_ID);
        map.put("forward_client_id", this.cpbs);
        map.put("sign", sign2);
        map.put("timestamp", date);
        map.put("userinfo", this.user_info);
        JSONObject jsonObject = new JSONObject(map);
        KLog.i(this.TAG, jsonObject);
        OutPutLogUtils.outPutLogWebInfo(jsonObject.toString());
        KLog.i(this.TAG, "javascript:" + methedName + "(" + jsonObject + ")");
        execJsMethod("javascript:" + methedName + "(" + jsonObject + ")");
    }

    @JavascriptInterface
    public void sxzfchatmsg(String msg) {
        HashMap<String, String> map;
        KLog.i(this.TAG, msg);
        try {
            JSONObject jsonObject = new JSONObject(msg);
            String type = jsonObject.optString("type");
            String needCallback = jsonObject.optString("needCallback");
            if ("async".equals(needCallback)) {
                this.zfChatMsgNeedResult = true;
                this.zfChatMsgResultName = jsonObject.optString("needCallbackuuid");
            }
            long time = System.currentTimeMillis();
            HashMap<String, String> map2 = new HashMap<>();
            if ("text".equals(type)) {
                map = map2;
                map.put("content", jsonObject.optString("text"));
                map.put("type", "3");
            } else {
                map = map2;
                map.put("fileUrl", jsonObject.optString("fileUrl"));
                map.put(DownloadInfo.FILE_NAME, jsonObject.optString(DownloadInfo.FILE_NAME));
                map.put("type", "6");
            }
            if (jsonObject.has("addInfo")) {
                map.put("addInfo", jsonObject.optJSONObject("addInfo").toString());
            }
            String uuid = UUID.randomUUID().toString();
            map.put("uqIdentNo", uuid);
            map.put("time", time + "");
            String object = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
            KLog.d(this.TAG, "加密前数据" + object.toString());
            String result = Base64Utils.encode(RSAUtils.encryptByPublicKey(object.getBytes(StandardCharsets.UTF_8.name()), RSAUtils.publicKey));
            String sign = URLEncoder.encode(result, StandardCharsets.UTF_8.name());
            KLog.d(this.TAG, RSAUtils.getValue(URLEncoder.encode(sign, StandardCharsets.UTF_8.name()), RSAUtils.privateKey));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.format(Long.valueOf(time));
            map.put("date", time + "");
            map.put("fromUserId", Utils.getMsg(this.mContext, "userid"));
            map.put("fromUserName", Utils.getMsg(this.mContext, c.e));
            map.put("respType", "1");
            map.put("isRead", StreamerConstants.FALSE);
            map.put("myId", Utils.getMsg(OaApplication.application, "userid"));
            map.put("sendStatus", "9");
            String object2 = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
            KLog.d(this.TAG, object2);
            Message message = (Message) new Gson().fromJson(object2, Message.class);
            Intent intent = new Intent(this.mContext, (Class<?>) RepeatSendSelectActivity.class);
            intent.putExtra("message", message);
            if (this.zfChatMsgNeedResult) {
                intent.putExtra("setResult", true);
                startActivityForResult(intent, 873);
            } else {
                startActivity(intent);
            }
        } catch (Exception e) {
            KLog.i(this.TAG, e.toString());
        }
    }

    public void saveImageToGallery(String imgUrl, String fileName) {
        this.saveImageUrl = imgUrl;
        this.saveImageName = fileName;
        ThreadPoolManager.getInstance().execute(this.saveImageRunnable);
    }

    @JavascriptInterface
    public void startKqdk() {
        KLog.i(this.TAG, "kqdk");
        this.mContext.startActivity(new Intent(this.mContext, (Class<?>) SignWorkingActivity.class));
    }

    @JavascriptInterface
    public void joinMeeting(String str) {
        KLog.i(this.TAG, "joinMeeting");
        try {
            JSONObject js = new JSONObject(str);
            String meetingId = js.getString("meetingId");
            String meetingName = js.getString("meetingName");
            if (OaApplication.isVideoConference) {
                if (meetingId.equals(OaApplication.meetingId)) {
                    if (NetUtils.checkWifi(this.mContext).intValue() == 3) {
                        ToastUtil.toast(this.mContext, ToastInfo.netError, ToastType.FAIL);
                        return;
                    }
                    Intent intent = new Intent(getBaseContext(), (Class<?>) PlayView.class);
                    intent.addFlags(268435456);
                    startActivity(intent);
                    return;
                }
                ToastUtil.toast(this.mContext, "msg", "您正在进行视频会议中，无法参与另外的视频会议", ToastType.FAIL);
                return;
            }
            if (NetUtils.checkWifi(this.mContext).intValue() == 3) {
                ToastUtil.toast(this.mContext, ToastInfo.netError, ToastType.FAIL);
                return;
            }
            Intent intent2 = new Intent(this.mContext, (Class<?>) PlayView.class);
            intent2.putExtra("meetingId", meetingId);
            intent2.putExtra("meetingName", meetingName);
            intent2.putExtra("type", 0);
            startActivity(intent2);
        } catch (Exception e) {
            e.toString();
        }
    }

    @JavascriptInterface
    public void launchMeeting() {
        if (OaApplication.isVideoConference) {
            ToastUtil.toast(this.mContext, "msg", "您正在进行视频会议中，无法参与另外的视频会议", ToastType.FAIL);
        } else {
            if (NetUtils.checkWifi(this.mContext).intValue() == 3) {
                ToastUtil.toast(this.mContext, ToastInfo.netError, ToastType.FAIL);
                return;
            }
            Intent intent = new Intent(this.mContext, (Class<?>) PlayView.class);
            intent.putExtra("type", 1);
            startActivity(intent);
        }
    }

    @JavascriptInterface
    public void startdktj() {
        KLog.i(this.TAG, "dktj");
        this.mContext.startActivity(new Intent(this.mContext, (Class<?>) AttendanceTongJiActivity.class));
    }

    @JavascriptInterface
    public void pushToNoSpeaking() {
        KLog.i(this.TAG, "pushToNoSpeaking");
        this.mContext.startActivity(new Intent(this.mContext, (Class<?>) NoSpeakingActivity.class));
    }

    @JavascriptInterface
    public void pushToChangePassword() {
        KLog.i(this.TAG, "pushToChangePassword");
        this.mContext.startActivity(new Intent(this.mContext, (Class<?>) ChangePasswordActivity.class));
    }

    @JavascriptInterface
    public void startLcsp(String str) {
    }

    @JavascriptInterface
    public void isOnBack() {
    }

    @JavascriptInterface
    public void switchLogon() {
        String s = this.mCache.getAsString("changeUser");
        final ChangeUserBean changeUserBean = (ChangeUserBean) new Gson().fromJson(s, ChangeUserBean.class);
        if (changeUserBean != null && changeUserBean.data != null && changeUserBean.data.size() > 0) {
            String[] items = new String[changeUserBean.data.size() + 1];
            items[0] = changeUserBean.data.get(0).primary_username + "(" + changeUserBean.data.get(0).primary_f_name + ")";
            for (int i = 1; i < changeUserBean.data.size() + 1; i++) {
                items[i] = changeUserBean.data.get(i - 1).other_username + "(" + changeUserBean.data.get(i - 1).other_f_name + ")";
            }
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this.mContext, R.style.CustomAlertDialog);
            alertBuilder.setTitle("请选择切换用户");
            alertBuilder.setItems(items, new DialogInterface.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.20
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    String user_sjhm = Utils.getMsg(WebActivity.this.mContext, "sjhm");
                    if (i2 == 0) {
                        if (!user_sjhm.equals(changeUserBean.data.get(0).primary_f_name)) {
                            WebActivity.this.alertDialog.dismiss();
                            WebActivity.this.UserLogin(changeUserBean.data.get(0).primary_f_name, changeUserBean.data.get(0).primary_f_password);
                            return;
                        }
                        return;
                    }
                    if (!user_sjhm.equals(changeUserBean.data.get(i2 - 1).other_f_name)) {
                        WebActivity.this.alertDialog.dismiss();
                        WebActivity.this.UserLogin(changeUserBean.data.get(i2 - 1).other_f_name, changeUserBean.data.get(i2 - 1).other_f_password);
                    }
                }
            });
            AlertDialog alertDialogCreate = alertBuilder.create();
            this.alertDialog = alertDialogCreate;
            alertDialogCreate.show();
        }
    }

    @JavascriptInterface
    public void HtmlJc(String message) {
        String qzzt;
        int qzts;
        try {
            KLog.i(this.TAG, "htmljq:" + message);
            JSONObject js = new JSONObject(message);
            String gid = js.getString("gid");
            String gname = js.getString("groupname");
            String gsingn = js.getString("groupsign");
            String guserid = js.getString("groupuserid");
            String choosekhbm = js.getString("choosekhbm");
            String grouplx = js.getString("grouplx");
            String ffname = js.getString("newgroup");
            if (!js.has("qzzt")) {
                qzzt = "01";
            } else {
                qzzt = js.getString("qzzt");
            }
            if (!js.has("qzts")) {
                qzts = 0;
            } else {
                qzts = js.getInt("qzts");
            }
            if (ffname.equals("1")) {
                createQunZu(gid, gname, gsingn, guserid, grouplx, choosekhbm, qzts, qzzt);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void startQrCode() {
        new PermissionUtils(this.mContext, Permission.Group.CAMERA);
        Intent intentScan = new Intent(this.mContext, (Class<?>) QrCodeActivity.class);
        startActivity(intentScan);
    }

    @JavascriptInterface
    public void previewRefreshData() {
        this.preRefresh = 1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0146  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void createQunZu(java.lang.String r16, java.lang.String r17, java.lang.String r18, final java.lang.String r19, java.lang.String r20, java.lang.String r21, int r22, java.lang.String r23) {
        /*
            Method dump skipped, instruction units count: 349
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shineyue.pm.web.WebActivity.createQunZu(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void send(String gid, String groupname, String guserid) {
        try {
            long time = System.currentTimeMillis();
            HashMap<String, String> map = new HashMap<>();
            map.put("uqIdentNo", UUID.randomUUID().toString());
            map.put(DtnConfigItem.KEY_GROUP, gid);
            map.put("groupName", groupname);
            map.put("type", "2");
            map.put(TransportConstants.KEY_OPERATION_TYPE, "1");
            map.put("time", time + "");
            map.put("userIds", guserid);
            String object = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
            String result = Base64Utils.encode(RSAUtils.encryptByPublicKey(object.getBytes(StandardCharsets.UTF_8.name()), RSAUtils.publicKey));
            KLog.i(this.TAG, object);
            String sign = URLEncoder.encode(URLEncoder.encode(result, StandardCharsets.UTF_8.name()), StandardCharsets.UTF_8.name());
            System.out.println(RSAUtils.getValue(sign, RSAUtils.privateKey));
            ChatServiceNew chatServiceNew = OaApplication.chatService;
            if (!ChatServiceNew.ws.isOpen()) {
                ToastUtil.toast(this.mContext, ToastInfo.serviceError, "聊天", ToastType.FAIL);
                return;
            }
            ChatServiceNew chatServiceNew2 = OaApplication.chatService;
            ChatServiceNew.ws.sendText("{\"systemName\":\"" + ChatServiceNew.getCHANNEL() + "\",\"sign\":\"" + sign + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void startHysp(String str) {
        KLog.i(this.TAG, "hysp");
        setUpSplash();
        AliRtcEngine.setH5CompatibleMode(1);
        AliRtcLoginPresenter aliRtcLoginPresenter = new AliRtcLoginPresenter();
        this.mLoginPresenter = aliRtcLoginPresenter;
        aliRtcLoginPresenter.attachView(this);
        try {
            JSONObject js = new JSONObject(str);
            this.mEtChannelId = js.optString("bpmid");
            String mUserName = Utils.getMsg(this.mContext, c.e);
            this.mLoginPresenter.getAuthInfo(mUserName, this.mEtChannelId, AliRtcConstants.GSLB_TEST);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getVersionCode(Context context) throws PackageManager.NameNotFoundException {
        return getPackageInfo(context).versionCode;
    }

    public String getVersionName(Context context) throws PackageManager.NameNotFoundException {
        return getPackageInfo(context).versionName;
    }

    public PackageInfo getPackageInfo(Context context) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
    }

    private void takeRecord() {
        Intent intent = new Intent("android.media.action.VIDEO_CAPTURE");
        Uri fileUri = null;
        try {
            fileUri = Uri.fromFile(createMediaFile(PictureMimeType.MP4));
        } catch (IOException e) {
            e.printStackTrace();
        }
        intent.putExtra("autofocus", true);
        intent.putExtra("output", fileUri);
        intent.putExtra("android.intent.extra.videoQuality", 0);
        intent.putExtra("android.intent.extra.sizeLimit", 1048576);
        startActivityForResult(intent, 2323);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openCamare() throws XmlPullParserException, IOException {
        if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            Intent openCameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            Uri imageUri = MyPhotoUtils.getOutputMediaFileUri(getApplicationContext(), this.fileCache, this.timeStamp, this.mediaFile);
            openCameraIntent.putExtra("output", imageUri);
            startActivityForResult(openCameraIntent, 520);
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, 1);
    }

    private void takePhoto() {
        Intent intent = new Intent("android.media.action.VIDEO_CAPTURE");
        Uri fileUri = null;
        try {
            fileUri = Uri.fromFile(createMediaFile(".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.imageUri = fileUri;
        intent.setAction("android.media.action.IMAGE_CAPTURE");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.putExtra("output", fileUri);
        intent.putExtra("android.intent.extra.videoQuality", 0);
        intent.putExtra("android.intent.extra.sizeLimit", 50);
        startActivityForResult(intent, 2324);
    }

    private void selectPhoto() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.setType(SelectMimeType.SYSTEM_IMAGE);
        intent.putExtra("android.intent.extra.sizeLimit", 1048576);
        startActivityForResult(intent, 2325);
    }

    private File createMediaFile(String end) throws IOException {
        boolean isSDcardExist = Environment.getExternalStorageState().equals("mounted");
        if (!isSDcardExist) {
            return null;
        }
        File mediaStorageDir = new File(CachePzthConfigs.getIns().getFilesDir(false, "other"), "MyApp");
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            KLog.d(this.TAG, "failed to create directory");
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "VID_" + timeStamp;
        String str = mediaStorageDir + File.separator + imageFileName + end;
        this.path = str;
        File mediaFile = new File(str);
        return mediaFile;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void take() {
        this.mPermissionList.clear();
        int i = 0;
        while (true) {
            String[] strArr = this.video_permissions;
            if (i >= strArr.length) {
                break;
            }
            if (ContextCompat.checkSelfPermission(this, strArr[i]) != 0) {
                this.mPermissionList.add(this.video_permissions[i]);
            }
            i++;
        }
        if (this.mPermissionList.size() > 0) {
            Context context = this.mContext;
            if (context instanceof BasicActivity) {
                super.showPermissionNotificationPop(2);
            }
            ActivityCompat.requestPermissions(this, this.video_permissions, 101);
            return;
        }
        readAndwrite();
    }

    private void readAndwrite() {
        this.mPermissionList.clear();
        int i = 0;
        while (true) {
            String[] strArr = this.permissions;
            if (i >= strArr.length) {
                break;
            }
            if (ContextCompat.checkSelfPermission(this, strArr[i]) != 0) {
                this.mPermissionList.add(this.permissions[i]);
            }
            i++;
        }
        if (this.mPermissionList.size() > 0) {
            Context context = this.mContext;
            if (context instanceof BasicActivity) {
                super.showPermissionNotificationPop(1);
            }
            ActivityCompat.requestPermissions(this, this.permissions, 102);
            return;
        }
        takeNew();
    }

    private void showChooserDialog() {
        this.timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.imageUri = MyPhotoUtils.getOutputMediaFileUri(getApplicationContext(), this.fileCache, this.timeStamp, this.mediaFile);
        if (this.bottomAnimDialog == null) {
            BottomAnimDialog bottomAnimDialog = new BottomAnimDialog(this.mContext, "相机", "文件", "取消");
            this.bottomAnimDialog = bottomAnimDialog;
            bottomAnimDialog.setCanceledOnTouchOutside(false);
            this.bottomAnimDialog.setClickListener(new BottomAnimDialog.BottonAnimDialogListener() { // from class: com.shineyue.pm.web.WebActivity.22
                @Override // com.shineyue.pm.dialog.BottomAnimDialog.BottonAnimDialogListener
                public void onItem1Listener() {
                    Intent openCameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                    openCameraIntent.putExtra("output", WebActivity.this.imageUri);
                    WebActivity.this.startActivityForResult(openCameraIntent, 1);
                    WebActivity.this.bottomAnimDialog.dismiss();
                }

                @Override // com.shineyue.pm.dialog.BottomAnimDialog.BottonAnimDialogListener
                public void onItem2Listener() {
                    Intent intentWenJian = new Intent("android.intent.action.OPEN_DOCUMENT");
                    intentWenJian.setType(MediaType.ALL_VALUE);
                    intentWenJian.addCategory("android.intent.category.OPENABLE");
                    intentWenJian.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
                    WebActivity.this.startActivityForResult(intentWenJian, 1);
                    WebActivity.this.bottomAnimDialog.dismiss();
                }

                @Override // com.shineyue.pm.dialog.BottomAnimDialog.BottonAnimDialogListener
                public void onItem3Listener() {
                    WebActivity.this.mUploadCallbackAboveL.onReceiveValue(null);
                    WebActivity.this.mUploadCallbackAboveL = null;
                    WebActivity.this.bottomAnimDialog.dismiss();
                }
            });
        }
        this.bottomAnimDialog.show();
    }

    private void takeNew() {
        showChooserDialog();
    }

    private void showDialog(String title, String secondTitle, String buttonString) {
        final PromptsDialog promptsDialog = new PromptsDialog(this.mContext);
        promptsDialog.show();
        promptsDialog.setTitle(title);
        promptsDialog.setContent(secondTitle);
        promptsDialog.setSureAndCancle(buttonString, "");
        promptsDialog.setCancleShow(false);
        promptsDialog.setOnDialogOnclickListener(new PromptsDialog.dialogOnclickListener() { // from class: com.shineyue.pm.web.WebActivity.23
            @Override // com.shineyue.pm.modle_email.dialog.PromptsDialog.dialogOnclickListener
            public void onClick(View v) {
                int id = v.getId();
                if (id == R.id.tv_cancle || id == R.id.tv_sure) {
                    promptsDialog.dismiss();
                }
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:288:0x02a4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:304:? A[Catch: Exception -> 0x02b2, SYNTHETIC, TRY_LEAVE, TryCatch #10 {Exception -> 0x02b2, blocks: (B:68:0x0182, B:71:0x01dc, B:98:0x0218, B:75:0x01e3, B:103:0x02a4, B:110:0x02b1, B:107:0x02ab, B:82:0x01fb, B:86:0x0202, B:91:0x020c, B:95:0x0213), top: B:286:0x0182, inners: #8, #11, #13, #15 }] */
    /* JADX WARN: Type inference failed for: r0v115 */
    /* JADX WARN: Type inference failed for: r13v14 */
    /* JADX WARN: Type inference failed for: r13v15, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r13v17 */
    /* JADX WARN: Type inference failed for: r13v18 */
    /* JADX WARN: Type inference failed for: r13v19 */
    /* JADX WARN: Type inference failed for: r13v21, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r13v22, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r13v23 */
    /* JADX WARN: Type inference failed for: r14v4 */
    /* JADX WARN: Type inference failed for: r15v4 */
    /* JADX WARN: Type inference failed for: r18v0 */
    /* JADX WARN: Type inference failed for: r19v0 */
    /* JADX WARN: Type inference failed for: r1v100 */
    /* JADX WARN: Type inference failed for: r1v101 */
    /* JADX WARN: Type inference failed for: r1v102 */
    /* JADX WARN: Type inference failed for: r1v103 */
    /* JADX WARN: Type inference failed for: r1v104 */
    /* JADX WARN: Type inference failed for: r1v105 */
    /* JADX WARN: Type inference failed for: r1v106 */
    /* JADX WARN: Type inference failed for: r1v107 */
    /* JADX WARN: Type inference failed for: r1v108 */
    /* JADX WARN: Type inference failed for: r1v109 */
    /* JADX WARN: Type inference failed for: r1v110 */
    /* JADX WARN: Type inference failed for: r1v111 */
    /* JADX WARN: Type inference failed for: r1v112 */
    /* JADX WARN: Type inference failed for: r1v38, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r1v39 */
    /* JADX WARN: Type inference failed for: r1v40 */
    /* JADX WARN: Type inference failed for: r1v42 */
    /* JADX WARN: Type inference failed for: r1v43, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r1v44, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r1v45 */
    /* JADX WARN: Type inference failed for: r1v46 */
    /* JADX WARN: Type inference failed for: r1v51, types: [com.shineyue.pm.web.WebActivity] */
    /* JADX WARN: Type inference failed for: r1v52, types: [java.io.FileOutputStream, java.io.OutputStream] */
    /* JADX WARN: Type inference failed for: r1v98 */
    /* JADX WARN: Type inference failed for: r1v99 */
    /* JADX WARN: Type inference failed for: r21v0 */
    /* JADX WARN: Type inference failed for: r24v0 */
    /* JADX WARN: Type inference failed for: r29v0, types: [android.content.Context, com.shineyue.pm.BaseTimeWebActivity, com.shineyue.pm.web.WebActivity] */
    /* JADX WARN: Type inference failed for: r4v1 */
    /* JADX WARN: Type inference failed for: r4v17 */
    /* JADX WARN: Type inference failed for: r4v18 */
    /* JADX WARN: Type inference failed for: r4v19 */
    /* JADX WARN: Type inference failed for: r4v20 */
    /* JADX WARN: Type inference failed for: r4v21, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r4v22 */
    /* JADX WARN: Type inference failed for: r4v39 */
    /* JADX WARN: Type inference failed for: r4v40 */
    /* JADX WARN: Type inference failed for: r4v41 */
    /* JADX WARN: Type inference failed for: r5v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r5v10 */
    /* JADX WARN: Type inference failed for: r5v11, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r5v20 */
    /* JADX WARN: Type inference failed for: r5v21 */
    /* JADX WARN: Type inference failed for: r5v22 */
    /* JADX WARN: Type inference failed for: r5v23 */
    /* JADX WARN: Type inference failed for: r5v7 */
    /* JADX WARN: Type inference failed for: r5v8 */
    /* JADX WARN: Type inference failed for: r5v9 */
    /* JADX WARN: Type inference failed for: r6v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r6v15 */
    /* JADX WARN: Type inference failed for: r6v16 */
    /* JADX WARN: Type inference failed for: r6v17 */
    /* JADX WARN: Type inference failed for: r6v3 */
    /* JADX WARN: Type inference failed for: r6v4 */
    /* JADX WARN: Type inference failed for: r6v5 */
    /* JADX WARN: Type inference failed for: r6v6 */
    /* JADX WARN: Type inference failed for: r6v7, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r6v8 */
    /* JADX WARN: Type inference failed for: r7v6 */
    /* JADX WARN: Type inference failed for: r7v7 */
    /* JADX WARN: Type inference failed for: r7v8, types: [android.graphics.Bitmap] */
    /* JADX WARN: Type inference failed for: r8v17 */
    /* JADX WARN: Type inference failed for: r8v18 */
    /* JADX WARN: Type inference failed for: r8v19 */
    /* JADX WARN: Type inference failed for: r8v20 */
    /* JADX WARN: Type inference failed for: r8v5 */
    /* JADX WARN: Type inference failed for: r8v6 */
    /* JADX WARN: Type inference failed for: r8v7 */
    /* JADX WARN: Type inference failed for: r8v8 */
    /* JADX WARN: Type inference failed for: r8v9, types: [android.graphics.Bitmap] */
    /* JADX WARN: Type inference failed for: r9v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r9v10 */
    /* JADX WARN: Type inference failed for: r9v11, types: [int] */
    /* JADX WARN: Type inference failed for: r9v16 */
    /* JADX WARN: Type inference failed for: r9v17 */
    /* JADX WARN: Type inference failed for: r9v18 */
    /* JADX WARN: Type inference failed for: r9v19 */
    /* JADX WARN: Type inference failed for: r9v20 */
    /* JADX WARN: Type inference failed for: r9v21 */
    /* JADX WARN: Type inference failed for: r9v22 */
    /* JADX WARN: Type inference failed for: r9v7 */
    /* JADX WARN: Type inference failed for: r9v8 */
    /* JADX WARN: Type inference failed for: r9v9 */
    /* JADX WARN: Type update failed for variable: r0v122 ??, new type: java.io.FileOutputStream
    jadx.core.utils.exceptions.JadxOverflowException: Type inference error: updates count limit reached with updateSeq = 21591. Try increasing type updates limit count.
    	at jadx.core.dex.visitors.typeinference.TypeUpdateInfo.requestUpdate(TypeUpdateInfo.java:37)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:224)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
     */
    /* JADX WARN: Type update failed for variable: r1v52 ??, new type: java.io.FileOutputStream
    jadx.core.utils.exceptions.JadxOverflowException: Type inference error: updates count limit reached with updateSeq = 21591. Try increasing type updates limit count.
    	at jadx.core.dex.visitors.typeinference.TypeUpdateInfo.requestUpdate(TypeUpdateInfo.java:37)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:224)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:480)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:197)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:454)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:119)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.allSameListener(TypeUpdate.java:473)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:241)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:225)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:202)
     */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:112:0x02b3 -> B:113:0x02ba). Please report as a decompilation issue!!! */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void onActivityResult(int r30, int r31, android.content.Intent r32) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 2159
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shineyue.pm.web.WebActivity.onActivityResult(int, int, android.content.Intent):void");
    }

    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        ValueCallback<Uri[]> valueCallback;
        KLog.e("result", data + "");
        KLog.e("requestCode", Integer.valueOf(requestCode));
        KLog.e("resultCode", Integer.valueOf(resultCode));
        if (requestCode != 1 || (valueCallback = this.mUploadCallbackAboveL) == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == -1) {
            if (data == null) {
                results = new Uri[]{this.imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
            if (results != null) {
                KLog.i(this.TAG, Integer.valueOf(results.length));
                this.mUploadCallbackAboveL.onReceiveValue(results);
                this.mUploadCallbackAboveL = null;
                return;
            } else {
                Uri[] results2 = {this.imageUri};
                KLog.i(this.TAG, results2.toString());
                this.mUploadCallbackAboveL.onReceiveValue(results2);
                this.mUploadCallbackAboveL = null;
                return;
            }
        }
        valueCallback.onReceiveValue(null);
        this.mUploadCallbackAboveL = null;
    }

    private String values(String s) {
        return s == null ? "" : s;
    }

    public void addActivity(Activity activity) {
        String str = this.TAG;
        Object[] objArr = new Object[1];
        StringBuilder sb = new StringBuilder();
        sb.append("addWebActivity===>");
        sb.append(activity);
        objArr[0] = sb.toString() != null ? activity.toString() : "";
        KLog.i(str, objArr);
        if (this.webactivityStack == null) {
            this.webactivityStack = new Stack<>();
        }
        this.webactivityStack.add(activity);
    }

    public void removeAllActivity() {
        Stack<Activity> stack = this.webactivityStack;
        if (stack != null && stack.size() > 0) {
            Stack<Activity> activityTemp = new Stack<>();
            for (Activity activity : this.webactivityStack) {
                if (activity != null) {
                    activityTemp.add(activity);
                    activity.finish();
                }
            }
            this.webactivityStack.removeAll(activityTemp);
        }
        KLog.i(this.TAG, "removeAllWebActivity===>removeAllWebActivity");
        System.gc();
    }

    public void refreshSignSessionEvery30min(Context context) {
        String lastTimeSaved = Utils.getMsg(context, this.PERF_KEY_REFRESH_SIGN_SESSION_TIME);
        long lastTime = 0;
        try {
            lastTime = Long.parseLong(lastTimeSaved);
        } catch (Exception e) {
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastTime >= 1800000) {
            refreshSignSession(context);
            Utils.saveMsg(context, this.PERF_KEY_REFRESH_SIGN_SESSION_TIME, String.valueOf(currentTimeMillis));
        }
    }

    private void refreshSignSession(Context context) {
        new android.webkit.WebView(context).clearCache(true);
        clearCookies(context);
    }

    public void clearCookies(Context context) {
        if (Build.VERSION.SDK_INT >= 22) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            clearCookieOld(context);
        }
    }

    private void clearCookieOld(Context context) {
        CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
        cookieSyncMngr.startSync();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.removeSessionCookie();
        cookieSyncMngr.stopSync();
        cookieSyncMngr.sync();
    }

    @Override // com.shineyue.pm.aliyun_rtc.contract.AliRtcLoginContract.view
    public void showAuthInfo(RTCAuthInfo rtcAuthInfo) {
        if (Utils.haveValue(this.mEtChannelId)) {
            Intent intent = new Intent(this.mContext, (Class<?>) AliRtcChatActivity.class);
            Bundle b = new Bundle();
            String mUserName = Utils.getMsg(this.mContext, c.e);
            b.putString("username", mUserName);
            String channel = this.mEtChannelId;
            b.putString("channel", channel);
            b.putSerializable("rtcAuthInfo", rtcAuthInfo);
            intent.putExtras(b);
            startActivity(intent);
            finish();
        }
    }

    @Override // com.shineyue.pm.aliyun_rtc.contract.AliRtcLoginContract.view
    public void showProgressDialog(boolean isShow) {
        ProgressDialog progressDialog;
        ProgressDialog progressDialog2;
        if (isShow && (progressDialog2 = this.mProgressDialog) != null && !progressDialog2.isShowing()) {
            this.mProgressDialog.show();
            return;
        }
        if (isShow && this.mProgressDialog == null) {
            ProgressDialog progressDialog3 = new ProgressDialog(this);
            this.mProgressDialog = progressDialog3;
            progressDialog3.setCanceledOnTouchOutside(false);
            this.mProgressDialog.setCancelable(false);
            this.mProgressDialog.setMessage("登陆中...");
            this.mProgressDialog.show();
            return;
        }
        if (!isShow && (progressDialog = this.mProgressDialog) != null) {
            progressDialog.dismiss();
        }
    }

    @Override // com.shineyue.pm.BasicActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        AliRtcEngine aliRtcEngine = this.mAliRtcEngine;
        if (aliRtcEngine != null) {
            aliRtcEngine.leaveChannel();
            this.mAliRtcEngine = null;
        }
        if (this.myHandler != null) {
            KLog.i(this.TAG, "ttt");
            this.myHandler.removeCallbacksAndMessages(null);
            this.myHandler = null;
        }
        if (this.mHandler != null) {
            KLog.i(this.TAG, "tttt");
            this.mHandler.removeCallbacksAndMessages(null);
            this.mHandler = null;
        }
        AMapLocationClient aMapLocationClient = this.mapLocationClient;
        if (aMapLocationClient != null) {
            aMapLocationClient.stopLocation();
            this.mapLocationClient.onDestroy();
            this.mapLocationClient = null;
        }
        AMapLocationClient aMapLocationClient2 = this.mlocationClient;
        if (aMapLocationClient2 != null) {
            aMapLocationClient2.stopLocation();
            this.mlocationClient.onDestroy();
            this.mlocationClient = null;
        }
        AliRtcLoginPresenter aliRtcLoginPresenter = this.mLoginPresenter;
        if (aliRtcLoginPresenter != null) {
            aliRtcLoginPresenter.detachView();
        }
        if (!this.webactivityStack.isEmpty()) {
            this.webactivityStack.remove(this);
        }
        AMapLocationClient aMapLocationClient3 = this.mlocationClient;
        if (aMapLocationClient3 != null) {
            aMapLocationClient3.onDestroy();
        }
        DWebView dWebView = this.webView;
        if (dWebView != null) {
            this.fl_container.removeView(dWebView);
            this.webView.getSettings().setJavaScriptEnabled(false);
            this.webView.setWebChromeClient(null);
            this.webView.setWebViewClient(null);
            this.webView.loadUrl("about:blank");
            this.webView.onPause();
            this.webView.removeAllViews();
            this.webView.destroy();
            this.webView = null;
        }
        RelativeLayout relativeLayout = this.ll;
        if (relativeLayout != null) {
            relativeLayout.removeAllViews();
            this.ll = null;
        }
        try {
            ServiceConnection serviceConnection = this.serviceConnection;
            if (serviceConnection != null) {
                unbindService(serviceConnection);
                this.serviceConnection = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TTSPlayerManager tTSPlayerManager = this.ttsManager;
        if (tTSPlayerManager != null) {
            tTSPlayerManager.onDestory();
        }
        TTSService.LocalBinder localBinder = this.localBinder;
        if (localBinder != null) {
            localBinder.destroy();
            this.localBinder = null;
        }
        if (this.playerService != null) {
            this.playerService = null;
        }
        if (this.chatPrepare) {
            String msg = this.editEmojicon.getText().toString().trim();
            if (OaApplication.JIQIRENID.equals(this.toUser)) {
                EventBus.getDefault().post(new Notify.Message(Notify.SAVE_JIQIREN_DRAFT, msg));
            } else {
                if (this.sfjy) {
                    msg = "";
                }
                if (Utils.haveValue(msg)) {
                    final String finalMsg = msg;
                    this.chatWindowListPresenter.getChatWindowFromDb(this.toUser, new ChatWindowListDaoManager.OnChatWindowItemResultListener() { // from class: com.shineyue.pm.web.WebActivity.27
                        @Override // com.shineyue.pm.modle_chat.presenter.chat_window_list.ChatWindowListDaoManager.OnChatWindowItemResultListener
                        public void onChatWindowItemResult(ChatWindowItem chatWindowItem) {
                            if (chatWindowItem != null) {
                                Message m = chatWindowItem.getContent().content;
                                m.draft = finalMsg;
                                EventBus.getDefault().post(new Notify.Message(Notify.ChatMsg3, m));
                                WebActivity.this.chatWindowListPresenter.updateDbInfoByMessageNotDraft(m, null);
                            }
                        }
                    });
                } else {
                    this.chatWindowListPresenter.getChatWindowFromDb(this.toUser, new ChatWindowListDaoManager.OnChatWindowItemResultListener() { // from class: com.shineyue.pm.web.WebActivity.28
                        @Override // com.shineyue.pm.modle_chat.presenter.chat_window_list.ChatWindowListDaoManager.OnChatWindowItemResultListener
                        public void onChatWindowItemResult(ChatWindowItem chatWindowItem) {
                            if (chatWindowItem != null) {
                                Message m = chatWindowItem.getContent().content;
                                m.draft = "";
                                try {
                                    Message message = (Message) WebActivity.this.list.get(WebActivity.this.list.size() - 1);
                                    if (message != null && !TextUtils.isEmpty(message.uqIdentNo)) {
                                        if (!message.uqIdentNo.equals(m.uqIdentNo)) {
                                            m = message;
                                        }
                                    }
                                } catch (Exception e2) {
                                    e2.toString();
                                }
                                EventBus.getDefault().post(new Notify.Message(Notify.ChatMsg3, m));
                                WebActivity.this.chatWindowListPresenter.updateDbInfoByMessageNotDraft(m, null);
                            }
                        }
                    });
                }
            }
        }
        EventBus.getDefault().unregister(this);
        ThreadPoolManager.getInstance().remove(this.saveImageRunnable);
        ThreadPoolManager.getInstance().remove(this.saveBitmapRunnable);
        if (this.mContext != null) {
            this.mContext = null;
        }
        super.onDestroy();
    }

    /* JADX WARN: Removed duplicated region for block: B:129:0x03d5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onEventMainThread(com.shineyue.pm.scancod.notify.Notify.Message r15) {
        /*
            Method dump skipped, instruction units count: 2191
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shineyue.pm.web.WebActivity.onEventMainThread(com.shineyue.pm.scancod.notify.Notify$Message):void");
    }

    @JavascriptInterface
    public void applicationBecomeActive() {
        KLog.i(this.TAG, "applicationBecomeActive");
        this.applicationBecomeActive = true;
    }

    @JavascriptInterface
    public void changeScreenbrightness() {
        KLog.i(this.TAG, "changeScreenbrightness");
        ContentResolver contentResolver = this.mContext.getContentResolver();
        int b = BrightnessController.getBrightness(contentResolver);
        KLog.i(this.TAG, Integer.valueOf(b));
        int i = this.brightness;
        if (b < i) {
            this.changeScreenbrightness = true;
            this.oldBrightness = i;
            runOnUiThread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.30
                @Override // java.lang.Runnable
                public void run() {
                    Window window = WebActivity.this.getWindow();
                    WindowManager.LayoutParams lp = window.getAttributes();
                    lp.screenBrightness = WebActivity.this.brightness / 255.0f;
                    window.setAttributes(lp);
                }
            });
        }
    }

    private void setUpSplash() {
        Subscription splash = Observable.timer(2000L, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1() { // from class: com.shineyue.pm.web.-$$Lambda$WebActivity$4dTjfuMyGDWwzI1jZhzD9oZjAhQ
            @Override // rx.functions.Action1
            public final void call(Object obj) {
                this.f$0.lambda$setUpSplash$0$WebActivity((Long) obj);
            }
        });
        this.mCompositeSubscription.add(splash);
    }

    public /* synthetic */ void lambda$setUpSplash$0$WebActivity(Long aLong) {
        requestPermission();
    }

    private void requestPermission() {
        PermissionUtils.requestMultiPermissions(this, new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.RECORD_AUDIO", "android.permission.READ_EXTERNAL_STORAGE"}, this.mGrant);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) throws XmlPullParserException, IOException {
        if (requestCode == 100) {
            PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, this.mGrant);
        } else if (requestCode == 101) {
            boolean hasPermissionDismiss = false;
            for (int i : grantResults) {
                if (i == -1) {
                    hasPermissionDismiss = true;
                }
            }
            boolean show = NotificationManagerCompat.from(this).areNotificationsEnabled();
            KLog.i(this.TAG, Boolean.valueOf(show));
            if (hasPermissionDismiss) {
                PermissionsUtils.getInstance().showPermissionDialog(this.mContext, 2);
            } else {
                super.hidePermissionNotificationPop();
                readAndwrite();
            }
        } else if (requestCode == 102) {
            boolean hasPermissionDismiss2 = false;
            for (int i2 : grantResults) {
                if (i2 == -1) {
                    hasPermissionDismiss2 = true;
                }
            }
            boolean show2 = NotificationManagerCompat.from(this).areNotificationsEnabled();
            KLog.i(this.TAG, Boolean.valueOf(show2));
            if (hasPermissionDismiss2) {
                PermissionsUtils.getInstance().showPermissionDialog(this.mContext, 1);
            } else {
                super.hidePermissionNotificationPop();
                takeNew();
            }
        } else if (requestCode == 1) {
            if (grantResults.length > 0) {
                boolean isflag = false;
                int i3 = 0;
                while (true) {
                    if (i3 >= grantResults.length) {
                        break;
                    }
                    if (grantResults[i3] == 0) {
                        i3++;
                    } else {
                        isflag = true;
                        break;
                    }
                }
                if (isflag) {
                    PermissionsUtils.getInstance().showPermissionDialog(this.mContext, 2);
                } else {
                    Intent openCameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                    Uri imageUri = MyPhotoUtils.getOutputMediaFileUri(getApplicationContext(), this.fileCache, this.timeStamp, this.mediaFile);
                    openCameraIntent.putExtra("output", imageUri);
                    startActivityForResult(openCameraIntent, 520);
                }
            } else {
                PermissionsUtils.getInstance().showPermissionDialog(this.mContext, 2);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        if (requestCode == PermissionsUtils.getInstance().mRequestCodeWebLocation) {
            if (permissions != null && permissions.length > 0) {
                PermissionsUtils.getInstance().onRequestPermissionsResultWebLocation((Activity) this.mContext, requestCode, permissions, grantResults);
                return;
            }
            return;
        }
        PermissionsUtils.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void UserLogin(String cellphone, String password) {
        Utils.outLoginClear(this.mContext);
        OkGoNetManager.AppLogin(this.mContext, cellphone, password, new Callback<Result<OkGoNetManager.JSONObjectResult>>() { // from class: com.shineyue.pm.web.WebActivity.32
            @Override // com.shineyue.pm.utils.async.Callback
            public void call(Result<OkGoNetManager.JSONObjectResult> value) {
                Utils.dismissProgressDialog();
                KLog.i("changeUser", Boolean.valueOf(value.succeed()));
                if (!value.succeed()) {
                    ToastUtil.toast(WebActivity.this.mContext, "msg", value.cause(), ToastType.FAIL);
                    return;
                }
                try {
                    JSONObject result = value.value().jsonObject;
                    KLog.d("changeUser", result);
                    if (!result.isNull("data")) {
                        JSONObject results = result.getJSONObject("data");
                        String msg = results.get("msg").toString();
                        String ret = results.get("ret").toString();
                        if (ret.equals("99")) {
                            ToastUtil.toast(WebActivity.this.mContext, "msg", msg, ToastType.FAIL);
                            return;
                        }
                        Context context = WebActivity.this.mContext;
                        String ret2 = results.toString();
                        Utils.saveMsg(context, "UserXx", ret2);
                        Utils.saveMsg(WebActivity.this.mContext, "userid", results.get("userid").toString());
                        Utils.saveMsg(WebActivity.this.mContext, "tokenid", results.get("tokenid").toString());
                        Utils.saveMsg(WebActivity.this.mContext, "sjlx", "android");
                        Utils.saveMsg(WebActivity.this.mContext, "khbh", results.get("grbh").toString());
                        Utils.saveMsg(WebActivity.this.mContext, "zxjgbm", results.get("zxjgbm").toString());
                        Utils.saveMsg(WebActivity.this.mContext, "jgbm", results.get("jgbm").toString());
                        Utils.saveMsg(WebActivity.this.mContext, "deptid", results.get("deptid").toString());
                        Utils.saveMsg(WebActivity.this.mContext, c.e, results.get("username").toString());
                        Utils.saveMsg(WebActivity.this.mContext, "zjhm", results.get("zjhm").toString());
                        Utils.saveMsg(WebActivity.this.mContext, "sjhm", results.get(c.e).toString());
                        Utils.saveMsg(WebActivity.this.mContext, "isconnect", results.get("isconnect").toString());
                        if (!results.has("imageurl")) {
                            Utils.saveMsg(WebActivity.this.mContext, "loadimgurl", "http://www.gjj12329.cn:6008/icon/logo/" + results.get("zxjgbm").toString() + ".png");
                        } else {
                            Utils.saveMsg(WebActivity.this.mContext, "loadimgurl", results.get("imageurl").toString());
                            if (!Utils.haveValue(Utils.getMsg(WebActivity.this.mContext, "loadimgurl"))) {
                                Utils.saveMsg(WebActivity.this.mContext, "loadimgurl", "http://www.gjj12329.cn:6008/icon/logo/" + results.get("zxjgbm").toString() + ".png");
                            }
                        }
                        if (Utils.haveValue(results.get("qycode").toString())) {
                            Utils.saveMsg(WebActivity.this.mContext, "qycode", results.get("qycode").toString());
                        } else {
                            Utils.saveMsg(WebActivity.this.mContext, "qycode", "shineyue");
                        }
                        if (results.get("blqd").toString() == null || results.get("blqd").toString().equals("")) {
                            Utils.saveMsg(WebActivity.this.mContext, "blqd", "app_02");
                        } else {
                            Utils.saveMsg(WebActivity.this.mContext, "blqd", results.get("blqd").toString());
                        }
                        WebActivity.this.AddJPushGroup(results.get("zxjgbm").toString(), results.get("grbh").toString());
                        String sec = Utils.getMsg(WebActivity.this.mContext, "secence");
                        if (Utils.haveValue(sec)) {
                            WebActivity.this.secence = Integer.parseInt(sec) + 1;
                        }
                        JPushInterface.setAlias(WebActivity.this.mContext, WebActivity.this.secence, results.get("userid").toString());
                        String isconnect = results.get("isconnect").toString();
                        if (isconnect.equals("1")) {
                            WebActivity.this.UserInfoByZjhm();
                            return;
                        }
                        ToastUtil.toast(WebActivity.this.mContext, "msg", msg, ToastType.SUCCESS);
                        Utils.saveMsg(WebActivity.this.mContext, "isLogin", StreamerConstants.TRUE);
                        EventBus.getDefault().post(new Notify.Message(1005));
                        Intent intent = new Intent(WebActivity.this.mContext, (Class<?>) MainActivity.class);
                        if (Utils.haveValue(WebActivity.this.type)) {
                            intent.putExtra("type", WebActivity.this.type);
                        } else {
                            intent.putExtra("type", "FragmentMessage");
                        }
                        WebActivity.this.mContext.startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void AddJPushGroup(String zxbm, String grbh) {
        Set<String> tags = new HashSet<>();
        tags.add(zxbm);
        JPushInterface.setAliasAndTags(this.mContext, grbh, tags, new TagAliasCallback() { // from class: com.shineyue.pm.web.WebActivity.33
            @Override // cn.jpush.android.api.TagAliasCallback
            public void gotResult(int responseCode, String alias, Set<String> tags2) {
                if (responseCode == 0) {
                    KLog.i("tags", tags2.toString());
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void UserInfoByZjhm() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("zxbm", Utils.getMsg(OaApplication.application, "zxjgbm"));
        map.put("jgbm", Utils.getMsg(OaApplication.application, "jgbm"));
        map.put("ywbm", "0199");
        map.put("userid", Utils.getMsg(OaApplication.application, "userid"));
        map.put("zjhm", Utils.getMsg(OaApplication.application, "zjhm"));
        map.put("zjbzxbm", Utils.getMsg(OaApplication.application, "qycode"));
        JSONObject jsonObject = new JSONObject(map);
        KLog.json("changeUser", jsonObject.toString());
        OkGoNetManager.getInstance();
        KLog.e("changeUser", OkGoNetManager.APP_URL + OkGoNetManager.USERINFOBYZJHM);
        ((PostRequest) OkGo.post(OkGoNetManager.APP_URL + OkGoNetManager.USERINFOBYZJHM).tag(this)).upJson(jsonObject.toString()).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.34
            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onSuccess(String s, Call call, Response response) {
                Utils.dismissProgressDialog();
                KLog.i("changeUser", s);
                try {
                    JSONObject result = new JSONObject(s);
                    JSONArray data = result.getJSONArray("data");
                    JSONObject success = data.getJSONObject(0);
                    Utils.saveMsg(WebActivity.this.mContext, "JGUserXx", success.toString());
                    Utils.saveMsg(WebActivity.this.mContext, "jgjgbm", success.get("jgbm").toString());
                    Utils.saveMsg(WebActivity.this.mContext, "jgzjhm", success.get("zjhm").toString());
                    Utils.saveMsg(WebActivity.this.mContext, "jgzxjgbm", success.get("zxjgbm").toString());
                    Utils.saveMsg(WebActivity.this.mContext, "jgusername", success.get("username").toString());
                    Utils.saveMsg(WebActivity.this.mContext, "jguserid", success.get("userid").toString());
                    Utils.saveMsg(WebActivity.this.mContext, "jggrbh", success.get("grbh").toString());
                    Utils.saveMsg(WebActivity.this.mContext, "jgdeptid", success.get("deptid").toString());
                    Utils.saveMsg(WebActivity.this.mContext, "jgdeptname", success.get("deptname").toString());
                    Utils.saveMsg(WebActivity.this.mContext, "jgjgmc", success.get("jgmc").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Utils.saveMsg(WebActivity.this.mContext, "isLogin", StreamerConstants.TRUE);
                EventBus.getDefault().post(new Notify.Message(1005));
                Intent intent = new Intent(WebActivity.this.mContext, (Class<?>) MainActivity.class);
                if (Utils.haveValue(WebActivity.this.type)) {
                    intent.putExtra("type", WebActivity.this.type);
                } else {
                    intent.putExtra("type", "FragmentMessage");
                }
                WebActivity.this.mContext.startActivity(intent);
            }

            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                KLog.i(e.toString());
                ToastUtil.toast(WebActivity.this.mContext, "error", "", ToastType.FAIL);
            }
        });
    }

    @Override // com.shineyue.pm.BasicActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.isOpenSideslip) {
            this.webView.loadUrl("javascript:onSideslipListener()");
        } else if (this.url.contains("/csgjj") || this.url.contains("/gjj_cs")) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private void initLocation() {
        KLog.i(this.TAG, NetUtils.checkWifi(this.mContext));
        if (NetUtils.checkWifi(this.mContext).intValue() == 3) {
            noNetShow(true);
            return;
        }
        noNetShow(false);
        if (this.isPrimaryLocalization) {
            if (this.gps != null) {
                this.gps = null;
            }
            this.gps = new GPS(this, this.locationCallBack);
            return;
        }
        try {
            int x = (int) (Math.random() * 100.0d);
            int y = (int) (Math.random() * 100.0d);
            KLog.i(Integer.valueOf(x));
            KLog.i(Integer.valueOf(y));
            String str = "{\"data\":{\"latitude\":38.0509" + x + ",\"longitude\":114.3477" + y + ",\"address\":\"河北省石家庄市鹿泉区御园路99号靠近金盛达科研生产楼\",\"province\":\"河北省\",\"city\":\"石家庄市\",\"district\":\"鹿泉区\",\"street\":\"御园路\"},\"errorCode\":0,\"cityCode\":\"0311\",\"provinceCode\":\"130000\",\"adCode\":\"130110\",\"provinceReferred\":\"冀\",\"msg\":\"定位成功\"}";
            KLog.i(str);
            String location = Utils.getMsg(this.mContext, PermissionConstants.LOCATION);
            if (this.isTicketLocation) {
                JSONObject js = new JSONObject(this.user_info);
                try {
                    js.put("locationMsg", location);
                    if (!TextUtils.isEmpty(this.intent.getStringExtra("flowtype"))) {
                        js.put("flowtype", this.intent.getStringExtra("flowtype"));
                    }
                    this.user_info = js.toString();
                } catch (Exception e) {
                    KLog.e(this.TAG, e.getMessage());
                }
                getTicket(this.user_info, this.cpbs);
                this.isTicketLocation = false;
                return;
            }
            execJsMethod("javascript:locationResult(" + location + ")");
        } catch (Exception e2) {
            KLog.i(this.TAG, e2.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initLocationNew() {
        KLog.i(this.TAG, NetUtils.checkWifi(this.mContext));
        if (NetUtils.checkWifi(this.mContext).intValue() == 3) {
            noNetShow(true);
            return;
        }
        noNetShow(false);
        boolean isLocationServiceEnabled = LocationUtils.isLocationServiceEnabled(this.mContext);
        if (!isLocationServiceEnabled) {
            if (!this.isShowLocationEnable) {
                ToastUtil.toast(this.mContext, "msg", "请开启位置信息", ToastType.FAIL);
                this.isShowLocationEnable = true;
            }
            try {
                JSONObject js = new JSONObject(this.user_info);
                js.put("locationMsg", setErrorLocationMsg());
                this.user_info = js.toString();
            } catch (Exception e) {
                e.toString();
            }
            execJsMethod("javascript:locationResult(" + setErrorLocationMsg() + ")");
            return;
        }
        if (this.isPrimaryLocalization) {
            if (this.gps != null) {
                this.gps = null;
            }
            this.gps = new GPS(this, this.locationCallBack);
            return;
        }
        KLog.i(this.TAG, NetUtils.checkWifi(this.mContext));
        KLog.i(this.TAG, this.mLocationOption);
        if (this.mLocationOption == null) {
            this.mLocationOption = new AMapLocationClientOption();
            this.mlocationClient.setLocationListener(new AMapLocationListener() { // from class: com.shineyue.pm.web.WebActivity.35
                /* JADX WARN: Removed duplicated region for block: B:19:0x011a A[ADDED_TO_REGION, REMOVE] */
                @Override // com.amap.api.location.AMapLocationListener
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct code enable 'Show inconsistent code' option in preferences
                */
                public void onLocationChanged(com.amap.api.location.AMapLocation r43) {
                    /*
                        Method dump skipped, instruction units count: 1767
                        To view this dump change 'Code comments level' option to 'DEBUG'
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.shineyue.pm.web.WebActivity.AnonymousClass35.onLocationChanged(com.amap.api.location.AMapLocation):void");
                }
            });
            this.mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            this.mLocationOption.setInterval(2000L);
            this.mLocationOption.setOnceLocation(true);
            this.mLocationOption.setNeedAddress(true);
            this.mLocationOption.setOnceLocationLatest(true);
            this.mLocationOption.setMockEnable(false);
            this.mLocationOption.setLocationCacheEnable(false);
            this.mlocationClient.setLocationOption(this.mLocationOption);
        }
        KLog.i(this.TAG, Boolean.valueOf(this.mlocationClient.isStarted()));
        if (this.mlocationClient.isStarted()) {
            this.mlocationClient.startLocation();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String setErrorLocationMsg() {
        JSONObject jsonResult = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("latitude", "");
            jsonObject.put("longitude", "");
            jsonObject.put("address", "获取位置信息失败");
            jsonObject.put(DistrictSearchQuery.KEYWORDS_PROVINCE, "");
            jsonObject.put(DistrictSearchQuery.KEYWORDS_CITY, "");
            jsonObject.put(DistrictSearchQuery.KEYWORDS_DISTRICT, "");
            jsonObject.put("street", "");
            jsonObject.put("cityCode", "");
            jsonObject.put("provinceCode", "");
            jsonObject.put("adCode", "");
            jsonObject.put("provinceReferred", "");
            jsonObject.put("isVirtuallocation", false);
            jsonResult.put("data", jsonObject);
            jsonResult.put(MyLocationStyle.ERROR_CODE, 1);
            jsonResult.put("msg", "定位失败");
            jsonResult.put("cityCode", "");
            jsonResult.put("provinceCode", "");
            jsonResult.put("adCode", "");
            jsonResult.put("provinceReferred", "");
            KLog.d(this.TAG, "location fail" + jsonResult.toString());
            String msg = jsonResult.toString();
            return msg;
        } catch (Exception e) {
            e.toString();
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startLongLocation() {
        boolean isLocationServiceEnabled = LocationUtils.isLocationServiceEnabled(this.mContext);
        KLog.i(this.TAG, Boolean.valueOf(isLocationServiceEnabled));
        if (!isLocationServiceEnabled) {
            if (!this.isShowLocationEnable) {
                ToastUtil.toast(this.mContext, "msg", "请开启位置信息", ToastType.FAIL);
                this.isShowLocationEnable = true;
            }
            try {
                JSONObject js = new JSONObject(this.user_info);
                js.put("locationMsg", setErrorLocationMsg());
                this.user_info = js.toString();
            } catch (Exception e) {
                e.toString();
            }
            execJsMethod("javascript:locationResult(" + setErrorLocationMsg() + ")");
            return;
        }
        KLog.i(this.TAG, NetUtils.checkWifi(this.mContext));
        KLog.i(this.TAG, this.mapLocationClient);
        if (this.mapLocationClient == null) {
            try {
                this.mapLocationClient = new AMapLocationClient(getApplicationContext());
            } catch (Exception e2) {
            }
        }
        if (this.mapLocationClientOption == null) {
            this.mapLocationClientOption = new AMapLocationClientOption();
            this.mapLocationClient.setLocationListener(new AMapLocationListener() { // from class: com.shineyue.pm.web.WebActivity.36
                /* JADX WARN: Removed duplicated region for block: B:19:0x00fe A[ADDED_TO_REGION, REMOVE] */
                /* JADX WARN: Unreachable blocks removed: 2, instructions: 10 */
                @Override // com.amap.api.location.AMapLocationListener
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct code enable 'Show inconsistent code' option in preferences
                */
                public void onLocationChanged(com.amap.api.location.AMapLocation r41) {
                    /*
                        Method dump skipped, instruction units count: 1319
                        To view this dump change 'Code comments level' option to 'DEBUG'
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.shineyue.pm.web.WebActivity.AnonymousClass36.onLocationChanged(com.amap.api.location.AMapLocation):void");
                }
            });
            this.mapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            this.mapLocationClientOption.setInterval(5000L);
            this.mapLocationClientOption.setOnceLocation(false);
            this.mapLocationClientOption.setNeedAddress(true);
            this.mapLocationClientOption.setOnceLocationLatest(true);
            this.mapLocationClientOption.setMockEnable(false);
            this.mapLocationClientOption.setLocationCacheEnable(false);
            this.mapLocationClient.setLocationOption(this.mapLocationClientOption);
        }
        KLog.i(this.TAG, Boolean.valueOf(this.mapLocationClient.isStarted()));
        this.mapLocationClient.startLocation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setErrorLocation(int code, String msg) {
        JSONObject jsonResult = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("latitude", "");
            jsonObject.put("longitude", "");
            jsonObject.put("address", "获取位置信息失败");
            jsonObject.put(DistrictSearchQuery.KEYWORDS_PROVINCE, "");
            jsonObject.put(DistrictSearchQuery.KEYWORDS_CITY, "");
            jsonObject.put(DistrictSearchQuery.KEYWORDS_DISTRICT, "");
            jsonObject.put("street", "");
            jsonObject.put("cityCode", "");
            jsonObject.put("provinceCode", "");
            jsonObject.put("adCode", "");
            jsonObject.put("provinceReferred", "");
            jsonObject.put("isVirtuallocation", false);
            jsonResult.put("data", jsonObject);
            jsonResult.put(MyLocationStyle.ERROR_CODE, code);
            jsonResult.put("msg", msg);
            jsonResult.put("cityCode", "");
            jsonResult.put("provinceCode", "");
            jsonResult.put("adCode", "");
            jsonResult.put("provinceReferred", "");
            KLog.d(this.TAG, "location fail" + jsonResult.toString());
            JSONObject js = new JSONObject(this.user_info);
            try {
                js.put("locationMsg", jsonResult.toString());
                this.user_info = js.toString();
            } catch (Exception e) {
                KLog.e(this.TAG, e.getMessage());
            }
            execJsMethod("javascript:locationResult(" + jsonResult.toString() + ")");
        } catch (Exception e2) {
            e2.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getJianCheng(String code) {
        int i = 0;
        while (true) {
            String[][] strArr = this.jianCheng;
            if (i < strArr.length) {
                if (!code.equals(strArr[i][0])) {
                    i++;
                } else {
                    return this.jianCheng[i][1];
                }
            } else {
                return "";
            }
        }
    }

    @JavascriptInterface
    public void getLocation() {
        KLog.i(this.TAG, "getLocation");
        checkLocationPermissionNew();
    }

    @JavascriptInterface
    public void getUpdatingLocation() {
        KLog.i(this.TAG, "getUpdatingLocation");
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        boolean isHaveRejection = false;
        int i = 0;
        while (true) {
            String[] strArr = this.permissions_location;
            if (i >= strArr.length) {
                break;
            }
            if (ContextCompat.checkSelfPermission(this.mContext, strArr[i]) != 0) {
                isHaveRejection = true;
            }
            i++;
        }
        if (isHaveRejection) {
            if (!PermissionsUtils.getInstance().isShowLocationWeb) {
                showPermissionNotificationPop(0);
                PermissionsUtils.getInstance().chekPermissionsWebLocation(this, this.permissions_location, this.permissionsResult_location);
                return;
            }
            return;
        }
        startLongLocation();
    }

    private void checkLocationPermissionNew() {
        boolean isHaveRejection = false;
        int i = 0;
        while (true) {
            String[] strArr = this.permissions_location;
            if (i >= strArr.length) {
                break;
            }
            if (ContextCompat.checkSelfPermission(this.mContext, strArr[i]) != 0) {
                isHaveRejection = true;
            }
            i++;
        }
        if (isHaveRejection) {
            if (!PermissionsUtils.getInstance().isShowLocationWeb) {
                showPermissionNotificationPop(0);
                PermissionsUtils.getInstance().chekPermissionsWebLocation(this, this.permissions_location, this.permissionsResult_location_new);
                return;
            }
            return;
        }
        initLocationNew();
    }

    @JavascriptInterface
    public void joinVideoChannel(String inParms) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void execJsMethod(final String method) {
        runOnUiThread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.39
            @Override // java.lang.Runnable
            public void run() {
                if (WebActivity.this.webView != null) {
                    WebActivity.this.webView.evaluateJavascript(method, new ValueCallback<String>() { // from class: com.shineyue.pm.web.WebActivity.39.1
                        @Override // android.webkit.ValueCallback
                        public void onReceiveValue(String value) {
                            KLog.i(value);
                            KLog.e("获取到了回调");
                        }
                    });
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void execJsMethod(final String method, final OnParmsCallBack<Boolean> callback) {
        if (this.webView == null) {
            return;
        }
        String jsCheckCode = "(typeof window." + method + " === 'function')";
        if (Build.VERSION.SDK_INT >= 19) {
            this.webView.evaluateJavascript(jsCheckCode, new ValueCallback<String>() { // from class: com.shineyue.pm.web.WebActivity.40
                @Override // android.webkit.ValueCallback
                public void onReceiveValue(String result) {
                    final boolean isExist = StreamerConstants.TRUE.equals(result);
                    KLog.i(WebActivity.this.TAG, "isExist:" + isExist);
                    KLog.i(WebActivity.this.TAG, "result:" + result);
                    WebActivity.this.runOnUiThread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.40.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (callback != null) {
                                callback.onParmsResult(Boolean.valueOf(isExist));
                                return;
                            }
                            if (isExist) {
                                WebActivity.this.execJsMethod("javascript:" + method + "()");
                            }
                        }
                    });
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public WebResourceResponse shouldUrlInterupt(String url) {
        if (url.contains("sxfw-static/dxgl-app/mapvgl/mapvgl.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("mapvgl/mapvgl.min.js"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (url.contains("sxfw-static/dxgl-app/mapvgl/mapvgl.threelayers.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("mapvgl/mapvgl.threelayers.min.js"));
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        WebResourceResponse webResourceResponse = replaceStaticResourcesNew(url);
        if (webResourceResponse != null) {
            return webResourceResponse;
        }
        WebResourceResponse dbWebResourceResponse = replaceDbStaticResourcesNew(url);
        if (dbWebResourceResponse != null) {
            return dbWebResourceResponse;
        }
        WebResourceResponse yjyWebResourceResponse = replaceYjyStaticResourcesNew(url);
        if (yjyWebResourceResponse != null) {
            return yjyWebResourceResponse;
        }
        if (url.contains("lib/fastclick.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("platformjs/fastclick.min.js"));
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        if (url.contains("lib/jquery.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("platformjs/jquery.min.js"));
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        if (url.contains("js/jquery-weui.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("platformjs/jquery-weui.min.js"));
            } catch (IOException e5) {
                e5.printStackTrace();
            }
        }
        if (url.contains("js/viewer.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("platformjs/viewer.min.js"));
            } catch (IOException e6) {
                e6.printStackTrace();
            }
        }
        if (url.contains("js/jquery-2.1.4.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("platformjs/jquery-2.1.4.js"));
            } catch (IOException e7) {
                e7.printStackTrace();
            }
        }
        if (url.contains("js/jquery-3.0.0.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("platformjs/jquery-3.0.0.js"));
            } catch (IOException e8) {
                e8.printStackTrace();
            }
        }
        if (url.contains("js/map/main.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("platformjs/main.js"));
            } catch (IOException e9) {
                e9.printStackTrace();
            }
        }
        if (url.contains("js/map/maps.js")) {
            try {
                return new WebResourceResponse("text/javascriredpt", "utf-8", getAssets().open("platformjs/maps.js"));
            } catch (IOException e10) {
                e10.printStackTrace();
                return null;
            }
        }
        return null;
    }

    private WebResourceResponse replaceDbStaticResourcesNew(String url) {
        if (url.contains("daibanmobile/DES3.js") || url.contains("dxgl-app/dxslgl/DES3.js")) {
            try {
                Log.e(RPCDataItems.SWITCH_TAG_LOG, "==" + url);
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("db/DES3.js"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (url.contains("daibanmobile/cdnStatic/polyfill.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("db/polyfill.min.js"));
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        if (url.contains("daibanmobile/cdnStatic/moment.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("db/moment.min.js"));
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        if (url.contains("daibanmobile/cdnStatic/en-gb.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("db/en-gb.js"));
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        if (url.contains("daibanmobile/cdnStatic/zh-cn.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("db/zh-cn.js"));
            } catch (IOException e5) {
                e5.printStackTrace();
            }
        }
        if (url.contains("daibanmobile/cdnStatic/react.production.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("db/react.production.min.js"));
            } catch (IOException e6) {
                e6.printStackTrace();
            }
        }
        if (url.contains("daibanmobile/cdnStatic/react-dom.production.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("db/react-dom.production.min.js"));
            } catch (IOException e7) {
                e7.printStackTrace();
            }
        }
        if (url.contains("daibanmobile/cdnStatic/react-router-dom.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("db/react-router-dom.min.js"));
            } catch (IOException e8) {
                e8.printStackTrace();
            }
        }
        if (url.contains("daibanmobile/cdnStatic/lodash.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("db/lodash.min.js"));
            } catch (IOException e9) {
                e9.printStackTrace();
            }
        }
        if (url.contains("daibanmobile/cdnStatic/antd-mobile.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("db/antd-mobile.min.js"));
            } catch (IOException e10) {
                e10.printStackTrace();
            }
        }
        if (url.contains("daibanmobile/cdnStatic/mobx.umd.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("db/mobx.umd.min.js"));
            } catch (IOException e11) {
                e11.printStackTrace();
            }
        }
        if (url.contains("daibanmobile/cdnStatic/index.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("db/index.min.js"));
            } catch (IOException e12) {
                e12.printStackTrace();
            }
        }
        if (url.contains("daibanmobile/cdnStatic/mock-min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("db/mock-min.js"));
            } catch (IOException e13) {
                e13.printStackTrace();
            }
        }
        if (url.contains("daibanmobile/fastclick.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("db/fastclick.js"));
            } catch (IOException e14) {
                e14.printStackTrace();
            }
        }
        if (url.contains("daibanmobile/cdnStatic/zh-cn.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("db/zh-cn.js"));
            } catch (IOException e15) {
                e15.printStackTrace();
            }
        }
        if (url.contains("daibanmobile/cdnStatic/antd-mobile.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("db/antd-mobile.min.css"));
            } catch (IOException e16) {
                e16.printStackTrace();
                return null;
            }
        }
        return null;
    }

    private WebResourceResponse replaceYjyStaticResourcesNew(String url) {
        if (url.contains("spark-pkgs/@ant-design/4.2.2/index.umd.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/@ant-design/4.2.2/index.umd.min.js"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/@antv-l7/2.9.31/l7.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/@antv-l7/2.9.31/l7.js"));
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/antd/4.10.3/antd.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/antd/4.10.3/antd.min.js"));
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/antd/4.10.3/antd.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("spark-pkgs/antd/4.10.3/antd.min.css"));
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/antd/4.17.0/antd.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/antd/4.17.0/antd.min.js"));
            } catch (IOException e5) {
                e5.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/antd/4.17.0/antd.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("spark-pkgs/antd/4.17.0/antd.min.css"));
            } catch (IOException e6) {
                e6.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/antd/4.24.12/antd.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/antd/4.24.12/antd.min.js"));
            } catch (IOException e7) {
                e7.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/antd/4.24.12/antd.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("spark-pkgs/antd/4.24.12/antd.min.css"));
            } catch (IOException e8) {
                e8.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/echarts/4.9.0-rc.1/echarts.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/echarts/4.9.0-rc.1/echarts.js"));
            } catch (IOException e9) {
                e9.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/echarts/4.9.0-rc.1/echarts.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/echarts/4.9.0-rc.1/echarts.min.js"));
            } catch (IOException e10) {
                e10.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/echarts/5.4.1/echarts.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/echarts/5.4.1/echarts.min.js"));
            } catch (IOException e11) {
                e11.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/lodash/4.17.20/lodash.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/lodash/4.17.20/lodash.min.js"));
            } catch (IOException e12) {
                e12.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/moment/2.27.0/locale/en-gb.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/moment/2.27.0/locale/en-gb.js"));
            } catch (IOException e13) {
                e13.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/moment/2.27.0/locale/zh-cn.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/moment/2.27.0/locale/zh-cn.js"));
            } catch (IOException e14) {
                e14.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/moment/2.27.0/moment.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/moment/2.27.0/moment.js"));
            } catch (IOException e15) {
                e15.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/moment/2.27.0/moment.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/moment/2.27.0/moment.min.js"));
            } catch (IOException e16) {
                e16.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/moment/2.27.0/moment-with-locales.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/moment/2.27.0/moment-with-locales.min.js"));
            } catch (IOException e17) {
                e17.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/react/16.13.1/react.development.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/react/16.13.1/react.development.js"));
            } catch (IOException e18) {
                e18.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/react/16.13.1/react.production.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/react/16.13.1/react.production.min.js"));
            } catch (IOException e19) {
                e19.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/react-dom/16.13.1/react-dom.development.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/react-dom/16.13.1/react-dom.development.js"));
            } catch (IOException e20) {
                e20.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/react-dom/16.13.1/react-dom.production.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/react-dom/16.13.1/react-dom.production.min.js"));
            } catch (IOException e21) {
                e21.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/react-router/5.2.0/react-router.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/react-router/5.2.0/react-router.js"));
            } catch (IOException e22) {
                e22.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/react-router/5.2.0/react-router.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/react-router/5.2.0/react-router.min.js"));
            } catch (IOException e23) {
                e23.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/react-router-config/5.1.1/react-router-config.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/react-router-config/5.1.1/react-router-config.js"));
            } catch (IOException e24) {
                e24.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/react-router-config/5.1.1/react-router-config.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/react-router-config/5.1.1/react-router-config.min.js"));
            } catch (IOException e25) {
                e25.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/react-router-dom/5.2.0/react-router-dom.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/react-router-dom/5.2.0/react-router-dom.js"));
            } catch (IOException e26) {
                e26.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/react-router-dom/5.2.0/react-router-dom.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/react-router-dom/5.2.0/react-router-dom.min.js"));
            } catch (IOException e27) {
                e27.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/spread/gc.spread.calcengine.languagepackages.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/spread/gc.spread.calcengine.languagepackages.min.js"));
            } catch (IOException e28) {
                e28.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/spread/gc.spread.excelio.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/spread/gc.spread.excelio.min.js"));
            } catch (IOException e29) {
                e29.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/spread/gc.spread.pivot.pivottables.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/spread/gc.spread.pivot.pivottables.min.js"));
            } catch (IOException e30) {
                e30.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/spread/gc.spread.sheets.all.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/spread/gc.spread.sheets.all.min.js"));
            } catch (IOException e31) {
                e31.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/spread/gc.spread.sheets.barcode.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/spread/gc.spread.sheets.barcode.min.js"));
            } catch (IOException e32) {
                e32.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/spread/gc.spread.sheets.charts.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/spread/gc.spread.sheets.charts.min.js"));
            } catch (IOException e33) {
                e33.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/spread/gc.spread.sheets.designer.all.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/spread/gc.spread.sheets.designer.all.min.js"));
            } catch (IOException e34) {
                e34.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/spread/gc.spread.sheets.designer.react.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/spread/gc.spread.sheets.designer.react.js"));
            } catch (IOException e35) {
                e35.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/spread/gc.spread.sheets.designer.react.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/spread/gc.spread.sheets.designer.react.min.js"));
            } catch (IOException e36) {
                e36.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/spread/gc.spread.sheets.designer.resource.cn.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/spread/gc.spread.sheets.designer.resource.cn.min.js"));
            } catch (IOException e37) {
                e37.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/spread/gc.spread.sheets.pdf.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/spread/gc.spread.sheets.pdf.min.js"));
            } catch (IOException e38) {
                e38.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/spread/gc.spread.sheets.print.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/spread/gc.spread.sheets.print.min.js"));
            } catch (IOException e39) {
                e39.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/spread/gc.spread.sheets.react.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/spread/gc.spread.sheets.react.min.js"));
            } catch (IOException e40) {
                e40.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/spread/gc.spread.sheets.resources.zh.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/spread/gc.spread.sheets.resources.zh.min.js"));
            } catch (IOException e41) {
                e41.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/spread/gc.spread.sheets.shapes.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/spread/gc.spread.sheets.shapes.min.js"));
            } catch (IOException e42) {
                e42.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/standalone/7.14.2/babel.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/standalone/7.14.2/babel.min.js"));
            } catch (IOException e43) {
                e43.printStackTrace();
            }
        }
        if (url.contains("spark-pkgs/myfonts.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("spark-pkgs/myfonts.js"));
            } catch (IOException e44) {
                e44.printStackTrace();
                return null;
            }
        }
        return null;
    }

    private WebResourceResponse replaceStaticResourcesNew(String url) {
        if (url.contains("dxgl-app/ajax/libs/crypto-js/4.0.0/index.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/ajax/libs/crypto-js/4.0.0/index.min.js"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/ajax/libs/vConsole/3.3.4/vconsole.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/ajax/libs/vConsole/3.3.4/vconsole.min.js"));
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/antd-mobile/2.3.1/antd-mobile.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/antd-mobile/2.3.1/antd-mobile.min.js"));
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/antd-mobile/2.3.1/antd-mobile.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/antd-mobile/2.3.1/antd-mobile.min.css"));
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/babel-polyfill/7.8.7/polyfill.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/dxgl-app/babel-polyfill/7.8.7/polyfill.min.js"));
            } catch (IOException e5) {
                e5.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/tinymce.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/tinymce.min.js"));
            } catch (IOException e6) {
                e6.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/mycontent.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/mycontent.css"));
            } catch (IOException e7) {
                e7.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/mycontent.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/mycontent.css"));
            } catch (IOException e8) {
                e8.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/tinymce.d.ts")) {
            try {
                return new WebResourceResponse("text/ts", "utf-8", getAssets().open("dxgl-app/libs/tinymce/tinymce.d.ts"));
            } catch (IOException e9) {
                e9.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/icons/default/icons.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/icons/default/icons.min.js"));
            } catch (IOException e10) {
                e10.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/langs/zh_CN.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/langs/zh_CN.js"));
            } catch (IOException e11) {
                e11.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/models/dom/model.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/models/dom/model.min.js"));
            } catch (IOException e12) {
                e12.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/accordion/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/accordion/plugin.min.js"));
            } catch (IOException e13) {
                e13.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/advlist/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/advlist/plugin.min.js"));
            } catch (IOException e14) {
                e14.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/anchor/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/anchor/plugin.min.js"));
            } catch (IOException e15) {
                e15.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/autolink/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/autolink/plugin.min.js"));
            } catch (IOException e16) {
                e16.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/autoresize/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/autoresize/plugin.min.js"));
            } catch (IOException e17) {
                e17.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/autosave/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/autosave/plugin.min.js"));
            } catch (IOException e18) {
                e18.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/bbcode/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/bbcode/plugin.min.js"));
            } catch (IOException e19) {
                e19.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/charmap/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/charmap/plugin.min.js"));
            } catch (IOException e20) {
                e20.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/code/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/code/plugin.min.js"));
            } catch (IOException e21) {
                e21.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/codesample/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/codesample/plugin.min.js"));
            } catch (IOException e22) {
                e22.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/colorpicker/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/colorpicker/plugin.min.js"));
            } catch (IOException e23) {
                e23.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/contextmenu/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/contextmenu/plugin.min.js"));
            } catch (IOException e24) {
                e24.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/directionality/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/directionality/plugin.min.js"));
            } catch (IOException e25) {
                e25.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/emoticons/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/emoticons/plugin.min.js"));
            } catch (IOException e26) {
                e26.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/emoticons/js/emojiimages.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/emoticons/js/emojiimages.js"));
            } catch (IOException e27) {
                e27.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/emoticons/js/emojiimages.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/emoticons/js/emojiimages.min.js"));
            } catch (IOException e28) {
                e28.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/emoticons/js/emojis.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/emoticons/js/emojis.js"));
            } catch (IOException e29) {
                e29.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/emoticons/js/emojis.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/emoticons/js/emojis.min.js"));
            } catch (IOException e30) {
                e30.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/formatpainter/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/formatpainter/plugin.min.js"));
            } catch (IOException e31) {
                e31.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/fullpage/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/fullpage/plugin.min.js"));
            } catch (IOException e32) {
                e32.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/fullscreen/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/fullscreen/plugin.min.js"));
            } catch (IOException e33) {
                e33.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/plugin.min.js"));
            } catch (IOException e34) {
                e34.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/ar.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/ar.js"));
            } catch (IOException e35) {
                e35.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/bg_BG.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/bg_BG.js"));
            } catch (IOException e36) {
                e36.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/ca.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/ca.js"));
            } catch (IOException e37) {
                e37.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/cs.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/cs.js"));
            } catch (IOException e38) {
                e38.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/da.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/da.js"));
            } catch (IOException e39) {
                e39.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/de.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/de.js"));
            } catch (IOException e40) {
                e40.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/el.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/el.js"));
            } catch (IOException e41) {
                e41.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/en.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/en.js"));
            } catch (IOException e42) {
                e42.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/es.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/es.js"));
            } catch (IOException e43) {
                e43.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/eu.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/eu.js"));
            } catch (IOException e44) {
                e44.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/fa.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/fa.js"));
            } catch (IOException e45) {
                e45.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/fi.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/fi.js"));
            } catch (IOException e46) {
                e46.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/fr_FR.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/fr_FR.js"));
            } catch (IOException e47) {
                e47.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/he_IL.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/he_IL.js"));
            } catch (IOException e48) {
                e48.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/hi.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/hi.js"));
            } catch (IOException e49) {
                e49.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/hr.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/hr.js"));
            } catch (IOException e50) {
                e50.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/hu_HU.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/hu_HU.js"));
            } catch (IOException e51) {
                e51.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/id.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/id.js"));
            } catch (IOException e52) {
                e52.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/it.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/it.js"));
            } catch (IOException e53) {
                e53.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/ja.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/ja.js"));
            } catch (IOException e54) {
                e54.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/kk.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/kk.js"));
            } catch (IOException e55) {
                e55.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/ko_KR.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/ko_KR.js"));
            } catch (IOException e56) {
                e56.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/ms.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/ms.js"));
            } catch (IOException e57) {
                e57.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/nb_NO.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/nb_NO.js"));
            } catch (IOException e58) {
                e58.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/nl.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/nl.js"));
            } catch (IOException e59) {
                e59.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/pl.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/pl.js"));
            } catch (IOException e60) {
                e60.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/pt_BR.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/pt_BR.js"));
            } catch (IOException e61) {
                e61.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/pt_PT.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/pt_PT.js"));
            } catch (IOException e62) {
                e62.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/ro.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/ro.js"));
            } catch (IOException e63) {
                e63.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/ru.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/ru.js"));
            } catch (IOException e64) {
                e64.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/sk.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/sk.js"));
            } catch (IOException e65) {
                e65.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/sl_SI.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/sl_SI.js"));
            } catch (IOException e66) {
                e66.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/sv_SE.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/sv_SE.js"));
            } catch (IOException e67) {
                e67.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/th_TH.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/th_TH.js"));
            } catch (IOException e68) {
                e68.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/tr.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/tr.js"));
            } catch (IOException e69) {
                e69.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/uk.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/uk.js"));
            } catch (IOException e70) {
                e70.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/vi.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/vi.js"));
            } catch (IOException e71) {
                e71.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/zh_CN.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/zh_CN.js"));
            } catch (IOException e72) {
                e72.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/zh_TW.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/help/js/i18n/keynav/zh_TW.js"));
            } catch (IOException e73) {
                e73.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/hr/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/hr/plugin.min.js"));
            } catch (IOException e74) {
                e74.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/image/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/image/plugin.min.js"));
            } catch (IOException e75) {
                e75.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/imagetools/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/imagetools/plugin.min.js"));
            } catch (IOException e76) {
                e76.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/importcss/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/importcss/plugin.min.js"));
            } catch (IOException e77) {
                e77.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/insertdatetime/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/insertdatetime/plugin.min.js"));
            } catch (IOException e78) {
                e78.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/legacyoutput/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/legacyoutput/plugin.min.js"));
            } catch (IOException e79) {
                e79.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/link/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/link/plugin.min.js"));
            } catch (IOException e80) {
                e80.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/lists/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/lists/plugin.min.js"));
            } catch (IOException e81) {
                e81.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/media/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/media/plugin.min.js"));
            } catch (IOException e82) {
                e82.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/nonbreaking/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/nonbreaking/plugin.min.js"));
            } catch (IOException e83) {
                e83.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/noneditable/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/noneditable/plugin.min.js"));
            } catch (IOException e84) {
                e84.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/pagebreak/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/pagebreak/plugin.min.js"));
            } catch (IOException e85) {
                e85.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/paste/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/paste/plugin.min.js"));
            } catch (IOException e86) {
                e86.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/preview/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/preview/plugin.min.js"));
            } catch (IOException e87) {
                e87.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/print/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/print/plugin.min.js"));
            } catch (IOException e88) {
                e88.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/quickbars/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/quickbars/plugin.min.js"));
            } catch (IOException e89) {
                e89.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/save/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/save/plugin.min.js"));
            } catch (IOException e90) {
                e90.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/searchreplace/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/searchreplace/plugin.min.js"));
            } catch (IOException e91) {
                e91.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/spellchecker/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/spellchecker/plugin.min.js"));
            } catch (IOException e92) {
                e92.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/tabfocus/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/tabfocus/plugin.min.js"));
            } catch (IOException e93) {
                e93.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/table/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/table/plugin.min.js"));
            } catch (IOException e94) {
                e94.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/template/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/template/plugin.min.js"));
            } catch (IOException e95) {
                e95.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/textcolor/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/textcolor/plugin.min.js"));
            } catch (IOException e96) {
                e96.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/textpattern/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/textpattern/plugin.min.js"));
            } catch (IOException e97) {
                e97.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/toc/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/toc/plugin.min.js"));
            } catch (IOException e98) {
                e98.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/visualblocks/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/visualblocks/plugin.min.js"));
            } catch (IOException e99) {
                e99.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/visualchars/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/visualchars/plugin.min.js"));
            } catch (IOException e100) {
                e100.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/plugins/wordcount/plugin.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/plugins/wordcount/plugin.min.js"));
            } catch (IOException e101) {
                e101.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/content/dark/content.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/content/dark/content.min.css"));
            } catch (IOException e102) {
                e102.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/content/default/content.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/content/default/content.min.css"));
            } catch (IOException e103) {
                e103.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/content/document/content.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/content/document/content.min.css"));
            } catch (IOException e104) {
                e104.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/content/tinymce-5/content.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/content/tinymce-5/content.min.css"));
            } catch (IOException e105) {
                e105.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/content/tinymce-5-dark/content.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/content/tinymce-5-dark/content.min.css"));
            } catch (IOException e106) {
                e106.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/content/writer/content.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/content/writer/content.min.css"));
            } catch (IOException e107) {
                e107.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/ui/oxide/content.inline.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/ui/oxide/content.inline.min.css"));
            } catch (IOException e108) {
                e108.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/ui/oxide/content.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/ui/oxide/content.min.css"));
            } catch (IOException e109) {
                e109.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/ui/oxide/skin.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/ui/oxide/skin.min.css"));
            } catch (IOException e110) {
                e110.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/ui/oxide/skin.shadowdom.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/ui/oxide/skin.shadowdom.min.css"));
            } catch (IOException e111) {
                e111.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/ui/oxide-dark/content.inline.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/ui/oxide-dark/content.inline.min.css"));
            } catch (IOException e112) {
                e112.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/ui/oxide-dark/content.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/ui/oxide-dark/content.min.css"));
            } catch (IOException e113) {
                e113.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/ui/oxide-dark/skin.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/ui/oxide-dark/skin.min.css"));
            } catch (IOException e114) {
                e114.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/ui/oxide-dark/skin.shadowdom.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/ui/oxide-dark/skin.shadowdom.min.css"));
            } catch (IOException e115) {
                e115.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/ui/tinymce-5/content.inline.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/ui/tinymce-5/content.inline.min.css"));
            } catch (IOException e116) {
                e116.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/ui/tinymce-5/content.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/ui/tinymce-5/content.min.css"));
            } catch (IOException e117) {
                e117.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/ui/tinymce-5/skin.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/ui/tinymce-5/skin.min.css"));
            } catch (IOException e118) {
                e118.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/ui/tinymce-5/skin.shadowdom.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/ui/tinymce-5/skin.shadowdom.min.css"));
            } catch (IOException e119) {
                e119.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/ui/tinymce-5-dark/content.inline.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/ui/tinymce-5-dark/content.inline.min.css"));
            } catch (IOException e120) {
                e120.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/ui/tinymce-5-dark/content.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/ui/tinymce-5-dark/content.min.css"));
            } catch (IOException e121) {
                e121.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/ui/tinymce-5-dark/skin.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/ui/tinymce-5-dark/skin.min.css"));
            } catch (IOException e122) {
                e122.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/skins/ui/tinymce-5-dark/skin.shadowdom.min.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/libs/tinymce/skins/ui/tinymce-5-dark/skin.shadowdom.min.css"));
            } catch (IOException e123) {
                e123.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/libs/tinymce/themes/silver/theme.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/libs/tinymce/themes/silver/theme.min.js"));
            } catch (IOException e124) {
                e124.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/lodash.js/4.17.15/lodash.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/lodash.js/4.17.15/lodash.min.js"));
            } catch (IOException e125) {
                e125.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/mobx/4.12.0/mobx.umd.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/mobx/4.12.0/mobx.umd.min.js"));
            } catch (IOException e126) {
                e126.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/mobx-react/5.1.2/index.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/mobx-react/5.1.2/index.min.js"));
            } catch (IOException e127) {
                e127.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/moment.js/2.24.0/moment.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/moment.js/2.24.0/moment.min.js"));
            } catch (IOException e128) {
                e128.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/moment.js/2.24.0/locale/en-gb.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/moment.js/2.24.0/locale/en-gb.js"));
            } catch (IOException e129) {
                e129.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/moment.js/2.24.0/locale/zh-cn.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/moment.js/2.24.0/locale/zh-cn.js"));
            } catch (IOException e130) {
                e130.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/other/fastclick.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/other/fastclick.js"));
            } catch (IOException e131) {
                e131.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/react/16.10.2/umd/react.production.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/react/16.10.2/umd/react.production.min.js"));
            } catch (IOException e132) {
                e132.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/react-dom/16.10.2/umd/react-dom.production.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/react-dom/16.10.2/umd/react-dom.production.min.js"));
            } catch (IOException e133) {
                e133.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/react-router-dom/5.1.2/react-router-dom.min.js")) {
            try {
                return new WebResourceResponse("text/javascript", "utf-8", getAssets().open("dxgl-app/react-router-dom/5.1.2/react-router-dom.min.js"));
            } catch (IOException e134) {
                e134.printStackTrace();
            }
        }
        if (url.contains("dxgl-app/shineyue-component-mobile/style.css")) {
            try {
                return new WebResourceResponse("text/css", "utf-8", getAssets().open("dxgl-app/shineyue-component-mobile/style.css"));
            } catch (IOException e135) {
                e135.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @JavascriptInterface
    public void sharePicture(Object msg, CompletionHandler<String> handler) {
        try {
            JSONObject jsonObject = new JSONObject((String) msg);
            String data = jsonObject.getString("imageData");
            if (data.indexOf(",") > -1) {
                data = data.split(",")[1];
            }
            Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), FileUtil.base64ToBitmap(data), (String) null, (String) null));
            Intent shareIntent = new Intent();
            shareIntent.setAction("android.intent.action.SEND");
            shareIntent.putExtra("android.intent.extra.STREAM", uri);
            shareIntent.setType(SelectMimeType.SYSTEM_IMAGE);
            startActivity(Intent.createChooser(shareIntent, "资源来自贝贝"));
            JSONObject jsonResult = new JSONObject();
            jsonResult.put("data", new JSONObject());
            jsonResult.put(MyLocationStyle.ERROR_CODE, 0);
            jsonResult.put("msg", "接口调用成功");
            handler.complete(jsonResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
            KLog.e(e.getMessage());
            JSONObject jsonResult2 = new JSONObject();
            try {
                jsonResult2.put("data", new JSONObject());
                jsonResult2.put(MyLocationStyle.ERROR_CODE, -1);
                jsonResult2.put("msg", e.getMessage());
                handler.complete(jsonResult2.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @JavascriptInterface
    public void callPhone(String msg) {
        try {
            JSONObject jsonObject = new JSONObject(msg);
            String phone = jsonObject.get("phone").toString();
            Intent intentCall = new Intent("android.intent.action.DIAL");
            Uri data = Uri.parse(com.tencent.smtt.sdk.WebView.SCHEME_TEL + phone);
            intentCall.setData(data);
            this.mContext.startActivity(intentCall);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void sharedata(String msg) {
        String base64Image;
        String title;
        String description;
        String url;
        String text;
        try {
            KLog.i(this.TAG, msg);
            JSONObject jsonObject = new JSONObject(msg);
            List<ShareTypeBean> list = new ArrayList<>();
            list.add(new ShareTypeBean("微信好友", R.mipmap.sheet_share, 1));
            list.add(new ShareTypeBean("朋友圈", R.mipmap.sheet_moments, 2));
            final int type = jsonObject.getInt("type");
            if (type == 1) {
                String base64Image2 = jsonObject.get("image").toString();
                list.add(new ShareTypeBean("保存本地", R.mipmap.sheet_download, 3));
                base64Image = base64Image2;
                title = null;
                description = null;
                url = null;
                text = null;
            } else if (type == 2) {
                String title2 = jsonObject.get("title").toString();
                String description2 = jsonObject.get(IntentConstant.DESCRIPTION).toString();
                String url2 = jsonObject.get("url").toString();
                list.add(new ShareTypeBean("复制链接", R.drawable.sheet_copylink, 3));
                base64Image = null;
                title = title2;
                description = description2;
                url = url2;
                text = null;
            } else if (type != 3) {
                base64Image = null;
                title = null;
                description = null;
                url = null;
                text = null;
            } else {
                String text2 = jsonObject.get("text").toString();
                base64Image = null;
                title = null;
                description = null;
                url = null;
                text = text2;
            }
            final String finalBase64Image = base64Image;
            final String finalUrl = url;
            final String finalTitle = title;
            final String finalDescription = description;
            final String finalUrl1 = url;
            final String finalText = text;
            new SharePop(OaApplication.application).showPopupWindow(this.fl_container, list, "", 62).callBack(new SharePop.OnConfirm() { // from class: com.shineyue.pm.web.WebActivity.41
                @Override // com.shineyue.pm.view.SharePop.OnConfirm
                public void confirm(int position, ShareTypeBean bean) {
                    if (bean.type == 1) {
                        int i = type;
                        if (i == 1) {
                            WebActivity.this.sharePic(false, finalBase64Image);
                            return;
                        } else if (i == 2) {
                            WebActivity.this.shareUrl(finalUrl, false, finalTitle, finalDescription);
                            return;
                        } else {
                            if (i == 3) {
                                WebActivity.this.shareText(finalText, false);
                                return;
                            }
                            return;
                        }
                    }
                    if (bean.type == 2) {
                        int i2 = type;
                        if (i2 == 1) {
                            WebActivity.this.sharePic(true, finalBase64Image);
                            return;
                        } else if (i2 == 2) {
                            WebActivity.this.shareUrl(finalUrl, true, finalTitle, finalDescription);
                            return;
                        } else {
                            if (i2 == 3) {
                                WebActivity.this.shareText(finalText, true);
                                return;
                            }
                            return;
                        }
                    }
                    if (bean.type == 3) {
                        int i3 = type;
                        if (i3 == 1) {
                            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                            Bitmap bitmap = FileUtil.base64ToBitmap(finalBase64Image);
                            String fileName = "IMG_" + timeStamp + ".jpg";
                            WebActivity.this.saveBitmap = bitmap;
                            WebActivity.this.bitmapName = fileName;
                            WebActivity.this.checkSavePermission();
                            return;
                        }
                        if (i3 == 2) {
                            ClipboardManager cm = (ClipboardManager) WebActivity.this.mContext.getSystemService("clipboard");
                            ClipData mClipData = ClipData.newPlainText("Label", finalUrl1);
                            cm.setPrimaryClip(mClipData);
                            ToastUtil.toast(WebActivity.this.mContext, ToastInfo.btnSuccess, "操作", ToastType.SUCCESS);
                        }
                    }
                }
            });
        } catch (Exception e) {
            KLog.i(this.TAG, e.toString());
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void Scan() {
        Intent intentScan = new Intent(this.mContext, (Class<?>) QrCodeActivity.class);
        intentScan.putExtra("bz", 1);
        startActivityForResult(intentScan, BaseQuickAdapter.FOOTER_VIEW);
    }

    @JavascriptInterface
    public void jumpPersonalPortrait(String id, String name) {
        Intent intent = new Intent(this.mContext, (Class<?>) BusinessCardActivity.class);
        intent.putExtra("id", id);
        intent.putExtra(c.e, name);
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkSavePermission() {
        chekPermissions(this, this.permissions, this.permissionssave);
    }

    public void chekPermissions(Activity context, String[] permissions, PermissionsUtils.IPermissionsResult permissionsResult) {
        this.mPermissionsResult = permissionsResult;
        if (Build.VERSION.SDK_INT < 23) {
            permissionsResult.passPermissons();
            return;
        }
        KLog.i("LLL", "111");
        List<String> mPermissionList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(context, permissions[i]) != 0) {
                mPermissionList.add(permissions[i]);
            }
        }
        KLog.i("LLL", "111");
        if (mPermissionList.size() > 0) {
            KLog.i("LLL", "111");
            Context context2 = this.mContext;
            if (context2 instanceof BasicActivity) {
                super.showPermissionNotificationPop(1);
            }
            requestPermissions(permissions, 102);
            return;
        }
        KLog.i("LLL", "111");
        permissionsResult.passPermissons();
    }

    public void saveBitmap(Bitmap bitmap, String bitName) {
        this.saveBitmapBp = bitmap;
        ThreadPoolManager.getInstance().execute(this.saveBitmapRunnable);
    }

    public void shareUrl(String webpageUrl, boolean isCircle, String title, String description) {
        try {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = webpageUrl;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = title;
            msg.description = description;
            Bitmap thumb = BitmapFactory.decodeResource(this.mContext.getResources(), R.mipmap.logo);
            msg.thumbData = Utils.bitmap2ByteArray(thumb);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.message = msg;
            req.scene = isCircle ? 1 : 0;
            OaApplication.api.sendReq(req);
        } catch (Exception e) {
            KLog.i(this.TAG, e.toString());
        }
    }

    public void shareText(String str, boolean z) {
        WXTextObject wXTextObject = new WXTextObject();
        wXTextObject.text = str;
        WXMediaMessage wXMediaMessage = new WXMediaMessage();
        wXMediaMessage.mediaObject = wXTextObject;
        wXMediaMessage.description = str;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = wXMediaMessage;
        req.scene = z ? 1 : 0;
        OaApplication.api.sendReq(req);
    }

    public void sharePic(boolean isCircle, String base64Imag) {
        try {
            Bitmap bmp = FileUtil.base64ToBitmap(base64Imag);
            KLog.i(this.TAG, bmp);
            WXImageObject imgObj = new WXImageObject(bmp);
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObj;
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
            bmp.recycle();
            msg.thumbData = Utils.bitmap2ByteArray(thumbBmp);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.message = msg;
            req.scene = isCircle ? 1 : 0;
            OaApplication.api.sendReq(req);
        } catch (Exception e) {
            KLog.i(this.TAG, e.toString());
        }
    }

    @JavascriptInterface
    public void encrypt(Object msg, CompletionHandler<String> handler) {
        this.webActivityApi.encrypt(msg, handler);
    }

    @JavascriptInterface
    public void decrypt(Object msg, CompletionHandler<String> handler) {
        this.webActivityApi.decrypt(msg, handler);
    }

    @JavascriptInterface
    public void getDepts(Object msg, CompletionHandler<String> handler) {
        this.webActivityApi.getDepts(msg, handler);
    }

    public void setProgressSmooth(ProgressBar progressBar, int progress) {
        long time = (progress - progressBar.getProgress()) * 5;
        if (time <= 0) {
            return;
        }
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", progress);
        animation.setDuration(time);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
    }

    public void stopProgressSmooth(final ProgressBar progressBar, final CustomLoadingView customLoadingView) {
        long time = (100 - progressBar.getProgress()) * 5;
        if (time <= 0) {
            progressBar.setVisibility(8);
            progressBar.setProgress(0);
            customLoadingView.setVisibility(8);
        } else {
            ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 100);
            animation.setDuration(time);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.start();
            animation.addListener(new Animator.AnimatorListener() { // from class: com.shineyue.pm.web.WebActivity.44
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animation2) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation2) {
                    progressBar.setVisibility(8);
                    progressBar.setProgress(0);
                    customLoadingView.setVisibility(8);
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animation2) {
                    progressBar.setVisibility(8);
                    progressBar.setProgress(0);
                    customLoadingView.setVisibility(8);
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animation2) {
                }
            });
        }
    }

    private void antCloudFaceCertifyNew(final String xingming, final String zjhm) {
        if (this.iService == null) {
            ServiceFactory.init(this);
            this.iService = ServiceFactory.create(this).build();
        }
        if (this.newLoginModule == null) {
            this.newLoginModule = new NewLoginModule();
        }
        final JSONObject jsonResult = new JSONObject();
        if (Utils.getBooleanMsg(this, "isGrxxUser")) {
            this.newLoginModule.antCloudFace(xingming, zjhm, new OnParmsCallBack<String>() { // from class: com.shineyue.pm.web.WebActivity.45
                @Override // com.shineyue.pm.modle_chat.chat_history.dao.OnParmsCallBack
                public void onParmsResult(String s) {
                    KLog.i(WebActivity.this.TAG, s);
                    if (Utils.haveValue(s)) {
                        try {
                            JSONObject results = new JSONObject(s);
                            int code = results.optInt("code");
                            if (code == 0) {
                                JSONObject result = results.optJSONObject("results");
                                final String certifyId = result.optString("certifyId");
                                com.alibaba.fastjson.JSONObject requestInfo = new com.alibaba.fastjson.JSONObject();
                                requestInfo.put("bizCode", (Object) BizCode.Value.FACE_APP);
                                requestInfo.put("certifyId", (Object) certifyId);
                                WebActivity.this.iService.startService(requestInfo, true, new ICallback() { // from class: com.shineyue.pm.web.WebActivity.45.1
                                    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
                                    @Override // com.alipay.mobile.android.verify.sdk.interfaces.ICallback
                                    public void onResponse(Map<String, String> response) {
                                        try {
                                            String responseCode = response.get(j.a);
                                            if (!"9000".equals(responseCode)) {
                                                Utils.errorUpload(WebActivity.this.mContext, "faceScan", responseCode, xingming, zjhm);
                                            }
                                            if ("9000".equals(responseCode)) {
                                                jsonResult.put("msg", "刷脸成功");
                                                jsonResult.put("code", 0);
                                                jsonResult.put("certifyId", certifyId);
                                            } else if ("6001".equals(responseCode)) {
                                                jsonResult.put("code", 1);
                                                jsonResult.put("msg", "认证失败");
                                                jsonResult.put("certifyId", certifyId);
                                            } else {
                                                jsonResult.put("code", 1);
                                                jsonResult.put("msg", "认证失败");
                                                jsonResult.put("certifyId", certifyId);
                                            }
                                            if (WebActivity.this.brushHandler == null) {
                                                KLog.i(WebActivity.this.TAG, jsonResult.toString());
                                                WebActivity.this.execJsMethod("javascript:brushFaceauthorizationResult(" + jsonResult.toString() + ")");
                                                return;
                                            }
                                            WebActivity.this.brushHandler.complete(jsonResult.toString());
                                            WebActivity.this.brushHandler = null;
                                        } catch (Exception e) {
                                            KLog.i(WebActivity.this.TAG, e.toString());
                                            try {
                                                jsonResult.put("code", 1);
                                                jsonResult.put("msg", "认证失败");
                                                jsonResult.put("certifyId", certifyId);
                                                if (WebActivity.this.brushHandler != null) {
                                                    WebActivity.this.brushHandler.complete(jsonResult.toString());
                                                    WebActivity.this.brushHandler = null;
                                                } else {
                                                    WebActivity.this.execJsMethod("javascript:brushFaceauthorizationResult(" + jsonResult.toString() + ")");
                                                }
                                            } catch (Exception ex) {
                                                ex.toString();
                                            }
                                        }
                                    }
                                });
                                return;
                            }
                            String msg = "刷脸服务启动失败";
                            try {
                                if (results.has("msg")) {
                                    msg = results.optString("msg");
                                }
                                jsonResult.put("code", 2);
                                jsonResult.put("msg", msg);
                                jsonResult.put("certifyId", "");
                                if (WebActivity.this.brushHandler == null) {
                                    KLog.i(WebActivity.this.TAG, jsonResult.toString());
                                    WebActivity.this.execJsMethod("javascript:brushFaceauthorizationResult('" + jsonResult.toString() + "')");
                                    return;
                                }
                                WebActivity.this.brushHandler.complete(jsonResult.toString());
                                WebActivity.this.brushHandler = null;
                                return;
                            } catch (Exception ex) {
                                ex.toString();
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            try {
                                jsonResult.put("code", 2);
                                jsonResult.put("msg", "刷脸服务启动失败");
                                jsonResult.put("certifyId", "");
                                if (WebActivity.this.brushHandler != null) {
                                    WebActivity.this.brushHandler.complete(jsonResult.toString());
                                    WebActivity.this.brushHandler = null;
                                } else {
                                    WebActivity.this.execJsMethod("javascript:brushFaceauthorizationResult(" + jsonResult.toString() + ")");
                                }
                                return;
                            } catch (Exception ex2) {
                                ex2.toString();
                                return;
                            }
                        }
                    }
                    try {
                        jsonResult.put("code", 2);
                        jsonResult.put("msg", "刷脸服务启动失败");
                        jsonResult.put("certifyId", "");
                        if (WebActivity.this.brushHandler != null) {
                            WebActivity.this.brushHandler.complete(jsonResult.toString());
                            WebActivity.this.brushHandler = null;
                        } else {
                            WebActivity.this.execJsMethod("javascript:brushFaceauthorizationResult(" + jsonResult.toString() + ")");
                        }
                    } catch (Exception ex3) {
                        ex3.toString();
                    }
                }
            });
        } else {
            this.newLoginModule.getFaceUrl(xingming, zjhm, new OnParmsCallBack<String>() { // from class: com.shineyue.pm.web.WebActivity.46
                @Override // com.shineyue.pm.modle_chat.chat_history.dao.OnParmsCallBack
                public void onParmsResult(String s) {
                    KLog.i(WebActivity.this.TAG, s);
                    if (Utils.haveValue(s)) {
                        try {
                            JSONObject result = new JSONObject(s);
                            if ("0".equals(result.optString("ret"))) {
                                final String certifyId = result.optString("certifyId");
                                com.alibaba.fastjson.JSONObject requestInfo = new com.alibaba.fastjson.JSONObject();
                                requestInfo.put("bizCode", (Object) BizCode.Value.FACE_APP);
                                requestInfo.put("certifyId", (Object) certifyId);
                                WebActivity.this.iService.startService(requestInfo, true, new ICallback() { // from class: com.shineyue.pm.web.WebActivity.46.1
                                    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
                                    @Override // com.alipay.mobile.android.verify.sdk.interfaces.ICallback
                                    public void onResponse(Map<String, String> response) {
                                        try {
                                            String responseCode = response.get(j.a);
                                            if (!"9000".equals(responseCode)) {
                                                Utils.errorUpload(WebActivity.this.mContext, "faceScan", responseCode, xingming, zjhm);
                                            }
                                            if ("9000".equals(responseCode)) {
                                                jsonResult.put("msg", "刷脸成功");
                                                jsonResult.put("code", 0);
                                                jsonResult.put("certifyId", certifyId);
                                            } else if ("6001".equals(responseCode)) {
                                                jsonResult.put("code", 1);
                                                jsonResult.put("msg", "认证失败");
                                                jsonResult.put("certifyId", certifyId);
                                            } else {
                                                jsonResult.put("code", 1);
                                                jsonResult.put("msg", "认证失败");
                                                jsonResult.put("certifyId", certifyId);
                                            }
                                            if (WebActivity.this.brushHandler == null) {
                                                KLog.i(WebActivity.this.TAG, jsonResult.toString());
                                                WebActivity.this.execJsMethod("javascript:brushFaceauthorizationResult(" + jsonResult.toString() + ")");
                                                return;
                                            }
                                            WebActivity.this.brushHandler.complete(jsonResult.toString());
                                            WebActivity.this.brushHandler = null;
                                        } catch (Exception e) {
                                            KLog.i(WebActivity.this.TAG, e.toString());
                                            try {
                                                jsonResult.put("code", 1);
                                                jsonResult.put("msg", "认证失败");
                                                jsonResult.put("certifyId", certifyId);
                                                if (WebActivity.this.brushHandler != null) {
                                                    WebActivity.this.brushHandler.complete(jsonResult.toString());
                                                    WebActivity.this.brushHandler = null;
                                                } else {
                                                    WebActivity.this.execJsMethod("javascript:brushFaceauthorizationResult(" + jsonResult.toString() + ")");
                                                }
                                            } catch (Exception ex) {
                                                ex.toString();
                                            }
                                        }
                                    }
                                });
                                return;
                            }
                            String msg = "刷脸服务启动失败";
                            try {
                                if (result.has("msg")) {
                                    msg = result.optString("msg");
                                }
                                jsonResult.put("code", 2);
                                jsonResult.put("msg", msg);
                                jsonResult.put("certifyId", "");
                                if (WebActivity.this.brushHandler == null) {
                                    KLog.i(WebActivity.this.TAG, jsonResult.toString());
                                    WebActivity.this.execJsMethod("javascript:brushFaceauthorizationResult('" + jsonResult.toString() + "')");
                                    return;
                                }
                                WebActivity.this.brushHandler.complete(jsonResult.toString());
                                WebActivity.this.brushHandler = null;
                                return;
                            } catch (Exception ex) {
                                ex.toString();
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            try {
                                jsonResult.put("code", 2);
                                jsonResult.put("msg", "刷脸服务启动失败");
                                jsonResult.put("certifyId", "");
                                if (WebActivity.this.brushHandler != null) {
                                    WebActivity.this.brushHandler.complete(jsonResult.toString());
                                    WebActivity.this.brushHandler = null;
                                } else {
                                    WebActivity.this.execJsMethod("javascript:brushFaceauthorizationResult(" + jsonResult.toString() + ")");
                                }
                                return;
                            } catch (Exception ex2) {
                                ex2.toString();
                                return;
                            }
                        }
                    }
                    try {
                        jsonResult.put("code", 2);
                        jsonResult.put("msg", "刷脸服务启动失败");
                        jsonResult.put("certifyId", "");
                        if (WebActivity.this.brushHandler != null) {
                            WebActivity.this.brushHandler.complete(jsonResult.toString());
                            WebActivity.this.brushHandler = null;
                        } else {
                            WebActivity.this.execJsMethod("javascript:brushFaceauthorizationResult(" + jsonResult.toString() + ")");
                        }
                    } catch (Exception ex3) {
                        ex3.toString();
                    }
                }
            });
        }
    }

    @JavascriptInterface
    public void createSphy(String str) {
        KLog.i(this.TAG, str);
        try {
            if (Utils.getBooleanMsg(OaApplication.application, "yspFlag")) {
                JSONObject jsonObject = new JSONObject(str);
                this.mettingId = jsonObject.optInt("mettingId") + "";
                this.toName = jsonObject.optString("toName");
                this.yqrIds = jsonObject.optString("yqrIds");
                this.yqrNames = jsonObject.optString("yqrNames");
                this.sxtFlag = jsonObject.optBoolean("sxtFlag");
                this.mkfFlag = jsonObject.optBoolean("mkfFlag");
                this.sphyType = 1;
                checkVideoPermission();
            } else {
                ToastUtil.toast(this.mContext, ToastInfo.notEnabled, "视频会议", ToastType.FAIL);
            }
        } catch (Exception e) {
            KLog.i(this.TAG, e.toString());
        }
    }

    @JavascriptInterface
    public void newAlipay(String str) {
        KLog.i(this.TAG, str);
        try {
            JSONObject jsonObject = new JSONObject(str);
            String strOptString = jsonObject.optString("data");
            this.newAiPayData = strOptString;
            if (!TextUtils.isEmpty(strOptString)) {
                Intent intent = new Intent(this.mContext, (Class<?>) AliPayActivity.class);
                intent.putExtra("newAiPayData", this.newAiPayData);
                startActivity(intent);
            } else {
                ToastUtil.toast(this.mContext, "msg", "数据信息不能为空", ToastType.FAIL);
            }
        } catch (Exception e) {
            KLog.i(this.TAG, e.toString());
        }
    }

    @JavascriptInterface
    public void joinSphy(String str) {
        KLog.i(this.TAG, str);
        try {
            if (Utils.getBooleanMsg(OaApplication.application, "yspFlag")) {
                JSONObject jsonObject = new JSONObject(str);
                this.mettingId = jsonObject.optInt("mettingId") + "";
                this.toName = jsonObject.optString("toName");
                this.sxtFlag = jsonObject.optBoolean("sxtFlag");
                this.mkfFlag = jsonObject.optBoolean("mkfFlag");
                this.sphyType = 2;
                checkVideoPermission();
            } else {
                ToastUtil.toast(this.mContext, ToastInfo.notEnabled, "视频会议", ToastType.FAIL);
            }
        } catch (Exception e) {
            KLog.i(this.TAG, e.toString());
        }
    }

    private void checkVideoPermission() {
        PermissionsUtils.getInstance().chekPermissions((WebActivity) this.mContext, this.permissions_video, this.permissionsResult_video);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkAudioPermission() {
        PermissionsUtils.getInstance().chekPermissions((WebActivity) this.mContext, this.permissions_audio, this.permissionsResult_audio);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void joinLive() {
        if (CallingActivity.IS_LIVE) {
            ToastUtil.toast(this.mContext, "msg", "当前正在会议中，请稍后", ToastType.WARNING);
        } else if (LiveAnchorActivity.mIsStartLive || LiveAudienceActivity.mIsStartLive) {
            ToastUtil.toast(this.mContext, "msg", "当前正在直播间中", ToastType.WARNING);
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("meetingId", this.mettingId);
            String object = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
            TRTCSetSocketUtil.getMeetingState(object, new OnParmsCallBack<TRTCOrderBean>() { // from class: com.shineyue.pm.web.WebActivity.49
                @Override // com.shineyue.pm.modle_chat.chat_history.dao.OnParmsCallBack
                public void onParmsResult(TRTCOrderBean result) {
                    if (result.code != 0) {
                        ToastUtil.toast(WebActivity.this.mContext, "msg", result.msg, ToastType.FAIL);
                        return;
                    }
                    if (result.data.meetingState != 0) {
                        if (result.data.lanchUserId.equals(Utils.getMsg(WebActivity.this.mContext, "userid"))) {
                            LiveAnchorActivity.UserInfo selfInfo = new LiveAnchorActivity.UserInfo();
                            selfInfo.userId = Utils.getMsg(WebActivity.this.mContext, "userid");
                            selfInfo.userName = Utils.getMsg(WebActivity.this.mContext, c.e);
                            LiveAnchorActivity.LiveTimeInfo timeInfo = new LiveAnchorActivity.LiveTimeInfo();
                            timeInfo.startTime = WebActivity.this.format(result.data.startTime);
                            timeInfo.endTime = WebActivity.this.format(result.data.endTime);
                            LiveAnchorActivity.startAnchorActivity(WebActivity.this.mContext, selfInfo, result.data.meetingId, result.data.meetingName, timeInfo, WebActivity.this.addInfo);
                            return;
                        }
                        LiveAnchorActivity.LiveTimeInfo liveTimeInfo = new LiveAnchorActivity.LiveTimeInfo();
                        LiveAnchorActivity.UserInfo mUser = new LiveAnchorActivity.UserInfo();
                        mUser.userId = Utils.getMsg(WebActivity.this.mContext, "userid");
                        mUser.userName = Utils.getMsg(WebActivity.this.mContext, c.e);
                        LiveAnchorActivity.UserInfo hUser = new LiveAnchorActivity.UserInfo();
                        hUser.userId = result.data.lanchUserId;
                        hUser.userName = result.data.lanchUserName;
                        liveTimeInfo.startTime = WebActivity.this.format(result.data.startTime);
                        liveTimeInfo.endTime = WebActivity.this.format(result.data.endTime);
                        LiveAudienceActivity.startAudienceActivity(WebActivity.this.mContext, mUser, hUser, liveTimeInfo, result.data.meetingName, result.data.meetingId, WebActivity.this.addInfo);
                        return;
                    }
                    Intent intent = new Intent(WebActivity.this.mContext, (Class<?>) LiveFinishActivity.class);
                    intent.putExtra("meetingId", result.data.meetingId);
                    WebActivity.this.mContext.startActivity(intent);
                }
            });
        }
        this.mettingId = "";
    }

    public Date format(String date) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = fmt.parse(date);
            return date1;
        } catch (Exception e) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startMeeting() {
        if (CallingActivity.IS_LIVE) {
            ToastUtil.toast(this.mContext, "msg", "当前正在会议中，请稍后", ToastType.WARNING);
        } else {
            CallingActivity.UserInfo userInfo = new CallingActivity.UserInfo();
            userInfo.userId = Utils.getMsg(this.mContext, "userid");
            userInfo.userName = Utils.getMsg(this.mContext, c.e);
            CallingActivity.startForWebMeeting(DavikActivityUtils.getScreenManager().getCurrentActivity(), userInfo, "single_changjing", this.yqrIds, this.yqrNames, this.mettingId, this.toName, this.sxtFlag, this.mkfFlag);
        }
        this.mettingId = "";
        this.toName = "";
        this.yqrIds = "";
        this.yqrNames = "";
        this.sxtFlag = false;
        this.mkfFlag = false;
        this.sphyType = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void joinMeeting() {
        if (CallingActivity.IS_LIVE) {
            ToastUtil.toast(this.mContext, "msg", "当前正在会议中，请稍后", ToastType.WARNING);
            return;
        }
        if (NetUtils.checkWifi(this.mContext).intValue() == 3) {
            ToastUtil.toast(this.mContext, ToastInfo.netError, ToastType.FAIL);
            return;
        }
        int i = this.sphyType;
        if (i == 3) {
            KLog.i(this.TAG, this.meetingParams);
            TRTCSetSocketUtil.getMeetingState(this.meetingParams, new OnParmsCallBack<TRTCOrderBean>() { // from class: com.shineyue.pm.web.WebActivity.50
                @Override // com.shineyue.pm.modle_chat.chat_history.dao.OnParmsCallBack
                public void onParmsResult(TRTCOrderBean result) {
                    if (result == null) {
                        ToastUtil.toast(WebActivity.this.mContext, ToastInfo.netError, ToastType.FAIL);
                        return;
                    }
                    if (result.code != 0) {
                        ToastUtil.toast(WebActivity.this.mContext, "msg", result.msg, ToastType.FAIL);
                        return;
                    }
                    if (result.data != null) {
                        KLog.i(WebActivity.this.TAG, Integer.valueOf(result.data.meetingState));
                        if (result.data.meetingState == 0) {
                            Intent intent = new Intent(WebActivity.this.mContext, (Class<?>) CallingFinishActivity.class);
                            intent.putExtra("meetingId", WebActivity.this.mettingId);
                            WebActivity.this.mContext.startActivity(intent);
                            return;
                        } else {
                            WebActivity webActivity = WebActivity.this;
                            webActivity.joinMeeting(webActivity.sphyType);
                            return;
                        }
                    }
                    ToastUtil.toast(WebActivity.this.mContext, "msg", "入会失败，会议不存在 ，请稍后尝试", ToastType.FAIL);
                }
            });
        } else {
            joinMeeting(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void joinMeeting(final int type) {
        TRTCSetSocketUtil.getMeetingUserInfo(this.mettingId, new OnParmsCallBack<TRTCUserBean>() { // from class: com.shineyue.pm.web.WebActivity.51
            @Override // com.shineyue.pm.modle_chat.chat_history.dao.OnParmsCallBack
            public void onParmsResult(TRTCUserBean result) {
                KLog.i(WebActivity.this.TAG, result.toString());
                KLog.i(WebActivity.this.TAG, Integer.valueOf(type));
                if (result == null || result.data == null) {
                    ToastUtil.toast(OaApplication.application, "error", "", ToastType.FAIL);
                } else if (result.data.size() > 0) {
                    if (result.data.size() > 300) {
                        ToastUtil.toast(WebActivity.this.mContext, "msg", "当前会议人数达到上限", ToastType.WARNING);
                    } else {
                        CallingActivity.UserInfo selfInfo = new CallingActivity.UserInfo();
                        selfInfo.userId = Utils.getMsg(OaApplication.application, "userid");
                        selfInfo.userName = Utils.getMsg(OaApplication.application, c.e);
                        List<CallingActivity.UserInfo> mList = new ArrayList<>();
                        mList.add(selfInfo);
                        CallingActivity.UserInfo userInfo = new CallingActivity.UserInfo();
                        userInfo.userId = Utils.getMsg(WebActivity.this.mContext, "userid");
                        userInfo.userName = Utils.getMsg(WebActivity.this.mContext, c.e);
                        if (type == 3) {
                            CallingActivity.startJoinVideoMeeting(DavikActivityUtils.getScreenManager().getCurrentActivity(), selfInfo, userInfo, 2, WebActivity.this.mettingId, WebActivity.this.toName, "single_changjing", true);
                        } else {
                            CallingActivity.startJoinVideoMeeting(DavikActivityUtils.getScreenManager().getCurrentActivity(), selfInfo, userInfo, 2, WebActivity.this.mettingId, WebActivity.this.toName, "single_changjing");
                        }
                    }
                } else if (type == 3) {
                    if (result.data.size() > 300) {
                        ToastUtil.toast(WebActivity.this.mContext, "msg", "当前会议人数达到上限", ToastType.WARNING);
                    } else {
                        CallingActivity.UserInfo selfInfo2 = new CallingActivity.UserInfo();
                        selfInfo2.userId = Utils.getMsg(OaApplication.application, "userid");
                        selfInfo2.userName = Utils.getMsg(OaApplication.application, c.e);
                        List<CallingActivity.UserInfo> mList2 = new ArrayList<>();
                        mList2.add(selfInfo2);
                        CallingActivity.UserInfo userInfo2 = new CallingActivity.UserInfo();
                        userInfo2.userId = Utils.getMsg(WebActivity.this.mContext, "userid");
                        userInfo2.userName = Utils.getMsg(WebActivity.this.mContext, c.e);
                        if (type == 3) {
                            CallingActivity.startJoinVideoMeeting(DavikActivityUtils.getScreenManager().getCurrentActivity(), selfInfo2, userInfo2, 2, WebActivity.this.mettingId, WebActivity.this.toName, "single_changjing", true);
                        } else {
                            CallingActivity.startJoinVideoMeeting(DavikActivityUtils.getScreenManager().getCurrentActivity(), selfInfo2, userInfo2, 2, WebActivity.this.mettingId, WebActivity.this.toName, "single_changjing");
                        }
                    }
                } else {
                    ToastUtil.toast(OaApplication.application, "msg", "会议未开始", ToastType.WARNING);
                }
                WebActivity.this.mettingId = "";
                WebActivity.this.toName = "";
                WebActivity.this.sxtFlag = false;
                WebActivity.this.mkfFlag = false;
                WebActivity.this.sphyType = 0;
            }
        });
    }

    @JavascriptInterface
    public void joinyuyueMeeting(String str) {
        KLog.i(this.TAG, str);
        try {
            if (Utils.getBooleanMsg(OaApplication.application, "yspFlag")) {
                JSONObject jsonObject = new JSONObject(str);
                this.meetingParams = jsonObject.optJSONObject("params").toString();
                JSONObject js = new JSONObject(this.meetingParams);
                this.mettingId = js.optString("meetingId");
                this.toName = js.optString("toName");
                this.sxtFlag = js.optBoolean("sxtFlag");
                this.mkfFlag = js.optBoolean("mkfFlag");
                this.sphyType = 3;
                checkVideoPermission();
            } else {
                ToastUtil.toast(this.mContext, ToastInfo.notEnabled, "视频会议", ToastType.FAIL);
            }
        } catch (Exception e) {
            KLog.i(this.TAG, e.toString());
        }
    }

    @JavascriptInterface
    public void joinyuyueZhibo(String str) {
        KLog.i(this.TAG, str);
        try {
            if (Utils.getBooleanMsg(OaApplication.application, "yspFlag")) {
                JSONObject jsonObject = new JSONObject(str);
                this.mettingId = jsonObject.optString("meetingId");
                if (jsonObject.has("addInfo")) {
                    this.addInfo = jsonObject.getString("addInfo").toString();
                }
                this.sphyType = 4;
                checkVideoPermission();
                return;
            }
            ToastUtil.toast(this.mContext, ToastInfo.notEnabled, "视频会议", ToastType.FAIL);
        } catch (Exception e) {
            KLog.i(this.TAG, e.toString());
        }
    }

    @JavascriptInterface
    public void getAppIconAndMore() {
        Map<String, Object> map = new HashMap<>();
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.logo_white);
            String icon = bitmapToBase64(bmp);
            map.put("appIcon", icon);
            map.put("appName", getResources().getString(R.string.app_name));
            map.put(AttributionReporter.APP_VERSION, Utils.getVersionName(this.mContext));
            JSONObject result = new JSONObject(com.alibaba.fastjson.JSONObject.toJSON(map).toString());
            KLog.i(this.TAG, result.toString());
            execJsMethod("javascript:getAppIconAndMoreResults(" + result.toString() + ")");
        } catch (Exception e) {
            e.toString();
        }
    }

    @JavascriptInterface
    public void getFontSize(String str) {
        KLog.i(this.TAG, str);
        try {
            JSONObject jsonObject = new JSONObject(str);
            String func = jsonObject.optString("func");
            if (Utils.getBooleanMsg(OaApplication.application, "CAREMODE")) {
                execJsMethod("javascript:" + func + "('1')");
            } else {
                execJsMethod("javascript:" + func + "('0')");
            }
        } catch (Exception e) {
            KLog.i(this.TAG, e.toString());
        }
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, 0);
    }

    @JavascriptInterface
    public void yuyueMeeting(String str) {
        KLog.i(this.TAG, str);
        try {
            JSONObject jsonObject = new JSONObject(str);
            String params = jsonObject.optJSONObject("params").toString();
            TRTCSetSocketUtil.orderMeeting(params, new OnParmsCallBack<TRTCOrderBean>() { // from class: com.shineyue.pm.web.WebActivity.52
                @Override // com.shineyue.pm.modle_chat.chat_history.dao.OnParmsCallBack
                public void onParmsResult(TRTCOrderBean result) {
                    String results;
                    KLog.i(WebActivity.this.TAG, result.toString());
                    if (result != null) {
                        try {
                            if (result.code == 0) {
                                result.success = true;
                            } else {
                                result.success = false;
                            }
                            Gson gson = new Gson();
                            String results2 = gson.toJson(result);
                            new HashMap();
                            Map json = (Map) JSON.parse(results2);
                            json.put("allData", JSON.parse(results2));
                            results = com.alibaba.fastjson.JSONObject.toJSON(json).toString();
                        } catch (Exception e) {
                            KLog.i(WebActivity.this.TAG, e.toString());
                            WebActivity.this.execJsMethod("javascript:yuyueMeetingResults('{\"success\":false,\"msg\":\"预约失败\"}')");
                            return;
                        }
                    } else {
                        results = "{\"success\":false,\"msg\":\"预约失败\"}";
                    }
                    KLog.i(WebActivity.this.TAG, results);
                    WebActivity.this.execJsMethod("javascript:yuyueMeetingResults('" + results + "')");
                }
            });
        } catch (Exception e) {
            KLog.i(this.TAG, e.toString());
            execJsMethod("javascript:yuyueMeetingResults('{\"success\":false,\"msg\":\"预约失败\"}')");
        }
    }

    @JavascriptInterface
    public void endMeeting(String str) {
        KLog.i(this.TAG, str);
        try {
            JSONObject jsonObject = new JSONObject(str);
            String params = jsonObject.optJSONObject("params").toString();
            TRTCSetSocketUtil.stopMeeting(params, new OnParmsCallBack<ToolsBean>() { // from class: com.shineyue.pm.web.WebActivity.53
                @Override // com.shineyue.pm.modle_chat.chat_history.dao.OnParmsCallBack
                public void onParmsResult(ToolsBean result) {
                    String results;
                    if (result != null) {
                        try {
                            if (result.code == 0) {
                                result.success = true;
                                Gson gson = new Gson();
                                results = gson.toJson(result);
                            } else {
                                result.success = false;
                                Gson gson2 = new Gson();
                                results = gson2.toJson(result);
                            }
                        } catch (Exception e) {
                            KLog.i(WebActivity.this.TAG, e.toString());
                            WebActivity.this.execJsMethod("javascript:endMeetingResults('{\"success\":false,\"msg\":\"操作失败\"}')");
                            return;
                        }
                    } else {
                        results = "{\"success\":false,\"msg\":\"操作失败\"}";
                    }
                    KLog.i(WebActivity.this.TAG, results);
                    WebActivity.this.execJsMethod("javascript:endMeetingResults('" + results + "')");
                }
            });
        } catch (Exception e) {
            KLog.i(this.TAG, e.toString());
            execJsMethod("javascript:endMeetingResults('{\"success\":false,\"msg\":\"操作失败\"}')");
        }
    }

    @JavascriptInterface
    public void versionUpdate() {
        if (AppUpgradeService.isDOWNLOAD) {
            ToastUtil.toast(this.mContext, ToastInfo.inOperation, "下载", ToastType.WARNING);
        } else {
            checkversionNew();
        }
    }

    @JavascriptInterface
    public void refreshToken() {
        OaApplication.refreshToken = true;
        KLog.i(this.TAG, Boolean.valueOf(OaApplication.refreshToken));
        EventBus.getDefault().post(new Notify.Message(70003));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void checkversionNew() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("zxbm", Utils.getMsg(this, "zxjgbm"));
        map.put("qdlx", "app_02");
        JSONObject jsonObject = new JSONObject(map);
        KLog.i(this.TAG, jsonObject);
        OkGoNetManager.getInstance();
        ((PostRequest) OkGo.post(OkGoNetManager.APP_URL + OkGoNetManager.CHECK_VERSION).tag(this)).upJson(jsonObject.toString()).execute(new MyStringCallbackNew() { // from class: com.shineyue.pm.web.WebActivity.54
            @Override // com.shineyue.pm.network.callback.MyStringCallbackNew, com.lzy.okgo.callback.AbsCallback
            public void onSuccess(String s, Call call, Response response) {
                KLog.i(WebActivity.this.TAG, s);
                try {
                    JSONObject object = new JSONObject(s);
                    boolean success = object.optBoolean(ToastType.SUCCESS);
                    if (success) {
                        JSONArray results = object.optJSONArray("results");
                        if (results.length() > 0) {
                            int versionCode = results.getJSONObject(0).optInt("advercode");
                            if (versionCode > WebActivity.this.getVersionCode()) {
                                String appurl = results.getJSONObject(0).optString("downloadurl");
                                String appgxnr = "";
                                if (results.getJSONObject(0).has("gxnr")) {
                                    appgxnr = results.getJSONObject(0).optString("gxnr");
                                }
                                String version = results.getJSONObject(0).has("version") ? results.getJSONObject(0).optString("version") : "";
                                if (appurl.trim().length() > 0) {
                                    WebActivity.this.showDialogUpdate(appurl, appgxnr, version);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override // com.shineyue.pm.network.callback.MyStringCallbackNew, com.lzy.okgo.callback.AbsCallback
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                KLog.i(WebActivity.this.TAG, e.toString());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getVersionCode() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDialogUpdate(final String appurl, String appgxnr, final String version) {
        if (this.mContext != null) {
            NewUpdateDialog newUpdateDialog = this.updateDialog;
            if (newUpdateDialog != null && newUpdateDialog.isShowing()) {
                return;
            }
            this.updateDialog = null;
            this.updateDialog = new NewUpdateDialog(this.mContext);
            String filePath = Utils.getMsg(this.mContext, "NewVersionFile" + version + Utils.getMsg(OaApplication.application, "zxjgbm"));
            this.updateDialog.show();
            if (!TextUtils.isEmpty(filePath)) {
                this.updateDialog.setUpdateRootShow(false);
                this.updateDialog.setSubmitText("立即安装");
            } else {
                this.updateDialog.setUpdateRootShow(true);
                this.updateDialog.setSubmitText("立即更新");
            }
            this.updateDialog.setOnDialogOnclickListener(new NewUpdateDialog.dialogOnclickListener() { // from class: com.shineyue.pm.web.WebActivity.55
                @Override // com.shineyue.pm.dialog.NewUpdateDialog.dialogOnclickListener
                public void onClick(View v) {
                    if ("立即安装".equals(WebActivity.this.updateDialog.getSubmitText())) {
                        WebActivity.this.updateDialog.getLl_progress().setVisibility(8);
                    }
                    WebActivity webActivity = WebActivity.this;
                    webActivity.loadNewVersionProgress(webActivity.updateDialog, appurl, version);
                }

                @Override // com.shineyue.pm.dialog.NewUpdateDialog.dialogOnclickListener
                public void onBackGroundClick(View v) throws XmlPullParserException, IOException {
                    WebActivity.this.downLoadApk(appurl, version);
                    WebActivity.this.updateDialog.dismiss();
                }
            });
            this.updateDialog.setContent(appgxnr);
            if (Utils.haveValue(version)) {
                this.updateDialog.setVersion(version);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downLoadApk(String url, String version) throws XmlPullParserException, IOException {
        String filePath = Utils.getMsg(this.mContext, "NewVersionFile" + version + Utils.getMsg(OaApplication.application, "zxjgbm"));
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            installApk(file);
        } else {
            if (OaApplication.isUpLoadVersion) {
                return;
            }
            Intent intent = new Intent(OaApplication.application, (Class<?>) AppUpgradeService.class);
            intent.putExtra("mDownloadUrl", url);
            intent.putExtra("version", version);
            intent.putExtra("flag", "1");
            OaApplication.application.startService(intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadNewVersionProgress(final NewUpdateDialog updateDialog, String uri, final String version) {
        try {
            String filePath = Utils.getMsg(this.mContext, "NewVersionFile" + version + Utils.getMsg(OaApplication.application, "zxjgbm"));
            if (!TextUtils.isEmpty(filePath)) {
                File file = new File(filePath);
                installApk(file);
                updateDialog.dismiss();
            } else {
                if (OaApplication.isUpLoadVersion) {
                    return;
                }
                String strDBPath3 = CachePzthConfigs.getIns().getFilesDir(false, "other");
                long time = System.currentTimeMillis();
                OkGo.get(uri).execute(new FileCallback(strDBPath3, time + "updata.apk") { // from class: com.shineyue.pm.web.WebActivity.56
                    @Override // com.lzy.okgo.callback.AbsCallback
                    public void onSuccess(final File file2, Call call, Response response) {
                        ThreadPoolManager.getInstance().execute(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.56.1
                            @Override // java.lang.Runnable
                            public void run() throws XmlPullParserException, IOException {
                                try {
                                    Thread.sleep(3000L);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Utils.saveMsg(OaApplication.application, "NewVersionFile" + version + Utils.getMsg(OaApplication.application, "zxjgbm"), file2.getAbsolutePath());
                                WebActivity.this.installApk(file2);
                                updateDialog.dismiss();
                            }
                        });
                    }

                    @Override // com.lzy.okgo.callback.AbsCallback
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                        updateDialog.setProgress(progress);
                        updateDialog.setSpeed(networkSpeed);
                        updateDialog.setPersont(progress);
                    }

                    @Override // com.lzy.okgo.callback.AbsCallback
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Toast.makeText(WebActivity.this.getApplicationContext(), "下载新版本失败", 1).show();
                        updateDialog.dismiss();
                    }
                });
            }
        } catch (Exception e) {
            KLog.i(e.toString());
        }
    }

    protected void installApk(File file) throws XmlPullParserException, IOException {
        if (file != null && file.exists()) {
            Intent intent = new Intent("android.intent.action.INSTALL_PACKAGE");
            intent.setAction("android.intent.action.VIEW");
            if (Build.VERSION.SDK_INT >= 24) {
                KLog.e(this.TAG, this.mContext.getPackageName() + ".fileprovider");
                Uri apkUri = FileProvider.getUriForFile(getApplicationContext(), this.mContext.getPackageName() + ".fileprovider", file);
                intent.addFlags(1);
                intent.addFlags(268435456);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                intent.addFlags(268435456);
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            startActivity(intent);
        }
    }

    @JavascriptInterface
    public void newstextTospeech(String str) {
        KLog.i(this.TAG, str);
        try {
            final JSONObject jsonObject = new JSONObject(str);
            String openyy = jsonObject.optString("openyy");
            if ("1".equals(openyy)) {
                runOnUiThread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.57
                    @Override // java.lang.Runnable
                    public void run() {
                        String tts = jsonObject.optString("tts");
                        WebActivity.this.text = tts;
                        KLog.i(WebActivity.this.TAG, WebActivity.this.text);
                        WebActivity.this.setTextToSpeech();
                    }
                });
            }
        } catch (Exception e) {
            KLog.i(this.TAG, e.toString());
        }
    }

    private void showTextToSpeechPopupWindow() {
        KLog.i(this.TAG, "showTextToSpeechPopupWindow");
        if (this.textToSpeechPopwWindow == null) {
            View contentView = LayoutInflater.from(this.mContext).inflate(R.layout.item_text_to_speech, (ViewGroup) null);
            TextView tv_title = (TextView) contentView.findViewById(R.id.tv_title);
            this.tv_time = (TextView) contentView.findViewById(R.id.tv_time);
            this.seekbar = (SeekBar) contentView.findViewById(R.id.seekbar);
            final ImageView iv_start = (ImageView) contentView.findViewById(R.id.iv_start);
            ImageView iv_copy = (ImageView) contentView.findViewById(R.id.iv_copy);
            ImageView iv_close = (ImageView) contentView.findViewById(R.id.iv_close);
            tv_title.setText(this.textToSpeechTitle);
            iv_close.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.58
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    WebActivity.this.textToSpeechPopwWindow.dismiss();
                }
            });
            iv_start.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.59
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= 23 && WebActivity.this.isPrepare) {
                        KLog.i(WebActivity.this.TAG, Boolean.valueOf(WebActivity.this.isPlayer));
                        if (WebActivity.this.isPlayer) {
                            WebActivity.this.ttsManager.pause();
                            iv_start.setImageResource(R.mipmap.text_to_speech_start);
                            WebActivity.this.iv_text_to_speech.setImageResource(R.mipmap.news_text_to_speech_false);
                            WebActivity.this.iv_speech.setImageResource(R.mipmap.news_text_to_speech_false);
                            WebActivity.this.isPlayer = false;
                            return;
                        }
                        WebActivity.this.ttsManager.resume();
                        iv_start.setImageResource(R.mipmap.text_to_speech_stop);
                        WebActivity.this.iv_text_to_speech.setImageResource(R.mipmap.news_text_to_speech_true);
                        WebActivity.this.iv_speech.setImageResource(R.mipmap.news_text_to_speech_true);
                        WebActivity.this.isPlayer = true;
                    }
                }
            });
            iv_copy.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.60
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    WebActivity.this.textToSpeechPopwWindow.dismiss();
                }
            });
            this.seekbar.setMax(this.duration / 1000);
            this.seekbar.setProgress(0);
            this.tv_time.setText("0:00/" + intToTime(this.duration / 1000));
            this.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.shineyue.pm.web.WebActivity.61
                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                }

                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onStopTrackingTouch(SeekBar seekBar) {
                    KLog.i(WebActivity.this.TAG, Integer.valueOf(seekBar.getProgress()));
                    WebActivity.this.ttsManager.getPlayer().seekTo(seekBar.getProgress());
                }
            });
            PopupWindow popupWindow = new PopupWindow(contentView, -1, -1);
            this.textToSpeechPopwWindow = popupWindow;
            popupWindow.setBackgroundDrawable(new ColorDrawable(0));
            this.textToSpeechPopwWindow.setOutsideTouchable(false);
            this.textToSpeechPopwWindow.setTouchable(true);
            contentView.measure(0, 0);
            this.textToSpeechPopwWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.shineyue.pm.web.WebActivity.62
                @Override // android.widget.PopupWindow.OnDismissListener
                public void onDismiss() {
                    WebActivity.this.isShow = false;
                    if (WebActivity.this.isPlayer) {
                        WebActivity.this.rl_text_to_speech.setVisibility(8);
                    }
                }
            });
        }
        this.textToSpeechPopwWindow.showAtLocation(this.ll, 0, 0, 0);
        this.isShow = true;
    }

    private String intToTime(int pro) {
        String s;
        String m;
        String h = "";
        int ss = pro % 60;
        if (ss < 10) {
            s = "0" + ss;
        } else {
            s = ss + "";
        }
        int mm = pro / 60;
        if (mm < 10) {
            m = "0" + mm;
        } else if (mm < 60) {
            m = mm + "";
        } else {
            int mmm = mm % 60;
            if (mmm < 10) {
                m = "0" + mmm;
            } else {
                m = mmm + "";
            }
            h = (mm / 60) + "";
        }
        if (Utils.haveValue(h)) {
            String time = h + ":" + m + ":" + s;
            return time;
        }
        String time2 = m + ":" + s;
        return time2;
    }

    private void bindTTSService() {
        KLog.i(this.TAG, "bindTTSService");
        if (!this.BINDINGACTIVITY) {
            setServiceConnection();
            Intent intent = new Intent(OaApplication.application, (Class<?>) TTSService.class);
            bindService(intent, this.serviceConnection, 1);
        }
    }

    /* JADX INFO: renamed from: com.shineyue.pm.web.WebActivity$63, reason: invalid class name */
    class AnonymousClass63 implements ServiceConnection {
        AnonymousClass63() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                KLog.i(WebActivity.this.TAG, name);
                WebActivity.this.BINDINGACTIVITY = true;
                WebActivity.this.playerService = new PlayerService() { // from class: com.shineyue.pm.web.WebActivity.63.1
                    @Override // com.shineyue.pm.web.PlayerService
                    public void isPlayer(final boolean player) {
                        WebActivity.this.runOnUiThread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.63.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                WebActivity.this.isPlayer = player;
                                if (WebActivity.this.isPlayer) {
                                    WebActivity.this.iv_speech.setImageResource(R.mipmap.news_text_to_speech_true);
                                } else {
                                    WebActivity.this.iv_speech.setImageResource(R.mipmap.news_text_to_speech_false);
                                }
                            }
                        });
                    }
                };
                WebActivity.this.localBinder = (TTSService.LocalBinder) service;
                WebActivity.this.localBinder.setOnPlayerStateChange(WebActivity.this.playerService);
                WebActivity.this.localBinder.prepare(WebActivity.this.getApplicationContext(), WebActivity.this.text);
            } catch (Exception e) {
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            WebActivity.this.BINDINGACTIVITY = false;
            WebActivity.this.localBinder = null;
        }
    }

    private void setServiceConnection() {
        this.serviceConnection = new AnonymousClass63();
    }

    @Override // com.shineyue.pm.BasicActivity
    public void showPermissionNotificationPop(int type) {
        if (type != 0) {
            return;
        }
        String mTitle = null;
        String mDetail = null;
        if (type == 0) {
            mTitle = "位置权限使用说明";
            mDetail = "在下方弹窗中选择允许后，你可以使用发考勤打卡、玥空间发布中获取所在位置等功能。";
        } else if (type == 1) {
            mTitle = "存储权限使用说明";
            mDetail = "在下方弹窗中选择允许后，你可以在聊天中选择相册里的图片视频进行发送、发送文件；保存聊天中的图片、视频；个人头像-从手机相册上传、保存图片；个人名片-头像本地上传；邮件-写信上传图片、上传附件；备忘录-上传图片；反馈-上传图片、文件；工作台事项-上传图片、文件。";
        } else if (type == 2) {
            mTitle = "相机权限使用说明";
            mDetail = "在下方弹窗中选择允许后，你可以使用扫一扫；聊天中拍摄照片或视频；视频通话；直播；个人头像-拍照；邮件-写信拍照；备忘录-拍照；反馈-拍照；玥空间发布；工作台事项-拍照等功能。";
        } else if (type == 3) {
            mTitle = "麦克风权限使用说明";
            mDetail = "在下方弹窗中选择允许后，你可以使用发送语音、语音通话、视频通话、直播等功能。";
        } else if (type == 4) {
            mTitle = "电话权限使用说明";
            mDetail = "在下方弹窗中选择允许后，你可以使用聊天中拨打电话功能。";
        }
        final String finalMTitle = mTitle;
        final String finalMDetail = mDetail;
        runOnUiThread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.64
            @Override // java.lang.Runnable
            public void run() {
                WebActivity.this.tv_permission_name.setText(finalMTitle);
                WebActivity.this.tv_permission_detail.setText(finalMDetail);
                WebActivity.this.rl_qx_content.setVisibility(0);
                WebActivity.this.isShowQxDialog = true;
            }
        });
    }

    @Override // com.shineyue.pm.BasicActivity
    public void hidePermissionNotificationPop() {
        super.hidePermissionNotificationPop();
        runOnUiThread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.65
            @Override // java.lang.Runnable
            public void run() {
                if (!PermissionsUtils.getInstance().isShowLocationWeb) {
                    WebActivity.this.rl_qx_content.setVisibility(8);
                    WebActivity.this.isShowQxDialog = false;
                }
            }
        });
    }

    private void updataFromLastReadTime() {
        try {
            this.chatWindowListPresenter.getChatWindowFromDb(this.toUser, new ChatWindowListDaoManager.OnChatWindowItemResultListener() { // from class: com.shineyue.pm.web.WebActivity.67
                @Override // com.shineyue.pm.modle_chat.presenter.chat_window_list.ChatWindowListDaoManager.OnChatWindowItemResultListener
                public void onChatWindowItemResult(ChatWindowItem chatWindowItem) {
                    if (chatWindowItem == null || OaApplication.chatService == null) {
                        return;
                    }
                    ChatServiceNew chatServiceNew = OaApplication.chatService;
                    if (ChatServiceNew.ws != null) {
                        ChatServiceNew chatServiceNew2 = OaApplication.chatService;
                        if (ChatServiceNew.ws.isOpen()) {
                            Message msg = chatWindowItem.getContent().content;
                            msg.notReadNum = 0;
                            msg.fromLastReadTime = System.currentTimeMillis();
                            WebActivity.this.chatWindowListPresenter.updateDbInfoByMessage(msg, null);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.shineyue.pm.modle_maillist.presenter.MailView
    public void onMailResult(boolean fromDb, List<MailItem> mailList) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void isHave() {
        String qycode;
        HashMap<String, Object> map = new HashMap<>();
        try {
            JSONObject result = new JSONObject(Utils.getMsg(this.mContext, "UserXx"));
            map.put("czlx", "00");
            map.put("userid", result.get("userid").toString());
            map.put("ffbm", "05");
            map.put("gid", this.toUser);
            map.put("groupuserid", result.get("userid").toString());
            map.put("jgbm", result.get("jgbm").toString());
            map.put("ywfl", "00");
            map.put("ywlb", "00");
            map.put("zhbh", "");
            map.put("zxbm", result.get("zxjgbm").toString());
            map.put("khbh", "");
            if (result.has("qycode")) {
                qycode = result.get("qycode").toString();
            } else {
                qycode = "shineyue";
            }
            map.put("qycode", qycode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(map);
        KLog.i(this.TAG, "jsonObject" + jsonObject);
        OkGoNetManager.getInstance();
        ((PostRequest) OkGo.post(OkGoNetManager.APP_URL + OkGoNetManager.QUNZUHAVE).tag(this)).upJson(jsonObject.toString()).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.68
            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onSuccess(String s, Call call, Response response) {
                KLog.i(WebActivity.this.TAG, ToastType.SUCCESS + s);
                try {
                    JSONObject js = new JSONObject(s);
                    if (js.has("data")) {
                        JSONArray ja = js.getJSONArray("data");
                        if (ja.length() == 0) {
                            String chatId = Utils.getMsg(OaApplication.application, "chatId");
                            chatId.split(",");
                            ThreadPoolManager.getInstance().execute(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.68.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    List<Message> msgs = GreenDaoHelper.getDaoSession().getMessageDao().queryBuilder().where(MessageDao.Properties.ToGroup.eq(WebActivity.this.toUser), new WhereCondition[0]).build().list();
                                    GreenDaoHelper.getDaoSession().getMessageDao().deleteInTx(msgs);
                                }
                            });
                            try {
                                WebActivity.this.chatWindowListPresenter.deleteChatWindow(Utils.getMsg(OaApplication.application, "userid"), WebActivity.this.toUser, "2");
                                WebActivity.this.qunZuItemInfoDaoManager.deleteGroupItemInfo(WebActivity.this.toUser);
                            } catch (Exception e2) {
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this.mContext, R.style.CustomAlertDialog);
                            builder.setTitle("此群不存在").setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.68.2
                                @Override // android.content.DialogInterface.OnClickListener
                                public void onClick(DialogInterface dialog, int which) {
                                    Notify.Message message = new Notify.Message(Notify.ChatMsg3);
                                    message.operation = 1;
                                    Message subMessage = new Message();
                                    subMessage.setToGroup(WebActivity.this.toUser);
                                    subMessage.setToUserId(WebActivity.this.toUser);
                                    message.message = subMessage;
                                    EventBus.getDefault().post(message);
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                            return;
                        }
                        JSONObject jsonObject1 = ja.getJSONObject(0);
                        WebActivity.this.sfgly = jsonObject1.optBoolean("sfgly");
                        WebActivity.this.sfqz = jsonObject1.optBoolean("sfqz");
                        if (WebActivity.this.chatAdapter != null) {
                            WebActivity.this.chatAdapter.setSfqz(WebActivity.this.sfqz);
                            WebActivity.this.chatAdapter.setSfgly(WebActivity.this.sfgly);
                        }
                        if (!jsonObject1.has("sfjy")) {
                            WebActivity.this.initLinisterChat();
                            return;
                        }
                        final String jy = jsonObject1.get("sfjy").toString();
                        WebActivity webActivity = WebActivity.this;
                        webActivity.getQunZuItemInfo(webActivity.toUser, new QunZuItemInfoDaoManager.OnGroupItemResultListener() { // from class: com.shineyue.pm.web.WebActivity.68.3
                            @Override // com.shineyue.pm.modle_chat.qunzu.dao.QunZuItemInfoDaoManager.OnGroupItemResultListener
                            public void onGroupItemResult(QunZuItemInfoBean qunZuItemInfoBean) {
                                if (qunZuItemInfoBean != null) {
                                    qunZuItemInfoBean.sfjy = jy;
                                    GreenDaoHelper.getDaoSession().getQunZuItemInfoBeanDao().update(qunZuItemInfoBean);
                                    String str = jy;
                                    if (str == null) {
                                        WebActivity.this.initLinisterChat();
                                        return;
                                    }
                                    if (str.equals("1")) {
                                        WebActivity.this.sfjy = true;
                                        WebActivity.this.ll_sendmessage.setVisibility(8);
                                        WebActivity.this.ll_no_speaking.setVisibility(0);
                                        WebActivity.this.editEmojicon.setEnabled(false);
                                        WebActivity.this.editEmojicon.setHint("禁言中......");
                                        return;
                                    }
                                    WebActivity.this.sfjy = false;
                                    WebActivity.this.ll_sendmessage.setVisibility(0);
                                    WebActivity.this.ll_no_speaking.setVisibility(8);
                                    WebActivity.this.editEmojicon.setHint("");
                                    WebActivity.this.initLinisterChat();
                                    return;
                                }
                                String str2 = jy;
                                if (str2 == null) {
                                    WebActivity.this.initLinisterChat();
                                    return;
                                }
                                if (str2.equals("1")) {
                                    WebActivity.this.sfjy = true;
                                    WebActivity.this.ll_sendmessage.setVisibility(8);
                                    WebActivity.this.ll_no_speaking.setVisibility(0);
                                    WebActivity.this.editEmojicon.setEnabled(false);
                                    WebActivity.this.editEmojicon.setHint("禁言中......");
                                    return;
                                }
                                WebActivity.this.sfjy = false;
                                WebActivity.this.ll_sendmessage.setVisibility(0);
                                WebActivity.this.ll_no_speaking.setVisibility(8);
                                WebActivity.this.editEmojicon.setHint("");
                                WebActivity.this.initLinisterChat();
                            }
                        });
                    }
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }

            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onError(Call call, Response response, Exception e2) {
                super.onError(call, response, e2);
                KLog.i(WebActivity.this.TAG, "error" + e2.getMessage());
                Utils.dismissProgressDialog();
                WebActivity.this.initLinisterChat();
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void isHuatiHave() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("groupId", this.toUser);
        map.put("lanchUserId", Utils.getMsg(this.mContext, "userid"));
        map.put("lanchUserName", Utils.getMsg(this.mContext, c.e));
        JSONObject jsonObject = new JSONObject(map);
        OkGoNetManager.getInstance();
        ((PostRequest) OkGo.post(OkGoNetManager.APP_URL + OkGoNetManager.HUATIDETAIL).tag(this)).upJson(jsonObject.toString()).execute(new AnonymousClass69());
    }

    /* JADX INFO: renamed from: com.shineyue.pm.web.WebActivity$69, reason: invalid class name */
    class AnonymousClass69 extends MyStringCallback {
        AnonymousClass69() {
        }

        @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
        public void onSuccess(String s, Call call, Response response) {
            try {
                HuaTiDetailBean huaTiDetailBean = (HuaTiDetailBean) new Gson().fromJson(s, HuaTiDetailBean.class);
                if (200 == huaTiDetailBean.getCode()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this.mContext, R.style.CustomAlertDialog);
                    builder.setTitle("话题已关闭").setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.69.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            ThreadPoolManager.getInstance().execute(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.69.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    List<Message> msgs = GreenDaoHelper.getDaoSession().getMessageDao().queryBuilder().where(MessageDao.Properties.ToGroup.eq(WebActivity.this.toUser), new WhereCondition[0]).build().list();
                                    GreenDaoHelper.getDaoSession().getMessageDao().deleteInTx(msgs);
                                }
                            });
                            try {
                                WebActivity.this.chatWindowListPresenter.deleteChatWindow(Utils.getMsg(OaApplication.application, "userid"), WebActivity.this.toUser, "3");
                                WebActivity.this.qunZuItemInfoDaoManager.deleteGroupItemInfo(WebActivity.this.toUser);
                            } catch (Exception e) {
                            }
                            Notify.Message message = new Notify.Message(Notify.ChatMsg3);
                            message.operation = 1;
                            Message subMessage = new Message();
                            subMessage.setToGroup(WebActivity.this.toUser);
                            subMessage.setToUserId(WebActivity.this.toUser);
                            message.message = subMessage;
                            EventBus.getDefault().post(message);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
            } catch (Exception e) {
                Utils.dismissProgressDialog();
                ToastUtil.toast(WebActivity.this.mContext, "error", "", ToastType.FAIL);
            }
        }

        @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
        public void onError(Call call, Response response, Exception e) {
            super.onError(call, response, e);
        }
    }

    private void showHuaTiCreateDialog() {
        PermissionDialog permissionDialog = this.huaTiDialog;
        if (permissionDialog == null) {
            PermissionDialog permissionDialog2 = new PermissionDialog(this.mContext);
            this.huaTiDialog = permissionDialog2;
            permissionDialog2.show();
            this.huaTiDialog.setTitle("创建话题");
            this.huaTiDialog.setContent("确定将此内容作为主题创建话题?");
            this.huaTiDialog.setSureAndCancle("确定", "取消");
            this.huaTiDialog.setOnDialogOnclickListener(new PermissionDialog.dialogOnclickListener() { // from class: com.shineyue.pm.web.WebActivity.70
                @Override // com.shineyue.pm.dialog.PermissionDialog.dialogOnclickListener
                public void onClick(View v) {
                    int id = v.getId();
                    if (id == R.id.tv_cancle) {
                        WebActivity.this.huaTiDialog.dismiss();
                        WebActivity.this.message1 = null;
                        return;
                    }
                    if (id == R.id.tv_sure) {
                        WebActivity webActivity = WebActivity.this;
                        if ("1".equals(webActivity.getTypeFromMessage(webActivity.message1))) {
                            WebActivity webActivity2 = WebActivity.this;
                            webActivity2.createHuaTi(webActivity2.message1.getContent(), "1", null);
                        } else {
                            WebActivity webActivity3 = WebActivity.this;
                            if ("2".equals(webActivity3.getTypeFromMessage(webActivity3.message1))) {
                                WebActivity webActivity4 = WebActivity.this;
                                webActivity4.createHuaTi("", "2", webActivity4.message1);
                            } else {
                                WebActivity webActivity5 = WebActivity.this;
                                if ("3".equals(webActivity5.getTypeFromMessage(webActivity5.message1))) {
                                    WebActivity webActivity6 = WebActivity.this;
                                    webActivity6.createHuaTi("", "3", webActivity6.message1);
                                }
                            }
                        }
                        WebActivity.this.huaTiDialog.dismiss();
                    }
                }
            });
            return;
        }
        permissionDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getTypeFromMessage(Message message) {
        int dotIndex;
        if (message.type.equals("3") || message.type.equals("4")) {
            return "1";
        }
        String fileName = message.fileName;
        String fileType = "";
        if (fileName != null && (dotIndex = fileName.lastIndexOf(StrUtil.DOT)) > 0) {
            fileType = fileName.substring(dotIndex, fileName.length()).toLowerCase();
            if (fileType.contains("?")) {
                fileType = fileType.substring(0, fileType.indexOf("?"));
            }
        }
        return FileUtil.getDealFileTyppe(fileType) == 1 ? "2" : "3";
    }

    private void downloadDateNew() {
        this.refreshTime = System.currentTimeMillis();
        downloadDate();
    }

    private void refreshQunzuData() {
        getQunZuItemInfo(this.toUser, new QunZuItemInfoDaoManager.OnGroupItemResultListener() { // from class: com.shineyue.pm.web.WebActivity.71
            @Override // com.shineyue.pm.modle_chat.qunzu.dao.QunZuItemInfoDaoManager.OnGroupItemResultListener
            public void onGroupItemResult(QunZuItemInfoBean qunZuItemInfoBean) {
                if (qunZuItemInfoBean == null || qunZuItemInfoBean.sfjy == null) {
                    if (WebActivity.this.groupNum != -1) {
                        if (UtilsChatTopic.isHuati(WebActivity.this.toUser)) {
                            WebActivity.this.tv_title_chat.setText("话题");
                        } else {
                            WebActivity.this.tv_title_chat.setText(WebActivity.this.toUserName);
                        }
                        if (!WebActivity.this.chatType.equals("4") && !WebActivity.this.chatType.equals("7")) {
                            WebActivity.this.tv_title_num.setVisibility(8);
                            return;
                        }
                        if (2 != Utils.judgeClient().intValue() || UtilsChatTopic.isHuati(WebActivity.this.toUser)) {
                            WebActivity.this.tv_title_num.setVisibility(8);
                            return;
                        }
                        WebActivity.this.tv_title_num.setVisibility(0);
                        WebActivity.this.tv_title_num.setText("(" + WebActivity.this.groupNum + "人)");
                        return;
                    }
                    return;
                }
                if (qunZuItemInfoBean.sfjy.equals("1")) {
                    WebActivity.this.editEmojicon.setEnabled(false);
                    WebActivity.this.editEmojicon.setHint("禁言中......");
                    WebActivity.this.isShutUp = true;
                } else {
                    WebActivity.this.initLinisterChat();
                    WebActivity.this.editEmojicon.setEnabled(true);
                    WebActivity.this.editEmojicon.setHint("");
                }
                if (Utils.haveValue(qunZuItemInfoBean.xmmc)) {
                    WebActivity.this.ll_company.setVisibility(8);
                    WebActivity.this.tv_company.setText("");
                    WebActivity.this.xmmc = qunZuItemInfoBean.xmmc;
                    WebActivity.this.xmbh = qunZuItemInfoBean.xmbh;
                }
                if (UtilsChatTopic.isHuati(WebActivity.this.toUser)) {
                    WebActivity.this.tv_title_chat.setText("话题");
                } else {
                    WebActivity.this.tv_title_chat.setText(WebActivity.this.toUserName);
                }
                if (!WebActivity.this.chatType.equals("4") && !WebActivity.this.chatType.equals("7")) {
                    WebActivity.this.tv_title_num.setVisibility(8);
                    return;
                }
                if (2 != Utils.judgeClient().intValue() || UtilsChatTopic.isHuati(WebActivity.this.toUser)) {
                    WebActivity.this.tv_title_num.setVisibility(8);
                    return;
                }
                WebActivity.this.tv_title_num.setVisibility(0);
                WebActivity.this.tv_title_num.setText("(" + qunZuItemInfoBean.qzrs + "人)");
            }
        });
    }

    private void initViewChat() {
        this.view = findViewById(R.id.view);
        this.iv_chat_person = (ImageView) findViewById(R.id.iv_chat_person);
        this.iv_qun_more = (ImageView) findViewById(R.id.iv_qun_more);
        this.iv_chat_qun_person = (ImageView) findViewById(R.id.iv_chat_qun_person);
        this.iv_qun_call = (ImageView) findViewById(R.id.iv_qun_call);
        this.tv_title_chat = (TextView) findViewById(R.id.tv_news_title_name);
        this.tv_title_num = (TextView) findViewById(R.id.tv_news_title_name_num);
        this.tv_total_num = (TextView) findViewById(R.id.tv_total_num);
        this.tv_total_num_new = (TextView) findViewById(R.id.tv_total_num_new);
        this.ll_no_speaking = (LinearLayout) findViewById(R.id.ll_no_speaking);
        this.ll_company = (LinearLayout) findViewById(R.id.ll_company);
        this.tv_company = (TextView) findViewById(R.id.tv_company);
        this.tv_dept = (TextView) findViewById(R.id.tv_dept);
        this.tv_detail = (TextView) findViewById(R.id.tv_chat_title_detail);
        this.ll_content = (LinearLayout) findViewById(R.id.ll_content);
        this.rl_content = (RelativeLayout) findViewById(R.id.rl_content);
        this.ll_sendmessage = (LinearLayout) findViewById(R.id.ll_sendmessage);
        this.sv_msg = (SwipeRefreshLayout) findViewById(R.id.sv_chat_body_msg_content);
        this.rv_msg = (RecyclerView) findViewById(R.id.rv_chat_body_msg_recyclerview);
        this.id_recorder_button = (AudioRecorderButton) findViewById(R.id.id_recorder_button);
        this.iv_voice = (ImageView) findViewById(R.id.iv_chat_buttom_voice);
        this.iv_rengong = (ImageView) findViewById(R.id.iv_chat_buttom_rengong);
        this.iv_icon = (ImageView) findViewById(R.id.iv_chat_buttom_emoj);
        this.btn_add = (ImageView) findViewById(R.id.iv_chat_buttom_add);
        this.btn_send = (TextView) findViewById(R.id.tv_chat_buttom_send);
        this.editEmojicon = (ExpressionEditText) findViewById(R.id.eetself_chat_buttom_input);
        this.mRvEditEmojiconContent = (RelativeLayout) findViewById(R.id.rl_eetself_chat_buttom_input);
        this.fl_emogi = (FrameLayout) findViewById(R.id.fl_emogi);
        this.fl_add = (FrameLayout) findViewById(R.id.fl_add);
        this.iv_del_quote = (ImageView) findViewById(R.id.iv_del_quote);
        this.tv_quote = (TextView) findViewById(R.id.tv_quote);
        this.rl_quote = (RelativeLayout) findViewById(R.id.rl_quote);
        this.mIvFullScreen = (ImageView) findViewById(R.id.iv_chat_full_screen);
        this.mRvFullScreen = (RelativeLayout) findViewById(R.id.rl_chat_full_screen);
        this.iv_close_full_screen = (ImageView) findViewById(R.id.iv_close_full_screen);
        this.rl_full_screen_content = (RelativeLayout) findViewById(R.id.rl_full_screen_content);
        this.mViewMask = findViewById(R.id.view_mask);
        this.getmExpressionEditTextFull = (ExpressionEditText) findViewById(R.id.eetself_chat_buttom_input_full);
        this.mIvEmojFull = (ImageView) findViewById(R.id.iv_chat_buttom_emoj_full);
        this.mIvOpenOrClose = (ImageView) findViewById(R.id.iv_open_or_close);
        this.mTvSendFull = (TextView) findViewById(R.id.tv_chat_buttom_send_full);
        this.mHsvMainContent = (LinearLayout) findViewById(R.id.hsv_main_content);
        this.mHsvMain = (HorizontalScrollView) findViewById(R.id.hsv_main);
        this.mRlHuaTiContent = (RelativeLayout) findViewById(R.id.rl_huati_content);
        this.mEetHuaTi = (ExpressionEditText) findViewById(R.id.eetself_chat_buttom_input_huati);
        this.mIvOpenOrCloseHuaTi = (ImageView) findViewById(R.id.iv_open_or_close_huati);
        this.mTvSendHuaTi = (TextView) findViewById(R.id.tv_chat_buttom_send_full_huati);
        this.mLlHuaTiLaiYuanContext = (LinearLayout) findViewById(R.id.ll_huati_laiyuan_content);
        this.mTvHuaTiLaiYuan = (TextView) findViewById(R.id.tv_huati_laiyuan);
        this.mLlTitleRightContent = (LinearLayout) findViewById(R.id.ll_title_right);
        this.mTvMultipleChoiceCancle = (TextView) findViewById(R.id.tv_multiple_choice_cancle);
        this.mLlForwardContent = (LinearLayout) findViewById(R.id.ll_forward_content);
        this.mLlForwardItem = (LinearLayout) findViewById(R.id.ll_forward_item);
        this.mLlForwardMerge = (LinearLayout) findViewById(R.id.ll_forward_merge);
        this.mIvForwardItem = (ImageView) findViewById(R.id.iv_forward_item);
        this.mIvForwardMerge = (ImageView) findViewById(R.id.iv_forward_merge);
        this.mTvForwardItem = (TextView) findViewById(R.id.tv_forward_item);
        this.mTvForwardMerge = (TextView) findViewById(R.id.tv_forward_merge);
        this.ll_norobot_button_root = (LinearLayout) findViewById(R.id.ll_norobot_button_root);
        this.ll_robot_button_root = (LinearLayout) findViewById(R.id.ll_robot_button_root);
        this.ll_robot_back_root = (LinearLayout) findViewById(R.id.ll_robot_back_root);
        this.iv_robot_keyboard = (ImageView) findViewById(R.id.iv_robot_keyboard);
        this.ll_robot_1 = (LinearLayout) findViewById(R.id.ll_robot_1);
        this.ll_robot_2 = (LinearLayout) findViewById(R.id.ll_robot_2);
        this.ll_robot_3 = (LinearLayout) findViewById(R.id.ll_robot_3);
        this.tv_robot_1 = (TextView) findViewById(R.id.tv_robot_1);
        this.tv_robot_2 = (TextView) findViewById(R.id.tv_robot_2);
        this.tv_robot_3 = (TextView) findViewById(R.id.tv_robot_3);
        this.mTvNewMessageDialog = (LinearLayout) findViewById(R.id.ll_new_message_dialog);
        this.tabLayoutCurr = (TabLayout) findViewById(R.id.tabLayout);
        this.scroll = (ScrollView) findViewById(R.id.scroll);
        this.iv_robot_keyboard.setOnClickListener(this);
        this.ll_robot_back_root.setOnClickListener(this);
        this.mTvNewMessageDialog.setOnClickListener(this);
        this.iv_del_quote.setOnClickListener(this);
        this.editEmojicon.setOnClickListener(this);
        this.getmExpressionEditTextFull.setOnClickListener(this);
        this.iv_chat_qun_person.setOnClickListener(this);
        this.iv_qun_more.setOnClickListener(this);
        this.iv_qun_call.setOnClickListener(this);
        this.iv_chat_person.setOnClickListener(this);
        this.mIvFullScreen.setOnClickListener(this);
        this.mRvFullScreen.setOnClickListener(this);
        this.iv_close_full_screen.setOnClickListener(this);
        this.mViewMask.setOnClickListener(this);
        this.mIvEmojFull.setOnClickListener(this);
        this.mIvOpenOrClose.setOnClickListener(this);
        this.mTvSendFull.setOnClickListener(this);
        this.mIvOpenOrCloseHuaTi.setOnClickListener(this);
        this.mTvSendHuaTi.setOnClickListener(this);
        this.mTvMultipleChoiceCancle.setOnClickListener(this);
        this.mLlForwardItem.setOnClickListener(this);
        this.mLlForwardMerge.setOnClickListener(this);
        this.tv_detail.setOnClickListener(this);
        this.sv_msg.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.shineyue.pm.web.WebActivity.72
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public void onRefresh() {
                WebActivity.this.newMoreDate();
            }
        });
        this.chatAdapter = new ChatRvAdapter(this, this.list, this.toLastReadTime, this.sfqz, this.sfgly);
        this.id_recorder_button.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() { // from class: com.shineyue.pm.web.WebActivity.73
            @Override // com.shineyue.pm.modle_chat.utiles.AudioRecorderButton.AudioFinishRecorderListener
            public void onFinish(float seconds, String filePath) {
                File file = new File(filePath);
                if (!OaApplication.JIQIRENID.equals(WebActivity.this.toUser)) {
                    WebActivity.this.saveFile(file.getAbsolutePath(), file.getName(), seconds, true, false, null, null);
                } else {
                    WebActivity.this.SpeechToTextByFile(file);
                }
            }
        });
        CustomLinearLayoutManager customLinearLayoutManager = new CustomLinearLayoutManager(this.mContext);
        this.llLayoutManager = customLinearLayoutManager;
        customLinearLayoutManager.setOrientation(1);
        this.llLayoutManager.setStackFromEnd(true);
        this.rv_msg.setLayoutManager(this.llLayoutManager);
        this.rv_msg.setAdapter(this.chatAdapter);
        this.rv_msg.setOnTouchListener(new View.OnTouchListener() { // from class: com.shineyue.pm.web.WebActivity.74
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 2) {
                    WebActivity webActivity = WebActivity.this;
                    if (!webActivity.isKeyboardShown(webActivity.ll)) {
                        if (WebActivity.this.isEmogiShow) {
                            WebActivity.this.editEmojicon.clearFocus();
                            WebActivity.this.hideEmogiPanel();
                            return false;
                        }
                        if (WebActivity.this.isAddShow) {
                            WebActivity.this.editEmojicon.clearFocus();
                            WebActivity.this.hideAddPanel();
                            return false;
                        }
                        return false;
                    }
                    WebActivity webActivity2 = WebActivity.this;
                    webActivity2.hideKeyboard(webActivity2);
                    return false;
                }
                return false;
            }
        });
        this.rv_msg.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.shineyue.pm.web.WebActivity.75
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (WebActivity.this.llLayoutManager != null) {
                    int position = WebActivity.this.llLayoutManager.findFirstVisibleItemPosition();
                    int lastItemPosition = WebActivity.this.llLayoutManager.findLastVisibleItemPosition();
                    if (position <= 2 && dy < 0 && !WebActivity.this.sv_msg.isRefreshing()) {
                        WebActivity.this.sv_msg.setRefreshing(true);
                        WebActivity.this.newMoreDate();
                    }
                    if (lastItemPosition >= WebActivity.this.list.size() - 2) {
                        WebActivity.this.isInBottom = true;
                    } else {
                        WebActivity.this.isInBottom = false;
                    }
                    if (lastItemPosition >= WebActivity.this.list.size() - 1) {
                        WebActivity.this.isInBottom_1 = true;
                    } else {
                        WebActivity.this.isInBottom_1 = false;
                    }
                    if (!WebActivity.this.isSearchOrDing && WebActivity.this.isInBottom_1 && WebActivity.this.mTvNewMessageDialog.getVisibility() == 0) {
                        WebActivity.this.mTvNewMessageDialog.setVisibility(8);
                    }
                }
            }
        });
        this.chatAdapter.setOnItemLongClick(new ChatRvAdapter.OnItemLongClick() { // from class: com.shineyue.pm.web.WebActivity.76
            @Override // com.shineyue.pm.modle_chat.adapter.ChatRvAdapter.OnItemLongClick
            public void onLongClick(String userId, String userName) {
                if (WebActivity.this.chatType.equals("7") || WebActivity.this.chatType.equals("4")) {
                    try {
                        if (!WebActivity.this.isShutUp) {
                            WebActivity.this.onAitDealLongClick(userId, userName);
                        }
                    } catch (Exception e) {
                        KLog.e(e.getMessage());
                    }
                }
            }
        });
        this.editEmojicon.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.shineyue.pm.web.WebActivity.77
            @Override // android.view.View.OnFocusChangeListener
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    WebActivity.this.hideAddPanel();
                } else {
                    WebActivity webActivity = WebActivity.this;
                    webActivity.hideKeyboard(webActivity);
                }
            }
        });
        this.getmExpressionEditTextFull.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: com.shineyue.pm.web.WebActivity.78
            @Override // android.view.View.OnFocusChangeListener
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    WebActivity.this.hideAddPanel();
                } else {
                    WebActivity webActivity = WebActivity.this;
                    webActivity.hideKeyboard(webActivity);
                }
            }
        });
        this.editEmojicon.addTextChangedListener(new TextWatcher() { // from class: com.shineyue.pm.web.WebActivity.79
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                WebActivity.this.isDelete = count > after;
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = WebActivity.this.editEmojicon.getText().toString();
                if (Utils.haveValue(content)) {
                    WebActivity.this.btn_send.setVisibility(0);
                    WebActivity.this.btn_add.setVisibility(8);
                } else {
                    WebActivity.this.btn_send.setVisibility(8);
                    WebActivity.this.btn_add.setVisibility(0);
                }
                if (!WebActivity.this.isshowFull) {
                    WebActivity.this.ll_sendmessage.requestLayout();
                    WebActivity.this.ll_sendmessage.postInvalidate();
                    if (WebActivity.this.isDelete) {
                        if (WebActivity.this.aitManager != null) {
                            WebActivity.this.aitManager.deletePerson(WebActivity.this.editEmojicon.getSelectionEnd());
                        }
                    } else if (!WebActivity.this.isEditSet && count > 0 && content.length() > 0 && "@".equals(content.substring(start, start + 1)) && (WebActivity.this.chatType.equals("4") || WebActivity.this.chatType.equals("7"))) {
                        WebActivity.this.start_ait = start;
                        WebActivity.this.callAit();
                    }
                    WebActivity.this.isEditSet = false;
                }
            }
        });
        this.getmExpressionEditTextFull.addTextChangedListener(new TextWatcher() { // from class: com.shineyue.pm.web.WebActivity.80
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    WebActivity.this.mTvSendFull.setBackgroundResource(R.mipmap.chat_send_unclick);
                } else {
                    WebActivity.this.mTvSendFull.setBackgroundResource(R.mipmap.chat_send);
                }
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                WebActivity.this.isDelete = count > after;
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (WebActivity.this.isshowFull) {
                    String content = WebActivity.this.getmExpressionEditTextFull.getText().toString();
                    WebActivity.this.ll_sendmessage.requestLayout();
                    WebActivity.this.ll_sendmessage.postInvalidate();
                    if (Utils.haveValue(content)) {
                        WebActivity.this.btn_send.setVisibility(0);
                        WebActivity.this.btn_add.setVisibility(8);
                    } else {
                        WebActivity.this.btn_send.setVisibility(8);
                        WebActivity.this.btn_add.setVisibility(0);
                    }
                    if (WebActivity.this.isDelete) {
                        if (WebActivity.this.aitManager != null) {
                            WebActivity.this.aitManager.deletePerson(WebActivity.this.getmExpressionEditTextFull.getSelectionEnd());
                        }
                    } else if (!WebActivity.this.isEditSet && count > 0 && content.length() > 0 && "@".equals(content.substring(start, start + 1)) && (WebActivity.this.chatType.equals("4") || WebActivity.this.chatType.equals("7"))) {
                        WebActivity.this.start_ait = start;
                        WebActivity.this.callAit();
                    }
                    WebActivity.this.isEditSet = false;
                }
            }
        });
        this.mEetHuaTi.addTextChangedListener(new TextWatcher() { // from class: com.shineyue.pm.web.WebActivity.81
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    WebActivity.this.mTvSendHuaTi.setEnabled(false);
                } else {
                    WebActivity.this.mTvSendHuaTi.setEnabled(true);
                }
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        this.ll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.shineyue.pm.web.WebActivity.82
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                WebActivity webActivity = WebActivity.this;
                webActivity.isKeyboardShow = webActivity.isKeyboardShown(webActivity.ll);
                if (WebActivity.this.isKeyboardShow) {
                    if (WebActivity.this.supportSoftInputHeight == 0) {
                        WebActivity webActivity2 = WebActivity.this;
                        webActivity2.supportSoftInputHeight = webActivity2.getSupportSoftInputHeight();
                        KLog.e(WebActivity.this.TAG, "键盘高度==" + WebActivity.this.supportSoftInputHeight);
                        WebActivity.this.fl_emogi.getLayoutParams().height = WebActivity.this.supportSoftInputHeight;
                        WebActivity.this.fl_emogi.requestLayout();
                        WebActivity.this.fl_add.getLayoutParams().height = Utils.Dp2Px(WebActivity.this.mContext, 200.0f);
                        WebActivity.this.fl_add.requestLayout();
                    }
                    WebActivity.this.scrollToBottom();
                }
            }
        });
        this.chatAdapter.setOnReWriteListener(new ChatRvAdapter.OnReWriteListener() { // from class: com.shineyue.pm.web.WebActivity.83
            @Override // com.shineyue.pm.modle_chat.adapter.ChatRvAdapter.OnReWriteListener
            public void onReWrite(String content) {
                if (WebActivity.this.isVideo) {
                    WebActivity.this.isVideo = false;
                    WebActivity.this.editEmojicon.setVisibility(0);
                    WebActivity.this.mRvEditEmojiconContent.setVisibility(0);
                    WebActivity.this.id_recorder_button.setVisibility(8);
                    WebActivity.this.iv_voice.setImageResource(R.mipmap.chat_voice);
                }
                if (!TextUtils.isEmpty(content)) {
                    WebActivity.this.isEditSet = true;
                    WebActivity.this.editEmojicon.setText(content);
                    WebActivity.this.editEmojicon.setSelection(content.length());
                }
                if (!WebActivity.this.isEmogiShow) {
                    WebActivity webActivity = WebActivity.this;
                    KeyBoardListener.showKeyboard(webActivity, webActivity.editEmojicon);
                }
            }
        });
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x00c6, code lost:
    
        r7 = r5.getJSONArray("button");
        r18.btnList = new org.json.JSONArray(r7.toString());
     */
    /* JADX WARN: Removed duplicated region for block: B:104:0x038e  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0396  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x03b3  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x03d9  */
    /* JADX WARN: Removed duplicated region for block: B:127:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0319  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void initDataChat() {
        /*
            Method dump skipped, instruction units count: 1030
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shineyue.pm.web.WebActivity.initDataChat():void");
    }

    private void switchWebButton() {
        KLog.i(this.TAG, Integer.valueOf(this.currPosition));
        KLog.i(this.TAG, Integer.valueOf(this.webView.getHeight()));
        if (this.currPosition == 3) {
            this.scroll.post(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.88
                @Override // java.lang.Runnable
                public void run() {
                    WebActivity.this.scroll.smoothScrollTo(0, WebActivity.this.webView.getHeight());
                }
            });
        }
    }

    private void setMessageTotalNum() {
        Integer integer = Integer.valueOf(Utils.getIntMsg(OaApplication.application, "chatTotalNum" + this.toUser + "_" + Utils.getMsg(OaApplication.application, "jgbh")));
        if ("4".equals(this.chatType) || "7".equals(this.chatType)) {
            this.tv_total_num.setVisibility(8);
            if (integer == null && integer.intValue() == -1) {
                this.tv_total_num_new.setVisibility(8);
                return;
            }
            if (integer.intValue() > 0) {
                this.tv_total_num_new.setText("(" + integer + ")");
                this.tv_total_num_new.setVisibility(0);
                return;
            }
            this.tv_total_num_new.setVisibility(8);
            return;
        }
        this.tv_total_num_new.setVisibility(8);
        if (integer == null && integer.intValue() == -1) {
            this.tv_total_num.setVisibility(8);
            return;
        }
        if (integer.intValue() > 0) {
            this.tv_total_num.setText("(" + integer + ")");
            this.tv_total_num.setVisibility(0);
            return;
        }
        this.tv_total_num.setVisibility(8);
    }

    private void initRobotButton() {
        try {
            JSONArray jSONArray = this.btnList;
            if (jSONArray != null && jSONArray.length() > 0) {
                if (this.btnList.length() > 3) {
                    final JSONObject jsonObject_0 = this.btnList.optJSONObject(0);
                    final JSONObject jsonObject_1 = this.btnList.getJSONObject(1);
                    this.tv_robot_1.setText(jsonObject_0.getString("btn"));
                    this.tv_robot_2.setText(jsonObject_1.getString("btn"));
                    this.tv_robot_3.setText("更多");
                    this.ll_robot_1.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.89
                        @Override // android.view.View.OnClickListener
                        public void onClick(View v) {
                            WebActivity.this.cardButtonClickEvent(jsonObject_0, v);
                        }
                    });
                    this.ll_robot_2.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.90
                        @Override // android.view.View.OnClickListener
                        public void onClick(View v) {
                            WebActivity.this.cardButtonClickEvent(jsonObject_1, v);
                        }
                    });
                    this.ll_robot_3.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.91
                        @Override // android.view.View.OnClickListener
                        public void onClick(View v) {
                            WebActivity webActivity = WebActivity.this;
                            webActivity.showCardButtonMoreDialog(webActivity.btnList, -1);
                        }
                    });
                    this.ll_robot_1.setVisibility(0);
                    this.ll_robot_2.setVisibility(0);
                    this.ll_robot_3.setVisibility(0);
                } else {
                    for (int i = 0; i < this.btnList.length(); i++) {
                        final JSONObject jsonObject_i = this.btnList.optJSONObject(i);
                        if (i == 0) {
                            this.tv_robot_1.setText(jsonObject_i.optString("btn"));
                            this.ll_robot_1.setVisibility(0);
                            this.ll_robot_1.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.92
                                @Override // android.view.View.OnClickListener
                                public void onClick(View v) {
                                    WebActivity.this.cardButtonClickEvent(jsonObject_i, v);
                                }
                            });
                        } else if (i == 1) {
                            this.tv_robot_2.setText(jsonObject_i.optString("btn"));
                            this.ll_robot_2.setVisibility(0);
                            this.ll_robot_2.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.93
                                @Override // android.view.View.OnClickListener
                                public void onClick(View v) {
                                    WebActivity.this.cardButtonClickEvent(jsonObject_i, v);
                                }
                            });
                        } else if (i == 2) {
                            this.tv_robot_3.setText(jsonObject_i.optString("btn"));
                            this.ll_robot_3.setVisibility(0);
                            this.ll_robot_3.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.94
                                @Override // android.view.View.OnClickListener
                                public void onClick(View v) {
                                    WebActivity.this.cardButtonClickEvent(jsonObject_i, v);
                                }
                            });
                        }
                    }
                }
                return;
            }
            this.ll_robot_back_root.setVisibility(8);
            this.ll_robot_button_root.setVisibility(8);
            this.ll_norobot_button_root.setVisibility(0);
        } catch (Exception e) {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void getQunZuData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("ffbm", "12");
        map.put("jgbm", Utils.getMsg(OaApplication.application, "jgbm"));
        map.put("userid", Utils.getMsg(OaApplication.application, "userid"));
        map.put("khbh", "");
        map.put("blqd", "app_02");
        map.put("zxbm", Utils.getMsg(OaApplication.application, "zxjgbm"));
        map.put("ywfl", "00");
        map.put("ywlb", "00");
        map.put("zqqbh", "");
        map.put("page", 1);
        map.put(LogUnAvailbleItem.EXTRA_KEY_SIZE, "");
        map.put("id", this.toUser);
        JSONObject jsonObject = new JSONObject(map);
        KLog.i(this.TAG, jsonObject);
        final OkGoNetManager okGoNetManager = OkGoNetManager.getInstance();
        ((PostRequest) OkGo.post(OkGoNetManager.APP_URL + OkGoNetManager.QUNZU).tag(this)).upJson(jsonObject.toString()).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.95
            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onSuccess(String s, Call call, Response response) {
                KLog.i(WebActivity.this.TAG, s);
                try {
                    QunXiangQing qunXiangQing = (QunXiangQing) new Gson().fromJson(s, QunXiangQing.class);
                    if (qunXiangQing != null && qunXiangQing.data != null && qunXiangQing.data.size() > 0 && qunXiangQing.data.get(0).groupuser != null && qunXiangQing.data.get(0).groupuser.size() > 0) {
                        final QunXiangQing.DataBean groupDetail = qunXiangQing.data.get(0);
                        if (2 != Utils.judgeClient().intValue() || UtilsChatTopic.isHuati(WebActivity.this.toUser)) {
                            WebActivity.this.tv_title_num.setVisibility(8);
                        } else {
                            WebActivity.this.tv_title_num.setVisibility(0);
                            WebActivity.this.tv_title_num.setText("(" + groupDetail.qzrs + "人)");
                        }
                        if (Utils.haveValue(qunXiangQing.data.get(0).xmmc)) {
                            WebActivity.this.ll_company.setVisibility(8);
                            WebActivity.this.tv_company.setText("");
                            WebActivity.this.xmmc = qunXiangQing.data.get(0).xmmc;
                            WebActivity.this.xmbh = qunXiangQing.data.get(0).xmbh;
                        }
                        WebActivity.this.groupListPresenter.getGroupInfoFromDb(WebActivity.this.toUser, new QunZuItemInfoDaoManager.OnGroupItemResultListener() { // from class: com.shineyue.pm.web.WebActivity.95.1
                            @Override // com.shineyue.pm.modle_chat.qunzu.dao.QunZuItemInfoDaoManager.OnGroupItemResultListener
                            public void onGroupItemResult(QunZuItemInfoBean qunZuItemInfoBean) {
                                if (qunZuItemInfoBean != null) {
                                    qunZuItemInfoBean.qzrs = Integer.valueOf(groupDetail.qzrs);
                                    WebActivity.this.groupListPresenter.updateGroupItemBean(qunZuItemInfoBean);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    Utils.dismissProgressDialog();
                    KLog.e(WebActivity.this.TAG, e.toString());
                    ToastUtil.toast(WebActivity.this.mContext, "error", "", ToastType.FAIL);
                    OutPutLogUtils.uMengLog("接口：" + OkGoNetManager.APP_URL + OkGoNetManager.QUNZU + "异常：" + e.getMessage().toString());
                }
            }

            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                KLog.i(WebActivity.this.TAG, e.toString());
                Utils.dismissProgressDialog();
            }
        });
    }

    private void shareMessage(Intent intent) {
        QunZuItemInfoBean qunZuItemInfoBean;
        if (intent == null) {
            intent = getIntent();
        }
        try {
            if (intent.hasExtra("shareMessage")) {
                List<Message> messageList = (List) intent.getSerializableExtra("shareMessage");
                KLog.i(this.TAG, messageList);
                if ((this.chatType.equals("4") || this.chatType.equals("7")) && (qunZuItemInfoBean = GreenDaoHelper.getDaoSession().getQunZuItemInfoBeanDao().load(this.toUser)) != null && qunZuItemInfoBean.sfjy != null && qunZuItemInfoBean.sfjy.equals("1")) {
                    return;
                }
                if ((this.chatType.equals("4") || this.chatType.equals("7")) && this.isShutUp) {
                    return;
                }
                if (messageList != null) {
                    for (int i = 0; i < messageList.size(); i++) {
                        Message m = messageList.get(i);
                        if (m.type.equals("3") || m.type.equals("4")) {
                            sendAgain(m, 0);
                        } else if (m.type.equals("6") || m.type.equals("7")) {
                            long time = System.currentTimeMillis();
                            m.date = time + "";
                            m.time = time;
                            upDataListAndUpFile(m, 0);
                        }
                    }
                }
            }
        } catch (Exception e) {
            KLog.i(this.TAG, e.toString());
        }
        KLog.i(this.TAG, this.toUser);
        KLog.i(this.TAG, this.toUserName);
        KLog.i(this.TAG, this.chatType);
    }

    private void getLastReadTime() {
        this.chatWindowListPresenter.getChatWindowFromDb(this.toUser, new ChatWindowListDaoManager.OnChatWindowItemResultListener() { // from class: com.shineyue.pm.web.WebActivity.96
            @Override // com.shineyue.pm.modle_chat.presenter.chat_window_list.ChatWindowListDaoManager.OnChatWindowItemResultListener
            public void onChatWindowItemResult(ChatWindowItem chatWindowItem) {
                String typeWindows;
                if (chatWindowItem == null) {
                    if ("3".equals(WebActivity.this.chatType) || "6".equals(WebActivity.this.chatType)) {
                        typeWindows = "1";
                    } else {
                        typeWindows = "2";
                    }
                    WebActivity.this.chatWindowListPresenter.addChatWindowToListFromNet(WebActivity.this.toUser, typeWindows, new ChatWindowListDaoManager.OnInsertComplete() { // from class: com.shineyue.pm.web.WebActivity.96.1
                        @Override // com.shineyue.pm.modle_chat.presenter.chat_window_list.ChatWindowListDaoManager.OnInsertComplete
                        public void onInsertComplete() {
                            WebActivity.this.getLastReadTimeFromNet();
                        }
                    });
                    return;
                }
                if (chatWindowItem.getContent().content.toLastReadTime == 0) {
                    WebActivity.this.getLastReadTimeFromNet();
                    return;
                }
                if (WebActivity.this.toLastReadTime < chatWindowItem.getContent().content.toLastReadTime) {
                    WebActivity.this.toLastReadTime = chatWindowItem.getContent().content.toLastReadTime;
                    WebActivity.this.fromLastReadTime = chatWindowItem.getContent().content.fromLastReadTime;
                    WebActivity.this.changeLastReadTime();
                    WebActivity.this.chatAdapter.toLastReadTime = WebActivity.this.toLastReadTime;
                    KLog.i(WebActivity.this.TAG, "toLastReadTime:" + WebActivity.this.toLastReadTime);
                    WebActivity.this.chatAdapter.notifyDataSetChanged();
                    return;
                }
                KLog.i(WebActivity.this.TAG, "toLastReadTime:" + chatWindowItem.getContent().content.toLastReadTime);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getLastReadTimeFromNet() {
        String sign = "";
        HashMap<String, Object> map = new HashMap<>();
        map.put("fromUserId", Utils.getMsg(OaApplication.application, "userid"));
        map.put("toUserId", this.toUser);
        String object = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
        try {
            String sign2 = Base64Utils.encode(RSAUtils.encryptByPublicKey(object.getBytes(StandardCharsets.UTF_8.name()), RSAUtils.publicKey));
            sign = URLEncoder.encode(URLEncoder.encode(sign2, StandardCharsets.UTF_8.name()), StandardCharsets.UTF_8.name());
            KLog.d(this.TAG, RSAUtils.getValue(sign, RSAUtils.privateKey));
            RSAUtils.getValue(sign, RSAUtils.privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkGoNetManager.getInstance();
        KLog.i(this.TAG, OkGoNetManager.CHAT_URL_FILE + OkGoNetManager.SINGLECHATWINDOWLASTREADTIME + "?systemName=" + ChatServiceNew.getCHANNEL() + "&sign=" + sign);
        OkGo.get(OkGoNetManager.CHAT_URL_FILE + OkGoNetManager.SINGLECHATWINDOWLASTREADTIME + "?systemName=" + ChatServiceNew.getCHANNEL() + "&sign=" + sign).tag(this).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.97
            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onSuccess(String s, Call call, Response response) {
                KLog.d(WebActivity.this.TAG, s);
                try {
                    JSONObject js = new JSONObject(s);
                    JSONObject jsonObject = js.getJSONObject("data");
                    if (jsonObject.has("toLastReadTime")) {
                        WebActivity.this.toLastReadTime = jsonObject.getLong("toLastReadTime");
                        WebActivity.this.changeLastReadTime();
                        WebActivity.this.chatAdapter.toLastReadTime = WebActivity.this.toLastReadTime;
                        WebActivity.this.chatAdapter.notifyDataSetChanged();
                    } else {
                        WebActivity.this.toLastReadTime = 0L;
                        WebActivity.this.changeLastReadTime();
                        WebActivity.this.chatAdapter.toLastReadTime = WebActivity.this.toLastReadTime;
                        WebActivity.this.chatAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e2) {
                    e2.toString();
                }
            }

            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onError(Call call, Response response, Exception e2) {
                super.onError(call, response, e2);
                KLog.d(WebActivity.this.TAG, e2.toString());
                KLog.d(WebActivity.this.TAG, e2.getLocalizedMessage());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initLinisterChat() {
        this.editEmojicon.setEnabled(true);
        this.editEmojicon.setHint("");
        this.btn_send.setOnClickListener(this);
        this.btn_add.setOnClickListener(this);
        this.iv_voice.setOnClickListener(this);
        this.iv_rengong.setOnClickListener(this);
        this.iv_icon.setOnClickListener(this);
    }

    private void cancelLinister() {
        this.btn_send.setOnClickListener(null);
        this.btn_add.setOnClickListener(null);
        this.iv_voice.setOnClickListener(null);
        this.iv_icon.setOnClickListener(null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void addChatQx() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("jgbh", Utils.getMsg(this.mContext, "jgbh"));
        map.put("sendUser", this.mId);
        map.put("receiverUser", this.toUser);
        JSONObject jsonObject = new JSONObject(map);
        KLog.d(this.TAG, jsonObject.toString());
        OkGoNetManager.getInstance();
        ((PostRequest) OkGo.post(OkGoNetManager.APP_URL + OkGoNetManager.ADDCHATQX).tag(this)).upJson(jsonObject.toString()).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.98
            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onSuccess(String s, Call call, Response response) {
                KLog.i(WebActivity.this.TAG, s);
            }

            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                KLog.i(WebActivity.this.TAG, e.toString());
            }
        });
    }

    private void initAit() {
        AitManager aitManager = new AitManager();
        this.aitManager = aitManager;
        aitManager.setOnPersonDelete(new AitManager.OnPersonDelete() { // from class: com.shineyue.pm.web.WebActivity.99
            @Override // com.shineyue.pm.modle_chat.presenter.chatview.AitManager.OnPersonDelete
            public void onDelete(int start, int end) {
                if (WebActivity.this.isshowFull) {
                    String resultText = WebActivity.this.aitManager.reSetChatText(WebActivity.this.getmExpressionEditTextFull.getText().toString(), start, end);
                    WebActivity.this.isEditSet = true;
                    WebActivity.this.getmExpressionEditTextFull.setText(resultText);
                    WebActivity.this.getmExpressionEditTextFull.setSelection(start);
                    return;
                }
                String resultText2 = WebActivity.this.aitManager.reSetChatText(WebActivity.this.editEmojicon.getText().toString(), start, end);
                WebActivity.this.isEditSet = true;
                WebActivity.this.editEmojicon.setText(resultText2);
                WebActivity.this.editEmojicon.setSelection(start);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void getGroupUser(String username) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("ywfl", "99");
        map.put("ywlb", "9901");
        map.put("zhbh", "");
        map.put("khbh", Utils.getMsg(this.mContext, "khbh"));
        map.put("blqd", Utils.getMsg(this.mContext, "blqd"));
        map.put("ffbm", "09");
        map.put("zxbm", Utils.getMsg(this.mContext, "zxjgbm"));
        map.put("userid", Utils.getMsg(this.mContext, "userid"));
        map.put("groupid", Double.valueOf(Double.parseDouble(this.toUser)));
        map.put("username", username);
        map.put("jgbm", Utils.getMsg(this.mContext, "jgbm"));
        JSONObject jsonObject = new JSONObject(map);
        KLog.i(this.TAG, jsonObject);
        OkGoNetManager.getInstance();
        ((PostRequest) OkGo.post(OkGoNetManager.APP_URL + OkGoNetManager.QUNZU).tag(this)).upJson(jsonObject.toString()).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.100
            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onSuccess(String s, Call call, Response response) {
                KLog.i(WebActivity.this.TAG, s);
                GroupUser groupUser = (GroupUser) new Gson().fromJson(s, GroupUser.class);
                if (groupUser != null) {
                    WebActivity.this.group_user_list = groupUser.data;
                    if (WebActivity.this.isShowChat) {
                        WebActivity.this.groupUserAdapter.qunZuChengYuans = WebActivity.this.group_user_list;
                        WebActivity.this.groupUserAdapter.notifyDataSetChanged();
                    } else {
                        WebActivity.this.showPopupWindow();
                    }
                }
            }

            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                KLog.e(WebActivity.this.TAG, e.getMessage());
                Utils.dismissProgressDialog();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPopupWindow() {
        this.isShowChat = true;
        if (this.popupWindow == null) {
            View contentView = LayoutInflater.from(this.mContext).inflate(R.layout.item_page_chat_user_count, (ViewGroup) null, false);
            initPopupView(contentView);
            this.popupWindow = new PopupWindow(contentView, -1, -1, true);
        }
        this.popupWindow.setBackgroundDrawable(new ColorDrawable(-1));
        this.popupWindow.setOutsideTouchable(true);
        this.popupWindow.setTouchable(true);
        this.popupWindow.showAtLocation(this.ll, 0, 0, 0);
        this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.shineyue.pm.web.WebActivity.101
            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                WebActivity.this.isShowChat = false;
            }
        });
    }

    private void initPopupView(View contentView) {
        this.rv_user = (RecyclerView) contentView.findViewById(R.id.rv_user);
        this.iv_back_user = (ImageView) contentView.findViewById(R.id.iv_back_user);
        EditText editText = (EditText) contentView.findViewById(R.id.et_search);
        this.et_search = editText;
        editText.setHint("请输入姓名");
        initPopupListener();
    }

    private void initPopupListener() {
        this.iv_back_user.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.102
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (WebActivity.this.isShowChat) {
                    WebActivity.this.isShowChat = false;
                    WebActivity.this.popupWindow.dismiss();
                }
            }
        });
        this.et_search.addTextChangedListener(new TextWatcher() { // from class: com.shineyue.pm.web.WebActivity.103
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = WebActivity.this.et_search.getText().toString().trim();
                WebActivity.this.getGroupUser(str);
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
            }
        });
        LinearLayoutManager llLayoutManager = new LinearLayoutManager(this.mContext);
        llLayoutManager.setOrientation(1);
        this.rv_user.setLayoutManager(llLayoutManager);
        ChatGroupUserPopupAdapter chatGroupUserPopupAdapter = new ChatGroupUserPopupAdapter(this.group_user_list, this.mContext);
        this.groupUserAdapter = chatGroupUserPopupAdapter;
        this.rv_user.setAdapter(chatGroupUserPopupAdapter);
        this.groupUserAdapter.setOnClickCallBackListener(new ChatGroupUserPopupAdapter.OnClickCallBack() { // from class: com.shineyue.pm.web.WebActivity.104
            @Override // com.shineyue.pm.modle_chat.adapter.ChatGroupUserPopupAdapter.OnClickCallBack
            public void onNormalClick(GroupUser.DataBean dataBean) {
                WebActivity.this.onAitDeal(dataBean.groupuserid, dataBean.username);
                WebActivity.this.popupWindow.dismiss();
                WebActivity.this.isShowChat = false;
            }

            @Override // com.shineyue.pm.modle_chat.adapter.ChatGroupUserPopupAdapter.OnClickCallBack
            public void onMoreClick(int type) {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAitDeal(String userId, String userName) {
        int start;
        if (this.isshowFull) {
            this.getmExpressionEditTextFull.getText().toString();
        } else {
            this.editEmojicon.getText().toString();
        }
        if (this.isshowFull) {
            this.getmExpressionEditTextFull.getText().toString();
            start = this.getmExpressionEditTextFull.getText().length();
        } else {
            this.editEmojicon.getText().toString();
            start = this.editEmojicon.getText().length();
        }
        this.isEditSet = true;
        if (this.isshowFull) {
            this.getmExpressionEditTextFull.getText().insert(this.start_ait + 1, userName + " ");
            this.getmExpressionEditTextFull.setSelection(this.start_ait + userName.length() + 1);
            int end = this.getmExpressionEditTextFull.getText().length() - 1;
            KLog.e("@后大小" + start + "  " + end);
            AitManager aitManager = this.aitManager;
            int i = this.start_ait;
            aitManager.addPerson(i + 1, i + userName.length() + 1, userId);
            showKeyboardFull((Activity) this.mContext);
            this.getmExpressionEditTextFull.requestFocus();
            return;
        }
        this.editEmojicon.getText().insert(this.start_ait + 1, userName + " ");
        this.editEmojicon.setSelection(this.start_ait + userName.length() + 1);
        int end2 = this.editEmojicon.getText().length() - 1;
        KLog.e("@后大小" + start + "  " + end2);
        AitManager aitManager2 = this.aitManager;
        int i2 = this.start_ait;
        aitManager2.addPerson(i2 + 1, i2 + userName.length() + 1, userId);
        this.editEmojicon.requestFocus();
        showKeyboard(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAitDealLongClick(String userId, String userName) {
        int start;
        if (this.isshowFull) {
            this.getmExpressionEditTextFull.getText().toString();
        } else {
            this.editEmojicon.getText().toString();
        }
        if (this.isshowFull) {
            this.getmExpressionEditTextFull.getText().toString();
            start = this.getmExpressionEditTextFull.getText().length();
        } else {
            this.editEmojicon.getText().toString();
            start = this.editEmojicon.getText().length();
        }
        this.isEditSet = true;
        if (this.isshowFull) {
            this.start_ait = this.getmExpressionEditTextFull.getSelectionStart();
            this.getmExpressionEditTextFull.getText().insert(this.start_ait, "@" + userName + " ");
            this.getmExpressionEditTextFull.setSelection(this.start_ait + userName.length() + 1);
            int end = this.getmExpressionEditTextFull.getText().length() - 1;
            KLog.e("@后大小" + start + "  " + end);
            AitManager aitManager = this.aitManager;
            int i = this.start_ait;
            aitManager.addPerson(i + 1, i + userName.length(), userId);
            showKeyboardFull((Activity) this.mContext);
            this.getmExpressionEditTextFull.requestFocus();
            return;
        }
        this.start_ait = this.editEmojicon.getSelectionStart();
        this.editEmojicon.getText().insert(this.start_ait, "@" + userName + " ");
        this.editEmojicon.setSelection(this.start_ait + userName.length() + 1);
        int end2 = this.editEmojicon.getText().length() - 1;
        KLog.e("@后大小" + start + "  " + end2);
        AitManager aitManager2 = this.aitManager;
        int i2 = this.start_ait;
        aitManager2.addPerson(i2 + 1, i2 + userName.length(), userId);
        this.editEmojicon.requestFocus();
        showKeyboard(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadDate() {
        if (this.dingTime > 0) {
            downloadDate();
        } else if (this.chatType.equals("6") || this.chatType.equals("3")) {
            this.chatQxModule.queryChatTime(this.mId, this.toUser, new OnParmsCallBack<Long>() { // from class: com.shineyue.pm.web.WebActivity.105
                @Override // com.shineyue.pm.modle_chat.chat_history.dao.OnParmsCallBack
                public void onParmsResult(Long result) {
                    WebActivity.this.firstlimitTime = result.longValue();
                    if (result.longValue() == 0) {
                        WebActivity.this.loadDateNew(-1L);
                    } else if (result.longValue() != -1) {
                        WebActivity.this.loadDateNew(result.longValue());
                    }
                }
            });
        } else {
            loadDateNew(-1L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadDateNew(final long limitTime) {
        ThreadPoolManager.getInstance().execute(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.106
            @Override // java.lang.Runnable
            public void run() {
                List<Message> listDao;
                if (limitTime > 0) {
                    listDao = GreenDaoHelper.getDaoSession().getMessageDao().queryBuilder().where(MessageDao.Properties.MyId.eq(WebActivity.this.mId), new WhereCondition[0]).where(MessageDao.Properties.ToUserId.eq(WebActivity.this.toUser), new WhereCondition[0]).where(MessageDao.Properties.Date.gt(limitTime + ""), new WhereCondition[0]).orderDesc(MessageDao.Properties.Date).limit(20).offset(0).build().list();
                } else {
                    listDao = GreenDaoHelper.getDaoSession().getMessageDao().queryBuilder().where(MessageDao.Properties.MyId.eq(WebActivity.this.mId), new WhereCondition[0]).where(MessageDao.Properties.ToUserId.eq(WebActivity.this.toUser), new WhereCondition[0]).orderDesc(MessageDao.Properties.Date).limit(20).offset(0).build().list();
                }
                if (listDao.size() >= 0) {
                    Collections.reverse(listDao);
                    if (listDao.size() == 0) {
                        WebActivity.this.list.clear();
                        EventBus.getDefault().post(new Notify.Message(Notify.ChatMsg4));
                    }
                    if (WebActivity.this.list.size() <= 0 && WebActivity.this.dingTime <= 0) {
                        WebActivity.this.list.clear();
                        WebActivity.this.list.addAll(listDao);
                        EventBus.getDefault().post(new Notify.Message(Notify.ChatMsg4));
                    }
                    long l = System.currentTimeMillis();
                    for (int i = 0; i < WebActivity.this.list.size(); i++) {
                        Message message_chat = (Message) WebActivity.this.list.get(i);
                        if (9 == message_chat.getSendStatus()) {
                            long time_jiange = l - Long.parseLong(message_chat.date);
                            if (time_jiange <= 30000) {
                                android.os.Message message = new android.os.Message();
                                Bundle bundle = new Bundle();
                                bundle.putString("uqIdentNo", message_chat.uqIdentNo);
                                bundle.putInt("num", 0);
                                message.what = 291;
                                message.setData(bundle);
                                WebActivity.this.myHandler.sendMessageDelayed(message, time_jiange);
                            } else {
                                message_chat.sendStatus = 2;
                            }
                        }
                        WebActivity.this.getMessageChecked(message_chat);
                    }
                }
                WebActivity.this.tryAgainTimes = 0;
                WebActivity.this.downloadDate();
            }
        });
    }

    private void OkHttpPost(String url, String key, String value) {
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder().build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new okhttp3.Callback() { // from class: com.shineyue.pm.web.WebActivity.107
            @Override // okhttp3.Callback
            public void onFailure(Call call2, IOException e) {
                Utils.dismissProgressDialog();
                if (WebActivity.this.tryAgainTimes < 3) {
                    WebActivity.access$20208(WebActivity.this);
                    WebActivity.this.downloadDate();
                }
            }

            @Override // okhttp3.Callback
            public void onResponse(Call call2, Response response) throws IOException {
                Utils.dismissProgressDialog();
                WebActivity.this.getDataFromNetSuccess(response.body().string());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getDataFromNetSuccess(final String s) {
        final String username = Utils.getMsg(this.mContext, c.e);
        final List<Message> mList = new ArrayList<>();
        KLog.e(this.TAG, s);
        ThreadPoolManager.getInstance().execute(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.108
            /* JADX WARN: Removed duplicated region for block: B:43:0x01d4 A[Catch: Exception -> 0x01de, TryCatch #0 {Exception -> 0x01de, blocks: (B:3:0x0004, B:6:0x0022, B:9:0x0032, B:11:0x0040, B:41:0x01c2, B:43:0x01d4, B:44:0x01da, B:15:0x0051, B:16:0x0062, B:18:0x0068, B:20:0x0076, B:22:0x009d, B:24:0x00ba, B:23:0x00ac, B:25:0x00d8, B:26:0x00db, B:27:0x00f1, B:28:0x0125, B:30:0x012b, B:32:0x0139, B:34:0x015c, B:36:0x0166, B:38:0x0187, B:37:0x017c, B:39:0x01a6, B:40:0x01ac), top: B:52:0x0004 }] */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void run() {
                /*
                    Method dump skipped, instruction units count: 505
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.shineyue.pm.web.WebActivity.AnonymousClass108.run():void");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downloadDate() {
        final long time;
        try {
            long j = this.dingTime;
            if (j != 0) {
                time = j;
                if (j > 0) {
                    scrollToDingMsg(true);
                    return;
                }
            } else {
                time = System.currentTimeMillis() + 3600000;
            }
            if (!this.chatType.equals("6") && !this.chatType.equals("3")) {
                originalLogic(time, -1L);
                return;
            }
            if (!this.isNeedSjc) {
                this.isNeedSjc = true;
                long j2 = this.firstlimitTime;
                if (j2 == 0) {
                    originalLogic(time, -1L);
                    return;
                } else {
                    if (j2 != -1) {
                        originalLogic(time, j2);
                        return;
                    }
                    return;
                }
            }
            this.chatQxModule.queryChatTime(this.mId, this.toUser, new OnParmsCallBack<Long>() { // from class: com.shineyue.pm.web.WebActivity.109
                @Override // com.shineyue.pm.modle_chat.chat_history.dao.OnParmsCallBack
                public void onParmsResult(Long result) {
                    if (result.longValue() == 0) {
                        WebActivity.this.originalLogic(time, -1L);
                    } else if (result.longValue() != -1) {
                        WebActivity.this.originalLogic(time, result.longValue());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void originalLogic(long time, long limitTime) {
        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put("fromUserId", this.mId);
            if (this.chatType.equals("3") || this.chatType.equals("6")) {
                map.put("toUserIds", this.toUser);
                map.put("groups", "");
                map.put("queryType", "1");
                if (limitTime != -1) {
                    map.put("limitTime", Long.valueOf(limitTime));
                }
            } else if (this.chatType.equals("4") || this.chatType.equals("7")) {
                map.put("toUserIds", "");
                map.put("groups", this.toUser);
                map.put("queryType", "2");
            }
            map.put("queryTime", Long.valueOf(time));
            map.put("num", 20);
            String object = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
            KLog.d(this.TAG, object);
            String result = Base64Utils.encode(RSAUtils.encryptByPublicKey(object.getBytes(StandardCharsets.UTF_8.name()), RSAUtils.publicKey));
            String sign = URLEncoder.encode(URLEncoder.encode(result, StandardCharsets.UTF_8.name()), StandardCharsets.UTF_8.name());
            KLog.d(this.TAG, sign);
            if (this.dingTime > 0) {
                runOnUiThread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.110
                    @Override // java.lang.Runnable
                    public void run() {
                        Utils.showProgressDialog(WebActivity.this.mContext, "加载中", false);
                    }
                });
            }
            System.currentTimeMillis();
            ((PostRequest) OkGo.post(OkGoNetManager.CHAT_URL_FILE + OkGoNetManager.CHATQUERYRECORD + "?systemName=" + ChatServiceNew.getCHANNEL() + "&sign=" + sign).tag(this)).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.111
                @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
                public void onSuccess(final String s, Call call, Response response) {
                    final String username = Utils.getMsg(WebActivity.this.mContext, c.e);
                    final List<Message> mList = new ArrayList<>();
                    KLog.i(WebActivity.this.TAG, s);
                    ThreadPoolManager.getInstance().execute(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.111.1
                        /* JADX WARN: Removed duplicated region for block: B:49:0x023c A[Catch: Exception -> 0x0248, TryCatch #0 {Exception -> 0x0248, blocks: (B:3:0x0004, B:6:0x0024, B:9:0x0036, B:11:0x0046, B:47:0x0226, B:49:0x023c, B:50:0x0244, B:15:0x0059, B:16:0x006c, B:18:0x0072, B:20:0x0080, B:22:0x00a9, B:24:0x00ca, B:23:0x00ba, B:25:0x00f0, B:26:0x00f4, B:27:0x010c, B:28:0x0144, B:30:0x014a, B:32:0x0158, B:34:0x017b, B:36:0x0185, B:38:0x01a8, B:37:0x019b, B:39:0x01cf, B:40:0x01d5, B:42:0x01f5, B:44:0x020d, B:46:0x021d), top: B:58:0x0004 }] */
                        @Override // java.lang.Runnable
                        /*
                            Code decompiled incorrectly, please refer to instructions dump.
                            To view partially-correct code enable 'Show inconsistent code' option in preferences
                        */
                        public void run() {
                            /*
                                Method dump skipped, instruction units count: 617
                                To view this dump change 'Code comments level' option to 'DEBUG'
                            */
                            throw new UnsupportedOperationException("Method not decompiled: com.shineyue.pm.web.WebActivity.AnonymousClass111.AnonymousClass1.run():void");
                        }
                    });
                }

                @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
                public void onError(Call call, Response response, Exception e) {
                    Utils.dismissProgressDialog();
                    KLog.i(WebActivity.this.TAG, e.toString());
                    super.onError(call, response, e);
                    if (WebActivity.this.tryAgainTimes < 3) {
                        WebActivity.access$20208(WebActivity.this);
                        WebActivity.this.downloadDate();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: renamed from: com.shineyue.pm.web.WebActivity$112, reason: invalid class name */
    class AnonymousClass112 implements OnParmsCallBack<Long> {
        AnonymousClass112() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.shineyue.pm.modle_chat.chat_history.dao.OnParmsCallBack
        public void onParmsResult(Long result1) {
            if (result1.longValue() == 0) {
                WebActivity.this.newMoreDateNew();
                return;
            }
            if (result1.longValue() != -1) {
                try {
                    long time = System.currentTimeMillis();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("fromUserId", WebActivity.this.mId);
                    if (WebActivity.this.chatType.equals("3") || WebActivity.this.chatType.equals("6")) {
                        map.put("toUserIds", WebActivity.this.toUser);
                        map.put("groups", "");
                        map.put("queryType", "1");
                        map.put("limitTime", result1);
                    } else if (WebActivity.this.chatType.equals("4") || WebActivity.this.chatType.equals("7")) {
                        map.put("toUserIds", "");
                        map.put("groups", WebActivity.this.toUser);
                        map.put("queryType", "2");
                    }
                    if (WebActivity.this.list.size() > 0) {
                        map.put("queryTime", ((Message) WebActivity.this.list.get(0)).date);
                    } else {
                        map.put("queryTime", Long.valueOf(time));
                    }
                    map.put("num", "");
                    String object = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
                    KLog.d(WebActivity.this.TAG, object);
                    String result = Base64Utils.encode(RSAUtils.encryptByPublicKey(object.getBytes(StandardCharsets.UTF_8.name()), RSAUtils.publicKey));
                    String sign = URLEncoder.encode(URLEncoder.encode(result, StandardCharsets.UTF_8.name()), StandardCharsets.UTF_8.name());
                    KLog.d(WebActivity.this.TAG, sign);
                    ((PostRequest) OkGo.post(OkGoNetManager.CHAT_URL_FILE + OkGoNetManager.CHATQUERYRECORD + "?systemName=" + ChatServiceNew.getCHANNEL() + "&sign=" + sign).tag(this)).execute(new AnonymousClass1());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /* JADX INFO: renamed from: com.shineyue.pm.web.WebActivity$112$1, reason: invalid class name */
        class AnonymousClass1 extends MyStringCallback {
            AnonymousClass1() {
            }

            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onSuccess(final String s, Call call, Response response) {
                WebActivity.this.sv_msg.setRefreshing(false);
                ThreadPoolManager.getInstance().execute(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.112.1.1
                    /* JADX WARN: Can't wrap try/catch for region: R(11:0|2|(4:87|3|4|89)|(5:96|6|(1:8)(4:9|(3:94|11|(4:17|(4:20|(8:22|23|24|(1:26)(1:27)|28|(2:33|(1:35)(1:36))(1:32)|37|103)(2:39|102)|40|18)|101|41)(1:14))(0)|75|(2:77|104)(1:105))|85|86)(1:44)|90|45|(4:48|(9:50|(2:58|59)(1:54)|60|61|92|62|(2:67|(1:69)(1:70))(1:66)|71|100)(2:72|99)|73|46)|98|74|75|(0)(0)) */
                    /* JADX WARN: Code restructure failed: missing block: B:81:0x032b, code lost:
                    
                        r0 = e;
                     */
                    /* JADX WARN: Removed duplicated region for block: B:105:? A[RETURN, SYNTHETIC] */
                    /* JADX WARN: Removed duplicated region for block: B:17:0x00ae A[Catch: Exception -> 0x01d1, TRY_ENTER, TryCatch #5 {Exception -> 0x01d1, blocks: (B:6:0x0066, B:9:0x007c, B:17:0x00ae, B:18:0x00c3, B:20:0x00c9, B:22:0x00d7), top: B:96:0x0066 }] */
                    /* JADX WARN: Removed duplicated region for block: B:77:0x0317 A[Catch: Exception -> 0x0329, TRY_LEAVE, TryCatch #3 {Exception -> 0x0329, blocks: (B:62:0x0294, B:64:0x02a4, B:66:0x02aa, B:71:0x02d0, B:73:0x02f3, B:67:0x02b6, B:69:0x02cb, B:70:0x02ce, B:75:0x0306, B:77:0x0317), top: B:92:0x0294 }] */
                    @Override // java.lang.Runnable
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                        To view partially-correct code enable 'Show inconsistent code' option in preferences
                    */
                    public void run() {
                        /*
                            Method dump skipped, instruction units count: 824
                            To view this dump change 'Code comments level' option to 'DEBUG'
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.shineyue.pm.web.WebActivity.AnonymousClass112.AnonymousClass1.RunnableC02711.run():void");
                    }
                });
            }

            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                e.toString();
                KLog.i(WebActivity.this.TAG, e.toString());
                WebActivity.this.sv_msg.setRefreshing(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void newMoreDate() {
        try {
            if (this.chatType.equals("3") || this.chatType.equals("6")) {
                this.chatQxModule.queryChatTime(this.mId, this.toUser, new AnonymousClass112());
            } else {
                newMoreDateNew();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void newMoreDateNew() {
        try {
            long time = System.currentTimeMillis();
            HashMap<String, Object> map = new HashMap<>();
            map.put("fromUserId", this.mId);
            if (this.chatType.equals("3") || this.chatType.equals("6")) {
                map.put("toUserIds", this.toUser);
                map.put("groups", "");
                map.put("queryType", "1");
            } else if (this.chatType.equals("4") || this.chatType.equals("7")) {
                map.put("toUserIds", "");
                map.put("groups", this.toUser);
                map.put("queryType", "2");
            }
            if (this.list.size() > 0) {
                map.put("queryTime", this.list.get(0).date);
            } else {
                map.put("queryTime", Long.valueOf(time));
            }
            map.put("num", "");
            String object = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
            KLog.d(this.TAG, object);
            String result = Base64Utils.encode(RSAUtils.encryptByPublicKey(object.getBytes(StandardCharsets.UTF_8.name()), RSAUtils.publicKey));
            String sign = URLEncoder.encode(URLEncoder.encode(result, StandardCharsets.UTF_8.name()), StandardCharsets.UTF_8.name());
            KLog.d(this.TAG, sign);
            ((PostRequest) OkGo.post(OkGoNetManager.CHAT_URL_FILE + OkGoNetManager.CHATQUERYRECORD + "?systemName=" + ChatServiceNew.getCHANNEL() + "&sign=" + sign).tag(this)).execute(new AnonymousClass113());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: renamed from: com.shineyue.pm.web.WebActivity$113, reason: invalid class name */
    class AnonymousClass113 extends MyStringCallback {
        AnonymousClass113() {
        }

        @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
        public void onSuccess(final String s, Call call, Response response) {
            WebActivity.this.sv_msg.setRefreshing(false);
            ThreadPoolManager.getInstance().execute(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.113.1
                /* JADX WARN: Can't wrap try/catch for region: R(11:0|2|(4:89|3|4|93)|(5:87|6|(1:8)(4:9|(3:91|11|(4:17|(4:20|(8:22|23|24|(1:26)(1:27)|28|(2:33|(1:35)(1:36))(1:32)|37|103)(2:39|102)|40|18)|101|41)(1:14))(0)|75|(2:77|104)(1:105))|85|86)(1:44)|94|45|(4:48|(9:50|(2:58|59)(1:54)|60|61|96|62|(2:67|(1:69)(1:70))(1:66)|71|100)(2:72|99)|73|46)|98|74|75|(0)(0)) */
                /* JADX WARN: Code restructure failed: missing block: B:81:0x02f7, code lost:
                
                    r0 = e;
                 */
                /* JADX WARN: Removed duplicated region for block: B:105:? A[RETURN, SYNTHETIC] */
                /* JADX WARN: Removed duplicated region for block: B:17:0x00a0 A[Catch: Exception -> 0x01af, TRY_ENTER, TryCatch #0 {Exception -> 0x01af, blocks: (B:6:0x005e, B:9:0x0072, B:17:0x00a0, B:18:0x00b3, B:20:0x00b9, B:22:0x00c7), top: B:87:0x005e }] */
                /* JADX WARN: Removed duplicated region for block: B:77:0x02e5 A[Catch: Exception -> 0x02f5, TRY_LEAVE, TryCatch #5 {Exception -> 0x02f5, blocks: (B:62:0x0268, B:64:0x0278, B:66:0x027e, B:71:0x02a2, B:73:0x02c1, B:67:0x028a, B:69:0x029d, B:70:0x02a0, B:75:0x02d4, B:77:0x02e5), top: B:96:0x0268 }] */
                @Override // java.lang.Runnable
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct code enable 'Show inconsistent code' option in preferences
                */
                public void run() {
                    /*
                        Method dump skipped, instruction units count: 772
                        To view this dump change 'Code comments level' option to 'DEBUG'
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.shineyue.pm.web.WebActivity.AnonymousClass113.AnonymousClass1.run():void");
                }
            });
        }

        @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
        public void onError(Call call, Response response, Exception e) {
            super.onError(call, response, e);
            e.toString();
            KLog.i(WebActivity.this.TAG, e.toString());
            WebActivity.this.sv_msg.setRefreshing(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0047  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void getMessageChecked(com.shineyue.pm.modle_chat.bean.Message r9) {
        /*
            r8 = this;
            java.lang.String r0 = "0"
            java.lang.String r1 = r9.addInfo
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 != 0) goto L50
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch: org.json.JSONException -> L49
            java.lang.String r2 = r9.addInfo     // Catch: org.json.JSONException -> L49
            r1.<init>(r2)     // Catch: org.json.JSONException -> L49
            java.lang.String r2 = "type"
            java.lang.String r2 = r1.optString(r2)     // Catch: org.json.JSONException -> L49
            java.lang.String r3 = "1"
            boolean r3 = r3.equals(r2)     // Catch: org.json.JSONException -> L49
            java.lang.String r4 = "2"
            if (r3 != 0) goto L47
            boolean r3 = r4.equals(r2)     // Catch: org.json.JSONException -> L49
            if (r3 != 0) goto L47
            java.lang.String r3 = "3"
            boolean r3 = r3.equals(r2)     // Catch: org.json.JSONException -> L49
            if (r3 != 0) goto L47
            java.lang.String r3 = "4"
            boolean r3 = r3.equals(r2)     // Catch: org.json.JSONException -> L49
            if (r3 != 0) goto L47
            java.lang.String r3 = "6"
            boolean r3 = r3.equals(r2)     // Catch: org.json.JSONException -> L49
            if (r3 != 0) goto L47
            java.lang.String r3 = "share"
            boolean r3 = r3.equals(r2)     // Catch: org.json.JSONException -> L49
            if (r3 == 0) goto L48
        L47:
            r0 = r4
        L48:
            goto L50
        L49:
            r1 = move-exception
            java.lang.RuntimeException r2 = new java.lang.RuntimeException
            r2.<init>(r1)
            throw r2
        L50:
            java.lang.String r1 = r9.fileName
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 != 0) goto L93
            java.lang.String r2 = ""
            if (r1 == 0) goto L81
            java.lang.String r3 = "."
            int r3 = r1.lastIndexOf(r3)
            if (r3 <= 0) goto L81
            int r4 = r1.length()
            java.lang.String r4 = r1.substring(r3, r4)
            java.lang.String r2 = r4.toLowerCase()
            java.lang.String r4 = "?"
            boolean r5 = r2.contains(r4)
            if (r5 == 0) goto L81
            r5 = 0
            int r4 = r2.indexOf(r4)
            java.lang.String r2 = r2.substring(r5, r4)
        L81:
            java.lang.String r3 = ".mp3"
            boolean r3 = r2.contains(r3)
            if (r3 == 0) goto L93
            double r3 = r9.speechTime
            r5 = 0
            int r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r7 == 0) goto L93
            java.lang.String r0 = "2"
        L93:
            int r2 = r9.sendStatus
            r3 = 2
            if (r2 == r3) goto L9e
            int r2 = r9.sendStatus
            r3 = 9
            if (r2 != r3) goto La0
        L9e:
            java.lang.String r0 = "2"
        La0:
            r9.setChecked(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shineyue.pm.web.WebActivity.getMessageChecked(com.shineyue.pm.modle_chat.bean.Message):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updataUrl(Message message) {
    }

    private void upDate(final Message message) {
        ThreadPoolManager.getInstance().execute(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.114
            @Override // java.lang.Runnable
            public void run() {
                try {
                    KLog.e(WebActivity.this.TAG, "upDate方法");
                    KLog.e(WebActivity.this.TAG, "toUserId==" + message.toUserId);
                    KLog.e(WebActivity.this.TAG, "fromUserId==" + message.fromUserId);
                    KLog.e(WebActivity.this.TAG, "toUser==" + WebActivity.this.toUser);
                    KLog.e(WebActivity.this.TAG, "data==" + message.date);
                    if (WebActivity.this.list.size() <= 0 || TextUtils.isEmpty(message.date)) {
                        if (!WebActivity.this.list.contains(message)) {
                            WebActivity.this.isShowNewMessageDialog(message);
                            WebActivity.this.list.add(message);
                        }
                    } else if (!WebActivity.this.list.contains(message)) {
                        WebActivity.this.isShowNewMessageDialog(message);
                        WebActivity.this.list.add(message);
                    }
                    if (message.respType == 2) {
                        WebActivity webActivity = WebActivity.this;
                        if (!webActivity.isAppIsInBackground(webActivity.mContext)) {
                            WebActivity.this.sendReceive();
                        }
                    }
                    if (!message.toUserName.equals(WebActivity.this.toUserName)) {
                        WebActivity.this.runOnUiThread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.114.1
                            @Override // java.lang.Runnable
                            public void run() {
                                if (UtilsChatTopic.isHuati(WebActivity.this.toUser)) {
                                    WebActivity.this.tv_title_chat.setText("话题");
                                } else {
                                    WebActivity.this.tv_title_chat.setText(message.toUserName);
                                }
                                WebActivity.this.toUserName = message.toUserName;
                            }
                        });
                    }
                    WebActivity.this.runOnUiThread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.114.2
                        @Override // java.lang.Runnable
                        public void run() {
                            WebActivity.this.chatAdapter.notifyDataSetChanged();
                            if (WebActivity.this.isInBottom_1) {
                                WebActivity.this.scrollToBottom();
                            }
                        }
                    });
                } catch (Exception e) {
                    KLog.i(WebActivity.this.TAG, e.toString());
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void isShowNewMessageDialog(Message message) {
        if (message.respType == 2) {
            if (this.isSearchOrDing) {
                runOnUiThread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.115
                    @Override // java.lang.Runnable
                    public void run() {
                        WebActivity.this.mTvNewMessageDialog.setVisibility(0);
                    }
                });
            } else if (!this.isInBottom_1) {
                runOnUiThread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.116
                    @Override // java.lang.Runnable
                    public void run() {
                        WebActivity.this.mTvNewMessageDialog.setVisibility(0);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateChatWindow(Message message) {
        String type;
        if (message.type.equals("3") || message.type.equals("6")) {
            type = "1";
        } else {
            type = "2";
        }
        this.chatWindowListPresenter.addChatWindowToList(message, type, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateChatWindowNew(Message message) {
        this.chatWindowListPresenter.addChatWindowToList(message, "3", null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendReceive() {
        if (OaApplication.chatService != null) {
            ChatServiceNew chatServiceNew = OaApplication.chatService;
            if (ChatServiceNew.ws != null) {
                ChatServiceNew chatServiceNew2 = OaApplication.chatService;
                if (ChatServiceNew.ws.isOpen()) {
                    if (this.chatType.equals("3") || this.chatType.equals("6")) {
                        System.currentTimeMillis();
                        HashMap<String, String> map = new HashMap<>();
                        map.put("receiveType", "0");
                        map.put(DtnConfigItem.KEY_GROUP, "");
                        map.put("userId", this.toUser);
                        map.put("type", "13");
                        String object = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
                        try {
                            String result = Base64Utils.encode(RSAUtils.encryptByPublicKey(object.getBytes(StandardCharsets.UTF_8.name()), RSAUtils.publicKey));
                            String sign = URLEncoder.encode(result, StandardCharsets.UTF_8.name());
                            String sign2 = URLEncoder.encode(sign, StandardCharsets.UTF_8.name());
                            ChatServiceNew chatServiceNew3 = OaApplication.chatService;
                            ChatServiceNew.ws.sendText("{\"systemName\":\"" + ChatServiceNew.getCHANNEL() + "\",\"sign\":\"" + sign2 + "\"}");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (this.chatType.equals("4") || this.chatType.equals("7")) {
                        System.currentTimeMillis();
                        HashMap<String, String> map2 = new HashMap<>();
                        map2.put("receiveType", "1");
                        map2.put(DtnConfigItem.KEY_GROUP, this.toUser);
                        map2.put("userId", "");
                        map2.put("type", "13");
                        String object2 = com.alibaba.fastjson.JSONObject.toJSON(map2).toString();
                        try {
                            String result2 = Base64Utils.encode(RSAUtils.encryptByPublicKey(object2.getBytes(StandardCharsets.UTF_8.name()), RSAUtils.publicKey));
                            String sign3 = URLEncoder.encode(result2, StandardCharsets.UTF_8.name());
                            String sign4 = URLEncoder.encode(sign3, StandardCharsets.UTF_8.name());
                            ChatServiceNew chatServiceNew4 = OaApplication.chatService;
                            ChatServiceNew.ws.sendText("{\"systemName\":\"" + ChatServiceNew.getCHANNEL() + "\",\"sign\":\"" + sign4 + "\"}");
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    updataFromLastReadTime();
                    EventBus.getDefault().post(new Notify.Message(10001, this.toUser));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendHello() {
        if (OaApplication.chatService != null) {
            ChatServiceNew chatServiceNew = OaApplication.chatService;
            if (ChatServiceNew.ws != null) {
                ChatServiceNew chatServiceNew2 = OaApplication.chatService;
                if (ChatServiceNew.ws.isOpen()) {
                    long time = System.currentTimeMillis();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("uqIdentNo", UUID.randomUUID().toString());
                    map.put("toUserIds", this.toUser);
                    map.put("content", "hello");
                    map.put("type", "8");
                    map.put("time", time + "");
                    String object = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
                    try {
                        String result = Base64Utils.encode(RSAUtils.encryptByPublicKey(object.getBytes(StandardCharsets.UTF_8.name()), RSAUtils.publicKey));
                        String sign = URLEncoder.encode(result, StandardCharsets.UTF_8.name());
                        String sign2 = URLEncoder.encode(sign, StandardCharsets.UTF_8.name());
                        ChatServiceNew chatServiceNew3 = OaApplication.chatService;
                        ChatServiceNew.ws.sendText("{\"systemName\":\"" + ChatServiceNew.getCHANNEL() + "\",\"sign\":\"" + sign2 + "\"}");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Message updateOldDate(Message message) {
        if (message.date.contains("-")) {
            String date = message.date;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date1 = sdf.parse(date);
                long time = date1.getTime();
                message.date = time + "";
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return message;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void getPhonePermissions() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userid", this.toUser);
        map.put("ffbm", FFmpegSessionConfig.CRF_24);
        map.put("qdbs", "app_02");
        map.put("zxbm", Utils.getMsg(OaApplication.application, "zxjgbm"));
        JSONObject jsonObject = new JSONObject(map);
        OkGoNetManager.getInstance();
        ((PostRequest) OkGo.post(OkGoNetManager.APP_URL + OkGoNetManager.LTRYZTNEW).tag(this)).upJson(jsonObject.toString()).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.117
            /* JADX WARN: Code restructure failed: missing block: B:23:0x00b2, code lost:
            
                if (r12.this$0.phoneBz != false) goto L47;
             */
            /* JADX WARN: Code restructure failed: missing block: B:46:0x0144, code lost:
            
                if (r12.this$0.phoneBz != false) goto L47;
             */
            /* JADX WARN: Code restructure failed: missing block: B:47:0x0146, code lost:
            
                r12.this$0.iv_qun_call.setImageResource(com.shineyue.pmcs.R.drawable.iv_qun_call);
             */
            /* JADX WARN: Code restructure failed: missing block: B:48:0x0150, code lost:
            
                r12.this$0.iv_qun_call.setVisibility(8);
             */
            /* JADX WARN: Code restructure failed: missing block: B:49:0x0159, code lost:
            
                return;
             */
            /* JADX WARN: Code restructure failed: missing block: B:76:?, code lost:
            
                return;
             */
            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void onSuccess(java.lang.String r13, okhttp3.Call r14, okhttp3.Response r15) {
                /*
                    Method dump skipped, instruction units count: 503
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.shineyue.pm.web.WebActivity.AnonymousClass117.onSuccess(java.lang.String, okhttp3.Call, okhttp3.Response):void");
            }

            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x00e7 A[Catch: Exception -> 0x02be, TryCatch #0 {Exception -> 0x02be, blocks: (B:3:0x0010, B:4:0x0029, B:7:0x0037, B:9:0x0040, B:11:0x004d, B:13:0x0062, B:16:0x0074, B:19:0x007d, B:21:0x0085, B:33:0x00df, B:35:0x00e7, B:36:0x00ed, B:38:0x00f3, B:25:0x0091, B:28:0x00ae, B:31:0x00c7, B:12:0x0058, B:39:0x0101, B:41:0x0110, B:42:0x0115, B:45:0x0136, B:48:0x013f, B:50:0x0147, B:57:0x01c0, B:60:0x01dd, B:63:0x01e6, B:65:0x01ee, B:69:0x0201, B:67:0x01f6, B:68:0x01fc, B:52:0x014f, B:54:0x016d, B:55:0x0174, B:56:0x017b), top: B:74:0x0010 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private com.shineyue.pm.modle_chat.bean.Message createMergeMessage() {
        /*
            Method dump skipped, instruction units count: 709
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shineyue.pm.web.WebActivity.createMergeMessage():com.shineyue.pm.modle_chat.bean.Message");
    }

    private String getContentFromMessage(Message message) {
        int dotIndex;
        String fileName = message.fileName;
        String fileType = "";
        if (fileName != null && (dotIndex = fileName.lastIndexOf(StrUtil.DOT)) > 0) {
            fileType = fileName.substring(dotIndex, fileName.length()).toLowerCase();
            if (fileType.contains("?")) {
                fileType = fileType.substring(0, fileType.indexOf("?"));
            }
        }
        if (1 == FileTypeUtils.getDealFileTyppe(fileType)) {
            return "[图片]";
        }
        return "[文件]" + fileName;
    }

    public void setButtonStyle() {
        List<Message> checkedList = this.chatAdapter.getCheckedList();
        if (checkedList.size() > 0) {
            this.mLlForwardMerge.setClickable(true);
            this.mLlForwardMerge.setAlpha(1.0f);
        } else {
            this.mLlForwardMerge.setClickable(false);
            this.mLlForwardMerge.setAlpha(0.5f);
        }
    }

    public void changeStatus() {
        if (this.chatAdapter.isEditingStatus()) {
            this.mLlTitleRightContent.setVisibility(8);
            this.mTvMultipleChoiceCancle.setVisibility(0);
            this.ll_sendmessage.setVisibility(8);
            if (this.sfjy) {
                this.ll_no_speaking.setVisibility(8);
            }
            this.mLlForwardContent.setVisibility(0);
            this.isVideo = false;
            this.editEmojicon.setVisibility(0);
            this.mRvEditEmojiconContent.setVisibility(0);
            this.id_recorder_button.setVisibility(8);
            this.iv_voice.setImageResource(R.mipmap.chat_voice);
            this.editEmojicon.setVisibility(0);
            this.mRvEditEmojiconContent.setVisibility(0);
            this.id_recorder_button.setVisibility(8);
            this.isEmogiShow = true;
            hideKeyboard(this);
            this.editEmojicon.requestFocus();
            hideEmogiPanel();
            hideAddPanel();
            return;
        }
        for (int i = 0; i < this.list.size(); i++) {
            Message message = this.list.get(i);
            getMessageChecked(message);
        }
        this.chatAdapter.cleanChecked();
        this.mLlTitleRightContent.setVisibility(0);
        this.mTvMultipleChoiceCancle.setVisibility(8);
        this.mLlForwardContent.setVisibility(8);
        if (this.sfjy) {
            this.ll_no_speaking.setVisibility(0);
        } else {
            this.ll_sendmessage.setVisibility(0);
        }
    }

    public void multipleChoiceNum(int num) {
        if (num <= 10) {
            this.mLlForwardItem.setClickable(true);
            this.mLlForwardItem.setAlpha(1.0f);
            this.mLlForwardMerge.setClickable(true);
            this.mLlForwardMerge.setAlpha(1.0f);
            return;
        }
        if (num <= 50 && num > 10) {
            this.mLlForwardItem.setClickable(false);
            this.mLlForwardItem.setAlpha(0.5f);
            this.mLlForwardMerge.setClickable(true);
            this.mLlForwardMerge.setAlpha(1.0f);
            return;
        }
        this.mLlForwardItem.setClickable(false);
        this.mLlForwardItem.setAlpha(0.5f);
        this.mLlForwardMerge.setClickable(false);
        this.mLlForwardMerge.setAlpha(0.5f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void createHuaTi(final String name, final String msgtype, final Message message) {
        if ("3".equals(this.chatType) || "6".equals(this.chatType)) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("jgbh", Utils.getMsg(OaApplication.application, "jgbh"));
            map.put("deptids", Utils.getMsg(this.mContext, "deptids_new"));
            map.put("roles", Utils.getMsg(this.mContext, "roleid_new"));
            map.put("mainDeptid", Utils.getMsg(this.mContext, "wddeptid"));
            map.put("userid", this.toUser);
            JSONObject jsonObject = new JSONObject(map);
            KLog.d(this.TAG, jsonObject.toString());
            OkGoNetManager.getInstance();
            KLog.d(this.TAG, OkGoNetManager.APP_URL + OkGoNetManager.CHATQXCXM);
            ((PostRequest) OkGo.post(OkGoNetManager.APP_URL + OkGoNetManager.CHATQXCXM).tag(this)).upJson(jsonObject.toString()).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.119
                @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
                public void onSuccess(String s, Call call, Response response) {
                    try {
                        KLog.d(WebActivity.this.TAG, s);
                        JSONObject jsonObject1 = new JSONObject(s);
                        if (jsonObject1.optBoolean(ToastType.SUCCESS)) {
                            WebActivity.this.createHuaTiNew(name, msgtype, message);
                        } else if (TextUtils.isEmpty(jsonObject1.optString("msg"))) {
                            ToastUtil.toast(WebActivity.this.mContext, "msg", "无法向该用户发起聊天", ToastType.FAIL);
                        } else {
                            ToastUtil.toast(WebActivity.this.mContext, "msg", jsonObject1.optString("msg"), ToastType.FAIL);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    KLog.d(WebActivity.this.TAG, e.toString());
                }
            });
            return;
        }
        if ("4".equals(this.chatType) || "7".equals(this.chatType)) {
            createHuaTiNew(name, msgtype, message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void createHuaTiNew(final String name, final String msgtype, final Message message) {
        if (this.sfjy) {
            ToastUtil.toast(this.mContext, "msg", "发送失败，您已被禁言", ToastType.FAIL);
            return;
        }
        Object sourceId = UUID.randomUUID().toString();
        String currName = null;
        if ("1".equals(msgtype)) {
            currName = name;
            if (currName.length() > 1000) {
                ToastUtil.toast(this.mContext, ToastInfo.outOfRange, "话题内容", "1000字", ToastType.WARNING);
                return;
            }
        } else if ("2".equals(msgtype)) {
            currName = "[图片]";
        } else if ("3".equals(msgtype)) {
            currName = "[文件]" + message.fileName;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("lanchUserId", this.mId);
        map.put("lanchUserName", this.mName);
        if ("3".equals(this.chatType) || "6".equals(this.chatType)) {
            map.put("groupType", "1");
        } else if ("4".equals(this.chatType) || "7".equals(this.chatType)) {
            map.put("groupType", "2");
        }
        if ((this.mName + "：" + currName).length() > 20) {
            StringBuilder sb = new StringBuilder();
            sb.append((this.mName + "：" + currName).substring(0, 20));
            sb.append("...");
            map.put("groupName", sb.toString());
        } else {
            map.put("groupName", this.mName + "：" + currName);
        }
        map.put("createTime", DateUtils.getCurrTime());
        map.put("sourceId", sourceId);
        map.put("toId", this.toUser);
        String jsonObject = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
        KLog.d(this.TAG, jsonObject);
        OkGoNetManager.getInstance();
        ((PostRequest) OkGo.post(OkGoNetManager.APP_URL + OkGoNetManager.CREATE_HUATI).tag(this)).upJson(jsonObject.toString()).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.120
            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onSuccess(String s, Call call, Response response) {
                try {
                    KLog.d(WebActivity.this.TAG, s);
                    JSONObject jsonObject1 = new JSONObject(s);
                    if (jsonObject1.optInt("code") != 0) {
                        ToastUtil.toast(WebActivity.this.mContext, ToastInfo.btnError, "创建话题", ToastType.FAIL);
                        return;
                    }
                    JSONObject dataJSONObject = jsonObject1.optJSONObject("data");
                    String groupId = dataJSONObject.optString("groupId");
                    HashMap<String, Object> addInfo = new HashMap<>();
                    addInfo.put("type", "6");
                    addInfo.put("groupName", WebActivity.this.mName + "：" + name);
                    addInfo.put("groupId", groupId);
                    addInfo.put("msgType", msgtype);
                    addInfo.put("createId", WebActivity.this.mId);
                    addInfo.put("createName", WebActivity.this.mName);
                    if ("1".equals(msgtype)) {
                        addInfo.put("content", name);
                        WebActivity webActivity = WebActivity.this;
                        webActivity.sendMsg(webActivity.toUser, "话题，" + name, com.alibaba.fastjson.JSONObject.toJSON(addInfo).toString(), dataJSONObject.optString("sourceId"));
                        WebActivity.this.sendHuaTiMsg(groupId, WebActivity.this.mName + "：" + name);
                    } else if ("2".equals(msgtype)) {
                        if (!TextUtils.isEmpty(message.addInfo)) {
                            JSONObject jsonObject2 = new JSONObject(message.addInfo);
                            addInfo.put(CustomIntentKey.EXTRA_IMAGE_WIDTH, jsonObject2.getString(CustomIntentKey.EXTRA_IMAGE_WIDTH));
                            addInfo.put(CustomIntentKey.EXTRA_IMAGE_HEIGHT, jsonObject2.getString(CustomIntentKey.EXTRA_IMAGE_HEIGHT));
                        }
                        addInfo.put(DownloadInfo.FILE_NAME, message.fileName);
                        addInfo.put("fileUrl", message.fileUrl);
                        WebActivity webActivity2 = WebActivity.this;
                        webActivity2.sendMsg(webActivity2.toUser, "话题，[图片]", com.alibaba.fastjson.JSONObject.toJSON(addInfo).toString(), dataJSONObject.optString("sourceId"));
                        WebActivity.this.sendHuaTiMsg(groupId, WebActivity.this.mName + "：[图片]");
                    } else if ("3".equals(msgtype)) {
                        addInfo.put(DownloadInfo.FILE_NAME, message.fileName);
                        addInfo.put("fileUrl", message.fileUrl);
                        WebActivity webActivity3 = WebActivity.this;
                        webActivity3.sendMsg(webActivity3.toUser, "话题，[文件]" + message.fileName, com.alibaba.fastjson.JSONObject.toJSON(addInfo).toString(), dataJSONObject.optString("sourceId"));
                        WebActivity.this.sendHuaTiMsg(groupId, WebActivity.this.mName + "：[文件]" + message.fileName);
                    }
                    if (WebActivity.this.mRlHuaTiContent.getVisibility() == 0) {
                        WebActivity.this.closeHuaTi();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.toast(WebActivity.this.mContext, ToastInfo.btnError, "创建话题", ToastType.FAIL);
                }
            }

            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                KLog.d(WebActivity.this.TAG, e.toString());
                ToastUtil.toast(WebActivity.this.mContext, ToastInfo.btnError, "创建话题", ToastType.FAIL);
            }
        });
    }

    private void initKeyBoradListener() {
        if (!Utils.isPad(this.mContext)) {
            KeyBoardListener.setListener((Activity) this.mContext, new KeyBoardListener.OnSoftKeyBoardChangeListener() { // from class: com.shineyue.pm.web.WebActivity.121
                @Override // com.shineyue.pm.utils.KeyBoardListener.OnSoftKeyBoardChangeListener
                public void keyBoardShow(int height) {
                    WebActivity.this.keyBoardShow = true;
                    WebActivity.this.replaceFullImg();
                    WebActivity.this.isEmogiShowFull = false;
                    WebActivity.this.mIvEmojFull.setImageResource(R.mipmap.chat_icon);
                }

                @Override // com.shineyue.pm.utils.KeyBoardListener.OnSoftKeyBoardChangeListener
                public void keyBoardHide(int height) {
                    WebActivity.this.keyBoardShow = false;
                    WebActivity.this.replaceFullImg();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void replaceFullImg() {
        if (this.keyBoardShow || this.isEmogiShowFull) {
            this.mIvOpenOrClose.setImageResource(R.mipmap.full_screen_down);
        } else {
            this.mIvOpenOrClose.setImageResource(R.mipmap.full_screen_up);
        }
    }

    private void closeFullScreen() {
        if (this.chatFullCloseAnimation == null) {
            TranslateAnimation translateAnimation = (TranslateAnimation) AnimationUtils.loadAnimation(this.mContext, R.anim.chat_full_screen_close);
            this.chatFullCloseAnimation = translateAnimation;
            translateAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.shineyue.pm.web.WebActivity.122
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation) {
                    if (WebActivity.this.isEmogiShowFull) {
                        WebActivity.this.showEmojFull();
                    }
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation) {
                    WebActivity.this.isshowFull = false;
                    WebActivity.this.rl_full_screen_content.setVisibility(8);
                    WebActivity.this.ll.setBackgroundColor(Color.parseColor("#ffffff"));
                    WebActivity.this.mViewMask.setVisibility(8);
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
        this.rl_full_screen_content.startAnimation(this.chatFullCloseAnimation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeHuaTi() {
        if (this.chatHuaTiCloseAnimation == null) {
            Animation animationLoadAnimation = AnimationUtils.loadAnimation(this.mContext, R.anim.chat_full_screen_close);
            this.chatHuaTiCloseAnimation = animationLoadAnimation;
            animationLoadAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.shineyue.pm.web.WebActivity.123
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation) {
                    WebActivity.this.mRlHuaTiContent.setVisibility(8);
                    WebActivity.this.ll.setBackgroundColor(Color.parseColor("#ffffff"));
                    WebActivity.this.mViewMask.setVisibility(8);
                    WebActivity.this.mEetHuaTi.setText("");
                    WebActivity.this.showAdd();
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
        this.mRlHuaTiContent.startAnimation(this.chatHuaTiCloseAnimation);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callAit() {
        if (UtilsChatTopic.isHuati(this.toUser)) {
            return;
        }
        if (isKeyboardShown(this.ll)) {
            hideKeyboard(this);
        }
        if (this.chatType.equals("4") || this.chatType.equals("7")) {
            getGroupUser("");
        }
    }

    private void openCamareChat() {
        checkCameraPermission();
    }

    private void checkCameraPermission() {
        PermissionsUtils.getInstance().chekPermissions(this, this.permissions_video, this.permissionsResult_camera);
    }

    private void openPic() {
        checkPicPermission();
    }

    private void checkPicPermission() {
        PermissionsUtils.getInstance().chekPermissions(this, this.permissions_write_external_storage, this.permissionsResult_pic);
    }

    public int[] getImageWidthHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        return new int[]{options.outWidth, options.outHeight};
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0078 A[Catch: Exception -> 0x0088, TRY_LEAVE, TryCatch #1 {Exception -> 0x0088, blocks: (B:20:0x005e, B:25:0x0078), top: B:61:0x005e }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x008b  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x017a A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void saveFile(java.lang.String r18, java.lang.String r19, double r20, boolean r22, boolean r23, java.lang.String r24, java.lang.String r25) {
        /*
            Method dump skipped, instruction units count: 402
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shineyue.pm.web.WebActivity.saveFile(java.lang.String, java.lang.String, double, boolean, boolean, java.lang.String, java.lang.String):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void upDataListAndUpFile(Message message, int num) {
        GreenDaoHelper.getDaoSession().getMessageDao().insertOrReplaceInTx(message);
        this.list.add(message);
        LogInfo();
        this.chatAdapter.notifyDataSetChanged();
        scrollToBottom();
        upDateFile(message, num);
    }

    private void upDateFile(Message message, int num) {
        int dotIndex;
        if (message.getFileUrl().contains("http")) {
            sendFile(message.fileUrl, message.fileName, message, num);
            return;
        }
        File file = new File(message.getFileUrl());
        String fileName = file.getName();
        if (fileName != null && (dotIndex = fileName.lastIndexOf(StrUtil.DOT)) > 0) {
            fileName.substring(dotIndex, fileName.length()).toLowerCase();
        }
        getToken(file, message, num);
    }

    /* JADX INFO: renamed from: com.shineyue.pm.web.WebActivity$126, reason: invalid class name */
    class AnonymousClass126 extends MyStringCallback {
        final /* synthetic */ File val$file;
        final /* synthetic */ Message val$message;
        final /* synthetic */ int val$num;

        AnonymousClass126(Message message, int i, File file) {
            this.val$message = message;
            this.val$num = i;
            this.val$file = file;
        }

        @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
        public void onSuccess(String s, Call call, Response response) {
            try {
                JSONObject result = new JSONObject(s);
                KLog.i(WebActivity.this.TAG + "1", result);
                String msg = result.getString("msg");
                msg.contains("成功");
                if (result.getString("msg").contains("成功")) {
                    String fileUrl = result.getString("fileUrl");
                    String fileName = result.getString(DownloadInfo.FILE_NAME);
                    WebActivity.this.sendFile(fileUrl, fileName, this.val$message, this.val$num);
                    if (this.val$file.length() < HttpFileUploader.BIG_FILE_SIZE_THRESHOLD) {
                        WebActivity.this.getKkfileType(fileUrl);
                    }
                    return;
                }
                this.val$message.sendStatus = 2;
                ToastUtil.toast(WebActivity.this.mContext, "msg", result.optString("msg"), ToastType.FAIL);
                WebActivity.this.updataMessage(this.val$message);
            } catch (Exception e) {
                e.printStackTrace();
                KLog.e(WebActivity.this.TAG, e.getMessage());
            }
        }

        @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
        public void onError(Call call, Response response, Exception e) {
            super.onError(call, response, e);
            KLog.i(WebActivity.this.TAG, e.toString());
            this.val$message.sendStatus = 2;
            WebActivity.this.updataMessage(this.val$message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendFile(String fileUrl, String fileName, Message message, int num) {
        int dotIndex;
        try {
            long time = System.currentTimeMillis();
            HashMap<String, Object> map = new HashMap<>();
            map.put("fileUrl", fileUrl);
            map.put(DownloadInfo.FILE_NAME, fileName);
            if (fileName != null && (dotIndex = fileName.lastIndexOf(StrUtil.DOT)) > 0) {
                fileName.substring(dotIndex, fileName.length()).toLowerCase();
            }
            if (message.speechTime > 0.0d) {
                map.put("speechTime", Double.valueOf(message.speechTime));
            }
            String ty = message.getType();
            if (ty.equals("6") || ty.equals("3")) {
                map.put("toUserId", message.toUserId);
                map.put("toUserName", message.toUserName);
                map.put("type", message.type);
            } else if (ty.equals("7") || ty.equals("4")) {
                map.put("toGroup", message.toGroup);
                map.put("groupName", message.groupName);
                map.put("toUserId", message.toUserId);
                map.put("type", message.type);
            }
            if (!TextUtils.isEmpty(message.addInfo)) {
                map.put("addInfo", message.addInfo);
            }
            if (OaApplication.JIQIRENID.equals(this.toUser)) {
                HashMap<String, Object> addInfo = new HashMap<>();
                addInfo.put("idcard", Utils.getMsg(this.mContext, "zjhm"));
                addInfo.put("phone", Utils.getMsg(this.mContext, "sjhm"));
                addInfo.put("tenantId", Utils.getMsg(this.mContext, "jgbh"));
                map.put("addInfo", com.alibaba.fastjson.JSONObject.toJSON(addInfo).toString());
            }
            map.put("uqIdentNo", message.uqIdentNo);
            map.put("time", time + "");
            String object = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
            KLog.d(this.TAG, object);
            String result = Base64Utils.encode(RSAUtils.encryptByPublicKey(object.getBytes(StandardCharsets.UTF_8.name()), RSAUtils.publicKey));
            String sign = URLEncoder.encode(result, StandardCharsets.UTF_8.name());
            KLog.d(this.TAG, RSAUtils.getValue(URLEncoder.encode(sign, StandardCharsets.UTF_8.name()), RSAUtils.privateKey));
            try {
                socketSend(map, num);
                message.sendStatus = 9;
                message.time = time;
                message.date = time + "";
                message.fileName = fileName;
                message.fileUrl = fileUrl;
                updataMessage(message);
            } catch (Exception e) {
                e = e;
                e.printStackTrace();
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    public File compressImage(Context context, Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String filename = format.format(date);
        File file = new File(CachePzthConfigs.getIns().getFilesDir(true, "image"), filename + ".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        return file;
    }

    private void sendMsgCnxw(String toUser, String content, String addInfoParam, String uqIdentNo) {
        long time;
        HashMap<String, Object> map;
        String uuid;
        try {
            time = System.currentTimeMillis();
            map = new HashMap<>();
            if (this.isQuote) {
                map.putAll(this.quoteMap);
            }
        } catch (Exception e) {
            e = e;
        }
        try {
            map.put("content", content);
            if (this.chatType.equals("6") || this.chatType.equals("3")) {
                map.put("toUserId", toUser);
                map.put("toUserName", this.toUserName);
                map.put("type", "3");
            } else if (this.chatType.equals("7") || this.chatType.equals("4")) {
                map.put("toGroup", toUser);
                map.put("groupName", this.toUserName);
                map.put("toUserId", toUser);
                map.put("type", "4");
            }
            if (OaApplication.JIQIRENID.equals(toUser)) {
                HashMap<String, Object> addInfo = new HashMap<>();
                addInfo.put("idcard", Utils.getMsg(this.mContext, "zjhm"));
                addInfo.put("phone", Utils.getMsg(this.mContext, "sjhm"));
                addInfo.put("tenantId", Utils.getMsg(this.mContext, "jgbh"));
                if (!TextUtils.isEmpty(addInfoParam)) {
                    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(addInfoParam);
                    jsonObject.putAll(addInfo);
                    map.put("addInfo", jsonObject.toString());
                } else {
                    map.put("addInfo", com.alibaba.fastjson.JSONObject.toJSON(addInfo).toString());
                }
            }
            if (TextUtils.isEmpty(uqIdentNo)) {
                uuid = UUID.randomUUID().toString();
            } else {
                uuid = uqIdentNo;
            }
            map.put("uqIdentNo", uuid);
            map.put("time", time + "");
            String object = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
            KLog.d(this.TAG, "加密前数据" + object.toString());
            String result = Base64Utils.encode(RSAUtils.encryptByPublicKey(object.getBytes(StandardCharsets.UTF_8.name()), RSAUtils.publicKey));
            String sign = URLEncoder.encode(result, StandardCharsets.UTF_8.name());
            KLog.d(this.TAG, RSAUtils.getValue(URLEncoder.encode(sign, StandardCharsets.UTF_8.name()), RSAUtils.privateKey));
            socketSend(map, 0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.format(Long.valueOf(time));
            map.put("date", time + "");
            map.put("fromUserId", this.mId);
            map.put("fromUserName", this.mName);
            map.put("respType", "1");
            map.put("toUserName", this.toUserName);
            map.put("isRead", StreamerConstants.FALSE);
            map.put("myId", this.mId);
            map.put("sendStatus", "9");
            AitManager aitManager = this.aitManager;
            if (aitManager != null) {
                List<String> userIds = aitManager.getUserArray();
                if (userIds.size() > 0) {
                    String content_str = "";
                    int i = 0;
                    while (true) {
                        SimpleDateFormat sdf2 = sdf;
                        if (i >= userIds.size()) {
                            break;
                        }
                        content_str = content_str + userIds.get(i) + ",";
                        i++;
                        sdf = sdf2;
                        userIds = userIds;
                    }
                    sendAitMsg(toUser, uuid);
                    Message message = (Message) new Gson().fromJson(com.alibaba.fastjson.JSONObject.toJSON(map).toString(), Message.class);
                    message.setContent(content_str);
                    ChatAitPresenter.sendAitMessage(message);
                }
            }
            this.isEditSet = true;
            this.editEmojicon.setText("");
            this.getmExpressionEditTextFull.setText("");
            delQuote();
            String object2 = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
            KLog.d(this.TAG, object2);
            KLog.i(this.TAG, this.chatType);
            updataMessage((Message) new Gson().fromJson(object2, Message.class));
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
            KLog.e(this.TAG, e.getMessage());
            ToastUtil.toast(this.mContext, ToastInfo.btnError, "发送", ToastType.FAIL);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMsg(String toUser, String content, String addInfoParam, String uqIdentNo) {
        long time;
        HashMap<String, Object> map;
        String uuid;
        try {
            time = System.currentTimeMillis();
            map = new HashMap<>();
            if (this.isQuote) {
                map.putAll(this.quoteMap);
            }
        } catch (Exception e) {
            e = e;
        }
        try {
            map.put("content", content);
            if (this.chatType.equals("6") || this.chatType.equals("3")) {
                map.put("toUserId", toUser);
                map.put("toUserName", this.toUserName);
                map.put("type", "3");
            } else if (this.chatType.equals("7") || this.chatType.equals("4")) {
                map.put("toGroup", toUser);
                map.put("groupName", this.toUserName);
                map.put("toUserId", toUser);
                map.put("type", "4");
            }
            if (OaApplication.JIQIRENID.equals(toUser)) {
                HashMap<String, Object> addInfo = new HashMap<>();
                addInfo.put("idcard", Utils.getMsg(this.mContext, "zjhm"));
                addInfo.put("phone", Utils.getMsg(this.mContext, "sjhm"));
                addInfo.put("tenantId", Utils.getMsg(this.mContext, "jgbh"));
                map.put("addInfo", com.alibaba.fastjson.JSONObject.toJSON(addInfo).toString());
            }
            if (!TextUtils.isEmpty(addInfoParam)) {
                try {
                    map.put("addInfo", addInfoParam);
                } catch (Exception e2) {
                    e = e2;
                    e.printStackTrace();
                    KLog.e(this.TAG, e.getMessage());
                    ToastUtil.toast(this.mContext, ToastInfo.btnError, "发送", ToastType.FAIL);
                }
            }
            if (TextUtils.isEmpty(uqIdentNo)) {
                uuid = UUID.randomUUID().toString();
            } else {
                uuid = uqIdentNo;
            }
            map.put("uqIdentNo", uuid);
            map.put("time", time + "");
            String object = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
            KLog.d(this.TAG, "加密前数据" + object.toString());
            String result = Base64Utils.encode(RSAUtils.encryptByPublicKey(object.getBytes(StandardCharsets.UTF_8.name()), RSAUtils.publicKey));
            String sign = URLEncoder.encode(result, StandardCharsets.UTF_8.name());
            KLog.d(this.TAG, RSAUtils.getValue(URLEncoder.encode(sign, StandardCharsets.UTF_8.name()), RSAUtils.privateKey));
            socketSend(map, 0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(Long.valueOf(time));
            map.put("date", time + "");
            map.put("fromUserId", this.mId);
            map.put("fromUserName", this.mName);
            map.put("respType", "1");
            map.put("toUserName", this.toUserName);
            map.put("isRead", StreamerConstants.FALSE);
            map.put("myId", this.mId);
            map.put("sendStatus", "9");
            AitManager aitManager = this.aitManager;
            if (aitManager != null) {
                List<String> userIds = aitManager.getUserArray();
                if (userIds.size() > 0) {
                    String content_str = "";
                    int i = 0;
                    while (true) {
                        SimpleDateFormat sdf2 = sdf;
                        if (i >= userIds.size()) {
                            break;
                        }
                        content_str = content_str + userIds.get(i) + ",";
                        i++;
                        sdf = sdf2;
                        date = date;
                    }
                    sendAitMsg(toUser, uuid);
                    Message message = (Message) new Gson().fromJson(com.alibaba.fastjson.JSONObject.toJSON(map).toString(), Message.class);
                    message.setContent(content_str);
                    ChatAitPresenter.sendAitMessage(message);
                }
            }
            this.isEditSet = true;
            this.editEmojicon.setText("");
            this.getmExpressionEditTextFull.setText("");
            delQuote();
            String object2 = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
            KLog.d(this.TAG, object2);
            KLog.i(this.TAG, this.chatType);
            updataMessage((Message) new Gson().fromJson(object2, Message.class));
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            KLog.e(this.TAG, e.getMessage());
            ToastUtil.toast(this.mContext, ToastInfo.btnError, "发送", ToastType.FAIL);
        }
    }

    private void sendRenGongMsg() {
        try {
            long time = System.currentTimeMillis();
            HashMap<String, Object> map = new HashMap<>();
            String uuid = UUID.randomUUID().toString();
            map.put("uqIdentNo", uuid);
            map.put("content", "转人工");
            map.put("toUserIds", OaApplication.JIQIRENID);
            map.put("type", "8");
            map.put("time", time + "");
            HashMap<String, Object> addInfo = new HashMap<>();
            addInfo.put("idcard", Utils.getMsg(this.mContext, "zjhm"));
            addInfo.put("phone", Utils.getMsg(this.mContext, "sjhm"));
            addInfo.put("tenantId", Utils.getMsg(this.mContext, "jgbh"));
            addInfo.put("massageType", "80");
            map.put("addInfo", com.alibaba.fastjson.JSONObject.toJSON(addInfo));
            String object = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
            KLog.d(this.TAG, "加密前数据" + object.toString());
            String result = Base64Utils.encode(RSAUtils.encryptByPublicKey(object.getBytes(StandardCharsets.UTF_8.name()), RSAUtils.publicKey));
            String sign = URLEncoder.encode(result, StandardCharsets.UTF_8.name());
            KLog.d(this.TAG, RSAUtils.getValue(URLEncoder.encode(sign, StandardCharsets.UTF_8.name()), RSAUtils.privateKey));
            socketSend(map, 0);
        } catch (Exception e) {
            e.printStackTrace();
            KLog.e(this.TAG, e.getMessage());
            ToastUtil.toast(this.mContext, ToastInfo.btnError, "发送", ToastType.FAIL);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendHuaTiMsg(String huaTiId, String groupName) {
        try {
            long time = System.currentTimeMillis();
            Object id = UUID.randomUUID().toString();
            HashMap<String, Object> map = new HashMap<>();
            map.put("content", this.mName + "创建了话题");
            map.put("noticeLaunchUserIds", this.mId);
            map.put("noticeLaunchUserNames", this.mName);
            map.put("noticeType", "10");
            try {
                map.put("toGroup", huaTiId);
                map.put("groupName", groupName);
                map.put("type", "4");
                map.put("uqIdentNo", id);
                map.put("time", time + "");
                String object = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
                KLog.d(this.TAG, object);
                String result = Base64Utils.encode(RSAUtils.encryptByPublicKey(object.getBytes(StandardCharsets.UTF_8.name()), RSAUtils.publicKey));
                String sign = URLEncoder.encode(URLEncoder.encode(result, StandardCharsets.UTF_8.name()), StandardCharsets.UTF_8.name());
                KLog.d(this.TAG, RSAUtils.getValue(sign, RSAUtils.privateKey));
                UtilTips.sendMsgTips(OaApplication.application);
                ChatServiceNew chatServiceNew = OaApplication.chatService;
                ChatServiceNew.ws.sendText("{\"systemName\":\"" + ChatServiceNew.getCHANNEL() + "\",\"sign\":\"" + sign + "\"}");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.format(Long.valueOf(time));
                StringBuilder sb = new StringBuilder();
                sb.append(time);
                sb.append("");
                map.put("date", sb.toString());
                map.put("fromUserId", Utils.getMsg(this, "userid"));
                map.put("fromUserName", Utils.getMsg(this, c.e));
                map.put("respType", "1");
                map.put("toUserName", groupName);
                map.put("isRead", StreamerConstants.FALSE);
                map.put("myId", Utils.getMsg(this, "userid"));
                map.put("sendStatus", "9");
                Message message = (Message) new Gson().fromJson(com.alibaba.fastjson.JSONObject.toJSON(map).toString(), Message.class);
                EventBus.getDefault().post(new Notify.Message(Notify.QUNMSG, message));
                updataMessageNew(message);
            } catch (Exception e) {
                e = e;
                e.printStackTrace();
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    private void socketSend(HashMap<String, Object> map, int num) {
        try {
            String object = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
            KLog.d(this.TAG, "加密前数据" + object.toString());
            String result = Base64Utils.encode(RSAUtils.encryptByPublicKey(object.getBytes(StandardCharsets.UTF_8.name()), RSAUtils.publicKey));
            String sign = URLEncoder.encode(URLEncoder.encode(result, StandardCharsets.UTF_8.name()), StandardCharsets.UTF_8.name());
            KLog.d(this.TAG, RSAUtils.getValue(sign, RSAUtils.privateKey));
            String tipMsg = UtilTips.sendMsgTips(this.mContext);
            if (!TextUtils.isEmpty(tipMsg)) {
                ToastUtil.toast(this.mContext, "msg", tipMsg, ToastType.FAIL);
            }
            if (NetUtils.checkWifi(this.mContext).intValue() != 3) {
                if (OaApplication.chatService != null) {
                    ChatServiceNew chatServiceNew = OaApplication.chatService;
                    if (ChatServiceNew.ws != null) {
                        ChatServiceNew chatServiceNew2 = OaApplication.chatService;
                        if (ChatServiceNew.ws.isOpen()) {
                            ChatServiceNew chatServiceNew3 = OaApplication.chatService;
                            ChatServiceNew.ws.sendText("{\"systemName\":\"" + ChatServiceNew.getCHANNEL() + "\",\"sign\":\"" + sign + "\"}");
                            KLog.e(this.TAG, "{\"systemName\":\"" + ChatServiceNew.getCHANNEL() + "\",\"sign\":\"" + sign + "\"}");
                            StringBuilder sb = new StringBuilder();
                            sb.append(map.get("uqIdentNo"));
                            sb.append("");
                            startTimer(sb.toString(), num);
                            if (!"6".equals(map.get("type") + "")) {
                                if ("7".equals(map.get("type") + "")) {
                                    fileUrlUpdata(map.get("fileUrl") + "", map.get(DownloadInfo.FILE_NAME) + "", this.mId, this.mName, null);
                                    return;
                                }
                                return;
                            }
                            String str = this.toUser;
                            if (str != null && str.equals(this.mId)) {
                                fileUrlUpdata(map.get("fileUrl") + "", map.get(DownloadInfo.FILE_NAME) + "", this.mId, this.mName, null);
                                return;
                            }
                            fileUrlUpdata(map.get("fileUrl") + "", map.get(DownloadInfo.FILE_NAME) + "", this.toUser, this.mName, null);
                            fileUrlUpdata(map.get("fileUrl") + "", map.get(DownloadInfo.FILE_NAME) + "", this.mId, this.mName, null);
                            return;
                        }
                    }
                }
                if (OaApplication.chatService != null) {
                    ChatServiceNew chatServiceNew4 = OaApplication.chatService;
                    if (ChatServiceNew.ws != null && OaApplication.chatService.isopen()) {
                        return;
                    }
                }
                ChatServiceNew.startChatService(OaApplication.application.getApplicationContext());
            }
        } catch (Exception e) {
            KLog.i(this.TAG, e.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updataMessage(final Message message) {
        ThreadPoolManager.getInstance().execute(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.127
            /* JADX WARN: Removed duplicated region for block: B:8:0x0014  */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void run() {
                /*
                    r4 = this;
                    com.shineyue.pm.modle_chat_new.ChatServiceNew r0 = com.shineyue.pm.OaApplication.chatService
                    if (r0 == 0) goto L14
                    com.shineyue.pm.modle_chat_new.ChatServiceNew r0 = com.shineyue.pm.OaApplication.chatService
                    com.neovisionaries.ws.client.WebSocket r0 = com.shineyue.pm.modle_chat_new.ChatServiceNew.ws
                    if (r0 == 0) goto L14
                    com.shineyue.pm.modle_chat_new.ChatServiceNew r0 = com.shineyue.pm.OaApplication.chatService
                    com.neovisionaries.ws.client.WebSocket r0 = com.shineyue.pm.modle_chat_new.ChatServiceNew.ws
                    boolean r0 = r0.isOpen()
                    if (r0 != 0) goto L3c
                L14:
                    com.shineyue.pm.modle_chat.bean.Message r0 = r2
                    r1 = 2
                    r0.sendStatus = r1
                    com.shineyue.pm.web.WebActivity r0 = com.shineyue.pm.web.WebActivity.this
                    com.shineyue.pm.web.WebActivity$127$1 r1 = new com.shineyue.pm.web.WebActivity$127$1
                    r1.<init>()
                    r0.runOnUiThread(r1)
                    java.lang.StringBuilder r0 = new java.lang.StringBuilder
                    r0.<init>()
                    java.lang.String r1 = "网络连接失败："
                    r0.append(r1)
                    com.shineyue.pm.modle_chat.bean.Message r1 = r2
                    java.lang.String r1 = r1.uqIdentNo
                    r0.append(r1)
                    java.lang.String r0 = r0.toString()
                    com.shineyue.pm.utils.OutPutLogUtils.uMengLog(r0)
                L3c:
                    com.ypy.eventbus.EventBus r0 = com.ypy.eventbus.EventBus.getDefault()
                    com.shineyue.pm.scancod.notify.Notify$Message r1 = new com.shineyue.pm.scancod.notify.Notify$Message
                    r2 = 1200(0x4b0, float:1.682E-42)
                    com.shineyue.pm.modle_chat.bean.Message r3 = r2
                    r1.<init>(r2, r3)
                    r0.post(r1)
                    com.shineyue.pm.modle_chat.dao.DaoSession r0 = com.shineyue.pm.modle_chat.dao.helper.GreenDaoHelper.getDaoSession()
                    com.shineyue.pm.modle_chat.dao.MessageDao r0 = r0.getMessageDao()
                    r1 = 1
                    com.shineyue.pm.modle_chat.bean.Message[] r1 = new com.shineyue.pm.modle_chat.bean.Message[r1]
                    r2 = 0
                    com.shineyue.pm.modle_chat.bean.Message r3 = r2
                    r1[r2] = r3
                    r0.insertOrReplaceInTx(r1)
                    com.shineyue.pm.web.WebActivity r0 = com.shineyue.pm.web.WebActivity.this
                    com.shineyue.pm.modle_chat.bean.Message r1 = r2
                    com.shineyue.pm.web.WebActivity.access$23300(r0, r1)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.shineyue.pm.web.WebActivity.AnonymousClass127.run():void");
            }
        });
    }

    private void updataMessageNew(final Message message) {
        ThreadPoolManager.getInstance().execute(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.128
            /* JADX WARN: Removed duplicated region for block: B:8:0x0014  */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void run() {
                /*
                    r4 = this;
                    com.shineyue.pm.modle_chat_new.ChatServiceNew r0 = com.shineyue.pm.OaApplication.chatService
                    if (r0 == 0) goto L14
                    com.shineyue.pm.modle_chat_new.ChatServiceNew r0 = com.shineyue.pm.OaApplication.chatService
                    com.neovisionaries.ws.client.WebSocket r0 = com.shineyue.pm.modle_chat_new.ChatServiceNew.ws
                    if (r0 == 0) goto L14
                    com.shineyue.pm.modle_chat_new.ChatServiceNew r0 = com.shineyue.pm.OaApplication.chatService
                    com.neovisionaries.ws.client.WebSocket r0 = com.shineyue.pm.modle_chat_new.ChatServiceNew.ws
                    boolean r0 = r0.isOpen()
                    if (r0 != 0) goto L3c
                L14:
                    com.shineyue.pm.modle_chat.bean.Message r0 = r2
                    r1 = 2
                    r0.sendStatus = r1
                    com.shineyue.pm.web.WebActivity r0 = com.shineyue.pm.web.WebActivity.this
                    com.shineyue.pm.web.WebActivity$128$1 r1 = new com.shineyue.pm.web.WebActivity$128$1
                    r1.<init>()
                    r0.runOnUiThread(r1)
                    java.lang.StringBuilder r0 = new java.lang.StringBuilder
                    r0.<init>()
                    java.lang.String r1 = "网络连接失败："
                    r0.append(r1)
                    com.shineyue.pm.modle_chat.bean.Message r1 = r2
                    java.lang.String r1 = r1.uqIdentNo
                    r0.append(r1)
                    java.lang.String r0 = r0.toString()
                    com.shineyue.pm.utils.OutPutLogUtils.uMengLog(r0)
                L3c:
                    com.ypy.eventbus.EventBus r0 = com.ypy.eventbus.EventBus.getDefault()
                    com.shineyue.pm.scancod.notify.Notify$Message r1 = new com.shineyue.pm.scancod.notify.Notify$Message
                    r2 = 1200(0x4b0, float:1.682E-42)
                    com.shineyue.pm.modle_chat.bean.Message r3 = r2
                    r1.<init>(r2, r3)
                    r0.post(r1)
                    com.shineyue.pm.modle_chat.dao.DaoSession r0 = com.shineyue.pm.modle_chat.dao.helper.GreenDaoHelper.getDaoSession()
                    com.shineyue.pm.modle_chat.dao.MessageDao r0 = r0.getMessageDao()
                    r1 = 1
                    com.shineyue.pm.modle_chat.bean.Message[] r1 = new com.shineyue.pm.modle_chat.bean.Message[r1]
                    r2 = 0
                    com.shineyue.pm.modle_chat.bean.Message r3 = r2
                    r1[r2] = r3
                    r0.insertOrReplaceInTx(r1)
                    com.shineyue.pm.web.WebActivity r0 = com.shineyue.pm.web.WebActivity.this
                    com.shineyue.pm.modle_chat.bean.Message r1 = r2
                    com.shineyue.pm.web.WebActivity.access$23400(r0, r1)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.shineyue.pm.web.WebActivity.AnonymousClass128.run():void");
            }
        });
    }

    private void sendAitMsg(String toUser, String uqIdentNo) {
        try {
            Map<String, String> map = new HashMap<>();
            List<String> userIds = this.aitManager.getUserArray();
            if (userIds.size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < userIds.size(); i++) {
                    String userid = userIds.get(i);
                    stringBuilder.append(userid + ",");
                }
                String resultUserIds = stringBuilder.toString();
                if (resultUserIds.length() > 0) {
                    resultUserIds = resultUserIds.substring(0, resultUserIds.length() - 1);
                }
                map.put("isAit", "1");
                map.put("content", resultUserIds);
            } else {
                map.put("isAit", "0");
                map.put("content", "");
            }
            map.put("type", "17");
            map.put("toGroup", toUser + "");
            map.put("uqIdentNo", uqIdentNo);
            String object = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
            KLog.e(this.TAG, "@消息内容：" + object.toString());
            String result = Base64Utils.encode(RSAUtils.encryptByPublicKey(object.getBytes(StandardCharsets.UTF_8.name()), RSAUtils.publicKey));
            String sign = URLEncoder.encode(URLEncoder.encode(result, StandardCharsets.UTF_8.name()), StandardCharsets.UTF_8.name());
            this.aitManager.clearAitInfo();
            ChatServiceNew chatServiceNew = OaApplication.chatService;
            if (ChatServiceNew.ws != null) {
                ChatServiceNew chatServiceNew2 = OaApplication.chatService;
                if (ChatServiceNew.ws.isOpen()) {
                    ChatServiceNew chatServiceNew3 = OaApplication.chatService;
                    ChatServiceNew.ws.sendText("{\"systemName\":\"" + ChatServiceNew.getCHANNEL() + "\",\"sign\":\"" + sign + "\"}");
                    String str = this.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("@消息内容：");
                    sb.append(sign);
                    KLog.e(str, sb.toString());
                }
            }
        } catch (Exception e) {
            KLog.e(this.TAG, e.getMessage());
        }
    }

    private void sendAgain(Message message, int num) {
        try {
            long time = System.currentTimeMillis();
            HashMap<String, Object> map = new HashMap<>();
            String ty = message.getType();
            if (ty.equals("3") || ty.equals("6")) {
                map.put("toUserId", message.toUserId);
                map.put("toUserName", message.toUserName);
                map.put("type", message.type);
            } else if (ty.equals("4") || ty.equals("7")) {
                map.put("toGroup", message.toGroup);
                map.put("groupName", message.groupName);
                map.put("toUserId", message.toUserId);
                map.put("type", message.type);
            }
            map.put("content", message.content);
            map.put("uqIdentNo", message.uqIdentNo);
            map.put("time", time + "");
            if (!TextUtils.isEmpty(message.addInfo)) {
                map.put("addInfo", message.addInfo);
            }
            String object = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
            KLog.d(this.TAG, object);
            String result = Base64Utils.encode(RSAUtils.encryptByPublicKey(object.getBytes(StandardCharsets.UTF_8.name()), RSAUtils.publicKey));
            String sign = URLEncoder.encode(result, StandardCharsets.UTF_8.name());
            KLog.d(this.TAG, RSAUtils.getValue(URLEncoder.encode(sign, StandardCharsets.UTF_8.name()), RSAUtils.privateKey));
            try {
                socketSend(map, num);
                message.sendStatus = 9;
                message.time = time;
                message.date = time + "";
                updataMessage(message);
                String str = this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("发送后最后一条内容：");
                sb.append(this.list.get(r15.size() - 1).content);
                KLog.e(str, sb.toString());
            } catch (Exception e) {
                e = e;
                e.printStackTrace();
                KLog.e(this.TAG, e.toString());
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    private void startTimer(String uqIdentNo, int num) {
        android.os.Message message = new android.os.Message();
        Bundle bundle = new Bundle();
        bundle.putString("uqIdentNo", uqIdentNo);
        bundle.putInt("num", num);
        message.what = 291;
        message.setData(bundle);
        this.myHandler.sendMessageDelayed(message, 30000L);
    }

    private void startDelayedTimer(String uqIdentNo, int num) {
        android.os.Message message = new android.os.Message();
        Bundle bundle = new Bundle();
        bundle.putString("uqIdentNo", uqIdentNo);
        bundle.putInt("num", num);
        message.what = 4660;
        message.setData(bundle);
        this.myHandler.sendMessageDelayed(message, 3000L);
    }

    private void chehuiMsg(Message message) {
        try {
            long time = System.currentTimeMillis();
            HashMap<String, String> map = new HashMap<>();
            if (message.type.equals("6") || message.type.equals("3")) {
                map.put("toUserId", message.toUserId);
                map.put("withdrawType", "0");
            } else if (message.type.equals("7") || message.type.equals("4")) {
                map.put("withdrawType", "1");
                map.put(DtnConfigItem.KEY_GROUP, message.toUserId);
            }
            map.put("withdrawUqIdentNo", message.uqIdentNo);
            map.put("type", "12");
            map.put("uqIdentNo", UUID.randomUUID().toString());
            map.put("time", time + "");
            String object = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
            KLog.d(this.TAG, object);
            String result = Base64Utils.encode(RSAUtils.encryptByPublicKey(object.getBytes(StandardCharsets.UTF_8.name()), RSAUtils.publicKey));
            String sign = URLEncoder.encode(URLEncoder.encode(result, StandardCharsets.UTF_8.name()), StandardCharsets.UTF_8.name());
            KLog.d(this.TAG, RSAUtils.getValue(sign, RSAUtils.privateKey));
            String s = UtilTips.sendMsgTips(this.mContext);
            if (!TextUtils.isEmpty(s)) {
                ToastUtil.toast(this.mContext, "msg", s, ToastType.FAIL);
            }
            ChatServiceNew chatServiceNew = OaApplication.chatService;
            ChatServiceNew.ws.sendText("{\"systemName\":\"" + ChatServiceNew.getCHANNEL() + "\",\"sign\":\"" + sign + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chehuiGlyMsg(Message message) {
        try {
            long time = System.currentTimeMillis();
            HashMap<String, String> map = new HashMap<>();
            if (message.type.equals("6") || message.type.equals("3")) {
                map.put("toUserId", message.toUserId);
                map.put("withdrawType", "0");
            } else if (message.type.equals("7") || message.type.equals("4")) {
                map.put("withdrawType", "-1");
                map.put(DtnConfigItem.KEY_GROUP, message.toUserId);
            }
            map.put("withdrawUqIdentNo", message.uqIdentNo);
            map.put("type", "12");
            map.put("uqIdentNo", UUID.randomUUID().toString());
            map.put("time", time + "");
            String object = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
            KLog.d(this.TAG, object);
            String result = Base64Utils.encode(RSAUtils.encryptByPublicKey(object.getBytes(StandardCharsets.UTF_8.name()), RSAUtils.publicKey));
            String sign = URLEncoder.encode(URLEncoder.encode(result, StandardCharsets.UTF_8.name()), StandardCharsets.UTF_8.name());
            KLog.d(this.TAG, RSAUtils.getValue(sign, RSAUtils.privateKey));
            String s = UtilTips.sendMsgTips(this.mContext);
            if (!TextUtils.isEmpty(s)) {
                ToastUtil.toast(this.mContext, "msg", s, ToastType.FAIL);
            }
            ChatServiceNew chatServiceNew = OaApplication.chatService;
            ChatServiceNew.ws.sendText("{\"systemName\":\"" + ChatServiceNew.getCHANNEL() + "\",\"sign\":\"" + sign + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.mabeijianxi.jianxiexpression.ExpressionGridFragment.ExpressionDeleteClickListener
    public void expressionDeleteClick(View v) {
        ExpressionShowFragment.delete(this.editEmojicon);
        ExpressionEditText expressionEditText = this.getmExpressionEditTextFull;
        if (expressionEditText != null) {
            ExpressionShowFragment.delete(expressionEditText);
        }
    }

    @Override // com.mabeijianxi.jianxiexpression.ExpressionGridFragment.ExpressionClickListener
    public void expressionClick(String str) {
        ExpressionShowFragment.input(this.editEmojicon, str);
        ExpressionEditText expressionEditText = this.getmExpressionEditTextFull;
        if (expressionEditText != null) {
            ExpressionShowFragment.input(expressionEditText, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showEmoj() {
        boolean zIsKeyboardShown = isKeyboardShown(this.ll);
        this.isKeyboardShow = zIsKeyboardShown;
        if (this.isEmogiShow) {
            if (!this.chatAdapter.isEditingStatus()) {
                lockContentViewHeight();
                showKeyboard(this);
            }
            this.editEmojicon.requestFocus();
            hideEmogiPanel();
            unlockContentViewHeight();
            return;
        }
        if (zIsKeyboardShown) {
            lockContentViewHeight();
            hideKeyboard(this);
            showEmogiPanel();
            this.editEmojicon.requestFocus();
            replaceEmogi();
            unlockContentViewHeight();
        } else {
            hideAddPanel();
            showEmogiPanel();
            this.editEmojicon.requestFocus();
            replaceEmogi();
        }
        scrollToBottom();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showEmojFull() {
        boolean zIsKeyboardShown = isKeyboardShown(this.ll);
        this.isKeyboardShow = zIsKeyboardShown;
        if (this.isEmogiShowFull) {
            lockContentViewHeight();
            showKeyboardFull(this);
            this.getmExpressionEditTextFull.requestFocus();
            hideEmogiPanelFull();
            unlockContentViewHeight();
            return;
        }
        if (zIsKeyboardShown) {
            lockContentViewHeight();
            hideKeyboardFull(this);
            showEmogiPanelFull();
            this.getmExpressionEditTextFull.requestFocus();
            replaceEmogiFull();
            unlockContentViewHeight();
        } else {
            showEmogiPanelFull();
            this.getmExpressionEditTextFull.requestFocus();
            replaceEmogiFull();
        }
        scrollToBottom();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAdd() {
        boolean zIsKeyboardShown = isKeyboardShown(this.ll);
        this.isKeyboardShow = zIsKeyboardShown;
        if (this.isAddShow) {
            lockContentViewHeight();
            hideAddPanel();
            this.editEmojicon.requestFocus();
            showKeyboard(this);
            unlockContentViewHeight();
        } else if (zIsKeyboardShown) {
            hideKeyboard(this);
            showAddPanel();
            replaceAdd();
        } else {
            hideEmogiPanel();
            showAddPanel();
            replaceAdd();
        }
        scrollToBottom();
    }

    private void showEmogiPanel() {
        this.isEmogiShow = true;
        this.fl_emogi.setVisibility(0);
        this.iv_icon.setImageResource(R.mipmap.chat_key);
    }

    private void showEmogiPanelFull() {
        this.isEmogiShowFull = true;
        this.fl_emogi.setVisibility(0);
        this.mIvEmojFull.setImageResource(R.mipmap.chat_key);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideEmogiPanel() {
        this.isEmogiShow = false;
        this.fl_emogi.setVisibility(8);
        this.iv_icon.setImageResource(R.mipmap.chat_icon);
    }

    private void hideEmogiPanelFull() {
        this.isEmogiShowFull = false;
        this.fl_emogi.setVisibility(8);
        this.mIvEmojFull.setImageResource(R.mipmap.chat_icon);
    }

    private void showAddPanel() {
        this.btn_add.setImageResource(R.mipmap.chat_more_select);
        this.isAddShow = true;
        this.fl_add.setVisibility(0);
        this.editEmojicon.clearFocus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideAddPanel() {
        this.btn_add.setImageResource(R.mipmap.chat_more);
        this.isAddShow = false;
        this.fl_add.setVisibility(8);
    }

    private void replaceEmogi() {
        if (this.expressionShowFragment == null) {
            KLog.e(this.TAG, "replaceEmogi");
            this.expressionShowFragment = ExpressionShowFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_emogi, ExpressionShowFragment.newInstance()).commit();
        }
    }

    private void replaceEmogiFull() {
        if (this.expressionShowFragment == null) {
            KLog.e(this.TAG, "replaceEmogi");
            this.expressionShowFragment = ExpressionShowFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_emogi, ExpressionShowFragment.newInstance()).commit();
        }
    }

    private void replaceAdd() {
        if (this.addShowFragment == null) {
            KLog.e(this.TAG, "replaceAdd");
            this.addShowFragment = AddShowFragment.newInstance(this.chatType, this.toUser);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_add, AddShowFragment.newInstance(this.chatType, this.toUser)).commit();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isKeyboardShown(View rootView) {
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return ((float) heightDiff) > dm.density * 100.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showKeyboard(Activity context) {
        KLog.e(this.TAG, "打开软键盘");
        if (context != null && this.editEmojicon != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService("input_method");
            imm.showSoftInput(this.editEmojicon, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showKeyboardFull(Activity context) {
        KLog.e(this.TAG, "打开软键盘");
        if (context != null && this.getmExpressionEditTextFull != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService("input_method");
            imm.showSoftInput(this.getmExpressionEditTextFull, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showKeyboardHuaTi(Activity context) {
        KLog.e(this.TAG, "打开软键盘");
        if (context != null && this.mEetHuaTi != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService("input_method");
            imm.showSoftInput(this.mEetHuaTi, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideKeyboard(Activity context) {
        KLog.e(this.TAG, "关闭软键盘");
        if (context == null) {
            return;
        }
        View v = context.getWindow().peekDecorView();
        if (v != null && v.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService("input_method");
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        this.editEmojicon.clearFocus();
    }

    private void hideKeyboardFull(Activity context) {
        KLog.e(this.TAG, "关闭软键盘");
        if (context == null) {
            return;
        }
        View v = context.getWindow().peekDecorView();
        if (v != null && v.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService("input_method");
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        this.getmExpressionEditTextFull.clearFocus();
    }

    private void hideKeyboardHuaTi(Activity context) {
        KLog.e(this.TAG, "关闭软键盘");
        if (context == null) {
            return;
        }
        View v = context.getWindow().peekDecorView();
        if (v != null && v.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService("input_method");
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        this.mEetHuaTi.clearFocus();
    }

    private void lockContentViewHeight() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.rl_content.getLayoutParams();
        layoutParams.height = this.rl_content.getHeight();
        layoutParams.weight = 0.0f;
    }

    private void unlockContentViewHeight() {
        android.os.Message message = android.os.Message.obtain();
        message.what = 100;
        this.handler.sendMessageDelayed(message, 200L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSupportSoftInputHeight() {
        Rect r = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        int screenHeight = getWindow().getDecorView().getRootView().getHeight();
        int softInputHeight = screenHeight - r.bottom;
        if (Build.VERSION.SDK_INT >= 20) {
            return softInputHeight - getSoftButtonsBarHeight();
        }
        return softInputHeight;
    }

    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        }
        return 0;
    }

    @Override // com.shineyue.pm.modle_chat.presenter.grouplist.GroupListView
    public void onGroupListResult(QunZu qunZu) {
    }

    @Override // com.shineyue.pm.modle_chat.presenter.grouplist.GroupListView
    public void onGroupItemNumRefresh() {
    }

    class MyThread extends Thread {
        Handler myHandler = new Handler();
        Runnable myHeartRun = new Runnable() { // from class: com.shineyue.pm.web.WebActivity.MyThread.1
            @Override // java.lang.Runnable
            public void run() {
                if (WebActivity.this.startTimeChat == 0) {
                    if (OaApplication.chatService != null) {
                        ChatServiceNew chatServiceNew = OaApplication.chatService;
                        if (ChatServiceNew.ws != null) {
                            ChatServiceNew chatServiceNew2 = OaApplication.chatService;
                            ChatServiceNew.ws.sendPing();
                        }
                    }
                    WebActivity.this.startTimeChat = System.currentTimeMillis();
                } else {
                    WebActivity.this.endTime = System.currentTimeMillis();
                    WebActivity.this.heartTime = WebActivity.this.endTime - WebActivity.this.startTimeChat;
                    if (WebActivity.this.heartTime > 2500) {
                        if (OaApplication.chatService != null) {
                            ChatServiceNew chatServiceNew3 = OaApplication.chatService;
                            if (ChatServiceNew.ws != null) {
                                ChatServiceNew chatServiceNew4 = OaApplication.chatService;
                                ChatServiceNew.ws.sendPing();
                            }
                        }
                        WebActivity.this.startTimeChat = System.currentTimeMillis();
                        WebActivity.this.heartTime = 2500L;
                    }
                }
                MyThread.this.startHeart();
            }
        };

        MyThread() {
        }

        public void startHeart() {
            this.myHandler.postDelayed(this.myHeartRun, 5000L);
        }

        public void stopHeart() {
            this.myHandler.removeCallbacks(this.myHeartRun);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            startHeart();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getQunZuItemInfo(String vid, QunZuItemInfoDaoManager.OnGroupItemResultListener onGroupItemResultListener) {
        try {
            this.qunZuItemInfoDaoManager.getGroupInfoById(vid, onGroupItemResultListener);
        } catch (Exception e) {
        }
    }

    @Override // com.shineyue.pm.modle_chat.presenter.chat_window_list.ChatWindowListView
    public void onChatWindowListResult(List<ChatWindowItem> chatWindows, boolean isFromNet) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void LogInfo() {
        for (Message message : this.list) {
            KLog.e("INFO", message.getContent());
        }
    }

    private void getMaxFileSize(Message message, int num) {
        LogUtils.e("获取最大文件大小");
        try {
            upDataListAndUpFile(message, num);
        } catch (Exception e) {
            Utils.dismissProgressDialog();
            e.printStackTrace();
            KLog.e(e.getMessage());
        }
    }

    /* JADX INFO: renamed from: com.shineyue.pm.web.WebActivity$130, reason: invalid class name */
    class AnonymousClass130 extends MyStringCallback {
        final /* synthetic */ Message val$message;
        final /* synthetic */ int val$num;

        AnonymousClass130(Message message, int i) {
            this.val$message = message;
            this.val$num = i;
        }

        @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
        public void onSuccess(String s, Call call, Response response) {
            Utils.dismissProgressDialog();
            KLog.i(WebActivity.this.TAG, s);
            if (TextUtils.isEmpty(s)) {
                WebActivity.this.upDataListAndUpFile(this.val$message, this.val$num);
                return;
            }
            try {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(s);
                Integer code = jsonObject.getInteger("code");
                String msg = jsonObject.getString("msg");
                if (code.intValue() != 0) {
                    WebActivity.this.upDataListAndUpFile(this.val$message, this.val$num);
                    ToastUtil.toast(WebActivity.this.mContext, "msg", msg, ToastType.FAIL);
                    return;
                }
                com.alibaba.fastjson.JSONObject data = jsonObject.getJSONObject("data");
                int maxSize = data.getInteger("maxFileSize").intValue();
                long mSize = FileUtil.getFileSize(new File(this.val$message.fileUrl));
                KLog.i(WebActivity.this.TAG, "文件大小" + mSize);
                KLog.i(WebActivity.this.TAG, "最大大小" + maxSize);
                if (mSize > maxSize) {
                    ToastUtil.toast(WebActivity.this.mContext, ToastInfo.fileTooLarge, ((maxSize / 1024) / 1024) + "M", ToastType.FAIL);
                    GreenDaoHelper.getDaoSession().getMessageDao().insertOrReplaceInTx(this.val$message);
                    WebActivity.this.list.add(this.val$message);
                    WebActivity.this.LogInfo();
                    WebActivity.this.chatAdapter.notifyDataSetChanged();
                    WebActivity.this.scrollToBottom();
                    this.val$message.sendStatus = 2;
                    WebActivity.this.updataMessage(this.val$message);
                } else {
                    WebActivity.this.upDataListAndUpFile(this.val$message, this.val$num);
                }
            } catch (Exception e) {
                KLog.e(e.getMessage());
            }
        }

        @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
        public void onError(Call call, Response response, Exception e) {
            Utils.dismissProgressDialog();
            super.onError(call, response, e);
            ToastUtil.toast(WebActivity.this.mContext, "error", "", ToastType.FAIL);
            WebActivity.this.upDataListAndUpFile(this.val$message, this.val$num);
            LogUtils.e("接口异常" + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scrollToBottom() {
        final int[] firstDely = {0};
        RecyclerView recyclerView = this.rv_msg;
        if (recyclerView != null) {
            recyclerView.postDelayed(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.131
                @Override // java.lang.Runnable
                public void run() {
                    WebActivity.this.rv_msg.scrollToPosition(WebActivity.this.rv_msg.getAdapter().getItemCount() - 1);
                    WebActivity.this.llLayoutManager.scrollToPositionWithOffset(WebActivity.this.rv_msg.getAdapter().getItemCount() - 1, 0);
                    firstDely[0] = 0;
                    boolean unused = WebActivity.this.isSearchOrDing;
                }
            }, firstDely[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scrollToPosition(final int position) {
        this.rv_msg.postDelayed(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.132
            @Override // java.lang.Runnable
            public void run() {
                WebActivity.this.rv_msg.scrollToPosition(position);
                WebActivity.this.llLayoutManager.scrollToPositionWithOffset(position, 0);
            }
        }, 100L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reTrySendMsg(android.os.Message msg) {
        String uqIdentNo = msg.getData().getString("uqIdentNo");
        msg.getData().getInt("num");
        List<Message> msgs = GreenDaoHelper.getDaoSession().getMessageDao().queryBuilder().where(MessageDao.Properties.UqIdentNo.eq(uqIdentNo), new WhereCondition[0]).build().list();
        if (msgs.size() != 0 && msgs.get(0).sendStatus == 9) {
            OutPutLogUtils.uMengLog("消息发送超时：" + msgs.get(0).uqIdentNo);
            msgs.get(0).sendStatus = 2;
            GreenDaoHelper.getDaoSession().getMessageDao().update(msgs.get(0));
            EventBus.getDefault().post(new Notify.Message(1201, msgs.get(0)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void testIsServiceOpen(android.os.Message msg) {
        String uqIdentNo = msg.getData().getString("uqIdentNo");
        msg.getData().getInt("num");
        List<Message> msgs = GreenDaoHelper.getDaoSession().getMessageDao().queryBuilder().where(MessageDao.Properties.UqIdentNo.eq(uqIdentNo), new WhereCondition[0]).build().list();
        if (msgs.size() != 0 && msgs.get(0).sendStatus == 9) {
            msgs.get(0).sendStatus = 2;
            OutPutLogUtils.uMengLog("socket连接失败：" + msgs.get(0).uqIdentNo);
            GreenDaoHelper.getDaoSession().getMessageDao().update(msgs.get(0));
            EventBus.getDefault().post(new Notify.Message(1201, msgs.get(0)));
        }
    }

    public void changeLastReadTime() {
        for (int i = 0; i < this.list.size(); i++) {
            try {
                Long date = Long.valueOf(Long.parseLong(this.list.get(i).date));
                if (this.list.get(i).respType == 2 && date.longValue() > this.toLastReadTime) {
                    this.toLastReadTime = date.longValue();
                }
            } catch (Exception e) {
                KLog.e(this.TAG, e.getMessage());
            }
        }
    }

    public void addNewMessageToList(List<Message> messages) {
        for (int o = 0; o < messages.size(); o++) {
            Message message = messages.get(o);
            int position = -1;
            for (int i = 0; i < this.list.size(); i++) {
                if (this.list.get(i).uqIdentNo.equals(message.uqIdentNo)) {
                    position = i;
                }
            }
            if (position == -1) {
                try {
                    this.list.add(message);
                } catch (Exception e) {
                    KLog.e(this.TAG, e.getMessage());
                }
            } else {
                this.list.set(position, message);
            }
        }
        Collections.sort(this.list, new Comparator<Message>() { // from class: com.shineyue.pm.web.WebActivity.133
            @Override // java.util.Comparator
            public int compare(Message o1, Message o2) {
                return o1.date.compareTo(o2.date);
            }
        });
        EventBus.getDefault().post(new Notify.Message(Notify.ChatMsg4));
    }

    public void addNewMessageToList(Message message, List<Message> mList) {
        int position = -1;
        for (int i = 0; i < this.list.size(); i++) {
            if (this.list.get(i).uqIdentNo.equals(message.uqIdentNo)) {
                position = i;
            }
        }
        try {
            if (position == -1) {
                mList.add(message);
            } else {
                this.list.set(position, message);
            }
        } catch (Exception e) {
            KLog.e(this.TAG, e.getMessage());
        }
        Collections.sort(this.list, new Comparator<Message>() { // from class: com.shineyue.pm.web.WebActivity.134
            @Override // java.util.Comparator
            public int compare(Message o1, Message o2) {
                return o1.date.compareTo(o2.date);
            }
        });
    }

    public void scrollToDingMsg(boolean isFirst) {
        for (int i = 0; i < this.list.size(); i++) {
            try {
                if ((this.list.get(i).getUqIdentNo() == null || !this.list.get(i).getUqIdentNo().equals(this.dingId)) && (this.list.get(i).getDingId() == null || !this.list.get(i).getDingId().equals(this.dingId))) {
                }
            } catch (Exception e) {
                KLog.i(this.TAG, e.toString());
                return;
            }
        }
        KLog.i(this.TAG, "当前定位位置:-1");
        if (-1 != -1) {
            scrollToPosition(-1);
        } else if (!isFirst && "-1".equals(this.dingId)) {
            scrollToPosition(0);
        } else if (isFirst) {
            loadHistoryBottom(false);
        }
        if (isFirst) {
            this.rv_msg.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.shineyue.pm.web.WebActivity.135
                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (WebActivity.this.dingTime > 0) {
                        int postion = WebActivity.this.llLayoutManager.findLastVisibleItemPosition();
                        if (!WebActivity.this.ISLOADING && postion >= WebActivity.this.list.size() - 3 && dy > 0) {
                            WebActivity.this.loadHistoryBottom(true);
                        }
                        super.onScrolled(recyclerView, dx, dy);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadHistoryBottom(final boolean isLoad) {
        if (this.chatType.equals("3") || this.chatType.equals("6")) {
            this.chatQxModule.queryChatTime(this.mId, this.toUser, new OnParmsCallBack<Long>() { // from class: com.shineyue.pm.web.WebActivity.136
                @Override // com.shineyue.pm.modle_chat.chat_history.dao.OnParmsCallBack
                public void onParmsResult(Long result) {
                    if (result.longValue() == 0) {
                        WebActivity.this.loadHistoryBottomNew(isLoad, -1L);
                    } else if (result.longValue() != -1) {
                        WebActivity.this.loadHistoryBottomNew(isLoad, result.longValue());
                    }
                }
            });
        } else {
            loadHistoryBottomNew(isLoad, -1L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void loadHistoryBottomNew(boolean isLoad, long limitTime) {
        this.ISLOADING = true;
        try {
            runOnUiThread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.137
                @Override // java.lang.Runnable
                public void run() {
                    Utils.showProgressDialog(WebActivity.this.mContext, "加载中", false);
                }
            });
            HashMap<String, Object> map = new HashMap<>();
            map.put("fromUserId", this.mId);
            if (this.chatType.equals("3") || this.chatType.equals("6")) {
                map.put("toId", this.toUser);
                map.put("queryType", "1");
                if (limitTime > 0) {
                    map.put("limitTime", Long.valueOf(limitTime));
                }
            } else if (this.chatType.equals("4") || this.chatType.equals("7")) {
                map.put("toId", this.toUser);
                map.put("queryType", "2");
            }
            if (!isLoad) {
                map.put("queryTime", Long.valueOf(this.dingTime));
            } else {
                try {
                    List<Message> list = this.list;
                    map.put("queryTime", list.get(list.size() - 1).date);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            map.put("slideType", 1);
            map.put("num", 20);
            String object = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
            KLog.d(this.TAG, object);
            String result = Base64Utils.encode(RSAUtils.encryptByPublicKey(object.getBytes(StandardCharsets.UTF_8.name()), RSAUtils.publicKey));
            String sign = URLEncoder.encode(URLEncoder.encode(result, StandardCharsets.UTF_8.name()), StandardCharsets.UTF_8.name());
            KLog.d(this.TAG, sign);
            ((PostRequest) OkGo.post(OkGoNetManager.CHAT_URL_FILE + OkGoNetManager.CUSTOM_QUERY_RECORDE + "?systemName=" + ChatServiceNew.getCHANNEL() + "&sign=" + sign).tag(this)).execute(new AnonymousClass138(isLoad));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: renamed from: com.shineyue.pm.web.WebActivity$138, reason: invalid class name */
    class AnonymousClass138 extends MyStringCallback {
        final /* synthetic */ boolean val$isLoad;

        AnonymousClass138(boolean z) {
            this.val$isLoad = z;
        }

        @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
        public void onSuccess(final String s, Call call, Response response) {
            Utils.dismissProgressDialog();
            WebActivity.this.ISLOADING = false;
            final String username = Utils.getMsg(WebActivity.this.mContext, "username");
            KLog.e(WebActivity.this.TAG, s);
            ThreadPoolManager.getInstance().execute(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.138.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        if (!AnonymousClass138.this.val$isLoad) {
                            WebActivity.this.list.clear();
                        }
                        JSONObject result = new JSONObject(s);
                        if (!WebActivity.this.chatType.equals("3") && !WebActivity.this.chatType.equals("6")) {
                            if (WebActivity.this.chatType.equals("4") || WebActivity.this.chatType.equals("7")) {
                                JSONObject groupMap = result.getJSONObject("groupMap");
                                JSONArray content = groupMap.getJSONArray(WebActivity.this.toUser);
                                for (int i = 0; i < content.length(); i++) {
                                    JSONObject js = content.getJSONObject(i);
                                    if (js.toString().contains("fromUserId")) {
                                        JSONObject message1 = js.getJSONObject("content");
                                        Message message = (Message) new Gson().fromJson(message1.toString(), Message.class);
                                        if (!message.fromUserId.equals(Utils.getMsg(WebActivity.this.mContext, "userid"))) {
                                            message.toUserId = message.toGroup;
                                            message.toUserName = WebActivity.this.toUserName;
                                            message.respType = 2;
                                        } else {
                                            message.toUserId = message.toGroup;
                                            message.toUserName = WebActivity.this.toUserName;
                                            message.respType = 1;
                                        }
                                        message.myId = WebActivity.this.mId;
                                        message.sendStatus = 1;
                                        if (!WebActivity.this.list.contains(message)) {
                                            WebActivity.this.list.add(message);
                                        }
                                    }
                                }
                            }
                            WebActivity.this.runOnUiThread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.138.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    WebActivity.this.chatAdapter.notifyDataSetChanged();
                                    if (!AnonymousClass138.this.val$isLoad) {
                                        WebActivity.this.scrollToPosition(0);
                                    }
                                }
                            });
                        }
                        JSONObject p2pMap = result.getJSONObject("p2pMap");
                        JSONArray content2 = p2pMap.getJSONArray(WebActivity.this.toUser);
                        KLog.e(WebActivity.this.TAG, "消息列表大小：" + content2.length());
                        for (int i2 = 0; i2 < content2.length(); i2++) {
                            JSONObject js2 = content2.getJSONObject(i2);
                            if (js2.toString().contains("fromUserId")) {
                                JSONObject message12 = js2.getJSONObject("content");
                                Message message2 = (Message) new Gson().fromJson(message12.toString(), Message.class);
                                if (!message2.toUserId.equals(Utils.getMsg(OaApplication.application, "userid")) || message2.toUserId.equals(message2.fromUserId)) {
                                    message2.toUserName = WebActivity.this.toUserName;
                                    message2.respType = 1;
                                } else {
                                    String change = message2.fromUserId;
                                    message2.fromUserId = message2.toUserId;
                                    message2.toUserId = change;
                                    String change2 = message2.fromUserName;
                                    String change22 = username;
                                    message2.fromUserName = change22;
                                    message2.toUserName = change2;
                                    message2.respType = 2;
                                }
                                message2.myId = WebActivity.this.mId;
                                message2.sendStatus = 1;
                                if (!WebActivity.this.list.contains(message2)) {
                                    WebActivity.this.list.add(message2);
                                }
                            }
                        }
                        WebActivity.this.runOnUiThread(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.138.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                WebActivity.this.chatAdapter.notifyDataSetChanged();
                                if (!AnonymousClass138.this.val$isLoad) {
                                    WebActivity.this.scrollToPosition(0);
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
        public void onError(Call call, Response response, Exception e) {
            super.onError(call, response, e);
            Utils.dismissProgressDialog();
            WebActivity.this.ISLOADING = false;
            e.printStackTrace();
        }
    }

    public boolean isNowChatActivity(MessageDing messageDing) {
        try {
            if (messageDing.getdWType().equals("1")) {
                if (Utils.getMsg(OaApplication.application, "userid").equals(messageDing.getToId())) {
                    return messageDing.getdFromId().equals(this.toUser);
                }
                return messageDing.getToId().equals(this.toUser);
            }
            return messageDing.getToId().equals(this.toUser);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
            return false;
        }
    }

    public void scrollDingMessage(MessageDing messageDing) {
        try {
            this.dingTime = messageDing.getMsgTime().longValue();
            this.dingId = messageDing.getUqIdentNo();
            loadDate();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
    }

    public void getReadState() {
        int first = this.llLayoutManager.findFirstVisibleItemPosition();
        int last = this.llLayoutManager.findLastVisibleItemPosition();
        for (int i = 0; i < this.list.size() && first <= last; i++) {
            Message message = this.list.get(i);
            if (message.beDingId != null && message.dingStatus != 2 && message.beDingId.contains(Utils.getMsg(this.mContext, "userid"))) {
                MessageDing messageDing = new MessageDing();
                messageDing.setdId(message.getDingId());
                messageDing.setdState("2");
                DingPopupWindow.sendMsg(messageDing);
            }
        }
    }

    public void resetMsg() {
        this.dingId = "";
        this.dingTime = 0L;
    }

    private void setQuote() {
        String lastChatUserName;
        String str;
        int dotIndex;
        Message message = this.quoteMessage;
        if (message != null) {
            this.isQuote = true;
            KLog.i(this.TAG, message.content);
            this.rl_quote.setVisibility(0);
            String type = this.quoteMessage.type;
            this.quoteMap.put("quoteMsgId", this.quoteMessage.uqIdentNo);
            String lastChatUserName2 = "";
            String quoteFromUserId = "";
            if (type.equals("3") || type.equals("6")) {
                if (this.quoteMessage.respType == 1) {
                    this.quoteMap.put("quoteFromUserName", this.quoteMessage.fromUserName);
                    this.quoteMap.put("quoteFromUserId", this.quoteMessage.fromUserId);
                    quoteFromUserId = this.quoteMessage.fromUserId;
                    lastChatUserName2 = this.quoteMessage.fromUserName;
                } else {
                    this.quoteMap.put("quoteFromUserName", this.quoteMessage.toUserName);
                    this.quoteMap.put("quoteFromUserId", this.quoteMessage.toUserId);
                    quoteFromUserId = this.quoteMessage.toUserId;
                    lastChatUserName2 = this.quoteMessage.toUserName;
                }
            }
            if (type.equals("4") || type.equals("7")) {
                this.quoteMap.put("quoteFromUserName", this.quoteMessage.fromUserName);
                this.quoteMap.put("quoteFromUserId", this.quoteMessage.fromUserId);
                quoteFromUserId = this.quoteMessage.fromUserId;
                lastChatUserName2 = this.quoteMessage.fromUserName;
            }
            if (quoteFromUserId != null && quoteFromUserId.equals(Utils.getMsg(this.mContext, "userid"))) {
                lastChatUserName = "我";
            } else {
                lastChatUserName = lastChatUserName2;
            }
            if (TextUtils.isEmpty(lastChatUserName)) {
                str = "";
            } else {
                str = lastChatUserName + ":";
            }
            String precontent = str;
            KLog.i(this.TAG, type);
            if (type.equals("3") || type.equals("4")) {
                if (!TextUtils.isEmpty(this.quoteMessage.addInfo)) {
                    try {
                        JSONObject jsonObject = new JSONObject(this.quoteMessage.addInfo);
                        String addType = jsonObject.optString("type");
                        if ("7".equals(addType)) {
                            this.quoteMap.put("quoteMsgType", "0");
                            this.quoteMap.put("quoteContent", this.quoteMessage.addInfo);
                            this.tv_quote.setText(precontent + " [聊天记录]" + Utils.textCheck(this.quoteMessage.content));
                        } else {
                            this.quoteMap.put("quoteMsgType", "0");
                            this.quoteMap.put("quoteContent", this.quoteMessage.content);
                            this.tv_quote.setText(precontent + " " + Utils.textCheck(this.quoteMessage.content));
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    this.quoteMap.put("quoteMsgType", "0");
                    this.quoteMap.put("quoteContent", this.quoteMessage.content);
                    this.tv_quote.setText(precontent + " " + Utils.textCheck(this.quoteMessage.content));
                }
            }
            if (type.equals("6") || type.equals("7")) {
                HashMap map = new HashMap();
                if (this.quoteMessage.speechTime != 0.0d) {
                    int speechtime = (int) Math.floor(this.quoteMessage.getSpeechTime());
                    map.put(DownloadInfo.FILE_NAME, " [语音] " + speechtime + "\"");
                } else {
                    map.put(DownloadInfo.FILE_NAME, this.quoteMessage.fileName);
                }
                map.put("fileUrl", this.quoteMessage.fileUrl);
                String object = com.alibaba.fastjson.JSONObject.toJSON(map).toString();
                String fileName = this.quoteMessage.fileName;
                String fileType = "";
                if (fileName != null && (dotIndex = fileName.lastIndexOf(StrUtil.DOT)) > 0) {
                    fileType = fileName.substring(dotIndex, fileName.length()).toLowerCase();
                }
                if (FileUtil.getDealFileTyppe(fileType) == 1) {
                    this.tv_quote.setText(precontent + " [图片]");
                    this.quoteMap.put("quoteMsgType", "1");
                } else if (this.quoteMessage.speechTime != 0.0d) {
                    int speechtime2 = (int) Math.floor(this.quoteMessage.getSpeechTime());
                    this.tv_quote.setText(precontent + " [语音] " + speechtime2 + "\"");
                    this.quoteMap.put("quoteMsgType", "3");
                } else {
                    this.tv_quote.setText(precontent + " [文件] " + fileName);
                    this.quoteMap.put("quoteMsgType", "2");
                }
                this.quoteMap.put("quoteContent", object);
            }
            this.quoteMap.put("quoteMsgState", "1");
            return;
        }
        ToastUtil.toast(this.mContext, "error", "引用消息失败", ToastType.FAIL);
    }

    private void delQuote() {
        this.isQuote = false;
        this.rl_quote.setVisibility(8);
        this.quoteMessage = null;
        this.quoteMap.clear();
    }

    private void quoteOnClick(Message message) {
        int dotIndex;
        if (message.quoteMsgType.equals("0")) {
            quoteDialogShow(message, message.getQuoteContent());
            return;
        }
        if (message.quoteMsgType.equals("1")) {
            String fileUrl = "";
            try {
                String s = message.quoteContent;
                JSONObject js = new JSONObject(s);
                if (js.has(DownloadInfo.FILE_NAME)) {
                    js.get(DownloadInfo.FILE_NAME).toString();
                }
                if (js.has("fileUrl")) {
                    fileUrl = js.get("fileUrl").toString();
                }
            } catch (Exception e) {
                KLog.i(this.TAG, e.toString());
            }
            Intent intent = new Intent(this.mContext, (Class<?>) PreViewMediaActivity.class);
            intent.putExtra("IMAGEURL", fileUrl);
            Rect rect = new Rect();
            intent.putExtra("rect", rect);
            intent.putExtra("scaleType", ImageView.ScaleType.FIT_XY);
            this.mContext.startActivity(intent);
            return;
        }
        if (message.quoteMsgType.equals("3")) {
            String fileUrl2 = "";
            try {
                String s2 = message.quoteContent;
                JSONObject js2 = new JSONObject(s2);
                if (js2.has(DownloadInfo.FILE_NAME)) {
                    js2.get(DownloadInfo.FILE_NAME).toString();
                }
                if (js2.has("fileUrl")) {
                    fileUrl2 = js2.get("fileUrl").toString();
                }
            } catch (Exception e2) {
                KLog.i(this.TAG, e2.toString());
            }
            if (MediaManager.isPlay()) {
                MediaManager.release();
            }
            MediaManager.playSound(fileUrl2, new MediaPlayer.OnCompletionListener() { // from class: com.shineyue.pm.web.WebActivity.139
                @Override // android.media.MediaPlayer.OnCompletionListener
                public void onCompletion(MediaPlayer mp) {
                }
            });
            return;
        }
        String fileUrl3 = "";
        String fileName = "";
        try {
            String s3 = message.quoteContent;
            JSONObject js3 = new JSONObject(s3);
            if (js3.has(DownloadInfo.FILE_NAME)) {
                fileName = js3.get(DownloadInfo.FILE_NAME).toString();
            }
            if (js3.has("fileUrl")) {
                fileUrl3 = js3.get("fileUrl").toString();
            }
        } catch (Exception e3) {
            KLog.i(this.TAG, e3.toString());
        }
        String fileType = "";
        if (fileName != null && (dotIndex = fileName.lastIndexOf(StrUtil.DOT)) > 0) {
            fileType = fileName.substring(dotIndex, fileName.length()).toLowerCase();
        }
        Intent intent2 = new Intent(this.mContext, (Class<?>) PreViewActivity.class);
        intent2.putExtra("FILEURL", fileUrl3);
        intent2.putExtra("FILENAME", fileName);
        intent2.putExtra("DATA", message.date);
        intent2.putExtra(MessageDao.TABLENAME, message);
        if (FileUtil.getDealFileTyppe(fileType) == 4 || FileUtil.getDealFileTyppe(fileType) == 3) {
            intent2.putExtra("CANPREVIEW", true);
        } else {
            intent2.putExtra("CANPREVIEW", false);
        }
        intent2.putExtra("FILETYPE", FileUtil.getMIMEType(fileName));
        intent2.putExtra("USERID", message.toUserId);
        this.mContext.startActivity(intent2);
    }

    private void quoteDialogShow(final Message message, final String msg) {
        try {
            JSONObject jsonObject = new JSONObject(msg);
            String type1 = jsonObject.optString("type");
            if ("7".equals(type1)) {
                Intent forwardIntent = new Intent(this.mContext, (Class<?>) ForwardHistoryActivity.class);
                forwardIntent.putExtra("id", message.uqIdentNo);
                this.mContext.startActivity(forwardIntent);
                this.handler.postDelayed(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.140
                    @Override // java.lang.Runnable
                    public void run() {
                        Notify.Message message1 = new Notify.Message(Notify.FORWARD_NUM);
                        message1.forwardAddinfo = msg;
                        message1.note = "1";
                        message1.msg = message.uqIdentNo;
                        EventBus.getDefault().post(message1);
                    }
                }, 300L);
                return;
            }
            quoteDialogShowNew(msg);
        } catch (JSONException e) {
            quoteDialogShowNew(msg);
            throw new RuntimeException(e);
        }
    }

    private void quoteDialogShowNew(String msg) {
        if (this.quoteDialog == null) {
            QuoteDialog quoteDialog = new QuoteDialog(this.mContext);
            this.quoteDialog = quoteDialog;
            Window window = quoteDialog.getWindow();
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = -1;
            window.setAttributes(layoutParams);
            window.getDecorView().setBackgroundColor(-1);
        }
        this.quoteDialog.show();
        this.quoteDialog.setMsg(Utils.textShow(msg));
    }

    private void fileUrlUpdata(String fileUrl, String fileName, String userid, String name, com.alibaba.fastjson.JSONArray jsonArray) {
        String documentVersion = Utils.getMsg(this.mContext, "documentVersion");
        if ("2".equals(documentVersion)) {
            fileUrlUpdata(fileUrl, fileName, userid, name);
        } else {
            fileUrlUpdata(fileUrl, fileName, userid, name);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void fileUrlUpdata(String fileUrl, String name, String userid, String username) {
        if (Utils.judgeClient().intValue() == 2) {
            try {
                String documentVersion = Utils.getMsg(this.mContext, "documentVersion");
                HashMap<String, Object> map = new HashMap<>();
                map.put("userid", userid);
                map.put(TbsVideoCacheTask.KEY_VIDEO_CACHE_PARAM_FILENAME, name);
                map.put("filetype", GroupListPresenter.CREATE_USER);
                map.put("parentid", "4");
                map.put("wjlx", 1);
                map.put("filepath", fileUrl);
                map.put("sourcename", username);
                map.put("sourceid", Utils.getMsg(this.mContext, "userid"));
                map.put("jgbh", Utils.getMsg(this.mContext, "zxjgbm"));
                if ("2".equals(documentVersion) && ((this.chatType.equals("4") || this.chatType.equals("7")) && Utils.haveValue(this.xmmc))) {
                    map.put("ssxmmc", this.xmmc);
                    map.put("ssxm", this.xmbh);
                }
                JSONObject buttomjo = new JSONObject(map);
                KLog.i(this.TAG, buttomjo);
                String url = OkGoNetManager.APP_URL + OkGoNetManager.FILE_CREATE;
                ((PostRequest) OkGo.post(url).tag(this)).upJson(buttomjo.toString()).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.141
                    @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
                    public void onSuccess(String s, Call call, Response response) {
                        KLog.i(WebActivity.this.TAG, s);
                        try {
                            JSONObject js = new JSONObject(s);
                            js.getBoolean(ToastType.SUCCESS);
                        } catch (Exception e) {
                            KLog.i(WebActivity.this.TAG, e.toString());
                        }
                    }

                    @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        KLog.e(WebActivity.this.TAG, e.getMessage());
                    }
                });
            } catch (Exception e) {
                KLog.i(this.TAG, e.toString());
            }
        }
    }

    private void fileUrlUpdataNew(final String fileUrl, final String fileName, final String userid, final String name, final com.alibaba.fastjson.JSONArray jsonArray) {
        ThreadPoolManager.getInstance().execute(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.142
            @Override // java.lang.Runnable
            public void run() {
                if (WebActivity.this.documentNewInterface == null) {
                    WebActivity webActivity = WebActivity.this;
                    webActivity.documentNewInterface = new DocumentNewInterface(webActivity.mContext);
                }
                WebActivity.this.documentNewInterface.uploadDocument(Utils.getMsg(WebActivity.this.mContext, "zxjgbm"), GroupListPresenter.CREATE_ORGANIZE, fileName, userid, name, "2", fileUrl, ((int) DownLoadUtil.get().getFileSize(fileUrl)) + "", "聊天文档", userid, name, jsonArray, "0", new DocumentNewInterface.FileCallBack() { // from class: com.shineyue.pm.web.WebActivity.142.1
                    @Override // com.shineyue.pm.modlepagefile.DocumentNewInterface.FileCallBack
                    public void onSuccess(String result) {
                    }

                    @Override // com.shineyue.pm.modlepagefile.DocumentNewInterface.FileCallBack
                    public void onError(String errmsg) {
                    }
                });
            }
        });
    }

    private com.alibaba.fastjson.JSONArray setFileUrlUpdataJson(String id, String name, String qxlx) {
        com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
        try {
            com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
            jsonObject.put("id", (Object) id);
            jsonObject.put(c.e, (Object) name);
            jsonObject.put("qx", (Object) "1");
            jsonObject.put("qxlx", (Object) qxlx);
            jsonArray.add(jsonObject);
        } catch (Exception e) {
            e.toString();
        }
        return jsonArray;
    }

    @Override // com.shineyue.pm.BasicActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        String aitgroupid = this.mCache.getAsString("AITGROUPID");
        if (!TextUtils.isEmpty(aitgroupid) && aitgroupid.contains(this.toUser)) {
            this.chatAitPresenter.deleteAitMessage(this.toUser);
            List<String> list = Arrays.asList(aitgroupid.split(","));
            String content = "";
            for (String str : list) {
                if (!str.equals(this.toUser)) {
                    content = content + str + ",";
                }
            }
            this.mCache.put("AITGROUPID", content);
            EventBus.getDefault().post(new Notify.Message(9000));
        }
    }

    private void checkPermissions_audio() {
        PermissionsUtils.getInstance().chekPermissions(this, this.permissions_audio, this.permissionsResult_audio_chat);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkYinPinPermissions() {
        PermissionsUtils.getInstance().chekPermissions(this, this.permissions_audio, this.permissionsResult_yinpin);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkPermissions_phone() {
        PermissionsUtils.getInstance().chekPermissions(this, this.permissions_phone, this.permissionsResult_phone);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkVideoPermissionChat() {
        PermissionsUtils.getInstance().chekPermissions(this, this.permissions_video, this.permissionsResult_video_chat);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkAudcioPermissionChat() {
        PermissionsUtils.getInstance().chekPermissions(this, this.permissions_audio, this.permissionsResult_audio2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkWriteExternalStoragePermission() {
        PermissionsUtils.getInstance().chekPermissions(this, this.permissions_write_external_storage, this.permissionsResult_write_external_storage);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getKkfileType(final String FILEURL) {
        String fileUrl;
        String url = OkGoNetManager.APP_URL + OkGoNetManager.KKFILE;
        String fileType = "";
        if (FILEURL != null) {
            if (FILEURL.contains("?")) {
                int num = FILEURL.indexOf("?");
                fileUrl = FILEURL.substring(0, num);
            } else {
                fileUrl = FILEURL;
            }
            int dotIndex = fileUrl.lastIndexOf(StrUtil.DOT);
            if (dotIndex > 0) {
                fileType = fileUrl.substring(dotIndex + 1, FILEURL.length()).toLowerCase();
            }
            Utils.getMsg(this.mContext, FILEURL);
        }
        String url2 = url + "?fileType=" + fileType;
        KLog.i(this.TAG, url2);
        OkGo.get(url2).tag(this).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.149
            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onSuccess(String s, Call call, Response response) {
                KLog.i(WebActivity.this.TAG, s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String task = jsonObject.optString("task");
                    if (com.alipay.mobile.common.logging.util.perf.Constants.VAL_YES.equals(task)) {
                        WebActivity.this.addKkFileTask(FILEURL);
                    }
                } catch (Exception e) {
                    KLog.i(WebActivity.this.TAG, e.toString());
                }
            }

            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                KLog.i(WebActivity.this.TAG, e.toString());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addKkFileTask(String fileUrl) {
        try {
            String url = (OkGoNetManager.APP_URL + OkGoNetManager.ADDKKFILETASK) + "?url=" + URLEncoder.encode(fileUrl, "UTF-8").replaceAll("\\+", "%20");
            KLog.i(this.TAG, url);
            OkGo.get(url).tag(this).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.150
                @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
                public void onSuccess(String s, Call call, Response response) {
                    KLog.i(WebActivity.this.TAG, s);
                }

                @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    KLog.i(WebActivity.this.TAG, e.toString());
                }
            });
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(PushConstants.INTENT_ACTIVITY_NAME);
        if (Build.VERSION.SDK_INT > 20) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == 100) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
            return isInBackground;
        }
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        if (!componentInfo.getPackageName().equals(context.getPackageName())) {
            return true;
        }
        return false;
    }

    private void showWenJianPopWindow() {
        KLog.i(this.TAG, "wenjianPopWindow");
        if (this.popIsShow) {
            return;
        }
        hideAddPanel();
        if (this.wenjianPopWindow == null) {
            View contentView = LayoutInflater.from(this.mContext).inflate(R.layout.item_chat_send_wenjian, (ViewGroup) null);
            RelativeLayout rl_back = (RelativeLayout) contentView.findViewById(R.id.rl_back);
            TextView tv_bendiwenjian = (TextView) contentView.findViewById(R.id.tv_bendiwenjian);
            TextView tv_wendang = (TextView) contentView.findViewById(R.id.tv_wendang);
            TextView tv_quxiao = (TextView) contentView.findViewById(R.id.tv_quxiao);
            rl_back.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.151
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    WebActivity.this.wenjianPopWindow.dismiss();
                }
            });
            tv_quxiao.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.152
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    WebActivity.this.wenjianPopWindow.dismiss();
                }
            });
            tv_bendiwenjian.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.153
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    WebActivity.this.checkWriteExternalStoragePermission();
                    WebActivity.this.wenjianPopWindow.dismiss();
                }
            });
            tv_wendang.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.154
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    String documentVersion = Utils.getMsg(WebActivity.this.mContext, "documentVersion");
                    if ("2".equals(documentVersion)) {
                        WebActivity.this.startActivityForResult(new Intent(WebActivity.this.mContext, (Class<?>) FileChatCheckNewActivity.class), 273);
                    } else {
                        Intent intent = new Intent(WebActivity.this.mContext, (Class<?>) FileChatCheckActivity.class);
                        intent.putExtra("bz", 1);
                        WebActivity.this.startActivityForResult(intent, 273);
                    }
                    WebActivity.this.wenjianPopWindow.dismiss();
                }
            });
            PopupWindow popupWindow = new PopupWindow(contentView, -1, -1);
            this.wenjianPopWindow = popupWindow;
            popupWindow.setBackgroundDrawable(new ColorDrawable(0));
            this.wenjianPopWindow.setOutsideTouchable(false);
            this.wenjianPopWindow.setTouchable(true);
            contentView.measure(0, 0);
            this.wenjianPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.shineyue.pm.web.WebActivity.155
                @Override // android.widget.PopupWindow.OnDismissListener
                public void onDismiss() {
                    WebActivity.this.popIsShow = false;
                }
            });
        }
        this.wenjianPopWindow.showAtLocation(this.ll, 0, 0, 0);
        this.popIsShow = true;
    }

    private void showYinShiPinPopWindow() {
        KLog.i(this.TAG, "wenjianPopWindow");
        if (this.popIsShow) {
            return;
        }
        hideAddPanel();
        if (this.yinShiPinPopupWindow == null) {
            View contentView = LayoutInflater.from(this.mContext).inflate(R.layout.item_chat_yinshipin, (ViewGroup) null);
            this.rl_back = (RelativeLayout) contentView.findViewById(R.id.rl_back);
            this.rl_call = (RelativeLayout) contentView.findViewById(R.id.rl_call);
            this.rl_yuyin = (RelativeLayout) contentView.findViewById(R.id.rl_yuyin);
            this.rl_shipin = (RelativeLayout) contentView.findViewById(R.id.rl_shipin);
            this.tv_quxiao = (TextView) contentView.findViewById(R.id.tv_quxiao);
            PopupWindow popupWindow = new PopupWindow(contentView, -1, -1);
            this.yinShiPinPopupWindow = popupWindow;
            popupWindow.setBackgroundDrawable(new ColorDrawable(0));
            this.yinShiPinPopupWindow.setOutsideTouchable(false);
            this.yinShiPinPopupWindow.setTouchable(true);
            contentView.measure(0, 0);
            this.yinShiPinPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.shineyue.pm.web.WebActivity.156
                @Override // android.widget.PopupWindow.OnDismissListener
                public void onDismiss() {
                    Utils.setBackgroundAlpha(WebActivity.this, 1.0f);
                    WebActivity.this.popIsShow = false;
                }
            });
        }
        this.rl_back.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.157
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                WebActivity.this.yinShiPinPopupWindow.dismiss();
            }
        });
        this.tv_quxiao.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.158
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                WebActivity.this.yinShiPinPopupWindow.dismiss();
            }
        });
        if (this.chatType.equals("3") || this.chatType.equals("6")) {
            this.rl_yuyin.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.159
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (!Utils.isNetworkAvailable(WebActivity.this.mContext)) {
                        ToastUtil.toast(WebActivity.this.mContext, ToastInfo.netError, ToastType.FAIL);
                        return;
                    }
                    if (WebActivity.this.sfjy) {
                        ToastUtil.toast(WebActivity.this.mContext, "msg", "发送失败，您已被禁言", ToastType.FAIL);
                        return;
                    }
                    if (Utils.getBooleanMsg(OaApplication.application, "yspFlag")) {
                        WebActivity.this.checkYinPinPermissions();
                    } else {
                        ToastUtil.toast(WebActivity.this.mContext, ToastInfo.notEnabled, "视频会议", ToastType.FAIL);
                    }
                    WebActivity.this.yinShiPinPopupWindow.dismiss();
                }
            });
            this.rl_call.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.160
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    WebActivity.this.checkPermissions_phone();
                    WebActivity.this.yinShiPinPopupWindow.dismiss();
                }
            });
        } else {
            this.rl_call.setVisibility(8);
            this.rl_yuyin.setVisibility(8);
        }
        if (this.phoneBz) {
            this.rl_call.setVisibility(0);
        } else {
            this.rl_call.setVisibility(8);
        }
        this.rl_shipin.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.161
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (!Utils.isNetworkAvailable(WebActivity.this.mContext)) {
                    ToastUtil.toast(WebActivity.this.mContext, ToastInfo.netError, ToastType.FAIL);
                    return;
                }
                if (WebActivity.this.sfjy) {
                    ToastUtil.toast(WebActivity.this.mContext, "msg", "发送失败，您已被禁言", ToastType.FAIL);
                    return;
                }
                if (Utils.getBooleanMsg(OaApplication.application, "yspFlag")) {
                    WebActivity.this.checkVideoPermissionChat();
                } else {
                    ToastUtil.toast(WebActivity.this.mContext, ToastInfo.notEnabled, "视频会议", ToastType.FAIL);
                }
                WebActivity.this.yinShiPinPopupWindow.dismiss();
            }
        });
        Utils.setBackgroundAlpha(this, 0.7f);
        this.yinShiPinPopupWindow.showAtLocation(this.ll, 0, 0, 0);
        this.popIsShow = true;
    }

    private void finishPage() {
        ThreadPoolManager.getInstance().execute(new Runnable() { // from class: com.shineyue.pm.web.WebActivity.162
            @Override // java.lang.Runnable
            public void run() {
                if (!TextUtils.isEmpty((CharSequence) WebActivity.this.quoteMap.get("quoteMsgId"))) {
                    MainFragmentMessageNewVersion.quoteMapGloble.put(WebActivity.this.toUser, WebActivity.this.quoteMessage);
                } else {
                    MainFragmentMessageNewVersion.quoteMapGloble.remove(WebActivity.this.toUser);
                }
                final String msg = WebActivity.this.editEmojicon.getText().toString().trim();
                if (OaApplication.JIQIRENID.equals(WebActivity.this.toUser)) {
                    EventBus.getDefault().post(new Notify.Message(Notify.SAVE_JIQIREN_DRAFT, msg));
                } else if (Utils.haveValue(msg)) {
                    WebActivity.this.chatWindowListPresenter.getChatWindowFromDb(WebActivity.this.toUser, new ChatWindowListDaoManager.OnChatWindowItemResultListener() { // from class: com.shineyue.pm.web.WebActivity.162.1
                        @Override // com.shineyue.pm.modle_chat.presenter.chat_window_list.ChatWindowListDaoManager.OnChatWindowItemResultListener
                        public void onChatWindowItemResult(ChatWindowItem chatWindowItem) {
                            if (chatWindowItem != null) {
                                Message message = chatWindowItem.getContent().content;
                                message.draft = msg;
                                EventBus.getDefault().post(new Notify.Message(Notify.ChatMsg3, message));
                                WebActivity.this.chatWindowListPresenter.updateDbInfoByMessageNotDraft(message, null);
                            }
                        }
                    });
                } else {
                    WebActivity.this.chatWindowListPresenter.getChatWindowFromDb(WebActivity.this.toUser, new ChatWindowListDaoManager.OnChatWindowItemResultListener() { // from class: com.shineyue.pm.web.WebActivity.162.2
                        @Override // com.shineyue.pm.modle_chat.presenter.chat_window_list.ChatWindowListDaoManager.OnChatWindowItemResultListener
                        public void onChatWindowItemResult(ChatWindowItem chatWindowItem) {
                            if (chatWindowItem != null) {
                                Message message = chatWindowItem.getContent().content;
                                message.draft = "";
                                EventBus.getDefault().post(new Notify.Message(Notify.ChatMsg3, message));
                                WebActivity.this.chatWindowListPresenter.updateDbInfoByMessageNotDraft(message, null);
                            }
                        }
                    });
                }
                WebActivity.this.myThread.stopHeart();
                WebActivity.this.myThread.interrupt();
                MediaManager.release();
            }
        });
        EventBus.getDefault().unregister(this);
        finish();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void getHotissues() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("tenantId", Utils.getMsg(OaApplication.application, "zxjgbm"));
        JSONObject jsonObject = new JSONObject(map);
        KLog.d(this.TAG, jsonObject.toString());
        OkGoNetManager.getInstance();
        ((PostRequest) OkGo.post(OkGoNetManager.APP_URL + OkGoNetManager.HOT_ISSUES).tag(this)).upJson(jsonObject.toString()).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.163
            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onSuccess(String s, Call call, Response response) {
                try {
                    KLog.i(WebActivity.this.TAG, s);
                    JSONObject jsonObject1 = new JSONObject(s);
                    if (jsonObject1.optBoolean(ToastType.SUCCESS)) {
                        Utils.saveMsg(WebActivity.this.mContext, "HOTISSUES", jsonObject1.getJSONArray("data").toString());
                        WebActivity.this.setHotissues();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                KLog.d(WebActivity.this.TAG, e.toString());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setHotissues() {
        List<HotIssuesBean> textData = JSON.parseArray(Utils.getMsg(this.mContext, "HOTISSUES"), HotIssuesBean.class);
        if (textData != null && textData.size() > 0) {
            for (int i = 0; i < textData.size(); i++) {
                View view = View.inflate(this.mContext, R.layout.chat_item_robot, null);
                final TextView mTvRedian = (TextView) view.findViewById(R.id.tv_redian);
                mTvRedian.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.164
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        WebActivity webActivity = WebActivity.this;
                        webActivity.sendMsg(webActivity.toUser, mTvRedian.getText().toString(), null, null);
                    }
                });
                LinearLayout.LayoutParams mTvRedianParams = new LinearLayout.LayoutParams(-1, -1);
                if (i == 0) {
                    mTvRedianParams.leftMargin = 0;
                } else {
                    mTvRedianParams.leftMargin = Utils.Dp2Px(this.mContext, 8.0f);
                }
                mTvRedian.setText(textData.get(i).getMatchQuestion());
                this.mHsvMainContent.addView(view, mTvRedianParams);
            }
            this.mHsvMain.setVisibility(0);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void getToken(final File file, final Message message, final int num) {
        try {
            if (OaApplication.chatUploadToken_time != 0 && System.currentTimeMillis() - OaApplication.chatUploadToken_time < 9000000) {
                String access_token = OaApplication.chatUploadToken;
                getSpaceCode(access_token, file, message, num);
            } else {
                String result = Utils.getMsg(this.mContext, "UserXx");
                JSONObject jsonObject = new JSONObject(result);
                String grxx = Utils.getMsg(this.mContext, "grxx");
                jsonObject.put("grxx", grxx);
                String user_info = jsonObject.toString();
                long time = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String date = sdf.format(Long.valueOf(time));
                String sign = "client_id" + Constants.CLIENT_ID + "roleadminserviceid" + Utils.getQyCode() + "timestamp" + date + "userid" + Utils.getMsg(this.mContext, "userid") + "userinfo" + user_info + Constants.APPSECRET;
                String sign2 = MD5Util.getMD5(sign);
                StringBuilder sb = new StringBuilder();
                String grxx2 = OkGoNetManager.TICKET_COMMENT_URL;
                sb.append(grxx2);
                sb.append(OkGoNetManager.GET_TOKEN);
                ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) OkGo.post(sb.toString()).tag(this)).headers("M-Sy-AppId", Constants.M_SY_APPID)).headers("M-Sy-Version", Constants.M_SY_VERSION)).headers("M-Sy-Service", Utils.getQyCode())).params("client_id", Constants.CLIENT_ID, new boolean[0])).params("role", "admin", new boolean[0])).params("serviceid", Utils.getQyCode(), new boolean[0])).params("timestamp", date, new boolean[0])).params("userid", Utils.getMsg(this.mContext, "userid"), new boolean[0])).params("userinfo", user_info, new boolean[0])).params("sign", sign2, new boolean[0])).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.165
                    @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject jsonObject2 = new JSONObject(s);
                            String access_token2 = jsonObject2.getString("access_token");
                            OaApplication.chatUploadToken = access_token2;
                            OaApplication.chatUploadToken_time = System.currentTimeMillis();
                            WebActivity.this.getSpaceCode(access_token2, file, message, num);
                        } catch (JSONException e) {
                            Utils.dismissProgressDialog();
                            ToastUtil.toast(WebActivity.this.mContext, ToastInfo.serviceError, "上传", ToastType.FAIL);
                            e.printStackTrace();
                            message.sendStatus = 2;
                            WebActivity.this.updataMessage(message);
                        }
                    }

                    @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Utils.dismissProgressDialog();
                        ToastUtil.toast(WebActivity.this.mContext, ToastInfo.serviceError, "上传", ToastType.FAIL);
                        message.sendStatus = 2;
                        WebActivity.this.updataMessage(message);
                        if (response == null) {
                        }
                    }
                });
            }
        } catch (JSONException e) {
            message.sendStatus = 2;
            updataMessage(message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getSpaceCode(final String access_token, final File file, final Message message, final int num) {
        boolean hasmMaxUpFileSize;
        String maxUpFileSize;
        String spaceCode_save = Utils.getMsg(OaApplication.application, "Upload_spaceCode");
        String maxUpFileSize_save = Utils.getMsg(OaApplication.application, "Upload_maxUpFileSize");
        if (!TextUtils.isEmpty(spaceCode_save) && !TextUtils.isEmpty(maxUpFileSize_save)) {
            if (TextUtils.isEmpty(maxUpFileSize_save) || "null".equals(maxUpFileSize_save)) {
                hasmMaxUpFileSize = false;
                maxUpFileSize = "0";
            } else {
                hasmMaxUpFileSize = true;
                maxUpFileSize = maxUpFileSize_save;
            }
            if (hasmMaxUpFileSize && file.length() > Long.parseLong(maxUpFileSize) * 1024) {
                ToastUtil.toast(this.mContext, ToastInfo.outOfRange, "上传文件", maxUpFileSize + "KB", ToastType.WARNING);
                Utils.dismissProgressDialog();
                message.sendStatus = 2;
                updataMessage(message);
                return;
            }
            upDateImg(access_token, file, spaceCode_save, message, num);
            return;
        }
        String url = OkGoNetManager.TICKET_COMMENT_URL + OkGoNetManager.GET_SPACECODE;
        OkGo.get(url).tag(this).headers("M-Sy-AppId", Constants.M_SY_APPID).headers("M-Sy-Service", Utils.getQyCode()).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.166
            /* JADX WARN: Removed duplicated region for block: B:15:0x0050  */
            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void onSuccess(java.lang.String r26, okhttp3.Call r27, okhttp3.Response r28) {
                /*
                    Method dump skipped, instruction units count: 282
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.shineyue.pm.web.WebActivity.AnonymousClass166.onSuccess(java.lang.String, okhttp3.Call, okhttp3.Response):void");
            }

            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Utils.dismissProgressDialog();
                ToastUtil.toast(WebActivity.this.mContext, ToastInfo.serviceError, "上传", ToastType.FAIL);
                message.sendStatus = 2;
                WebActivity.this.updataMessage(message);
                if (response == null) {
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r15v0, types: [com.shineyue.pm.web.WebActivity] */
    /* JADX WARN: Type inference failed for: r15v1 */
    /* JADX WARN: Type inference failed for: r15v10 */
    /* JADX WARN: Type inference failed for: r15v11 */
    /* JADX WARN: Type inference failed for: r15v12 */
    /* JADX WARN: Type inference failed for: r15v13 */
    /* JADX WARN: Type inference failed for: r15v6 */
    /* JADX WARN: Type inference failed for: r15v7 */
    /* JADX WARN: Type inference failed for: r15v9 */
    /* JADX WARN: Type inference failed for: r23v0, types: [com.shineyue.pm.web.WebActivity] */
    /* JADX WARN: Type inference failed for: r7v1 */
    /* JADX WARN: Type inference failed for: r7v3 */
    /* JADX WARN: Type inference failed for: r7v4, types: [com.shineyue.pm.web.WebActivity] */
    /* JADX WARN: Type inference failed for: r7v6 */
    public void upDateImg(final String str, final File file, final String str2, final Message message, final int i) {
        ?? r15;
        ?? r152 = this;
        try {
            try {
                if (!Utils.getBooleanMsg(r152.mContext, "mgcjc")) {
                    try {
                        try {
                            Utils.upDataFile(r152.mContext, str2, str, file, new OnParmsCallBack<String>() { // from class: com.shineyue.pm.web.WebActivity.168
                                @Override // com.shineyue.pm.modle_chat.chat_history.dao.OnParmsCallBack
                                public void onParmsResult(String s) {
                                    if (s != null) {
                                        try {
                                            JSONObject result = new JSONObject(s);
                                            KLog.i(WebActivity.this.TAG + "1", result);
                                            int retcode = result.optInt("retcode");
                                            if (retcode == 0) {
                                                String fileUrl = OkGoNetManager.UPDATA_URL + result.getString("path");
                                                String fileName = file.getName();
                                                WebActivity.this.sendFile(fileUrl, fileName, message, i);
                                                if (file.length() < HttpFileUploader.BIG_FILE_SIZE_THRESHOLD) {
                                                    WebActivity.this.getKkfileType(fileUrl);
                                                }
                                                return;
                                            }
                                            message.sendStatus = 2;
                                            ToastUtil.toast(WebActivity.this.mContext, "msg", result.optString("msg"), ToastType.FAIL);
                                            WebActivity.this.updataMessage(message);
                                            return;
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            KLog.e(WebActivity.this.TAG, e.getMessage());
                                            message.sendStatus = 2;
                                            WebActivity.this.updataMessage(message);
                                            return;
                                        }
                                    }
                                    Utils.dismissProgressDialog();
                                    ToastUtil.toast(WebActivity.this.mContext, ToastInfo.serviceError, "上传", ToastType.FAIL);
                                    message.sendStatus = 2;
                                    WebActivity.this.updataMessage(message);
                                }
                            });
                            return;
                        } catch (JSONException e) {
                            return;
                        }
                    } catch (JSONException e2) {
                        return;
                    }
                }
                try {
                    final String msg = Utils.getMsg(r152.mContext, "khbh");
                    final String msg2 = Utils.getMsg(OaApplication.application, "zxjgbm");
                    final JSONObject jSONObject = new JSONObject();
                    final String[] strArr = new String[1];
                    jSONObject.put("laiyuan", "聊天");
                    jSONObject.put("xingming", Utils.getMsg(OaApplication.application, c.e));
                    jSONObject.put("cllj", "");
                    try {
                        try {
                            jSONObject.put("danwei", Utils.getMsg(r152.mContext, "deptmc"));
                        } catch (JSONException e3) {
                            if (r152.chatType.equals("7") || r152.chatType.equals("4")) {
                                JSONObject jSONObject2 = jSONObject;
                                ?? r7 = r152;
                                strArr[0] = "在【" + r7.toUserName + "】的聊天文件【" + file.getName() + "】";
                                try {
                                    jSONObject2.put("ywzy", strArr[0]);
                                } catch (JSONException e4) {
                                }
                                upDateImgNew(str, str2, msg, msg2, jSONObject2, file, message, i);
                            }
                            if (r152.chatType.equals("3") || r152.chatType.equals("6")) {
                                r152.mailPresenter.getMailListFromDbById(r152.toUser, new MailListDaoManager.OnMailListItem() { // from class: com.shineyue.pm.web.WebActivity.167
                                    @Override // com.shineyue.pm.modle_maillist.dao.MailListDaoManager.OnMailListItem
                                    public void onMailItemResult(MailItem mailItem) {
                                        if (mailItem != null && !TextUtils.isEmpty(mailItem.deptFullName)) {
                                            strArr[0] = "在【" + mailItem.deptFullName + "-" + WebActivity.this.toUserName + "】的聊天文件【" + file.getName() + "】";
                                        } else {
                                            strArr[0] = "在【" + WebActivity.this.toUserName + "】的聊天文件【" + file.getName() + "】";
                                        }
                                        try {
                                            jSONObject.put("ywzy", strArr[0]);
                                            WebActivity.this.upDateImgNew(str, str2, msg, msg2, jSONObject, file, message, i);
                                        } catch (JSONException e5) {
                                            throw new RuntimeException(e5);
                                        }
                                    }
                                });
                            }
                        }
                        if (r152.chatType.equals("7")) {
                            strArr[0] = "在【" + r152.toUserName + "】的聊天文件【" + file.getName() + "】";
                            jSONObject.put("ywzy", strArr[0]);
                            JSONObject jSONObject3 = jSONObject;
                            upDateImgNew(str, str2, msg, msg2, jSONObject3, file, message, i);
                            r152 = jSONObject3;
                        } else {
                            try {
                                if (r152.chatType.equals("4")) {
                                    strArr[0] = "在【" + r152.toUserName + "】的聊天文件【" + file.getName() + "】";
                                    try {
                                        jSONObject.put("ywzy", strArr[0]);
                                    } catch (JSONException e5) {
                                    }
                                    JSONObject jSONObject32 = jSONObject;
                                    try {
                                        upDateImgNew(str, str2, msg, msg2, jSONObject32, file, message, i);
                                        r152 = jSONObject32;
                                    } catch (JSONException e6) {
                                    }
                                } else if (r152.chatType.equals("3") || r152.chatType.equals("6")) {
                                    r152.mailPresenter.getMailListFromDbById(r152.toUser, new MailListDaoManager.OnMailListItem() { // from class: com.shineyue.pm.web.WebActivity.167
                                        @Override // com.shineyue.pm.modle_maillist.dao.MailListDaoManager.OnMailListItem
                                        public void onMailItemResult(MailItem mailItem) {
                                            if (mailItem != null && !TextUtils.isEmpty(mailItem.deptFullName)) {
                                                strArr[0] = "在【" + mailItem.deptFullName + "-" + WebActivity.this.toUserName + "】的聊天文件【" + file.getName() + "】";
                                            } else {
                                                strArr[0] = "在【" + WebActivity.this.toUserName + "】的聊天文件【" + file.getName() + "】";
                                            }
                                            try {
                                                jSONObject.put("ywzy", strArr[0]);
                                                WebActivity.this.upDateImgNew(str, str2, msg, msg2, jSONObject, file, message, i);
                                            } catch (JSONException e52) {
                                                throw new RuntimeException(e52);
                                            }
                                        }
                                    });
                                    r152 = r152;
                                } else {
                                    r152 = r152;
                                }
                            } catch (JSONException e7) {
                                r15 = r152;
                            }
                        }
                    } catch (Throwable th) {
                        if (!r152.chatType.equals("7") && !r152.chatType.equals("4")) {
                            if (!r152.chatType.equals("3") && !r152.chatType.equals("6")) {
                                throw th;
                            }
                            r152.mailPresenter.getMailListFromDbById(r152.toUser, new MailListDaoManager.OnMailListItem() { // from class: com.shineyue.pm.web.WebActivity.167
                                @Override // com.shineyue.pm.modle_maillist.dao.MailListDaoManager.OnMailListItem
                                public void onMailItemResult(MailItem mailItem) {
                                    if (mailItem != null && !TextUtils.isEmpty(mailItem.deptFullName)) {
                                        strArr[0] = "在【" + mailItem.deptFullName + "-" + WebActivity.this.toUserName + "】的聊天文件【" + file.getName() + "】";
                                    } else {
                                        strArr[0] = "在【" + WebActivity.this.toUserName + "】的聊天文件【" + file.getName() + "】";
                                    }
                                    try {
                                        jSONObject.put("ywzy", strArr[0]);
                                        WebActivity.this.upDateImgNew(str, str2, msg, msg2, jSONObject, file, message, i);
                                    } catch (JSONException e52) {
                                        throw new RuntimeException(e52);
                                    }
                                }
                            });
                            throw th;
                        }
                        strArr[0] = "在【" + r152.toUserName + "】的聊天文件【" + file.getName() + "】";
                        try {
                            jSONObject.put("ywzy", strArr[0]);
                        } catch (JSONException e8) {
                        }
                        upDateImgNew(str, str2, msg, msg2, jSONObject, file, message, i);
                        throw th;
                    }
                } catch (JSONException e9) {
                }
            } catch (JSONException e10) {
            }
        } catch (JSONException e11) {
            r15 = r152;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void upDateImgNew(String access_token, String spaceCode, String grbh, String tenantId, JSONObject notes, final File file, final Message message, final int num) {
        Utils.upDataFileNew(this.mContext, access_token, spaceCode, grbh, tenantId, notes, file, new OnParmsCallBack<String>() { // from class: com.shineyue.pm.web.WebActivity.169
            @Override // com.shineyue.pm.modle_chat.chat_history.dao.OnParmsCallBack
            public void onParmsResult(String s) {
                if (s != null) {
                    try {
                        JSONObject result = new JSONObject(s);
                        KLog.i(WebActivity.this.TAG + "1", result);
                        int retcode = result.optInt("retcode");
                        if (retcode == 0) {
                            String fileUrl = OkGoNetManager.UPDATA_URL + result.getString("path");
                            String fileName = file.getName();
                            WebActivity.this.sendFile(fileUrl, fileName, message, num);
                            if (file.length() < HttpFileUploader.BIG_FILE_SIZE_THRESHOLD) {
                                WebActivity.this.getKkfileType(fileUrl);
                            }
                            return;
                        }
                        message.sendStatus = 2;
                        ToastUtil.toast(WebActivity.this.mContext, "msg", result.optString("msg"), ToastType.FAIL);
                        WebActivity.this.updataMessage(message);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        KLog.e(WebActivity.this.TAG, e.getMessage());
                        message.sendStatus = 2;
                        WebActivity.this.updataMessage(message);
                        return;
                    }
                }
                Utils.dismissProgressDialog();
                ToastUtil.toast(WebActivity.this.mContext, ToastInfo.serviceError, "上传", ToastType.FAIL);
                message.sendStatus = 2;
                WebActivity.this.updataMessage(message);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void SpeechToTextByFile(File file) {
        OkGoNetManager.getInstance();
        ((PostRequest) OkGo.post(OkGoNetManager.APP_URL + OkGoNetManager.CHATBOTASR).tag(this)).params("file", file).isMultipart(true).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.170
            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject result = new JSONObject(s);
                    if (result.optInt("code") != 0) {
                        ToastUtil.toast(WebActivity.this.mContext, ToastInfo.btnError, "转文本", ToastType.FAIL);
                        return;
                    }
                    JSONObject jsonObject1 = result.optJSONObject("data");
                    String text = jsonObject1.optString("text");
                    if (!TextUtils.isEmpty(text)) {
                        if (WebActivity.this.dingTime > 0) {
                            WebActivity.this.dingTime = 0L;
                            WebActivity.this.loadDate();
                        }
                        if (text.trim().length() > 0) {
                            if (text.trim().length() > 2000) {
                                ToastUtil.toast(WebActivity.this.mContext, ToastInfo.outOfRange, "文本内容", "2000字", ToastType.FAIL);
                            } else {
                                WebActivity webActivity = WebActivity.this;
                                webActivity.sendMsg(webActivity.toUser, text.trim(), null, null);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    KLog.e(WebActivity.this.TAG, e.getMessage());
                    ToastUtil.toast(WebActivity.this.mContext, ToastInfo.btnError, "转文本", ToastType.FAIL);
                }
            }

            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                KLog.i(WebActivity.this.TAG, e.toString());
                ToastUtil.toast(WebActivity.this.mContext, ToastInfo.btnError, "转文本", ToastType.FAIL);
            }
        });
    }

    public void showCardButtonMoreDialog(JSONArray btnList, int position) {
        if (isKeyboardShown(this.ll)) {
            hideKeyboard(this);
        }
        if (this.mCareButtonMorePopup == null) {
            View contentView = LayoutInflater.from(this.mContext).inflate(R.layout.chat_card_button_popup, (ViewGroup) null);
            this.ll_content_view = (LinearLayout) contentView.findViewById(R.id.ll_content_view);
            TextView tv_lable_cancle = (TextView) contentView.findViewById(R.id.tv_lable_cancle);
            tv_lable_cancle.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.171
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    WebActivity.this.mCareButtonMorePopup.dismiss();
                }
            });
            PopupWindow popupWindow = new PopupWindow(contentView, -1, -2);
            this.mCareButtonMorePopup = popupWindow;
            popupWindow.setOutsideTouchable(false);
            this.mCareButtonMorePopup.setTouchable(true);
            this.mCareButtonMorePopup.setFocusable(true);
            this.mCareButtonMorePopup.setOutsideTouchable(true);
            contentView.measure(0, 0);
            this.mCareButtonMorePopup.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.shineyue.pm.web.WebActivity.172
                @Override // android.widget.PopupWindow.OnDismissListener
                public void onDismiss() {
                    Utils.setBackgroundAlpha((Activity) WebActivity.this.mContext, 1.0f);
                }
            });
        }
        showCardButton(btnList, position);
        Utils.setBackgroundAlpha((Activity) this.mContext, 0.7f);
        this.mCareButtonMorePopup.showAtLocation(findViewById(R.id.v_back), 80, 0, 0);
    }

    private void showCardButton(JSONArray btnList, final int position) {
        int totalNum;
        this.ll_content_view.removeAllViews();
        if (position == -1) {
            totalNum = 3;
        } else {
            totalNum = 2;
        }
        if (btnList.length() > totalNum) {
            for (int i = totalNum - 1; i < btnList.length(); i++) {
                final JSONObject jsonObject = btnList.optJSONObject(i);
                LinearLayout rootView = (LinearLayout) View.inflate(this.mContext, R.layout.chat_card_button_popup_item, null);
                TextView mCardButton = (TextView) rootView.findViewById(R.id.tv_card_buttom_name);
                mCardButton.setText(jsonObject.optString("btn"));
                mCardButton.setOnClickListener(new View.OnClickListener() { // from class: com.shineyue.pm.web.WebActivity.173
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        if (position != -1) {
                            if (WebActivity.this.chatAdapter != null) {
                                WebActivity.this.chatAdapter.cardButtonClickEvent(jsonObject, v, position);
                            }
                        } else {
                            WebActivity.this.cardButtonClickEvent(jsonObject, v);
                        }
                        WebActivity.this.mCareButtonMorePopup.dismiss();
                    }
                });
                this.ll_content_view.addView(rootView);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x029b  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0230 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0206  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0208 A[Catch: Exception -> 0x02a7, TRY_ENTER, TRY_LEAVE, TryCatch #3 {Exception -> 0x02a7, blocks: (B:66:0x01f2, B:80:0x021d, B:79:0x021a, B:74:0x0208, B:76:0x0213), top: B:132:0x01f2, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0238 A[Catch: Exception -> 0x02a4, TRY_ENTER, TryCatch #7 {Exception -> 0x02a4, blocks: (B:82:0x0225, B:89:0x024c, B:95:0x028a, B:94:0x0287, B:88:0x0238), top: B:140:0x0225 }] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0283 A[Catch: Exception -> 0x0234, TRY_ENTER, TRY_LEAVE, TryCatch #6 {Exception -> 0x0234, blocks: (B:84:0x0230, B:92:0x0283), top: B:138:0x0230 }] */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0287 A[Catch: Exception -> 0x02a4, TRY_ENTER, TryCatch #7 {Exception -> 0x02a4, blocks: (B:82:0x0225, B:89:0x024c, B:95:0x028a, B:94:0x0287, B:88:0x0238), top: B:140:0x0225 }] */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0290  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void cardButtonClickEvent(org.json.JSONObject r21, android.view.View r22) {
        /*
            Method dump skipped, instruction units count: 798
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shineyue.pm.web.WebActivity.cardButtonClickEvent(org.json.JSONObject, android.view.View):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void callPublicMethods(String url, String fwbs, String param) {
        String location = Utils.getMsg(this.mContext, PermissionConstants.LOCATION);
        JSONObject jsonResult_param = null;
        try {
            JSONObject jsonResult = new JSONObject(location);
            double latitude = jsonResult.optDouble("latitude", 0.0d);
            double longitude = jsonResult.optDouble("longitude", 0.0d);
            if (!TextUtils.isEmpty(param)) {
                jsonResult_param = new JSONObject(param);
                jsonResult_param.put("coordinate", latitude + "," + longitude);
            }
        } catch (JSONException e) {
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("url", url);
        map.put("fwbs", fwbs);
        if (jsonResult_param != null) {
            map.put(RemoteMessageConst.MessageBody.PARAM, jsonResult_param);
        }
        JSONObject jsonObject = new JSONObject(map);
        KLog.d(this.TAG, jsonObject.toString());
        OkGoNetManager.getInstance();
        ((PostRequest) OkGo.post(OkGoNetManager.APP_URL + OkGoNetManager.ROBOT_PUBLIC_INTERFACE).tag(this)).upJson(jsonObject).execute(new MyStringCallback() { // from class: com.shineyue.pm.web.WebActivity.174
            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onSuccess(String s, Call call, Response response) {
                try {
                    if (TextUtils.isEmpty(s)) {
                        ToastUtil.toast(WebActivity.this.mContext, ToastInfo.btnError, "请求", ToastType.FAIL);
                        return;
                    }
                    JSONObject jsonObject2 = new JSONObject(s);
                    if (jsonObject2.optBoolean(ToastType.SUCCESS)) {
                        if (!TextUtils.isEmpty(jsonObject2.optString("msg"))) {
                            ToastUtil.toast(WebActivity.this.mContext, "msg", jsonObject2.optString("msg"), ToastType.SUCCESS);
                        } else {
                            ToastUtil.toast(WebActivity.this.mContext, ToastInfo.btnSuccess, "请求", ToastType.SUCCESS);
                        }
                    } else if (!TextUtils.isEmpty(jsonObject2.optString("msg"))) {
                        ToastUtil.toast(WebActivity.this.mContext, "msg", jsonObject2.optString("msg"), ToastType.FAIL);
                    } else {
                        ToastUtil.toast(WebActivity.this.mContext, ToastInfo.btnError, "请求", ToastType.FAIL);
                    }
                } catch (Exception e2) {
                    ToastUtil.toast(WebActivity.this.mContext, ToastInfo.btnError, "请求", ToastType.FAIL);
                }
            }

            @Override // com.shineyue.pm.network.callback.MyStringCallback, com.lzy.okgo.callback.AbsCallback
            public void onError(Call call, Response response, Exception e2) {
                super.onError(call, response, e2);
                ToastUtil.toast(WebActivity.this.mContext, ToastInfo.btnError, "请求", ToastType.FAIL);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initChat() {
        this.mHeight100 = Utils.Dp2Px(this.mContext, 100.0f);
        this.mHeight30 = Utils.Dp2Px(this.mContext, 30.0f);
        this.chatWindowListPresenter = new ChatWindowListPresenter(this);
        this.mailListDaoManager = new MailListDaoManager();
        this.groupListPresenter = new GroupListPresenter(this);
        this.chatAitPresenter = new ChatAitPresenter();
        this.mailPresenter = new MailPresenter(this);
        this.noSpeakingDaoManager = new NoSpeakingDaoManager();
        this.mailListPrimaryDepartmentDaoManager = new MailListPrimaryDepartmentDaoManager();
        initViewChat();
        initDataChat();
        initKeyBoradListener();
        getLastReadTime();
        shareMessage(null);
        if (OaApplication.chatService != null) {
            ChatServiceNew chatServiceNew = OaApplication.chatService;
            if (ChatServiceNew.ws != null) {
                ChatServiceNew chatServiceNew2 = OaApplication.chatService;
                if (ChatServiceNew.ws.isOpen()) {
                    this.myThread.start();
                }
            }
        }
        if (OaApplication.chatService != null) {
            ChatServiceNew chatServiceNew3 = OaApplication.chatService;
            if (ChatServiceNew.ws != null && OaApplication.chatService.isopen()) {
                return;
            }
        }
        ChatServiceNew.startChatService(OaApplication.application.getApplicationContext());
    }
}