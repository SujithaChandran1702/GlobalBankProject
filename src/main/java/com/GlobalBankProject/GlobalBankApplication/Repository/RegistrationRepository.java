package com.GlobalBankProject.GlobalBankApplication.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.GlobalBankProject.GlobalBankApplication.Model.Registration;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
	Optional<Registration> findByUserName(String username);

}
