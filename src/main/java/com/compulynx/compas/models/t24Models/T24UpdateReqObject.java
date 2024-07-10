package com.compulynx.compas.models.t24Models;

public class T24UpdateReqObject {
	
	String userName;
	String passWord;
	T24UpdateParams object;	
	
	public T24UpdateReqObject(String username,String password, T24UpdateParams params) {
		this.userName=username;
		this.passWord = username;
		this.object = params;
		
	}

	public String getUserName() {
		return userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public T24UpdateParams getObject() {
		return object;
	}

}
