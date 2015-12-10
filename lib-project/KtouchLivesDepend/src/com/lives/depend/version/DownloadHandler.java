package com.lives.depend.version;

import android.os.Handler;
/**
 * ****************************************************************
 * 文件名称 : DownloadHandler.java
 * 作 者 :   hyl
 * 创建时间 : 2015-11-23 下午4:14:44
 * 文件描述 : 
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-23 1.00 初始版本
 *****************************************************************
 */
public abstract class DownloadHandler extends Handler
{

    public abstract void doDownloadProgress(long totalSize,long progressSize);
    
    public abstract void doDownloadSuccess(String path,String uri);
    
    public abstract void doDownloadFail(String reason);
    
}
