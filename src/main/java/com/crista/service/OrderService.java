package com.crista.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.crista.enties.Order;
import com.crista.repositories.OrderRepository;

@Service
public class OrderService {

	@Autowired
	OrderRepository repo ;
	
	public Order create(Order order) {
		return repo.save(order) ;
	}
	public List<Order> createOders(List<Order> order) {
		return (List<Order>) repo.saveAll(order) ;
	}
	public void delete(long id) {
		if(repo.getById(id) != null) {
			 throw new UsernameNotFoundException("order not found") ;
		   }
	}
	public List<Order> getAllOrers(){
		return repo.findAll();
	}
	public List<Order> findByOrderId(String orderid){
	   return repo.getByOrdid(orderid) ;	
	}
}











































































































































