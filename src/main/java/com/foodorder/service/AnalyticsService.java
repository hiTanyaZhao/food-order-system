package com.foodorder.service;

import com.foodorder.dao.CustomerDAO;
import com.foodorder.dao.EmployeeDAO;
import com.foodorder.dao.MenuDAO;
import com.foodorder.dao.OrderDAO;
import com.foodorder.dao.OrderItemDAO;

/**
 * Data analytics service
 * Provides system-wide data analysis and reporting functionality
 */
public class AnalyticsService {
    
    private MenuDAO menuDAO;
    private CustomerDAO customerDAO;
    private EmployeeDAO employeeDAO;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    
    public AnalyticsService() {
        this.menuDAO = new MenuDAO();
        this.customerDAO = new CustomerDAO();
        this.employeeDAO = new EmployeeDAO();
        this.orderDAO = new OrderDAO();
        this.orderItemDAO = new OrderItemDAO();
    }
    
    /**
     * Generate complete system report
     */
    public void generateSystemReport() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                    Restaurant Management System Complete Report");
        System.out.println("=".repeat(80));
        
        // Menu analysis
        System.out.println("\n=== Menu Analysis ===");
        menuDAO.printMenuStatistics();
        
        // Customer analysis
        System.out.println("\n=== Customer Analysis ===");
        customerDAO.printCustomerStatistics();
        
        // Employee analysis
        System.out.println("\n=== Employee Analysis ===");
        employeeDAO.printEmployeeStatistics();
        employeeDAO.printEmployeeWorkloadStatistics();
        
        // Order analysis
        System.out.println("\n=== Order Analysis ===");
        orderDAO.printOrderStatistics();
        orderDAO.printTodayOrderStatistics();
        
        // Sales analysis
        System.out.println("\n=== Sales Analysis ===");
        orderItemDAO.printPopularItemsStatistics();
        orderItemDAO.printCategorySalesStatistics();
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                         Report End");
        System.out.println("=".repeat(80));
    }
    
    /**
     * Generate business summary
     */
    public void generateBusinessSummary() {
        System.out.println("\nBusiness Summary");
        System.out.println("=".repeat(40));
        
        try {
            // Here you can add more business analysis logic
            System.out.println("System is running normally, all functional modules are ready.");
            System.out.println("Recommend regularly checking employee workload and reasonable order allocation.");
            System.out.println("Pay attention to popular menu item sales and optimize menu configuration.");
            
        } catch (Exception e) {
            System.err.println("Error occurred while generating business summary: " + e.getMessage());
        }
    }
}