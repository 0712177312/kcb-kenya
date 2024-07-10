package com.compulynx.compas.customs.responses;

import com.compulynx.compas.models.Teller;

public class TellerResponse {
	public static final String  APIV="1.0.0";
	private String respCode;
	private String respMessage;
	private boolean status;
	private String version;
	private Teller teller;
	
	public TellerResponse(String respCode, String respMessage, boolean status, String version, Teller teller) {
		super();
		this.respCode = respCode;
		this.respMessage = respMessage;
		this.status = status;
		this.version = version;
		this.teller = teller;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespMessage() {
		return respMessage;
	}
	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Teller getTeller() {
		return teller;
	}
	public void setTeller(Teller teller) {
		this.teller = teller;
	}
	public static String getApiv() {
		return APIV;
	}
	
}
