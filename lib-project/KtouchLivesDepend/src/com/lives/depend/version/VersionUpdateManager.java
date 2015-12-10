package com.lives.depend.version;

import android.content.Context;

/**
 * ****************************************************************
 * 文件名称 : VersionUpdateManager.java
 * 作 者 :   hyl
 * 创建时间 : 2015-11-17 下午3:28:31
 * 文件描述 : 版本检查更新管理类，对外暴露
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-17 1.00 初始版本
 *****************************************************************
 */
public class VersionUpdateManager
{
    private static VersionUpdateManager manager;
    private VersionUpdate versionUpdate;
    
    private VersionUpdateManager()
    {
        versionUpdate = new PtVersionUpdate();
    }

    public static VersionUpdateManager getInstance()
    {
        if(manager == null)
        {
            manager = new VersionUpdateManager();
        }
        return manager;
    }

    /**
     * 自动检查版本更新
     * void
     */
    public void autoCheckUpdate(Context context)
    {
        versionUpdate.autoCheckUpdate(context);
    }

    /**
     * 手动检查版本更新
     * void
     */
    public void manualCheckUpdate(Context context)
    {
        versionUpdate.manualCheckUpdate(context);
    }
}
