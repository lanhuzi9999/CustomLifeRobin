package so.contacts.hub.basefunction.location;

import com.putao.live.R;

import android.content.Context;
import android.text.TextUtils;
import so.contacts.hub.ContactsApp;
import so.contacts.hub.basefunction.location.action.GaodeAction;
import so.contacts.hub.basefunction.location.action.ILocateAction;
import so.contacts.hub.basefunction.location.bean.LBSInfoItem;
import so.contacts.hub.basefunction.location.listener.LBSServiceListener;

public class LBSManager
{
    public static final double DEFAULT_LATITUDE = 30.2784662;

    public static final double DEFAULT_LONGITUDE = 120.1194347;

    private ILocateAction mLocationAction;

    private volatile static LBSManager mLbsManager;

    private LBSInfoItem mLbsInfoItem;// 最新的定位信息

    private LBSManager()
    {
        mLocationAction = new GaodeAction();
    }

    public static LBSManager getInstance()
    {
        if (mLbsManager == null)
        {
            synchronized (LBSManager.class)
            {
                mLbsManager = new LBSManager();
            }
        }
        return mLbsManager;
    }

    /**
     * 定位操作
     * 
     * @param context
     * @param listener void
     */
    public void activate(Context context, final LBSServiceListener listener)
    {
        if (context == null || listener == null)
        {
            return;
        }
        LBSInfoItem lbsInfoItem = null;
        if (mLbsInfoItem != null)
        {
            lbsInfoItem = mLbsInfoItem;
        }
        if (lbsInfoItem != null && listener != null)
        {
            listener.onLocationChanged(lbsInfoItem);
        }
        doLocation(context, listener);
    }

    public void doLocation(Context context, final LBSServiceListener listener)
    {
        mLocationAction.startLocation(context, new LBSServiceListener()
        {

            @Override
            public void onLocationFailed()
            {
                // 定位失败
                if (listener != null)
                {
                    listener.onLocationFailed();
                }
            }

            @Override
            public void onLocationChanged(LBSInfoItem item)
            {
                // 定位成功
                if (listener != null && item != null)
                {
                    item.city = cityRedress(item.city);
                    mLbsInfoItem = item;
                    listener.onLocationChanged(item);
                }
            }
        });
    }
    
    /**
     * 城市矫正
     */
    private String cityRedress(String city)
    {
        String cityNew = new String(city);
        if (!TextUtils.isEmpty(cityNew)
                && cityNew.endsWith(ContactsApp.getInstance().getString(R.string.putao_common_city)))
        {
            cityNew = city.substring(0, cityNew.length() - 1);
        }
        return cityNew;
    }

    /**
     * 
     * 获取最近一次定位的纬度，如果从来没有定位成功过，则使用默认的纬度
     * 
     * @return double
     */
    public double getLatitude()
    {
        double latitude = 0;
        if (mLbsInfoItem != null)
        {
            latitude = mLbsInfoItem.latitude;
        }
        if (latitude == 0)
        {
            latitude = DEFAULT_LATITUDE;
        }

        return latitude;
    }
    
    public double getLongitude()
    {
        double longitude =0;
        if(mLbsInfoItem != null)
        {
            longitude = mLbsInfoItem.longitude;
        }
        if(longitude == 0)
        {
            longitude = DEFAULT_LONGITUDE;
        }
        return longitude;
    }
    
    public String getCity()
    {
        String city = "";
        if(mLbsInfoItem != null)
        {
            city = mLbsInfoItem.city;
        }
        if(TextUtils.isEmpty(city))
        {
            city = getDefaultCity();
        }
        return city;
    }
    
    public String getDefaultCity()
    {
        return ContactsApp.getInstance().getResources().getString(R.string.putao_shenzhen);
    }
}
