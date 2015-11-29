package so.contacts.hub.basefunction.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TelAreaUtil
{
    private static TelAreaUtil telAreaUtil = null;

    private TelAreaUtil()
    {

    }

    public static TelAreaUtil getInstance()
    {
        if (telAreaUtil == null)
        {
            telAreaUtil = new TelAreaUtil();
        }
        return telAreaUtil;
    }

    /**
     * 检查是否是合法的电话号码
     */
    public boolean isValidMobile(String mobile)
    {
        /*
         * 中国移动号段：134、135、136、137、138、139、150、151、152、157、158、159、182、183、184、187
         * 、188、178(4G)、147(上网卡) 中国电信号段：133、153、180、181、189 、177(4G)
         * 中国联通号段：130、131、132、155、156、185、186、176(4G)、145(上网卡) 卫星通信号段：1349
         * 虚拟运营商号段：170
         */
        // 过滤总号段： 13[0-9] , 14[5 7] , 15[0-9] , 17[0 6 7 8] 18[0-9]
        String MOBILE = "^1(3[0-9]|4[57]|5[0-9]|7[0678]|8[0-9])\\d{8}$";
        Pattern pattern = Pattern.compile(MOBILE);
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }
}
