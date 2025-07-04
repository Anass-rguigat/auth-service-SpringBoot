package com.auth.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class addPermissionToRole {
	
	
	public addPermissionToRole() {
		super();
	}

	public addPermissionToRole(@NotNull(message = "Le rôle est obligatoire.") Long roleId,
			@NotNull(message = "La permission est obligatoire.") Long permissionId) {
		super();
		this.roleId = roleId;
		this.permissionId = permissionId;
	}

	@NotNull(message = "Le rôle est obligatoire.")
	private Long roleId;

	@NotNull(message = "La permission est obligatoire.")
    private Long permissionId;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}
	
    
    
    
}