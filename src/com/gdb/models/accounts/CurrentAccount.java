package com.gdb.models.accounts;

public class CurrentAccount extends Account{
    private String businessName;
    private String regNo;
    private String website;
    private String gstNo;
    public String getRegNo() {
        return regNo;
    }
    public void setRegNo(String regno) {
        this.regNo = regno;
    }
    public String getBusinessName() {
        return businessName;
    }
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        this.website = website;
    }
    public String getGstNo() {
        return gstNo;
    }
    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }
}
