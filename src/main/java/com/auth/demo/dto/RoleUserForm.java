package com.auth.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class RoleUserForm {
	@NotBlank(message = "Le nom d'utilisateur est obligatoire.")
    private String username;

    @NotBlank(message = "Le nom du rôle est obligatoire.")
    private String roleName;

    public RoleUserForm() {}

    public RoleUserForm(String username, String roleName) {
        this.username = username;
        this.roleName = roleName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
