package com.crista.exceptions;

public class ReviewExceptionResponse {
 private String reviewnotfound ;

 
public ReviewExceptionResponse(String reviewnotfound) {
	super();
	this.reviewnotfound = reviewnotfound;
}

public String getReviewnotfound() {
	return reviewnotfound;
}

public void setReviewnotfound(String reviewnotfound) {
	this.reviewnotfound = reviewnotfound;
}
 
}
