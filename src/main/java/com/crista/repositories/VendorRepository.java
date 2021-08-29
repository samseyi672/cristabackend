package com.crista.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.crista.enties.Vendor;

@Repository
public interface VendorRepository extends PagingAndSortingRepository<Vendor,Long> {
	Vendor getByVendorname(String  vendorname);
}




















































































































































































