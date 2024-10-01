package com.compulynx.compas.models;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

/**
 * @author Mutwol
 *
 */
@Entity
@Table(name ="customer")
public class Customer extends BaseModel {
	
	private String customerName;
	private String customerIdNumber;
	private String customerId;
	private String mnemonic;
	private String phoneNumber;
	private String email;
	private String gender;
    private String action;
    @Column(name = "reject_reason")
    private String reason1;
	@Column(name="waive_reason")
	private String reason;
	private String country;
	private String nationality;
	private String customerType;
	@Column(name="branch_id")
	private int branchId;
	private boolean active;
    @Column(name="created_by")                  
    private int createdBy;
    @Column(name="verified")
    private String verified;
    @Column(name="branch_code")
    private String branchCode;
    
    @Column(name="verified_by")
    private int verifiedBy;
    
    @Column(name="verified_on")
    private Date verifiedOn;
    
	@Column(name="waived")
	private String waived;
	
	@Column(name="waived_by")
	private int waivedBy;
	
    @Column(name = "created_at", nullable = false,updatable = false)
    @CreationTimestamp
    private Date createdOn;
    
    @Column(name = "customerwaived_on", nullable = true,updatable = true)
    @CreationTimestamp
    private Date waivedOn;
    
	@Column(name="waive_approved_by")
	private int waivedApprovedBy;
	
    @Column(name = "approvedcustomerwaive_on", nullable = true,updatable = true)
    @CreationTimestamp
    private Date approveWaiveOn;

    @Column(name="deleted_by")
    private Integer deletedBy;

    @Column(name = "deleted_on", nullable = true, updatable = true)
    private Date deletedOn;

    @Column(name="rejected_by")
	private Integer rejectedBy;

    @Column(name = "rejected_on", nullable = true, updatable = true)
	private Date rejectedOn;
    
        
	public Customer() {
		super();
	}
	
	public Customer(String customerName, String customerIdNumber, String customerId, String mnemonic,
			String phoneNumber, String email, String gender, String reason,      String country, String nationality, String customerType, int branchId,
			boolean active, int createdBy, String verified, int verifiedBy, Date verifiedOn,
			Date createdOn,String branchCode, String waived, int waivedBy, Date waivedOn,
			int waivedApprovedBy,Date approveWaiveOn, int deletedBy, Date deletedOn, int rejectedBy,
					Date rejectedOn) {
		super();
		this.customerName = customerName;
		this.customerIdNumber = customerIdNumber;
		this.customerId = customerId;
		this.mnemonic = mnemonic;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.gender = gender;
		this.reason=reason;
		this.country = country;
		this.nationality = nationality;
		this.customerType = customerType;
		this.branchId = branchId;
		this.active = active;
		this.createdBy = createdBy;
		this.verified = verified;
		this.verifiedBy = verifiedBy;
		this.verifiedOn = verifiedOn;
		this.createdOn = createdOn;
		this.branchCode = branchCode;
		this.waived = waived;
		this.waivedBy = waivedBy;
		this.waivedOn=waivedOn;
		this.waivedApprovedBy =waivedApprovedBy;
		this.approveWaiveOn = approveWaiveOn;
		this.deletedBy = deletedBy;
		this.deletedOn = deletedOn;
		this.rejectedBy = rejectedBy;
		this.deletedOn = deletedOn;
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

	public int getWaivedBy() {
		return waivedBy;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerIdNumber() {
		return customerIdNumber;
	}

	public void setCustomerIdNumber(String customerIdNumber) {
		this.customerIdNumber = customerIdNumber;
	}

	public String getMnemonic() {
		return mnemonic;
	}

	public void setMnemonic(String mnemonic) {
		this.mnemonic = mnemonic;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public String getReason(){
		return reason;
	}


	public String setReason(){
		return reason;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getVerified() {
		return verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

	public int getVerifiedBy() {
		return verifiedBy;
	}

	public void setVerifiedBy(int verifiedBy) {
		this.verifiedBy = verifiedBy;
	}

	public Date getVerifiedOn() {
		return verifiedOn;
	}

	public void setVerifiedOn(Date verifiedOn) {
		this.verifiedOn = verifiedOn;
	}

	public Date getWaivedOn() {
		return waivedOn;
	}

	public void setWaivedOn(Date waivedOn) {
		this.waivedOn = waivedOn;
	}

	public int getDeletedBy() {
		if(deletedBy == null){
			return 0;
		}else {
			return deletedBy;
		}
	}

	public void setDeletedBy(int deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Date getDeletedOn() {
		return deletedOn;
	}

	public String getReason1() {
		return reason1;
	}

	public void setReason1(String reason1) {
		this.reason1 = reason1;
	}

	public void setDeletedOn(Date deletedOn) {
		this.deletedOn = deletedOn;
	}

	public int getRejectedBy() {
		if(rejectedBy == null){
			return 0;
		}else{
			return rejectedBy;
		}
	}

	public void setRejectedBy(int rejectedBy) {
		this.rejectedBy = rejectedBy;
	}

	public Date getRejectedOn() {
		return rejectedOn;
	}

	public void setRejectedOn(Date rejectedOn) {
		this.rejectedOn = rejectedOn;
	}
}
