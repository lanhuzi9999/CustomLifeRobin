package so.contacts.hub.basefunction.net.bean;

/**
 * ****************************************************************
 * 文件名称 : ResetPasswordResponseData.java
 * 作 者 :   Robin
 * 创建时间 : 2016-1-7 上午10:36:25
 * 文件描述 : 设置密码响应
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2016-1-7 1.00 初始版本
 *****************************************************************
 */
public class ResetPasswordResponseData extends BaseResponseData
{
    /** 下面是发生异常情况下各种返回码以及返回码的意义*/
    private static final long serialVersionUID = 1L;

    public static final String USER_NOT_EXISTS_CODE = "-100001";

    public static final String USER_NOT_EXISTS_CODE_MEAN = "user_not_exists";

    public static final String SET_PASSWORD_FAILED_CODE = "-100002";

    public static final String SET_PASSWORD_FAILED_CODE_MEAN = "set_password_failed";

    public static final String CHECK_NOT_AUTH_USER = "-100003";

    public static final String CHECK_NOT_AUTH_USER_MENA = "非鉴权用户";

    public static final String CHECK_CODE_IS_FAILED = "-100004";

    public static final String CHECK_CODE_IS_FALIED_MEAN = "验证码错误，请重新填写";

    public static final String SET_PASSWORD_SUCCESS_CODE = "0000";

    public static final String SET_PASSWORD_SUCCESS_CODE_MEAN = "set_password_success";

    public static final String LOGIN_FAILED_TONG_DUN_FORBIDDEN_CODE = "-100016";

    /** 设置密码返回的状态码 */
    public String code;

    /** 返回的状态码的含义 */
    public String code_mean;
}
