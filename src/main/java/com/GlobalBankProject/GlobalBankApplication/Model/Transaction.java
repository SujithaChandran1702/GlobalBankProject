package com.GlobalBankProject.GlobalBankApplication.Model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String fromAccount;
	private String fromAccountHolderName;

	private String toAccount;
	private String toAccountHolderName;

	private String accountType;
	private double amount;
	private double balance;
//	private String transactionType;

	private LocalDateTime timestamp;

	@PrePersist
	public void setTimestamp() {
		this.timestamp = LocalDateTime.now();
	}
}
