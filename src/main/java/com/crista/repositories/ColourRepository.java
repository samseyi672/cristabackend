package com.crista.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.crista.enties.Colour;

public interface ColourRepository extends PagingAndSortingRepository<Colour,Long> {

}
