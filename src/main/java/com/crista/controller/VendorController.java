package com.crista.controller;

import java.io.File;
import java.io.FileNotFoundException;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.crista.enties.Category;
import com.crista.enties.Vendor;
import com.crista.repositories.VendorRepository;
import com.crista.service.VendorService;

@RestController
@RequestMapping("/vendor")
public class VendorController {
 
	@Autowired
	VendorService vService ;
	@Value("${upload.dir}")
	String fileDirectory;
	
	@PostMapping(value = "/vendor", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = "application/json")
	public String createVendor(@Valid @ModelAttribute Vendor vendor, BindingResult result,
			@RequestParam(value = "vendorlogo") MultipartFile file) throws IOException {
		System.out.println("got here to category creation 2 ");
		File filetoupload = new File(fileDirectory + "/" + file.getOriginalFilename());
		filetoupload.createNewFile();
		FileOutputStream out = new FileOutputStream(filetoupload);
		out.write(file.getBytes());
		out.close();
		System.out.println("file path "+filetoupload.getAbsolutePath()+ " vendor "+vendor.getVendorname());
	   vendor.setVendorlogo(filetoupload.getAbsolutePath());
	     vService.updateVendor(vendor) ;
	  return "successful";
	}
	@PutMapping("/vendor")
	public Vendor createUpdate(@RequestBody Vendor vendor,BindingResult result) {
		 return  vService.updateVendor(vendor) ;
	}
	 @GetMapping("/vendor")
	public Vendor findVendor(@RequestParam("vendorname") String  vendorName) {
		 System.out.println(" vendorname "+vendorName);
		 return  vService.findVendorName(vendorName) ;
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> createUpdate(@PathVariable("id") long id) {
		 vService.deleteVendor(id); ;
		 return new ResponseEntity<String>("Vendor Removed",HttpStatus.ACCEPTED)  ;
	}
}






























