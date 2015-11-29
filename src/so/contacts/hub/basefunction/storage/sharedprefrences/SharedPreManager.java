package so.contacts.hub.basefunction.storage.sharedprefrences;

import java.util.HashMap;
import java.util.Map;

import so.contacts.hub.ContactsApp;

import android.R.bool;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

/**
 * **************************************************************** 文件名称 :
 * SharedPreManager.java 作 者 : Administrator 创建时间 : 2015-11-26 下午8:40:36 文件描述 :
 * sharedprefrences管理类 版权声明 : 深圳市葡萄信息技术有限公司 版权所有 修改历史 : 2015-11-26 1.00 初始版本
 ***************************************************************** 
 */
public class SharedPreManager
{
    private static volatile SharedPreManager mSharedPreManager;

    private SharedPreferences mDefaultSharedPreferences;

    private Map<String, SharedPreferences> mSharedMap = new HashMap<String, SharedPreferences>();

    public SharedPreManager()
    {

    }

    public static SharedPreManager getInstance()
    {
        if (mSharedPreManager == null)
        {
            synchronized (SharedPreManager.class)
            {
                if (mSharedPreManager == null)
                {
                    mSharedPreManager = new SharedPreManager();
                }
            }
        }
        return mSharedPreManager;
    }

    public SharedPreferences getSharedPreferences(String fileName)
    {
        if (TextUtils.isEmpty(fileName))
        {
            if (mDefaultSharedPreferences == null)
            {
                mDefaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ContactsApp.getInstance());
            }
            return mDefaultSharedPreferences;
        }

        SharedPreferences sp = mSharedMap.get(fileName);
        if (sp == null)
        {
            sp = ContactsApp.getInstance().getSharedPreferences(fileName, Context.MODE_MULTI_PROCESS);
            mSharedMap.put(fileName, sp);
        }
        return sp;
    }

    public String getString(boolean isNeedEnc, String fileName, String key, String defaultvalue)
    {
        String result = getSharedPreferences(fileName).getString(key, "");
        if (TextUtils.isEmpty(result))
        {
            result = defaultvalue;
        }
        return result;
    }

    public void putString(boolean isNeedEnc, String fileName, String key, String value)
    {
        getSharedPreferences(fileName).edit().putString(key, value).commit();
    }
    
    public boolean remove(String fileName, String key)
    {
        return getSharedPreferences(fileName).edit().remove(key).commit();
    }
}
