package com.lives.depend.account;

import android.app.Activity;
import android.content.Context;

/**
 * 三方账号统一接口
 * 
 * @author putao_lhq
 * 
 */
public interface IThirdAccount
{
    

    /**
     * 获取三方账号的唯一标识
     * 
     * @param silent TODO
     * @return
     */
    public String getUid(boolean silent);

    /**
     * 获取三方账号账户信息
     * 
     * @return
     */
    public BaseAccountInfo getAccountInfo();

    /**
     * 获取账号来源
     * accSource 绑定鉴权账号来源 : 0 表使用设备创建, 1 表使用手机创建, 2 表使用酷派创建
     * @return
     */
    public int getSource();

    /**
     * 获取账户类型
     * @param accType 绑定鉴权账号类型: 0 表临时用户, 1 表普通绑定用户, 2 表平台绑定用户
     * @return
     */
    public int getType();

    /**
     * 判断三方账号是否登录
     * 
     * @return
     */
    public boolean isLogin();

    /**
     * 显示登录界面
     */
    public void showLoginView(Activity context, final ILoginCallback cb);

    /**
     * 显示账户信息界面
     */
    public void showAccountInfoView(Context context);

    /**
     * 是否支持设备号登陆
     */
    public boolean isSupportDeviceLogin();

}
