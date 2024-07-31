package com.banquito.corecobros.companydoc.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import com.banquito.corecobros.companydoc.dto.AccountDTO;
import com.banquito.corecobros.companydoc.service.AccountService;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST,
    RequestMethod.PUT })
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        return ResponseEntity.ok(this.service.obtainAllAccounts());
    }

    @GetMapping("/{uniqueId}")
    public ResponseEntity<AccountDTO> getAccountByUniqueId(@PathVariable String uniqueId) {
        AccountDTO account = service.getAccountByUniqueId(uniqueId);
        if (account != null) {
            return ResponseEntity.ok(account);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/uniqueIds")
    public ResponseEntity<List<AccountDTO>> getAccountsByUniqueIds(@RequestParam List<String> uniqueIds) {
        List<AccountDTO> accounts = service.getAccountsByUniqueIds(uniqueIds);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<AccountDTO>> getAccountsByCompanyId(@PathVariable String companyId) {
        List<AccountDTO> accounts = this.service.getAccountsByCompanyId(companyId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/number/{codeInternalAccount}")
    public ResponseEntity<AccountDTO> getAccountByCodeInternalAccount(@PathVariable String codeInternalAccount) {
        AccountDTO account = this.service.getAccountByCodeInternalAccount(codeInternalAccount);
        if (account != null) {
            return ResponseEntity.ok(account);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO dto) {
        try {
            this.service.create(dto);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{uniqueId}")
    public ResponseEntity<Void> updateAccount(@PathVariable String uniqueId, @RequestBody AccountDTO dto) {
        this.service.updateAccount(uniqueId, dto);
        return ResponseEntity.ok().build();
    }

}
