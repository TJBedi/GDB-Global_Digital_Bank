CREATE TABLE Account (
    Account_Id BIGINT AUTO_INCREMENT PRIMARY KEY,
    Account_Number VARCHAR(50) UNIQUE NOT NULL,
    Account_Name VARCHAR(100) NOT NULL,
    Account_Type ENUM('CurrentAccount', 'SavingsAccount') NOT NULL,
    Account_Balance DECIMAL(15,2) NOT NULL DEFAULT 0.0,
    Account_Status ENUM('Active', 'InActive') NOT NULL DEFAULT 'Active',
    Account_Privilege ENUM('Premium', 'Gold', 'Silver') NOT NULL,
    Account_Transfer_Limit DECIMAL(15,2) NOT NULL,
    Date_Of_Birth DATE NOT NULL,
    Activated_Date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Closed_Date TIMESTAMP NULL,
    Pin_Number INT NOT NULL
);

CREATE TABLE Savings_Account (
    Account_Id BIGINT PRIMARY KEY,
    Phone_Number BIGINT NOT NULL,
    Aadhaar_Number VARCHAR(20) UNIQUE NOT NULL,
    Gender ENUM('Male', 'Female', 'Other') NOT NULL,
    FOREIGN KEY (Account_Id) REFERENCES Account(Account_Id) ON DELETE CASCADE
);

CREATE TABLE Current_Account (
    Account_Id BIGINT PRIMARY KEY,
    GST_Number VARCHAR(20) UNIQUE NOT NULL,
    Business_Name VARCHAR(255) NOT NULL,
    Business_Registration_Number VARCHAR(50) UNIQUE NOT NULL,
    Business_Website VARCHAR(255),
    FOREIGN KEY (Account_Id) REFERENCES Account(Account_Id) ON DELETE CASCADE
);

create table Transactions(
    Id BIGINT AUTO_INCREMENT PRIMARY KEY,
	Timestamp datetime,
    From_Account_Number varchar(50) not null,
    To_Account_Number varchar(50) default null,
    Amount int not null,
    Type ENUM('Withdraw','Deposit','Transfer') not null,
    Status ENUM('Successful','Pending','Failed') not null,
    FOREIGN KEY (From_Account_Number) REFERENCES Account(Account_Number) ON DELETE CASCADE,
    FOREIGN KEY (To_Account_Number) REFERENCES Account(Account_Number) ON DELETE CASCADE
);

DELIMITER //
CREATE EVENT reset_transfer_limit
ON SCHEDULE EVERY 1 DAY
STARTS TIMESTAMP(CURRENT_DATE + INTERVAL 1 DAY)
DO
BEGIN
    UPDATE Account 
    SET Account_Transfer_Limit = 
        CASE 
            WHEN Account_Privilege = 'Premium' THEN 100000
            WHEN Account_Privilege = 'Gold' THEN 50000
            WHEN Account_Privilege = 'Silver' THEN 25000
            ELSE Account_Transfer_Limit -- If any other value, retain current limit
        END;
END //
DELIMITER ;

SET GLOBAL event_scheduler = ON;

-- Queries:

select * from account;
select * from savings_account;
select * from current_account;
select * from transactions;
show events;