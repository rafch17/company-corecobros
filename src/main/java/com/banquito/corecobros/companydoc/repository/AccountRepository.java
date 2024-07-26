package com.banquito.corecobros.companydoc.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.banquito.corecobros.companydoc.model.Account;

public interface AccountRepository extends MongoRepository<Account, String> {

    List<Account> findByCompanyId(String companyId);

    Account findByUniqueID(String uniqueID);

    List<Account> findByUniqueIDIn(List<String> uniqueIDs);

    Account findByCodeInternalAccountAndCompanyId(String codeInternalAccount, String companyId);

    Account findByCodeInternalAccount(String codeInternalAccount);

}
