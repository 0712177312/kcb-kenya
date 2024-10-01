package com.compulynx.compas.models.t24Models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerStaffUpdateRes {
	
	private String errorCode;
	private String errorMessage;
    private CustStaffUpdateSuccessPayload payload;

    public CustomerStaffUpdateRes() {}

    public CustStaffUpdateSuccessPayload getPayload() {
        return payload;
    }

	public String getErrorCode() {
		return errorCode;
	}


	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public String toString() {
		return "CustomerStaffUpdateRes{" +
				"errorCode='" + errorCode + '\'' +
				", errorMessage='" + errorMessage + '\'' +
				", payload=" + payload +
				'}';
	}
}

