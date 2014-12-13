package com.rdlab.model;

import com.orm.SugarRecord;

public class Configuration extends SugarRecord {
	String Key;
	String Value;
	
	public String getKey() {
		return Key;
	}
	public void setKey(String key) {
		Key = key;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}

}
