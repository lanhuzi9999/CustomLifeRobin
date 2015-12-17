package so.contacts.hub.basefunction.account.ui;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import so.contacts.hub.BaseActivity;
import so.contacts.hub.basefunction.account.IAccCallback;
import so.contacts.hub.basefunction.account.manager.AccountManager;
import so.contacts.hub.basefunction.utils.NetUtil;
import com.putao.live.R;

public class YellowpageLoginByPasswordActivity extends BaseActivity implements OnClickListener, IAccCallback
{
    private static final String TAG = "YellowpageLoginByPasswordActivity";

    private EditText mUserNameET;

    private ImageView mClearUserImgv;

    private EditText mPassWordET;

    private ImageView mClearPasswordImgv;

    private Button mConfirmBtn;

    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.putao_login_by_password_activity);
        initView();
    }

    private void initView()
    {
        setTitle(R.string.putao_login_title);
        mUserNameET = (EditText) findViewById(R.id.putao_username_et);
        mPassWordET = (EditText) findViewById(R.id.putao_password_et);

        mClearUserImgv = (ImageView) findViewById(R.id.putao_clear_username_iv);
        mClearUserImgv.setOnClickListener(this);
        mClearPasswordImgv = (ImageView) findViewById(R.id.putao_clear_password_iv);
        mClearPasswordImgv.setOnClickListener(this);

        mUserNameET.setTag(mClearUserImgv);
        mUserNameET.addTextChangedListener(new PutaoTextWatcher(mUserNameET));
        mPassWordET.setTag(mClearPasswordImgv);
        mPassWordET.addTextChangedListener(new PutaoTextWatcher(mPassWordET));

        mConfirmBtn = (Button) findViewById(R.id.putao_confirm_bt);
        mConfirmBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        switch (id)
        {
            case R.id.putao_confirm_bt:
                loginByPassword();
                break;
            case R.id.putao_clear_username_iv:

                break;
            case R.id.putao_clear_password_iv:

                break;
            default:
                break;
        }

    }

    /**
     * 密码登陆 void
     */
    private void loginByPassword()
    {
        if (NetUtil.isNetworkAvailable(this))
        {
            String userName = mUserNameET.getText().toString().trim();
            // 去除空格
            userName = userName.replace(" ", "");
            String password = mPassWordET.getText().toString().trim();
            // 去除空格
            password = password.replace(" ", "");

            AccountManager.getInstance().loginByPassword(this, userName, password, this);
        }
    }

    @Override
    public void onSuccess()
    {
        // 登录成功后，直接finish返回到menufagment
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onFail(int failed_code)
    {

    }

    @Override
    public void onCancel()
    {

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

}
