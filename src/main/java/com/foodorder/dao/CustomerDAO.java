package com.foodorder.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.foodorder.config.DatabaseConnection;
import com.foodorder.model.Customer;

/**
 * Customer data access layer
 * Handles database interactions for related CRUD operations
 */
public class CustomerDAO {
    
    private Connection connection;
    
    public CustomerDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Create new
     */
    public int createCustomer(Customer customer) {
        String sql = "INSERT INTO Customer (name, email, phone) VALUES (?, ?, ?) RETURNING customer_id";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getPhone());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int customerId = rs.getInt("customer_id");
                    customer.setCustomerId(customerId);
                    return customerId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating customer: " + e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Get by ID
     */
    public Customer getCustomerById(int customerId) {
        String sql = "SELECT customer_id, name, email, phone FROM Customer WHERE customer_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching customer by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get by
     */
    public Customer getCustomerByEmail(String email) {
        String sql = "SELECT customer_id, name, email, phone FROM Customer WHERE email = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching customer by email: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get all
     */
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT customer_id, name, email, phone FROM Customer ORDER BY name";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Customer customer = new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all customers: " + e.getMessage());
        }
        
        return customers;
    }
    
    /**
     * Search
     */
    public List<Customer> searchCustomersByName(String searchTerm) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT customer_id, name, email, phone FROM Customer WHERE LOWER(name) LIKE LOWER(?) ORDER BY name";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchTerm + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Customer customer = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                    );
                    customers.add(customer);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching customers by name: " + e.getMessage());
        }
        
        return customers;
    }
    
    /**
     * Update information
     */
    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE Customer SET name = ?, email = ?, phone = ? WHERE customer_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getPhone());
            stmt.setInt(4, customer.getCustomerId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating customer: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * DeleteSoft delete - actually we do not delete customers with orders
     */
    public boolean deleteCustomer(int customerId) {
        // First check if has orders
        if (hasOrders(customerId)) {
            System.out.println("Cannot delete customer");
            return false;
        }
        
        String sql = "DELETE FROM Customer WHERE customer_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if
     */
    public boolean hasOrders(int customerId) {
        String sql = "SELECT COUNT(*) FROM Orders WHERE customer_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking customer orders: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Check ifalready exists
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM Customer WHERE email = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Check ifalready existscustomer ID
     */
    public boolean emailExistsForOtherCustomer(String email, int excludeCustomerId) {
        String sql = "SELECT COUNT(*) FROM Customer WHERE email = ? AND customer_id != ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setInt(2, excludeCustomerId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking email existence for other customer: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Get customer order count using database function
     */
    public int getCustomerOrderCount(int customerId) {
        String sql = "SELECT get_customer_order_count(?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting customer order count: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Customer statistics information
     */
    public void printCustomerStatistics() {
        String sql = """
            SELECT 
                COUNT(*) as total_customers,
                COUNT(CASE WHEN phone IS NOT NULL AND phone != '' THEN 1 END) as customers_with_phone
            FROM Customer
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                System.out.println("\n=== statisticsinformation ===");
                System.out.println("total customer count: " + rs.getInt("total_customers"));
                System.out.println("phone number: " + rs.getInt("customers_with_phone"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching customer statistics: " + e.getMessage());
        }
    }
}
