package com.crista.exceptions;

public class ProfileExceptionResponse {
  private String profilenotfound ;

public ProfileExceptionResponse(String profilenotfound) {
	super();
	this.profilenotfound = profilenotfound;
}

public String getProfilenotfound() {
	return profilenotfound;
}

public void setProfilenotfound(String profilenotfound) {
	this.profilenotfound = profilenotfound;
}
  
}
