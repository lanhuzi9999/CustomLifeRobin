package so.contacts.hub.basefunction.account.ui;

import com.putao.live.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import so.contacts.hub.BaseActivity;

public class ModifyPasswordActivity extends BaseActivity implements OnClickListener
{
    private EditText mUsernameET;

    private EditText mPasswordET;

    private Button mConfirmBT;

    private Button mGetCaptcharBT;

    private String phoneNum;
    
    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.putao_modify_password_activity);
        initView();
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
    }

    @Override
    public void onClick(View view)
    {
        
    }
    
}
