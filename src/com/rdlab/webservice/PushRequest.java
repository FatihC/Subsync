package com.rdlab.webservice;


public class PushRequest {
	
	private Long userSerno;
	private Long districtCode;
	private Long createDate;
	private Long uavtCode;
	private String wiringNo;
	private String meterNo;
	private String meterBrand;
	private Integer checkStatus; // biirm yok, kapali, sayac var ulasilamadi....
	private Integer existOnUavt;  // 0-exist, 1-not
	private String doorNumber;
	private String customerName;
	
	
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
	public Long getDistrictCode() {
		return districtCode;
	}
	public void setDistrictCode(Long districtCode) {
		this.districtCode = districtCode;
	}
	public Long getUavtCode() {
		return uavtCode;
	}
	public void setUavtCode(Long uavtCode) {
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
	public Integer getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(Integer checkStatus) {
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
	
	
	
	

}