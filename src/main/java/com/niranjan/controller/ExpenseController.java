package com.niranjan.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.niranjan.DTO.ExpenseDTO;
import com.niranjan.service.ExpenseServices;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/expense")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ExpenseController {

	@Autowired
	private ExpenseServices expenseServices;
	
	@PostMapping
	public ResponseEntity<?> postExpense(@RequestBody ExpenseDTO expenseDTO){
		return ResponseEntity.ok(expenseServices.postExpense(expenseDTO));
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllExpenses(){
		return ResponseEntity.ok(expenseServices.getAllExpenses());
	}
	
	@GetMapping("/all/{bankName}")
	public ResponseEntity<?> getAllExpensesByBankName(@PathVariable String bankName){
		return ResponseEntity.ok(expenseServices.getAllExpensesByBankName(bankName));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getExpenseById(@PathVariable Long id){
			return ResponseEntity.ok(expenseServices.getExpenseById(id));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateExpense(@PathVariable Long id,@RequestBody ExpenseDTO expenseDTO){
			return ResponseEntity.ok(expenseServices.updateExpense(id, expenseDTO));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteExpense(@PathVariable Long id){
			expenseServices.deleteExpense(id);
			return ResponseEntity.ok(null);
	}
	
}
