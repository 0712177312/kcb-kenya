package com.compulynx.compas.models.t24Models;

public class CustomerReqObject {
	private String userName;
	private String password;
	private Customer object;
	
	public CustomerReqObject(String userName, String password, Customer cust) {
		super();
		this.userName = userName;
		this.password = password;
		this.object = cust;
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

	public Customer getObject() {
		return object;
	}

	public void setObject(Customer object) {
		this.object = object;
	}
	
	
}
