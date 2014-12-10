package com.rdlab.model;

public class UnitItem {

	private String IndoorNumber;
	private String WiringNo;
	private String SubscriberName;
	private String MeterpointBrand;
	private String MeterpointSerno;
	private String StatusName;
	private String StatusCode;
	private String UAVTNo;
	private boolean Synced;
	
	public UnitItem() {
		// TODO Auto-generated constructor stub
	}
	
	public UnitItem(String indoor,String uavt,boolean synced) {
		// TODO Auto-generated constructor stub
		this.IndoorNumber=indoor;
		this.UAVTNo=uavt;
		this.Synced=synced;
	}
	
	public UnitItem(String indoor,String wiring,String sbsName,String mpName,String mpSer,String stat,String statCode,boolean synced) {
		// TODO Auto-generated constructor stub
		this.IndoorNumber=indoor;
		this.WiringNo=wiring;
		this.SubscriberName=sbsName;
		this.MeterpointBrand=mpName;
		this.MeterpointSerno=mpSer;
		this.StatusCode=statCode;
		this.StatusName=stat;
		this.Synced=synced;
	}

	public String getIndoorNumber() {
		return IndoorNumber;
	}

	public void setIndoorNumber(String indoorNumber) {
		IndoorNumber = indoorNumber;
	}

	public String getWiringNo() {
		return WiringNo;
	}

	public void setWiringNo(String wiringNo) {
		WiringNo = wiringNo;
	}

	public String getSubscriberName() {
		return SubscriberName;
	}

	public void setSubscriberName(String subscriberName) {
		SubscriberName = subscriberName;
	}

	public String getMeterpointBrand() {
		return MeterpointBrand;
	}

	public void setMeterpointBrand(String meterpointBrand) {
		MeterpointBrand = meterpointBrand;
	}

	public String getMeterpointSerno() {
		return MeterpointSerno;
	}

	public void setMeterpointSerno(String meterpointSerno) {
		MeterpointSerno = meterpointSerno;
	}

	public String getStatusName() {
		return StatusName;
	}

	public void setStatusName(String statusName) {
		StatusName = statusName;
	}

	public String getStatusCode() {
		return StatusCode;
	}

	public void setStatusCode(String statusCode) {
		StatusCode = statusCode;
	}

	public boolean isSynced() {
		return Synced;
	}

	public void setSynced(boolean synced) {
		Synced = synced;
	}

	public String getUAVTNo() {
		return UAVTNo;
	}

	public void setUAVTNo(String uAVTNo) {
		UAVTNo = uAVTNo;
	}
}
