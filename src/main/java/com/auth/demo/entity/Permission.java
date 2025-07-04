package com.auth.demo.entity;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
	
	

    public Permission() {
		super();
	}

	public Permission(Long id,
			@NotBlank(message = "Le nom de la permission est obligatoire.") @Size(min = 3, max = 50, message = "Le nom de la permission doit contenir entre 3 et 50 caractères.") @Pattern(regexp = "^[A-Z_]+$", message = "Le nom de la permission doit être en majuscules et contenir uniquement des underscores (ex : VIEW_USERS).") String name) {
		super();
		this.id = id;
		this.name = name;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Le nom de la permission est obligatoire.")
    @Size(min = 3, max = 50, message = "Le nom de la permission doit contenir entre 3 et 50 caractères.")
    @Pattern(regexp = "^[A-Z_]+$", message = "Le nom de la permission doit être en majuscules et contenir uniquement des underscores (ex : VIEW_USERS).")
    private String name;
    
    @ManyToMany(mappedBy = "permissions")
    @JsonIgnore
    private Collection<AppRole> roles = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<AppRole> getRoles() {
		return roles;
	}

	public void setRoles(Collection<AppRole> roles) {
		this.roles = roles;
	}
    
	
    
    
}
