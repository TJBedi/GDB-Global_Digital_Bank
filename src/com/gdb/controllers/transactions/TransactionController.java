package com.gdb.controllers.transactions;

import com.gdb.exceptions.TransferFailedException;
import com.gdb.exceptions.WithdrawFailedException;
import com.gdb.services.transactions.TransactionService;
import com.gdb.util.LoggerUtil;

public class TransactionController {
	private TransactionService transactionService = new TransactionService();
	public void withdraw(String accNo, int pin, int amount) {
		try {
			if(accNo == null || accNo.equals(""))
				throw new WithdrawFailedException("Error! Account number can't be null.");
			if(pin < 1000 || pin > 9999)
				throw new WithdrawFailedException("Error! Pin number has only 4 digits and can't be negative.");
			if(amount < 1)
				throw new WithdrawFailedException("Error! Amount can't be negative.");
		}
		catch(WithdrawFailedException e) {
			System.out.println(e.getMessage() + " Can't withdraw.");
			LoggerUtil.logError(e);
			return;
		}
		transactionService.withdraw(accNo, pin, amount);
	}
	
	public void deposit(String accNo, int pin, int amount) {
		try {
			if(accNo == null || accNo.equals(""))
				throw new WithdrawFailedException("Error! Account number can't be null.");
			if(pin < 1000 || pin > 9999)
				throw new WithdrawFailedException("Error! Pin number has only 4 digits and can't be negative.");
			if(amount < 1)
				throw new WithdrawFailedException("Error! Amount can't be negative.");
		}
		catch(WithdrawFailedException e) {
			System.out.println(e.getMessage() + " Can't Deposit.");
			LoggerUtil.logError(e);
			return;
		}
		transactionService.deposit(accNo, pin, amount);
	}
	
	public void transfer(String fromAccNo, String toAccNo, int pin, int amount) {
		try {
			if(fromAccNo == null || fromAccNo.equals("") || toAccNo == null || toAccNo.equals(""))
				throw new TransferFailedException("Error! Account number can't be null.");
			if(pin < 1000 || pin > 9999)
				throw new TransferFailedException("Error! Pin number has only 4 digits and can't be negative.");
			if(amount < 1)
				throw new TransferFailedException("Error! Amount can't be negative.");
		}
		catch(TransferFailedException e) {
			System.out.println(e.getMessage() + " Can't Transfer.");
			LoggerUtil.logError(e);
			return;
		}
		transactionService.transferFunds(fromAccNo, toAccNo, pin, amount);
	}
}
