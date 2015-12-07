package so.contacts.hub.basefunction.account.bean;

/**
 * description:
 * 
 * @author gwq
 * @since 2015/5/9
 */
public class BasicUserInfoBean
{
    private int has_set_password;

    private String head_pic;

    private String birthday;

    private int gender;

    private String city;

    private String favorable;

    public BasicUserInfoBean()
    {
    }

    public BasicUserInfoBean(int has_set_password, String head_pic, String city, int gender, String birthday)
    {
        this.has_set_password = has_set_password;
        this.head_pic = head_pic;
        this.city = city;
        this.gender = gender;
        this.birthday = birthday;
    }

    public int getHas_set_password()
    {
        return has_set_password;
    }

    public void setHas_set_password(int has_set_password)
    {
        this.has_set_password = has_set_password;
    }

    public String getHead_pic()
    {
        if ("null".equals(head_pic))
        {
            head_pic = null;
        }
        return head_pic;
    }

    public void setHead_pic(String head_pic)
    {
        this.head_pic = head_pic;
    }

    public String getBirthday()
    {
        if ("null".equals(birthday))
        {
            return null;
        }
        return birthday;
    }

    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }

    public int getGender()
    {
        return gender;
    }

    public void setGender(int gender)
    {
        this.gender = gender;
    }

    public String getCity()
    {
        if ("null".equals(city))
        {
            return null;
        }
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getFavorable()
    {
        return favorable;
    }

    public void setFavorable(String favorable)
    {
        this.favorable = favorable;
    }
}
