package so.contacts.hub.basefunction.account;

import android.content.Context;

/**
 * ****************************************************************
 * 文件名称 : IPutaoAccount.java
 * 作 者 :   Robin
 * 创建时间 : 2015-11-16 上午12:16:20
 * 文件描述 : 葡萄账户接口
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-16 1.00 初始版本
 *****************************************************************
 */
public interface IPutaoAccount
{

    public void sendCaptchar(Context context, String mobile, String actionCode, IAccCallbackAdv<String> cb);
}
