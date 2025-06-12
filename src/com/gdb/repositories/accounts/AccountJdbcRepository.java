package com.gdb.repositories.accounts;

import java.sql.*;
import java.time.LocalDateTime;

import com.gdb.database.connection.DatabaseConnection;
import com.gdb.models.accounts.*;
import com.gdb.ui.AccountUI;
import com.gdb.util.LoggerUtil;

public class AccountJdbcRepository {

    // 🔹 Check if Account Exists
     public boolean accountExists(String accountNumber) {
        String query = "SELECT COUNT(*) FROM account WHERE Account_Number = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
             
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // Account already exists
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace(); // Print stack trace to debug
            LoggerUtil.logError(e);
        }
        return false; // Account does not exist
    }
    // 🔹 Insert New Account (Only if it doesn't exist)
    public void insertIntoAccount(Account account) {

        String insertQuery = "INSERT INTO account (Account_Name, Date_of_Birth, Account_Type, Account_Balance, Account_Status, Account_Privilege, Account_Transfer_Limit, Activated_Date,Closed_Date,Pin_Number,Account_Number) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, account.getName());
            ps.setDate(2, java.sql.Date.valueOf(account.getDateOfBirth()));
            ps.setString(3, String.valueOf(account.getAccountType()));
            ps.setDouble(4, account.getBalance());
            ps.setString(5, String.valueOf(account.getAccountStatus()));
            ps.setString(6, String.valueOf(account.getPrivilege()));
            ps.setDouble(7, account.getFundTransferLimit());
            ps.setTimestamp(8, java.sql.Timestamp.valueOf(account.getActivatedDate()));
            ps.setTimestamp(9, null);
            ps.setInt(10, account.getPinNumber());
            ps.setString(11, account.getAccountNumber());

            int rowsAffected = ps.executeUpdate();
            int accountId=0;
            System.out.println(rowsAffected + " Account Added to Database Successfully");
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    accountId= rs.getInt(1); 
                }
            }
            account.setAccountId(accountId);
            int temp=1000+accountId;
            account.setAccountNumber(account.getAccountNumber()+temp);
            updateAccountNumber(accountId,account.getAccountNumber());
            insertIntoSavingsOrCurrent(account);

        } catch (SQLException e) {
        	System.out.println("Database error: " + e.getMessage()); 
            e.printStackTrace(); // Print stack trace to debug
            LoggerUtil.logError(e);
            AccountUI.showMenu();
        }
    }
    public void updateAccountNumber(int accountId, String accountNo) {
        String query = "UPDATE account SET Account_Number = ? WHERE Account_Id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, accountNo);
            ps.setInt(2, accountId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LoggerUtil.logError(e);
        }
    }


    // 🔹 Insert Into Savings or Current Account (Only if Account Exists)
    public void insertIntoSavingsOrCurrent(Account account1) {

        if (account1 instanceof SavingsAccount) {
        	SavingsAccount account=(SavingsAccount)account1;
            String insertQuery = "INSERT INTO Savings_Account (Account_Id, Phone_Number, Aadhaar_Number, Gender) VALUES (?,?,?,?)";

            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(insertQuery)) {

                ps.setLong(1, account.getAccountId());
                ps.setLong(2, account.getPhNo());
                ps.setString(3, ((SavingsAccount) account).getAadhaarNumber());
                ps.setString(4, account.getGender());

                int rowsAffected = ps.executeUpdate();
                System.out.println(rowsAffected + " Savings Account Added to Database Successfully");

            } catch (SQLException e) {
            	System.out.println("Database error: " + e.getMessage()); 
                e.printStackTrace(); // Print stack trace to debug
                LoggerUtil.logError(e);
                AccountUI.showMenu();
            }
        } else if (account1 instanceof CurrentAccount) {
        	CurrentAccount account=(CurrentAccount)account1;
            String insertQuery = "INSERT INTO Current_Account (Account_Id, GST_Number, Business_Name, Business_Registration_Number, Business_Website) VALUES (?,?,?,?,?)";

            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(insertQuery)) {

                ps.setLong(1, account.getAccountId());
                ps.setString(2, ((CurrentAccount) account).getGstNo());
                ps.setString(3, ((CurrentAccount) account).getBusinessName());
                ps.setString(4, ((CurrentAccount) account).getRegNo());
                ps.setString(5, ((CurrentAccount) account).getWebsite());

                int rowsAffected = ps.executeUpdate();
                System.out.println(rowsAffected + " Current Account Added to Database Successfully");

            } catch (SQLException e) {
            	System.out.println("Database error: " + e.getMessage()); 
                e.printStackTrace(); // Print stack trace to debug
                LoggerUtil.logError(e);
                AccountUI.showMenu();
            }
        }
    }
    public boolean isAadhaarUnique(String aadhaarNumber) {
        String query = "SELECT COUNT(*) FROM Savings_Account WHERE Aadhaar_Number = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, aadhaarNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false; 
            }
        } catch (SQLException e) {
            System.out.println("Database error while checking Aadhaar uniqueness: " + e.getMessage()); 
            e.printStackTrace(); // Print stack trace to debug
            LoggerUtil.logError(e);
        }
        
        return true; 
    }
   
    public boolean isGstUnique(String gstNumber) {
        String query = "SELECT COUNT(*) FROM Current_Account WHERE GST_Number = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, gstNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return false; // GST number already exists
            }
        } catch (SQLException e) {
            System.out.println("Database error while checking GST uniqueness: " + e.getMessage());
            e.printStackTrace(); // Print stack trace to debug
            LoggerUtil.logError(e);
        }
        
        return true; // GST number is unique
    }
    public SavingsAccount getSavingsAccountByNumber(String accountNumber) {
    	if(accountExists(accountNumber))
    	{
	        String query = "SELECT a.Account_Id, a.Account_Number, a.Account_Name, a.Account_Type, " +
	                       "a.Account_Balance, a.Account_Status, a.Account_Privilege, a.Account_Transfer_Limit, " +
	                       "a.Date_Of_Birth, a.Activated_Date, a.Closed_Date, a.Pin_Number, " +
	                       "s.Phone_Number, s.Aadhaar_Number, " +
	                       "(YEAR(CURDATE()) - YEAR(a.Date_Of_Birth)) AS Age, " +
	                       "s.Gender " +
	                       "FROM Account a " +
	                       "JOIN Savings_Account s ON a.Account_Id = s.Account_Id " +
	                       "WHERE a.Account_Number = ?";
	
	        try (Connection con = DatabaseConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(query)) {
	
	            ps.setString(1, accountNumber);
	            ResultSet rs = ps.executeQuery();
	
	            if (rs.next()) {
	                SavingsAccount savingsAccount = new SavingsAccount();
	                
	                // Setting inherited Account properties
	                savingsAccount.setAccountId(rs.getLong("Account_Id"));
	                savingsAccount.setAccountNumber(rs.getString("Account_Number"));
	                savingsAccount.setName(rs.getString("Account_Name"));
	                savingsAccount.setAccountType(AccountType.valueOf(rs.getString("Account_Type")));
	                savingsAccount.setBalance(rs.getDouble("Account_Balance"));
	                savingsAccount.setAccountStatus(AccountStatus.valueOf(rs.getString("Account_Status")));
	                savingsAccount.setPrivelege(AccountPrivilege.valueOf(rs.getString("Account_Privilege")));
	                savingsAccount.setFundTransferLimit(rs.getDouble("Account_Transfer_Limit"));
	                savingsAccount.setDateOfBirth(rs.getDate("Date_Of_Birth").toLocalDate());
	                savingsAccount.setActivatedDate(rs.getTimestamp("Activated_Date").toLocalDateTime());
	                savingsAccount.setClosedDate(rs.getTimestamp("Closed_Date") != null ? 
	                                             rs.getTimestamp("Closed_Date").toLocalDateTime() : null);
	                savingsAccount.setPinNumber(rs.getInt("Pin_Number"));
	
	                // Setting SavingsAccount-specific properties
	                savingsAccount.setPhNo(rs.getLong("Phone_Number"));
	                savingsAccount.setAadhaarNumber(rs.getString("Aadhaar_Number"));
	                savingsAccount.setAge(rs.getInt("Age"));
	                savingsAccount.setGender(rs.getString("Gender"));
	
	                return savingsAccount;
	            }
	        } catch (SQLException e) {
	            System.out.println("Database error while retrieving savings account: " + e.getMessage());
	            e.printStackTrace(); // Print stack trace to debug
	            LoggerUtil.logError(e);
	        }
    	}
    	return null; // Return null if no account is found
    }
    public CurrentAccount getCurrentAccountByNumber(String accountNumber) {
    	if(accountExists(accountNumber))
    	{
	        String query = "SELECT a.Account_Id, a.Account_Number, a.Account_Name, a.Account_Type, " +
	                       "a.Account_Balance, a.Account_Status, a.Account_Privilege, a.Account_Transfer_Limit, " +
	                       "a.Date_Of_Birth, a.Activated_Date, a.Closed_Date, a.Pin_Number, " +
	                       "c.GST_Number, c.Business_Name, c.Business_Registration_Number, c.Business_Website " +
	                       "FROM Account a " +
	                       "JOIN Current_Account c ON a.Account_Id = c.Account_Id " +
	                       "WHERE a.Account_Number = ?";
	
	        try (Connection con = DatabaseConnection.getConnection();
	             PreparedStatement ps = con.prepareStatement(query)) {
	
	            ps.setString(1, accountNumber);
	            ResultSet rs = ps.executeQuery();
	
	            if (rs.next()) {
	                CurrentAccount currentAccount = new CurrentAccount();
	                
	                // Setting inherited Account properties
	                currentAccount.setAccountId(rs.getLong("Account_Id"));
	                currentAccount.setAccountNumber(rs.getString("Account_Number"));
	                currentAccount.setName(rs.getString("Account_Name"));
	                currentAccount.setAccountType(AccountType.valueOf(rs.getString("Account_Type")));
	                currentAccount.setBalance(rs.getDouble("Account_Balance"));
	                currentAccount.setAccountStatus(AccountStatus.valueOf(rs.getString("Account_Status")));
	                currentAccount.setPrivelege(AccountPrivilege.valueOf(rs.getString("Account_Privilege")));
	                currentAccount.setFundTransferLimit(rs.getDouble("Account_Transfer_Limit"));
	                currentAccount.setDateOfBirth(rs.getDate("Date_Of_Birth").toLocalDate());
	                currentAccount.setActivatedDate(rs.getTimestamp("Activated_Date").toLocalDateTime());
	                currentAccount.setClosedDate(rs.getTimestamp("Closed_Date") != null ? 
	                                             rs.getTimestamp("Closed_Date").toLocalDateTime() : null);
	                currentAccount.setPinNumber(rs.getInt("Pin_Number"));
	
	                // Setting CurrentAccount-specific properties
	                currentAccount.setGstNo(rs.getString("GST_Number"));
	                currentAccount.setBusinessName(rs.getString("Business_Name"));
	                currentAccount.setRegNo(rs.getString("Business_Registration_Number"));
	                currentAccount.setWebsite(rs.getString("Business_Website"));
	                return currentAccount;
	            }
	        } catch (SQLException e) {
	            System.out.println("Database error while retrieving current account: " + e.getMessage());
	            e.printStackTrace(); // Print stack trace to debug
	            LoggerUtil.logError(e);
	        }
    	}
        return null; // Return null if no account is found
    }
    public void updateAndCloseAccount(String accountNumber, AccountStatus status, LocalDateTime closedDate) {
        String query = "UPDATE account SET Account_Status = ?, Closed_Date = ? WHERE Account_Number = ?";
        
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, status.toString());
            ps.setTimestamp(2, closedDate != null ? Timestamp.valueOf(closedDate) : null);
            ps.setString(3, accountNumber);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account status updated successfully.");
            } else {
                System.out.println("No account found with the given number.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating account status: " + e.getMessage());
            e.printStackTrace(); // Print stack trace to debug
            LoggerUtil.logError(e);
        }
    }
}
