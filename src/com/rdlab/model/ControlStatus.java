package com.rdlab.model;

public enum ControlStatus {
	NoWiringMeterExist(0),
	UnreachableUnitOrMeter(1),
	ClosedOrMeterUnreachable(2),
	NoMeterNoSubscriber(3);
	
	public int Type=0;
	private ControlStatus(int type) {
		// TODO Auto-generated constructor stub
		this.Type=type;
	}
	
	public int getVal() {
        return Type;
    }
}
