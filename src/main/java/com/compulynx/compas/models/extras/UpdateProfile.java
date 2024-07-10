package com.compulynx.compas.models.extras;

public class UpdateProfile {
	private Long profileId;
	private String customerId;
	private String profileType;
	private String action;
	public UpdateProfile(Long profileId, String customerId, String profileType, String action) {
		super();
		this.profileId = profileId;
		this.customerId = customerId;
		this.profileType = profileType;
		this.action = action;
	}
	public Long getProfileId() {
		return profileId;
	}
	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getProfileType() {
		return profileType;
	}
	public void setProfileType(String profileType) {
		this.profileType = profileType;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	

}
