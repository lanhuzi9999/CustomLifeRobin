package so.contacts.hub.basefunction.net.bean;

public class LoginByPasswordResponseData extends BaseResponseData
{
    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;

    public static final String CHECK_PASSWORD_IS_VALID = "-100011";

    public static final String PASSWORD_VALID = "password_valid";

    public static final String CHECK_USER_NOT_EXISTS = "-100012";

    public static final String USER_NOT_EXISTS = "user_not_exists";

    public static final String USER_NOT_HAVE_PASSWORD = "-100013";

    public static final String USER_NOT_SET_PASSWORD_MEAN = "user_not_set_password";

    public static final String USER_NOT_TRANS_PASSWORD = "-100014";

    public static final String USER_NOT_TRANS_PASSWIRD_MEAN = "user_not_transport_passowrd";

    public static final String LOGIN_OK_CODE = "0000";

    public static final String LOGIN_OK_SUCCESS = "login_success";

    /**
     * 登陆返回状态码
     */
    public String login_code;

    /**
     * 状态码含义
     */
    public String code_mean;
}
