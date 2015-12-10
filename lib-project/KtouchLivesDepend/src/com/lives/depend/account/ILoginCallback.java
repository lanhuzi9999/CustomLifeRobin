package com.lives.depend.account;

/**
 * 账号登录回调接口
 * 
 * @author putao_lhq
 * 
 */
public interface ILoginCallback
{

    /**
     * 登录成功
     */
    public void onSuccess();

    /**
     * 登录失败
     * 
     * @param failedCode
     */
    public void onFail(int failedCode);

    /**
     * 登录取消
     * 
     * @param
     */
    public void onCancel();
}
