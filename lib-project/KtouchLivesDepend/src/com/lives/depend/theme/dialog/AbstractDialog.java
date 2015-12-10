package com.lives.depend.theme.dialog;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;

/**
 * ****************************************************************<br>
 * 文件名称 : AbstractDialog.java<br>
 * 作 者 : zjh<br>
 * 创建时间 : 2015-11-27 下午7:28:04<br>
 * 文件描述 : 对话框的方法抽象基类<br>
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-27 1.00 初始版本<br>
 ***************************************************************** 
 */
public abstract class AbstractDialog
{
    /**
     * 显示对话框
     */
    public abstract void show();

    /**
     * 对话框是否显示
     */
    public abstract boolean isShowing();

    /**
     * 隐藏对话框
     */
    public abstract void dismiss();

    /**
     * 取消对话框
     */
    public abstract void cancel();

    /**
     * 设置标题
     */
    public abstract void setTitle(CharSequence title);

    public abstract void setTitle(int title);

    /**
     * 设置对话框内容
     */
    public abstract void setMessage(int message);

    public abstract void setMessage(CharSequence message);

    /**
     * 设置对话框特性
     */
    public abstract void setCancelable(boolean flag);

    public abstract void setCanceledOnTouchOutside(boolean cancel);

    public abstract void setOnKeyListener(DialogInterface.OnKeyListener listener);

    public abstract void setOnCancelListener(OnCancelListener listener);

    public abstract void setOnDismissListener(OnDismissListener listener);
}
