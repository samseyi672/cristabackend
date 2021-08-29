package com.crista.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.crista.exception.ReviewException;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler
	public final ResponseEntity<Object> handleUserNameAlreadyExists(UserNameAlreadyExistsException ex , WebRequest request){
	     UserNameAlreadyExistsResponse exceptionResponse  =  new UserNameAlreadyExistsResponse(ex.getMessage()) ;
	  return new  ResponseEntity<Object>(exceptionResponse,HttpStatus.BAD_REQUEST) ;	
	}
	@ExceptionHandler
	public final ResponseEntity<Object> handleProfileNotfoundExists(ProfileException ex , WebRequest request){
	  ProfileExceptionResponse exceptionResponse  =  new ProfileExceptionResponse(ex.getMessage()) ;
	  return new  ResponseEntity<Object>(exceptionResponse,HttpStatus.BAD_REQUEST) ;	
	}
	@ExceptionHandler
	public final ResponseEntity<Object> handleVendorNotfoundExists(VendorException ex , WebRequest request){
		VendorExceptionResponse exceptionResponse  =  new VendorExceptionResponse(ex.getMessage()) ;
	  return new  ResponseEntity<Object>(exceptionResponse,HttpStatus.BAD_REQUEST) ;	
	}
	@ExceptionHandler
	public final ResponseEntity<Object> handleproductNotfoundExists(ProductException ex , WebRequest request){
		ProductExceptionResponse exceptionResponse  =  new ProductExceptionResponse(ex.getMessage()) ;
	  return new  ResponseEntity<Object>(exceptionResponse,HttpStatus.BAD_REQUEST) ;	
	}
	@ExceptionHandler
	public final ResponseEntity<Object> handlecouponNotfoundExists(CouponException ex , WebRequest request){
		CouponExceptionResponse exceptionResponse  =  new CouponExceptionResponse(ex.getMessage()) ;
	  return new  ResponseEntity<Object>(exceptionResponse,HttpStatus.BAD_REQUEST) ;	
	}
	@ExceptionHandler
	public final ResponseEntity<Object> handlecustomerNotfoundExists(CustomerException ex , WebRequest request){
		CustomerExceptionResponse exceptionResponse  =  new CustomerExceptionResponse(ex.getMessage()) ;
	  return new  ResponseEntity<Object>(exceptionResponse,HttpStatus.BAD_REQUEST) ;	
	}
	@ExceptionHandler
	public final ResponseEntity<Object> handlecustomerNotfoundExists(ReviewException ex , WebRequest request){
		ReviewExceptionResponse exceptionResponse  =  new ReviewExceptionResponse(ex.getMessage()) ;
	  return new  ResponseEntity<Object>(exceptionResponse,HttpStatus.BAD_REQUEST) ;	
	}
}
