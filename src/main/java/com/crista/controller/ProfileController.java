package com.crista.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.crista.enties.Profile;
import com.crista.exceptions.ProfileException;
import com.crista.repositories.ProfileRepository;
import com.crista.securityconfig.JwtRequest;
import com.crista.service.MapValidationServiceError;
import com.crista.service.ProfileService;

@RestController
@RequestMapping("/profile")
public class ProfileController {
@Autowired
ProfileService pService ;
	
	@Autowired
	private MapValidationServiceError mapValidationErrorService;
	
	@Autowired
	ProfileService p  ;
	
	@Value("${upload.dir}")
	String fileDirectory;

	@PostMapping(value = "/create", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = "application/json")
	public ResponseEntity<?> create(@Valid @ModelAttribute Profile profile
			,@RequestParam(value = "profilepict") MultipartFile file,BindingResult result) throws IOException{
//       ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationErrorService(result);
//        if(errorMap != null) 
//        	return errorMap;	
		System.out.println("got here to category creation 2 "+profile.toString());
		File filetoupload = new File(fileDirectory + "/" + file.getOriginalFilename());
		filetoupload.createNewFile();
		FileOutputStream out = new FileOutputStream(filetoupload);
		out.write(file.getBytes());
		out.close();
		Profile myProfile  = pService.whereProfile(profile.getUserid()) ;
		if(myProfile != null) {  // if  it is  not  null 
	    System.out.println("resetting  the profile");
		myProfile.setAddress(profile.getAddress());	
		myProfile.setMobilenumber(profile.getMobilenumber());
		myProfile.setProfilepicture(filetoupload.getAbsolutePath());
		pService.create(myProfile);  // update the profile  picture 
		return new ResponseEntity<String>("successful",HttpStatus.OK) ; 
		}
		profile.setProfilepicture(filetoupload.getAbsolutePath());
		pService.create(profile);
        return new ResponseEntity<String>("successful",HttpStatus.OK) ;   
      }
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") long id){
		 p.delete(id);
		return new ResponseEntity<String>("Profile removed",HttpStatus.OK) ;
	}
	@GetMapping("/profile/{userid}")
	public Profile profile(@PathVariable("userid") long userid){
		Profile profile = p.whereProfile(String.valueOf(userid))   ;
		if(profile != null) {
			//profile.setProfilepicture("");
			String pic  = new File(profile.getProfilepicture()).getName() ;
			profile.setProfilepicture(pic);
			return profile;	   
		   }else 
	   throw new ProfileException("The user does not exists ") ;   
		   }
  
	@GetMapping("/profile2/{userid}")
 public Profile profile2(@PathVariable("userid") long userid){
	Profile profile = p.whereProfile(String.valueOf(userid))   ;
	if(profile != null) {
		//profile.setProfilepicture("");
		String pic  = new File(profile.getProfilepicture()).getName() ;
		profile.setProfilepicture(pic);
		profile.setDob(null);
		profile.setAddress(null);
		profile.setLocation(null);
		profile.setUserid(null);
		profile.setMobilenumber(null);
		return profile;	   
	   }else 
   throw new ProfileException("The user does not exists ") ;   
	   }
  
       }




























































































































