package com.niranjan.service;

import java.util.List;

import com.niranjan.DTO.IncomeDTO;
import com.niranjan.entities.Income;

public interface IncomeService {

	Income postIncome(IncomeDTO incomeDTO);
	
	List<IncomeDTO> getAllIncome();
	
	IncomeDTO getIncomeById(Long id);
	
	List<Income> getAllIncomesByBankName(String bankName);
	
	Income updateIncome(Long id,IncomeDTO incomeDTO);
	
	void deleteIncome(Long id);
	
}
