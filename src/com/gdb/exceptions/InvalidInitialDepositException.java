package com.gdb.exceptions;

public class InvalidInitialDepositException extends Exception {
	private static final long serialVersionUID = 1L;
	public InvalidInitialDepositException(String message) 
	{
		super(message);
	}
}
