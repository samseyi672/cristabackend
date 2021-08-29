package com.crista.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.crista.enties.Profile;
import com.crista.repositories.ProfileRepository;

@Service
public class ProfileService {
	
 @Autowired
 ProfileRepository profileRepo ;
 
 public Profile create(Profile profile) {
	return profileRepo.save(profile) ;
   }
 
 public Profile whereProfile(String userid) {
		return profileRepo.getByUserid(userid) ;
	   }
 public void delete(long id) {
	 Profile p  = profileRepo.getById(id)  ;
	 if(p == null){
		 throw new UsernameNotFoundException("This profile pic does not exists") ; 
	 }
	profileRepo.delete(p) ;	   
   }
}

























































































































