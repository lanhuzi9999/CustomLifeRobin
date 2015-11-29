package so.contacts.hub.basefunction.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import so.contacts.hub.basefunction.net.bean.BaseOldRequestData;
import so.contacts.hub.basefunction.net.bean.BaseRequestData;
import so.contacts.hub.basefunction.net.manager.NetDisposetUtils;
import android.R.integer;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * ****************************************************************
 * 文件名称 : BaseOldHttpRequest.java
 * 作 者 :   Robin
 * 创建时间 : 2015-11-28 下午6:44:29
 * 文件描述 : 改造替换之前的IgnitedHttpRequest为volley请求
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-28 1.00 初始版本
 *****************************************************************
 */
public class BaseOldHttpRequest extends Request<String>
{
    /**
     * request 请求成功回调接口
     */
    protected Listener<String> mListener;
    
    /**
     * request 请求数据类实体类
     */
    @SuppressWarnings("rawtypes")
    protected BaseOldRequestData mBaseRequestData;
    
    /**
     * request 请求字符串
     */
    protected String mQueryStr;
    
    /**
     * request 请求头文件信息
     */
    protected Map<String, String> mDefaultHeaders = new HashMap<String, String>();
    
    /**
     * Charset for request.
     */
    protected static final String PROTOCOL_CHARSET = "utf-8";
    
    /**
     * Content type for request.
     */
    public static final String JSON_PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s",
            PROTOCOL_CHARSET);

    public static final String URL_PROTOCOL_CONTENT_TYPE = String.format(
            "application/x-www-form-urlencoded; charset=%s", PROTOCOL_CHARSET);
    
    public BaseOldHttpRequest(int method, String url, String queryStr, Listener<String> listener,
            ErrorListener errorListener)
    {
        super(method, url, errorListener);
        mQueryStr = queryStr;
        mListener = listener;
        processHeaderInfo();
    }

    @SuppressWarnings("rawtypes")
    public BaseOldHttpRequest(int method, String url, BaseOldRequestData requestData, Listener<String> listener,
            ErrorListener errorListener)
    {
        super(method, url, errorListener);
        mBaseRequestData = requestData;
        mListener = listener;
        processHeaderInfo();
    }

    /**
     * 回调解析之后的数据
     */
    @Override
    protected void deliverResponse(String response)
    {
        mListener.onResponse(response);
    }

    /**
     * 解析数据，把网络请求到的或者缓存中的数据，解析成String
     */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response)
    {
//        String parsedStr = "";
//        try
//        {
//            String type = response.headers.get(HTTP.CONTENT_TYPE);
//            if (type == null)
//            {
//                type = PROTOCOL_CHARSET;
//                response.headers.put(HTTP.CONTENT_TYPE, type);
//            }
//            else if (!type.contains(PROTOCOL_CHARSET))
//            {
//                type += ";" + PROTOCOL_CHARSET;
//                response.headers.put(HTTP.CONTENT_TYPE, type);
//            }
//            parsedStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//        }
//        catch (UnsupportedEncodingException e)
//        {
//            parsedStr = new String(response.data);
//            e.printStackTrace();
//        }
//        return Response.success(parsedStr, HttpHeaderParser.parseCacheHeaders(response));
        return Response.success(NetDisposetUtils.getRealString(response.data),
                HttpHeaderParser.parseCacheHeaders(response));
    }

    /**
     * 处理头文件信息，放在子类中需要的时候实现
     * void
     */
    protected void processHeaderInfo()
    {
        setHeaders("Accept-Encoding", "gzip");
        setHeaders("Content-Type", "application/json");
    }
    
    /**
     * 
     * 设置请求头文件信息
     * @param keyStr
     * @param valueStr
     * void
     */
    public void setHeaders(String keyStr, String valueStr)
    {
        mDefaultHeaders.put(keyStr, valueStr);
    }
    /**
     * 重写获取volley请求头部信息
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        return mDefaultHeaders;
    }
    
//    @Override
//    public byte[] getBody() throws AuthFailureError
//    {
//        String contentType = mDefaultHeaders.get(HTTP.CONTENT_TYPE);
//        if (contentType.equals(URL_PROTOCOL_CONTENT_TYPE))
//        {
//            return super.getBody();
//        }
//        else
//        {
//            try
//            {
//                return mQueryStr.getBytes(JSON_PROTOCOL_CONTENT_TYPE);
//            }
//            catch (UnsupportedEncodingException e)
//            {
//                e.printStackTrace();
//                return mQueryStr.getBytes();
//            }
//        }
//    }
    
    @Override
    public byte[] getBody() throws AuthFailureError
    {
//        if (mBaseRequestData != null && mBaseRequestData.getData() != null)
//        {
//            try
//            {
//                return EntityUtils.toByteArray(mBaseRequestData.getData());
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        }
        try
        {
            return mBaseRequestData == null? null:mBaseRequestData.getJsonData().getBytes(HTTP.UTF_8);
        }
        catch (UnsupportedEncodingException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        if (!TextUtils.isEmpty(mQueryStr))
        {
            try
            {
                return EntityUtils.toByteArray(new StringEntity(mQueryStr, HTTP.UTF_8));
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return super.getBody();
    }
}
