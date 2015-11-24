/**
 * 
 */
package so.contacts.hub.basefunction.imageloader.image;

import java.io.File;

import com.putao.live.R;

import so.contacts.hub.basefunction.imageloader.DataCache;
import so.contacts.hub.basefunction.imageloader.DataLoader;
import so.contacts.hub.basefunction.imageloader.DataLoaderFactory;
import so.contacts.hub.basefunction.imageloader.DataLoaderParams;
import so.contacts.hub.basefunction.imageloader.utils.DiskLruCache;

import android.content.Context;

/**************************************************
 * <br>文件名称: ImageLoaderFactory.java
 * <br>版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * <br>创建人员: wcy
 * <br>文件描述: 生产各个业务loader的工厂类 注意：尽量复用已经存在的loader,
 *            因为一个业务会创建一个lrucache和disklrucache对象 
 * <br>修改时间: 2015-7-6 下午1:09:55
 * <br>修改历史: 2015-7-6 1.00 初始版本
 **************************************************/
public class ImageLoaderFactory extends DataLoaderFactory
{

    private static final String UNIQUE_NAME = "images";

    public ImageLoaderFactory(Context context)
    {
        super(context);
    }

    /**
     * 获取通用的图片下载器（不限制图片大小）
     */
    public DataLoader getNormalLoader(boolean defaultLoader, boolean needNorner)
    {
        DataCache.DataCacheParams cacheParams = new DataCache.DataCacheParams(context, "normal_loader");
        ImageLoaderParams loaderParams = null;
        if (needNorner)
        {
            int corner = context.getResources().getDimensionPixelSize(R.dimen.putao_image_round_corner);
            loaderParams = new ImageLoaderParams(context, 0, corner);
        }
        else
        {
            loaderParams = new ImageLoaderParams(context);
        }
        loaderParams.setLoadingImage(R.drawable.putao_service_def_logo_small);
        loaderParams.setImageCornerIn(false);
        loaderParams.setImageFadeIn(false);
        return getDataLoader(defaultLoader, cacheParams, loaderParams);
    }

    /**
     * 获取通用的图片下载器（不限制图片大小）
     */
    public DataLoader getNormalLoader(boolean defaultLoader, boolean needNorner, boolean needLoadImg)
    {
        DataCache.DataCacheParams cacheParams = new DataCache.DataCacheParams(context, "normal_loader");
        ImageLoaderParams loaderParams = null;
        if (needNorner)
        {
            int corner = context.getResources().getDimensionPixelSize(R.dimen.putao_image_round_corner);
            loaderParams = new ImageLoaderParams(context, 0, corner);
        }
        else
        {
            loaderParams = new ImageLoaderParams(context);
        }
        if (needLoadImg)
        {
            loaderParams.setLoadingImage(R.drawable.putao_service_def_logo_small);
        }
        loaderParams.setImageCornerIn(false);
        loaderParams.setImageFadeIn(false);
        return getDataLoader(defaultLoader, cacheParams, loaderParams);
    }

    /**
     * add by zj 2015-03-27 获取通用的图片下载器（不限制图片大小）,默认loading图片为原始形状
     */
    public DataLoader getNormalLoaderWithNoCorner(boolean defaultLoader)
    {
        return getNormalLoaderWithNoCorner(defaultLoader, R.drawable.putao_pic_list_none);
    }

    /**
     * 加载没有圆角的bitmap的loader
     * 
     * @author wcy
     * @since 2015-5-28
     * @param defaultLoader
     * @param resIdOfDrawable
     * @return
     */
    public DataLoader getNormalLoaderWithNoCorner(boolean defaultLoader, int resIdOfDrawable)
    {
        DataCache.DataCacheParams cacheParams = new DataCache.DataCacheParams(context, "normal_loader");
        ImageLoaderParams loaderParams = null;
        loaderParams = new ImageLoaderParams(context, 0, -1);
        loaderParams.setLoadingImage(resIdOfDrawable);
        loaderParams.setImageCornerIn(false);
        loaderParams.setImageFadeIn(false);
        return getDataLoader(defaultLoader, cacheParams, loaderParams);
    }

