package com.crista.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.crista.enties.UserRole;
import com.crista.enties.UserType;
import com.crista.enties.Users;
import com.crista.enties.Vendor;
import com.crista.exceptions.UnAuthorizedRequestExceptions;
import com.crista.exceptions.UserNameAlreadyExistsException;
import com.crista.repositories.UserRoleRepository;
import com.crista.repositories.UserTypeRepository;
import com.crista.repositories.VendorRepository;
import com.crista.securityconfig.JwtRequest;
import com.crista.securityconfig.JwtResponse;
import com.crista.securityconfig.JwtResponse2;
import com.crista.securityconfig.JwtTokenUtil;
import com.crista.securityconfig.UserValidationRepository;
import com.crista.securityconfig.UserValidationService;
import com.crista.securityconfig.UserValidator;
import com.crista.service.MapValidationServiceError;
import com.crista.service.UserService;

@RestController
public class UserValidationcontroller {
	// all logic for controllers is inside their services
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserValidationService userDetailsService;

	@Autowired
	private UserValidator userValidator;

	@Autowired
	private UserValidationRepository userRepository;

	@Autowired
	VendorRepository vendorRepo;

	@Autowired
	UserTypeRepository userTypeRepo;

	@Autowired
	UserRoleRepository userRoleRepo;

