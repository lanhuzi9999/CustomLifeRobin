package com.lives.depend.theme.dialog.progress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;

/**
 * ****************************************************************<br>
 * 文件名称 : DefaultProgressDialog.java<br>
 * 作 者 : zjh<br>
 * 创建时间 : 2015-11-27 下午7:43:22<br>
 * 文件描述 : 默认ProgressDialog样式<br>
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-27 1.00 初始版本<br>
 ***************************************************************** 
 */
public class DefaultProgressDialog extends AbstractProgressDialog
{

    private ProgressDialog mProgressDialog = null;

    public DefaultProgressDialog(Context context, int theme, int resLayout)
    {
        this.mContext = context;
        init(resLayout, theme);
    }

    private void init(int resLayout, int theme)
    {
        mProgressDialog = new ProgressDialog(mContext);
    }

    @Override
    public void show()
    {
        // TODO Auto-generated method stub
        mProgressDialog.show();
    }

    @Override
    public boolean isShowing()
    {
        // TODO Auto-generated method stub
        return mProgressDialog.isShowing();
    }

    @Override
    public void dismiss()
    {
        // TODO Auto-generated method stub
        mProgressDialog.dismiss();
    }
    
    @Override
    public void cancel()
    {
        // TODO Auto-generated method stub
        mProgressDialog.cancel();
    }

    @Override
    public void setTitle(CharSequence title)
    {
        // TODO Auto-generated method stub
        mProgressDialog.setTitle(title);
    }

    @Override
    public void setTitle(int title)
    {
        // TODO Auto-generated method stub
        mProgressDialog.setTitle(title);
    }

    @Override
    public void setMessage(int message)
    {
        // TODO Auto-generated method stub
        mProgressDialog.setMessage(mContext.getText(message));
    }

    @Override
    public void setMessage(CharSequence message)
    {
        // TODO Auto-generated method stub
        mProgressDialog.setMessage(message);
    }

    @Override
    public void setCancelable(boolean flag)
    {
        // TODO Auto-generated method stub
        mProgressDialog.setCancelable(flag);
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel)
    {
        // TODO Auto-generated method stub
        mProgressDialog.setCanceledOnTouchOutside(cancel);
    }

    @Override
    public void setOnKeyListener(OnKeyListener onKeyListener)
    {
        // TODO Auto-generated method stub
        mProgressDialog.setOnKeyListener(onKeyListener);
    }

    @Override
    public void setOnCancelListener(OnCancelListener listener)
    {
        // TODO Auto-generated method stub
        mProgressDialog.setOnCancelListener(listener);
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener)
    {
        // TODO Auto-generated method stub
        mProgressDialog.setOnDismissListener(listener);
    }

    @Override
    public void setProgressStyle(int style)
    {
        // TODO Auto-generated method stub
        mProgressDialog.setProgressStyle(style);
    }

    @Override
    public void setMax(int max)
    {
        // TODO Auto-generated method stub
        mProgressDialog.setMax(max);
    }

    @Override
    public int getMax()
    {
        // TODO Auto-generated method stub
        return mProgressDialog.getMax();
    }

    @Override
    public void setButton(int whichButton, CharSequence text, final OnClickListener listener)
    {
        // TODO Auto-generated method stub
        mProgressDialog.setButton(whichButton, text, listener);
    }

    @Override
    public void setProgress(int value)
    {
        // TODO Auto-generated method stub
        mProgressDialog.setProgress(value);
    }
}
