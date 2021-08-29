package com.crista.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CouponException extends RuntimeException {

	public CouponException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
