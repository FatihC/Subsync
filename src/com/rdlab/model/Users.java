package com.rdlab.model;

import com.orm.SugarRecord;

public class Users extends SugarRecord{
	Long UserSerno;
	String Username;
	String Password;
	String FullName;
	Integer Status;
	Long CreateDate;
	
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
		return UserSerno;
	}

	public void setUserSerno(Long userSerno) {
		UserSerno = userSerno;
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

	public String getFullName() {
		return FullName;
	}

	public void setFullName(String fullName) {
		FullName = fullName;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer status) {
		Status = status;
	}

	public Long getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(Long createDate) {
		CreateDate = createDate;
	}
	

}
