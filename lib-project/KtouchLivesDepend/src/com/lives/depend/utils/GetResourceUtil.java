package com.lives.depend.utils;

import android.content.Context;

/**
 * ****************************************************************<br>
 * 文件名称 : GetResourceUtil.java<br>
 * 作 者 : putao_xcx<br>
 * 创建时间 : 2015-11-23 下午4:42:21<br>
 * 文件描述 : 通过反射获取资源的工具<br>
 * 版权声明 :深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-23 1.00 初始版本<br>
 ***************************************************************** 
 */
public class GetResourceUtil
{

    public static String getString(Context ctx, String name)
    {
        int id = getStringResource(ctx, name);
        String str = "";
        if (id > 0)
        {
            str = ctx.getResources().getString(id);
        }
        return str;
    }

    /**
     * 根据名称获取对应string 资源Id
     * 
     * @param ctx
     * @param name 资源名称
     * @return int 资源id
     */
    public static int getStringResource(Context ctx, String name)
    {
        String packageName = ctx.getPackageName();
        try
        {
            Object obj;
            obj = Class.forName(packageName + ".R$string").newInstance();
            return Class.forName(packageName + ".R$string").getDeclaredField(name).getInt(obj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 
     * 根据名称获取对应布局文件 资源Id
     * 
     * @param ctx
     * @param 资源名称
     * @return int 资源id
     */
    public static int getLayoutResource(Context ctx, String name)
    {
        String packageName = ctx.getPackageName();
        try
        {
            Object obj;
            obj = Class.forName(packageName + ".R$layout").newInstance();
            return Class.forName(packageName + ".R$layout").getDeclaredField(name).getInt(obj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 
     * 根据名称获取对Drawable 资源Id
     * 
     * @param ctx
     * @param 资源名称
     * @return int 资源id
     */
    public static int getDrawableResource(Context ctx, String name)
    {
        String packageName = ctx.getPackageName();
        try
        {
            Object obj;
            obj = Class.forName(packageName + ".R$drawable").newInstance();
            return Class.forName(packageName + ".R$drawable").getDeclaredField(name).getInt(obj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getIdResource(Context ctx, String name)
    {
        String packageName = ctx.getPackageName();
        try
        {
            Object obj;
            obj = Class.forName(packageName + ".R$id").newInstance();
            return Class.forName(packageName + ".R$id").getDeclaredField(name).getInt(obj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 
     * 根据名称获取对Dimens 资源Id
     * 
     * @param ctx
     * @param 资源名称
     * @return int 资源id
     */
    public static int getDimensResource(Context ctx, String name)
    {
        String packageName = ctx.getPackageName();
        try
        {
            Object obj;
            obj = Class.forName(packageName + ".R$dimen").newInstance();
            return Class.forName(packageName + ".R$dimen").getDeclaredField(name).getInt(obj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }
}
