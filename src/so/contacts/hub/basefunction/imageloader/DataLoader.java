package so.contacts.hub.basefunction.imageloader;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import so.contacts.hub.basefunction.imageloader.DataCache.DataCacheParams;
import so.contacts.hub.basefunction.imageloader.image.ImageLoaderParams;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;


/**************************************************
 * <br>文件名称: DataLoader.java
 * <br>版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * <br>创建人员: wcy
 * <br>文件描述: 图片加载基类
 * <br>修改时间: 2015-7-6 上午11:40:36
 * <br>修改历史: 2015-7-6 1.00 初始版本
 **************************************************/

public abstract class DataLoader
{

    protected Context context;

    /**
     * 缓存对象
     */
    protected DataCache mDataCache;

    /**
     * 控制挂后台onPause的时候停止去加载数据
     */
    private boolean mExitTasksEarly = false;

    /**
     * 控制listview或者gridview滚动停止的的时候采取加载数据，滚动过程中不加载数据，避免卡顿
     */
    protected boolean mPauseWork = false;

    /**
     * 同步控制变量
     */
    private final Object mPauseWorkLock = new Object();

    /**
     * 下载完成后回调接口
     */
    private DataLoaderListener mLoaderListener;

    /**
     * 加载参数
     */
    protected ImageLoaderParams mLoaderParams;

    /**
     * 构建线程工厂
     */
    private static final ThreadFactory sThreadFactory = new ThreadFactory()
    {

        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r)
        {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };

    /**
     * 构建固定大小的线程池
     */
    public static final Executor DUAL_THREAD_EXECUTOR = Executors.newFixedThreadPool(5, sThreadFactory);

    public DataLoader(Context context)
    {
        this.context = context;
    }

    /**
     * 根据缓存参数(uniqueName)去获取缓存对象,
     * 
     * @author wcy
     * @since 2015-5-29
     * @param cacheParams
     */
    public void setDataCache(DataCacheParams cacheParams)
    {
        mDataCache = DataCache.findOrCreateCache(cacheParams);
    }

    public DataCache getDataCache()
    {
        return mDataCache;
    }

    public void setDataLoaderParams(DataLoaderParams loaderParams)
    {
        mLoaderParams = (ImageLoaderParams) loaderParams;
    }

    public DataLoaderParams getDataLoaderParams()
    {
        return mLoaderParams;
    }

    /**
     * 图片加载方法(不带回调接口)
     * 
     * @param data The URL of the image to download, or The Id of the contact to
     * fetch.
     * @param view The View to bind the process data to.
     */
    public void loadData(Object data, View view)
    {
        loadData(data, view, null);
    }

    /**
     * 图片加载方法(带回调接口)
     * 
     * @param data The URL of the image to download, or The Id of the contact to
     * fetch.
     * @param view The View to bind the process data to.
     * @param listener a callback to fill data in view
     */
    public void loadData(Object data, View view, DataLoaderListener listener)
    {
        if (data == null)
        {
            return;
        }
        mLoaderListener = listener;
        // 如果在缓存中命中，即数据已经存在在缓存中
        Object result = hitInCache(data);
        if (result != null)
        {
            // Result found in memory cache
            if (mLoaderListener == null)
            {
                fillDataInView(result, view);
            }
            else
            {
                // 多个视图的更新，复杂性和重用性不高，通过数据加载监听器回调Adapter中的数据填充和更新视图
                mLoaderListener.fillDataInView(result, view);
            }

        }
        else if (cancelPotentialWork(data, view))
        {
            final LoaderTask task = createLoaderTask(view);
            task.executeOnExecutor(DUAL_THREAD_EXECUTOR, data);
        }
    }

    /**
     * 找到图像时不做其他动作直接返回Bitmap
     * 
     * @param data
     * @param view
     * @return Bitmap
     */
    public Bitmap loadDataReturnBitmap(Object data, View view)
    {
        if (data == null)
        {
            return null;
        }
        mLoaderListener = null;
        // 如果在缓存中命中，即数据已经存在在缓存中
        Object result = hitInCache(data);
        if (result != null)
        {
            return (Bitmap) result;
        }
        else if (cancelPotentialWork(data, view))
        {
            final LoaderTask task = createLoaderTask(view);
            task.executeOnExecutor(DUAL_THREAD_EXECUTOR, data);
        }
        return null;
    }

    /**
     * Get from memory cache.
     * 
     * @param data Unique identifier for which item to get
     * @return The result if found in cache, null otherwise
     */
    protected Object hitInCache(Object data)
    {
        Object cachedResult = null;
        if (mDataCache != null)
        {
            cachedResult = mDataCache.getResultFromCache(String.valueOf(data));
        }
        return cachedResult;
    }

    /**
     * Returns true if the current work has been canceled or if there was no
     * work in progress on this image view. Returns false if the work in
     * progress deals with the same data. The work is not stopped in that case.
     */
    public boolean cancelPotentialWork(Object data, View view)
    {
        final LoaderTask task = getLoaderTask(view);

        if (task != null)
        {
            final Object requestData = task.data;
            if (requestData == null || !requestData.equals(data))
            {
                task.cancel(true);
            }
            else
            {
                // The same work is already in progress.
                return false;
            }
        }
        return true;
    }

    /**
     * The actual AsyncTask that will asynchronously process the data.
     */
    protected class LoaderTask extends AsyncTask<Object, Void, Object>
    {

        private Object data;

        private final WeakReference<View> viewReference;

        public LoaderTask(View view)
        {
            viewReference = new WeakReference<View>(view);
        }

