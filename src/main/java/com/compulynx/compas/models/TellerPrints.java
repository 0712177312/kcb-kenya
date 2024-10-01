package com.compulynx.compas.models;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name ="teller_prints")
public class TellerPrints extends BaseModel {
	private String position;
	private String quality;
	@Column(name = "print", columnDefinition = "BLOB")
	private String fingerPrint;
	
    @ManyToOne
    @JoinColumn(name = "profile")
    private Teller profile;
    
    protected TellerPrints() {
    	super();
    }

	public TellerPrints(String position, String quality, String fingerPrint, Teller profile) {
		super();
		this.position = position;
		this.quality = quality;
		this.fingerPrint = fingerPrint;
		this.profile = profile;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getFingerPrint() {
		return fingerPrint;
	}

	public void setFingerPrint(String fingerPrint) {
		this.fingerPrint = fingerPrint;
	}
	
	@JsonIgnore
	public Teller getProfile() {
		return profile;
	}

	public void setProfile(Teller profile) {
		this.profile = profile;
	}
    
    
    
}
