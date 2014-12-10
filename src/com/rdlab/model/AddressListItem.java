package com.rdlab.model;

public class AddressListItem {

	int Id;
	String Code;
	String Name;

	public AddressListItem() {
		// TODO Auto-generated constructor stub
	}

	public AddressListItem(int id,String code, String name) {
		this.Code = code;
		this.Id=id;
		this.Name = name;
	}
	
	public AddressListItem(String code, String name) {
		this.Code = code;
		this.Name = name;
	}

	public String GetCode() {
		return this.Code;
	}

	public String GetName() {
		return this.Name;
	}
	
	public int GetId() {
		return this.Id;
	}

	@Override
	public boolean equals(Object object) {
		AddressListItem check = (AddressListItem) object;
		if (check.Code.equals(Code)) {
			return true;
		}
		return false;
	}
}
