package com.compulynx.compas.models.extras;

public interface UserReport {

	
    //action performed by
    String getVerifiedBy();
    String getCreatedBy(); 
   // String getCreatedName();
    String getCreatedOn();
    String getVerifiedOn();
    String getGroupName();
    
    String getBranchName ();
    String getFullName();
    String getTeller();
    
}
