/**
 * 
 */

package so.contacts.hub.basefunction.imageloader.image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import so.contacts.hub.basefunction.imageloader.DataCache;
import so.contacts.hub.basefunction.imageloader.DataLoader;
import so.contacts.hub.basefunction.imageloader.utils.CacheUtils;
import so.contacts.hub.basefunction.imageloader.utils.DiskLruCache;
import so.contacts.hub.basefunction.imageloader.utils.Md5Util;
import so.contacts.hub.basefunction.utils.ContactsHubUtils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**************************************************
 * <br>文件名称: ImageLoader.java
 * <br>版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * <br>创建人员: wcy
 * <br>文件描述: 图片加载实现类 
 * <br>修改时间: 2015-7-6 上午11:43:04
 * <br>修改历史: 2015-7-6 1.00 初始版本
 **************************************************/

public class ImageLoader extends DataLoader
{

    public static final int FADE_IN_TIME = 200;

    /**
     * 缓存文件夹名称
     */
    public static final String HTTP_CACHE_DIR = "http";

    protected Resources mResources;

    public ImageLoader(Context context)
    {
        super(context);
        mResources = context.getResources();
    }

    @Override
    protected void fillDataInView(Object result, View view)
    {
        if (result == null || view == null)
        {
            return;
        }
        if (mLoaderParams.mFadeInBitmap)
        {
            final TransitionDrawable td = new TransitionDrawable(new Drawable[]
            { new ColorDrawable(android.R.color.transparent),
                    new BitmapDrawable(mResources, mLoaderParams.mLoadingBitmap) });
            fillDataWithView(td, view);
            td.startTransition(FADE_IN_TIME);
        }
        else
        {
            fillDataWithView(result, view);
        }
    }

    @Override
    protected LoaderTask createLoaderTask(View view)
    {
        LoaderTask task = new LoaderTask(view);
        final AsyncDrawable asyncDrawable = new AsyncDrawable(mResources, mLoaderParams.mLoadingBitmap, task);
        fillDataWithView(asyncDrawable, view);
        return task;
    }

