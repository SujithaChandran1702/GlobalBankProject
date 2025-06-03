package com.GlobalBankProject.GlobalBankApplication.Controller;

import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.GlobalBankProject.GlobalBankApplication.Model.Transaction;
import com.GlobalBankProject.GlobalBankApplication.Model.TransactionResponseDTO;
import com.GlobalBankProject.GlobalBankApplication.Service.PdfService;
import com.GlobalBankProject.GlobalBankApplication.Service.TransactionService;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/v1/transactioncontroller")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private PdfService pdfService;

	@GetMapping("/alltransaction")
	public List<Transaction> getAllTransaction() {
		return transactionService.getAllTransactions();
	}

	@PostMapping("/savetransaction")
	public Transaction saveTransaction(@RequestBody Transaction transaction) {
		return transactionService.saveTransaction(transaction);
	}

	@GetMapping("/history")
	public ResponseEntity<List<TransactionResponseDTO>> getTransactionHistory(@RequestParam String accountNumber,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		LocalDateTime startDateTime = null;
		LocalDateTime endDateTime = null;
		if (startDate != null) {
			startDateTime = startDate.atStartOfDay();
		}
		if (endDate != null) {
			endDateTime = endDate.atTime(23, 59, 59, 999999999);
		}
		List<TransactionResponseDTO> filteredTransactions = transactionService.getTransactionsForAccount(accountNumber,
				search, startDateTime, endDateTime);
		return ResponseEntity.ok(filteredTransactions);
	}

	@PostMapping("/transfer")
	public ResponseEntity<?> performTransaction(@RequestBody TransactionResponseDTO request) {
		double amount = request.getAmount();
		if (amount <= 0) {
			return ResponseEntity.badRequest().body("Amount must be greater than zero");
		}
		if (amount > 5000) {
			return ResponseEntity.badRequest().body("Transfers to new beneficiaries are limited to ₹5000.");
		}
		Transaction tx = new Transaction();
		tx.setFromAccount(request.getFromAccount());
		tx.setFromAccountHolderName(request.getFromAccountHolderName());
		tx.setToAccount(request.getToAccount());
		tx.setToAccountHolderName(request.getToAccountHolderName());
		tx.setAmount(request.getAmount());
		tx.setAccountType(request.getAccountType());
		tx.setBalance(request.getBalance());

		transactionService.saveTransaction(tx);
		return ResponseEntity.ok("Transaction successful");
	}

	// get All transactions for an account
	@GetMapping("/{accountNumber}")
	public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions(@PathVariable String accountNumber) {
		List<TransactionResponseDTO> transactions = transactionService.getTransactionsForUser(accountNumber);
		return ResponseEntity.ok(transactions);
	}

	// Filter transactions by date range (optional) and search text
	@GetMapping("/filter")
	public ResponseEntity<List<TransactionResponseDTO>> filterTransactions(@RequestParam String accountNumber,
			@RequestParam(required = false) String searchText,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
		List<TransactionResponseDTO> transactions = transactionService.getTransactionsForAccount(accountNumber,
				searchText, startDate, endDate);
		return ResponseEntity.ok(transactions);
	}

	// Download PDF for a given account
	@GetMapping("/download/pdf/{accountNumber}")
	public ResponseEntity<ByteArrayResource> downloadPdf(@PathVariable String accountNumber) throws IOException {
		List<Transaction> transactions = transactionService.getAllTransactions().stream()
				.filter(t -> accountNumber.equals(t.getFromAccount()) || accountNumber.equals(t.getToAccount()))
				.toList();
		byte[] pdfData = pdfService.generateTransactionHistoryPdf(transactions, accountNumber);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=Transaction_History_" + accountNumber + ".pdf")
				.contentLength(pdfData.length).contentType(MediaType.APPLICATION_PDF)
				.body(new ByteArrayResource(pdfData));
	}

}
