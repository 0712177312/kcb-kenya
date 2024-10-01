package com.compulynx.compas.models.extras;

public class SysLogs {
    private String activity;
    private int activityBy;
  
    
	public SysLogs(String activity, int activityBy) {
		super();
		this.activity = activity;
		this.activityBy = activityBy;
	}
	
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public int getActivityBy() {
		return activityBy;
	}
	public void setActivityBy(int activityBy) {
		this.activityBy = activityBy;
	}
  
}
