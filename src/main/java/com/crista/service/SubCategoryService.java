package com.crista.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crista.enties.SubCategories;
import com.crista.repositories.SubCategoryRepository;

@Service
public class SubCategoryService {

	@Autowired
	SubCategoryRepository repo ;
	
	public void subCreateCategories(SubCategories subcategory) {
		repo.save(subcategory) ;
	}

	public List<String> findAllSub(){
	   return repo.findAllSubCategory() ;    	
	}

	public List<SubCategories> getAllSubCategories() {
		 return repo.getAllSubCategories();
	}
	
	public void delete(SubCategories subcategory) {
		  repo.delete(subcategory);
	}
}






































































































