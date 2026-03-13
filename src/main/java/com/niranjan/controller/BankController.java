package com.niranjan.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niranjan.DTO.CreateNewBank;
import com.niranjan.DTO.UpdateBankAccountDTO;
import com.niranjan.service.BankService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bank")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BankController {

	private final BankService bankService;
	
	@PostMapping("/create")
	public ResponseEntity<?> createBankAccount(@RequestBody CreateNewBank request){
		return ResponseEntity.status(HttpStatus.CREATED).body(bankService.createBankAccount(request));
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<?> getAllBankAccounts(){
		return ResponseEntity.status(HttpStatus.OK).body(bankService.getAllBankAccounts());
	}
	
	@GetMapping("/get/name/{bankName}")
	public ResponseEntity<?> getBankAccountByBankName(@PathVariable String bankName){
		return ResponseEntity.status(HttpStatus.OK).body(bankService.getBankAccountByBankName(bankName));
	}
	
	@GetMapping("/get/accountNumber/{accountNumber}")
	public ResponseEntity<?> getBankAccountByAccountNumber(@PathVariable String accountNumber){
		return ResponseEntity.status(HttpStatus.OK).body(bankService.getBankAccountByAccountNumber(accountNumber));
	}
	
	@GetMapping("/get/user")
	public ResponseEntity<?> getAllBankAccountsByUser(){
		return ResponseEntity.status(HttpStatus.OK).body(bankService.getAllBankAccountsByUser());
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateBank(@PathVariable String id ,@RequestBody UpdateBankAccountDTO request){
		return ResponseEntity.status(HttpStatus.OK).body(bankService.updateBank(id, request));
	}
	
	@DeleteMapping("/delete/{accountNumber}")
	public ResponseEntity<?> deleBankAccount(@PathVariable String accountNumber){
		bankService.deleteBankAccount(accountNumber);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
}
