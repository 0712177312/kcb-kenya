package com.compulynx.compas.models.extras;

public class ServerConfig {
	private String t24;
	private String cobanking;
	private String abis;
	private String greenbit;
	private String secugen;
	private String authPs;
	private String authUsr;
	private String sessionTimeout;
	private String sessionIdle;
	private String jwt;
	private String customerInqEndpoint;
	private String staffInqEndpoint;
	private String outUri;
	private String compasUri;
	
	public ServerConfig(String t24, String cobanking, String abis, String greenbit, String secugen, String authPs, String authUsr,
	String sessionTimeout, String sessionIdle, String jwt,String customerInqEndpoint,String staffInqEndpoint,String compasUri,String outUri) {
		super();
		this.t24 = t24;
		this.cobanking = cobanking;
		this.abis = abis;
		this.greenbit = greenbit;
		this.secugen = secugen;
		this.authPs=authPs;
		this.authUsr=authUsr;
		this.sessionTimeout = sessionTimeout;
		this.sessionIdle = sessionIdle;
		this.jwt = jwt;
		this.customerInqEndpoint=customerInqEndpoint;
		this.staffInqEndpoint=staffInqEndpoint;
		this.compasUri=compasUri;
		this.outUri=outUri;
	}
	public String getGreenbit() {
		return greenbit;
	}
	public void setGreenbit(String greenbit) {
		this.greenbit = greenbit;
	}
	public String getSecugen() {
		return secugen;
	}
	public void setSecugen(String secugen) {
		this.secugen = secugen;
	}
	public String getT24() {
		return t24;
	}
	public void setT24(String t24) {
		this.t24 = t24;
	}
	public String getCobanking() {
		return cobanking;
	}
	public void setCobanking(String cobanking) {
		this.cobanking = cobanking;
	}
	public String getAbis() {
		return abis;
	}
	public void setAbis(String abis) {
		this.abis = abis;
	}
	public String getAuthPs() {
		return authPs;
	}
	public void setAuthPs(String authPs) {
		this.authPs = authPs;
	}
	public String getAuthUsr() {
		return authUsr;
	}
	public void setAuthUsr(String authUsr) {
		this.authUsr = authUsr;
	}

	public String getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(String sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public String getSessionIdle() {
		return sessionIdle;
	}

	public void setSessionIdle(String sessionIdle) {
		this.sessionIdle = sessionIdle;
	}

	public String getJwt() { return jwt; }
	public void setJwt(String jwt) { this.jwt = jwt; }
	
	public String getCustomerInqEndpoint() {
		return customerInqEndpoint;
	}
	public void setCustomerInqEndpoint(String customerInqEndpoint) {
		this.customerInqEndpoint = customerInqEndpoint;
	}
	public String getStaffInqEndpoint() {
		return staffInqEndpoint;
	}
	public void setStaffInqEndpoint(String staffInqEndpoint) {
		this.staffInqEndpoint = staffInqEndpoint;
	}
	public String getOutUri() {
		return outUri;
	}
	public void setOutUri(String outUri) {
		this.outUri = outUri;
	}
	public String getCompasUri() {
		return compasUri;
	}
	public void setCompasUri(String compasUri) {
		this.compasUri = compasUri;
	}
	
}
