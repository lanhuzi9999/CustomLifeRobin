package so.contacts.hub.basefunction.utils;

import java.io.File;

import android.net.Uri;
import android.os.Environment;


public class ConstantsParameter {
	/**tips 显示位置**/
	public static final int LOCATION_DIAL 		= 0;	//拨号
	public static final int LOCATION_CONTACT_LIST	= 1;//联系人列表
	public static final int LOCATION_SNS		= 2;//动态
	public static final int LOCATION_CIRCLE		= 4;//群组
	public static final int LOCATION_CONTACT_INFO = 11;//名片页
	
	/**tips 状态**/
	public static final int TIPS_STATUS_SHOW = 2;//已经展示
	public static final int TIPS_STATUS_CLICK = 3;//已经点击
	public static final int TIPS_STATUS_CLOSE = 4;//已经展示
	
	/**ContactsSelect 接收的Action**/
	public static final String INVITE_CIRCLE_MEMBER = "invite_circle_member";
	public static final String FROM_GROUP_TO_ADD = "from_group_to_add";
	public static final String FROM_GROUP_TO_SEND_MSG = "from_group_to_send_msg";
	public static final String FROM_GROUP_TO_CREATE = "from_group_to_create";
	
	/*热度分数*/
	public static final int HOT_DAYS = 30; //查询天数
	public static final float POINT_CALLLOG = 6.0f; //电话
	public static final float POINT_SMS = 2.0f;	   //短彩信
	public static final float POINT_SNS = 2.0f;	   //微博互动
	
	public static final String COOLCLOUD_INFO = "coolcloud_info";
	public static final String IMAGE_CACHE_DIR = "thumbs";
	public static final String CONTACTS_SETTING = "c_setting";
	public static final String TIPS_SETTING = "tips_setting";
	public static final String SHARED_PREFS_YELLOW_PAGE ="Shared_prefs_yellow_page"; 
	public static final String SHARED_PREFS_YELLOW_PAGE_DEAL ="Shared_prefs_yellow_page_deal";
	public static final String SHARED_PREFS_YELLOW_PAGE_COUPON ="Shared_prefs_yellow_page_coupon";
	public static final String YELLOW_PAGE_SELECTED_CITY = "yellow_page_selected_city";
	public static final String YELLOW_PAGE_MOVIE_SELECTED_CITY = "yellow_page_movie_selected_city";
	public static final String YELLOW_PAGE_MOVIE_SELECTED_CINEMA = "yellow_page_movie_selected_cinema";
	public static final String YELLOW_PAGE_DELICACY_SELECTED_CITY = "yellow_page_delicacy_selected_city";
	public static final String YELLOW_PAGE_OFFEN_USED_CITY = "yellow_page_offen_used_city";
	public static final String YELLOW_PAGE_TAXI_MAP = "yellow_page_taxi_map";
    public static final String PUSH_REG_CONFIG = "push_reg_config";
    
    /**
     * 个人中心设置本地存储
     * add by zjh 2015-05-17 start
     */
    public static final String PERSON_SETTING = "person_setting"; 						//个人信息设置：文件名
    public static final String PERSON_SETTING_STORE_STATE = "has_set_password_state";	//个人信息设置：是否存储
    /** add by zjh 2015-05-17 end */
	
	/** 搜索历史 SharedPreferences KEY */
	public static final String YELLOW_PAGE_SEARCH_HISTORY = "yellow_page_selected_history";
	
	/**酒店入住时间和离店时间*/
	public static final String PREFS_KEY_COM_DATE = "hotel_com_date";
	public static final String PREFS_KEY_LEAVE_DATE = "hotel_leave_date";
	
	/**
	 * 酷云信息
     */
    public static final String COOLCLOUD_AVATAR_HD = "coolcloud_avatar_hd";
    public static final String COOLCLOUD_AVATAR = "coolcloud_avatar";
	
