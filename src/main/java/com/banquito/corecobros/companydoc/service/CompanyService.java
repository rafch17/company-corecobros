package com.banquito.corecobros.companydoc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.banquito.corecobros.companydoc.dto.CompanyDTO;
import com.banquito.corecobros.companydoc.model.Account;
import com.banquito.corecobros.companydoc.model.Company;
import com.banquito.corecobros.companydoc.model.Servicee;
import com.banquito.corecobros.companydoc.repository.CompanyRepository;
import com.banquito.corecobros.companydoc.util.mapper.CompanyMapper;
import com.banquito.corecobros.companydoc.util.uniqueId.UniqueIdGeneration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public CompanyService(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
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

    public CompanyDTO getCompanyByRuc(String ruc) {
        log.info("Va a buscar la compañía con RUC: {}", ruc);
        try {
            Company company = this.companyRepository.findByRuc(ruc);
            if (company != null) {
                log.info("Se encontró la compañía: {}", company);
                return this.companyMapper.toDTO(company);
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
            List<Company> companies = this.companyRepository.findByCompanyNameContainingIgnoreCase(companyName);
            return companies.stream().map(c -> this.companyMapper.toDTO(c))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al buscar las compañías con nombre: {}", companyName, e);
            throw new RuntimeException("Error al buscar las compañías con nombre: " + companyName, e);
        }
    }

    public CompanyDTO getCommissionById(String commissionId) {
        log.info("Va a buscar la compañía por commissionId: {}", commissionId);
        try {
            Company company = this.companyRepository.findByCommissionId(commissionId);
            if (company != null) {
                log.info("Se encontró la compañía: {}", company);
                return this.companyMapper.toDTO(company);
            } else {
                throw new RuntimeException("No existe compañía con el commissionId: " + commissionId);
            }
        } catch (Exception e) {
            log.error("Error al buscar la compañía por commissionId: {}", commissionId, e);
            throw new RuntimeException("Error al buscar la compañía por commissionId: " + commissionId, e);
        }
    }

    public Company create(Company company) {
        UniqueIdGeneration uniqueIdGenerator = new UniqueIdGeneration();
        String uniqueId;
        boolean uniqueIdExists;

        if (this.companyRepository.findByRuc(company.getRuc()) != null) {
            throw new RuntimeException("Ya existe una compañía con el RUC: " + company.getRuc());
        }

        boolean accountExists = this.companyRepository.findAll().stream()
                .flatMap(c -> c.getAccounts().stream())
                .anyMatch(account -> account.getCodeInternalAccount()
                        .equals(company.getAccounts().get(0).getCodeInternalAccount()));
        if (accountExists) {
            throw new RuntimeException("Ya existe una cuenta con el número de cuenta: "
                    + company.getAccounts().get(0).getCodeInternalAccount());
        }

        do {
            uniqueId = uniqueIdGenerator.getUniqueId();
            uniqueIdExists = companyRepository.findByUniqueId(uniqueId) != null;
        } while (uniqueIdExists);

        log.info("Creacion de compania: {}", company);
        company.setUniqueId(uniqueId);

        for (Account account : company.getAccounts()) {
            account.setUniqueId(uniqueId);
        }

        for (Servicee servicee : company.getServicees()) {
            servicee.setUniqueId(uniqueId);
        }

        Company savedCompany = this.companyRepository.save(company);

        log.info("Compania creada: {}", savedCompany);
        return savedCompany;
    }

    public void updateCompany(String uniqueId, Company companyDetails) {
        log.info("Va a actualizar la compañía con ID: {}", uniqueId);
        Company existingCompany = this.companyRepository.findByUniqueId(uniqueId);
        if (existingCompany == null) {
            throw new RuntimeException("No se encontró la compañía con ID: " + uniqueId);
        }
        existingCompany.setRuc(companyDetails.getRuc());
        existingCompany.setCompanyName(companyDetails.getCompanyName());
        existingCompany.setStatus(companyDetails.getStatus());
        existingCompany.setAccounts(companyDetails.getAccounts());
        existingCompany.setServicees(companyDetails.getServicees());
        Company updatedCompany = this.companyRepository.save(existingCompany);
        log.info("Se actualizó la compañía: {}", updatedCompany);
    }

    public List<Account> getAccountsByCompanyId(String companyId) {
        log.info("Va a retornar todas las cuentas para la compañía con ID: {}", companyId);
        Company company = this.companyRepository.findByUniqueId(companyId);
        return company.getAccounts();
    }

    public CompanyDTO getCompanyByCodeInternalAccount(String codeInternalAccount) {
        log.info("Buscando compañía por numero de cuenta interno: {}", codeInternalAccount);
        List<Company> companies = this.companyRepository.findByAccountsCodeInternalAccount(codeInternalAccount);
        if (companies.isEmpty()) {
            log.info("No se encontró compañía con el numero de cuenta: {}", codeInternalAccount);
            throw new RuntimeException("No se encontró compañía con el numero de cuenta: " + codeInternalAccount);
        }
        Company foundCompany = companies.get(0);
        return this.companyMapper.toDTO(foundCompany);
    }

    public List<Account> getAccountsByCompany(String companyIdentifier) {
        log.info("Buscando cuentas para la compañía con ID o nombre: {}", companyIdentifier);
        List<Company> companies = this.companyRepository.findByCompanyNameContainingIgnoreCase(companyIdentifier);
        if (companies.isEmpty()) {
            Company company = this.companyRepository.findByUniqueId(companyIdentifier);
            if (company != null) {
                return company.getAccounts();
            }
            throw new RuntimeException("No se encontró la compañía con ID o nombre: " + companyIdentifier);
        }
        return companies.stream().flatMap(c -> c.getAccounts().stream()).collect(Collectors.toList());
    }

    public String addAccountToCompany(String companyId, Account account) {
        Company company = this.companyRepository.findByUniqueId(companyId);
        if (company.getAccounts().stream().anyMatch(a -> a.getUniqueId().equals(account.getUniqueId()))) {
            return "La cuenta ya existe en la compañía";
        }
        company.getAccounts().add(account);
        this.companyRepository.save(company);
        return "Cuenta añadida con éxito";
    }

    public String getCompanyNameByAccountId(String accountId) {
        log.info("Buscando compañía por accountId: {}", accountId);
        List<Company> companies = this.companyRepository.findByAccountsUniqueId(accountId);
        if (companies.isEmpty()) {
            log.info("No se encontró compañía con el accountId: {}", accountId);
            throw new RuntimeException("No se encontró compañía con el accountId: " + accountId);
        }
        Company foundCompany = companies.get(0); // Asumimos que el accountId es único en todas las compañías
        return foundCompany.getCompanyName();
    }

    public List<Servicee> getServicesByCompanyId(String companyId) {
        log.info("Va a retornar todos los servicios para la compañía con ID: {}", companyId);
        Company company = this.companyRepository.findByUniqueId(companyId);
        return company.getServicees();
    }

    public String addServiceToCompany(String companyId, Servicee servicee) {
        Company company = this.companyRepository.findByUniqueId(companyId);
        if (company.getServicees().stream().anyMatch(s -> s.getUniqueId().equals(servicee.getUniqueId()))) {
            return "El servicio ya existe en la compañía";
        }
        company.getServicees().add(servicee);
        this.companyRepository.save(company);
        return "Servicio añadido con éxito";
    }

    public CompanyDTO getCompanyByServiceesName(String name) {
        log.info("Buscando compañía por nombre de servicio: {}", name);
        List<Company> companies = this.companyRepository.findByServiceesName(name);
        if (companies.isEmpty()) {
            log.info("No se encontró por nombre de servicio: {}", name);
            throw new RuntimeException("No se encontró por nombre de servicio: " + name);
        }
        Company foundCompany = companies.get(0);
        return this.companyMapper.toDTO(foundCompany);
    }

}
