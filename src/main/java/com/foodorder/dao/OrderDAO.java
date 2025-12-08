package com.foodorder.dao;

import com.foodorder.config.DatabaseConnection;
import com.foodorder.model.Order;
import com.foodorder.model.OrderItem;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Order data access layer
 * Handles database interactions for related CRUD operations
 */
public class OrderDAO {
    
    private Connection connection;
    
    public OrderDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Create new
     */
    public int createOrder(Order order) {
        String sql = "INSERT INTO Orders (customer_id, employee_id, order_time, total_amount, current_status) VALUES (?, ?, ?, ?, ?) RETURNING order_id";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, order.getCustomerId());
            stmt.setInt(2, order.getEmployeeId());
            stmt.setTimestamp(3, order.getOrderTime());
            stmt.setBigDecimal(4, order.getTotalAmount());
            stmt.setString(5, order.getCurrentStatus());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    order.setOrderId(orderId);
                    return orderId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating order: " + e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Get by IDinformation
     */
    public Order getOrderById(int orderId) {
        String sql = """
            SELECT o.order_id, o.customer_id, o.employee_id, o.order_time, 
                   o.total_amount, o.current_status,
                   c.name as customer_name, e.name as employee_name
            FROM Orders o
            JOIN Customer c ON o.customer_id = c.customer_id
            JOIN Employee e ON o.employee_id = e.employee_id
            WHERE o.order_id = ?
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = new Order(
                        rs.getInt("order_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("employee_id"),
                        rs.getTimestamp("order_time"),
                        rs.getBigDecimal("total_amount"),
                        rs.getString("current_status")
                    );
                    order.setCustomerName(rs.getString("customer_name"));
                    order.setEmployeeName(rs.getString("employee_name"));
                    return order;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching order by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get all
     */
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = """
            SELECT o.order_id, o.customer_id, o.employee_id, o.order_time, 
                   o.total_amount, o.current_status,
                   c.name as customer_name, e.name as employee_name
            FROM Orders o
            JOIN Customer c ON o.customer_id = c.customer_id
            JOIN Employee e ON o.employee_id = e.employee_id
            ORDER BY o.order_time DESC
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("order_id"),
                    rs.getInt("customer_id"),
                    rs.getInt("employee_id"),
                    rs.getTimestamp("order_time"),
                    rs.getBigDecimal("total_amount"),
                    rs.getString("current_status")
                );
                order.setCustomerName(rs.getString("customer_name"));
                order.setEmployeeName(rs.getString("employee_name"));
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all orders: " + e.getMessage());
        }
        
        return orders;
    }
    
    /**
     * Get by
     */
    public List<Order> getOrdersByCustomerId(int customerId) {
        List<Order> orders = new ArrayList<>();
        String sql = """
            SELECT o.order_id, o.customer_id, o.employee_id, o.order_time, 
                   o.total_amount, o.current_status,
                   c.name as customer_name, e.name as employee_name
            FROM Orders o
            JOIN Customer c ON o.customer_id = c.customer_id
            JOIN Employee e ON o.employee_id = e.employee_id
            WHERE o.customer_id = ?
            ORDER BY o.order_time DESC
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(
                        rs.getInt("order_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("employee_id"),
                        rs.getTimestamp("order_time"),
                        rs.getBigDecimal("total_amount"),
                        rs.getString("current_status")
                    );
                    order.setCustomerName(rs.getString("customer_name"));
                    order.setEmployeeName(rs.getString("employee_name"));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching orders by customer ID: " + e.getMessage());
        }
        
        return orders;
    }
    
    /**
     * Get by
     */
    public List<Order> getOrdersByEmployeeId(int employeeId) {
        List<Order> orders = new ArrayList<>();
        String sql = """
            SELECT o.order_id, o.customer_id, o.employee_id, o.order_time, 
                   o.total_amount, o.current_status,
                   c.name as customer_name, e.name as employee_name
            FROM Orders o
            JOIN Customer c ON o.customer_id = c.customer_id
            JOIN Employee e ON o.employee_id = e.employee_id
            WHERE o.employee_id = ?
            ORDER BY o.order_time DESC
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(
                        rs.getInt("order_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("employee_id"),
                        rs.getTimestamp("order_time"),
                        rs.getBigDecimal("total_amount"),
                        rs.getString("current_status")
                    );
                    order.setCustomerName(rs.getString("customer_name"));
                    order.setEmployeeName(rs.getString("employee_name"));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching orders by employee ID: " + e.getMessage());
        }
        
        return orders;
    }
    
    /**
     * Get by
     */
    public List<Order> getOrdersByStatus(String status) {
        List<Order> orders = new ArrayList<>();
        String sql = """
            SELECT o.order_id, o.customer_id, o.employee_id, o.order_time, 
                   o.total_amount, o.current_status,
                   c.name as customer_name, e.name as employee_name
            FROM Orders o
            JOIN Customer c ON o.customer_id = c.customer_id
            JOIN Employee e ON o.employee_id = e.employee_id
            WHERE o.current_status = ?
            ORDER BY o.order_time DESC
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(
                        rs.getInt("order_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("employee_id"),
                        rs.getTimestamp("order_time"),
                        rs.getBigDecimal("total_amount"),
                        rs.getString("current_status")
                    );
                    order.setCustomerName(rs.getString("customer_name"));
                    order.setEmployeeName(rs.getString("employee_name"));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching orders by status: " + e.getMessage());
        }
        
        return orders;
    }
    
    /**
     * Update order status
     */
    public boolean updateOrderStatus(int orderId, String newStatus) {
        String sql = "UPDATE Orders SET current_status = ? WHERE order_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, orderId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating order status: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * updatetotal amount
     */
    public boolean updateOrderTotal(int orderId, BigDecimal totalAmount) {
        String sql = "UPDATE Orders SET total_amount = ? WHERE order_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, totalAmount);
            stmt.setInt(2, orderId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating order total: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * DeleteOrder with order items
     */
    public boolean deleteOrder(int orderId) {
        try {
            connection.setAutoCommit(false);
            
            // First order items
            String deleteItemsSql = "DELETE FROM OrderItem WHERE order_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteItemsSql)) {
                stmt.setInt(1, orderId);
                stmt.executeUpdate();
            }
            
            // Then order
            String deleteOrderSql = "DELETE FROM Orders WHERE order_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteOrderSql)) {
                stmt.setInt(1, orderId);
                int rowsAffected = stmt.executeUpdate();
                
                connection.commit();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error rolling back transaction: " + rollbackEx.getMessage());
            }
            System.err.println("Error deleting order: " + e.getMessage());
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
    
    /**
     * CalculateUpdate ordertotal amount
     */
    public boolean recalculateOrderTotal(int orderId) {
        String sql = """
            UPDATE Orders 
            SET total_amount = (
                SELECT COALESCE(SUM(oi.quantity * m.current_price), 0)
                FROM OrderItem oi
                JOIN MenuItem m ON oi.item_id = m.item_id
                WHERE oi.order_id = ?
            )
            WHERE order_id = ?
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, orderId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error recalculating order total: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * statisticsinformation
     */
    public void printOrderStatistics() {
        String sql = """
            SELECT 
                current_status,
                COUNT(*) as order_count,
                AVG(total_amount) as avg_amount,
                SUM(total_amount) as total_revenue
            FROM Orders
            GROUP BY current_status
            ORDER BY 
                CASE current_status
                    WHEN 'PENDING' THEN 1
                    WHEN 'ACCEPTED' THEN 2
                    WHEN 'PREPARING' THEN 3
                    WHEN 'COMPLETED' THEN 4
                    WHEN 'CANCELLED' THEN 5
                END
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            System.out.println("\n=== statisticsinformation ===");
            System.out.printf("%-12s %-8s %-12s %-12s%n", 
                            "order status", "quantity", "average amount", "total revenue");
            System.out.println("-".repeat(50));
            
            BigDecimal totalRevenue = BigDecimal.ZERO;
            int totalOrders = 0;
            
            while (rs.next()) {
                String status = rs.getString("current_status");
                int count = rs.getInt("order_count");
                BigDecimal avgAmount = rs.getBigDecimal("avg_amount");
                BigDecimal revenue = rs.getBigDecimal("total_revenue");
                
                System.out.printf("%-12s %-8d %-12s %-12s%n",
                    getStatusDescription(status),
                    count,
                    avgAmount != null ? String.format("$%.2f", avgAmount) : "$0.00",
                    revenue != null ? String.format("$%.2f", revenue) : "$0.00"
                );
                
                totalOrders += count;
                if (revenue != null) {
                    totalRevenue = totalRevenue.add(revenue);
                }
            }
            
            System.out.println("-".repeat(50));
            System.out.printf("total: %d orderstotal revenue: $%.2f%n", totalOrders, totalRevenue);
            
        } catch (SQLException e) {
            System.err.println("Error fetching order statistics: " + e.getMessage());
        }
    }
    
    /**
     * Get status description
     */
    private String getStatusDescription(String status) {
        return switch (status) {
            case "PENDING" -> "Handle";
            case "ACCEPTED" -> "accepted";
            case "PREPARING" -> "preparing";
            case "COMPLETED" -> "complete";
            case "CANCELLED" -> "cancel";
            default -> status;
        };
    }
    
    /**
     * statistics
     */
    public void printTodayOrderStatistics() {
        String sql = """
            SELECT 
                COUNT(*) as today_orders,
                COUNT(CASE WHEN current_status = 'COMPLETED' THEN 1 END) as completed_orders,
                SUM(CASE WHEN current_status = 'COMPLETED' THEN total_amount ELSE 0 END) as today_revenue
            FROM Orders
            WHERE DATE(order_time) = CURRENT_DATE
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                System.out.println("\n=== statistics ===");
                System.out.println("Today orders: " + rs.getInt("today_orders"));
                System.out.println("Completed orders: " + rs.getInt("completed_orders"));
                System.out.printf("Today revenue: $%.2f%n", rs.getBigDecimal("today_revenue"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching today's order statistics: " + e.getMessage());
        }
    }
}
