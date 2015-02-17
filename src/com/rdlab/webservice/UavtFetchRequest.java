package com.rdlab.webservice;

public class UavtFetchRequest {
	
	private String DistrictCode;
	private Long LastProcessDate;
	
	
	public void setDistrictCode(String districtCode) {
		DistrictCode = districtCode;
	}
	
	public String getDistrictCode() {
		return DistrictCode;
	}
	
	public void setLastProcessDate(Long userSerno) {
		LastProcessDate = userSerno;
	}
	
	public Long getLastProcessDate() {
		return LastProcessDate;
	}
	
}
