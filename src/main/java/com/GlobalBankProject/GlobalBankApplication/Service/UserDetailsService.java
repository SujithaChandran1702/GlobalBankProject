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

}
