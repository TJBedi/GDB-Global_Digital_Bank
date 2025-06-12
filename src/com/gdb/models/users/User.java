package com.gdb.models.users;

import java.time.LocalDate;
import java.util.List;
import com.gdb.models.accounts.Account;

public class User {
    private String loginId;
    private String password;
    private String accountId;
    private String name;
    private String email;
    private long phoneNumber;
    private Account account;
    private RoleType role;
    private List<Verification> verifications;
    private LocalDate createdDate;
    private LocalDate updatedDate;
    public String getLoginId() {
        return loginId;
    }
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public long getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
    public RoleType getRole() {
        return role;
    }
    public void setRole(RoleType role) {
        this.role = role;
    }
    public List<Verification> getVerifications() {
        return verifications;
    }
    public void setVerifications(List<Verification> verifications) {
        this.verifications = verifications;
    }
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
    public LocalDate getUpdatedDate() {
        return updatedDate;
    }
    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }

}

