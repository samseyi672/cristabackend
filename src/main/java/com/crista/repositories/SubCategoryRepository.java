package com.crista.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.crista.enties.Category;
import com.crista.enties.SubCategories;

@Repository
public interface SubCategoryRepository extends PagingAndSortingRepository<SubCategories,Long> {
	    
	    SubCategories getById(long id);	    
	    @Query("select c.id,c.subcategoryname,c.pricerange from SubCategories c")
	    List<String> findAllSubCategory() ;
	    
	    @Query("select s from SubCategories s ")
	    List<SubCategories> getAllSubCategories() ;
}