    /**
     * 获取通用的图片下载器（不限制图片大小） 可以设置默认图片
     * 
     * @param defaultLoader
     * @param needNorner
     * @param resId
     * @return
     */
    public DataLoader getNormalLoaderWithDefaultImg(boolean defaultLoader, boolean needNorner, int resId)
    {
        DataCache.DataCacheParams cacheParams = new DataCache.DataCacheParams(context, "normal_loader");
        ImageLoaderParams loaderParams = null;
        if (needNorner)
        {
            int corner = context.getResources().getDimensionPixelSize(R.dimen.putao_image_round_corner);
            loaderParams = new ImageLoaderParams(context, 0, corner);
        }
        else
        {
            loaderParams = new ImageLoaderParams(context);
        }
        loaderParams.setImageCornerIn(false);
        loaderParams.setImageFadeIn(false);
        loaderParams.setLoadingImage(resId);
        return getDataLoader(defaultLoader, cacheParams, loaderParams);
    }

    /**
     * 电影业务加载图片loader
     * 
     * @author wcy
     * @since 2015-5-28
     * @return
     */
    public DataLoader getMovieListLoader()
    {
        DataCache.DataCacheParams cacheParams = new DataCache.DataCacheParams(context, "movie_list");
        ImageLoaderParams loaderParams = new ImageLoaderParams(context, 210, 280, -1);
        loaderParams.setLoadingImage(R.drawable.putao_bg_pic_dianying);
        loaderParams.setImageCornerIn(false);
        loaderParams.setImageFadeIn(false);
        return getDataLoader(true, cacheParams, loaderParams);
    }

    /**
     * 用于优惠券业务的图片loader
     * 
     * @author wcy
     * @since 2015-5-28
     * @return
     */
    public DataLoader getActiveHistoryLoader()
    {
        DataCache.DataCacheParams cacheParams = new DataCache.DataCacheParams(context, "activities_history");
        ImageLoaderParams loaderParams = new ImageLoaderParams(context, -1, -1);
        loaderParams.setLoadingImage(R.drawable.putao_pic_quick_replace);
        loaderParams.setImageCornerIn(false);
        loaderParams.setImageFadeIn(false);
        return getDataLoader(true, cacheParams, loaderParams);
    }

    /**
     * 通用黄页业务图片加载loader
     * 
     * @author wcy
     * @since 2015-5-28
     * @param isShowLoadingImage
     * @param resId
     * @param imageSize
     * @param corner
     * @return
     */
    public DataLoader getYellowPageLoader(boolean isShowLoadingImage, int resId, int imageSize, int corner)
    {
        DataCache.DataCacheParams cacheParams = new DataCache.DataCacheParams(context, "yellow_page_images");
        ImageLoaderParams loaderParams = new ImageLoaderParams(context, imageSize, corner);
        if (isShowLoadingImage)
        {
            loaderParams.setLoadingImage(resId);
        }
        loaderParams.setImageCornerIn(false);
        loaderParams.setImageFadeIn(false);

        return getDataLoader(true, cacheParams, loaderParams);
    }

    /**
     * 主要用于美食、酒店订单、商铺详情页、搜索更多业务的图片加载loader(带圆角)
     * 
     * @author wcy
     * @since 2015-5-28
     * @param resId
     * @param imageSize
     * @return
     */
    public DataLoader getYellowPageLoader(int resId, int imageSize)
    {
        int corner = context.getResources().getDimensionPixelSize(R.dimen.putao_image_round_corner);
        return getYellowPageLoader(true, resId, imageSize, corner);
    }

    /**
     * 酒店业务加载图片loader(带圆角)
     * 
     * @author wcy
     * @since 2015-5-28
     * @param resId
     * @param imageSize
     * @return
     */
    public DataLoader getYellowPageHotelRoomLoader(int resId, int imageSize)
    {
        int corner = context.getResources().getDimensionPixelSize(R.dimen.putao_image_round_corner_hotel_room);
        // return getYellowPageLoader(true,resId,imageSize,corner);//带圆角
        return getNormalLoaderWithNoCorner(true, resId);// 不带圆角
    }

