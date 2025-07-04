package com.auth.demo.service;

import java.util.List;
import java.util.Optional;

import com.auth.demo.entity.AppRole;

public interface RoleService {
	AppRole addNewRole(AppRole appRole);
    AppRole updateRole(Long id, AppRole roleRequest);
    void deleteRole(Long id);
    List<AppRole> getAllRoles();
    AppRole findRoleByName(String roleName);
    Optional<AppRole> findRoleById(Long id);
}
