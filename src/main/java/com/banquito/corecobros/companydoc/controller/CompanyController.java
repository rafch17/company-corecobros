package com.banquito.corecobros.companydoc.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banquito.corecobros.companydoc.dto.CodeInternalAccountDTO;
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
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/name/{companyName}")
    @Operation(summary = "Get company by companyName", description = "Retrieve companies by companyName")
    public ResponseEntity<List<CompanyDTO>> getCompanyByCompanyName(@PathVariable String companyName) {
        try {
            List<CompanyDTO> companies = service.getCompanyByCompanyName(companyName);
            return ResponseEntity.ok(companies);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/commission/{commissionId}")
    @Operation(summary = "Get company by commissionId", description = "Retrieve a company by its commissionId")
    public ResponseEntity<CompanyDTO> getCommissionById(@PathVariable String commissionId) {
        try {
            CompanyDTO company = service.getCommissionById(commissionId);
            return ResponseEntity.ok(company);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping
    @Operation(summary = "Create a company", description = "Create a new company")
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        try {
            Company createdCompany = this.service.create(company);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
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

    @GetMapping("/account/uniqueId/{uniqueId}")
    @Operation(summary = "Get codeInternalAccount by uniqueId", description = "Retrieve codeInternalAccount by its uniqueId")
    public ResponseEntity<CodeInternalAccountDTO> getCodeInternalAccountByUniqueId(@PathVariable String uniqueId) {
        if (uniqueId == null || uniqueId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            String codeInternalAccount = this.service.getCodeInternalAccountByUniqueId(uniqueId);
            CodeInternalAccountDTO response = CodeInternalAccountDTO.builder()
                    .codeInternalAccount(codeInternalAccount)
                    .build();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{companyId}/accounts")
    @Operation(summary = "Get accounts by companyId", description = "Retrieve accounts by companyId")
    public ResponseEntity<List<Account>> getAccountsByCompanyId(@PathVariable String companyId) {
        try {
            List<Account> accounts = this.service.getAccountsByCompanyId(companyId);
            return ResponseEntity.ok(accounts);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/account/code/{codeInternalAccount}")
    @Operation(summary = "Get company by codeInternalAccount", description = "Retrieve a company by its codeInternalAccount")
    public ResponseEntity<CompanyDTO> getCompanyByCodeInternalAccount(@PathVariable String codeInternalAccount) {
        if (codeInternalAccount == null || codeInternalAccount.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            CompanyDTO company = this.service.getCompanyByCodeInternalAccount(codeInternalAccount);
            return ResponseEntity.ok(company);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{companyId}/account")
    @Operation(summary = "Add an account to a company", description = "Add a new account to a company")
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
    @Operation(summary = "Get services by companyId", description = "Retrieve services by companyId")
    public ResponseEntity<List<Servicee>> getServicesByCompanyId(@PathVariable String companyId) {
        try {
            List<Servicee> services = this.service.getServicesByCompanyId(companyId);
            return ResponseEntity.ok(services);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/service/{companyId}")
    @Operation(summary = "Add a service to a company", description = "Add a new service to a company")
    public ResponseEntity<String> addServiceToCompany(@PathVariable String companyId, @RequestBody Servicee servicee) {
        String result = this.service.addServiceToCompany(companyId, servicee);
        if ("Servicio añadido con éxito".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("/service/name/{name}")
    @Operation(summary = "Get company by service name", description = "Retrieve a company by its service name")
    public ResponseEntity<CompanyDTO> getCompanyByServiceesName(@PathVariable String name) {
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            CompanyDTO company = this.service.getCompanyByServiceesName(name);
            return ResponseEntity.ok(company);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
