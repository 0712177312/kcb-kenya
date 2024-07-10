package com.compulynx.compas.models;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "tellermaster")
public class Teller extends BaseModel {
    @Column(unique = true)
    private String tellerId;
    private String tellerStatus;
    private String deptCode;
    private String recordStatus;
    private String tellerEmail;
    private String tellerName;
    @Column(unique = true)
    private String tellerSignOnName;
    private String departmentCode;
    private String companyCode;
    private String customerId;
    private String verified;
    private int createdBy;
    private int verifiedBy;
    @Column(name = "reject_reason")
    private String reason;
    @Column(name = "verified_on")
    private Date approvedOn;
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdOn;

    @Column(name = "deleted_by")
    private Integer deletedBy;

    @Column(name = "deleted_on", nullable = true, updatable = true)
    private Date deletedOn;

    @Column(name = "rejected_by")
    private Integer rejectedBy;
    
    @Column(name="enroll_status")
    private String enroll;

    @Column(name = "rejected_on", nullable = true, updatable = true)
    private Date rejectedOn;

    protected Teller() {

    }

    public Teller(String tellerId, String tellerStatus, String deptCode, String recordStatus, String tellerEmail,
                  String tellerName, String tellerSignOnName, String departmentCode, String companyCode,
                  String customerId, String verified, int createdBy, int verifiedBy,
                  Date approvedOn, Date createdOn, String enrollStatus, int deletedBy, Date deletedOn,
                  int rejectedBy, Date rejectedOn) {
        super();
        this.tellerId = tellerId;
        this.tellerStatus = tellerStatus;
        this.deptCode = deptCode;
        this.recordStatus = recordStatus;
        this.tellerEmail = tellerEmail;
        this.tellerName = tellerName;
        this.tellerSignOnName = tellerSignOnName;
        this.departmentCode = departmentCode;
        this.companyCode = companyCode;
        this.customerId = customerId;
        this.verified = verified;
        this.createdBy = createdBy;
        this.verifiedBy = verifiedBy;
        this.approvedOn = approvedOn;
        this.createdOn = createdOn;
        this.deletedBy = deletedBy;
        this.deletedOn = deletedOn;
        this.rejectedBy = rejectedBy;
        this.rejectedOn = rejectedOn;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getApprovedOn() {
        return approvedOn;
    }

    public void setApprovedOn(Date approvedOn) {
        this.approvedOn = approvedOn;
    }

    public int getVerifiedBy() {
        return verifiedBy;
    }

 
	public String getEnroll() {
		return enroll;
	}

	public void setEnroll(String enroll) {
		this.enroll = enroll;
	}

	public void setVerifiedBy(int verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getTellerId() {
        return tellerId;
    }

    public void setTellerId(String tellerId) {
        this.tellerId = tellerId;
    }

    public String getTellerStatus() {
        return tellerStatus;
    }

    public void setTellerStatus(String tellerStatus) {
        this.tellerStatus = tellerStatus;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getTellerEmail() {
        return tellerEmail;
    }

    public void setTellerEmail(String tellerEmail) {
        this.tellerEmail = tellerEmail;
    }

    public String getTellerName() {
        return tellerName;
    }

    public void setTellerName(String tellerName) {
        this.tellerName = tellerName;
    }

    public String getTellerSignOnName() {
        return tellerSignOnName;
    }

    public void setTellerSignOnName(String tellerSignOnName) {
        this.tellerSignOnName = tellerSignOnName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getDeletedBy() {
        if (deletedBy == null) {
            return 0;
        } else {
            return deletedBy;
        }
    }

    public void setDeletedBy(int deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Date getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(Date deletedOn) {
        this.deletedOn = deletedOn;
    }

    public int getRejectedBy() {
        if (rejectedBy == null) {
            return 0;
        } else {
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
