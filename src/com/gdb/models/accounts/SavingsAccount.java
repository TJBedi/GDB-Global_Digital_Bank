package com.gdb.models.accounts;

public class SavingsAccount extends Account{
    private int age;
    private String gender;
	private String aadhaarNumber;
    private long phNo;
    public String getAadhaarNumber() {
		return aadhaarNumber;
	}
	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}
	public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public long getPhNo() {
        return phNo;
    }
    public void setPhNo(long phNo) {
        this.phNo = phNo;
    }
}
