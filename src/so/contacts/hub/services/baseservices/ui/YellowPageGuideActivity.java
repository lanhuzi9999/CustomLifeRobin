package so.contacts.hub.services.baseservices.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.putao.live.R;

/**
 * 引导页
 * 
 * @author Administrator
 * 
 */
public class YellowPageGuideActivity extends Activity {

	private ViewPager mViewPager;
	private GuidePagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.putao_guide_layout);
		initViews();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		mViewPager = (ViewPager) findViewById(R.id.guide_viewpager);
		mPagerAdapter = new GuidePagerAdapter();
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(0);
	}

	private class GuidePagerAdapter extends PagerAdapter {
		List<ImageView> imageList = null;

		public GuidePagerAdapter() {
			initImageViews();
		}

		private void initImageViews() {
			// TODO Auto-generated method stub
			imageList = new ArrayList<ImageView>();
			ImageView imageView1 = new ImageView(YellowPageGuideActivity.this);
			imageView1.setScaleType(ScaleType.FIT_XY);
			imageView1.setImageResource(R.drawable.putao_pic_guide1);
			imageList.add(imageView1);
			ImageView imageView2 = new ImageView(YellowPageGuideActivity.this);
			imageView2.setScaleType(ScaleType.FIT_XY);
			imageView2.setImageResource(R.drawable.putao_pic_guide2);
			imageList.add(imageView2);
			ImageView imageView3 = new ImageView(YellowPageGuideActivity.this);
			imageView3.setScaleType(ScaleType.FIT_XY);
			imageView3.setImageResource(R.drawable.putao_pic_guide3);
			imageList.add(imageView3);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (imageList == null) {
				return 0;
			} else {
				return imageList.size();
			}
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method
			// 点击到最后一张的时候退出引导页，进入mainactivity
			if (position == imageList.size() - 1) {
				imageList.get(position).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View view) {
								// TODO Auto-generated method stub
								finish();
							}
						});
			}
			container.addView(imageList.get(position));
			return imageList.get(position);
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView(imageList.get(position));
		}
	}
}
