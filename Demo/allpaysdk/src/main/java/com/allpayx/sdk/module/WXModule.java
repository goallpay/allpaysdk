package com.allpayx.sdk.module;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.allpayx.sdk.AllPayEngine;
import com.allpayx.sdk.constants.AllPayConst;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WXModule extends PayModule {
	private final int  WX_USER_NOPAY=-200; //用户未发起支付
	private final int  WX_USER_NO_CLICK_FINISHED=-201; //用户未点击完成按钮
	private final int  WX_USER_PAY_CANCEL=-2;  //调起支付后取消
	private final int  WX_USER_PAY_SUCCESS=0; //支付成功
	public static String APP_ID;
     @SuppressLint("NewApi") @Override
    public void pay(Activity activity, String orderNum, String paymentSchema,
    		String parameter) {
    	super.pay(activity, orderNum, paymentSchema, parameter);
    	
    	if(!mParameter.isEmpty()){
    		final IWXAPI api = WXAPIFactory.createWXAPI(
					mActivity, getQueryString("&"+mParameter,"appid"));
    		APP_ID=getQueryString("&"+mParameter,"appid");
			if (!isWXAppInstalledAndSupported(mActivity, api)) {
				Toast toast = Toast.makeText(
						mActivity,
						"You did not install the WeChat！",
						Toast.LENGTH_SHORT);
				toast.show();
				returnResult("fail","You did not install the WeChat");
			
			}else{
				AllPayEngine.mWxPayErrorcode=WX_USER_NOPAY;
				PayReq req = new PayReq();
				req.appId =getQueryString("&"+mParameter,"appid");
				req.partnerId = getQueryString(mParameter,"partnerid");;
				req.prepayId = getQueryString(mParameter,"prepayid");;
				req.nonceStr = getQueryString(mParameter,"noncestr");;
				req.timeStamp = getQueryString(mParameter,"timestamp");;
				req.packageValue = getQueryString(mParameter,"package");;
	            req.sign=getQueryString(mParameter,"sign");
				api.sendReq(req);
				AllPayEngine.mWxPayErrorcode=WX_USER_NO_CLICK_FINISHED;
			}
		}else{
			returnResult("fail","merchant parameter error");
		}
    	
    }
     
     
     @Override
    public void onResume() {
    	super.onResume();
    	Log.i("opp", "onResume " + AllPayEngine.mWxPayErrorcode);

		if (AllPayEngine.mWxPayErrorcode != WX_USER_NOPAY) {
			if (AllPayEngine.mWxPayErrorcode == WX_USER_PAY_SUCCESS) {
				returnResult("success","pay success");

			} else if (AllPayEngine.mWxPayErrorcode == WX_USER_PAY_CANCEL) {
				returnResult("cancel","user cancel operation");
			} else if (AllPayEngine.mWxPayErrorcode == WX_USER_NO_CLICK_FINISHED) {
				returnResult("query","need to confirm payment results like server");
			}else {
				returnResult("fail","pay fail");
			}
			
			
			mActivity.setResult(AllPayConst.VTPAY_PAY_RESULT_CODE, mIntent);

			mActivity.finish();
			

		} 
		
		
    	
    }
     
     
     private static boolean isWXAppInstalledAndSupported(Context context,
 			IWXAPI api) {
 		// LogOutput.d(TAG, "isWXAppInstalledAndSupported");
    	 boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;

 		return isPaySupported;
 	}
     
     private String getQueryString(String oldStr,String name){
    		
    	 String result="";
    	 Pattern pt=Pattern.compile("(^|&)" + name + "=()([^&]*)");
    	 Matcher mt=pt.matcher(oldStr);
    	 if(mt.find()){
    	 result=mt.group(0).toString().substring(name.length()+2);
    	 //System.out.println(result);
    	 }
    	
    	 return result;
    	 }
}
