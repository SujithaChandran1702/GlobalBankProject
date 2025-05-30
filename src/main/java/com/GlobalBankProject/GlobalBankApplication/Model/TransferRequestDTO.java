package com.GlobalBankProject.GlobalBankApplication.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestDTO {

	private String fromUserName;
    private String toAccountNumber;
    private double amount;

}
