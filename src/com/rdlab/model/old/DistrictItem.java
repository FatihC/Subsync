package com.rdlab.model.old;

import com.orm.SugarRecord;

public class DistrictItem extends SugarRecord{

	private String DistrictCode;
	private String DistrictName;
	private int CountyId;
	
	public DistrictItem() {
		// TODO Auto-generated constructor stub
	}
	
	public DistrictItem(String code,String name,int countyId) {
		// TODO Auto-generated constructor stub
		this.setCode(code);
		this.setName(name);
		this.setCountyId(countyId);
	}

	public String getCode() {
		return DistrictCode;
	}

	private void setCode(String code) {
		DistrictCode = code;
	}

	public String getName() {
		return DistrictName;
	}

	private void setName(String name) {
		DistrictName = name;
	}

	public int getCountyId() {
		return CountyId;
	}

	private void setCountyId(int relateId) {
		CountyId = relateId;
	}
	
}
