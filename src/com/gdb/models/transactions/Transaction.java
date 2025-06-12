package com.gdb.models.transactions;

import java.time.LocalDateTime;
import com.gdb.models.accounts.Account;

public class Transaction {
    private long transactionId;
    private TransactionType transactionType;
    private double amount;
    private Account fromAccount;
	private Account toAccount;
    private LocalDateTime timestamp;
    private TransactionStatus transactionStatus;
    
    public long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	public TransactionType getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Account getFromAccount() {
		return fromAccount;
	}
	public void setFromAccount(Account fromAccount) {
		this.fromAccount = fromAccount;
	}
	public Account getToAccount() {
		return toAccount;
	}
	public void setToAccount(Account toAccount) {
		this.toAccount = toAccount;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public TransactionStatus getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
   
}
