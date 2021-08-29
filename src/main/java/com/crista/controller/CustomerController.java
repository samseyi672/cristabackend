package com.crista.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crista.enties.Customer;
import com.crista.enties.UserRole;
import com.crista.enties.UserType;
import com.crista.enties.Users;
import com.crista.exceptions.CustomerException;
import com.crista.exceptions.UnAuthorizedRequestExceptions;
import com.crista.repositories.CustomerRepository;
import com.crista.repositories.UserRoleRepository;
import com.crista.repositories.UserTypeRepository;
import com.crista.securityconfig.JwtRequest;
import com.crista.securityconfig.JwtResponse2;
import com.crista.securityconfig.JwtTokenUtil;
import com.crista.securityconfig.UserValidationRepository;
import com.crista.securityconfig.UserValidationService;
import com.crista.service.CustomerService;
import com.crista.service.MapValidationServiceError;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	private MapValidationServiceError mapValidationErrorService;
	
	/*
	 * @Autowired private AuthenticationManager authenticationManager;
	 * 
	 * @Autowired private JwtTokenUtil jwtTokenUtil;
	 * 
	 * @Autowired private UserValidationService userDetailsService;
	 */

	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Autowired
	CustomerRepository customerRepo ;
	
	@Autowired
	CustomerService customerService ;
	
	@Autowired
	UserTypeRepository userTypeRepo  ;
	
	@Autowired
	UserRoleRepository userRoleRepo  ;
	
	 @Autowired
	private UserValidationRepository userRepository;
	 
	 @PutMapping("/register")
	 public  ResponseEntity<?> updateRegister(@Valid @RequestBody Customer cust,BindingResult result,HttpServletRequest request){
		 String  url  =  request.getRequestURL().toString() ;
		Customer custRes  = customerRepo.getByEmail(cust.getEmail())  ;
		  if(custRes ==null){
			throw new CustomerException("You have not registered")   ;
		    }
		  cust.setId(custRes.getId());
		  customerRepo.save(cust) ;
		return new ResponseEntity<String>("successful",HttpStatus.OK); 
	 }
	
	 @PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody Customer cust,BindingResult result,HttpServletRequest request){		
		 ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationErrorService(result);
			if (errorMap != null)
				return errorMap;
			BCryptPasswordEncoder pwdEncode = new BCryptPasswordEncoder();
			cust.setPassword(pwdEncode.encode(cust.getPassword()));
		Customer customer  = customerRepo.save(cust)  ;
		 Users users  =  new Users() ;
		 users.setEmail(cust.getEmail());
		 users.setFirstname(cust.getFirstname());
		 users.setLastname(cust.getLastname());
		 users.setVendorname("default");  //since  vendor name  is required , we have  to chose  a default  name  for vendorname
		 users.setVendorowner(false);
		 users.setPassword(cust.getPassword());
		// users.setUserstatus("1");
		 userRepository.save(users)  ;
		UserType userType  = new UserType() ;
		    userType.setUserid(String.valueOf(users.getId()));
		    userType.setUsertype("CUSTOMER");
		    userTypeRepo.save(userType) ;
		    UserRole userRole  = new UserRole() ;
		    userRole.setRole("CUSTOMER");
		    userRole.setUserid(String.valueOf(users.getId()));
		  //  userRole.setPermission("");
		    userRoleRepo.save(userRole) ;
		    System.out.println("overall insertion");
		  return new ResponseEntity<String>(String.valueOf(customer.getId()),HttpStatus.OK);
	}
	
	 @GetMapping("/register")
  public List<Customer> getAllCustomer(){
	  return customerService.getAllCustomer(); 
  }
	 @DeleteMapping("/register")
  public Customer deleteCustomer(Customer rev){
	  return customerService.deleteCustomer(rev); 
  } 
}



































































