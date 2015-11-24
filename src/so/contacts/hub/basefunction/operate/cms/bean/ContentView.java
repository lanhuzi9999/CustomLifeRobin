package so.contacts.hub.basefunction.operate.cms.bean;

import java.util.List;

public class ContentView
{
    private int type;//数据类型 1-活动 2-服务 3-商品 4-搜索

    private int sort;//排序

    private List<ContentBean> content_beans;//商品列表

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getSort()
    {
        return sort;
    }

    public void setSort(int sort)
    {
        this.sort = sort;
    }

    public List<ContentBean> getContent_beans()
    {
        return content_beans;
    }

    public void setContent_beans(List<ContentBean> content_beans)
    {
        this.content_beans = content_beans;
    }

}
