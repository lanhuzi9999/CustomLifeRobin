package so.contacts.hub.basefunction.operate.cms.bean;

import java.io.Serializable;

/**
 * ****************************************************************
 * 文件名称 : ContentBean.java
 * 作 者 :   Robin
 * 创建时间 : 2015-11-14 下午4:42:45
 * 文件描述 : 商品基类
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-14 1.00 初始版本
 *****************************************************************
 */
public class ContentBean implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String name;// 商品名称

    private String name_color;// 商品名称颜色

    private double longitude;// 经度

    private double latitude;// 纬度

    private int type;// 数据类型 1-活动 2-服务 3-商品 4-搜索

    private int open_type;// 开放平台数据类型

    private ClickAction click_action;// 点击事件

    private String description;// 内容描述

    private int sort;// 排序

    private float price;// 售价

    private String price_unit;// 商品价格单位
    
    private int open_goods_id;// 开放平台接入的商品id

    private int open_service_id;// 开放平台接入的服务id

    private String price_desc_color;// 价钱描述颜色

    private String description_color;// 内容描述的颜色

    private int open_cp_id;// 开放平台接入的内容提供者id

    private int sold_count;// 已售数量

    private float source_price;// 原价

    private String price_desc;// 价格描述

    private String title;// 标题

    private String title_color;//标题颜色
    
    private String img_url;// 图片url

    private String price_color;// 价格颜色
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getName_color()
    {
        return name_color;
    }
    public void setName_color(String name_color)
    {
        this.name_color = name_color;
    }
    public double getLongitude()
    {
        return longitude;
    }
    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }
    public double getLatitude()
    {
        return latitude;
    }
    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }
    public int getOpen_type()
    {
        return open_type;
    }
    public void setOpen_type(int open_type)
    {
        this.open_type = open_type;
    }
    public ClickAction getClick_action()
    {
        return click_action;
    }
    public void setClick_action(ClickAction click_action)
    {
        this.click_action = click_action;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public int getSort()
    {
        return sort;
    }
    public void setSort(int sort)
    {
        this.sort = sort;
    }
    public float getPrice()
    {
        return price;
    }
    public void setPrice(float price)
    {
        this.price = price;
    }
    public int getOpen_goods_id()
    {
        return open_goods_id;
    }
    public void setOpen_goods_id(int open_goods_id)
    {
        this.open_goods_id = open_goods_id;
    }
    public int getOpen_service_id()
    {
        return open_service_id;
    }
    public void setOpen_service_id(int open_service_id)
    {
        this.open_service_id = open_service_id;
    }
    public String getPrice_desc_color()
    {
        return price_desc_color;
    }
    public void setPrice_desc_color(String price_desc_color)
    {
        this.price_desc_color = price_desc_color;
    }
    public String getDescription_color()
    {
        return description_color;
    }
    public void setDescription_color(String description_color)
    {
        this.description_color = description_color;
    }
    public int getOpen_cp_id()
    {
        return open_cp_id;
    }
    public void setOpen_cp_id(int open_cp_id)
    {
        this.open_cp_id = open_cp_id;
    }
    public int getSold_count()
    {
        return sold_count;
    }
    public void setSold_count(int sold_count)
    {
        this.sold_count = sold_count;
    }
    public float getSource_price()
    {
        return source_price;
    }
    public void setSource_price(float source_price)
    {
        this.source_price = source_price;
    }
    public String getPrice_desc()
    {
        return price_desc;
    }
    public void setPrice_desc(String price_desc)
    {
        this.price_desc = price_desc;
    }
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public String getImg_url()
    {
        return img_url;
    }
    public void setImg_url(String img_url)
    {
        this.img_url = img_url;
    }
    public String getPrice_color()
    {
        return price_color;
    }
    public void setPrice_color(String price_color)
    {
        this.price_color = price_color;
    }
    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }
    
    @Override
    public String toString()
    {
        return "ContentBean [open_cp_id=" + open_cp_id + ", open_service_id=" + open_service_id + ", open_goods_id="
                + open_goods_id + ", open_type=" + open_type + ", type=" + type + ", sort=" + sort + ", img_url="
                + img_url + ", title=" + title + ", description=" + description + ", price=" + price
                + ", source_price=" + source_price + ", sold_count=" + sold_count + ", price_unit=" + price_unit
                + ", latitude=" + latitude + ", longitude=" + longitude + ", click_action=" + click_action
                + ", title_color=" + title_color + ", description_color=" + description_color + ", name=" + name
                + ", name_color=" + name_color + ", price_color=" + price_color + ", price_desc=" + price_desc
                + ", price_desc_color=" + price_desc_color + "]";
    }
}
