package com.GlobalBankProject.GlobalBankApplication.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.GlobalBankProject.GlobalBankApplication.Model.Transaction;
import com.GlobalBankProject.GlobalBankApplication.Model.TransactionResponseDTO;
import com.GlobalBankProject.GlobalBankApplication.Repository.TransactionRepository;
import com.GlobalBankProject.GlobalBankApplication.Repository.UserDetailsRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	public Transaction saveTransaction(Transaction tx) {

		String fromName = userDetailsRepository.findNameByAccountNumber(tx.getFromAccount());
		String toName = userDetailsRepository.findNameByAccountNumber(tx.getToAccount());

		tx.setFromAccountHolderName(fromName);
		tx.setToAccountHolderName(toName);

		if (tx.getTimestamp() == null) {
			tx.setTimestamp(LocalDateTime.now());
		}

		return transactionRepository.save(tx);
	}

	public List<Transaction> getAllTransactions() {
		List<Transaction> allTrx = transactionRepository.findAll();
		System.out.println("All Transaction Count: " + allTrx.size());
		return allTrx;
	}

//	public List<Transaction> getFilteredTransaction(String searchText, LocalDateTime startDate, LocalDateTime endDate) {
//		List<Transaction> transactions;
//		if (startDate != null && endDate != null) {
//			transactions = transactionRepository.findByTimestampBetween(startDate, endDate);
//		} else {
//			transactions = transactionRepository.findAllByOrderByTimestampDesc();
//		}
//		System.out.println(transactions.size());
//		if (searchText != null && !searchText.trim().isEmpty()) {
//			String lowerSearch = searchText.toLowerCase();
//			transactions = transactions.stream().filter(t -> t.getFromAccount().toLowerCase().contains(lowerSearch)
//					|| t.getToAccount().toLowerCase().contains(lowerSearch)).collect(Collectors.toList());
//		}
//		return transactions;
//	}

	public List<TransactionResponseDTO> getTransactionsForAccount(String userAccount, String searchText,
			LocalDateTime startDate, LocalDateTime endDate) {

		List<Transaction> transactions;

		if (startDate != null && endDate != null) {
			transactions = transactionRepository.findByTimestampBetween(startDate, endDate);
		} else {
			transactions = transactionRepository.findAllByOrderByTimestampDesc();
		}
		// ✅ Filter only transactions where the user is either sender or receiver
		transactions = transactions.stream()
				.filter(t -> userAccount.equals(t.getFromAccount()) || userAccount.equals(t.getToAccount()))
				.collect(Collectors.toList());
		// ✅ Optional search
		if (searchText != null && !searchText.trim().isEmpty()) {
			String lowerSearch = searchText.toLowerCase();
			transactions = transactions.stream()
					.filter(t -> t.getFromAccount().toLowerCase().contains(lowerSearch)
							|| t.getToAccount().toLowerCase().contains(lowerSearch)
							|| (t.getFromAccountHolderName() != null
									&& t.getFromAccountHolderName().toLowerCase().contains(lowerSearch))
							|| (t.getToAccountHolderName() != null
									&& t.getToAccountHolderName().toLowerCase().contains(lowerSearch)))
					.collect(Collectors.toList());
		}
		// ✅ Convert to Response DTO
		return transactions.stream().map(t -> {
			TransactionResponseDTO dto = new TransactionResponseDTO();
			dto.setId(t.getId());
			dto.setFromAccount(t.getFromAccount());
			dto.setFromAccountHolderName(t.getFromAccountHolderName());
			dto.setToAccount(t.getToAccount());
			dto.setToAccountHolderName(t.getToAccountHolderName());
			dto.setAccountType(t.getAccountType());
			dto.setAmount(t.getAmount());
			dto.setBalance(t.getBalance());
			dto.setTimestamp(t.getTimestamp());
			// ✅ Set transaction type
			if (userAccount.equals(t.getToAccount())) {
				dto.setTransactionType("Credit");
			} else if (userAccount.equals(t.getFromAccount())) {
				dto.setTransactionType("Debit");
			} else {
				dto.setTransactionType("Unknown");
			}
			return dto;
		}).collect(Collectors.toList());
	}

	public List<TransactionResponseDTO> getTransactionsForUser(String accountNumber) {
		List<Transaction> transactions = transactionRepository
				.findByFromAccountOrToAccountOrderByTimestampDesc(accountNumber, accountNumber);
		return transactions.stream().map(this::mapToDTO).collect(Collectors.toList());
	}

	public List<TransactionResponseDTO> getTransactionsForUserByDate(String accountNumber, LocalDateTime startDate,
			LocalDateTime endDate) {
		List<Transaction> transactions = transactionRepository
				.findByFromAccountOrToAccount(accountNumber, accountNumber).stream()
				.filter(tx -> !tx.getTimestamp().isBefore(startDate) && !tx.getTimestamp().isAfter(endDate))
				.collect(Collectors.toList());
		return transactions.stream().map(this::mapToDTO).collect(Collectors.toList());
	}

	public ResponseEntity<ByteArrayResource> generateTransactionPdf(String accountNumber) {
		List<Transaction> transactions = transactionRepository.findByFromAccountOrToAccount(accountNumber,
				accountNumber);

		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Document document = new Document(null);
			PdfWriter.getInstance(document, out);
			document.open();
			Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
			Paragraph title = new Paragraph("Transaction History - " + accountNumber, fontTitle);
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);
			document.add(new Paragraph(" ")); // space

			PdfPTable table = new PdfPTable(6);
			table.setWidthPercentage(100);
			table.addCell("Date");
			table.addCell("From");
			table.addCell("To");
			table.addCell("Type");
			table.addCell("Amount");
			table.addCell("Balance");
			for (Transaction tx : transactions) {
				table.addCell(tx.getTimestamp().toLocalDate().toString());
				table.addCell(tx.getFromAccount());
				table.addCell(tx.getToAccount());
				table.addCell(tx.getTransactionType());
				table.addCell(String.valueOf(tx.getAmount()));
				table.addCell(String.valueOf(tx.getBalance()));
			}

			document.add(table);
			document.close();

			ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

			return ResponseEntity.ok().header("Content-Disposition", "attachment;filename=Transaction_History.pdf")
					.contentLength(out.toByteArray().length).body(resource);
		} catch (Exception e) {
			throw new RuntimeException("Failed to generate PDF", e);
		}
	}

	private TransactionResponseDTO mapToDTO(Transaction tx) {
		return new TransactionResponseDTO(tx.getId(), tx.getFromAccount(), tx.getFromAccountHolderName(),
				tx.getToAccount(), tx.getToAccountHolderName(), tx.getAccountType(), tx.getAmount(), tx.getBalance(),
				tx.getTimestamp(), tx.getTransactionType(), tx.getDate());
	}

}
