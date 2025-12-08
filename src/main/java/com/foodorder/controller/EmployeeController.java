package com.foodorder.controller;

import java.util.List;
import java.util.Scanner;

import com.foodorder.model.Employee;
import com.foodorder.service.EmployeeService;

/**
 * Controller
 * HandleManagementrelated user interface interactions
 */
public class EmployeeController {
    
    private EmployeeService employeeService;
    private Scanner scanner;
    
    public EmployeeController() {
        this.employeeService = new EmployeeService();
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * DisplayManagement
     */
    public void showEmployeeInterface() {
        while (true) {
            displayEmployeeOptions();
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1 -> createEmployee();
                    case 2 -> viewAllEmployees();
                    case 3 -> searchEmployees();
                    case 4 -> viewEmployeeDetails();
                    case 5 -> updateEmployee();
                    case 6 -> updateEmployeeAvailability();
                    case 7 -> deleteEmployee();
                    case 8 -> {
                        System.out.println("Return to main menu...");
                        return;
                    }
                    default -> System.out.println("Invalid selection, please try again");
                }
                
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
                
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number selection");
            } catch (Exception e) {
                System.out.println("Error occurred: " + e.getMessage());
            }
        }
    }
    
    /**
     * DisplayManagement
     */
    private void displayEmployeeOptions() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           Employee Management System");
        System.out.println("=".repeat(50));
        System.out.println("1. Add new employee");
        System.out.println("2. View all employees");
        System.out.println("3. Search employees");
        System.out.println("4. View employee details");
        System.out.println("5. Update employee information");
        System.out.println("6. Update employee status");
        System.out.println("7. Delete employee");
        System.out.println("8. Return to main menu");
        System.out.println("=".repeat(50));
        System.out.print("Please select operation (1-8): ");
    }
    
    /**
     * Create new employee
     */
    private void createEmployee() {
        System.out.println("\nAdd New Employee");
        System.out.println("=".repeat(30));
        
        try {
            System.out.print("Please enter employee name: ");
            String name = scanner.nextLine().trim();
            
            System.out.print("Please enter phone number (optional): ");
            String phone = scanner.nextLine().trim();
            if (phone.isEmpty()) phone = null;
            
            System.out.print("Is available (Y/n): ");
            String availableInput = scanner.nextLine().trim().toLowerCase();
            boolean available = availableInput.isEmpty() || "y".equals(availableInput) || "yes".equals(availableInput);
            
            int employeeId = employeeService.createEmployee(name, phone, available);
            
            if (employeeId > 0) {
                System.out.println("✓ Employee added successfully! Employee ID: " + employeeId);
            } else {
                System.out.println("✗ Failed to add employee.");
            }
            
        } catch (Exception e) {
            System.out.println("Employee creation failed: " + e.getMessage());
        }
    }
    
    /**
     * View all employees
     */
    private void viewAllEmployees() {
        System.out.println("\nAll Employees");
        System.out.println("=".repeat(40));
        
        try {
            List<Employee> employees = employeeService.getAllEmployees();
            
            if (employees.isEmpty()) {
                System.out.println("No employees found.");
            } else {
                System.out.printf("%-4s %-20s %-15s %-10s%n", "ID", "Name", "Phone", "Status");
                System.out.println("-".repeat(55));
                
                employees.forEach(employee -> 
                    System.out.printf("%-4d %-20s %-15s %-10s%n",
                        employee.getEmployeeId(),
                        employee.getName(),
                        employee.getPhone() != null ? employee.getPhone() : "Not provided",
                        employee.getAvailabilityStatusText())
                );
                
                System.out.println("\nTotal employees: " + employees.size());
                System.out.println("Available employees: " + employeeService.getAvailableEmployeeCount());
            }
            
        } catch (Exception e) {
            System.out.println("Failed to retrieve employee information: " + e.getMessage());
        }
    }
    
    
    /**
     * Search employees
     */
    private void searchEmployees() {
        System.out.println("\nSearch Employees");
        System.out.println("=".repeat(30));
        
        System.out.print("Please enter employee name to search: ");
        String searchTerm = scanner.nextLine().trim();
        
        if (searchTerm.isEmpty()) {
            System.out.println("Search term cannot be empty.");
            return;
        }
        
        try {
            List<Employee> results = employeeService.searchEmployeesByName(searchTerm);
            
            System.out.println("\nSearch results for: \"" + searchTerm + "\"");
            System.out.println("=".repeat(60));
            
            if (results.isEmpty()) {
                System.out.println("No employees found.");
            } else {
                System.out.printf("%-4s %-20s %-15s %-10s%n", "ID", "Name", "Phone", "Status");
                System.out.println("-".repeat(60));
                
                results.forEach(employee -> 
                    System.out.printf("%-4d %-20s %-15s %-10s%n",
                        employee.getEmployeeId(),
                        employee.getName(),
                        employee.getPhone() != null ? employee.getPhone() : "Not provided",
                        employee.getAvailabilityStatusText())
                );
                
                System.out.println("\nFound " + results.size() + " employee(s).");
            }
            
        } catch (Exception e) {
            System.out.println("Employee search failed: " + e.getMessage());
        }
    }
    
    /**
     * View employee details
     */
    private void viewEmployeeDetails() {
        System.out.println("\nEmployee Details");
        System.out.println("=".repeat(30));
        
        try {
            System.out.print("Please enter employee ID: ");
            int employeeId = Integer.parseInt(scanner.nextLine().trim());
            
            String detailInfo = employeeService.getEmployeeDetailInfo(employeeId);
            System.out.println("\n" + detailInfo);
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid employee ID.");
        } catch (Exception e) {
            System.out.println("Failed to retrieve employee details: " + e.getMessage());
        }
    }
    
    /**
     * Update employee information
     */
    private void updateEmployee() {
        System.out.println("\nUpdate Employee Information");
        System.out.println("=".repeat(40));
        
        try {
            System.out.print("Please enter employee ID: ");
            int employeeId = Integer.parseInt(scanner.nextLine().trim());
            
            // Get current employee information
            Employee existingEmployee = employeeService.getEmployeeById(employeeId);
            if (existingEmployee == null) {
                System.out.println("Employee does not exist.");
                return;
            }
            
            System.out.println("Current employee information:");
            System.out.println("Name: " + existingEmployee.getName());
            System.out.println("Phone: " + (existingEmployee.getPhone() != null ? existingEmployee.getPhone() : "Not provided"));
            System.out.println("Status: " + existingEmployee.getAvailabilityStatusText());
            
            System.out.println("\nEnter new information (press Enter to keep current value):");
            
            System.out.print("Name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) name = existingEmployee.getName();
            
            System.out.print("Phone: ");
            String phone = scanner.nextLine().trim();
            if (phone.isEmpty()) phone = existingEmployee.getPhone();
            
            System.out.print("Is available (Y/n): ");
            String availableInput = scanner.nextLine().trim().toLowerCase();
            boolean available = existingEmployee.isAvailabilityStatus();
            if (!availableInput.isEmpty()) {
                available = "y".equals(availableInput) || "yes".equals(availableInput);
            }
            
            boolean success = employeeService.updateEmployee(employeeId, name, phone, available);
            
            if (success) {
                System.out.println("✓ Employee information updated successfully!");
            } else {
                System.out.println("✗ Failed to update employee information.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid employee ID.");
        } catch (Exception e) {
            System.out.println(" updatefailed: " + e.getMessage());
        }
    }
    
    /**
     * Update employee availability status
     */
    private void updateEmployeeAvailability() {
        System.out.println("\nUpdate Employee Status");
        System.out.println("=".repeat(35));
        
        try {
            System.out.print("Please enter employee ID: ");
            int employeeId = Integer.parseInt(scanner.nextLine().trim());
            
            // Get current employee information
            Employee employee = employeeService.getEmployeeById(employeeId);
            if (employee == null) {
                System.out.println("Employee does not exist.");
                return;
            }
            
            System.out.println("Employee: " + employee.getName());
            System.out.println("Current status: " + employee.getAvailabilityStatusText());
            
            System.out.print("Set as available (Y/n): ");
            String availableInput = scanner.nextLine().trim().toLowerCase();
            boolean available = "y".equals(availableInput) || "yes".equals(availableInput) || availableInput.isEmpty();
            
            boolean success = employeeService.updateEmployeeAvailability(employeeId, available);
            
            if (success) {
                System.out.println("✓ Status updated successfully! New status: " + (available ? "Available" : "Unavailable"));
            } else {
                System.out.println("✗ Failed to update status.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid employee ID.");
        } catch (Exception e) {
            System.out.println("Status update failed: " + e.getMessage());
        }
    }
    
    /**
     * Delete employee
     */
    private void deleteEmployee() {
        System.out.println("\nDelete Employee");
        System.out.println("=".repeat(30));
        
        try {
            System.out.print("Please enter employee ID to delete: ");
            int employeeId = Integer.parseInt(scanner.nextLine().trim());
            
            // Get employee information for confirmation
            Employee employee = employeeService.getEmployeeById(employeeId);
            if (employee == null) {
                System.out.println("Employee does not exist.");
                return;
            }
            
            System.out.println("Employee information:");
            System.out.println("ID: " + employee.getEmployeeId());
            System.out.println("Name: " + employee.getName());
            System.out.println("Status: " + employee.getAvailabilityStatusText());
            
            // Check if employee has handled orders
            if (employeeService.hasOrders(employeeId)) {
                System.out.println("⚠ Warning: This employee has handled orders.");
            }
            
            System.out.print("Confirm deletion (y/N): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            
            if ("y".equals(confirm) || "yes".equals(confirm)) {
                boolean success = employeeService.deleteEmployee(employeeId);
                
                if (success) {
                    System.out.println("✓ Employee deleted successfully!");
                } else {
                    System.out.println("✗ Failed to delete employee.");
                }
            } else {
                System.out.println("Deletion cancelled.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid employee ID.");
        } catch (Exception e) {
            System.out.println("Delete operation failed: " + e.getMessage());
        }
    }
    
    
    
    /**
     * Select an employee from the list
     */
    public Employee selectEmployee(boolean onlyAvailable) {
        System.out.println("\nEmployee List" + (onlyAvailable ? " (Available Only)" : ""));
        System.out.println("=".repeat(40));
        
        try {
            List<Employee> employees = onlyAvailable ? 
                employeeService.getAvailableEmployees() : 
                employeeService.getAllEmployees();
            
            if (employees.isEmpty()) {
                System.out.println("No " + (onlyAvailable ? "available " : "") + "employees found.");
                return null;
            }
            
            System.out.println("Available employees:");
            for (int i = 0; i < employees.size(); i++) {
                Employee employee = employees.get(i);
                System.out.printf("%d. %s (%s)%n", 
                    i + 1, 
                    employee.getName(), 
                    employee.getAvailabilityStatusText());
            }
            
            System.out.print("Please enter employee number: ");
            int choice = Integer.parseInt(scanner.nextLine().trim());
            
            if (choice >= 1 && choice <= employees.size()) {
                return employees.get(choice - 1);
            } else {
                System.out.println("Invalid selection.");
                return null;
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return null;
        } catch (Exception e) {
            System.out.println("Employee selection failed: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Auto assign available employee
     */
    public Employee autoAssignEmployee() {
        Employee employee = employeeService.getRandomAvailableEmployee();
        if (employee != null) {
            System.out.println("Auto-assigned employee: " + employee.getName());
        }
        return employee;
    }
}
