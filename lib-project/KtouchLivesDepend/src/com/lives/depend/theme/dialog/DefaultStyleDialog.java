package com.lives.depend.theme.dialog;

import java.util.ArrayList;
import com.lives.depend.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ****************************************************************<br>
 * 文件名称 : DefaultStyleDialog.java<br>
 * 作 者 : zjh<br>
 * 创建时间 : 2015-11-24 下午3:29:16<br>
 * 文件描述 :使用系统默认样式实现dialog(title 及 button等采用Dialog默认样式) <br>
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-24 1.00 初始版本<br>
 ***************************************************************** 
 */
public class DefaultStyleDialog extends CommonDialog
{

    protected AlertDialog.Builder mDialogBuilder = null;

    protected boolean mCanceledOnTouchOutside = true;

    protected boolean mCancelable = true;

    protected CharSequence mOkButtonText = null;

    protected CharSequence mCancelButtonText = null;

    // 是否显示Dialog默认的“确定”按钮
    protected boolean mNeedShowOkButton = false;

    // 是否显示Dialog默认的“取消”按钮
    protected boolean mNeedShowCancelButton = false;

    protected OnClickListener mOkButtonListener = null;

    protected OnClickListener mCancelButtonListener = null;

    protected OnItemClickListener mOnItemClickListener = null;

    // Dialog上的按钮（不是Dialog默认的Button）
    private TextView mLeftButton, mRightButton;

    private TextView mMessageTv;

    protected DefaultStyleDialog(Context context, int resLayout)
    {
        this.mContext = context;
        init(resLayout, R.style.Theme_Ptui_Dialog);
    }

    protected DefaultStyleDialog(Context context, int theme, int resLayout)
    {
        this.mContext = context;
        init(resLayout, theme);
    }

    private void init(int resLayout, int theme)
    {
        mDialogBuilder = new AlertDialog.Builder(mContext);
        View viewLayout = null;

        if (resLayout == R.layout.putao_common_list_dialog)
        {
        }
        else if (resLayout == R.layout.putao_common_ok_cancel_dialog)
        {
            mNeedShowOkButton = true;
            mNeedShowCancelButton = true;
        }
        else if (resLayout == R.layout.putao_common_ok_dialog)
        {
            mNeedShowOkButton = true;
        }
        else if (resLayout == R.layout.putao_common_wheel_dialog)
        {
            viewLayout = View.inflate(mContext, resLayout, null);
            mDialogBuilder.setView(viewLayout);

            mContainLayout = (LinearLayout) viewLayout.findViewById(R.id.wheel_container_ll);

            mNeedShowOkButton = true;
            mNeedShowCancelButton = true;
        }
        else if (resLayout == R.layout.putao_common_scrollview_dialog)
        {
            viewLayout = View.inflate(mContext, resLayout, null);
            mDialogBuilder.setView(viewLayout);

            mContainLayout = (LinearLayout) viewLayout.findViewById(R.id.putao_scrolldialog_layout);
            mLeftButton = (TextView) viewLayout.findViewById(R.id.putao_voucher_center_userimmediately);
            mRightButton = (TextView) viewLayout.findViewById(R.id.putao_voucher_center_lookdetail);
            mCloseDialogImg = (ImageView) viewLayout.findViewById(R.id.putao_voucher_center_closedialog_img);
        }
        else if (resLayout == R.layout.putao_train_common_gridview_dialog)
        {
            viewLayout = View.inflate(mContext, resLayout, null);
            mDialogBuilder.setView(viewLayout);

            mGridView = (GridView) viewLayout.findViewById(R.id.hot_gridview);
            showMoreStation = (TextView) viewLayout.findViewById(R.id.more_station);
        }
        else if (resLayout == R.layout.putao_common_gridview_dialog)
        {
            viewLayout = View.inflate(mContext, resLayout, null);
            mDialogBuilder.setView(viewLayout);

            mGridView = (GridView) viewLayout.findViewById(R.id.dialog_gridview);
            mGridView.setSelector(new ColorDrawable());
            mRightButton = (TextView) mDialog.findViewById(R.id.dialog_more_action);
        }
        else if (resLayout == R.layout.putao_car_color_gridview_dialog)
        {
            viewLayout = View.inflate(mContext, resLayout, null);
            mDialogBuilder.setView(viewLayout);

            mGridView = (GridView) viewLayout.findViewById(R.id.dialog_gridview);
        }
        else if (resLayout == R.layout.putao_getvoucher_fail_dialog)
        {
            viewLayout = View.inflate(mContext, resLayout, null);
            mDialogBuilder.setView(viewLayout);

            mVoucherFailTv = (TextView) viewLayout.findViewById(R.id.putao_getvoucherfail_tv);
            mCloseDialogImg = (ImageView) viewLayout.findViewById(R.id.putao_voucher_center_closedialog_img);
        }
        else if (resLayout == R.layout.putao_yearandmonth_dialog)
        {
            viewLayout = View.inflate(mContext, resLayout, null);
            mDialogBuilder.setView(viewLayout);

            mContainLayout = (LinearLayout) viewLayout.findViewById(R.id.dialog_yearandmonth_layout);
        }
        else if (resLayout == R.layout.putao_twogridview_dialog)
        {
            viewLayout = View.inflate(mContext, resLayout, null);
            mDialogBuilder.setView(viewLayout);
            
            mContainLayout = (LinearLayout) mDialog.findViewById(R.id.dialog_twogridview_layout);
            mNeedShowOkButton = true;
        }
        else if (resLayout == R.layout.putao_screen_ad_dialog)
        {
            viewLayout = View.inflate(mContext, resLayout, null);
            mDialogBuilder.setView(viewLayout);
            
            mContainLayout = (RelativeLayout) mDialog.findViewById(R.id.dialog_screenad_layout);
        }

        hideTitleAndBottom(viewLayout);
    }

