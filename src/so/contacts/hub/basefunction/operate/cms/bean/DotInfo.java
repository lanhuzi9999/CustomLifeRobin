package so.contacts.hub.basefunction.operate.cms.bean;

import java.io.Serializable;

public class DotInfo implements Serializable
{

    private static final long serialVersionUID = 1L;

    private String img_url;// 背景图片url

    private String dot_text;// 气泡文字描述信息

    private String dot_text_bg_color; // 气泡背景色,默认为红色

    private int dot_style = -1; // 默认气泡类型 1：热 2：惠 3：团 4：荐

    private long end_time = -1; // 打点或者气泡消失时间

    public String getImg_url()
    {
        return img_url;
    }

    public void setImg_url(String img_url)
    {
        this.img_url = img_url;
    }

    public String getDot_text()
    {
        return dot_text;
    }

    public void setDot_text(String dot_text)
    {
        this.dot_text = dot_text;
    }

    public String getDot_text_bg_color()
    {
        return dot_text_bg_color;
    }

    public void setDot_text_bg_color(String dot_text_bg_color)
    {
        this.dot_text_bg_color = dot_text_bg_color;
    }

    public int getDot_style()
    {
        return dot_style;
    }

    public void setDot_style(int dot_style)
    {
        this.dot_style = dot_style;
    }

    public long getEnd_time()
    {
        return end_time;
    }

    public void setEnd_time(long end_time)
    {
        this.end_time = end_time;
    }

}
