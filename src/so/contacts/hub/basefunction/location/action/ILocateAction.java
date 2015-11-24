package so.contacts.hub.basefunction.location.action;

import so.contacts.hub.basefunction.location.listener.LBSServiceListener;
import android.content.Context;

/**
 * ****************************************************************
 * 文件名称 : ILocateAction.java
 * 作 者 :   Administrator
 * 创建时间 : 2015-9-7 下午12:06:31
 * 文件描述 : 定位接口
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-9-7 1.00 初始版本
 *****************************************************************
 */
public interface ILocateAction
{
    public void startLocation(Context context, LBSServiceListener listener);

    public void deactivate();
}
