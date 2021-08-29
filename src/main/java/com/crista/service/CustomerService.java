package com.crista.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crista.enties.Customer;
import com.crista.repositories.CustomerRepository;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepository custRepo ;
	
public List<Customer> getAllCustomer(){
	return  custRepo.findAll() ;
}

public Customer deleteCustomer(Customer rev) {
	custRepo.delete(rev);
	return rev ;
}
}
