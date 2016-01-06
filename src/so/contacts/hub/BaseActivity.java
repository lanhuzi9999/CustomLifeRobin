package so.contacts.hub;

import org.json.JSONException;
import org.json.JSONObject;

import so.contacts.hub.basefunction.utils.YellowUtil;
import so.contacts.hub.services.baseservices.bean.YellowParams;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lives.depend.theme.dialog.CommonDialogFactory;
import com.lives.depend.theme.dialog.progress.AbstractProgressDialog;
import com.putao.live.R;


public class BaseActivity extends BaseUIActivity {
	
	private static final String TAG = BaseActivity.class.getSimpleName();
	
	protected AbstractProgressDialog mProgressDialog = null;

	// CommonDialog commonDialog = null;

	protected String mTitleContent = null;

	protected int mRemindCode = -1;

	protected YellowParams mYellowParams = null;

	public String cpInfo;// cpInfo结构 add by hyl 2015-3-26
	public String cpName;// cp名称 add by hyl 2015-3-26

	/**
	 * 服务点击跳转时传入的参数[Json格式] add by zjh 2015-03-21
	 */
	protected String mClickParams = null;

	/**
	 * 加载广告数据 add by zjh 2015-05-19
	 */
	// protected AdViewCreator mAdViewCreator = null;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Intent intent = getIntent();
		// 解析cp,跳转信息
		if (intent != null) {
			mTitleContent = intent.getStringExtra("title");
			mClickParams = intent.getStringExtra(YellowUtil.ClickIntentParams);
			mYellowParams = (YellowParams) intent
					.getSerializableExtra(YellowUtil.TargetIntentParams);
			mServiceId = intent.getLongExtra(YellowUtil.ServiceIdParams, 0);
			if (mServiceId == 0 && mYellowParams != null) {
				mServiceId = mYellowParams.getCategory_id();
			}
			if (TextUtils.isEmpty(mTitleContent) && mYellowParams != null) {
				mTitleContent = mYellowParams.getTitle();
			}

			cpInfo = intent.getStringExtra(YellowUtil.CP_INFO_PARAMS);
			try {
				if (cpInfo != null) {
					JSONObject jsonObject = new JSONObject(cpInfo);
					if (jsonObject.has("provider")) {
						cpName = jsonObject.getString("provider");
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 显示cp信息
	 */
	private void showCpInfo() {
		TextView subTitle = (TextView) findViewById(R.id.subtitle);
		if (subTitle != null && !TextUtils.isEmpty(cpName)) {
			subTitle.setText(cpName);
			subTitle.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResume() {
		showCpInfo();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		dismissLoadingDialog();
		super.onDestroy();
	}

	public void showLoadingDialog() {
		showLoadingDialog(true);
	}

	public void showLoadingDialog(boolean isHaveContent) {
		if (isFinishing()) {
			return;
		}
		if (mProgressDialog == null) {
			mProgressDialog = CommonDialogFactory.getProgressDialog(this, R.style.Theme_Ptui_Dialog_Progress);
			mProgressDialog
					.setMessage(getString(R.string.putao_yellow_page_loading));
			mProgressDialog.setCanceledOnTouchOutside(false);
		}
		if (!mProgressDialog.isShowing()) {
			mProgressDialog.show();
		}
	}

	public void dismissLoadingDialog() {
		if (isFinishing()) {
			return;
		}
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}
}
