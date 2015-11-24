package so.contacts.hub.basefunction.net.bean;

import so.contacts.hub.ContactsApp;
import android.content.Context;

public abstract class BaseOldRequestData<T extends BaseResponseData>
{
    public String pt_token;// 葡萄令牌

    public String action_code;// [string][not
                              // null][指令码,例如登录获取验证码，修改密码获取验证码是两个不同的指令]

    public String token;// [string][null able][用户令牌,云端生成]

    public String mcode;// [string][null able][用户设备ID，云端生成]

    public UaInfo ua;// [UaInfo][null able][Ua系统]

    public long timestamp;// [long][not null][时间戳：毫秒数]

    public VersionInfo version;// [VersionInfo][not null][版本结构]

    public String secret_key;// [string][not null][交互密钥]

    public int active_status;// [int][not null][0:后台运行,1:主程序]

    public String channel_no = "1001";// :[String][not null][渠道号,无渠道号为PUTAO]

    public int app_id = 10; // 标识appId,当前通讯录+的appid是2; doov版本id=3 eton=4
                            // coolpad=5 葡萄生活 10 测试103

    public String device_code; // [String][not null][手机设备唯一标识]

    public int local_dual_version;// [int][not null][本地保存双卡信息版本，默认为0]

    public BaseOldRequestData(String actionCode)
    {
        Context context = ContactsApp.getInstance();
        action_code = actionCode;
    }
}
