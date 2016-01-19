package so.contacts.hub.services.telephone.ui;

import com.lives.depend.utils.LogUtil;
import com.putao.live.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import so.contacts.hub.BaseFragment;

public class YellowPageChargeTelephoneFragmentNew extends BaseFragment
{
    private static final String TAG = "ChargeTelephoneFragment";

    private View mContentView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mContentView = inflater.inflate(R.layout.putao_yellow_page_charge_new, null);
        LogUtil.i(TAG, "SpeedLog onCreateView end");
        return mContentView;
    }

}
