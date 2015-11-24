package so.contacts.hub.basefunction.net.bean;

import so.contacts.hub.basefunction.utils.SystemUtil;

public class UaInfo
{
    public String system_name;// [not null][系统名字android/ios]

    public String system_version;// [not null][当前系统版本]

    public String band;// [not null][手机型号]

    public UaInfo()
    {
        system_name = "android";
        system_version = SystemUtil.getOS();
        band = SystemUtil.getMachine();
    }
}
