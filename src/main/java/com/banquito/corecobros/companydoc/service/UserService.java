package com.banquito.corecobros.companydoc.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import com.banquito.corecobros.companydoc.dto.PasswordDTO;
import com.banquito.corecobros.companydoc.dto.UserDTO;
import com.banquito.corecobros.companydoc.model.User;
import com.banquito.corecobros.companydoc.repository.UserRepository;
import com.banquito.corecobros.companydoc.util.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

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

    public UserDTO getUserById(String id) {
        log.info("Va a buscar el usuario con ID: {}", id);
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            log.info("No se encontró el usuario con ID: {}", id);
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

    public void create(UserDTO dto) {
        if (this.userRepository.findByUser(dto.getUser()) != null) {
            throw new RuntimeException("usuario repetido");
        }
        log.info("Va a registrar un usuario: {}", dto);
        User user = this.mapper.toPersistence(dto);
        user.setStatus("BLO");
        user.setCreateDate(LocalDate.now());
        String rawPassword = "CambiarClave1";
        user.setPassword(DigestUtils.md5Hex(rawPassword));
        log.info("Usuario a registrar: {}", user);
        user = this.userRepository.save(user);
        log.info("Se creó el usuario: {}", user);
    }

    public void updateUser(String id, UserDTO dto) {
        log.info("Va a actualizar el usuario con ID: {}", id);
        User user = this.mapper.toPersistence(dto);
        user.setId(id);
        user = this.userRepository.save(user);
        log.info("Se actualizó el usuario: {}", user);
    }

    public UserDTO login(PasswordDTO dto) {
        String errorMessage = "Usuario o contraseña incorrecta";
        if (dto.getUser() != null && dto.getPassword() != null && dto.getUser().length() > 3
                && dto.getPassword().length() == 32) {
            User user = this.userRepository.findByUser(dto.getUser());
            String md5 = DigestUtils.md5Hex(dto.getPassword());
            if (user.getPassword().equals(md5)) {
                user.setLastConnection(LocalDateTime.now());
                this.userRepository.save(user);
                if ("ACT".equals(user.getStatus())) {
                    return this.mapper.toDTO(user);
                } else {
                    errorMessage = "Usuario no es activo";
                }
            }
        }
        throw new RuntimeException(errorMessage);
    }

    public void changePassword(PasswordDTO userPassword) {
        User user = this.userRepository.findByUser(userPassword.getUser());
        if (user == null) {
            throw new RuntimeException("No existe el usuario: " + userPassword.getUser());
        }
        user.setPassword(userPassword.getPassword());
        this.userRepository.save(user);
    }

    public void generatePassword(String userName) {
        User user = this.userRepository.findByUser(userName);
        if (user == null) {
            throw new RuntimeException("No existe el usuario: " + userName);
        }
        // TODO: Generate password
        String password = "GenerarClave2";
        String md5Hex = DigestUtils.md5Hex(password);
        user.setPassword(md5Hex);
        this.userRepository.save(user);
    }

}
