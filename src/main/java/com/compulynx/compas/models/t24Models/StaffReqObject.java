package com.compulynx.compas.models.t24Models;

public class StaffReqObject {
	private String userName;
	private String password;
	private Staff object;
	
	public StaffReqObject(String userName, String password, Staff object) {
		super();
		this.userName = userName;
		this.password = password;
		this.object = object;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Staff getObject() {
		return object;
	}
	public void setObject(Staff object) {
		this.object = object;
	}
	
	
	
	
}
