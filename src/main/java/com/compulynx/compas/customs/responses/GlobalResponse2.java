package com.compulynx.compas.customs.responses;

import com.compulynx.compas.models.extras.UserGroupRights;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class GlobalResponse2 {
    public static final String  APIV="1.0.0";
    private String respCode;
    private String respMessage;
    private boolean status;
    private String version;
    @JsonProperty("collection")
    private Collection<?> collection;

    public GlobalResponse2(){}

    public GlobalResponse2(String version,String respCode, boolean status, String respMessage) {
        super();
        this.version = version;
        this.respCode = respCode;
        this.respMessage = respMessage;
        this.status = status;
    }
    
    public GlobalResponse2(String version,String respCode, boolean status, String respMessage, Collection<?> collection) {
        super();
        this.version = version;
        this.respCode = respCode;
        this.respMessage = respMessage;
        this.status = status;
        this.collection = new HashSet<>(collection);
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

    public Collection<?> getHashset() {
        return collection;
    }

    public void setHashset(HashSet<?> collection) {
        this.collection = collection;
    }

    @Override
    public String toString() {
        return "GlobalResponse2{" +
                "respCode='" + respCode + '\'' +
                ", respMessage='" + respMessage + '\'' +
                ", status=" + status +
                ", version='" + version + '\'' +
                ", collection=" + collection +
                '}';
    }
}
