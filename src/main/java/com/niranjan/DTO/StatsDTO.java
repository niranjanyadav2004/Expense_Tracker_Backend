package com.niranjan.DTO;

import com.niranjan.entities.Expense;
import com.niranjan.entities.Income;

import lombok.Data;

@Data
public class StatsDTO {

	private Double income;
	
	private Double expense;
	
	private Income latestIncome;
	
	private Expense lastestExpense;
	
	private Double balance;
	
	private Double minIncome;
	
	private Double maxIncome;
	
	private Double minExpense;
	
	private Double maxExpense;
	
}
