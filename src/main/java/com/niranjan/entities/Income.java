package com.niranjan.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.niranjan.DTO.IncomeDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Income {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	
	private Integer amount;
	
	private LocalDate date;
	
	private String category;
	
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bank_account_id", nullable = false)
	@JsonIgnore
	private BankAccount bankAccount;
	
	public IncomeDTO getIncomeDTO() {
		IncomeDTO incomeDTO = new IncomeDTO();
		incomeDTO.setId(id);
		incomeDTO.setTitle(title);
		incomeDTO.setAmount(amount);
		incomeDTO.setDate(date);
		incomeDTO.setCategory(category);
		incomeDTO.setDescription(description);
		incomeDTO.setBankName(bankAccount.getBankName());
		
		return incomeDTO;
	}
	
}
