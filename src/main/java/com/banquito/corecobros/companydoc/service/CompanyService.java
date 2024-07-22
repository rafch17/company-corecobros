package com.banquito.corecobros.companydoc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.banquito.corecobros.companydoc.dto.CompanyDTO;
import com.banquito.corecobros.companydoc.model.Account;
import com.banquito.corecobros.companydoc.model.Company;
import com.banquito.corecobros.companydoc.model.User;
import com.banquito.corecobros.companydoc.repository.AccountRepository;
import com.banquito.corecobros.companydoc.repository.CompanyRepository;
import com.banquito.corecobros.companydoc.util.mapper.CompanyMapper;
import com.banquito.corecobros.companydoc.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final AccountRepository accountRepository;
    private final CompanyMapper companyMapper;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, AccountRepository accountRepository,
            CompanyMapper companyMapper, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.accountRepository = accountRepository;
        this.companyMapper = companyMapper;
        this.userRepository = userRepository;
    }

    public List<CompanyDTO> obtainAllCompanies() {
        log.info("Va a retornar todas las compañías");
        List<Company> companies = this.companyRepository.findAll();
        return companies.stream().map(c -> this.companyMapper.toDTO(c))
                .collect(Collectors.toList());
    }

    public CompanyDTO getCompanyByUniqueID(String uniqueID) {
        log.info("Va a buscar el usuario con uniqueID: {}", uniqueID);
        Company company = this.companyRepository.findByUniqueID(uniqueID);
        if (company == null) {
            log.info("No se encontró el usuario con uniqueID: {}", uniqueID);
            return null;
        }
        log.info("Se encontró el usuario: {}", company);
        return this.companyMapper.toDTO(company);
    }

    public Company getCompanyByRuc(String ruc) {
        log.info("Va a buscar la compañía con RUC: {}", ruc);
        Company company = this.companyRepository.findByRuc(ruc);
        if (company != null) {
            log.info("Se encontró la compañía: {}", company);
            return company;
        } else {
            throw new RuntimeException("No existe compañía con el RUC:" + ruc);
        }
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
            throw new RuntimeException("No existe compañía con el RUC:" + ruc);
        }
        List<User> users = this.userRepository.findByCompanyId(company.getId());
        if (!users.isEmpty()) {
            throw new RuntimeException("La compañía ya tiene usuarios asociados.");
        }
        Account account = this.accountRepository.findByCodeInternalAccountAndCompanyId(accountNumber, company.getId());
        if (account == null) {
            throw new RuntimeException("No existe cuenta con el número de cuenta proporcionado.");
        }
        return this.companyMapper.toDTO(company);
    }

}
