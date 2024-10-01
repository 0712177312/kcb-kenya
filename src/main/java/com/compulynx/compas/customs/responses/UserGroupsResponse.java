package com.compulynx.compas.customs.responses;

import com.compulynx.compas.models.extras.UserGroupImpl;

import java.util.HashSet;
import java.util.List;

public class UserGroupsResponse {
    public static final String  APIV="1.0.0";
    private String respCode;
    private String respMessage;
    private boolean status;
    private String version;

    private HashSet<UserGroupImpl> groupRights;


    public UserGroupsResponse(){}

    public UserGroupsResponse(String respCode, String respMessage, boolean status, String version, List<UserGroupImpl> rights) {
        this.respCode = respCode;
        this.respMessage = respMessage;
        this.status = status;
        this.version = version;
        this.groupRights = new HashSet<>(rights);

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

    public HashSet<UserGroupImpl> getRights() {
        return groupRights;
    }

    public void setRights(HashSet<UserGroupImpl> rights) {
        this.groupRights = rights;
    }
}
