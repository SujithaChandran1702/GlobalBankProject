package com.GlobalBankProject.GlobalBankApplication.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.GlobalBankProject.GlobalBankApplication.Service.RegistrationServiceImplement;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/v1/pagecontroller")
@Slf4j
public class PageController {

	@Autowired
	private RegistrationServiceImplement service;

	@PostMapping("/login")
	public String login(@RequestParam("userName") String userName, @RequestParam("password") String password) {
		log.info("Login attempt for user: {}", userName);

		if (service.validateUser(userName, password)) {
			log.info("Login successful for user: {}", userName);
			return "redirect:/Dashboard.html?username=" + userName;
		} else {
			log.warn("Login failed for user: {}", userName);
			return "redirect:/index.html?error=true";
		}
	}

	/*
	 * @PostMapping("/apilogin")
	 * 
	 * @ResponseBody public ResponseEntity<?> apiLogin(@RequestBody Map<String,
	 * String> credentials) { String userName = credentials.get("userName"); String
	 * password = credentials.get("password");
	 * 
	 * if (service.validateUser(userName, password)) { Map<String, String> response
	 * = new HashMap<>(); response.put("username", userName); return
	 * ResponseEntity.ok(response); } else { return
	 * ResponseEntity.status(401).body("Invalid username or password"); } }
	 */

	@GetMapping("/")
	public String loadLoadingPage() {
		return "redirect:/index.html";
	}

	@GetMapping("/home")
	public String goToHome() {
		return "redirect:/Dashboard.html";
	}

	@GetMapping("/balance")
	@ResponseBody
	public ResponseEntity<Map<String, Double>> getBalance(@RequestParam("userName") String userName) {
		log.info("Fetching balance for user: {}", userName);
		Double balance = service.getUserBalance(userName);
		Map<String, Double> response = new HashMap<>();
		response.put("balance", balance);
		return ResponseEntity.ok(response);
	}
}
