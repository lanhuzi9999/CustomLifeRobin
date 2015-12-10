package com.lives.depend.payment;

/**
 * ****************************************************************<br>
 * 文件名称 : IPaymentDesc.java<br>
 * 作 者 : putao_xcx<br>
 * 创建时间 : 2015-11-23 下午12:10:26<br>
 * 文件描述 : 支付描述类 <br>
 * 版权声明 :深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-23 1.00 初始版本<br>
 ***************************************************************** 
 */
public interface IPaymentDesc
{

    /**
     * 
     * 获取支付图标资源ID
     * 
     * @return int
     */
    public int getIconId();

    /**
     * 
     * 获取支付类型
     * 
     * @return int
     */
    public int getPaymentType();

    /**
     * 
     * 获取支付名称
     * 
     * @return String
     */
    public String getPaymentLabel();

    /**
     * 
     * 获取支付执行者对象
     * 
     * @return IPaymentAction
     */
    public IPaymentAction getPaymentAciton();

    /**
     * 
     * 获取退款描述信息,用于订单显示
     * 
     * @return String
     */
    public String getRefundInstructionsForOrder();
    
    
    /**
     * 
     * 获取退款描述信息,用于支付结果页面显示
     * 
     * @return int
     */
    public int getRefundInstructionsForResult();
    
    
    /**
     * 
     * 获取失败描述信息
     * 
     * @return int
     */
    public int getFailInstructionsForResult();
    /**
     * 
     * 是否打开该支付
     * 
     * @return boolean
     */
    public boolean isEnable();
    /**
     * 是否有自己的支付结果页
     * 
     * @return boolean
     */
    public boolean hasSelfResultView();
}
