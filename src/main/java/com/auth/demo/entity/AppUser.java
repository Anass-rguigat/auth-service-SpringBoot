package com.auth.demo.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {

	
	
    public AppUser() {
		super();
	}

	public AppUser(Long id, @NotBlank(message = "Le prénom est obligatoire.") @Size(min = 2, max = 50) String firstName,
			@NotBlank(message = "Le nom est obligatoire.") @Size(min = 2, max = 50) String lastName, String username,
			@NotBlank(message = "L'email est obligatoire.") @Email(message = "Email invalide.") String email,
			@NotBlank(message = "Le mot de passe est obligatoire.") @Size(min = 8, max = 100) @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "Le mot de passe doit contenir une majuscule, une minuscule et un chiffre.") String password,
			@Pattern(regexp = "^\\+?\\d{10,15}$", message = "Numéro de téléphone invalide.") String phoneNumber,
			String address, String gender, LocalDate dateOfBirth, Instant createdAt, Collection<AppRole> appRoles) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.createdAt = createdAt;
		this.appRoles = appRoles;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le prénom est obligatoire.")
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire.")
    @Size(min = 2, max = 50)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "L'email est obligatoire.")
    @Email(message = "Email invalide.")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire.")
    @Size(min = 8, max = 100)
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
        message = "Le mot de passe doit contenir une majuscule, une minuscule et un chiffre."
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "Numéro de téléphone invalide.")
    private String phoneNumber;

    private String address;

    private String gender; 

    private LocalDate dateOfBirth;

    private Instant createdAt = Instant.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "app_user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Collection<AppRole> appRoles = new ArrayList<>();

    
   
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Collection<AppRole> getAppRoles() {
		return appRoles;
	}

	public void setAppRoles(Collection<AppRole> appRoles) {
		this.appRoles = appRoles;
	}

	
    
    
}
