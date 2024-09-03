package com.banquito.corecobros.companydoc.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.banquito.corecobros.companydoc.dto.CompanyDTO;
import com.banquito.corecobros.companydoc.model.Account;
import com.banquito.corecobros.companydoc.model.Company;
import com.banquito.corecobros.companydoc.model.Servicee;
import com.banquito.corecobros.companydoc.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(CompanyController.class)
public class CompanyControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private CompanyService companyService;

        @InjectMocks
        private CompanyController companyController;

        private CompanyDTO companyDTO;
        private Company company;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);

                company = new Company();
                company.setUniqueId("ZGE0000866");
                company.setCommissionId("RTJ0045673");
                company.setRuc("1745433234001");
                company.setCompanyName("LA FAVORITA");

                companyDTO = CompanyDTO.builder()
                                .uniqueId("ZGE0000866")
                                .commissionId("RTJ0045673")
                                .ruc("1745433234001")
                                .companyName("LA FAVORITA")
                                .build();
        }

        @Test
        void testGetAllCompanies() throws Exception {
                when(companyService.obtainAllCompanies()).thenReturn(List.of(companyDTO));

                ResultActions result = mockMvc.perform(get("/company-microservice/api/v1/companies")
                                .accept(MediaType.APPLICATION_JSON));

                result.andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$[0].uniqueId").value("ZGE0000866"))
                                .andExpect(jsonPath("$[0].commissionId").value("RTJ0045673"))
                                .andExpect(jsonPath("$[0].ruc").value("1745433234001"))
                                .andExpect(jsonPath("$[0].companyName").value("LA FAVORITA"));
        }

        @Test
        void testGetCompanyByUniqueId() throws Exception {
                when(companyService.getCompanyByUniqueId("ZGE0000866")).thenReturn(companyDTO);

                ResultActions result = mockMvc
                                .perform(get("/company-microservice/api/v1/companies/{companyId}", "ZGE0000866")
                                                .accept(MediaType.APPLICATION_JSON));

                result.andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.uniqueId").value("ZGE0000866"))
                                .andExpect(jsonPath("$.commissionId").value("RTJ0045673"))
                                .andExpect(jsonPath("$.ruc").value("1745433234001"))
                                .andExpect(jsonPath("$.companyName").value("LA FAVORITA"));
        }

        @Test
        void testCreateCompany() throws Exception {
                Company company = new Company();
                company.setUniqueId("ZGE0000866");
                company.setCommissionId("RTJ0045673");
                company.setRuc("1745433234001");
                company.setCompanyName("LA FAVORITA");

                when(companyService.create(any(Company.class))).thenReturn(company);

                ResultActions result = mockMvc.perform(post("/company-microservice/api/v1/companies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"uniqueId\":\"ZGE0000866\",\"commissionId\":\"RTJ0045673\",\"ruc\":\"1745433234001\",\"companyName\":\"LA FAVORITA\"}"));

                result.andExpect(status().isCreated())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.uniqueId").value("ZGE0000866"))
                                .andExpect(jsonPath("$.commissionId").value("RTJ0045673"))
                                .andExpect(jsonPath("$.ruc").value("1745433234001"))
                                .andExpect(jsonPath("$.companyName").value("LA FAVORITA"));
        }

        @Test
        void testUpdateCompany() throws Exception {
                doNothing().when(companyService).updateCompany(anyString(), any(Company.class));

                ResultActions result = mockMvc.perform(put("/company-microservice/api/v1/companies/{companyId}",
                                "ZGE0000866")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"uniqueId\":\"ZGE0000866\",\"commissionId\":\"RTJ0045673\",\"ruc\":\"1745433234001\",\"companyName\":\"LA FAVORITA\"}"));

                result.andExpect(status().isOk());
        }

        @Test
        void testGetCompanyByRuc() throws Exception {
                when(companyService.getCompanyByRuc("1745433234001")).thenReturn(companyDTO);

                ResultActions result = mockMvc
                                .perform(get("/company-microservice/api/v1/companies/ruc/{ruc}", "1745433234001")
                                                .accept(MediaType.APPLICATION_JSON));

                result.andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.uniqueId").value("ZGE0000866"))
                                .andExpect(jsonPath("$.commissionId").value("RTJ0045673"))
                                .andExpect(jsonPath("$.ruc").value("1745433234001"))
                                .andExpect(jsonPath("$.companyName").value("LA FAVORITA"));
        }

        @Test
        void testGetCompanyByCompanyName() throws Exception {
                when(companyService.getCompanyByCompanyName("LA FAVORITA")).thenReturn(List.of(companyDTO));

                ResultActions result = mockMvc
                                .perform(get("/company-microservice/api/v1/companies/name/{companyName}", "LA FAVORITA")
                                                .accept(MediaType.APPLICATION_JSON));

                result.andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$[0].uniqueId").value("ZGE0000866"))
                                .andExpect(jsonPath("$[0].commissionId").value("RTJ0045673"))
                                .andExpect(jsonPath("$[0].ruc").value("1745433234001"))
                                .andExpect(jsonPath("$[0].companyName").value("LA FAVORITA"));
        }

        @Test
        void testGetCommissionById() throws Exception {
                when(companyService.getCommissionById("RTJ0045673")).thenReturn(companyDTO);

                ResultActions result = mockMvc.perform(
                                get("/company-microservice/api/v1/companies/commission/{commissionId}", "RTJ0045673")
                                                .accept(MediaType.APPLICATION_JSON));

                result.andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.uniqueId").value("ZGE0000866"))
                                .andExpect(jsonPath("$.commissionId").value("RTJ0045673"))
                                .andExpect(jsonPath("$.ruc").value("1745433234001"))
                                .andExpect(jsonPath("$.companyName").value("LA FAVORITA"));
        }

        @Test
        void testGetAccountsByCompanyId() throws Exception {
                Account account = new Account();
                account.setUniqueId("ACC12345");

                when(companyService.getAccountsByCompanyId("ZGE0000866")).thenReturn(List.of(account));

                ResultActions result = mockMvc.perform(
                                get("/company-microservice/api/v1/companies/{companyId}/accounts", "ZGE0000866")
                                                .accept(MediaType.APPLICATION_JSON));

                result.andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$[0].uniqueId").value("ACC12345"));
        }

        @Test
        void testGetCodeInternalAccountByUniqueId() throws Exception {
                when(companyService.getCodeInternalAccountByUniqueId("ACC12345")).thenReturn("CIA001");

                ResultActions result = mockMvc.perform(
                                get("/company-microservice/api/v1/companies/account/unique-id/{uniqueId}", "ACC12345")
                                                .accept(MediaType.APPLICATION_JSON));

                result.andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.codeInternalAccount").value("CIA001"));
        }

        @Test
        void testAddAccountToCompany() throws Exception {
                String companyId = "ZGE0000866";
                Account account = new Account();
                account.setCodeInternalAccount("2258471601");
                account.setType("Corriente");
                account.setStatus("ACT");

                when(companyService.addAccountToCompany(anyString(), any(Account.class)))
                                .thenReturn("Cuenta añadida con éxito");

                ResultActions result = mockMvc.perform(post(
                                "/company-microservice/api/v1/companies/{companyId}/accounts", companyId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"codeInternalAccount\":\"2258471601\",\"type\":\"Corriente\",\"status\":\"ACT\"}"));

                result.andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                                .andExpect(content().string("Cuenta añadida con éxito"));
        }

        @Test
        void testAddServiceToCompany() throws Exception {
                String companyId = "ZGE0000866";
                Servicee servicee = new Servicee();
                servicee.setUniqueId("ZGE0000866");
                servicee.setName("Recaudo");
                servicee.setStatus("ACT");
                servicee.setDescription("Servicio de recaudo");

                when(companyService.addServiceToCompany(anyString(), any(Servicee.class)))
                                .thenReturn("Servicio añadido con éxito");

                ResultActions result = mockMvc.perform(post(
                                "/company-microservice/api/v1/companies/{companyId}/services", companyId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"uniqueId\":\"ZGE0000866\",\"name\":\"Recaudo\",\"status\":\"ACT\",\"description\":\"Servicio de recaudo\"}"));

                result.andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                                .andExpect(content().string("Servicio añadido con éxito"));
        }

        @Test
        void testGetCompanyNameByAccountId() throws Exception {
                String accountId = "ACC123456";
                String companyName = "LA FAVORITA";

                when(companyService.getCompanyNameByAccountId(accountId))
                                .thenReturn(companyName);

                mockMvc.perform(get("/company-microservice/api/v1/companies/account/{accountId}", accountId))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                                .andExpect(content().string(companyName));
        }

        @Test
        void testGetServicesByCompanyId() throws Exception {
                String companyId = "ZGE0000866";
                Servicee servicee = new Servicee();
                servicee.setUniqueId("SVC123456");
                servicee.setName("Recaudo");
                servicee.setStatus("ACT");
                servicee.setDescription("Servicio de recaudo");

                List<Servicee> services = List.of(servicee);

                when(companyService.getServicesByCompanyId(companyId))
                                .thenReturn(services);

                mockMvc.perform(get("/company-microservice/api/v1/companies/{companyId}/services", companyId))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(jsonPath("$.size()").value(1))
                                .andExpect(jsonPath("$[0].uniqueId").value("SVC123456"))
                                .andExpect(jsonPath("$[0].name").value("Recaudo"))
                                .andExpect(jsonPath("$[0].status").value("ACT"))
                                .andExpect(jsonPath("$[0].description").value("Servicio de recaudo"));
        }

}
