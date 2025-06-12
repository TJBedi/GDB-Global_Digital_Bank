package com.gdb.ui;

import java.time.LocalDate;
import java.util.*;
import com.gdb.controllers.accounts.AccountController;
import com.gdb.controllers.transactions.TransactionController;
import com.gdb.models.accounts.Account;
import com.gdb.models.accounts.AccountPrivilege;
import com.gdb.models.accounts.AccountStatus;
import com.gdb.models.accounts.AccountType;
import com.gdb.models.accounts.CurrentAccount;
import com.gdb.models.accounts.SavingsAccount;
import com.gdb.util.InputUtil;
import com.gdb.util.LoggerUtil;
import com.gdb.util.SimpleFileEncryptor;

public class AccountUI {
	private static AccountController controller = new AccountController();
	private static TransactionController transactionController = new  TransactionController();
	public static void showMenu() {
		Scanner in = InputUtil.getScanner();
		int choice = 0;
		do {
			System.out.println("************************************************************************************");
			System.out.println("\t\t\tWelcome to Global Destination Bank!");
			System.out.println("************************************************************************************");
			System.out.println("DASHBOARD");
			System.out.println("1.Open");
			System.out.println("2.Close");
			System.out.println("3.Deposit");
			System.out.println("4.Withdraw");
			System.out.println("5.Transfer");
			System.out.println("6.Exit");
			System.out.println("Enter your choice (1/2/3/4/5/6):");
			try {
				choice = in.nextInt();
				in.nextLine();
				switch (choice) {
				case 0:
					encryptData();
					break;
				case 1:
					AccountUI.openAccount();
					break;
				case 2:
					AccountUI.closeAccount();
					break;
				case 3:
					AccountUI.deposit();
					break;
				case 4:
					AccountUI.withdraw();
					break;
				case 5:
					AccountUI.transfer();
					break;
				case 6:
					System.out.println("Exiting the application. Thank you!");
					break;
				default:
					System.out.println("Invalid input. Please enter a number between 1 and 6.");
					System.exit(0);
					break;
				}
			} catch (Exception e) {
				System.out.println("Invalid Input! Integer input only."+e.getMessage());
				LoggerUtil.logError(e);
				in.nextLine(); 
				choice = 0; 
			}
		} while (choice != 6); 
		in.close();
	}

	public static void openAccount() {
		Scanner in=InputUtil.getScanner();
		int choice = 0;
			System.out.println("Enter which Account you want to open:\n1.Savings\n2.Current:");
			try {
				choice = in.nextInt();
				in.nextLine();
			} catch (Exception e) {
				System.out.println("Invalid Input! Integer input only.");
				LoggerUtil.logError(e);
				in.nextLine(); 
				openAccount();
			}
		
		switch (choice) {
		case 1:
			System.out.println("Enter details for the Savings Account:\n");
			SavingsAccount savings=acceptSavingsAccountInfo();
			controller.openAccount(savings);
			if (savings.getAccountStatus() == AccountStatus.Active) {
				System.out.println("********************************************************");
				System.out.println("Account details:");
				displaySavingsAccountInfo(savings);
				System.out.println("********************************************************");
			}
			else
			{
				System.out.println("Failed to create account.");
			}
			break;
		case 2:
			System.out.println("Enter details for the Current Account:\n");
			CurrentAccount current= acceptCurrentAccountInfo();
			controller.openAccount(current);
			if (current.getAccountStatus() == AccountStatus.Active) {
				System.out.println("********************************************************");
				System.out.println("Account details:");
				displayCurrentAccountInfo(current);
				System.out.println("********************************************************");
			}
			else
			{
				System.out.println("Failed to create account.");
			}
			break;
		default:
			System.out.println("Invalid Input");
			break;
		}
	}

	public static void displaySavingsAccountInfo(SavingsAccount savings) {
		// Display from the object
		System.out.println("Savings Account Details:");
		System.out.println("Account Id: " + savings.getAccountId());
		System.out.println("Accountholder Name: " + savings.getName());
		System.out.println("Account Number: " + savings.getAccountNumber());
		System.out.println("Account Type: " + savings.getAccountType());
		System.out.println("Phone Number: " + savings.getPhNo());
		System.out.println("Date of Birth: "+savings.getDateOfBirth());
		System.out.println("Gender: " + savings.getGender());
		System.out.println("Privilege: " + savings.getPrivilege());
		System.out.println("Account Balance: " + savings.getBalance());
		System.out.println("Account Status: " + savings.getAccountStatus());
	}

