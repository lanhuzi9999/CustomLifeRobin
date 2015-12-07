package so.contacts.hub.basefunction.storage.db;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import so.contacts.hub.basefunction.config.Config;
import so.contacts.hub.basefunction.operate.cms.bean.CMSResponseBaseData;
import so.contacts.hub.basefunction.operate.cms.bean.CommonView;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import com.putao.live.R;

public class CMSDataDB
{
    private static final String PackageName = "CMSDataDB";

    SQLiteDatabase dataBase;

    public CMSDataDB(DatabaseHelper helper)
    {
        dataBase = helper.getWritableDatabase();
    }

    public SQLiteDatabase getCMSDataDB()
    {
        return dataBase;
    }

    public static class CommonServicesTable implements BaseColumns
    {
        public static final Uri URI = Uri.parse("content://" + PackageName + ".db.common.services");

        public static final String TABLE_NAME = "common_services";

        public static final String DATA_VERSION = "data_version";

        public static final String DATA = "data";

        public static final String UPDATE_TIME = "update_time";

        public static final String EXPAND_DATA = "expand_data";
    }

    public static class ContentConfigTable implements BaseColumns
    {
        public static final Uri URI = Uri.parse("content://" + PackageName + ".db.content.config");

        public static final String TABLE_NAME = "content_config";

        public static final String DATA_VERSION = "data_version";

        public static final String DATA = "data";
    }
    
