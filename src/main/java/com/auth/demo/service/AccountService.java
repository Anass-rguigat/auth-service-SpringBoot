package com.auth.demo.service;

import java.util.List;
import java.util.Optional;

import com.auth.demo.dto.UpdateUserDTO;
import com.auth.demo.entity.AppRole;
import com.auth.demo.entity.AppUser;

public interface AccountService {

	
	
	void addRoleToUserById(Long userId, Long roleId);
	void detachRolesFromUser(Long userId, List<Long> roleIds);

	
	AppUser loadUserByUsername(String username);
	
	List<AppUser> listUsers();
	
	void addPermissionToRoleById(Long roleId, Long permissionId);
    void detachPermissionsFromRole(Long roleId, List<Long> permissionIds);

	// New CRUD methods
    AppUser updateUser(Long id, UpdateUserDTO updatedUser);
    void deleteUserById(Long id);
    void changePassword(Long userId, String oldPassword, String newPassword);
    AppUser addNewUser(AppUser appUser);
    Optional<AppUser> findUserById(Long id); 
}
