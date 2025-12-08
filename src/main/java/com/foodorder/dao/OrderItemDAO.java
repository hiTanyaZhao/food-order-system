package com.foodorder.dao;

import com.foodorder.config.DatabaseConnection;
import com.foodorder.model.OrderItem;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Order item data access layer
 * Handles database interactions for related CRUD operations
 */
public class OrderItemDAO {
    
    private Connection connection;
    
    public OrderItemDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Add order items
     */
    public boolean addOrderItem(OrderItem orderItem) {
        String sql = "INSERT INTO OrderItem (order_id, item_id, quantity) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getItemId());
            stmt.setInt(3, orderItem.getQuantity());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding order item: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Add order items
     */
    public boolean addOrderItems(List<OrderItem> orderItems) {
        String sql = "INSERT INTO OrderItem (order_id, item_id, quantity) VALUES (?, ?, ?)";
        
        try {
            connection.setAutoCommit(false);
            
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                for (OrderItem item : orderItems) {
                    stmt.setInt(1, item.getOrderId());
                    stmt.setInt(2, item.getItemId());
                    stmt.setInt(3, item.getQuantity());
                    stmt.addBatch();
                }
                
                int[] results = stmt.executeBatch();
                connection.commit();
                
                // Check ifsuccessful
                for (int result : results) {
                    if (result <= 0) {
                        return false;
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error rolling back transaction: " + rollbackEx.getMessage());
            }
            System.err.println("Error adding order items: " + e.getMessage());
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
     * Get byinformation
     */
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = """
            SELECT oi.order_id, oi.item_id, oi.quantity,
                   m.item_name, m.current_price, c.name as category_name
            FROM OrderItem oi
            JOIN MenuItem m ON oi.item_id = m.item_id
            JOIN Category c ON m.category_id = c.category_id
            WHERE oi.order_id = ?
            ORDER BY c.name, m.item_name
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem orderItem = new OrderItem(
                        rs.getInt("order_id"),
                        rs.getInt("item_id"),
                        rs.getInt("quantity"),
                        rs.getString("item_name"),
                        rs.getBigDecimal("current_price")
                    );
                    orderItem.setCategoryName(rs.getString("category_name"));
                    orderItems.add(orderItem);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching order items by order ID: " + e.getMessage());
        }
        
        return orderItems;
    }
    
    /**
     * 
     */
    public OrderItem getOrderItem(int orderId, int itemId) {
        String sql = """
            SELECT oi.order_id, oi.item_id, oi.quantity,
                   m.item_name, m.current_price, c.name as category_name
            FROM OrderItem oi
            JOIN MenuItem m ON oi.item_id = m.item_id
            JOIN Category c ON m.category_id = c.category_id
            WHERE oi.order_id = ? AND oi.item_id = ?
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, itemId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    OrderItem orderItem = new OrderItem(
                        rs.getInt("order_id"),
                        rs.getInt("item_id"),
                        rs.getInt("quantity"),
                        rs.getString("item_name"),
                        rs.getBigDecimal("current_price")
                    );
                    orderItem.setCategoryName(rs.getString("category_name"));
                    return orderItem;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching order item: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * updatequantity
     */
    public boolean updateOrderItemQuantity(int orderId, int itemId, int newQuantity) {
        if (newQuantity <= 0) {
            return removeOrderItem(orderId, itemId);
        }
        
        String sql = "UPDATE OrderItem SET quantity = ? WHERE order_id = ? AND item_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, newQuantity);
            stmt.setInt(2, orderId);
            stmt.setInt(3, itemId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating order item quantity: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete
     */
    public boolean removeOrderItem(int orderId, int itemId) {
        String sql = "DELETE FROM OrderItem WHERE order_id = ? AND item_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, itemId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error removing order item: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete
     */
    public boolean removeAllOrderItems(int orderId) {
        String sql = "DELETE FROM OrderItem WHERE order_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected >= 0; // successful
        } catch (SQLException e) {
            System.err.println("Error removing all order items: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check ifexists
     */
    public boolean orderItemExists(int orderId, int itemId) {
        String sql = "SELECT COUNT(*) FROM OrderItem WHERE order_id = ? AND item_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, itemId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking order item existence: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Calculatetotal amount
     */
    public BigDecimal calculateOrderTotal(int orderId) {
        String sql = """
            SELECT SUM(oi.quantity * m.current_price) as total
            FROM OrderItem oi
            JOIN MenuItem m ON oi.item_id = m.item_id
            WHERE oi.order_id = ?
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal total = rs.getBigDecimal("total");
                    return total != null ? total : BigDecimal.ZERO;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error calculating order total: " + e.getMessage());
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * quantity
     */
    public int getOrderItemCount(int orderId) {
        String sql = "SELECT COUNT(*) FROM OrderItem WHERE order_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting order item count: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Total items in orderquantity
     */
    public int getTotalItemQuantity(int orderId) {
        String sql = "SELECT SUM(quantity) FROM OrderItem WHERE order_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting total item quantity: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * statistics
     */
    public void printPopularItemsStatistics() {
        String sql = """
            SELECT 
                m.item_name,
                c.name as category_name,
                SUM(oi.quantity) as total_ordered,
                COUNT(DISTINCT oi.order_id) as order_count,
                AVG(oi.quantity) as avg_quantity_per_order
            FROM OrderItem oi
            JOIN MenuItem m ON oi.item_id = m.item_id
            JOIN Category c ON m.category_id = c.category_id
            JOIN Orders o ON oi.order_id = o.order_id
            WHERE o.current_status != 'CANCELLED'
            GROUP BY m.item_id, m.item_name, c.name
            ORDER BY total_ordered DESC
            LIMIT 10
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            System.out.println("\n=== statistics (Top 10) ===");
            System.out.printf("%-25s %-15s %-8s %-8s %-8s%n", 
                            "menu item name", "category", "total sales", "order count", "quantity");
            System.out.println("-".repeat(70));
            
            while (rs.next()) {
                System.out.printf("%-25s %-15s %-8d %-8d %-8.1f%n",
                    rs.getString("item_name"),
                    rs.getString("category_name"),
                    rs.getInt("total_ordered"),
                    rs.getInt("order_count"),
                    rs.getDouble("avg_quantity_per_order")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching popular items statistics: " + e.getMessage());
        }
    }
    
    /**
     * categorystatistics
     */
    public void printCategorySalesStatistics() {
        String sql = """
            SELECT 
                c.name as category_name,
                SUM(oi.quantity) as total_quantity,
                SUM(oi.quantity * m.current_price) as total_revenue,
                COUNT(DISTINCT oi.item_id) as unique_items_sold,
                COUNT(DISTINCT oi.order_id) as order_count
            FROM OrderItem oi
            JOIN MenuItem m ON oi.item_id = m.item_id
            JOIN Category c ON m.category_id = c.category_id
            JOIN Orders o ON oi.order_id = o.order_id
            WHERE o.current_status != 'CANCELLED'
            GROUP BY c.category_id, c.name
            ORDER BY total_revenue DESC
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            System.out.println("\n=== categorystatistics ===");
            System.out.printf("%-15s %-8s %-12s %-8s %-8s%n", 
                            "category", "sales", "revenue", "variety count", "order count");
            System.out.println("-".repeat(60));
            
            while (rs.next()) {
                System.out.printf("%-15s %-8d %-12s %-8d %-8d%n",
                    rs.getString("category_name"),
                    rs.getInt("total_quantity"),
                    String.format("$%.2f", rs.getBigDecimal("total_revenue")),
                    rs.getInt("unique_items_sold"),
                    rs.getInt("order_count")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching category sales statistics: " + e.getMessage());
        }
    }
}
