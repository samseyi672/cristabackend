package com.crista.exceptions;

public class VendorExceptionResponse {
private String  vendornotfound  ;

public VendorExceptionResponse(String vendornotfound) {
	super();
	this.vendornotfound = vendornotfound;
}

public String getVendornotfound() {
	return vendornotfound;
}

public void setVendornotfound(String vendornotfound) {
	this.vendornotfound = vendornotfound;
}

}
