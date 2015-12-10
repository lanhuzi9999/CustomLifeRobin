package com.lives.depend.version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

/**
 * ****************************************************************
 * 文件名称 : HttpUtils.java
 * 作 者 :   hyl
 * 创建时间 : 2015-11-23 下午4:22:18
 * 文件描述 : 下载模块网络请求类
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-23 1.00 初始版本
 *****************************************************************
 */
class HttpUtils
{

    private static final String TAG = "HttpUtils";

    public static String postRequest(String path, String requestStr)
    {
        HttpURLConnection conn = null;
        OutputStream outPut = null;
        InputStream inPut = null;
        BufferedReader buffReader = null;

        String result = null;
        try
        {
            byte[] requestByte = requestStr.toString().getBytes("UTF-8");
            URL url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30 * 1000);
            conn.setDoOutput(true);// 允许输出
            conn.setDoInput(true);
            conn.setUseCaches(false);// 不使用缓存
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Length", String.valueOf(requestByte.length));
            conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");

            outPut = conn.getOutputStream();
            outPut.write(requestByte);
            outPut.flush();

            inPut = conn.getInputStream();
            buffReader = new BufferedReader(new InputStreamReader(inPut));
            String line;

            while ((line = buffReader.readLine()) != null)
            {
                result += line;
            }
            int firstIndex = result.indexOf("{");
            int endIndex = result.lastIndexOf("}");
            result = result.substring(firstIndex, endIndex + 1);
            Log.i(TAG, "result:" + result);
        }
        catch (UnsupportedEncodingException e)
        {
            Log.w(TAG, "catch UnsupportedEncodingException throw by postRequest", e);
        }
        catch (IOException e)
        {
            Log.w(TAG, "catch IOException throw by postRequest", e);
        }
        catch (Exception e)
        {
            Log.w(TAG, "catch Exception throw by postRequest", e);
        }
        finally
        {
            if (conn != null)
            {
                conn.disconnect();
            }
            closeBufferedReader(buffReader);
            closeOutputStream(outPut);
            clostInputStream(inPut);
        }
        return result;
    }

    private static void closeBufferedReader(BufferedReader buffReader)
    {
        if (buffReader != null)
        {
            try
            {
                buffReader.close();
            }
            catch (IOException e)
            {
                Log.w(TAG, "queryExpress finally buffReader IOException.");
            }
        }
    }

    private static void closeOutputStream(OutputStream outPut)
    {
        if (outPut != null)
        {
            try
            {
                outPut.close();
            }
            catch (IOException e)
            {
                Log.w(TAG, "queryExpress finally outPut IOException.");
            }
        }
    }

    private static void clostInputStream(InputStream inPut)
    {
        if (inPut != null)
        {
            try
            {
                inPut.close();
            }
            catch (IOException e)
            {
                Log.w(TAG, "queryExpress finally inPut IOException.");
            }
        }
    }
}
