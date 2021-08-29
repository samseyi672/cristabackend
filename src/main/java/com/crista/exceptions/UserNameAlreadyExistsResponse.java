package com.crista.exceptions;

import lombok.Data;

@Data
public class UserNameAlreadyExistsResponse {
	private String email ;
	public UserNameAlreadyExistsResponse(String email) {
		super();
		this.email = email;
	}
}
