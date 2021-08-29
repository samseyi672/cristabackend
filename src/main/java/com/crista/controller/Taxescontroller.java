package com.crista.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.crista.enties.Taxes;

@RestController
@RequestMapping("/taxes")
public class Taxescontroller {

	@Autowired
	TaxesService taxesService;

	@PostMapping("/updatetax")
	public Taxes createTax(@Valid @ModelAttribute Taxes tax, BindingResult result) {
		System.out.println("got here to category " + tax);
		return taxesService.createTax(tax);
		// return "created successfully";
	}

	@GetMapping("/updatetax")
	public List<Taxes> getCategory() {
		System.out.println("got here to category ");
		return taxesService.getAllCategories();
	}

	@PutMapping("/updatetax")
	public String updateTax(@Valid @ModelAttribute Taxes tax, BindingResult result) {
		System.out.println("calling put tax ....");
		System.out.println(tax);
		taxesService.createTax(tax);
		return "successful";
	}

	@DeleteMapping("/updatetax")
	public String deleteCategory(@Valid @ModelAttribute Taxes tax, BindingResult result) {
		System.out.println("got here to category " + tax);
		taxesService.deleteTax(tax);
		return "successful";
	}
}
