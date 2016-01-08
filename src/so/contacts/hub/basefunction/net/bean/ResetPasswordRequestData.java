package so.contacts.hub.basefunction.net.bean;

import so.contacts.hub.basefunction.config.Config;

/**
 * ****************************************************************
 * 文件名称 : ResetPasswordRequestData.java
 * 作 者 :   Robin
 * 创建时间 : 2016-1-7 上午10:49:04
 * 文件描述 : 设置密码请求
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2016-1-7 1.00 初始版本
 *****************************************************************
 */
public class ResetPasswordRequestData extends BaseOldRequestData<ResetPasswordResponseData>
{
    /** 验证码 */
    public String check_code;

    /** 账户来源 */
    public int accSource;

    /** 账户类型 */
    public int accType;

    /** 手机号码 */
    public String mobile;

    /** 新设置的密码 */
    public String password;

    public ResetPasswordRequestData(String mobile, String password, String captchar)
    {
        super("200006");
        this.accSource = 1;
        this.accType = 1;
        this.mobile = mobile;
        this.password = password;
        this.check_code = captchar;
    }

    @Override
    protected ResetPasswordResponseData getNewInstance()
    {
        return new ResetPasswordResponseData();
    }

    @Override
    protected ResetPasswordResponseData fromJson(String content)
    {
        return Config.mGson.fromJson(content, ResetPasswordResponseData.class);
    }
}
