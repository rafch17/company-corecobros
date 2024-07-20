package com.banquito.corecobros.companydoc.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banquito.corecobros.companydoc.dto.PasswordDTO;
import com.banquito.corecobros.companydoc.dto.UserDTO;
import com.banquito.corecobros.companydoc.service.UserService;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(this.service.obtainAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        UserDTO user = this.service.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserDTO dto) {
        this.service.create(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable String id, @RequestBody UserDTO dto) {
        this.service.updateUser(id, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody PasswordDTO dto) {
        try {
            return ResponseEntity.ok(this.service.login(dto));
        } catch (RuntimeException rte) {
            rte.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/changePassword")
    public ResponseEntity<Void> changePassword(@RequestBody PasswordDTO dto) {
        System.out.println("Va a Cambiar clave para: " + dto.toString());
        try {
            this.service.changePassword(dto);
            return ResponseEntity.ok().build();
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/generatePassword/{user}")
    public ResponseEntity<Void> generatePassword(@PathVariable("user") String user) {
        System.out.println("Va a generar clave para: " + user);
        try {
            this.service.generatePassword(user);
            return ResponseEntity.ok().build();
        } catch (RuntimeException rte) {
            return ResponseEntity.notFound().build();
        }
    }

}
