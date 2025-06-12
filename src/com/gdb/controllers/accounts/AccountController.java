package com.gdb.controllers.accounts;

import com.gdb.exceptions.InvalidAgeException;
import com.gdb.exceptions.InvalidGstNoException;
import com.gdb.exceptions.InvalidPhNoException;
import com.gdb.exceptions.NullValueException;
import com.gdb.models.accounts.AccountStatus;
import com.gdb.models.accounts.CurrentAccount;
import com.gdb.models.accounts.SavingsAccount;
import com.gdb.services.accounts.AccountService;
import com.gdb.ui.AccountUI;
import com.gdb.util.LoggerUtil;

public class AccountController {
	private AccountService accountService = new AccountService(); 
	
	public void openAccount(SavingsAccount account) {
		account.setAccountStatus(AccountStatus.InActive);
		try {
			if(account.getName() == null || account.getName().equals(""))
				throw new NullValueException("Error! Name is empty.");
			if(account.getAge() > 100 || account.getAge() <= 0)
				throw new InvalidAgeException("Error! Invalid age.");
			if(Long.toString(account.getPhNo()).length() != 10)
				throw new InvalidPhNoException("Error! Invalid Phone No.");
		}
		catch(NullValueException | InvalidAgeException | InvalidPhNoException e) {
			System.out.println(e.getMessage() + " Can't create Account. Enter details again");
			LoggerUtil.logError(e);
			AccountUI.acceptSavingsAccountInfo();
		}
		accountService.openAccount(account);
	}
	
	public void openAccount(CurrentAccount account) {
		account.setAccountStatus(AccountStatus.InActive);
		try {
			if(account.getName() == null || account.getName().equals(""))
				throw new NullValueException("Error! Name is empty.");
			if(account.getBusinessName() == null || account.getBusinessName().equals(""))
				throw new NullValueException("Error! Business Name is empty.");
			if(account.getRegNo() == null || account.getRegNo().equals(""))
				throw new NullValueException("Error! Registration number is empty.");
			if(account.getGstNo() == null || account.getGstNo().length() != 15)
				throw new InvalidGstNoException("Error! Invalid GST number entered.");
		}
		catch(NullValueException | InvalidGstNoException e) {
			System.out.println(e.getMessage() + " Can't create Account. Enter details again");
			LoggerUtil.logError(e);
			AccountUI.acceptCurrentAccountInfo();
		}
		accountService.openAccount(account);
	}
	
	public void setPin(int pin, SavingsAccount account) {
		if(pin < 1000 || pin > 9999) {
			System.out.println("Four digits only! Try again");
			AccountUI.setPin(account);
		}
		account.setPinNumber(pin);
	}
	
	public void setPin(int pin, CurrentAccount account) {
		if(pin < 1000 || pin > 9999) {
			System.out.println("Four digits only! Try again");
			AccountUI.setPin(account);
		}
		account.setPinNumber(pin);
	}
	
	public void closeAccount(String accountNumber) {
		try {
			if(accountNumber == null || accountNumber.equals(""))
				throw new NullValueException("Error! Account number is empty.");
		}
		catch(NullValueException e) {
			System.out.println(e.getMessage() + " Can't close Account.");
			LoggerUtil.logError(e);
			return;
		}
		accountService.closeAccount(accountNumber);
	}
}