package com.auth.demo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserDTO {

	
    public UpdateUserDTO() {
		super();
	}

    
	public UpdateUserDTO(@NotBlank(message = "Le prénom est obligatoire.") @Size(min = 2, max = 50) String firstName,
			@NotBlank(message = "Le nom est obligatoire.") @Size(min = 2, max = 50) String lastName,
			@NotBlank(message = "L'email est obligatoire.") @Email(message = "Email invalide.") String email,
			@Pattern(regexp = "^\\+?\\d{10,15}$", message = "Numéro de téléphone invalide.") String phoneNumber,
			String address, String gender, LocalDate dateOfBirth) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
	}


	@NotBlank(message = "Le prénom est obligatoire.")
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire.")
    @Size(min = 2, max = 50)
    private String lastName;

    @NotBlank(message = "L'email est obligatoire.")
    @Email(message = "Email invalide.")
    private String email;

    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "Numéro de téléphone invalide.")
    private String phoneNumber;

    private String address;

    private String gender;

    private LocalDate dateOfBirth;

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


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
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
    
    
}
