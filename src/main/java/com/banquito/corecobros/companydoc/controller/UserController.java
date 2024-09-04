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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST,
        RequestMethod.PUT })
@RestController
@RequestMapping("/company-microservice/api/v1/users")
@Tag(name = "User", description = "Endpoints for managing users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = this.service.obtainAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{uniqueId}")
    @Operation(summary = "Get user by uniqueId", description = "Retrieve a user by its uniqueId")
    public ResponseEntity<UserDTO> getUserByUniqueId(@PathVariable String uniqueId) {
        UserDTO user = service.getUserByUniqueId(uniqueId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("company-name/{user}")
    @Operation(summary = "Get company by user", description = "Retrieve a company by its user")
    public ResponseEntity<String> getCompanyNameByUser(@PathVariable String user) {
        try {
            String companyName = service.getCompanyNameByUser(user);
            return ResponseEntity.ok(companyName);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/")
    @Operation(summary = "Create a user", description = "Create a new user")
    public ResponseEntity<User> createUser(@RequestBody User userRequest) {
        try {
            User createdUser = service.createUser(
                    userRequest.getCompanyId(),
                    userRequest.getFirstName(),
                    userRequest.getLastName(),
                    userRequest.getEmail(),
                    userRequest.getRole(),
                    userRequest.getStatus(),
                    userRequest.getUserType());
            return ResponseEntity.ok(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{uniqueId}")
    @Operation(summary = "Update a user", description = "Update an existing user")
    public ResponseEntity<Void> updateUser(@PathVariable String uniqueId, @RequestBody User user) {
        try {
            this.service.updateUser(uniqueId, user);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get users by companyId", description = "Retrieve a user by companyId")
    public ResponseEntity<List<UserDTO>> getUsersByCompanyId(@PathVariable String companyId) {
        List<UserDTO> users = service.getUsersByCompanyId(companyId);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user with credentials")
    public ResponseEntity<?> login(@RequestBody User dto) {
        try {
            User loggedUser = service.login(dto);
            return ResponseEntity.ok(loggedUser);
        } catch (RuntimeException e) {
            if ("Primera vez que inicia sesión. Debe cambiar su contraseña.".equals(e.getMessage())) {
                return ResponseEntity.status(403).body(e.getMessage());
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/auth")
    @Operation(summary = "auth user", description = "Authenticate user with credentials and return JWT")
    public ResponseEntity<?> login2(@RequestBody User dto) {
        try {
            User loggedUser = service.login2(dto);
            String token = loggedUser.getMessage(); // El token JWT está almacenado en el campo message
            return ResponseEntity.ok().header("Authorization", token).body(loggedUser);
        } catch (RuntimeException e) {
            if ("Primera vez que inicia sesión. Debe cambiar su contraseña.".equals(e.getMessage())) {
                return ResponseEntity.status(403).body(e.getMessage());
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/change-password")
    @Operation(summary = "Change Password", description = "Change password for a user")
    public ResponseEntity<String> changePassword(@RequestParam String userName, @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        try {
            String message = this.service.changePassword(userName, oldPassword, newPassword);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/reset-password")
    @Operation(summary = "Reset Password", description = "Reset password from user")
    public ResponseEntity<String> resetPassword(@RequestParam String userName, @RequestParam String email) {
        try {
            String message = this.service.resetPassword(userName, email);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
