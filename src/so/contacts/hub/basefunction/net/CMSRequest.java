package so.contacts.hub.basefunction.net;

import so.contacts.hub.basefunction.net.bean.BaseRequestData;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

/**
 * ****************************************************************
 * 文件名称 : CMSRequest.java
 * 作 者 :   Robin
 * 创建时间 : 2015-11-7 下午9:59:33
 * 文件描述 : cms数据请求类
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-7 1.00 初始版本
 *****************************************************************
 */
public class CMSRequest extends BasePTRequest
{
    public CMSRequest(int method, String url, BaseRequestData requestData, Listener<String> listener,
            ErrorListener errorListener)
    {
        super(method, url, requestData, listener, errorListener);
    }

    public CMSRequest(int method, String url, String queryStr, Listener<String> listener, ErrorListener errorListener)
    {
        super(method, url, queryStr, listener, errorListener);
    }

    @Override
    protected void processHeaderInfo()
    {
        setHeaders("Accept-Encoding", "gzip,deflate");
        setHeaders("Accept-Language", "en-US");
        setHeaders("Accept", "version=1.0");
    }

}
