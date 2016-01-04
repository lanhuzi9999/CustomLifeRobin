package com.lives.depend.theme.dialog.progress;

import com.lives.depend.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * ****************************************************************<br>
 * 文件名称 : CustomProgressDialog.java<br>
 * 作 者 : zjh<br>
 * 创建时间 : 2015-11-27 下午8:32:25<br>
 * 文件描述 : 自定义样式的ProgressDialog<br>
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-27 1.00 初始版本<br>
 ***************************************************************** 
 */
public class CustomProgressDialog extends AbstractProgressDialog
{
    private Dialog mDialog = null;

    private boolean mHasBackground = true; // 是否需要背景

    private TextView mTitleTv;

    private TextView mMessageTv = null;

    private ProgressBar mProgressBar = null;

    private TextView mProgressTxt = null;

    private TextView mOkBtn, mCancelBtn;

    private ImageView mProgressImage;

    public CustomProgressDialog(Context context, int theme, int resLayout, boolean hasBackground)
    {
        this.mContext = context;
        init(resLayout, theme);
    }

    private void init(int resLayout, int theme)
    {
        mDialog = new Dialog(mContext, theme);
        mDialog.setContentView(resLayout);

        mTitleTv = (TextView) mDialog.findViewById(R.id.dialog_title_tv);
        if (resLayout == R.layout.putao_version_update_download)
        {
            mProgressBar = (ProgressBar) mDialog.findViewById(R.id.dialog_update_progress);
            mProgressTxt = (TextView) mDialog.findViewById(R.id.dialog_update_progress_txt);
            mCancelBtn = (TextView) mDialog.findViewById(R.id.dialog_bootom_cancel_btn);
        }
        else if (resLayout == R.layout.putao_progress_dialog)
        {
            //设置dialog不能获取焦点
            Window dialogWindow = mDialog.getWindow();
            WindowManager.LayoutParams lParams = dialogWindow.getAttributes();
            lParams.dimAmount = 0.1f;
            lParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            dialogWindow.setAttributes(lParams);
            
            mMessageTv = (TextView) mDialog.findViewById(R.id.message_tv);
            View dialog = mDialog.findViewById(R.id.putao_progress_dialog);
            View dialogWithbg = mDialog.findViewById(R.id.putao_progress_dialog_with_bg);
            if (mHasBackground)
            {
                mProgressImage = (ImageView) mDialog.findViewById(R.id.putao_progress_image);
                dialogWithbg.setVisibility(View.VISIBLE);
                dialog.setVisibility(View.GONE);
            }
            else
            {
                dialogWithbg.setVisibility(View.GONE);
                dialog.setVisibility(View.VISIBLE);
                dialog.setBackgroundResource(R.color.putao_transparent);
                mMessageTv.setTextColor(mContext.getResources().getColor(R.color.putao_progress_dialog_text_grey));
            }
            setCanceledOnTouchOutside(true);
        }
    }

    @Override
    public void show()
    {
        // TODO Auto-generated method stub
        mDialog.show();
    }

    @Override
    public boolean isShowing()
    {
        // TODO Auto-generated method stub
        return mDialog.isShowing();
    }

    @Override
    public void dismiss()
    {
        // TODO Auto-generated method stub
        if (mContext != null && !((Activity) mContext).isFinishing())
        {
            mDialog.dismiss();
        }
    }
    
    @Override
    public void cancel()
    {
        // TODO Auto-generated method stub
        mDialog.cancel();
    }

    @Override
    public void setTitle(CharSequence title)
    {
        // TODO Auto-generated method stub
        if (mTitleTv != null)
        {
            mTitleTv.setText(title);
        }
    }

    @Override
    public void setTitle(int title)
    {
        // TODO Auto-generated method stub
        if (mTitleTv != null)
        {
            mTitleTv.setText(title);
        }
    }

    @Override
    public void setMessage(int message)
    {
        // TODO Auto-generated method stub
        if (mMessageTv != null && message > 0)
        {
            mMessageTv.setText(message);
            mMessageTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setMessage(CharSequence message)
    {
        // TODO Auto-generated method stub
        if (mMessageTv != null)
        {
            mMessageTv.setText(message);
        }
    }

    @Override
    public void setCancelable(boolean flag)
    {
        // TODO Auto-generated method stub
        mDialog.setCancelable(flag);
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel)
    {
        // TODO Auto-generated method stub
        mDialog.setCanceledOnTouchOutside(cancel);
    }

    @Override
    public void setOnKeyListener(OnKeyListener onKeyListener)
    {
        // TODO Auto-generated method stub
        mDialog.setOnKeyListener(onKeyListener);
    }

    @Override
    public void setOnCancelListener(OnCancelListener listener)
    {
        // TODO Auto-generated method stub
        mDialog.setOnCancelListener(listener);
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener)
    {
        // TODO Auto-generated method stub
        mDialog.setOnDismissListener(listener);
    }

    @Override
    public void setProgressStyle(int style)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setMax(int max)
    {
        // TODO Auto-generated method stub
        if (mProgressBar != null)
        {
            mProgressBar.setMax(max);
        }
    }

    @Override
    public int getMax()
    {
        // TODO Auto-generated method stub
        if (mProgressBar != null)
        {
            return mProgressBar.getMax();
        }
        return 0;
    }

    @Override
    public void setButton(int whichButton, CharSequence text, final OnClickListener listener)
    {
        // TODO Auto-generated method stub
        View view = mDialog.findViewById(R.id.dialog_bottom_layout);
        if (view != null)
        {
            view.setVisibility(View.VISIBLE);
        }
        
        if (whichButton == DialogInterface.BUTTON_POSITIVE)
        {
            if (mOkBtn != null)
            {
                if (!TextUtils.isEmpty(text))
                {
                    mOkBtn.setVisibility(View.VISIBLE);
                    mOkBtn.setText(text);
                }
                if (listener != null)
                {
                    mOkBtn.setOnClickListener(new View.OnClickListener()
                    {

                        @Override
                        public void onClick(View view)
                        {
                            // TODO Auto-generated method stub
                            listener.onClick(mDialog, 0);
                        }
                    });
                }
            }
        }
        else if (whichButton == DialogInterface.BUTTON_NEGATIVE)
        {
            if (mCancelBtn != null)
            {
                if (!TextUtils.isEmpty(text))
                {
                    mCancelBtn.setVisibility(View.VISIBLE);
                    mCancelBtn.setText(text);
                }
                if (listener != null)
                {
                    mCancelBtn.setOnClickListener(new View.OnClickListener()
                    {

                        @Override
                        public void onClick(View view)
                        {
                            // TODO Auto-generated method stub
                            listener.onClick(mDialog, 0);
                        }
                    });
                }
            }
        }
        else
        {

        }
    }

    @Override
    public void setProgress(int value)
    {
        // TODO Auto-generated method stub
        if (mProgressBar != null)
        {
            mProgressBar.setProgress(value);
        }
        if (mProgressTxt != null)
        {
            mProgressTxt.setText(mContext.getString(R.string.putao_vupdate_download_progress_txt, value));
        }
    }

}
