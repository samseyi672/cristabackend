package com.crista.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.crista.enties.Review;

public interface ReviewRepository  extends PagingAndSortingRepository<Review,Long>{
public Review getByEmail(String email) ;
public List<Review> findAll();
}
