package so.contacts.hub.basefunction.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import so.contacts.hub.ContactsApp;
import so.contacts.hub.basefunction.MD5.MD5;
import so.contacts.hub.basefunction.net.manager.PTHTTPManager;

public class QiNiuCloudManager
{
    private static final String TAG = "QiNiuCloudManager";

    private volatile static QiNiuCloudManager mInstance;

    public QiNiuCloudManager()
    {

    }

    public static QiNiuCloudManager getInstance()
    {
        if (mInstance == null)
        {
            synchronized (QiNiuCloudManager.class)
            {
                if (mInstance == null)
                {
                    mInstance = new QiNiuCloudManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 从葡萄服务器获取上传文件的token
     * 
     * @param url
     * @return String
     */
    public String getUploadToken(String url)
    {
        String result = PTHTTPManager.getHttp().get(url, "");
        String token = "";
        String errMsg = "";
        int errCode;
        JSONObject jsonObject;
        try
        {
            jsonObject = new JSONObject(result);
            if (!jsonObject.isNull("token"))
            {
                token = jsonObject.getString("token");
            }
            if (!jsonObject.isNull("errMsg"))
            {
                errMsg = jsonObject.getString("errMsg");
            }
            if (!jsonObject.isNull("errCode"))
            {
                errCode = jsonObject.getInt("errCode");
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return token;
    }

    /**
     * 获取上传文件需要的key，对应的是指定七牛服务上的文件名 使用：md5(设备号+手机号+当前时间+文件名) 来保证key是唯一的
     * 
     * @param fileName
     * @return String
     */
    public String getExpectKey(String fileName)
    {
        String deviceId = SystemUtil.getDeviceId(ContactsApp.getInstance());
        String phoneNumber = ContactsHubUtils.getPhoneNumber(ContactsApp.getInstance());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDateStr = dateFormat.format(new Date());
        String keyStr = MD5.toMD5(deviceId + phoneNumber + nowDateStr + fileName);

        return keyStr;
    }
}
