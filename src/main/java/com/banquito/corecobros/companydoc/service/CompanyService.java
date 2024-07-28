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
import com.banquito.corecobros.companydoc.util.uniqueId.UniqueIdGeneration;
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

    public CompanyDTO getCompanyByUniqueId(String uniqueId) {
        log.info("Va a buscar el usuario con uniqueId: {}", uniqueId);
        Company company = this.companyRepository.findByUniqueId(uniqueId);
        if (company == null) {
            log.info("No se encontró el usuario con uniqueId: {}", uniqueId);
            return null;
        }
        log.info("Se encontró el usuario: {}", company);
        return this.companyMapper.toDTO(company);
    }

    public Company getCompanyByRuc(String ruc) {
        log.info("Va a buscar la compañía con RUC: {}", ruc);
        try {
            Company company = this.companyRepository.findByRuc(ruc);
            if (company != null) {
                log.info("Se encontró la compañía: {}", company);
                return company;
            } else {
                throw new RuntimeException("No existe compañía con el RUC:" + ruc);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la compañía con RUC: " + ruc, e);
        }
    }

    public List<CompanyDTO> getCompanyByCompanyName(String companyName) {
        log.info("Va a buscar las compañías con nombre: {}", companyName);
        try {
            List<Company> companies = this.companyRepository.findByCompanyName(companyName);
            return companies.stream().map(c -> this.companyMapper.toDTO(c))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al buscar las compañías con nombre: {}", companyName, e);
            throw new RuntimeException("Error al buscar las compañías con nombre: " + companyName, e);
        }
    }

    public CompanyDTO getCommissionIdByUniqueId(String uniqueId) {
        log.info("Va a buscar la comisión de la compañía con ID: {}", uniqueId);
        try {
            Company company = this.companyRepository.findByUniqueId(uniqueId);
            if (company != null) {
                log.info("Se encontró la compañía: {}", company);
                if (company.getCommissionId() != null) {
                    Company commission = this.companyRepository.findByCommissionId(company.getCommissionId());
                    if (commission != null) {
                        log.info("Se encontró la comisión: {}", commission);
                        return this.companyMapper.toDTO(commission);
                    } else {
                        throw new RuntimeException(
                                "No existe la comisión para la empresa con ID: " + company.getCommissionId());
                    }
                } else {
                    throw new RuntimeException("La empresa con ID " + uniqueId + " no tiene una comisión asociada.");
                }
            } else {
                throw new RuntimeException("No existe la empresa con ID: " + uniqueId);
            }
        } catch (Exception e) {
            log.error("Error al buscar la comisión de la compañía con ID: {}", uniqueId, e);
            throw new RuntimeException("Error al buscar la comisión de la compañía con ID: " + uniqueId, e);
        }
    }

    public CompanyDTO create(CompanyDTO dto) {

        UniqueIdGeneration uniqueIdGenerator = new UniqueIdGeneration();
        String uniqueId;
        boolean uniqueIdExists;

        do {
            uniqueId = uniqueIdGenerator.getUniqueId();
            uniqueIdExists = companyRepository.findByUniqueId(uniqueId) != null;
        } while (uniqueIdExists);

        log.info("Va a registrar una compañía: {}", dto);
        Company company = this.companyMapper.toPersistence(dto);
        company.setUniqueId(uniqueId);
        log.info("Compañía a registrar: {}", company);
        company = this.companyRepository.save(company);
        log.info("Se creó la compañía: {}", company);
        return this.companyMapper.toDTO(company);
    }

    public void updateCompany(String uniqueId, CompanyDTO dto) {
        log.info("Va a actualizar la compañía con ID: {}", uniqueId);
        Company company = this.companyMapper.toPersistence(dto);
        company.setId(uniqueId);
        company = this.companyRepository.save(company);
        log.info("Se actualizó la compañía: {}", company);
    }

    public CompanyDTO registerCompany(String ruc, String codeInternalAccount) {
        log.info("Iniciando registro de compañía con RUC: {} y número de cuenta: {}", ruc, codeInternalAccount);
    
        Company company = this.companyRepository.findByRuc(ruc);
        if (company == null) {
            log.error("No existe compañía con el RUC: {}", ruc);
            throw new RuntimeException("No existe compañía con el RUC:" + ruc);
        }
    
        List<User> users = this.userRepository.findByCompanyId(company.getId());
        if (!users.isEmpty()) {
            log.error("La compañía ya tiene usuarios asociados.");
            throw new RuntimeException("La compañía ya tiene usuarios asociados.");
        }
    
        Account account = this.accountRepository.findByCodeInternalAccountAndCompanyId(codeInternalAccount, company.getId());
        if (account == null) {
            log.error("No existe cuenta con el número de cuenta proporcionado: {}", codeInternalAccount);
            throw new RuntimeException("No existe cuenta con el número de cuenta proporcionado.");
        }
    
        log.info("Registro de compañía completado para RUC: {}", ruc);
        return this.companyMapper.toDTO(company);
    }    

}
