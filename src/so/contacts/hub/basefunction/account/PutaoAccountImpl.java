package so.contacts.hub.basefunction.account;

import so.contacts.hub.basefunction.account.bean.PTUser;
import so.contacts.hub.basefunction.config.Config;
import so.contacts.hub.basefunction.net.bean.CheckCaptchaRequestData;
import so.contacts.hub.basefunction.net.bean.CheckCaptchaResponseData;
import so.contacts.hub.basefunction.net.bean.GetCaptchaRequestData;
import so.contacts.hub.basefunction.net.bean.GetCaptchaResponse;
import so.contacts.hub.basefunction.net.manager.PTHTTPManager;
import so.contacts.hub.basefunction.storage.sharedprefrences.PrefConstants;
import so.contacts.hub.basefunction.storage.sharedprefrences.SharedPreManager;
import android.R.integer;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

/**
 * **************************************************************** 文件名称 :
 * PutaoAccountImpl.java 作 者 : Robin 创建时间 : 2015-11-16 上午12:17:14 文件描述 :
 * 葡萄账户接口具体实现类 版权声明 : 深圳市葡萄信息技术有限公司 版权所有 修改历史 : 2015-11-16 1.00 初始版本
 ***************************************************************** 
 */
public class PutaoAccountImpl implements IPutaoAccount
{
    private static final String TAG = "PutaoAccountImpl";

    private PTUser mPtUser;

    public PutaoAccountImpl()
    {

    }

    private static final int MSG_LOGIN_SUCCESS = 100;

    private static final int MSG_LOGIN_FAIL = 101;

    private static final int MSG_LOGIN_CANCEL = 102;

    private Handler mainHandler = new Handler(Looper.getMainLooper())
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MSG_LOGIN_SUCCESS:

                    break;
                case MSG_LOGIN_FAIL:

                    break;
                case MSG_LOGIN_CANCEL:

                    break;
                default:
                    break;
            }
        };
    };

    @Override
    public void sendCaptchar(final Context context, final String mobile, final String actionCode,
            final IAccCallbackAdv<String> cb)
    {
        Config.execute(new Runnable()
        {

            @Override
            public void run()
            {
                GetCaptchaRequestData captchaRequestData = new GetCaptchaRequestData(actionCode, mobile);
                String content = PTHTTPManager.getHttp().syncPostString(Config.SERVER, captchaRequestData);

                final GetCaptchaResponse response = captchaRequestData.getObject(content);
                if (response != null && response.isSuccess())
                {
                    mainHandler.post(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            cb.onSuccess(response.send_num);
                        }
                    });
                }
                else
                {
                    mainHandler.post(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            cb.onFail(IAccCallback.LOGIN_FAILED_CODE_SERVER_EXCEPTION);
                        }
                    });
                }
            }
        });
    }

    @Override
    public PTUser getPtUser()
    {
        if (mPtUser == null)
        {
            String ptUser = SharedPreManager.getInstance().getString(false, PrefConstants.PrefAccountTable.TABLE_NAME,
                    PrefConstants.PrefAccountTable.KEY_PT_USER, null);
            if (!TextUtils.isEmpty(ptUser))
            {
                mPtUser = new PTUser(ptUser);
            }
        }
        return mPtUser;
    }

    /**
     * 加载葡萄用户信息 void
     */
    public void loadPtuser()
    {
        String ptUser = SharedPreManager.getInstance().getString(false, PrefConstants.PrefAccountTable.TABLE_NAME,
                PrefConstants.PrefAccountTable.KEY_PT_USER, null);
        if (!TextUtils.isEmpty(ptUser))
        {
            mPtUser = new PTUser(ptUser);
        }
    }

    /**
     * 保存账户信息
     * 
     * @param content void
     */
    public void savePtUser(String content)
    {
        if (TextUtils.isEmpty(content))
        {
            return;
        }
        // 保存
        SharedPreManager.getInstance().putString(false, PrefConstants.PrefAccountTable.TABLE_NAME,
                PrefConstants.PrefAccountTable.KEY_PT_USER, content);
    }

    /**
     * 清除账户信息
     * 
     * @param context void
     */
    public void cleanPtUser(String fileName, String key)
    {
        SharedPreManager.getInstance().remove(fileName, key);
    }

    @Override
    public void loginByCaptcha(Context context, final String accName, final int checkCode, IAccCallback cb)
    {
        Config.execute(new Runnable()
        {

            @Override
            public void run()
            {
                CheckCaptchaRequestData requestData = new CheckCaptchaRequestData(accName, checkCode);

                String content = PTHTTPManager.getHttp().syncPostString(Config.SERVER, requestData);

                CheckCaptchaResponseData response = requestData.getObject(content);
                if (response != null && response.isSuccess())
                {
                    Log.d("PutaoAccountImpl", "pxy--" + response.toString());
                }
            }
        });
    }
}
