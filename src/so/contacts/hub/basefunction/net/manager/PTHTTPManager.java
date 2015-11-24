package so.contacts.hub.basefunction.net.manager;

import android.net.wifi.WifiConfiguration.Status;


/**
 * ****************************************************************
 * 文件名称 : PTHTTPManager.java
 * 作 者 :   Robin
 * 创建时间 : 2015-11-7 下午2:34:26
 * 文件描述 : http请求管理类，业务中所有与后台交互都要使用该类来获取请求接口
 * http请求分为两类：普通葡萄http请求，和cms数据http请求
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-7 1.00 初始版本
 *****************************************************************
 */
public class PTHTTPManager
{
    // 请求类型：普通葡萄http请求
    public static final int PT_HTTP_IMPL = 0;

    // 请求类型：cms http请求
    public static final int CMS_HTTP_IMPL = 1;

    private static IPTHTTP mHttp;

    private static IPTHTTP mCmsHttp;

    public static IPTHTTP getHttp()
    {
        if (mHttp == null)
        {
            mHttp = new PTHTTPImpl();
        }
        return mHttp;
    }

    public static IPTHTTP getCmsHttp()
    {
        if (mCmsHttp == null)
        {
            mCmsHttp = new PTHTTPImpl(CMS_HTTP_IMPL);
        }
        return mCmsHttp;
    }
}
