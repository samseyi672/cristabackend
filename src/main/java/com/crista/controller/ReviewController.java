package com.crista.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crista.enties.Category;
import com.crista.enties.Review;
import com.crista.service.ReviewService;

@RestController
@RequestMapping()
public class ReviewController {

	@Autowired
	ReviewService revService;

	@PostMapping("/review")
	public ResponseEntity<?> saveReview(@Valid @ModelAttribute Review rev, BindingResult result) {
		revService.save(rev);
		return new ResponseEntity<String>("successful", HttpStatus.OK);
	}

	@GetMapping("/review")
	public List<Review> getReview() {
		System.out.println("got here to category ");
		return revService.getReview();
	}

	@PutMapping("/review")
	public String updateCategory(@Valid @ModelAttribute Review rev, BindingResult result) {
		System.out.println("got here to category " + rev);
		revService.save(rev);
		return "successful";
	}

	@DeleteMapping("/review")
	public String deleteCategory(@Valid @ModelAttribute Review rev, BindingResult result) {
		System.out.println("got here to category " + rev);
		revService.deleteReview(rev);
		return "successful";
	}
}
