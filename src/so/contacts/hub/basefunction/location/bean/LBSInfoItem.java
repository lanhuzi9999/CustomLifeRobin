package so.contacts.hub.basefunction.location.bean;

import android.location.Location;

public class LBSInfoItem
{
    public String city;// 城市

    public double latitude;// 纬度

    public double longitude;// 经度

    public long time;// 定位时间

    public String province;// 省份

    public String district;

    public String street;

    public String address;

    public String cityCode;

    public Location location;

    public LBSInfoItem()
    {
        city = "";
    }
    
}
