package so.contacts.hub.basefunction.account;

/**
 * **************************************************************** 文件名称 :
 * IAccCallback.java 作 者 : Robin 创建时间 : 2015-11-16 上午12:38:21 文件描述 : 账户登录回调接口
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有 修改历史 : 2015-11-16 1.00 初始版本
 ***************************************************************** 
 */
public interface IAccCallback
{

    /**
     * 账号已存在
     */
    public static final int LOGIN_FAILED_CODE_HAS_BIND = 1001;

    /**
     * 服务端异常
     */
    public static final int LOGIN_FAILED_CODE_SERVER_EXCEPTION = 1002;

    /**
     * 网络连接异常
     */
    public static final int LOGIN_FAILED_CODE_CONNECTION_EXCEPTION = 1003;

    /**
     * 密码或验证码错误
     */
    public static final int LOGIN_FAILED_CODE_CODE_WRONG = 1004;

    /**
     * 用户没有设置密码
     */
    public static final int LOGIN_FAILED_CODE_NO_PASSWORD = 1005;

    /**
     * 用户不存在
     */
    public static final int LOGIN_FAILED_CODE_NO_ACCOUNT = 1006;

    /**
     * 输入验证码超过3次
     */
    public static final int LOGIN_FAILED_CODE_TIMES_LIMIT = 1007;

    /**
     * 同盾风险决策认为用户不可信，登录失败
     */
    public static final int LOGIN_FAILED_CODE_TONG_DUN_FORBIDDEN = 1008;

    /**
     * 不支持静默登陆
     */
    public static final int LOGIN_FAILED_CANOT_SILENT_LOGIN = 1009;

    /**
     * 其他
     */
    public static final int LOGIN_FAILE_OTHER = 1010;

    /**
     * 无网络
     */
    public static final int LOGIN_CODE_NO_CONNECTION = 1011;

    /**
     * 链接超时
     */
    public static final int LOGIN_CODE_CONNECTION_TIMEOUT = 1012;

    /**
     * 服务器异常
     */
    public static final int LOGIN_CODE_SERVER_ERROR = 1013;

    /**
     * 网络异常
     */
    public static final int LOGIN_CODE_NET_ERROR = 1014;

    /**
     * 数据解析异常
     */
    public static final int LOGIN_CODE_PARSE_ERROR = 1015;

    /**
     * 登录成功 方法表述 void
     */
    public void onSuccess();

    /**
     * 登录失败 方法表述
     * 
     * @param failed_code void
     */
    public void onFail(int errorCode, String errorMsg);

    /**
     * 登录取消 方法表述 void
     */
    public void onCancel();
}
