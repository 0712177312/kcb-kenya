package com.compulynx.compas.models;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="syslogs")
public class SysLogs extends BaseModel {
   @Column(name="sysuser")
   private String user;
   
   @Column(name="user_id")
   private int userId;
   
   @Column(name="sysactivity")
   private String activity;

	@Column(name="login_type")
	private String loginType;
   @Column(name="ip_address")
   private String ipAddress;
   
   @Column(name = "created_at", nullable = false,updatable = false)
   @CreationTimestamp
   private Date createdOn;
  
   protected SysLogs() {
	  super();
   }

	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getActivity() {
		return activity;
	}
	
	public void setActivity(String activity) {
		this.activity = activity;
	}
	
	public Date getCreatedOn() {
		return createdOn;
	}
	
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}
