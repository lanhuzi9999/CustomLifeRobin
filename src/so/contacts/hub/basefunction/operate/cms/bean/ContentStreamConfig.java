package so.contacts.hub.basefunction.operate.cms.bean;


/**
 * 客户端自取内容流配置信息：用于附近精选中 本地搜索配置信息
 * 
 * @author hyl 2015-3-16
 */
public class ContentStreamConfig
{
    private String title; // 团购跳转要显示的标题可配

    private String city; // 包含团购信息的城市名称

    private String destination_city;// 指定目的地城市名称，适用于“酒店”、“旅游”等分类

    private float latitude; // 纬度坐标，须与经度坐标同时传入

    private float longitude; // 经度坐标，须与纬度坐标同时传入

    private int radius; // 搜索半径，单位为米，最小值1，最大值5000，如不传入默认为1000

    private String region; // 包含团购信息的城市区域名

    private String category; // 包含团购信息的分类名

    private int is_local; // 根据是否是本地单来筛选返回的团购，1:是，0:不是

    private String keyword; // 关键词，搜索范围包括商户名、商品名、地址等

    private int sort; // 结果排序，1:默认，2:价格低优先，3:价格高优先，4:购买人数多优先，5:最新发布优先，6:即将结束优先，7:离经纬度坐标距离近优先

    private int limit; // 每页返回的团单结单页果条目数上限，最小值1，最大值40，如不传入默认为20

    private int page=1; // 页码，如不传入默认为1，即第一页

    /** 用于搜索条件改变后的监听 */
    private OnSearchConditionChangedListener changedListener;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getDestination_city()
    {
        return destination_city;
    }

    public void setDestination_city(String destination_city)
    {
        this.destination_city = destination_city;
    }

    public float getLatitude()
    {
        return latitude;
    }

    public void setLatitude(float latitude)
    {
        this.latitude = latitude;
    }

    public float getLongitude()
    {
        return longitude;
    }

    public void setLongitude(float longitude)
    {
        this.longitude = longitude;
    }

    public int getRadius()
    {
        return radius;
    }

    public void setRadius(int radius)
    {
        this.radius = radius;
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public int getIs_local()
    {
        return is_local;
    }

    public void setIs_local(int is_local)
    {
        this.is_local = is_local;
    }

    public String getKeyword()
    {
        return keyword;
    }

    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
    }

    public int getSort()
    {
        return sort;
    }

    public void setSort(int sort)
    {
        this.sort = sort;
    }

    public int getLimit()
    {
        return limit;
    }

    public void setLimit(int limit)
    {
        this.limit = limit;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public OnSearchConditionChangedListener getChangedListener()
    {
        return changedListener;
    }

    public void setChangedListener(OnSearchConditionChangedListener changedListener)
    {
        this.changedListener = changedListener;
    }

    /**
     * 用于搜索条件改变后的监听
     * 
     * @author zj 2015-3-16
     * 
     */
    public interface OnSearchConditionChangedListener
    {

        /**
         * 搜索条件改变
         * 
         * @param conditions
         */
        public void onChanged(ContentStreamConfig config);

    }
}
