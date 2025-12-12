# Food Order Management System

A database-driven restaurant Food Order Management System built with Java 17 and PostgreSQL, following an MVC + Service + DAO architecture. The system simulates a restaurant’s internal ordering workflow through a Command-Line Interface (CLI) and supports full CRUD operations for customers, employees, menu items, and orders. It also leverages PostgreSQL functions, stored procedures, triggers, and views to enforce business rules and automate calculations.

## Features

### Customer Management
- Full CRUD for customers
- Validations:
  - Non-empty names
  - Email format (regex)
  - Unique email constraint
- Prevents deletion of customers with existing order history

### Menu Management
- Full CRUD for menu items
- Category-based organization
- Activate and deactivate items
- Dynamic price updates with validation

### Order Management
- Create orders with multiple items and quantities
- Automatically assigns an available employee to new orders
- Automatically calculates order totals via DB triggers and functions
- Enforced status lifecycle:
  - PENDING -> ACCEPTED -> PREPARING -> COMPLETED
  - Controlled transitions to CANCELLED
- Status validation in both Java service logic and PostgreSQL triggers

### Employee Management
- Track employee records and availability status
- Assignment restricted to employees marked as available
- Basic load balancing (random or selection-based assignment)

### System Statistics and Reports
- Total counts of customers, employees, menu items, and orders
- Revenue by status and by day
- Top selling menu items
- Category-level sales
- Employee workload and performance (backed by SQL views)

## Tech Stack

| Component | Technology |
| --- | --- |
| Programming Language | Java 17 |
| Database | PostgreSQL 12+ |
| Database Connectivity | JDBC |
| Build Tool | Apache Maven 3.6+ |
| Architecture | MVC + Service + DAO + Controller layers |
| User Interface | Command-Line Interface (CLI) |
| Tooling (optional) | DBeaver |

## System Requirements

To build and run this project, you need:
- OS: Windows 10+, macOS 12+, or recent Linux distribution
- Java Development Kit (JDK): Java 17 (e.g., Temurin 17)
- PostgreSQL Database Server: 12+
- Apache Maven: 3.6+
- Optional: DBeaver (for schema inspection and running SQL scripts)

Expected PostgreSQL local defaults:
- Host: localhost
- Port: 5432
- Default database: postgres (used to create restaurant_db)
- A database user with permission to create databases and tables (e.g., postgres)

## Build and Run Instructions

## Database Setup

### 1) Create database
Run in psql or DBeaver:

    CREATE DATABASE restaurant_db;

### 2) Execute schema script (restaurant_db.sql)

This script creates:
- Tables: Customer, Employee, Category, MenuItem, Orders, OrderItem
- Constraints and indexes
- Functions, stored procedures, triggers, and views used by the application

Option A: Using DBeaver
1. Create a new PostgreSQL connection
2. Right-click restaurant_db
3. Open SQL Editor and execute the contents of restaurant_db.sql

Option B: Using psql
Run:
```md
psql -d restaurant_db -f restaurant_db.sql
```
## Application Configuration

The database connection settings are centralized in:
```md
src/main/java/com/foodorder/config/DatabaseConnection.java
```
Update the JDBC URL, username, and password to match your local PostgreSQL credentials:

1. Open:
```md
src/main/java/com/foodorder/config/DatabaseConnection.java
```

3. Update these fields:
```md
private static final String URL = "jdbc:postgresql://localhost:5432/restaurant_db";
private static final String USERNAME = "your_username";
private static final String PASSWORD = "your_password";
```

5. Save the file.

Notes:
- If your PostgreSQL host/port/database differs, update URL accordingly.
- Make sure the user has permission to access restaurant_db and read/write tables.

## Build Instructions

From the project root directory (where pom.xml is located):

1. Open a terminal at the project root (same level as pom.xml).

2. Run:
```md
mvn clean compile
```
What this does:
- Downloads required Maven dependencies
- Compiles Java sources under src/main/java

## Run Instructions

The CLI main entry point class is:
```md
src/main/java/com/foodorder/app/Main.java
```
1. Ensure PostgreSQL is running and restaurant_db has been created and initialized.

