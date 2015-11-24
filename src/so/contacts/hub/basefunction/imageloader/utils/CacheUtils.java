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

import java.io.File;

import com.putao.live.R;

import so.contacts.hub.ContactsApp;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;


/**************************************************
 * <br>文件名称: CacheUtils.java
 * <br>版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * <br>创建人员: wcy
 * <br>文件描述: 工具类(比较android版本、外部缓存文件路径等功能) 
 *            Class containing some static utility methods. 
 * <br>修改时间: 2015-7-6 下午1:11:14
 * <br>修改历史: 2015-7-6 1.00 初始版本
 **************************************************/
public class CacheUtils
{
	private final static String TAG = "CacheUtils";

    public static final int IO_BUFFER_SIZE = 8 * 1024;

    private CacheUtils()
    {
    };

    /**
     * Workaround for bug pre-Froyo, see here for more info:
     * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     */
    public static void disableConnectionReuseIfNecessary()
    {
        // HTTP connection reuse which was buggy pre-froyo
        if (hasHttpConnectionBug())
        {
            System.setProperty("http.keepAlive", "false");
        }
    }

    /**
     * Check if external storage is built-in or removable.
     * 
     * @return True if external storage is removable (like an SD card), false
     * otherwise.
     */
    public static boolean isExternalStorageRemovable()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
        {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    /**
     * Get the external app cache directory.
     * 
     * @param context The context to use
     * @return The external cache dir
     */
    public static File getExternalCacheDir(Context context)
    {
        if (context == null)
        {
            if (ContactsApp.getInstance() != null)
            {
                context = ContactsApp.getInstance().getApplicationContext();
            }
        }
        if (context == null)
        {
            return null;
        }
        /**
         * modify by zjh 2014-10-13 start 在插件调用在Android 4.4上会出现异常 可能是该路径在Android
         * 4.4中有进程权限问题
         */
        // if (hasExternalCacheDir()) {
        // return context.getExternalCacheDir();
        // }
        /*** modify by zjh 2014-10-13 end */

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    /**
     * Check how much usable space is available at a given path.
     * 
     * @param path The path to check
     * @return The space available in bytes
     */
    public static long getUsableSpace(File path)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
        {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }

    /**
     * Get the memory class of this device (approx. per-app memory limit)
     * 
     * @param context
     * @return
     */
    public static int getMemoryClass(Context context)
    {
        return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
    }

    /**
     * Check if OS version has a http URLConnection bug. See here for more
     * information:
     * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     * 
     * @return
     */
    public static boolean hasHttpConnectionBug()
    {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO;
    }

    /**
     * Check if OS version has built-in external cache dir method.
     * 
     * @return
     */
    public static boolean hasExternalCacheDir()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * Check if ActionBar is available.
     * 
     * @return
     */
    public static boolean hasActionBar()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasFroyo()
    {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed
        // behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * 防止用户手动删除cache文件夹或者手机管家清理缓存文件将图片缓存文件夹删除的情况，将图片缓存文件夹存放在sd卡的另外一个路径上
     * 
     * @param failedCachePath
     * @return File 外置sd卡上的图片缓存文件夹
     */
    public static File getExternalSDCacheDir(String failedCachePath)
    {
        String packageName = R.class.getPackage().getName();
        String fileName = packageName;
        if (!TextUtils.isEmpty(failedCachePath))
        {
            int index = failedCachePath.indexOf(packageName);
            if (index != -1)
            {
                fileName = failedCachePath.substring(index);
            }
        }
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
        {
            File file = new File(Environment.getExternalStorageDirectory(), fileName);
            if (file.exists())
            {
                return file;
            }
            else
            {
                if (file.mkdirs())
                {
                    return file;
                }
            }
        }
        return null;
    }
}
