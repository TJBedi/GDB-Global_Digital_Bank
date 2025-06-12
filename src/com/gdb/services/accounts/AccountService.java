package com.gdb.services.accounts;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Scanner;
import com.gdb.exceptions.DuplicateAccountException;
import com.gdb.exceptions.InvalidAgeException;
import com.gdb.exceptions.InvalidGstNoException;
import com.gdb.exceptions.InvalidInitialDepositException;
import com.gdb.exceptions.InvalidRegNoException;
import com.gdb.exceptions.InvalidVerificationException;
import com.gdb.models.accounts.Account;
import com.gdb.models.accounts.AccountStatus;
import com.gdb.models.accounts.CurrentAccount;
import com.gdb.models.accounts.SavingsAccount;
import com.gdb.repositories.accounts.AccountJdbcRepository;
import com.gdb.repositories.transactions.TransactionConfigurationRepository;
import com.gdb.ui.AccountUI;
import com.gdb.util.InputUtil;
import com.gdb.util.LoggerUtil;

import IntegrationsExternalPackage.AadhaarVerificationServiceImpl;
import IntegrationsExternalPackage.GstVerificationServiceImpl;
import IntegrationsExternalPackage.RegNoVerificationServiceImpl;

public class AccountService {
	private AccountJdbcRepository accountJdbcRepository=new AccountJdbcRepository();
	public void openAccount(SavingsAccount account) {			
		try {
			checkAgeEligibility(account);
			checkAadharVerification(account);
			System.out.println("Checking Aadhar Number for duplicate accounts...");
			checkForDuplicateAccount(account);
			checkInitialDeposit(account);
			
			account.setAccountStatus(AccountStatus.Active);
			account.setActivatedDate(LocalDateTime.now());
			account.setAccountNumber(accountNumberGeneration(account));
			AccountUI.setPin(account);
			AccountUI.setAccountPrivilege(account);
			account.setFundTransferLimit(TransactionConfigurationRepository.getTransferLimit(account.getPrivilege()));
			accountJdbcRepository.insertIntoAccount(account);
			System.out.println("Savings Account created successfully.");
		}
		catch(InvalidInitialDepositException | InvalidVerificationException | 
		      InvalidAgeException | DuplicateAccountException e) {
			System.out.println("Can't create account due to conditions not met.\nCause: " + e.getMessage());
			LoggerUtil.logError(e);
			return;
		}
	}
	
	public void openAccount(CurrentAccount account) {
		try {
			checkRegistrationNumber(account);
            checkGSTNumber(account);
            System.out.println("Checking GST Number for duplicate accounts...");
            checkForDuplicateAccount(account);
            checkInitialDeposit(account);
            
			account.setAccountStatus(AccountStatus.Active);
			account.setActivatedDate(LocalDateTime.now());
			account.setAccountNumber(accountNumberGeneration(account));
			AccountUI.setPin(account);
			AccountUI.setAccountPrivilege(account);
			account.setFundTransferLimit(TransactionConfigurationRepository.getTransferLimit(account.getPrivilege()));
			accountJdbcRepository.insertIntoAccount(account);
			System.out.println("Current Account created successfully.");
		}
		catch(InvalidInitialDepositException | InvalidGstNoException | 
		      InvalidRegNoException | DuplicateAccountException e) {
			System.out.println("Can't create account due to conditions not met.\nCause: " + e.getMessage());
			LoggerUtil.logError(e);
			return;
		}
	}
	public String accountNumberGeneration(Account account) {
		String prefix = account.getName().substring(0, Math.min(3, account.getName().length())).toUpperCase();
		String suffix = (account instanceof SavingsAccount) ? 
			((SavingsAccount) account).getAadhaarNumber().substring(8) :
			((CurrentAccount) account).getGstNo().substring(11);
		return prefix + suffix;
	}
	
	
	private void checkGSTNumber(CurrentAccount account) throws InvalidGstNoException {
		GstVerificationServiceImpl service = new GstVerificationServiceImpl();
        if(!(service.checkGstNo(account.getGstNo())))
        	throw new InvalidGstNoException("Invalid GST number!");		
	}
	
	private void checkRegistrationNumber(CurrentAccount account) throws InvalidRegNoException {
		RegNoVerificationServiceImpl service = new RegNoVerificationServiceImpl();
        if(!(service.checkRegNo(account.getRegNo())))
        	throw new InvalidRegNoException("Invalid Registration number!");	
	}
	
