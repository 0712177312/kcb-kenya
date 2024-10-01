package com.compulynx.compas.models.extras;

public interface CustomerToDispaly {
	    
	    String getCustomerName();
	    String getCustomerIdNumber();
	    String getGender();
	    String getPhoneNumber();
	    String getDeletedBy();   
	    String getCustomerDao();
	    //action performed by
	    String getVerifiedBy();
	    String getCreatedBy(); 
	    String getRejectedBy();
	    String getCustomerWaivedBy();
	    String getBranchCode();
	    
	    ///action performed on
	    String getBranch();
	    String getCustomerId();
	    String getVerifiedOn();
	    String getCustomerWaivedOn();
	    String getCreatedOn(); 
	    String getRejectedOn();
	    

}
