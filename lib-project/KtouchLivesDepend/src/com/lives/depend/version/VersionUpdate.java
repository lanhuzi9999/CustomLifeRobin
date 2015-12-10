package com.lives.depend.version;

import android.content.Context;

/**
 * ****************************************************************
 * 文件名称 : VersionUpdate.java
 * 作 者 :   hyl
 * 创建时间 : 2015-11-23 下午4:25:49
 * 文件描述 : 版本更新接口类，定义版本更新所使用到的接口方法
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-23 1.00 初始版本
 *****************************************************************
 */
interface VersionUpdate
{
    /**
     * 自动检查更新
     * @param context
     */
    void autoCheckUpdate(Context context);
    
    /**
     * 手动检查更新
     * @param context
     */
    void manualCheckUpdate(Context context);
}
