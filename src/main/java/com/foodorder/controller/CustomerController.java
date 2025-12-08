package com.foodorder.controller;

import java.util.List;
import java.util.Scanner;

import com.foodorder.model.Customer;
import com.foodorder.service.CustomerService;

/**
 * Controller
 * HandleManagementrelated user interface interactions
 */
public class CustomerController {
    
    private CustomerService customerService;
    private Scanner scanner;
    
    public CustomerController() {
        this.customerService = new CustomerService();
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * DisplayManagement
     */
    public void showCustomerInterface() {
        while (true) {
            displayCustomerOptions();
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1 -> createCustomer();
                    case 2 -> viewAllCustomers();
                    case 3 -> searchCustomers();
                    case 4 -> viewCustomerDetails();
                    case 5 -> updateCustomer();
                    case 6 -> deleteCustomer();
                    case 7 -> customerLogin();
                    case 8 -> viewCustomerStatistics();
                    case 9 -> {
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
    private void displayCustomerOptions() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("             Customer Management System");
        System.out.println("=".repeat(50));
        System.out.println("1. Customer registration");
        System.out.println("2. View all customers");
        System.out.println("3. Search customers");
        System.out.println("4. View customer details");
        System.out.println("5. Update customer information");
        System.out.println("6. Delete customer");
        System.out.println("7. Customer login");
        System.out.println("8. View customer statistics");
        System.out.println("9. Return to main menu");
        System.out.println("=".repeat(50));
        System.out.print("Please select operation (1-9): ");
    }
    
    /**
     * Create new customer
     */
    private void createCustomer() {
        System.out.println("\nCustomer Registration");
        System.out.println("=".repeat(35));
        
        try {
            System.out.print("Please enter customer name: ");
            String name = scanner.nextLine().trim();
            
            System.out.print("Please enter email address: ");
            String email = scanner.nextLine().trim();
            
            System.out.print("Please enter phone number (optional): ");
            String phone = scanner.nextLine().trim();
            if (phone.isEmpty()) phone = null;
            
            int customerId = customerService.createCustomer(name, email, phone);
            
            if (customerId > 0) {
                System.out.println("✓ Customer registration successful! Customer ID: " + customerId);
            } else {
                System.out.println("✗ Customer registration failed.");
            }
            
        } catch (Exception e) {
            System.out.println("Customer registration failed: " + e.getMessage());
        }
    }
    
    /**
     * View all customers
     */
    private void viewAllCustomers() {
        System.out.println("\n information");
        System.out.println("=".repeat(40));
        
        try {
            List<Customer> customers = customerService.getAllCustomers();
            
            if (customers.isEmpty()) {
                System.out.println("No customers found.");
            } else {
                System.out.printf("%-4s %-20s %-30s %-15s%n", "ID", "Name", "Email", "Phone");
                System.out.println("-".repeat(75));
                
                customers.forEach(customer -> 
                    System.out.printf("%-4d %-20s %-30s %-15s%n",
                        customer.getCustomerId(),
                        customer.getName(),
                        customer.getEmail(),
                        customer.getPhone() != null ? customer.getPhone() : "Not provided")
                );
                
                System.out.println("\nTotal customers: " + customers.size());
            }
            
        } catch (Exception e) {
            System.out.println("Failed to retrieve customer information: " + e.getMessage());
        }
    }
    
    /**
     * Search customers
     */
    private void searchCustomers() {
        System.out.println("\nSearch Customers");
        System.out.println("=".repeat(30));
        
        System.out.print("Please enter customer name to search: ");
        String searchTerm = scanner.nextLine().trim();
        
        if (searchTerm.isEmpty()) {
            System.out.println("Search term cannot be empty.");
            return;
        }
        
        try {
            List<Customer> results = customerService.searchCustomersByName(searchTerm);
            
            System.out.println("\nSearch results for: \"" + searchTerm + "\"");
            System.out.println("=".repeat(75));
            
            if (results.isEmpty()) {
                System.out.println("No customers found.");
            } else {
                System.out.printf("%-4s %-20s %-30s %-15s%n", "ID", "Name", "Email", "Phone");
                System.out.println("-".repeat(75));
                
                results.forEach(customer -> 
                    System.out.printf("%-4d %-20s %-30s %-15s%n",
                        customer.getCustomerId(),
                        customer.getName(),
                        customer.getEmail(),
                        customer.getPhone() != null ? customer.getPhone() : "Not provided")
                );
                
                System.out.println("\nFound " + results.size() + " customer(s).");
            }
            
        } catch (Exception e) {
            System.out.println("Customer search failed: " + e.getMessage());
        }
    }
    
    /**
     * View customer details
     */
    private void viewCustomerDetails() {
        System.out.println("\nCustomer Details");
        System.out.println("=".repeat(30));
        
        try {
            System.out.print("Please enter customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine().trim());
            
            String detailInfo = customerService.getCustomerDetailInfo(customerId);
            System.out.println("\n" + detailInfo);
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid customer ID.");
        } catch (Exception e) {
            System.out.println("Failed to retrieve customer details: " + e.getMessage());
        }
    }
    
    /**
     * Update customer information
     */
    private void updateCustomer() {
        System.out.println("\nUpdate Customer Information");
        System.out.println("=".repeat(40));
        
        try {
            System.out.print("Please enter customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine().trim());
            
            // Get current customer information
            Customer existingCustomer = customerService.getCustomerById(customerId);
            if (existingCustomer == null) {
                System.out.println("Customer does not exist.");
                return;
            }
            
            System.out.println("Current customer information:");
            System.out.println("Name: " + existingCustomer.getName());
            System.out.println("Email: " + existingCustomer.getEmail());
            System.out.println("Phone: " + (existingCustomer.getPhone() != null ? existingCustomer.getPhone() : "Not provided"));
            
            System.out.println("\nEnter new information (press Enter to keep current value):");
            
            System.out.print("Name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) name = existingCustomer.getName();
            
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            if (email.isEmpty()) email = existingCustomer.getEmail();
            
            System.out.print("Phone: ");
            String phone = scanner.nextLine().trim();
            if (phone.isEmpty()) phone = existingCustomer.getPhone();
            
            boolean success = customerService.updateCustomer(customerId, name, email, phone);
            
            if (success) {
                System.out.println("✓ Customer information updated successfully!");
            } else {
                System.out.println("✗ Failed to update customer information.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid customer ID.");
        } catch (Exception e) {
            System.out.println("Error updating customer: " + e.getMessage());
        }
    }
    
    /**
     * Delete customer
     */
    private void deleteCustomer() {
        System.out.println("\nDelete Customer");
        System.out.println("=".repeat(30));
        
        try {
            System.out.print("Please enter customer ID to delete: ");
            int customerId = Integer.parseInt(scanner.nextLine().trim());
            
            // Get customer information for confirmation
            Customer customer = customerService.getCustomerById(customerId);
            if (customer == null) {
                System.out.println("Customer does not exist.");
                return;
            }
            
            System.out.println("Customer information:");
            System.out.println("ID: " + customer.getCustomerId());
            System.out.println("Name: " + customer.getName());
            System.out.println("Email: " + customer.getEmail());
            
            // Check if customer has orders
            if (customerService.hasOrders(customerId)) {
                System.out.println("⚠ Warning: This customer has order history.");
            }
            
            System.out.print("Confirm deletion (y/N): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            
            if ("y".equals(confirm) || "yes".equals(confirm)) {
                boolean success = customerService.deleteCustomer(customerId);
                
                if (success) {
                    System.out.println("✓ Customer deleted successfully!");
                } else {
                    System.out.println("✗ Failed to delete customer.");
                }
            } else {
                System.out.println("Deletion cancelled.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid customer ID.");
        } catch (Exception e) {
            System.out.println("Delete operation failed: " + e.getMessage());
        }
    }
    
    /**
     * Customer login
     */
    private void customerLogin() {
        System.out.println("\nCustomer Login");
        System.out.println("=".repeat(30));
        
        try {
            System.out.print("Please enter email address: ");
            String email = scanner.nextLine().trim();
            
            Customer customer = customerService.authenticateCustomer(email);
            
            System.out.println("✓ Login successful!");
            System.out.println("Welcome, " + customer.getName() + "!");
            System.out.println("Customer ID: " + customer.getCustomerId());
            
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }
    
    /**
     * View customer statistics
     */
    private void viewCustomerStatistics() {
        System.out.println("\nCustomer Statistics");
        System.out.println("=".repeat(35));
        
        try {
            System.out.println("Basic statistics:");
            System.out.println("  - Total customer count: " + customerService.getTotalCustomerCount());
            
            customerService.printCustomerStatistics();
            
        } catch (Exception e) {
            System.out.println("Failed to retrieve customer statistics: " + e.getMessage());
        }
    }
    
    /**
     * Select a customer
     */
    public Customer selectCustomer() {
        System.out.println("\nSelect Customer");
        System.out.println("=".repeat(30));
        
        try {
            System.out.println("1. Search by customer ID");
            System.out.println("2. Search by email");
            System.out.println("3. Search by name");
            System.out.print("Please select method: ");
            
            int choice = Integer.parseInt(scanner.nextLine().trim());
            
            switch (choice) {
                case 1 -> {
                    System.out.print("Please enter customer ID: ");
                    int customerId = Integer.parseInt(scanner.nextLine().trim());
                    return customerService.getCustomerById(customerId);
                }
                case 2 -> {
                    System.out.print("Please enter email: ");
                    String email = scanner.nextLine().trim();
                    return customerService.getCustomerByEmail(email);
                }
                case 3 -> {
                    System.out.print("Please enter name to search: ");
                    String searchTerm = scanner.nextLine().trim();
                    List<Customer> results = customerService.searchCustomersByName(searchTerm);
                    
                    if (results.isEmpty()) {
                        System.out.println("No customers found.");
                        return null;
                    }
                    
                    System.out.println("Search results:");
                    for (int i = 0; i < results.size(); i++) {
                        Customer customer = results.get(i);
                        System.out.printf("%d. %s (%s)%n", 
                            i + 1, customer.getName(), customer.getEmail());
                    }
                    
                    System.out.print("Select customer (number): ");
                    int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
                    
                    if (index >= 0 && index < results.size()) {
                        return results.get(index);
                    } else {
                        System.out.println("Invalid selection.");
                        return null;
                    }
                }
                default -> {
                    System.out.println("Invalid selection.");
                    return null;
                }
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return null;
        } catch (Exception e) {
            System.out.println("Customer selection failed: " + e.getMessage());
            return null;
        }
    }
}
