package com.lives.depend.payment.wechat;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class WeChatConfig
{
    /**
     * 微信支付 APPID
     */
    public static String WX_PAY_APPID = "wx2fce80711c310445";

    /**
     * 获取微信支付渠道标识 add by hyl 2015-1-4
     * 
     * @param context
     */
    public static String getWxChannelNo(Context context)
    {
        String wxChannelNo = "";
        try
        {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            if (ai.metaData.containsKey("WX_CHANNEL_NO"))
            {
                wxChannelNo = ai.metaData.get("WX_CHANNEL_NO").toString();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return wxChannelNo;
    }

    public static interface WeChat
    {
        public static final int Success = 0;

        public static final int Failed = -1;

        public static final int Cancel = -2;
    }
}
