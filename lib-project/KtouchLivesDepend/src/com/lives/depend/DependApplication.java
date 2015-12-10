package com.lives.depend;

import android.content.Context;

public class DependApplication
{
    private static Context mContext;

    public static void init(Context context)
    {
        mContext = context;
    }

    public static Context getContext()
    {
        return mContext;
    }

}
