package com.crista.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VendorException extends RuntimeException {

	public VendorException(String message) {
		super(message);
	}

}
