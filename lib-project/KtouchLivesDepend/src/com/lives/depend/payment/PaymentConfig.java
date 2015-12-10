package com.lives.depend.payment;

public class PaymentConfig
{

    public static final String SERVER_HOST = "http://api.putao.so/spay"; // for
                                                                         // product

//     public static final String SERVER_HOST = "http://api.test.putao.so/spay";

    public static final String NEW_SERVER_HOST = SERVER_HOST + "/spay";

    public interface PAY
    {
        /**
         * 阿里创建订单地址
         */
        public static final String ALIPAY_CREATE_ORDER_URL = SERVER_HOST + "/pay/order/alipay";

        /**
         * 微信创建订单地址
         */
        public static final String WECHAT_CREATE_ORDER_URL = SERVER_HOST + "/pay/order/wx";
    }
    
    /**
     * 支付宝的支付类型标识
     */
    public static final int TYPE_ALIPAY = 1;

    /**
     * 微信的支付类型标识
     */
    public static final int TYPE_WE_CHAT = 2;
}
