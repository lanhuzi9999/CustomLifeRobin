package so.contacts.hub.basefunction.account;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.lives.depend.utils.LogUtil;

import so.contacts.hub.basefunction.account.bean.PTUser;
import so.contacts.hub.basefunction.config.Config;
import so.contacts.hub.basefunction.net.bean.CheckCaptchaRequestData;
import so.contacts.hub.basefunction.net.bean.CheckCaptchaResponseData;
import so.contacts.hub.basefunction.net.bean.GetCaptchaRequestData;
import so.contacts.hub.basefunction.net.bean.GetCaptchaResponseData;
import so.contacts.hub.basefunction.net.bean.LoginByPasswordRequestData;
import so.contacts.hub.basefunction.net.bean.LoginByPasswordResponseData;
import so.contacts.hub.basefunction.net.bean.ResetPasswordRequestData;
import so.contacts.hub.basefunction.net.bean.ResetPasswordResponseData;
import so.contacts.hub.basefunction.net.bean.VerifyCaptchaRequestData;
import so.contacts.hub.basefunction.net.bean.VerifyCaptchaResponseData;
import so.contacts.hub.basefunction.net.manager.PTHTTPManager;
import so.contacts.hub.basefunction.storage.sharedprefrences.PrefConstants;
import so.contacts.hub.basefunction.storage.sharedprefrences.SharedPreManager;
import so.contacts.hub.basefunction.utils.HexUtil;
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
public class AccountImpl implements IAccountAction
{
    private static final String TAG = "PutaoAccountImpl";

    private PTUser mPtUser;

    private IAccCallback mIAccCallback;

    /** 账号变更接口list，用于账号变更时逐一通知做变更处理 */
    private List<IAccChangeListener> mAccChangeListeners = new ArrayList<IAccChangeListener>();

    private static final int MSG_LOGIN_SUCCESS = 100;

    private static final int MSG_LOGIN_FAIL = 101;

    private static final int MSG_LOGIN_CANCEL = 102;

    private static final int MSG_NORMAL_SUCCESS = 103;

    private static final int MSG_NORMAL_FAIL = 104;

    /** 请求失败fail的错误标识码 */
    private int mErrorCode = -1;

    public AccountImpl()
    {

    }

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
                    loginSuccess();
                    break;
                case MSG_LOGIN_FAIL:
                    String errorMsg = "";
                    mErrorCode = msg.arg1;
                    if (msg.obj != null)
                    {
                        errorMsg = (String) msg.obj;
                    }
                    if (mIAccCallback != null)
                    {
                        mIAccCallback.onFail(mErrorCode, errorMsg);
                    }
                    mErrorCode = -1;
                    break;
                case MSG_LOGIN_CANCEL:
                    if (mIAccCallback != null)
                    {
                        mIAccCallback.onCancel();
                    }
                    break;
                case MSG_NORMAL_SUCCESS:
                    if (mIAccCallback != null)
                    {
                        mIAccCallback.onSuccess();
                    }
                    break;
                case MSG_NORMAL_FAIL:
                    String normalMsg = "";
                    mErrorCode = msg.arg1;
                    if (msg.obj != null)
                    {
                        normalMsg = (String) msg.obj;
                    }
                    if (mIAccCallback != null)
                    {
                        mIAccCallback.onFail(mErrorCode, normalMsg);
                    }
                    mErrorCode = -1;
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
    public PTUser loadPtuser()
    {
        String ptUser = SharedPreManager.getInstance().getString(false, PrefConstants.PrefAccountTable.TABLE_NAME,
                PrefConstants.PrefAccountTable.KEY_PT_USER, null);
        if (!TextUtils.isEmpty(ptUser))
        {
            mPtUser = new PTUser(ptUser);
        }
        return mPtUser;
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
        PTUser newPtUser = new PTUser(content);
        PTUser curUser = loadPtuser();
        LogUtil.d(TAG, "curUser : " + curUser);
        if (curUser != null && !TextUtils.isEmpty(curUser.getPt_uid()) && !curUser.getPt_uid().equals(newPtUser.pt_uid))
        {
            onAccoutChange();
        }

        // 保存
        SharedPreManager.getInstance().putString(false, PrefConstants.PrefAccountTable.TABLE_NAME,
                PrefConstants.PrefAccountTable.KEY_PT_USER, content);
        mPtUser = newPtUser;
    }

