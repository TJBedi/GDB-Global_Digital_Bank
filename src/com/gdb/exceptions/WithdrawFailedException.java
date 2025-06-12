package com.gdb.exceptions;

public class WithdrawFailedException extends Exception {
	private static final long serialVersionUID = 1L;

	public WithdrawFailedException(String message) 
	{
		super(message);
	}
}
