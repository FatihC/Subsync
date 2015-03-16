package com.rdlab.model.mardin;

import com.orm.SugarRecord;

public class NusaybinMbs extends SugarRecord {
	Long TesisatNo;
	Long IlkSozlesmeTarihi;
	String Unvan;
	
	public NusaybinMbs() {
		// TODO Auto-generated constructor stub
	}
	
	public NusaybinMbs(Long tesisatNo,Long ilk,String unvan) {
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