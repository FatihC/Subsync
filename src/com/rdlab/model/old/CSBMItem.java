package com.rdlab.model.old;

import com.orm.SugarRecord;

public class CSBMItem extends SugarRecord {

	private String CSBMCode;
	private String CSBMName;
	private int StreetId;
	
	public CSBMItem() {
		// TODO Auto-generated constructor stub
	}
	
	public CSBMItem(String code,String name,int relateId) {
		// TODO Auto-generated constructor stub
		this.setCode(code);
		this.setName(name);
		this.setStreetId(relateId);
	}

	public String getCode() {
		return CSBMCode;
	}

	private void setCode(String code) {
		CSBMCode = code;
	}

	public String getName() {
		return CSBMName;
	}

	private void setName(String name) {
		CSBMName = name;
	}

	public int getStreetId() {
		return StreetId;
	}

	private void setStreetId(int relateId) {
		StreetId = relateId;
	}
}
