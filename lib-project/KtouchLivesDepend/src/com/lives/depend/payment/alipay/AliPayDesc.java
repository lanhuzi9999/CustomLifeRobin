package com.lives.depend.payment.alipay;

import com.lives.depend.DependApplication;
import com.lives.depend.payment.IPaymentAction;
import com.lives.depend.payment.IPaymentDesc;
import com.lives.depend.payment.PaymentConfig;
import com.lives.depend.utils.GetResourceUtil;

/**
 * ****************************************************************<br>
 * 文件名称 : AliPayDesc.java<br>
 * 作 者 : putao_xcx<br>
 * 创建时间 : 2015-11-23 下午4:55:28<br>
 * 文件描述 : 阿里支付相关内容 <br>
 * 版权声明 :深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-23 1.00 初始版本<br>
 ***************************************************************** 
 */
public class AliPayDesc implements IPaymentDesc
{

    private IPaymentAction mIPaymentAction;

    @Override
    public int getIconId()
    {
        return GetResourceUtil.getDrawableResource(DependApplication.getContext(), "putao_icon_pay_zfbzf");
    }

    @Override
    public int getPaymentType()
    {
        return PaymentConfig.TYPE_ALIPAY;
    }

    @Override
    public String getPaymentLabel()
    {
        return GetResourceUtil.getString(DependApplication.getContext(), "putao_pay_by_alipay");
    }

    @Override
    public IPaymentAction getPaymentAciton()
    {
        if (mIPaymentAction == null)
        {
            mIPaymentAction = new AliPayPaymentAction();
        }
        return mIPaymentAction;
    }

    @Override
    public String getRefundInstructionsForOrder()
    {
        return GetResourceUtil.getString(DependApplication.getContext(), "putao_order_status_hint_askforrefund_alipay");
    }

    @Override
    public boolean isEnable()
    {
        return true;
    }

    @Override
    public boolean hasSelfResultView()
    {
        return false;
    }
    
    @Override
    public int getRefundInstructionsForResult()
    {
        return GetResourceUtil.getDrawableResource(DependApplication.getContext(), "putao_charge_traffic_tips_for_ask_for_refund_alipay");
    }

    @Override
    public int getFailInstructionsForResult()
    {
        return GetResourceUtil.getDrawableResource(DependApplication.getContext(), "putao_charge_deal_failed_hint");
    }
}
