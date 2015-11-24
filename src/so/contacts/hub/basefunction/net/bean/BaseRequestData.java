package so.contacts.hub.basefunction.net.bean;

import java.util.HashMap;
import java.util.Map;

import so.contacts.hub.ContactsApp;
import so.contacts.hub.basefunction.utils.SystemUtil;

public abstract class BaseRequestData
{
    public static final String SECRITY = "kksd%sj*77";

    /**
     * 设备号
     */
    private String dev_no;

    /**
     * 时间戳
     */
    private long timestamp;

    /**
     * 渠道号
     */
    private String channel_no;

    /**
     * 签名
     */
    private String localSign;

    /**
     * HTTP请求参数
     */
    private Map<String, String> params;

    public BaseRequestData()
    {
        setDev_no(SystemUtil.getPutaoDeviceId(ContactsApp.getInstance()));
        setTimestamp(System.currentTimeMillis());
        setChannel_no(SystemUtil.getChannelNo(ContactsApp.getInstance()));
        params = new HashMap<String, String>();
    }

    public Map<String, String> getParams()
    {
        if (getDev_no() != null)
        {
            params.put("dev_no", getDev_no());
        }
        if (getTimestamp() > 0)
        {
            params.put("timestamp", String.valueOf(getTimestamp()));
        }
        if (getChannel_no() != null)
        {
            params.put("channel_no", getChannel_no());
        }
        if (getLocalSign() != null)
        {
            params.put("localSign", getLocalSign());
        }
        setParams(params);
        return params;
    }

    /**
     * 子类需要设置的请求参数在这里设置
     * 
     * @param params void
     */
    protected abstract void setParams(Map<String, String> params);

    public void setParams(String paramStr, String valueStr)
    {
        params.put(paramStr, valueStr);
    }

    public String getDev_no()
    {
        return dev_no;
    }

    public void setDev_no(String dev_no)
    {
        this.dev_no = dev_no;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getChannel_no()
    {
        return channel_no;
    }

    public void setChannel_no(String channel_no)
    {
        this.channel_no = channel_no;
    }

    public String getLocalSign()
    {
        return localSign;
    }

    public void setLocalSign(String localSign)
    {
        this.localSign = localSign;
    }

}
