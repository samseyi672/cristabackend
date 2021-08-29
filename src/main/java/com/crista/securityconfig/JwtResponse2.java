package com.crista.securityconfig;

public class JwtResponse2 {
	private String jwtToken;
	public JwtResponse2(){}
	public JwtResponse2(String jwttoken) {
		this.jwtToken = jwttoken;
	}
	public String getJwtToken() {
		return jwtToken;
	}
	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
	@Override
	public String toString() {
		return "JwtResponse [jwtToken=" + jwtToken + "]";
	}

}
