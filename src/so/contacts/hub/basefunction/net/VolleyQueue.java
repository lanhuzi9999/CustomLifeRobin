package so.contacts.hub.basefunction.net;

import so.contacts.hub.ContactsApp;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * ****************************************************************
 * 文件名称 : VolleyQueue.java
 * 作 者 :   Robin
 * 创建时间 : 2015-11-7 下午10:34:19
 * 文件描述 : volley请求队列(单例)
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-7 1.00 初始版本
 *****************************************************************
 */
public class VolleyQueue
{
    private static volatile RequestQueue mRequestQueue;

    public static RequestQueue getQueue()
    {
        if (mRequestQueue == null)
        {
            synchronized (VolleyQueue.class)
            {
                mRequestQueue = Volley.newRequestQueue(ContactsApp.getInstance());
            }
        }
        return mRequestQueue;
    }
}
