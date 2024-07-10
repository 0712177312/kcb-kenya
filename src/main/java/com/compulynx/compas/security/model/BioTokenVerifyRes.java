package com.compulynx.compas.security.model;

import org.springframework.security.core.userdetails.UserDetails;

public class BioTokenVerifyRes {

    private ResponseHeader header;
    private boolean verified;
    private UserDetails userDetails;

    public BioTokenVerifyRes(){}

    public BioTokenVerifyRes(boolean verified, UserDetails userDetails,ResponseHeader responseHeader) {
        this.header = responseHeader;
        this.verified = verified;
        this.userDetails = userDetails;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public ResponseHeader getResponseHeader() {
        return header;
    }
}
