package com.banquito.corecobros.companydoc.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banquito.corecobros.companydoc.dto.CompanyDTO;
import com.banquito.corecobros.companydoc.model.Account;
import com.banquito.corecobros.companydoc.model.Company;
import com.banquito.corecobros.companydoc.model.Servicee;
import com.banquito.corecobros.companydoc.service.CompanyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST,
        RequestMethod.PUT })
@RestController
@RequestMapping("/company-microservice/api/v1/companies")
@Tag(name = "Company", description = "Endpoints for managing companies")
public class CompanyController {

    private final CompanyService service;

    public CompanyController(CompanyService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all companies", description = "Retrieve a list of all companies")
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        return ResponseEntity.ok(this.service.obtainAllCompanies());
    }

    @GetMapping("/{uniqueId}")
    @Operation(summary = "Get company by uniqueId", description = "Retrieve a company by its uniqueId")
    public ResponseEntity<CompanyDTO> getCompanyByUniqueId(@PathVariable String uniqueId) {
        CompanyDTO company = service.getCompanyByUniqueId(uniqueId);
        if (company != null) {
            return ResponseEntity.ok(company);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ruc/{ruc}")
    @Operation(summary = "Get company by ruc", description = "Retrieve a company by its ruc")
    public ResponseEntity<CompanyDTO> getCompanyByRuc(@PathVariable String ruc) {
        try {
            CompanyDTO company = service.getCompanyByRuc(ruc);
            return ResponseEntity.ok(company);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/name/{companyName}")
    @Operation(summary = "Get company by companyName", description = "Retrieve a company by its companyName")
    public ResponseEntity<List<CompanyDTO>> getCompanyByCompanyName(@PathVariable String companyName) {
        try {
            List<CompanyDTO> companies = service.getCompanyByCompanyName(companyName);
            return ResponseEntity.ok(companies);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/commision/{commissionId}")
    @Operation(summary = "Get company by commissionId", description = "Retrieve a company by its commissionId")
    public ResponseEntity<CompanyDTO> getCommissionIdByUniqueId(@PathVariable String commissionId) {
        try {
            CompanyDTO company = service.getCommissionById(commissionId);
            return ResponseEntity.ok(company);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/")
    @Operation(summary = "Create a company", description = "Create a new company")
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        try {
            Company createdCompany = this.service.create(company);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{uniqueId}")
    @Operation(summary = "Update a company", description = "Update an existing company")
    public ResponseEntity<Void> updateCompany(@PathVariable String uniqueId, @RequestBody Company company) {
        try {
            this.service.updateCompany(uniqueId, company);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{companyId}/accounts")
    @Operation(summary = "Get account by companyId", description = "Retrieve a account by its companyId")
    public ResponseEntity<List<Account>> getAccountsByCompanyId(@PathVariable String companyId) {
        try {
            List<Account> accounts = this.service.getAccountsByCompanyId(companyId);
            return ResponseEntity.ok(accounts);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/accounts/code/{codeInternalAccount}")
    @Operation(summary = "Get company by codeInternalAccount", description = "Retrieve a company by its codeInternalAccount")
    public ResponseEntity<CompanyDTO> getCompanyByCodeInternalAccount(@PathVariable String codeInternalAccount) {
        if (codeInternalAccount == null || codeInternalAccount.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            CompanyDTO company = this.service.getCompanyByCodeInternalAccount(codeInternalAccount);
            return ResponseEntity.ok(company);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{companyId}/accounts")
    @Operation(summary = "Add an account by companyId", description = "Add a new account to company")
    public ResponseEntity<String> addAccountToCompany(@PathVariable String companyId, @RequestBody Account account) {
        String result = this.service.addAccountToCompany(companyId, account);
        if ("Cuenta añadida con éxito".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("/account/{accountId}")
    @Operation(summary = "Get company by accountId", description = "Retrieve a company by its accountId")
    public ResponseEntity<String> getCompanyNameByAccountId(@PathVariable String accountId) {
        try {
            String companyName = service.getCompanyNameByAccountId(accountId);
            return ResponseEntity.ok(companyName);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{companyId}/services")
    @Operation(summary = "Get service by companyId", description = "Retrieve a service by its companyId")
    public ResponseEntity<List<Servicee>> getServicesByCompanyId(@PathVariable String companyId) {
        List<Servicee> services = this.service.getServicesByCompanyId(companyId);
        return ResponseEntity.ok(services);
    }

    @PostMapping("/{companyId}/services")
    @Operation(summary = "Add an service by companyId", description = "Add a new service to company")
    public ResponseEntity<String> addServiceToCompany(@PathVariable String companyId, @RequestBody Servicee servicee) {
        String result = this.service.addServiceToCompany(companyId, servicee);
        if ("Servicio añadido con éxito".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("/services/name/{name}")
    @Operation(summary = "Get company by service name", description = "Retrieve a company by its service name")
    public ResponseEntity<CompanyDTO> getCompanyByServiceesName(@PathVariable String name) {
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            CompanyDTO companies = this.service.getCompanyByServiceesName(name);
            return ResponseEntity.ok(companies);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
