package com.allpayx.sdk.listener;

public interface CommunicationListener {
	public void onResult(String result);
	public void onError(String error);
}
