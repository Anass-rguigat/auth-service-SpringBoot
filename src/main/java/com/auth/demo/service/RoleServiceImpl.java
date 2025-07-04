package com.auth.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.auth.demo.entity.AppRole;
import com.auth.demo.entity.AppUser;
import com.auth.demo.repository.AppRoleRepository;
import com.auth.demo.repository.AppUserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final AppRoleRepository appRoleRepository;
    private final AppUserRepository appUserRepository;

    public RoleServiceImpl(AppRoleRepository appRoleRepository, AppUserRepository appUserRepository) {
        this.appRoleRepository = appRoleRepository;
        this.appUserRepository = appUserRepository;
    }

    @Override
    public AppRole addNewRole(AppRole appRole) {
        if (appRoleRepository.findByRoleName(appRole.getRoleName()) != null) {
            throw new IllegalArgumentException("Le rôle existe déjà.");
        }
        return appRoleRepository.save(appRole);
    }

    @Override
    public AppRole updateRole(Long id, AppRole roleRequest) {
        AppRole role = appRoleRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Rôle introuvable."));

        role.setRoleName(roleRequest.getRoleName());
        return appRoleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        AppRole role = appRoleRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Rôle introuvable."));

        // Supprimer les associations entre ce rôle et les utilisateurs
        if (role.getUsers() != null && !role.getUsers().isEmpty()) {
            for (AppUser user : role.getUsers()) {
                user.getAppRoles().remove(role);
                appUserRepository.save(user); // <<== Sauvegarde mise à jour
            }
        }

        // Supprimer le rôle
        appRoleRepository.delete(role);
    }



    @Override
    public List<AppRole> getAllRoles() {
        List<AppRole> roles = appRoleRepository.findAll();
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Aucun rôle trouvé.");
        }
        return roles;
    }

    @Override
    public AppRole findRoleByName(String roleName) {
        AppRole role = appRoleRepository.findByRoleName(roleName);
        if (role == null) {
            throw new IllegalArgumentException("Rôle avec le nom '" + roleName + "' introuvable.");
        }
        return role;
    }

    @Override
    public Optional<AppRole> findRoleById(Long id) {
        Optional<AppRole> role = appRoleRepository.findById(id);
        if (role.isEmpty()) {
            throw new IllegalArgumentException("Rôle avec l'ID '" + id + "' introuvable.");
        }
        return role;
    }

}
