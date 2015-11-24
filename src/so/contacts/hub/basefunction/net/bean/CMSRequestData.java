package so.contacts.hub.basefunction.net.bean;

import java.util.Map;

import android.text.TextUtils;

import so.contacts.hub.ContactsApp;
import so.contacts.hub.basefunction.MD5.MD5;
import so.contacts.hub.basefunction.location.LBSManager;
import so.contacts.hub.basefunction.utils.SystemUtil;

/**
 * **************************************************************** 文件名称 :
 * CMSRequestData.java 作 者 : robin 创建时间 : 2015-11-2 下午8:11:31 文件描述 : cms数据请求类
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有 修改历史 : 2015-11-2 1.00 初始版本
 ***************************************************************** 
 */
public class CMSRequestData extends BaseRequestData
{

    /**
     * 应用版本号
     */
    private String app_version;

    /**
     * 设备型号
     */
    private String device;

    /**
     * 位置信息，经度和纬度
     */
    private String location;

    /**
     * 城市信息
     */
    private String city;

    /**
     * 设备号，注意和设备型号做区分
     */
    private String device_no;

    /**
     * android系统的版本
     */
    private String sys_version;

    /**
     * 签名，构成是：MD5(device+ timestamp+secret)
     */
    private String sign;

    /**
     * 用户登录令牌,未登录状态时可以为空
     */
    private String pt_token;

    public CMSRequestData()
    {
        super();
        app_version = SystemUtil.getAppVersion(ContactsApp.getInstance());
        device = SystemUtil.getMachine();
        location = LBSManager.getInstance().getLatitude() + "," + LBSManager.getInstance().getLongitude();
        city = LBSManager.getInstance().getCity();
        device_no = getDev_no();
        sys_version = SystemUtil.getOS();
        sign = MD5.toMD5(getDevice() + getTimestamp() + SECRITY);
        // 账号登陆模块还没写，先放在这里
        // pt_token
    }

    public String getDevice_no()
    {
        return device_no;
    }

    public void setDevice_no(String device_no)
    {
        this.device_no = device_no;
    }

    public String getSys_version()
    {
        return sys_version;
    }

    public void setSys_version(String sys_version)
    {
        this.sys_version = sys_version;
    }

    public String getSign()
    {
        return sign;
    }

    public void setSign(String sign)
    {
        this.sign = sign;
    }

    public String getPt_token()
    {
        return pt_token;
    }

    public void setPt_token(String pt_token)
    {
        this.pt_token = pt_token;
    }

    public String getApp_version()
    {
        return app_version;
    }

    public void setApp_version(String app_version)
    {
        this.app_version = app_version;
    }

    public String getDevice()
    {
        return device;
    }

    public void setDevice(String device)
    {
        this.device = device;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    @Override
    protected void setParams(Map<String, String> params)
    {
        if (!TextUtils.isEmpty(getApp_version()))
        {
            params.put("app_version", getApp_version());
        }
        if (!TextUtils.isEmpty(getDevice()))
        {
            params.put("device", getDevice());
        }
        if (!TextUtils.isEmpty(getDevice_no()))
        {
            params.put("device_no", getDevice_no());
        }
        if (!TextUtils.isEmpty(getLocation()))
        {
            params.put("location", getLocation());
        }
        if (!TextUtils.isEmpty(getCity()))
        {
            params.put("city", getCity());
        }
        if (!TextUtils.isEmpty(getSys_version()))
        {
            params.put("sys_version", getSys_version());
        }
        if (!TextUtils.isEmpty(getPt_token()))
        {
            params.put("pt_token", getPt_token());
        }
        if (!TextUtils.isEmpty(getSign()))
        {
            params.put("sign", getSign());
        }
    }
}
