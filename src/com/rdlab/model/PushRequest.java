package com.rdlab.model;

import com.orm.SugarRecord;

public class PushRequest extends SugarRecord{

	public Long userSerno;
	public String  districtCode;
	public Long createDate;
	public String uavtCode;
	public String wiringNo;
	public String meterNo;
	public String meterBrand;
	public String  checkStatus; // biirm yok, kapali, sayac var ulasilamadi....
	public Integer existOnUavt;  // 0-exist, 1-not
	public String doorNumber;
	public String customerName;
	public Boolean pushed;
	public String villageCode;
	public String streetCode;
	public String csbmCode;
	public String indoorNumber;
	
	
	
	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}
	
	public Long getCreateDate() {
		return createDate;
	}
	
	public Long getUserSerno() {
		return userSerno;
	}
	public void setUserSerno(Long userSerno) {
		this.userSerno = userSerno;
	}
	public String getDistrictCode() {
		return districtCode;
	}
	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}
	public String getUavtCode() {
		return uavtCode;
	}
	public void setUavtCode(String uavtCode) {
		this.uavtCode = uavtCode;
	}
	public String getWiringNo() {
		return wiringNo;
	}
	public void setWiringNo(String wiringNo) {
		this.wiringNo = wiringNo;
	}
	public String getMeterNo() {
		return meterNo;
	}
	public void setMeterNo(String meterNo) {
		this.meterNo = meterNo;
	}
	public String getMeterBrand() {
		return meterBrand;
	}
	public void setMeterBrand(String meterBrand) {
		this.meterBrand = meterBrand;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public Integer getExistOnUavt() {
		return existOnUavt;
	}
	public void setExistOnUavt(Integer existOnUavt) {
		this.existOnUavt = existOnUavt;
	}
	public String getDoorNumber() {
		return doorNumber;
	}
	public void setDoorNumber(String doorNumber) {
		this.doorNumber = doorNumber;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public boolean isPushed() {
		return pushed;
	}

	public void setPushed(boolean pushed) {
		this.pushed = pushed;
	}

	public String getVillageCode() {
		return villageCode;
	}

	public void setVillageCode(String villageCode) {
		this.villageCode = villageCode;
	}

	public String getStreetCode() {
		return streetCode;
	}

	public void setStreetCode(String streetCode) {
		this.streetCode = streetCode;
	}

	public String getCsbmCode() {
		return csbmCode;
	}

	public void setCsbmCode(String csbmCode) {
		this.csbmCode = csbmCode;
	}

	public String getIndoorNumber() {
		return indoorNumber;
	}

	public void setIndoorNumber(String indoorNumber) {
		this.indoorNumber = indoorNumber;
	}
}
