package com.allpayx.sdk;



import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.allpayx.sdk.activity.AllPayActivity;
import com.allpayx.sdk.constants.AllPayConst;


public class AllPayEngine {
	private static final String TAG = "AllPayEngine";
	public static int mWxPayErrorcode=-200;
	public static boolean isDebug=false;
	
	public static void pay(Activity activity,String tn,boolean mode){
		Log.d(TAG, "pay" );
		AllPayEngine.isDebug = !mode;
		Intent intent=new Intent(activity, AllPayActivity.class);
    	intent.putExtra("tn", tn);
    	mWxPayErrorcode=-200;
    	activity.startActivityForResult(intent, AllPayConst.VTPAY_PAY_REQUESR_CODE);
		
	}

	
  
}
