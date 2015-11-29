package so.contacts.hub.basefunction.account;

/**
 * ****************************************************************
 * 文件名称 : IAccCallback.java
 * 作 者 :   Robin
 * 创建时间 : 2015-11-16 上午12:38:21
 * 文件描述 : 账户登录回调接口
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-16 1.00 初始版本
 *****************************************************************
 */
public interface IAccCallback
{

    /**
     * 账号已存在
     */
    public static final int LOGIN_FAILED_CODE_HAS_BIND = 1001;

    /**
     * 服务端异常
     */
    public static final int LOGIN_FAILED_CODE_SERVER_EXCEPTION = 1002;
    
    /**
     * 登录成功
     * 方法表述
     * void
     */
    public void onSuccess();
    
    /**
     * 登录失败
     * 方法表述
     * @param failed_code
     * void
     */
    public void onFail(int failed_code);
    
    /**
     * 登录取消
     * 方法表述
     * void
     */
    public void onCancel();
}
