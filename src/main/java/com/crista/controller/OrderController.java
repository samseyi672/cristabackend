package com.crista.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crista.enties.Customer;
import com.crista.enties.Order;
import com.crista.enties.Profile;
import com.crista.enties.Users;
import com.crista.exceptions.CustomerException;
import com.crista.repositories.CustomerRepository;
import com.crista.securityconfig.UserValidationRepository;
import com.crista.service.MapValidationServiceError;
import com.crista.service.OrderService;
import com.crista.utils.CompositeOrder;

import net.bytebuddy.utility.RandomString;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	OrderService oService ;
	
	@Autowired
    UserValidationRepository userRepo ;
	
	@PostMapping("/create")
	public ResponseEntity<?> create(@Valid @RequestBody CompositeOrder order,BindingResult result) {
	  	System.out.println("order "+order);
	  Users cust =  userRepo.getById(Long.parseLong(order.getOrder()[0].getUserid())) ;
	    if(cust == null){
	    	System.out.println("null user");
	     throw new CustomerException("please use a registered email")	;
	     }
	    if(!cust.getEmail().equalsIgnoreCase(order.getOrder()[0].getEmail())) {
	    	System.out.println("incorrect email");
	    	 throw new CustomerException("please use a registered email")	;
	            }
	  	Order[] productorder  =  order.getOrder()  ;
	  	String orderid  = RandomString.make(7) ;
	  	List<Double> listoftotal  = new ArrayList<Double>()  ;
	  	Arrays.asList(productorder).stream().forEach(o->{
	  		o.setOrdid(orderid);
	  		double peritemsold  = 0.0;
	    if(o.getQuantity()!=null && o.getPrice() != null) {
	    peritemsold = Double.parseDouble(o.getQuantity())*Double.parseDouble(o.getPrice().replace("N","").replace(",","").replace(".","")) ; 
	       }	  	  
	    System.out.println("per item sold "+ peritemsold);
	  	    o.setTotal(String.valueOf(peritemsold));  // reset total for checking
	  		listoftotal.add(Double.parseDouble(o.getTotal().replace("N","").replace(",","").replace(".",""))) ;  // recalculate  total  		
	  	});
	  	//sum total
	  final	double overalltotal = listoftotal.stream().mapToDouble(f -> f.doubleValue()).sum();
	  	Arrays.asList(productorder).stream().forEach(o->{
	  		o.setOrdstatus("processing");
	  		o.setPaymentstatus("awaiting");
	  		o.setOveralltotal(String.valueOf(overalltotal));
	  	});
	  	System.out.println(" Total order "+ Arrays.asList(productorder));
	    oService.createOders(Arrays.asList(productorder));
		return new ResponseEntity<String>("Order successful",HttpStatus.OK) ;
	}
//	@PostMapping("/create")
//	public ResponseEntity<?> create(@Valid @RequestBody Order order,BindingResult result) {
//	  	System.out.println("order "+order);
//	  	Order order_result  = oService.create(order) ;
//	  	System.out.println(order_result);
//		return new ResponseEntity<String>("Order successful",HttpStatus.OK) ;
//	}
	    @DeleteMapping("/delete/{id}")
	public ResponseEntity<?> create(@PathVariable("id") long id) {
		 	oService.delete(id);	
		return new ResponseEntity<String>("order removed",HttpStatus.OK) ;
	}
	
	    @GetMapping("/order")
	    public  String getAllOrders() {
//	    	  <tr>
//              <td>#51240</td>                                   
//              <td><span class="badge badge-secondary">Cash On Delivery</span></td>
//              <td>Paypal</td>
//              <td><span class="badge badge-success">Delivered</span></td>
//              <td>Dec 10,18</td>
//              <td>$54671</td>
//              <td><span class="fa fa-eye" style="cursor:pointer;" onclick="showprd('2')" title="view details">
//              </span><span class="fa fa-remove" style="cursor:pointer;" onclick="" title="cancel"></span></td>
//          </tr>
	    	List<Order> orders  = oService.getAllOrers();
	       StringBuffer ordertext  =  new StringBuffer();
	       orders.parallelStream().forEach(ord->{
	    	   ordertext.append("<tr><td>"+ord.getOrdid()+"</td>") ;
	           ordertext.append("<td><span class=\"badge badge-secondary\">"+ord.getPaymentstatus()
	           +"</span></td>") ;
	           ordertext.append("<td>"+ord.getPaymentgroup()+"</td>") ;   
	           ordertext.append("<td><span class=\"badge badge-success\">"+ord.getOrdstatus()+"</span></td>");
	           ordertext.append("<td>"+ord.getOrderdate()+"</td>") ;
	           ordertext.append("<td>"+ord.getTotal()+"</td>") ;
	           ordertext.append("<td><span class=\"fa fa-eye\" style=\"cursor:pointer;\" onclick=\"showprd('"+ord.getOrdid()+"')\" title=\"view details\"> </span>") ;
	           ordertext.append("<span class=\"fa fa-remove\" style=\"cursor:pointer;\" onclick=\"\" title=\"cancel\"></span></td>") ;
	           });
	       return ordertext.toString() ;
	    }
	    
    @GetMapping("orders/{id}")
   public List<Order> getOrdersById(@PathVariable("id") String id){
	   return oService.findByOrderId(id); 
   }
}


























































































































































































































































