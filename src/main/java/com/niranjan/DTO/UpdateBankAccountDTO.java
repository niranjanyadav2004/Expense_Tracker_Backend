package com.niranjan.DTO;

import lombok.Data;

@Data
public class UpdateBankAccountDTO {

	private String bankName;
	private String accountNumber;
	private Double balance;
	 
}
