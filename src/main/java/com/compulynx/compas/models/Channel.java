package com.compulynx.compas.models;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Channel extends BaseModel{
    private String channelCode;
    private String channelName;
	@Column(name="status")
	private boolean status=false;
	
    @Column(name="created_by")                  
    private int createdBy;
    
	@Column(name="waived")
	private String waived;
    
    @Column(name = "created_at", nullable = false,updatable = false)
    @CreationTimestamp
    private Date createdOn;
    
	@Column(name="waived_by")
	private int waivedBy;
	
    @Column(name = "waived_on", nullable = false,updatable = false)
    @CreationTimestamp
    private Date waivedOn;
    
	@Column(name="waive_approved_by")
	private int waivedApprovedBy;
	
    @Column(name = "approvedwaive_on", nullable = true,updatable = true)
    @CreationTimestamp
    private Date approveWaiveOn;
  
    protected Channel() {
		super();
	}
    
	public Channel(String channelCode, String channelName, boolean status, int createdBy,
			Date createdOn, String waived, Date waivedOn, int waivedBy,int waivedApprovedBy,Date approveWaiveOn ) {
		super();
		this.channelCode = channelCode;
		this.channelName = channelName;
		this.status = status;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.waived = waived;
		this.waivedOn = waivedOn;
		this.waivedBy =waivedBy;
		this.waivedApprovedBy=waivedApprovedBy;
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

	public int getWaivedApprovedBy() {
		return waivedApprovedBy;
	}

	public void setWaivedApprovedBy(int waivedApprovedBy) {
		this.waivedApprovedBy = waivedApprovedBy;
	}

	public Date getApproveWaiveOn() {
		return approveWaiveOn;
	}

	public void setApproveWaiveOn(Date approveWaiveOn) {
		this.approveWaiveOn = approveWaiveOn;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
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
