package so.contacts.hub.basefunction.location.listener;

import so.contacts.hub.basefunction.location.bean.LBSInfoItem;

public interface LBSServiceListener
{
    public void onLocationChanged(LBSInfoItem item);

    public void onLocationFailed();
}