	/**
	 * 热词搜索
	 */
	public static final String HOTKEY_WORDS_VERSION = "hot_keywords_version";
	public static final String HOTKEY_WORDS = "hotkey_words";
	/**
	 * 搜索推荐词
	 */
	public static final String RECOMMEND_WORDS_VERSION = "recommend_words_version";
	public static final String RECOMMEND_WORDS = "recommend_words";
	
	/**
	 * 服务号头像
	 */
	public static final String ETON_SERVICE_URL = "eton_service_url";
	
	/**********
	 * haole -- 微信同步因子KEY
	 */
	public static final String WECHAT_SYNC_FET = "wechat_sync_fet";
	public static final String SHOW_WECHAT_IN_LIST = "show_wechat_in_list";// 在列表中显示微信标识
	
	/*****************
	 * haole -- 双卡设置KEY
	 */
	public static final String DUAL_SIM_CARD_SETTING = "dual_sim_card_setting";
	public static final String SIM1_NAME = "sim1_name";
	public static final String SIM2_NAME = "sim2_name";
	
	/****
	 * haole -- 是否获取过友盟事件发送策略 2014-05-09
	 */
	public static final String IS_UM_LOADED = "is_um_loaded";
	
	/****
	 * haole -- 下次心跳时间 2014-05-09
	 */
	public static final String NEXT_HEART_BEAT_TIME = "next_heard_beat_time";
	
	/****
	 * haole -- 心跳时间间隔记录 2014-05-09
	 */
	public static final String HEART_DELAY_CONFIG = "heart_delay_config";
	
	public static final String DEF_BUBBLES = "def_bubbles";
	
	public static final String TAB_REMIND_FLAG = "tab_remind_flag";
	
	public static final String REMIND_VERSION = "remind_version";
	
	public static final String REMIND_MAX_COUNT = "remind_max_count";// add by putao_lhq 2014年11月8日

	public static final String USE_NET_SEARCH_STRATEGY = "use_net_search_strategy";// add by cj 2014年12月12日
	
	public static final String WX_OPEN_FLAG = "wx_open_flag";// add by cj 2015年02月07日

	public static final String ALARM_WAKEUP_TIME = "alarm_wakup_times";// add by cj 2014年12月19日

	public static final int NO_CONNECT_ALERT = 1003;// 无网络提示
	public static final int CHANGE_MSG_STATUS_TO_FAIL = 1002;// 改变消息状态处理为发送失败

	public static final int ADD_DEFAULT_CIRCLE_REQUEST = 9106;//默认引导圈子，点击创建请求码
	public static final int TO_GROUPD_DETAILS_REQUEST = 9107;//跳往群组详情请求码
	public static final int SYNC_WECHAT = 9105;
	public static final int SEN_MSG_STATUS = 9102;
	public static final int READ_CHECK_CODE = 9101;// 邀请加入圈子
	public static final int CIRCLE_NOTIFY_ID = 9100;// 邀请加入圈子
	public static final int NOTIFY_CIRCLE_INVITE_IN = 9001;// 邀请加入圈子
	public static final int NOTIFY_CIRCLE_KICK_OUT = 9002;// 被踢出圈子
	public static final int NOTIFY_CIRCLE_DISMISS = 9003;// 圈子被解散
	
	public static final int NOTIFY_WEIXIN_MATCH = 9004;// 通知微信匹配

