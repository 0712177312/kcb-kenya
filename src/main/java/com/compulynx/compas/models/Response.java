package com.compulynx.compas.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Response{

	@JsonProperty("groupName")
	private String groupName;

	@JsonProperty("rights")
	private List<RightsItem> rights;

	@JsonProperty("active")
	private boolean active;

	@JsonProperty("id")
	private int id;

	@JsonProperty("groupCode")
	private String groupCode;

	public String getGroupName(){
		return groupName;
	}

	public List<RightsItem> getRights(){
		return rights;
	}

	public boolean isActive(){
		return active;
	}

	public int getId(){
		return id;
	}

	public String getGroupCode(){
		return groupCode;
	}
}