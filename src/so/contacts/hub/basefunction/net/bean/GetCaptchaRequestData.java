package so.contacts.hub.basefunction.net.bean;

import so.contacts.hub.basefunction.config.Config;

public class GetCaptchaRequestData extends BaseOldRequestData<GetCaptchaResponse>
{
    public String mobile;// 接收验证码的手机号

    public GetCaptchaRequestData(String actionCode, String mobile)
    {
        super(actionCode);
        this.mobile = mobile;
    }

    @Override
    protected GetCaptchaResponse getNewInstance()
    {
        return new GetCaptchaResponse();
    }

    @Override
    protected GetCaptchaResponse fromJson(String content)
    {
        return Config.mGson.fromJson(content, GetCaptchaResponse.class);
    }

}
