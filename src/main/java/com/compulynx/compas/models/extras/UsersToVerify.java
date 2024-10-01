package com.compulynx.compas.models.extras;

import java.io.Serializable;
import java.util.Date;

public interface UsersToVerify extends Serializable{
	int getCounter();
	int getId();
	int getCreatedBy();
	int getUpdatedBy();
	String getFullName();
	String getGroupName();
	String getCreatedAt(); 
}
