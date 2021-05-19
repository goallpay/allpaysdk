package com.allpayx.sdk.module;

import com.allpayx.sdk.constants.AllPayConst;
import com.allpayx.sdk.util.ResultJson;
import android.app.Activity;
import android.content.Intent;

public class PayModule {
    protected Activity mActivity;
    protected String mOrderNum;
    protected String mPaymentSchema;
    protected String mParameter;
    protected Intent mIntent;
    protected ResultJson mResultJson;
	public void pay( Activity activity,String orderNum,String paymentSchema,String parameter){
		mActivity=activity;
		mOrderNum=orderNum;
		mPaymentSchema=paymentSchema;
		mParameter=parameter;
		mIntent=activity.getIntent();
		mResultJson=new ResultJson();
	}
	
	public void onResume(){
		
	}
	
	public void onPause(){
		
	}
	
    public void onDestroy(){
		
	}
    
    
    public void onStart(){
		
   	}
	
    public void onActivityResult(int requestCode, int resultCode, Intent data){
		
   	}
   
   
   public void returnResult(String state,String errorDetail){
		mResultJson.putString("state",state);
		mResultJson.putString("errorDetail",
				 errorDetail);
		mResultJson.putString("orderNum", mOrderNum);
		mResultJson.putString("paymentSchema",
				mPaymentSchema);
		mIntent.putExtra("pay_result", mResultJson
				.getJson().toString());
		mActivity.setResult(AllPayConst.VTPAY_PAY_RESULT_CODE,
				mIntent);
		mActivity.finish();
	}
	
}
