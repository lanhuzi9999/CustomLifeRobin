package so.contacts.hub.basefunction.utils.staticresource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class StaticResourceToDBUtils
{

    public static void loadAllCityList(InputStream inputStream)
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null)
            {
                if (line == null || "".equals(line))
                {
                    continue;
                }
                //根据空格分割
                String elements[] = line.split("\t");
                if(elements == null)
                {
                    continue;
                }
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
    }

    public static void loadAllCityList()
    {
        // 如果数据库已经有数据则直接return
    }
}
