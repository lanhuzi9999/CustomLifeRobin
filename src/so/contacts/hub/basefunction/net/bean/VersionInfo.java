package so.contacts.hub.basefunction.net.bean;

public class VersionInfo
{
    public String app_version;// [string][not null][应用程序版本]

    public String mobile_no_version;// [string][not null][号段版本]

    public String public_sns_version;// [String][not
                                     // null][公用账号版本,若客户端当前没有存储公用账号，则提交”null”]

    public VersionInfo(String app_version, String mobile_no_version, String public_sns_version)
    {
        this.app_version = app_version;
        this.mobile_no_version = mobile_no_version;
        this.public_sns_version = public_sns_version;
    }
}
