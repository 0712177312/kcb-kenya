package com.compulynx.compas.security.model;

public class ResponseHeader {
    private long resCode;
    private String resMessage;

    public ResponseHeader(long resCode, String resMessage) {
        this.resCode = resCode;
        this.resMessage = resMessage;
    }

    public long getResCode() {
        return resCode;
    }

    public String getResMessage() {
        return resMessage;
    }
}