    /**
     * 布局中的Title 和 Bottom中的按钮隐藏，采用Dialog默认的
     */
    private void hideTitleAndBottom(View viewLayout)
    {
        if (viewLayout == null)
        {
            return;
        }
        View titleView = viewLayout.findViewById(R.id.dialog_title_tv);
        if (titleView != null)
        {
            titleView.setVisibility(View.GONE);
        }
        View dividerView = viewLayout.findViewById(R.id.dialog_title_divider);
        if (dividerView != null)
        {
            dividerView.setVisibility(View.GONE);
        }
        View bottomLayout = viewLayout.findViewById(R.id.dialog_bottom_layout);
        if (bottomLayout != null)
        {
            bottomLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void show()
    {
        // TODO Auto-generated method stub
        if (mNeedShowOkButton)
        {
            if (TextUtils.isEmpty(mOkButtonText))
            {
                mOkButtonText = mContext.getText(R.string.putao_confirm);
            }
            mDialogBuilder.setPositiveButton(mOkButtonText, new DialogClickListener(mOkButtonListener));
        }
        if (mNeedShowCancelButton)
        {
            if (TextUtils.isEmpty(mCancelButtonText))
            {
                mCancelButtonText = mContext.getText(R.string.putao_cancel);
            }
            mDialogBuilder.setNegativeButton(mCancelButtonText, new DialogClickListener(mCancelButtonListener));
        }
        mDialogBuilder.setCancelable(mCancelable);

        if (mDialog == null)
        {
            mDialog = mDialogBuilder.create();
        }
        mDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
        if (!mDialog.isShowing())
        {
            mDialog.show();
        }
    }

    @Override
    public boolean isShowing()
    {
        // TODO Auto-generated method stub
        if (mDialog != null)
        {
            return mDialog.isShowing();
        }
        return false;
    }

    @Override
    public void dismiss()
    {
        // TODO Auto-generated method stub
        if (mDialog != null)
        {
            mDialog.dismiss();
        }
    }
    
    @Override
    public void cancel()
    {
        // TODO Auto-generated method stub
        if(mDialog != null)
        {
            mDialog.cancel();
        }
    }

    @Override
    public void setTitle(CharSequence title)
    {
        // TODO Auto-generated method stub
        mDialogBuilder.setTitle(title);
    }

    @Override
    public void setTitle(int title)
    {
        // TODO Auto-generated method stub
        mDialogBuilder.setTitle(title);
    }

    @Override
    public void setMessage(int message)
    {
        // TODO Auto-generated method stub
        if (mMessageTv != null)
        {
            mMessageTv.setText(message);
        }
        else
        {
            mDialogBuilder.setMessage(message);
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
        else
        {
            mDialogBuilder.setMessage(message);
        }
    }

    @Override
    public void setMessageVisible(boolean isVisible)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void setPositiveButton(CharSequence text, OnClickListener listener)
    {
        // TODO Auto-generated method stub
        if (mLeftButton != null)
        {
            if (!TextUtils.isEmpty(text))
            {
                mLeftButton.setText(text);
            }
            if (listener != null)
            {
                mLeftButton.setOnClickListener(listener);
            }
        }
        else
        {
            if (!TextUtils.isEmpty(text))
            {
                mOkButtonText = text;
                mNeedShowOkButton = true;
            }
            if (listener != null)
            {
                mNeedShowOkButton = true;
                mOkButtonListener = listener;
            }
        }
    }

    @Override
    public void setPositiveButton(int textId, OnClickListener listener)
    {
        // TODO Auto-generated method stub
        CharSequence text = mContext.getText(textId);
        setPositiveButton(text, listener);
    }

    @Override
    public void setPositiveButtonResource(int textColor, int bgResId)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPositiveButtonResource(ColorStateList colorStateList, int bgResId)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPositiveButtonVisable(boolean isVisible)
    {
        // TODO Auto-generated method stub
        if (mLeftButton != null)
        {
            if (isVisible)
            {
                mLeftButton.setVisibility(View.VISIBLE);
            }
            else
            {
                mLeftButton.setVisibility(View.GONE);
            }
        }
        else
        {
            if (isVisible)
            {
                mNeedShowOkButton = true;
            }
            else
            {
                mNeedShowOkButton = false;
            }
        }
    }

    @Override
    public void setNegativeButton(CharSequence text, OnClickListener listener)
    {
        // TODO Auto-generated method stub
        if (listener == null)
        {
            listener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dismiss();
                }
            };
        }
        if (mRightButton != null)
        {
            if (!TextUtils.isEmpty(text))
            {
                mRightButton.setText(text);
            }
            mRightButton.setOnClickListener(listener);
        }
        else
        {
            if (!TextUtils.isEmpty(text))
            {
                mCancelButtonText = text;
            }
            mNeedShowCancelButton = true;
            mCancelButtonListener = listener;
        }
    }

    @Override
    public void setNegativeButton(int textId, OnClickListener listener)
    {
        // TODO Auto-generated method stub
        CharSequence text = mContext.getText(textId);
        setNegativeButton(text, listener);
    }

    @Override
    public void setNegativeButtonVisible(boolean isVisible)
    {
        // TODO Auto-generated method stub
        if (mCancelButtonText != null)
        {
            if (isVisible)
            {
                mRightButton.setVisibility(View.VISIBLE);
            }
            else
            {
                mRightButton.setVisibility(View.GONE);
            }
        }
        else
        {
            if (isVisible)
            {
                mNeedShowCancelButton = true;
            }
            else
            {
                mNeedShowCancelButton = false;
            }
        }
    }

    @Override
    public void hideBottom()
    {
        // TODO Auto-generated method stub
        if (mLeftButton != null)
        {
            mLeftButton.setVisibility(View.GONE);
        }
        if (mRightButton != null)
        {
            mRightButton.setVisibility(View.GONE);
        }
        mNeedShowOkButton = false;
        mNeedShowCancelButton = false;
    }

    @Override
    public void setCancelable(boolean flag)
    {
        // TODO Auto-generated method stub
        mCancelable = flag;
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel)
    {
        // TODO Auto-generated method stub
        mCanceledOnTouchOutside = cancel;
    }

    @Override
    public void setOnKeyListener(OnKeyListener listener)
    {
        // TODO Auto-generated method stub
        mDialogBuilder.setOnKeyListener(listener);
    }

    @Override
    public void setOnCancelListener(OnCancelListener listener)
    {
        // TODO Auto-generated method stub
        mDialogBuilder.setOnCancelListener(listener);
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setListViewItemClickListener(OnItemClickListener listener)
    {
        // TODO Auto-generated method stub
        mOnItemClickListener = listener;
    }

    @Override
    public void setListViewDatas(String[] dataList)
    {
        // TODO Auto-generated method stub
        setSingleChoiceListViewDatas(dataList, 0);
    }

    @Override
    public void setListViewDatas(ArrayList<String> dataList)
    {
        // TODO Auto-generated method stub
        setSingleChoiceListViewDatas(dataList, 0);
    }

    @Override
    public void setListAdapter(BaseAdapter adapter)
    {
        mDialogBuilder.setAdapter(adapter, new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (mOnItemClickListener != null)
                {
                    mOnItemClickListener.onItemClick(getListView(), null, which, which);
                }
            }
        });
    }

