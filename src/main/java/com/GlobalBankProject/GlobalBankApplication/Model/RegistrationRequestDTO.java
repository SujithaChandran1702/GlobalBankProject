package com.GlobalBankProject.GlobalBankApplication.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegistrationRequestDTO {
	  @NotBlank(message = "Username is required")
	    private String userName;

	    @Email(message = "Email format is invalid")
	    @NotBlank(message = "Email is required")
	    private String email;

	    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
	             message = "Password must be strong")
	    private String password;

	    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
	    private String phoneNumber;

	    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]$", message = "Invalid PAN format")
	    private String panNumber;
}
