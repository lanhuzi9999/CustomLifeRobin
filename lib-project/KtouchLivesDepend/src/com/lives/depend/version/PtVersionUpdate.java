package com.lives.depend.version;

import org.json.JSONException;
import org.json.JSONObject;

import com.lives.depend.R;
import com.lives.depend.theme.dialog.CommonDialogFactory;
import com.lives.depend.theme.dialog.progress.AbstractProgressDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * ****************************************************************
 * 文件名称 : PtVersionUpdate.java
 * 作 者 :   hyl
 * 创建时间 : 2015-11-23 下午4:23:26
 * 文件描述 : 葡萄升级模块实现类，处理葡萄对应的逻辑
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-23 1.00 初始版本
 *****************************************************************
 */
class PtVersionUpdate implements VersionUpdate
{
    private static final String TAG = "PtVersionUpdate";

    private Context mContext;

    private static final String path = "http://api.putao.so/sandroid1/PT_SERVER/interface.s";

    @Override
    public void autoCheckUpdate(Context context)
    {
        mContext = context;
        VersionUpdateHelper.getInstance().checkUpdate(context, path, true, versionHandler);
    }

    @Override
    public void manualCheckUpdate(Context context)
    {
        mContext = context;
        //显示加载框
        showProgressDialog(context);
        // 开始检查更新
        VersionUpdateHelper.getInstance().checkUpdate(context, path, false, versionHandler);
    }

    private AbstractProgressDialog mProgressDialog = null;

    private void showProgressDialog(Context context)
    {
        mProgressDialog = CommonDialogFactory.getProgressDialog(context, R.style.Theme_Ptui_Dialog_Progress);
        mProgressDialog.setMessage(R.string.putao_vupdate_checking);
        mProgressDialog.show();
    }

    private void dismissProgressDialog()
    {
        if (mProgressDialog != null)
        {
            mProgressDialog.dismiss();
        }
    }

    private Handler versionHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            dismissProgressDialog();
            switch (msg.what)
            {
                case VersionUpdateHelper.MSG_SUCCESS:// 检测到有新的版本需要更新
                    checkSuccess(msg, msg.arg1);
                    break;
                case VersionUpdateHelper.MSG_FAIL:// 检查更新失败
                    checkFail(msg);
                    break;
                default:
                    break;
            }
        }

        /**
         * 检查更新失败
         * @param msg 
         */
        private void checkFail(Message msg)
        {
            int errorCode = msg.arg1;
            if (errorCode == VersionUpdateHelper.FAILED_REASON_SERVER_ERROR)
            {
                // 服务器异常
                String errorMsg = (String) msg.obj;
                String errorText = mContext.getString(R.string.putao_vupdate_check_server_error) +":"+errorMsg;
                Toast.makeText(mContext, errorText, Toast.LENGTH_SHORT).show();
            }
            else if (errorCode == VersionUpdateHelper.FAILED_REASON_NOTFOUND)
            {
                // 没有新版本需要更新
                Toast.makeText(mContext, R.string.putao_vupdate_check_no_new_version, Toast.LENGTH_SHORT)
                        .show();
            }
            else if (errorCode == VersionUpdateHelper.FAILED_REASON_NETWORK)
            {
                // 网络异常
                Toast.makeText(mContext, R.string.putao_vupdate_check_error, Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * 检查更新成功
         * @param msg
         */
        private void checkSuccess(Message msg, int checkAction)
        {
            String versionInfo = (String) msg.obj;
            Log.i(TAG, "suceess :" + versionInfo);
            JSONObject jsonObject;
            try
            {
                jsonObject = new JSONObject(versionInfo);
                int enforce = jsonObject.getInt("enforce");
                // 两种情况可以提示框更新1.强制更新；2.到了该提示的点了
                if (checkAction == VersionUtil.VERSION_CHECK_ACTION_AUTO)
                {
                    if (enforce == VersionUtil.VERSION_CHECK_STATE_ENFORCE || shouldPromptUpdate(mContext))
                    {
                        savePromptTime(mContext);// 保 最近一次弹框更新的时间戳
                        Intent intent = new Intent(mContext, VersionUpdateActivity.class);
                        intent.putExtra("versionInfo", versionInfo);
                        mContext.startActivity(intent);
                    }
                }
                else
                {
                    savePromptTime(mContext);// 保 最近一次弹框更新的时间戳
                    Intent intent = new Intent(mContext, VersionUpdateActivity.class);
                    intent.putExtra("versionInfo", versionInfo);
                    mContext.startActivity(intent);
                }
            }
            catch (JSONException e)
            {
                Log.w(TAG, "catch Exception JSONException throw by handleMessage.", e);
            }
        }

    };

    /**
     * 检查是否需要提醒用户升级(距离上次检查时间24小时内不提醒)
     * 
     * @param context
     * @return boolean
     */
    private boolean shouldPromptUpdate(Context context)
    {
        // 保存最新检查更新时间
        SharedPreferences setting = context.getSharedPreferences("version_setting", Context.MODE_PRIVATE);
        long lastCheckUpdate = setting.getLong("lastCheckUpdate", 0);
        long current = System.currentTimeMillis();
        return (current - lastCheckUpdate) >= 1 * 24 * 60 * 60 * 1000;
    }

    /**
     * 保存 最近一次弹框更新的时间戳
     * 
     * @param context
     */
    private void savePromptTime(Context context)
    {
        SharedPreferences setting = context.getSharedPreferences("version_setting", Context.MODE_PRIVATE);
        setting.edit().putLong("lastCheckUpdate", System.currentTimeMillis()).commit();
    }

}
