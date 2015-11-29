package so.contacts.hub.basefunction.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil
{
    public static boolean checkNet(Context context)
    {
        try
        {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null)
            {
                NetworkInfo info = manager.getActiveNetworkInfo();
                if (info != null && info.isConnected())
                {
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isNetworkAvailable(Context context)
    {
        return checkNet(context);
    }
}
