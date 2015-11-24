package so.contacts.hub.basefunction.account;

/**
 * ****************************************************************
 * 文件名称 : IAccCallbackAdv.java
 * 作 者 :   Robin
 * 创建时间 : 2015-11-16 上午12:34:37
 * 文件描述 : 获取验证码的回调接口
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-16 1.00 初始版本
 *****************************************************************
 */
public interface IAccCallbackAdv<T>
{
    /**
     * 获取验证码成功
     * 方法表述
     * @param t
     * void
     */
    public void onSuccess(T t);

    /**
     * 获取验证码失败
     * 方法表述
     * @param failed_code
     * void
     */
    public void onFail(int failed_code);

    /**
     * 取消掉了
     * 方法表述
     * void
     */
    public void onCancel();
}
