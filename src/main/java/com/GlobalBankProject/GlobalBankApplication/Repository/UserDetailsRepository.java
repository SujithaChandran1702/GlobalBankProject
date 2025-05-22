package com.GlobalBankProject.GlobalBankApplication.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GlobalBankProject.GlobalBankApplication.Model.UserDetails;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

	Optional<UserDetails> findByUserName(String userName);
}
