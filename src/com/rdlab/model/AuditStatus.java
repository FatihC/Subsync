package com.rdlab.model;

public enum AuditStatus {
	Active(1), Passive(2);

	public int Type;

	private AuditStatus(int type) {
		this.Type = type;
	}

	public int getVal() {
		return this.Type;
	}

	public String getStringVal() {
		return String.valueOf(this.Type);
	}
}
