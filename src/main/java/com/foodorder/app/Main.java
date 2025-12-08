package com.foodorder.app;

import java.util.Scanner;

import com.foodorder.config.DatabaseConnection;
import com.foodorder.controller.CustomerController;
import com.foodorder.controller.EmployeeController;
import com.foodorder.controller.MenuController;
import com.foodorder.controller.OrderController;
import com.foodorder.service.CustomerService;
import com.foodorder.service.EmployeeService;
import com.foodorder.service.MenuService;
import com.foodorder.service.OrderService;

/**
 * Restaurant order system main class
 * Provides system entry point and main menu navigation
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("Restaurant Order Management System starting...");
        
        // Test database connection
        try {
            DatabaseConnection.getInstance().getConnection();
            System.out.println("Database connection successful!");
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            System.err.println("Please ensure PostgreSQL service is running and database configuration is correct.");
            return;
        }
        
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
            displayMainMenu();
            
            try {
                String input = scanner.nextLine();
                if (input == null) {
                    System.out.println("Input stream closed. Exiting...");
                    break;
                }
                
                int choice = Integer.parseInt(input.trim());
                
                switch (choice) {
                    case 1 -> {
                        MenuController menuController = new MenuController();
                        menuController.displayMenuInterface();
                    }
                    case 2 -> {
                        OrderController orderController = new OrderController();
                        orderController.showOrderInterface();
                    }
                    case 3 -> {
                        CustomerController customerController = new CustomerController();
                        customerController.showCustomerInterface();
                    }
                    case 4 -> {
                        EmployeeController employeeController = new EmployeeController();
                        employeeController.showEmployeeInterface();
                    }
                    case 5 -> showSystemStatistics();
                    case 6 -> {
                        System.out.println("Thank you for using Restaurant Order Management System! Goodbye!");
                        // Close database connection
                        DatabaseConnection.getInstance().closeConnection();
                        return;
                    }
                    default -> System.out.println("Invalid selection, please try again.");
                }
                
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number selection.");
            } catch (java.util.NoSuchElementException e) {
                System.out.println("No input available. Exiting...");
                break;
            } catch (Exception e) {
                System.out.println("Error occurred: " + e.getMessage());
                break;
            }
            }
        }
    }
    
    /**
     * Display main menu
     */
    private static void displayMainMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("              Restaurant Order Management System");
        System.out.println("=".repeat(60));
        System.out.println("1. Menu Browse & Search");
        System.out.println("2. Order Management");
        System.out.println("3. Customer Management");
        System.out.println("4. Employee Management");
        System.out.println("5. System Statistics");
        System.out.println("6. Exit System");
        System.out.println("=".repeat(60));
        System.out.print("Please select function module (1-6): ");
    }
    
    /**
     * Display system statistics
     */
    private static void showSystemStatistics() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("              System Statistics");
        System.out.println("=".repeat(60));
        
        try {
            // Create service instances
            MenuService menuService = new MenuService();
            CustomerService customerService = new CustomerService();
            EmployeeService employeeService = new EmployeeService();
            OrderService orderService = new OrderService();
            
            // Basic statistics
            System.out.println("\nSystem Overview:");
            System.out.println("  \u2022 Menu categories: " + menuService.getTotalCategoryCount());
            System.out.println("  \u2022 Available menu items: " + menuService.getTotalMenuItemCount());
            System.out.println("  \u2022 Registered customers: " + customerService.getTotalCustomerCount());
            System.out.println("  \u2022 Total employees: " + employeeService.getTotalEmployeeCount());
            System.out.println("  \u2022 Available employees: " + employeeService.getAvailableEmployeeCount());
            System.out.println("  \u2022 Total orders: " + orderService.getTotalOrderCount());
            System.out.println("  \u2022 Pending orders: " + orderService.getOrderCountByStatus("PENDING"));
            System.out.println("  \u2022 Completed orders: " + orderService.getOrderCountByStatus("COMPLETED"));
            
            // Detailed statistics
            menuService.printMenuStatistics();
            customerService.printCustomerStatistics();
            employeeService.printEmployeeStatistics();
            orderService.printOrderStatistics();
            orderService.printTodayOrderStatistics();
            
        } catch (Exception e) {
            System.err.println("Error occurred while getting statistics: " + e.getMessage());
        }
    }
}
