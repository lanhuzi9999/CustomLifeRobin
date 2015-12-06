package so.contacts.hub.basefunction.widget.dialog;

import com.putao.live.R;

import android.content.Context;

public class CommonDialogFactory
{
    /**
     * dialog主题样式
     * 
     * @return int
     */
    public static int getDialogTheme()
    {
        return R.style.putao_Dialog;
    }

    /**
     * 获取list列表对话框
     * 
     * @param context
     * @return CommonDialog
     */
    public static CommonDialog getListCommonDialog(Context context)
    {
        CommonDialog dialog = new CommonDialog(context, getDialogTheme(), R.layout.putao_common_list_dialog);
        return dialog;
    }
}
