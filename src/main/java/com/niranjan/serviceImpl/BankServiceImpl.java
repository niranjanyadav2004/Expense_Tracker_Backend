package com.niranjan.serviceImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niranjan.DTO.CreateNewBank;
import com.niranjan.DTO.NewAccountResponse;
import com.niranjan.DTO.UpdateBankAccountDTO;
import com.niranjan.customExceptions.BankAccountAlreadyExist;
import com.niranjan.customExceptions.BankAccountNotFoundException;
import com.niranjan.entities.BankAccount;
import com.niranjan.entities.User;
import com.niranjan.repos.BankAccountRepository;
import com.niranjan.repos.BankTransferRepository;
import com.niranjan.service.BankService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
	
	private final BankAccountRepository bankAccountRepository;
	private final BankTransferRepository bankTransferRepository;
	
	private User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
	
	private NewAccountResponse saveBankAccount(BankAccount account, CreateNewBank request) {
		account.setBankName(request.getBankName());
		account.setAccountNumber(request.getAccountNumber());
		account.setBalance(0.0);
		
		BankAccount save = bankAccountRepository.save(account);
		
		return NewAccountResponse.builder()
								 .id(save.getId())
								 .bankName(save.getBankName())
								 .accountNumber(save.getAccountNumber())
								 .balance(save.getBalance())
								 .accountHolder(getCurrentUser().getName())
								 .build();
	}
	
	private NewAccountResponse updateBankAccount(BankAccount account, UpdateBankAccountDTO request) {
		account.setBankName(request.getBankName());
		account.setAccountNumber(request.getAccountNumber());
		account.setBalance(request.getBalance());
		
		BankAccount save = bankAccountRepository.save(account);
		
		return NewAccountResponse.builder()
								 .id(save.getId())
								 .bankName(save.getBankName())
								 .accountNumber(save.getAccountNumber())
								 .balance(save.getBalance())
								 .accountHolder(getCurrentUser().getName())
								 .build();
	}

	@Override
	public NewAccountResponse createBankAccount(CreateNewBank request) {
		
		if(bankAccountRepository.existsByAccountNumber(request.getAccountNumber())) 
					throw new BankAccountAlreadyExist("Bank account already exist with account number : " + request.getAccountNumber());
		
		User currentUser = getCurrentUser();
		BankAccount bankAccount = new BankAccount();
		bankAccount.setUser(currentUser);
		bankAccount.setId(UUID.randomUUID().toString());
		return saveBankAccount(bankAccount, request);
	}

	@Override
	public List<NewAccountResponse> getAllBankAccounts() {
		return bankAccountRepository.findAll().stream()
				  .map(account->NewAccountResponse.builder()
					  		.id(account.getId())
					  		.bankName(account.getBankName())
					  		.accountNumber(account.getAccountNumber())
					  		.balance(account.getBalance())
					  		.accountHolder(account.getUser().getName())
					  		.build())
			  .toList();
	}

	@Override
	public List<NewAccountResponse> getBankAccountByBankName(String bankName) {
		return bankAccountRepository.findByBankName(bankName).stream()
													  .map(account->NewAccountResponse.builder()
															  		.id(account.getId())
															  		.bankName(account.getBankName())
															  		.accountNumber(account.getAccountNumber())
															  		.balance(account.getBalance())
															  		.accountHolder(account.getUser().getName())
															  		.build())
													  .toList();
													  
	}

	@Override
	public NewAccountResponse getBankAccountByAccountNumber(String accountNumber) {
		BankAccount bankAccount = bankAccountRepository.findByAccountNumber(accountNumber).orElseThrow(()->new BankAccountNotFoundException("Bank account Not found"));
		return NewAccountResponse.builder()
						  .id(bankAccount.getId())
						  .bankName(bankAccount.getBankName())
						  .accountNumber(bankAccount.getAccountNumber())
						  .balance(bankAccount.getBalance())
						  .accountHolder(bankAccount.getUser().getName())
						  .build();
	}
	
	@Override
	public List<NewAccountResponse> getAllBankAccountsByUser() {
		return bankAccountRepository.findAllByUser(getCurrentUser()).stream()
				  .map(account->NewAccountResponse.builder()
					  		.id(account.getId())
					  		.bankName(account.getBankName())
					  		.accountNumber(account.getAccountNumber())
					  		.balance(account.getBalance())
					  		.accountHolder(account.getUser().getName())
					  		.build())
			  .toList();
	}

	@Override
	public NewAccountResponse updateBank(String id, UpdateBankAccountDTO request) {
		BankAccount bankAccount = bankAccountRepository.findById(id).orElseThrow(()->new BankAccountNotFoundException("Bank account not found"));
		return updateBankAccount(bankAccount, request);
	}

	@Override
	@Transactional
	public void deleteBankAccount(String accountNumber) {
		BankAccount bankAccount = bankAccountRepository.findByAccountNumber(accountNumber).orElseThrow(()->new BankAccountNotFoundException("Bank account not found"));
		bankTransferRepository.deleteByFromBankOrToBank(bankAccount, bankAccount);
		bankAccountRepository.delete(bankAccount);
		return;
	}

}
