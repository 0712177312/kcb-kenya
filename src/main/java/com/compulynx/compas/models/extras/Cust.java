package com.compulynx.compas.models.extras;

import java.util.List;

public class Cust {
	private String customerId;
	private String customerName;
	private String phoneNumber;
	private String country;
	private String customerIdNumber;
	private String nationality;
	private String gender;
	private String mnemonic;
	private List<FingerPrint> fingerPrints;
	
	public Cust(String customerId, String customerName, String phoneNumber, String country, String customerIdNumber,
			String nationality, String gender, String mnemonic, List<FingerPrint> fingerPrints) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
		this.phoneNumber = phoneNumber;
		this.country = country;
		this.customerIdNumber = customerIdNumber;
		this.nationality = nationality;
		this.gender = gender;
		this.mnemonic = mnemonic;
		this.fingerPrints = fingerPrints;
	}
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCustomerIdNumber() {
		return customerIdNumber;
	}
	public void setCustomerIdNumber(String customerIdNumber) {
		this.customerIdNumber = customerIdNumber;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getMnemonic() {
		return mnemonic;
	}
	public void setMnemonic(String mnemonic) {
		this.mnemonic = mnemonic;
	}
	public List<FingerPrint> getFingerPrints() {
		return fingerPrints;
	}
	public void setFingerPrints(List<FingerPrint> fingerPrints) {
		this.fingerPrints = fingerPrints;
	}
	
	
	
	
}
