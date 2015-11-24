/**
 * 
 */
package so.contacts.hub.basefunction.imageloader.image;

import com.putao.live.R;

import so.contacts.hub.basefunction.imageloader.DataLoaderParams;
import so.contacts.hub.basefunction.utils.ContactsHubUtils;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


/**************************************************
 * <br>文件名称: ImageLoaderParams.java
 * <br>版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * <br>创建人员: Acher
 * <br>文件描述: 图片加载参数(宽度、高度、圆角、淡入效果) 
 * <br>修改时间: 2015-7-6 下午1:10:42
 * <br>修改历史: 2015-7-6 1.00 初始版本
 **************************************************/
public class ImageLoaderParams extends DataLoaderParams
{

    private Resources mResources;

    /**
     * 图片宽度
     */
    public int mImageWidth;

    /**
     * 图片高度
     */
    public int mImageHeight;

    /**
     * 圆角参数
     */
    public int mRoundPx;

    /**
     * 占位图像
     */
    public Bitmap mLoadingBitmap;

    /**
     * 加载失败站位bitmap
     */
    public Bitmap mLoadFailedBitmap;

    public boolean mFadeInBitmap = false;

    public boolean mCornerInBitmap = false;

    public ImageLoaderParams(Context context)
    {
        mResources = context.getResources();
    }

    public ImageLoaderParams(Context context, int imageWidth, int imageHeight, int roundPx)
    {
        this(context);
        mImageWidth = imageWidth;
        mImageHeight = imageHeight;
        mRoundPx = roundPx;
    }

    public ImageLoaderParams(Context context, int imageSize)
    {
        this(context, imageSize, imageSize, 0);
    }

    public ImageLoaderParams(Context context, int imageSize, int roundPx)
    {
        this(context, imageSize, imageSize, roundPx);
    }

    /**
     * Set placeholder bitmap that shows when the the background thread is
     * running.
     * 
     * @param bitmap
     */
    public void setLoadingImage(Bitmap bitmap)
    {
        mLoadingBitmap = bitmap;
    }

    /**
     * Set placeholder bitmap that shows when the the background thread is
     * running. 设置占位图像
     * 
     * @param resId
     */
    public void setLoadingImage(int resId)
    {
        Bitmap bitmap = BitmapFactory.decodeResource(mResources, resId);
        if (mRoundPx > 0)
        {
            mLoadingBitmap = ContactsHubUtils.corner(bitmap, mRoundPx, mImageWidth);
        }
        else if (mRoundPx == 0)
        {
            Bitmap bitmapmask = BitmapFactory.decodeResource(mResources, R.drawable.putao_mask);
            mLoadingBitmap = ContactsHubUtils.makeRoundCornerforCoolPad(bitmap, bitmapmask);
        }
        else
        {
            mLoadingBitmap = bitmap;
        }

    }

    /**
     * Set placeholder bitmap that shows when the the background thread is
     * running.
     * 
     * @param bitmap
     */
    public void setLoadFailedImage(Bitmap bitmap)
    {
        mLoadFailedBitmap = bitmap;
    }

    /**
     * Set placeholder bitmap that shows when the the background thread is
     * running. 设置占位图像
     * 
     * @param resId
     */
    public void setLoadFailedImage(int resId)
    {
        mLoadFailedBitmap = BitmapFactory.decodeResource(mResources, resId);
    }

    /**
     * If set to true, the image will fade-in once it has been loaded by the
     * background thread.
     */
    public void setImageFadeIn(boolean fadeIn)
    {
        mFadeInBitmap = fadeIn;
    }

    /**
     * If set to true, the image will corner-in.
     */
    public void setImageCornerIn(boolean cornerIn)
    {
        mCornerInBitmap = cornerIn;
    }
}
