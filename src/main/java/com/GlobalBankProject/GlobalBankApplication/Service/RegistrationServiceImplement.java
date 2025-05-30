package com.GlobalBankProject.GlobalBankApplication.Service;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.GlobalBankProject.GlobalBankApplication.Model.Registration;
import com.GlobalBankProject.GlobalBankApplication.Model.RegistrationRequestDTO;
import com.GlobalBankProject.GlobalBankApplication.Model.RegistrationResponseDTO;
import com.GlobalBankProject.GlobalBankApplication.Model.UserDetails;
import com.GlobalBankProject.GlobalBankApplication.Repository.RegistrationRepository;
import com.GlobalBankProject.GlobalBankApplication.Repository.UserDetailsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RegistrationServiceImplement implements RegistrationService {

	@Autowired
	private RegistrationRepository repository;

	public RegistrationResponseDTO newRegistration(RegistrationRequestDTO request) {

		if (repository.findByUserName(request.getUserName()).isPresent()) {
			log.warn("Registration failed: Username {} already exists", request.getUserName());
			throw new RuntimeException("Username already exists");
		}

		Registration reg = new Registration();
		UserDetails userDetails = new UserDetails();

		reg.setUserName(request.getUserName());
		reg.setPassword(request.getPassword());
		reg.setUserDetails(userDetails);

		userDetails.setUserName(request.getUserName()); // ✅ ADD THIS LINE
		userDetails.setEmail(request.getEmail());
		userDetails.setPhoneNumber(request.getPhoneNumber());
		userDetails.setPanNumber(request.getPanNumber());

		// Auto-generate value
		userDetails.setBalance(10000.00);
		userDetails.setAccountNumber("AC" + System.currentTimeMillis());
		userDetails.setIfscCode("GLBL" + String.format("%04d", new Random().nextInt(10000)));
		userDetails.setAccountType("SAVINGS");

		userDetails.setRegistration(reg);

		Registration savedRegistration = repository.save(reg);
		System.out.println("saved UserDetails: " + savedRegistration.getUserDetails());
		log.info("User registered successfully: {}", request.getUserName());

		return mapToResponse(savedRegistration, savedRegistration.getUserDetails());
	}

	@Override
	public boolean validateUser(String userName, String password) {
		Optional<Registration> optionalUser = repository.findByUserName(userName);
		boolean isValid = optionalUser.map(user -> user.getPassword().equals(password)).orElse(false);
		log.info("Login {} for user: {}", isValid ? "successful" : "failed", userName);
		return isValid;
	}

	@Override
	public Double getUserBalance(String userName) {
		return repository.findByUserName(userName).map(reg -> {
			if (reg.getUserDetails() == null) {
				log.warn("UserDetails is null for user: {}", userName);
				return 0.0;
			}
			return reg.getUserDetails().getBalance();
		}).orElse(0.0);
	}

	private RegistrationResponseDTO mapToResponse(Registration reg, UserDetails details) {
		RegistrationResponseDTO regResponse = new RegistrationResponseDTO();
		regResponse.setUsername(reg.getUserName());
		regResponse.setEmail(details.getEmail());
		regResponse.setPhoneNumber(details.getPhoneNumber());
		regResponse.setPanNumber(details.getPanNumber());
		regResponse.setAccountNumber(details.getAccountNumber());
		regResponse.setIfscCode(details.getIfscCode());
		regResponse.setAccountType(details.getAccountType());
		regResponse.setBalance(details.getBalance());
		return regResponse;
	}

}
