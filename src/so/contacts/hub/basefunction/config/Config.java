package so.contacts.hub.basefunction.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.gson.Gson;
import so.contacts.hub.ContactsApp;
import so.contacts.hub.basefunction.storage.db.DatabaseHelper;

public class Config
{
    public static final String KEY = "233&*Adc^%$$per";
    public static int STATE = 1;
    /**
     * 服务器域名
     */
    public static final String SERVER_HOST = "http://api.putao.so";
    
    /**
     * CMS请求地址前缀
     */
    public static final String NEW_CMS_SERVER_HOST = SERVER_HOST+"/scmsface";
    
    public static Gson mGson = new Gson();
    
    private static DatabaseHelper mDatabaseHelper;

    public static DatabaseHelper getDatabaseHelper()
    {
        if (mDatabaseHelper == null)
        {
            mDatabaseHelper = new DatabaseHelper(ContactsApp.getInstance());
        }
        return mDatabaseHelper;
    }
    
    public static void execute(Runnable runnable)
    {
        getExecutor().execute(runnable);
    }
    
    /**
     * 线程池，线程数量控制在10个，防止线程堵车，请求等待时间过长
     */
    private static final int CORE_POOL_SIZE = 10;

    private static ExecutorService mExecutorService;

    private static ThreadFactory mThreadFactory = new ThreadFactory()
    {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r)
        {
            return new Thread(r, "putao thread #" + mCount.getAndIncrement());
        }
    };

    public static ExecutorService getExecutor()
    {
        if (mExecutorService == null)
        {
            mExecutorService = Executors.newFixedThreadPool(CORE_POOL_SIZE, mThreadFactory);
        }
        return mExecutorService;
    }
}
