package com.lives.depend.theme.customstyle;

import java.util.List;

import com.lives.depend.R;
import com.lives.depend.theme.customstyle.CommonPopWinowHelper.OnPopupListViewItemClickListener;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

/**
 * ****************************************************************<br>
 * 文件名称 : CustomSpinner.java<br>
 * 作 者 : zjh<br>
 * 创建时间 : 2015-11-13 上午10:26:40<br>
 * 文件描述 : 自定义类Spinner效果的组件<br>
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-13 1.00 初始版本<br>
 ***************************************************************** 
 */
public class CustomSpinner extends RelativeLayout implements OnClickListener
{

    private Context mContext = null;

    /**
     * 显示内容的view
     */
    private TextView mContentTView = null;

    /**
     * 点击用来显示数据的弹出框控制器
     */
    private CommonPopWinowHelper mShowDialog = null;

    private IClickAndShow mIClickAndShow = null;

    /**
     * 箭头图片资源：未选中
     */
    private int mSpinnerTagRes = -1;

    /**
     * 箭头图片资源：选中
     */
    private int mSpinnerSelectedTagRes = -1;

    /**
     * 文字颜色：默认
     */
    private int mSpinnerTextColor = -1;

    /**
     * 文字颜色：选中
     */
    private int mSpinnerSelectedTextColor = -1;

    public CustomSpinner(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
        initResource(context, null);
    }

    public CustomSpinner(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initResource(context, attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initResource(context, attrs);
    }

    private void initResource(Context context, AttributeSet attrs)
    {
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.putao_custom_spinner_layout, this, true);

        setOnClickListener(this);
        mContentTView = (TextView) findViewById(R.id.custom_spinner_content);

        if (attrs != null)
        {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomSpinner);
            if (typedArray != null)
            {
                // 背景色
                int bgLayoutResId = typedArray.getResourceId(R.styleable.CustomSpinner_spinner_background, -1);
                if (bgLayoutResId > 0)
                {
                    setBackgroundResource(bgLayoutResId);
                }

                // 显示的内容
                String showContent = typedArray.getString(R.styleable.CustomSpinner_spinner_text);
                if (!TextUtils.isEmpty(showContent))
                {
                    mContentTView.setText(showContent);
                }

                // 文字大小
                int textSize = typedArray.getDimensionPixelOffset(R.styleable.CustomSpinner_spinner_textSize, -1);
                if (textSize > 0)
                {
                    mContentTView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                }

                // 默认文字颜色
                mSpinnerTextColor = typedArray.getResourceId(R.styleable.CustomSpinner_spinner_textColor, -1);
                if (mSpinnerTextColor > 0)
                {
                    mContentTView.setTextColor(context.getResources().getColor(mSpinnerTextColor));
                }
                // 选中文字颜色
                mSpinnerSelectedTextColor = typedArray.getResourceId(
                        R.styleable.CustomSpinner_spinner_textColor_selected, -1);

                // 默认图标
                mSpinnerTagRes = typedArray.getResourceId(R.styleable.CustomSpinner_spinner_drawableRight, -1);
                if (mSpinnerTagRes > 0)
                {
                    mContentTView.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources()
                            .getDrawable(mSpinnerTagRes), null);
                }
                // 选中图标
                mSpinnerSelectedTagRes = typedArray.getResourceId(
                        R.styleable.CustomSpinner_spinner_drawableRight_selected, -1);

                typedArray.recycle();
            }
        }
    }

    /**
     * 如果需要通过Spinner来控制弹出框的显示，则需要调用该方法 方法表述
     * 
     * @param listener 弹出框的item点击回调
     * @param asDownView 弹出框的其实位置线 void
     */
    public void init(OnPopupListViewItemClickListener listener, View asDownView)
    {
        if (mShowDialog == null)
        {
            mShowDialog = new CommonPopWinowHelper((Activity) getContext(), asDownView, listener);
            mShowDialog.bindView(mContentTView);
            mShowDialog.setSpinnerSelectRes(mSpinnerTagRes, mSpinnerSelectedTagRes, mSpinnerTextColor,
                    mSpinnerSelectedTextColor);
        }
    }

    public void setOnClickAndShow(IClickAndShow iClickAndShow)
    {
        mIClickAndShow = iClickAndShow;
    }

    public void dismiss()
    {
        if (mShowDialog != null)
        {
            mShowDialog.dismiss();
        }
    }

    public void show()
    {
        if (mShowDialog != null)
        {
            mShowDialog.show();
        }
    }

    public void setDataAndShow(String[] dataList, int index)
    {
        if (mShowDialog != null)
        {
            mShowDialog.setDataAndShow(dataList, index);
        }
    }

    public void setDataAndShow(List<String> dataList, int index)
    {
        if (mShowDialog != null)
        {
            mShowDialog.setDataAndShow(dataList, index);
        }
    }

    public void setSelectedIndex(int index)
    {
        if (mShowDialog != null)
        {
            mShowDialog.setSelectedIndex(index);
        }
    }

    @Override
    public void onClick(View view)
    {
        // TODO Auto-generated method stub
        if (mIClickAndShow != null)
        {
            mIClickAndShow.onClick();
        }
    }

    public void clear()
    {
        if (mShowDialog != null)
        {
            mShowDialog.clear();
        }
    }

    public boolean isEmpty()
    {
        if (mShowDialog == null || mShowDialog.isEmpty())
        {
            return true;
        }
        return false;
    }

    public void setText(String content)
    {
        mContentTView.setText(content);
    }

    public void setText(int resId)
    {
        mContentTView.setText(resId);
    }

    public interface IClickAndShow
    {
        /**
         * 点击回调接口
         */
        void onClick();
    }

    public void setSpinnerTagSelect(boolean isSelected)
    {
        if (isSelected)
        {
            if (mSpinnerSelectedTextColor > 0)
            {
                mContentTView.setTextColor(mContext.getResources().getColor(mSpinnerSelectedTextColor));
            }
            if (mSpinnerSelectedTagRes > 0)
            {
                mContentTView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        mContext.getResources().getDrawable(mSpinnerSelectedTagRes), null);
            }
        }
        else
        {
            if (mSpinnerTextColor > 0)
            {
                mContentTView.setTextColor(mContext.getResources().getColor(mSpinnerTextColor));
            }
            if (mSpinnerTagRes > 0)
            {
                mContentTView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        mContext.getResources().getDrawable(mSpinnerTagRes), null);
            }
        }
    }

}
