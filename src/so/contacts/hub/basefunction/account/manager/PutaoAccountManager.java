package so.contacts.hub.basefunction.account.manager;

import android.content.Context;
import so.contacts.hub.basefunction.account.IAccCallback;
import so.contacts.hub.basefunction.account.IAccCallbackAdv;
import so.contacts.hub.basefunction.account.IPutaoAccount;
import so.contacts.hub.basefunction.account.PutaoAccountImpl;
import so.contacts.hub.basefunction.account.bean.PTUser;

/**
 * ****************************************************************
 * 文件名称 : PutaoAccountManager.java
 * 作 者 :   Robin
 * 创建时间 : 2015-11-16 上午12:14:27
 * 文件描述 : 葡萄账户对外接口，也就是葡萄账户接口管理类
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-16 1.00 初始版本
 *****************************************************************
 */
public class PutaoAccountManager
{
    private volatile static PutaoAccountManager mInstance;

    private IPutaoAccount mPutaoAccount;
    /**
     * 单例，双重检查锁定
     * 
     * @return PutaoAccountManager
     */
    public static PutaoAccountManager getInstance()
    {
        if (mInstance == null)
        {
            synchronized (PutaoAccountManager.class)
            {
                if (mInstance == null)
                {
                    mInstance = new PutaoAccountManager();
                }
            }
        }
        return mInstance;
    }

    private PutaoAccountManager()
    {
        mPutaoAccount = new PutaoAccountImpl();
    }
    
    /**
     * 获取ptuser
     * @return
     * PTUser
     */
    public PTUser getPtUser()
    {
        return mPutaoAccount.getPtUser();
    }
    
    /**
     * 请求服务器发送验证码到相应手机号
     * @param context
     * @param mobile
     * @param actionCode
     * @param cb
     * void
     */
    public void sendCaptchar(Context context, String mobile, String actionCode, IAccCallbackAdv<String> cb)
    {
        mPutaoAccount.sendCaptchar(context, mobile, actionCode, cb);
    }
    
    public void loginByCaptcha(Context context, String accName, int checkCode, IAccCallback cb)
    {
        mPutaoAccount.loginByCaptcha(context, accName, checkCode, cb);
    }
}
