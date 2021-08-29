package com.crista.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crista.enties.UserRole;
import com.crista.enties.UserType;
import com.crista.repositories.UserRoleRepository;

@Service
public class UserRoleService {
	   @Autowired
	 UserRoleRepository userRoleRepo ;
	   public UserRole create(UserRole userType) {
			 return userRoleRepo.save(userType)  ;
		 }
}
