package com.allpayx.sdk.activity;


import com.allpayx.sdk.AllPayEngine;
import com.allpayx.sdk.constants.AllPayConst;
import com.allpayx.sdk.module.PayModule;
import com.allpayx.sdk.util.HttpUtil;
import com.allpayx.sdk.util.JsonUtil;
import com.allpayx.sdk.util.NetworkUtils;
import com.allpayx.sdk.util.ResultJson;
import com.allpayx.sdk.util.ServerUtil;
import com.allpayx.sdk.view.LoadView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

import java.util.HashMap;
import java.util.Map;


public class AllPayActivity extends Activity {
	private final String TAG = "AllPayActivity";
	private Intent mIntent;
	private String mTn;
	private String mParameter;
	private String mOrderNum;
	private String mPaymentSchema;
	private LoadView mLoadView;
	private ResultJson mResultJson;
	private Context mContext;
    private PayModule mPayModule;
    private long firstTime = 0; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mResultJson = new ResultJson();
		mIntent = getIntent();
		mTn = mIntent.getStringExtra("tn");
		mLoadView = new LoadView(mContext);
		setContentView(mLoadView.getLoadingView());
		mLoadView.startLoading();
		firstTime= System.currentTimeMillis();

		if(!NetworkUtils.isConnected(mContext)){
			returnResult("fail","Network error");
			return;
		}

		new Thread(new Runnable() {
			@Override
			public void run() {

				String phoneModel=android.os.Build.MODEL;
				String sdkVersion="5.0.0";
				String systemVersion=android.os.Build.VERSION.RELEASE;

				Log.i(TAG, "phoneModel:"+phoneModel+"\nsystemVersion"+systemVersion+"\nsdkVersion:"+sdkVersion);
				Map<String,String> params = new HashMap<>();
				params.put("tn", mTn);
				params.put("phoneModel", phoneModel);
				params.put("systemVersion", systemVersion);
				params.put("sdkVersion", sdkVersion);
				params.put("terminalId", "");

				String url = ServerUtil.SERVER_URL;
				if(AllPayEngine.isDebug){
					url = ServerUtil.SERVER_URL_TEST;
				}

				//url = "http://172.30.1.112:8080";

				String result = HttpUtil.reqStr(url+"/api/getPayment", params,HttpUtil.METHOD_POST);

				String code = JsonUtil.getParam(result,
						"RespCode");

				if(!code.equals("00")){
					returnResult("fail",JsonUtil.getParam(result,
							"RespMsg"));
					return;

				}


				mPaymentSchema = JsonUtil.getParam(result,
						"paymentSchema");
				mTn = JsonUtil.getParam(result, "tn");
				mParameter = JsonUtil.getParam(result, "Parameter");
				mOrderNum = JsonUtil.getParam(result, "orderNum");
				String module=JsonUtil.getParam(result, "module");
				//module="MUPModule";
				String moduleClass="com.allpayx.sdk.module."+module;
				try {
					Class<?>payModule=Class.forName(moduleClass);
					mPayModule=(PayModule) payModule.newInstance();


				} catch (Exception e) {

					e.printStackTrace();
				}

				if(mPayModule==null){
					returnResult("fail",module+" is not addded");
				}else{
					runOnUiThread(new Runnable()
					{
						public void run()
						{
							mPayModule.pay(AllPayActivity.this, mOrderNum, mPaymentSchema, mParameter);
						}

					});

				}



			}
		}).start();





	}

	

	

	
	
	
	@Override
	protected void onDestroy() {
		if(mPayModule!=null){
			mPayModule.onDestroy();
		}
		
		super.onDestroy();
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(mPayModule!=null){
			mPayModule.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mLoadView.endLoading();
		
		 if(mPayModule!=null){
	        	mPayModule.onPause();
	        }
	}

	
	@Override
	protected void onResume() {
		super.onResume();
        if(mPayModule!=null){
        	mPayModule.onResume();
        }
	}
	
	private void returnResult(String state,String errorDetail){
		mResultJson.putString("state",state);
		mResultJson.putString("errorDetail",
				 errorDetail);
		mResultJson.putString("orderNum", mOrderNum);
		mResultJson.putString("paymentSchema",
				mPaymentSchema);
		mIntent.putExtra("pay_result", mResultJson
				.getJson().toString());
		setResult(AllPayConst.VTPAY_PAY_RESULT_CODE,
				mIntent);
		finish();
	}
	

	
	  @Override  
	  public boolean onKeyDown(int keyCode, KeyEvent event) {  
	         // TODO Auto-generated method stub  
	         switch(keyCode)  
	         {  
	         case KeyEvent.KEYCODE_BACK:  
	              long secondTime = System.currentTimeMillis();
	              Log.e(TAG, "secondTime - firstTime:"+(secondTime - firstTime));
	               if (secondTime - firstTime < 6000) {                                         //如果两次按键时间间隔大于2秒，则不退出  
	                   
	                   return false;   
	               }  
	             break;
	             
	         }  
	       return super.onKeyDown(keyCode, event);  
	     }  

}
