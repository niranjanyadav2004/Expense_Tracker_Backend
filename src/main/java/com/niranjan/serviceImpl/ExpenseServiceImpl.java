package com.niranjan.serviceImpl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.niranjan.DTO.ExpenseDTO;
import com.niranjan.customExceptions.BankAccountNotFoundException;
import com.niranjan.customExceptions.CannotAddExpense;
import com.niranjan.customExceptions.ExpenseNotFoundException;
import com.niranjan.entities.BankAccount;
import com.niranjan.entities.Expense;
import com.niranjan.entities.User;
import com.niranjan.repos.BankAccountRepository;
import com.niranjan.repos.ExpenseRepository;
import com.niranjan.repos.IncomeRepository;
import com.niranjan.service.ExpenseServices;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseServices {

	private final ExpenseRepository expenseRepository;
	private final IncomeRepository incomeRepository;
	private final BankAccountRepository bankAccountRepository;
	
	private User getCurrentUser() {
	    return (User) SecurityContextHolder
	            .getContext()
	            .getAuthentication()
	            .getPrincipal();
	}
	
	
	private Expense saveOrUpdateExpense(Expense expense,ExpenseDTO expenseDTO) {
	     expense.setTitle(expenseDTO.getTitle());
	     expense.setDescription(expenseDTO.getDescription());
	     expense.setCategory(expenseDTO.getCategory());
	     expense.setDate(expenseDTO.getDate());
	     expense.setAmount(expenseDTO.getAmount());
	     
	     return expenseRepository.save(expense);
	}
	
	private ExpenseDTO convertToDTO(Expense expense) {

	    return ExpenseDTO.builder()
	            .id(expense.getId())
	            .title(expense.getTitle())
	            .description(expense.getDescription())
	            .category(expense.getCategory())
	            .date(expense.getDate())
	            .amount(expense.getAmount())
	            .bankName(expense.getBankAccount().getBankName())
	            .build();
	}

	@Override
	public ExpenseDTO postExpense(ExpenseDTO expenseDTO) {

	    User currentUser = getCurrentUser();
	    BankAccount bankAccount = bankAccountRepository.findByBankNameAndUser(expenseDTO.getBankName(), currentUser)
				.orElseThrow(()-> new BankAccountNotFoundException("Bank account not found for user" + currentUser));

	    if( incomeRepository.sumAllAmountByUser(currentUser).isEmpty() ) throw new CannotAddExpense("Income is zero. Can't add expense !!");
	    
	    if (bankAccount.getBalance() < expenseDTO.getAmount()) {
            throw new CannotAddExpense("Insufficient balance!");
        }
	    
	    Expense expense = new Expense();
	    expense.setUser(currentUser); 
	    expense.setBankAccount(bankAccount);

	     Expense saveOrUpdateExpense = saveOrUpdateExpense(expense, expenseDTO);
	     
	     bankAccount.setBalance(bankAccount.getBalance() - expenseDTO.getAmount());
	     bankAccountRepository.save(bankAccount);
	     
	     return convertToDTO(saveOrUpdateExpense);
	}

	@Override
	public List<ExpenseDTO> getAllExpenses() {

	    return expenseRepository
	            .findByUser(getCurrentUser())
	            .stream()
	            .sorted(Comparator.comparing(Expense::getDate).reversed())
	            .map(this::convertToDTO)
	            .collect(Collectors.toList());
	}
	
	@Override
	public List<ExpenseDTO> getAllExpensesByBankName(String bankName) {
		BankAccount bankAccount = bankAccountRepository.findByBankNameAndUser(bankName, getCurrentUser())
				 .orElseThrow(()-> new BankAccountNotFoundException("Bank account not found for user" + getCurrentUser()));
		
		return expenseRepository
	            .findAllByUserAndBankAccount(getCurrentUser(), bankAccount)
	            .stream()
	            .sorted(Comparator.comparing(Expense::getDate).reversed())
	            .map(this::convertToDTO)
	            .collect(Collectors.toList());
	}

	@Override
	public ExpenseDTO getExpenseById(Long id) {

	    Expense expense = expenseRepository.findByIdAndUser(id,getCurrentUser())
	            .orElseThrow(() ->
	                    new ExpenseNotFoundException("Expense not found with id " + id));

	    return convertToDTO(expense);
	}

	@Override
	public ExpenseDTO updateExpense(Long id, ExpenseDTO expenseDTO) {
		
		Expense expense = expenseRepository.findByIdAndUser(id, getCurrentUser())
		 				.orElseThrow(() ->
		 						new ExpenseNotFoundException("Expense not found with id " + id));
		
		BankAccount bankAccount = bankAccountRepository.findByBankNameAndUser(expenseDTO.getBankName(), getCurrentUser())
					 .orElseThrow(()-> new BankAccountNotFoundException("Bank account not found for user" + getCurrentUser()));
		  
		Integer oldAmount = expense.getAmount();
	    Integer newAmount = expenseDTO.getAmount();

	    if (newAmount > oldAmount) {
	        Integer diff = newAmount - oldAmount;
	        if (bankAccount.getBalance() < diff) {
	                throw new CannotAddExpense("Insufficient balance!");
	            }
	    }
	    
	    bankAccount.setBalance(bankAccount.getBalance() + oldAmount - newAmount);
	    bankAccountRepository.save(bankAccount);
	    
	    Expense updatedExpense = saveOrUpdateExpense(expense, expenseDTO);

	    return convertToDTO(updatedExpense);
	}

	@Override
	public void deleteExpense(Long id) {
		
		Expense expense = expenseRepository.findByIdAndUser(id, getCurrentUser())
		 				.orElseThrow(() ->
		 						new ExpenseNotFoundException("Expense not found with id " + id));
		 BankAccount bankAccount = expense.getBankAccount();
	        
	     bankAccount.setBalance(bankAccount.getBalance() + expense.getAmount());
	     bankAccountRepository.save(bankAccount);
	     
	     expenseRepository.delete(expense);
	}

}
