package com.foodorder.model;

/**
 * Employee model class
 */
public class Employee {
    private int employeeId;
    private String name;
    private String phone;
    private boolean availabilityStatus;
    
    // Constructors
    public Employee() {}
    
    public Employee(int employeeId, String name, String phone, boolean availabilityStatus) {
        this.employeeId = employeeId;
        this.name = name;
        this.phone = phone;
        this.availabilityStatus = availabilityStatus;
    }
    
    public Employee(String name, String phone, boolean availabilityStatus) {
        this.name = name;
        this.phone = phone;
        this.availabilityStatus = availabilityStatus;
    }
    
    // Getters and Setters
    public int getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public boolean isAvailabilityStatus() {
        return availabilityStatus;
    }
    
    public void setAvailabilityStatus(boolean availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
    
    /**
     * Check if employee is available
     */
    public boolean isAvailable() {
        return availabilityStatus;
    }
    
    /**
     * Get availability status text description
     */
    public String getAvailabilityStatusText() {
        return availabilityStatus ? "Available" : "Unavailable";
    }
    
    /**
     * Validate employee information completeness
     */
    public boolean isValid() {
        return name != null && !name.trim().isEmpty();
    }
    
    @Override
    public String toString() {
        return String.format("Employee{id=%d, name='%s', phone='%s', available=%s}", 
                           employeeId, name, phone, getAvailabilityStatusText());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee employee = (Employee) obj;
        return employeeId == employee.employeeId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(employeeId);
    }
}
