package com.GlobalBankProject.GlobalBankApplication.Service;

import com.GlobalBankProject.GlobalBankApplication.Model.RegistrationRequestDTO;
import com.GlobalBankProject.GlobalBankApplication.Model.RegistrationResponseDTO;

public interface RegistrationService {

	RegistrationResponseDTO newRegistration(RegistrationRequestDTO regRequest);

	boolean validateUser(String userName, String password);

	Double getUserBalance(String userName);

}
