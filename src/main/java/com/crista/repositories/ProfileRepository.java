package com.crista.repositories;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crista.enties.Profile;

@Repository
public interface ProfileRepository extends PagingAndSortingRepository<Profile,Long> {
 Profile  getById(long id);
 @Query("select p from profile p where p.userid=:userid")
 Profile getByUserid(@Param("userid") String userid) ;
}
