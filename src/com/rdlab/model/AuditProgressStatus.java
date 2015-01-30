package com.rdlab.model;

public enum AuditProgressStatus {
	Initial(0),
	Progress(1),
	Finished(2);
	
	public int Type=0;
	private AuditProgressStatus(int type) {
		// TODO Auto-generated constructor stub
		this.Type=type;
	}
	
	public int getVal() {
        return Type;
    }
	
	public String getStringVal(){
		return String.valueOf(this.Type);
	}
}
