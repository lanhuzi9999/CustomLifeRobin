package so.contacts.hub.basefunction.operate.cms.manager;

import so.contacts.hub.basefunction.config.Config;
import so.contacts.hub.basefunction.net.bean.CMSRequestData;
import so.contacts.hub.basefunction.operate.cms.bean.CMSResponseBaseData;
import so.contacts.hub.basefunction.operate.cms.util.CMSHttpRequestUtil;
import android.content.Context;

/**
 * ****************************************************************
 * 文件名称 : CMSCommServicesManager.java
 * 作 者 :   Robin
 * 创建时间 : 2015-11-8 下午9:39:00
 * 文件描述 : cms常用服务接口管理类
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-8 1.00 初始版本
 *****************************************************************
 */
public class CMSCommServicesManager
{
    private static final String TAG = "CMSCommServicesManager";

    private static CMSCommServicesManager mCmsCommServicesManager;

    public CMSCommServicesManager()
    {
    }

    public static CMSCommServicesManager getInstance()
    {
        if (mCmsCommServicesManager == null)
        {
            mCmsCommServicesManager = new CMSCommServicesManager();
        }
        return mCmsCommServicesManager;
    }

    public void updateCommonServicesData(Context context)
    {
        if (context == null)
        {
            return;
        }
        int commonServicesDataVersion = 0;
        CMSResponseBaseData data = Config.getDatabaseHelper().getCmsDataDB().getCommonServicesData();
        if (data != null)
        {
            commonServicesDataVersion = data.getData_version();
        }
        CMSRequestData requestObj = new CMSRequestData();
        requestObj.setParams("data_version", String.valueOf(commonServicesDataVersion));
        String urlStr = "http://api.putao.so/scmsface/view/services";
        CMSResponseBaseData resultData = CMSHttpRequestUtil.getCmsResponseData(urlStr, requestObj);

        if (resultData != null)
        {
            Config.getDatabaseHelper().getCmsDataDB().updateCommonServicesData(context, resultData);
        }
    }
}
