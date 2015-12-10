package com.lives.depend.version;

/**
 * ****************************************************************
 * 文件名称 : DownloadStatus.java
 * 作 者 :   hyl
 * 创建时间 : 2015-11-23 下午4:14:03
 * 文件描述 : 下载状态对象
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-23 1.00 初始版本
 *****************************************************************
 */
public class DownloadStatus {

	public int status = -1;  // see DownloadManager.STATUS_SUCCESSFUL
	public long id = -1;
	public long bytesSoFar;
	public long totalSize;
	public String localUri;
	public String localPath;
	public String reason;

}
