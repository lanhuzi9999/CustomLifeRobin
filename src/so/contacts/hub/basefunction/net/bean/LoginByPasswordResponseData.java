package so.contacts.hub.basefunction.net.bean;

public class LoginByPasswordResponseData extends BaseResponseData
{
    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;

    /**
     * 登陆返回状态码
     */
    public String login_code;

    /**
     * 状态码含义
     */
    public String code_mean;
}