	@Autowired
	private MapValidationServiceError mapValidationErrorService;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody JwtRequest authenticationRequest,
			BindingResult result) throws Exception {
		System.out.println(" request " + authenticationRequest);
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationErrorService(result);
		if (errorMap != null)
			return errorMap;
		// String custdata =
		// authenticationRequest.getUsername()+","+authenticationRequest.getPassword() ;
		// BCryptPasswordEncoder pwdEncode = new BCryptPasswordEncoder() ;
		// String pwd = pwdEncode.encode(authenticationRequest.getPassword()) ;
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		System.out.println("calling the service");
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		System.out.println("from login request " + authenticationRequest.getUsername());
		System.out.println(userDetails.getUsername() + " and " + userDetails.getPassword());
		final String token = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new JwtResponse2(token));
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> register(@Valid @RequestBody Users user, BindingResult result) {
		// find pther entities to which this is attached
		// user entity is attached and also persist it here too. since am not using
		// enity relation
		System.out.println("before calling the service ");
		System.out.println(user + " password " + user.getPassword());
		userValidator.validate(user, result);
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationErrorService(result);
		if (errorMap != null)
			return errorMap;
		// disabling this endpoint for updating users
		if (user.getId() != 0) {
			return new ResponseEntity<String>(
					"This endpoint is disabled for updating.go through /update if you have already registered",
					HttpStatus.BAD_REQUEST);
		}
		// check if the vendor is available
		Vendor vendor = vendorRepo.getByVendorname(user.getVendorname());
		System.out.println("working on users and entities");
		if (!user.isVendorowner()) { // incase the person is not a vendor owner but will have to be activated at the
										// backend
			System.out.println("processing user not a  vendor owner ....");
			if ((vendor == null || vendor.getVendorname().equalsIgnoreCase(""))) {
				return new ResponseEntity<String>("This vendor does not exists.", HttpStatus.BAD_REQUEST);
			}
			// process the user and return from here if is not a vendor owner
			user.setRole(new String[] { "supervisor" });// set roles if is not admin and vendor owner
			user.setType("employee");
			Users theUser = userRepository.save(user);
			BCryptPasswordEncoder pwdEncode = new BCryptPasswordEncoder();
			user.setPassword(pwdEncode.encode(user.getPassword()));
			UserType userType = new UserType();
			userType.setUserid(String.valueOf(theUser.getId()));
			userType.setUsertype(theUser.getType());
			userTypeRepo.save(userType);
			System.out.println("persisted usertype id of " + theUser.getId() + " role " + theUser.getRole()
					+ " permission " + theUser.getPermission());
			// user role
			String roles[] = theUser.getRole();
			UserRole[] role = new UserRole[roles.length];
			int roleCounter = 0;
			// admin,supervisor,rootadmin,customer
			for (String roleType : roles) {
				role[roleCounter] = new UserRole();
				role[roleCounter].setRole(roleType);
				role[roleCounter].setUserid(String.valueOf(theUser.getId()));
				roleCounter++;
			}
			roleCounter = 0;
			// persist userrole
			userRoleRepo.saveAll(Arrays.asList(role));
			// set permission
			List<UserRole> updateRolesWithPermissions = userRoleRepo.findByUserid(String.valueOf(theUser.getId()));
			String[] permissions = user.getPermission();
			System.out.println(" permission " + user.getPermission());
			if (permissions.length != 0) {
				for (String permission : permissions) {
					if (updateRolesWithPermissions.size() <= permissions.length) {
						updateRolesWithPermissions.get(roleCounter).setPermission(permission);
						roleCounter++;
					}
					if (permissions.length > updateRolesWithPermissions.size()) {
						for (String rol : roles) {
							UserRole newUserRole = new UserRole();
							newUserRole.setPermission(permission);
							newUserRole.setUserid(String.valueOf(theUser.getId()));
							newUserRole.setRole(rol);
							updateRolesWithPermissions.add(newUserRole);
						}
					}
				}
				userRoleRepo.saveAll(updateRolesWithPermissions);
			}
			roleCounter = 0;
			// persist it back to db
			return new ResponseEntity(theUser, HttpStatus.OK);
		}
		// insert into vendor
		// if it is a vendor owner
		System.out.println(user.getEmail());
		System.out.println(user.getVendorname());
		Vendor newvendor = new Vendor();
		//newvendor.setVendoremail(user.getEmail());
		newvendor.setVendorname(user.getVendorname());
		vendorRepo.save(newvendor);
		// there are two types of users -employees and customer
		Users theUser = userRepository.save(user);
		BCryptPasswordEncoder pwdEncode = new BCryptPasswordEncoder();
		user.setPassword(pwdEncode.encode(user.getPassword()));
		UserType userType = new UserType();
		userType.setUserid(theUser.getEmail());
		theUser.setType("employee");
		userType.setUsertype(theUser.getType());
		userTypeRepo.save(userType);
		System.out.println("persisted usertype id of " + theUser.getId());
		// user role
		theUser.setRole(new String[] { "ADMIN" });
		String roles[] = theUser.getRole();
		UserRole[] role = new UserRole[roles.length];
		int roleCounter = 0;
//		  for(String roleType:roles){
//			  role[roleCounter]  = new UserRole();
//			  role[roleCounter].setRole(roleType);
//			  role[roleCounter].setUserid(String.valueOf(theUser.getId()));
//			  roleCounter++ ;
//		       }
//		  roleCounter = 0;
//		  //persist  userrole
//		  userRoleRepo.saveAll(Arrays.asList(role)) ;
		// set permission
//		  List<UserRole> updateRolesWithPermissions =  userRoleRepo.findByUserid(String.valueOf(theUser.getId())) ;
		List<UserRole> updateRolesWithPermissions = new ArrayList<>();
		String[] permissions = user.getPermission();
		if (permissions.length != 0) {
			for (String permission : permissions) {
//			   if(updateRolesWithPermissions.size()<=permissions.length) {
//				   updateRolesWithPermissions.get(roleCounter).setPermission(permission);
//				   roleCounter++ ;   
//			      }
				// if(permissions.length>updateRolesWithPermissions.size()) {
				for (String rol : roles) {
					UserRole newUserRole = new UserRole();
					newUserRole.setPermission(permission);
					newUserRole.setRole(rol);
					newUserRole.setUserid(String.valueOf(theUser.getId()));
					updateRolesWithPermissions.add(newUserRole);
				}
			}
			userRoleRepo.saveAll(updateRolesWithPermissions);
		} else {
			for (String rol : roles) {
				UserRole newUserRole = new UserRole();
				// newUserRole.setPermission(permission) ;
				newUserRole.setRole(rol);
				newUserRole.setUserid(String.valueOf(theUser.getId()));
				updateRolesWithPermissions.add(newUserRole);
			}
			userRoleRepo.saveAll(updateRolesWithPermissions);
		}
		// }
		roleCounter = 0;
		return new ResponseEntity(theUser, HttpStatus.OK);
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			System.out.println("about to authenticate ........");
			System.out.println(username + " " + password);
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			System.out.println("authenticated ......");
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		} catch (InternalAuthenticationServiceException in) {
			throw new UnAuthorizedRequestExceptions("Possibly user1 email/password does not exists");
		}

	}

	// update users
	@RequestMapping(value = "/update", method = RequestMethod.PUT) // from the backend
	public ResponseEntity<?> updateUsers(@Valid @RequestBody Users user, BindingResult result) throws Exception {
		// find pther entities to which this is attached
		// user entity is attached and also persist it here too. since am n ot using
		// enity relation
		System.out.println("before calling the service ");
		System.out.println(user + " password " + user.getPassword() + " vendorowner " + user.isVendorowner());
		System.out.println("before calling the service ");
		Users checkUser = userRepository.getById(user.getId());
		if (checkUser == null) {
			throw new UserNameAlreadyExistsException("User does not exists");
		}
		if (user.getPassword() == null || user.getConfirmPassword() == null) {
			user.setPassword("test123456");
			user.setConfirmPassword("test123456");
		} else {
			System.out.println("new user " + user);
			userValidator.validate(user, result);
			ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationErrorService(result);
			if (errorMap != null)
				return errorMap;
		}
		// there are two types of users -employees and customer
		BCryptPasswordEncoder pwdEncode = new BCryptPasswordEncoder();
		user.setPassword(pwdEncode.encode(user.getPassword()));
		Users u = userRepository.save(user);
		// set role based on vendor owner
		String roles[] = null;
		if (u.isVendorowner()) {
			roles = new String[] { "ADMIN" };
		} else if (!u.isVendorowner()) {
			roles = new String[] { "SUPERVISOR" };
		}
		List<UserRole> listofRoles = userRoleRepo.findByUserid(String.valueOf(u.getId()));
		;
		System.out.println("listofroles size " + listofRoles.size());
		int counter = 0;
		if (listofRoles.isEmpty()) {
			UserType userType = new UserType();
			userType.setUserid(String.valueOf(user.getId()));
			user.setType("employee");
			userType.setUsertype(user.getType());
			userTypeRepo.save(userType);
			if (user.getPermission().length != 0) {
				for (String permission : user.getPermission()) {
//						   if(updateRolesWithPermissions.size()<=permissions.length) {
//							   updateRolesWithPermissions.get(roleCounter).setPermission(permission);
//							   roleCounter++ ;   
//						      }
					// if(permissions.length>updateRolesWithPermissions.size()) {
					for (String rol : roles) {
						UserRole newUserRole = new UserRole();
						newUserRole.setPermission(permission);
						newUserRole.setRole(rol);
						newUserRole.setUserid(String.valueOf(user.getId()));
						listofRoles.add(newUserRole);
					}
				}
				userRoleRepo.saveAll(listofRoles);
			} else {
				for (String rol : roles) {
					UserRole newUserRole = new UserRole();
				   // newUserRole.setPermission(permission) ;
					newUserRole.setRole(rol);
					newUserRole.setUserid(String.valueOf(user.getId()));
					listofRoles.add(newUserRole);
				}
				userRoleRepo.saveAll(listofRoles);
			}

		} else {
			counter = 0; // reset
			if (user.getPermission().length != 0) {
				for (String permission : user.getPermission()) {
					if(listofRoles.size() >= user.getPermission().length) {
						for (String rol : roles) {
							listofRoles.get(counter).setRole(rol);
							listofRoles.get(counter).setPermission(permission);
						       }						 
					       }else if(listofRoles.size() < user.getPermission().length) {
					    	   System.out.println("user permission "+user.getPermission()+" size "+ listofRoles.size());
					    	   if(counter <= (listofRoles.size()-1)) {
					    		   for (String rol : roles) {
					     System.out.println("counter"+counter+"user permission "+user.getPermission()+" size "+ listofRoles.size());
										listofRoles.get(counter).setRole(rol);
										listofRoles.get(counter).setPermission(permission);
									       }  
					    	           }else if(counter > listofRoles.size()) {  // then create  new roles
					    	        	   UserRole newRole  =  new UserRole() ;
					    	     System.out.println("counter"+counter+"user permission "+user.getPermission()+" size "+ listofRoles.size());
					    	        	   for (String rol : roles) {
					    	        		   newRole.setRole(rol);
					    	        		   newRole.setPermission(permission);
					    	        		   newRole.setUserid(String.valueOf(user.getId()));
											       } 
					    	        	    listofRoles.add(newRole) ;
					    	                }
					          }	
                         counter++ ;
				       }
				userRoleRepo.saveAll(listofRoles);
			}
		}
		return new ResponseEntity("successful", HttpStatus.OK);
		// return userService.updateUser(user) ;
	}

	// delete users
	@DeleteMapping("/delete/{id}")
	// @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public ResponseEntity<?> deleteUsers(@PathVariable("id") long id) {
		Users user = userRepository.getById(id);
		if (user == null) {
			throw new UsernameNotFoundException("User with the id could not  be  found " + id);
		}
		System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		System.out.println(user);
		// delete user role
		List<UserRole> userRole = userRoleRepo.findByUserid(String.valueOf(id));
		userRoleRepo.deleteAll(userRole);
		// delete usertype
		UserType userType = userTypeRepo.getByUserid(user.getEmail());
		userTypeRepo.save(userType);
		// delete user
		userRepository.delete(user);
		return new ResponseEntity<String>("User removed", HttpStatus.OK);
	}

	   @GetMapping("/usersearch")
	public ResponseEntity<?> searchusers(@RequestParam(value="vendorname",required = false) String vendorname,
			@RequestParam("action_users") String action_users) {	
		System.out.println(" vendorname " + vendorname + " firstname " + action_users);
		List<Users> user  =null;
		if(vendorname != null) {
		   user = userRepository.getByVendornameAndFirstnameContainingIgnoreCase(vendorname, action_users);
			if (user == null) {
				throw new RuntimeException("user does not exists");
			}
			user.stream().forEach(u -> {
				u.setPassword("");
				u.setUserdate(null);
				u.setToken("");
				u.setType("");
				//u.setVendorowner(false);
				u.setPermission(null);
				u.setRole(null);
			});	
		}else if(vendorname == null){
			 user = userRepository.getByVendornameContainingIgnoreCase(action_users);
				if (user == null) {
					throw new RuntimeException("user does not exists");
				}
				user.stream().forEach(u -> {
					u.setPassword("");
					u.setUserdate(null);
					u.setToken("");
					u.setType("");
					//u.setVendorowner(false);
					u.setPermission(null);
					u.setRole(null);
				});		
		}
		return new ResponseEntity<List<Users>>(user, HttpStatus.OK);
	}

	  @GetMapping("/vendoruser")
	public ResponseEntity<?> returnUser(@RequestParam("vendorname") String vendorname, @RequestParam("id") String id) {
		Users user = userRepository.getByVendornameAndId(vendorname, Integer.parseInt(id));
		if (user == null) {
			throw new RuntimeException("user does not exists");
		}
		return new ResponseEntity<Users>(user, HttpStatus.OK);
	}

	  @PutMapping("/user")
	public ResponseEntity<?> updateUser(@Valid @RequestBody Users user, BindingResult result) {
		Users myUser = userRepository.getById(user.getId());
		if (myUser == null) {
			throw new UsernameNotFoundException("user not found");
		}
		userRepository.getByVendornameAndId(user.getVendorname(), user.getId());
		return new ResponseEntity<String>("User updated", HttpStatus.OK);
	}
	   @GetMapping("/finduser/{userid}")
	public Users findUser(@PathVariable("userid") long userid) {
		// get user by id 
		Users user  = userRepository.findUsersById(userid)  ;
		if(user == null) {
		  throw new UserNameAlreadyExistsException("user not found")  ;	
		}
		System.out.println("user "+user);
		user.setPassword(null);
		user.setConfirmPassword(null);
		user.setVendorowner(false);
		user.setVendorname(null);
		return user ;
	}
}



