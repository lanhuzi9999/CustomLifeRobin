package so.contacts.hub.basefunction.operate.cms.bean;

import java.io.Serializable;

/**
 * ****************************************************************
 * 文件名称 : CMSResponseBaseData.java
 * 作 者 :   Administrator
 * 创建时间 : 2015-9-8 下午9:07:19
 * 文件描述 : CMS 常用服务接口 返回数据
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-9-8 1.00 初始版本
 *****************************************************************
 */
public class CMSResponseBaseData implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int ret_code;// 返回码

    private int data_version;// 版本号

    private String data = null;// 返回的数值

    public int getRet_code()
    {
        return ret_code;
    }

    public void setRet_code(int ret_code)
    {
        this.ret_code = ret_code;
    }

    public int getData_version()
    {
        return data_version;
    }

    public void setData_version(int data_version)
    {
        this.data_version = data_version;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }
}
