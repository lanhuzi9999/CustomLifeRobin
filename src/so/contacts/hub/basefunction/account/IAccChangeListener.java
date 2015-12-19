package so.contacts.hub.basefunction.account;

/**
 * ****************************************************************
 * 文件名称 : IAccChangeListener.java
 * 作 者 :   Robin
 * 创建时间 : 2015-12-18 上午10:23:30
 * 文件描述 : 账号变更接口
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-12-18 1.00 初始版本
 *****************************************************************
 */
public interface IAccChangeListener
{
    /**
     * 用户登录 void
     */
    public void onLogin();

    /**
     * 用户退出 void
     */
    public void onLogout();

    /**
     * 账号变更 void
     */
    public void onChange();

}
