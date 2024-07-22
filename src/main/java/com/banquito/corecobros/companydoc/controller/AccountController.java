package com.banquito.corecobros.companydoc.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/unique/{uniqueID}")
    public ResponseEntity<AccountDTO> getAccountByUniqueID(@PathVariable String uniqueID) {
        AccountDTO account = service.getAccountByUniqueID(uniqueID);
        if (account != null) {
            return ResponseEntity.ok(account);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/uniqueIDs")
    public ResponseEntity<List<AccountDTO>> getAccountsByUniqueIDs(@RequestParam List<String> uniqueIDs) {
        List<AccountDTO> accounts = service.getAccountsByUniqueIDs(uniqueIDs);
        return ResponseEntity.ok(accounts);
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

}
