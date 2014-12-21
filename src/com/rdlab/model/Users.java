package com.rdlab.model;

import com.orm.SugarRecord;

public class Users extends SugarRecord{
	Long userSerno;
	String username;
	String password;
	String fullName;
	Integer status;
	Long createDate;
	
	public Users() {
		// TODO Auto-generated constructor stub
	}
	
	public Users(Long userSerno,String name, String password,String fullName,Integer status,Long createDate) {
		// TODO Auto-generated constructor stub
		this.setCreateDate(createDate);
		this.setFullName(fullName);
		this.setUsername(name);
		this.setUserSerno(userSerno);
		this.setPassword(password);
		this.setStatus(status);
	}

	public Long getUserSerno() {
		return userSerno;
	}

	public void setUserSerno(Long userSerno) {
		this.userSerno = userSerno;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}
	

}
