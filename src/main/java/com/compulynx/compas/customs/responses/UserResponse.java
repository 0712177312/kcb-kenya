package com.compulynx.compas.customs.responses;

import java.util.List;
import java.util.UUID;

import com.compulynx.compas.models.User;
import com.compulynx.compas.models.extras.UsersToVerify;

public class UserResponse {
	
   private User model;
   private List<User> collection;
   private List<UsersToVerify> toverify;
   private String respMessage;
   private boolean status;
   private String responseCode;
   private String version;
   
   public UserResponse(User model, String respMessage, boolean status, String responseCode,String version) {
		super();
		this.model = model;
		this.respMessage = respMessage;
		this.status = status;
		this.responseCode = responseCode;
		this.version = version;
	}
   
   public UserResponse(String respMessage, boolean status, String responseCode,String version) {
		super();
		this.respMessage = respMessage;
		this.status = status;
		this.responseCode = responseCode;
		this.version = version;
	}
   
   public UserResponse(List<User> collection, String responseMessage, boolean status, String responseCode,String version) {
		super();
		this.collection = collection;
		this.respMessage = responseMessage;
		this.status = status;
		this.responseCode = responseCode;
		this.version = version;
	}
    
   public UserResponse( String respMessage, boolean status, String responseCode,String version,List<UsersToVerify> toverify) {
		super();
		this.respMessage = respMessage;
		this.status = status;
		this.responseCode = responseCode;
		this.version = version;
		this.toverify = toverify;
	}
	
	public User getModel() {
	    return model;
	}
	public void setModel(User model) {
		this.model = model;
	}
	public List<User> getCollection() {
		return collection;
	}
	public void setCollection(List<User> collection) {
		this.collection = collection;
	}
	public String getRespMessage() {
		return respMessage;
	}
	
	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}
	
	public boolean isStatus() {
		return status;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public String getResponseCode() {
		return responseCode;
	}
	
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public List<UsersToVerify> getToverify() {
		return toverify;
	}
	
	public void setToverify(List<UsersToVerify> toverify) {
		this.toverify = toverify;
	}
	
}
