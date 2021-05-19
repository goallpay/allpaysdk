package com.allpayx.sdk.util;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultJson {
	private JSONObject mJsonObj;
	
	public ResultJson(){
		mJsonObj=new JSONObject();
	}
	
	public void putString(String key,String value){
		try {
			mJsonObj.put(key, value);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JSONObject getJson(){
		return  mJsonObj;
	}

}
