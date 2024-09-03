package com.banquito.corecobros.companydoc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.banquito.corecobros.companydoc.dto.CompanyDTO;
import com.banquito.corecobros.companydoc.model.Account;
import com.banquito.corecobros.companydoc.model.Company;
import com.banquito.corecobros.companydoc.model.Servicee;
import com.banquito.corecobros.companydoc.repository.CompanyRepository;
import com.banquito.corecobros.companydoc.util.mapper.CompanyMapper;

public class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyMapper companyMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtainAllCompanies() {
        Company company = new Company();
        company.setUniqueId("ZGE0000866");
        company.setCommissionId("RTJ0045673");
        company.setRuc("1745433234001");
        company.setCompanyName("LA FAVORITA");
        company.setStatus("ACT");

        List<Company> companies = List.of(company);
        when(companyRepository.findAll()).thenReturn(companies);

        CompanyDTO companyDTO = CompanyDTO.builder()
                .uniqueId("ZGE0000866")
                .build();
        when(companyMapper.toDTO(company)).thenReturn(companyDTO);

        List<CompanyDTO> result = companyService.obtainAllCompanies();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ZGE0000866", result.get(0).getUniqueId());
    }

    @Test
    void testGetCompanyByUniqueId() {
        Company company = new Company();
        company.setUniqueId("ZGE0000866");
        company.setCommissionId("RTJ0045673");
        company.setRuc("1745433234001");
        company.setCompanyName("LA FAVORITA");
        company.setStatus("ACT");

        when(companyRepository.findByUniqueId("ZGE0000866")).thenReturn(company);

        CompanyDTO companyDTO = CompanyDTO.builder()
                .uniqueId("ZGE0000866")
                .build();
        when(companyMapper.toDTO(company)).thenReturn(companyDTO);

        CompanyDTO result = companyService.getCompanyByUniqueId("ZGE0000866");
        assertNotNull(result);
        assertEquals("ZGE0000866", result.getUniqueId());
    }

    @Test
    void testGetCompanyByRuc() {
        Company company = new Company();
        company.setUniqueId("ZGE0000866");
        company.setCommissionId("RTJ0045673");
        company.setRuc("1745433234001");
        company.setCompanyName("LA FAVORITA");
        company.setStatus("ACT");

        when(companyRepository.findByRuc("1745433234001")).thenReturn(company);

        CompanyDTO companyDTO = CompanyDTO.builder()
                .uniqueId("ZGE0000866")
                .build();
        when(companyMapper.toDTO(company)).thenReturn(companyDTO);

        CompanyDTO result = companyService.getCompanyByRuc("1745433234001");
        assertNotNull(result);
        assertEquals("ZGE0000866", result.getUniqueId());
    }

    @Test
    void testCreateCompany() {
        Company company = new Company();
        company.setRuc("1745433234001");
        company.setCompanyName("LA FAVORITA");

        Account account = new Account();
        account.setCodeInternalAccount("123456");
        company.setAccounts(List.of(account));
        company.setServicees(new ArrayList<>());

        when(companyRepository.findByRuc("1745433234001")).thenReturn(null);
        when(companyRepository.findAll()).thenReturn(List.of());
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        Company result = companyService.create(company);

        assertNotNull(result);
        assertNotNull(result.getUniqueId());
        assertEquals("1745433234001", result.getRuc());
        assertEquals(1, result.getAccounts().size());
        assertEquals("123456", result.getAccounts().get(0).getCodeInternalAccount());
    }

    @Test
    void testCreateCompanyWithExistingRuc() {
        Company existingCompany = new Company();
        existingCompany.setRuc("1745433234001");

        Company newCompany = new Company();
        newCompany.setRuc("1745433234001");

        when(companyRepository.findByRuc("1745433234001")).thenReturn(existingCompany);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            companyService.create(newCompany);
        });

        assertEquals("Ya existe una compañía con el RUC: 1745433234001", exception.getMessage());
    }

    @Test
    void testCreateCompanyWithExistingAccount() {
        Account existingAccount = new Account();
        existingAccount.setCodeInternalAccount("123456");

        Company existingCompany = new Company();
        existingCompany.setRuc("1745433234002");
        existingCompany.setAccounts(List.of(existingAccount));

        Company newCompany = new Company();
        newCompany.setRuc("1745433234001");

        Account newAccount = new Account();
        newAccount.setCodeInternalAccount("123456");
        newCompany.setAccounts(List.of(newAccount));

        when(companyRepository.findByRuc("1745433234001")).thenReturn(null);
        when(companyRepository.findAll()).thenReturn(List.of(existingCompany));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            companyService.create(newCompany);
        });

        assertEquals("Ya existe una cuenta con el número de cuenta: 123456", exception.getMessage());
    }

    @Test
    void testGetCompanyByCompanyName() {
        Company company = new Company();
        company.setUniqueId("ZGE0000866");
        company.setCompanyName("LA FAVORITA");

        List<Company> companies = List.of(company);
        when(companyRepository.findByCompanyNameContainingIgnoreCase("LA FAVORITA")).thenReturn(companies);

        CompanyDTO companyDTO = CompanyDTO.builder()
                .uniqueId("ZGE0000866")
                .companyName("LA FAVORITA")
                .build();
        when(companyMapper.toDTO(company)).thenReturn(companyDTO);

        List<CompanyDTO> result = companyService.getCompanyByCompanyName("LA FAVORITA");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ZGE0000866", result.get(0).getUniqueId());
        assertEquals("LA FAVORITA", result.get(0).getCompanyName());
    }

    @Test
    void testGetCommissionById() {
        Company company = new Company();
        company.setUniqueId("ZGE0000866");
        company.setCommissionId("RTJ0045673");

        when(companyRepository.findByCommissionId("RTJ0045673")).thenReturn(company);

        CompanyDTO companyDTO = CompanyDTO.builder()
                .uniqueId("ZGE0000866")
                .commissionId("RTJ0045673")
                .build();
        when(companyMapper.toDTO(company)).thenReturn(companyDTO);

        CompanyDTO result = companyService.getCommissionById("RTJ0045673");
        assertNotNull(result);
        assertEquals("ZGE0000866", result.getUniqueId());
        assertEquals("RTJ0045673", result.getCommissionId());
    }

    @Test
    void testGetCodeInternalAccountByUniqueId() {
        Company company = new Company();
        company.setUniqueId("ZGE0000866");

        Account account = new Account();
        account.setUniqueId("ACC123456");
        account.setCodeInternalAccount("CINT001");
        company.setAccounts(List.of(account));

        when(companyRepository.findByAccountsUniqueId("ACC123456")).thenReturn(List.of(company));

        String result = companyService.getCodeInternalAccountByUniqueId("ACC123456");
        assertNotNull(result);
        assertEquals("CINT001", result);
    }

    @Test
    void testGetAccountsByCompanyId() {
        Company company = new Company();
        company.setUniqueId("ZGE0000866");

        Account account = new Account();
        account.setUniqueId("ACC123456");
        account.setCodeInternalAccount("CINT001");
        company.setAccounts(List.of(account));

        when(companyRepository.findByUniqueId("ZGE0000866")).thenReturn(company);

        List<Account> result = companyService.getAccountsByCompanyId("ZGE0000866");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ACC123456", result.get(0).getUniqueId());
    }

    @Test
    void testGetCompanyByCodeInternalAccount() {
        Company company = new Company();
        company.setUniqueId("ZGE0000866");

        Account account = new Account();
        account.setCodeInternalAccount("CINT001");
        company.setAccounts(List.of(account));

        when(companyRepository.findByAccountsCodeInternalAccount("CINT001")).thenReturn(List.of(company));

        CompanyDTO companyDTO = CompanyDTO.builder()
                .uniqueId("ZGE0000866")
                .build();
        when(companyMapper.toDTO(company)).thenReturn(companyDTO);

        CompanyDTO result = companyService.getCompanyByCodeInternalAccount("CINT001");
        assertNotNull(result);
        assertEquals("ZGE0000866", result.getUniqueId());
    }

    @Test
    void testAddAccountToCompany() {
        Company company = new Company();
        company.setUniqueId("ZGE0000866");
        company.setAccounts(new ArrayList<>());

        Account account = new Account();
        account.setUniqueId("ACC123456");
        account.setCodeInternalAccount("CINT001");

        when(companyRepository.findByUniqueId("ZGE0000866")).thenReturn(company);
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        String result = companyService.addAccountToCompany("ZGE0000866", account);
        assertEquals("Cuenta añadida con éxito", result);
    }

    @Test
    void testGetCompanyNameByAccountId() {
        Company company = new Company();
        company.setUniqueId("ZGE0000866");
        company.setCompanyName("LA FAVORITA");

        Account account = new Account();
        account.setUniqueId("ACC123456");
        company.setAccounts(List.of(account));

        when(companyRepository.findByAccountsUniqueId("ACC123456")).thenReturn(List.of(company));

        String result = companyService.getCompanyNameByAccountId("ACC123456");
        assertNotNull(result);
        assertEquals("LA FAVORITA", result);
    }

    @Test
    void testGetServicesByCompanyId() {
        Company company = new Company();
        company.setUniqueId("ZGE0000866");

        Servicee servicee = new Servicee();
        servicee.setUniqueId("SVC123456");
        company.setServicees(List.of(servicee));

        when(companyRepository.findByUniqueId("ZGE0000866")).thenReturn(company);

        List<Servicee> result = companyService.getServicesByCompanyId("ZGE0000866");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("SVC123456", result.get(0).getUniqueId());
    }

    @Test
    void testAddServiceToCompany() {
        Company company = new Company();
        company.setUniqueId("ZGE0000866");
        company.setServicees(new ArrayList<>());

        Servicee servicee = new Servicee();
        servicee.setUniqueId("SVC123456");

        when(companyRepository.findByUniqueId("ZGE0000866")).thenReturn(company);
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        String result = companyService.addServiceToCompany("ZGE0000866", servicee);

        assertEquals("Servicio añadido con éxito", result);

        assertTrue(company.getServicees().contains(servicee));

        verify(companyRepository).save(argThat(c -> c.getServicees().contains(servicee)));
    }

    @Test
    void testGetCompanyByServiceesName() {
        Company company = new Company();
        company.setUniqueId("ZGE0000866");

        Servicee servicee = new Servicee();
        servicee.setName("Service1");
        company.setServicees(List.of(servicee));

        when(companyRepository.findByServiceesName("Service1")).thenReturn(List.of(company));

        CompanyDTO companyDTO = CompanyDTO.builder()
                .uniqueId("ZGE0000866")
                .build();
        when(companyMapper.toDTO(company)).thenReturn(companyDTO);

        CompanyDTO result = companyService.getCompanyByServiceesName("Service1");
        assertNotNull(result);
        assertEquals("ZGE0000866", result.getUniqueId());
    }

}
