package com.lives.depend.theme.dialog;

import java.util.ArrayList;
import com.lives.depend.R;
import com.lives.depend.utils.UiHelper;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
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
 * 文件名称 : CustomDialog.java<br>
 * 作 者 : zjh<br>
 * 创建时间 : 2015-11-24 下午6:00:39<br>
 * 文件描述 : 采用自定义样式的对话框<br>
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-24 1.00 初始版本<br>
 ***************************************************************** 
 */
public class CustomDialog extends CommonDialog
{

    // Title
    private TextView mTitleTv;

    // 用于显示Message信息
    private TextView mMessageTv;

    // 确定、取消
    private TextView mOkBtn, mCancelBtn;

    // Dialog显示形式
    private int mshowStyle;

    protected CustomDialog(Context context, int resLayout)
    {
        this.mContext = context;
        init(resLayout, R.style.Theme_Ptui_Dialog);
    }

    protected CustomDialog(Context context, int theme, int resLayout)
    {
        this.mContext = context;
        init(resLayout, theme);
    }

    protected CustomDialog(Context context, int theme, int resLayout, int showStyle)
    {
        this.mContext = context;
        this.mshowStyle = showStyle;
        init(resLayout, theme);
    }

    private void init(int resLayout, int theme)
    {
        mDialog = new Dialog(mContext, theme);
        if (mshowStyle == CommonDialog.DIALOG_SHOW_STYLE_NORMAL)
        {
            mDialog.setContentView(resLayout);
        }
        else if (mshowStyle == CommonDialog.DIALOG_SHOW_STYLE_BOTTOM)
        {
            mDialog.setContentView(resLayout);

            final DisplayMetrics displayMetrics = UiHelper.getDisplayMetrics(mContext);
            int screenWidth = displayMetrics.widthPixels;
            mDialog.getWindow().setBackgroundDrawableResource(R.color.putao_transparent);
            WindowManager.LayoutParams p = mDialog.getWindow().getAttributes();
            p.width = screenWidth;
            mDialog.getWindow().setAttributes(p);
            mDialog.getWindow().setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
            mDialog.getWindow().setWindowAnimations(R.style.Theme_Ptui_Animation_Dialog); // 添加动画
        }
        else
        {
            int width = 0;
            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                width = (int) (mContext.getResources().getDisplayMetrics().heightPixels * 0.9f);
            }
            else
            {
                width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.9f);
            }
            LayoutParams params = new LayoutParams(width, LayoutParams.WRAP_CONTENT);
            mDialog.setContentView(View.inflate(mContext, resLayout, null), params);
        }

        // 查找控件
        mTitleTv = (TextView) mDialog.findViewById(R.id.dialog_title_tv);
        if (resLayout == R.layout.putao_common_list_dialog)
        {
            mListView = (ListView) mDialog.findViewById(R.id.listview);
            mOkBtn = (TextView) mDialog.findViewById(R.id.dialog_bootom_ok_btn);
            mCancelBtn = (TextView) mDialog.findViewById(R.id.dialog_bootom_cancel_btn);
        }
        else if (resLayout == R.layout.putao_common_ok_cancel_dialog)
        {
            mOkBtn = (TextView) mDialog.findViewById(R.id.dialog_bootom_ok_btn);
            mCancelBtn = (TextView) mDialog.findViewById(R.id.dialog_bootom_cancel_btn);
            mMessageTv = (TextView) mDialog.findViewById(R.id.message_tv);
        }
        else if (resLayout == R.layout.putao_common_ok_dialog)
        {
            mOkBtn = (TextView) mDialog.findViewById(R.id.dialog_bootom_ok_btn);
            mMessageTv = (TextView) mDialog.findViewById(R.id.message_tv);
        }
        else if (resLayout == R.layout.putao_common_wheel_dialog)
        {
            mOkBtn = (TextView) mDialog.findViewById(R.id.dialog_bootom_ok_btn);
            mCancelBtn = (TextView) mDialog.findViewById(R.id.dialog_bootom_cancel_btn);
            mContainLayout = (LinearLayout) mDialog.findViewById(R.id.wheel_container_ll);
        }
        else if (resLayout == R.layout.putao_common_scrollview_dialog)
        {
            mDialog.getWindow().setWindowAnimations(android.R.style.Animation_Dialog);
            mDialog.getWindow().setGravity(Gravity.CENTER);
            mContainLayout = (LinearLayout) mDialog.findViewById(R.id.putao_scrolldialog_layout);
            mOkBtn = (TextView) mDialog.findViewById(R.id.putao_voucher_center_userimmediately);
            mCancelBtn = (TextView) mDialog.findViewById(R.id.putao_voucher_center_lookdetail);
            mCloseDialogImg = (ImageView) mDialog.findViewById(R.id.putao_voucher_center_closedialog_img);
        }
        else if (resLayout == R.layout.putao_train_common_gridview_dialog)
        {
            mGridView = (GridView) mDialog.findViewById(R.id.hot_gridview);
            showMoreStation = (TextView) mDialog.findViewById(R.id.more_station);
        }
        else if (resLayout == R.layout.putao_common_gridview_dialog)
        {
            mGridView = (GridView) mDialog.findViewById(R.id.dialog_gridview);
            mGridView.setSelector(new ColorDrawable());
            mCancelBtn = (TextView) mDialog.findViewById(R.id.dialog_more_action);
        }
        else if (resLayout == R.layout.putao_car_color_gridview_dialog)
        {
            mGridView = (GridView) mDialog.findViewById(R.id.dialog_gridview);
        }
        else if (resLayout == R.layout.putao_getvoucher_fail_dialog)
        {
            mDialog.getWindow().setWindowAnimations(android.R.style.Animation_Dialog);
            mDialog.getWindow().setGravity(Gravity.CENTER);
            mVoucherFailTv = (TextView) mDialog.findViewById(R.id.putao_getvoucherfail_tv);
            mCloseDialogImg = (ImageView) mDialog.findViewById(R.id.putao_voucher_center_closedialog_img);
        }
        else if (resLayout == R.layout.putao_yearandmonth_dialog)
        {
            mContainLayout = (LinearLayout) mDialog.findViewById(R.id.dialog_yearandmonth_layout);
        }
        else if (resLayout == R.layout.putao_twogridview_dialog)
        {
            mContainLayout = (LinearLayout) mDialog.findViewById(R.id.dialog_twogridview_layout);
            mOkBtn = (TextView) mDialog.findViewById(R.id.dialog_bootom_ok_btn);
        }
        else if (resLayout == R.layout.putao_screen_ad_dialog)
        {
            mDialog.getWindow().setWindowAnimations(R.style.Theme_Ptui_Dialog_ScreenAd);
            mContainLayout = (RelativeLayout) mDialog.findViewById(R.id.dialog_screenad_layout);
        }
    }

    /*
     * 判断activity是否finish,如果finished,就不show
     */
    @Override
    public void show()
    {
        if (mContext instanceof Activity)
        {
            if (((Activity) mContext).isFinishing())
            {
                return;
            }
        }
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
        // Dialog所属的Activity没有结束时，则dismiss
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
        mTitleTv.setText(title);
    }

    @Override
    public void setTitle(int title)
    {
        mTitleTv.setText(title);
    }

    /**
     * 设置dialog内容
     * 
     * @param messageId
     */
    public void setMessage(int message)
    {
        if (message > 0 && mMessageTv != null)
        {
            mMessageTv.setText(message);
            mMessageTv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置dialog内容
     * 
     * @param message
     */
    public void setMessage(CharSequence message)
    {
        if (mMessageTv != null)
        {
            mMessageTv.setText(message);
        }
    }

    @Override
    public void setMessageVisible(boolean isVisible)
    {
        // TODO Auto-generated method stub
        if (mMessageTv != null)
        {
            if (isVisible)
            {
                mMessageTv.setVisibility(View.VISIBLE);
            }
            else
            {
                mMessageTv.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void setPositiveButton(CharSequence text, OnClickListener listener)
    {
        // TODO Auto-generated method stub
        if (mOkBtn != null)
        {
            if (!TextUtils.isEmpty(text))
            {
                mOkBtn.setText(text);
            }
            if (listener != null)
            {
                mOkBtn.setOnClickListener(listener);
            }
        }
        else
        {
            throw new NullPointerException("ok button is null.");
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
        if (mOkBtn != null)
        {
            if (textColor > 0)
            {
                mOkBtn.setTextColor(mContext.getResources().getColor(textColor));
            }
            if (bgResId > 0)
            {
                mOkBtn.setBackgroundResource(bgResId);
            }
        }
    }

    @Override
    public void setPositiveButtonResource(ColorStateList colorStateList, int bgResId)
    {
        // TODO Auto-generated method stub
        if (mOkBtn != null)
        {
            if (colorStateList != null)
            {
                mOkBtn.setTextColor(colorStateList);
            }
            if (bgResId > 0)
            {
                mOkBtn.setBackgroundResource(bgResId);
            }
        }
    }

    @Override
    public void setPositiveButtonVisable(boolean isVisible)
    {
        // TODO Auto-generated method stub
        if (mOkBtn != null)
        {
            if (isVisible)
            {
                mOkBtn.setVisibility(View.VISIBLE);
            }
            else
            {
                mOkBtn.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void setNegativeButton(CharSequence text, OnClickListener listener)
    {
        // TODO Auto-generated method stub
        if (mCancelBtn != null)
        {
            if (!TextUtils.isEmpty(text))
            {
                mCancelBtn.setText(text);
            }
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
            mCancelBtn.setOnClickListener(listener);
        }
        else
        {
            throw new NullPointerException("cancel button is null.");
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
        if (mCancelBtn != null)
        {
            if (isVisible)
            {
                mCancelBtn.setVisibility(View.VISIBLE);
            }
            else
            {
                mCancelBtn.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void hideBottom()
    {
        View view = mDialog.findViewById(R.id.dialog_bottom_layout);
        if (view != null)
        {
            view.setVisibility(View.GONE);
        }
        if (mCancelBtn != null)
        {
            mCancelBtn.setVisibility(View.GONE);
        }
        if (mOkBtn != null)
        {
            mOkBtn.setVisibility(View.GONE);
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
    public void setOnKeyListener(OnKeyListener listener)
    {
        // TODO Auto-generated method stub
        if (listener != null)
        {
            mDialog.setOnKeyListener(listener);
        }

    }

    @Override
    public void setOnCancelListener(OnCancelListener listener)
    {
        // TODO Auto-generated method stub
        if (listener != null)
        {
            mDialog.setOnCancelListener(listener);
        }
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener)
    {
        // TODO Auto-generated method stub

    }

    /**
     * 设置ListView ItemClick
     */
    @Override
    public void setListViewItemClickListener(OnItemClickListener listener)
    {
        if (mListView != null)
        {
            mListView.setOnItemClickListener(listener);
        }
        else
        {
            throw new NullPointerException("ListView is null.");
        }
        this.dismiss();
    }

    /**
     * 设置ListView控件数据库，调用了该方法不需要再setListAdapter <br/>
     */
    @Override
    public void setListViewDatas(String[] dataList)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.putao_common_dialog_base_lv_item,
                dataList);
        if (mListView != null)
        {
            mListView.setAdapter(adapter);
        }
        else
        {
            throw new NullPointerException("ListView is null.");
        }
    }

    @Override
    public void setListViewDatas(ArrayList<String> dataList)
    {
        if (dataList == null || dataList.size() == 0)
        {
            return;
        }
        int size = dataList.size();
        setListViewDatas((String[]) dataList.toArray(new String[size]));
    }

    /**
     * 设置ListView Adapter
     */
    @Override
    public void setListAdapter(BaseAdapter adapter)
    {
        if (adapter != null && mListView != null)
        {
            mListView.setAdapter(adapter);
        }
        else
        {
            throw new NullPointerException("adapter orListView is null.");
        }
    }

    @Override
    public void setSingleChoiceListViewDataList(ArrayList<CharSequence> dataList)
    {
        if (dataList == null || dataList.size() == 0)
        {
            return;
        }
        int size = dataList.size();
        CharSequence[] datas = (CharSequence[]) dataList.toArray(new CharSequence[size]);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(mContext,
                R.layout.putao_voucher_dialog_listitem, datas);
        if (mListView != null)
        {
            mListView.setItemsCanFocus(false);
            mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            mListView.setAdapter(adapter);
        }
        else
        {
            throw new NullPointerException("ListView is null.");
        }
    }

    @Override
    public void setSingleChoiceListViewDatas(String[] dataList)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.putao_list_item_single_choice,
                dataList);
        if (mListView != null)
        {
            mListView.setItemsCanFocus(false);
            mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            mListView.setAdapter(adapter);
        }
        else
        {
            throw new NullPointerException("ListView is null.");
        }
    }

    @Override
    public void setSingleChoiceListViewDatas(ArrayList<String> dataList)
    {
        if (dataList == null || dataList.size() == 0)
        {
            return;
        }
        int size = dataList.size();
        setSingleChoiceListViewDatas((String[]) dataList.toArray(new String[size]));
    }

    @Override
    public void setSingleChoiceListViewDatas(ArrayList<String> dataList, int checkedPosition)
    {
        // TODO Auto-generated method stub
        setSingleChoiceListViewDatas(dataList);
        if (mListView != null)
        {
            mListView.setSelection(checkedPosition);
        }
    }

    @Override
    public void setSingleChoiceListViewDatas(String[] dataList, int checkedPosition)
    {
        // TODO Auto-generated method stub
        setSingleChoiceListViewDatas(dataList);
        if (mListView != null)
        {
            mListView.setSelection(checkedPosition);
        }
    }

}
