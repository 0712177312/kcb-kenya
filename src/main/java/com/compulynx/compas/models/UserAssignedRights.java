package com.compulynx.compas.models;

import java.io.Serializable;
 
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
 
 
@Entity
@Table(name = "userassignedrights")
public class UserAssignedRights extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean AllowView;
    private boolean AllowAdd;
    private boolean AllowEdit;
    private boolean AllowDelete;
    private int CreatedBy;
    private int rightId;
    
    @ManyToOne
    @JoinColumn(name = "GroupId")
    private UserGroup GroupId;
     
    protected  UserAssignedRights() {
        super();
    }
 
    public UserAssignedRights(boolean allowView, boolean allowAdd, boolean allowEdit, boolean allowDelete,
            int createdBy, int rightId, UserGroup groupId) {
        super();
        AllowView = allowView;
        AllowAdd = allowAdd;
        AllowEdit = allowEdit;
        AllowDelete = allowDelete;
        CreatedBy = createdBy;
        this.rightId = rightId;
        GroupId = groupId;
    }
    
    public UserAssignedRights(boolean allowView, boolean allowAdd, boolean allowEdit, boolean allowDelete,
            int rightId) {
        super();
        AllowView = allowView;
        AllowAdd = allowAdd;
        AllowEdit = allowEdit;
        AllowDelete = allowDelete;
        this.rightId = rightId;
    }
    
 
    public boolean isAllowView() {
        return AllowView;
    }
 
    public void setAllowView(boolean allowView) {
        AllowView = allowView;
    }
 
    public boolean isAllowAdd() {
        return AllowAdd;
    }
 
    public void setAllowAdd(boolean allowAdd) {
        AllowAdd = allowAdd;
    }
 
    public boolean isAllowEdit() {
        return AllowEdit;
    }
 
    public void setAllowEdit(boolean allowEdit) {
        AllowEdit = allowEdit;
    }
 
    public boolean isAllowDelete() {
        return AllowDelete;
    }
 
    public void setAllowDelete(boolean allowDelete) {
        AllowDelete = allowDelete;
    }
 
    public int getCreatedBy() {
        return CreatedBy;
    }
 
    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }
 
    public int getRightId() {
        return rightId;
    }
 
    public void setRightId(int rightId) {
        this.rightId = rightId;
    }
     
    @JsonIgnore
    public UserGroup getGroupId() {
        return GroupId;
    }
 
    public void setGroupId(UserGroup groupId) {
        GroupId = groupId;
    }
 
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
 
     
}