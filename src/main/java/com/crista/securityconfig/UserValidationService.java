package com.crista.securityconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crista.enties.UserRole;
import com.crista.enties.Users;
import com.crista.exceptions.UserNameAlreadyExistsException;
import com.crista.repositories.UserRoleRepository;

@Service
public class UserValidationService implements UserDetailsService {

	@Autowired
	UserValidationRepository userDao;
	@Autowired
	UserRoleRepository userRole;

	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		System.out.println(" the email in the userdetailservice  " + email);
		Users user = userDao.getByEmail(email);
		System.out.println("user " + user.getFirstname());
		if (user == null) {
			throw new UsernameNotFoundException("User not found with mail and/or password ");
		}
		System.out.println("user id " + String.valueOf(user.getId()));
		List<UserRole> roles = userRole.findByUserid(String.valueOf(user.getId()));
//		List<UserRole> roles2 = userRole.findAll(String.valueOf(user.getId()));
//		System.out.println(roles2.size());
		if (roles == null) {
			throw new UserNameAlreadyExistsException("User not found with mail and/or password ");
		}
//		List<GrantedAuthority> grantedAuthorities = roles.stream().map(r -> {
//			System.out.println("ROLE_" + r.getRole());
//				 return new SimpleGrantedAuthority("ROLE_" + r.getRole());	 
//		}).collect(Collectors.toList());
		// I have used this  because  users might  likely have roles  and  permission	
//		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
//				grantedAuthorities);
		System.out.println(roles);
		System.out.println("going to set roles and  permission");
		HashMap<String,GrantedAuthority> grantedAuthorities2 = listToMap(roles) ;
		// different between roles  by checking  if start  with ROLE
		System.out.println("granted authorities "+ grantedAuthorities2.values());
		System.out.println("roles and permission set "+ grantedAuthorities2.values());
		//return  new User( .getEmail(),user.getPassword(),grantedAuthorities2.values());
		return new User(user.getEmail(), user.getPassword(),grantedAuthorities2.values()) ;
	}
	public HashMap<String, GrantedAuthority> listToMap(List<UserRole> userRole) {
		HashMap<String, GrantedAuthority> map  = new HashMap() ;
	      for (UserRole userRole2 : userRole) {
//	    	    if(userRole2.getRole() != null && userRole2.getPermission()!=null) {
//			   map.put("role",new SimpleGrantedAuthority("ROLE_"+userRole2.getRole())) ;
//			   System.out.println("role "+userRole2.getRole());
//	    	      }
	    	    if(userRole2.getRole() != null) {
	 			   map.put("role",new SimpleGrantedAuthority("ROLE_"+userRole2.getRole())) ;
	 			   System.out.println("role "+userRole2.getRole());
	 	    	      }
	    	    if(userRole2.getPermission() !=null && userRole2.getPermission()!=null) {
		 			   map.put("permission",new SimpleGrantedAuthority(userRole2.getPermission())) ;
		 			   System.out.println("permission "+userRole2.getPermission());
		 	    	       }  
		      }
	     return map ;
	}
	//delete users
	
}









































