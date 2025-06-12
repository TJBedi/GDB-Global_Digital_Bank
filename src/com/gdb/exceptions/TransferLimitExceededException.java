package com.gdb.exceptions;

public class TransferLimitExceededException extends Exception {
	private static final long serialVersionUID = 1L;
	public TransferLimitExceededException(String message) 
	{
		super(message);
	}
}
