package so.contacts.hub;

import android.app.Application;
import android.content.Context;

public class ContactsApp extends Application
{
    private static Context mContext;

    public static Context getInstance()
    {
        return mContext;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        mContext = this;
    }
}
