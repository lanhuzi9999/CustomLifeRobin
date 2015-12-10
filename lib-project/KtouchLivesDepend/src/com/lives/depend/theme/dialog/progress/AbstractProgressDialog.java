package com.lives.depend.theme.dialog.progress;

import com.lives.depend.theme.dialog.AbstractDialog;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;

/**
 * ****************************************************************<br>
 * 文件名称 : AbstractProgressDialog.java<br>
 * 作 者 : zjh<br>
 * 创建时间 : 2015-11-27 下午7:38:07<br>
 * 文件描述 : ProgressDialog的基类<br>
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-27 1.00 初始版本<br>
 ***************************************************************** 
 */
public abstract class AbstractProgressDialog extends AbstractDialog
{
    protected Context mContext = null;

    public abstract void setProgressStyle(int style);

    public abstract void setMax(int max);

    public abstract int getMax();

    public abstract void setButton(int whichButton, CharSequence text, final OnClickListener listener);

    public abstract void setProgress(int value);
}
