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

import com.niranjan.DTO.TransferDTO;
import com.niranjan.service.BankTransferService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BankTransferController {

	private final BankTransferService transferService;
	
	@PostMapping
	public ResponseEntity<?> transfer(@RequestBody TransferDTO transferDTO){
		transferService.transferAmount(transferDTO);
		return ResponseEntity.status(HttpStatus.OK).body("Amount Transfered Successfully");
	}
	
	@GetMapping("/{bankName}")
	public ResponseEntity<?> getTransferByBank(@PathVariable String bankName){
		return ResponseEntity.status(HttpStatus.OK).body(transferService.getTransfersByBankName(bankName));
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllTransfer(){
		return ResponseEntity.status(HttpStatus.OK).body(transferService.getAllTranfer());
	}
	
	@PutMapping("/{id}")
    public ResponseEntity<?> updateTransfer(@PathVariable Long id, @RequestBody TransferDTO transferDTO){	
    	return ResponseEntity.status(HttpStatus.OK).body(transferService.updateTransfer(id, transferDTO));
    }
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteTransfer(@PathVariable Long id){
		transferService.deleteTransfer(id);
		return ResponseEntity.status(HttpStatus.OK).body("Transfer deleted successfully");
	}
	
}
