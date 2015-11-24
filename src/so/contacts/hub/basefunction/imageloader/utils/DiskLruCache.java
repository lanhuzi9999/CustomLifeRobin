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

package so.contacts.hub.basefunction.imageloader.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import so.contacts.hub.basefunction.MD5.MD5;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * 硬盘缓存cache,采用lru算法 A simple disk LRU bitmap cache to illustrate how a disk
 * cache would be used for bitmap caching. A much more robust and efficient disk
 * LRU cache solution can be found in the ICS source code
 * (libcore/luni/src/main/java/libcore/io/DiskLruCache.java) and is preferable
 * to this simple implementation.
 */
public class DiskLruCache
{
    
    /**
     * 缓存文件前缀
     */
    private static final String CACHE_FILENAME_PREFIX = "cache_";

    private static final int MAX_REMOVALS = 4;

    /**
     * 初始大小 hashmap的初始容量
     */
    private static final int INITIAL_CAPACITY = 32;

    /**
     * 加载因子 用于hashmap的创建
     */
    private static final float LOAD_FACTOR = 0.75f;

    /**
     * 缓存路径
     */
    private final File mCacheDir;

    /**
     * 缓存个数
     */
    private int cacheSize = 0;

    /**
     * 缓存字节数
     */
    private int cacheByteSize = 0;

    private final int maxCacheItemSize = 80; // 64 item default

    private long maxCacheByteSize = 1024 * 1024 * 25; // 5MB default

    /**
     * 图片压缩格式
     */
    private CompressFormat mCompressFormat = CompressFormat.JPEG;

    /**
     * 图片压缩质量
     */
    private int mCompressQuality = 100;

    private final Map<String, String> mLinkedHashMap = Collections.synchronizedMap(new LinkedHashMap<String, String>(
            INITIAL_CAPACITY, LOAD_FACTOR, true));

    /**
     * A filename filter to use to identify the cache filenames which have
     * CACHE_FILENAME_PREFIX prepended.
     */
    private static final FilenameFilter cacheFileFilter = new FilenameFilter()
    {
        @Override
        public boolean accept(File dir, String filename)
        {
            return filename.startsWith(CACHE_FILENAME_PREFIX);
        }
    };

    /**
     * Used to fetch an instance of DiskLruCache.
     * 
     * @param context
     * @param cacheDir
     * @param maxByteSize
     * @return
     */
    public static DiskLruCache openCache(File cacheDir, long maxByteSize)
    {
        if (cacheDir == null)
        {
            return null;
        }
        if (!cacheDir.exists())
        {
            cacheDir.mkdirs();
        }
        if (cacheDir.isDirectory() && cacheDir.canWrite() && CacheUtils.getUsableSpace(cacheDir) > maxByteSize)
        {
            return new DiskLruCache(cacheDir, maxByteSize);
        }
        // 防止用户手动删除cache文件夹或者手机管家清理缓存文件将图片缓存文件夹删除的情况
        // 将图片缓存文件夹存放在sd卡的另外一个路径上
        File externalSDCacheDir = CacheUtils.getExternalSDCacheDir(cacheDir.getAbsolutePath());
        if (externalSDCacheDir != null)
        {
            return new DiskLruCache(externalSDCacheDir, maxByteSize);
        }
        return null;
    }

    /**
     * Constructor that should not be called directly, instead use
     * {@link DiskLruCache#openCache(Context, File, long)} which runs some extra
     * checks before creating a DiskLruCache instance.
     * 
     * @param cacheDir
     * @param maxByteSize
     */
    private DiskLruCache(File cacheDir, long maxByteSize)
    {
        mCacheDir = cacheDir;
        maxCacheByteSize = maxByteSize;
    }

    /**
     * Add a bitmap to the disk cache.
     * 
     * @param key A unique identifier for the bitmap.
     * @param data The bitmap to store.
     */
    public void put(String key, Bitmap data)
    {
        synchronized (mLinkedHashMap)
        {
            if (mLinkedHashMap.get(key) == null)
            {
                try
                {
                    final String file = createFilePath(mCacheDir, key);
                    if (file != null && writeBitmapToFile(data, file))
                    {
                        put(key, file);
                        flushCache();
                    }
                }
                catch (final FileNotFoundException e)
                {
                }
                catch (final IOException e)
                {
                }
            }
        }
    }

    private void put(String key, String file)
    {
        mLinkedHashMap.put(key, file);
        cacheSize = mLinkedHashMap.size();
        cacheByteSize += new File(file).length();
    }

    /**
     * Flush the cache, removing oldest entries if the total size is over the
     * specified cache size. Note that this isn't keeping track of stale files
     * in the cache directory that aren't in the HashMap. If the images and keys
     * in the disk cache change often then they probably won't ever be removed.
     */
    private void flushCache()
    {
        Entry<String, String> eldestEntry;
        File eldestFile;
        long eldestFileSize;
        int count = 0;

        while (count < MAX_REMOVALS && (cacheSize > maxCacheItemSize || cacheByteSize > maxCacheByteSize))
        {
            eldestEntry = mLinkedHashMap.entrySet().iterator().next();
            eldestFile = new File(eldestEntry.getValue());
            eldestFileSize = eldestFile.length();
            mLinkedHashMap.remove(eldestEntry.getKey());
            eldestFile.delete();
            cacheSize = mLinkedHashMap.size();
            cacheByteSize -= eldestFileSize;
            count++;
        }
    }

