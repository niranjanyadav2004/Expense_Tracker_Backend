package com.niranjan.serviceImpl;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niranjan.DTO.TransferDTO;
import com.niranjan.DTO.TransferResponse;
import com.niranjan.customExceptions.BankAccountNotFoundException;
import com.niranjan.entities.BankAccount;
import com.niranjan.entities.BankTransfer;
import com.niranjan.entities.User;
import com.niranjan.repos.BankAccountRepository;
import com.niranjan.repos.BankTransferRepository;
import com.niranjan.service.BankTransferService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BankTransferServiceImpl implements BankTransferService {

	private final BankAccountRepository bankAccountRepository;
	private final BankTransferRepository bankTransferRepository;
	
	private User getCurrentUser() {
	    return (User) SecurityContextHolder
	            .getContext()
	            .getAuthentication()
	            .getPrincipal();
	}
	
	public TransferResponse mapToTransferResponse(BankTransfer transfer) {

	    return TransferResponse.builder()
	    		.id(transfer.getId())
	            .fromBankName(transfer.getFromBank().getBankName())
	            .toBankName(transfer.getToBank().getBankName())
	            .amount(transfer.getAmount())
	            .date(transfer.getDate())
	            .description(transfer.getDescription())
	            .build();
	}
	
	@Override
	@Transactional
	public void transferAmount(TransferDTO transferDTO) {
		BankAccount fromBank = bankAccountRepository.findByBankNameAndUser(transferDTO.getFromBankName(), getCurrentUser())
								.orElseThrow(()->new BankAccountNotFoundException("Bank not found with name : " + transferDTO.getFromBankName()));
		BankAccount toBank = bankAccountRepository.findByBankNameAndUser(transferDTO.getToBankName(), getCurrentUser())
				.orElseThrow(()->new BankAccountNotFoundException("Bank not found with name : " + transferDTO.getToBankName()));
		
		BankTransfer bankTransfer = BankTransfer.builder()
					.fromBank(fromBank)
					.toBank(toBank)
					.amount(transferDTO.getAmount())
					.date(transferDTO.getDate())
					.description(transferDTO.getDescription())
					.build();
		
		if(fromBank.getBalance() < transferDTO.getAmount()){
	        throw new RuntimeException("Insufficient balance");
	    }
		
		fromBank.setBalance(fromBank.getBalance() - transferDTO.getAmount());
		toBank.setBalance(toBank.getBalance() + transferDTO.getAmount());
		
		bankAccountRepository.save(fromBank);
		bankAccountRepository.save(toBank);
		
		bankTransferRepository.save(bankTransfer);
		
		return;
	}

	@Override
	public List<TransferResponse> getTransfersByBankName(String bankName) {
		BankAccount bankAccount = bankAccountRepository.findByBankNameAndUser(bankName, getCurrentUser())
				.orElseThrow(()->new BankAccountNotFoundException("Bank not found with name : " + bankName));
		
		 List<BankTransfer> transfers =
		            bankTransferRepository.findByFromBankOrToBank(bankAccount, bankAccount);

		    return transfers.stream()
		            .map(this::mapToTransferResponse)
		            .toList();
	}

	@Override
	public List<TransferResponse> getAllTranfer() {
		return bankTransferRepository.findAll().stream()
	            .map(this::mapToTransferResponse)
	            .toList();
	}

	@Override
	public TransferResponse updateTransfer(Long id, TransferDTO transferDTO) {
		BankTransfer transfer = bankTransferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transfer not found"));
		
		BankAccount fromBank = bankAccountRepository.findByBankNameAndUser(transferDTO.getFromBankName(), getCurrentUser())
				.orElseThrow(()->new BankAccountNotFoundException("Bank not found with name : " + transferDTO.getFromBankName()));
		BankAccount toBank = bankAccountRepository.findByBankNameAndUser(transferDTO.getToBankName(), getCurrentUser())
				.orElseThrow(()->new BankAccountNotFoundException("Bank not found with name : " + transferDTO.getToBankName()));
		
		Double oldAmount = transfer.getAmount();
		
		//revert old changes
		fromBank.setBalance(fromBank.getBalance() + oldAmount);
		toBank.setBalance(toBank.getBalance() - oldAmount);
		
		//new Changes
		fromBank.setBalance(fromBank.getBalance() - transferDTO.getAmount());
		toBank.setBalance(toBank.getBalance() + transferDTO.getAmount());
		
		transfer.setFromBank(fromBank);
		transfer.setToBank(toBank);
		transfer.setAmount(transferDTO.getAmount());
		transfer.setDate(transferDTO.getDate());
		transfer.setDescription(transferDTO.getDescription());
		
		bankAccountRepository.save(fromBank);
		bankAccountRepository.save(toBank);
		
		BankTransfer updatedTransfer = bankTransferRepository.save(transfer);
		
		return mapToTransferResponse(updatedTransfer);
	}

	@Override
	public void deleteTransfer(Long id) {
		 BankTransfer transfer = bankTransferRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Transfer not found"));

	        BankAccount fromBank = transfer.getFromBank();
	        BankAccount toBank = transfer.getToBank();

	        Double amount = transfer.getAmount();

	        // Revert balances
	        fromBank.setBalance(fromBank.getBalance() + amount);
	        toBank.setBalance(toBank.getBalance() - amount);

	        bankAccountRepository.save(fromBank);
	        bankAccountRepository.save(toBank);

	        bankTransferRepository.delete(transfer);
	}

}
