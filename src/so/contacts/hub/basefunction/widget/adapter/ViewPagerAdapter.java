package so.contacts.hub.basefunction.widget.adapter;

import java.util.List;

import so.contacts.hub.BaseFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    /**
     * 修改Fragment为BaseFragment
     * modified by wcy 2015-5-14 start 
     * old code:private List<Fragment> mFragmentList;
     */
	private List<BaseFragment> mFragmentList;
	/** modified by wcy 2015-5-14 end */
	
	public ViewPagerAdapter(FragmentManager fm, List<BaseFragment> fragmentList) {
		super(fm);
		mFragmentList = fragmentList;
	}

	@Override
	public Fragment getItem(int arg0) {
		return (mFragmentList == null || mFragmentList.size() == 0) ? null
				: mFragmentList.get(arg0);
	}

	@Override
	public int getCount() {
		return mFragmentList == null ? 0 : mFragmentList.size();
	}

}
