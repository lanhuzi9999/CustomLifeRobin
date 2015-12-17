package so.contacts.hub.basefunction.net.bean;

import java.util.Map;

import so.contacts.hub.basefunction.account.manager.AccountManager;
import so.contacts.hub.basefunction.utils.SystemUtil;

import android.content.Context;
import android.text.TextUtils;

public class UpLoadUserBasicInfoRequestData extends BaseRequestData
{
    private Context context;

    private String token;

    private String app_id;

    private String app_version;

    private String brand;

    private String head_pic;

    private String city;

    private String gender;

    private String birthday;

    private String favorable;

    public UpLoadUserBasicInfoRequestData(Context context, String head_pic, String city, String gender, String birthday)
    {
        super();
        this.context = context;
        token = AccountManager.getInstance().getPtToken();
        // end 2015-7-3 by zj
        app_id = "10";
        app_version = SystemUtil.getAppVersion(context);
        brand = android.os.Build.MODEL;
        this.head_pic = head_pic;
        this.city = city;
        this.gender = gender;
        this.birthday = birthday;
        this.favorable = "";
    }

    @Override
    protected void setParams(Map<String, String> params)
    {
        if (!TextUtils.isEmpty(token))
        {
            params.put("token", token);
        }
        if (!TextUtils.isEmpty(app_id))
        {
            params.put("app_id", app_id);
        }
        if (!TextUtils.isEmpty(app_version))
        {
            params.put("app_version", app_version);
        }
        if (!TextUtils.isEmpty(brand))
        {
            params.put("brand", brand);
        }
        if (!TextUtils.isEmpty(head_pic))
        {
            params.put("head_pic", head_pic);
        }
        if (!TextUtils.isEmpty(birthday))
        {
            params.put("birthday", birthday);
        }
        if (!TextUtils.isEmpty(city))
        {
            params.put("city", city);
        }
        if (!TextUtils.isEmpty(gender))
        {
            params.put("gender", gender);
        }
        if (!TextUtils.isEmpty(favorable))
        {
            params.put("favorable", favorable);
        }
    }
}