        /**
         * Background processing.
         */
        @Override
        protected Object doInBackground(final Object... params)
        {
            data = params[0];

            // Wait here if work is paused and the task is not cancelled
            synchronized (mPauseWorkLock)
            {
                while (mPauseWork && !isCancelled())
                {
                    try
                    {
                        mPauseWorkLock.wait();
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            Object result = null;
            if (result == null && !isCancelled() && getAttachedView() != null && !mExitTasksEarly)
            {
                result = processData(data);
            }
            return result;
        }

        /**
         * Once the data is processed, associates it to the view
         */
        @Override
        protected void onPostExecute(Object result)
        {
            //add by xcx 2015-08-25 start mDataCache==null 证明已经执行了clearCache，不再需要将图片再添加到cache中
            if(mDataCache==null)
            {
                return;
            }
            //add by xcx 2015-08-25 end mDataCache==null 证明已经执行了clearCache，不再需要将图片再添加到cache中
            
            
            //modify by xcx 2015-08-25 start 将添加对象的操作放在这里，可以避免多线程操作 mDataCache
            if (result != null)
            {
                String dataString = String.valueOf(data);
                mDataCache.addDataToCache(dataString, result);
            }
            //modify by xcx 2015-08-25 end 将添加对象的操作放在这里，可以避免多线程操作 mDataCache
            
            if (isCancelled() || mExitTasksEarly)
            {
                result = null;
            }
            
            final View view = getAttachedView();
            if(view != null)
            {
                
                // deleted by xcx 2015-08-25 start 无用代码，存在内存泄露隐患
//                if (result instanceof Bitmap)
//                {
//                    view.setTag(result);
//                }
                // deleted by xcx 2015-08-25 end 无用代码，存在内存泄露隐患
                
                if (mLoaderListener == null)
                {
                    fillDataInView(result, view);
                }
                else
                {
                    // 多个视图的更新，复杂性和重用性不高，通过数据加载监听器回调Adapter中的数据填充和更新视图
                    mLoaderListener.fillDataInView(result, view);
                }
            }
            
        }

        @Override
        protected void onCancelled(Object result)
        {
            super.onCancelled(result);
            synchronized (mPauseWorkLock)
            {
                mPauseWorkLock.notifyAll();
            }
        }

        /**
         * Returns the View associated with this task as long as the View task
         * still points to this task as well. Returns null otherwise.
         */
        private View getAttachedView()
        {
            final View view = viewReference.get();
            final LoaderTask task = getLoaderTask(view);
            if (this == task)
            {
                return view;
            }
            return null;
        }
    }

    /**
     * @param view Any View
     * @return Returns a new loader task associated with this View. modified by
     * wcy 2015-5-29 抽离出公有部分,延迟到子类去实现
     */
    protected abstract LoaderTask createLoaderTask(View view);

    /**
     * @param view Any View
     * @return Retrieve the currently active loader task (if any) associated
     * with this View. null if there is no such task. modified by wcy 2015-5-29
     * 抽离出公有部分，延迟到子类去实现
     */
    protected abstract LoaderTask getLoaderTask(View view);

    /**
     * The main process method, which will be called by the ImageWorker in the
     * AsyncTask background thread.
     * 
     * @param data The data to load the result, in this case, a regular http URL
     * or id
     * @return The downloaded and resized bitmap, or Object
     */
    public abstract Object processData(Object data);

    /**
     * Called when the processing is complete and the final result should be set
     * on the View.
     * 
     * @param view
     * @param bitmap
     */
    protected abstract void fillDataInView(Object result, View view);

    /**
     * 支持对多种view的适配
     * 
     * @param result
     * @param view
     */
    protected abstract void fillDataWithView(Object result, View view);

    /**
     * Cancels any pending loader attached to the provided View.
     * 
     * @param view
     */
    public void cancelWork(View view)
    {
        final LoaderTask loaderTask = getLoaderTask(view);
        if (loaderTask != null)
        {
            loaderTask.cancel(true);
        }
    }

    /**
     * 控制是否加载数据
     * 
     * @author wcy
     * @since 2015-5-29
     * @param pauseWork
     */
    public void setPauseWork(boolean pauseWork)
    {
        synchronized (mPauseWorkLock)
        {
            mPauseWork = pauseWork;
            // false 状态则唤醒线程去处理图片的下载
            if (!mPauseWork)
            {
                mPauseWorkLock.notifyAll();
            }
        }
    }

    /**
     * 来控制是否在后台下载和加载图片
     * 
     * @param exitTasksEarly 当为true时，就停止在后台下载和加载图片，为false时可以在后台下载和加载图片
     */
    public void setExitTasksEarly(boolean exitTasksEarly)
    {
        mExitTasksEarly = exitTasksEarly;
    }

    /**
     * 通过key删除缓存
     */
    public void removeCache(Object data)
    {
        if (mDataCache != null)
        {
            mDataCache.removeCache(String.valueOf(data));
        }
    }

    /**
     * 清除缓存数据
     * 
     * @author wcy
     * @since 2015-5-28
     */
    public void clearCache()
    {
        if (mDataCache != null)
        {
            mDataCache.clearCache();
            mDataCache=null;
        }
        //modify by xcx 2015-08-25 start 置null 可以避免cache 销毁后，图片后台下载完成后还继续通过接口回调设置界面显示
        mLoaderListener=null;
        //modify by xcx 2015-08-25 end 置null 可以避免cache 销毁后，图片后台下载完成后还继续通过接口回调设置界面显示
        clearLoaderParams();
    }

    /**
     * 清除默认加载图片数据 added by wcy 2015-4-23
     */
    public void clearLoaderParams()
    {
        if (mLoaderParams != null && mLoaderParams.mLoadingBitmap != null && !mLoaderParams.mLoadingBitmap.isRecycled())
        {
            mLoaderParams.mLoadingBitmap.recycle();
            mLoaderParams.mLoadingBitmap = null;
        }
    }
}
