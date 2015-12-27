package so.contacts.hub.basefunction.storage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DB_NAME = "putao.db";

    private static DatabaseHelper mInstance;

    private Context mContext;

    private CMSDataDB mCmsDataDB;

    private PersonInfoDB mPersonaInfoDB;

    private CityListDB mCityListDB;
    
    public static synchronized DatabaseHelper getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new DatabaseHelper(context);
        }
        return mInstance;
    }

    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, 1);
        this.mContext = context;
    }

    public synchronized CMSDataDB getCmsDataDB()
    {
        if (mCmsDataDB == null)
        {
            mCmsDataDB = new CMSDataDB(this);
        }
        return mCmsDataDB;
    }

    public synchronized PersonInfoDB getPersonInfoDB()
    {
        if (mPersonaInfoDB == null)
        {
            mPersonaInfoDB = new PersonInfoDB(this);
        }
        return mPersonaInfoDB;
    }

    public synchronized CityListDB getCityListDB()
    {
        if(mCityListDB == null)
        {
            mCityListDB = new CityListDB(this);
        }
        return mCityListDB;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.beginTransaction();
        // 创建cmsdb表的sql
        db.execSQL(CMSDataDB.getCreateCommonServicesTableSQL());
        db.execSQL(CMSDataDB.getCreateContentConfigTableSQL());
        db.execSQL(PersonInfoDB.getCreatePersonalInfoDBSQL());
        db.execSQL(CityListDB.getCreateCityListDbTableSQL());
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

}
