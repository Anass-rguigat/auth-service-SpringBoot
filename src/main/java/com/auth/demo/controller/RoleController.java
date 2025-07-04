package com.auth.demo.controller;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.auth.demo.dto.addPermissionToRole;
import com.auth.demo.dto.addRoleToUser;
import com.auth.demo.entity.AppRole;
import com.auth.demo.service.AccountService;
import com.auth.demo.service.RoleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;
    private final AccountService accountService;

    public RoleController(RoleService roleService, AccountService accountService) {
        this.roleService = roleService;
        this.accountService = accountService;
    }

    // Ajouter un rôle
    @PostMapping
    public ResponseEntity<Map<String, Object>> saveRole(@Valid @RequestBody AppRole appRole) {
        Map<String, Object> response = new HashMap<>();
        try {
            AppRole savedRole = roleService.addNewRole(appRole);

            Map<String, Object> data = new HashMap<>();
            data.put("roleId", savedRole.getId());
            data.put("roleName", savedRole.getRoleName());

            response.put("status", "success");
            response.put("message", "Rôle ajouté avec succès.");
            response.put("data", data);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("errorCode", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erreur lors de l'ajout du rôle.");
            response.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Ajouter une permission à un rôle (reste dans AccountService)
    @PostMapping("/addPermissionToRole")
    public ResponseEntity<Map<String, Object>> addPermissionToRoleById(@Valid @RequestBody addPermissionToRole form) {
        Map<String, Object> response = new HashMap<>();
        try {
            accountService.addPermissionToRoleById(form.getRoleId(), form.getPermissionId());

            response.put("status", "success");
            response.put("message", "Permission avec ID '" + form.getPermissionId() + "' ajoutée au rôle avec ID '" + form.getRoleId() + "' avec succès.");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("errorCode", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erreur lors de l'ajout de la permission au rôle.");
            response.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Récupérer tous les rôles
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllRoles() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<AppRole> roles = roleService.getAllRoles();

            response.put("status", "success");
            response.put("data", roles);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erreur lors de la récupération des rôles.");
            response.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Mettre à jour un rôle
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateRole(@PathVariable Long id, @RequestBody AppRole roleRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            AppRole updatedRole = roleService.updateRole(id, roleRequest);

            Map<String, Object> data = new HashMap<>();
            data.put("roleId", updatedRole.getId());
            data.put("roleName", updatedRole.getRoleName());

            response.put("status", "success");
            response.put("message", "Rôle mis à jour avec succès.");
            response.put("data", data);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("errorCode", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erreur lors de la mise à jour du rôle.");
            response.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Supprimer un rôle
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteRole(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            roleService.deleteRole(id);
            response.put("status", "success");
            response.put("message", "Rôle supprimé avec succès.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("errorCode", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erreur lors de la suppression du rôle.");
            response.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/{roleId}/permissions/detach")
    public ResponseEntity<Map<String, Object>> detachPermissionsFromRole(
            @PathVariable Long roleId,
            @RequestBody List<Long> permissionIds) {

        Map<String, Object> response = new HashMap<>();
        try {
            accountService.detachPermissionsFromRole(roleId, permissionIds);
            response.put("status", "success");
            response.put("message", "Permissions détachées du rôle avec succès.");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("errorCode", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erreur lors de la dissociation des permissions.");
            response.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getRoleById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<AppRole> roleOpt = roleService.findRoleById(id);
            if (roleOpt.isEmpty()) {
                response.put("status", "error");
                response.put("message", "Rôle introuvable.");
                response.put("errorCode", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            response.put("status", "success");
            response.put("data", roleOpt.get());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erreur lors de la récupération du rôle.");
            response.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Récupérer un rôle par nom
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> getRoleByName(@RequestParam String roleName) {
        Map<String, Object> response = new HashMap<>();
        try {
            AppRole role = roleService.findRoleByName(roleName);
            if (role == null) {
                response.put("status", "error");
                response.put("message", "Rôle introuvable.");
                response.put("errorCode", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            response.put("status", "success");
            response.put("data", role);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erreur lors de la récupération du rôle.");
            response.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
