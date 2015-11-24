/**
 * 
 */
package so.contacts.hub.basefunction.imageloader;

import android.content.Context;

/**************************************************
 * <br>文件名称: DataLoaderFactory.java
 * <br>版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * <br>创建人员: wcy
 * <br>文件描述: 生产业务图片加载loader的抽象工厂类 
 * <br>修改时间: 2015-7-6 上午11:41:05
 * <br>修改历史: 2015-7-6 1.00 初始版本
 **************************************************/
public abstract class DataLoaderFactory
{

    protected Context context;

    public DataLoaderFactory(Context context)
    {
        this.context = context;
    }

    public abstract DataLoader getDataLoader(boolean defaultLoader);

    public abstract DataLoader getDataLoader(boolean defaultLoader, DataCache.DataCacheParams cacheParams,
            DataLoaderParams loaderParams);

}