	// 拍照的照片存储位置
	public static final File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory() + "/DCIM/Camera");

	public static final int TYPE_TEXT = 1;
	public static final int TYPE_IMG = 2;
	public static final int TYPE_LOVE = 3;
	public static final int TYPE_TASK = 4;
	public static final int TYPE_SYSTEM = 5;
	public static final int TYPE_WEIBO = 6;

	public static final int MSG_STAUTUS_SENGING = 1;// 发送中
	public static final int MSG_STAUTUS_SENG_OVER = 2;// 发送完成
	public static final int MSG_STAUTUS_SENG_FAIL = 3;// 发送失败

	public static final Uri MSG_URI = Uri.parse("content://putao_msg_list");
	public static final Uri PIC_URI = Uri.parse("content://putao_pic_list");
	public static final Uri CIRCLE_URI = Uri
			.parse("content://putao_circle_list");
	public static final Uri SNS_FRIENDS_URI = Uri
			.parse("content://sns_friends_list");
	public static final Uri RELATIONSHIP_URI = Uri
			.parse("content://putao_relationship");

	public static final int BOTTOM_BAR_ALPHA = 150;

	// 是否合并号码（IP号码、+86号码等）
	public static final boolean isMeargeNumber = false;
	public static final int GET_CALL_LOG_DATA = 1001;
	public static final int DELETE_ONE_BY_ONE = 1002;
	public static final int LOAD_INTENT_DATA = 1003;

	/*** 发送新微博的各种状态 ***/
	/** 总体状态 0--葡萄状态(包括发送成功,发送成功会发出广播供相关UI更新)；1--发送中；-1 -- 发送失败； **/
	public static int sendWeiboAllStatus = 0;
	public static int sendWeiboSina = 0; // 发送新浪 0--正常； 1--发送成功；-1--发送失败
	public static int sendWeiboTencent = 0; // 发送腾讯 0--正常； 1--发送成功；-1--发送失败
	public static int sendWeiboRenren = 0; // 发送人人 0--正常； 1--发送成功；-1--发送失败
	public static int shareWechat = 0; // 发送微信 0--正常； 1--发送成功；-1--发送失败

	public static final String  ENTER_FUNCTION_DESCRIPTION = "enter_funciton_description"; // 是否功能介绍
	public static final String SEND_SUCCESS = "send_status_success";
	public static final String SEND_FAIL = "send_status_fail";
	public static final String SENDING = "send_status_sending";
	public static final String SEND_TYPE_KEY = "send_type_key";
	
	public static final String REFRESH_SNS_INFO_STATE = "refresh_sns_info_state";

	/** Intent传递的参数及Action **/
	public static final int BINDSNS = 1; // 关联社交账号
