package com.GlobalBankProject.GlobalBankApplication.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.GlobalBankProject.GlobalBankApplication.Model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

//	@Query(value = "select * from transaction where date_format(timestamp,'%Y-%m-%d') between (:start) and (:end) ; ",nativeQuery = true)
	List<Transaction> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

	List<Transaction> findAllByOrderByTimestampDesc();

	List<Transaction> findByFromAccountOrToAccountOrderByTimestampDesc(String fromAccount, String toAccount);

	List<Transaction> findByFromAccountOrToAccount(String fromAccount, String toAccount);

}
