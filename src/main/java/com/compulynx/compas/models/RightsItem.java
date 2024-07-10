package com.compulynx.compas.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RightsItem{

	@JsonProperty("path")
	private Object path;

	@JsonProperty("allowDelete")
	private boolean allowDelete;

	@JsonProperty("rightName")
	private String rightName;

	@JsonProperty("allowView")
	private boolean allowView;

	@JsonProperty("allowEdit")
	private boolean allowEdit;

	@JsonProperty("rightId")
	private int rightId;

	@JsonProperty("allowAdd")
	private boolean allowAdd;

	public Object getPath(){
		return path;
	}

	public boolean isAllowDelete(){
		return allowDelete;
	}

	public String getRightName(){
		return rightName;
	}

	public boolean isAllowView(){
		return allowView;
	}

	public boolean isAllowEdit(){
		return allowEdit;
	}

	public int getRightId(){
		return rightId;
	}

	public boolean isAllowAdd(){
		return allowAdd;
	}
}