//	public static final int SELECTCONTACTSSHOW = 2; // 联系人--选择要显示的联系人
	public static final int MOBILELOGIN = 3;
	public static final int AUTH = 4;// 授权登录
	public static final int CONTACT_RECORD_EDIT = 5;// 联系记录
	public static final int IMAGE_CODE = 6;// 图库
	public static final int CAMERA_CODE = 7;// 照相机
	public static final int CROP_IMAGE_CODE = 8;// 照片截图
	public static final int EDIT_ME_PROFILE = 9;// 编辑我
	public static final int INSERT_OR_EDIT_CONTACT = 10;// 新建或编辑联系人
	public static final int FRIEND_UPDATE_CODE = 11;// 好友更新
	public static final int FRIEND_UPDATE_MORE_CODE = 12;// 开通列表，更新资料
	public static final int EDIT_CONTACT_AVATAR_CODE = 13;// 编辑联系人头像
    public static final int MOBILE_LOGIN_REQUEST = 14;//账号登陆界面跳向手机号码登陆界面的请求

	public static String CALL_LOG = "call_log";
	public static String CONTACTS = "contact"; // 传递 ContactsBean 对象
    public static String CONTACT_ME = "contact_me"; // 传递 ContactsBean 我 对象
    public static String CONTACT_AVATAR = "contact_avatar"; // 传递 联系人头像URL
	public static String NUMBER = "number"; // 传递 号码
	public static String RAW_CONTACT_ID = "raw_contact_id"; // 传递 联系人Raw id 对象
	public static String CONTACT_ID = "contact_id";// 传递 联系人id 对象
	public static String SNS_USER = "sns_user"; // 传递 SNSUser 对象
	public static String CIRCLE_INFO = "circle_info";// 传递圈子信息对象
	public static String CIRCLE_MEMBER = "circle_member";// 传递圈子成员对象
	public static String CONTACT_RECORD = "contact_record";// 联系记录
	
	/*
	 * 服务号标识
	 * add by hyl 2014-4-24
	 */
	public static final String IS_SERVICE_CONTACT = "is_service_contact";

	public static String WEIXIN_SYNC_STATUS_CHANGE = "weixin.sync.status.change";
	
	public static final String BATCH_OPERATE_END = "so.contacts.hub.batch_operate_end";
	public static String CIRCLE_MEMBER_CHANGE = "so.contacts.hub.circle.member.changed";
	public static String CIRCLEINIT = "so.contacts.hub.circle.inited";
	public static String RECEIVER_MSG_INFO = "com.yulong.android.contacts.discover.Receiver.msg";
	public static String CONTATCTS_DATA_INITED = "so.contacts.hub.contact.data.inited";
	public static String RESEND_QUENE_MSG = "so.contacts.hub.chat.resendmsg";
	public static String REFRESH_MSG_STATUS_TO_FAILED = "so.contacts.hub.chat.refersh.msg.stauts.failed";
	public static String REFRESH_MSG_STATUS = "so.contacts.hub.chat.refersh.msg.stauts";
	public static String DELETE_MSGINFO = "so.contacts.hub.chat.delete.msg";
	public static String CHANGE_MSG_TO_FAIL = "so.contacts.hub.chat.msg.to.fail";
	public static String DESTORY_ROOMACTIVITY = "so.contacts.hub.chat.destory.RoomActivity";
	public static String FORCE_FRIEND_UPDATE = "so.contacts.hub.force.FriendUpdate";// 強制刷新好友更新
	public static String FORCE_QUIT = "so.contacts.hub.force.quit";// 账号在其他地方登录
																	// 强制下线
	public static String LOGOUT = "so.contacts.hub.Logout";// 退出登录
	public static String LOGIN_SUCCESS = "so.contacts.hub.LoginSuccess";// 登录成功
	public static String UNBIND_SUCCESS = "so.contacts.hub.UnbindSuccess";// 解除绑定成功
    public static final String ACTION_DUAL_CARD_MATCH = "so.contacts.hub.DualCardMatch";// 双卡匹配
    public static final String ACTION_APP_RECOMMEND = "so.contacts.hub.AppRecommend";// 应用推荐Change
    public static final String ACTION_NEED_TO_SHOW_CONTACTS_CHANGE = "so.contacts.hub.NeedToShowContactsChange";// 要显示的联系人发生变化

	public static String MSGINFO = "msgInfo";
	public static String COMMENTTYPE = "commentType";
	public static String STATUS = "status";
	public static String SNS_TYPE = "sns_type";
	public static String STATUSID = "stauts_id";
	public static String COMMENTID = "comment_id";
	public static String STATUSCONTENT = "status_content";
	public static String STATUS_REPLY_CONTENT = "status_reply_content";
	public static String STATUSMIDDPIC = "status_midd_pic";
	public static String STATUSORIGINALPIC = "original_pic";
	public static String STATUSMIDLLPIC = "bmiddle_pic";
	public static String FRIEND_UPDATE = "friend_update";// 好友更新
	public static String FRIEND_UPDATE_LIST = "friend_update_list";// 好友更新
	public static String UNREAD_COUNT = "unread_count";// 未读数
	public static String RELATIONSHIP = "relationship";// 关联关系
    public static final String SNS_STATUS = "sns_status";
    public static final String FORCED_APP_RECOMMEND_ID = "forced_app_recommend_id";// 强推荐应用ID
    public static final String DUAL_CARD_MATCH = "dual_card_match";// 双卡匹配
    public static final String DUAL_CARD_ID = "dual_card_id";// 双卡id
    public static final String APP_RECOMMEND_INFO = "app_recommend_info";// 双卡匹配

	public static final String SINAPEX = "sina.putao.cn://";
	public static final String TENCENTPEX = "tencent.putao.cn://";
	public static final String RENRENPEX = "renren.putao.cn://";

	// 名片
	public static final String FORCE_REFRESH_RALATIONSHIP = "force_refresh_relationship";
	public static final String FORCE_REFRESH_CONTACTS = "force_refresh_contacts";
	public static final String FORCE_CLOSE = "force_close";
	public static final String FORWARD = "forward";// 跳转到个人详情的第几个页签标识
	public static final String NOTIFY_WEIXIN = "notify_weixin";// 通知进入微信匹配
	public static final String REFRESH_ASSOCIATION_TITLE = "refresh_association_title";
	public static final String RECOMMEND_ASSOCIATION_COUNT = "recommend_association_count";

	public static final String SINA_PROVINCE = "sina_province";
	public static final String SINA_CITY = "sina_city";

	// 社交网络搜索
	public static final int SOCAIL_SEARCH_CODE = 101;
	public static final String SEARCH_KEY = "search";

	// 人人相关
	public static String UID = "uid";
	public static String OWERID = "owerId";
	public static String CONTENTID = "contentId";
	public static String COMMENTABLUM = "commentablum";

	// 同步
	public static final String SYNC_COMPLETION = "sync_completion";
	public static final String SYNC_BEGIN = "sync_begin";

	/******* 与服务器交互的ActionCode *********/
	public static final String NetSourceAuthRequestCode = "10002"; // 社交账号登录/授权
	public static final String NetSourceAuthExRequestCode = "10014"; // 用户登陆后授权社交账号[10014]
	public static final String AgreeMegerUserRequestCode = "10015"; // 用户同意合并账号接口[10015]
	public static final String MegerUserDataByMobileRequestCode = "10016"; // 用户合并账号验证接口[10016]
	public static final String QueryMeInfoRequestDataCode = "10005"; // 社交账号登录/授权
	public static final String UnBindingSnsExRequestCode = "10007"; // 解除社交账号授权
	public static final String BatchContactToSnsReqestCode = "20003"; // 批量上报关联关系
	public static final String GetRelationshipRecommendByMobileCode = "20004"; // 根据号码取推荐关联
	public static final String SnsContactRequestCode = "20002"; // 批量取得关联关系
	public static final String QueryContactRequestCode = "20005"; // 用户画像查询接口
	public static final String ReportContactsRequestCode = "20006"; // 用户画像查询接口
	public static final String UserUpdateNotifyRequestCode = "20009"; // 联系人更新接口
	public static final String QueryCircleInfoRequestCode = "30007"; // 查询圈子基本信息
	public static final String GetCircleNotifyRequestCode = "30012"; // 圈子消息通知接口
	public static final String ForcedAppRecommendRequestCode = "60001";// 强推荐应用接口
	public static final String CallLogColumnReportRequestCode = "70002";// 通话记录字段上报接口
	public static final String TIPS_REQUEST_CODE = "90001";// tips获取接口
	public static final String TIPS_REPORT_CODE = "90002";// tips上报接口
	public static final String ChargeTelephoneHistoryDataCode = "110003";// 话费充值历史获取接口
	public static final String REFRESH_CONTACTS = "so.hub.contacts.update_contacts"; // 更新联系人广播
	public static final String FORWARD_TYPE = "forward_type"; // 转发类型
	
	/******** 自定义NextCode，用于执行完某个请求时，继续操作的下一个请求 **********/
	public static String LoginOpenfireCode = "0001"; // 登录openfire

	public static final int FROM_ROOM_PHOTO_ACTIVITY = 3772;
	public static final int INVITE_NEW_MEMBER_IN_CIRCLE = 4001;
	public static final int DELETE_FAILURE = 0;
	public static final int DELETE_SUCCESS = 1;
	public static final int DELETE_START = 2;
	
	
    public static final int SHOW_KEY_BOARD = 2997; // 隐藏拨号盘 
    
    
    /**
     * 2013.12.5
     * zwj add for
     * wiget相关常量
     * ********************************************************************************************************
     */
    public static final String REFRESH_WIDGETS = "com.yulong.android.contacts.discover.Refresh_widget"; // 更新widget。
    public static final String WIDGET_SHARE_PREFS_NAME = "widget_data" ; // WIDGET SHAREPREFERENCE的文件名
    public static final String WIDGET_CACHE_OFFSET = "widget_cache_offset"; // 已经取得的widget数据库缓存的当前偏移量
    public static final String WIDGET_HAS_IT = "has_widget"; // 是否有widget 
    public static final String WIDGET_CONTACT_ID = "widget_contact_id"; // 是否有widget 
    public static final String WIDGET_CONTACT_ID_FLIPE_0 = "widget_contact_id_flipe_0"; // widget翻转头像的id
    public static final String WIDGET_CONTACT_ID_FLIPE_1 = "widget_contact_id_flipe_1"; // widget翻转头像的id
    public static final String WIDGET_CONTACT_ID_FLIPE_2 = "widget_contact_id_flipe_2"; // widget翻转头像的id
    public static final String WIDGET_CONTACT_ID_FLIPE_3 = "widget_contact_id_flipe_3"; // widget翻转头像的id
    public static final String WIDGET_CONTACT_ID_FLIPE_4 = "widget_contact_id_flipe_4"; // widget翻转头像的id
    public static final int WIDGET_SHOW_TIP = 100; // 显示默认widget界面
    public static final int WIDGET_LOAD_DATA = 101; // 加载数据
    public static final int WIDGET_PROCESS_DATA = 102; // 获取数据并刷新widget
    public static final int WIDGET_UPDATE = 103; // 翻转指定头像
    public static final int WIDGET_LOGOUT = 104; // 退出登录
    public static final int WIDGET_AN_HOUR_HAS_PASS = 105; // 一个小时已经过去了
    /** widget 刷新用到的常量  **/
    public static final int WIDGET_TYPE_0 = 0;// 0-4 显示翻转头像
    public static final int WIDGET_TYPE_1 = 1;
    public static final int WIDGET_TYPE_2 = 2;
    public static final int WIDGET_TYPE_3 = 3;
    public static final int WIDGET_TYPE_4 = 4;
    public static final int WIDGET_TYPE_5 = 5;// 5-9显示翻转微博图片
    public static final int WIDGET_TYPE_6 = 6;
    public static final int WIDGET_TYPE_7 = 7;
    public static final int WIDGET_TYPE_8 = 8;
    public static final int WIDGET_TYPE_9 = 9;
    public static final int WIDGET_TYPE_10 = 10; // 10 显示微博
    public static final int WIDGET_TYPE_11 = 11; // 11 显示静态图片
    public static final int WIDGET_TYPE_12 = 12; // 12 网络异常
    public static final int WIDGET_TYPE_13 = 13; // 13 没有足够的关联关系
    /**********************************************************************************************************/
    public static final int WIDGET_MANU_REFRESH_WEIBO = 14; // 14 刷新微博
    public static final int ADD_TO_LIST = 5;
    
    // 动态是否有更新的数据
    public static final String DISCOVER_TO_SNS_INFO_NEED_REFRESH_STATE = "need_refresh_state";
    
    /**********************************************************************************************************/
    /**
     * 黄页详情 中数据来源标识
     */
    public static final int YELLOWPAGE_SOURCETYPE_PUTAO = 1;     //数据来源：葡萄
    public static final int YELLOWPAGE_SOURCETYPE_DIANPING = 2;  //数据来源：点评
    public static final int YELLOWPAGE_SOURCETYPE_SOUGOU = 3;    //数据来源：搜狗
    public static final int YELLOWPAGE_SOURCETYPE_GAODE = 4;     //数据来源：高德
    public static final int YELLOWPAGE_SOURCETYPE_WUBA = 5;      //数据来源：58同城
    public static final int YELLOWPAGE_SOURCETYPE_ELONG = 6;     //数据来源：艺龙
    // add xcx 2014_12_25 start 新增同程搜索
    public static final int YELLOWPAGE_SOURCETYPE_TONGCHENG = 7; //数据来源：同程
    // add xcx 2014_12_25 end 新增同程搜索
    public static final String YELLOWPAGE_ERROR_RECOVERY_URL = "http://android1.putao.so/PT_SERVER/error_recovery.s?";
    
    /**********************************************************************************************************/
    /** 我的->类型->收藏 */
    public static final int YELLOWPAGE_DATA_TYPE_FAVORITE = 1;
    /** 我的->类型->历史 */
    public static final int YELLOWPAGE_DATA_TYPE_HISTORY = 2;

    public static final int YELLOWPAGE_DATA_TYPE_DEFAULT = 0;

    // 通知联系人刷新点