    @Override
    public void setSingleChoiceListViewDataList(ArrayList<CharSequence> dataList)
    {
        // TODO Auto-generated method stub
        if (dataList == null || dataList.size() == 0)
        {
            return;
        }
        int size = dataList.size();
        CharSequence[] datas = (CharSequence[]) dataList.toArray(new CharSequence[size]);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(mContext,
                R.layout.putao_voucher_dialog_listitem, datas);
        setListAdapter(adapter);
    }

    @Override
    public void setSingleChoiceListViewDatas(String[] dataList)
    {
        // TODO Auto-generated method stub
        setSingleChoiceListViewDatas(dataList, 0);
    }

    @Override
    public void setSingleChoiceListViewDatas(ArrayList<String> dataList, int checkedPosition)
    {
        // TODO Auto-generated method stub
        if (dataList == null || dataList.size() == 0)
        {
            return;
        }
        int size = dataList.size();
        setSingleChoiceListViewDatas((String[]) dataList.toArray(new String[size]), checkedPosition);
    }

    @Override
    public void setSingleChoiceListViewDatas(String[] dataList, int checkedPosition)
    {
        // TODO Auto-generated method stub
        mDialogBuilder.setSingleChoiceItems(dataList, checkedPosition, new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // TODO Auto-generated method stub
                if (mOnItemClickListener != null)
                {
                    mOnItemClickListener.onItemClick(getListView(), null, which, which);
                }
            }
        });
    }

    @Override
    public void setSingleChoiceListViewDatas(ArrayList<String> dataList)
    {
        // TODO Auto-generated method stub
        if (dataList == null || dataList.size() == 0)
        {
            return;
        }
        int size = dataList.size();
        setSingleChoiceListViewDatas((String[]) dataList.toArray(new String[size]), 0);
    }

    @Override
    public ListView getListView()
    {
        // TODO Auto-generated method stub
        if (mDialog == null)
        {
            mDialog = mDialogBuilder.create();
        }
        return ((AlertDialog) mDialog).getListView();
    }

    private static class DialogClickListener implements DialogInterface.OnClickListener
    {
        private OnClickListener onClickListener = null;

        public DialogClickListener(OnClickListener onClickListener)
        {
            this.onClickListener = onClickListener;
        }

        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            // TODO Auto-generated method stub
            if (onClickListener != null)
            {
                onClickListener.onClick(null);
            }
        }

    }

}
