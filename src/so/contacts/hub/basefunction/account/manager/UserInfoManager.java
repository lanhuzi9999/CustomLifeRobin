package so.contacts.hub.basefunction.account.manager;

import so.contacts.hub.basefunction.config.Config;
import so.contacts.hub.basefunction.net.bean.GetUserBasicInfoRequestData;
import so.contacts.hub.basefunction.net.manager.IResponse;
import so.contacts.hub.basefunction.net.manager.PTHTTPManager;
import android.content.Context;

public class UserInfoManager
{
    public static final String TAG = "UserInfoManager";

    private static volatile UserInfoManager mUserInfoManager;

    public UserInfoManager()
    {
    }

    public static UserInfoManager getInstance()
    {
        if (mUserInfoManager == null)
        {
            synchronized (UserInfoManager.class)
            {
                if (mUserInfoManager == null)
                {
                    mUserInfoManager = new UserInfoManager();
                }
            }
        }
        return mUserInfoManager;
    }
    
    /**
     * 从服务器获取用户基础数据
     * @param context
     * @param response
     * void
     */
    public void getUserBasicData(Context context, IResponse cb)
    {
        GetUserBasicInfoRequestData requestData = new GetUserBasicInfoRequestData(context);
        PTHTTPManager.getHttp().asynGet(Config.GET_BASIC_INFO_URL, requestData, cb);
    }
}
