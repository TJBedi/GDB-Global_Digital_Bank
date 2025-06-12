package com.gdb.exceptions;

public class AccountNotActiveException extends Exception {
	private static final long serialVersionUID = 1L;
	public AccountNotActiveException(String message) 
	{
		super(message);
	}
}
