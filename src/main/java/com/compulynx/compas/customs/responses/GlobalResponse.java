package com.compulynx.compas.customs.responses;

import java.util.Optional;
import java.util.Set;

public class GlobalResponse {
	
	public static final String  APIV="1.0.0";
	private String respCode;
	private String respMessage;
	private boolean status;
	private String version;
	Set<?> collection;
	Optional<?> model;
	
	
	public GlobalResponse(String version,String respCode,boolean status, String respMessage,Set<?> collection) {
		super();
		this.respCode = respCode;
		this.respMessage = respMessage;
		this.status = status;
		this.version = version;
		this.collection = collection;
	}
	
	/**
	 * @param respCode
	 * @param respMessage
	 * @param status
	 * @param version
	 */
	public GlobalResponse(String respCode, String respMessage, boolean status, String version) {
		super();
		this.respCode = respCode;
		this.respMessage = respMessage;
		this.status = status;
		this.version = version;
	}



	public GlobalResponse(String respCode, String respMessage, boolean status, String version,
			Optional<?> model) {
		super();
		this.respCode = respCode;
		this.respMessage = respMessage;
		this.status = status;
		this.version = version;
		this.model = model;
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
	
	public Set<?> getCollection() {
		return collection;
	}
	
	public void setCollection(Set<?> collection) {
		this.collection = collection;
	}
	
	public Optional<?> getModel() {
		return model;
	}
	
	public void setModel(Optional<?> model) {
		this.model = model;
	}
	
	public static String getApiv() {
		return APIV;
	}


	@Override
	public String toString() {
		return "GlobalResponse{" +
				"respCode='" + respCode + '\'' +
				", respMessage='" + respMessage + '\'' +
				", status=" + status +
				", version='" + version + '\'' +
				", collection=" + collection +
				", model=" + model +
				'}';
	}
}
