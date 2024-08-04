package com.banquito.corecobros.companydoc.service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        log.info("Va a retornar todos los usuarios");
        List<User> users = this.userRepository.findAll();
        return users.stream().map(u -> this.mapper.toDTO(u))
                .collect(Collectors.toList());
    }

    public UserDTO getUserByUniqueId(String uniqueId) {
        log.info("Va a buscar el usuario con uniqueId: {}", uniqueId);
        User user = this.userRepository.findByUniqueId(uniqueId);
        if (user == null) {
            log.info("No se encontró el usuario con uniqueId: {}", uniqueId);
            return null;
        }
        log.info("Se encontró el usuario: {}", user);
        return this.mapper.toDTO(user);
    }

    public User getUser(String user) {
        User usern = this.userRepository.findByUser(user);
        if (user != null) {
            return usern;
        } else {
            throw new RuntimeException("No existe usuario con el userName:" + user);
        }
    }

    public String getCompanyNameByUser(String user) {
        log.info("Va a buscar la empresa para el usuario: {}", user);
        User userEntity = this.userRepository.findByUser(user);
        if (userEntity == null) {
            throw new RuntimeException("No se encontró el usuario con user: " + user);
        }
        Company company = this.companyRepository.findByUniqueId(userEntity.getCompanyId());
        if (company == null) {
            throw new RuntimeException("No se encontró la empresa para el user: " + user);
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
            throw new RuntimeException("El nombre de usuario ya existe.");
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
        String logMessage = String.format("Usuario creado con nombre de usuario: %s, contraseña: %s", userName,
                password);
        log.info(logMessage);
        user.setMessage(logMessage);
        return user;
    }

    public void updateUser(String uniqueId, User userDetails) {
        log.info("Va a actualizar el usuario con ID: {}", uniqueId);
        User existingUser = this.userRepository.findByUniqueId(uniqueId);
        if (existingUser == null) {
            throw new RuntimeException("No se encontró la compañía con ID: " + uniqueId);
        }
        existingUser.setCompanyId(userDetails.getCompanyId());
        existingUser.setFirstName(userDetails.getFirstName());
        existingUser.setLastName(userDetails.getLastName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setRole(userDetails.getRole());
        existingUser.setStatus(userDetails.getStatus());
        existingUser.setUserType(userDetails.getUserType());
        User updatedUser = this.userRepository.save(existingUser);
        log.info("Se actualizó el usuario: {}", updatedUser);
    }

    public List<UserDTO> getUsersByCompanyId(String companyId) {
        log.info("Va a retornar todos los usuarios para la compañía con ID: {}", companyId);
        List<User> users = this.userRepository.findByCompanyId(companyId);
        return users.stream().map(u -> this.mapper.toDTO(u)).collect(Collectors.toList());
    }

    public User login(User dto) {
        String errorMessage = "Usuario o contraseña incorrecta";
        log.info("Usuario: {}", dto);
        if (dto.getUser() != null && dto.getPassword() != null && dto.getUser().length() > 3
                && dto.getPassword().length() > 5) {
            log.info("El usuario y contraseña coinciden");
            User user = this.userRepository.findByUser(dto.getUser());
            if (user != null) {
                log.info("Usuario encontrado: {}", user);
                String md5 = DigestUtils.md5Hex(dto.getPassword());
                log.info("MD5 de la contraseña proporcionada: {}", md5);
                if (user.getPassword().equals(md5)) {
                    user.setLastConnection(LocalDateTime.now());
                    if ("ACT".equals(user.getStatus())) {
                        log.info("Usuario activo");
                        if (user.isFirstLogin()) {
                            errorMessage = "Primera vez que inicia sesión. Debe cambiar su contraseña.";
                            log.info(errorMessage);
                            throw new RuntimeException(errorMessage);
                        }
                        this.userRepository.save(user);
                        return user;
                    } else {
                        errorMessage = "Usuario no es activo";
                    }
                }
            }
        }
        log.error("Error: {}", errorMessage);
        throw new RuntimeException(errorMessage);
    }

    public String changePassword(String userName, String oldPassword, String newPassword) {
        User user = this.userRepository.findByUser(userName);
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado.");
        }
        String md5OldPassword = DigestUtils.md5Hex(oldPassword);
        if (!user.getPassword().equals(md5OldPassword)) {
            throw new RuntimeException("La contraseña actual es incorrecta.");
        }
        String md5NewPassword = DigestUtils.md5Hex(newPassword);
        user.setPassword(md5NewPassword);
        user.setFirstLogin(false);
        this.userRepository.save(user);
        String logMessage = String.format("Contraseña actualizada para el usuario: %s. Nueva contraseña: %s", userName,
                newPassword);
        log.info(logMessage);
        return logMessage;
    }

    public void generatePassword(String userName) {
        User user = this.userRepository.findByUser(userName);
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado.");
        }
        String password = generateRandomPassword();
        user.setPassword(DigestUtils.md5Hex(password));
        this.userRepository.save(user);
        log.info("Contraseña generada para el usuario {}: {}", userName, password);
    }

    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public String resetPassword(String userName, String email) {
        User user = this.userRepository.findByUser(userName);
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado.");
        }
        if (!user.getEmail().equals(email)) {
            log.error("El correo electrónico no coincide para el usuario: {}", userName);
            throw new RuntimeException("Correo electrónico incorrecto.");
        }
        String newPassword = generateRandomPassword();
        user.setPassword(DigestUtils.md5Hex(newPassword));
        this.userRepository.save(user);
        String logMessage = String.format("Contraseña reseteada para el usuario: %s. Nueva contraseña: %s", userName,
                newPassword);
        log.info(logMessage);
        user.setMessage(logMessage);
        return logMessage;
    }

}