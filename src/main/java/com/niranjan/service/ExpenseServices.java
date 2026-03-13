package com.niranjan.service;

import java.util.List;

import com.niranjan.DTO.ExpenseDTO;

public interface ExpenseServices {
     ExpenseDTO postExpense(ExpenseDTO expenseDTO);
     
     List<ExpenseDTO> getAllExpenses();
     
     ExpenseDTO getExpenseById(Long id);
     
     List<ExpenseDTO> getAllExpensesByBankName(String bankName);
     
     ExpenseDTO updateExpense(Long id,ExpenseDTO expenseDTO);
     
     void deleteExpense(Long id);
}
