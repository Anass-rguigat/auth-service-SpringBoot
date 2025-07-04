package com.auth.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.demo.dto.UpdateUserDTO;
import com.auth.demo.entity.AppRole;
import com.auth.demo.entity.AppUser;
import com.auth.demo.entity.Permission;
import com.auth.demo.repository.AppRoleRepository;
import com.auth.demo.repository.AppUserRepository;
import com.auth.demo.repository.PermissionRepository;

import jakarta.transaction.Transactional;
@Service
@Transactional
public class AccountServiceImpl implements AccountService{

	private AppUserRepository appUserRepository;
	private AppRoleRepository appRoleRepository;
	private PasswordEncoder passwordEncoder;
	private PermissionRepository permissionRepository;
	public AccountServiceImpl(AppUserRepository appUserRepository,AppRoleRepository appRoleRepository, PasswordEncoder passwordEncoder, PermissionRepository permissionRepository) {
		this.appUserRepository = appUserRepository;
		this.appRoleRepository = appRoleRepository;
		this.passwordEncoder = passwordEncoder;
		this.permissionRepository = permissionRepository;
	}
	
	

	@Override
	public void addRoleToUserById(Long userId, Long roleId) {
	    AppUser user = appUserRepository.findById(userId)
	        .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));

	    AppRole role = appRoleRepository.findById(roleId)
	        .orElseThrow(() -> new IllegalArgumentException("Rôle introuvable."));

	    if (user.getAppRoles().contains(role)) {
	        throw new IllegalArgumentException("L'utilisateur possède déjà ce rôle.");
	    }

	    user.getAppRoles().add(role);
	    appUserRepository.save(user);
	}

	@Override
    public void detachRolesFromUser(Long userId, List<Long> roleIds) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));

        for (Long roleId : roleIds) {
            AppRole role = appRoleRepository.findById(roleId)
                    .orElseThrow(() -> new IllegalArgumentException("Rôle introuvable : " + roleId));

            if (user.getAppRoles().contains(role)) {
                user.getAppRoles().remove(role);
            }
        }
        appUserRepository.save(user);
    }


	@Override
	public AppUser loadUserByUsername(String username) {
		
		return appUserRepository.findByUsername(username);
	}

	@Override
	public List<AppUser> listUsers() {
		
		return appUserRepository.findAll();
	}
	
	
	
	@Override
	public void addPermissionToRoleById(Long roleId, Long permissionId) {
	    AppRole role = appRoleRepository.findById(roleId)
	        .orElseThrow(() -> new IllegalArgumentException("Rôle introuvable."));

	    Permission permission = permissionRepository.findById(permissionId)
	        .orElseThrow(() -> new IllegalArgumentException("Permission introuvable."));

	    if (role.getPermissions().contains(permission)) {
	        throw new IllegalArgumentException("Le rôle possède déjà cette permission.");
	    }

	    role.getPermissions().add(permission);
	    appRoleRepository.save(role);
	}

	@Override
	public void detachPermissionsFromRole(Long roleId, List<Long> permissionIds) {
	    AppRole role = appRoleRepository.findById(roleId)
	            .orElseThrow(() -> new IllegalArgumentException("Rôle introuvable."));

	    for (Long permissionId : permissionIds) {
	        Permission permission = permissionRepository.findById(permissionId)
	                .orElseThrow(() -> new IllegalArgumentException("Permission introuvable : " + permissionId));

	        if (!role.getPermissions().contains(permission)) {
	            throw new IllegalArgumentException("Le rôle ne possède pas la permission avec l'id " + permissionId);
	        }

	        role.getPermissions().remove(permission);
	    }
	    appRoleRepository.save(role);
	}



	

	
	@Override
	public AppUser addNewUser(AppUser appUser) {
	    if (appUserRepository.findByUsername(appUser.getUsername()) != null) {
	        throw new IllegalArgumentException("Le nom d'utilisateur existe déjà.");
	    }
	    String pw = appUser.getPassword();
	    appUser.setPassword(passwordEncoder.encode(pw));
	    return appUserRepository.save(appUser);
	}
	
	@Override
    public Optional<AppUser> findUserById(Long id) {
        return appUserRepository.findById(id);
    }

	@Override
	public AppUser updateUser(Long id, UpdateUserDTO dto) {
	    AppUser user = appUserRepository.findById(id)
	        .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé."));

	    String uniqueUsername = generateUniqueUsername(dto.getFirstName(), dto.getLastName(), id);
	    user.setUsername(uniqueUsername);

	    user.setFirstName(dto.getFirstName());
	    user.setLastName(dto.getLastName());
	    user.setEmail(dto.getEmail());
	    user.setPhoneNumber(dto.getPhoneNumber());
	    user.setAddress(dto.getAddress());
	    user.setGender(dto.getGender());
	    user.setDateOfBirth(dto.getDateOfBirth());

	    return appUserRepository.save(user);
	}


    @Override
    public void deleteUserById(Long id) {
        if (!appUserRepository.existsById(id)) {
            throw new IllegalArgumentException("Utilisateur introuvable.");
        }
        appUserRepository.deleteById(id);
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect.");
        }

        if (newPassword == null || !newPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
            throw new IllegalArgumentException("Le nouveau mot de passe doit contenir au moins 8 caractères, avec au moins une lettre et un chiffre.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        appUserRepository.save(user);
    }


    
    private String generateUniqueUsername(String firstName, String lastName, Long currentUserId) {
        String base = (firstName + "." + lastName).toLowerCase().replaceAll("\\s+", "");
        String candidate = base;
        int counter = 1;

        while (true) {
            AppUser existing = appUserRepository.findByUsername(candidate);
            if (existing == null || (currentUserId != null && existing.getId().equals(currentUserId))) {
                break;
            }
            
            if (counter <= 999) {
                candidate = base + counter;
            } else {
                candidate = base + (char) ('a' + (counter - 1000));
            }
            counter++;
        }

        return candidate;
    }

}
