package so.contacts.hub.basefunction.account.bean;

import java.io.Serializable;

public class CityBean implements Serializable
{

    private static final long serialVersionUID = 1L;
    
    //城市名
    private String cityName;
    //城市名对应的拼音
    private String cityPy;
    //城市本身id
    private int selfId;
    //父id，如:市辖区和县的parentId为市的selfId
    private int parentId;
    //类型：1-市， 2-市辖区， 3-县
    private int cityType;
    //区号
    private String districtCode;
    //是否热门城市
    private int cityHot = 0;
    //五八同城状态?什么鬼
    private int wubaState = 0;
    //
    private String wubaCode;
    
    private int elongState = 0;

    private String elongCode;
    
    private int tongchengState = 0;

    private String tongchengCode;
    
    private int gewaraState = 0;

    private String gewaraCode;
    
    private int gaodeState = 0;

    private String gaodeCode;

    public String getCityName()
    {
        return cityName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public String getCityPy()
    {
        return cityPy;
    }

    public void setCityPy(String cityPy)
    {
        this.cityPy = cityPy;
    }

    public int getSelfId()
    {
        return selfId;
    }

    public void setSelfId(int selfId)
    {
        this.selfId = selfId;
    }

    public int getParentId()
    {
        return parentId;
    }

    public void setParentId(int parentId)
    {
        this.parentId = parentId;
    }

    public int getCityType()
    {
        return cityType;
    }

    public void setCityType(int cityType)
    {
        this.cityType = cityType;
    }

    public String getDistrictCode()
    {
        return districtCode;
    }

    public void setDistrictCode(String districtCode)
    {
        this.districtCode = districtCode;
    }

    public int getCityHot()
    {
        return cityHot;
    }

    public void setCityHot(int cityHot)
    {
        this.cityHot = cityHot;
    }

    public int getWubaState()
    {
        return wubaState;
    }

    public void setWubaState(int wubaState)
    {
        this.wubaState = wubaState;
    }

    public String getWubaCode()
    {
        return wubaCode;
    }

    public void setWubaCode(String wubaCode)
    {
        this.wubaCode = wubaCode;
    }

    public int getElongState()
    {
        return elongState;
    }

    public void setElongState(int elongState)
    {
        this.elongState = elongState;
    }

    public String getElongCode()
    {
        return elongCode;
    }

    public void setElongCode(String elongCode)
    {
        this.elongCode = elongCode;
    }

    public int getTongchengState()
    {
        return tongchengState;
    }

    public void setTongchengState(int tongchengState)
    {
        this.tongchengState = tongchengState;
    }

    public String getTongchengCode()
    {
        return tongchengCode;
    }

    public void setTongchengCode(String tongchengCode)
    {
        this.tongchengCode = tongchengCode;
    }

    public int getGewaraState()
    {
        return gewaraState;
    }

    public void setGewaraState(int gewaraState)
    {
        this.gewaraState = gewaraState;
    }

    public String getGewaraCode()
    {
        return gewaraCode;
    }

    public void setGewaraCode(String gewaraCode)
    {
        this.gewaraCode = gewaraCode;
    }

    public int getGaodeState()
    {
        return gaodeState;
    }

    public void setGaodeState(int gaodeState)
    {
        this.gaodeState = gaodeState;
    }

    public String getGaodeCode()
    {
        return gaodeCode;
    }

    public void setGaodeCode(String gaodeCode)
    {
        this.gaodeCode = gaodeCode;
    }
    
    
}
