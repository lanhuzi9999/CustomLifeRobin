package so.contacts.hub;

import java.util.ArrayList;
import java.util.List;

import com.putao.live.R;

import android.R.integer;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import so.contacts.hub.basefunction.config.Config;
import so.contacts.hub.basefunction.operate.cms.adapter.CommonServicesAdapter;
import so.contacts.hub.basefunction.operate.cms.adapter.ContentBeanAdapter;
import so.contacts.hub.basefunction.operate.cms.bean.CommonView;
import so.contacts.hub.basefunction.operate.cms.manager.CMSCommServicesManager;
import so.contacts.hub.basefunction.operate.cms.manager.CMSContentConfigManager;
import so.contacts.hub.basefunction.operate.cms.manager.CMSContentConfigManager.ContentConfigResultListener;
import so.contacts.hub.basefunction.storage.db.CMSDataDB.CommonServicesTable;
import so.contacts.hub.basefunction.storage.db.CMSDataDB.ContentConfigTable;
import so.contacts.hub.basefunction.utils.ServiceClickAgentUtil;
import so.contacts.hub.basefunction.widget.CustomListView;
import so.contacts.hub.basefunction.widget.ExpandGridView;

public class YellowPageServicesFragment extends BaseFragment implements ContentConfigResultListener, OnScrollListener
{
    private static final String TAG = YellowPageServicesFragment.class.getSimpleName();
    
    private View mPlugView;// 生活页view

    private CustomListView mContentListView;// 添加广告，常用服务，全部服务的父容器(商品流)

    private ContentBeanAdapter mContentListViewAdapter;
    
    private List<Object> mContentBeans = new ArrayList<Object>();
    
    private ExpandGridView mOffenGridView;// 常用服务

    private HandlerThread mCMSContentHandlerThread;

    private CMSContentHandler mCMSContentHandler;

    private CMSContentObserver mCmsContentObserver;// 数据库变化监听

    // ----cms 常用服务start
    private static final int CMS_COMMON_SERVICE_INIT = 0x1;
    
    private static final int CMS_COMMON_SERVICE_UPDATE = 0x2;

    private static final int REFRESH_OFFEN_SERVICES = 0x2001;

    private List<CommonView> mCommonServiceBeans; // 常用服务列表

    private CommonServicesAdapter mCommonServicesAdapter;

    // ----cms常用服务end
    
    // ----商品流，附近精选服务 start
    private static final int CMS_CONTENT_CONFIG_INIT = 0x3;

    private static final int CMS_CONTENT_CONFIG_UPDATE = 0x4;

    private static final int REFRESH_CONTENT_CONFIG = 0x2002;

    // ----商品流，附近精选服务 end
    
    /**
     * 颜色渐变高度
     */
    private int mHeaderHeight;
    
