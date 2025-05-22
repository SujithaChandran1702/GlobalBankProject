package com.GlobalBankProject.GlobalBankApplication.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegistrationResponseDTO {
	private String username;
	private String email;
	private String phoneNumber;
	private String panNumber;
	private String accountNumber;
	private String ifscCode;
	private String accountType;
	private Double balance;

}
