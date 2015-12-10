package com.lives.depend.version;

import java.io.File;
import java.util.HashMap;

import com.lives.depend.R;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

/**
 * ****************************************************************
 * 文件名称 : DownloadUtils.java
 * 作 者 :   hyl
 * 创建时间 : 2015-11-23 下午4:15:05
 * 文件描述 : 下载管理类
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-23 1.00 初始版本
 *****************************************************************
 */
public class DownloadUtils
{
    private static final String TAG = "DownloadUtils";

    private static final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");

    private static long currentDownloadId = 0;

    private static DownloadContentObserver downloadContentObserver;
    
    public static boolean isDownloading()
    {
        return currentDownloadId > 0;
    }
    
    /**
     * 下载App
     * @param context
     * @param url 下载地址
     * @param handler 下载回调handler
     * @return long 下载队列中的id
     */
    public static long downloadApp(Context context, String url, final DownloadHandler handler)
    {
        long downloadId = 0;
        if (currentDownloadId == 0)
        {
            Uri resource = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(resource);
            request.setAllowedNetworkTypes(Request.NETWORK_MOBILE | Request.NETWORK_WIFI);
            String downloadPath = "/PtLives/download/";
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            {
                File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + downloadPath);
                if (!file.exists())
                {
                    file.mkdirs();
                }
                String filename = "putao_"+System.currentTimeMillis()+"." + MimeTypeMap.getFileExtensionFromUrl(url);
                try
                {
                    request.setDestinationInExternalPublicDir(downloadPath, filename);
                    DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    downloadId = dm.enqueue(request);
                    currentDownloadId = downloadId;
                }
                catch (IllegalStateException e)
                {
                    Toast.makeText(context, R.string.putao_vupdate_check_sd_card_status, Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(context, R.string.putao_vupdate_check_sd_card_status, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            downloadId = currentDownloadId;
        }
        
        downloadContentObserver = new DownloadContentObserver(handler, context);
        context.getContentResolver().registerContentObserver(CONTENT_URI, true, downloadContentObserver);
        return downloadId;
    }

    /**
     * 取消当前正在下载的App
     * @param context
     */
    public static void cancelDownloadApp(Context context)
    {
        cancelDownloadApp(context, currentDownloadId);
    }

    /**
     * 取消下载
     * @param context
     * @param downloadId 下载队列id
     */
    private static void cancelDownloadApp(Context context, long downloadId)
    {
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        dm.remove(downloadId);
        currentDownloadId = 0;
    }

    
    private static class DownloadContentObserver extends ContentObserver
    {

        DownloadHandler mhandler;

        Context mContext;

        long lastBytesSoFar;

        public DownloadContentObserver(Handler handler, Context context)
        {
            super(handler);
            mhandler = (DownloadHandler) handler;
            mContext = context;
        }

        @Override
        public void onChange(boolean selfChange)
        {
            if(currentDownloadId > 0)
            {
                DownloadStatus downloadStatus = queryDownloadStatus(mContext, currentDownloadId);
                switch (downloadStatus.status)
                {
                    case DownloadManager.STATUS_PAUSED:
                        Log.v(TAG, "STATUS_PAUSED");
                    case DownloadManager.STATUS_PENDING:
                        Log.v(TAG, "STATUS_PENDING");
                    case DownloadManager.STATUS_RUNNING:
                        Log.v(TAG, "STATUS_RUNNING");
                        
                        Log.i(TAG, "progressSize:" + downloadStatus.bytesSoFar + " totalSize:" + downloadStatus.totalSize);
                        
                        if(downloadStatus.bytesSoFar > lastBytesSoFar)
                        {
                            lastBytesSoFar = downloadStatus.bytesSoFar;
                            mhandler.doDownloadProgress(downloadStatus.totalSize, downloadStatus.bytesSoFar);
                        }
                        break;
                    case DownloadManager.STATUS_SUCCESSFUL:
                        Log.v(TAG, "STATUS_SUCCESSFUL");
                        mhandler.doDownloadProgress(downloadStatus.totalSize, downloadStatus.totalSize);
                        mhandler.doDownloadSuccess(downloadStatus.localPath, downloadStatus.localUri);
                        currentDownloadId = 0;
                        break;
                    case DownloadManager.STATUS_FAILED:
                        Log.v(TAG, "STATUS_FAILED");
                        cancelDownloadApp(mContext);
                        mhandler.doDownloadFail(downloadStatus.reason);
                        break;
                    default:
                        Log.v(TAG, "queryDownloadStatus=" + downloadStatus.status);
                        break;
                }
            }
        }

    }

    /**
     * 查询下载状态
     * @param context
     * @param ids 下载的ids 
     * @return DownloadStatus 下载状态
     */
    public static DownloadStatus queryDownloadStatus(Context context, long... ids)
    {
        DownloadStatus ds = new DownloadStatus();

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(ids);

        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Cursor c = dm.query(query);
        if (c != null && c.moveToFirst())
        {
            ds.status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            ds.id = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_ID));
            ds.bytesSoFar = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            ds.totalSize = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            ds.localUri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
            ds.localPath = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
            ds.reason = c.getString(c.getColumnIndex(DownloadManager.COLUMN_REASON));
        }
        if (c != null)
        {
            c.close();
        }
        return ds;
    }

}
