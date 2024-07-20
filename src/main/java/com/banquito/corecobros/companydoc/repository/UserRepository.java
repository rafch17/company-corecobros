package com.banquito.corecobros.companydoc.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.banquito.corecobros.companydoc.model.User;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUserAndCompanyId(String user, String companyId);

    List<User> findByCompanyId(String companyId);

    User findByUser(String user);

}