package com.crista.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crista.utils.CartProduct;
import com.crista.utils.CartProductDao;

@RestController
public class CartProductController {
	
	@Autowired
	private CartProductDao dao;

	@PostMapping("/cartproduct")
	public CartProduct save(@RequestBody CartProduct product) {
		return dao.save(product);
	}
	
	@PutMapping("/cartproduct")
	public CartProduct saveUpdate(@RequestBody CartProduct product) {
		return dao.save(product);
	}
	
	@GetMapping("/cartproduct")
	public List<CartProduct> getAllProducts() {
		return dao.findAll();
	}

	@GetMapping("/cartproduct/{id}")
	public CartProduct findProduct(@PathVariable Long id) {
		System.out.println(" cart entered  here ");
		CartProduct prd  = dao.findCartProductById(id);
		System.out.println(prd);
		return prd;
	}

	@DeleteMapping("/cartproduct/{id}")
	public String remove(@PathVariable int id) {
		return dao.deleteProduct(new Long(id));
	}
}
