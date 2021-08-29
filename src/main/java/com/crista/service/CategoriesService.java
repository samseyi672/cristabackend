package com.crista.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.crista.enties.Category;
import com.crista.repositories.CategoriesRepository;

@Service
public class CategoriesService {
	
   @Autowired
  CategoriesRepository cateRepo ;
  //for update and save
  public Category createCategories(Category cat) {
	  return cateRepo.save(cat) ;
  }
  
  public List<Category> getAllCategories() {
	  System.out.println(cateRepo.findAllCategory());
	  List<String> listofcat  = cateRepo.findAllCategory()	;
	 List<Category> cat  =  listofcat.parallelStream().map((r)->{
		 Category c  =  new Category() ;
          String list[] =  r.split(",")  ;
            c.setId(Long.parseLong(list[0]));
		    c.setCategoryname(list[1]);
		    c.setPricerange(list[2]);
		  return c;
	  }).collect(Collectors.toList()) ;
	  return cat  ;
  }
  
  public void deleteCategoryById(long id) {
	    Category cat  = cateRepo.getById(id) ;
	    if(cat ==null) {
	    throw new UsernameNotFoundException("the categories does not exists "+id) ;	
	      }
	  cateRepo.deleteById(id);
  }
  public void deleteCategories(Category cat2) {
	  long catid  = cat2.getId() ;
	    if(catid == 0) {
	    throw new UsernameNotFoundException("the categories does not exists "+catid) ;	
	      } 
	    cateRepo.delete(cat2);
  }
  public int countCategory() {
	return cateRepo.countCategory() ;
  }
}












































































































































