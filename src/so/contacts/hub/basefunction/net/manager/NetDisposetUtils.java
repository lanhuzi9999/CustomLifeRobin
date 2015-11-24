package so.contacts.hub.basefunction.net.manager;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


/**
 * 网络请求处理工具类
 * 
 * @author wcy
 * @since 2015-5-30
 */
public class NetDisposetUtils
{

    public static final String TAG = NetDisposetUtils.class.getSimpleName();

    /**
     * 解压从服务器传过来的数据
     * 
     * @author wcy
     * @since 2015-5-30
     * @param data
     * @return
     */
    public static String getRealString(byte[] data)
    {
        byte[] h = new byte[2];
        h[0] = (data)[0];
        h[1] = (data)[1];
        int head = getShort(h);
        boolean t = head == 0x1f8b;
        InputStream in;
        StringBuilder sb = new StringBuilder();
        try
        {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            if (t)
            {
                in = new GZIPInputStream(bis);
            }
            else
            {
                in = bis;
            }
            BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
            for (String line = r.readLine(); line != null; line = r.readLine())
            {
                sb.append(line);
            }
            in.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static int getShort(byte[] data)
    {
        return (int) ((data[0] << 8) | data[1] & 0xFF);
    }

    /**
     * 获取数据通过httpclient
     * 
     * @author wcy
     * @since 2015-6-2
     * @param url
     * @return
     */
    public static String getResultByHttpGet(String url)
    {
        String result = "";
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpRequest = new HttpGet(url);
        HttpResponse httpResponse = null;
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
        try
        {
            httpResponse = httpClient.execute(httpRequest);
            if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                result = EntityUtils.toString(httpResponse.getEntity());
                return result;
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
        return result;
    }

    /**
     * 通过post方式去与后台通信
     * 
     * @param url
     * @param strEntity
     * @return String
     */
    public static String getResultByHttpPost(String url, String strEntity)
    {
        String result = "";
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpRequest = new HttpPost(url);
        HttpResponse httpResponse = null;
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
        try
        {
            httpRequest.setEntity(new StringEntity(strEntity, HTTP.UTF_8));
            httpResponse = httpClient.execute(httpRequest);
            if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                result = EntityUtils.toString(httpResponse.getEntity());
                return result;
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
        return result;
    }
}
