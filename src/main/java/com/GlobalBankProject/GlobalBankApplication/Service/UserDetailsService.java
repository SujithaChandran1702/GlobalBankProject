package com.GlobalBankProject.GlobalBankApplication.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.GlobalBankProject.GlobalBankApplication.Model.TransactionResponseDTO;
import com.GlobalBankProject.GlobalBankApplication.Model.TransferRequestDTO;
import com.GlobalBankProject.GlobalBankApplication.Model.UserDetails;
import com.GlobalBankProject.GlobalBankApplication.Model.UserDetailsDTO;
import com.GlobalBankProject.GlobalBankApplication.Repository.UserDetailsRepository;

@Service
public class UserDetailsService {

	@Autowired
	private UserDetailsRepository userRepository;

	@Autowired
	private EmailService emailService;

	public UserDetailsDTO getUserDetailsByUserName(String userName) {
		UserDetails user = userRepository.findByUserName(userName)
				.orElseThrow(() -> new RuntimeException("User not found"));

		return new UserDetailsDTO(user.getUserName(), user.getAccountNumber(), user.getBalance(), user.getAccountType(),
				user.getIfscCode(), user.getPanNumber(), user.getPhoneNumber(), user.getEmail());
	}

	public void transferMoney(TransferRequestDTO dto) {
		UserDetails sender = userRepository.findByUserName(dto.getFromUserName())
				.orElseThrow(() -> new IllegalArgumentException("Sender not found"));

		UserDetails receiver = userRepository.findByAccountNumber(dto.getToAccountNumber())
				.orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

		if (sender.getBalance() < dto.getAmount()) {
			throw new IllegalArgumentException("Insufficient balance");
		}

		sender.setBalance(sender.getBalance() - dto.getAmount());
		receiver.setBalance(receiver.getBalance() + dto.getAmount());

		userRepository.save(sender);
		userRepository.save(receiver);

		// Send email to sender
		emailService.sendMail(sender.getEmail(), "Debit Alert - Global Bank",
				"Dear " + sender.getUserName() + ",\n\nYou have successfully transferred ₹" + dto.getAmount()
						+ " to account " + receiver.getAccountNumber() + ".\n\nYour remaining balance is ₹"
						+ sender.getBalance() + ".\n\nThank you for using Global Bank.");

		// Send email to receiver
		emailService.sendMail(receiver.getEmail(), "Credit Alert - Global Bank",
				"Dear " + receiver.getUserName() + ",\n\nYou have received ₹" + dto.getAmount() + " from "
						+ sender.getUserName() + ".\n\nYour new balance is ₹" + receiver.getBalance()
						+ ".\n\nThank you for using Global Bank.");

		// Prepare transaction response DTO for SMS
		TransactionResponseDTO transactionResponse = new TransactionResponseDTO();
		transactionResponse.setAmount(dto.getAmount());
		transactionResponse.setFromAccount(sender.getAccountNumber());
		transactionResponse.setToAccount(receiver.getAccountNumber());

		// Send SMS notifications
		emailService.notifyUserViaSms(sender, receiver, transactionResponse);
	}

}
