package so.contacts.hub.basefunction.utils;

import java.util.List;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

/**
 * so.contacts.hub.uitl
 * 
 * @author kzl
 * @created at 13-6-7 下午3:37
 */
public class Utils {
    private static final String TAG = "Utils";
    
    public static Handler mhandler = new Handler(Looper.getMainLooper());
    public static void _BitmapRecyle(final Bitmap bm){
        new Exception().printStackTrace();
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(bm != null){
                   bm.recycle();
                }
            }
        }, 100);
    }
    
	/**
	 * dip转为 px
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 *  px 转为 dip
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**
     * 将sp值转换为px值，保证文字大小不变
     * 
     * @param spValue
     * @param fontScale
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */ 
    public static int sp2px(Context context, float spValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale + 0.5f); 
    } 
    
	/**
	 * add by zj 
	 * 方法表述  判断两个String是否相同的方法
	 * @param str1
	 * @param str2
	 * @return
	 * boolean
	 */
	public static boolean isTextSame(String str1,String str2)
    {
        if (TextUtils.isEmpty(str1))
        {
            if (!TextUtils.isEmpty(str2))
            {
                return false;
            }
        }else {
            if (!str1.equals(str2))
            {
                return false;
            }
        }
        return true;
    }
    
	
	/**
     * 实现号码3-4-4格式
     * @param contents
     * @param edit
     */
	public static String formatPhoneNum(String contents){
	    if(TextUtils.isEmpty(contents)){
	        return "";
	    }
	    String newStr=contents.replace(" ", "");
	    int length = newStr.length();
	    
	    if(length<3){
	        //不用处理
	    }else if(length==3){
	        newStr=newStr+" ";
	    }else if(length<7){
	        newStr=newStr.substring(0, 3)+" "+newStr.substring(3);
	    }else if(length==7){
	        newStr=newStr.substring(0, 3)+" "+newStr.substring(3)+" ";
	    }else{
	        newStr=newStr.substring(0, 3)+" "+newStr.substring(3,7)+" "+newStr.substring(7);
	    }
	    
	    return newStr;
	}
	
	/**
	 * 号码输入编辑框，实现号码3-4-4格式
	 * @param contents
	 * @param edit
	 */
	public static void setEditPhoneNumFormat(String contents,EditText edit){
	    if(TextUtils.isEmpty(contents)){
	        return;
	    }
	    int length = contents.length();
        if(length == 4){
            if(contents.substring(3).equals(" ")){ 
//                edit.setText(contents);
//                edit.setSelection(contents.length());
            }else{
                contents = contents.substring(0, 3) + " " + contents.substring(3);
                edit.setText(contents);
                edit.setSelection(contents.length());
            }
        }
        else if(length == 9){
            if(contents.substring(8).equals(" ")){
//                edit.setText(contents);
//                edit.setSelection(contents.length());
            }else{
                contents = contents.substring(0, 8) + " " + contents.substring(8);
                edit.setText(contents);
                edit.setSelection(contents.length());
            }
        }
	}
	
	public static boolean isEmptyList(List list){
		return (list == null || list.size() <1);
	}
	
	/**
	 * 隐藏内容(急)
	 * @author lxh
	 * @since 2015-6-14
	 * @param content
	 * @param hideStart
	 * @param hideEnd
	 * @return
	 */
	public static String hideContent(String content, int hideStart, int hideEnd) {
        if(TextUtils.isEmpty(content) || content.length() <= hideStart) {
            return content;
        }

        char[] contentArray = content.toCharArray();
        for(int i = hideStart; i <= hideEnd; i++) {
            if(i == contentArray.length) {
                break;
            }
            contentArray[i] = '*';
        }

        return new String(contentArray);
    }
	
	/**
	 * 去掉价格小数点后面多余的0
	 * 方法表述
	 * @param price
	 * @return
	 * String
	 */
	public static String deleteZeroForPrice(String price){
	    if (price.contains("."))
        {
	        while (price.endsWith("0"))
	        {
	            price=price.substring(0, price.length()-1);
	        }
	        if (price.endsWith("."))
            {
	            price=price.substring(0, price.length()-1);
            }
        }
	    return price;
	}
	
}
