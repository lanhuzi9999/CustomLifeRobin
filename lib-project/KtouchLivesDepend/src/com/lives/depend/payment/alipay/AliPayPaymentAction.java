package com.lives.depend.payment.alipay;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;

import com.alipay.sdk.app.PayTask;
import com.lives.depend.payment.IPaymentAction;
import com.lives.depend.payment.PayResult;
import com.lives.depend.payment.PaymentConfig;
import com.lives.depend.payment.PaymentEnvironmentException;
import com.lives.depend.payment.PaymentResultCode;
import com.lives.depend.payment.ResultCallback;
import com.lives.depend.utils.LogUtil;

/**
 * ****************************************************************<br>
 * 文件名称 : AliPayPaymentAction.java<br>
 * 作 者 : putao_xcx<br>
 * 创建时间 : 2015-11-23 下午3:50:24<br>
 * 文件描述 : 阿里支付宝支付实现 <br>
 * 版权声明 :深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-23 1.00 初始版本<br>
 ***************************************************************** 
 */
class AliPayPaymentAction implements IPaymentAction
{

    private static final String TAG = "AliPayPaymentAction";

    @Override
    public int getActionType()
    {
        return PaymentConfig.TYPE_ALIPAY;
    }

    @Override
    public String getPutaoCreateOrderUrl()
    {
        return PaymentConfig.PAY.ALIPAY_CREATE_ORDER_URL;
    }

    @Override
    public Map<String, String> convertToParamMap(JSONObject paymentBean, Map<String, String> queryMap) throws Exception
    {
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("partner", paymentBean.getString("partner"));
        hashMap.put("out_trade_no", paymentBean.getString("out_trade_no"));
        hashMap.put("subject", paymentBean.getString("subject"));
        hashMap.put("body", paymentBean.getString("body"));
        hashMap.put("total_fee", paymentBean.getString("total_fee"));
        hashMap.put("notify_url", paymentBean.getString("notify_url"));
        hashMap.put("service", paymentBean.getString("service"));
        hashMap.put("return_url", "http://m.alipay.com");
        hashMap.put("payment_type", paymentBean.getString("payment_type"));
        hashMap.put("seller_id", paymentBean.getString("seller_id"));
        hashMap.put("sign", paymentBean.getString("sign"));
        return hashMap;

    }

    @SuppressWarnings("deprecation")
    private String createAlipayUrl(Map<String, String> map)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("partner=\"");
        sb.append(map.get("partner"));
        sb.append("\"&out_trade_no=\"");
        sb.append(map.get("out_trade_no"));
        sb.append("\"&subject=\"");
        sb.append(map.get("subject"));
        sb.append("\"&body=\"");
        sb.append(map.get("body"));
        sb.append("\"&total_fee=\"");
        sb.append(map.get("total_fee"));
        sb.append("\"&notify_url=\"");
        // 网址需要做URL编码
        sb.append(URLEncoder.encode(map.get("notify_url")));
        sb.append("\"&service=\"");
        sb.append(map.get("service"));
        sb.append("\"&_input_charset=\"utf-8");
        sb.append("\"&return_url=\"" + URLEncoder.encode("http://m.alipay.com"));
        sb.append("\"&payment_type=\"");
        sb.append(map.get("payment_type"));
        sb.append("\"&seller_id=\"");
        sb.append(map.get("seller_id"));
        sb.append("\"&it_b_pay=\"");
        sb.append(AliConfig.ORDER_TIME_OUT);
        sb.append("\"&sign=\"");
        sb.append(URLEncoder.encode(map.get("sign")));
        sb.append("\"&sign_type=\"RSA\"");
        return sb.toString();
    }

    @Override
    public void checkPaymentEnvironment(Activity activity) throws PaymentEnvironmentException
    {

    }

    @Override
    public String cutomizePostBody(String postBody)
    {
        return postBody;
    }

    @Override
    public void doPayment(Activity activity, Map<String, String> map, ResultCallback cb) throws Exception
    {
        if (activity != null)
        {
            PayTask alipay = new PayTask(activity);
            String param = createAlipayUrl(map);

            LogUtil.d(TAG, "alipay param=" + param);
            String alipayResult = alipay.pay(param);
            LogUtil.d(TAG, "alipay result=" + alipayResult);
            PayResult result = AliConfig.parseAlipayResult(alipayResult);
            cb.onPaymentFeedback(result);
        }
        else
        {
            PayResult result = new PayResult();
            result.resultCode = PaymentResultCode.Result.Failed;
            result.errorCode = PaymentResultCode.ErrorCode.ParamErr;
            cb.onPaymentFeedback(result);
        }
        
    }
}
