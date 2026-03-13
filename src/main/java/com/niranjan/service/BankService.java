package com.niranjan.service;

import java.util.List;

import com.niranjan.DTO.CreateNewBank;
import com.niranjan.DTO.NewAccountResponse;
import com.niranjan.DTO.UpdateBankAccountDTO;


public interface BankService {

	NewAccountResponse createBankAccount(CreateNewBank request);

	List<NewAccountResponse> getAllBankAccounts();
	
	List<NewAccountResponse> getBankAccountByBankName(String bankName);
	
	List<NewAccountResponse> getAllBankAccountsByUser();
	
	NewAccountResponse getBankAccountByAccountNumber(String accountNumber);
	
	NewAccountResponse updateBank(String id, UpdateBankAccountDTO request);
	
	void deleteBankAccount(String accountNumber);
}
