package com.rdlab.model.old;

import com.orm.SugarRecord;

public class StreetItem extends SugarRecord {

	private String StreetCode;
	private String StreetName;
	private int VillageId;
	
	public StreetItem() {
		// TODO Auto-generated constructor stub
	}
	
	public StreetItem(String code,String name,int relateId) {
		// TODO Auto-generated constructor stub
		this.setCode(code);
		this.setName(name);
		this.setVillageId(relateId);
	}

	public String getCode() {
		return StreetCode;
	}

	private void setCode(String code) {
		StreetCode = code;
	}

	public String getName() {
		return StreetName;
	}

	private void setName(String name) {
		StreetName = name;
	}

	public int getRelateId() {
		return VillageId;
	}

	private void setVillageId(int relateId) {
		VillageId = relateId;
	}
}
