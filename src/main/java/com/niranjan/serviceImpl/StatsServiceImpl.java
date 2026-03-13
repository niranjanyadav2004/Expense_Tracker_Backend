package com.niranjan.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.niranjan.DTO.StatsDTO;
import com.niranjan.entities.Expense;
import com.niranjan.entities.Income;
import com.niranjan.entities.User;
import com.niranjan.repos.ExpenseRepository;
import com.niranjan.repos.IncomeRepository;
import com.niranjan.service.StatsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

	@Autowired
	private final ExpenseRepository expenseRepository;
	
	@Autowired
	private final IncomeRepository incomeRepository;
	
	private User getCurrentUser() {
	    return (User) SecurityContextHolder
	            .getContext()
	            .getAuthentication()
	            .getPrincipal();
	}

	
	@Override
	public StatsDTO getStats() {
		 
		User user = getCurrentUser();

        Double totalIncome = incomeRepository.sumAllAmountByUser(user).orElse(0.0);
        Double totalExpense = expenseRepository.sumAllAmountByUser(user).orElse(0.0);

        StatsDTO stats = new StatsDTO();
        stats.setIncome(totalIncome);
        stats.setExpense(totalExpense);
        stats.setBalance(totalIncome - totalExpense);

        // Latest records
        incomeRepository.findFirstByUserOrderByDateDesc(user)
                .ifPresent(stats::setLatestIncome);

        expenseRepository.findFirstByUserOrderByDateDesc(user)
                .ifPresent(stats::setLastestExpense);

        // Min/Max
        List<Income> incomeList = incomeRepository.findByUser(user);
        List<Expense> expenseList = expenseRepository.findByUser(user);

        stats.setMinIncome(
                incomeList.stream().mapToDouble(Income::getAmount).min().orElse(0.0));

        stats.setMaxIncome(
                incomeList.stream().mapToDouble(Income::getAmount).max().orElse(0.0));

        stats.setMinExpense(
                expenseList.stream().mapToDouble(Expense::getAmount).min().orElse(0.0));

        stats.setMaxExpense(
                expenseList.stream().mapToDouble(Expense::getAmount).max().orElse(0.0));

        return stats;
	}


	@Override
	public StatsDTO getStatsByBank(String bankName) {
		User user = getCurrentUser();

        Double totalIncome = incomeRepository
                .sumAmountByBankName(bankName, user)
                .orElse(0.0);

        Double totalExpense = expenseRepository
                .sumAmountByBankName(bankName, user)
                .orElse(0.0);

        StatsDTO stats = new StatsDTO();
        stats.setIncome(totalIncome);
        stats.setExpense(totalExpense);
        stats.setBalance(totalIncome - totalExpense);

        // Latest for specific bank
        incomeRepository
                .findFirstByBankAccount_BankNameAndUserOrderByDateDesc(bankName, user)
                .ifPresent(stats::setLatestIncome);

        expenseRepository
                .findFirstByBankAccount_BankNameAndUserOrderByDateDesc(bankName, user)
                .ifPresent(stats::setLastestExpense);

        List<Income> incomeList =
                incomeRepository.findByBankAccount_BankNameAndUser(bankName, user);

        List<Expense> expenseList =
                expenseRepository.findByBankAccount_BankNameAndUser(bankName, user);

        stats.setMinIncome(
                incomeList.stream().mapToDouble(Income::getAmount).min().orElse(0.0));

        stats.setMaxIncome(
                incomeList.stream().mapToDouble(Income::getAmount).max().orElse(0.0));

        stats.setMinExpense(
                expenseList.stream().mapToDouble(Expense::getAmount).min().orElse(0.0));

        stats.setMaxExpense(
                expenseList.stream().mapToDouble(Expense::getAmount).max().orElse(0.0));

        return stats;
	}
	
	
	
}
