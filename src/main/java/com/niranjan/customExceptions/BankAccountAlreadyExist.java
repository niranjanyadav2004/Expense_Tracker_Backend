package com.niranjan.customExceptions;

public class BankAccountAlreadyExist extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BankAccountAlreadyExist(String message) {
		super(message);
	}
	
}
