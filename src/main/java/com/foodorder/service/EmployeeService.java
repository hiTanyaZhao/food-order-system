package com.foodorder.service;

import java.util.List;

import com.foodorder.dao.EmployeeDAO;
import com.foodorder.model.Employee;

/**
 * Employee business logic layer
 * Handle employee-related business logic, including data validation and formatting
 */
public class EmployeeService {
    
    private EmployeeDAO employeeDAO;
    
    public EmployeeService() {
        this.employeeDAO = new EmployeeDAO();
    }
    
    /**
     * Create new employee
     */
    public int createEmployee(String name, String phone, boolean availabilityStatus) {
        // Validate input parameters
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("employee namecannot be empty");
        }
        
        Employee employee = new Employee(name.trim(), 
                                       phone != null ? phone.trim() : null, 
                                       availabilityStatus);
        
        return employeeDAO.createEmployee(employee);
    }
    
    /**
     * Get employee by ID
     */
    public Employee getEmployeeById(int employeeId) {
        if (employeeId <= 0) {
            throw new IllegalArgumentException("employee IDmust be greater than0");
        }
        return employeeDAO.getEmployeeById(employeeId);
    }
    
    /**
     * Get all employees
     */
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }
    
    /**
     * Get all available employees
     */
    public List<Employee> getAvailableEmployees() {
        return employeeDAO.getAvailableEmployees();
    }
    
    /**
     * Search employees by name
     */
    public List<Employee> searchEmployeesByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be empty");
        }
        return employeeDAO.searchEmployeesByName(searchTerm.trim());
    }
    
    /**
     * Update information
     */
    public boolean updateEmployee(int employeeId, String name, String phone, boolean availabilityStatus) {
        // Validate input parameters
        if (employeeId <= 0) {
            throw new IllegalArgumentException("employee IDmust be greater than0");
        }
        
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("employee namecannot be empty");
        }
        
        Employee employee = new Employee(employeeId, name.trim(), 
                                       phone != null ? phone.trim() : null, 
                                       availabilityStatus);
        
        return employeeDAO.updateEmployee(employee);
    }
    
    /**
     * Update employee availability status
     */
    public boolean updateEmployeeAvailability(int employeeId, boolean availabilityStatus) {
        if (employeeId <= 0) {
            throw new IllegalArgumentException("employee IDmust be greater than0");
        }
        
        // Check ifexists
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Employee does not exist");
        }
        
        return employeeDAO.updateEmployeeAvailability(employeeId, availabilityStatus);
    }
    
    /**
     * Delete employee
     */
    public boolean deleteEmployee(int employeeId) {
        if (employeeId <= 0) {
            throw new IllegalArgumentException("employee IDmust be greater than0");
        }
        
        // Check ifexists
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Employee does not exist");
        }
        
        return employeeDAO.deleteEmployee(employeeId);
    }
    
    /**
     * Check if employee has orders
     */
    public boolean hasOrders(int employeeId) {
        if (employeeId <= 0) {
            throw new IllegalArgumentException("employee IDmust be greater than0");
        }
        return employeeDAO.hasOrders(employeeId);
    }
    
    /**
     * Validate if employee exists
     */
    public boolean employeeExists(int employeeId) {
        return employeeDAO.getEmployeeById(employeeId) != null;
    }
    
    /**
     * Validate if employee is available
     */
    public boolean isEmployeeAvailable(int employeeId) {
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        return employee != null && employee.isAvailable();
    }
    
    /**
     * Get random available employee for order assignment
     */
    public Employee getRandomAvailableEmployee() {
        return employeeDAO.getRandomAvailableEmployee();
    }
    
    /**
     * Auto assign available employee (returns null if no available employee)
     */
    public Employee assignAvailableEmployee() {
        List<Employee> availableEmployees = employeeDAO.getAvailableEmployees();
        
        if (availableEmployees.isEmpty()) {
            return null;
        }
        
         // Return first available employee, can implement more complex assignment logic
        return availableEmployees.get(0);
    }
    
    /**
     * Format employee list as string for display
     */
    public String formatEmployeesForDisplay(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            return "No employee information found.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-4s %-20s %-15s %-8s%n", "ID", "Name", "Phone", "Status"));
        sb.append("-".repeat(50)).append("\n");
        
        for (Employee employee : employees) {
            sb.append(String.format("%-4d %-20s %-15s %-8s%n",
                employee.getEmployeeId(),
                employee.getName(),
                employee.getPhone() != null ? employee.getPhone() : "not provided",
                employee.getAvailabilityStatusText()
            ));
        }
        
        return sb.toString();
    }
    
    /**
     * Print employee statistics information
     */
    public void printEmployeeStatistics() {
        employeeDAO.printEmployeeStatistics();
    }
    
    /**
     * Print employee workload statistics
     */
    public void printEmployeeWorkloadStatistics() {
        employeeDAO.printEmployeeWorkloadStatistics();
    }
    
    /**
     * Get total employee count
     */
    public int getTotalEmployeeCount() {
        return employeeDAO.getAllEmployees().size();
    }
    
    /**
     * Get available employee count using database function
     */
    public int getAvailableEmployeeCount() {
        return employeeDAO.getAvailableEmployeesCount();
    }
    
    /**
     * Get available employee count (legacy method for compatibility)
     */
    public int getAvailableEmployeeCountLegacy() {
        return employeeDAO.getAvailableEmployees().size();
    }
    
    /**
     * Get employee detailed information including work statistics
     */
    public String getEmployeeDetailInfo(int employeeId) {
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        if (employee == null) {
            return "Employee does not exist";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== Employee Detailed Information ===\n");
        sb.append("ID: ").append(employee.getEmployeeId()).append("\n");
        sb.append("Name: ").append(employee.getName()).append("\n");
        sb.append("Phone: ").append(employee.getPhone() != null ? employee.getPhone() : "Not provided").append("\n");
        sb.append("availability status: ").append(employee.getAvailabilityStatusText()).append("\n");
        sb.append("Order Status: ").append(hasOrders(employeeId) ? "Has handled orders" : "No order records").append("\n");
        
        return sb.toString();
    }
    
    /**
     * Batch update employee availability status
     */
    public int updateMultipleEmployeeAvailability(List<Integer> employeeIds, boolean availabilityStatus) {
        if (employeeIds == null || employeeIds.isEmpty()) {
            throw new IllegalArgumentException("Employee ID list cannot be empty");
        }
        
        int successCount = 0;
        for (Integer employeeId : employeeIds) {
            try {
                if (updateEmployeeAvailability(employeeId, availabilityStatus)) {
                    successCount++;
                }
            } catch (Exception e) {
                System.err.println("Update employee " + employeeId + " status failed: " + e.getMessage());
            }
        }
        
        return successCount;
    }
    
    /**
     * Get employee selection list for UI display
     */
    public String getEmployeeSelectionList(boolean onlyAvailable) {
        List<Employee> employees = onlyAvailable ? 
            employeeDAO.getAvailableEmployees() : 
            employeeDAO.getAllEmployees();
        
        if (employees.isEmpty()) {
            return onlyAvailable ? "No available employees" : "No employees";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            sb.append(String.format("%d. %s (%s)%n", 
                i + 1, 
                employee.getName(), 
                employee.getAvailabilityStatusText()));
        }
        
        return sb.toString();
    }
}
