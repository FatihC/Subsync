package com.rdlab.model;

public class BlockItem {

	private String DoorNumber;
	private String SiteName;
	private String BlockName;
	boolean IsNewlyAdded;
	private int CheckStatus;
	private int UnitCount;
	private long Id;

	public BlockItem() {
		// TODO Auto-generated constructor stub
	}

	public BlockItem(String doorNumber, String siteName, String blockName,
			int checkStatus) {
		// TODO Auto-generated constructor stub
		this.DoorNumber = doorNumber;
		this.SiteName = siteName;
		this.BlockName = blockName;
		this.CheckStatus = checkStatus;
	}

	public BlockItem(long id, String doorNumber, String siteName,
			String blockName, int checkStatus, int unitCount) {
		// TODO Auto-generated constructor stub
		this.Id = id;
		this.DoorNumber = doorNumber;
		this.SiteName = siteName;
		this.BlockName = blockName;
		this.CheckStatus = checkStatus;
		this.UnitCount = unitCount;
	}

	public BlockItem(String doorNumber, String siteName, String blockName,
			int checkStatus, int unitCount) {
		// TODO Auto-generated constructor stub
		this.DoorNumber = doorNumber;
		this.SiteName = siteName;
		this.BlockName = blockName;
		this.CheckStatus = checkStatus;
		this.UnitCount = unitCount;
	}

	public String getDoorNumber() {
		return DoorNumber;
	}

	public void setDoorNumber(String doorNumber) {
		DoorNumber = doorNumber;
	}

	public String getSiteName() {
		return SiteName;
	}

	public void setSiteName(String siteName) {
		SiteName = siteName;
	}

	public String getBlockName() {
		return BlockName;
	}

	public void setBlockName(String blockName) {
		BlockName = blockName;
	}

	public boolean isIsNewlyAdded() {
		if (this.CheckStatus == Enums.NewlyAdded.getVal()) {
			return true;
		}
		return false;
	}

	public void setIsNewlyAdded(boolean isNewlyAdded) {
		IsNewlyAdded = isNewlyAdded;
	}

	@Override
	public boolean equals(Object object) {
		BlockItem check = (BlockItem) object;
		if (check.DoorNumber.equals(DoorNumber)) {
			return true;
		}
		return false;
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public int getUnitCount() {
		return UnitCount;
	}

	public void setUnitCount(int unitCount) {
		UnitCount = unitCount;
	}

	public int getCheckStatus() {
		return CheckStatus;
	}

	public void setCheckStatus(int checkStatus) {
		CheckStatus = checkStatus;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.DoorNumber;
	}
}
