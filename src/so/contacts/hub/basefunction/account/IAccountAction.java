package so.contacts.hub.basefunction.account;

import so.contacts.hub.basefunction.account.bean.PTUser;
import android.content.Context;

/**
 * **************************************************************** 文件名称 :
 * IPutaoAccount.java 作 者 : Robin 创建时间 : 2015-11-16 上午12:16:20 文件描述 : 葡萄账户接口
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有 修改历史 : 2015-11-16 1.00 初始版本
 ***************************************************************** 
 */
public interface IAccountAction
{

    /**
     * 获取葡萄账号信息
     * 
     * @return 没有登陆的时候返回null PTUser
     */
    public PTUser getPtUser();

    /**
     * 获取葡萄token
     * 
     * @return String
     */
    public String getPtToken();

    /**
     * 请求服务器发送验证码到手机
     * 
     * @param context
     * @param mobile
     * @param actionCode 200001登录 200004设置密码
     * @param cb 回调到主线程，修改ui void
     */
    public void sendCaptchar(Context context, String mobile, String actionCode, IAccCallbackAdv<String> cb);

    /**
     * 
     * 验证手机号码登录
     * 
     * @param context
     * @param accName
     * @param checkCode
     * @param cb void
     */
    public void loginByCaptcha(Context context, String accName, int checkCode, IAccCallback cb);

    /**
     * 
     * 修改密码时获取验证码
     * @param context
     * @param accName
     * @param checkCode
     * @param cb
     * void
     */
    public void verifyCaptchar(Context context, String actionCode, String accName, String checkCode, IAccCallback cb);
    
    /**
     * 账号密码登陆
     * @param context
     * @param accName
     * @param password
     * @param cb
     * void
     */
    public void loginByPassword(Context context, String accName, String password, IAccCallback cb);
    
    /**
     * 注销登陆
     * 
     * @param context void
     */
    public void logout(Context context);
    
    /**
     * 添加账号状态变化监听器
     * @param listener
     * void
     */
    public void registerAccChangeListener(IAccChangeListener listener);
    
    /**
     * 移除账号状态变化监听器
     * @param listener
     * void
     */
    public void unregisterAccChangeListener(IAccChangeListener listener);
}
