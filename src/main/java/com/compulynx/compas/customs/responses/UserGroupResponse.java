package com.compulynx.compas.customs.responses;

import com.compulynx.compas.models.Teller;
import com.compulynx.compas.models.UserGroup;

public class UserGroupResponse {
    public static final String  APIV="1.0.0";
    private String respCode;
    private String respMessage;
    private boolean status;
    private String version;
    private UserGroup userGroup;

    public UserGroupResponse(){}

    public UserGroupResponse(String respCode, String respMessage, boolean status, String version, UserGroup userGroup) {
        this.respCode = respCode;
        this.respMessage = respMessage;
        this.status = status;
        this.version = version;
        this.userGroup = userGroup;
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

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }
}
