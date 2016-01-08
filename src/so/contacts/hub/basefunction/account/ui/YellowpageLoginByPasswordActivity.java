package so.contacts.hub.basefunction.account.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewAnimator;
import so.contacts.hub.BaseActivity;
import so.contacts.hub.basefunction.account.IAccCallback;
import so.contacts.hub.basefunction.account.manager.AccountManager;
import so.contacts.hub.basefunction.utils.NetUtil;
import so.contacts.hub.basefunction.utils.TelAreaUtil;

import com.putao.live.R;

public class YellowpageLoginByPasswordActivity extends BaseActivity implements OnClickListener, IAccCallback,
        OnFocusChangeListener
{
    private static final String TAG = "YellowpageLoginByPasswordActivity";

    private EditText mUserNameET;

    private ImageView mClearUserImgv;

    private EditText mPassWordET;

    private ImageView mClearPasswordImgv;

    private ImageView mShowPasswordIv;

    private Button mConfirmBtn;

    //密码是否明文显示
    private boolean isShowPassword = false;
    
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
        mUserNameET.setOnFocusChangeListener(this);
        mPassWordET.setTag(mClearPasswordImgv);
        mPassWordET.addTextChangedListener(new PutaoTextWatcher(mPassWordET));
        mPassWordET.setOnFocusChangeListener(this);

        mShowPasswordIv = (ImageView) findViewById(R.id.putao_show_password);
        mShowPasswordIv.setOnClickListener(this);
        findViewById(R.id.putao_login_captchar_tv).setOnClickListener(this);
        
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
                mUserNameET.setText("");
                mClearUserImgv.setVisibility(View.GONE);
                break;
            case R.id.putao_clear_password_iv:
                mPassWordET.setText("");
                mClearPasswordImgv.setVisibility(View.GONE);
                break;
            case R.id.putao_show_password:
                if (isShowPassword)
                {
                    isShowPassword = false;
                    mPassWordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPassWordET.setSelection(mPassWordET.getText().toString().length());
                    mShowPasswordIv.setImageResource(R.drawable.putao_icon_login_eyeclosed);
                }
                else
                {
                    isShowPassword = true;
                    mPassWordET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mPassWordET.setSelection(mPassWordET.getText().toString().length());
                    mShowPasswordIv.setImageResource(R.drawable.putao_icon_login_eyeopen);
                }
                break;
            case R.id.putao_login_captchar_tv:
                Intent intent = new Intent(YellowpageLoginByPasswordActivity.this,
                        YellowpageLoginByCaptureActivity.class);
                startActivity(intent);
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
        if (isMoblieRight())
        {
            if (NetUtil.isNetworkAvailable(this))
            {
                setIsLoginingStatus();
                String userName = mUserNameET.getText().toString().trim();
                // 去除空格
                userName = userName.replace(" ", "");
                String password = mPassWordET.getText().toString().trim();
                // 去除空格
                password = password.replace(" ", "");

                AccountManager.getInstance().loginByPassword(this, userName, password, this);
            }
            else
            {
                Toast.makeText(this, R.string.putao_login_net_wrong, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, R.string.putao_charge_phonenum_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess()
    {
        setNotLoginStatus();
        // 登录成功后，直接finish返回到menufagment
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onFail(int errorCode, String errorMsg)
    {
        setNotLoginStatus();
    }

    @Override
    public void onCancel()
    {
        setNotLoginStatus();
    }

    /**
     * 正在登录状态 void
     */
    private void setIsLoginingStatus()
    {
        showLoadingDialog(true);
        mProgressDialog.setMessage(getString(R.string.putao_logining));
        mConfirmBtn.getBackground().setAlpha(125);
        mConfirmBtn.setEnabled(false);
    }

    /**
     * 未登录状态 void
     */
    private void setNotLoginStatus()
    {
        dismissLoadingDialog();
        mConfirmBtn.getBackground().setAlpha(255);
        mConfirmBtn.setEnabled(true);
    }

    /**
     * 检查手机号是否合法
     * 
     * @return boolean
     */
    private boolean isMoblieRight()
    {
        String phonenumber = mUserNameET.getText().toString().trim();
        // 去除空格
        phonenumber = phonenumber.replace(" ", "");
        if (!TextUtils.isEmpty(phonenumber))
        {
            // 长度是否够，是否是电话号码的格式
            if (phonenumber.length() == 11 && TelAreaUtil.getInstance().isValidMobile(phonenumber))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus)
    {
        int id = view.getId();
        switch (id)
        {
            case R.id.putao_username_et:
                if (hasFocus)
                {
                    mUserNameET.setCompoundDrawablesWithIntrinsicBounds(R.drawable.putao_icon_account_phone_p, 0, 0, 0);
                }
                else
                {
                    mUserNameET.setCompoundDrawablesWithIntrinsicBounds(R.drawable.putao_icon_account_phone, 0, 0, 0);
                    mClearUserImgv.setVisibility(View.GONE);
                }
                break;
            case R.id.putao_password_et:
                if (hasFocus)
                {
                    mPassWordET.setCompoundDrawablesWithIntrinsicBounds(R.drawable.putao_icon_account_password_p, 0, 0,
                            0);
                }
                else
                {
                    mPassWordET
                            .setCompoundDrawablesWithIntrinsicBounds(R.drawable.putao_icon_account_password, 0, 0, 0);
                    mClearPasswordImgv.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

}
