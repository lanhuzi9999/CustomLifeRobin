package so.contacts.hub.basefunction.operate.cms.bean;

import java.io.Serializable;

public class CommonView implements Serializable
{

    private static final long serialVersionUID = 1L;

    private DotInfo dot_info; // 打点信息

    private String desc; // 服务描述

    private int sort; // 服务排序字段

    private String icon_url; // 服务图标url，有本地资源取本地资源，否则取网络资源

    private long open_service_id; // 开放平台接入的服务id

    private long open_cp_id; // 开放平台接入的cp id

    private int open_type; // 开放平台接入的类别，0：服务，1：商品

    private String search_keyword; // 搜索关键字

    private int id; // 功能服务id

    private String name_color; // 服务名字颜色

    private ClickAction click_action; // 点击操作处理

    private String desc_color; // 描述字体颜色

    private String name; // 服务名

    private String small_icon_url; // 小图标url

    private long open_goods_id; // 开放平台接入的商品id

    public DotInfo getDot_info()
    {
        return dot_info;
    }

    public void setDot_info(DotInfo dot_info)
    {
        this.dot_info = dot_info;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public int getSort()
    {
        return sort;
    }

    public void setSort(int sort)
    {
        this.sort = sort;
    }

    public String getIcon_url()
    {
        return icon_url;
    }

    public void setIcon_url(String icon_url)
    {
        this.icon_url = icon_url;
    }

    public long getOpen_service_id()
    {
        return open_service_id;
    }

    public void setOpen_service_id(long open_service_id)
    {
        this.open_service_id = open_service_id;
    }

    public long getOpen_cp_id()
    {
        return open_cp_id;
    }

    public void setOpen_cp_id(long open_cp_id)
    {
        this.open_cp_id = open_cp_id;
    }

    public int getOpen_type()
    {
        return open_type;
    }

    public void setOpen_type(int open_type)
    {
        this.open_type = open_type;
    }

    public String getSearch_keyword()
    {
        return search_keyword;
    }

    public void setSearch_keyword(String search_keyword)
    {
        this.search_keyword = search_keyword;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName_color()
    {
        return name_color;
    }

    public void setName_color(String name_color)
    {
        this.name_color = name_color;
    }

    public ClickAction getClick_action()
    {
        return click_action;
    }

    public void setClick_action(ClickAction click_action)
    {
        this.click_action = click_action;
    }

    public String getDesc_color()
    {
        return desc_color;
    }

    public void setDesc_color(String desc_color)
    {
        this.desc_color = desc_color;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSmall_icon_url()
    {
        return small_icon_url;
    }

    public void setSmall_icon_url(String small_icon_url)
    {
        this.small_icon_url = small_icon_url;
    }

    public long getOpen_goods_id()
    {
        return open_goods_id;
    }

    public void setOpen_goods_id(long open_goods_id)
    {
        this.open_goods_id = open_goods_id;
    }
}
