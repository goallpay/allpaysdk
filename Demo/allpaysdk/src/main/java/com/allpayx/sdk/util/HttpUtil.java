package com.allpayx.sdk.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * HTTP请求工具类
 * @author zhuwei
 *
 */
public class HttpUtil {
	public final static String METHOD_GET = "GET";
	public final static String METHOD_PUT = "PUT";
	public final static String METHOD_DELETE = "DELETE";
	public final static String METHOD_POST = "POST";
	
	/**
	 * 请求成功,返回字符串
	 * @param serviceUrl 地址
	 * @param parameter 参数
	 * @param restMethod 方法类型
	 * @return
	 */
	public static String reqStr(String serviceUrl, Map<String,String> parameter,String restMethod) {
		System.out.println(serviceUrl);
		StringBuffer result = new StringBuffer("");
		try {

			List<String> keys = new ArrayList<String>(parameter.keySet());
			Collections.sort(keys);
			String prestr = "";
			for (int i = 0; i < keys.size(); i++) {
				String key = (String) keys.get(i);
				String value = (String) parameter.get(key);
				if (i == keys.size() - 1) {
					prestr = prestr + key + "=" + URLEncoder.encode(value, "UTF-8");
				} else {
					prestr = prestr + key + "=" +  URLEncoder.encode(value, "UTF-8") + "&";
				}
			}


			URL url = new URL(serviceUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(restMethod);
			// 如果请求方法为PUT,POST和DELETE设置DoOutput为真
			if (!METHOD_GET.equals(restMethod)) {
				con.setDoOutput(true);
				if (!METHOD_DELETE.equals(restMethod)) { // 请求方法为PUT或POST时执行
					OutputStream os = con.getOutputStream();
					os.write(prestr.getBytes("UTF-8"));
					os.close();
				}
			}
			
			InputStream is= con.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is,"UTF-8"));
		    String line = "";
		    while ((line = in.readLine()) != null){
			  result.append(line);
		    }
			 
		} catch (Exception e) {
			Log.e("HttpUtil",""+e.toString());
			e.printStackTrace();
		}

		return result.toString();

	}


}
