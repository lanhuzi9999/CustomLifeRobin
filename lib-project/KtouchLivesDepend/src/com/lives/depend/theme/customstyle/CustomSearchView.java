package com.lives.depend.theme.customstyle;

import com.lives.depend.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

/**
 * ****************************************************************<br>
 * 文件名称 : CustomSearchView.java<br>
 * 作 者 : zjh<br>
 * 创建时间 : 2015-11-13 下午2:35:47<br>
 * 文件描述 : 自定义类SearchView效果的组件<br>
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-13 1.00 初始版本<br>
 ***************************************************************** 
 */
public class CustomSearchView extends RelativeLayout implements TextWatcher, OnEditorActionListener, OnClickListener
{

    private EditText mSearchEditText = null;

    private ImageView mClearImgView = null;

    private IOnSearchCallback mIOnSearchCallback = null;
    
    private IOnSearchClickCallback mIOnSearchClickCallback = null;

    public CustomSearchView(Context context)
    {
        super(context);
        // TODO Auto-generated constructor stub
        initResource(context, null);
    }

    public CustomSearchView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initResource(context, attrs);
    }

    public CustomSearchView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initResource(context, attrs);
    }

    private void initResource(Context context, AttributeSet attrs)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.putao_custom_searchview_layout, this, true);

        mSearchEditText = (EditText) findViewById(R.id.search_edittext);
        mClearImgView = (ImageView) findViewById(R.id.search_clear);

        if (attrs != null)
        {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomSearchView);
            if (typedArray != null)
            {
                // 背景色
                int bgLayoutResId = typedArray.getResourceId(R.styleable.CustomSearchView_searchview_background, -1);
                if (bgLayoutResId > 0)
                {
                    setBackgroundResource(bgLayoutResId);
                }

                // 清除图片
                int clearImgRes = typedArray.getResourceId(R.styleable.CustomSearchView_searchview_drawable_clear, -1);
                if (clearImgRes > 0)
                {
                    mClearImgView.setImageResource(clearImgRes);
                }

                // 搜索图片
                int searchImgRes = typedArray.getResourceId(R.styleable.CustomSearchView_searchview_drawableLeft, -1);
                if (searchImgRes > 0)
                {
                    mSearchEditText.setCompoundDrawablesWithIntrinsicBounds(
                            context.getResources().getDrawable(searchImgRes), null, null, null);
                }

                // 内容文字大小
                int textSize = typedArray.getDimensionPixelOffset(R.styleable.CustomSearchView_searchview_textSize, -1);
                if (textSize > 0)
                {
                    mSearchEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                }

                // 内容颜色
                int textColor = typedArray.getResourceId(R.styleable.CustomSearchView_searchview_textColor, -1);
                if (textColor > 0)
                {
                    mSearchEditText.setTextColor(context.getResources().getColor(textColor));
                }

                // 提示内容颜色
                int textColorHint = typedArray.getResourceId(R.styleable.CustomSearchView_searchview_textColorHint, -1);
                if (textColorHint > 0)
                {
                    mSearchEditText.setHintTextColor(context.getResources().getColor(textColorHint));
                }

                // 是否可以获取焦点
                boolean focusable = typedArray.getBoolean(R.styleable.CustomSearchView_searchview_focusable, true);
                mSearchEditText.setFocusable(focusable);
                mSearchEditText.setFocusableInTouchMode(focusable);

                // 显示的提示内容
                String showHintContent = typedArray.getString(R.styleable.CustomSearchView_searchview_hint);
                if (!TextUtils.isEmpty(showHintContent))
                {
                    mSearchEditText.setHint(showHintContent);
                }
            }
        }

        initListener();
    }

    private void initListener()
    {
        mSearchEditText.setOnEditorActionListener(this);
        mSearchEditText.addTextChangedListener(this);
        mSearchEditText.setOnClickListener(this);
        mSearchEditText.requestFocus();

        mClearImgView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        // TODO Auto-generated method stub
        int viewId = view.getId();
        if (viewId == R.id.search_edittext)
        {
            if (mIOnSearchClickCallback != null)
            {
                mIOnSearchClickCallback.onClickCallback();
            }
        }
        else if (viewId == R.id.search_clear)
        {
            mSearchEditText.getText().clear();
            if (mIOnSearchCallback != null)
            {
                mIOnSearchCallback.onClearCallback();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable)
    {
        // TODO Auto-generated method stub
        if (TextUtils.isEmpty(editable))
        {
            mClearImgView.setVisibility(View.GONE);
        }
        else
        {
            mClearImgView.setVisibility(View.VISIBLE);
        }
        if (mIOnSearchCallback != null)
        {
            mIOnSearchCallback.onAfterTextChanged(editable);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        // TODO Auto-generated method stub
        if(mIOnSearchCallback != null)
        {
            mIOnSearchCallback.onTextChanged(s, start, before, count);
        }
    }

    @Override
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event)
    {
        // TODO Auto-generated method stub
        if (mIOnSearchCallback != null)
        {
            return mIOnSearchCallback.onEditorAction(view, actionId, event);
        }
        return false;
    }

    public void setHint(String contentHint)
    {
        mSearchEditText.setHint(contentHint);
    }

    public void setHint(int resId)
    {
        mSearchEditText.setHint(resId);
    }

    public void setText(String content)
    {
        mSearchEditText.setText(content);
        mSearchEditText.setSelection(content.length());
    }

    public void setTextColor(int color)
    {
        mSearchEditText.setTextColor(color);
    }

    public void hideInputWindow()
    {
        ((InputMethodManager) mSearchEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(mSearchEditText.getApplicationWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void clear()
    {
        mSearchEditText.getText().clear();
        mSearchEditText.clearFocus();
    }
    
    public boolean isEmpty()
    {
        if(mSearchEditText != null && !TextUtils.isEmpty(mSearchEditText.getText().toString()))
        {
            return false;
        }
        return true;
    }

    public void setOnSearchCallback(IOnSearchCallback onSearchCallback)
    {
        mIOnSearchCallback = onSearchCallback;
    }
    
    public void setOnSearchClickCallback(IOnSearchClickCallback onSearchClickCallback)
    {
        mIOnSearchClickCallback = onSearchClickCallback;
    }

    public interface IOnSearchCallback
    {
        /**
         * 点击清除的回调(SearchView)
         */
        void onClearCallback();

        /**
         * 内容改变后的回调(SearchView)
         */
        void onAfterTextChanged(Editable editable);

        /**
         * 内容改变时的回调(SearchView)
         */
        void onTextChanged(CharSequence s, int start, int before, int count);

        /**
         * 点击执行动作的毁掉(SearchView)
         */
        boolean onEditorAction(TextView view, int actionId, KeyEvent event);
    }

    public interface IOnSearchClickCallback
    {
        /**
         * 点击编辑框的回调(SearchView)
         */
        void onClickCallback();
    }
}
