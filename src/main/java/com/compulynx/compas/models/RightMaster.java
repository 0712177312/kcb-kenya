package com.compulynx.compas.models;
 

import java.util.Set;
 
import javax.persistence.*;
 
import com.fasterxml.jackson.annotation.JsonIgnore;
 
@Entity
@Table(name="rightsmaster")
public class RightMaster extends BaseModel{
     
     @Column(name="AllowView",columnDefinition="tinyint")
     private boolean  allowView;
      
     @Column(name="AllowAdd",columnDefinition="tinyint")
     private boolean allowAdd;    
      
     @Column(name="AllowEdit",columnDefinition="tinyint")
     private boolean allowEdit;    
      
     @Column(name="AllowDelete",columnDefinition="tinyint")
     private boolean allowDelete=false; 
      
     @Column(name="RightViewName")
     private String path;
      
     @ManyToOne
     @JoinColumn(name = "RightHeaderID")
     private MenuHeaderMaster rightHeaderID;
      
     @Column(name="RightDisplayName")
     private String title;
      
     @Column(name="RightIconCss")    
     private String icon;
      
     @Column(name="CreatedBy")
     private int createdBy;
      
     @Column(name="RightClass")
     private String rightClass;
      
     @Column(name="ExtraLInk",columnDefinition="tinyint")
     private boolean extralink=false;
      
     @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
//     @JoinTable(name = "userassignedrights", joinColumns = @JoinColumn(name = "rightId",
//     referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "GroupId", referencedColumnName = "id"))
     @JoinTable(name = "userassignedrights", joinColumns = {
             @JoinColumn(name = "rightId", nullable = true)}, inverseJoinColumns = {
             @JoinColumn(name = "GroupId", nullable = true)})
     private Set<UserGroup> groups; //  = new ArrayList<>();
      
     protected RightMaster() {
         super();
     }
 
    public RightMaster(boolean allowView, boolean allowAdd, boolean allowEdit, boolean allowDelete, String path,
            MenuHeaderMaster rightHeaderID, String title, String icon, int createdBy, String rightClass,
            boolean extralink) {
        super();
        this.allowView = allowView;
        this.allowAdd = allowAdd;
        this.allowEdit = allowEdit;
        this.allowDelete = allowDelete;
        this.path = path;
        this.rightHeaderID = rightHeaderID;
        this.title = title;
        this.icon = icon;
        this.createdBy = createdBy;
        this.rightClass = rightClass;
        this.extralink = extralink;
    }
 
    public boolean isAllowView() {
        return allowView;
    }
 
    public void setAllowView(boolean allowView) {
        this.allowView = allowView;
    }
 
    public boolean isAllowAdd() {
        return allowAdd;
    }
 
    public void setAllowAdd(boolean allowAdd) {
        this.allowAdd = allowAdd;
    }
 
    public boolean isAllowEdit() {
        return allowEdit;
    }
 
    public void setAllowEdit(boolean allowEdit) {
        this.allowEdit = allowEdit;
    }
 
    public boolean isAllowDelete() {
        return allowDelete;
    }
 
    public void setAllowDelete(boolean allowDelete) {
        this.allowDelete = allowDelete;
    }
 
    public String getPath() {
        return path;
    }
 
    public void setPath(String path) {
        this.path = path;
    }
     
    @JsonIgnore
    public MenuHeaderMaster getRightHeaderID() {
        return rightHeaderID;
    }
 
    public void setRightHeaderID(MenuHeaderMaster rightHeaderID) {
        this.rightHeaderID = rightHeaderID;
    }
 
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String title) {
        this.title = title;
    }
 
    public String getIcon() {
        return icon;
    }
 
    public void setIcon(String icon) {
        this.icon = icon;
    }
 
    public int getCreatedBy() {
        return createdBy;
    }
 
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
 
    public String getRightClass() {
        return rightClass;
    }
 
    public void setRightClass(String rightClass) {
        this.rightClass = rightClass;
    }
 
    public boolean isExtralink() {
        return extralink;
    }
 
    public void setExtralink(boolean extralink) {
        this.extralink = extralink;
    }
      
      
}