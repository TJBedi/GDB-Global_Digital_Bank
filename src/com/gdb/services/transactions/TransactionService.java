package com.gdb.services.transactions;

import java.time.LocalDateTime;
import com.gdb.exceptions.AccountNotActiveException;
import com.gdb.exceptions.AccountNotFoundException;
import com.gdb.exceptions.InsufficientFundsException;
import com.gdb.exceptions.InvalidPinNoException;
import com.gdb.exceptions.TransferLimitExceededException;
import com.gdb.models.accounts.Account;
import com.gdb.models.accounts.AccountStatus;
import com.gdb.models.transactions.Transaction;
import com.gdb.models.transactions.TransactionStatus;
import com.gdb.models.transactions.TransactionType;
import com.gdb.repositories.accounts.AccountJdbcRepository;
import com.gdb.repositories.transactions.TransactionJdbcRepository;
import com.gdb.ui.AccountUI;
import com.gdb.util.LoggerUtil;

public class TransactionService {
     AccountJdbcRepository accountJdbcRepository = new AccountJdbcRepository();
     TransactionJdbcRepository transactionJdbcRepository = new TransactionJdbcRepository();
     public void withdraw(String accNo, int pin, int amount) {
        try {
            if (amount <= 0)
                throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
            
            	Account account = null; 
            	account=accountJdbcRepository.getSavingsAccountByNumber(accNo);
                if(account==null) 
                	
                	account=accountJdbcRepository.getCurrentAccountByNumber(accNo);
                if(account==null)
                	throw new AccountNotFoundException("No account found with the given account number. Can't withdraw.");
                accountIsActive(account);
                pinNumberIsValid(account,pin);
                accountHasSufficientFunds(account,amount);
                
                withdrawFromAccount(account, amount);

	            Transaction transaction = new Transaction();
	            transaction.setTransactionType(TransactionType.Withdraw);
	            transaction.setTimestamp(LocalDateTime.now());
	            transaction.setAmount(amount);
	            transaction.setFromAccount(account);
	            transaction.setTransactionStatus(TransactionStatus.Successful);

	            transactionJdbcRepository.withdraw(transaction);
	            
	            System.out.println("Withdrawal Successful.");
	    		System.out.println("Amount After Withdraw: "+account.getBalance());
        } 
        catch (IllegalArgumentException | AccountNotFoundException | InsufficientFundsException | InvalidPinNoException | AccountNotActiveException e) {
            System.out.println(e.getMessage());
            System.out.println("Returning to Main Menu");
            LoggerUtil.logError(e);
            AccountUI.showMenu();
        }
    }
     public void deposit(String accNo, int pin, int amount) {
         try {
         	if (amount <= 0)
               throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
             
           	Account account = null; 
            	account=accountJdbcRepository.getSavingsAccountByNumber(accNo);
             if(account==null) 
                	account=accountJdbcRepository.getCurrentAccountByNumber(accNo);
             if(account==null)
                	throw new AccountNotFoundException("No account found with the given account number. Can't deposit.");
            accountIsActive(account);
            pinNumberIsValid(account, pin);
            depositToAccount(account, amount);

 	       Transaction transaction = new Transaction();
 	       transaction.setTransactionType(TransactionType.Deposit);
 	       transaction.setTimestamp(LocalDateTime.now());
 	       transaction.setAmount(amount);
 	       transaction.setFromAccount(account);
 	       transaction.setTransactionStatus(TransactionStatus.Successful);

 	       transactionJdbcRepository.deposit(transaction);
 	       System.out.println("Deposit Successful.");
 	       System.out.println("Amount After Deposit: "+account.getBalance()); 
         }
         catch (AccountNotFoundException | AccountNotActiveException | InvalidPinNoException | IllegalArgumentException e) {
         	System.out.println(e.getMessage());
             System.out.println("Returning to Main Menu");
             LoggerUtil.logError(e);
             AccountUI.showMenu();
         }
     }
 	public void transferFunds(String fromAccNo, String toAccNo, int pin, int amount) {
        Transaction transaction = new Transaction();
	      transaction.setTimestamp(LocalDateTime.now());
	      transaction.setTransactionType(TransactionType.Transfer);
	      transaction.setTransactionStatus(TransactionStatus.Pending);
        try {
            if (amount <= 0) 
                throw new IllegalArgumentException("Transfer amount must be greater than zero");
             transaction.setAmount(amount);
            	Account fromAccount = null; 
             	fromAccount=accountJdbcRepository.getSavingsAccountByNumber(fromAccNo);
              if(fromAccount==null) 
                 	fromAccount=accountJdbcRepository.getCurrentAccountByNumber(fromAccNo);
              if(fromAccount==null)
                 	throw new AccountNotFoundException("No account found with your account number. Can't Transfer.");
           
	 	      transaction.setAmount(amount);
	 	      transaction.setFromAccount(fromAccount);
	 	      
             accountIsActive(fromAccount);
             pinNumberIsValid(fromAccount, pin);
             accountHasSufficientFunds(fromAccount,amount);
             checkTransferLimit(fromAccount,amount);
             Account toAccount = null; 
         	toAccount=accountJdbcRepository.getSavingsAccountByNumber(toAccNo);
	          if(toAccount==null) 
	             	toAccount=accountJdbcRepository.getCurrentAccountByNumber(toAccNo);
	          if(toAccount==null)
	             	throw new AccountNotFoundException("No account found with the recepient account number. Can't Transfer");
	         accountIsActive(toAccount);
             withdrawFromAccount(fromAccount, amount);
	         depositToAccount(toAccount, amount);
	         fromAccount.setFundTransferLimit(fromAccount.getFundTransferLimit()-amount);
	 	     transaction.setToAccount(toAccount);
	 	     transaction.setTransactionStatus(TransactionStatus.Successful);
	         transactionJdbcRepository.fundTransfer(transaction);
	         System.out.println("Funds transferred successfully!");
	 	     System.out.println("Your Balance After Deposit: "+fromAccount.getBalance());
        }
        catch (AccountNotFoundException | AccountNotActiveException | InvalidPinNoException | 
                InsufficientFundsException | TransferLimitExceededException | IllegalArgumentException e) {
        	
        	System.out.println(e.getMessage());
        	LoggerUtil.logError(e);
        	transactionJdbcRepository.insertIntoTransaction(transaction);
        }

    }
    private void checkTransferLimit(Account account, int amount) throws TransferLimitExceededException{
    	if(account.getFundTransferLimit()<amount)
    		throw new TransferLimitExceededException("Can't Transfer");
	}
	private void withdrawFromAccount(Account account, int amount) {
        account.setBalance(account.getBalance() - amount);
    }
	private void depositToAccount(Account account, int amount) {
	    account.setBalance(account.getBalance() + amount);
	}
    private void accountHasSufficientFunds(Account account, int amount) throws InsufficientFundsException {
		if(account.getBalance()<amount)
			throw new InsufficientFundsException("Not Enough money in your Account. Can't withdraw.");
		
	}

	private void pinNumberIsValid(Account account, int pin) throws InvalidPinNoException {
		if(account.getPinNumber()!=pin)
			throw new InvalidPinNoException("The pin number you entered is invalid.");	
	}

	private void accountIsActive(Account account) throws AccountNotActiveException{
		if(account.getAccountStatus()==AccountStatus.InActive)
			throw new AccountNotActiveException("The account number you entered is not active anymore. Can't perform transactions.");
	}
}