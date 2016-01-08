package so.contacts.hub.basefunction.account.ui;

import com.putao.live.R;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import so.contacts.hub.BaseActivity;
import so.contacts.hub.basefunction.account.IAccCallback;
import so.contacts.hub.basefunction.account.manager.AccountManager;
import so.contacts.hub.basefunction.utils.NetUtil;

public class YellowpageSetPasswordActivity extends BaseActivity implements OnClickListener, IAccCallback
{

    private EditText mPasswordET;

    private EditText mConfirmPWET;

    private Button mConfirmBT;

    private String mAccName;

    private String mCaptchar;

    private String mPassword;

    private String mConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.putao_set_password_activity);
        parseIntent();
        initView();
    }

    private void initView()
    {
        mPasswordET = (EditText) findViewById(R.id.putao_password_et);
        mConfirmPWET = (EditText) findViewById(R.id.putao_confirm_password_et);
        mConfirmBT = (Button) findViewById(R.id.putao_confirm_bt);
        mConfirmBT.setOnClickListener(this);

        findViewById(R.id.putao_clear_password_iv).setOnClickListener(this);
        findViewById(R.id.putao_clear_confirm_password_iv).setOnClickListener(this);
    }

    private void parseIntent()
    {
        Intent intent = this.getIntent();
        if (intent != null)
        {
            mAccName = intent.getStringExtra("accName");
            mCaptchar = intent.getStringExtra("captchar");
        }
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        switch (id)
        {
            case R.id.putao_confirm_bt:
                resetPassword();
                break;
            case R.id.putao_clear_password_iv:
                mPasswordET.setText("");
                findViewById(R.id.putao_clear_password_iv).setVisibility(View.INVISIBLE);
                break;
            case R.id.putao_clear_confirm_password_iv:
                mConfirmPWET.setText("");
                findViewById(R.id.putao_clear_confirm_password_iv).setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 重置密码 void
     */
    private void resetPassword()
    {
        mPassword = mPasswordET.getText().toString().trim();
        mConfirmPassword = mConfirmPWET.getText().toString().trim();
        // 首先检查密码是否合法
        if (isPasswordValid(mPassword))
        {
            // 检查两次输入的密码是否一致
            if (isPasswordEquals())
            {
                if (NetUtil.isNetworkAvailable(this))
                {
                    // 修改密码,需要参数：手机号，验证码，新密码
                    AccountManager.getInstance().resetPasswordByCaptchar(this, mAccName, mPassword, mCaptchar, this);
                }
                else
                {
                    Toast.makeText(this, R.string.putao_login_net_wrong, Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, R.string.putao_input_not_equal, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, R.string.putao_login_password_invalid, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 密码是否正确
     * 
     * @return boolean
     */
    private boolean isPasswordValid(String passwordStr)
    {
        if (TextUtils.isEmpty(passwordStr))
        {
            return false;
        }
        String format = "[a-zA-Z0-9]{6,15}";
        if (passwordStr.matches(format))
        {
            return true;
        }
        return false;
    }

    /**
     * 两次输入的密码是否一致
     * 
     * @return boolean
     */
    private boolean isPasswordEquals()
    {
        if (TextUtils.isEmpty(mPassword))
        {
            mPassword = mPasswordET.getText().toString().trim();
        }
        if (TextUtils.isEmpty(mConfirmPassword))
        {
            mConfirmPassword = mConfirmPWET.getText().toString().trim();
        }
        if (!TextUtils.isEmpty(mPassword) && !TextUtils.isEmpty(mConfirmPassword))
        {
            if (mPassword.equals(mConfirmPassword))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSuccess()
    {
        Toast.makeText(YellowpageSetPasswordActivity.this, R.string.putao_login_set_password_ok, Toast.LENGTH_SHORT)
                .show();
        finish();
    }

    @Override
    public void onFail(int errorCode, String errorMsg)
    {
        if (!TextUtils.isEmpty(errorMsg))
        {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCancel()
    {

    }

    private void setIsLoginingStatus()
    {

    }

    private void setNotLoginingStatus()
    {

    }
}
