package so.contacts.hub.basefunction.widget.dialog;

import com.putao.live.R;

import so.contacts.hub.basefunction.utils.Utils;
import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CommonDialog extends Dialog
{
    private Context mContext = null;

    private TextView mTitleTv;

    private ListView mListView;

    private TextView mMessageTv;

    private TextView mOkBtn, mCancelBtn;

    public CommonDialog(Context context, int resLayout)
    {
        super(context);
        this.mContext = context;
        init(resLayout);
    }

    public CommonDialog(Context context, int theme, int resLayout)
    {
        super(context, theme);
        this.mContext = context;
        init(resLayout);
    }

    /**
     * dialog初始化
     * 
     * @param resLayout void
     */
    private void init(int resLayout)
    {
        // 设置dialog的位置和大小，宽度为屏幕宽度的0.8，高度则自适应
        int width = 0;
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        // 需要考虑到横竖屏
        if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            // 横屏
            width = Utils.px2dip(mContext, displayMetrics.heightPixels * 0.8f);
        }
        else
        {
            // 竖屏
            width = Utils.px2dip(mContext, displayMetrics.widthPixels * 0.8f);
        }
        // 设置dailog的宽高
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = width;
        params.height = LayoutParams.WRAP_CONTENT;
        // 设置dialog在屏幕中的位置
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setAttributes(params);
        // 设置内容
        setContentView(resLayout);

        mTitleTv = (TextView) findViewById(R.id.title_tv);
        // 初始化不同的布局
        if (resLayout == R.layout.putao_common_list_dialog)
        {
            mListView = (ListView) findViewById(R.id.listview);
            mMessageTv = (TextView) findViewById(R.id.message_tv);
            mOkBtn = (TextView) findViewById(R.id.ok_btn);
            mCancelBtn = (TextView) findViewById(R.id.cancel_btn);
            setCancelButtonClickListener(null);
        }
        else if (resLayout == R.layout.putao_common_ok_cancel_dialog)
        {

        }
    }

    @Override
    public void show()
    {
        super.show();
    }

    @Override
    public void dismiss()
    {
        if (mContext != null && !((Activity) mContext).isFinishing())
        {
            super.dismiss();
        }
    }

    @Override
    public void setTitle(CharSequence title)
    {
        mTitleTv.setText(title);
    }

    @Override
    public void setTitle(int titleId)
    {
        mTitleTv.setText(titleId);
    }

    public View getTitle()
    {
        return mTitleTv;
    }

    /**
     * 获取lisview控件
     * 
     * @return ListView
     */
    public ListView getListView()
    {
        return mListView;
    }

    /**
     * 设置listview itemclcik事件
     * 
     * @param itemClickListener void
     */
    public void setListViewItemClickListener(OnItemClickListener itemClickListener)
    {
        if (mListView != null)
        {
            mListView.setOnItemClickListener(itemClickListener);
        }
        else
        {
            throw new NullPointerException("listview is null");
        }
        this.dismiss();
    }

    /**
     * 设置ok button的点击事件
     * 
     * @param clickListener void
     */
    public void setOkButtonClickListener(View.OnClickListener clickListener)
    {
        if (mOkBtn != null)
        {
            mOkBtn.setOnClickListener(clickListener);
        }
        else
        {
            throw new NullPointerException("mOkBtn is null");
        }
    }

    /**
     * 设置取消按钮的点击事件
     * 
     * @param clickListener void
     */
    public void setCancelButtonClickListener(View.OnClickListener clickListener)
    {
        if (mCancelBtn != null)
        {
            // 这里当cancellistener为空的时候，dailog直接dismiss
            if (clickListener == null)
            {
                clickListener = new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        dismiss();
                    }
                };
            }
            else
            {
                mCancelBtn.setOnClickListener(clickListener);
            }
        }
        else
        {
            throw new NullPointerException("mCancelBtn is null");
        }
    }

    /**
     * 设置listview adapter
     * 
     * @param adapter void
     */
    public void setListViewAdapter(BaseAdapter adapter)
    {
        if (mListView != null)
        {
            mListView.setAdapter(adapter);
        }
        else
        {
            throw new NullPointerException("mListView is null");
        }
    }

    /**
     * 设置listview数据源,简单listview直接调用setListViewDatas，
     * 复杂listview调用setListViewAdapter
     * 
     * @param data void
     */
    public void setListViewDatas(String[] data)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.putao_common_dialog_base_lv_item,
                data);
        if (mListView != null)
        {
            mListView.setAdapter(adapter);
        }
        else
        {
            throw new NullPointerException("mListView is null");
        }
    }
}
