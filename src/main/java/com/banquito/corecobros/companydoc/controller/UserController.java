package com.banquito.corecobros.companydoc.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        List<UserDTO> users = this.service.obtainAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/unique/{uniqueID}")
    public ResponseEntity<UserDTO> getUserByUniqueID(@PathVariable String uniqueID) {
        UserDTO user = service.getUserByUniqueID(uniqueID);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/company/name/{user}")
    public ResponseEntity<String> getCompanyNameByUser(@PathVariable String user) {
        try {
            String companyName = service.getCompanyNameByUser(user);
            return ResponseEntity.ok(companyName);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@RequestParam String companyId, @RequestParam String uniqueID,
            @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email,
            @RequestParam String role, @RequestParam String status, @RequestParam String userType) {
        try {
            service.createUser(companyId, uniqueID, firstName, lastName, email, role, status, userType);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{uniqueID}")
    public ResponseEntity<Void> updateUser(@PathVariable String uniqueID, @RequestBody UserDTO dto) {
        try {
            this.service.updateUser(uniqueID, dto);
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
    public ResponseEntity<UserDTO> login(@RequestBody PasswordDTO dto) {
        try {
            UserDTO user = this.service.login(dto);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody PasswordDTO dto) {
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
