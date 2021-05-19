package com.allpayx.sdk.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.allpayx.sdk.AllPayEngine;
import com.allpayx.sdk.module.WXModule;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate" );
    	api = WXAPIFactory.createWXAPI(this, WXModule.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

			AllPayEngine.mWxPayErrorcode=resp.errCode;
			finish();
		}
	}
	
	@Override
	protected void onPause() {
		Log.d(TAG, "onPause" );
		super.onPause();
	}
	
	
	@Override
	protected void onResume() {
		Log.d(TAG, "onResume" );
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy" );
		super.onDestroy();
	}
	
}