package so.contacts.hub.basefunction.account;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import so.contacts.hub.basefunction.account.bean.PTUser;
import so.contacts.hub.basefunction.config.Config;
import so.contacts.hub.basefunction.net.bean.CheckCaptchaRequestData;
import so.contacts.hub.basefunction.net.bean.CheckCaptchaResponseData;
import so.contacts.hub.basefunction.net.bean.GetCaptchaRequestData;
import so.contacts.hub.basefunction.net.bean.GetCaptchaResponseData;
import so.contacts.hub.basefunction.net.bean.LoginByPasswordRequestData;
import so.contacts.hub.basefunction.net.bean.LoginByPasswordResponseData;
import so.contacts.hub.basefunction.net.manager.PTHTTPManager;
import so.contacts.hub.basefunction.storage.sharedprefrences.PrefConstants;
import so.contacts.hub.basefunction.storage.sharedprefrences.SharedPreManager;
import so.contacts.hub.basefunction.utils.HexUtil;
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
public class AccountImpl implements IPutaoAccount
{
    private static final String TAG = "PutaoAccountImpl";

    private PTUser mPtUser;

    private IAccCallback mIAccCallback;

    public AccountImpl()
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
                    // 回调给验证码登录界面
                    if (mIAccCallback != null)
                    {
                        mIAccCallback.onSuccess();
                    }
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

    /**
     * 发送验证码接口
     */
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

                final GetCaptchaResponseData response = captchaRequestData.getObject(content);
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
    public void loginByCaptcha(Context context, final String accName, final int checkCode, final IAccCallback cb)
    {
        mIAccCallback = cb;
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
                    // 清除账户信息
                    cleanPtUser(PrefConstants.PrefAccountTable.TABLE_NAME, PrefConstants.PrefAccountTable.KEY_PT_USER);
                    // 保存最新的账户信息
                    savePtUser(content);
                    // 通知主线程，登录成功
                    mainHandler.sendEmptyMessage(MSG_LOGIN_SUCCESS);
                }
                else
                {

                }

            }
        });
    }

    /**
     * 获取葡萄token
     */
    @Override
    public String getPtToken()
    {
        loadPtuser();
        if (mPtUser != null && mPtUser.getPt_token() != null)
        {
            return mPtUser.getPt_token();
        }
        return "";
    }

    /**
     * 注销登陆
     */
    @Override
    public void logout(Context context)
    {
        cleanPtUser(PrefConstants.PrefAccountTable.TABLE_NAME, PrefConstants.PrefAccountTable.KEY_PT_USER);
        mPtUser = null;
    }

    /**
     * 账号密码登陆
     */
    @Override
    public void loginByPassword(Context context, final String accName, final String password, IAccCallback cb)
    {
        mIAccCallback = cb;
        Config.execute(new Runnable()
        {

            @Override
            public void run()
            {
                try
                {
                    LoginByPasswordRequestData requestData = new LoginByPasswordRequestData(accName,
                            encryptPassword(password));
                    String content = PTHTTPManager.getHttp().syncPostString(Config.SERVER, requestData);
                    LoginByPasswordResponseData responseData = requestData.getObject(content);

                    if (responseData != null && responseData.isSuccess())
                    {
                        // 清除账户信息
                        cleanPtUser(PrefConstants.PrefAccountTable.TABLE_NAME,
                                PrefConstants.PrefAccountTable.KEY_PT_USER);
                        // 保存最新的账户信息
                        savePtUser(content);
                        // 通知主线程，登录成功
                        mainHandler.sendEmptyMessage(MSG_LOGIN_SUCCESS);
                    }
                }
                catch (NoSuchAlgorithmException e)
                {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 对密码做加密处理，一次MD5加密，一次SHA1加密，再做一次MD5加密
     * 
     * @param password
     * @return
     * @throws NoSuchAlgorithmException String
     */
    private String encryptPassword(String password) throws NoSuchAlgorithmException
    {
        // 第一次做MD5转换,拿到MD5转换器
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        // 将输入字符串转换为字节数组
        byte[] inputByteArray = password.getBytes(Charset.forName("UTF-8"));
        // 转换并返回结果,得到第一次MD5转换的结果
        messageDigest.update(inputByteArray);
        byte[] resultByteArray = messageDigest.digest();

        // 第二次做SHA1转换,拿到SHA1转换器
        messageDigest = MessageDigest.getInstance("SHA1");
        messageDigest.update(resultByteArray);
        resultByteArray = messageDigest.digest();

        // 第三次再做MD5转换
        messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(resultByteArray);
        resultByteArray = messageDigest.digest();

        // 将最终的字节数组转成字符串返回
        return HexUtil.byteArrayToString(resultByteArray);
    }
}
