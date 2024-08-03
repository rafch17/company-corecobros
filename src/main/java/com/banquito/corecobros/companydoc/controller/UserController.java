package com.banquito.corecobros.companydoc.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banquito.corecobros.companydoc.dto.UserDTO;
import com.banquito.corecobros.companydoc.model.User;
import com.banquito.corecobros.companydoc.service.UserService;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST,
        RequestMethod.PUT })
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = this.service.obtainAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{uniqueId}")
    public ResponseEntity<UserDTO> getUserByUniqueId(@PathVariable String uniqueId) {
        UserDTO user = service.getUserByUniqueId(uniqueId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("companyName/{user}")
    public ResponseEntity<String> getCompanyNameByUser(@PathVariable String user) {
        try {
            String companyName = service.getCompanyNameByUser(user);
            return ResponseEntity.ok(companyName);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/")
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        try {
            User user = service.createUser(
                    userDTO.getCompanyId(),
                    userDTO.getFirstName(),
                    userDTO.getLastName(),
                    userDTO.getEmail(),
                    userDTO.getRole(),
                    userDTO.getStatus(),
                    userDTO.getUserType());
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{uniqueId}")
    public ResponseEntity<Void> updateUser(@PathVariable String uniqueId, @RequestBody User user) {
        try {
            this.service.updateUser(uniqueId, user);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<UserDTO>> getUsersByCompanyId(@PathVariable String companyId) {
        List<UserDTO> users = service.getUsersByCompanyId(companyId);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserDTO dto) {
        try {
            UserDTO user = this.service.login(dto);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/change-password/{uniqueId}")
    public ResponseEntity<Void> changePassword(@RequestBody UserDTO dto) {
        try {
            this.service.changePassword(dto);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam String userName, @RequestParam String email) {
        try {
            this.service.resetPassword(userName, email);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/validate-code")
    public ResponseEntity<Boolean> validateResetCode(@RequestParam String userName, @RequestParam String resetCode) {
        try {
            boolean isValid = service.validateResetCode(userName, resetCode);
            return ResponseEntity.ok(isValid);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
