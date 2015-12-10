package com.lives.depend.version;

/**
 * ****************************************************************<br>
 * 文件名称 : VersionUtil.java<br>
 * 作 者 : zjh<br>
 * 创建时间 : 2015-11-27 下午9:27:41<br>
 * 文件描述 : 版本升级工具类<br>
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-27 1.00 初始版本<br>
 ***************************************************************** 
 */
public class VersionUtil
{

    /**
     * 检查升级状态：自动检测升级
     */
    public static int VERSION_CHECK_ACTION_AUTO = 1;

    /**
     * 检查升级状态：手动检测升级
     */
    public static int VERSION_CHECK_ACTION_NOT_AUTO = 0;

    /**
     * 强制升级 value = 1
     */
    public static int VERSION_CHECK_STATE_ENFORCE = 1;

}
