package com.niranjan.DTO;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferResponse {

	private Long id;
	private String fromBankName;
	private String toBankName;
	private Double amount;
	private LocalDate date;
	private String description;
	
}
