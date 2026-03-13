package com.niranjan.customExceptions;

public class TokenDoesNotExistException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public TokenDoesNotExistException(String message) {
		super(message);
	}

}
