package so.contacts.hub.basefunction.account.bean;

import java.util.List;

import com.google.gson.JsonSyntaxException;

import so.contacts.hub.basefunction.config.Config;
import so.contacts.hub.basefunction.net.bean.RelateUser;
import so.contacts.hub.basefunction.net.bean.UserRegisterResponse;

/**
 * ****************************************************************
 * 文件名称 : PTUser.java
 * 作 者 :   Robin
 * 创建时间 : 2015-11-25 上午12:09:50
 * 文件描述 : 葡萄账户基类
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-25 1.00 初始版本
 *****************************************************************
 */
public class PTUser
{
    public String pt_uid;// 用户id

    public String pt_token;// 用户登录令牌，标识

    public String pt_open_token;// 提供给第三方授权的open_token

    public List<RelateUser> relateUsers;// 凭证列表
    
    public PTUser(String jsonStr)
    {
        UserRegisterResponse userRegisterResponse = null;
        try
        {
            userRegisterResponse = Config.mGson.fromJson(jsonStr, UserRegisterResponse.class);
        }
        catch (JsonSyntaxException e)
        {
            e.printStackTrace();
        }
        if(userRegisterResponse != null)
        {
            pt_uid = userRegisterResponse.pt_uid;
            pt_token = userRegisterResponse.pt_token;
            pt_open_token = userRegisterResponse.open_token;
            relateUsers = userRegisterResponse.relateUsers;
        }
    }

    public String getPt_uid()
    {
        return pt_uid;
    }

    public void setPt_uid(String pt_uid)
    {
        this.pt_uid = pt_uid;
    }

    public String getPt_token()
    {
        return pt_token;
    }

    public void setPt_token(String pt_token)
    {
        this.pt_token = pt_token;
    }

    public String getPt_open_token()
    {
        return pt_open_token;
    }

    public void setPt_open_token(String pt_open_token)
    {
        this.pt_open_token = pt_open_token;
    }

    public List<RelateUser> getRelateUsers()
    {
        return relateUsers;
    }

    public void setRelateUsers(List<RelateUser> relateUsers)
    {
        this.relateUsers = relateUsers;
    }
    
    @Override
    public String toString()
    {
        return "pt_uid: " + pt_uid + " ,pt_token: " + pt_token + " ,relateUsers: " + relateUsers;
    }
}
