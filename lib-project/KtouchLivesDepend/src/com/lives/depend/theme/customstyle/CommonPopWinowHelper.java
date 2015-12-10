package com.lives.depend.theme.customstyle;

import java.util.ArrayList;
import java.util.List;

import com.lives.depend.R;
import com.lives.depend.theme.dialog.CommonDialog;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

/**
 * ****************************************************************<br>
 * 文件名称 : CommonPopWinowHelper.java<br>
 * 作 者 : zjh<br>
 * 创建时间 : 2015-11-27 下午2:18:03<br>
 * 文件描述 : 类似Spinner效果的弹出窗<br>
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-27 1.00 初始版本<br>
 *****************************************************************
 */
public class CommonPopWinowHelper
{

    private PopupWindow mPopupWindow;

    private View mAsDownView;

    private Activity mContext;

    private ListView mListView;

    private List<String> mData;

    private int mSelectedIndex = -1;

    private DataAdapter mHistoryAdapter = null;

    private TextView mBindView = null;

    private OnPopupListViewItemClickListener mItemClickListener;

    /**
     * 箭头图片资源：选中
     */
    private int mSpinnerSelectedTagRes = -1;

    /**
     * 箭头图片资源：未选中
     */
    private int mSpinnerTagRes = -1;

    /**
     * 文字颜色：默认
     */
    private int mSpinnerTextColor = -1;

    /**
     * 文字颜色：选中
     */
    private int mSpinnerSelectedTextColor = -1;

    public CommonPopWinowHelper(Activity c, View asDownView, OnPopupListViewItemClickListener listener)
    {
        init(c, asDownView, listener, -1);
    }

    public CommonPopWinowHelper(Activity c, View asDownView, OnPopupListViewItemClickListener listener, int selecteIndex)
    {
        init(c, asDownView, listener, selecteIndex);
    }

    private void init(Activity c, View asDownView, OnPopupListViewItemClickListener listener, int selecteIndex)
    {
        mContext = c;
        mAsDownView = asDownView;
        mSelectedIndex = selecteIndex;
        mItemClickListener = listener;

        View view = View.inflate(mContext, R.layout.putao_popupwindow_layout, null);
        mListView = (ListView) view.findViewById(R.id.history_list);
        initPopupWindow(view);
        CommonDialog.setListViewHeightBasedOnChildren(mListView, 6);
    }

    public void setSpinnerSelectRes(int spinnerTagRes, int spinnerSelectedTagRes, int spinnerTextColor,
            int spinnerSelectedTextColor)
    {
        mSpinnerTagRes = spinnerTagRes;
        mSpinnerSelectedTagRes = spinnerSelectedTagRes;
        mSpinnerTextColor = spinnerTextColor;
        mSpinnerSelectedTextColor = spinnerSelectedTextColor;
    }

    public void bindView(TextView bindView)
    {
        mBindView = bindView;
    }

