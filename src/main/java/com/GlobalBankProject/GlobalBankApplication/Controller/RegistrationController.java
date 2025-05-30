package com.GlobalBankProject.GlobalBankApplication.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.GlobalBankProject.GlobalBankApplication.Model.RegistrationRequestDTO;
import com.GlobalBankProject.GlobalBankApplication.Model.RegistrationResponseDTO;
import com.GlobalBankProject.GlobalBankApplication.Service.RegistrationServiceImplement;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/registrationcontroller")
@Slf4j
public class RegistrationController {

	@Autowired
	private RegistrationServiceImplement service;

	@PostMapping("/signup")
	public ResponseEntity<RegistrationResponseDTO> newRegistration(@RequestBody RegistrationRequestDTO requestDTO) {
		log.info("Received registration request for user: {}", requestDTO.getUserName());

		try {
			RegistrationResponseDTO responseDTO = service.newRegistration(requestDTO);
			log.info("Registration successful for user: {}", responseDTO.getUsername());
			return ResponseEntity.ok(responseDTO);
		} catch (RuntimeException e) {
			log.error("Registration failed: {}", e.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}
}
