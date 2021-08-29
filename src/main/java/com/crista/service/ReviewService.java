package com.crista.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crista.enties.Customer;
import com.crista.enties.Review;
import com.crista.exception.ReviewException;
import com.crista.repositories.CustomerRepository;
import com.crista.repositories.ReviewRepository;

@Service
public class ReviewService {
 @Autowired
 ReviewRepository reviewRepo ;
 @Autowired
 CustomerRepository custRepo ;
 
 public Review save(Review review) {
	 String email  =  review.getEmail() ;
	Customer review2  =  custRepo.getByEmail(email) ;
	//System.out.println("customer email "+ review2.getEmail());
	  if(review2== null) {  // check
		  System.out.println("email not equal .......");
		   throw new ReviewException("please use a registered mail") ; 
	       }
	  System.out.println("email equal .......");
	 return reviewRepo.save(review) ;
 }
public List<Review> getReview() {
	return reviewRepo.findAll() ;
}
public void deleteReview(Review rev) {
   reviewRepo.delete(rev);
}
 
 
 
 
}
