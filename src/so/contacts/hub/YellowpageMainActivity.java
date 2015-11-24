package so.contacts.hub;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import so.contacts.hub.basefunction.location.LBSManager;
import so.contacts.hub.basefunction.location.bean.LBSInfoItem;
import so.contacts.hub.basefunction.location.listener.LBSServiceListener;
import so.contacts.hub.basefunction.service.PlugService;
import so.contacts.hub.basefunction.widget.adapter.ViewPagerAdapter;
import so.contacts.hub.services.baseservices.ui.YellowPageGuideActivity;

import com.putao.live.R;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class YellowpageMainActivity extends BaseActivity implements OnClickListener, OnPageChangeListener,
        LBSServiceListener
{

    public static final String TAG = YellowpageMainActivity.class.getSimpleName();

    public static final int MSG_LOCATION_SUCCESS_ACTION = 0x271;

    public static final int MSG_LOCATION_FAILED_ACTION = 0x273;

    private static final int LOCATION_STATE_SUCCESS = 3;

    public static final String SHARED_PREFS_YELLOW_PAGE = "shared_prefs_yellow_page";

    public static final String SHOW_GUIDE_VIEW = "show_guide_view";

    private SharedPreferences mSharedPreferences;

    private EditText mEditText = null;

    private TextView mPosition;

    private RelativeLayout mTabMyLayout;

    private TextView mTabLifeTxtView, mTabNaviTxtView, mTabMyTxtView;

    // 卡片页卡
    private ImageView mTabMyRemindImgView;

    public ViewPager mMainViewPager;

    private List<BaseFragment> mFragmentList = new ArrayList<BaseFragment>();

    private ViewPagerAdapter mViewPagerAdapter = null;

    private YellowPageServicesFragment servicesFragment;

    private YellowPageNaviFragment naviFragment;

    private MenuFragment menuFragment;

    private boolean isInitData = false;

    private String mSelectedCity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.putao_yellow_page_main_layout);
        getWindow().setBackgroundDrawable(null);
        initViews();
        registerListeners();

        initData();

        Intent intent = new Intent(this, PlugService.class);
        startService(intent);
    }

    private void initData()
    {
        initSearchLoctionData();
    }

    private void initSearchLoctionData()
    {
        new Thread()
        {
            public void run()
            {
                // 进行定位操作
                LBSManager.getInstance().activate(YellowpageMainActivity.this, YellowpageMainActivity.this);
            };
        }.start();
    }

    private void registerListeners()
    {
        mMainViewPager.setOnPageChangeListener(this);
        mTabLifeTxtView.setOnClickListener(this);
        mTabNaviTxtView.setOnClickListener(this);
        mTabMyTxtView.setOnClickListener(this);
    }

    private void initViews()
    {
        mTabMyLayout = (RelativeLayout) findViewById(R.id.putao_main_tab_my);

        mTabMyTxtView = (TextView) findViewById(R.id.putao_main_tab_my_text);
        mTabLifeTxtView = (TextView) findViewById(R.id.putao_main_tab_life_text);
        mTabNaviTxtView = (TextView) findViewById(R.id.putao_main_tab_navigation_text);

        mTabMyRemindImgView = (ImageView) findViewById(R.id.putao_main_tab_my_remind);

        mMainViewPager = (ViewPager) findViewById(R.id.main_viewpager);

        menuFragment = new MenuFragment();
        naviFragment = new YellowPageNaviFragment();
        servicesFragment = new YellowPageServicesFragment();

        mFragmentList.add(servicesFragment);
        mFragmentList.add(naviFragment);
        mFragmentList.add(menuFragment);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPagerAdapter = new ViewPagerAdapter(fragmentManager, mFragmentList);
        mMainViewPager.setCurrentItem(0);
        mMainViewPager.setAdapter(mViewPagerAdapter);
        mMainViewPager.setOffscreenPageLimit(2);
        /*
         * add by zj 2015-4-10 start 把搜索框从fragment移到activity里
         */
        if (mEditText == null)
        {
            mEditText = (EditText) findViewById(R.id.search_content);
            mEditText.setOnClickListener(this);
        }
        if (mPosition == null)
        {
            mPosition = (TextView) findViewById(R.id.city_btn);
            mPosition.setOnClickListener(this);
        }
        // end 2015-4-10 by zj
        mSharedPreferences = getSharedPreferences(SHARED_PREFS_YELLOW_PAGE, Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (!mSharedPreferences.getBoolean(SHOW_GUIDE_VIEW, false))
        {
            mSharedPreferences.edit().putBoolean(SHOW_GUIDE_VIEW, true).commit();
            Intent intent = new Intent(this, YellowPageGuideActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        switch (id)
        {
            case R.id.putao_main_tab_life_text:
                mMainViewPager.setCurrentItem(0, true);
                break;
            case R.id.putao_main_tab_navigation_text:
                mMainViewPager.setCurrentItem(1, true);
                break;
            case R.id.putao_main_tab_my_text:
                mMainViewPager.setCurrentItem(2, true);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrollStateChanged(int arg0)
    {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2)
    {

    }

    @Override
    public void onPageSelected(int index)
    {
        ColorStateList defaultColor = getResources().getColorStateList(R.color.putao_tab_text_color_default);
        ColorStateList selectColor = getResources().getColorStateList(R.color.putao_tab_text_color_selected);
        switch (index)
        {
            case 0:
                mTabLifeTxtView.setTextColor(selectColor);
                mTabLifeTxtView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.putao_tab_icon_logo_p, 0, 0);
                mTabNaviTxtView.setTextColor(defaultColor);
                mTabNaviTxtView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.putao_tab_icon_navigation, 0, 0);
                mTabMyTxtView.setTextColor(defaultColor);
                mTabMyTxtView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.putao_tab_icon_me, 0, 0);
                break;
            case 1:
                mTabLifeTxtView.setTextColor(defaultColor);
                mTabLifeTxtView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.putao_tab_icon_logo, 0, 0);
                mTabNaviTxtView.setTextColor(selectColor);
                mTabNaviTxtView
                        .setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.putao_tab_icon_navigation_p, 0, 0);
                mTabMyTxtView.setTextColor(defaultColor);
                mTabMyTxtView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.putao_tab_icon_me, 0, 0);
                break;
            case 2:
                mTabLifeTxtView.setTextColor(defaultColor);
                mTabLifeTxtView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.putao_tab_icon_logo, 0, 0);
                mTabNaviTxtView.setTextColor(defaultColor);
                mTabNaviTxtView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.putao_tab_icon_navigation, 0, 0);
                mTabMyTxtView.setTextColor(selectColor);
                mTabMyTxtView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.putao_tab_icon_me_p, 0, 0);
                break;
            default:
                break;
        }
    }

    @Override
    protected boolean needReset()
    {
        return true;
    }

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MSG_LOCATION_SUCCESS_ACTION:
                    String city = (String) msg.obj;
                    //首页header显示定位城市
                    showLocationText(LOCATION_STATE_SUCCESS, city);
                    //定位为止变化后，通知各个fragment刷新数据
                    sendLocationChangedBroadCast();
                    break;

                default:
                    break;
            }
        };
    };

    protected void showLocationText(int locationState, String city)
    {
        switch (locationState)
        {
            case LOCATION_STATE_SUCCESS:
                mPosition.setText(city);
                break;

            default:
                break;
        }
    }

    /**
     * 定位发生变化,通知各个fragment刷新数据
     * void
     */
    private void sendLocationChangedBroadCast()
    {
        if(mFragmentList != null)
        {
            for(BaseFragment fragment : mFragmentList )
            {
                if(fragment != null)
                {
                    fragment.refreshDataByLocationChanged();
                }
            }
        }
    }

    @Override
    public void onLocationChanged(LBSInfoItem item)
    {
        if (item != null && !TextUtils.isEmpty(item.city))
        {
            Message msg = mHandler.obtainMessage();
            msg.what = MSG_LOCATION_SUCCESS_ACTION;
            msg.obj = item.city;
            mHandler.sendMessage(msg);
        }
        else
        {
            onLocationFailed();
        }
    }

    @Override
    public void onLocationFailed()
    {

    }

}
