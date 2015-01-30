package com.rdlab.model;

public enum AuditOptions {
	AlreadySubscribedMeterUpToDate(0),
	AlreadySubscribedMeterNotUpToDate(1),
	NotSubscribeSubscribed(2),
	NotSubscriberCouldntSubscribed(3),
	CheckInfoCorrect(4),
	NoNeedForSubscription(5),
	DataChanged(6);
	
	public int Type=0;
	private AuditOptions(int type) {
		// TODO Auto-generated constructor stub
		this.Type=type;
	}
	
	public int getVal() {
        return Type;
    }
	
	public String getValString(){
		return String.valueOf(this.Type);
	}
}
