package com.rdlab.model.mardin;

import com.orm.SugarRecord;

public class DerikMbs extends SugarRecord {
	Long TesisatNo;
	Long IlkSozlesmeTarihi;
	String Unvan;
	
	public DerikMbs() {
		// TODO Auto-generated constructor stub
	}
	
	public DerikMbs(Long tesisatNo,Long ilk,String unvan) {
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