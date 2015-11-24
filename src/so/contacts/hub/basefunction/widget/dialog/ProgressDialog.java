package so.contacts.hub.basefunction.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.putao.live.R;

public class ProgressDialog extends Dialog {
	private View view;
	private CharSequence mMessage;
	private Context mContext;
	private boolean mIsHaveContent = true;

	public ProgressDialog(Context context) {
		this(context, R.style.putao_ProgressDialog); // ProgressDialog Dialog
		this.mContext = context;
	}
	
	public ProgressDialog(Context context,boolean isHaveContent) {
		this(context, R.style.putao_ProgressDialog); // ProgressDialog Dialog
		this.mIsHaveContent = isHaveContent;
		this.mContext = context;
	}

	public ProgressDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
	}

	ProgressDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.mContext = context;
	}

	public void setContentFlag(boolean isHaveContent){
		this.mIsHaveContent = isHaveContent;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Window dialogWindow = getWindow();
		// Display d = dialogWindow.getWindowManager().getDefaultDisplay(); //
		// 获取屏幕宽、高用
		WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
		// p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
		// p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
		dialogWindow.setAttributes(p);
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.putao_progress_dialog, null);

		TextView textView = (TextView) view.findViewById(R.id.msg);
		View dialogBackground = view.findViewById(R.id.putao_progress_dialog_bg);
		if (!TextUtils.isEmpty(mMessage)) {
			textView.setText(mMessage);
		}
		
		if(mIsHaveContent){
			dialogBackground.setBackgroundResource(R.drawable.putao_bg_card_yindao);
			textView.setTextColor(mContext.getResources().getColor(R.color.putao_progress_dialog_text_white));
		}else{
			dialogBackground.setBackgroundResource(R.color.putao_transparent);
			textView.setTextColor(mContext.getResources().getColor(R.color.putao_progress_dialog_text_grey));
		}
		setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		
		setCanceledOnTouchOutside(true);
	}

	public static ProgressDialog show(Context context, CharSequence msg) {
		return show(context, msg, false);
	}

	public static ProgressDialog show(Context context, int id) {
		return show(context, context.getResources().getString(id), false);
	}

	public static ProgressDialog show(Context context, CharSequence msg,
			boolean cancelable) {
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setMessage(msg);
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(cancelable);
		dialog.show();
		return dialog;
	}

	public void setMessage(CharSequence msg) {
		mMessage = msg;
		if (view != null && !TextUtils.isEmpty(mMessage)) {
			TextView textView = (TextView) view.findViewById(R.id.msg);
			textView.setText(mMessage);
		}
	}
	
	/*
	 * 判断activity是否finish,如果finished,就不show
     * add by zj 2015-2-10 start
     */
	@Override
    public void show() {
        if (mContext instanceof Activity) {
            if(((Activity)mContext).isFinishing()){
                return;
            }
        }
        super.show();
    }
	// end 2015-2-10 by zj
}
