package com.banquito.corecobros.companydoc.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banquito.corecobros.companydoc.dto.AccountDTO;
import com.banquito.corecobros.companydoc.service.AccountService;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        return ResponseEntity.ok(this.service.obtainAllAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable String id) {
        AccountDTO account = this.service.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @PostMapping
    public ResponseEntity<Void> createAccount(@RequestBody AccountDTO dto) {
        this.service.create(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAccount(@PathVariable String id, @RequestBody AccountDTO dto) {
        this.service.updateAccount(id, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<AccountDTO>> getAccountsByCompanyId(@PathVariable String companyId) {
        List<AccountDTO> accounts = this.service.getAccountsByCompanyId(companyId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/number/{number}")
    public ResponseEntity<AccountDTO> getAccountByNumber(@PathVariable String number) {
        AccountDTO account = this.service.getAccountByNumber(number);
        return ResponseEntity.ok(account);
    }

}