    private void initPopupWindow(View view)
    {
        mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x22000000));
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.getContentView().setFilterTouchesWhenObscured(true);
        mPopupWindow.getContentView().setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (v.getId() != mAsDownView.getId())
                {
                    mPopupWindow.dismiss();
                    mPopupWindow.setFocusable(false);
                }
                return true;
            }
        });
    }

    public void setDataAndShow(String[] data, int index)
    {
        setData(data);
        mSelectedIndex = index;
        show();
    }

    public void setData(String[] data)
    {
        if (data == null || data.length < 1)
        {
            return;
        }
        List<String> list = new ArrayList<String>();
        for (String str : data)
        {
            list.add(str);
        }
        mData = list;
    }

    public void setSelectedIndex(int index)
    {
        if (index >= 0)
        {
            mSelectedIndex = index;
        }
        if (mHistoryAdapter != null)
        {
            mHistoryAdapter.notifyDataSetChanged();
        }
    }

    public void setDataAndShow(List<String> data, int index)
    {

        mData = data;
        mSelectedIndex = index;
        if (mData == null || mData.size() == 0)
        {
            return;
        }
        show();
    }

    public void show()
    {

        if (mHistoryAdapter == null)
        {
            mHistoryAdapter = new DataAdapter(mContext, mData);
            mListView.setAdapter(mHistoryAdapter);
            CommonDialog.setListViewHeightBasedOnChildren(mListView, 6);
        }
        else
        {
            mHistoryAdapter.setData(mData);
        }

        mPopupWindow.setOnDismissListener(new OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                if (mBindView != null)
                {
                    setSpinnerTagSelect(mContext, mBindView, false);
                }
            }
        });

        if (!isShowing())
        {
            mPopupWindow.showAsDropDown(mAsDownView, 0, 0);
            if (mBindView != null)
            {
                setSpinnerTagSelect(mContext, mBindView, true);
            }
        }
    }

    public boolean isShowing()
    {
        return mPopupWindow.isShowing();
    }

    public void setData(List<String> data, int index)
    {
        if (data != null)
        {
            mData = data;
        }
        if (index >= 0)
        {
            mSelectedIndex = index;
        }
        mHistoryAdapter.notifyDataSetChanged();
    }

    public static class ViewHolder
    {
        public TextView phoneTv;

        public View line;

    }

    private class DataAdapter extends BaseAdapter
    {

        private Context mContext;

        private List<String> mDataList = null;

        public DataAdapter(Context context, List<String> dataList)
        {
            this.mContext = context;
            this.mDataList = dataList;
        }

        public void setData(List<String> dataList)
        {
            this.mDataList = dataList;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount()
        {
            return (mDataList != null) ? mDataList.size() : 0;
        }

        @Override
        public Object getItem(int position)
        {
            return position;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder;
            if (convertView == null)
            {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(this.mContext).inflate(R.layout.putao_popwin_listview_item, null);
                holder.phoneTv = (TextView) convertView.findViewById(R.id.popupwin_list_item_text);
                holder.line = convertView.findViewById(R.id.popupwin_line);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            final String data = mDataList.get(position);
            if (mSelectedIndex >= 0 && (position == mSelectedIndex))
            {
                holder.phoneTv.setTextColor(mContext.getResources().getColor(R.color.putao_theme));
                holder.line.setBackgroundColor(mContext.getResources().getColor(R.color.putao_list_selector));
            }
            else
            {
                holder.phoneTv.setTextColor(mContext.getResources().getColor(R.color.putao_text_color_primary));
                holder.line.setBackgroundColor(mContext.getResources().getColor(R.color.putao_list_selector));
            }
            holder.phoneTv.setText(data);

            // 选择项
            convertView.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View view)
                {
                    if (mItemClickListener != null)
                    {
                        mItemClickListener.onPopupListViewItemClick(position, data);
                    }

                    dismiss();
                }
            });

            return convertView;
        }
    }

    public void dismiss()
    {
        if (mPopupWindow != null && mPopupWindow.isShowing())
        {
            mPopupWindow.dismiss();
        }
    }

    private void setSpinnerTagSelect(Context context, TextView text, boolean isSelected)
    {
        if (isSelected)
        {
            if (mSpinnerSelectedTextColor > 0)
            {
                text.setTextColor(context.getResources().getColor(mSpinnerSelectedTextColor));
            }
            if (mSpinnerSelectedTagRes > 0)
            {
                text.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        context.getResources().getDrawable(mSpinnerSelectedTagRes), null);
            }
        }
        else
        {
            if (mSpinnerTextColor > 0)
            {
                text.setTextColor(context.getResources().getColor(mSpinnerTextColor));
            }
            if (mSpinnerTagRes > 0)
            {
                text.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        context.getResources().getDrawable(mSpinnerTagRes), null);
            }
        }
    }

    public void clear()
    {
        if (mData != null)
        {
            mData = null;
        }
    }

    public boolean isEmpty()
    {
        if (mData != null && mData.size() > 0)
        {
            return false;
        }
        return true;
    }

    public interface OnPopupListViewItemClickListener
    {
        public void onPopupListViewItemClick(int position, String data);
    }
}
