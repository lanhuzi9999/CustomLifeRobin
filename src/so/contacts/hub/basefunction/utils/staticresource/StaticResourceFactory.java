package so.contacts.hub.basefunction.utils.staticresource;

import java.io.InputStream;

public class StaticResourceFactory
{

    public static final int TYPE_CITY_LIST = 0;

    public static final int TYPE_EXPRESS = 1;

    public static final int TYPE_TRAIN_TICKET = 3;

    public static final int TYPE_VIOLATION_CITY = 4;

    public static final int TYPE_TELAREA = 5;

    public static final int TYPE_WEG_DATA = 6;

    public static final int TYPE_FLIGHT_CITY = 7;

    public static final int TYPE_RES = 8;

    public static void putNetResDataToDb(InputStream inputStream, int type)
    {
        switch (type)
        {
            case TYPE_CITY_LIST:
                StaticResourceToDBUtils.loadAllCityList(inputStream);
                break;

            default:
                break;
        }
    }

    public static void putLocalResDataToDb(int type)
    {
        switch (type)
        {
            case TYPE_CITY_LIST:
                StaticResourceToDBUtils.loadAllCityList();
                break;

            default:
                break;
        }
    }
}
