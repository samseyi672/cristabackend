package com.crista.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.crista.enties.ServerMessages;

public interface ServerMessageRepository extends PagingAndSortingRepository<ServerMessages,
    Long>{

}
