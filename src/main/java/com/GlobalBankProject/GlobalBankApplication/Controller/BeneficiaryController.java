package com.GlobalBankProject.GlobalBankApplication.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.GlobalBankProject.GlobalBankApplication.Model.BeneficiaryDTO;
import com.GlobalBankProject.GlobalBankApplication.Service.BeneficiaryService;

@RestController
@RequestMapping("/v1/beneficiaries")
public class BeneficiaryController {

	@Autowired
	private BeneficiaryService beneficiaryService;

	@PostMapping("/add")
	public ResponseEntity<String> addBeneficiary(@RequestBody BeneficiaryDTO dto) {
		try {
			beneficiaryService.addBeneficiary(dto);
			return ResponseEntity.ok("Beneficiary added successfully.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("An error occurred while adding the beneficiary.");
		}
	}

	@GetMapping("/user/{username}")
	public ResponseEntity<List<BeneficiaryDTO>> getBeneficiariesByUser(@PathVariable String username) {
		try {
			List<BeneficiaryDTO> beneficiaries = beneficiaryService.getBeneficiariesByUserName(username);
			return ResponseEntity.ok(beneficiaries);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}