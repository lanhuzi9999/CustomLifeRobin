package so.contacts.hub;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart()
    {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    public void onDestroyView()
    {
        // TODO Auto-generated method stub
        super.onDestroyView();
    }

    @Override
    public void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void showLoadingDialog()
    {
        showLoadingDialog(true);
    }

    private void showLoadingDialog(boolean isHaveContent)
    {
        Activity activity = this.getActivity();
        if (activity != null && activity instanceof BaseActivity)
        {
            ((BaseActivity) activity).showLoadingDialog(isHaveContent);
        }
    }

    public void dismissLoadingDialog()
    {
        Activity activity = this.getActivity();
        if (activity != null && activity instanceof BaseActivity)
        {
            ((BaseActivity) activity).dismissLoadingDialog();
        }
    }

    protected Context getContext()
    {
        if (getActivity() != null)
        {
            return getActivity();
        }
        else
        {
            return ContactsApp.getInstance();
        }
    }

    /**
     * 首页定位位置发生改变，通知fragment进行数据更新 
     * void
     */
    public void refreshDataByLocationChanged()
    {

    }
}
