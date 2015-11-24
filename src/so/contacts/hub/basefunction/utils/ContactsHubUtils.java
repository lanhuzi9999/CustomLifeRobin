package so.contacts.hub.basefunction.utils;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.widget.Toast;

import com.putao.live.R;

public class ContactsHubUtils {
    private static final String HTTP_HEAD = "http://";
    private static final String HTTPS_HEAD = "https://";
    
    /**插入图片中文字的大小 单位dip*/
    private final static int IMAGE_TEXT_SIZE = 28;
    /**插入图片中文字离顶部位置 单位dip*/
    private final static int IMAGE_TEXT_MARGIN_TOP = 6;
    
    /**
     * 判断是否为手机号码
     * add by hyl 2014-9-20
     * @param number
     * @return
     */
    public static boolean isTelephoneNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        /**
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 增加17* 号码
         */
        if (!TextUtils.isEmpty(number)) {
            number = number.replaceAll("-", "");
            number = number.replaceAll(" ", "");
        }
        // 处理+86
        int length = number.length();
        if (length == 13 || length == 14) {
            number = number.substring(length - 11, length);
        }
        String regExp = "^[1](3|4|5|8|7)[0-9]{9}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(number);
        return m.find();
    }
    
    /**
     * 发送验证码短信
     * add by hyl 2014-9-19
     * @param c
     * @param number
     * @param msgBody
     */
    public static void sendMessage(Context c, String number, String msgBody) {
        String number_not_correct = c.getResources().getString(R.string.putao_number_not_correct);
        if (TextUtils.isEmpty(number)) {
            Toast.makeText(c, number_not_correct, Toast.LENGTH_SHORT).show();
        } else {
            SmsManager smsManager = SmsManager.getDefault();
            // PendingIntent sentIntent = PendingIntent.getBroadcast(c, 0, new
            // Intent(), 0);
            smsManager.sendTextMessage(number, null, msgBody, null, null);
        }
    }
    
    /**
     * 普通拨号
     * 
     * @param c
     * @param number
     */
    public static void call(Context c, String number) {
    	if( c == null || TextUtils.isEmpty(number) ){
    		return;
    	}
        //add by lisheng start 修复bug #1909,1908
        if(number.startsWith("+")){
        	number = number.replace("+", "00");
        }
        if(number.contains(c.getResources().getString(R.string.putao_yellow_page_phonenum_filter))){
        	number = number.split(c.getResources().getString(R.string.putao_yellow_page_phonenum_filter))[0];
        }
        number = number.replace(" ", "");
        //add by lisheng end
        
        Intent intent = null;
        Uri uri = null;
        try {
        	/**
        	 * modify by zjh 2014-12-23 start
        	 * 采用Intent.ACTION_VIEW在酷派8720L上会出现crash(找不到com.yulong.android.contacts.ui.main.ContactMainActivity)
        	 */
//            uri = Uri.fromParts("tel", number, null);
//            intent = new Intent(Intent.ACTION_VIEW, uri);
        	
        	uri = Uri.parse("tel:" + number);
        	intent = new Intent(Intent.ACTION_DIAL, uri); 
        	/** modify by zjh 2014-12-23 end */
            c.startActivity(intent);
        } catch (Exception e) {

        }
    }

    /**
     * 获取返回本机号码，某些sim无法获取，则返回""
     * 
     * @param mContext
     * @return
     */
    public static String getPhoneNumber(Context mContext) {
        if(mContext == null){
            return "";
        }
        TelephonyManager telephonyManager = (TelephonyManager)mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        String num = telephonyManager.getLine1Number();
        if (TextUtils.isEmpty(num))
            return "";
        try {
            num = num.substring(num.length() - 11, num.length());
        } catch (Exception e) {
            num = "";
        }
        return num;
    }

    public static String getShowName(Context c, String name) {
        // name = zh_CN:快递;en_US:Courier;zh_TW:快遞;
        String showName = "";
        final String unsupportedLocale = "en_US";  // 不支持的语言
        try {
            Locale locale = c.getResources().getConfiguration().locale;
            String[] names = name.split(";");
            for (String temp : names) {
                String[] ts = temp.split(":");
                if (!unsupportedLocale.equals(ts[0]) && locale.toString().equals(ts[0])) {
                    showName = ts[1];
                    break;
                }
            }
            if(!name.contains(";")){
                showName = name;
            }
            
            //当showName 为空时，说明没有匹配到和当前手机系统对应的语言，默认显示中文  modified by hyl 修改注释描述
            if(TextUtils.isEmpty(showName)){
                String[] ts =  names[0].split(":");
                showName = ts[1];
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        
        return showName;
    }

//    public static Bitmap addbackground4onlyicon(Resources r, Bitmap icon) {
//        Bitmap b1 = BitmapFactory.decodeResource(r, R.drawable.tx);
//        
////        Picture.createFromStream(stream)
//        
//        Bitmap b2 = icon;
//        if (!b1.isMutable()) {
//            // 设置图片为背景为透明
//            b1 = b1.copy(Bitmap.Config.ARGB_8888, true);
//        }
//        Paint paint = new Paint();
//        Canvas canvas = new Canvas(b1);
//        int b1w = b1.getWidth();
//        int b1h = b1.getHeight();
////        int b2w = b2.getWidth();
////        int b2h = b2.getHeight();
////        int bx = (b1w - b2w) / 2;
////        int by = (b1h - b2h) / 2;
//        canvas.drawBitmap(b2, 0, 0, paint);
//        // 叠加新图b2 并且居中
//        canvas.save(Canvas.ALL_SAVE_FLAG);
//        canvas.restore();
//        
//        return b1;
//    }
    
    /** 
     * 给图片添加文字
     * @param size 传入DIP 
     * @return BitmapDrawable 
     */
    public static BitmapDrawable drawTextOnImage(Bitmap bmp,String text,int size,int top,Context context) {  
        
        //图象大小要根据文字大小算下,以和文本长度对应   
        Canvas canvasTemp = new Canvas(bmp);  
        canvasTemp.drawBitmap(bmp, 0, 0, null);
        
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(Utils.dip2px(context, size));
        textPaint.setColor(context.getResources().getColor(R.color.putao_white));
        StaticLayout sl= new StaticLayout(text, textPaint, canvasTemp.getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        canvasTemp.translate(0,Utils.dip2px(context,top));
        sl.draw(canvasTemp); 
        BitmapDrawable bd = new BitmapDrawable(context.getResources(), bmp);
        
        /**
         * 对bitmap进行回收
         * add by zjh 2015-05-09 start
         */
        if( bmp != null && !bmp.isRecycled() ){
            bmp.recycle();
            bmp = null;
        }
        canvasTemp = null;
        /** add by zjh 2015-05-09 end */
        
        return bd;  
    }  
    
    /**  
     * @return 返回指定笔和指定字符串的长度  
     */  
    public static float getFontlength(Paint paint, String str) {  
        return paint.measureText(str);  
    }  
    /**  
     * @return 返回指定笔的文字高度  
     */  
    public static float getFontHeight(Paint paint)  {    
        FontMetrics fm = paint.getFontMetrics();   
        return fm.descent - fm.ascent;    
    }   
    /**  
     * @return 返回指定笔离文字顶部的基准距离  
     */  
    public static float getFontLeading(Paint paint)  {    
        FontMetrics fm = paint.getFontMetrics();   
        return fm.leading- fm.ascent;    
    } 
    
    public static boolean isURlStr(String str){
        if(str !=null && str.length()>0){
            return str.startsWith(HTTP_HEAD)||str.startsWith(HTTPS_HEAD);
        }else{
            return false;
        }
    }
    
    
    
    /**
     * 生成圆角图片
     * 
     * @param bitmap
     * @param roundPx
     * @return
     */
    public static Bitmap corner(Bitmap bitmap, float roundPx,int imageSize) {
    	if(bitmap == null){
    		return bitmap;
    	}
        try {
            int width = bitmap.getWidth(), height = bitmap.getHeight();
            if( width == 0 || height == 0 ){
            	return null;
            }
            int newWidth = imageSize,newHeight = imageSize;
            int left = 0, top = 0;
            if(width > imageSize){
            	left = (width - imageSize)/2;
            	newWidth = imageSize;
            }else{
            	newWidth = width;
            }
            if(height > imageSize){
            	top = (height - imageSize)/2;
            	newHeight = imageSize;
            }else{
            	newHeight = height;
            }
            Bitmap newBitmap = null;
            Bitmap targetBitmap = null;
            if(imageSize > 0){
            	if(width/height > 2 || height/width > 2){
	            	newBitmap = Bitmap.createBitmap(bitmap, left, top, newWidth, newHeight);
	            	targetBitmap = Bitmap.createScaledBitmap(newBitmap, imageSize, imageSize, false);
            	}else{
            		targetBitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false);
            	}
            }else{
            	targetBitmap = bitmap;
            }
            width = targetBitmap.getWidth();
            height = targetBitmap.getHeight();
            
            Bitmap output = Bitmap.createBitmap(width, height,
                    android.graphics.Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            int color = 0xff424242;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, width, height);
            RectF rectF = new RectF(rect);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(targetBitmap, rect, rect, paint);
            
            /**
             * 对bitmap进行回收
             * add by zjh 2015-05-09 start
             */
            if( targetBitmap != null && !targetBitmap.isRecycled() ){
                targetBitmap.recycle();
                targetBitmap = null;
            }
            if( newBitmap != null && !newBitmap.isRecycled() ){
                newBitmap.recycle();
                newBitmap = null;
            }
            if( bitmap != null && !bitmap.isRecycled() ){
            	bitmap.recycle();
            	bitmap = null;
            }
            /** add by zjh 2015-05-09 end */
            
            canvas = null;
            paint = null;
            rect = null;
            rectF = null;
            return output;
        } catch (OutOfMemoryError error) {
            return bitmap;
        }
    }

    /**
     * 生成圆形图片
     * 
     * @param bitmap
     * @param roundPx
     * @return
     */
    /*public static Bitmap cornerWithCircle(Bitmap bitmap) {
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            int left = 0, top = 0, right = width, bottom = height;
            float roundPx = height / 2;
            if (width > height) {
                left = (width - height) / 2;
                top = 0;
                right = left + height;
                bottom = height;
            } else if (height > width) {
                left = 0;
                top = (height - width) / 2;
                right = width;
                bottom = top + width;
                roundPx = width / 2;
            }

            Bitmap output = Bitmap.createBitmap(width, height,
                    android.graphics.Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            int color = 0xff424242;
            Paint paint = new Paint();
            // Rect rect = new Rect(0, 0, width, height);
            Rect rect = new Rect(left, top, right, bottom);
            RectF rectF = new RectF(rect);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            paint.reset();
            paint.setColor(Color.WHITE);
            paint.setAlpha(128);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            // canvas.drawCircle(width / 2, height / 2, width / 2 - 2.5f,
            // paint);
            canvas.drawCircle(roundPx, roundPx, roundPx - 2.5f, paint);
            canvas = null;
            paint = null;
            rect = null;
            rectF = null;
            return output;
        } catch (OutOfMemoryError error) {
            return bitmap;
        }
    }
*/
    public static Bitmap makeRoundCorner(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int left = 0, top = 0, right = width, bottom = height;
        float roundPx = height / 2;
        if (width > height) {
            left = (width - height) / 2;
            top = 0;
            right = left + height;
            bottom = height;
        } else if (height > width) {
            left = 0;
            top = (height - width) / 2;
            right = width;
            bottom = top + width;
            roundPx = width / 2;
        }

        int newWidth = (int)roundPx * 2;

        Bitmap bitmap2 = Bitmap.createBitmap(bitmap, left, top, left + newWidth, newWidth);

        Bitmap output = Bitmap.createBitmap(newWidth, newWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, newWidth, newWidth);
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap2, rect, rect, paint);
        
        /**
         * 对bitmap进行回收
         * add by zjh 2015-05-09 start
         */
        if( bitmap2 != null && !bitmap2.isRecycled() ){
            bitmap2.recycle();
            bitmap2 = null;
        }
        canvas = null;
        paint = null;
        /** add by zjh 2015-05-09 end */
        
        return output;
    }
    
    /**
     * CoolPad detail title icon
     * sml
     * @param bitmap
     * @param bitmap
     * @return bitmap
     */        
    public static Bitmap makeRoundCornerforCoolPad(Bitmap bitmap, Bitmap bitmapmask) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if( width == 0 || height == 0 ){
        	return null;
        }
        int left = 0, top = 0;
        float roundPx = height / 2;
        if (width > height) {
            left = (width - height) / 2;
            top = 0;
        } else if (height > width) {
            left = 0;
            top = (height - width) / 2;
            roundPx = width / 2;
        }

        int newWidth = (int)roundPx * 2;

        Bitmap bitmap2 = Bitmap.createBitmap(bitmap, left, top, left + newWidth, newWidth);
        Bitmap bitmapmaskresize = scaleBitmap(bitmapmask, newWidth - 1, newWidth - 1);
        Bitmap output = Bitmap.createBitmap(newWidth, newWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, newWidth, newWidth);
        paint.setAntiAlias(true);
        canvas.drawBitmap(bitmapmaskresize, rect, rect, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap2, rect, rect, paint);
        
        /**
         * 对bitmap进行回收
         * add by zjh 2015-05-09 start
         */
        if( bitmap2 != null && !bitmap2.isRecycled() ){
            bitmap2.recycle();
            bitmap2 = null;
        }
        if( bitmapmaskresize != null && !bitmapmaskresize.isRecycled() ){
            bitmapmaskresize.recycle();
            bitmapmaskresize = null;
        }
        if( bitmapmask != null && !bitmapmask.isRecycled() ){
            bitmapmask.recycle();
            bitmapmask = null;
        }
        canvas = null;
        paint = null;
        /** add by zjh 2015-05-09 end */
        
        return output;
    }
    
    
    /**
     * scaleBitmap
     * sml
     * @param bitmapOrg
     * @param setht
     * @return bitmap
     */     
    public static Bitmap scaleBitmap(Bitmap bitmapOrg,int setwh,int setht){

        //get bitmapOrg width and height   
    	int width = bitmapOrg.getWidth();   
    	int height = bitmapOrg.getHeight();   
    	  
    	//define new width and height  
    	int newWidth = setwh;   
    	int newHeight = setht;   
    	  
    	//caculate scale   
    	float scaleWidth = ((float) newWidth) / width;   
    	float scaleHeight = ((float) newHeight) / height;   
    	  
    	//create matrix   
    	Matrix matrix = new Matrix();   
    	  
    	//scale picture   
    	matrix.postScale(scaleWidth, scaleHeight);   
    	  
    	//create new picture   
    	Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,   
    	                  width, height, matrix, true);  
    	
        /**
         * 对bitmap进行回收
         * add by zjh 2015-05-09 start
         */
        if( bitmapOrg != null && !bitmapOrg.isRecycled() ){
            bitmapOrg.recycle();
            bitmapOrg = null;
        }
        matrix = null;
        /** add by zjh 2015-05-09 end */

    	return resizedBitmap;
    }
    
    /**
     * 获取本地版本号
     * 
     * @param context
     * @return
     */
    private static String getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(getPackageName(context), 0).versionCode;
        } catch (NameNotFoundException e) {
            // Log.e(TAG, e.getMessage());
        }
        return Integer.toString(verCode);
    }
    
    /**
     * 获取本地版本号
     * add by hyl 2015-1-4
     * @param context
     * @return
     */
    private static String getVerName(Context context) {
        String versionName = "";
        try {
        	versionName = context.getPackageManager().getPackageInfo(getPackageName(context), 0).versionName;
        } catch (NameNotFoundException e) {
            // Log.e(TAG, e.getMessage());
        }
        return versionName;
    }
    
    /**
     * 获取版本号标识
     * add by hyl 2015-1-4 
     * @param context 
     */
    public static String getVersionName(Context context){
    	String versionName = "";
    	try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
				 PackageManager.GET_META_DATA);
			if(ai.metaData.containsKey("versionName")){
				versionName = ai.metaData.get("versionName").toString();
			}else{
				versionName = getVerName(context);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return versionName;
    }
    
    /**
     * 获取版本号标识
     * add by hyl 2015-1-4 
     */
    public static String getVersionCode(Context context){
    	String versionName = "";
    	try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
				 PackageManager.GET_META_DATA);
			if(ai.metaData.containsKey("versionName")){
				versionName = ai.metaData.get("versionName").toString();
			}else{
				versionName = getVerCode(context);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return versionName;
    }
    
    /**
     * 获取微信支付渠道标识
     * add by hyl 2015-1-4 
     * @param context
     */
    public static String getWxChannelNo(Context context){
    	String wxChannelNo = "";
    	try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
				 PackageManager.GET_META_DATA);
			if(ai.metaData.containsKey("WX_CHANNEL_NO")){
				wxChannelNo = ai.metaData.get("WX_CHANNEL_NO").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return wxChannelNo;
    }
    
    public static String getPackageName(Context context) {
        String packageName = context.getPackageName();
        return packageName;
    }
    
    public static String formatIPNumber(String number, Context context) {
        String temp = number;
        if (TextUtils.isEmpty(temp))
            return temp;
        if(temp.length()<7)return temp;
        StringBuffer sb = new StringBuffer(temp);
        temp = sb.toString();
        temp = temp.replace(" ", "");
        // 过滤ip号码
        if (temp.startsWith("17951")) {
            temp = temp.replace("17951", "");
        } else if (temp.startsWith("17911")) {
            temp = temp.replace("17911", "");
        } else if (temp.startsWith("12593")) {
            temp = temp.replace("12593", "");
        } else if (temp.startsWith("17909")) {
            temp = temp.replace("17909", "");
        } else {
            // 过滤自定义ip拨号前缀
            //Context context = Library.Instance().getApplication().getApplicationContext();
            temp = filterSelfDefIpNumber(context, temp);
        }
        // 国家/地区编号过滤
        if (temp.startsWith("0086")) {
            temp = temp.substring(4);
        } else if (temp.startsWith("00")) {
            temp = temp.substring(2);
        }
        if (temp.startsWith("+")) {
            temp = temp.substring(1);
        }
        if (temp.startsWith("86")) {
            temp = temp.substring(2);
        }
        // //过滤加号
        // if(sb.charAt(0) == '+'){
        // sb.deleteCharAt(0);
        // }
        // //过滤86
        // if(sb.charAt(0) == '8' && sb.charAt(1) == '6'){
        // sb.delete(0, 2);
        // }

        temp = PhoneNumberUtils.formatNumber(temp);
        if (temp != null && temp.contains("-")) {
            temp = temp.replaceAll("-", "");
        }
        return temp;
    }
    

    //用于记录 智能ip拨号 卡1 和 卡 2 自定义的ip号码
     public static final String SMART_IP_USER_DEFINED_SIM1 = "smart_ip_user_defined_sim1";
     public static final String SMART_IP_USER_DEFINED_SIM2 = "smart_ip_user_defined_sim2";
    
    public static String filterSelfDefIpNumber(Context context, String formatNumber) {
        // 过滤掉自定义ip号码
        SharedPreferences preferences = context.getSharedPreferences(
                ConstantsParameter.CONTACTS_SETTING, Context.MODE_MULTI_PROCESS);
        // String closeIp = context.getString(R.string.putao_close_smart_ip_call);
        // // IP拨号类型
        // String switch_sim1 =
        // preferences.getString(SharePreferenceHelper.SMART_IP_SWITCH_SIM1,
        // closeIp);
        // String switch_sim2 =
        // preferences.getString(SharePreferenceHelper.SMART_IP_SWITCH_SIM2,
        // closeIp);
        String ip_number_one = preferences.getString(SMART_IP_USER_DEFINED_SIM1, "");
        String ip_number_two = preferences.getString(SMART_IP_USER_DEFINED_SIM2, "");
        if (!TextUtils.isEmpty(ip_number_one) && formatNumber.startsWith(ip_number_one)) {
            // 卡1ip拨号没有关，才过滤掉这个ip号码
//            if (!closeIp.equals(switch_sim1) && !TextUtils.isEmpty(ip_number_one)) {
            if (!TextUtils.isEmpty(ip_number_one)) {
                formatNumber = formatNumber.substring(ip_number_one.length());
            }
        } else if (!TextUtils.isEmpty(ip_number_two) && formatNumber.startsWith(ip_number_two)) {
            // 卡2ip拨号没有关，才过滤到这个ip号码
//            if (!closeIp.equals(switch_sim2) && !TextUtils.isEmpty(ip_number_two)) {
        	if (!TextUtils.isEmpty(ip_number_two)) {
                formatNumber = formatNumber.substring(ip_number_two.length());
            }
        }
        return formatNumber;
    }
    
    /**
     *  通过sim card id 返回sim名字
     * @param context
     * @param simcard
     * @return
     */
	public static String getSimCardName(Context context,int simcard) {
		String name = null;
		String place_a_call1=context.getResources().getString(R.string.putao_place_a_call1);
		String place_a_call2=context.getResources().getString(R.string.putao_place_a_call2);
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		try {
			Method method = tm.getClass().getMethod("getSimStateGemini", int.class);
			if (null != method) {
				int state = (Integer) method.invoke(tm, simcard);
				if (state == TelephonyManager.SIM_STATE_READY) {
					name = (simcard==0 ? place_a_call1:place_a_call2);
//					String operatorName = tm.getNetworkOperatorName();
//					if (!TextUtils.isEmpty(operatorName)) {
//						name = operatorName
//					}
				}
			}
		} catch (Exception e) {
		}
		return name;
	}
	
