package com.crista.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crista.enties.UserType;
import com.crista.repositories.UserRoleRepository;
import com.crista.repositories.UserTypeRepository;

@Service
public class UserTypeService {
	 
	 @Autowired
	 UserTypeRepository userTypeRepo ;
	
	 public UserType create(UserType userType) {
		 return userTypeRepo.save(userType)  ;
	 }
}
