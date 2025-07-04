package com.auth.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class addRoleToUser {
    
	
	public addRoleToUser() {
		super();
	}
	public addRoleToUser(@NotNull(message = "Le rôle est obligatoire.") Long userId,
			@NotNull(message = "la permission est obligatoire.") Long roleId) {
		super();
		this.userId = userId;
		this.roleId = roleId;
	}
	@NotNull(message = "Le nom d'utilisateur est obligatoire.")
    private Long userId;
	@NotNull(message = "Le rôle est obligatoire.")
    private Long roleId;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	
    
    
}
