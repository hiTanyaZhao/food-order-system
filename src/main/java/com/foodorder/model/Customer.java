package com.foodorder.model;

/**
 * Customer model class
 */
public class Customer {
    private int customerId;
    private String name;
    private String email;
    private String phone;
    
    // Constructors
    public Customer() {}
    
    public Customer(int customerId, String name, String email, String phone) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    
    public Customer(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    
    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    /**
     * Validate email format
     */
    public boolean isValidEmail() {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    /**
     * Validate customer information completeness
     */
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() && 
               email != null && !email.trim().isEmpty() && isValidEmail();
    }
    
    @Override
    public String toString() {
        return String.format("Customer{id=%d, name='%s', email='%s', phone='%s'}", 
                           customerId, name, email, phone);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;
        return customerId == customer.customerId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(customerId);
    }
}
