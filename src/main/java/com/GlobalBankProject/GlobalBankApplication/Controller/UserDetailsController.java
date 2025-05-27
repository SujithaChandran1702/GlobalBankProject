package com.GlobalBankProject.GlobalBankApplication.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.GlobalBankProject.GlobalBankApplication.Model.TransferRequestDTO;
//import com.GlobalBankProject.GlobalBankApplication.Model.TransferRequestDTO;
import com.GlobalBankProject.GlobalBankApplication.Model.UserDetailsDTO;
import com.GlobalBankProject.GlobalBankApplication.Service.UserDetailsService;

@RestController
@RequestMapping("/v1/userdetailcontroller")
public class UserDetailsController {

	@Autowired
	UserDetailsService userService;

	@GetMapping("/summary/{userName}")
	public ResponseEntity<UserDetailsDTO> getUserDetails(@PathVariable String userName) {
		UserDetailsDTO userDTO = userService.getUserDetailsByUserName(userName);
		return ResponseEntity.ok(userDTO);

	}
	
	@PostMapping("/transfer")
	public ResponseEntity<String> transferMoney(@RequestBody TransferRequestDTO requestDTO) {
	    try {
	        userService.transferMoney(
	            requestDTO.getFromUserName(),
	            requestDTO.getToAccountNumber(),
	            requestDTO.getAmount()
	        );
	        return ResponseEntity.ok("Transfer successful");
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().body(e.getMessage());
	    } catch (Exception e) {
	        return ResponseEntity.internalServerError().body("Something went wrong");
	    }
	}

}
