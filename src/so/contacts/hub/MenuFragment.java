package so.contacts.hub;

import org.json.JSONObject;

import so.contacts.hub.basefunction.account.IAccChangeListener;
import so.contacts.hub.basefunction.account.bean.BasicUserInfoBean;
import so.contacts.hub.basefunction.account.bean.PTUser;
import so.contacts.hub.basefunction.account.manager.AccountManager;
import so.contacts.hub.basefunction.account.manager.UserInfoManager;
import so.contacts.hub.basefunction.account.ui.YellowpageLoginByCaptureActivity;
import so.contacts.hub.basefunction.account.ui.YellowpagePersonalInfoActivity;
import so.contacts.hub.basefunction.config.Config;
import so.contacts.hub.basefunction.imageloader.DataLoader;
import so.contacts.hub.basefunction.imageloader.image.ImageLoaderFactory;
import so.contacts.hub.basefunction.net.bean.RelateUser;
import so.contacts.hub.basefunction.net.manager.IResponse;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.putao.live.R;

public class MenuFragment extends BaseFragment implements OnClickListener, IAccChangeListener, IResponse
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

    /** ptuser */
    private PTUser mPtUser;

    /**
     * 账户头像url
     */
    private String mHeadIconUrl;

    /**
     * 头像加载器
     */
    private DataLoader mImageLoader;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        AccountManager.getInstance().registerAccChangeListener(this);
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
                    // 加载账户信息,这里就先不考虑静默登录了
                    mPtUser = AccountManager.getInstance().getPtUser();
                    if (mPtUser != null)
                    {
                        initAccoutInfo();
                    }
                    else
                    {
                        showAccountInfo(false, null);
                    }
                    break;

                default:
                    break;
            }
        };
    };

    /**
     * 初始化布局 void
     */
    private void inflateMyLayout()
    {
        if (mMyLayoutStub != null && mMyLayout == null)
        {
            mMyLayout = (LinearLayout) mMyLayoutStub.inflate();
            // 设置headerview
            ((TextView) mView.findViewById(R.id.title)).setText(R.string.putao_menu_title);
            mView.findViewById(R.id.back_layout).setVisibility(View.INVISIBLE);

            mAccHeadImv = (ImageView) mView.findViewById(R.id.menu_acc_head_icon);
            mAccNameTv = (TextView) mView.findViewById(R.id.menu_acc_name);
            mLoginTv = (TextView) mView.findViewById(R.id.putao_login);
            mLogintip = (TextView) mView.findViewById(R.id.menu_acc_logintip);

            mView.findViewById(R.id.putao_header_fl).setOnClickListener(this);

        }
    }

    /**
     * 初始化 void
     */
    protected void initAccoutInfo()
    {
        mPtUser = AccountManager.getInstance().getPtUser();
        if (mPtUser != null)
        {
            RelateUser relateUser = AccountManager.getInstance().getRelateUser(RelateUser.TYPE_PHONE);
            if (relateUser != null)
            {
                showAccountInfo(true, relateUser);
                return;
            }
        }
        showAccountInfo(false, null);
    }

    /**
     * 显示账户信息
     * 
     * @param isLogin
     * @param relateUser void
     */
    private void showAccountInfo(boolean isLogin, RelateUser relateUser)
    {
        // 区分是否登陆，分别显示
        if (isLogin)
        {
            // 有账户登陆，名称显示顺序：手机号码，帐户名accName
            String displayName = AccountManager.getInstance().getDisplayName(relateUser);
            if (TextUtils.isEmpty(displayName))
            {
                displayName = relateUser.accName;
            }
            mAccNameTv.setText(displayName);
            mAccNameTv.setVisibility(View.VISIBLE);
            // 从个人信息数据库读取头像地址，为空的时候才去加载默认头像
            mHeadIconUrl = Config.getDatabaseHelper().getPersonInfoDB().queryImgUrl();
            if (!TextUtils.isEmpty(mHeadIconUrl))
            {
                if (mImageLoader == null)
                {
                    mImageLoader = new ImageLoaderFactory(getContext()).getStatusAvatarLoader();
                }
                mImageLoader.loadData(mHeadIconUrl, mAccHeadImv);
            }
            else
            {
                mAccHeadImv.setImageResource(R.drawable.putao_menu_acc_headimg_logined);
            }

            mLoginTv.setVisibility(View.INVISIBLE);
            mLogintip.setVisibility(View.INVISIBLE);
        }
        else
        {
            mAccHeadImv.setImageResource(R.drawable.putao_menu_acc_headimg_nologin);
            mAccNameTv.setVisibility(View.INVISIBLE);
            mLoginTv.setVisibility(View.VISIBLE);
            mLogintip.setVisibility(View.VISIBLE);
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
                RelateUser relateUser = AccountManager.getInstance().getRelateUser(RelateUser.TYPE_PHONE);
                if (relateUser != null)
                {
                    intent.setClass(getContext(), YellowpagePersonalInfoActivity.class);
                }
                else
                {
                    intent.setClass(getContext(), YellowpageLoginByCaptureActivity.class);
                }
                break;

            default:
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onLogin()
    {
        UserInfoManager.getInstance().getUserBasicData(getContext(), this);
    }

    @Override
    public void onLogout()
    {

    }

    @Override
    public void onChange()
    {
        UserInfoManager.getInstance().getUserBasicData(getContext(), this);
    }

    @Override
    public void onSuccess(String content)
    {
        try
        {
            JSONObject object = new JSONObject(content);
            String ret_code = object.getString("ret_code");
            if ("0000".endsWith(ret_code))
            {
                JSONObject data = object.getJSONObject("data");
                int hasSetPassword = data.optInt("is_set_password");
                String head_pic = data.optString("head_pic");
                String city = data.optString("city");
                int gender = data.optInt("gender");
                String birthDay = data.optString("birthday");
                BasicUserInfoBean bean = new BasicUserInfoBean(hasSetPassword, head_pic, city, gender, birthDay);
                Config.getDatabaseHelper().getPersonInfoDB().insertData(bean);
                initAccoutInfo();
            }
            else
            {// 此次为后台查询,不提示 for BUG #5109 modified by jsy 2015-06-11
             // Toast.makeText(getContext(),
             // getContext().getString(R.string.putao_personal_get_user_info_fail),
             // Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onFail(int errorCode)
    {

    }
}
