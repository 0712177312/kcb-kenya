package com.compulynx.compas.security.model;

import java.util.Date;

public class Token {
    private String access_token;
    private Date issued_at;
    private Date expires_in;

    public Token(){}

    public Token(String access_token, Date issued_at, Date expires_at) {
        this.access_token = access_token;
        this.issued_at = issued_at;
        this.expires_in = expires_at;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Date getIssued_at() {
        return issued_at;
    }

    public void setIssued_at(Date issued_at) {
        this.issued_at = issued_at;
    }

    public Date getExpires_at() {
        return expires_in;
    }

    public void setExpires_in(Date expires_at) {
        this.expires_in = expires_at;
    }

    @Override
    public String toString() {
        return "Token{" +
                "access_token='" + access_token + '\'' +
                ", issued_at=" + issued_at +
                ", expires_in=" + expires_in +
                '}';
    }
}
