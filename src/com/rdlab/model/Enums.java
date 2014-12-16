package com.rdlab.model;

public enum Enums {
	NotStarted(0),
	AlmostFinished(1),
	Completed(2),
	NotSynchronised(3),
	Synchronised(4),
	NewlyAdded(5),
	Deleted(6),
	ReadyToSync(7);
	
	
	public int Type=0;
	private Enums(int type) {
		// TODO Auto-generated constructor stub
		this.Type=type;
	}
	
	public int getVal() {
        return Type;
    }
	
}
