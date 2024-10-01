package com.compulynx.compas.models;
 
import java.io.Serializable;
import java.util.List;
import java.util.Set;
 
import javax.persistence.*;
 
 
@Entity
@Table(name="usergroupsmaster")
public class UserGroup extends BaseModel implements  Serializable {
    private static final long serialVersionUID = 1L;
     
    @Column(name="GroupCode")
    private String groupCode;
    @Column(name="GroupName")
    private String groupName;
    @Column(name="GroupTypeID")
    private int groupTypeID;
    @Column(name="Active")
    private boolean active;
    @Column(name="CreatedBy")
    private int createdBy;
     
     @ManyToMany(mappedBy = "groups")
     private Set<RightMaster> roles;
      
      
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="GroupId")
    private List<UserAssignedRights> rights;
     
    protected UserGroup() {
        super();
    }
 
    public UserGroup(String groupCode, String groupName, int groupTypeID, boolean active, int createdBy,
            List<UserAssignedRights> rights) {
        super();
        this.groupCode = groupCode;
        this.groupName = groupName;
        this.groupTypeID = groupTypeID;
        this.active = active;
        this.createdBy = createdBy;
        this.rights = rights;
    }
    
     
    public void setRights(List<UserAssignedRights> rights) {
		this.rights = rights;
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return super.getId();
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		super.setId(id);
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
 
    public List<UserAssignedRights> getRights() {
        return rights;
    }
     
    public void addRight(UserAssignedRights right) {
        right.setGroupId(this);
        rights.add(right);
    }
     
    public void removeRight(UserAssignedRights rights) {
        rights.setGroupId(null);
        this.rights.remove(rights);
    }
     
    public void removeAllRights() {
        rights.forEach(rights -> rights.setGroupId(null));
        this.rights.clear();
    }
 
    public Set<RightMaster> getRoles() {
        return roles;
    }
     
}