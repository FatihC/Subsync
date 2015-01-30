package com.rdlab.model;

public enum RecordStatus {
	Active(0), Deleted(1);

	public int Type;

	private RecordStatus(int type) {
		this.Type = type;
	}

	public int getVal() {
		return Type;
	}
	
	public String getStringVal(){
		return String.valueOf(this.Type);
	}
}
