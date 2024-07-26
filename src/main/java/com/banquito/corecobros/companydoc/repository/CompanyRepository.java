package com.banquito.corecobros.companydoc.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.banquito.corecobros.companydoc.model.Company;

public interface CompanyRepository extends MongoRepository<Company, String> {

    Company findByCompanyId(String companyId);

    Company findByRuc(String ruc);

    List<Company> findByCompanyName(String companyName);

    Company findByUniqueID(String uniqueID);

    Company findByCommissionId(String commisionId);

}
