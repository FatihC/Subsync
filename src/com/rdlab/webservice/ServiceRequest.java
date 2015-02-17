package com.rdlab.webservice;

import java.util.List;

import org.apache.http.NameValuePair;

public class ServiceRequest {

	String methodName;
	List<NameValuePair> params;
	
	public ServiceRequest() {
		// TODO Auto-generated constructor stub
	}
	
	public ServiceRequest(String methodName,List<NameValuePair> params) {
		// TODO Auto-generated constructor stub
		this.methodName=methodName;
		this.params=params;
	}
}
