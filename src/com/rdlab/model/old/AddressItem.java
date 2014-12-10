package com.rdlab.model.old;



public class AddressItem  {

	private int Id;
	private String CityCode;
	private String CountyCode;
	private String CountyName;
	private String DistrictCode;
	private String DistrictName;
	private String VillageCode;
	private String VillageName;
	private String StreetCode;
	private String StreetName;
	private String CSBMCode;
	private String CSBMName;
	private String BuildingCode;
	private String DoorNumber;
	private String SiteName;
	private String BlockName;
	private String UAVTAddressCode;
	private String IndoorNumber;

	public AddressItem() {
		// TODO Auto-generated constructor stub
	}

	// with id
	public AddressItem(int id, String cityCode, String countyCode,
			String countyName, String districtCode, String districtName,
			String villageCode, String villageName, String streetCode,
			String streetName, String csbmCode, String csbmName,
			String buildingCode, String doorNumber, String siteName,
			String blockName, String uavtAddressCode, String indoorNumber) {

		this.Id = id;
		this.CityCode = cityCode;
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
		this.UAVTAddressCode = uavtAddressCode;
		this.IndoorNumber = indoorNumber;
	}

	// without id
	public AddressItem(String cityCode, String countyCode, String countyName,
			String districtCode, String districtName, String villageCode,
			String villageName, String streetCode, String streetName,
			String csbmCode, String csbmName, String buildingCode,
			String doorNumber, String siteName, String blockName,
			String uavtAddressCode, String indoorNumber) {

		this.CityCode = cityCode;
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
		this.UAVTAddressCode = uavtAddressCode;
		this.IndoorNumber = indoorNumber;
	}
	
	// without id and indoorNumber
		public AddressItem(String cityCode, String countyCode, String countyName,
				String districtCode, String districtName, String villageCode,
				String villageName, String streetCode, String streetName,
				String csbmCode, String csbmName, String buildingCode,
				String doorNumber, String siteName, String blockName,
				String uavtAddressCode) {

			this.CityCode = cityCode;
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
			this.UAVTAddressCode = uavtAddressCode;
			this.IndoorNumber = "";
		}

	public int GetId() {
		return this.Id;
	}

	public String GetCityCode() {
		return this.CityCode;
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

	public String GetUAVTAddressCode() {
		return this.UAVTAddressCode;
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

}
