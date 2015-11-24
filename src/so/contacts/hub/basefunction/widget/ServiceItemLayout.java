package so.contacts.hub.basefunction.widget;

import com.putao.live.R;

import so.contacts.hub.basefunction.imageloader.DataLoader;
import so.contacts.hub.basefunction.operate.cms.bean.CommonView;
import so.contacts.hub.basefunction.utils.ContactsHubUtils;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ServiceItemLayout extends RelativeLayout
{
    // view部分
    private ImageView mImageView;

    private TextView mTextView;

    // 数据部分
    private Context mContext;

    private CommonView mCommonView;

    private DataLoader mDataLoader;

    public ServiceItemLayout(Context context)
    {
        super(context);
        init(context);
    }

    public ServiceItemLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public ServiceItemLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context)
    {
        mContext = context;
    }

    public void setData(CommonView commonView, DataLoader dataLoader)
    {
        if (commonView == null || dataLoader == null)
        {
            return;
        }
        mCommonView = commonView;
        mDataLoader = dataLoader;

        refreshData();
    }

    private void refreshData()
    {
        String name = mCommonView.getName();
        String nameColor = mCommonView.getName_color();
        String photoUrl = mCommonView.getIcon_url();

        if (!TextUtils.isEmpty(name))
        {
            mTextView.setText(name);
        }
        if (!TextUtils.isEmpty(nameColor))
        {
            mTextView.setTextColor(Color.parseColor(nameColor));
        }
        if (!TextUtils.isEmpty(photoUrl))
        {
            if (ContactsHubUtils.isURlStr(photoUrl))
            {
                mDataLoader.loadData(photoUrl, mImageView);
            }
            else
            {
                mImageView.setImageResource(R.drawable.putao_service_def_logo_big);
            }
        }
        else
        {
            mImageView.setImageResource(R.drawable.putao_service_def_logo_big);
        }
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        mImageView = (ImageView) findViewById(R.id.yellow_page_img_item);
        mTextView = (TextView) findViewById(R.id.yellow_page_item);
    }
}
