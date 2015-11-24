package so.contacts.hub.basefunction.imageloader;

import android.view.View;

/**************************************************
 * <br>文件名称: DataLoaderListener.java
 * <br>版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * <br>创建人员: wcy
 * <br>文件描述: 数据加载监听器 用于回调处理异步获取到的数据，填充数据到且更新多个View的方式
 * <br>修改时间: 2015-7-6 上午11:41:30
 * <br>修改历史: 2015-7-6 1.00 初始版本
 **************************************************/
public interface DataLoaderListener
{

    /**
     * 将下载对象数据回调出去处理
     * @param result
     * @param view
     * void
     */
    void fillDataInView(Object result, View view);

}
