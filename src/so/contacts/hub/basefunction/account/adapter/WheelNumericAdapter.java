package so.contacts.hub.basefunction.account.adapter;

import com.putao.live.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import so.contacts.hub.basefunction.widget.wheel.adapter.AbstractWheelTextAdapter;

public class WheelNumericAdapter extends AbstractWheelTextAdapter
{
    private int mCurrentItem;// 当前选中的item值

    private String mUinit;// 年月日的后缀

    private int mMinValue;// 年月日最小值

    private int mMaxValue;// 年月日最大值

    public WheelNumericAdapter(Context context, int minValue, int maxValue, String unit)
    {
        super(context, R.layout.putao_common_wheel_dialog_text_item, NO_RESOURCE);
        setItemTextResource(R.id.putao_wheel_dialog_text_item);
        mUinit = unit;
        mMinValue = minValue;
        mMaxValue = maxValue;
    }

    @Override
    public int getItemsCount()
    {
        return mMaxValue - mMinValue + 1;
    }

    @Override
    protected CharSequence getItemText(int index)
    {
        return mMinValue + index + mUinit;
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent)
    {
        mCurrentItem = index;
        return super.getItem(index, convertView, parent);
    }
    
    @Override
    protected void configureTextView(TextView view)
    {
        view.setGravity(Gravity.CENTER);
        view.setTextSize(16);
        view.setLines(1);
        view.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
    }
}
