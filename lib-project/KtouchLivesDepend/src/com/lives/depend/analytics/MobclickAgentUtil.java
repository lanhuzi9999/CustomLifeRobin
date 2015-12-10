package com.lives.depend.analytics;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
//import cn.jpush.android.api.JPushInterface;

import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 ************************************************ <br>
 * 文件名称: MobclickAgentUtil.java <br>
 * 版权声明: <b>深圳市葡萄信息技术有限公司</b> 版权所有 <br>
 * 创建人员: lxh <br>
 * 文件描述:
 * 统计工具聚合类，包含<b>友盟统计</b>、<b>葡萄统计</b>、<b>talkingdata统计</b>（另外还包含有<b>极光推送</b>
 * 的工具类）。备注：在友盟统计中每个应用至多添加500个自定义事件，每个event 的 key不能超过10个，每个key的取值不能超过1000个<br>
 * 修改时间: 2015-10-17 下午7:57:45 <br>
 * 修改历史: 2015-10-17 1.00 初始版本 <br>
 ************************************************* 
 */
public class MobclickAgentUtil
{

    /**
     * 是否打开葡萄统计
     */
    private static final boolean NEED_PUTAO_ANALYTICS = true;

    // 包含葡萄SDK
    public static void onEvent(Context context, String eventId)
    {
        if (context != null)
        {
            if (NEED_PUTAO_ANALYTICS)
            {
                com.putao.analytics.MobclickAgent.onEvent(context, eventId);
            }
            MobclickAgent.onEvent(context, eventId);
            TCAgent.onEvent(context, eventId);
        }
    }

    /**
     * 事件只统计在友盟上
     * 
     * @author lxh
     * @since 2015-5-29
     * @param context
     * @param eventId
     */
    public static void onEventUmeng(Context context, String eventId)
    {
        if (context != null)
        {
            MobclickAgent.onEvent(context, eventId);
        }
    }

    /**
     * 事件只统计在葡萄上
     * 
     * @author lxh
     * @since 2015-5-29
     * @param context
     * @param eventId
     */
    public static void onEventPutao(Context context, String eventId)
    {
        if (context != null)
        {
            if (NEED_PUTAO_ANALYTICS)
            {
                com.putao.analytics.MobclickAgent.onEvent(context, eventId);
            }
        }
    }

    /**
     * 统计含有父事件和子事件次数
     * 
     * @author lxh
     * @since 2015-10-17
     * @param context
     * @param eventId 父事件ID
     * @param eventLabel 子事件ID
     */
    public static void onEvent(Context context, String eventId, String eventLabel)
    {
        if (context != null)
        {
         // 处理特殊字符，友盟中不允许eventLabel有特殊字符的
            eventLabel = doSpecialChar(eventLabel);
            MobclickAgent.onEvent(context, eventId, eventLabel);
            TCAgent.onEvent(context, eventId, eventLabel);
            if (NEED_PUTAO_ANALYTICS)
            {
                HashMap<String, String> localHashMap = new HashMap<String, String>();
                localHashMap.put(eventId, (eventLabel == null) ? "" : eventLabel);
                com.putao.analytics.MobclickAgent.onEvent(context, eventId, localHashMap);
            }
        }
    }

    /**
     * 处理特殊字符
     */
    private static String doSpecialChar(String eventLabel)
    {
        if (TextUtils.isEmpty(eventLabel))
        {
            return "";
        }
        // 只允许字母和数字
        // String regEx = "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(eventLabel);
        return m.replaceAll("").trim();
    }

    /**
     * 统计含有父事件和子事件次数
     * 
     * @author lxh
     * @since 2015-10-17
     * @param context
     * @param eventId 父事件
     * @param map 友盟中key最多五个，子事件由value统计
     */
    public static void onEvent(Context context, String eventId, Map<String, String> map)
    {
        if (context != null)
        {
            if (NEED_PUTAO_ANALYTICS)
            {
                com.putao.analytics.MobclickAgent.onEvent(context, eventId, map);
            }
            MobclickAgent.onEvent(context, eventId, map);
        }
    }

    public static void onPageEnd(String arg0)
    {
        MobclickAgent.onPageEnd(arg0);
    }

    public static void onPageStart(String arg0)
    {
        MobclickAgent.onPageStart(arg0);
    }

    // 包含葡萄SDK
    public static void onPause(Context context)
    {
        if (NEED_PUTAO_ANALYTICS)
        {
            com.putao.analytics.MobclickAgent.onPause(context, false);
        }
        MobclickAgent.onPause(context);

        if (context instanceof Activity)
        {
            TCAgent.onPause((Activity) context);
//            JPushInterface.onPause(context);
        }
    }

    // 包含葡萄SDK
    public static void onResume(Context context)
    {
        if (context != null)
        {
            if (NEED_PUTAO_ANALYTICS)
            {
                com.putao.analytics.MobclickAgent.onResume(context, true);
            }
            MobclickAgent.onResume(context);

            if (context instanceof Activity)
            {
                TCAgent.onResume((Activity) context);
//                JPushInterface.onResume(context);
            }
        }
    }

    public static void setDebugMode(boolean arg0)
    {
        MobclickAgent.setDebugMode(arg0);
    }

}
