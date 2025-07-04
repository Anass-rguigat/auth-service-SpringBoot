package com.auth.demo.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth.demo.dto.LoginRequestDTO;
import com.auth.demo.dto.addRoleToUser;
import com.auth.demo.dto.addPermissionToRole;
import com.auth.demo.dto.RoleUserForm;
import com.auth.demo.entity.AppRole;
import com.auth.demo.entity.AppUser;
import com.auth.demo.service.AccountService;
import com.auth.demo.service.AuthService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@RestController
public class AuthController {

    private final AuthService authService;
    private final AccountService accountService;

    public AuthController(AuthService authService, AccountService accountService) {
        this.authService = authService;
        this.accountService = accountService;
    }

    @PostMapping(path="/register")
    public ResponseEntity<Map<String, Object>> saveUser(@Valid @RequestBody AppUser appUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            AppUser savedUser = accountService.addNewUser(appUser);

            Map<String, Object> data = new HashMap<>();
            data.put("userId", savedUser.getId());
            data.put("username", savedUser.getUsername());

            response.put("status", "success");
            response.put("message", "Inscription réussie.");
            response.put("data", data);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());  // Message en français depuis le service
            response.put("errorCode", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Une erreur inattendue est survenue lors de l'inscription.");
            response.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



   


    



    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDTO loginRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> data = authService.login(
                loginRequest.getGrantType(),
                loginRequest.getUsername(),
                loginRequest.getPassword(),
                loginRequest.isWithRefreshToken(),
                loginRequest.getRefreshToken()
            );

            response.put("status", "success");
            response.put("message", "Connexion réussie.");
            response.put("data", data);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("errorCode", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Une erreur inattendue est survenue lors de la connexion.");
            response.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    
    
    

 // Change password
    @PostMapping("/{id}/changePassword")
    public ResponseEntity<Map<String, Object>> changePassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> passwords) {

        Map<String, Object> response = new HashMap<>();
        try {
            String oldPassword = passwords.get("oldPassword");
            String newPassword = passwords.get("newPassword");

            if (oldPassword == null || newPassword == null) {
                throw new IllegalArgumentException("Ancien et nouveau mot de passe sont requis.");
            }

            // Déplacement de la validation dans le service
            accountService.changePassword(id, oldPassword, newPassword);

            response.put("status", "success");
            response.put("message", "Mot de passe modifié avec succès.");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erreur lors de la modification du mot de passe.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    



}

