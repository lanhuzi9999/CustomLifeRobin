package so.contacts.hub.basefunction.net.bean;

public class VerifyCaptchaResponseData extends BaseResponseData
{

    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;

    public static final String CHECK_CODE_IS_FAILED = "-100010";

    public static final String CHECK_CODE_IS_FALIED_MEAN = "验证码错误，请重新填写";

    public static final String CHECK_NOT_AUTH_USER = "-100011";

    public static final String CHECK_NOT_AUTH_USER_MENA = "非鉴权用户";

    public static final String CHECK_CODE_SUCCESS = "0000";

    public static final String CHECK_CODE_SUCCESS_MEAN = "check_code_success";

    public String code;

    public String code_meanString;
}
