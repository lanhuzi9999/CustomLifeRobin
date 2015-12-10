package com.lives.depend.account;

import android.app.Activity;
import android.content.Context;

public interface IBaseAccountAction
{

    /**
     * 
     * 绑定第三方账号
     * @param context
     * @param cb
     * void
     */
    public void bindThirdAccount(Activity context);
    
    
    
    /**
     * 
     *解绑第三方账号
     * @param context
     * @param cb
     * void
     */
    public void unBindThirdAccount(Context context);
    
    /**
     * 
     * 获取旧的第三方UID
     * @return
     * String
     */
    public String getOldThirdUid();
    
    /**
     * 
     * 存储第三方UID
     * @param uid
     * void
     */
    public void saveThirdUid(String uid);
}
