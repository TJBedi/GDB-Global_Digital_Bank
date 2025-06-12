package com.gdb.exceptions;

public class InvalidVerificationException extends Exception {
	private static final long serialVersionUID = 1L;
	public InvalidVerificationException(String message) 
	{
		super(message);
	}
}
