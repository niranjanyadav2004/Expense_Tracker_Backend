package com.niranjan.DTO;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpenseDTO {

	private Long id;
	
	private String title;
	
	private String description;
	
	private String category;
	
	private LocalDate date;
	
	private Integer amount;
	
	private String bankName;
	
}
