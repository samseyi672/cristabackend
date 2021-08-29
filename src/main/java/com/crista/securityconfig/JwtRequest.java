package com.crista.securityconfig;

import javax.validation.constraints.NotBlank;

public class JwtRequest {
	@NotBlank(message = "Username email cannot be blank")
	private String username;    
    @NotBlank(message = "Password cannot be blank")
	private String password;	
	//default constructor for JSON Parsing
	public JwtRequest()
	{
	}
	public JwtRequest(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
	}
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "JwtRequest [username=" + username + ", password=" + password + "]";
	}
}