    /**
     * Get an image from the disk cache.
     * 
     * @param key The unique identifier for the bitmap
     * @return The bitmap or null if not found
     */
    public Bitmap get(String key)
    {
        synchronized (mLinkedHashMap)
        {
            final String file = mLinkedHashMap.get(key);
            if (file != null)
            {
                return BitmapFactory.decodeFile(file);
            }
            else
            {
                final String existingFile = createFilePath(mCacheDir, key);
                if (new File(existingFile).exists())
                {
                    put(key, existingFile);
                    return BitmapFactory.decodeFile(existingFile);
                }
            }
            return null;
        }
    }

    public String getFilePath(String key)
    {
        String file = mLinkedHashMap.get(key);
        if (file == null)
        {
            file = createFilePath(mCacheDir, key);
        }
        return file;
    }

    /**
     * Checks if a specific key exist in the cache.
     * 
     * @param key The unique identifier for the bitmap
     * @return true if found, false otherwise
     */
    public boolean containsKey(String key)
    {
        // See if the key is in our HashMap
        if (mLinkedHashMap.containsKey(key))
        {
            return true;
        }

        // Now check if there's an actual file that exists based on the key
        final String existingFile = createFilePath(mCacheDir, key);
        if (new File(existingFile).exists())
        {
            // File found, add it to the HashMap for future use
            put(key, existingFile);
            return true;
        }
        return false;
    }

    /**
     * Removes all disk cache entries from this instance cache dir
     */
    public void clearCache()
    {
        DiskLruCache.clearCache(mCacheDir);
    }

    /**
     * Removes all disk cache entries from the application cache directory in
     * the uniqueName sub-directory.
     * 
     * @param context The context to use
     * @param uniqueName A unique cache directory name to append to the app
     * cache directory
     */
    public static void clearCache(Context context, String uniqueName)
    {
        File cacheDir = getDiskCacheDir(context, uniqueName);
        clearCache(cacheDir);
    }

    /**
     * Removes all disk cache entries from the given directory. This should not
     * be called directly, call {@link DiskLruCache#clearCache(Context, String)}
     * or {@link DiskLruCache#clearCache()} instead.
     * 
     * @param cacheDir The directory to remove the cache files from
     */
    private static void clearCache(File cacheDir)
    {
        final File[] files = cacheDir.listFiles(cacheFileFilter);
        if (files == null)
        {
            return;
        }
        for (int i = 0; i < files.length; i++)
        {
            files[i].delete();
        }
    }

    /**
     * Get a usable cache directory (external if available, internal otherwise).
     * 
     * @param context The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     */
    public static File getDiskCacheDir(Context context, String uniqueName)
    {

        // Check if media is mounted or storage is built-in, if so, try and use
        // external cache dir
        // otherwise use internal cache dir
        String cacheDirPath = "";
        if (context != null && context.getCacheDir() != null)
        {
            cacheDirPath = context.getCacheDir().getPath();
        }

        File externalCacheDir = CacheUtils.getExternalCacheDir(context);
        final String cachePath = (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !CacheUtils
                .isExternalStorageRemovable()) && externalCacheDir != null ? externalCacheDir.getPath() : cacheDirPath;
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * Creates a constant cache file path given a target cache directory and an
     * image key.
     * 
     * @param cacheDir
     * @param key
     * @return
     */
    public static String createFilePath(File cacheDir, String key)
    {
        try
        {
            // Use URLEncoder to ensure we have a valid filename, a tad hacky
            // but it will do for
            // this example

            /**
             * old code: return cacheDir.getAbsolutePath() + File.separator +
             * CACHE_FILENAME_PREFIX + URLEncoder.encode(key.replace("*", ""),
             * "UTF-8");
             */
            return cacheDir.getAbsolutePath() + File.separator + CACHE_FILENAME_PREFIX
                    + getFilenameForKey(URLEncoder.encode(key.replace("*", ""), "UTF-8"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解决有些Url过长的问题，采用统一命名 add by zjh 2015-05-12 Creates a pseudo-unique
     * filename for the specified cache key.
     * 
     * @param key The key to generate a file name for.
     * @return A pseudo-unique filename.
     */
    private static String getFilenameForKey(String key)
    {
        /*
         * int firstHalfLength = key.length() / 2; String localFilename =
         * String.valueOf(key.substring(0, firstHalfLength).hashCode());
         * localFilename +=
         * String.valueOf(key.substring(firstHalfLength).hashCode());
         */

        // modified by wcy 2015-6-5
        // 统一采用MD5编码(32位数字与字母之间的数据)
        return MD5.toMD5(key);
    }

    /**
     * Create a constant cache file path using the current cache directory and
     * an image key.
     * 
     * @param key
     * @return
     */
    public String createFilePath(String key)
    {
        return createFilePath(mCacheDir, key);
    }

    /**
     * Sets the target compression format and quality for images written to the
     * disk cache.
     * 
     * @param compressFormat
     * @param quality
     */
    public void setCompressParams(CompressFormat compressFormat, int quality)
    {
        mCompressFormat = compressFormat;
        mCompressQuality = quality;
    }

    /**
     * Writes a bitmap to a file. Call
     * {@link DiskLruCache#setCompressParams(CompressFormat, int)} first to set
     * the target bitmap compression and format.
     * 
     * @param bitmap
     * @param file
     * @return
     */
    private boolean writeBitmapToFile(Bitmap bitmap, String file) throws IOException, FileNotFoundException
    {

        OutputStream out = null;
        try
        {
            out = new BufferedOutputStream(new FileOutputStream(file), CacheUtils.IO_BUFFER_SIZE);
            return bitmap.compress(mCompressFormat, mCompressQuality, out);
        }
        finally
        {
            if (out != null)
            {
                out.close();
                out = null;
            }
        }
    }

    public void clearMap()
    {
        if (mLinkedHashMap != null && mLinkedHashMap.size() > 0)
        {
            mLinkedHashMap.clear();
        }
    }
}
