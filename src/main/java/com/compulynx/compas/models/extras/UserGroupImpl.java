package com.compulynx.compas.models.extras;

import java.util.List;

public class UserGroupImpl {
	private Long id;
    private String groupCode;
    private String groupName;
    private int groupTypeID;
    private boolean active;
    private int createdBy;
	private List<UserGroupRights> rights;
	
	public UserGroupImpl() {
		super();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public int getGroupTypeID() {
		return groupTypeID;
	}
	public void setGroupTypeID(int groupTypeID) {
		this.groupTypeID = groupTypeID;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public List<UserGroupRights> getRights() {
		return rights;
	}
	public void setRights(List<UserGroupRights> rights) {
		this.rights = rights;
	}
	
}
