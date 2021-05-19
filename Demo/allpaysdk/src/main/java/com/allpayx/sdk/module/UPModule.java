package com.allpayx.sdk.module;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

import com.allpayx.sdk.util.JsonUtil;
import com.unionpay.UPPayAssistEx;

public class UPModule extends PayModule {

	@SuppressLint("NewApi")
	@Override
	public void pay(Activity activity, String orderNum, String paymentSchema,
			String parameter) {
		super.pay(activity, orderNum, paymentSchema, parameter);
		if (mPaymentSchema.equals("UP")) {
			String up = JsonUtil.getParam(mParameter, "up");
			final String tn = JsonUtil.getParam(up, "tn");
			final String mode = JsonUtil.getParam(up, "mode");

			if (tn.isEmpty()) {
				returnResult("fail", "merchant parameter error");
				return;
			}

			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					UPPayAssistEx.startPay(mActivity, null, null, tn, mode);

				}
			});

		} else if (mPaymentSchema.equals("UPACP")) {
			String up = JsonUtil.getParam(mParameter, "upacp");
			final String tn = JsonUtil.getParam(up, "tn");
			final String mode = JsonUtil.getParam(up, "mode");
			if (tn.isEmpty()) {
				returnResult("fail", "merchant parameter error");
				return;
			}
			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					UPPayAssistEx.startPay(mActivity, null, null, tn, mode);

				}
			});

		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (data == null) {
			return;
		}

		String str = data.getExtras().getString("pay_result");
		if (str.equals("success")) {
			returnResult("success", "pay success");

		} else if (str.equals("cancel")) {
			returnResult("cancel", "user cancel operation");
		} else {
			returnResult("fail", "pay fail");
		}

	}

}
