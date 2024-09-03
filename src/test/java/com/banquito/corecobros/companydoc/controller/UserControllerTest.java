package com.banquito.corecobros.companydoc.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.banquito.corecobros.companydoc.dto.PasswordDTO;
import com.banquito.corecobros.companydoc.dto.UserDTO;
import com.banquito.corecobros.companydoc.model.User;
import com.banquito.corecobros.companydoc.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();

        user = new User();
        user.setUniqueId("JDT0047002");
        user.setFirstName("CARLOS");
        user.setLastName("RAMIREZ");
        user.setEmail("carlos.ramirez@globaltrading.ec");
        user.setCompanyId("NME0093756");
        user.setRole("APR");
        user.setStatus("ACT");
        user.setUserType("EMP");

        userDTO = UserDTO.builder()
                .uniqueId("JDT0047002")
                .firstName("CARLOS")
                .lastName("RAMIREZ")
                .email("carlos.ramirez@globaltrading.ec")
                .companyId("NME0093756")
                .role("APR")
                .status("ACT")
                .userType("EMP")
                .build();
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userService.obtainAllUsers()).thenReturn(List.of(userDTO));

        ResultActions result = mockMvc.perform(get("/company-microservice/api/v1/users")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].uniqueId").value("JDT0047002"))
                .andExpect(jsonPath("$[0].firstName").value("CARLOS"))
                .andExpect(jsonPath("$[0].lastName").value("RAMIREZ"))
                .andExpect(jsonPath("$[0].email").value("carlos.ramirez@globaltrading.ec"))
                .andExpect(jsonPath("$[0].companyId").value("NME0093756"))
                .andExpect(jsonPath("$[0].role").value("APR"))
                .andExpect(jsonPath("$[0].status").value("ACT"))
                .andExpect(jsonPath("$[0].userType").value("EMP"));
    }

    @Test
    void testGetUserByUniqueId() throws Exception {
        when(userService.getUserByUniqueId("JDT0047002")).thenReturn(userDTO);

        ResultActions result = mockMvc
                .perform(get("/company-microservice/api/v1/users/{uniqueId}", "JDT0047002")
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uniqueId").value("JDT0047002"))
                .andExpect(jsonPath("$.firstName").value("CARLOS"))
                .andExpect(jsonPath("$.lastName").value("RAMIREZ"))
                .andExpect(jsonPath("$.email").value("carlos.ramirez@globaltrading.ec"))
                .andExpect(jsonPath("$.companyId").value("NME0093756"))
                .andExpect(jsonPath("$.role").value("APR"))
                .andExpect(jsonPath("$.status").value("ACT"))
                .andExpect(jsonPath("$.userType").value("EMP"));
    }

    @Test
    void testCreateUser() throws Exception {
        User userRequest = new User();
        userRequest.setUniqueId("JDT0047002");
        userRequest.setFirstName("CARLOS");
        userRequest.setLastName("RAMIREZ");
        userRequest.setEmail("carlos.ramirez@globaltrading.ec");
        userRequest.setCompanyId("NME0093756");
        userRequest.setRole("APR");
        userRequest.setStatus("ACT");
        userRequest.setUserType("EMP");

        when(userService.createUser(
                userRequest.getCompanyId(),
                userRequest.getFirstName(),
                userRequest.getLastName(),
                userRequest.getEmail(),
                userRequest.getRole(),
                userRequest.getStatus(),
                userRequest.getUserType())).thenReturn(userRequest);

        mockMvc.perform(post("/company-microservice/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"uniqueId\":\"JDT0047002\", \"firstName\":\"CARLOS\", \"lastName\":\"RAMIREZ\", \"email\":\"carlos.ramirez@globaltrading.ec\", \"companyId\":\"NME0093756\", \"role\":\"APR\", \"status\":\"ACT\", \"userType\":\"EMP\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uniqueId").value("JDT0047002"))
                .andExpect(jsonPath("$.firstName").value("CARLOS"))
                .andExpect(jsonPath("$.lastName").value("RAMIREZ"))
                .andExpect(jsonPath("$.email").value("carlos.ramirez@globaltrading.ec"));
    }

    @Test
    void testUpdateUser() throws Exception {
        mockMvc.perform(put("/company-microservice/api/v1/users/JDT0047002")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"uniqueId\":\"JDT0047002\", \"firstName\":\"CARLOS\", \"lastName\":\"RAMIREZ\", \"email\":\"carlos.ramirez@globaltrading.ec\", \"companyId\":\"NME0093756\", \"role\":\"APR\", \"status\":\"ACT\", \"userType\":\"EMP\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUsersByCompanyId() throws Exception {
        when(userService.getUsersByCompanyId("NME0093756")).thenReturn(List.of(userDTO));

        ResultActions result = mockMvc
                .perform(get("/company-microservice/api/v1/users/company/{companyId}", "NME0093756")
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].uniqueId").value("JDT0047002"))
                .andExpect(jsonPath("$[0].firstName").value("CARLOS"))
                .andExpect(jsonPath("$[0].lastName").value("RAMIREZ"))
                .andExpect(jsonPath("$[0].email").value("carlos.ramirez@globaltrading.ec"))
                .andExpect(jsonPath("$[0].companyId").value("NME0093756"))
                .andExpect(jsonPath("$[0].role").value("APR"))
                .andExpect(jsonPath("$[0].status").value("ACT"))
                .andExpect(jsonPath("$[0].userType").value("EMP"));
    }

    @Test
    void testLogin() throws Exception {
        User loggedUser = new User();
        loggedUser.setUniqueId("JDT0047002");
        loggedUser.setFirstName("CARLOS");
        loggedUser.setLastName("RAMIREZ");
        loggedUser.setEmail("carlos.ramirez@globaltrading.ec");
        loggedUser.setCompanyId("NME0093756");
        loggedUser.setRole("APR");
        loggedUser.setStatus("ACT");
        loggedUser.setUserType("EMP");

        when(userService.login(loggedUser)).thenReturn(loggedUser);

        mockMvc.perform(post("/company-microservice/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"carlos.ramirez@globaltrading.ec\", \"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uniqueId").value("JDT0047002"))
                .andExpect(jsonPath("$.firstName").value("CARLOS"))
                .andExpect(jsonPath("$.lastName").value("RAMIREZ"))
                .andExpect(jsonPath("$.email").value("carlos.ramirez@globaltrading.ec"));
    }

    @Test
    void testChangePassword() throws Exception {
        PasswordDTO passwordDTO = PasswordDTO.builder()
                .user("JDT0047002")
                .oldPassword("oldPassword123")
                .newPassword("newPassword123")
                .build();

        when(userService.changePassword(passwordDTO))
                .thenReturn("Password changed successfully");

        ResultActions result = mockMvc.perform(put("/company-microservice/api/v1/users/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordDTO)));

        result.andExpect(status().isOk())
                .andExpect(content().string("Password changed successfully"));
    }

    @Test
    void testResetPassword() throws Exception {
        PasswordDTO passwordDTO = PasswordDTO.builder()
                .user("JDT0047002")
                .email("carlos.ramirez@globaltrading.ec")
                .build();

        when(userService.resetPassword(passwordDTO.getUser(), passwordDTO.getEmail()))
                .thenReturn("Password reset successfully");

        ResultActions result = mockMvc.perform(put("/company-microservice/api/v1/users/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordDTO)));

        result.andExpect(status().isOk())
                .andExpect(content().string("Password reset successfully"));
    }

}