2. From the project root, run:
```md
mvn exec:java -Dexec.mainClass="com.foodorder.app.Main"
```
4. After successful startup, the CLI main menu will appear in the console.

If you see an error about exec plugin:
- Check pom.xml includes exec-maven-plugin configuration

## Testing Instructions

An automated test runner is provided in:
```md
src/main/java/com/foodorder/app/TestNewMethods.java
```

Option A: Run via IDE (recommended)
1. Open TestNewMethods.java
2. Run the class directly

Option B: Run via Maven (only if tests are configured under src/test/java)
1. Run:
```md
    mvn test
```
Notes:
- This project’s test runner is located under src/main/java, so IDE run is the most direct.
- All implemented test methods passed in the development environment (per project report).

## Project Structure

Key files and directories:
- pom.xml
  Maven build configuration

- restaurant_db.sql
  PostgreSQL schema script (tables, constraints, indexes, functions, procedures, triggers, views)

- src/main/java/com/foodorder/app/Main.java
  CLI entry point

- src/main/java/com/foodorder/config/DatabaseConnection.java
  JDBC connection configuration

- src/main/java/com/foodorder/app/TestNewMethods.java
  Test runner for DAO and service methods

Architecture overview:
- Models: domain objects (Customer, Employee, MenuItem, Orders, etc.)
- DAO: SQL operations per entity
- Service: business logic and validation
- Controllers: CLI flows and user interaction coordination

## Database Design

Core entities:
- Customer(customer_id PK, name, email UNIQUE, phone)
- Employee(employee_id PK, name, phone, availability_status)
- Category(category_id PK, name UNIQUE)
- MenuItem(item_id PK, category_id FK, item_name, current_price, is_active)
- Orders(order_id PK, customer_id FK, employee_id FK, order_time, total_amount, current_status)
- OrderItem(order_id FK, item_id FK, quantity, PRIMARY KEY(order_id, item_id))

Relationships:
- Customer 1..N Orders
- Employee 1..N Orders
- Category 1..N MenuItem
- Orders N..M MenuItem through OrderItem

Constraints and data quality:
- Unique constraints: Customer.email, Category.name
- Check constraints:
  - MenuItem.current_price >= 0
  - OrderItem.quantity > 0
  - Orders.current_status in {PENDING, ACCEPTED, PREPARING, COMPLETED, CANCELLED}

Database programming objects include:
- Functions:
  - calculate_order_total(p_order_id)
  - get_available_employees_count()
  - get_customer_order_count(p_customer_id)
  - is_menu_item_available(p_item_id)
- Stored Procedures:
  - update_order_total(p_order_id)
  - assign_employee_to_order(p_order_id)
- Triggers:
  - Triggers on OrderItem that update order totals after insert, update, or delete
  - Trigger on Orders that validates status transitions
- Views:
  - order_summary
  - menu_with_category
  - employee_workload

## User Flow

Main menu:
```md
============================================================
           Restaurant Order Management System
============================================================
1. Menu Browse & Search
2. Order Management
3. Customer Management
4. Employee Management
5. System Statistics
6. Exit System
============================================================
```
Typical operations:

Create a new customer:
1) Main Menu -> Customer Management
2) Create new customer
3) Input name, email, phone
4) System validates and creates the record
5) New customer_id is displayed

Browse and search menu items:
1) Main Menu -> Menu Browse & Search
2) Browse all items or browse by category
3) Search by partial item name (fuzzy match)
4) Filter by price range
5) Add/update/activate/deactivate menu items

Create a new order:
1) Main Menu -> Order Management
2) Create new order
3) Select an existing customer
4) System assigns an available employee
5) Add menu items with quantities
6) Status starts as PENDING
7) DB triggers and functions compute total_amount automatically

Employee management:
1) Main Menu -> Employee Management
2) Create/update employee records
3) Set availability status (available or unavailable)
4) View employee workload and performance (backed by employee_workload view)

System statistics:
1) Main Menu -> System Statistics
2) View counts, revenue summaries, top selling items, category sales, and employee metrics

## Course Info
CS5200 Fall 2025
Final Project: Food Order Management System
