package com.allpayx.sdk.model;

import java.io.Serializable;

public class PayOrder implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String orderNum;
	public String orderAmount;
	public String orderCurrency;
	public String backUrl;
	public String merID;
	public String secretKey;
	public String name;
	public String ID;
	public String  paymentSchema;
	
}
