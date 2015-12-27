package so.contacts.hub.basefunction.account.adapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import so.contacts.hub.basefunction.widget.wheel.adapter.AbstractWheelTextAdapter;
import com.putao.live.R;

public class WheelTextAdapter extends AbstractWheelTextAdapter
{
    private List<String> mList = new ArrayList<String>();

    public WheelTextAdapter(Context context, List<String> list)
    {
        super(context,R.layout.putao_common_wheel_dialog_text_item, NO_RESOURCE);
        setItemTextResource(R.id.putao_wheel_dialog_text_item);
        mList.addAll(list);
    }

    @Override
    public int getItemsCount()
    {
        return mList.size();
    }

    @Override
    protected CharSequence getItemText(int index)
    {
        return mList.get(index);
    }

    @Override
    protected void configureTextView(TextView view)
    {
        view.setGravity(Gravity.CENTER);
        view.setTextSize(16);
        view.setLines(1);
        view.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent)
    {
        return super.getItem(index, convertView, parent);
    }

    /**
     * 传入数据
     * 
     * @param list void
     */
    public void setData(List<String> list)
    {
        mList.clear();
        mList.addAll(list);
        notifyDataInvalidatedEvent();
    }
}
