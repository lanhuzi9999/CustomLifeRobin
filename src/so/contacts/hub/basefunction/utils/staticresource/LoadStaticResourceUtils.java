package so.contacts.hub.basefunction.utils.staticresource;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import com.lives.depend.utils.LogUtil;
import com.qiniu.android.utils.Etag;

import android.text.TextUtils;

import so.contacts.hub.ContactsApp;
import so.contacts.hub.basefunction.MD5.MD5;
import so.contacts.hub.basefunction.config.Config;
import so.contacts.hub.basefunction.storage.sharedprefrences.PrefConstants;
import so.contacts.hub.basefunction.storage.sharedprefrences.SharedPreManager;
import so.contacts.hub.basefunction.utils.ConstantsParameter;

public class LoadStaticResourceUtils
{
    private static final String TAG = "LoadStaticResourceUtils";

    /**
     * 更新静态数据的频率，现在周期设为一天
     */
    private static final long TIME_UPDATE_STATIC = 1000; //24 * 60 * 60 * 1000

    private static final int STATIC_RES_REQ_SUCCESS = 200;

    private static final int STATIC_RES_REQ_FAIL = 304;

    public static void initStaticYellowPageData()
    {
        long lastTime = SharedPreManager.getInstance().getLong(PrefConstants.StaticResourceUpdateTable.TABLE_NAME,
                PrefConstants.StaticResourceUpdateTable.KEY_STATIC_RESOURCE_UPDATE, 0);
        if (System.currentTimeMillis() - lastTime < TIME_UPDATE_STATIC)
        {
            return;
        }
        // 城市列表
        updateStaticResource(Config.URL_GET_STATIC_FILE + Config.KEY_CITY_LIST_NEW,
                StaticResourceFactory.TYPE_CITY_LIST);
        // 将本次更新静态数据的时间保存
        SharedPreManager.getInstance().putLong(PrefConstants.StaticResourceUpdateTable.TABLE_NAME,
                PrefConstants.StaticResourceUpdateTable.KEY_STATIC_RESOURCE_UPDATE, System.currentTimeMillis());
    }

