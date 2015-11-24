package so.contacts.hub.basefunction.location.action;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

import so.contacts.hub.basefunction.location.bean.LBSInfoItem;
import so.contacts.hub.basefunction.location.listener.LBSServiceListener;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;

public class GaodeAction implements ILocateAction
{

    private static LocationManagerProxy mlocaLocationManagerProxy;

    private LBSServiceListener mLbsServiceListener;

    private AMapLocationListener mAMapLocationListener = new AMapLocationListener()
    {

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2)
        {

        }

        @Override
        public void onProviderEnabled(String arg0)
        {

        }

        @Override
        public void onProviderDisabled(String arg0)
        {

        }

        @Override
        public void onLocationChanged(Location arg0)
        {

        }

        @Override
        public void onLocationChanged(AMapLocation location)
        {
            if (mLbsServiceListener != null)
            {
                if (location != null && location.getAMapException().getErrorCode() == 0)
                {
                    // 定位成功
                    LBSInfoItem item = new LBSInfoItem();
                    item.city = location.getCity();
                    item.latitude = location.getLatitude();
                    item.longitude = location.getLongitude();
                    item.time = location.getTime();
                    item.province = location.getProvince();
                    item.district = location.getDistrict();
                    item.street = location.getStreet();
                    item.address = location.getAddress();
                    item.cityCode = location.getCityCode();
                    item.location = location;
                    mLbsServiceListener.onLocationChanged(item);
                }
                else
                {
                    // 定位失败,这里可以一次定位未成功则进行多次定位保证正确率，后续待添加
                    mLbsServiceListener.onLocationFailed();
                    mLbsServiceListener = null;
                }
            }
        }
    };

    @Override
    public void startLocation(Context context, LBSServiceListener listener)
    {
        mLbsServiceListener = listener;
        if (mlocaLocationManagerProxy == null)
        {
            mlocaLocationManagerProxy = LocationManagerProxy.getInstance(context);
            // 开始定位操作，结果信息回调到mAMapLocationListener
            mlocaLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 500,
                    mAMapLocationListener);
        }
    }

    @Override
    public void deactivate()
    {
        if (mlocaLocationManagerProxy != null)
        {
            mlocaLocationManagerProxy.removeUpdates(mAMapLocationListener);
            mlocaLocationManagerProxy.destroy();
        }
        mlocaLocationManagerProxy = null;
    }
}
