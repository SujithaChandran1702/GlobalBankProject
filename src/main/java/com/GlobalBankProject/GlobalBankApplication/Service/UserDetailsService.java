package com.GlobalBankProject.GlobalBankApplication.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.GlobalBankProject.GlobalBankApplication.Model.UserDetails;
import com.GlobalBankProject.GlobalBankApplication.Model.UserDetailsDTO;
import com.GlobalBankProject.GlobalBankApplication.Repository.UserDetailsRepository;

@Service
public class UserDetailsService {

	@Autowired
	private UserDetailsRepository userRepository;

	public UserDetailsDTO getUserDetailsByUserName(String userName) {
		UserDetails user = userRepository.findByUserName(userName)
				.orElseThrow(() -> new RuntimeException("User not found"));

		return new UserDetailsDTO(user.getUserName(), user.getAccountNumber(), user.getBalance(), user.getAccountType(),
				user.getIfscCode(), user.getPanNumber(), user.getPhoneNumber(), user.getEmail());
	}

	public void transferMoney(String fromUsername, String toAccountNumber, double amount) {
		UserDetails sender = userRepository.findByUserName(fromUsername)
				.orElseThrow(() -> new IllegalArgumentException("Sender not found"));

		UserDetails receiver = userRepository.findByAccountNumber(toAccountNumber)
				.orElseThrow(() -> new IllegalArgumentException("Recipient not found"));

		if (amount <= 0) {
			throw new IllegalArgumentException("Amount must be greater than zero");
		}

		if (sender.getBalance() < amount) {
			throw new IllegalArgumentException("Insufficient balance");
		}

		sender.setBalance(sender.getBalance() - amount);
		receiver.setBalance(receiver.getBalance() + amount);

		userRepository.save(sender);
		userRepository.save(receiver);
	}

}
