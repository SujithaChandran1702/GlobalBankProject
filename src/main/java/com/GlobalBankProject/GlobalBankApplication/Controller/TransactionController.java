package com.GlobalBankProject.GlobalBankApplication.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.GlobalBankProject.GlobalBankApplication.Model.Transaction;
import com.GlobalBankProject.GlobalBankApplication.Model.TransactionResponseDTO;
import com.GlobalBankProject.GlobalBankApplication.Service.TransactionService;

@RestController
@RequestMapping("/v1/transactioncontroller")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@GetMapping("/alltransaction")
	public List<Transaction> getAllTransaction() {
		return transactionService.getAllTransactions();
	}

	@PostMapping("/savetransaction")
	public Transaction saveTransaction(@RequestBody Transaction transaction) {
		return transactionService.saveTransaction(transaction);
	}

	@GetMapping("/history")
	public List<TransactionResponseDTO> getFilteredTransactions(@RequestParam String accountNumber,
			@RequestParam(required = false) String search, @RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate) {

		LocalDateTime startDateTime = null;
		LocalDateTime endDateTime = null;
		try {
			if (startDate != null && !startDate.isEmpty()) {
				LocalDate start = LocalDate.parse(startDate);
				startDateTime = start.atStartOfDay();
			}
			if (endDate != null && !endDate.isEmpty()) {
				LocalDate end = LocalDate.parse(endDate);
				endDateTime = end.atTime(LocalTime.MAX);
			}
		} catch (Exception e) {
			System.err.println("Date parsing error: " + e.getMessage());
		}

		return transactionService.getTransactionsForAccount(accountNumber, search, startDateTime, endDateTime);
	}

	@PostMapping("/transfer")
	public ResponseEntity<?> performTransaction(@RequestBody TransactionResponseDTO request) {
		Transaction tx = new Transaction();
		tx.setFromAccount(request.getFromAccount());
		tx.setFromAccountHolderName(request.getFromAccountHolderName());
		tx.setToAccount(request.getToAccount());
		tx.setToAccountHolderName(request.getToAccountHolderName());
		tx.setAmount(request.getAmount());
		tx.setAccountType(request.getAccountType());
		tx.setBalance(request.getBalance()); // Optional, or calculate dynamically

		transactionService.saveTransaction(tx);
		return ResponseEntity.ok("Transaction successful");
	}

}
