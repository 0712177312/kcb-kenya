package com.compulynx.compas.customs.responses;

import com.compulynx.compas.models.Branch;

public class BranchResponse {
	public static final String  APIV="1.0.0";
	private String respCode;
	private String respMessage;
	private boolean status;
	private String version;
	private Branch branch;
	
	public BranchResponse(boolean status, String respCode, String respMessage, String version, Branch branch) {
		super();
		this.status = status;
		this.respCode = respCode;
		this.respMessage = respMessage;	
		this.version = version;
		this.branch = branch;
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
	
	public Branch getBranch() {
		return branch;
	}
	public void setBranch(Branch branch) {
		this.branch = branch;
	}
	public static String getApiv() {
		return APIV;
	}
	
	
}
