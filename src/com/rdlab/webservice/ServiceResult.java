package com.rdlab.webservice;

public class ServiceResult {

	public ServiceOperation operation;
	public Object data;
	
	public ServiceResult() {
		// TODO Auto-generated constructor stub
	}
	
	public ServiceResult(ServiceOperation operation,Object data) {
		// TODO Auto-generated constructor stub
		this.operation=operation;
		this.data=data;
	}
	
	public ServiceOperation getOperation() {
		return operation;
	}
	public void setOperation(ServiceOperation operation) {
		this.operation = operation;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
