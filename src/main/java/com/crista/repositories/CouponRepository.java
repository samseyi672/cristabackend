package com.crista.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.crista.enties.Coupon;

@Repository
public interface CouponRepository extends PagingAndSortingRepository<Coupon,Long> {

	//public List<Coupon> findByVendor(String vendorname);
	
     @Query("select c from coupon c where c.vendor=:vendorname")
	public Page<Coupon> findByVendor(Pageable pageable, String vendorname);

	public Coupon getById(long l);
}
