//    public static StateListDrawable addStateDrawable(Context context, int idNormal, int idPressed,CategoryBean categoryBean) {
//        StateListDrawable sd = new StateListDrawable();
//        Drawable normal = null ;
//        Drawable pressed = null ;
//        
//        int type = categoryBean.getEditType();
//        String text = categoryBean.getName().substring(0,1);
//        
//        //add ljq start 2014-10-10 如果是用户添加类型 现在只有快捷查询
//        //则取出数据 插入文字
//        if(type == YellowUtil.YELLOW_CATEGORY_EDITTYPE_USER_ADD ){
//            
//            Bitmap normalBitmap = BitmapFactory.decodeResource(context.getResources(),
//                    idNormal).copy(Bitmap.Config.ARGB_8888, true);
//            Bitmap pressedBitmap = BitmapFactory.decodeResource(context.getResources(),
//                    idPressed).copy(Bitmap.Config.ARGB_8888, true);
//            
//            if(normalBitmap !=null && pressedBitmap != null){
//                normal = drawTextOnImage(normalBitmap, text, IMAGE_TEXT_SIZE,IMAGE_TEXT_MARGIN_TOP,context);
//                pressed = drawTextOnImage(pressedBitmap, text, IMAGE_TEXT_SIZE,IMAGE_TEXT_MARGIN_TOP,context);
//            }
//        }else{
//            normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal);
//            pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed);
//        }
//        //add ljq end
//        
//        // 注意该处的顺序，只要有一个状态与之相配，背景就会被换掉
//        // 所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了
//        sd.addState(new int[] {android.R.attr.state_pressed }, pressed);
//        sd.addState(new int[] {},normal);
//        return sd;
//    }
    
    public static StateListDrawable addStateDrawable(Context context, Bitmap normal, Bitmap pressed) {
        StateListDrawable sd = new StateListDrawable();
        BitmapDrawable normalDrawable = new BitmapDrawable(context.getResources(), normal);
        BitmapDrawable pressedDrawable = new BitmapDrawable(context.getResources(), pressed);
        // 注意该处的顺序，只要有一个状态与之相配，背景就会被换掉
        // 所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了
        sd.addState(new int[] {android.R.attr.state_pressed }, pressedDrawable);
        sd.addState(new int[] {},normalDrawable);
        return sd;
    }
	
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager)context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
    
    /**
     * 判断是否给酷派版本的当前主进程
     * @param context
     * @return
     */
    public static boolean isMainProcess(Context context){
    	String processName = getCurProcessName(context);
    	boolean isMainProcess = true;
    	
    	/*
    	 * 由于isCoolUIVersion 返回值始终是false 注释掉废弃代码
    	 * 厂商版本如要添加请修改
         * modify by ffh 2015-4-24 start
         */
//    	if(YellowPageApplicationProxy.isCoolUIVersion()) {
//    		if(processName != null && processName.endsWith(":yellow_page")) {
//    			isMainProcess = true;
//    		} else {
//    			isMainProcess = false;
//    		}
//    	} else {
    		if("com.yulong.android.contacts.discover".equals(processName)){
    			isMainProcess = true;
    		}else{
    			isMainProcess = false;
    		}
//    	}
    	/*
         * end 2015-4-24 by ffh
         */
    	return isMainProcess;
    }
    
    public static boolean isValidateString(String str){
        return (str !=null && !str.trim().equals(""));
    }
    
}
