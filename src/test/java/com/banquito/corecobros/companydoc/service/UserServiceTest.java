package com.banquito.corecobros.companydoc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.banquito.corecobros.companydoc.dto.PasswordDTO;
import com.banquito.corecobros.companydoc.dto.UserDTO;
import com.banquito.corecobros.companydoc.model.User;
import com.banquito.corecobros.companydoc.repository.CompanyRepository;
import com.banquito.corecobros.companydoc.repository.UserRepository;
import com.banquito.corecobros.companydoc.util.mapper.UserMapper;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() {
        String companyId = "NME0093756";
        String firstName = "CARLOS";
        String lastName = "RAMIREZ";
        String email = "carlos.ramirez@globaltrading.ec";
        String role = "APR";
        String status = "ACT";
        String userType = "EMP";

        when(userRepository.findByUser(any(String.class))).thenReturn(null);
        when(userRepository.findByUniqueId(any(String.class))).thenReturn(null);

        User user = userService.createUser(companyId, firstName, lastName, email, role, status, userType);

        assertNotNull(user);
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(companyId, user.getCompanyId());
        assertEquals(email, user.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_UsernameAlreadyExists() {
        String companyId = "NME0093756";
        String firstName = "CARLOS";
        String lastName = "RAMIREZ";
        String email = "carlos.ramirez@globaltrading.ec";
        String role = "APR";
        String status = "ACT";
        String userType = "EMP";

        when(userRepository.findByUser(any(String.class))).thenReturn(new User());

        assertThrows(RuntimeException.class, () -> {
            userService.createUser(companyId, firstName, lastName, email, role, status, userType);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUserByUniqueId_UserDoesNotExist() {
        when(userRepository.findByUniqueId("uniqueId")).thenReturn(null);

        UserDTO userDTO = userService.getUserByUniqueId("uniqueId");

        assertNull(userDTO);
        verify(userRepository, times(1)).findByUniqueId("uniqueId");
    }

    @Test
    void testUpdateUser_Success() {
        String uniqueId = "JDT0047002";
        User userDetails = new User();
        userDetails.setCompanyId("newCompanyId");
        userDetails.setFirstName("Jane");
        userDetails.setLastName("Doe");

        User existingUser = new User();
        existingUser.setUniqueId(uniqueId);
        when(userRepository.findByUniqueId(uniqueId)).thenReturn(existingUser);

        userService.updateUser(uniqueId, userDetails);

        verify(userRepository, times(1)).save(existingUser);
        assertEquals("newCompanyId", existingUser.getCompanyId());
        assertEquals("Jane", existingUser.getFirstName());
        assertEquals("Doe", existingUser.getLastName());
    }

    @Test
    void testUpdateUser_UserNotFound() {
        when(userRepository.findByUniqueId("uniqueId")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            userService.updateUser("uniqueId", new User());
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testObtainAllUsers() {
        List<User> userList = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(userList);

        List<UserDTO> result = userService.obtainAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUser_UserExists() {
        User user = new User();
        user.setUser("testUser");
        when(userRepository.findByUser("testUser")).thenReturn(user);

        User result = userService.getUser("testUser");

        assertNotNull(result);
        assertEquals("testUser", result.getUser());
        verify(userRepository, times(1)).findByUser("testUser");
    }

    @Test
    void testGetUser_UserDoesNotExist() {
        when(userRepository.findByUser("testUser")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            userService.getUser("testUser");
        });
        verify(userRepository, times(1)).findByUser("testUser");
    }

    @Test
    void testGetCompanyNameByUser_UserDoesNotExist() {
        when(userRepository.findByUser("testUser")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            userService.getCompanyNameByUser("testUser");
        });
        verify(userRepository, times(1)).findByUser("testUser");
        verify(companyRepository, never()).findByUniqueId(any(String.class));
    }

    @Test
    void testGetCompanyNameByUser_CompanyDoesNotExist() {
        User user = new User();
        user.setCompanyId("companyId");
        when(userRepository.findByUser("testUser")).thenReturn(user);
        when(companyRepository.findByUniqueId("companyId")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> userService.getCompanyNameByUser("testUser"));
    }

    @Test
    void testGetUsersByCompanyId() {
        List<User> userList = List.of(new User(), new User());
        when(userRepository.findByCompanyId("companyId")).thenReturn(userList);

        List<UserDTO> result = userService.getUsersByCompanyId("companyId");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findByCompanyId("companyId");
    }

    @Test
    void testLogin_Success() {
        User user = new User();
        user.setUser("testUser");
        user.setPassword(DigestUtils.md5Hex("testPass"));
        user.setStatus("ACT");
        user.setFirstLogin(false);
        when(userRepository.findByUser("testUser")).thenReturn(user);

        User loginUser = new User();
        loginUser.setUser("testUser");
        loginUser.setPassword("testPass");
        User result = userService.login(loginUser);

        assertNotNull(result);
        assertEquals("testUser", result.getUser());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testLogin_FirstLogin() {
        User user = new User();
        user.setUser("testUser");
        user.setPassword(DigestUtils.md5Hex("testPass"));
        user.setStatus("ACT");
        user.setFirstLogin(true);
        when(userRepository.findByUser("testUser")).thenReturn(user);

        assertThrows(RuntimeException.class, () -> {
            User loginUser = new User();
            loginUser.setUser("testUser");
            loginUser.setPassword("testPass");
            userService.login(loginUser);
        });

        verify(userRepository, never()).save(user);
    }

    @Test
    void testLogin_IncorrectPassword() {
        User user = new User();
        user.setUser("testUser");
        user.setPassword(DigestUtils.md5Hex("correctPass"));
        when(userRepository.findByUser("testUser")).thenReturn(user);

        assertThrows(RuntimeException.class, () -> {
            User loginUser = new User();
            loginUser.setUser("testUser");
            loginUser.setPassword("wrongPass");
            userService.login(loginUser);
        });

        verify(userRepository, never()).save(user);
    }

    @Test
    void testLogin_UserNotActive() {
        User user = new User();
        user.setUser("testUser");
        user.setPassword(DigestUtils.md5Hex("testPass"));
        user.setStatus("INA");
        when(userRepository.findByUser("testUser")).thenReturn(user);

        assertThrows(RuntimeException.class, () -> {
            User loginUser = new User();
            loginUser.setUser("testUser");
            loginUser.setPassword("testPass");
            userService.login(loginUser);
        });

        verify(userRepository, never()).save(user);
    }

    @Test
    void testGeneratePassword_UserExists() {
        User user = new User();
        user.setUser("testUser");
        when(userRepository.findByUser("testUser")).thenReturn(user);

        userService.generatePassword("testUser");

        verify(userRepository, times(1)).save(user);
        assertNotNull(user.getPassword());
    }

    @Test
    void testGeneratePassword_UserNotFound() {
        when(userRepository.findByUser("testUser")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            userService.generatePassword("testUser");
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testChangePassword_Success() {
        User user = new User();
        user.setUser("testUser");
        user.setPassword(DigestUtils.md5Hex("correctPassword"));
        when(userRepository.findByUser("testUser")).thenReturn(user);

        PasswordDTO passwordDTO = PasswordDTO.builder()
                .user("testUser")
                .oldPassword("correctPassword")
                .newPassword("newPassword")
                .build();

        String result = userService.changePassword(passwordDTO);

        assertNotNull(result);
        assertEquals("Password updated successfully", result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testChangePassword_IncorrectCurrentPassword() {
        User user = new User();
        user.setUser("testUser");
        user.setPassword(DigestUtils.md5Hex("correctPassword"));
        when(userRepository.findByUser("testUser")).thenReturn(user);

        PasswordDTO passwordDTO = PasswordDTO.builder()
                .user("testUser")
                .oldPassword("wrongPassword")
                .newPassword("newPassword")
                .build();

        assertThrows(RuntimeException.class, () -> userService.changePassword(passwordDTO));
    }

    @Test
    void testResetPassword_Success() {
        User user = new User();
        user.setUser("testUser");
        user.setEmail("test@example.com");
        when(userRepository.findByUser("testUser")).thenReturn(user);

        String result = userService.resetPassword("testUser", "test@example.com");

        assertNotNull(result);
        assertEquals("Password reset successfully. New password: ", result.substring(0, 43));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testResetPassword_UserNotFound() {
        when(userRepository.findByUser("testUser")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            userService.resetPassword("testUser", "test@example.com");
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testResetPassword_IncorrectEmail() {
        User user = new User();
        user.setUser("testUser");
        user.setEmail("correct@example.com");
        when(userRepository.findByUser("testUser")).thenReturn(user);

        assertThrows(RuntimeException.class, () -> {
            userService.resetPassword("testUser", "wrong@example.com");
        });

        verify(userRepository, never()).save(user);
    }

}
