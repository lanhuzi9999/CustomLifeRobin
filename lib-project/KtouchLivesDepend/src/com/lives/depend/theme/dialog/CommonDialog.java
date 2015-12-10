package com.lives.depend.theme.dialog;

import java.util.ArrayList;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * ****************************************************************<br>
 * 文件名称 : CommonDialog.java<br>
 * 作 者 : zjh<br>
 * 创建时间 : 2015-11-24 上午10:50:57<br>
 * 文件描述 : 对话框公共对话框<br>
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-24 1.00 初始版本<br>
 ***************************************************************** 
 */
public abstract class CommonDialog extends AbstractDialog
{
    /**
     * Dialog显示形式：正常显示
     */
    public static final int DIALOG_SHOW_STYLE_NORMAL = 1;

    /**
     * Dialog显示形式：居中显示
     */
    public static final int DIALOG_SHOW_STYLE_CENTER = 2;

    /**
     * Dialog显示形式：底部显示
     */
    public static final int DIALOG_SHOW_STYLE_BOTTOM = 3;

    protected Context mContext = null;

    protected Dialog mDialog = null;

    protected GridView mGridView;

    protected ListView mListView;

    protected TextView showMoreStation;

    protected ViewGroup mContainLayout; // Dialog的Content

    protected TextView mVoucherFailTv; // 优惠券领取失败dialog文本

    protected ImageView mCloseDialogImg; // 优惠券dialog关闭按钮

    /**
     * 设置对话框内容 是否可见
     */
    public abstract void setMessageVisible(boolean isVisible);

    /**
     * 设置确认按钮栏
     */
    public abstract void setPositiveButton(CharSequence text, View.OnClickListener listener);

    public abstract void setPositiveButton(int textId, View.OnClickListener listener);

    public abstract void setPositiveButtonResource(int textColor, int bgResId);

    public abstract void setPositiveButtonResource(ColorStateList colorStateList, int bgResId);

    public abstract void setPositiveButtonVisable(boolean isVisible);

    /**
     * 设置取消按钮
     */
    public abstract void setNegativeButton(CharSequence text, View.OnClickListener listener);

    public abstract void setNegativeButton(int textId, View.OnClickListener listener);

    public abstract void setNegativeButtonVisible(boolean isVisible);

    /**
     * 隐藏底部栏（button栏）
     */
    public abstract void hideBottom();

    /**
     * 设置点击事件
     */

    public abstract void setListViewItemClickListener(OnItemClickListener listener);

    /**
     * 设置列表 数据
     */
    public abstract void setListViewDatas(String[] dataList);

    public abstract void setListViewDatas(ArrayList<String> dataList);

    public abstract void setListAdapter(BaseAdapter adapter);

    public abstract void setSingleChoiceListViewDataList(ArrayList<CharSequence> dataList);

    public abstract void setSingleChoiceListViewDatas(String[] dataList);

    public abstract void setSingleChoiceListViewDatas(ArrayList<String> dataList, int checkedPosition);

    public abstract void setSingleChoiceListViewDatas(String[] dataList, int checkedPosition);

    public abstract void setSingleChoiceListViewDatas(ArrayList<String> dataList);

    /**
     * 获取对话框内容栏 中的view 及 相关操作
     */
    public void setGridViewItemClickListener(OnItemClickListener listener)
    {
        if (mGridView != null)
        {
            mGridView.setOnItemClickListener(listener);
        }
        else
        {
            throw new NullPointerException("mGridView is null.");
        }
        this.dismiss();
    }

    public void setGridViewAdapter(BaseAdapter adapter)
    {
        if (adapter != null && mGridView != null)
        {
            mGridView.setAdapter(adapter);
        }
        else
        {
            throw new NullPointerException("adapter or GridView is null.");
        }
    }

    public ViewGroup getContainerLayout()
    {
        return mContainLayout;
    }

    public TextView getFailVoucherTv()
    {
        return mVoucherFailTv;
    }

    public void setCloseButtonClickListener(View.OnClickListener listener)
    {
        if (mCloseDialogImg != null)
        {
            mCloseDialogImg.setOnClickListener(listener);
        }
        else
        {
            throw new NullPointerException("close button is null.");
        }
    }

    // 获取火车票热门站点对话框“更多“TextView
    public TextView getMoreStation()
    {
        return showMoreStation;
    }

    /**
     * 获取GridView控件
     */
    public GridView getGridView()
    {
        return mGridView;
    }

    /**
     * 获取ListView控件
     * 
     * @return
     */
    public ListView getListView()
    {
        return mListView;
    }

    /**
     * listView高度自适应 add by zj
     * 
     * @param listView
     * @param maxShowItemNum listView最大不超过这个数量的item高度,少于这个数量,则有多少显示多少
     */
    public static void setListViewHeightBasedOnChildren(ListView listView, int maxShowItemNum)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            return;
        }
        int totalHeight = 0;
        int showNum = listAdapter.getCount() > maxShowItemNum ? maxShowItemNum : listAdapter.getCount();
        for (int i = 0; i < showNum; i++)
        {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            // listItem.measure(listItem.getParent(), heightMeasureSpec);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * showNum);
        listView.setLayoutParams(params);
    }
}
