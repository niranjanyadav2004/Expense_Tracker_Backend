package com.niranjan.DTO;

import java.time.LocalDate;

import com.niranjan.entities.BankAccount;

import lombok.Data;

@Data
public class IncomeDTO {

	private Long id;

	private String title;

	private Integer amount;

	private LocalDate date;

	private String category;

	private String description;
	
	private String bankName;

}
