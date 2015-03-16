package com.rdlab.model.mardin;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class Yesilli extends SugarRecord {
	public String CountyCode;
	public String CountyName;
	public String DistrictCode;
	public String DistrictName;
	public String VillageCode;
	public String VillageName;
	public String StreetCode;
	public String StreetName;
	public String CSBMCode;
	public String CSBMName;
	public String BuildingCode;
	public String DoorNumber;
	public String SiteName;
	public String BlockName;
	public String UAVTAddressNo;
	public String IndoorNumber;
	private int CheckStatus;
	public int isCheckStatus() {
		return CheckStatus;
	}

	public void setCheckStatus(int checkStatus) {
		CheckStatus = checkStatus;
	}
	@Ignore
	private int UnitCount;
	
	public int getUnitCount(){
		return UnitCount;
	}

	public Yesilli() {
		// TODO Auto-generated constructor stub
	}

	// without id
	public Yesilli(String countyCode, String countyName, String districtCode,
			String districtName, String villageCode, String villageName,
			String streetCode, String streetName, String csbmCode,
			String csbmName, String buildingCode, String doorNumber,
			String siteName, String blockName, String UAVTAddressNo,
			String indoorNumber,int check) {

		this.CountyCode = countyCode;
		this.CountyName = countyName;
		this.DistrictCode = districtCode;
		this.DistrictName = districtName;
		this.VillageCode = villageCode;
		this.VillageName = villageName;
		this.StreetCode = streetCode;
		this.StreetName = streetName;
		this.CSBMCode = csbmCode;
		this.CSBMName = csbmName;
		this.BuildingCode = buildingCode;
		this.DoorNumber = doorNumber;
		this.SiteName = siteName;
		this.BlockName = blockName;
		this.UAVTAddressNo = UAVTAddressNo;
		this.IndoorNumber = indoorNumber;
		this.CheckStatus=check;
	}

	public String GetCountyCode() {
		return this.CountyCode;
	}

	public String GetCountyName() {
		return this.CountyName;
	}

	public String GetDistrictCode() {
		return this.DistrictCode;
	}

	public String GetDistrictName() {
		return this.DistrictName;
	}

	public String GetVillageCode() {
		return this.VillageCode;
	}

	public String GetVillageName() {
		return this.VillageName;
	}

	public String GetStreetCode() {
		return this.StreetCode;
	}

	public String GetStreetName() {
		return this.StreetName;
	}

	public String GetCSBMCode() {
		return this.CSBMCode;
	}

	public String GetCSBMName() {
		return this.CSBMName;
	}

	public String GetBuildingCode() {
		return this.BuildingCode;
	}

	public String GetDoorNumber() {
		return this.DoorNumber;
	}

	public String GetUAVTAddressNo() {
		return this.UAVTAddressNo;
	}

	public String GetIndoorNumber() {
		return this.IndoorNumber;
	}

	public String GetSiteName() {
		return this.SiteName;
	}

	public String GetBlockName() {
		return this.BlockName;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.DistrictCode + "  " + this.DistrictName;
	}
}
