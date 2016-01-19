package so.contacts.hub.basefunction.utils;

import com.lives.depend.utils.LogUtil;

import so.contacts.hub.basefunction.operate.cms.bean.ClickAction;
import so.contacts.hub.basefunction.operate.cms.bean.CommonView;
import so.contacts.hub.basefunction.utils.constant.IntentKeyConstants;
import so.contacts.hub.services.telephone.YellowPageReChargeActivity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * ****************************************************************<br>
 * 文件名称 : ServiceClickAgentUtil.java<br>
 * 作 者 : Robin Pei<br>
 * 创建时间 : 2016-1-13 下午7:32:06<br>
 * 文件描述 : 服务点击跳转工具类<br>
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2016-1-13 1.00 初始版本<br>
 ***************************************************************** 
 */
public class ServiceClickAgentUtil
{
    private static final String TAG = "ServiceClickAgentUtil";

    /**
     * 处理常用服务的点击操作
     * 
     * @param context
     * @param commonView void
     */
    public static void doClickAction(Context context, CommonView commonView)
    {
        if (context == null || commonView == null)
        {
            return;
        }

        doClickAction(context, commonView, false);
    }

    /**
     * 点击操作，增加字段：点击进入的时候是否需要触发定位操作
     * 
     * @param context
     * @param commonView
     * @param isNeedLocation void
     */
    public static void doClickAction(Context context, CommonView commonView, boolean isNeedLocation)
    {
        if (context == null || commonView == null)
        {
            return;
        }
        doClickAction(context, commonView.getClick_action(), commonView.getId(), isNeedLocation);
    }

    /**
     * 点击操作，增加字段：clickAction， 服务id，分类id
     * 
     * @param context
     * @param clickAction
     * @param serviceId
     * @param categoryId
     * @param isNeedLocation void
     */
    public static void doClickAction(Context context, ClickAction clickAction, long serviceId, boolean isNeedLocation)
    {
        LogUtil.i(TAG, "doClickAction " + System.currentTimeMillis());
        if (context == null || clickAction == null)
        {
            return;
        }
        String clickActivity = clickAction.getClick_activity();
        // 因为代码重构，导致完整的类名可能发生变化，现做临时转换
        clickActivity = filterCurrentActivityName(clickActivity);
        if (TextUtils.isEmpty(clickActivity))
        {
            return;
        }
        // 跳转activity类型：1-本地nativie服务 2-h5服务
        int clickType = clickAction.getClick_type();
        // 服务title
        String showTitle = clickAction.getShow_title();
        // H5服务的跳转链接
        String clickLink = clickAction.getClick_link();
        // 跳转带的cp具体信息
        String cpInfo = clickAction.getCp_info();
        // 跳转扩展参数信息
        String expandParams = clickAction.getExpend_params();
        Intent intent = null;
        try
        {
            Class<?> cls = Class.forName(clickActivity);
            if (clickType == ClickAction.CLICK_TYPE_NATIVE)
            {
                // native服务
                intent = new Intent(context, cls);
            }
            else if (clickType == ClickAction.CLICK_TYPE_H5)
            {
                // h5服务,要判断cls是不是H5activity的子类,暂且不写
                intent = new Intent(context, cls);
                // H5页的url参数
                intent.putExtra("url", clickLink);
            }

            if (intent != null)
            {
                // 添加nativie和H5需要的公共参数
                intent.putExtra(IntentKeyConstants.EXTRA_CLICK_INTENT_TITLE, showTitle);
                intent.putExtra(IntentKeyConstants.EXTRA_CLICK_INTENT_PARAMS, expandParams);
                intent.putExtra(IntentKeyConstants.EXTRA_CP_INFO_PARAMS, cpInfo);
                intent.putExtra(IntentKeyConstants.EXTRA_NEED_INIT_LOCATION, isNeedLocation);
                intent.putExtra(IntentKeyConstants.EXTRA_SERVICEID_PARAMS, serviceId);
                context.startActivity(intent);
            }

        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    public static String filterCurrentActivityName(String activityName)
    {
        if (TextUtils.isEmpty(activityName))
        {
            return activityName;
        }
        if (activityName.endsWith(YellowPageReChargeActivity.class.getSimpleName()))
        {
            return YellowPageReChargeActivity.class.getName();
        }
        return activityName;
    }
}
