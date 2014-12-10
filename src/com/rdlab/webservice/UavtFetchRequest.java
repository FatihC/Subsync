package com.rdlab.webservice;

import org.parceler.Parcel;

import com.google.gson.annotations.SerializedName;

@Parcel
public class UavtFetchRequest {
	
	@SerializedName("DistrictCode")
	private Long DistrictCode;
	
	@SerializedName("UserSerno")
	private Long UserSerno;
	
	
	public void setDistrictCode(Long districtCode) {
		DistrictCode = districtCode;
	}
	
	public Long getDistrictCode() {
		return DistrictCode;
	}
	
	public void setUserSerno(Long userSerno) {
		UserSerno = userSerno;
	}
	
	public Long getUserSerno() {
		return UserSerno;
	}
	
}
