package so.contacts.hub.basefunction.net.bean;

import so.contacts.hub.basefunction.config.Config;

public class VerifyCaptchaRequestData extends BaseOldRequestData<VerifyCaptchaResponseData>
{
    public String check_code;

    public int accSource;

    public int accType;

    public String mobile;

    public VerifyCaptchaRequestData(String actionCode, String checkCode, String accName)
    {
        super(actionCode);
        check_code = checkCode;
        accSource = 1;
        accType = 1;
        mobile = accName;
    }

    @Override
    protected VerifyCaptchaResponseData getNewInstance()
    {
        return new VerifyCaptchaResponseData();
    }

    @Override
    protected VerifyCaptchaResponseData fromJson(String content)
    {
        return Config.mGson.fromJson(content, VerifyCaptchaResponseData.class);
    }
}
