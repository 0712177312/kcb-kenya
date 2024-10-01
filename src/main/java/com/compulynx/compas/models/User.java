package com.compulynx.compas.models;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

import com.compulynx.compas.models.roles_authorities.RoleEntity;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="USERMASTER")
public class User extends BaseModel {

	@Column(name="email")
    private String email;
	
	@Column(name="first_name")
    private String firstName;
	
    @Column(name="fullname")
    private String fullName;
    
	@Column(name="password")
    private String password;
	
	@Column(name="phone")
    private String phone;
	
	@Column(name="surname")
    private String surName;
	
	@Column(name="username")
    private String username;
	
	@Column(name="other_names")
    private String otherNames;
	
	@Column(name="group_id")
    private int group;
	
    @Column(name="created_by")                  
    private int createdBy;
    
    @Column(name="status")
    private boolean status = false;
    
    @Column(name="verified")
    private String approved;
    
    @Column(name="verified_by")
    private int approvedBy;
    
    @Column(name="verified_on")
    private Date approvedOn;
    
    
    @Column(name="updated_by")
    private int updatedBy;
    
    @Column(name="updated_on")
    private Date updatedOn;
    
    @Column(name="country")
    private String country;
    
    @Column(name="branch")
    private String branch;
	
    @Column(name="teller")
    private String teller;
    
    @Column(name = "created_at", nullable = false,updatable = false)
    @CreationTimestamp
    private Date createdOn;

    private boolean locked= false;
    private int trials;

	@ManyToMany(
			cascade = {CascadeType.PERSIST},
			fetch = FetchType.EAGER)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id"))
	private Collection<RoleEntity> roles;



	public User(String email, String firstName, String fullName, String password, String phone, String surName,
			String username, String otherNames, int group, int createdBy, boolean status, String approved,
			int approvedBy, Date approvedOn, String country, String branch, String teller, Date createdOn) {
		super();
		this.email = email;
		this.firstName = firstName;
		this.fullName = fullName;
		this.password = password;
		this.phone = phone;
		this.surName = surName;
		this.username = username;
		this.otherNames = otherNames;
		this.group = group;
		this.createdBy = createdBy;
		this.status = status;
		this.approved = approved;
		this.approvedBy = approvedBy;
		this.approvedOn = approvedOn;
		this.country = country;
		this.branch = branch;
		this.teller = teller;
		this.createdOn = createdOn;
	}

	public User(String email, String firstName, String fullName, String password, String phone, String surName, String username, String otherNames, int group, int createdBy, boolean status, String approved, int approvedBy, Date approvedOn, String country, String branch, String teller, Date createdOn, boolean locked, int trials, Collection<RoleEntity> roles) {
		this.email = email;
		this.firstName = firstName;
		this.fullName = fullName;
		this.password = password;
		this.phone = phone;
		this.surName = surName;
		this.username = username;
		this.otherNames = otherNames;
		this.group = group;
		this.createdBy = createdBy;
		this.status = status;
		this.approved = approved;
		this.approvedBy = approvedBy;
		this.approvedOn = approvedOn;
		this.country = country;
		this.branch = branch;
		this.teller = teller;
		this.createdOn = createdOn;
		this.locked = locked;
		this.trials = trials;
		this.roles = roles;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getTeller() {
		return teller;
	}

	public void setTeller(String teller) {
		this.teller = teller;
	}

	protected User() {
		super();
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOtherNames() {
		return otherNames;
	}

	public void setOtherNames(String otherNames) {
		this.otherNames = otherNames;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
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

	public String getApproved() {
		return approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	public int getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(int approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public int getTrials() {
		return trials;
	}

	public void setTrials(int trials) {
		this.trials = trials;
	}

	public Collection<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Collection<RoleEntity> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", firstName=" + firstName + ", fullName=" + fullName + ", password=" + password
				+ ", phone=" + phone + ", surName=" + surName + ", username=" + username + ", otherNames=" + otherNames
				+ ", group=" + group + ", createdBy=" + createdBy + ", status=" + status + ", approved=" + approved
				+ ", approvedBy=" + approvedBy + ", approvedOn=" + approvedOn + ", updatedBy=" + updatedBy
				+ ", updatedOn=" + updatedOn + ", country=" + country + ", branch=" + branch + ", teller=" + teller
				+ ", createdOn=" + createdOn + ", locked=" + locked + ", trials=" + trials + ", roles=" + roles + "]";
	}


	
}
