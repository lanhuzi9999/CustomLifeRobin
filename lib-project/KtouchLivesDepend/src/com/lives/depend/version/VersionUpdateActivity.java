package com.lives.depend.version;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import com.lives.depend.R;
import com.lives.depend.theme.dialog.CommonDialog;
import com.lives.depend.theme.dialog.CommonDialogFactory;
import com.lives.depend.theme.dialog.progress.AbstractProgressDialog;

/**
 * **************************************************************** 
 * 文件名称 :VersionUpdateActivity.java 
 * 作 者 : hyl 
 * 创建时间 : 2015-11-23 下午3:00:05 
 * 文件描述 :升级提示对话框Activity，提示用户当前有新版本需要升级 
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有 
 * 修改历史 : 2015-11-23 1.00 初始版本
 ***************************************************************** 
 */
public class VersionUpdateActivity extends Activity
{
    private static final String TAG = "VersionUpdateActivity";

    /**
     * 判断用户是否手动取消下载对话框
     */
    private boolean isUserCancle = false;

    SharedPreferences preferences = null;

    private CommonDialog versionUpdateDialog = null;

    boolean isForceRemind = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        preferences = getSharedPreferences("version_sertting", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        if (intent != null)
        {
            String versionInfo = intent.getStringExtra("versionInfo");
            showVersionUpdateDialog(versionInfo);
        }
    }

    /**
     * 显示版本更新对话框
     * 
     * @param versionInfo 新版本信息
     */
    private void showVersionUpdateDialog(final String versionInfo)
    {
        try
        {
            if(versionUpdateDialog != null && versionUpdateDialog.isShowing())
            {
                return ;
            }
            
            JSONObject versionJson = new JSONObject(versionInfo);
            String remark = versionJson.getString("remark");
            final String down_url = versionJson.getString("down_url");
            final long totalSize = versionJson.getLong("size");
            final String version = versionJson.getString("version");

            int enforce = versionJson.getInt("enforce");
            if (enforce == VersionUtil.VERSION_CHECK_STATE_ENFORCE)
            {
                isForceRemind = true;
            }
            
            if(DownloadUtils.isDownloading())
            {
                // 开始下载
                startDownloadApp(version, down_url, totalSize);
                return;
            }
            
            if (versionUpdateDialog == null)
            {
                
               
                if (isForceRemind)
                {
                    versionUpdateDialog = CommonDialogFactory.getDialog(this, R.style.Theme_Ptui_Dialog_Ok);

                }
                else
                {
                    versionUpdateDialog = CommonDialogFactory.getDialog(this, R.style.Theme_Ptui_Dialog_OkCancel);
                    versionUpdateDialog.setNegativeButton(R.string.putao_vupdate_check_cancel_update,
                            new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View view)
                                {
                                    // 用户选择了'以后再说'
                                    versionUpdateDialog.dismiss();
                                    finish();
                                }
                            });
                }
                
                versionUpdateDialog.setTitle(R.string.putao_vupdate_check_title);
                versionUpdateDialog.setMessage(remark);
                versionUpdateDialog.setCancelable(false);
                versionUpdateDialog.setPositiveButton(R.string.putao_vupdate_check_confirm_update, new View.OnClickListener()
                {

                    @Override
                    public void onClick(View view)
                    {
                        // TODO Auto-generated method stub
                        // 获取本地保存的上次已下载的文件目录和版本
                        String lastVersion = preferences.getString("version", "");
                        String storePath = preferences.getString("localPath", "");
                        File file = new File(storePath);
                        if (file.exists() && lastVersion.equals(version)) // 上次下载的文件已存在并且版本和线上版本一致，则直接启动安装
                        {
                            VersionUpdateHelper.getInstance().installApk(VersionUpdateActivity.this, storePath);
                            if (!isForceRemind)
                            {
                                versionUpdateDialog.dismiss();
                                finish();
                            }
                        }
                        else
                        {
                            versionUpdateDialog.dismiss();
                            // 开始下载
                            startDownloadApp(version, down_url, totalSize);
                        }
                    }
                });
            }
            versionUpdateDialog.show();
        }
        catch (JSONException e)
        {
            Log.w(TAG, "catch Exception JSONException throw by handleMessage.",e);
            finish();
        }
    }

    /**
     * 开始下载App
     * 
     * @param version 下载的app版本
     * @param downUrl 下载的url
     * @param totalSize 下载文件的大小 void
     */
    protected void startDownloadApp(final String version, String downUrl, long totalSize)
    {
        // 显示下载进度对话框
        showProgressDialog(0, totalSize);

        // 开始下载
        VersionUpdateHelper.getInstance().startDownload(VersionUpdateActivity.this, downUrl, new DownloadHandler()
        {
            @Override
            public void doDownloadProgress(long totalSize, long progressSize)
            {
                showProgressDialog(progressSize, totalSize);// 更新下载进度
            }

            @Override
            public void doDownloadSuccess(String path, String uri)
            {
                dismissProgressDialog();
                // 下载完成，保存文件下载目录
                preferences.edit().putString("localPath", path).commit();
                preferences.edit().putString("version", version).commit();
                
                // 下载完成，开始安装
                VersionUpdateHelper.getInstance().installApk(VersionUpdateActivity.this, path);
            }

            @Override
            public void doDownloadFail(String reason)
            {
                dismissProgressDialog();
                // 下载失败，提示用户重新下载
                Toast.makeText(VersionUpdateActivity.this, R.string.putao_vupdate_download_error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private AbstractProgressDialog mProgressDialog = null;

    protected void showProgressDialog(long progressSize, long totalSize)
    {
        Log.i(TAG, "progressSize:" + progressSize + " totalSize:" + totalSize);
        if (isFinishing() || isUserCancle)
        {
            return;
        }

        if (mProgressDialog == null)
        {
            mProgressDialog = CommonDialogFactory.getProgressDialog(this, R.style.Theme_Ptui_Dialog_Progress_Download);
            
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setMax(100);
            mProgressDialog.setTitle(R.string.putao_vupdate_downloading);
            mProgressDialog.setCanceledOnTouchOutside(!isForceRemind);
            mProgressDialog.setCancelable(!isForceRemind);
            if(!isForceRemind)
            {
                String cancelText = getString(R.string.putao_vupdate_cancel);
                mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,cancelText, new OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        isUserCancle = true;
                        dialog.dismiss();
                        VersionUpdateHelper.getInstance().cancelDownload(VersionUpdateActivity.this);
                    }
                }); 
            
            }
            mProgressDialog.setOnDismissListener(new OnDismissListener()
            {

                @Override
                public void onDismiss(DialogInterface dialog)
                {
                    isUserCancle = true;
                    finish();
                }
            });
        }
        int progress = (int) ((float) progressSize / totalSize * 100);
        mProgressDialog.setProgress(progress);
        if (!mProgressDialog.isShowing())
        {
            mProgressDialog.show();
        }
    }

    private void dismissProgressDialog()
    {
        if (mProgressDialog != null)
        {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (isForceRemind)
            {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(DownloadUtils.isDownloading())
        {
            return ;
        }
        if(versionUpdateDialog != null && !versionUpdateDialog.isShowing())
        {
           versionUpdateDialog.show();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        dismissProgressDialog();
        if (isForceRemind)
        {
            // ActivityMgr.getInstance().finishAllActivity();
            Process.killProcess(Process.myPid());// 退出应用
        }
    }
}