	public static void displayCurrentAccountInfo(CurrentAccount current) {
		// Display from the object
		System.out.println("Current Account Details:");
		System.out.println("Account Id: " + current.getAccountId());
		System.out.println("Accountholder Name: " + current.getName());
		System.out.println("Date of Birth: "+current.getDateOfBirth());
		System.out.println("Account Number: " + current.getAccountNumber());
		System.out.println("Account Type: " + current.getAccountType());
		System.out.println("Business name: " + current.getBusinessName());
		System.out.println("Website: " + current.getWebsite());
		System.out.println("Privilege " + current.getPrivilege());
		System.out.println("Account Balance: " + current.getBalance());
		System.out.println("Account Status: " + current.getAccountStatus());
	}

	public static SavingsAccount acceptSavingsAccountInfo() {
	    SavingsAccount savings = new SavingsAccount();
	    Scanner in = InputUtil.getScanner();

	    while (true) {  // Loop until valid input is received
	        try {
	            System.out.println("Enter full name:");
	            String name = in.nextLine().trim();
	            if (name.isEmpty()) throw new Exception("Name cannot be empty.");
	            savings.setName(name);

	            System.out.println("Enter your gender (Male/Female):");
	            String gender = in.nextLine().trim();
	            if (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female")) {
	                throw new Exception("Invalid gender. Please enter Male or Female.");
	            }
	            savings.setGender(gender);

	            System.out.println("Enter your date of birth (yyyy-mm-dd):");
	            String stDate = in.nextLine().trim();
	            LocalDate date = LocalDate.parse(stDate);
	            savings.setDateOfBirth(date);

	            System.out.println("Enter age:");
	            if (!in.hasNextInt()) {
	                throw new Exception("Invalid age. Please enter a valid number.");
	            }
	            int age = in.nextInt();
	            in.nextLine();  // Consume leftover newline
	            if (age < 0) throw new Exception("Age cannot be negative.");
	            savings.setAge(age);

	            System.out.println("Enter phone number:");
	            if (!in.hasNextLong()) {
	                throw new Exception("Invalid phone number. Enter a valid 10-digit number.");
	            }
	            long phNo = in.nextLong();
	            in.nextLine();  // Consume leftover newline
	            savings.setPhNo(phNo);

	            break;  // Exit loop if all inputs are valid
	        } catch (Exception e) {
	            System.out.println("Error: " + e.getMessage() + " Try again.\n");
	            LoggerUtil.logError(e);
	            in.nextLine();  // Clear buffer to avoid infinite loop
	        }
	    }

	    savings.setAccountType(AccountType.SavingsAccount);
	    return savings;
	}

	public static CurrentAccount acceptCurrentAccountInfo() {
	    CurrentAccount current = new CurrentAccount();
	    Scanner in = InputUtil.getScanner();

	    while (true) {
	        try {
	            System.out.println("Enter full name:");
	            String name = in.nextLine().trim();
	            if (name.isEmpty()) throw new Exception("Name cannot be empty.");
	            current.setName(name);
	            
	            System.out.println("Enter your date of birth (yyyy-mm-dd):");
	            String stDate = in.nextLine().trim();
	            LocalDate date = LocalDate.parse(stDate);
	            current.setDateOfBirth(date);

	            System.out.println("Enter Business Name:");
	            String busName = in.nextLine().trim();
	            if (busName.isEmpty()) throw new Exception("Business name cannot be empty.");
	            current.setBusinessName(busName);

	            System.out.println("Enter Registration Number:");
	            String regNo = in.nextLine().trim();
	            if (regNo.isEmpty()) throw new Exception("Registration number cannot be empty.");
	            current.setRegNo(regNo);

	            System.out.println("Enter the GST number:");
	            String gstNo = in.nextLine().trim();
	            if (gstNo.isEmpty()) throw new Exception("GST number cannot be empty.");
	            current.setGstNo(gstNo);

	            System.out.println("Enter your Website:");
	            String website = in.nextLine().trim();
	            if (website.isEmpty()) throw new Exception("Website cannot be empty.");
	            current.setWebsite(website);

	            break;  // Exit loop if all inputs are valid
	        } catch (Exception e) {
	            System.out.println("Error: " + e.getMessage() + " Try again.\n");
	            LoggerUtil.logError(e);
	            in.nextLine();  // Clear buffer to avoid infinite loop
	        }
	    }

	    current.setAccountType(AccountType.CurrentAccount);
	    return current;
	}
	
	public static void setPin(SavingsAccount account) {
		Scanner in = InputUtil.getScanner();
			try {
				System.out.println("Enter new Pin number(4 digits only):");
				int pin = in.nextInt();
				in.nextLine();
				controller.setPin(pin, account);
			} catch (Exception e) {
				System.out.println("Invalid format. Integer only");
				LoggerUtil.logError(e);
				in.nextLine(); // Clear the invalid input
			}
		
	}

