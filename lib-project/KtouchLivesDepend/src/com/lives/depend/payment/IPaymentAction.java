package com.lives.depend.payment;

import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;

/**
 * ****************************************************************<br>
 * 文件名称 : IPaymentAction.java<br>
 * 作 者 : putao_xcx<br>
 * 创建时间 : 2015-11-23 下午2:43:39<br>
 * 文件描述 : 支付操作接口, 用于执行具体的不同的支付操作 <br>
 * 版权声明 :深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-23 1.00 初始版本<br>
 ***************************************************************** 
 */
public interface IPaymentAction
{

    /**
     * 获取支付类型
     * 
     * @return int
     */
    public int getActionType();

    /**
     * 获取下单地址
     * 
     * @return String
     */
    public String getPutaoCreateOrderUrl();

    /**
     * 
     * 参数转换
     * 
     * @param paymentBean
     * @param queryMap
     * @return Map<String,String>
     */
    public Map<String, String> convertToParamMap(JSONObject paymentBean, Map<String, String> queryMap) throws Exception;

    /**
     * 
     * 完成支付
     * 
     * @param map
     * @return
     * @throws Exception PayResult
     */
    public void doPayment(Activity activity, Map<String, String> map,ResultCallback cb) throws Exception;

    /**
     * 检查支付环境十分满足需求
     * 
     * @throws PaymentEnvironmentException void
     */
    public void checkPaymentEnvironment(Activity activity) throws PaymentEnvironmentException;

    /**
     * 
     * Hook方法. 给子类一个自定义定义创建订单参数的机会.默认返回原值.
     * 
     * @param postBody
     * @return String
     */
    public String cutomizePostBody(String postBody);
}
