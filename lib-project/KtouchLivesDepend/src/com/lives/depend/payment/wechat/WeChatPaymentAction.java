package com.lives.depend.payment.wechat;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;

import com.lives.depend.DependApplication;
import com.lives.depend.payment.IPaymentAction;
import com.lives.depend.payment.PayResult;
import com.lives.depend.payment.PaymentConfig;
import com.lives.depend.payment.PaymentEnvironmentException;
import com.lives.depend.payment.PaymentResultCode;
import com.lives.depend.payment.ResultCallback;
import com.lives.depend.utils.GetResourceUtil;
import com.lives.depend.utils.LogUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * ****************************************************************<br>
 * 文件名称 : WeChatPaymentAction.java<br>
 * 作 者 : putao_xcx<br>
 * 创建时间 : 2015-11-24 下午7:48:21<br>
 * 文件描述 : 微信支付实现 <br>
 * 版权声明 :深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-24 1.00 初始版本<br>
 ***************************************************************** 
 */
public class WeChatPaymentAction implements IPaymentAction
{
    private static final String TAG = "WeChatPaymentAction";

    @Override
    public int getActionType()
    {
        return PaymentConfig.TYPE_WE_CHAT;
    }

    @Override
    public String getPutaoCreateOrderUrl()
    {
        return PaymentConfig.PAY.WECHAT_CREATE_ORDER_URL;
    }

    @Override
    public Map<String, String> convertToParamMap(JSONObject paymentBean, Map<String, String> queryMap) throws Exception
    {
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("appId", paymentBean.getString("app_id"));
        hashMap.put("partnerId", paymentBean.getString("partner_id"));
        hashMap.put("prepayId", paymentBean.getString("prepay_id"));
        hashMap.put("nonceStr", paymentBean.getString("nonce_str"));
        hashMap.put("timeStamp", paymentBean.getString("timestamp"));
        hashMap.put("packageValue", paymentBean.getString("package_value"));
        hashMap.put("sign", paymentBean.getString("sign"));
        LogUtil.d(TAG, "convert param map: json = " + paymentBean + ", map = " + hashMap);
        return hashMap;
    }

    @Override
    public String cutomizePostBody(String postBody)
    {
        return postBody + "&pay_channel_no=" + WeChatConfig.getWxChannelNo(DependApplication.getContext());
    }

    @Override
    public void checkPaymentEnvironment(Activity activity) throws PaymentEnvironmentException
    {
        if (!isWXAppInstalledAndSupported(activity, WXAPIFactory.createWXAPI(activity, "")))
        {
            throw new PaymentEnvironmentException(GetResourceUtil.getString(activity, "putao_pay_by_no_wechat"));
        }

    }

    private static void checkParams(Map<String, String> map, String key)
    {
        if (!map.containsKey(key))
        {
            throw new IllegalArgumentException("wechat " + key + "  not found!");
        }
    }

    private static boolean isWXAppInstalledAndSupported(Context context, IWXAPI api)
    {
        boolean sIsWXAppInstalledAndSupported = api.isWXAppInstalled() && api.isWXAppSupportAPI();
        return sIsWXAppInstalledAndSupported;
    }

    @Override
    public void doPayment(Activity activity, Map<String, String> map, ResultCallback cb) throws Exception
    {
        PayResult result = new PayResult();
        checkParams(map, "appId");
        checkParams(map, "partnerId");
        checkParams(map, "prepayId");
        checkParams(map, "nonceStr");
        checkParams(map, "timeStamp");
        checkParams(map, "packageValue");
        checkParams(map, "sign");
        String appId = map.get("appId");
        WeChatConfig.WX_PAY_APPID = appId;
        IWXAPI api = WXAPIFactory.createWXAPI(activity, appId);
        if (!isWXAppInstalledAndSupported(activity, api))
        {
            result.resultCode = PaymentResultCode.Result.Failed;
            result.errorCode = PaymentResultCode.ErrorCode.UnknownErr;

            // 需修改
            result.errorDesc =GetResourceUtil.getString(activity, "putao_pay_by_no_wechat");
        }
        PayReq req = new PayReq();
        req.appId = appId;
        req.partnerId = map.get("partnerId");
        req.prepayId = map.get("prepayId");
        req.nonceStr = map.get("nonceStr");
        req.timeStamp = map.get("timeStamp");
        req.packageValue = map.get("packageValue");
        req.sign = map.get("sign");
        req.extData = map.get("extData");
        api.registerApp(appId);
        boolean isSuccess = api.sendReq(req);
        if (isSuccess)
        {
            result.resultCode = PaymentResultCode.Result.WaitForResult;
        }
        else
        {
            result.resultCode = PaymentResultCode.Result.Failed;
        }
        cb.onPaymentFeedback(result);
    }
}
