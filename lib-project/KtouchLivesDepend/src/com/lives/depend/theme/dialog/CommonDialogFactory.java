package com.lives.depend.theme.dialog;

import android.content.Context;

import com.lives.depend.R;
import com.lives.depend.theme.ThemeController;
import com.lives.depend.theme.dialog.progress.AbstractProgressDialog;
import com.lives.depend.theme.dialog.progress.CustomProgressDialog;
import com.lives.depend.theme.dialog.progress.DefaultProgressDialog;

/**
 * ****************************************************************<br>
 * 文件名称 : CommonDialogFactory.java<br>
 * 作 者 : zjh<br>
 * 创建时间 : 2015-11-24 上午10:09:41<br>
 * 文件描述 : 公共对话框生产类<br>
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-24 1.00 初始版本<br>
 ***************************************************************** 
 */
public class CommonDialogFactory
{

    /**
     * 获取Dialog
     */
    public static CommonDialog getDialog(Context context, int dialogStyle)
    {
        int dialogLayout = -1;
        int showStyle = CommonDialog.DIALOG_SHOW_STYLE_CENTER;
        if (dialogStyle == R.style.Theme_Ptui_Dialog_ListView)
        {
            // 获取【List列表】对话框
            dialogLayout = R.layout.putao_common_list_dialog;
        }
        else if (dialogStyle == R.style.Theme_Ptui_Dialog_OkCancel)
        {
            // 获取【确定|取消】对话框
            dialogLayout = R.layout.putao_common_ok_cancel_dialog;
        }
        else if (dialogStyle == R.style.Theme_Ptui_Dialog_Ok)
        {
            // 获取【确定】对话框
            dialogLayout = R.layout.putao_common_ok_dialog;
        }
        else if (dialogStyle == R.style.Theme_Ptui_Dialog_Wheel)
        {
            // 获取【带Wheel控件】对话框
            dialogLayout = R.layout.putao_common_wheel_dialog;
        }
        else if (dialogStyle == R.style.Theme_Ptui_Dialog_ScrollLayout)
        {
            // 获取【“获得优惠券成功”】对话框
            dialogLayout = R.layout.putao_common_scrollview_dialog;
        }
        else if (dialogStyle == R.style.Theme_Ptui_Dialog_GridView)
        {
            // 获取【gridview】对话框
            dialogLayout = R.layout.putao_train_common_gridview_dialog;
            showStyle = CommonDialog.DIALOG_SHOW_STYLE_BOTTOM;
        }
        else if (dialogStyle == R.style.Theme_Ptui_Dialog_GridView_NoTitle)
        {
            // 获取【不带title的gridView】对话框
            dialogLayout = R.layout.putao_common_gridview_dialog;
            showStyle = CommonDialog.DIALOG_SHOW_STYLE_BOTTOM;
        }
        else if (dialogStyle == R.style.Theme_Ptui_Dialog_GridView_NoTitle_NoBottom)
        {
            // 获取【不带title ，不带按钮的，只有GridView】对话框
            dialogLayout = R.layout.putao_car_color_gridview_dialog;
            showStyle = CommonDialog.DIALOG_SHOW_STYLE_BOTTOM;
        }
        else if (dialogStyle == R.style.Theme_Ptui_Dialog_VoucherFailed)
        {
            // 获取【方法表述 获取“获得优惠券失败”对话框】对话框
            dialogLayout = R.layout.putao_getvoucher_fail_dialog;
        }
        else if (dialogStyle == R.style.Theme_Ptui_Dialog_YearAndMonth)
        {
            // 获取【日期选择：年、月】对话框
            dialogLayout = R.layout.putao_yearandmonth_dialog;
        }
        else if (dialogStyle == R.style.Theme_Ptui_Dialog_TwoGridView)
        {
            // 获取【包含垂直两个GridView效果】对话框
            dialogLayout = R.layout.putao_twogridview_dialog;
            showStyle = CommonDialog.DIALOG_SHOW_STYLE_BOTTOM;
        }
        else if (dialogStyle == R.style.Theme_Ptui_Dialog_ScreenAd)
        {
            // 获取【开屏广告】对话框
            dialogLayout = R.layout.putao_screen_ad_dialog;
            showStyle = CommonDialog.DIALOG_SHOW_STYLE_NORMAL;
        }
        else
        {
            throw new IllegalArgumentException("The dialog style do not supported.");
        }

        CommonDialog dialog = null;
        if (ThemeController.isDefaultTheme())
        {
            dialog = new DefaultStyleDialog(context, dialogStyle, dialogLayout);
        }
        else
        {
            dialog = new CustomDialog(context, dialogStyle, dialogLayout, showStyle);
        }
        return dialog;
    }

    /**
     * 获取ProgressDialog
     */
    public static AbstractProgressDialog getProgressDialog(Context context, int dialogStyle)
    {
        int dialogLayout = -1;
        boolean hasBackground = false;

        if (dialogStyle == R.style.Theme_Ptui_Dialog_Progress)
        {
            // 获取【ProgressBar 有背景】对话框
            dialogLayout = R.layout.putao_progress_dialog;
            hasBackground = true;
        }
        else if (dialogStyle == R.style.Theme_Ptui_Dialog_Progress_Transparent)
        {
            // 获取【ProgressBar 无背景】对话框
            dialogLayout = R.layout.putao_progress_dialog;
        }
        else if (dialogStyle == R.style.Theme_Ptui_Dialog_Progress_Charge)
        {
            // 获取【ProgressBar 支付结果弹出】对话框
            dialogLayout = R.layout.putao_progress_dialog;
            hasBackground = true;
        }
        else if (dialogStyle == R.style.Theme_Ptui_Dialog_Progress_Download)
        {
            // 获取【升级下载】对话框
            dialogLayout = R.layout.putao_version_update_download;
        }
        else
        {
            throw new IllegalArgumentException("The dialog style do not supported.");
        }

        AbstractProgressDialog progressDialog = null;
        if (ThemeController.isDefaultTheme())
        {
            progressDialog = new DefaultProgressDialog(context, dialogStyle, dialogLayout);
        }
        else
        {
            progressDialog = new CustomProgressDialog(context, dialogStyle, dialogLayout, hasBackground);
        }
        return progressDialog;
    }
}
