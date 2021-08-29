package com.crista.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.crista.enties.Category;

@Repository
public interface CategoriesRepository extends PagingAndSortingRepository<Category,Long> {
    Category getById(long id);
    @Query("select c.id,c.categoryname,c.pricerange from Category c where not (c.categoryname=null)")
    List<String> findAllCategory() ;
    
    @Query("select count(c) from Category c ")
    int countCategory();
    
}
