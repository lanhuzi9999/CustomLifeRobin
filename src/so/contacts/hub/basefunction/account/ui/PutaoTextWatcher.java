package so.contacts.hub.basefunction.account.ui;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/**
 * ****************************************************************
 * 文件名称 : PutaoTextWatcher.java
 * 作 者 :   Robin
 * 创建时间 : 2015-12-17 下午5:57:18
 * 文件描述 : EditetxT自定义清除按钮
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-12-17 1.00 初始版本
 *****************************************************************
 */
public class PutaoTextWatcher implements TextWatcher
{

    private static final String TAG = "PutaoTextWatcher";

    private EditText mEditText;

    public PutaoTextWatcher(EditText editText)
    {
        mEditText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    @Override
    public void afterTextChanged(Editable s)
    {
        // s为编辑框写入的内容
        if (TextUtils.isEmpty(s))
        {
            if (mEditText != null && mEditText.getTag() != null)
            {
                ((View) mEditText.getTag()).setVisibility(View.GONE);
            }
        }
        else
        {
            if (mEditText != null && mEditText.getTag() != null)
            {
                ((View) mEditText.getTag()).setVisibility(View.VISIBLE);
            }
        }
    }
}
