package com.rdlab.model.diyarbakir;

import com.orm.SugarRecord;


public class CermikUM extends SugarRecord {
	Long UserSerno;
	Long UavtAddressNo;
	String CustomerName;
	String WiringNo;
	String MeterNo;
	String MeterBrand;
	Integer CheckStatus; // biirm yok, kapali, sayac var ulasilamadi....
	Integer ExistOnUavt; // 0-exist, 1-not
	Long CreateDate;
	boolean Synchronised;

	public CermikUM() {
		// TODO Auto-generated constructor stub
	}

	public CermikUM(Long userSerno, Long uavtAddressNo, String customerName,
			String wiringNo, String meterNo, String meterBrand,
			Integer checkStatus, Integer existOnUavt, Long createDate,
			boolean sychronised) {
		this.UserSerno = userSerno;
		this.UavtAddressNo = uavtAddressNo;
		this.CustomerName = customerName;
		this.WiringNo = wiringNo;
		this.MeterNo = meterNo;
		this.MeterBrand = meterBrand;
		this.CheckStatus = checkStatus;
		this.ExistOnUavt = existOnUavt;
		this.CreateDate = createDate;
		this.Synchronised = sychronised;
	}

	public CermikUM(Long uavtAddressNo, String customerName, String wiringNo,
			String meterNo, String meterBrand, Integer checkStatus,
			Integer existOnUavt, Long createDate, boolean sychronised) {
		this.UavtAddressNo = uavtAddressNo;
		this.CustomerName = customerName;
		this.WiringNo = wiringNo;
		this.MeterNo = meterNo;
		this.MeterBrand = meterBrand;
		this.CheckStatus = checkStatus;
		this.ExistOnUavt = existOnUavt;
		this.CreateDate = createDate;
		this.Synchronised = sychronised;
	}

	public Long getUserSerno() {
		return this.UserSerno;
	}

	public Long getUavtAddressNo() {
		return this.UavtAddressNo;
	}

	public String getCustomerName() {
		return this.CustomerName;
	}

	public String getWiringNo() {
		return this.WiringNo;
	}

	public String getMeterNo() {
		return this.MeterNo;
	}

	public String getMeterBrand() {
		return this.MeterBrand;
	}

	public Integer getCheckStatus() {
		return this.CheckStatus;
	}

	public Integer getExistOnUavt() {
		return this.ExistOnUavt;
	}

	public Long getCreateDate() {
		return this.CreateDate;
	}

	public boolean isSynchronised() {
		return Synchronised;
	}
}
