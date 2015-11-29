package so.contacts.hub;

import so.contacts.hub.basefunction.account.IAccCallbackAdv;
import so.contacts.hub.basefunction.account.manager.PutaoAccountManager;
import so.contacts.hub.basefunction.account.ui.YellowpageLoginByCaptureActivity;
import so.contacts.hub.basefunction.net.bean.RelateUser;
import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.putao.live.R;

public class MenuFragment extends BaseFragment implements OnClickListener
{

    private static final String TAG = "MenuFragment";

    private static final int MSG_INIT_USER_INFO = 0x01;

    private View mView;

    private ViewStub mMyLayoutStub;

    private LinearLayout mMyLayout;

    /** 账户头像 */
    private ImageView mAccHeadImv;

    /** 登录按钮 */
    private TextView mLoginTv;

    /** 登录提示信息 */
    private TextView mLogintip;

    /** 账号名字 */
    private TextView mAccNameTv;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.putao_menu, null);
        mMyLayoutStub = (ViewStub) mView.findViewById(R.id.putao_my_layout_stub);
        return mView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        /**
         * 首次启动加载延迟500ms,mMyLayout为空就表示还没有inflate过 ，是首次启动加载
         */
        if (mHandler != null)
        {
            if (mMyLayout == null)
            {
                mHandler.sendEmptyMessageDelayed(MSG_INIT_USER_INFO, 500);
            }
            else
            {
                mHandler.sendEmptyMessage(MSG_INIT_USER_INFO);
            }
        }
    }

    Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MSG_INIT_USER_INFO:
                    inflateMyLayout();
                    break;

                default:
                    break;
            }
        };
    };

    private void inflateMyLayout()
    {
        if (mMyLayoutStub != null)
        {
            mMyLayout = (LinearLayout) mMyLayoutStub.inflate();
            // 设置headerview
            ((TextView) mView.findViewById(R.id.title)).setText(R.string.putao_menu_title);
            mView.findViewById(R.id.back_layout).setVisibility(View.INVISIBLE);

            mAccHeadImv = (ImageView) mView.findViewById(R.id.menu_acc_head_icon);
            mLoginTv = (TextView) mView.findViewById(R.id.putao_login);
            mLogintip = (TextView) mView.findViewById(R.id.menu_acc_logintip);
            mAccNameTv = (TextView) mView.findViewById(R.id.menu_acc_name);

            mView.findViewById(R.id.putao_header_fl).setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        Intent intent = new Intent();
        switch (id)
        {
            case R.id.putao_header_fl:
//                PutaoAccountManager.getInstance().sendCaptchar(getActivity(), "18589032823", "200001", new IAccCallbackAdv<String>()
//                {
//                    
//                    @Override
//                    public void onSuccess(String t)
//                    {
//                        
//                    }
//                    
//                    @Override
//                    public void onFail(int failed_code)
//                    {
//                        
//                    }
//                    
//                    @Override
//                    public void onCancel()
//                    {
//                        
//                    }
//                });
                intent.setClass(getContext(), YellowpageLoginByCaptureActivity.class);
                break;

            default:
                break;
        }
        startActivity(intent);
    }
}
