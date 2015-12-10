package com.lives.depend.payment.alipay;

import android.text.TextUtils;

import com.lives.depend.payment.PayResult;
import com.lives.depend.payment.PaymentResultCode;
import com.lives.depend.utils.LogUtil;

public class AliConfig
{

    private static final String TAG = "AliConfig";

    // 订单超时时间，默认0.5小时
    public static final String ORDER_TIME_OUT = "30m";

    public static interface AliPay
    {
        /** 支付成功 */
        public static final int Success = 9000;

        /** 正在处理 */
        public static final int Processing = 8000;

        /** 支付失败 */
        public static final int Failed = 4000;

        /** 请求参数错误 */
        public static final int ParamErr = 4001;

        /** 订单取消 */
        public static final int Canceled = 6001;

        /** 网络连接错误 */
        public static final int NetError = 6002;
    }

    public static PayResult parseAlipayResult(String alipayResult)
    {

        PayResult payResult = new PayResult();
        if (TextUtils.isEmpty(alipayResult))
        {
            payResult.resultCode = PaymentResultCode.Result.Failed;
            payResult.errorCode = PaymentResultCode.ErrorCode.InternalErr;
            return payResult;
        }
        int errcode = AliConfig.AliPay.Failed;
        try
        {
            String[] result_array = alipayResult.split(";");
            /*
             * modify by zj 2015-3-30 start 原代码 if (result_array != null &&
             * result_array.length == 3)
             * 现在的返回参数可能出现=3也可能出现=4的情况,暂时没有摸清规律,所以先做个兼容处理 暂时认为可能跟机型有关
             */
            if (result_array != null && (result_array.length == 3 || result_array.length == 4))
            {
                // end 2015-3-30 by zj
                // 返回码
                String resultStatus = result_array[0].substring(result_array[0].indexOf('{') + 1,
                        result_array[0].length() - 1);
                errcode = Integer.parseInt(resultStatus);
                String result = result_array[2].substring(result_array[2].indexOf('{') + 1,
                        result_array[2].length() - 1);
                if (result != null && result.length() > 0)
                {
                    // 解析订单信息
                    String orderInfo[] = result.split("\\&");
                    if (orderInfo != null && orderInfo.length > 0)
                    {
                        for (int i = 0; i < orderInfo.length; i++)
                        {
                            String[] tmp = orderInfo[i].split("\\=");
                            String key = tmp[0];
                            String val = tmp[1].substring(1, tmp[1].length() - 1);
                            payResult.extras.put(key, val);
                        }
                    }
                }

            }
        }
        catch (Exception e)
        {
            LogUtil.e(TAG, "catch Exception throw by parseAlipayResult! ", e);
        }

        switch (errcode)
        {
            case AliPay.Canceled:
            {
                payResult.resultCode = PaymentResultCode.Result.Cancel;
                break;
            }
            case AliPay.Failed:
            {
                payResult.resultCode = PaymentResultCode.Result.Failed;
                payResult.errorCode = PaymentResultCode.ErrorCode.UnknownErr;
                break;
            }
            case AliPay.NetError:
            {
                payResult.resultCode = PaymentResultCode.Result.Failed;
                payResult.errorCode = PaymentResultCode.ErrorCode.InternalErr;
                break;
            }
            case AliPay.ParamErr:
            {
                payResult.resultCode = PaymentResultCode.Result.Failed;
                payResult.errorCode = PaymentResultCode.ErrorCode.ParamErr;
            }
            case AliPay.Processing:
            {
                payResult.resultCode = PaymentResultCode.Result.Failed;
                payResult.errorCode = PaymentResultCode.ErrorCode.UnknownErr;
            }
            case AliPay.Success:
            {
                payResult.resultCode = PaymentResultCode.Result.Success;
                break;
            }
            default:
            {
                payResult.resultCode = PaymentResultCode.Result.Failed;
                payResult.errorCode = PaymentResultCode.ErrorCode.UnknownErr;
                break;
            }

        }
        return payResult;
    }
}
