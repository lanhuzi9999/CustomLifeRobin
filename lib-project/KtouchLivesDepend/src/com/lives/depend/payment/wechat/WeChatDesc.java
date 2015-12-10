package com.lives.depend.payment.wechat;

import com.lives.depend.DependApplication;
import com.lives.depend.payment.IPaymentAction;
import com.lives.depend.payment.IPaymentDesc;
import com.lives.depend.payment.PaymentConfig;
import com.lives.depend.utils.GetResourceUtil;

/**
 * ****************************************************************<br>
 * 文件名称 : WeChatDesc.java<br>
 * 作 者 :   putao_xcx<br>
 * 创建时间 : 2015-11-24 下午7:48:01<br>
 * 文件描述 : 微信支付描述                 <br>
 * 版权声明 :深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-24 1.00 初始版本<br>
 *****************************************************************
 */
public class WeChatDesc implements IPaymentDesc
{

    private IPaymentAction mIPaymentAction;

    @Override
    public int getIconId()
    {
        return GetResourceUtil.getDrawableResource(DependApplication.getContext(), "putao_icon_pay_wxzf");
    }

    @Override
    public int getPaymentType()
    {
        return PaymentConfig.TYPE_WE_CHAT;
    }

    @Override
    public String getPaymentLabel()
    {
        return GetResourceUtil.getString(DependApplication.getContext(), "putao_pay_way_wxzf");
    }

    @Override
    public IPaymentAction getPaymentAciton()
    {
        if (mIPaymentAction == null)
        {
            mIPaymentAction = new WeChatPaymentAction();
        }
        return mIPaymentAction;
    }

    @Override
    public String getRefundInstructionsForOrder()
    {
        return GetResourceUtil.getString(DependApplication.getContext(), "putao_order_status_hint_askforrefund_weixin");
    }

    
    @Override
    public boolean isEnable()
    {
//        SharedPreferences pref = DependApplication.getContext().getSharedPreferences("c_setting",
//                Context.MODE_MULTI_PROCESS);
//
//        boolean isEnable = (1 == pref.getInt("wx_open_flag", 1));
//        return isEnable;
//        
        return true;
    }

    @Override
    public boolean hasSelfResultView()
    {
        return true;
    }

    @Override
    public int getRefundInstructionsForResult()
    {
        return GetResourceUtil.getDrawableResource(DependApplication.getContext(), "putao_charge_traffic_tips_for_ask_for_refund_wechat");
    }

    @Override
    public int getFailInstructionsForResult()
    {
        return GetResourceUtil.getDrawableResource(DependApplication.getContext(), "putao_charge_deal_failed_hint_weixin");
    }
}
