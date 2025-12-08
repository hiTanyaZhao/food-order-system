package com.foodorder.service;

import java.util.List;

import com.foodorder.dao.CustomerDAO;
import com.foodorder.model.Customer;

/**
 * Business logic layer
 * HandleValidateFormat
 */
public class CustomerService {
    
    private CustomerDAO customerDAO;
    
    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }
    
    /**
     * Create new
     */
    public int createCustomer(String name, String email, String phone) {
        // Validate
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("customer namecannot be empty");
        }
        
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("email addresscannot be empty");
        }
        
        // Validate
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("incorrect format");
        }
        
        // Check ifalready exists
        if (customerDAO.emailExists(email)) {
            throw new IllegalArgumentException("registration");
        }
        
        Customer customer = new Customer(name.trim(), email.trim(), 
                                       phone != null ? phone.trim() : null);
        
        return customerDAO.createCustomer(customer);
    }
    
    /**
     * Get by ID
     */
    public Customer getCustomerById(int customerId) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("customer IDmust be greater than0");
        }
        return customerDAO.getCustomerById(customerId);
    }
    
    /**
     * Get by
     */
    public Customer getCustomerByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("email addresscannot be empty");
        }
        return customerDAO.getCustomerByEmail(email.trim());
    }
    
    /**
     * Get all
     */
    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }
    
    /**
     * Search
     */
    public List<Customer> searchCustomersByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Searchcannot be empty");
        }
        return customerDAO.searchCustomersByName(searchTerm.trim());
    }
    
    /**
     * Update information
     */
    public boolean updateCustomer(int customerId, String name, String email, String phone) {
        // Validate
        if (customerId <= 0) {
            throw new IllegalArgumentException("customer IDmust be greater than0");
        }
        
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("customer namecannot be empty");
        }
        
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("email addresscannot be empty");
        }
        
        // Validate
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("incorrect format");
        }
        
        // Check if
        if (customerDAO.emailExistsForOtherCustomer(email, customerId)) {
            throw new IllegalArgumentException("");
        }
        
        Customer customer = new Customer(customerId, name.trim(), email.trim(), 
                                       phone != null ? phone.trim() : null);
        
        return customerDAO.updateCustomer(customer);
    }
    
    /**
     * Delete
     */
    public boolean deleteCustomer(int customerId) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("customer IDmust be greater than0");
        }
        
        // Check ifexists
        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer does not exist");
        }
        
        return customerDAO.deleteCustomer(customerId);
    }
    
    /**
     * Validate
     */
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    /**
     * Check if
     */
    public boolean hasOrders(int customerId) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("customer IDmust be greater than0");
        }
        return customerDAO.hasOrders(customerId);
    }
    
    /**
     * Validateexists
     */
    public boolean customerExists(int customerId) {
        return customerDAO.getCustomerById(customerId) != null;
    }
    
    /**
     * FormatDisplay
     */
    public String formatCustomersForDisplay(List<Customer> customers) {
        if (customers == null || customers.isEmpty()) {
            return "foundinformation";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-4s %-20s %-30s %-15s%n", "ID", "", "", ""));
        sb.append("-".repeat(75)).append("\n");
        
        for (Customer customer : customers) {
            sb.append(String.format("%-4d %-20s %-30s %-15s%n",
                customer.getCustomerId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone() != null ? customer.getPhone() : "not provided"
            ));
        }
        
        return sb.toString();
    }
    
    /**
     * statisticsinformation
     */
    public void printCustomerStatistics() {
        customerDAO.printCustomerStatistics();
    }
    
    /**
     * 
     */
    public int getTotalCustomerCount() {
        return customerDAO.getAllCustomers().size();
    }
    
    
    /**
     * Get customer order count using database function
     */
    public int getCustomerOrderCount(int customerId) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("Customer ID must be greater than 0");
        }
        return customerDAO.getCustomerOrderCount(customerId);
    }
    
    /**
     * Get customer detailed information
     */
    public String getCustomerDetailInfo(int customerId) {
        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer == null) {
            return "Customer does not exist";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== Customer Detailed Information ===\n");
        sb.append("ID: ").append(customer.getCustomerId()).append("\n");
        sb.append("Name: ").append(customer.getName()).append("\n");
        sb.append("Email: ").append(customer.getEmail()).append("\n");
        sb.append("Phone: ").append(customer.getPhone() != null ? customer.getPhone() : "Not provided").append("\n");
        sb.append("Order History: ").append(hasOrders(customerId) ? "Has order history" : "No orders yet").append("\n");
        
        return sb.toString();
    }
}
