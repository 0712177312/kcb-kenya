package com.compulynx.compas.models.t24Models;

public class T24UpdateParams {
	String mnemonic;
	String status;
	
	public T24UpdateParams(String mnemonic,String status) {
		this.mnemonic = mnemonic;
		this.status=status;
	}

	public String getMnemonic() {
		return mnemonic;
	}
	public String getStatus() {
		return status;
	}

}
