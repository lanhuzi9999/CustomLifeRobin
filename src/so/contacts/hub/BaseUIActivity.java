package so.contacts.hub;

import so.contacts.hub.basefunction.utils.ActivityMgr;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.putao.live.R;

public class BaseUIActivity extends FragmentActivity {
	private FrameLayout mContainer;
	protected long mServiceId = -1;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		ActivityMgr.getInstance().addActivity(this);
	}

	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		if (layoutResID == R.layout.putao_base_ui_with_ad_layout
				|| layoutResID == R.layout.putao_base_ui_layout || needReset()) {
			super.setContentView(layoutResID);
		} else {
			setContentView(R.layout.putao_base_ui_layout);
			View view = getLayoutInflater().inflate(layoutResID, null);
			mContainer = (FrameLayout) findViewById(R.id.container);
			mContainer.addView(view);
			addBackPressListener();
		}
	}

	private void addBackPressListener() {
		findViewById(R.id.back_layout).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View view) {
						onBackPressed();
					}
				});
	}

	@Override
	public void setContentView(View view) {
		if (needReset()) {
			super.setContentView(view);
			return;
		} else {
			setContentView(R.layout.putao_base_ui_layout);
			mContainer = (FrameLayout) findViewById(R.id.container);
			mContainer.addView(view);
			addBackPressListener();
		}
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		if (needReset()) {
			super.setContentView(view, params);
		} else {
			setContentView(R.layout.putao_base_ui_layout);
			mContainer = (FrameLayout) findViewById(R.id.container);
			mContainer.addView(view, params);
			addBackPressListener();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityMgr.getInstance().removeActivity(this);
	}

	/**
	 * 如果子类不用提供的好的布局方式，则重写此方法，返回true
	 * 
	 * @return
	 */
	protected boolean needReset() {
		return false;
	}

	/**
	 * 设置标题
	 */
	public void setTitle(int resId) {
		((TextView) findViewById(R.id.title)).setText(resId);
	}

	protected void setTitle(String title) {
		((TextView) findViewById(R.id.title)).setText(title);
	}
}
