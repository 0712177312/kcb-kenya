package com.compulynx.compas.models;


import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Country extends BaseModel {
	
	private String countryCode;
	private String name;
	
	@Column(name="status")
	private boolean status=false;
	
    @Column(name="created_by")                  
    private int createdBy;
    
    @Column(name = "created_at", nullable = false,updatable = false)
    @CreationTimestamp
    private Date createdOn;
    
    @Column(name = "waived_on", nullable = true,updatable = true)
    @CreationTimestamp
    private Date waivedOn;
    
	@Column(name="waived_by")
	private int waivedBy;
	
	@Column(name="waived")
	private String waived;
	
	@Column(name="waive_approved_by")
	private int waivedApprovedBy;
	
    @Column(name = "approvewaive_on", nullable = true,updatable = true)
    @CreationTimestamp
    private Date approveWaiveOn;
    
	protected Country() {
		super();
	}

	public Country(String countryCode, String name, boolean status, int createdBy, 
			Date createdOn, String waived,Date waivedOn,int waivedBy, Date approveWaiveOn,int waivedApprovedBy ) {
		super();
		this.countryCode = countryCode;
		this.name = name;
		this.status = status;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.waived = waived;
		this.waivedOn =waivedOn;
		this.waivedBy  =waivedBy;
		this.approveWaiveOn =approveWaiveOn;
		this.waivedApprovedBy = waivedApprovedBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getApproveWaiveOn() {
		return approveWaiveOn;
	}

	public void setApproveWaiveOn(Date approveWaiveOn) {
		this.approveWaiveOn = approveWaiveOn;
	}

	public int getWaivedApprovedBy() {
		return waivedApprovedBy;
	}

	public void setWaivedApprovedBy(int waivedApprovedBy) {
		this.waivedApprovedBy = waivedApprovedBy;
	}

	public int getWaivedBy() {
		return waivedBy;
	}

	public void setWaivedBy(int waivedBy) {
		this.waivedBy = waivedBy;
	}

	public Date getWaivedOn() {
		return waivedOn;
	}

	public void setWaivedOn(Date waivedOn) {
		this.waivedOn = waivedOn;
	}

	public String getWaived() {
		return waived;
	}

	public void setWaived(String waived) {
		this.waived = waived;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
