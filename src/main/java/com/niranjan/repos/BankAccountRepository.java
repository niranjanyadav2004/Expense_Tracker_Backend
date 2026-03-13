package com.niranjan.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niranjan.entities.BankAccount;
import com.niranjan.entities.User;
import java.util.List;




public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

	Optional<BankAccount> findByBankNameAndUser(String bankName, User user);
	List<BankAccount> findByBankName(String bankName);
	Optional<BankAccount> findByAccountNumber(String accountNumber);
	List<BankAccount> findAllByUser(User user);
	boolean existsByAccountNumber(String accountNumber);
	
}
