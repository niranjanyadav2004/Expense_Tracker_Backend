package com.niranjan.customExceptions;

public class CannotAddExpense extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CannotAddExpense(String message) {
		super(message);
	}
	
}
