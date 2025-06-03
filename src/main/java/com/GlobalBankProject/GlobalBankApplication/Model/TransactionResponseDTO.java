package com.GlobalBankProject.GlobalBankApplication.Model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO {

	private Long id;
	private String fromAccount;
	private String fromAccountHolderName;
	private String toAccount;
	private String toAccountHolderName;
	private String accountType;
	private double amount;
	private double balance;
	private LocalDateTime timestamp;
	private String transactionType;
	private LocalDateTime date;

}
