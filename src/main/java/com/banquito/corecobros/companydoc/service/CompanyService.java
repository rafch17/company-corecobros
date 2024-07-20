package com.banquito.corecobros.companydoc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.banquito.corecobros.companydoc.dto.CompanyDTO;
import com.banquito.corecobros.companydoc.model.Account;
import com.banquito.corecobros.companydoc.model.Company;
import com.banquito.corecobros.companydoc.repository.AccountRepository;
import com.banquito.corecobros.companydoc.repository.CompanyRepository;
import com.banquito.corecobros.companydoc.util.mapper.CompanyMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final AccountRepository accountRepository;
    private final CompanyMapper companyMapper;

    public List<CompanyDTO> obtainAllCompanies() {
        log.info("Va a retornar todas las compañías");
        List<Company> companies = this.companyRepository.findAll();
        return companies.stream().map(c -> this.companyMapper.toDTO(c))
                .collect(Collectors.toList());
    }

    public CompanyDTO getCompanyById(String id) {
        log.info("Va a buscar la compañía con ID: {}", id);
        Company company = this.companyRepository.findById(id).orElse(null);
        if (company == null) {
            log.info("No se encontró la compañía con ID: {}", id);
            return null;
        }
        log.info("Se encontró la compañía: {}", company);
        return this.companyMapper.toDTO(company);
    }

    public void create(CompanyDTO dto) {
        log.info("Va a registrar una compañía: {}", dto);
        Company company = this.companyMapper.toPersistence(dto);
        log.info("Compañía a registrar: {}", company);
        company = this.companyRepository.save(company);
        log.info("Se creó la compañía: {}", company);
    }

    public void updateCompany(String id, CompanyDTO dto) {
        log.info("Va a actualizar la compañía con ID: {}", id);
        Company company = this.companyMapper.toPersistence(dto);
        company.setId(id);
        company = this.companyRepository.save(company);
        log.info("Se actualizó la compañía: {}", company);
    }

    public CompanyDTO registerCompany(String ruc, String accountNumber) {
        Company company = this.companyRepository.findByRuc(ruc);
        if (company == null) {
            Company newCompany = new Company();
            newCompany.setRuc(ruc);
            company = this.companyRepository.save(newCompany);
            Account newAccount = new Account();
            newAccount.setNumber(accountNumber);
            newAccount.setCompanyId(newCompany.getId());
            accountRepository.save(newAccount);
        }
        return this.companyMapper.toDTO(company);
    }

}
