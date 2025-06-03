package com.GlobalBankProject.GlobalBankApplication.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.GlobalBankProject.GlobalBankApplication.Model.TransactionResponseDTO;
import com.GlobalBankProject.GlobalBankApplication.Model.*;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private RestTemplate restTemplate;


	@Value("${spring.mail.username}")
	private String name;

	public void sendMail(String to, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(name);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		mailSender.send(message);
	}

	private String last4Digits(String accNumber) {
		return accNumber != null && accNumber.length() >= 4 ? accNumber.substring(accNumber.length() - 4) : accNumber;
	}

	private String formatPhone(String phone) {
		return phone.startsWith("+91") ? phone : "+91" + phone;
	}

	public void notifyUserViaSms(UserDetails sender, UserDetails receiver, TransactionResponseDTO dto) {
		String senderSms = "Dear " + sender.getUserName() + ", ₹" + dto.getAmount()
				+ " has been debited from your account ending with " + last4Digits(sender.getAccountNumber())
				+ ". Remaining balance: ₹" + sender.getBalance() + ". Thank you for using Global Bank.";

		String receiverSms = "Dear " + receiver.getUserName() + ", ₹" + dto.getAmount()
				+ " has been credited to your account ending with " + last4Digits(receiver.getAccountNumber())
				+ ". New balance: ₹" + receiver.getBalance() + ". Thank you for using Global Bank.";

		SmsDTO smsToSender = new SmsDTO(formatPhone(sender.getPhoneNumber()), senderSms);
		SmsDTO smsToReceiver = new SmsDTO(formatPhone(receiver.getPhoneNumber()), receiverSms);

		String smsApiUrl = "http://localhost:9091/v1/smscontroller/send";

		try {
			System.out.println("Sending SMS to sender: " + smsToSender.getPhoneNumber());
			restTemplate.postForEntity(smsApiUrl, smsToSender, String.class);
		} catch (Exception e) {
			System.err.println("Failed to send SMS to sender: " + e.getMessage());
		}

		try {
			System.out.println("Sending SMS to receiver: " + smsToReceiver.getPhoneNumber());
			restTemplate.postForEntity(smsApiUrl, smsToReceiver, String.class);
		} catch (Exception e) {
			System.err.println("Failed to send SMS to receiver: " + e.getMessage());
		}
	}

}
