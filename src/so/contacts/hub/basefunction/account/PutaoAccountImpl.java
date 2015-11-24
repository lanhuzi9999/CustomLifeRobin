package so.contacts.hub.basefunction.account;

import so.contacts.hub.basefunction.config.Config;
import android.content.Context;

/**
 * **************************************************************** 文件名称 :
 * PutaoAccountImpl.java 作 者 : Robin 创建时间 : 2015-11-16 上午12:17:14 文件描述 :
 * 葡萄账户接口具体实现类 版权声明 : 深圳市葡萄信息技术有限公司 版权所有 修改历史 : 2015-11-16 1.00 初始版本
 ***************************************************************** 
 */
public class PutaoAccountImpl implements IPutaoAccount
{
    private static final String TAG = "PutaoAccountImpl";

    public PutaoAccountImpl()
    {

    }

    @Override
    public void sendCaptchar(Context context, String mobile, String actionCode, IAccCallbackAdv<String> cb)
    {
        Config.execute(new Runnable()
        {

            @Override
            public void run()
            {

            }
        });
    }
}
