package so.contacts.hub.services.telephone;

import java.util.List;

import com.putao.live.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import so.contacts.hub.services.baseservices.ui.YellowPageIndicatorFragmentActivity;
import so.contacts.hub.services.telephone.ui.YellowPageChargeTelephoneFragmentNew;
import so.contacts.hub.services.telephone.ui.YellowPageTrafficTelephoneFragment;

public class YellowPageReChargeActivity extends YellowPageIndicatorFragmentActivity
{

    private static final String TAG = "YellowPageReChargeActivity";

    public static final int FRAGMENT_CHARGETELEPHONE = 0;

    public static final int FRAGMENT_TRAFFICTELEPHONE = 1;

    protected int mCurrentTab = 0; // 当前显示的页面tab

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (TextUtils.isEmpty(mTitleContent))
        {
            mTitleContent = getString(R.string.putao_charge_tag_title);
        }
    }

    @Override
    protected int supplyTabs(List<TabInfo> tabs)
    {
        tabs.add(new TabInfo(FRAGMENT_CHARGETELEPHONE, getString(R.string.putao_charge_tag_title_charge),
                YellowPageChargeTelephoneFragmentNew.class));
        tabs.add(new TabInfo(FRAGMENT_TRAFFICTELEPHONE, getString(R.string.putao_charge_tag_title_tiffic),
                YellowPageTrafficTelephoneFragment.class));
        return mCurrentTab;
    }

    @Override
    protected void onInitFragmentEnd(int index, Fragment fragment)
    {

    }

    @Override
    protected void onPageSelectedAction(int index, Fragment fragment)
    {
        if (index == FRAGMENT_CHARGETELEPHONE)
        {
            setTitle(R.string.putao_charge_tag_title_charge);
        }
        else
        {
            setTitle(R.string.putao_charge_tag_title_tiffic);
        }
    }

}
