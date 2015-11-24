/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package so.contacts.hub.basefunction.imageloader;

import java.io.File;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import so.contacts.hub.ContactsApp;
import so.contacts.hub.basefunction.imageloader.utils.CacheUtils;
import so.contacts.hub.basefunction.imageloader.utils.DiskLruCache;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;


/**************************************************
 * <br>
 * 文件名称: DataCache.java <br>
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有 <br>
 * 创建人员: wcy <br>
 * 文件描述: 缓存类封装内存缓存和硬盘缓存，并可以根据业务去生产缓存对象 <br>
 * 修改时间: 2015-7-6 上午11:39:40 <br>
 * 修改历史: 2015-7-6 1.00 初始版本
 **************************************************/
public class DataCache
{
    private static final String TAG = "DataCache";

    /**
     * 每一个业务的内存缓存大小 8M
     */
    private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 1024 * 8; // 8MB

    /**
     * 每一个业务的硬盘缓存大小
     */
    private static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB

    /**
     * 缓存文件夹名称
     */
    public static final String HTTP_CACHE_DIR = "http_";

    // Constants to easily toggle various caches
    private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;

    /**
     * 硬盘缓存
     */
    private DiskLruCache mDiskLruCache;

    /**
     * 内存缓存
     */
    private LruCache<String, Object> mMemCache;

    /**
     * 缓存参数，包括：缓存名称uniqueName，内存缓存的大小(目前是10M),硬盘缓存的大小(20M)
     */
    private DataCacheParams mCacheParams;
    
    /**
     * 图片复用map，
     * 可以避免前向路径上同一张图片多次调入内存，使用WeakReference类型，既能保证引入此map的目的，又可以避免内存泄露
     */
    private static Map<String,WeakReference<Bitmap>> mWeakBitmaps = Collections.synchronizedMap(new HashMap<String,WeakReference<Bitmap>>());; 
    
    /**
     * Creating a new ImageCache object using the specified parameters.
     * 根据指定的parameters来new 一个ImageCache对象
     * 
     * @param cacheParams The cache parameters to use to initialize the cache
     */
    public DataCache(DataCacheParams cacheParams)
    {
        init(cacheParams);
    }

    /**
     * Creating a new ImageCache object using the default parameters.
     * 
     * @param context The context to use
     * @param uniqueName A unique name that will be appended to the cache
     * directory
     */
    public DataCache(Context context, String uniqueName)
    {
        init(new DataCacheParams(context, uniqueName));
    }

    /**
     * Find and return an existing ImageCache stored in a {@link RetainCache} ,
     * if not found a new one is created using the supplied params and saved to
     * a {@link RetainCache}.
     * 
     * @param cacheParams The cache parameters to use if creating the DataCache
     * @return An existing retained DataCache object or a new one if one did not
     * exist
     */
    public static DataCache findOrCreateCache(DataCacheParams cacheParams)
    {
        //modify by xcx 2015-08-25 start cache里的图片，每次使用完都会recycle，保留cache的对象已经没有意义，所以修改为每次都新建cache
        // // Search for, or create an instance of the non-UI RetainObject
//        final RetainCache mRetainObject = RetainCache.getInstance();
//
//        // See if we already have an ImageCache instance in RetainApplication
//        DataCache dataCache = (DataCache) mRetainObject.getCache(cacheParams.uniqueName);
//
//        LogUtil.i(TAG, "datacache: " + dataCache + " uniqueName:" + cacheParams.uniqueName);
//
//        // No existing ImageCache, create one and store it in RetainApplication
//        if (dataCache == null)
//        {
//            LogUtil.i(TAG, "datacache is created");
//            dataCache = new DataCache(cacheParams);
//            mRetainObject.setCache(cacheParams.uniqueName, dataCache);
//        }
        
        DataCache dataCache= new DataCache(cacheParams);
        return dataCache;
        //modify by xcx 2015-08-25 end cache里的图片，每次使用完都会recycle，保留cache的对象已经没有意义，所以修改为每次都新建cache
    }

