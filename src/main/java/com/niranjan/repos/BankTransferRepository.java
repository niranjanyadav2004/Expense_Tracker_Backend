package com.niranjan.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niranjan.entities.BankTransfer;
import java.util.List;
import com.niranjan.entities.BankAccount;



public interface BankTransferRepository extends JpaRepository<BankTransfer, Long> {

	List<BankTransfer> findByFromBankOrToBank(BankAccount fromBank, BankAccount toBank);
	void deleteByFromBankOrToBank(BankAccount fromBank, BankAccount toBank);
	
}
