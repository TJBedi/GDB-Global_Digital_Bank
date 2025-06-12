package com.gdb.models.accounts;

import java.time.LocalDateTime;
import java.time.LocalDate;

public class Account {
    private long accountId;
    private String name;
    private LocalDate dateOfBirth;
	private double balance;
    private String accountNumber;
    private AccountStatus accountStatus;
    private AccountType accountType;
    private LocalDateTime activatedDate;
    private LocalDateTime closedDate;
    private int pinNumber;
    private double fundTransferLimit;
    private AccountPrivilege privilege;

	public long getAccountId() {
        return accountId;
    }
    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public AccountStatus getAccountStatus() {
        return accountStatus;
    }
    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }
    public AccountType getAccountType() {
        return accountType;
    }
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    public LocalDateTime getActivatedDate() {
        return activatedDate;
    }
    public void setActivatedDate(LocalDateTime activatedDate) {
        this.activatedDate = activatedDate;
    }
    public LocalDateTime getClosedDate() {
        return closedDate;
    }
    public void setClosedDate(LocalDateTime closedDate) {
        this.closedDate = closedDate;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public int getPinNumber() {
        return pinNumber;
    }
    public void setPinNumber(int pinNumber) {
        this.pinNumber = pinNumber;
    }
    public AccountPrivilege getPrivilege() {
        return privilege;
    }
    public void setPrivelege(AccountPrivilege privilege) {
        this.privilege = privilege;
    }
	public double getFundTransferLimit() {
		return this.fundTransferLimit;
	}
	public void setFundTransferLimit(double fundTransferLimit) {
		this.fundTransferLimit = fundTransferLimit;
	}
}
