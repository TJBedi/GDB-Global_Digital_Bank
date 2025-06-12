package com.gdb.exceptions;

public class TransferFailedException extends Exception {
	private static final long serialVersionUID = 1L;
	public TransferFailedException(String message) 
	{
		super(message);
	}
}
