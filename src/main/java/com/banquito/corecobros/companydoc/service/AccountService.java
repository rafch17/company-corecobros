package com.banquito.corecobros.companydoc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.banquito.corecobros.companydoc.dto.AccountDTO;
import com.banquito.corecobros.companydoc.model.Account;
import com.banquito.corecobros.companydoc.repository.AccountRepository;
import com.banquito.corecobros.companydoc.util.mapper.AccountMapper;
import com.banquito.corecobros.companydoc.util.uniqueId.UniqueIdGeneration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper mapper;

    public AccountService(AccountRepository accountRepository, AccountMapper mapper) {
        this.accountRepository = accountRepository;
        this.mapper = mapper;
    }

    public List<AccountDTO> obtainAllAccounts() {
        log.info("Va a retornar todas las cuentas");
        List<Account> accounts = this.accountRepository.findAll();
        return accounts.stream().map(a -> this.mapper.toDTO(a))
                .collect(Collectors.toList());
    }

    public AccountDTO getAccountByUniqueId(String uniqueId) {
        log.info("Va a buscar la cuenta con uniqueId: {}", uniqueId);
        Account account = this.accountRepository.findByUniqueId(uniqueId);
        if (account == null) {
            log.info("No se encontró la cuenta con uniqueId: {}", uniqueId);
            return null;
        }
        log.info("Se encontró la cuenta: {}", account);
        return this.mapper.toDTO(account);
    }

    public List<AccountDTO> getAccountsByCompanyId(String companyId) {
        log.info("Va a retornar todas las cuentas para la compañía con ID: {}", companyId);
        List<Account> accounts = this.accountRepository.findByCompanyId(companyId);
        return accounts.stream().map(a -> this.mapper.toDTO(a))
                .collect(Collectors.toList());
    }

    public List<AccountDTO> getAccountsByUniqueIds(List<String> uniqueIds) {
        log.info("Va a buscar cuentas con los uniqueIds: {}", uniqueIds);
        List<Account> accounts = this.accountRepository.findByUniqueIdIn(uniqueIds);
        return accounts.stream().map(a -> this.mapper.toDTO(a))
                .collect(Collectors.toList());
    }

    public AccountDTO getAccountByCodeInternalAccount(String codeInternalAccount) {
        log.info("Va a buscar la cuenta con codeInternalAccount: {}", codeInternalAccount);
        Account account = this.accountRepository.findByCodeInternalAccount(codeInternalAccount);
        if (account == null) {
            log.info("No se encontró la cuenta con codeInternalAccount: {}", codeInternalAccount);
            return null;
        }
        log.info("Se encontró la cuenta: {}", account);
        return this.mapper.toDTO(account);
    }

    public AccountDTO create(AccountDTO dto) {

        UniqueIdGeneration uniqueIdGenerator = new UniqueIdGeneration();
        String uniqueId;
        boolean uniqueIdExists;

        do {
            uniqueId = uniqueIdGenerator.getUniqueId();
            uniqueIdExists = accountRepository.findByUniqueId(uniqueId) != null;
        } while (uniqueIdExists);

        log.info("Va a registrar una cuenta: {}", dto);
        Account account = this.mapper.toPersistence(dto);
        account.setUniqueId(uniqueId);
        log.info("Cuenta a registrar: {}", account);
        account = this.accountRepository.save(account);
        log.info("Se creó la cuenta: {}", account);
        return this.mapper.toDTO(account);
    }

    public void updateAccount(String uniqueId, AccountDTO dto) {
        log.info("Va a actualizar la cuenta con ID: {}", uniqueId);
        Account account = this.mapper.toPersistence(dto);
        account.setId(uniqueId);
        account = this.accountRepository.save(account);
        log.info("Se actualizó la cuenta: {}", account);
    }
}
