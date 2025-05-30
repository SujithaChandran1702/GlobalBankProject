package com.GlobalBankProject.GlobalBankApplication.Model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "registration_details")
public class Registration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String userName;

	@Column(nullable = false)
	private String password;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_details_id", referencedColumnName = "id")
	private UserDetails userDetails;

}
