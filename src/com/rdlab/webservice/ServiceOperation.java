package com.rdlab.webservice;

public enum ServiceOperation {

	Login(1),
	Push(2),
	FetchRequest(3),
	FetchUserRequest(4),
	FetchMBS(5),
	FetchNewUavt(6),
	Error(5);
	
	private int Value;
	private ServiceOperation(int value) {
		// TODO Auto-generated constructor stub
		this.Value=value;
	}
	public int getVal(){
		return this.Value;
	}
}
