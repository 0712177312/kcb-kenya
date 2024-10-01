package com.compulynx.compas.models.extras;

public class FingerPrint {
	
	private String position;
	private String quality;
	private String fingerPrint;
	
	public FingerPrint(String position, String quality, String fingerPrint) {
		super();
		this.position = position;
		this.quality = quality;
		this.fingerPrint = fingerPrint;
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
	
	
	
}
