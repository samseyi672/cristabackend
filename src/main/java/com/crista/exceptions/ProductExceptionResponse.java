package com.crista.exceptions;

public class ProductExceptionResponse {
	private String productnotfound ;

	public ProductExceptionResponse(String productnotfound) {
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