    /**
     * 
     * 账号变更 void
     */
    private void onAccoutChange()
    {
        if (mAccChangeListeners == null || mAccChangeListeners.size() <= 0)
        {
            return;
        }
        for (int i = 0; i < mAccChangeListeners.size(); i++)
        {
            IAccChangeListener l = mAccChangeListeners.get(i);
            l.onChange();
        }
    }

    private void loginSuccess()
    {
        Config.execute(new Runnable()
        {
            @Override
            public void run()
            {
                LogUtil.d(TAG, "handle MSG_HANLE_LOGIN_SUCCESS start");
                if (mAccChangeListeners == null || mAccChangeListeners.size() <= 0)
                {
                    return;
                }
                for (int i = 0; i < mAccChangeListeners.size(); i++)
                {
                    IAccChangeListener l = mAccChangeListeners.get(i);
                    l.onLogin();
                }
                LogUtil.d(TAG, "handle MSG_HANLE_LOGIN_SUCCESS end");
            }
        });
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
                if (response != null)
                {
                    if (response.isSuccess())
                    {
                        // 清除账户信息
                        cleanPtUser(PrefConstants.PrefAccountTable.TABLE_NAME,
                                PrefConstants.PrefAccountTable.KEY_PT_USER);
                        // 保存最新的账户信息
                        savePtUser(content);
                        // 通知主线程，登录成功
                        sendMainLoopMessage(MSG_LOGIN_SUCCESS, -1, null);
                    }
                    else if (CheckCaptchaResponseData.LOGIN_FAILED_CODE_TIMES_LIMIT.equals(response.ret_code))
                    {
                        // 验证码输入次数过多
                        sendMainLoopMessage(MSG_LOGIN_FAIL, IAccCallback.LOGIN_FAILED_CODE_TIMES_LIMIT,
                                response.error_remark);
                    }
                    else
                    {
                        // 验证码不正确
                        sendMainLoopMessage(MSG_LOGIN_FAIL, IAccCallback.LOGIN_FAILED_CODE_CODE_WRONG,
                                response.error_remark);
                    }
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

                    if (responseData != null)
                    {
                        if (responseData.isSuccess())
                        {
                            // 请求成功还需要对返回码做分类处理
                            if (LoginByPasswordResponseData.LOGIN_OK_CODE.equals(responseData.login_code))
                            {
                                // 清除账户信息
                                cleanPtUser(PrefConstants.PrefAccountTable.TABLE_NAME,
                                        PrefConstants.PrefAccountTable.KEY_PT_USER);
                                // 保存最新的账户信息
                                savePtUser(content);
                                // 通知主线程，登录成功
                                sendMainLoopMessage(MSG_LOGIN_SUCCESS, -1, null);
                            }
                            else if (LoginByPasswordResponseData.USER_NOT_HAVE_PASSWORD.equals(responseData.login_code))
                            {
                                sendMainLoopMessage(MSG_LOGIN_FAIL, IAccCallback.LOGIN_FAILED_CODE_NO_PASSWORD,
                                        responseData.error_remark);
                            }
                        }
                        else
                        {
                            mainHandler.sendEmptyMessage(MSG_LOGIN_FAIL);
                            sendMainLoopMessage(MSG_LOGIN_FAIL, IAccCallback.LOGIN_FAILED_CODE_CODE_WRONG,
                                    responseData.error_remark);
                        }
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

    @Override
    public void registerAccChangeListener(IAccChangeListener listener)
    {
        if (!mAccChangeListeners.contains(listener))
        {
            mAccChangeListeners.add(listener);
        }
    }

    @Override
    public void unregisterAccChangeListener(IAccChangeListener listener)
    {
        if (mAccChangeListeners != null && mAccChangeListeners.contains(listener))
        {
            mAccChangeListeners.remove(listener);
        }
    }

    @Override
    public void verifyCaptchar(Context context, final String actionCode, final String accName, final String checkCode,
            final IAccCallback cb)
    {
        mIAccCallback = cb;
        Config.execute(new Runnable()
        {
            @Override
            public void run()
            {
                VerifyCaptchaRequestData requestData = new VerifyCaptchaRequestData(actionCode, checkCode, accName);

                String content = PTHTTPManager.getHttp().syncPostString(Config.SERVER, requestData);

                VerifyCaptchaResponseData response = requestData.getObject(content);
                if (response != null)
                {
                    if (response.isSuccess())
                    {
                        // 请求成功
                        if (VerifyCaptchaResponseData.CHECK_CODE_SUCCESS.equals(response.code))
                        {
                            // 验证码正确
                            sendMainLoopMessage(MSG_NORMAL_SUCCESS, -1, null);
                        }
                        else if (VerifyCaptchaResponseData.CHECK_NOT_AUTH_USER.equals(response.code))
                        {
                            // 非鉴权用户
                            sendMainLoopMessage(MSG_NORMAL_FAIL, IAccCallback.LOGIN_FAILED_CODE_NO_ACCOUNT,
                                    response.error_remark);
                        }
                        else if (VerifyCaptchaResponseData.CHECK_CODE_IS_FAILED.equals(response.code))
                        {
                            // 验证码错误
                            sendMainLoopMessage(MSG_NORMAL_FAIL, IAccCallback.LOGIN_FAILED_CODE_CODE_WRONG,
                                    response.error_remark);
                        }
                        else if (CheckCaptchaResponseData.LOGIN_FAILED_CODE_TIMES_LIMIT.equals(response.code))
                        {
                            // 输入验证码次数超过三次
                            sendMainLoopMessage(MSG_NORMAL_FAIL, IAccCallback.LOGIN_FAILED_CODE_TIMES_LIMIT,
                                    response.error_remark);
                        }
                        else
                        {
                            // 服务端异常
                            sendMainLoopMessage(MSG_NORMAL_FAIL, IAccCallback.LOGIN_FAILED_CODE_SERVER_EXCEPTION,
                                    response.error_remark);
                        }
                    }
                    else
                    {
                        // 服务端异常
                        sendMainLoopMessage(MSG_NORMAL_FAIL, IAccCallback.LOGIN_FAILED_CODE_SERVER_EXCEPTION,
                                response.error_remark);
                    }
                }
            }
        });
    }

    @Override
    public void resetPasswordByCaptchar(Context context, final String accName, final String passWord,
            final String captchar, IAccCallback cb)
    {
        mIAccCallback = cb;
        Config.execute(new Runnable()
        {
            @Override
            public void run()
            {
                String encryPwdStr = "";
                try
                {
                    encryPwdStr = encryptPassword(passWord);
                }
                catch (NoSuchAlgorithmException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                ResetPasswordRequestData requestData = new ResetPasswordRequestData(accName, encryPwdStr, captchar);
                String content = PTHTTPManager.getHttp().syncPostString(Config.SERVER, requestData);
                ResetPasswordResponseData responseData = requestData.getObject(content);
                if (responseData != null)
                {
                    if (responseData.isSuccess())
                    {
                        if (ResetPasswordResponseData.SET_PASSWORD_SUCCESS_CODE.equals(responseData.code))
                        {
                            // 修改密码成功
                            sendMainLoopMessage(MSG_NORMAL_SUCCESS, -1, null);
                        }
                        else if (ResetPasswordResponseData.CHECK_CODE_IS_FAILED.equals(responseData.code))
                        {
                            // 验证码错误
                            sendMainLoopMessage(MSG_NORMAL_FAIL, IAccCallback.LOGIN_FAILED_CODE_CODE_WRONG,
                                    responseData.error_remark);
                        }
                        else if (ResetPasswordResponseData.CHECK_NOT_AUTH_USER.equals(responseData.code))
                        {
                            // 非鉴权用户
                            sendMainLoopMessage(MSG_NORMAL_FAIL, IAccCallback.LOGIN_FAILED_CODE_NO_ACCOUNT,
                                    responseData.error_remark);
                        }
                        else if (ResetPasswordResponseData.USER_NOT_EXISTS_CODE.equals(responseData.code))
                        {
                            // 非鉴权用户
                            sendMainLoopMessage(MSG_NORMAL_FAIL, IAccCallback.LOGIN_FAILED_CODE_NO_ACCOUNT,
                                    responseData.error_remark);
                        }
                        else
                        {
                            // 服务端异常
                            sendMainLoopMessage(MSG_NORMAL_FAIL, IAccCallback.LOGIN_FAILED_CODE_SERVER_EXCEPTION,
                                    responseData.error_remark);
                        }
                    }
                    else
                    {
                        sendMainLoopMessage(MSG_NORMAL_FAIL, IAccCallback.LOGIN_FAILED_CODE_SERVER_EXCEPTION,
                                responseData.error_remark);
                    }
                }
            }
        });
    }

    /**
     * 向主线程发送消息
     * 
     * @param what
     * @param obj void
     */
    private void sendMainLoopMessage(int what, int errorCode, Object obj)
    {
        Message message = new Message();
        message.what = what;
        message.obj = obj;
        message.arg1 = errorCode;
        mainHandler.sendMessage(message);
    }
}
