package so.contacts.hub;

import org.json.JSONException;
import org.json.JSONObject;

import so.contacts.hub.basefunction.utils.ActivityMgr;
import so.contacts.hub.basefunction.utils.YellowUtil;
import so.contacts.hub.basefunction.utils.constant.IntentKeyConstants;
import so.contacts.hub.services.baseservices.bean.YellowParams;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lives.depend.theme.dialog.CommonDialogFactory;
import com.lives.depend.theme.dialog.progress.AbstractProgressDialog;
import com.putao.live.R;

public class BaseActivity extends BaseUIActivity
{

    private static final String TAG = BaseActivity.class.getSimpleName();

    private FrameLayout mContainer;

    protected AbstractProgressDialog mProgressDialog = null;

    // CommonDialog commonDialog = null;

    protected String mTitleContent = null;

    protected int mRemindCode = -1;

    protected YellowParams mYellowParams = null;

    /**
     * 是否需要触发定位
     */
    protected boolean mNeedInitLocation = false;

    public String cpInfo;// cpInfo结构 add by hyl 2015-3-26

    public String cpName;// cp名称 add by hyl 2015-3-26

    /**
     * 服务点击跳转时传入的参数[Json格式] add by zjh 2015-03-21
     */
    protected String mClickParams = null;

    @Override
    public void setContentView(int layoutResID)
    {
        if (needReset())
        {
            super.setContentView(layoutResID);
        }
        else
        {
            // 这里暂时不考虑广告
            super.setContentView(R.layout.putao_base_ui_layout);
            View view = getLayoutInflater().inflate(layoutResID, null);
            mContainer = (FrameLayout) findViewById(R.id.container);
            mContainer.addView(view);
        }
        // 初始化headerview
        initHeadLayout();
    }

    @Override
    public void setContentView(View view)
    {
        if (needReset())
        {
            super.setContentView(view);
            initHeadLayout();
            return;
        }
        setContentView(R.layout.putao_base_ui_layout);
        mContainer = (FrameLayout) findViewById(R.id.container);
        mContainer.addView(view);
    }

    @Override
    public void setContentView(View view, LayoutParams params)
    {
        if (needReset())
        {
            super.setContentView(view, params);
            initHeadLayout();
            return;
        }
        setContentView(R.layout.putao_base_ui_layout);
        mContainer = (FrameLayout) findViewById(R.id.container);
        mContainer.addView(view, params);
    }

    @Override
    protected void onCreate(Bundle arg0)
    {
        super.onCreate(arg0);
        ActivityMgr.getInstance().addActivity(this);
        Intent intent = getIntent();
        // 解析cp,跳转信息
        if (intent != null)
        {
            mTitleContent = intent.getStringExtra(IntentKeyConstants.EXTRA_CLICK_INTENT_TITLE);
            mClickParams = intent.getStringExtra(IntentKeyConstants.EXTRA_CLICK_INTENT_PARAMS);
            Object object = intent.getSerializableExtra(IntentKeyConstants.EXTRA_TARGET_INTENT_PARAMS);
            if (object != null && object instanceof YellowParams)
            {
                mYellowParams = (YellowParams) object;
            }
            mServiceId = intent.getLongExtra(IntentKeyConstants.EXTRA_SERVICEID_PARAMS, 0);
            if (mServiceId == 0 && mYellowParams != null)
            {
                mServiceId = mYellowParams.getCategory_id();
            }
            if (TextUtils.isEmpty(mTitleContent) && mYellowParams != null)
            {
                mTitleContent = mYellowParams.getTitle();
            }
            mNeedInitLocation = intent.getBooleanExtra(IntentKeyConstants.EXTRA_NEED_INIT_LOCATION, false);

            cpInfo = intent.getStringExtra(YellowUtil.CP_INFO_PARAMS);
            try
            {
                if (cpInfo != null)
                {
                    JSONObject jsonObject = new JSONObject(cpInfo);
                    if (jsonObject.has("provider"))
                    {
                        cpName = jsonObject.getString("provider");
                    }
                }
            }
            catch (JSONException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        dismissLoadingDialog();
        ActivityMgr.getInstance().removeActivity(this);
        super.onDestroy();
    }

    /**
     * 如果子类不用提供的好的布局方式，则重写此方法，返回true
     * 
     * @return
     */
    protected boolean needReset()
    {
        return false;
    }

    /**
     * 显示加载框 void
     */
    public void showLoadingDialog()
    {
        showLoadingDialog(true);
    }

    /**
     * 显示加载框，是否有内容
     * 
     * @param isHaveContent void
     */
    public void showLoadingDialog(boolean isHaveContent)
    {
        if (isFinishing())
        {
            return;
        }
        if (mProgressDialog == null)
        {
            mProgressDialog = CommonDialogFactory.getProgressDialog(this, R.style.Theme_Ptui_Dialog_Progress);
            mProgressDialog.setMessage(getString(R.string.putao_yellow_page_loading));
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        if (!mProgressDialog.isShowing())
        {
            mProgressDialog.show();
        }
    }
    /**
     *加上注释，加载提消失
     ***/
    public void dismissLoadingDialog()
    {
        if (isFinishing())
        {
            return;
        }
        if (mProgressDialog != null)
        {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
