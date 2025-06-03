package com.GlobalBankProject.GlobalBankApplication.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.GlobalBankProject.GlobalBankApplication.Model.Beneficiary;
import com.GlobalBankProject.GlobalBankApplication.Model.UserDetails;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

	Optional<UserDetails> findByUserName(String userName);

	Optional<UserDetails> findByAccountNumber(String accountNumber);

	@Query("SELECT u.userName  FROM UserDetails u WHERE u.accountNumber = :accountNumber")
	String findNameByAccountNumber(@Param("accountNumber") String accountNumber);

}
