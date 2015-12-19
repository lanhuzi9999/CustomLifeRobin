package so.contacts.hub.basefunction.operate.cms.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.android.volley.Response.Listener;
import com.google.gson.JsonSyntaxException;

import so.contacts.hub.basefunction.config.Config;
import so.contacts.hub.basefunction.location.LBSManager;
import so.contacts.hub.basefunction.location.bean.LBSInfoItem;
import so.contacts.hub.basefunction.location.listener.LBSServiceListener;
import so.contacts.hub.basefunction.net.bean.CMSRequestData;
import so.contacts.hub.basefunction.operate.cms.bean.CMSResponseBaseData;
import so.contacts.hub.basefunction.operate.cms.bean.ContentBean;
import so.contacts.hub.basefunction.operate.cms.bean.ContentConfig;
import so.contacts.hub.basefunction.operate.cms.bean.ContentView;
import so.contacts.hub.basefunction.operate.cms.util.CMSCommonConfig;
import so.contacts.hub.basefunction.operate.cms.util.CMSHttpRequestUtil;
import so.contacts.hub.basefunction.storage.db.CMSDataDB;
import android.R.integer;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

public class CMSContentConfigManager
{
    private static final String TAG = "CMSContentConfigManager";
    
    private static CMSContentConfigManager mCmsContentConfigManager;
    
    public CMSContentConfigManager()
    {
    }

    public static CMSContentConfigManager getInstance()
    {
        if(mCmsContentConfigManager == null)
        {
            mCmsContentConfigManager = new CMSContentConfigManager();
        }
        return mCmsContentConfigManager;
    }
    
    /**
     * 从服务器更新商品配置流数据(定位触发，再定位一次刷新定位信息后更新数据)
     * @param context
     * void
     */
    public void updateContentConfigData(final Context context)
    {
        //检查网络状态，无网络直接返回
        
        //定位操作
//        LBSManager.getInstance().doLocation(context, new LBSServiceListener()
//        {
//            
//            @Override
//            public void onLocationFailed()
//            {
//                startUpdate(context);
//            }
//            
//            @Override
//            public void onLocationChanged(LBSInfoItem item)
//            {
//                startUpdate(context);
//            }
//        });
        startUpdate(context);
    }
    
    /**
     * 更新操作
     * @param context
     * void
     */
    private void startUpdate(Context context)
    {
        // 本地数据库商品流版本号
        int localVersion = -1;
        CMSResponseBaseData localData = Config.getDatabaseHelper().getCmsDataDB().getContentConfigData();
        if(localData != null)
        {
            localVersion = localData.getData_version();
        }
        CMSRequestData cmsRequestData = new CMSRequestData();
        cmsRequestData.setParam("data_version", String.valueOf(localVersion));
        CMSResponseBaseData responseData = CMSHttpRequestUtil.getCmsResponseData(CMSCommonConfig.CMS_URL_STREAMS, cmsRequestData);
        if(responseData != null)
        {
            CMSDataDB cmsDataDB = Config.getDatabaseHelper().getCmsDataDB();
            if(localVersion == -1)
            {
                //localVersion == -1表示是第一次请求，做插入操作
                cmsDataDB.insertContentConfigData(context, responseData);
            }else {
                //做更新操作
                cmsDataDB.updateContentConfigData(context, localVersion, responseData);
            }
        }
    }
    
    /**
    * 查询商品流数据后的回调
    */
    public interface ContentConfigResultListener
    {
        public void onResult(List<?> resultList);

        public Handler getHandler();
    }
    
    /**
     * 本地数据库查询内容流，回调给首页显示
     * @param context
     * @param listener
     * void
     */
    public void queryContentConfigData(Context context, ContentConfigResultListener listener)
    {
      //从数据库查询CMSResponseBaseData
      ContentConfig config = getContentConfig(context);
      //排序，通过回调返回
      if(config != null)
      {
          List<ContentView> contentViews = config.getContent_views();
          //做回调返回参数用的ContentBean
          List<ContentBean> contentBeans = null;
          if(contentViews != null && contentViews.size()>0)
          {
                contentBeans = new ArrayList<ContentBean>();
                //ContentView排序
                Collections.sort(contentViews, new Comparator<ContentView>()
                {

                    @Override
                    public int compare(ContentView left, ContentView right)
                    {
                        return left.getSort() - right.getSort();
                    }
                });
                for (ContentView item : contentViews)
                {
                    List<ContentBean> beans = item.getContent_beans();
                    if (beans != null && beans.size() > 0)
                    {
                        //ContentBean排序
                        Collections.sort(beans, new Comparator<ContentBean>()
                        {

                            @Override
                            public int compare(ContentBean leftBean, ContentBean rightBean)
                            {
                                return leftBean.getSort() - rightBean.getSort();
                            }
                        });
                        //将排序后的ContentBeans回调返回
                        for(ContentBean contentBean:beans)
                        {
                            contentBeans.add(contentBean);
                        }
                    }
                }
          }
          //回调返回ContentBean
          returnData(contentBeans, listener);
      }
    }
    
    /**
     * 回调返回数据
     * @param contentBeans
     * @param listener
     * void
     */
    private void returnData(final List<ContentBean> contentBeans, final ContentConfigResultListener listener)
    {
        Handler handler = listener.getHandler();
        if (handler != null)
        {
            handler.post(new Runnable()
            {

                @Override
                public void run()
                {
                    listener.onResult(contentBeans);
                }
            });
        }
    }

    /**
     * 从数据获取原始的内容流数据
     * @param context
     * @return
     * ContentConfig
     */
    public ContentConfig getContentConfig(Context context)
    {
        ContentConfig config = null;
        CMSResponseBaseData localData = Config.getDatabaseHelper().getCmsDataDB().getContentConfigData();
        if (localData != null)
        {
            String data = localData.getData();
            if (!TextUtils.isEmpty(data))
            {
                try
                {
                    config = Config.mGson.fromJson(data, ContentConfig.class);
                }
                catch (JsonSyntaxException e)
                {
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return config;
    }
}