    /**
     * 滚动距离
     */
    private int mHeaderScroll;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mPlugView = inflater.inflate(R.layout.putao_yellow_page_plug_home, null);
        initView();
        initListener();
        initData();
        registerContentObserver();
        return mPlugView;
    }

    private void initListener()
    {
        // 快捷服务
        mOffenGridView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                CommonView bean = (CommonView) mCommonServicesAdapter.getItem(position);
                ServiceClickAgentUtil.doClickAction(getContext(), bean);
            }
        });

        mContentListView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
            {
                // add by lxh 2015年4月14日 修正精选统计不准的问题
                int pos = position - mContentListView.getHeaderViewsCount();
                mContentListViewAdapter.onItemClick(pos);
            }
        });
    }

    /**
     * 初始化数据 方法表述 void
     */
    private void initData()
    {
        mCMSContentHandlerThread = new HandlerThread("pageConfig");
        mCMSContentHandlerThread.start();

        mCMSContentHandler = new CMSContentHandler(mCMSContentHandlerThread.getLooper());

        initCmsData();
    }

    private void initCmsData()
    {
        // 初始化 常用服务 数据
        mCMSContentHandler.removeMessages(CMS_COMMON_SERVICE_INIT);
        mCMSContentHandler.sendEmptyMessage(CMS_COMMON_SERVICE_INIT);
        
        //初始化附近精选服务
        mCMSContentHandler.removeMessages(CMS_CONTENT_CONFIG_INIT);
        mCMSContentHandler.sendEmptyMessage(CMS_CONTENT_CONFIG_INIT);
    }

    /**
     * 注册数据库变化监听 方法表述 void
     */
    private void registerContentObserver()
    {
        mCmsContentObserver = new CMSContentObserver(new Handler());
        getContext().getContentResolver().registerContentObserver(CommonServicesTable.URI, true, mCmsContentObserver);
        getContext().getContentResolver().registerContentObserver(ContentConfigTable.URI, true, mCmsContentObserver);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unRegisterContentObserver();
    }

    private void unRegisterContentObserver()
    {
        if (mCmsContentObserver != null)
        {
            getContext().getContentResolver().unregisterContentObserver(mCmsContentObserver);
            mCmsContentObserver = null;
        }
    }

    private void initView()
    {
        mContentListView = (CustomListView) mPlugView.findViewById(R.id.putao_content_listView);
        mContentListView.setOnScrollListener(this);
        mHeaderHeight = 350;
        // 常用服务
        mOffenGridView = new ExpandGridView(getContext());
        mOffenGridView.setNumColumns(4);
        mOffenGridView.setBackgroundResource(R.color.putao_white);
        mOffenGridView.setSelector(R.color.putao_transparent);
        mOffenGridView.setVerticalSpacing(getResources().getDimensionPixelSize(R.dimen.putao_live_commserver_vspacing));
        int commServerVPdding = getResources().getDimensionPixelSize(R.dimen.putao_live_commserver_vpadding);
        int commTopPadding = getResources().getDimensionPixelSize(R.dimen.putao_title_bar_hight);
        mOffenGridView.setPadding(0, commServerVPdding+commTopPadding, 0, commServerVPdding);
        mContentListView.addHeaderView(View.inflate(getActivity(), R.layout.putao_comm_divider_horizontal, null), null,
                false);
        // 添加常用服务的数据
        mContentListView.addHeaderView(mOffenGridView, null, false);
        mContentListView.addHeaderView(View.inflate(getActivity(), R.layout.putao_comm_divider_horizontal, null), null,
                false);
        //添加
        mContentListViewAdapter = new ContentBeanAdapter(getContext(), mContentBeans);
        mContentListView.setAdapter(mContentListViewAdapter);
        mContentListView.scrollTo(0, 0);
    }

    private class CMSContentObserver extends ContentObserver
    {

        public CMSContentObserver(Handler handler)
        {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri)
        {
            if (mCMSContentHandler != null && uri != null)
            {
                String uriStr = uri.toString();
                if (uriStr.equals(CommonServicesTable.URI.toString()))
                {
                    // 更新常用服务部分的界面
                    mCMSContentHandler.removeMessages(CMS_COMMON_SERVICE_INIT);
                    mCMSContentHandler.sendEmptyMessage(CMS_COMMON_SERVICE_INIT);
                }
                else if (uriStr.equals(ContentConfigTable.URI.toString()))
                {
                    mCMSContentHandler.removeMessages(CMS_CONTENT_CONFIG_INIT);
                    mCMSContentHandler.sendEmptyMessage(CMS_CONTENT_CONFIG_INIT);
                }
            }
        }

    }

    private class CMSContentHandler extends Handler
    {
        public CMSContentHandler(Looper looper)
        {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case CMS_COMMON_SERVICE_INIT:
                    // 从数据库获取
                    mCommonServiceBeans = Config.getDatabaseHelper().getCmsDataDB().getCommonServicesList();
                    // handler发送消息到ui线程显示
                    mUiHandler.sendEmptyMessage(REFRESH_OFFEN_SERVICES);
                    break;
                case CMS_COMMON_SERVICE_UPDATE:
                    updateCommonServicesData();
                    break;
                case CMS_CONTENT_CONFIG_INIT:
                    // 从本地数据库查询商品流信息
                    CMSContentConfigManager.getInstance().queryContentConfigData(getContext(),
                            YellowPageServicesFragment.this);
                    break;
                case CMS_CONTENT_CONFIG_UPDATE:
                    CMSContentConfigManager.getInstance().updateContentConfigData(getContext());
                    break;
                default:
                    break;
            }
        }
    }
    
    /**
     * 从网络获取常用服务数据
     * void
     */
    public void updateCommonServicesData()
    {
        CMSCommServicesManager.getInstance().updateCommonServicesData(getContext());
    }

    private Handler mUiHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case REFRESH_OFFEN_SERVICES:
                    refreshOffenServices();
                    break;
                case REFRESH_CONTENT_CONFIG:
                    refreshContentConfig(msg);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 刷新常用服务界面 方法表述 void
     */
    protected void refreshOffenServices()
    {
        if (mCommonServicesAdapter == null)
        {
            mCommonServicesAdapter = new CommonServicesAdapter(getContext(), mCommonServiceBeans);
            mOffenGridView.setAdapter(mCommonServicesAdapter);
        }
        else
        {
            mCommonServicesAdapter.refreshData(mCommonServiceBeans);
            mCommonServicesAdapter.notifyDataSetChanged();
        }
    }

    protected void refreshContentConfig(Message msg)
    {
        if (mContentBeans != null)
        {
            // 先清空之前的内容流数据
            mContentBeans.clear();
            List<Object> contentBeans = (List<Object>) msg.obj;
            if (contentBeans != null && contentBeans.size() > 0)
            {
                mContentBeans.addAll(0, contentBeans);
            }

            if (mContentListViewAdapter != null)
            {
                mContentListViewAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 定位位置发生改变，从网络刷新获cms数据
     */
    @Override
    public void refreshDataByLocationChanged()
    {
        super.refreshDataByLocationChanged();

        updateCmsDataFromNet();
    }

    /**
     * 从网络刷新获cms数据 void
     */
    private void updateCmsDataFromNet()
    {
         Log.d(TAG, "updateCmsDataFromNet");
        if (mCMSContentHandler == null)
        {
            return;
        }
        //更新常用服务
        mCMSContentHandler.removeMessages(CMS_COMMON_SERVICE_UPDATE);
        mCMSContentHandler.sendEmptyMessage(CMS_COMMON_SERVICE_UPDATE);
        
        //更新商品流
        mCMSContentHandler.removeMessages(CMS_CONTENT_CONFIG_UPDATE);
        mCMSContentHandler.sendEmptyMessage(CMS_CONTENT_CONFIG_UPDATE);
    }

    @Override
    public void onResult(List<?> resultList)
    {
        //发消息到ui线程显示
        Message message = mUiHandler.obtainMessage();
        message.what = REFRESH_CONTENT_CONFIG;
        message.obj = resultList;
        mUiHandler.sendMessage(message);
    }

    @Override
    public Handler getHandler()
    {
        if (getActivity() != null)
        {
            return new Handler(getActivity().getMainLooper());
        }
        return null;
    }

    @Override
    public void onScrollStateChanged(AbsListView arg0, int arg1)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        final View child = absListView.getChildAt(0);
        if (child != null)
        {
            if (child == mContentListView.getHeadView())
            {
                scrollHeaderTo(0);
                return;
            }
            View targetView = null;
//            if (mOperationLayout.getVisibility() != View.GONE)
//            {
//                targetView = mOperationLayout;
//            }
//            else
//            {
                targetView = mOffenGridView;
//            }
            mHeaderHeight = Math.max(mHeaderHeight, targetView.getMeasuredHeight());
            scrollHeaderTo(child == targetView ? child.getTop() : -mHeaderHeight);
        }
    }

    private void scrollHeaderTo(int scrollTo)
    {
        scrollHeaderTo(scrollTo, false);
    }

    /**
     * 根据滚动的位置进行滚动处理，考虑滚动位置超出最大滚动位置的情况
     * 
     * @param scrollTo
     * @param forceChange void
     */
    private void scrollHeaderTo(int scrollTo, boolean forceChange)
    {
        scrollTo = Math.min(Math.max(scrollTo, -mHeaderHeight), 0);
        if (mHeaderScroll == scrollTo && !forceChange)
        {
            return;
        }
        mHeaderScroll = scrollTo;
        processScrollChanged((float) -scrollTo / mHeaderHeight, mHeaderHeight, -scrollTo);
    }

    /**
     * 处理滚动变化，根据余弦变化数值去控制alpha
     * 
     * @param progress
     * @param height
     * @param scroll void
     */
    private void processScrollChanged(float progress, int height, int scroll)
    {
        height -= getResources().getDimension(R.dimen.putao_title_bar_and_status_hight);
        progress = (float) scroll / height;
        if (progress > 1f)
        {
            progress = 1f;
        }
        // 余弦变化
        progress = (1 - (float) Math.cos(progress * Math.PI)) * 0.5f;
//        mFadingTitlebarHelper.setTitlebarAlpha((int) (255 * progress));
    }
}
