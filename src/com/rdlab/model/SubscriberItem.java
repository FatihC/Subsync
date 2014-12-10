package com.rdlab.model;

public class SubscriberItem {
	Long TesisatNo;
	Long IlkSozlesmeTarihi;
	String Unvan;
	
	public SubscriberItem() {
		// TODO Auto-generated constructor stub
	}
	
	public SubscriberItem(Long tesisatNo,Long ilk,String unvan) {
		// TODO Auto-generated constructor stub
		this.TesisatNo=tesisatNo;
		this.IlkSozlesmeTarihi=ilk;
		this.Unvan=unvan;
	}
	
	public Long getTesisatNo(){
		return this.TesisatNo;
	}
	
	public Long getIlkSozlesmeTarihi(){
		return this.IlkSozlesmeTarihi;
	}
	
	public String getUnvan(){
		return this.Unvan;
	}
}
