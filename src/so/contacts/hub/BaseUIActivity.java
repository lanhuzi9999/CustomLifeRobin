package so.contacts.hub;

import com.putao.live.R;

import so.contacts.hub.basefunction.utils.ActivityMgr;
import android.R.integer;
import android.app.ActionBar;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BaseUIActivity extends FragmentActivity
{
    private FrameLayout mContainer;

    protected long mServiceId = -1;

    private View mHeadLayout = null;

    private ImageView mBackImageView = null;

    private TextView mTitleTView = null;

    private TextView mSubTitleTView = null;

    private RelativeLayout mNextStepLayout = null;

    private ImageView mNextStepImgView = null;

    private TextView mNextStepTView = null;

    private View mDividerView = null;

    /**
     * 1.将setContentView放到baseactivity做处理，baseuiactivity只对headvie的ui做处理
     * 2.baseactivity还负责intent传递参数的解析和progressdailog等控件的控制
     */
    protected void initHeadLayout()
    {
        if (!needShowHeadLayout())
        {
            return;
        }

        /** 兼容actionbar,首先获取mHeadLayout */
        ActionBar actionBar = getActionBar();
        if (actionBar != null)
        {
            mHeadLayout = LayoutInflater.from(this).inflate(getHeadLayoutRes(), null);
            actionBar.setCustomView(mHeadLayout);
            /** enable自定义view */
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        }
        else
        {
            View view = findViewById(R.id.head_layout_viewstub);
            if (view != null && view instanceof ViewStub)
            {
                ViewStub viewStub = (ViewStub) view;
                mHeadLayout = viewStub.inflate();
            }
            else
            {
                throw new IllegalArgumentException("no head view to show.");
            }
        }
        handleBackLayout();
        handleTitleLayout();
        handleNextStepLayout();
        mDividerView = mHeadLayout.findViewById(R.id.header_divider);
    }

    /**
     * 是否显示headview,默认是要显示的
     * 
     * @return boolean
     */
    protected boolean needShowHeadLayout()
    {
        return true;
    }

    /**
     * 处理head返回事件 void
     */
    private void handleBackLayout()
    {
        View backLayout = mHeadLayout.findViewById(R.id.back_layout);
        if (backLayout != null)
        {
            backLayout.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View view)
                {
                    onBackPressCallback();
                }
            });
        }
        View backImageView = mHeadLayout.findViewById(R.id.back);
        if (backImageView instanceof ImageView)
        {
            mBackImageView = (ImageView) backImageView;
        }
    }

    /**
     * 设置back键资源
     * 
     * @param resId void
     */
    protected void setBackImageResource(int resId)
    {
        if (mBackImageView != null && resId > 0)
        {
            mBackImageView.setImageResource(resId);
        }
    }

    /**
     * 处理head title内容 void
     */
    private void handleTitleLayout()
    {
        View titleView = mHeadLayout.findViewById(R.id.title);
        if (titleView instanceof TextView)
        {
            mTitleTView = (TextView) titleView;
        }

        View subTitleView = mHeadLayout.findViewById(R.id.subtitle);
        if (subTitleView instanceof TextView)
        {
            mSubTitleTView = (TextView) subTitleView;
        }

    }

    /**
     * 设置标题
     */
    @Override
    public void setTitle(int titleId)
    {
        if (mTitleTView != null)
        {
            mTitleTView.setText(titleId);
        }
    }

    /**
     * 设置标题
     * 
     * @param title void
     */
    protected void setTitle(String title)
    {
        if (mTitleTView != null)
        {
            mTitleTView.setText(title);
        }
    }

    /**
     * get title
     * 
     * @return TextView
     */
    public TextView getTitleTView()
    {
        return mTitleTView;
    }

    /**
     * 是否显示title
     * 
     * @param isShow
     */
    public void showTitleTView(boolean isShow)
    {
        if (mTitleTView != null)
        {
            if (isShow)
            {
                mTitleTView.setVisibility(View.VISIBLE);
            }
            else
            {
                mTitleTView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置副标题 方法表述
     * 
     * @param subTitle void
     */
    protected void setSubTitle(String subTitle)
    {
        if (mSubTitleTView != null)
        {
            if (TextUtils.isEmpty(subTitle))
            {
                mSubTitleTView.setVisibility(View.GONE);
            }
            else
            {
                mSubTitleTView.setText(subTitle);
                mSubTitleTView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 设置副标题
     * 
     * @param subTitleId void
     */
    protected void setSubTitle(int subTitleId)
    {
        if (mSubTitleTView != null && subTitleId > 0)
        {
            mSubTitleTView.setText(subTitleId);
            mSubTitleTView.setVisibility(View.VISIBLE);
        }
    }

    public TextView getSubTitleTView()
    {
        return mSubTitleTView;
    }

    /**
     * 处理NextStep的内容 void
     */
    private void handleNextStepLayout()
    {
        View nextStepLayout = mHeadLayout.findViewById(R.id.next_setp_layout);
        if (nextStepLayout != null && nextStepLayout instanceof RelativeLayout)
        {
            mNextStepLayout = (RelativeLayout) nextStepLayout;
        }

        View nextStepImgview = mHeadLayout.findViewById(R.id.next_step_img);
        if (nextStepImgview != null && nextStepImgview instanceof ImageView)
        {
            mNextStepImgView = (ImageView) nextStepImgview;
        }

        View nextStepTextView = mHeadLayout.findViewById(R.id.next_step_btn);
        if (nextStepTextView != null && nextStepTextView instanceof TextView)
        {
            mNextStepTView = (TextView) nextStepTextView;
        }
    }

    /**
     * 是否显示nextstep layout
     * 
     * @param isShow void
     */
    public void showNextStepLayout(boolean isShow)
    {
        if (mNextStepLayout != null)
        {
            if (isShow)
            {
                mNextStepLayout.setVisibility(View.VISIBLE);
            }
            else
            {
                mNextStepLayout.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置nextstep title标题
     * 
     * @param title void
     */
    protected void setNextStepTitle(String title)
    {
        if (!TextUtils.isEmpty(title) && mNextStepTView != null)
        {
            mNextStepTView.setText(title);
            mNextStepTView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置nextstep title标题
     * 
     * @param nextTitleId void
     */
    protected void setNextStepTitle(int nextTitleId)
    {
        if (mNextStepTView != null && nextTitleId > 0)
        {
            mNextStepTView.setText(nextTitleId);
            mNextStepTView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置nextstep title标题字体的大小和颜色
     * 
     * @param textColor
     * @param textSize void
     */
    protected void setNextStepTextColorAndSize(int textColor, int textSize)
    {
        if (mNextStepTView != null)
        {
            if (textColor > 0)
            {
                mNextStepTView.setTextColor(textColor);
            }
            if (textSize > 0)
            {
                mNextStepTView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            }
        }
    }

    /**
     * 设置nextstep图标背景资源
     * 
     * @param resId void
     */
    public void setNextStepImgResource(int resId)
    {
        if (mNextStepImgView != null && resId > 0)
        {
            mNextStepImgView.setImageResource(resId);
            mNextStepImgView.setVisibility(View.VISIBLE);
        }
    }

    public TextView getNextStepTView()
    {
        return mNextStepTView;
    }

    public ImageView getNextStepImgView()
    {
        return mNextStepImgView;
    }

    public RelativeLayout getNextStepLayout()
    {
        return mNextStepLayout;
    }

    public void showHeadDivider(boolean isShow)
    {
        if (mDividerView != null)
        {
            if (isShow)
            {
                mDividerView.setVisibility(View.VISIBLE);
            }
            else
            {
                mDividerView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Head栏的布局
     */
    protected int getHeadLayoutRes()
    {
        return R.layout.putao_header_view;
    }

    public View getHeadLayout()
    {
        return mHeadLayout;
    }

    /**
     * Head栏 中 点击“返回”时的回调
     */
    protected void onBackPressCallback()
    {
        onBackPressed();
    }
}
