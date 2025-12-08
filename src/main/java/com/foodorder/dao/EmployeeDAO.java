package com.foodorder.dao;

import com.foodorder.config.DatabaseConnection;
import com.foodorder.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Employee data access layer
 * Handles database interactions for related CRUD operations
 */
public class EmployeeDAO {
    
    private Connection connection;
    
    public EmployeeDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Create new
     */
    public int createEmployee(Employee employee) {
        String sql = "INSERT INTO Employee (name, phone, availability_status) VALUES (?, ?, ?) RETURNING employee_id";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getPhone());
            stmt.setBoolean(3, employee.isAvailabilityStatus());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int employeeId = rs.getInt("employee_id");
                    employee.setEmployeeId(employeeId);
                    return employeeId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating employee: " + e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Get by ID
     */
    public Employee getEmployeeById(int employeeId) {
        String sql = "SELECT employee_id, name, phone, availability_status FROM Employee WHERE employee_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getBoolean("availability_status")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employee by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get all
     */
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT employee_id, name, phone, availability_status FROM Employee ORDER BY name";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Employee employee = new Employee(
                    rs.getInt("employee_id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getBoolean("availability_status")
                );
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all employees: " + e.getMessage());
        }
        
        return employees;
    }
    
    /**
     * Get allavailable
     */
    public List<Employee> getAvailableEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT employee_id, name, phone, availability_status FROM Employee WHERE availability_status = TRUE ORDER BY name";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Employee employee = new Employee(
                    rs.getInt("employee_id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getBoolean("availability_status")
                );
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching available employees: " + e.getMessage());
        }
        
        return employees;
    }
    
    /**
     * Search
     */
    public List<Employee> searchEmployeesByName(String searchTerm) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT employee_id, name, phone, availability_status FROM Employee WHERE LOWER(name) LIKE LOWER(?) ORDER BY name";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchTerm + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Employee employee = new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getBoolean("availability_status")
                    );
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching employees by name: " + e.getMessage());
        }
        
        return employees;
    }
    
    /**
     * Update information
     */
    public boolean updateEmployee(Employee employee) {
        String sql = "UPDATE Employee SET name = ?, phone = ?, availability_status = ? WHERE employee_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getPhone());
            stmt.setBoolean(3, employee.isAvailabilityStatus());
            stmt.setInt(4, employee.getEmployeeId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * updateavailability status
     */
    public boolean updateEmployeeAvailability(int employeeId, boolean availabilityStatus) {
        String sql = "UPDATE Employee SET availability_status = ? WHERE employee_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, availabilityStatus);
            stmt.setInt(2, employeeId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating employee availability: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete
     */
    public boolean deleteEmployee(int employeeId) {
        // First check if has associated orders
        if (hasOrders(employeeId)) {
            System.out.println("Cannot delete employee");
            return false;
        }
        
        String sql = "DELETE FROM Employee WHERE employee_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if
     */
    public boolean hasOrders(int employeeId) {
        String sql = "SELECT COUNT(*) FROM Orders WHERE employee_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking employee orders: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Get random available employeeFor order assignment
     */
    public Employee getRandomAvailableEmployee() {
        String sql = "SELECT employee_id, name, phone, availability_status FROM Employee WHERE availability_status = TRUE ORDER BY RANDOM() LIMIT 1";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return new Employee(
                    rs.getInt("employee_id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getBoolean("availability_status")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching random available employee: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * workloadstatistics
     */
    public void printEmployeeWorkloadStatistics() {
        String sql = """
            SELECT 
                e.employee_id,
                e.name,
                e.availability_status,
                COUNT(o.order_id) as total_orders,
                COUNT(CASE WHEN o.current_status = 'PENDING' THEN 1 END) as pending_orders,
                COUNT(CASE WHEN o.current_status = 'PREPARING' THEN 1 END) as preparing_orders,
                COUNT(CASE WHEN o.current_status = 'COMPLETED' THEN 1 END) as completed_orders
            FROM Employee e
            LEFT JOIN Orders o ON e.employee_id = o.employee_id
            GROUP BY e.employee_id, e.name, e.availability_status
            ORDER BY total_orders DESC, e.name
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            System.out.println("\n=== workloadstatistics ===");
            System.out.printf("%-20s %-8s %-8s %-8s %-8s %-8s%n", 
                            "employee name", "", "total orders", "Handle", "preparing", "complete");
            System.out.println("-".repeat(70));
            
            while (rs.next()) {
                System.out.printf("%-20s %-8s %-8d %-8d %-8d %-8d%n",
                    rs.getString("name"),
                    rs.getBoolean("availability_status") ? "available" : "available",
                    rs.getInt("total_orders"),
                    rs.getInt("pending_orders"),
                    rs.getInt("preparing_orders"),
                    rs.getInt("completed_orders")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employee workload statistics: " + e.getMessage());
        }
    }
    
    /**
     * statisticsinformation
     */
    public void printEmployeeStatistics() {
        String sql = """
            SELECT 
                COUNT(*) as total_employees,
                COUNT(CASE WHEN availability_status = TRUE THEN 1 END) as available_employees,
                COUNT(CASE WHEN availability_status = FALSE THEN 1 END) as unavailable_employees
            FROM Employee
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                System.out.println("\n=== statisticsinformation ===");
                System.out.println("total employee count: " + rs.getInt("total_employees"));
                System.out.println("available: " + rs.getInt("available_employees"));
                System.out.println("unavailable employees: " + rs.getInt("unavailable_employees"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employee statistics: " + e.getMessage());
        }
    }
}
