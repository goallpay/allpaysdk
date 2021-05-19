package com.allpayx.sdk.module;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.allpayx.sdk.constants.AllPayConst;
import com.allpayx.sdk.util.PayResult;

public class ALIModule extends PayModule {
	private final String TAG = "ALIModule";
	private Handler mHandler; 
		
 @SuppressLint("NewApi") @Override
public void pay(Activity activity, String orderNum, String paymentSchema,
		String parameter) {
	super.pay(activity, orderNum, paymentSchema, parameter);
	activity.runOnUiThread(new Runnable() {
		
		@Override
		public void run() {
			initHandler();
			if(!mParameter.isEmpty()){
				Message msg = new Message();
				msg.what = AllPayConst.MSG_VTPAY_PAY_START_AP;
				msg.obj = mParameter;
				mHandler.sendMessageDelayed(msg, 0);
			}else{
				returnResult("fail","merchant parameter error");
			}
		}
	});
	
	
	
}
 
 private void initHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case AllPayConst.MSG_VTPAY_PAY_START_AP:
					final String result = (String) msg.obj;
					if (!result.contains("partner")) {					
						returnResult("fail","Order parameter error");
						return;
					}
					Runnable payRunnable = new Runnable() {

						@Override
						public void run() {
							// 构造PayTask 对象
							PayTask alipay = new PayTask(mActivity);
							// 调用支付接口，获取支付结果
							Log.i(TAG, result);
							String pay_result = alipay.pay(result,true);
							Log.i(TAG, pay_result);
							PayResult payResult = new PayResult(pay_result);
							String resultStatus = payResult.getResultStatus();
							if (resultStatus.equals("9000")) {						
								returnResult("success","pay success");
							} else if (resultStatus.equals("6001")) {
								returnResult("cancel","user cancel operation");
							} else {
								returnResult("fail","pay fail");
							}

						}
					};

					// 必须异步调用
					Thread payThread = new Thread(payRunnable);
					payThread.start();

					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
	}
 
 

}
