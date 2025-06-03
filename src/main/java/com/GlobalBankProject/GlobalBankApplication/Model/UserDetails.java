package com.GlobalBankProject.GlobalBankApplication.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_details")
public class UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_name")
	@NotBlank(message = "username is required")
	private String userName;

	@Column(name = "email")
	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	private String email;

	@Column(name = "phone_number")
	@Pattern(regexp = "\\+91\\d{10}", message = "Phone number must be in format +91XXXXXXXXXX")
	private String phoneNumber;

	@Column(name = "pan_number")
	 @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]", message = "PAN must be in format: 5 letters, 4 digits, 1 letter (e.g. ABCDE1234F)")
	private String panNumber;

	@NotNull
	private Double balance = 10000.0;
	private String accountNumber;
	private String ifscCode;
	private String accountType;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToOne(mappedBy = "userDetails")
	private Registration registration;
}