    /**
     * Get the size in bytes of a bitmap. 获得Bitmap的byte大小
     * 
     * @param bitmap
     * @return size in bytes
     */
    @TargetApi(12)
    public static int getBitmapSize(Bitmap bitmap)
    {
        if (CacheUtils.hasHoneycombMR1())
        {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * Initialize the cache, providing all parameters. 初始化缓存，提供所有参数
     * 
     * @param cacheParams The cache parameters to initialize the cache
     */
    private void init(DataCacheParams cacheParams)
    {
        mCacheParams = cacheParams;
        // Set up memory cache
        // 设置内存缓存
        if (mCacheParams.memoryCacheEnabled)
        {
            mMemCache = new LruCache<String, Object>(mCacheParams.memCacheSize)
            {
                /**
                 * Measure item size in bytes rather than units which is more
                 * practical for a bitmap cache 这个方法在每次向LruCache存储项时，回调
                 */
                @Override
                protected int sizeOf(String key, Object obj)
                {
                    if (obj instanceof Bitmap)
                    {
                        return getBitmapSize((Bitmap) obj);
                    }
                    return 0;
                }
            };
            if (mCacheParams.diskCacheDir != null)
            {
                mDiskLruCache = DiskLruCache.openCache(mCacheParams.diskCacheDir, mCacheParams.diskCacheSize);
            }
            else
            {
                // 获取外部缓存文件夹存放路径
                mCacheParams.diskCacheDir = DiskLruCache.getDiskCacheDir(ContactsApp.getInstance(), HTTP_CACHE_DIR
                        + mCacheParams.uniqueName);
                // 设置外部缓存文件夹最大缓存大小
                mCacheParams.diskCacheSize = DEFAULT_DISK_CACHE_SIZE;
                mDiskLruCache = DiskLruCache.openCache(mCacheParams.diskCacheDir, mCacheParams.diskCacheSize);
            }

        }
    }

    /**
     * Adds a bitmap to both memory and disk cache.
     * 
     * @param data Unique identifier for the bitmap to store
     * @param bitmap The bitmap to store
     */
    public void addDataToCache(String data, Object result)
    {
        if (data == null || result == null)
        {
            return;
        }
        // Add to memory cache
        if (mMemCache != null)
        {
            mMemCache.put(data, result);
        }
        //add by xcx 2015-08-25 start 图片复用
        if(result instanceof Bitmap)
        {
            mWeakBitmaps.put(data, new WeakReference<Bitmap>((Bitmap)result));  
        }
        //add by xcx 2015-08-25 end 图片复用
    }

    /**
     * Get from memory cache.
     * 
     * @param data Unique identifier for which item to get
     * @return The bitmap if found in cache, null otherwise
     */
    public Object getResultFromCache(String data)
    {
        if (mMemCache != null)
        {
            final Object mCacheResult = mMemCache.get(data);
            if (mCacheResult != null)
            {
                return mCacheResult;
            }
        }
        //add by xcx 2015-08-25 start 图片复用
        if(mWeakBitmaps!=null)
        {
            WeakReference<Bitmap> weakRef=mWeakBitmaps.get(data);
            if(weakRef!=null)
            {
                Bitmap bitmap =weakRef.get();
                if(bitmap!=null && !bitmap.isRecycled())
                {
                    return bitmap;  
                }
            }
        }
        //add by xcx 2015-08-25 end 图片复用
        return null;
    }

    /**
     * Clears both the memory and disk cache associated with this ImageCache
     * object. Note that this includes disk access so this should not be
     * executed on the main/UI thread.
     */
    public void clearCache()
    {
        if (mMemCache != null)
        {
           //modify by xcx 2015-08-25 start 对释放的图片主动recycle，尽快回收内存
           Map<String, Object> map=mMemCache.snapshot();
           for (Map.Entry<String, Object> entry : map.entrySet())
           {  
               Object value=entry.getValue();
               if(value!=null && value instanceof Bitmap)
               {
                    Bitmap bitmap = (Bitmap) value;
                    if (bitmap != null && !bitmap.isRecycled())
                    {
                        bitmap.recycle();
                        bitmap = null;
                    }
                   mWeakBitmaps.remove(entry.getKey());
               }
           } 
           mMemCache.evictAll();
           //modify by xcx 2015-08-25 end 对释放的图片主动recycle，尽快回收内存
        }
        //add by xcx 2015-08-25 start 清除硬盘缓存中的数据，避免图片被长期持有
        if(mDiskLruCache != null)
        {
            mDiskLruCache.clearMap();
        }
        //add by xcx 2015-08-25 end 清除硬盘缓存中的数据，避免图片被长期持有
        System.gc();
    }

    public void removeCache(String key)
    {
        if (mMemCache != null)
        {
            mMemCache.remove(key);
        }
    }

    public DiskLruCache getDiskLruCache()
    {
        return mDiskLruCache;
    }

    /**
     * A holder class that contains cache parameters. 一个持有者类，包含缓存参数
     * 磁盘缓存路径，内存缓存大小限制，缓存切换，图片压缩方式等
     */
    public static class DataCacheParams
    {

        private Context context;

        /**
         * 缓存对应的缓存名称
         */
        private String uniqueName;

        /**
         * 内存中缓存的最大限制
         */
        public int memCacheSize = DEFAULT_MEM_CACHE_SIZE;

        /**
         * 内存缓存是否可用，用来轻松切换不同缓存
         */
        public boolean memoryCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;

        /**
         * 磁盘中缓存的目录dir
         */
        public File diskCacheDir;

        /**
         * 磁盘中缓存的最大限制
         */
        public int diskCacheSize = DEFAULT_DISK_CACHE_SIZE;

        /**
         * @param context
         * @param uniqueName
         */
        public DataCacheParams(Context context, String uniqueName)
        {
            this.context = ContactsApp.getInstance();
            this.uniqueName = uniqueName;
        }

        public void setDiskCacheDir(File diskCacheDir)
        {
            this.diskCacheDir = diskCacheDir;
        }

        public int getMemoryClass()
        {
            return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        }
    }

    /**
     * A simple non-UI single Object that stores a single Object and is retained
     * over configuration changes. It will be used to retain the ImageCache
     * object.
     */
    public static class RetainCache
    {

        private static RetainCache instance;

        private Map<String, SoftReference<DataCache>> mRetainMap;

        /**
         * Private empty constructor as per the Object documentation
         */
        public RetainCache()
        {
        }

        /**
         * Get the stored object.
         * 
         * @return The stored object
         */
        public static synchronized RetainCache getInstance()
        {
            if (instance == null)
            {
                instance = new RetainCache();
            }
            return instance;
        }

        /**
         * Store a single object in this RetainObject.
         * 
         * @param object The object to store
         */
        public void setCache(String uniqueName, DataCache cache)
        {
            SoftReference<DataCache> mCachedObject = new SoftReference<DataCache>(cache);
            if (mRetainMap == null)
            {
                mRetainMap = new HashMap<String, SoftReference<DataCache>>();
            }
            mRetainMap.put(uniqueName, mCachedObject);
        }

        /**
         * Get the stored object.
         * 
         * @return The stored object
         */
        public Object getCache(String uniqueName)
        {
            if (mRetainMap != null)
            {
                SoftReference<DataCache> mCachedObject = mRetainMap.get(uniqueName);
                if (mCachedObject != null)
                {
                    return mCachedObject.get();
                }
            }
            return null;
        }

        /**
         * 清除缓存数据
         * 
         * @author wcy
         * @since 2015-5-15
         */
        public void clear()
        {
            if (mRetainMap != null)
            {
                mRetainMap.clear();
                mRetainMap = null;
            }
            instance = null;
        }
    }

}
