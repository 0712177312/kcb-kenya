package com.compulynx.compas.models.extras;


public interface ChannelRep {
	int getCount();
	int getId();
	String getChannelCode();
	String getChannelName();
	boolean getStatus();
	int getCreatedBy();
	String getCreatedOn();
	String getWaived();
	String getActiveStatus();
}
