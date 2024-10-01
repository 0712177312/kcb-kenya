package com.compulynx.compas.models.extras;

public interface TellerToDisplay {
	String getTellerEmail();
	String getTellerName();
    //action performed by
    String getVerifiedBy();
    String getCreatedBy(); 
    String getRejectedBy();
    String getBranchCode();
    String getBranch();
    
    ///action performed on
    
    String getVerifiedOn();
    String getCreatedOn(); 
    String getRejectedOn();
}