    /**
     * 主要用于附近、收藏历史业务的图片加载loader(带圆角)
     * 
     * @author wcy
     * @since 2015-5-28
     * @return
     */
    public DataLoader getDefaultYellowPageLoader()
    {
        int imageSize = context.getResources().getDimensionPixelSize(R.dimen.putao_listview_item_imgsize);
        int resId = R.drawable.putao_icon_logo_placeholder;
        int corner = context.getResources().getDimensionPixelSize(R.dimen.putao_image_round_corner);
        return getYellowPageLoader(true, resId, imageSize, corner);
    }

    /**
     * 主要是商户详情页的图片加载loader(带圆角)
     * 
     * @author wcy
     * @since 2015-5-28
     * @param isShowLoadingImage
     * @param imageSize
     * @return
     */
    public DataLoader getYellowPageDetailLoader(boolean isShowLoadingImage, int imageSize)
    {
        int corner = context.getResources().getDimensionPixelSize(R.dimen.putao_image_round_corner);
        return getYellowPageDealLoader(isShowLoadingImage, imageSize, corner);
    }

    /**
     * 主要是商户详情页的图片加载loader
     * 
     * @author wcy
     * @since 2015-5-28
     * @param isShowLoadingImage
     * @param imageSize
     * @param corner
     * @return
     */
    public DataLoader getYellowPageDealLoader(boolean isShowLoadingImage, int imageSize, int corner)
    {
        DataCache.DataCacheParams cacheParams = new DataCache.DataCacheParams(context, "yellow_page_deal_images");
        ImageLoaderParams loaderParams = new ImageLoaderParams(context, imageSize, corner);
        if (isShowLoadingImage)
        {
            loaderParams.setLoadingImage(R.drawable.putao_icon_logo_placeholder);
        }
        loaderParams.setImageCornerIn(false);
        loaderParams.setImageFadeIn(false);

        return getDataLoader(true, cacheParams, loaderParams);
    }

    /**
     * 个人中心头像图片缓存 modify by gwq on 2015-5-13
     */
    public DataLoader getStatusAvatarLoader()
    {
        DataCache.DataCacheParams cacheParams = new DataCache.DataCacheParams(context, UNIQUE_NAME);
        File cacheDir = DiskLruCache.getDiskCacheDir(context, ImageLoader.HTTP_CACHE_DIR);
        cacheParams.setDiskCacheDir(cacheDir);
        ImageLoaderParams loaderParams = new ImageLoaderParams(context, 500, 250);
        loaderParams.setImageCornerIn(true);
        loaderParams.setImageFadeIn(false);
        return getDataLoader(true, cacheParams, loaderParams);
    }

    @Override
    public DataLoader getDataLoader(boolean defaultLoader, DataCache.DataCacheParams cacheParams,
            DataLoaderParams loaderParams)
    {
        DataLoader dataLoader = null;
        dataLoader = new ImageLoader(context);
        dataLoader.setDataCache(cacheParams);
        dataLoader.setDataLoaderParams(loaderParams);
        return dataLoader;
    }

    @Override
    public DataLoader getDataLoader(boolean defaultLoader)
    {
        return new ImageLoader(context);
    }

    /**
     * 没有占位图的loader
     * 主要考虑对于气泡来说，占位图会拉伸气泡的宽度和高度（不美观），所以去除占位图
     * @return
     * DataLoader
     */
    public DataLoader getBubbleLoader()
    {
        DataCache.DataCacheParams cacheParams = new DataCache.DataCacheParams(context, ImageLoader.HTTP_CACHE_DIR);
        ImageLoaderParams loaderParams = new ImageLoaderParams(context, -1, -1);
        loaderParams.setImageCornerIn(false);
        loaderParams.setImageFadeIn(false);
        return getDataLoader(true, cacheParams, loaderParams);
    }
}
