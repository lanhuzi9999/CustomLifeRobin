package so.contacts.hub.basefunction.operate.cms.adapter;

import java.util.List;

import so.contacts.hub.basefunction.imageloader.DataLoader;
import so.contacts.hub.basefunction.imageloader.image.ImageLoaderFactory;
import so.contacts.hub.basefunction.operate.cms.bean.CommonView;
import so.contacts.hub.basefunction.widget.ServiceItemLayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.putao.live.R;

/**
 * 首页-生活 常用服务 适配器
 * 
 * @author zjh
 * 
 */
public class CommonServicesAdapter extends BaseAdapter
{
    private static final String TAG = CommonServicesAdapter.class.getSimpleName();

    private List<CommonView> mDataList = null;

    private DataLoader mDataLoader = null;

    private Context context = null;

    private LayoutInflater mInflater = null;

    public CommonServicesAdapter(Context context, List<CommonView> dataList)
    {
        mInflater = LayoutInflater.from(context);
        mDataList = dataList;
        mDataLoader = new ImageLoaderFactory(context).getNormalLoaderWithDefaultImg(false, true,
                R.drawable.putao_service_def_logo_big);
    }

    /**
     * 刷新数据 方法表述
     * 
     * @param dataList void
     */
    public void refreshData(List<CommonView> dataList)
    {
        mDataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        if (mDataList != null)
        {
            return mDataList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int arg0)
    {
        // TODO Auto-generated method stub
        if (mDataList != null)
        {
            return mDataList.get(arg0);
        }
        return null;
    }

    @Override
    public long getItemId(int arg0)
    {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.putao_service_item_layout, null);
            holder.itemLayout = (ServiceItemLayout) convertView;
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.itemLayout.setData(mDataList.get(position), mDataLoader);
        return convertView;
    }

    private class ViewHolder
    {
        public ServiceItemLayout itemLayout;
    }

    /**
     * 清除缓存数据
     * 
     * @author wcy
     * @since 2015-5-12
     */
    public void clearCache()
    {
        if (mDataLoader != null)
        {
            mDataLoader.clearCache();
            mDataLoader = null;
        }
    }
}
