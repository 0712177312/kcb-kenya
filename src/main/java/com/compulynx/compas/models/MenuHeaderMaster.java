package com.compulynx.compas.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="menuheadermaster")
public class MenuHeaderMaster extends BaseModel {
		
	@Column(name = "HeaderName")
    private String title;
	
	@Column(name="HeaderIconCss")
    private String icon;
	
	@Column(name="HeaderIconClass")
    private String headerClass;
	
	@Column(name="HeaderPos")
    private int headerPos;
	
	@Column(name="CreatedBy")
    private int createdBy;
	
	@Column(name="extraLink")
	private boolean extralink=false;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy = "rightHeaderID",cascade = CascadeType.ALL)
	private List<RightMaster> menus;
	
	protected MenuHeaderMaster() {
		super();
		menus=new ArrayList<>();
	}

	public MenuHeaderMaster(String title, String icon, String headerClass, int headerPos, int createdBy,
			boolean extralink, List<RightMaster> menus) {
		super();
		this.title = title;
		this.icon = icon;
		this.headerClass = headerClass;
		this.headerPos = headerPos;
		this.createdBy = createdBy;
		this.extralink = extralink;
		this.menus = menus;
	}
	
	@Override
	public void setId(Long id) {
		super.setId(id);
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

	public String getHeaderClass() {
		return headerClass;
	}

	public void setHeaderClass(String headerClass) {
		this.headerClass = headerClass;
	}

	public int getHeaderPos() {
		return headerPos;
	}

	public void setHeaderPos(int headerPos) {
		this.headerPos = headerPos;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public boolean isExtralink() {
		return extralink;
	}

	public void setExtralink(boolean extralink) {
		this.extralink = extralink;
	}

	public List<RightMaster> getMenus() {
		return menus;
	}

	public void setMenus(List<RightMaster> menus) {
		this.menus = menus;
	}
	
	
}
