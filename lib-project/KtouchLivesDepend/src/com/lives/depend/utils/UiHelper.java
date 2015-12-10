package com.lives.depend.utils;

import com.lives.depend.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class UiHelper
{

    // private final static String TAG = "UiHelper";

    public static int widthPixels = 480;

    public static int heightPixels = 800;

    public static final String SECTION_CONTACTS[] =
    /* { "~", "☆", */{ "☆", "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

    public static final String SECTION_ADD_CONTACTS[] =
    { "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z" };

    public static int getImageThreshold(Context context)
    {
        int threshold = context.getResources().getDimensionPixelSize(R.dimen.putao_image_threshold);
        int width = UiHelper.getDisplayMetrics(context).widthPixels;
        return width / 4 > threshold ? width / 4 : threshold;
    }

    /**
     * 获取屏幕显示的宽度和高度
     * 
     * @param context 上下文
     * @return 屏幕显示的宽度和高度
     */
    public static DisplayMetrics getDisplayMetrics(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    /**
     * 获取对话框的边距
     * 
     * @return 边距
     */
    public static int getDialogPadding(Context context)
    {
        // return getDisplayMetrics(context).widthPixels > 720 ? 40 : 15;
        return context.getResources().getDimensionPixelSize(
                getDisplayMetrics(context).widthPixels > 320 ? R.dimen.putao_dialog_padding_bigscreen
                        : R.dimen.putao_dialog_padding_smallscreen);
    }

    // /**
    // * 主动检测
    // * @param context
    // */
    // public static void checkOrStartCheckUpdate(final Context context) {
    // LogUtil.d(TAG, "checkOrStartCheckUpdate()");
    // if (NetUtil.isNetworkAvailable(context)) {
    // new Thread(new Runnable() {
    // @Override
    // public void run() {
    // UiHelper.checkUpdate(context, false);
    // }
    // }).start();
    // }
    // }
    //
    // /**
    // * 用户主动检测升级
    // */
    // public static void checkOrStartCheckUpdateByUser(final Context
    // context,final ProcessCallback callback) {
    // callback.onMethodBegin();
    // LogUtil.d(TAG, "checkOrStartCheckUpdateByUser()");
    // if(!NetUtil.isNetworkAvailable(context)){
    // callback.onMethodEnd();
    // showToast(context,context.getString(R.string.putao_search_time_out));
    // return;
    // }
    // new Thread(new Runnable() {
    // @Override
    // public void run() {
    // UiHelper.checkUpdate(context, true);
    // if (context!=null&&context instanceof Activity) {
    // ( (Activity)context).runOnUiThread(new Runnable() {
    //
    // @Override
    // public void run() {
    // callback.onMethodEnd();
    // }
    // });
    // }
    //
    // }
    // }).start();
    // }
    //
    // /**
    // * 检测更新
    // * @param context
    // * @param isShowDialog
    // */
    // public static void checkUpdate(final Context context, final boolean
    // isShowDialog) {
    // // 检查更新
    // final NewVersionRequestData requestData = new NewVersionRequestData();
    // try {
    // IgnitedHttpResponse httpResponse = Config.getApiHttp().post(
    // Config.SERVER, requestData.getData()).send();
    // String content = httpResponse.getResponseBodyAsString();
    // LogUtil.d(TAG, content);
    // if (!TextUtils.isEmpty(content)) {
    // NewVersionResponseData responseData = requestData.getObject(content);
    //
    // // 保存最新检查更新时间
    // SharedPreferences setting =
    // context.getSharedPreferences(ConstantsParameter.CONTACTS_SETTING,
    // Context.MODE_PRIVATE);
    // long lastCheckUpdate = setting.getLong("lastCheckUpdate", 0);
    // long current = System.currentTimeMillis();
    // /*
    // * 用户手动检测和强制升级不用间隔时间
    // * modify by ffh 2015-4-9 start
    // */
    // if (current - lastCheckUpdate >= 1 * 24 * 60 * 60 * 1000 ||
    // responseData.enforce == 1 || isShowDialog) {// 1天时间间隔
    // /*
    // * end 2015-4-9 by ffh
    // */
    // setting.edit().putLong("lastCheckUpdate",
    // System.currentTimeMillis()).commit();
    // String localAppVersion = SystemUtil.getAppVersion(context);
    // String version = responseData.version;
    // LogUtil.d(TAG, "localAppVersion:" + localAppVersion + ";version:" +
    // version);
    // if (!TextUtils.isEmpty(version)) {
    // if (!localAppVersion.equals(version)) {
    // /*
    // * 用户手动检测和强制升级在任何网络下都可升级
    // * modify by ffh 2015-4-9 start
    // */
    // if(responseData.enforce == 1 || isShowDialog ||
    // SystemUtil.isWIFI(context)){
    // showUpdateDialog(context, responseData.remark, responseData.down_url,
    // responseData.enforce);
    // }
    // /*
    // * end 2015-4-9 by ffh
    // */
    // } else if (isShowDialog) {
    // /*
    // * 修改检测升级提示语
    // * modify by ffh 2015-4-9 start
    // */
    // showToast(context,
    // context.getString(R.string.putao_vupdate_check_no_version));
    // /*
    // * end 2015-4-9 by ffh
    // */
    // }
    // } else if (isShowDialog) {
    // /*
    // * 修改检测升级提示语
    // * modify by ffh 2015-4-9 start
    // */
    // showToast(context,
    // context.getString(R.string.putao_vupdate_check_no_version));
    // /*
    // * end 2015-4-9 by ffh
    // */
    // }
    // }
    // }else if(isShowDialog){
    // /*
    // * 修改检测升级失败提示语
    // * modify by ffh 2015-4-9 start
    // */
    // showToast(context,context.getString(R.string.putao_vupdate_check_error));
    // /*
    // * end 2015-4-9 by ffh
    // */
    // }
    // } catch (IOException e) {
    // /*
    // * 修改检测升级失败提示语
    // * modify by ffh 2015-4-9 start
    // */
    // if(isShowDialog){
    // showToast(context,context.getString(R.string.putao_vupdate_check_error));
    // }
    // /*
    // * end 2015-4-9 by ffh
    // */
    // e.printStackTrace();
    // }
    // }
    //
    // /**
    // * 升级提示
    // * @author ffh
    // * @param context
    // * @param message
    // * @since 2015-04-09
    // */
    // private static void showToast(final Context context,final String
    // message){
    // Handler handler = new Handler(context.getMainLooper()){
    //
    // @Override
    // public void handleMessage(Message msg) {
    // Toast.makeText(context, message, 0).show();
    // super.handleMessage(msg);
    // }
    //
    // };
    // handler.sendEmptyMessage(0);
    // }
    //
    // /**
    // * 升级提示框
    // * @param context
    // * @param txt
    // * @param url
    // * @param enforce
    // */
    // private static void showUpdateDialog(final Context context,final String
    // txt,final String url,final int enforce){
    // Handler handler= new Handler(context.getMainLooper()){
    // @Override
    // public void handleMessage(android.os.Message msg) {
    // super.handleMessage(msg);
    // LogUtil.d(TAG, "show msg:" + msg.what);
    // final CommonDialog dialog =
    // CommonDialogFactory.getOkCancelCommonDialog(context);
    // dialog.getTitleTextView().setText(R.string.putao_vupdate_check_title);
    //
    // dialog.getMessageTextView().setText(txt);
    // dialog.getCancelButton().setText(R.string.putao_vupdate_check_cancel_update);
    // /*
    // * 增加强制升级逻辑
    // * modify by ffh 2015-4-8 start
    // */
    // if(enforce == 1){
    // dialog.getCancelButton().setVisibility(View.GONE);
    // dialog.setCancelable(false);
    // dialog.setCanceledOnTouchOutside(false);
    // }
    // /*
    // * end 2015-4-8 by ffh
    // */
    // dialog.getOkButton().setText(R.string.putao_vupdate_check_confirm_update);
    // dialog.setOkButtonClickListener(new OnClickListener() {
    // @Override
    // public void onClick(View v) {
    // LogUtil.d("PlugService", "plugResume checkUpdate downInstallApp");
    // AppDownInstaller.getInstance().downInstallApp(context,
    // context.getString(R.string.putao_app_name), null, url,null);
    // dialog.dismiss();
    // /*
    // * 增加强制升级逻辑
    // * modify by ffh 2015-4-8 start
    // */
    // if (enforce == 1) {
    // FullScreenProgressDialog progressDialog = new
    // FullScreenProgressDialog(context);
    // progressDialog.setMessage(context
    // .getString(R.string.putao_yellow_page_downloading));
    // progressDialog.setCanceledOnTouchOutside(false);
    // progressDialog.setCancelable(false);
    // if (!progressDialog.isShowing()) {
    // progressDialog.show();
    // }
    // }
    // /*
    // * end 2015-4-9 by ffh
    // */
    // }
    // });
    // dialog.show();
    // };
    // };
    //
    // handler.sendEmptyMessage(0);
    // }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setViewBackground(View view, Bitmap bitmap)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            view.setBackground(new BitmapDrawable(view.getContext().getResources(), bitmap));
        }
        else
        {
            view.setBackgroundDrawable(new BitmapDrawable(view.getContext().getResources(), bitmap));
        }
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setViewBackground(View view, Drawable drawable)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            view.setBackground(drawable);
        }
        else
        {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static Bitmap corner(Bitmap bitmap, float roundPx, int imageWidth, int imageHeight)
    {
        if (bitmap == null)
        {
            return bitmap;
        }
        int width = bitmap.getWidth(), height = bitmap.getHeight();
        if (width == 0 || height == 0)
        {
            return null;
        }
        int newWidth = imageWidth, newHeight = imageHeight;
        int left = 0, top = 0;
        Bitmap newBitmap = null;
        Bitmap targetBitmap = null;
        if (imageHeight > 0 && imageWidth > 0)
        {
            if (width >= imageWidth && height >= imageHeight)
            {
                left = (width - imageWidth) / 2;
                top = (height - imageHeight) / 2;
            }
            else if (width <= imageWidth && height >= imageHeight)
            {
                newWidth = width;
                newHeight = imageHeight * width / imageWidth;
                top = (height - newHeight) / 2;
            }
            else if (width >= imageWidth && height <= imageHeight)
            {
                newHeight = height;
                newWidth = imageWidth * height / imageHeight;
                left = (width - newWidth) / 2;
            }
            else
            {
                if (width / height > imageWidth / imageHeight)
                {
                    newHeight = height;
                    newWidth = imageWidth * height / imageHeight;
                    left = (width - newWidth) / 2;
                    if (left < 0)
                    {
                        left = 0;
                    }
                }
                else
                {
                    newWidth = width;
                    newHeight = imageHeight * width / imageWidth;
                    top = (height - newHeight) / 2;
                    if (top < 0)
                    {
                        top = 0;
                    }
                }
            }

            newBitmap = Bitmap.createBitmap(bitmap, left, top, newWidth, newHeight);
            targetBitmap = Bitmap.createScaledBitmap(newBitmap, imageWidth, imageHeight, false);
        }
        else
        {
            targetBitmap = bitmap;
        }

        if (targetBitmap != null)
        {
            width = targetBitmap.getWidth();
            height = targetBitmap.getHeight();
        }
        Bitmap output = null;
        try
        {
            output = Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888);
        }
        catch (OutOfMemoryError e)
        {
            recycleBitmap(targetBitmap);
            recycleBitmap(newBitmap);
            System.gc();
            return bitmap;
        }
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

        recycleBitmap(targetBitmap);
        recycleBitmap(newBitmap);

        return output;
    }

    /**
     * 生成圆角图片
     * 
     * @param bitmap
     * @param roundPx
     * @return
     */
    public static Bitmap corner(Bitmap bitmap, float roundPx, int imageSize)
    {
        if (bitmap == null)
        {
            return bitmap;
        }
        int width = bitmap.getWidth(), height = bitmap.getHeight();
        if (width == 0 || height == 0)
        {
            return null;
        }
        int newWidth = imageSize, newHeight = imageSize;
        int left = 0, top = 0;
        if (width > imageSize)
        {
            left = (width - imageSize) / 2;
            newWidth = imageSize;
        }
        else
        {
            newWidth = width;
        }
        if (height > imageSize)
        {
            top = (height - imageSize) / 2;
            newHeight = imageSize;
        }
        else
        {
            newHeight = height;
        }
        Bitmap newBitmap = null;
        Bitmap targetBitmap = null;
        if (imageSize > 0)
        {
            if (width / height > 2 || height / width > 2)
            {
                newBitmap = Bitmap.createBitmap(bitmap, left, top, newWidth, newHeight);
                targetBitmap = Bitmap.createScaledBitmap(newBitmap, imageSize, imageSize, false);
            }
            else
            {
                targetBitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false);
            }
        }
        else
        {
            targetBitmap = bitmap;
        }
        if (targetBitmap != null)
        {
            width = targetBitmap.getWidth();
            height = targetBitmap.getHeight();
        }
        Bitmap output = null;
        try
        {
            output = Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888);
        }
        catch (OutOfMemoryError e)
        {
            recycleBitmap(targetBitmap);
            recycleBitmap(newBitmap);
            System.gc();
            return bitmap;
        }
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

        recycleBitmap(targetBitmap);
        recycleBitmap(newBitmap);
        // recycleBitmap(bitmap);

        canvas = null;
        paint = null;
        rect = null;
        rectF = null;
        return output;
    }

    /**
     * 获取商户详情页的椭圆图片
     * 
     * @param bitmap
     * @param bitmap
     * @return bitmap
     */
    public static Bitmap makeRoundCorner(Bitmap bitmap, Bitmap bitmapmask)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width == 0 || height == 0)
        {
            return null;
        }
        int left = 0, top = 0;
        float roundPx = height / 2;
        if (width > height)
        {
            left = (width - height) / 2;
            top = 0;
        }
        else if (height > width)
        {
            left = 0;
            top = (height - width) / 2;
            roundPx = width / 2;
        }

        int newWidth = (int) roundPx * 2;

        Bitmap bitmap2 = Bitmap.createBitmap(bitmap, left, top, left + newWidth, newWidth);
        Bitmap bitmapmaskresize = UiHelper.scaleBitmap(bitmapmask, newWidth - 1, newWidth - 1);
        if (bitmapmaskresize == null)
        {
            return null;
        }
        Bitmap output = Bitmap.createBitmap(newWidth, newWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, newWidth, newWidth);
        paint.setAntiAlias(true);
        canvas.drawBitmap(bitmapmaskresize, rect, rect, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap2, rect, rect, paint);

        recycleBitmap(bitmap2);
        recycleBitmap(bitmapmaskresize);
        recycleBitmap(bitmapmask);
        canvas = null;
        paint = null;
        /** add by zjh 2015-05-09 end */

        return output;
    }

    private static void recycleBitmap(Bitmap bitmap)
    {
        if (bitmap != null && !bitmap.isRecycled())
        {
            bitmap.recycle();
            bitmap = null;
        }
    }

    /**
     * scaleBitmap sml
     * 
     * @param bitmapOrg
     * @param setht
     * @return bitmap
     */
    public static Bitmap scaleBitmap(Bitmap bitmapOrg, int setwh, int setht)
    {
        if (bitmapOrg == null)
        {
            return null;
        }

        // get bitmapOrg width and height
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();

        // define new width and height
        int newWidth = setwh;
        int newHeight = setht;

        // caculate scale
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create matrix
        Matrix matrix = new Matrix();

        // scale picture
        matrix.postScale(scaleWidth, scaleHeight);

        // create new picture
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true);

        /**
         * 对bitmap进行回收 add by zjh 2015-05-09 start
         */
        if (bitmapOrg != null && !bitmapOrg.isRecycled())
        {
            bitmapOrg.recycle();
            bitmapOrg = null;
        }
        matrix = null;
        /** add by zjh 2015-05-09 end */

        return resizedBitmap;
    }

}
