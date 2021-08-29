package com.crista.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.crista.enties.Customer;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer,Long> {
  public Customer getByEmail(String email) ;
  public Customer getById(long id) ;
  public List<Customer> findAll() ;
}
