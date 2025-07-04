package com.auth.demo.entity;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppRole {
	
	

    public AppRole() {
		super();
	}

	public AppRole(Long id,
			@NotBlank(message = "Le nom du rôle est obligatoire.") @Size(min = 3, max = 30, message = "Le nom du rôle doit contenir entre 3 et 30 caractères.") String roleName,
			Collection<Permission> permissions) {
		super();
		this.id = id;
		this.roleName = roleName;
		this.permissions = permissions;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Le nom du rôle est obligatoire.")
    @Size(min = 3, max = 30, message = "Le nom du rôle doit contenir entre 3 et 30 caractères.")
    private String roleName;

    @ManyToMany(mappedBy = "appRoles")
    @JsonIgnore
    private Collection<AppUser> users = new ArrayList<>();
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "app_role_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Collection<Permission> permissions = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Collection<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Collection<Permission> permissions) {
		this.permissions = permissions;
	}

	public Collection<AppUser> getUsers() {
		return users;
	}

	public void setUsers(Collection<AppUser> users) {
		this.users = users;
	}
    
    
    
}
