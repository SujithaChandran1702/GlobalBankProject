package com.GlobalBankProject.GlobalBankApplication.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.GlobalBankProject.GlobalBankApplication.Model.Beneficiary;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

	List<Beneficiary> findByAddedBy_UserName(String userName);

}