	public static void setPin(CurrentAccount account) {
		Scanner in = InputUtil.getScanner();
			try {
				System.out.println("Enter new Pin number(4 digits only):");
				int pin = in.nextInt();
				controller.setPin(pin, account);
			} catch (Exception e) {
				System.out.println("Invalid format. Integer only");
				LoggerUtil.logError(e);
				in.nextLine(); // Clear the invalid input
			}
	
	}
	public static <T extends Account> void setAccountPrivilege(T account) {
		Scanner in = null;
		try {
			in = InputUtil.getScanner();
			System.out.println("Enter the type of privilege(1/2/3):\n1.Premium\n2.Gold\n3.Silver");
			int choice = getValidChoiceInput(in, 1, 3);
			
			if(choice == 1)
				account.setPrivelege(AccountPrivilege.Premium);
			else if(choice == 2)
				account.setPrivelege(AccountPrivilege.Gold);
			else 
				account.setPrivelege(AccountPrivilege.Silver);
		} catch (InputMismatchException e) {
			System.out.println("Invalid input. Setting default Silver privilege.");
			LoggerUtil.logError(e);
			account.setPrivelege(AccountPrivilege.Silver);
		}
	}
	public static int getValidChoiceInput(Scanner scanner, int min, int max) {
		int choice = min;
		boolean validInput = false;
		
		while (!validInput) {
			try {
				choice = scanner.nextInt();
				if (choice >= min && choice <= max) {
					validInput = true;
				} else {
					System.out.println("Please enter a number between " + min + " and " + max + ":");
				}
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Please enter a number:");
				LoggerUtil.logError(e);
				scanner.nextLine(); // Clear the invalid input
			}
		}
		
		return choice;
	}
	
	public static void closeAccount() {
		Scanner in=InputUtil.getScanner();
		System.out.println("Enter Account Number to close the account: ");
		String accountNumber = in.nextLine();
		controller.closeAccount(accountNumber);
	}

	public static void withdraw() {
		Scanner in = InputUtil.getScanner();
		String accNo = "";
		int pin = 0, amount = 0;
			try {
				System.out.println("Enter the Account Number:");
				accNo = in.nextLine();
				
				System.out.println("Enter your pin:");
				pin = in.nextInt();
				in.nextLine();
				
				System.out.println("Enter the Amount to withdraw:");
				amount = in.nextInt();
				in.nextLine();

			} catch (Exception e) {
				System.out.println("Invalid format. Enter Again:");
				in.nextLine(); // Clear the invalid input
				LoggerUtil.logError(e);
				withdraw();
			}
			transactionController.withdraw(accNo, pin, amount);
	}
	public static void deposit() {
		Scanner in = InputUtil.getScanner();
		String accNo = "";
		int pin = 0, amount = 0;
			try {
				System.out.println("Enter the Account Number:");
				accNo = in.nextLine();
				
				System.out.println("Enter your pin:");
				pin = in.nextInt();
				in.nextLine();
				System.out.println("Enter the Amount to deposit:");
				amount = in.nextInt();
				in.nextLine();
			} catch (Exception e) {
				System.out.println("Invalid format. Enter Again:");
				in.nextLine(); // Clear the invalid input
				LoggerUtil.logError(e);
				deposit();
			}
			transactionController.deposit(accNo, pin, amount);
	}
	public static void transfer() {
		Scanner in = InputUtil.getScanner();
		String fromAccNo = "", toAccNo = "";
		int pin = 0, amount = 0;
			try {
				System.out.println("Enter your Account Number:");
				fromAccNo = in.nextLine();
				
				System.out.println("Enter your pin:");
				pin = in.nextInt();
				in.nextLine(); 
				
				System.out.println("Enter the Account Number to which you wish to tranfer funds:");
				toAccNo = in.nextLine();
				
				System.out.println("Enter the Amount to Tranfer:");
				amount = in.nextInt();
				in.nextLine(); 
				
			} catch (Exception e) {
				System.out.println("Invalid format. Enter Again:");
				in.nextLine(); 
				LoggerUtil.logError(e);
				transfer();
			}
		
			transactionController.transfer(fromAccNo, toAccNo, pin, amount);
	}
    protected static void encryptData() {
    	try {
    		String jdbcUrl = SimpleFileEncryptor.encrypt("jdbc:mysql://localhost:3306/gdb");
    		SimpleFileEncryptor.saveToFile(jdbcUrl);
    		String user = SimpleFileEncryptor.encrypt("root");
    		SimpleFileEncryptor.saveToFile(user);
    		String password = SimpleFileEncryptor.encrypt("Waheguruji63");
    		SimpleFileEncryptor.saveToFile(password);
    	}
    	catch(Exception e) {
    		System.out.println(e.getMessage());
    		LoggerUtil.logError(e);
    	}
    }
}
