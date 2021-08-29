package com.crista.repositories;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crista.enties.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
	UserRole getById(long id) ;	
	@Query("select u from UserRole u where u.userid=:userid")
	List<UserRole> findByUserid(@Param("userid") String userid) ;
	//void saveAll(UserRole[] role);
}
