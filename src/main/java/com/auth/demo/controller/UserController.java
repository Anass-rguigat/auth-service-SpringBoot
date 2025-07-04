package com.auth.demo.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.auth.demo.dto.RoleUserForm;
import com.auth.demo.dto.UpdateUserDTO;
import com.auth.demo.dto.addRoleToUser;
import com.auth.demo.entity.AppUser;
import com.auth.demo.service.AccountService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    public UserController(AccountService accountService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    
    @GetMapping
    public ResponseEntity<?> listUsers() {
        try {
            List<AppUser> users = accountService.listUsers();
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Liste des utilisateurs récupérée avec succès.",
                "data", users
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "status", "error",
                "message", "Erreur lors de la récupération de la liste des utilisateurs."
            ));
        }
    }

    // Get user by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<AppUser> optionalUser = accountService.findUserById(id);
        
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("status", "error", "message", "Utilisateur non trouvé."));
        }
    }


    // Update user (except password)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
        @PathVariable Long id,
        @Valid @RequestBody UpdateUserDTO dto
    ) {
        try {
            AppUser updated = accountService.updateUser(id, dto);

            Map<String, Object> data = new HashMap<>();
            data.put("userId", updated.getId());
            data.put("username", updated.getUsername());

            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Utilisateur mis à jour avec succès.",
                "data", data
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "status", "error",
                "errorCode", 404,
                "message", e.getMessage()
            ));
        }
    }


    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            accountService.deleteUserById(id);
            response.put("status", "success");
            response.put("message", "Utilisateur supprimé avec succès.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erreur lors de la suppression de l'utilisateur.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping(path = "/addRoleToUser")
    public ResponseEntity<Map<String, Object>> addRoleToUserById(@Valid @RequestBody addRoleToUser form) {
        Map<String, Object> response = new HashMap<>();

        try {
            accountService.addRoleToUserById(form.getUserId(), form.getRoleId());

            response.put("status", "success");
            response.put("message", "Le rôle avec ID '" + form.getRoleId() + "' a été ajouté à l'utilisateur avec ID '" + form.getUserId() + "' avec succès.");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("errorCode", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erreur lors de l'ajout du rôle à l'utilisateur : " + e.getMessage());
            response.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/{userId}/roles/detach")
    public ResponseEntity<Map<String, Object>> detachRolesFromUser(
            @PathVariable Long userId,
            @RequestBody List<Long> roleIds) {

        Map<String, Object> response = new HashMap<>();
        try {
            accountService.detachRolesFromUser(userId, roleIds);
            response.put("status", "success");
            response.put("message", "Rôles détachés de l'utilisateur avec succès.");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("errorCode", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erreur lors de la dissociation des rôles.");
            response.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    


}

