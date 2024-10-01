package com.compulynx.compas.security.model;

public class BioAuthReq {
    private String username;
    private boolean loginStatus;

    public BioAuthReq(){}

    public BioAuthReq(String username,boolean loginStatus){
        this.username=username;
        this.loginStatus=loginStatus;
    }

    public boolean getLoginStatus(){
        return loginStatus;
    }
    public void setLoginStatus(boolean status){
         this.loginStatus=status;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username=username;
    }
}
