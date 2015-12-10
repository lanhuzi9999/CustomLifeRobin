package com.lives.depend.payment;

import java.util.ArrayList;
import java.util.List;

import com.lives.depend.payment.alipay.AliPayDesc;
import com.lives.depend.payment.wechat.WeChatDesc;

/**
 * ****************************************************************<br>
 * 文件名称 : PaymentDescFactory.java<br>
 * 作 者 : putao_xcx<br>
 * 创建时间 : 2015-11-23 下午2:51:49<br>
 * 文件描述 : <br>
 * 版权声明 :深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-23 1.00 初始版本<br>
 ***************************************************************** 
 */
public class PaymentDescFactory
{

    /**
     * 默认的支付列表
     */
    private static List<IPaymentDesc> mAllPaymentDescList;

    /**
     * 获取默认的支付列表
     * 
     * @return List<IPaymentDesc>
     */
    public static List<Integer> getDefaultPaymentTypeList()
    {

        List<Integer> typeList = new ArrayList<Integer>();
        typeList.add(PaymentConfig.TYPE_WE_CHAT);
        typeList.add(PaymentConfig.TYPE_ALIPAY);
        return typeList;
    }
    
    
    public static List<IPaymentDesc> getAllPaymentDesc()
    {

        if (mAllPaymentDescList == null)
        {
            createDescs();
        }
        List<IPaymentDesc> allList=new ArrayList<IPaymentDesc>();
        allList.addAll(mAllPaymentDescList);
        return allList;
    }

    private static void createDescs()
    {
        mAllPaymentDescList = new ArrayList<IPaymentDesc>();
        mAllPaymentDescList.add(new WeChatDesc());// 添加微信支付
        mAllPaymentDescList.add(new AliPayDesc());// 添加支付宝
    }

    /**
     * 
     * 方法表述
     * 
     * @param paymentType
     * @return IPaymentDesc
     */
    public static IPaymentDesc getPaymentDescByType(int paymentType)
    {
        if (mAllPaymentDescList == null)
        {
            createDescs();
        }

        if (mAllPaymentDescList != null)
        {
            for (IPaymentDesc desc : mAllPaymentDescList)
            {
                if (desc.getPaymentType() == paymentType)
                {
                    return desc;
                }
            }
        }
        return null;
    }

    /**
     * 
     * 获取默认选中的支付方式
     * @return
     * int
     */
    public static int getDefaultPaymentType()
    {
        /**
         * 葡萄生活修改默认支付为微信支付
         * modified by wcy 2015-12-9 start old code:
         */
        //return PaymentConfig.TYPE_ALIPAY;
        return PaymentConfig.TYPE_WE_CHAT;
        /** modified by wcy 2015-12-9 end */
    }
}
