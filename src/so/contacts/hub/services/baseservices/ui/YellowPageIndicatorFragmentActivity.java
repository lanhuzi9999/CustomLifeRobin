/*
 * @author http://blog.csdn.net/singwhatiwanna
 */
package so.contacts.hub.services.baseservices.ui;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import so.contacts.hub.BaseActivity;
import so.contacts.hub.basefunction.widget.viewpagerindicator.PagerSlidingTabStrip;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.lives.depend.theme.ThemeController;
import com.putao.live.R;

public abstract class YellowPageIndicatorFragmentActivity extends BaseActivity implements OnPageChangeListener
{
    // private static final String TAG = "YellowPageIndicatorFragmentActivity";

    /**
     * 第一次进入时的页面tab add by zjh 2015-04-18
     */
    protected int mComeinTab = 0;

    protected int mCurrentTab = 0; // 当前显示的页面tab

    protected int mLastTab = -1;

    // 存放选项卡信息的列表
    protected ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

    protected MyAdapter myAdapter = null;

    protected ViewPager mPager;

    protected PagerSlidingTabStrip mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getMainViewResId());

        parseIntent();
        initViews();
    }

    protected void parseIntent()
    {
    }

    private final void initViews()
    {
        // 这里初始化界面
        mCurrentTab = supplyTabs(mTabs);

        if (ThemeController.isDefaultTheme())
        {
            initDataWithDefault();
        }
        else
        {
            initDataWithCustom();
        }
    }

    private void initDataWithDefault()
    {
        // TODO: 不同的主题 或者 厂商 实现自己不同的效果
        // 注：如果没有特殊UI、规范要求，可采用initDataWithCustom
        initDataWithCustom();
    }

    private void initDataWithCustom()
    {
        myAdapter = new MyAdapter(this, getSupportFragmentManager(), mTabs);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(myAdapter);
        mPager.setOffscreenPageLimit(mTabs.size());
        // 设置viewpager内部页面之间的间距
        mPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.putao_page_margin_width));
        // 设置viewpager内部页面间距的drawable
        mPager.setPageMarginDrawable(R.color.putao_page_viewer_margin_color);
        mPager.setCurrentItem(mCurrentTab);
        mLastTab = mCurrentTab;

        View headLayout = getHeadLayout();
        if (headLayout != null)
        {
            mIndicator = (PagerSlidingTabStrip) headLayout.findViewById(R.id.pagerindicator);
            mIndicator.init(mCurrentTab, mTabs, mPager);
            mIndicator.setOnPageChangeListener(this);
        }
    }

    @Override
    public int getHeadLayoutRes()
    {
        return R.layout.putao_tab_head_layout;
    }

    @Override
    protected void onBackPressCallback()
    {
        // TODO Auto-generated method stub
        finish();
    }

    @Override
    protected void onDestroy()
    {
        mTabs.clear();
        mTabs = null;
        if (myAdapter != null)
        {
            myAdapter.notifyDataSetChanged();
            myAdapter = null;
        }
        if (mPager != null)
        {
            mPager.setAdapter(null);
            mPager = null;
        }
        if (mIndicator != null)
        {
            mIndicator = null;
        }

        super.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
    }

    @Override
    public void onPageSelected(int position)
    {
        // mIndicator.onSwitched(position);
        mCurrentTab = position;
        onPageSelectedAction(mCurrentTab, mTabs.get(mCurrentTab).fragment);
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {
        if (state == ViewPager.SCROLL_STATE_IDLE)
        {
            mLastTab = mCurrentTab;
        }
    }

    protected TabInfo getFragmentById(int tabId)
    {
        if (mTabs == null)
        {
            return null;
        }
        for (int index = 0, count = mTabs.size(); index < count; index++)
        {
            TabInfo tab = mTabs.get(index);
            if (tab.getId() == tabId)
            {
                return tab;
            }
        }
        return null;
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

    /**
     * 获得布局id，默认为R.layout.putao_titled_fragment_tab_activity，可修改重写
     * 
     * @author lxh
     * @since 2015-9-8
     * @return 返回layout id
     */
    protected int getMainViewResId()
    {
        return R.layout.putao_titled_fragment_tab_activity;
    }

    /**
     * 在这里提供要显示的选项卡数据
     */
    protected abstract int supplyTabs(List<TabInfo> tabs);

    /**
     * 在这里提供初始化后的Fragment的特性初始化操作
     */
    protected abstract void onInitFragmentEnd(int index, Fragment fragment);

    /**
     * 切换选项卡监听器
     */
    protected abstract void onPageSelectedAction(int index, Fragment fragment);

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        // for fix a known issue in support library
        // https://code.google.com/p/android/issues/detail?id=19917
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    /**
     * 单个选项卡类，每个选项卡包含名字，图标以及提示（可选，默认不显示）
     */
    public class TabInfo implements Parcelable
    {

        private int id;

        private int icon;

        private String name = null;

        public boolean hasTips = false;

        public Fragment fragment = null;

        public boolean notifyChange = false;

        @SuppressWarnings("rawtypes")
        public Class fragmentClass = null;

        @SuppressWarnings("rawtypes")
        public TabInfo(int id, String name, Class clazz)
        {
            this(id, name, 0, clazz);
        }

        @SuppressWarnings("rawtypes")
        public TabInfo(int id, String name, boolean hasTips, Class clazz)
        {
            this(id, name, 0, clazz);
            this.hasTips = hasTips;
        }

        @SuppressWarnings("rawtypes")
        public TabInfo(int id, String name, int iconid, Class clazz)
        {
            super();

            this.name = name;
            this.id = id;
            icon = iconid;
            fragmentClass = clazz;
        }

        public TabInfo(Parcel p)
        {
            this.id = p.readInt();
            this.name = p.readString();
            this.icon = p.readInt();
            this.notifyChange = p.readInt() == 1;
        }

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public void setIcon(int iconid)
        {
            icon = iconid;
        }

        public int getIcon()
        {
            return icon;
        }

        @SuppressWarnings(
        { "rawtypes", "unchecked" })
        public Fragment createFragment(int pos)
        {
            if (fragment == null)
            {
                Constructor constructor;
                try
                {
                    constructor = fragmentClass.getConstructor(new Class[0]);
                    fragment = (Fragment) constructor.newInstance(new Object[0]);
                    onInitFragmentEnd(pos, fragment);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            return fragment;
        }

        public final Parcelable.Creator<TabInfo> CREATOR = new Parcelable.Creator<TabInfo>()
        {
            public TabInfo createFromParcel(Parcel p)
            {
                return new TabInfo(p);
            }

            public TabInfo[] newArray(int size)
            {
                return new TabInfo[size];
            }
        };

        @Override
        public int describeContents()
        {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel p, int flags)
        {
            p.writeInt(id);
            p.writeString(name);
            p.writeInt(icon);
            p.writeInt(notifyChange ? 1 : 0);
        }

    }

    private static class MyAdapter extends FragmentPagerAdapter
    {
        ArrayList<TabInfo> tabs = null;

        public MyAdapter(Context context, FragmentManager fm, ArrayList<TabInfo> tabs)
        {
            super(fm);
            this.tabs = tabs;
        }

        @Override
        public Fragment getItem(int pos)
        {
            Fragment fragment = null;
            if (tabs != null && pos < tabs.size())
            {
                TabInfo tab = tabs.get(pos);
                if (tab == null)
                    return null;
                fragment = tab.createFragment(pos);
            }
            return fragment;
        }

        @Override
        public int getItemPosition(Object object)
        {
            return POSITION_NONE;
        }

        @Override
        public int getCount()
        {
            if (tabs != null && tabs.size() > 0)
                return tabs.size();
            return 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            TabInfo tab = tabs.get(position);
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            tab.fragment = fragment;
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return tabs.get(position).name;
        }
    }

    // add by lxh 2015-9-11 源于原父类BaseRemindFragmentActivity
    @Override
    protected boolean needReset()
    {
        return true;
    }
}
