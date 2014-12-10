package com.rdlab.model.old;

import com.orm.SugarRecord;

public class CountyItem extends SugarRecord{

	private String CountyCode;
	private String CountyName;
	
	public CountyItem() {
		// TODO Auto-generated constructor stub
	}
	
	public CountyItem(String code,String name) {
		// TODO Auto-generated constructor stub
		this.setCode(code);
		this.setName(name);
	}

	public String getCode() {
		return CountyCode;
	}

	private void setCode(String code) {
		CountyCode = code;
	}

	public String getName() {
		return CountyName;
	}

	private void setName(String name) {
		CountyName = name;
	}
	
	
}
