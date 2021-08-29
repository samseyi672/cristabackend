package com.crista.exceptions;

public class CustomerExceptionResponse {
private String customerNotFound ;

public CustomerExceptionResponse(String customerNotFound) {
	super();
	this.customerNotFound = customerNotFound;
}

public String getCustomerNotFound() {
	return customerNotFound;
}

public void setCustomerNotFound(String customerNotFound) {
	this.customerNotFound = customerNotFound;
}

}