	private void checkInitialDeposit(SavingsAccount account) throws InvalidInitialDepositException {
		if (account.getBalance() >= 2000) {
			return;
		}
		
		System.out.println("For Savings Account you need to deposit at least 2000 in order to open account.\nDo you wish to proceed?(1-Yes|2-No):");
		Scanner in = InputUtil.getScanner();
			int choice = AccountUI.getValidChoiceInput(in, 1, 2);
			if(choice == 1)
				account.setBalance(2000);
			
			if(account.getBalance() >= 2000)
				return;
		
		throw new InvalidInitialDepositException("Insufficient balance to open the account. Couldn't open Savings account.");
	}
	
	private void checkInitialDeposit(CurrentAccount account) throws InvalidInitialDepositException {
		if (account.getBalance() >= 10000) {
			return;
		}
		
		System.out.println("For Current Account you need to deposit at least 10000 in order to open account.\nDo you wish to proceed?(1-Yes|2-No):");
		Scanner in = InputUtil.getScanner();
			int choice = AccountUI.getValidChoiceInput(in, 1, 2);
			if(choice == 1)
				account.setBalance(10000);
			
			if(account.getBalance() >= 10000)
				return;
		
		throw new InvalidInitialDepositException("Insufficient balance to open the account. Couldn't open Current account.");
	}
	private void checkAadharVerification(SavingsAccount account) throws InvalidVerificationException {
		try {
	        Scanner in = InputUtil.getScanner();
	        
	        System.out.println("Enter your Aadhaar number for Identification:");
	        String aadhaar = in.nextLine();
	        
	        AadhaarVerificationServiceImpl service = new AadhaarVerificationServiceImpl();
	        if(aadhaar.length()!=12 ||aadhaar.equals(""))
	        	throw new Exception("Invalid aadhaar format.");
	        if(service.checkAadhaar(aadhaar))
	            account.setAadhaarNumber(aadhaar);
	        else
	            throw new InvalidVerificationException("Invalid Verification Number!");
	    }
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			LoggerUtil.logError(e);
		}
	}
	
	private void checkAgeEligibility(SavingsAccount account) throws InvalidAgeException {
		if(account.getAge() < 18) {
			throw new InvalidAgeException("You are Underage. Can't open savings account.");
		}
	}
	
	private void checkForDuplicateAccount(SavingsAccount account) throws DuplicateAccountException {
		if(!accountJdbcRepository.isAadhaarUnique(account.getAadhaarNumber())) {
			System.out.println("Found an account with that Aadhaar number!");
			throw new DuplicateAccountException("Error! There already exist an account with that Aadhaar Number."); 			
		}
			
		System.out.println("Unique Aadhaar Number!");
		return;
	}
	
	private void checkForDuplicateAccount(CurrentAccount account) throws DuplicateAccountException {
		if(!accountJdbcRepository.isGstUnique(account.getGstNo())) {
			System.out.println("Found an account with that GST number!");
			throw new DuplicateAccountException("Error! There already exist an account with that GST Number."); 
		}
		System.out.println("Unique GST Number!");
		return;
	}
	
	public void closeAccount(String accNo) {
		    if (accNo == null || accNo.isEmpty()) {
		        System.out.println("Invalid account number provided.");
		        return;
		    }

		    Account account=accountJdbcRepository.getSavingsAccountByNumber(accNo);
		    
		    if (account == null) {
		        account = accountJdbcRepository.getCurrentAccountByNumber(accNo);
		    }

		    if (account == null) {
		        System.out.println("Account Not Found.");
		        return;
		    }

		    if (verifyPinAndClose(account)) {
		    	accountJdbcRepository.updateAndCloseAccount(accNo, AccountStatus.InActive, LocalDateTime.now());
		    }
		}
	
	private boolean verifyPinAndClose(Account account) {
	    System.out.println("Enter pin number:");
	    Scanner in = InputUtil.getScanner();
	    try {
	        int pin = in.nextInt();
	        if (pin == account.getPinNumber()) {
	            account.setAccountStatus(AccountStatus.InActive);
	            account.setClosedDate(LocalDateTime.now());
	            System.out.println("Account terminated successfully.");
	            return true;
	        } else {
	            System.out.println("Invalid Pin Entered! Can't close Account.");
	        }
	    } catch (InputMismatchException e) {
	        System.out.println("Invalid input format. Please enter a numeric PIN.");
	        in.nextLine(); // Clear invalid input
	        verifyPinAndClose(account);
	    } catch (Exception e) {
	        System.out.println("An error occurred: " + e.getMessage());
	        LoggerUtil.logError(e);
	    }
	    return false;
	}
}