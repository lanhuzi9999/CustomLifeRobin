/* Copyright (c) 2009-2011 Matthias Kaeppler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package so.contacts.hub.basefunction.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

import so.contacts.hub.basefunction.MD5.MD5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class SystemUtil
{

    // add by lxh 2015-7-2
    private static final String PT_DEVICE_ID_FILENAME = "pt_sys2.txt";

    public static final int SCREEN_DENSITY_LOW = 120;

    public static final int SCREEN_DENSITY_MEDIUM = 160;

    public static final int SCREEN_DENSITY_HIGH = 240;

    private static int screenDensity = -1;

    private static String sPtDeviceId;

    public static int dipToPx(Context context, int dip)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (dip * displayMetrics.density + 0.5f);
    }

    public static Drawable scaleDrawable(Context context, int drawableResourceId, int width, int height)
    {
        Bitmap sourceBitmap = BitmapFactory.decodeResource(context.getResources(), drawableResourceId);
        return new BitmapDrawable(Bitmap.createScaledBitmap(sourceBitmap, width, height, true));
    }

    public static int getScreenDensity(Context context)
    {
        if (screenDensity == -1)
        {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            try
            {
                screenDensity = DisplayMetrics.class.getField("densityDpi").getInt(displayMetrics);
            }
            catch (Exception e)
            {
                screenDensity = SCREEN_DENSITY_MEDIUM;
            }
        }
        return screenDensity;
    }

    /**
     * @return ip地址
     */
    public static String getIp()
    {
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        catch (SocketException ex)
        {
            // Ignore
        }
        return "";
    }

    /**
     * @return 系统版本
     */
    public static String getOS()
    {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * @return 手机型号
     */
    public static String getMachine()
    {
        return android.os.Build.MODEL;
    }

    /**
     * @return SDK版本
     */
    public static String getSDKVersion()
    {
        return android.os.Build.VERSION.SDK;
    }

    /**
     * @param context
     * @return 当前VersionName
     */
    public static String getAppVersion(Context context)
    {
        /**
         * 在合一版本上读取不到我们自己的versionName和versionCode,把版本写在META_DATA里 modified by cj
         * 2014/11/05 start
         */
        try
        {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            return ai.metaData.get("versionName").toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
        // modified by cj 2014/11/05 end

        // old code
        // String versionName = "";
        // try {
        // PackageManager pm = context.getPackageManager();
        // PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
        // versionName = pi.versionName;
        // } catch (NameNotFoundException e) {
        // e.printStackTrace();
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // return versionName;
    }

    /**
     * 在AndroidManifest.xml的META_DATA中获取appid
     * 
     * @param context
     * @return 当前appid
     */
    public static String getAppid(Context context)
    {
        try
        {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            return String.valueOf(ai.metaData.get("PUTAO_APPID"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * @param context
     * @return 当前VersionCode
     */
    public static int getAppVersionCode(Context context)
    {
        /**
         * 在合一版本上读取不到我们自己的versionName和versionCode,把版本写在META_DATA里 modified by cj
         * 2014/11/05 start
         */
        try
        {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            return Integer.parseInt(String.valueOf(ai.metaData.get("versionCode")));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;
        // modified by cj 2014/11/05 end

        // old code
        // int versionCode = 0;
        // try {
        // PackageManager pm = context.getPackageManager();
        // PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
        // versionCode = pi.versionCode;
        // } catch (NameNotFoundException e) {
        // e.printStackTrace();
        // }
        // return versionCode;
    }

    /**
     * @param context
     * @return
     */
    public static String getMetaData(Context context, String meta_data_key)
    {
        try
        {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            return ai.metaData.get(meta_data_key).toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * 
     * @param 地址
     * @param context
     */
    public static void openSystemUrl(String url, Context context)
    {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    /**
     * 检测是否有活动网络
     */
    public static boolean contactNet(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);// 获取系统的连接服务
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();// 获取网络的连接情况
        if (activeNetInfo != null && activeNetInfo.isConnected() && activeNetInfo.isAvailable())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean isConnectionType(Context context, int type)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo active = cm.getActiveNetworkInfo();
        return (active != null && active.getType() == type);
    }

    public static boolean isWIFI(Context context)
    {
        return isConnectionType(context, ConnectivityManager.TYPE_WIFI);
    }

    public static boolean is2GNetWork(Context context)
    {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null)
        {
            return false;
        }
        else
        {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo net = cm.getActiveNetworkInfo();
            if (net.getState() == NetworkInfo.State.CONNECTED)
            {
                int type = net.getType();
                int subtype = net.getSubtype();

                return !isConnectionFast(type, subtype);
            }
        }
        return false;
    }

    public static boolean isConnectionFast(int type, int subType)
    {
        if (type == ConnectivityManager.TYPE_WIFI)
        {
            return true;
        }
        else if (type == ConnectivityManager.TYPE_MOBILE)
        {
            switch (subType)
            {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    return false;
                default:
                    return false;
            }
        }
        else
        {
            return false;
        }
    }

    public static int getNetStatus(Context context)
    {
        // [网络状况.0:未定义网络状况，1:2G,2:3G,3:WIFI,4:4G]
        int netStatus = 0;// 默认未定义

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = cm.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected())
        {
            int type = activeInfo.getType();

            switch (type)
            {
                case ConnectivityManager.TYPE_WIFI:
                case ConnectivityManager.TYPE_ETHERNET:
                    netStatus = 3;
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    switch (activeInfo.getSubtype())
                    {
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            netStatus = 1;
                            break;
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            netStatus = 2;
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            netStatus = 4;
                            break;
                        default:
                            netStatus = 2;
                    }
                    break;
                case ConnectivityManager.TYPE_WIMAX:
                    netStatus = 4;
                    break;
                default:
                    netStatus = 0;// 默认未定义
                    break;
            }
        }
        return netStatus;
    }

    public static String getUUID(Activity activity)
    {
        String uuid = "";
        try
        {
            final TelephonyManager tm = (TelephonyManager) activity.getBaseContext().getSystemService(
                    Context.TELEPHONY_SERVICE);
            final String tmDevice, tmSerial, tmPhone, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = ""
                    + android.provider.Settings.Secure.getString(activity.getContentResolver(),
                            android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());

            uuid = "android:" + deviceUuid.toString();
        }
        catch (Exception e)
        {

        }

        return uuid;
    }

    public static String getImsi(Context context)
    {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String subid = tm.getSubscriberId();
        if (subid == null)
        {
            subid = "";
        }
        return subid;
    }

    /**
     * 获取设备id (该方法已废弃，获取设备的唯一标识 请使用方法 getPutaoDeviceId(),获取imei号请使用方法 getImei())
     * 
     * @deprecated
     * @param context
     * @return
     */
    public static String getDeviceId(Context context)
    {
        /**
         * 由于山寨机的存在，imei号存在被盗用的可能，imei不能保整设备的唯一性，所以不再使用imei作为设备号 old
         * code：getImei TelephonyManager tm = (TelephonyManager)
         * context.getSystemService(Context.TELEPHONY_SERVICE); String devid =
         * tm.getDeviceId(); if(null == devid) { // add by cj 2015/04/07 devid =
         * ""; } return devid;
         */
        return getPutaoDeviceId(context);
    }

    /**
     * 
     * 获取imei号
     * 
     * @param context
     * @return String imei号
     */
    public static String getImei(Context context)
    {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String devid = tm.getDeviceId();
        if (null == devid)
        {
            devid = "";
        }
        return devid;
    }

    public static String getChannelNo(Context context)
    {
        try
        {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            return ai.metaData.get("UMENG_CHANNEL").toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatNumber2KW(int number)
    {
        String result = number + "";
        if (number > 10000)
        {
            result = number / 10000 + "W+";
        }
        else if (number > 1000)
        {
            result = number / 1000 + "K+";
        }
        return result;
    }

    /**
     * 新版获取设备唯一标识 使用IMEI+base64(MAC)
     * 
     * @author change
     * @param context
     * @return
     */
    public static String getDeviceId2(Context context)
    {
        String device_id = "";
        try
        {
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            String mac = wifi.getConnectionInfo().getMacAddress();
            if (!TextUtils.isEmpty(mac))
            {
                // device_id += ConvUtil.bytes2HexStr(mac.getBytes());
                device_id += ConvUtil.convertStrToBase64String(mac);
            }

            /*
             * if (TextUtils.isEmpty(device_id)) { device_id =
             * android.provider.Settings.Secure.getString(
             * context.getContentResolver(),
             * android.provider.Settings.Secure.ANDROID_ID); }
             */
            // if (!TextUtils.isEmpty(device_id)) {
            // //去掉可能产生的换行符add by lxh 2015-3-14
            // device_id=device_id.replace("\n", "");
            // }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return device_id;
    }

    /**
     * 获取ptDeviceId作为用户唯一标识 （MD5(imei+mac+androidId)） add by hyl 2015-6-12
     * 
     * @param context
     * @return
     */
    public static String getPutaoDeviceId(Context context)
    {
        if (!TextUtils.isEmpty(sPtDeviceId))
        {
            return sPtDeviceId;
        }
        // 从SD卡中获取ptcid
        String ptDeviceId = getPtDeviceIdBySDCrad();
        sPtDeviceId = ptDeviceId;
        if (TextUtils.isEmpty(ptDeviceId))
        {
            /*
             * 
             * 多个线程访问时出现数据不同步的问题 add by lxh 2015-7-2 start old code: ptDeviceId
             * = MD5.toMD5(imeiMacAndroidId);
             * savePtDeviceIdToSDCard(ptDeviceId);
             */
            String androidId = android.provider.Settings.Secure.getString(context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            // imei＋mac+androidId
            String imeiMacAndroidId = getImei(context) + getLocalMacAddress(context) + androidId;
            synchronized (SystemUtil.class)
            {
                ptDeviceId = getPtDeviceIdBySDCrad();
                if (TextUtils.isEmpty(ptDeviceId))
                {
                    ptDeviceId = MD5.toMD5(imeiMacAndroidId);
                    sPtDeviceId = ptDeviceId;
                    savePtDeviceIdToSDCard(ptDeviceId);
                }
            }
            // end 2015-7-2 by lxh
        }
        return ptDeviceId;
    }

    /**
     * 获取mac地址 add by hyl 2015-6-12
     * 
     * @param context
     * @return
     */
    public static String getLocalMacAddress(Context context)
    {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String mac = info.getMacAddress();
        if (null == mac)
        { // add by cj 2015/04/07
            mac = "";
        }
        return mac;
    }

    /**
     * 保存ptDeviceId到SDCard add by hyl 2015-6-12
     * 
     * @param ptDeviceId
     */
    private static void savePtDeviceIdToSDCard(String ptDeviceId)
    {
        File fileDir = new File(Environment.getExternalStorageDirectory() + "/pt_system");
        if (!fileDir.exists())
            fileDir.mkdirs();
        File cidFile = new File(fileDir, PT_DEVICE_ID_FILENAME);
        if (!cidFile.exists())
        {
            try
            {
                cidFile.createNewFile();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        FileOutputStream foos = null;
        try
        {
            foos = new FileOutputStream(cidFile, false);
            foos.write(ptDeviceId.toString().getBytes());
            foos.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (foos != null)
            {
                try
                {
                    foos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                foos = null;
            }
        }
    }

    /**
     * 从SD卡中读取ptDeviceId add by hyl 2015-6-12
     * 
     * @return
     */
    private static String getPtDeviceIdBySDCrad()
    {
        File fileDir = null;
        try
        {
            fileDir = new File(Environment.getExternalStorageDirectory(), "/pt_system");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        File cidFile = new File(fileDir, PT_DEVICE_ID_FILENAME);
        InputStreamReader inputReader = null;
        BufferedReader bufferReader = null;
        StringBuffer strBuffer = new StringBuffer();
        try
        {
            InputStream inputStream = new FileInputStream(cidFile);
            inputReader = new InputStreamReader(inputStream);
            bufferReader = new BufferedReader(inputReader);
            String line = null;
            while ((line = bufferReader.readLine()) != null)
            {
                strBuffer.append(line);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (inputReader != null)
                {
                    inputReader.close();
                }
                if (bufferReader != null)
                {
                    bufferReader.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            inputReader = null;
            bufferReader = null;
        }
        return strBuffer.toString();
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     * 
     * @return boolean
     */
    public static boolean hasSdcard()
    {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}