package com.lives.depend.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ThirdAccount implements IThirdAccount
{

    public ThirdAccount(IBaseAccountAction IAccountAction)
    {

    }

    @Override
    public String getUid(boolean silent)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BaseAccountInfo getAccountInfo()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getSource()
    {
        return 1;
    }

    @Override
    public int getType()
    {
        return 1;
    }

    @Override
    public boolean isLogin()
    {
        return false;
    }

    @Override
    public void showLoginView(Activity context, ILoginCallback cb)
    {
        try
        {
            Class<?> cls = Class.forName("so.contacts.hub.basefunction.account.ui.LoginByCaptureActivity");
            Intent intent = new Intent(context, cls);
            context.startActivity(intent);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            if (cb != null)
            {
                cb.onFail(-1);
            }
        }

    }

    @Override
    public void showAccountInfoView(Context context)
    {
        try
        {
            Class<?> cls = Class.forName("so.contacts.hub.basefunction.account.user.ui.PersonalInfoActivity");
            Intent intent = new Intent(context, cls);
            context.startActivity(intent);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public boolean isSupportDeviceLogin()
    {
        return true;
    }

}
