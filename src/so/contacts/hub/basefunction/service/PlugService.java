package so.contacts.hub.basefunction.service;

import com.putao.live.aidl.IPutaoService;
import so.contacts.hub.basefunction.utils.YellowUtil;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class PlugService extends Service
{
    private Context mContext;

    private PlugServiceImpl mplugServiceImpl = null;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mContext = this.getApplicationContext();
        mplugServiceImpl = new PlugServiceImpl();
        initData();
    }

    /**
     * 
     * 从资产文件中加载默认数据，并存储到数据库当中
     * void
     */
    private void initData()
    {
        //加载cms默认数据
        YellowUtil.loadCmsDefaultData();
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return mplugServiceImpl;
    }

    public class PlugServiceImpl extends IPutaoService.Stub
    {

        @Override
        public boolean userIsBind() throws RemoteException
        {
            return false;
        }

        @Override
        public void plugPause() throws RemoteException
        {

        }

        @Override
        public void plugResume() throws RemoteException
        {

        }

    }
}
