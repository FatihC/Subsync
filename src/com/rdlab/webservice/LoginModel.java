package com.rdlab.webservice;



public class LoginModel {

	String Username;
	String Password;
	Long Serno;
	
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
	
	public String getPassword() {
		return Password;
	}
	
	public Long getSerno() {
		return Serno;
	}
	
}
