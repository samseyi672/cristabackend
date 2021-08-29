package com.crista.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CouponExceptionResponse{

	private String productnotfound ;

	public CouponExceptionResponse(String productnotfound) {
		super();
		this.productnotfound = productnotfound;
	}

	public String getProductnotfound() {
		return productnotfound;
	}

	public void setProductnotfound(String productnotfound) {
		this.productnotfound = productnotfound;
	}

}
