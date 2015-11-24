package so.contacts.hub.basefunction.operate.cms.bean;

import java.io.Serializable;

public class ClickAction implements Serializable
{

    private static final long serialVersionUID = 1L;

    private String click_link; // 点击跳转url

    private String click_activity;// 点击跳转的activity

    private String show_title;// 跳转后显示的title

    private int click_type;// 点击类型:1,跳转到特定的服务页面 2,跳转到特定的H5页面

    private String cp_info;// 产品信息 json字符串格式

    private String expend_params;// 扩展信息 json格式

    public String getClick_link()
    {
        return click_link;
    }

    public void setClick_link(String click_link)
    {
        this.click_link = click_link;
    }

    public String getClick_activity()
    {
        return click_activity;
    }

    public void setClick_activity(String click_activity)
    {
        this.click_activity = click_activity;
    }

    public String getShow_title()
    {
        return show_title;
    }

    public void setShow_title(String show_title)
    {
        this.show_title = show_title;
    }

    public int getClick_type()
    {
        return click_type;
    }

    public void setClick_type(int click_type)
    {
        this.click_type = click_type;
    }

    public String getCp_info()
    {
        return cp_info;
    }

    public void setCp_info(String cp_info)
    {
        this.cp_info = cp_info;
    }

    public String getExpend_params()
    {
        return expend_params;
    }

    public void setExpend_params(String expend_params)
    {
        this.expend_params = expend_params;
    }
}
