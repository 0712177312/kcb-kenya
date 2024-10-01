package com.compulynx.compas.models.extras;

import java.util.*;

public class DashboardStats {
	
	private int enrolledCustomers;
	private LineChart Line;
	private int branch;
	private int territory;
	private int district;
	private int activeCustomers;
	private int user;
	private int monthlyEnrollment;
	private int activeMonthlyCusts;
	private int rejects;
	private List<String> usrs;
	public DashboardStats() {
		super();
	}
	
	public DashboardStats(int enrolledCustomers, LineChart line,int branch,int territory,
			int district,int user,int activeCustomers,int monthlyEnrollment,int activeMonthlyCusts,List<String> usrs,int rejects) {
		super();
		this.enrolledCustomers = enrolledCustomers;
		Line = line;
		this.branch=branch;
		this.territory=territory;
		this.district = district;
		this.user=user;
		this.activeCustomers=activeCustomers;
		this.monthlyEnrollment=monthlyEnrollment;
		this.activeMonthlyCusts=activeMonthlyCusts;
		this.usrs=usrs;
		this.rejects=rejects;
	}

	public int getRejects() {
		return rejects;
	}

	public void setRejects(int rejects) {
		this.rejects = rejects;
	}

	public int getMonthlyEnrollment() {
		return monthlyEnrollment;
	}
	
	public void setMonthlyEnrollment(int monthlyEnrollment) {
		this.monthlyEnrollment = monthlyEnrollment;
	}
	
	public int getEnrolledCustomers() {
		return enrolledCustomers;
	}


	public void setEnrolledCustomers(int enrolledCustomers) {
		this.enrolledCustomers = enrolledCustomers;
	}


	public LineChart getLine() {
		return Line;
	}


	public void setLine(LineChart line) {
		Line = line;
	}
	
	public int getBranch() {
		return branch;
	}
	
	public void setBranch(int branch) {
		this.branch = branch;
	}
	
	public int getTerritory() {
		return territory;
	}
	
	public void setTerritory(int territory) {
		this.territory = territory;
	}
	public int getDistrict() {
		return district;
	}
	
	public void setDistrict(int district) {
		this.district = district;
	}
	
	public int getActiveCustomers() {
		return activeCustomers;
	}
	
	public void setActiveCustomers(int activeCustomers) {
		this.activeCustomers = activeCustomers;
	}
	
	public int getUser() {
		return user;
	}
	
	public void setUser(int user) {
		this.user = user;
	}

	public int getActiveMonthlyCusts() {
		return activeMonthlyCusts;
	}

	public void setActiveMonthlyCusts(int activeMonthlyCusts) {
		this.activeMonthlyCusts = activeMonthlyCusts;
	}

	public List<String> getusrs() {
		return usrs;
	}

	public void setusrs(List<String> usrs) {
		this.usrs = usrs;
	}

	   
	
}
