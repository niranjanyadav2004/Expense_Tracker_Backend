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

import com.niranjan.DTO.IncomeDTO;
import com.niranjan.entities.Income;
import com.niranjan.service.IncomeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/income")
@RequiredArgsConstructor
@CrossOrigin("*")
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<Income> createIncome(@RequestBody IncomeDTO incomeDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(incomeService.postIncome(incomeDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllIncome() {
        return ResponseEntity.ok(incomeService.getAllIncome());
    }

    @GetMapping("/all/{bankName}")
    public ResponseEntity<?> getAllIncomeByBankName(@PathVariable String bankName){
    	return ResponseEntity.status(HttpStatus.OK).body(incomeService.getAllIncomesByBankName(bankName));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getIncomeById(@PathVariable Long id) {
            return ResponseEntity.ok(incomeService.getIncomeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateIncome(
            @PathVariable Long id,
            @RequestBody IncomeDTO incomeDTO) {
            return ResponseEntity.ok(incomeService.updateIncome(id, incomeDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIncome(@PathVariable Long id) {
            incomeService.deleteIncome(id);
            return ResponseEntity.noContent().build();
    }
}