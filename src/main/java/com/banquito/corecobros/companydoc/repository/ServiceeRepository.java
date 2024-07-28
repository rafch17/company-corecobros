package com.banquito.corecobros.companydoc.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.banquito.corecobros.companydoc.model.Servicee;

public interface ServiceeRepository extends MongoRepository<Servicee, String> {

    Servicee findByUniqueId(String uniqueId);

    List<Servicee> findByName(String name);

}
