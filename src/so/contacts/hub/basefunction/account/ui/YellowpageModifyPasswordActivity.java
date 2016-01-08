package so.contacts.hub.basefunction.account.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.putao.live.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import so.contacts.hub.BaseActivity;
import so.contacts.hub.basefunction.account.IAccCallback;
import so.contacts.hub.basefunction.account.IAccCallbackAdv;
import so.contacts.hub.basefunction.account.manager.AccountManager;
import so.contacts.hub.basefunction.utils.NetUtil;
import so.contacts.hub.basefunction.utils.TelAreaUtil;

public class YellowpageModifyPasswordActivity extends BaseActivity implements OnClickListener, IAccCallback
{
    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    private EditText mUsernameET;

    private EditText mPasswordET;

    private Button mConfirmBT;

    private Button mGetCaptcharBT;

    private String mPhoneNum;

    private TimeCount mtimeCount;

    private SMSBroadcastReceiver mSmsBroadcastReceiver;

    private String captchar;

    private String mSender;
    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.putao_modify_password_activity);
        initView();
        initReceiver();
    }

    private void initReceiver()
    {
        mSmsBroadcastReceiver = new SMSBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SMS_RECEIVED_ACTION);
        registerReceiver(mSmsBroadcastReceiver, intentFilter);
    }

    private void initView()
    {
        setTitle(R.string.putao_login_set_password);
        mUsernameET = (EditText) findViewById(R.id.putao_phone_num_et);
        mPasswordET = (EditText) findViewById(R.id.putao_verify_code_et);
        mConfirmBT = (Button) findViewById(R.id.putao_confirm_bt);
        mConfirmBT.setText(R.string.putao_login_next);
        mConfirmBT.setOnClickListener(this);

        Intent intent = getIntent();
        String phoneNum = "";
        if (intent != null)
        {
            phoneNum = intent.getStringExtra("old_phone");
        }
        if (!TextUtils.isEmpty(phoneNum))
        {
            mUsernameET.setText(phoneNum);
            mUsernameET.setEnabled(false);
            mPasswordET.requestFocus();
        }

        findViewById(R.id.putao_clear_phone_num_iv).setOnClickListener(this);
        findViewById(R.id.putao_clear_verify_code_iv).setOnClickListener(this);

        mGetCaptcharBT = (Button) findViewById(R.id.putao_get_captchar_bt);
        mGetCaptcharBT.setOnClickListener(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(mSmsBroadcastReceiver);
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        switch (id)
        {
            case R.id.putao_get_captchar_bt:
                getCaptcha();
                break;
            case R.id.putao_confirm_bt:
                verifyCaptcha();
                break;
            case R.id.putao_clear_phone_num_iv:
                mUsernameET.setText("");
                findViewById(R.id.putao_clear_phone_num_iv).setVisibility(View.GONE);
                break;
            case R.id.putao_clear_verify_code_iv:
                mPasswordET.setText("");
                findViewById(R.id.putao_clear_verify_code_iv).setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private void getCaptcha()
    {
        if (NetUtil.isNetworkAvailable(this))
        {
            if (isMoblieRight())
            {
                String actionCode = "200004";
                String mobile = mUsernameET.getText().toString().trim();
                mobile = mobile.replace(" ", "");
                mtimeCount = new TimeCount(60000, 1000);

                AccountManager.getInstance().sendCaptchar(this, mobile, actionCode, new IAccCallbackAdv<String>()
                {

                    @Override
                    public void onSuccess(String t)
                    {
                        mSender = t;
                    }

                    @Override
                    public void onFail(int failed_code)
                    {
                        Toast.makeText(YellowpageModifyPasswordActivity.this, R.string.putao_send_captcha_error,
                                Toast.LENGTH_SHORT).show();
                        mGetCaptcharBT.setText(getString(R.string.putao_get_check_code));
                        mGetCaptcharBT.getBackground().setAlpha(255);
                        mGetCaptcharBT.setEnabled(true);
                        mGetCaptcharBT.setClickable(true);
                    }

                    @Override
                    public void onCancel()
                    {

                    }
                });
                mtimeCount.start();
            }
            else
            {
                Toast.makeText(this, R.string.putao_charge_phonenum_error, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, R.string.putao_login_net_wrong, Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyCaptcha()
    {
        if (NetUtil.isNetworkAvailable(this))
        {
            if (isMoblieRight())
            {
                setIsLoginingStatus();
                mPhoneNum = mUsernameET.getText().toString().trim();
                mPhoneNum = mPhoneNum.replace(" ", "");
                captchar = mPasswordET.getText().toString().trim();
                AccountManager.getInstance().verifyCaptchar(this, "200005", mPhoneNum, captchar, this);
            }
            else
            {
                Toast.makeText(this, R.string.putao_charge_phonenum_error, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, R.string.putao_login_net_wrong, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isMoblieRight()
    {
        String phonenumber = mUsernameET.getText().toString().trim();
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

    class TimeCount extends CountDownTimer
    {
        public TimeCount(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish()
        {
            mGetCaptcharBT.setText(getString(R.string.putao_get_check_code));
            mGetCaptcharBT.getBackground().setAlpha(255);
            mGetCaptcharBT.setEnabled(true);
            mGetCaptcharBT.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            mGetCaptcharBT.setText(millisUntilFinished / 1000 + "秒后重发");
            mGetCaptcharBT.getBackground().setAlpha(125);
            mGetCaptcharBT.setEnabled(false);
            mGetCaptcharBT.setClickable(false);
        }
    }

    class SMSBroadcastReceiver extends BroadcastReceiver
    {
        public SMSBroadcastReceiver()
        {

        }

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action) && action.equals(SMS_RECEIVED_ACTION))
            {
                Object[] pdus = (Object[]) intent.getExtras().get("pdus");
                for (Object pdu : pdus)
                {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    String content = "";
                    String sender = "";
                    if (smsMessage != null)
                    {
                        content = smsMessage.getDisplayMessageBody();
                        sender = smsMessage.getDisplayOriginatingAddress();
                    }
                    if (!TextUtils.isEmpty(sender) && sender.equals(mSender) || !TextUtils.isEmpty(content)
                            && content.contains(getString(R.string.putao_app_name))
                            && content.contains(getString(R.string.putao_phone_check_code)))
                    {
                        // 获取短信中的验证码
                        String code = getCode(content);
                        if (!TextUtils.isEmpty(code))
                        {
                            // 将验证码填写到验证码框
                            if (mPasswordET != null)
                            {
                                mPasswordET.setText(code);
                            }
                        }
                    }
                }
            }
        }
    }

    private String getCode(String content)
    {
        if (TextUtils.isEmpty(content))
        {
            return "";
        }
        Pattern pattern = Pattern.compile("(?<!\\d)\\d{4}(?!\\d)");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find())
        {
            return matcher.group();
        }
        return "";
    }

    @Override
    public void onSuccess()
    {
        setNotLoginStatus();
        // 验证码验证成功之后，跳到密码修改页
        Intent intent = new Intent(YellowpageModifyPasswordActivity.this, YellowpageSetPasswordActivity.class);
        intent.putExtra("accName", mPhoneNum);
        intent.putExtra("captchar", captchar);
        startActivity(intent);
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
     * 正在登陆按钮不可点击状态 void
     */
    private void setIsLoginingStatus()
    {
        showLoadingDialog();
        mConfirmBT.getBackground().setAlpha(125);
        mConfirmBT.setEnabled(false);
        mConfirmBT.setClickable(false);
    }

    /**
     * 按钮正常状态 void
     */
    private void setNotLoginStatus()
    {
        dismissLoadingDialog();
        mConfirmBT.getBackground().setAlpha(255);
        mConfirmBT.setEnabled(true);
        mConfirmBT.setClickable(true);
    }
}
