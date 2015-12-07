package so.contacts.hub.basefunction.net;

import so.contacts.hub.basefunction.net.bean.BaseRequestData;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

/**
 * ****************************************************************
 * 文件名称 : PTRequest.java
 * 作 者 :   Robin Pei
 * 创建时间 : 2015-12-7 下午7:02:08
 * 文件描述 : 葡萄volley数据请求类
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-12-7 1.00 初始版本
 *****************************************************************
 */
public class PTRequest extends BasePTStrRequest
{

    public PTRequest(int method, String url, BaseRequestData requestData, Listener<String> listener,
            ErrorListener errorListener)
    {
        super(method, url, requestData, listener, errorListener);
    }

    public PTRequest(int method, String url, String queryStr, Listener<String> listener, ErrorListener errorListener)
    {
        super(method, url, queryStr, listener, errorListener);
    }

    @Override
    protected void processHeaderInfo()
    {
        setHeaders("Accept-Encoding", "gzip");
        setHeaders("Content-Type", URL_PROTOCOL_CONTENT_TYPE);
        setHeaders("Accept", "version=1.0");
    }

}
