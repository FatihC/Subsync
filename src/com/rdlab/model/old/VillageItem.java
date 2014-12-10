package com.rdlab.model.old;

import com.orm.SugarRecord;

public class VillageItem extends SugarRecord {

	private String VillageCode;
	private String VillageName;
	private int DistrictId;
	
	public VillageItem() {
		// TODO Auto-generated constructor stub
	}
	
	public VillageItem(String code,String name,int relateId) {
		// TODO Auto-generated constructor stub
		this.setCode(code);
		this.setName(name);
		this.setDistrictId(relateId);
	}

	public String getCode() {
		return VillageCode;
	}

	private void setCode(String code) {
		VillageCode = code;
	}

	public String getName() {
		return VillageName;
	}

	private void setName(String name) {
		VillageName = name;
	}

	public int getDistrictId() {
		return DistrictId;
	}

	private void setDistrictId(int relateId) {
		DistrictId = relateId;
	}
}