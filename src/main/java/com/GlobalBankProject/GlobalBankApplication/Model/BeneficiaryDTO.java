package com.GlobalBankProject.GlobalBankApplication.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryDTO {
	private String name;
	private String accountNumber;
	private String ifscCode;
	private String addedByUserName;
}
