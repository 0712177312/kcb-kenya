package com.compulynx.compas.security.model;

public class BioTokenVerifyReq {
    private String token;

    public BioTokenVerifyReq(){}

    public BioTokenVerifyReq(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
