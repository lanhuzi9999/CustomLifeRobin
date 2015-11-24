package so.contacts.hub.basefunction.operate.cms.bean;

import java.util.List;

/**
 * ****************************************************************
 * 文件名称 : ContentConfig.java
 * 作 者 :   Robin
 * 创建时间 : 2015-11-14 下午4:35:39
 * 文件描述 : 首页商品流配置
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-14 1.00 初始版本
 *****************************************************************
 */
public class ContentConfig
{
    private List<ContentView> content_views;

    private ContentStreamConfig content_sc;  //客户端自取内容流配置信息

    /* 
     * add by zj 2015-6-6 start 
     * 更多精选页的分类配置
     */
    private List<ChoicenesCategory> content_choiceness;  
    //end 2015-6-6 by zj
    
    public List<ContentView> getContent_views()
    {
        return content_views;
    }

    public void setContent_views(List<ContentView> content_views)
    {
        this.content_views = content_views;
    }

    public ContentStreamConfig getContent_sc()
    {
        return content_sc;
    }

    public void setContent_sc(ContentStreamConfig content_sc)
    {
        this.content_sc = content_sc;
    }

    public List<ChoicenesCategory> getContent_choiceness()
    {
        return content_choiceness;
    }

    public void setContent_choiceness(List<ChoicenesCategory> content_choiceness)
    {
        this.content_choiceness = content_choiceness;
    }
}
