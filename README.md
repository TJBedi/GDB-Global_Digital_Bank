# 💼 Global Digital Bank (GDB)

## 📖 Project Overview  
The **Global Digital Bank (GDB)** is an enterprise-grade banking backend system developed entirely in **Java**. It simulates the core functionalities of a modern digital bank, including account lifecycle management, secure fund transfers, transaction logging, and persistent data handling using **JDBC** and the **Java Collections Framework**.

This project reflects a modular, object-oriented architecture designed to mimic the operations of real-world banking platforms such as ICICI Bank, focusing on clean logic implementation and backend robustness.

---

## ✅ Key Features

### 🔐 Account Management System
- Supports creation and management of both **Savings** and **Current** accounts.  
- Tracks account status including **active**, **closed**, and **suspended** states.  
- Updates and maintains account metadata, including **closure dates**.

### 💸 Fund Transfer Engine
- Enables transfers between accounts with built-in **transfer limits** based on account tiers:
  - **PREMIUM**: ₹100,000  
  - **GOLD**: ₹50,000  
  - **SILVER**: ₹25,000  
- Validates **balance sufficiency**, **transaction status**, and **transfer conditions**.

### 📊 Transaction Logging & Tracking
- Captures and organizes transaction data **daily** using `Map<LocalDate, List<Transaction>>`.  
- Maintains **transaction history** for audits and account activity insights.  
- Categorizes transactions for both **debit** and **credit** flows.

### 🗃️ Backend Data Management
- Implements **JDBC** for persistent storage and retrieval of account and transaction data.  
- Provides **repository layer abstractions** for decoupled data access and updates.

### ⚙️ Clean OOP Structure
- Follows **Object-Oriented Design Principles** for modularity and scalability.  
- Clearly defined domain models: `Account`, `Transaction`, `Payment`, and corresponding services.  
- Promotes **code reusability**, **readability**, and **maintainability**.

---

## 🛠 Technical Stack
- **Programming Language**: Java  
- **Persistence Layer**: JDBC (Java Database Connectivity)  
- **Data Structures Used**: List, Set, Map, and Generics  
- **Design Principles**: OOP (Encapsulation, Abstraction, Modularity)

---

## 🎯 Objectives Achieved
- Demonstrated use of Java to simulate real-world banking logic.  
- Applied business rules with dynamic validations based on account privileges.  
- Created a reliable transaction logging service using Java's collection framework.  
- Integrated Java with relational databases for secure and structured data handling.  
- Developed a fully functional, extensible backend ready for further expansion into APIs or GUI layers.
