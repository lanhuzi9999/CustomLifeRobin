package so.contacts.hub.basefunction.net.bean;

import java.util.Map;

import so.contacts.hub.basefunction.account.manager.AccountManager;
import so.contacts.hub.basefunction.utils.SystemUtil;

import android.content.Context;
import android.text.TextUtils;

public class GetUserBasicInfoRequestData extends BaseRequestData
{

    private Context context;

    private String token;

    private String app_id;

    private String app_version;

    private String brand;

    public GetUserBasicInfoRequestData(Context context)
    {
        this.context = context;
        token = AccountManager.getInstance().getPtToken();
        app_id = SystemUtil.getAppid(context);
        app_version = SystemUtil.getAppVersion(context);
        brand = android.os.Build.MODEL;
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
    }

}
