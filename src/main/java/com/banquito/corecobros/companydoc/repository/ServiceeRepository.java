package com.banquito.corecobros.companydoc.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.banquito.corecobros.companydoc.model.Servicee;

public interface ServiceeRepository extends MongoRepository<Servicee, String> {

    Servicee findByUniqueID(String uniqueID);

    List<Servicee> findByName(String name);

}
