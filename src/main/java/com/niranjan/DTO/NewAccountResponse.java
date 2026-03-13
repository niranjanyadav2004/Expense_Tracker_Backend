package com.niranjan.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewAccountResponse {

	private String id;
	private String bankName;
	private String accountNumber;
	private Double balance;
	private String accountHolder;
	
}
