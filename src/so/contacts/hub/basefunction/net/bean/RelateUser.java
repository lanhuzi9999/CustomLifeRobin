package so.contacts.hub.basefunction.net.bean;

import java.io.Serializable;


/**
 * ****************************************************************
 * 文件名称 : RelateUserResponse.java
 * 作 者 :   Robin
 * 创建时间 : 2015-11-25 上午12:38:00
 * 文件描述 : 用户鉴权信息
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-25 1.00 初始版本
 *****************************************************************
 */
public class RelateUser implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 创建用户来源： 0 表使用设备创建, 1 表使用手机创建, 2 表使用酷派创建
     */
    public static final int SOURCE_DEVICE = 0;

    public static final int SOURCE_PHONE = 1;

    public static final int SOURCE_FACTORY = 2;

    /**
     * 创建的用户类型:0 表临时用户, 1 表普通绑定用户, 2 表平台绑定用户
     */
    public static final int TYPE_DEVICE = 0;

    public static final int TYPE_PHONE = 1;

    public static final int TYPE_FACTORY = 2;

    public String accName;//帐户名

    public int accSource;//帐户来源

    public int accType;//帐户类型

    public String accMsg;//帐户具体信息
    
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("RelateUser accName =").append(accName).
        append(" accSource=").append(accSource).
        append(" accType =").append(accType).append(" accMsg=").append(accMsg);
        return sb.toString();
    }
}
