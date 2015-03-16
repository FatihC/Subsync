package com.rdlab.model;

import com.orm.SugarRecord;

public class AuditLog extends SugarRecord {

	public Long UserSerno;
	public String AuditOptionSelection;
	public String AuditFormSerno;
	public String AuditFormSernoText;
	public String AuditFormDescription;
	public String AuditProgressStatus;
	public String AuditedCheckStatus;
	public String AuditStatus;
	public String DistrictCode;
	public String VillageCode;
	public String StreetCode;
	public String CsbmCode;
	public String DoorNumber;
	public String SiteName;
	public String BlockName;
	public String IndoorNumber;
	public String UavtCode;
	public Long CreateDate;
	public boolean Pushed;
	public Integer RecordStatus;

	@Override
	public String toString() {
		return this.IndoorNumber;
	}
}
