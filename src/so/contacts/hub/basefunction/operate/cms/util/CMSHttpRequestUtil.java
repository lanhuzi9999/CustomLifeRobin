package so.contacts.hub.basefunction.operate.cms.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.text.TextUtils;
import android.util.Log;
import so.contacts.hub.basefunction.net.bean.CMSRequestData;
import so.contacts.hub.basefunction.net.manager.PTHTTPManager;
import so.contacts.hub.basefunction.operate.cms.bean.CMSResponseBaseData;

/**
 * ****************************************************************
 * 文件名称 : CMSHttpRequestUtil.java
 * 作 者 :   Robin
 * 创建时间 : 2015-11-8 下午2:53:23
 * 文件描述 : 获取cms数据接口工具类
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-8 1.00 初始版本
 *****************************************************************
 */
public class CMSHttpRequestUtil
{
    public static final String TAG = "CMSHttpRequestUtil";

    /**
     * cms返回数据字段
     */
    private static final String RESPONSE_RET_CODE = "ret_code";
    private static final String RESPONSE_DATA_VERSION = "data_version";
    private static final String RESPONSE_DATA = "data";
    
    /**
     * ret_code的三种状态码
     */
    private static final int RET_CODE_NONE= 0;
    private static final int RET_CODE_HAVE =1;
    private static final int RET_CODE_EXCEPTION = -1;
    
    /**
     * data_version版本号的两种状态：0没有数据，-1是默认版本号，正常情况下是大于0的一个数值
     */
    private static final int DATA_VERSION_NONE = 0;
    private static final int DATA_VERSION_DEFAULT = -1;
    
    /**
     * 同步获取cms数据
     * @param url
     * @param requestData
     * @return
     * CMSResponseBaseData
     */
    public static CMSResponseBaseData getCmsResponseData(String url, CMSRequestData requestData)
    {
        String responseStr = PTHTTPManager.getCmsHttp().get(url, requestData);
        Log.d(TAG, "getCmsResponseBaseData responseStr:" + responseStr);
        //解析返回的字符串成CMSResponseBaseData对象
        return parseCmsData(responseStr);
    }
    
    /**
     * 解析返回的字符串成CMSResponseBaseData对象
     * @param responseStr
     * @return
     * CMSResponseBaseData
     */
    public static CMSResponseBaseData parseCmsData(String responseStr)
    {
        if(TextUtils.isEmpty(responseStr))
        {
            return null;
        }
        CMSResponseBaseData responseData = null;
        int ret_code = -1;
        int data_version = -1;
        String data = "";
        try
        {
            JSONObject jsonObject = new JSONObject(responseStr);
            //检查ret_code
            ret_code = jsonObject.getInt(RESPONSE_RET_CODE);
            if(ret_code == RET_CODE_NONE)
            {
                return null;
            }else if (ret_code == RET_CODE_EXCEPTION) {
                return null;
            }else if (ret_code == RET_CODE_HAVE) {
                data_version = jsonObject.getInt(RESPONSE_DATA_VERSION);
                if(data_version == DATA_VERSION_NONE)
                {
                    return null;
                }
                data = jsonObject.getString(RESPONSE_DATA);
                if(TextUtils.isEmpty(data))
                {
                    return null;
                }
            }
            
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        if (ret_code != RET_CODE_EXCEPTION && data_version != DATA_VERSION_DEFAULT && !TextUtils.isEmpty(data))
        {
            responseData = new CMSResponseBaseData();
           responseData.setRet_code(ret_code);
           responseData.setData_version(data_version);
           responseData.setData(data);
        }
        return responseData;
    }
}

