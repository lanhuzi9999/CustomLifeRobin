package so.contacts.hub.basefunction.account.bean;

import java.io.Serializable;

public class AccountInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String name;

    private String username;

    private String uid;

    private String nickname;

    private String avatar_url;

    private String avatar_hd_url;

    private String mobile;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getAvatar_url()
    {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url)
    {
        this.avatar_url = avatar_url;
    }

    public String getAvatar_hd_url()
    {
        return avatar_hd_url;
    }

    public void setAvatar_hd_url(String avatar_hd_url)
    {
        this.avatar_hd_url = avatar_hd_url;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("name: ").append(name).append(",username: ").append(username).append(",uid: ").append(uid)
                .append(", nickname: ").append(nickname).append(", avatar_url: ").append(avatar_url)
                .append(", avatar_hd_url: ").append(avatar_hd_url).append(", mobile: ").append(mobile);
        return buffer.toString();
    }

}
