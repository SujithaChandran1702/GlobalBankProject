package com.GlobalBankProject.GlobalBankApplication.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO {
	private String userName;
	private String accountNumber;
	private double balance;
	private String accountType;
	private String ifscCode;
	private String panNumber;
	private String phoneNumber;
	private String email;

}
