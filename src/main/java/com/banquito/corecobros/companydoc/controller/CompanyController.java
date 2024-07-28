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

import com.banquito.corecobros.companydoc.dto.CompanyDTO;
import com.banquito.corecobros.companydoc.model.Company;
import com.banquito.corecobros.companydoc.service.CompanyService;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService service;

    public CompanyController(CompanyService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        return ResponseEntity.ok(this.service.obtainAllCompanies());
    }

    @GetMapping("/{uniqueId}")
    public ResponseEntity<CompanyDTO> getCompanyByUniqueId(@PathVariable String uniqueId) {
        CompanyDTO company = service.getCompanyByUniqueId(uniqueId);
        if (company != null) {
            return ResponseEntity.ok(company);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<CompanyDTO> getCompanyByRuc(@PathVariable String ruc) {
        Company company = service.getCompanyByRuc(ruc);
        if (company != null) {
            return ResponseEntity.ok(service.getCompanyByUniqueId(company.getCompanyName()));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/name/{companyName}")
    public ResponseEntity<List<CompanyDTO>> getCompanyByCompanyName(@PathVariable String companyName) {
        List<CompanyDTO> companies = service.getCompanyByCompanyName(companyName);
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/commision/{uniqueId}")
    public ResponseEntity<CompanyDTO> getCommissionIdByUniqueId(@PathVariable String uniqueId) {
        CompanyDTO company = service.getCommissionIdByUniqueId(uniqueId);
        if (company != null) {
            return ResponseEntity.ok(service.getCompanyByUniqueId(company.getUniqueId()));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyDTO dto) {
        try {
            this.service.create(dto);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping("/{uniqueId}")
    public ResponseEntity<Void> updateCompany(@PathVariable String uniqueId, @RequestBody CompanyDTO dto) {
        this.service.updateCompany(uniqueId, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<CompanyDTO> registerCompany(@RequestParam String ruc, @RequestParam String codeInternalAccount) {
        try {
            CompanyDTO company = this.service.registerCompany(ruc, codeInternalAccount);
            return ResponseEntity.ok(company);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); 
        }
    }

}