    /**
     * @param view Any View
     * @return Retrieve the currently active work task (if any) associated with
     * this imageView. null if there is no such task.
     */
    @Override
    protected LoaderTask getLoaderTask(View view)
    {
        if (view == null)
        {
            return null;
        }
        if (view instanceof ImageView)
        {
            ImageView imageView = (ImageView) view;
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable)
            {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getLoaderTask();
            }
        }
        else if (view instanceof TextView)
        {
            TextView tView = (TextView) view;
            final Drawable drawable = tView.getBackground();
            if (drawable instanceof AsyncDrawable)
            {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getLoaderTask();
            }
        }
        return null;
    }

    /**
     * 支持对多种view显示图片的适配
     * 
     * @param result
     * @param view
     */
    @Override
    protected void fillDataWithView(Object result, View view)
    {
        if (result == null || view == null)
        {
            return;
        }
        if (view instanceof ImageView)
        {
            ImageView imgView = (ImageView) view;
            if (result instanceof Bitmap)
            {
                imgView.setImageBitmap((Bitmap) result);
            }
            else if (result instanceof Drawable)
            {
                imgView.setImageDrawable((Drawable) result);
            }
        }
        else if (view instanceof TextView)
        {
            TextView tView = (TextView) view;
            if (result instanceof Bitmap)
            {
                Drawable drawable = new BitmapDrawable(context.getResources(), (Bitmap) result);
                tView.setBackgroundDrawable(drawable);
            }
            else if (result instanceof Drawable)
            {
                tView.setBackgroundDrawable((Drawable) result);
            }
        }
        else
        {
            // Other view.
        }
    }

    /**
     * A custom Drawable that will be attached to the imageView while the work
     * is in progress. Contains a reference to the actual worker task, so that
     * it can be stopped if a new binding is required, and makes sure that only
     * the last started worker process can bind its result, independently of the
     * finish order.
     */
    private static class AsyncDrawable extends BitmapDrawable
    {
        private final WeakReference<LoaderTask> taskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, LoaderTask loaderTask)
        {
            super(res, bitmap);
            taskReference = new WeakReference<LoaderTask>(loaderTask);
        }

        public LoaderTask getLoaderTask()
        {
            return taskReference.get();
        }
    }

    @Override
    public Bitmap processData(Object data)
    {
        // 下载并缩放图片
        Bitmap bitmap = null;
        if (data instanceof Integer)
        {// 加载本地资源图片
            int resId = Integer.parseInt(String.valueOf(data));
            bitmap = processResId(resId);
        }
        else
        {
            String dataString = String.valueOf(data);
            if (!TextUtils.isEmpty(dataString))
            {
                bitmap = processUrl(dataString);
            }
        }
        if (mLoaderParams.mRoundPx <= 0)
        {
            return bitmap;
        }
        return ContactsHubUtils.corner(bitmap, mLoaderParams.mRoundPx, mLoaderParams.mImageWidth);
    }

    /**
     * The main process method, which will be called by the ImageWorker in the
     * AsyncTask background thread.
     * 
     * @param data The data to load the bitmap, in this case, a regular http URL
     * @return The downloaded and resized bitmap
     */
    public Bitmap processUrl(String data)
    {
        return downloadBitmap(context, data);
    }

    /**
     * 通过HttpUrlConnection方式去下载数据并写入缓存文件中
     * 
     * @author wcy
     * @since 2015-5-28
     * @param urlString
     * @param diskLruCache
     * @return Bitmap
     */
    @SuppressWarnings("unused")
    protected Bitmap downloadDataByUrlConn(String urlString, DiskLruCache diskLruCache)
    {
        CacheUtils.disableConnectionReuseIfNecessary();
        HttpURLConnection urlConnection = null;
        int len = 0;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        Bitmap bitmap = null;
        try
        {
            URL url = new URL(urlString); // 设置URL
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection(); // 打开连接
            connection.setRequestMethod("GET"); // 设置请求方法
            connection.setConnectTimeout(5000);

            // 添加请求头信息 这里照搬通过IgnitedHttpRequest方式请求数据时中的头部信息 兼容之前的版本
            connection.addRequestProperty("Accept-Encoding", "gzip");
            connection.addRequestProperty("Content-Type", "application/json");

            is = connection.getInputStream(); // 取得字节输入流
            byte[] bytes = new byte[1024 * 4];
            baos = new ByteArrayOutputStream();
            while ((len = is.read(bytes)) != -1)
            {
                baos.write(bytes, 0, len);
            }
            if (is != null)
            {
                bitmap = decodeSampledBitmapFromByteArrayNew(baos.toByteArray(), mLoaderParams.mImageWidth,
                        mLoaderParams.mImageHeight);
                // 存入硬盘缓存
                putToDiskLrucache(urlString, diskLruCache, bitmap);
                return bitmap;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return bitmap;
        }
        finally
        {
            // 关闭连接
            closeNetConnect(urlConnection, is, baos);
        }
        return null;
    }
    
    /**
     * 存放到硬盘缓存
     * @param urlString
     * @param diskLruCache
     * @param bitmap
     * void
     */
    private void putToDiskLrucache(String urlString, DiskLruCache diskLruCache, Bitmap bitmap)
    {
        // 加上同步锁
        synchronized (diskLruCache)
        {
            // 区分png格式和jpg格式的压缩方式
            if (urlString.endsWith(".png") || urlString.endsWith(".PNG"))
            {
                diskLruCache.setCompressParams(CompressFormat.PNG, 100);
            }
            else
            {
                diskLruCache.setCompressParams(CompressFormat.JPEG, 100);
            }
            // 存入硬盘缓存对象中 也就是写入缓存文件中
            diskLruCache.put(Md5Util.md5(urlString), bitmap);
        }
    }
    
    /**
     * 关闭网络连接
     * @param urlConnection
     * @param is
     * @param baos
     * void
     */
    private void closeNetConnect(HttpURLConnection urlConnection, InputStream is, ByteArrayOutputStream baos)
    {
        if (urlConnection != null)
        {
            urlConnection.disconnect();
            urlConnection = null;
        }
        if (is != null)
        {
            try
            {
                is.close();
                is = null;
            }
            catch (final IOException e)
            {
                e.printStackTrace();
            }
        }
        if (baos != null)
        {
            try
            {
                baos.close();
                baos = null;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Download a bitmap from a URL, write it to a disk and return the File
     * pointer. This implementation uses a simple disk cache.
     * 
     * @param context The context to use
     * @param urlString The URL to fetch
     * @return A File pointing to the fetched bitmap
     */
    public Bitmap downloadBitmap(Context context, String urlString)
    {
        // 获取该loader的缓存对象
        DataCache dataCache = getDataCache();
        //add by xcx 2015-08-25 start cache为null，loader已经调用clearCache(),不需要继续下载
        if(dataCache==null)
        {
            return null;
        }
        //add by xcx 2015-08-25 end cache为null，loader已经调用clearCache(),不需要继续下载
        // 获取该loader的硬盘缓存
        DiskLruCache diskLruCache = dataCache.getDiskLruCache();
        String md5 = Md5Util.md5(urlString);
        // 先从硬盘缓存中读取数据
        if (diskLruCache != null && diskLruCache.containsKey(md5))
        {
            Bitmap bitmap = diskLruCache.get(md5);
            if (bitmap != null && !bitmap.isRecycled())
            {
                return bitmap;
            }
        }
        // 硬盘中没有缓存文件则从网络上获取数据并将信息写入缓存文件中
        // 替换为通过HttpUrlConnection去获取数据
        return downloadDataByUrlConn(urlString, diskLruCache);
    }

    /**
     * The main processing method. This happens in a background task. In this
     * case we are just sampling down the bitmap and returning it from a
     * resource.
     * 
     * @param resId
     * @return
     */
    private Bitmap processResId(int resId)
    {
        return decodeSampledBitmapFromResource(mResources, resId, mLoaderParams.mImageWidth, mLoaderParams.mImageHeight);
    }

    /**
     * Decode and sample down a bitmap from resources to the requested width and
     * height.
     * 
     * @param res The resources object containing the image data
     * @param resId The resource id of the image data
     * @param reqWidth The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return A bitmap sampled down from the original with the same aspect
     * ratio and dimensions that are equal to or greater than the requested
     * width and height
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight)
    {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        // return BitmapFactory.decodeResource(res, resId, options);
        Bitmap sourceBitmap = null;
        int retryTime = 0;
        do
        {
            try
            {
                sourceBitmap = BitmapFactory.decodeResource(res, resId, options);
            }
            catch (OutOfMemoryError e)
            {
            }
            options.inSampleSize += 1;
            retryTime++;
        }
        while (sourceBitmap == null && retryTime < 2);
        return sourceBitmap;
    }

    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options}
     * object when decoding bitmaps using the decode* methods from
     * {@link BitmapFactory}. This implementation calculates the closest
     * inSampleSize that will result in the final decoded bitmap having a width
     * and height equal to or larger than the requested width and height. This
     * implementation does not ensure a power of 2 is returned for inSampleSize
     * which can be faster when decoding but results in a larger bitmap which
     * isn't as useful for caching purposes.
     * 
     * @param options An options object with out* params already populated (run
     * through a decode* method with inJustDecodeBounds==true
     * @param reqWidth The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if ((reqHeight > 0 && reqWidth > 0) && (height > reqHeight || width > reqWidth))
        {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap)
            {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    /**
     * Decode and sample down a bitmap from a file to the requested width and
     * height.
     * 
     * @param filename The full path of the file to decode
     * @param reqWidth The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return A bitmap sampled down from the original with the same aspect
     * ratio and dimensions that are equal to or greater than the requested
     * width and height
     */
    public static Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight)
    {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try
        {
            BitmapFactory.decodeFile(filename, options);
        }
        catch (OutOfMemoryError e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        // return BitmapFactory.decodeFile(filename, options);
        Bitmap sourceBitmap = null;
        int retryTime = 0;
        do
        {
            try
            {
                sourceBitmap = BitmapFactory.decodeFile(filename, options);
            }
            catch (OutOfMemoryError e)
            {
            }
            options.inSampleSize += 1;
            retryTime++;
        }
        while (sourceBitmap == null && retryTime < 2);

        if (options.inSampleSize == 2 && reqWidth > 0 && reqHeight > 0)
        {
            // 请求图片高度或宽度与图片实际高度或宽度小时，放大图片达到要求的效果
            sourceBitmap = scaleBitmap(sourceBitmap, reqWidth, reqHeight);
        }

        return sourceBitmap;
    }

    private static Bitmap scaleBitmap(Bitmap bm, int reqWidth, int reqHeight)
    {
        if (bm == null || bm.isRecycled())
        {
            return bm;
        }

        int width = bm.getWidth();
        int height = bm.getHeight();

        if (width < reqWidth && height < reqHeight)
        {
            /* 计算图片放大的比例 */
            float scale = 1.25f;

            // Calculate ratios of height and width to requested height and
            // width
            final float heightRatio = (float) reqHeight / (float) height;
            final float widthRatio = (float) reqWidth / (float) width;

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee a final image with both dimensions larger than or equal
            // to the requested height and width.
            scale = heightRatio < widthRatio ? heightRatio : widthRatio;

            /* 生成resize后的Bitmap对象 */
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            try
            {
                bm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
            }
            catch (OutOfMemoryError e)
            {
                System.gc();
            }
            catch (Exception e)
            {
            }
        }
        return bm;
    }

    /**
     * Decode and sample down a bitmap from a file input stream to the requested
     * width and height.
     * 
     * @param fileDescriptor The file descriptor to read from
     * @param reqWidth The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return A bitmap sampled down from the original with the same aspect
     * ratio and dimensions that are equal to or greater than the requested
     * width and height
     */
    public static Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor fileDescriptor, int reqWidth, int reqHeight)
    {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap sourceBitmap = null;
        int retryTime = 0;
        do
        {
            try
            {
                sourceBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
            }
            catch (OutOfMemoryError e)
            {
                System.gc();
            }
            options.inSampleSize += 1;
            retryTime++;
        }
        while (sourceBitmap == null && retryTime < 2);
        return sourceBitmap;
    }

    public Bitmap decodeSampledBitmapFromByteArrayNew(byte[] data, int reqWidth, int reqHeight)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap sourceBitmap = null;
        int retryTime = 0;
        do
        {
            try
            {
                sourceBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
            }
            catch (OutOfMemoryError e)
            {
                System.gc();
            }
            options.inSampleSize += 1;
            retryTime++;
        }
        while (sourceBitmap == null && retryTime < 2);

        if (options.inSampleSize == 2 && reqWidth > 0 && reqHeight > 0)
        {
            // 请求图片高度或宽度与图片实际高度或宽度小时，放大图片达到要求的效果
            sourceBitmap = scaleBitmap(sourceBitmap, reqWidth, reqHeight);
        }
        return sourceBitmap;
    }

}
