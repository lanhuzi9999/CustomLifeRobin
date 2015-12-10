package com.lives.depend.payment;

import java.util.HashMap;
import java.util.Map;

/**
 * ****************************************************************<br>
 * 文件名称 : PayResult.java<br>
 * 作 者 : putao_xcx<br>
 * 创建时间 : 2015-11-24 下午5:44:32<br>
 * 文件描述 : 支付结果 <br>
 * 版权声明 :深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-24 1.00 初始版本<br>
 ***************************************************************** 
 */
public class PayResult
{
    /**
     * 订单是否创建
     */
    public boolean isOrderCreated=false;
    
    /**
     * 支付结果码
     */
    public int resultCode;

    /**
     * 错误码
     */
    public int errorCode;

    /**
     * 错误描述
     */
    public String errorDesc;

    /**
     * 支付参数
     */
    public Map<String, String> extras = new HashMap<String, String>();
}