    /**
     * 从网络获取静态数据资源
     * 
     * @param string
     * @param i void
     */
    private static void updateStaticResource(String url, int type)
    {
        InputStream inputStream = null;
        HttpResponse response = null;
        // url做MD5加密，稍后etag会用到，保证etag的唯一性
        String md5Url = MD5.toMD5(url);
        // 新建HttpClient对象
        HttpClient httpClient = new DefaultHttpClient();
        // 设置连接超时
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 5000);
        // 设置数据读取时间超时
        HttpConnectionParams.setSoTimeout(httpClient.getParams(), 5000);
        // 设置从连接池中取连接超时
        ConnManagerParams.setTimeout(httpClient.getParams(), 5000);
        // 获取请求
        HttpGet httpget = new HttpGet(url);
        // 设置请求头信息
//        disposeRequestHeader(httpget, url, md5Url);
        try
        {
            response = httpClient.execute(httpget); // 执行请求，获取响应结果
            if (response != null && response.getStatusLine() != null
                    && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                // 响应通过
                inputStream = response.getEntity().getContent();
                // 在这里需要保存从服务端传过来的etag和Last-Modified信息
                saveEtag(response, md5Url);
            }
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            int statusCode = 0;
            if (response != null && response.getStatusLine() != null)
            {
                statusCode = response.getStatusLine().getStatusCode();
            }
            // 将inputstream读取存入数据库
            disposeReqResult(inputStream, statusCode, type);
        }
    }

    /**
     * 设置处理请求头部信息：主要是last modified和etag
     * 
     * @param httpget
     * @param url
     * @param md5Url void
     */
    private static void disposeRequestHeader(HttpGet httpget, String url, String md5Url)
    {
        if (httpget == null)
        {
            return;
        }
        /**
         * 从sp获取上次请求保存的etag信息，如果有设置请求头信息，如果没有获取本地文件etag并设置请求头信息
         */
        String headerInfo = SharedPreManager.getInstance().getString(false,
                PrefConstants.StaticResourceUpdateTable.TABLE_NAME, md5Url, "");
        String[] headerList = null;
        String etag = null;
        if (!TextUtils.isEmpty(headerInfo))
        {
            if (headerInfo.contains(ConstantsParameter.SHAREDPREFERENCES_DATA_DELIMITER))
            {
                headerList = headerInfo.split(ConstantsParameter.SHAREDPREFERENCES_DATA_DELIMITER);
                httpget.setHeader("If-Modified-Since", headerList[0]);
                httpget.setHeader("If-None-Match", headerList[1]);
            }
            else
            {
                httpget.setHeader("If-None-Match", headerInfo);
            }
        }
        else
        {
            // 获取本地文件的etag信息
            // 根据url获取文件名字
            String fileName = "";
            int index = url.lastIndexOf("=");
            if (index != -1)
            {
                fileName = url.substring(index + 1);
            }
            // 获取本地文件的etag
            etag = getEtag(fileName);
            httpget.setHeader("If-None-Match", etag);
            SharedPreManager.getInstance().putString(false, PrefConstants.StaticResourceUpdateTable.TABLE_NAME, md5Url,
                    etag);
        }
    }

    /**
     * 获取本地文件的etag
     * 
     * @param fileName
     * @return String
     */
    private static String getEtag(String fileName)
    {
        InputStream open = null;
        String etg = "";
        try
        {
            open = ContactsApp.getInstance().getAssets().open(fileName);
            int available = open.available();
            etg = Etag.stream(open, available);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        finally
        {
            if (open != null)
            {
                try
                {
                    open.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        // 加上双引号
        if (!TextUtils.isEmpty(etg))
        {
            etg = "\"" + etg + "\"";
        }
        return etg;
    }

    /**
     * 根据type处理请求结果
     * 
     * @param inputStream
     * @param type void
     */
    private static void disposeReqResult(InputStream inputStream, int statusCode, int type)
    {
        // 根据请求结果的状态码,决定记载写入网络数据还是本地数据
        LogUtil.d(TAG, "disposeReqResult statusCode:" + statusCode);
        switch (statusCode)
        {
            case STATIC_RES_REQ_SUCCESS:
                StaticResourceFactory.putNetResDataToDb(inputStream, type);
                break;
            case STATIC_RES_REQ_FAIL:
                StaticResourceFactory.putLocalResDataToDb(type);
                break;
            default:
                break;
        }
        // 记得关闭inputstream
        if (inputStream != null)
        {
            try
            {
                inputStream.close();
                inputStream = null;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存从服务端传过来的etag信息
     * 
     * @param response
     * @param md5Url void
     */
    private static void saveEtag(HttpResponse response, String md5Url)
    {
        if (response == null)
        {
            return;
        }
        String headerInfo = "";
        String lastModifyInfo = "";
        String etagInfo = "";
        // 获取"Last-Modified"和"ETag"信息，保存到sp中
        Header[] headers = response.getAllHeaders();
        if (headers != null && headers.length > 0)
        {
            for (Header header : headers)
            {
                String headerName = header.getName();
                if (headerName.equals("Last-Modified"))
                {
                    lastModifyInfo = header.getValue();
                }
                if (headerName.equals("ETag"))
                {
                    etagInfo = header.getValue();
                }
            }
        }
        // 将lastModifyInfo和etagInfo拼接
        if (lastModifyInfo == "")
        {
            headerInfo = etagInfo;
        }
        else
        {
            if (etagInfo != "")
            {
                headerInfo = lastModifyInfo + ConstantsParameter.SHAREDPREFERENCES_DATA_DELIMITER + etagInfo;
            }
            else
            {
                headerInfo = lastModifyInfo;
            }
        }
        // 保存到sp中
        SharedPreManager.getInstance().putString(false, PrefConstants.StaticResourceUpdateTable.TABLE_NAME, md5Url,
                headerInfo);
    }
}
