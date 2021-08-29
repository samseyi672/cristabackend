package com.crista.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.crista.enties.UserRole;
import com.crista.enties.UserType;
import com.crista.enties.Users;
import com.crista.enties.Vendor;
import com.crista.repositories.UserRoleRepository;
import com.crista.repositories.UserTypeRepository;
import com.crista.repositories.VendorRepository;
import com.crista.securityconfig.UserValidationRepository;

@Service
public class UserService { 
       @Autowired
       private VendorRepository vendorRepo ;      
       @Autowired
       private UserTypeRepository userTypeRepo ;
  	 
  	 @Autowired
  	private	 UserRoleRepository userRoleRepo ;
      
       @Autowired
   	private UserValidationRepository userRepository ;
	
 public  ResponseEntity<?> updateUser(Users user) {
	 Vendor vendor   =  vendorRepo.getByVendorname(user.getVendorname()) ;
	 if(!user.isVendorowner()) {  // incase  the person  is  not a vendor owner but will have  to be activated at the backend	
			System.out.println("processing user not a  vendor owner ....");
			if((vendor == null || vendor.getVendorname().equalsIgnoreCase(""))){
				  return new ResponseEntity<String>("This vendor does not exists.",HttpStatus.BAD_REQUEST) ;	   
				       }
			//process the user and return from here if is not a vendor owner
			    user.setRole(new String[]{"supervisor"});//set roles if is not admin and vendor owner
			     user.setType("employee");
			  Users theUser  = userRepository.save(user) ;
				BCryptPasswordEncoder pwdEncode  =  new BCryptPasswordEncoder() ;
				  user.setPassword(pwdEncode.encode(user.getPassword()));
				  UserType userType  =  new UserType() ;
				  userType.setUserid(String.valueOf(theUser.getId()));
				  System.out.println("usertype "+user.getType()+" and new usertype "+ theUser.getType());
				  userType.setUsertype(user.getType());
				  userTypeRepo.save(userType) ;
				  System.out.println("persisted usertype id of "+theUser.getId()+" role "+theUser.getRole()+
						  " permission "+theUser.getPermission());
				  //user role 
				  String roles[]  = theUser.getRole() ;
				  UserRole[] role  = new UserRole[roles.length] ;
				   int roleCounter =  0 ;
				   //admin,supervisor,rootadmin,customer
				  for(String roleType:roles){
					  role[roleCounter]  = new UserRole();
					  role[roleCounter].setRole(roleType);
					  role[roleCounter].setUserid(String.valueOf(theUser.getId()));
					  roleCounter++ ;
				       }
				  roleCounter = 0;
				  //persist  userrole
				  userRoleRepo.saveAll(Arrays.asList(role)) ;
				  //set permission
				  List<UserRole> updateRolesWithPermissions =  userRoleRepo.findByUserid(String.valueOf(theUser.getId())) ;
				  String[] permissions  = user.getPermission() ; 
				  System.out.println(" permission "+ user.getPermission());
				  if(permissions.length != 0) {
					  for (String permission : permissions){
						   if(updateRolesWithPermissions.size()<=permissions.length) {
							   updateRolesWithPermissions.get(roleCounter).setPermission(permission);
							   roleCounter++ ;   
						      }
						   if(permissions.length>updateRolesWithPermissions.size()){
							   for(String rol:roles) {
								   UserRole newUserRole =  new UserRole() ;
								   newUserRole.setPermission(permission) ;
								   newUserRole.setUserid(String.valueOf(theUser.getId()));
								   newUserRole.setRole(rol);
								   updateRolesWithPermissions.add(newUserRole) ;
							           }						   
					                 }
					             }
					  userRoleRepo.saveAll(updateRolesWithPermissions) ;
				            }			  
				     roleCounter = 0;
				  //persist it back to db		
				   return new ResponseEntity(theUser,HttpStatus.OK);
		       }
		        // insert  into vendor 
		// if it is a vendor owner 
		System.out.println(user.getEmail());
		System.out.println(user.getVendorname());
		     Vendor newvendor  = new Vendor() ;
		    // newvendor.setVendoremail(String.valueOf(user.getId()));
		     newvendor.setVendorname(user.getVendorname());
		     vendorRepo.save(newvendor) ;
		//there are two types of users -employees and customer 
		  Users theUser  = userRepository.save(user) ;
		BCryptPasswordEncoder pwdEncode  =  new BCryptPasswordEncoder() ;
		  user.setPassword(pwdEncode.encode(user.getPassword()));
		  UserType userType  =  new UserType() ;
		  userType.setUserid(theUser.getEmail());
		  theUser.setType("employee");
		  userType.setUsertype(theUser.getType());
		  userTypeRepo.save(userType) ;
		  System.out.println("persisted usertype id of "+theUser.getId());
		  //user role 
		  theUser.setRole(new String[]{"ADMIN"});
		  String roles[]  = theUser.getRole() ;
		  UserRole[] role  = new UserRole[roles.length] ;
		   int roleCounter =  0 ;
		  for(String roleType:roles){
			  role[roleCounter]  = new UserRole();
			  role[roleCounter].setRole(roleType);
			  role[roleCounter].setUserid(String.valueOf(theUser.getId()));
			  roleCounter++ ;
		       }
		  roleCounter = 0;
		  //persist  userrole
		  userRoleRepo.saveAll(Arrays.asList(role)) ;
		  //set permission
		  List<UserRole> updateRolesWithPermissions =  userRoleRepo.findByUserid(String.valueOf(theUser.getId())) ;
		  String[] permissions  = user.getPermission() ;
		  if(permissions.length !=0){
		   for (String permission : permissions) {
			   if(updateRolesWithPermissions.size()<=permissions.length) {
				   updateRolesWithPermissions.get(roleCounter).setPermission(permission);
				   roleCounter++ ;   
			      }
			   if(permissions.length>updateRolesWithPermissions.size()) {
				 for(String rol:roles){
					 UserRole newUserRole =  new UserRole() ;
					   newUserRole.setPermission(permission) ;
					   newUserRole.setRole(rol);
					   newUserRole.setUserid(String.valueOf(theUser.getId()));
					   updateRolesWithPermissions.add(newUserRole) ;
				              }			 			  
		                 }
		             }
		         }
		     roleCounter = 0;
		  //persist it back to db
		  userRoleRepo.saveAll(updateRolesWithPermissions) ;
		return new ResponseEntity<String>("Successful",HttpStatus.OK);		
	 // return user ; 
 }	
	
	
}



















































































































































