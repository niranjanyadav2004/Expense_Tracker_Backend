package com.niranjan.globalExceptionHandler;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.niranjan.customExceptions.BankAccountAlreadyExist;
import com.niranjan.customExceptions.BankAccountNotFoundException;
import com.niranjan.customExceptions.CannotAddExpense;
import com.niranjan.customExceptions.ExpenseNotFoundException;
import com.niranjan.customExceptions.IncomeNotFoundException;
import com.niranjan.customExceptions.RefreshTokenExpiredException;
import com.niranjan.customExceptions.TokenDoesNotExistException;
import com.niranjan.customExceptions.UserAlreadyExistException;
import com.niranjan.customExceptions.UserNotFoundException;

import io.jsonwebtoken.security.SignatureException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CannotAddExpense.class)
	public String cannotAddExpenseExceptionHandler(CannotAddExpense ex) {
		return ex.getMessage();
	}

	@ExceptionHandler(ExpenseNotFoundException.class)
	public String expenseNotFoundExceptionHandler(ExpenseNotFoundException ex) {
		return ex.getMessage();
	}

	@ExceptionHandler(IncomeNotFoundException.class)
	public String incomeNotFoundExceptionHandler(IncomeNotFoundException ex) {
		return ex.getMessage();
	}

	@ExceptionHandler(UserNotFoundException.class)
	public String UserNotFoundExceptionHandler(UserNotFoundException ex) {
		return ex.getMessage();
	}

	@ExceptionHandler(UserAlreadyExistException.class)
	public String UserAlreadyExistExceptionHandler(UserAlreadyExistException ex) {
		return ex.getMessage();
	}

	@ExceptionHandler(SignatureException.class)
	public String UserAlreadyExistExceptionHandler(SignatureException ex) {
		return ex.getMessage();
	}

	@ExceptionHandler(BadCredentialsException.class)
	public String exceptionHandler() {
		return "Credentials Invalid !!";
	}
	
	@ExceptionHandler(BankAccountNotFoundException.class)
	public String bankAccountNotFoundExceptionHandler(BankAccountNotFoundException ex) {
		return ex.getMessage();
	}
	
	@ExceptionHandler(BankAccountAlreadyExist.class)
	public String bankAccountAlreadyExistExceptionHandler(BankAccountAlreadyExist ex) {
		return ex.getMessage();
	}
	
	@ExceptionHandler(TokenDoesNotExistException.class)
	public String tokenDoesNotExistExceptionHandler(TokenDoesNotExistException ex) {
		return ex.getMessage();
	}
	
	@ExceptionHandler(RefreshTokenExpiredException.class)
	public String refreshTokenExpiredExceptionHandler(RefreshTokenExpiredException ex) {
		return ex.getMessage();
	}

	
	
}
