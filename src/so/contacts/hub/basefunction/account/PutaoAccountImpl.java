package so.contacts.hub.basefunction.account;

import so.contacts.hub.ContactsApp;
import so.contacts.hub.basefunction.account.bean.PTUser;
import so.contacts.hub.basefunction.config.Config;
import so.contacts.hub.basefunction.storage.sharedprefrences.PrefConstants;
import so.contacts.hub.basefunction.storage.sharedprefrences.SharedPreManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

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

    @Override
    public void sendCaptchar(Context context, String mobile, String actionCode, IAccCallbackAdv<String> cb)
    {
        Config.execute(new Runnable()
        {

            @Override
            public void run()
            {

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
}
