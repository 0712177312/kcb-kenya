package com.compulynx.compas.models.t24Models;

public class CustStaffUpdateSuccessPayload {
	private String status;

    public CustStaffUpdateSuccessPayload(){}

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "CustStaffUpdateSuccessPayload{" +
                "status='" + status + '\'' +
                '}';
    }
}
