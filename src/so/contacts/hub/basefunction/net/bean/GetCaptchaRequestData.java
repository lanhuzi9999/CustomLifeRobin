package so.contacts.hub.basefunction.net.bean;

import so.contacts.hub.basefunction.config.Config;

public class GetCaptchaRequestData extends BaseOldRequestData<GetCaptchaResponseData>
{
    public String mobile;// 接收验证码的手机号

    public GetCaptchaRequestData(String actionCode, String mobile)
    {
        super(actionCode);
        this.mobile = mobile;
    }

    @Override
    protected GetCaptchaResponseData getNewInstance()
    {
        return new GetCaptchaResponseData();
    }

    @Override
    protected GetCaptchaResponseData fromJson(String content)
    {
        return Config.mGson.fromJson(content, GetCaptchaResponseData.class);
    }

}
