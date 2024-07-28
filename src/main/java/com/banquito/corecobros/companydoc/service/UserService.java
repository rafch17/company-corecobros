package com.banquito.corecobros.companydoc.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

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
        user.setEmail(email);
        user.setRole(role);
        user.setStatus(status);
        user.setUserType(userType);
        this.userRepository.save(user);
        log.info("Usuario creado con nombre de usuario: {}, contraseña: {}", userName, password);
        return user;
    }

    public void updateUser(String uniqueId, UserDTO dto) {
        log.info("Va a actualizar el usuario con ID: {}", uniqueId);
        User user = this.mapper.toPersistence(dto);
        user.setId(uniqueId);
        user = this.userRepository.save(user);
        log.info("Se actualizó el usuario: {}", user);
    }

    public List<UserDTO> getUsersByCompanyId(String companyId) {
        log.info("Va a retornar todos los usuarios para la compañía con ID: {}", companyId);
        List<User> users = this.userRepository.findByCompanyId(companyId);
        return users.stream().map(u -> this.mapper.toDTO(u)).collect(Collectors.toList());
    }

    public UserDTO login(UserDTO dto) {
        String errorMessage = "Usuario o contraseña incorrecta";
        System.out.println("UserDTO received: " + dto);
        
        if (dto.getUser() != null && dto.getPassword() != null && dto.getUser().length() > 3
                && dto.getPassword().length() > 5) {  
            System.out.println("Valid user and password length");
            User user = this.userRepository.findByUser(dto.getUser());
            
            if (user != null) {
                System.out.println("User found: " + user);
                String md5 = DigestUtils.md5Hex(dto.getPassword());
                System.out.println("MD5 of provided password: " + md5);
                if (user.getPassword().equals(md5)) {
                    user.setLastConnection(LocalDateTime.now());
                    this.userRepository.save(user);
                    
                    if ("ACT".equals(user.getStatus())) {
                        System.out.println("User is active");
                        return this.mapper.toDTO(user);
                    } else {
                        errorMessage = "Usuario no es activo";
                    }
                }
            }
        }
        System.out.println("Error: " + errorMessage);
        throw new RuntimeException(errorMessage);
    }    

    public void changePassword(UserDTO userDTO) {
        User user = this.userRepository.findByUser(userDTO.getUser());
        if (user == null) {
            throw new RuntimeException("No existe el usuario: " + userDTO.getUser());
        }
        if (!user.getPassword().equals(DigestUtils.md5Hex(userDTO.getPassword()))) {
            throw new RuntimeException("Contraseña actual incorrecta.");
        }
        user.setPassword(DigestUtils.md5Hex(userDTO.getPassword()));
        this.userRepository.save(user);
        Company company = this.companyRepository.findById(user.getCompanyId()).orElse(null);
        if (company != null) {
            log.info("Nombre de la empresa asociada al usuario: {}", company.getCompanyName());
        }
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

    public void resetPassword(String userName, String email) {
        User user = this.userRepository.findByUser(userName);
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado.");
        }
        if (!user.getEmail().equals(email)) {
            log.error("El correo electrónico no coincide para el usuario: {}", userName);
            throw new RuntimeException("Correo electrónico incorrecto.");
        }
        String resetCode = generate8DigitCode();
        user.setResetCode(resetCode);
        this.userRepository.save(user);
        log.info("Código de restablecimiento generado para el usuario {}: {}", userName, resetCode);
    }

    private String generate8DigitCode() {
        return String.format("%08d", (int) (Math.random() * 1000000));
    }

    public boolean validateResetCode(String userName, String resetCode) {
        User user = this.userRepository.findByUser(userName);
        if (user == null || !resetCode.equals(user.getResetCode())) {
            throw new RuntimeException("Código de restablecimiento inválido.");
        }
        return true;
    }

}