package com.crista.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAuthorizedRequestExceptions extends RuntimeException {
	public UnAuthorizedRequestExceptions(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
}
