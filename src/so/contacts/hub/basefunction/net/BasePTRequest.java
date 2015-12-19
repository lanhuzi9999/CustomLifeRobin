package so.contacts.hub.basefunction.net;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.http.protocol.HTTP;

import so.contacts.hub.basefunction.net.bean.BaseRequestData;
import so.contacts.hub.basefunction.net.manager.NetDisposetUtils;
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
 * 文件名称 : BasePTStrRequest.java
 * 作 者 :   Robin
 * 创建时间 : 2015-11-7 下午4:36:38
 * 文件描述 : volley请求字符串数据的基类
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-7 1.00 初始版本
 *****************************************************************
 */
public abstract class BasePTRequest extends Request<String>
{
    /**
     * request 请求成功回调接口
     */
    protected Listener<String> mListener;
    
    /**
     * request 请求数据类实体类
     */
    protected BaseRequestData mBaseRequestData;
    
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
    
    public BasePTRequest(int method, String url, String queryStr, Listener<String> listener,
            ErrorListener errorListener)
    {
        super(method, url, errorListener);
        mQueryStr = queryStr;
        mListener = listener;
        processHeaderInfo();
    }

    public BasePTRequest(int method, String url, BaseRequestData requestData, Listener<String> listener,
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
    protected abstract void processHeaderInfo();
    
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

    @Override
    protected Map<String, String> getParams() throws AuthFailureError
    {
        if (mBaseRequestData == null)
        {
            return super.getParams();
        }
        Map<String, String> param = mBaseRequestData.getParams();
        if (param == null)
        {
            return null;
        }

        Map<String, String> newParam = new TreeMap<String, String>();
        for (Entry<String, String> entry : param.entrySet())
        {
            if (!TextUtils.isEmpty(entry.getValue()))
            {
                newParam.put(entry.getKey(), entry.getValue());
            }
        }
        return newParam;
    }

    @Override
    public byte[] getBody() throws AuthFailureError
    {
        String contentType = mDefaultHeaders.get(HTTP.CONTENT_TYPE);
        if (contentType.equals(URL_PROTOCOL_CONTENT_TYPE))
        {
            return super.getBody();
        }
        else
        {
            try
            {
                return mQueryStr.getBytes(JSON_PROTOCOL_CONTENT_TYPE);
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
                return mQueryStr.getBytes();
            }
        }
    }
}
