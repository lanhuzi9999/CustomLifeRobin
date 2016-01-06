package so.contacts.hub.basefunction.storage.db;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.putao.live.R;

import so.contacts.hub.basefunction.account.bean.CityBean;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CityListDB
{
    private static final String TAG = "CityListDB";

    private SQLiteDatabase mDatabase;

    public CityListDB(DatabaseHelper helper)
    {
        mDatabase = helper.getWritableDatabase();
    }

    public SQLiteDatabase getSQLiteDatabase()
    {
        return mDatabase;
    }

    public static class CityListDbTable
    {
        public static final String TABLE_NAME = "yellow_citylist";

        public static final String _ID = "_id";

        public static final String CITY_NAME = "city_name";// 城市名称

        public static final String CITY_PY = "city_py";// 城市拼音

        public static final String SELF_ID = "self_id";// 城市ID

        public static final String PARENT_ID = "parent_id";// 城市所属父级

        public static final String CITY_TYPE = "city_type";// 城市类别（1：省份；2：地级市；3：县级）

        public static final String DISTRICT_CODE = "district_code";// 行政区编码

        public static final String CITY_HOT = "city_hot";// 热门城市（1：是；0：不是）

        public static final String WUBA_STATE = "wuba_state";// 58同城城市是否存在（1：存在；0：不存在）

        public static final String WUBA_CODE = "wuba_code";// 58同城城市编码

        public static final String ELONG_STATE = "elong_state";// 艺龙城市是否存在（1：存在；0：不存在）

        public static final String ELONG_CODE = "elong_code";// 艺龙城市编码

        public static final String TONGCHENG_STATE = "tongcheng_state";// 同城城市是否存在（1：存在；0：不存在）

        public static final String TONGCHENG_CODE = "tongcheng_code";// 同城城市编码

        public static final String GEWARA_STATE = "gewara_state";// 格瓦拉城市是否存在（1：存在；0：不存在）

        public static final String GEWARA_CODE = "gewara_code";// 格瓦拉城市编码

        public static final String GAODE_STATE = "gaode_state";// 高德城市是否存在（1：存在；0：不存在）

        public static final String GAODE_CODE = "gaode_code";// 高德城市编码
    }

    public static String getCreateCityListDbTableSQL()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(CityListDbTable.TABLE_NAME).append(" (")
                .append(CityListDbTable._ID).append(" INTEGER PRIMARY KEY autoincrement,");
        sb.append(CityListDbTable.CITY_NAME).append(" TEXT,");
        sb.append(CityListDbTable.CITY_PY).append(" TEXT,");
        sb.append(CityListDbTable.SELF_ID).append(" INTEGER,");
        sb.append(CityListDbTable.PARENT_ID).append(" INTEGER,");
        sb.append(CityListDbTable.CITY_TYPE).append(" INTEGER,");
        sb.append(CityListDbTable.DISTRICT_CODE).append(" TEXT,");
        sb.append(CityListDbTable.CITY_HOT).append(" INTEGER,");
        sb.append(CityListDbTable.WUBA_STATE).append(" INTEGER,");
        sb.append(CityListDbTable.WUBA_CODE).append(" TEXT,");
        sb.append(CityListDbTable.ELONG_STATE).append(" INTEGER,");
        sb.append(CityListDbTable.ELONG_CODE).append(" TEXT,");
        sb.append(CityListDbTable.TONGCHENG_STATE).append(" INTEGER,");
        sb.append(CityListDbTable.TONGCHENG_CODE).append(" TEXT,");
        sb.append(CityListDbTable.GEWARA_STATE).append(" INTEGER,");
        sb.append(CityListDbTable.GEWARA_CODE).append(" TEXT,");
        sb.append(CityListDbTable.GAODE_STATE).append(" INTEGER,");
        sb.append(CityListDbTable.GAODE_CODE).append(" TEXT");
        sb.append(" );");
        return sb.toString();
    }

    /**
     * 全量插入城市列表数据
     * 
     * @param cityList void
     */
    public void insertCityList(List<CityBean> cityList)
    {
        if (cityList == null || cityList.size() == 0)
        {
            return;
        }
        // 因为是大批量的数据插入，所以需要用到事物
        mDatabase.beginTransaction();
        try
        {
            if (hasCityData())
            {
                deleteAllCityData();
            }
            for (CityBean bean : cityList)
            {
                insert(bean);
            }
            mDatabase.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            mDatabase.endTransaction();
        }
        finally
        {
            mDatabase.endTransaction();
        }
    }

    /**
     * 插入一条城市数据
     * 
     * @param bean void
     */
    private void insert(CityBean bean)
    {
        if (bean == null)
        {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(CityListDbTable.CITY_NAME, bean.getCityName());
        values.put(CityListDbTable.CITY_PY, bean.getCityPy());
        values.put(CityListDbTable.CITY_TYPE, bean.getCityType());
        values.put(CityListDbTable.DISTRICT_CODE, bean.getDistrictCode());
        values.put(CityListDbTable.ELONG_CODE, bean.getElongCode());
        values.put(CityListDbTable.ELONG_STATE, bean.getElongState());
        values.put(CityListDbTable.GAODE_CODE, bean.getGaodeCode());
        values.put(CityListDbTable.GAODE_STATE, bean.getGaodeState());
        values.put(CityListDbTable.GEWARA_CODE, bean.getGewaraCode());
        values.put(CityListDbTable.GEWARA_STATE, bean.getGewaraState());
        values.put(CityListDbTable.PARENT_ID, bean.getParentId());
        values.put(CityListDbTable.SELF_ID, bean.getSelfId());
        values.put(CityListDbTable.TONGCHENG_CODE, bean.getTongchengCode());
        values.put(CityListDbTable.TONGCHENG_STATE, bean.getTongchengState());
        values.put(CityListDbTable.WUBA_CODE, bean.getWubaCode());
        values.put(CityListDbTable.WUBA_STATE, bean.getWubaState());
        values.put(CityListDbTable.CITY_HOT, bean.getCityHot());

        mDatabase.insert(CityListDbTable.TABLE_NAME, null, values);
    }

    /**
     * 清空城市表 void
     */
    public void deleteAllCityData()
    {
        mDatabase.delete(CityListDbTable.TABLE_NAME, null, null);
    }

    /**
     * 城市表是否有数据
     * 
     * @return boolean
     */
    public boolean hasCityData()
    {
        boolean hasData = false;
        Cursor cursor = null;
        try
        {
            cursor = mDatabase.query(CityListDbTable.TABLE_NAME, null, null, null, null, null, null);
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

    public LinkedList<String> getDistrictNameByParentId(Context context, int parent_id)
    {
        LinkedList<String> list = new LinkedList<String>();
        Cursor cursor = null;
        try
        {
            cursor = mDatabase.query(
                    CityListDbTable.TABLE_NAME,
                    new String[]
                    { CityListDbTable.CITY_NAME },
                    CityListDbTable.PARENT_ID + " =? AND " + CityListDbTable.CITY_NAME + " NOT LIKE ? AND "
                            + CityListDbTable.CITY_NAME + " NOT LIKE ? AND " + CityListDbTable.CITY_NAME
                            + " NOT LIKE ? ",
                    new String[]
                    { String.valueOf(parent_id), context.getString(R.string.putao_train_quhua),
                            context.getString(R.string.putao_train_shixiaqu),
                            context.getString(R.string.putao_train_xian) }, null, null, null);
            if (cursor != null)
            {
                cursor.moveToFirst();
                while (!cursor.isAfterLast())
                {

                    String cityName = cursor.getString(cursor.getColumnIndex(CityListDbTable.CITY_NAME));
                    //需要去除后缀
                    cityName = cutSuffix(context, cityName);
                    list.add(cityName);
                    cursor.moveToNext();
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
        return list;
    }
    
    /**
     * 去除“省”“市”“自治区”等后缀
     * @param context
     * @param cityName
     * @return
     * String
     */
    public static String cutSuffix(Context context, String cityName)
    {
        if (cityName.endsWith(context.getString(R.string.putao_common_province))
                || cityName.endsWith(context.getString(R.string.putao_common_city)))
        {
            cityName = cityName.substring(0, cityName.length() - 1);
        }
        else if (cityName.endsWith(context.getString(R.string.putao_common_autonomous_regions)))
        {
            cityName = cityName.substring(0, 2);
        }
        return cityName;
    }

    /**
     * 根据省份的名称来获取省份id
     * 
     * @param cityName
     * @return int
     */
    public int getCityIdByCityName(String cityName)
    {
        int cityId = -1;
        Cursor cursor = null;
        try
        {
            cursor = mDatabase.query(CityListDbTable.TABLE_NAME, new String[]
            { CityListDbTable.SELF_ID }, CityListDbTable.CITY_NAME + " LIKE ? AND " + CityListDbTable.PARENT_ID
                    + " = ?", new String[]
            { cityName + "%", "0" }, null, null, null);
            if (cursor != null && cursor.moveToFirst())
            {
                cityId = cursor.getInt(cursor.getColumnIndex(CityListDbTable.SELF_ID));
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
        return cityId;
    }
}
