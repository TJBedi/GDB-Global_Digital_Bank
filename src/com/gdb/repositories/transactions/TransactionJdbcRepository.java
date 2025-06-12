package com.gdb.repositories.transactions;

import java.sql.*;
import java.time.LocalDateTime;
import com.gdb.database.connection.DatabaseConnection;
import com.gdb.models.accounts.Account;
import com.gdb.models.transactions.Transaction;
import com.gdb.models.transactions.TransactionStatus;
import com.gdb.util.LoggerUtil;

public class TransactionJdbcRepository {

    public void deposit(Transaction transaction) {
        String updateAccountQuery = "UPDATE Account SET Account_Balance = ? WHERE Account_Number = ?";
        String insertTransactionQuery = "INSERT INTO Transactions (Timestamp, From_Account_Number, Amount, Type, Status) VALUES (?, ?, ?, ?, ?)";

        Account account = transaction.getFromAccount();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(updateAccountQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertTransactionQuery)) {

            updateStmt.setDouble(1, account.getBalance());
            updateStmt.setString(2, account.getAccountNumber());
           
            updateStmt.executeUpdate();

            insertStmt.setTimestamp(1, Timestamp.valueOf(transaction.getTimestamp()));
            insertStmt.setString(2, account.getAccountNumber());
            insertStmt.setDouble(3, transaction.getAmount());
            insertStmt.setString(4, String.valueOf(transaction.getTransactionType()));
            insertStmt.setString(5, String.valueOf(transaction.getTransactionStatus()));
            insertStmt.executeUpdate();

            System.out.println("Deposit Transaction Added into Database");

        } catch (SQLException e) {
            e.printStackTrace();
            LoggerUtil.logError(e);
            return;
        }
    }

    public void withdraw(Transaction transaction) {
    	String updateAccountQuery = "UPDATE Account SET Account_Balance = ? WHERE Account_Number = ?";
        String insertTransactionQuery = "INSERT INTO Transactions (Timestamp, From_Account_Number, Amount, Type, Status) VALUES (?, ?, ?, ?, ?)";

        Account account = transaction.getFromAccount();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(updateAccountQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertTransactionQuery)) {

            updateStmt.setDouble(1, account.getBalance());
            updateStmt.setString(2, account.getAccountNumber());
            updateStmt.executeUpdate();

            insertStmt.setTimestamp(1, Timestamp.valueOf(transaction.getTimestamp()));
            insertStmt.setString(2, account.getAccountNumber());
            insertStmt.setDouble(3, transaction.getAmount());
            insertStmt.setString(4, String.valueOf(transaction.getTransactionType()));
            insertStmt.setString(5, String.valueOf(transaction.getTransactionStatus()));
            insertStmt.executeUpdate();

            System.out.println("Withdrawal Transaction Added into Database");

        } catch (SQLException e) {
            e.printStackTrace();
            LoggerUtil.logError(e);
            return;
        }
    }
    
    public void fundTransfer(Transaction transaction) {

        String updateSenderQuery = "UPDATE Account SET Account_Balance = ?,Account_Transfer_Limit = ? WHERE Account_Number = ?";
        String updateReceiverQuery = "UPDATE Account SET Account_Balance = ? WHERE Account_Number = ?";
        
        
        Account fromAccount = transaction.getFromAccount();
        Account toAccount = transaction.getToAccount();

        try (Connection conn = DatabaseConnection.getConnection())
        {
        	PreparedStatement updateSenderStmt = conn.prepareStatement(updateSenderQuery);
            PreparedStatement updateReceiverStmt = conn.prepareStatement(updateReceiverQuery);
            

            updateSenderStmt.setDouble(1, fromAccount.getBalance());
            updateSenderStmt.setDouble(2, fromAccount.getFundTransferLimit());
            updateSenderStmt.setString(3, fromAccount.getAccountNumber()); 
            updateSenderStmt.executeUpdate();

            updateReceiverStmt.setDouble(1, toAccount.getBalance());
            updateReceiverStmt.setString(2, toAccount.getAccountNumber());
            updateReceiverStmt.executeUpdate();
            
            insertIntoTransaction(transaction);
        }
        catch (SQLException e) {
        	System.out.println("Transaction Error: " + e.getMessage());
        	LoggerUtil.logError(e);
        }
    }

	public void insertIntoTransaction(Transaction transaction) {
		String insertTransactionQuery = "INSERT INTO Transactions (Timestamp, From_Account_Number, To_Account_Number, Amount, Type, Status) VALUES (?, ?, ?, ?, ?, ?)";
    	if(transaction.getTransactionStatus()==TransactionStatus.Pending)
    	{
    		transaction.setTransactionStatus(TransactionStatus.Failed);
    	}
		Account fromAccount = transaction.getFromAccount();
        Account toAccount = transaction.getToAccount();
		try (Connection conn = DatabaseConnection.getConnection())
        {
			PreparedStatement insertStmt = conn.prepareStatement(insertTransactionQuery);
			
	            insertStmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
	            insertStmt.setString(2, fromAccount.getAccountNumber());
	            insertStmt.setString(3, toAccount.getAccountNumber());
	            insertStmt.setDouble(4, transaction.getAmount());
	            insertStmt.setString(5, String.valueOf(transaction.getTransactionType()));
	            insertStmt.setString(6, String.valueOf(transaction.getTransactionStatus()));
	            insertStmt.executeUpdate();
	            
        }
		catch (SQLException e) {
        	System.out.println("Transaction Error while inserting into Transactions table: " + e.getMessage());
        	LoggerUtil.logError(e);
		}
	}

}
