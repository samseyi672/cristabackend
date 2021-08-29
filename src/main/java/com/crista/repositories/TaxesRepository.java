package com.crista.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.crista.enties.Taxes;

@Repository
public interface TaxesRepository extends PagingAndSortingRepository<Taxes,Long> {
	
	@Query("select t.id,t.taxdetail,t.taxschedule,t.totaltaxamount,t.taxrate from Taxes t")
    List<String> findAllCategory() ;
}
