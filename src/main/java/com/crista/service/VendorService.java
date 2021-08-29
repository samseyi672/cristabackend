package com.crista.service;

import java.io.File;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crista.enties.Vendor;
import com.crista.exceptions.VendorException;
import com.crista.repositories.VendorRepository;

@Service
public class VendorService {
	
	@Autowired
	VendorRepository vendorRepo;

	public Vendor updateVendor(Vendor vendorin) {
		System.out.println(" vendor in "+vendorin);
		Vendor vendor = vendorRepo.getByVendorname(vendorin.getVendorname()) ;
		if(vendor == null) {
			// vendor.setVendorlogo(vendorin.getVendorlogo());
			return vendorRepo.save(vendorin);	
		}
		 vendor.setVendorlogo(vendorin.getVendorlogo());
		 return vendorRepo.save(vendor); 
	}

	public Vendor createVendor(Vendor vendor) {	
		return vendorRepo.save(vendor);
	}
   public Vendor findVendorName(String vendorName) {
	   System.out.println(" vendor in "+vendorName);
		Vendor vendor = vendorRepo.getByVendorname(vendorName) ;   
		if(vendor == null){
		  throw new VendorException("the vendor logo does not exists") ;	
		   }
		String name = new File(vendor.getVendorlogo()).getName();
		System.out.println(" name "+name);
		vendor.setVendorlogo(name);
		vendor.setVendorname("");
		vendor.setDateofcreation(null);
		vendor.setId(0);
		return vendor ;
   }
	public void deleteVendor(long vendorId) {
      Optional<Vendor>  vendor  =  vendorRepo.findById(vendorId) ;
         if(!vendor.isPresent()) {
        	 return ; 
         }
		vendorRepo.deleteById(vendorId);
		return;
	}
}























































































