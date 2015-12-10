package com.lives.depend.payment;

/**
 * ****************************************************************<br>
 * 文件名称 : PaymentResultCode.java<br>
 * 作 者 : putao_xcx<br>
 * 创建时间 : 2015-11-23 下午3:46:47<br>
 * 文件描述 : 支付结果状态码<br>
 * 版权声明 :深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-23 1.00 初始版本<br>
 ***************************************************************** 
 */
public class PaymentResultCode
{

    /**
     * 
     * 错误原因状态码
     * 
     **/
    public static interface ErrorCode
    {

        /** 服务器异常 */
        public static final int InternalErr = -1;

        /** 签名异常 */
        public static final int SignErr = -2;

        /** 优惠券无效 */
        public static final int CouponInvalid = -3;

        /** 参数异常 */
        public static final int ParamErr = -4;

        /** 无产品报价 */
        public static final int MissingProduct = -5;

        /** 服务器维护中 */
        public static final int ServiceStopped = -6;

        /** 未知错误 */
        public static final int UnknownErr = -99;

        /** 支付价格不正确 */
        public static final int PriceNotRight = -7;

        /** 网络错误 */
        public static final int NetError = -0x200;

        /** 超时 */
        public static final int Timeout = -0x201;

        /** 服务器繁忙 */
        public static final int ServerBusy = -0x202;
        
        /** 未登录 */
        public static final int UnLogin = -0x203;
        
        /** 支付条件异常 */
        public static final int ConditionError = -0x204;

        public static final String ResultCodeSuccess = "0000";

        public static final String CouponNotExists = "12104";

        public static final String CouponExpired = "12105";

        public static final String PriceWrong = "12101";

    }

    /**
     * 
     * 支付结果状态码，为Failed 时 错误码参照 @link ErrorCode
     * 
     **/
    public static interface Result
    {
        /**
         * 结果确认中：如支付宝状态码8000
         */
        public static final int Confirming = 2;
        
        /**
         * 等待结果：如微信支付，需要等待结果页面的支付结果作为最后支付状态
         */
        public static final int WaitForResult = 1;
        
        /**
         * 支付成功
         */
        public static final int Success = 0;

        /**
         * 支付失败
         */
        public static final int Failed = -1;

        /**
         * 取消支付
         */
        public static final int Cancel = -2;
    }
}
