package com.banquito.corecobros.companydoc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.banquito.corecobros.companydoc.dto.AccountDTO;
import com.banquito.corecobros.companydoc.model.Account;
import com.banquito.corecobros.companydoc.repository.AccountRepository;
import com.banquito.corecobros.companydoc.util.mapper.AccountMapper;

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

    public void create(AccountDTO dto) {
        log.info("Va a registrar una cuenta: {}", dto);
        Account account = this.mapper.toPersistence(dto);
        log.info("Cuenta a registrar: {}", account);
        account = this.accountRepository.save(account);
        log.info("Se creó la cuenta: {}", account);
    }

    public AccountDTO getAccountById(String id) {
        log.info("Va a buscar la cuenta con ID: {}", id);
        Account account = this.accountRepository.findById(id).orElse(null);
        if (account == null) {
            log.info("No se encontró la cuenta con ID: {}", id);
            return null;
        }
        log.info("Se encontró la cuenta: {}", account);
        return this.mapper.toDTO(account);
    }

    public void updateAccount(String id, AccountDTO dto) {
        log.info("Va a actualizar la cuenta con ID: {}", id);
        Account account = this.mapper.toPersistence(dto);
        account.setId(id);
        account = this.accountRepository.save(account);
        log.info("Se actualizó la cuenta: {}", account);
    }

    public List<AccountDTO> getAccountsByCompanyId(String companyId) {
        log.info("Va a retornar todas las cuentas para la compañía con ID: {}", companyId);
        List<Account> accounts = this.accountRepository.findByCompanyId(companyId);
        return accounts.stream().map(a -> this.mapper.toDTO(a)).collect(Collectors.toList());
    }

    public AccountDTO getAccountByNumber(String number) {
        log.info("Va a buscar la cuenta con número: {}", number);
        Account account = this.accountRepository.findByNumber(number);
        if (account == null) {
            log.info("No se encontró la cuenta con número: {}", number);
            return null;
        }
        log.info("Se encontró la cuenta: {}", account);
        return this.mapper.toDTO(account);
    }
}
