# Playtech Java Assignment 2024

## Overview

This project is developed as a solution to the Java Developer Test Assignment for the First Bank of Teldrassil. The main goal is to process transaction logs from CSV files to reconstruct customer balances and generate transaction outcomes. This solution adheres to the restrictions of using only JDK functionalities without any third-party libraries.

## Project Structure

The project consists of the following main components:

- `TransactionProcessorSample.java`: The main class that orchestrates reading input files, processing transactions, and writing output files.
- `User.java`: Defines the structure of a User, including their balance, country, and transaction limits.
- `Transaction.java`: Represents a transaction, including its type, amount, and the account involved.
- `BinMapping.java`: Used for validating card details against BIN mappings.
- `Event.java`: Represents the outcome of processing a transaction, including its approval status and any relevant messages.

## How to Compile and Run

### Prerequisites

- Java Development Kit (JDK) up to version 21 LTS.

### Compilation

Navigate to the project directory and compile all Java files using the following command:

`javac TransactionProcessorSample.java User.java Transaction.java BinMapping.java Event.java`

### Running the Application

Run the application by providing paths to the input and output files as arguments. Ensure to quote paths that contain spaces:

`java TransactionProcessorSample "path/to/users.csv" "path/to/transactions.csv" "path/to/bins.csv" "path/to/balances.csv" "path/to/events.csv"`

Replace `path/to/...` with the actual paths to your input CSV files and where you want the output CSV files to be saved.

## Input Files Format

- **Users**: CSV file containing user details such as user ID, username, balance, country, and transaction limits.
- **Transactions**: CSV file with transaction records, including transaction ID, user ID, type, amount, method, and account number.
- **Bin Mapping**: CSV file used for validating card details, including the issuing bank name, card number range, card type, and country.

## Output Files

- **balances.csv**: Lists user IDs with their corresponding new balance after processing all transactions.
- **events.csv**: Contains the outcomes of each transaction processed, including transaction ID, status (APPROVED or DECLINED), and a message.

## Processing Requirements

The application processes transactions based on several criteria, including uniqueness of transaction IDs, user status, payment method validation, and transaction limits. Detailed processing requirements are outlined in the assignment document.

## Notes

- Ensure that the input CSV files do not contain escaped characters, quote marks, or commas within columns.
- The application is designed to handle unexpected errors gracefully, skipping problematic transactions without interrupting the processing of remaining transactions.

## Submission

This project is submitted as part of the Playtech Java Assignment 2024. It includes all source code files and any testing input files created during development.