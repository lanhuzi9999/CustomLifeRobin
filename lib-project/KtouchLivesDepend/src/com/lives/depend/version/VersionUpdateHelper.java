package com.lives.depend.version;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * ****************************************************************
 * 文件名称 : VersionUpdateHelper.java
 * 作 者 :   hyl
 * 创建时间 : 2015-11-23 下午4:26:58
 * 文件描述 : 版本更新帮助类，抽象出检查更新过程中一些公用的方法
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-23 1.00 初始版本
 *****************************************************************
 */
public class VersionUpdateHelper
{
    private static final String TAG = "VersionUtils";
    public static final String KEY = "233&*Adc^%$$per"; //请求加密key
    
    public static final int MSG_SUCCESS = 0x1;
    public static final int MSG_FAIL = 0x2;
    
    public static final int FAILED_REASON_NOTFOUND = 0;
    public static final int FAILED_REASON_NETWORK = 1;
    public static final int FAILED_REASON_SERVER_ERROR = 2;
    
    private VersionUpdateHelper(){};
    
    private static VersionUpdateHelper versionUpdateHelper = null;
    
    public static VersionUpdateHelper getInstance()
    {
        if(versionUpdateHelper == null)
        {
            versionUpdateHelper = new VersionUpdateHelper();
        }
        return versionUpdateHelper;
    }
    
    public String getVersionName(Context context)
    {
        String versionName = "";
        try
        {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return versionName;
    }

    public String getDeviceId(Context context)
    {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String devid = tm.getDeviceId();
        if (null == devid)
        {
            devid = "";
        }
        return devid;
    }
    
    public String getChannelNo(Context context) {
        String channelNo = getMetaData(context, "PUTAO_CHANNEL");
        return channelNo;
    }
    
    public int getAppId(Context context) {
        String appid = getMetaData(context, "PUTAO_APPID");
        int appId = 10;
        if(!TextUtils.isEmpty(appid))
        {
            appId = Integer.parseInt(appid);
        }
        return appId;
    }
    
    public String getMetaData(Context context, String meta_data_key) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                 PackageManager.GET_META_DATA);
            return ai.metaData.get(meta_data_key).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    private Thread checkThread;

    /**
     * 检查更新
     * @param context 上下文
     * @param path 目标url
     * @param isAutoCheck true-自动检查更新 false-收到检查更新
     * @param handler 回调Handler
     */
    public void checkUpdate(final Context context,final String path,final boolean isAutoCheck, final Handler handler)
    {
        if (checkThread != null && checkThread.isAlive())
        {
            checkThread.interrupt();
            checkThread = null;
        }
        checkThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String requestStr = getRequestParm(context);
                String newVersionInfo = getNewVersionInfo(requestStr,path);

                if (!TextUtils.isEmpty(newVersionInfo))
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(newVersionInfo);
                        String retCode = jsonObject.getString("ret_code");
                        if ("0000".equals(retCode))
                        {
                            // 表示请求返回成功
                            String currentVersion = getVersionName(context);
                            String newVersion = null;
                            if(jsonObject.has("version"))
                            {
                                newVersion = jsonObject.getString("version");
                            }
                            if (!TextUtils.isEmpty(newVersion) && !currentVersion.equals(newVersion))
                            {
                                Message msg = handler.obtainMessage(MSG_SUCCESS);
                                msg.obj = newVersionInfo;
                                msg.arg1 = isAutoCheck ? VersionUtil.VERSION_CHECK_ACTION_AUTO : VersionUtil.VERSION_CHECK_ACTION_NOT_AUTO;
                                handler.sendMessage(msg);
                            }
                            else if (!isAutoCheck)
                            {
                                // 当前已经是最新版本,没有版本需要更新
                                Message msg = handler.obtainMessage(MSG_FAIL);
                                msg.arg1 = FAILED_REASON_NOTFOUND;
                                handler.sendMessage(msg);
                            }
                        }
                        else if (!isAutoCheck)
                        {
                            // 表示请求返回失败
                            String error = jsonObject.getString("msg");
                            Message msg = handler.obtainMessage(MSG_FAIL);
                            msg.arg1 = FAILED_REASON_SERVER_ERROR;
                            msg.obj = error;
                            handler.sendMessage(msg);
                        }
                    }
                    catch (JSONException e)
                    {
                        Log.w(TAG, "catch JSONException throw by checkUpdate.", e);
                        if (!isAutoCheck)
                        {
                            Message msg = handler.obtainMessage(MSG_FAIL);
                            msg.arg1 = FAILED_REASON_SERVER_ERROR;
                            handler.sendMessage(msg);
                        }
                    }
                }
                else if (!isAutoCheck)
                {
                    // 服务器返回的数据为空，表示网络异常
                    Message msg = handler.obtainMessage(MSG_FAIL);
                    msg.arg1 = FAILED_REASON_NETWORK;
                    handler.sendMessage(msg);
                }
            }
        });
        checkThread.start();
    }
    
    /**
     * 获取联网请求参数
     * @param context 上下文
     * @return String 请求参数
     */
    private String getRequestParm(Context context){
        JSONObject requestJson = new JSONObject();
        try
        {
            JSONObject versionJson = new JSONObject();
            String version = getVersionName(context);
            versionJson.put("app_version", version);
            requestJson.put("version", versionJson);

            JSONObject uaJson = new JSONObject();
            uaJson.put("system_name", "android");
            uaJson.put("system_version", android.os.Build.VERSION.RELEASE);
            uaJson.put("band", android.os.Build.MODEL);
            requestJson.put("ua", uaJson);
            
            String actionCode = "00002";
            requestJson.put("action_code", actionCode);
            requestJson.put("app_id", getAppId(context));
            requestJson.put("channel_no",getChannelNo(context));
            long timestamp = System.currentTimeMillis();
            String secret_key = VersionMd5Util.md5(actionCode + version + timestamp+ KEY);
            requestJson.put("secret_key", secret_key);
            requestJson.put("timestamp", timestamp);
            requestJson.put("device_code", getDeviceId(context));
            requestJson.put("local_dual_version", 0);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return requestJson.toString();
    }
    
    /**
     * 联网请求获取新版本信息
     * @param requestParm 请求参数
     * @param path 目标url
     * @return String 请求返回的信息
     */
    private String getNewVersionInfo(String requestParm,String path){
        Log.i(TAG, "requestParm:"+requestParm);
        return HttpUtils.postRequest(path, requestParm);
    }
    
    /**
     * 开始下载
     * @param context 上下文
     * @param url  目标url
     * @param handler 下载状态回调handler
     */
    public void startDownload(Context context,String url,DownloadHandler handler)
    {
        DownloadUtils.downloadApp(context, url, handler);
    }
    
    /**
     * 取消下载
     * @param context
     */
    public void cancelDownload(Context context)
    {
        DownloadUtils.cancelDownloadApp(context);
    }
    
    /**
     * 安装应用
     * @param context 山下文
     * @param path 文件路径
     */
    public void installApk(Context context, String path)
    {
        if (null != path) {
            // 通过Intent安装APK文件
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(new File(path));
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
