package com.banquito.corecobros.companydoc.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.banquito.corecobros.companydoc.model.Account;

public interface AccountRepository extends MongoRepository<Account, String> {

    List<Account> findByCompanyId(String companyId);

    Account findByUniqueId(String uniqueId);

    List<Account> findByUniqueIdIn(List<String> uniqueIds);

    Account findByCodeInternalAccountAndCompanyId(String codeInternalAccount, String companyId);

    Account findByCodeInternalAccount(String codeInternalAccount);

}
