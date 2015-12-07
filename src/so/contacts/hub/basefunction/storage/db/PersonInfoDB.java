package so.contacts.hub.basefunction.storage.db;

import so.contacts.hub.basefunction.account.bean.BasicUserInfoBean;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * ****************************************************************
 * 文件名称 : PersonaInfoDB.java
 * 作 者 :   Robin Pei
 * 创建时间 : 2015-12-7 下午8:30:03
 * 文件描述 : 个人信息表
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-12-7 1.00 初始版本
 *****************************************************************
 */
public class PersonInfoDB
{
    private static final String TAG = "PersonaInfoDB";

    SQLiteDatabase database;

    public PersonInfoDB(DatabaseHelper helper)
    {
        database = helper.getWritableDatabase();
    }

    public SQLiteDatabase getPersonaInfoDB()
    {
        return database;
    }

    public static class PersonInfoTable implements BaseColumns
    {
        public static final String TABLE_NAME = "personal_info_db";

        public static final String HAS_SET_PASSWORD = "has_set_password";

        public static final String HEAD_PIC = "head_pic";

        public static final String CITY = "city";

        public static final String GENDER = "gender";

        public static final String BIRTHDAY = "birthday";

        public static final String FAVORABLE = "favorable";
    }

    public static String getCreatePersonalInfoDBSQL()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append(PersonInfoTable.TABLE_NAME).append(" (");
        sb.append(PersonInfoTable._ID).append(" INTEGER  PRIMARY KEY autoincrement,");
        sb.append(PersonInfoTable.HAS_SET_PASSWORD).append(" INTEGER,");
        sb.append(PersonInfoTable.HEAD_PIC).append(" TEXT,");
        sb.append(PersonInfoTable.CITY).append(" TEXT,");
        sb.append(PersonInfoTable.GENDER).append(" INTEGER,");
        sb.append(PersonInfoTable.BIRTHDAY).append(" TEXT,");
        sb.append(PersonInfoTable.FAVORABLE).append(" TEXT");
        sb.append(");");

        return sb.toString();
    }
    
    /**
     * 个人中心表是否有数据
     * @return
     * boolean
     */
    public boolean hasData()
    {
        boolean flag = false;

        Cursor cursor = null;
        try
        {
            cursor = database.query(PersonInfoTable.TABLE_NAME, null, null, null, null, null, null);
            if (null != cursor && cursor.getCount() > 0)
            {
                flag = true;
            }
        }
        finally
        {
            if (null != cursor)
                cursor.close();
        }

        return flag;
    }
    
    /**
     * 清空表中数据
     * void
     */
    public void clearTable()
    {
        database.delete(PersonInfoTable.TABLE_NAME, null, null);
    }

    /**
     * 插入数据
     * @param bean
     * @return
     * boolean
     */
    public boolean insertData(BasicUserInfoBean bean)
    {
        long row = -1;

        if (hasData())
        {
            clearTable();
        }

        ContentValues values = new ContentValues();
        values.put(PersonInfoTable.HAS_SET_PASSWORD, bean.getHas_set_password());
        values.put(PersonInfoTable.HEAD_PIC, bean.getHead_pic());
        values.put(PersonInfoTable.CITY, bean.getCity());
        values.put(PersonInfoTable.GENDER, bean.getGender());
        values.put(PersonInfoTable.BIRTHDAY, bean.getBirthday());
        values.put(PersonInfoTable.FAVORABLE, bean.getFavorable());

        row = database.insert(PersonInfoTable.TABLE_NAME, null, values);

        return row != -1 ? true : false;
    }

    /**
     * 查询个人信息数据
     * @return
     * BasicUserInfoBean
     */
    public BasicUserInfoBean queryData()
    {
        Cursor cursor = null;
        BasicUserInfoBean bean = null;

        try
        {
            cursor = database.query(PersonInfoTable.TABLE_NAME, null, null, null, null, null, null);

            if (null != cursor && cursor.getCount() != 0)
            {
                cursor.moveToFirst();
                bean = new BasicUserInfoBean();
                bean.setHas_set_password(cursor.getInt(cursor.getColumnIndex(PersonInfoTable.HAS_SET_PASSWORD)));
                bean.setHead_pic(cursor.getString(cursor.getColumnIndex(PersonInfoTable.HEAD_PIC)));
                bean.setCity(cursor.getString(cursor.getColumnIndex(PersonInfoTable.CITY)));
                bean.setGender(cursor.getInt(cursor.getColumnIndex(PersonInfoTable.GENDER)));
                bean.setBirthday(cursor.getString(cursor.getColumnIndex(PersonInfoTable.BIRTHDAY)));
                bean.setFavorable(cursor.getString(cursor.getColumnIndex(PersonInfoTable.FAVORABLE)));
            }

        }
        finally
        {
            if (null != cursor)
                cursor.close();
        }

        return bean;
    }

    /**
     * 查询头像url
     * @return
     * String
     */
    public String queryImgUrl()
    {
        String imgUrl = null;
        Cursor cursor = null;

        try
        {
            cursor = database.query(PersonInfoTable.TABLE_NAME, new String[]
            { PersonInfoTable.HEAD_PIC }, null, null, null, null, null);

            if (cursor != null && cursor.getCount() != 0)
            {
                cursor.moveToFirst();
                imgUrl = cursor.getString(cursor.getColumnIndex(PersonInfoTable.HEAD_PIC));
            }
        }
        finally
        {
            if (null != cursor)
                cursor.close();
        }

        return imgUrl;
    }
}
