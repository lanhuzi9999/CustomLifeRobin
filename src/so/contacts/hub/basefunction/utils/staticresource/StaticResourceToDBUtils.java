package so.contacts.hub.basefunction.utils.staticresource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.lives.depend.utils.LogUtil;

import android.nfc.Tag;
import android.text.TextUtils;

import so.contacts.hub.basefunction.account.bean.CityBean;
import so.contacts.hub.basefunction.config.Config;
import so.contacts.hub.basefunction.storage.db.CityListDB;

public class StaticResourceToDBUtils
{
    private static final String TAG = "StaticResourceToDBUtils";

    public static void loadAllCityList(InputStream inputStream)
    {
        BufferedReader br = null;
        List<CityBean> cityList = new ArrayList<CityBean>();
        try
        {
            br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null)
            {
                if (line == null || "".equals(line))
                {
                    continue;
                }
                LogUtil.d(TAG, "loadAllCityList line:"+line);
                // 根据空格分割
                String elements[] = line.split("\t");
                if (elements == null || elements.length < 17)
                {
                    continue;
                }
                CityBean cityBean = new CityBean();
                setCityBeanValues(cityBean, elements);
                cityList.add(cityBean);
            }
            if (cityList.size() > 0)
            {
                CityListDB cityListDB = Config.getDatabaseHelper().getCityListDB();
                cityListDB.insertCityList(cityList);
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (br != null)
                {
                    br.close();
                }
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    /**
     * 给citybean赋值
     * 
     * @param cityBean
     * @param elements void
     */
    private static void setCityBeanValues(CityBean cityBean, String[] elements)
    {
        if (cityBean == null)
        {
            return;
        }
        if (!TextUtils.isEmpty(elements[0]))
        {
            cityBean.setCityName(elements[0]);
        }
        if (!TextUtils.isEmpty(elements[1]))
        {
            cityBean.setCityPy(elements[1]);
        }
        if (!TextUtils.isEmpty(elements[2]))
        {
            cityBean.setSelfId(Integer.valueOf(elements[2]));
        }
        if (!TextUtils.isEmpty(elements[3]))
        {
            cityBean.setParentId(Integer.valueOf(elements[3]));
        }
        if (!TextUtils.isEmpty(elements[4]))
        {
            cityBean.setCityType(Integer.valueOf(elements[4]));
        }
        if (!TextUtils.isEmpty(elements[5]))
        {
            cityBean.setDistrictCode(elements[5]);
        }
        if (!TextUtils.isEmpty(elements[6]))
        {
            cityBean.setCityHot(Integer.valueOf(elements[6]));
        }
        if (!TextUtils.isEmpty(elements[7]))
        {
            cityBean.setWubaState(Integer.valueOf(elements[7]));
        }
        if (!TextUtils.isEmpty(elements[8]))
        {
            cityBean.setWubaCode(elements[8]);
        }
        if (!TextUtils.isEmpty(elements[9]))
        {
            cityBean.setElongState(Integer.valueOf(elements[9]));
        }
        if (!TextUtils.isEmpty(elements[10]))
        {
            cityBean.setElongCode(elements[10]);
        }
        if (!TextUtils.isEmpty(elements[11]))
        {
            cityBean.setTongchengState(Integer.valueOf(elements[11]));
        }
        if (!TextUtils.isEmpty(elements[12]))
        {
            cityBean.setTongchengCode(elements[12]);
        }
        if (!TextUtils.isEmpty(elements[13]))
        {
            cityBean.setGewaraState(Integer.valueOf(elements[13]));
        }
        if (!TextUtils.isEmpty(elements[14]))
        {
            cityBean.setGewaraCode(elements[14]);
        }
        if (!TextUtils.isEmpty(elements[15]))
        {
            cityBean.setGaodeState(Integer.valueOf(elements[15]));
        }
        if (!TextUtils.isEmpty(elements[16]))
        {
            cityBean.setGaodeCode(elements[16]);
        }
    }

    public static void loadAllCityList()
    {
        // 如果数据库已经有数据则直接return
    }
}