//    public static final String ACTION_REMIND_UPDATE = "so.contacts.hub.service.action_remind_update";  // removed by cj 2015/02/10
    
    // 通知插件刷新点
    public static final String ACTION_REMIND_UPDATE_PLUG = "so.contacts.hub.service.action_remind_update_plug";
    
    // 远程更新打点信息
    public static final String ACTION_REMOTE_UPDATE_REMIND = "so.contacts.hub.service.action_remote_update_remind";

    // 黄页插件数据更新
    public static final String ACTION_YELLOW_DATA_UPATE_PLUG = "com.yulong.android.contacts.yellowpage.data.update";
    
    // 远程更新活动彩蛋信息
    public static final String ACTION_REMOTE_UPDATE_ACTIVE = "so.contacts.hub.service.action_remote_update_active";
    
    //获得新的优惠券
    public static final String ACTION_NEW_VOUCHER_GET = "so.contacts.hub.service.action_new_voucher_get";
    
    //add by lisheng end
    //add by xcx  start 通知刷新订单数据 
    public static final String ACTION_ORDER_UPDATE_DATA = "so.contacts.hub.service.action_order_update_data";
    //add by xcx  end 通知刷新订单数据 
    // nextcode 定义
    //更新黄页数据
    public static final String YellowPageDataRequestCode = "110001"; 
    
    public static final String SHAREDPREFERENCES_DATA_DELIMITER = "♀";
    public static final String SHAREDPREFERENCES_DATA_DELIMITER_SECOND = "¤";
    
    //add ljq 用户的所在城市
    public static final String YELLOW_PAGE_GPSLOCATION = "gpslocation";
    public static final String YELLOW_PAGE_GPSLOCATION_LATITUDE = "gpslocation_latitude";
    public static final String YELLOW_PAGE_GPSLOCATION_LONGTITUDE = "gpslocation_longtitude";
    public static final String YELLOW_PAGE_GPSLOCATION_CITY = "gpslocation_city";
    public static final String CATEGORY_ITEMID = "ItemId";
    
    public static final String EXPAND_PARAM = "expand_param";  // 给彩蛋匹配扩展参数用
    
    public static final String IS_NEED_UPDATE_RECHARGE_NAME_FLAG = "is_need_update_recharge_name_flag";
    
    //定位信息改变Action add by hyl 2015-3-24
    public static final String SO_CONTACTS_HUB_LOCATION_CHANGED = "so.contacts.hub.location.changed";
    
    // added by wcy 2015-5-15 start 公用的message类别   
    public static final int MSG_LOCATION_SUCCESS_ACTION = 0x271;
    public static final int MSG_NETWORK_EXCEPTION_ACTION = 0x272;
    public static final int MSG_LOCATION_FAILED_ACTION = 0x273;
    public static final int MSG_INIT_DATA_ACTION = 0x274;
    public static final int MSG_REQUEST_SUCCESS_ACTION = 0x275;
    public static final int MSG_REQUEST_FAILED_ACTION = 0x276;
    public static final int MSG_SHOW_NODATA_ACTION = 0x277;
    // added by wcy 2015-5-15 end
    //更新用户习惯数据
    public static final String HabitDataRequestCode = "140002";
    
    
}
