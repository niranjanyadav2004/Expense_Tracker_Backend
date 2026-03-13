package com.niranjan.serviceImpl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.niranjan.DTO.IncomeDTO;
import com.niranjan.customExceptions.BankAccountNotFoundException;
import com.niranjan.customExceptions.IncomeNotFoundException;
import com.niranjan.entities.BankAccount;
import com.niranjan.entities.Income;
import com.niranjan.entities.User;
import com.niranjan.repos.BankAccountRepository;
import com.niranjan.repos.IncomeRepository;
import com.niranjan.service.IncomeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;
    private final BankAccountRepository bankAccountRepository;

    private User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private Income saveOrUpdateIncome(Income income, IncomeDTO incomeDTO) {
        income.setTitle(incomeDTO.getTitle());
        income.setDate(incomeDTO.getDate());
        income.setAmount(incomeDTO.getAmount());
        income.setCategory(incomeDTO.getCategory());
        income.setDescription(incomeDTO.getDescription());

        return incomeRepository.save(income);
    }

    @Override
    public Income postIncome(IncomeDTO incomeDTO) {
        User currentUser = getCurrentUser();
        BankAccount bankAccount = bankAccountRepository.findByBankNameAndUser(incomeDTO.getBankName(), currentUser)
        											.orElseThrow(()-> new BankAccountNotFoundException("Bank account not found for user" + currentUser));
        

        Income income = new Income();
        income.setUser(currentUser); 
        income.setBankAccount(bankAccount);

       Income saveOrUpdateIncome = saveOrUpdateIncome(income, incomeDTO);
       
       bankAccount.setBalance(bankAccount.getBalance()+incomeDTO.getAmount());
       bankAccountRepository.save(bankAccount);
       return saveOrUpdateIncome;
    }

    @Override
    public List<IncomeDTO> getAllIncome() {

        User currentUser = getCurrentUser();

        return incomeRepository.findByUser(currentUser)
                .stream()
                .sorted(Comparator.comparing(Income::getDate).reversed())
                .map(Income::getIncomeDTO)
                .collect(Collectors.toList());
    }
    
    @Override
	public List<Income> getAllIncomesByBankName(String bankName) {
		BankAccount bankAccount = bankAccountRepository.findByBankNameAndUser(bankName, getCurrentUser())
							 .orElseThrow(()-> new BankAccountNotFoundException("Bank account not found for user" + getCurrentUser()));
		
		return incomeRepository.findAllByUserAndBankAccount(getCurrentUser(), bankAccount);
	}
    
    @Override
    public IncomeDTO getIncomeById(Long id) {

        User currentUser = getCurrentUser();

        Income income = incomeRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() ->
                        new IncomeNotFoundException("Income not found with id " + id));

        return income.getIncomeDTO();
    }

    @Override
    public Income updateIncome(Long id, IncomeDTO incomeDTO) {

        User currentUser = getCurrentUser();
        

        Income income = incomeRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() ->
                        new IncomeNotFoundException("Income not found with id " + id));
        
        BankAccount bankAccount = bankAccountRepository.findByBankNameAndUser(incomeDTO.getBankName(), getCurrentUser())
				 .orElseThrow(()-> new BankAccountNotFoundException("Bank account not found for user" + getCurrentUser()));
        
        Integer oldAmount = income.getAmount();
        Integer newAmount = incomeDTO.getAmount();
        
        bankAccount.setBalance(bankAccount.getBalance() - oldAmount + newAmount);
        bankAccountRepository.save(bankAccount);
 
        return saveOrUpdateIncome(income, incomeDTO);
    }

    @Override
    public void deleteIncome(Long id) {

        User currentUser = getCurrentUser();

        Income income = incomeRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() ->
                        new IncomeNotFoundException("Income not found with id " + id));
        
        BankAccount bankAccount = income.getBankAccount();
        
        bankAccount.setBalance(bankAccount.getBalance() - income.getAmount());
        bankAccountRepository.save(bankAccount);

        incomeRepository.delete(income);
    }
    
}