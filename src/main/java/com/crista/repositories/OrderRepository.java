package com.crista.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.crista.enties.Order;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order,Long>{
 Order getById(long id);
 Order getByEmail(String id);
 List<Order> findAll() ;
 List<Order> getByOrdid(String orderid) ;
}
