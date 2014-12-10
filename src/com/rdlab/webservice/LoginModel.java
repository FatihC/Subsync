package com.rdlab.webservice;

import org.parceler.Parcel;

import com.google.gson.annotations.SerializedName;

@Parcel
public class LoginModel {

	@SerializedName("Username")
	private String Username;
	@SerializedName("Password")
	private String Password;
	
	public LoginModel() {
		// TODO Auto-generated constructor stub
	}
	
	public LoginModel(String username,String password) {
		// TODO Auto-generated constructor stub
		this.Username=username;
		this.Password=password;
	}
	
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
}
