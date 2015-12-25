package so.contacts.hub.basefunction.storage.db;

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
}
