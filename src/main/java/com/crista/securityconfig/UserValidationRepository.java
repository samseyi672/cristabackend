package com.crista.securityconfig;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crista.enties.Users;

@Repository
public interface UserValidationRepository extends PagingAndSortingRepository<Users,Long> {
	Users getById(long id) ;
	@Query("select u.email,u.lastname.u.firstname from Users u where u.id=:userid")
	Users findUsersById(@Param("userid") long userid) ;
	Users getByEmail(String email);
	@Query("select u from Users where u.vendorname=:vendorname and u.firstname like %:action_users%")
	List<Users> getByVendornameAndFirstnameContainingIgnoreCase(@Param("vendorname") String vendorname,@Param("action_users") String action_users);
     Users getByVendornameAndId(String vendorname,long id) ;
     
     @Query("select u from Users where u.vendorname like %:vendorname%")
 	List<Users> getByVendornameContainingIgnoreCase(@Param("vendorname") String vendorname);
}




