    public static String getCreateCommonServicesTableSQL()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ").append(CommonServicesTable.TABLE_NAME).append(" (")
                .append(CommonServicesTable._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(CommonServicesTable.DATA_VERSION).append(" INTEGER,").append(CommonServicesTable.DATA)
                .append(" TEXT,").append(CommonServicesTable.UPDATE_TIME).append(" LONG,")
                .append(CommonServicesTable.EXPAND_DATA).append(" TEXT").append("); ");

        return builder.toString();
    }

    public static String getCreateContentConfigTableSQL()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ").append(ContentConfigTable.TABLE_NAME).append(" (")
                .append(ContentConfigTable._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(ContentConfigTable.DATA_VERSION).append(" INTEGER,").append(ContentConfigTable.DATA)
                .append(" TEXT").append("); ");

        return builder.toString();
    }
    
    /**
     * 插入或更新常用服务数据
     */
    public void insertOrUpdateCommonServicesData(Context context, CMSResponseBaseData responseBaseData)
    {
        if (responseBaseData == null)
        {
            return;
        }
        if (!hasData(CommonServicesTable.TABLE_NAME))
        {
            insertCommonServicesData(context, responseBaseData);
        }
        else
        {
            updateCommonServicesData(context, responseBaseData);
        }
    }

    /**
     * 判断表中是否已经有数据 方法表述
     * 
     * @param tableName
     * @return boolean
     */
    private boolean hasData(String tableName)
    {
        boolean hasData = false;
        Cursor cursor = null;
        try
        {
            cursor = dataBase.query(tableName, null, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0)
            {
                hasData = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return hasData;
    }

    /**
     * 插入 常用服务数据
     */
    public long insertCommonServicesData(Context context, CMSResponseBaseData responseBaseData)
    {
        ContentValues values = new ContentValues();
        values.put(CommonServicesTable.DATA_VERSION, responseBaseData.getData_version());
        values.put(CommonServicesTable.DATA, responseBaseData.getData());
        values.put(CommonServicesTable.UPDATE_TIME, System.currentTimeMillis());
        values.put(CommonServicesTable.EXPAND_DATA, "");

        dataBase.delete(CommonServicesTable.TABLE_NAME, null, null);
        long id = dataBase.insert(CommonServicesTable.TABLE_NAME, null, values);

        context.getContentResolver().notifyChange(CommonServicesTable.URI, null);

        return id;
    }

    /**
     * 更新常用服务数据
     */

    public long updateCommonServicesData(Context context, CMSResponseBaseData responseBaseData)
    {
        ContentValues values = new ContentValues();
        values.put(CommonServicesTable.DATA_VERSION, responseBaseData.getData_version());
        values.put(CommonServicesTable.DATA, responseBaseData.getData());
        values.put(CommonServicesTable.UPDATE_TIME, System.currentTimeMillis());
        values.put(CommonServicesTable.EXPAND_DATA, "");
        long id = dataBase.update(CommonServicesTable.TABLE_NAME, values, null, null);

        context.getContentResolver().notifyChange(CommonServicesTable.URI, null);

        return id;
    }

    public List<CommonView> getCommonServicesList()
    {
        List<CommonView> commonServicesList = null;
        // 从数据库查询，CMSResponseBaseData
        CMSResponseBaseData commonServiesData = getCommonServicesData();
        // 转成List<CommonView>
        if (commonServiesData != null)
        {
            String dataStr = commonServiesData.getData();
            if (TextUtils.isEmpty(dataStr))
            {
                return null;
            }
            try
            {
                commonServicesList = Config.mGson.fromJson(dataStr, new TypeToken<List<CommonView>>()
                {
                }.getType());
            }
            catch (JsonSyntaxException e)
            {
                commonServicesList = null;
                e.printStackTrace();
            }
        }
        // 根据sort排序
        if (commonServicesList != null && commonServicesList.size() > 1)
        {
            Collections.sort(commonServicesList, new Comparator<CommonView>()
            {

                @Override
                public int compare(CommonView left, CommonView right)
                {
                    return left.getSort() - right.getSort();
                }
            });
        }
        
        return commonServicesList;
    }

    /**
     * 获取本地常用服务数据
     * @return
     * CMSResponseBaseData
     */
    public CMSResponseBaseData getCommonServicesData()
    {
        Cursor cursor = null;
        CMSResponseBaseData responseBaseData = null;

        try
        {
            cursor = dataBase.query(CommonServicesTable.TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null)
            {
                if (cursor.moveToNext())
                {
                    responseBaseData = new CMSResponseBaseData();
                    int dataVersion = cursor.getInt(cursor.getColumnIndex(CommonServicesTable.DATA_VERSION));
                    String data = cursor.getString(cursor.getColumnIndex(CommonServicesTable.DATA));
                    responseBaseData.setData_version(dataVersion);
                    responseBaseData.setData(data);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return responseBaseData;
    }
    
    /**
     * 获取本地商品配置流数据
     * @return
     * CMSResponseBaseData
     */
    public CMSResponseBaseData getContentConfigData()
    {
        CMSResponseBaseData responseData = null;
        Cursor cursor = null;
        try
        {
            cursor = dataBase.query(ContentConfigTable.TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null)
            {
                if (cursor.moveToNext())
                {
                    responseData = new CMSResponseBaseData();
                    int dataVersion = cursor.getInt(cursor.getColumnIndex(ContentConfigTable.DATA_VERSION));
                    String data = cursor.getString(cursor.getColumnIndex(ContentConfigTable.DATA));
                    responseData.setData_version(dataVersion);
                    responseData.setData(data);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return responseData;
    }

    /**
     * 插入商品流信息
     * @param context
     * @param responseData
     * @return
     * long
     */
    public long insertContentConfigData(Context context, CMSResponseBaseData responseData)
    {
        ContentValues values = new ContentValues();
        values.put(ContentConfigTable.DATA_VERSION, responseData.getData_version());
        values.put(ContentConfigTable.DATA, responseData.getData());
        long id = dataBase.insert(ContentConfigTable.TABLE_NAME, null, values);
        
        context.getContentResolver().notifyChange(ContentConfigTable.URI, null);
        return id;
    }

    /**
     * 更新商品流信息
     * @param context
     * @param localVersion
     * @param responseData
     * @return
     * long
     */
    public long updateContentConfigData(Context context, int localVersion, CMSResponseBaseData responseData)
    {
        ContentValues values = new ContentValues();
        values.put(ContentConfigTable.DATA_VERSION, responseData.getData_version());
        values.put(ContentConfigTable.DATA, responseData.getData());
        long id = dataBase.update(ContentConfigTable.TABLE_NAME, values, ContentConfigTable.DATA_VERSION + "= ?",
                new String[]
                { String.valueOf(localVersion) });
        context.getContentResolver().notifyChange(ContentConfigTable.URI, null);
        return id;
    }
}
