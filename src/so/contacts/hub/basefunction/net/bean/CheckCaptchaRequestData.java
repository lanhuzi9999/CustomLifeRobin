package so.contacts.hub.basefunction.net.bean;

import android.R.integer;
import so.contacts.hub.basefunction.config.Config;

public class CheckCaptchaRequestData extends BaseOldRequestData<CheckCaptchaResponseData>
{
    public int check_code;

    public String accName;

    public int accSource;
    
    public int accType;
    
    public CheckCaptchaRequestData(String accName, int checkCode)
    {
        super("200002");
        this.accName = accName;
        this.check_code = checkCode;
        this.accSource = 1;
        this.accType = 1;
    }

    @Override
    protected CheckCaptchaResponseData getNewInstance()
    {
        return new CheckCaptchaResponseData();
    }
    
    @Override
    protected CheckCaptchaResponseData fromJson(String content)
    {
        return Config.mGson.fromJson(content, CheckCaptchaResponseData.class);
    }
}
