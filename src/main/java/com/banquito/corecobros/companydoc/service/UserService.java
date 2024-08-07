package com.banquito.corecobros.companydoc.service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banquito.corecobros.companydoc.dto.PasswordDTO;
import com.banquito.corecobros.companydoc.dto.UserDTO;
import com.banquito.corecobros.companydoc.model.Company;
import com.banquito.corecobros.companydoc.model.User;
import com.banquito.corecobros.companydoc.repository.CompanyRepository;
import com.banquito.corecobros.companydoc.repository.UserRepository;
import com.banquito.corecobros.companydoc.util.mapper.UserMapper;
import com.banquito.corecobros.companydoc.util.uniqueId.UniqueIdGeneration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    @Autowired
    private CompanyRepository companyRepository;

    private final UserRepository userRepository;
    private final UserMapper mapper;

    public UserService(UserRepository userRepository, UserMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public List<UserDTO> obtainAllUsers() {
        log.info("Fetching all users");
        List<User> users = this.userRepository.findAll();
        return users.stream().map(this.mapper::toDTO).collect(Collectors.toList());
    }

    public UserDTO getUserByUniqueId(String uniqueId) {
        log.info("Fetching user with uniqueId: {}", uniqueId);
        User user = this.userRepository.findByUniqueId(uniqueId);
        if (user == null) {
            log.info("User with uniqueId {} not found", uniqueId);
            return null;
        }
        return this.mapper.toDTO(user);
    }

    public User getUser(String user) {
        User usern = this.userRepository.findByUser(user);
        if (usern == null) {
            throw new RuntimeException("User with username " + user + " does not exist.");
        }
        return usern;
    }

    public String getCompanyNameByUser(String user) {
        log.info("Fetching company for user: {}", user);
        User userEntity = this.userRepository.findByUser(user);
        if (userEntity == null) {
            throw new RuntimeException("User with username " + user + " not found.");
        }
        Company company = this.companyRepository.findByUniqueId(userEntity.getCompanyId());
        if (company == null) {
            throw new RuntimeException("Company for user " + user + " not found.");
        }
        return company.getCompanyName();
    }

    public User createUser(String companyId, String firstName, String lastName,
                           String email, String role, String status, String userType) {

        UniqueIdGeneration uniqueIdGenerator = new UniqueIdGeneration();
        String uniqueId;
        boolean uniqueIdExists;

        do {
            uniqueId = uniqueIdGenerator.getUniqueId();
            uniqueIdExists = userRepository.findByUniqueId(uniqueId) != null;
        } while (uniqueIdExists);

        String userName = firstName.substring(0, 1).toLowerCase() + lastName.toLowerCase();
        String password = generateRandomPassword();
        String md5Password = DigestUtils.md5Hex(password);

        if (this.userRepository.findByUser(userName) != null) {
            throw new RuntimeException("Username already exists.");
        }
        User user = new User();
        user.setCompanyId(companyId);
        user.setUniqueId(uniqueId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUser(userName);
        user.setPassword(md5Password);
        user.setCreateDate(LocalDate.now());
        user.setFirstLogin(true);
        user.setEmail(email);
        user.setRole(role);
        user.setStatus(status);
        user.setUserType(userType);
        this.userRepository.save(user);
        String logMessage = String.format("User created with username: %s, password: %s", userName, password);
        log.info(logMessage);
        user.setMessage(logMessage);
        return user;
    }

    public void updateUser(String uniqueId, User userDetails) {
        log.info("Updating user with ID: {}", uniqueId);
        User existingUser = this.userRepository.findByUniqueId(uniqueId);
        if (existingUser == null) {
            throw new RuntimeException("User with ID " + uniqueId + " not found.");
        }
        existingUser.setCompanyId(userDetails.getCompanyId());
        existingUser.setFirstName(userDetails.getFirstName());
        existingUser.setLastName(userDetails.getLastName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setRole(userDetails.getRole());
        existingUser.setStatus(userDetails.getStatus());
        existingUser.setUserType(userDetails.getUserType());
        User updatedUser = this.userRepository.save(existingUser);
        log.info("User updated: {}", updatedUser);
    }

    public List<UserDTO> getUsersByCompanyId(String companyId) {
        log.info("Fetching all users for company with ID: {}", companyId);
        List<User> users = this.userRepository.findByCompanyId(companyId);
        return users.stream().map(this.mapper::toDTO).collect(Collectors.toList());
    }

    public User login(User dto) {
        String errorMessage = "Incorrect username or password";
        log.info("Logging in user: {}", dto);
        if (dto.getUser() != null && dto.getPassword() != null && dto.getUser().length() > 3
                && dto.getPassword().length() > 5) {
            User user = this.userRepository.findByUser(dto.getUser());
            if (user != null) {
                String md5 = DigestUtils.md5Hex(dto.getPassword());
                if (user.getPassword().equals(md5)) {
                    user.setLastConnection(LocalDateTime.now());
                    if ("ACT".equals(user.getStatus())) {
                        if (user.isFirstLogin()) {
                            errorMessage = "First login. Must change password.";
                            throw new RuntimeException(errorMessage);
                        }
                        this.userRepository.save(user);
                        return user;
                    } else {
                        errorMessage = "User is not active";
                    }
                }
            }
        }
        throw new RuntimeException(errorMessage);
    }

    public void generatePassword(String userName) {
        User user = this.userRepository.findByUser(userName);
        if (user == null) {
            throw new RuntimeException("User not found.");
        }
        String password = generateRandomPassword();
        user.setPassword(DigestUtils.md5Hex(password));
        this.userRepository.save(user);
        log.info("Generated password for user {}: {}", userName, password);
    }

    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public String changePassword(PasswordDTO passwordDTO) {
        User user = this.userRepository.findByUser(passwordDTO.getUser());
        if (user == null) {
            throw new RuntimeException("User not found.");
        }
        String md5OldPassword = DigestUtils.md5Hex(passwordDTO.getOldPassword());
        if (!user.getPassword().equals(md5OldPassword)) {
            throw new RuntimeException("Current password is incorrect.");
        }
        String md5NewPassword = DigestUtils.md5Hex(passwordDTO.getNewPassword());
        user.setPassword(md5NewPassword);
        user.setFirstLogin(false);
        this.userRepository.save(user);
        return "Password updated successfully";
    }

    public String resetPassword(String userName, String email) {
        User user = this.userRepository.findByUser(userName);
        if (user == null) {
            throw new RuntimeException("User not found.");
        }
        if (!user.getEmail().equals(email)) {
            throw new RuntimeException("Incorrect email.");
        }
        String newPassword = generateRandomPassword();
        user.setPassword(DigestUtils.md5Hex(newPassword));
        this.userRepository.save(user);
        return "Password reset successfully. New password: " + newPassword;
    }

}