package com.allpayx.sdk.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class LoadView {
	private Context mContext;
	private TextView mTextView;
	private LinearLayout mLinearLayout;
	public LoadView(Context context){
		mContext=context;
	}
	
	public void endLoading(){
		if(mLinearLayout!=null){
			mLinearLayout.setVisibility(View.INVISIBLE);
		}
		
	}
	
	public void startLoading(){
		if(mLinearLayout!=null){
		mLinearLayout.setVisibility(View.VISIBLE);
		}
	}
	
	public  LinearLayout getLoadingView() {
		
		 mLinearLayout = new LinearLayout(mContext);
		 mLinearLayout.setVisibility(View.INVISIBLE);
		LayoutParams layoutParams1 = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParams1.gravity = Gravity.CENTER;
		mLinearLayout.setLayoutParams(layoutParams1);

		mLinearLayout.setOrientation(LinearLayout.VERTICAL);

		RelativeLayout mRelativeLayout = new RelativeLayout(mContext);
		LayoutParams layoutParams2 = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mRelativeLayout.setLayoutParams(layoutParams2);

		mLinearLayout.addView(mRelativeLayout);
		
		((Activity) mContext).getWindow().getDecorView()
		.setBackgroundColor(Color.parseColor("#80000000"));
	

//		mTextView = new TextView(mContext);
//		mTextView.setBackgroundColor(Color.parseColor("#9BBAD4"));
//		RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(
//				700, 200);
//		layoutParams3.addRule(RelativeLayout.CENTER_IN_PARENT);
//		mTextView.setLayoutParams(layoutParams3);
//		mTextView.setPadding(60, 0, 60, 0);
//		mTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
//		mTextView.setTextColor(Color.parseColor("#33271A"));
//		mRelativeLayout.addView(mTextView);	
		

//		new Thread(new Runnable() {
//			int mDotCount = 0;
//			@Override
//			public void run() {
//				while (true) {
//
//					((Activity) mContext).runOnUiThread(new Runnable() {
//						public void run() {
//							String loadStr = "Directed to payment gateway, please wait";
//							for (int i = 0; i < mDotCount; i++) {
//								loadStr = loadStr + " .";
//							}
//							mTextView.setText(loadStr);
//
//						}
//
//					});
//
//					try {
//						Thread.sleep(200);
//						mDotCount++;
//						if (mDotCount > 5) {
//							mDotCount = 0;
//						}
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//
//			}
//		}).start();
		
		
		return mLinearLayout;

	}
}
