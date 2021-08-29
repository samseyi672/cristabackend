package com.crista.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.crista.enties.Category;
import com.crista.enties.Taxes;
import com.crista.repositories.TaxesRepository;

@Service
public class TaxesService {

  @Autowired
  TaxesRepository taxRepo ;
   
  public Taxes createTax(Taxes tax) {
	   return taxRepo.save(tax) ;
  }

public List<Taxes> getAllCategories() {
	System.out.println(taxRepo.findAllCategory());
	  List<String> listofcat  = taxRepo.findAllCategory()	;
	 List<Taxes> cat  =  listofcat.parallelStream().map((r)->{
		 Taxes c  =  new Taxes() ;
        String list[] =  r.split(",")  ;
          c.setId(Long.parseLong(list[0]));
		    c.setTaxdetail(list[1]);
		    c.setTaxschedule(list[2]);
		    c.setTotaltaxamount(list[3]);
		    c.setTaxrate(list[4]);
		  return c;
	  }).collect(Collectors.toList()) ;
	  return cat  ;
}

public void deleteTax(Taxes tax) {
	 long catid  = tax.getId() ;
	    if(catid == 0) {
	    throw new TaxesException("the tax does not exists "+catid) ;	
	      } 
	    taxRepo.delete(tax);
  }
}












































































































































































