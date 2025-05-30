package com.GlobalBankProject.GlobalBankApplication.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.GlobalBankProject.GlobalBankApplication.Model.Beneficiary;
import com.GlobalBankProject.GlobalBankApplication.Model.BeneficiaryDTO;
import com.GlobalBankProject.GlobalBankApplication.Model.UserDetails;
import com.GlobalBankProject.GlobalBankApplication.Repository.BeneficiaryRepository;
import com.GlobalBankProject.GlobalBankApplication.Repository.UserDetailsRepository;

@Service
public class BeneficiaryService {

	@Autowired
	private BeneficiaryRepository beneficiaryRepository;

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	public void addBeneficiary(BeneficiaryDTO dto) {
		Optional<UserDetails> beneficiaryUser = userDetailsRepository.findByAccountNumber(dto.getAccountNumber());
		if (!beneficiaryUser.isPresent()) {
			throw new IllegalArgumentException("No user found with the given account number");
		}

		Optional<UserDetails> addedBy = userDetailsRepository.findByUserName(dto.getAddedByUserName());
		if (!addedBy.isPresent()) {
			throw new IllegalArgumentException("Invalid user adding the beneficiary.");
		}

		Beneficiary beneficiary = new Beneficiary();
		beneficiary.setName(dto.getName());
		beneficiary.setAccountNumber(dto.getAccountNumber());
		beneficiary.setIfscCode(dto.getIfscCode());
		beneficiary.setAddedBy(addedBy.get());

		beneficiaryRepository.save(beneficiary);
	}

	public List<BeneficiaryDTO> getBeneficiariesByUserName(String userName) {
		List<Beneficiary> beneficiaries = beneficiaryRepository.findByAddedBy_UserName(userName);
		return beneficiaries.stream().map(b -> {
			BeneficiaryDTO dto = new BeneficiaryDTO();
			dto.setName(b.getName());
			dto.setAccountNumber(b.getAccountNumber());
			dto.setIfscCode(b.getIfscCode());
			dto.setAddedByUserName(userName);
			return dto;
		}).collect(Collectors.toList());
	}

}
