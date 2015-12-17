package so.contacts.hub.basefunction.net.bean;

import so.contacts.hub.basefunction.config.Config;

public class LoginByPasswordRequestData extends BaseOldRequestData<LoginByPasswordResponseData>
{
    public String mobile;

    public String accName;

    public String password;

    public int accSource;

    public int accType;

    public LoginByPasswordRequestData(String accName, String password)
    {
        // 账号密码登陆的actioncode是200003
        super("200003");
        this.mobile = accName;
        this.accName = accName;
        this.password = password;
        this.accSource = 1;
        this.accType = 1;
    }

    @Override
    protected LoginByPasswordResponseData getNewInstance()
    {
        return new LoginByPasswordResponseData();
    }

    @Override
    protected LoginByPasswordResponseData fromJson(String content)
    {
        return Config.mGson.fromJson(content, LoginByPasswordResponseData.class);
    }
}
