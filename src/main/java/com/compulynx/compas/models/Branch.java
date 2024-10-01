package com.compulynx.compas.models;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Branch extends BaseModel {
	
	private String branchCode;
	
	@Column(name="branch_name")
	private String branchName;
	
	@Column(name="status")
	private boolean status=false;
	
	@Column(name="waived")
	private String waived;
	
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
	
	@Column(name="waive_approved_by")
	private int waivedApprovedBy;
	
    @Column(name = "approvedbranchwaive_on", nullable = true,updatable = true)
    @CreationTimestamp
    private Date approveWaiveOn;
    
    @Column(name="country_code")
    private String countryCode;
	
	protected Branch() {
		super();
	}

	public Branch(String branchCode, String branchName, boolean status, int createdBy, 
			Date createdOn,String countryCode, String waived, Date waivedOn,
			int waivedBy,int waivedApprovedBy, Date approveWaiveOn) {
		super();
		this.branchCode = branchCode;
		this.branchName = branchName;
		this.status = status;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.countryCode = countryCode;
		this.waived = waived;
		this.waivedOn = waivedOn;
		this.waivedBy =waivedBy;
		this.waivedApprovedBy = waivedApprovedBy;
		this.approveWaiveOn = approveWaiveOn;
	}

	public int getWaivedBy() {
		return waivedBy;
	}

	public void setWaivedBy(int waivedBy) {
		this.waivedBy = waivedBy;
	}

	public String getWaived() {
		return waived;
	}

	public void setWaived(String waived) {
		this.waived = waived;
	}

	public int getWaivedApprovedBy() {
		return waivedApprovedBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getWaivedOn() {
		return waivedOn;
	}

	public void setWaivedOn(Date waivedOn) {
		this.waivedOn = waivedOn;
	}

	public Date getApproveWaiveOn() {
		return approveWaiveOn;
	}

	public void setApproveWaiveOn(Date approveWaiveOn) {
		this.approveWaiveOn = approveWaiveOn;
	}

	public void setWaivedApprovedBy(int waivedApprovedBy) {
		this.waivedApprovedBy = waivedApprovedBy;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	
}
