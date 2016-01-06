package so.contacts.hub.basefunction.account.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import com.putao.live.R;

public class YellowpageLoginByCaptureActivity extends BaseActivity implements OnClickListener, IAccCallback
{
    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    private EditText mUserNamEditText;

    private EditText mPassWordEditText;

    private Button mGetCaptcharButton;

    private Button mLoginButton;

    // 填入的手机号码
    private String mPhoneNumber;

    private SMSBroadcastReceiver mSMSBroadcastReceiver;

    // 验证码重发计时器
    private TimeCount mTimeCount;

    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.putao_login_by_capture_activity);
        initView();
        initReceiver();
    }

    private void initReceiver()
    {
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SMS_RECEIVED_ACTION);
        registerReceiver(mSMSBroadcastReceiver, filter);
    }

    private void initView()
    {
        // 设置标题
        setTitle(getString(R.string.putao_login_title));
        mUserNamEditText = (EditText) findViewById(R.id.putao_username_et);
        mPassWordEditText = (EditText) findViewById(R.id.putao_password_et);
        mGetCaptcharButton = (Button) findViewById(R.id.putao_get_captchar_bt);
        mLoginButton = (Button) findViewById(R.id.putao_confirm_bt);

        mGetCaptcharButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
        
        findViewById(R.id.putao_login_password_tv).setOnClickListener(this);
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
        unregisterReceiver(mSMSBroadcastReceiver);
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
                    String content = smsMessage.getDisplayMessageBody();
                    String sender = smsMessage.getDisplayOriginatingAddress();
                    if (content.contains(getString(R.string.putao_app_name))
                            || content.contains(getString(R.string.putao_phone_check_code)))
                    {
                        // 获取短信中的验证码
                        String code = getCode(content);
                        if (!TextUtils.isEmpty(code))
                        {
                            // 将验证码填写到验证码框
                            mPassWordEditText.setText(code);
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
    public void onClick(View view)
    {
        int id = view.getId();
        switch (id)
        {
            case R.id.putao_get_captchar_bt:
                GetCaptchar();
                break;
            case R.id.putao_confirm_bt:
                VerifyCaptchar();
                break;
            case R.id.putao_login_password_tv:
                Intent intent = new Intent(this, YellowpageLoginByPasswordActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }

    }

    /**
     * 
     * 登录验证 void
     */
    private void VerifyCaptchar()
    {
        // 先不做检验
        if (NetUtil.isNetworkAvailable(this))
        {
            String phoneNum = mUserNamEditText.getText().toString().trim();
            phoneNum = phoneNum.replace(" ", "");

            String checkCode = mPassWordEditText.getText().toString().trim();
            checkCode = checkCode.replace(" ", "");
            AccountManager.getInstance().loginByCaptcha(this, phoneNum, Integer.parseInt(checkCode), this);
        }
        else
        {
            Toast.makeText(this, R.string.putao_login_net_wrong, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 
     * 获取验证码 void
     */
    private void GetCaptchar()
    {
        // 判断网络是否ok
        if (NetUtil.isNetworkAvailable(this))
        {
            // 判断手机号码是否正确，位数是否够
            if (isMoblieRight())
            {
                String actionCode = "200001";
                String phoneNum = mUserNamEditText.getText().toString().trim();
                phoneNum = phoneNum.replace(" ", "");
                mTimeCount = new TimeCount(60000, 1000);

                AccountManager.getInstance().sendCaptchar(this, phoneNum, actionCode, new IAccCallbackAdv<String>()
                {

                    @Override
                    public void onSuccess(String t)
                    {
                        String number = t;
                    }

                    @Override
                    public void onFail(int failed_code)
                    {
                        // 发送验证码失败按钮需要做处理
                        mGetCaptcharButton.setText(getString(R.string.putao_get_check_code));
                        mGetCaptcharButton.getBackground().setAlpha(125);
                        mGetCaptcharButton.setEnabled(false);
                        mGetCaptcharButton.setClickable(false);
                    }

                    @Override
                    public void onCancel()
                    {

                    }
                });
                // handler发送消息处理按钮变化
                mTimeCount.start();
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
        String phonenumber = mUserNamEditText.getText().toString().trim();
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
        public void onTick(long millisUntilFinished)
        {
            mGetCaptcharButton.setText(millisUntilFinished / 1000 + "秒后重发");
            mGetCaptcharButton.getBackground().setAlpha(255);
            mGetCaptcharButton.setEnabled(false);
            mGetCaptcharButton.setClickable(false);
        }

        @Override
        public void onFinish()
        {
            mGetCaptcharButton.setText(getString(R.string.putao_get_check_code));
            mGetCaptcharButton.getBackground().setAlpha(125);
            mGetCaptcharButton.setEnabled(false);
            mGetCaptcharButton.setClickable(false);
        }
    }

    /**
     * 登录成功
     */
    @Override
    public void onSuccess()
    {
        // 登录成功后，直接finish返回到menufagment
        setResult(RESULT_OK);
        finish();
    }

    /**
     * 登录失败
     */
    @Override
    public void onFail(int failed_code)
    {

    }

    /**
     * 取消登录
     */
    @Override
    public void onCancel()
    {

    }
}
