package com.foodorder.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.foodorder.config.DatabaseConnection;
import com.foodorder.model.Category;
import com.foodorder.model.MenuItem;

/**
 * Menu data access layer
 * Handles database interactions for menu-related CRUD operations
 */
public class MenuDAO {
    
    private Connection connection;
    
    public MenuDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Get all categories
     */
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT category_id, name FROM Category ORDER BY name";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Category category = new Category(
                    rs.getInt("category_id"),
                    rs.getString("name")
                );
                categories.add(category);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching categories: " + e.getMessage());
        }
        
        return categories;
    }
    
    /**
     * Get all available menu items with category information
     */
    public List<MenuItem> getAllAvailableMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = """
            SELECT m.item_id, m.category_id, c.name as category_name, 
                   m.item_name, m.current_price, m.is_active
            FROM MenuItem m
            JOIN Category c ON m.category_id = c.category_id
            WHERE m.is_active = TRUE
            ORDER BY c.name, m.item_name
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                MenuItem menuItem = new MenuItem(
                    rs.getInt("item_id"),
                    rs.getInt("category_id"),
                    rs.getString("category_name"),
                    rs.getString("item_name"),
                    rs.getBigDecimal("current_price"),
                    rs.getBoolean("is_active")
                );
                menuItems.add(menuItem);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching menu items: " + e.getMessage());
        }
        
        return menuItems;
    }
    
    /**
     * Get menu items by category
     */
    public List<MenuItem> getMenuItemsByCategory(int categoryId) {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = """
            SELECT m.item_id, m.category_id, c.name as category_name, 
                   m.item_name, m.current_price, m.is_active
            FROM MenuItem m
            JOIN Category c ON m.category_id = c.category_id
            WHERE m.category_id = ? AND m.is_active = TRUE
            ORDER BY m.item_name
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MenuItem menuItem = new MenuItem(
                        rs.getInt("item_id"),
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getString("item_name"),
                        rs.getBigDecimal("current_price"),
                        rs.getBoolean("is_active")
                    );
                    menuItems.add(menuItem);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching menu items by category: " + e.getMessage());
        }
        
        return menuItems;
    }
    
    /**
     * Search menu items by name (fuzzy search)
     */
    public List<MenuItem> searchMenuItemsByName(String searchTerm) {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = """
            SELECT m.item_id, m.category_id, c.name as category_name, 
                   m.item_name, m.current_price, m.is_active
            FROM MenuItem m
            JOIN Category c ON m.category_id = c.category_id
            WHERE m.is_active = TRUE AND LOWER(m.item_name) LIKE LOWER(?)
            ORDER BY m.item_name
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchTerm + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MenuItem menuItem = new MenuItem(
                        rs.getInt("item_id"),
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getString("item_name"),
                        rs.getBigDecimal("current_price"),
                        rs.getBoolean("is_active")
                    );
                    menuItems.add(menuItem);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching menu items by name: " + e.getMessage());
        }
        
        return menuItems;
    }
    
    /**
     * Search menu items by price range
     */
    public List<MenuItem> searchMenuItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = """
            SELECT m.item_id, m.category_id, c.name as category_name, 
                   m.item_name, m.current_price, m.is_active
            FROM MenuItem m
            JOIN Category c ON m.category_id = c.category_id
            WHERE m.is_active = TRUE AND m.current_price BETWEEN ? AND ?
            ORDER BY m.current_price, m.item_name
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, minPrice);
            stmt.setBigDecimal(2, maxPrice);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MenuItem menuItem = new MenuItem(
                        rs.getInt("item_id"),
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getString("item_name"),
                        rs.getBigDecimal("current_price"),
                        rs.getBoolean("is_active")
                    );
                    menuItems.add(menuItem);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching menu items by price range: " + e.getMessage());
        }
        
        return menuItems;
    }
    
    /**
     * Comprehensive search by name, category and price range
     */
    public List<MenuItem> searchMenuItems(String searchTerm, Integer categoryId, 
                                        BigDecimal minPrice, BigDecimal maxPrice) {
        List<MenuItem> menuItems = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT m.item_id, m.category_id, c.name as category_name, 
                   m.item_name, m.current_price, m.is_active
            FROM MenuItem m
            JOIN Category c ON m.category_id = c.category_id
            WHERE m.is_active = TRUE
            """);
        
        List<Object> parameters = new ArrayList<>();
        
         // Add name search condition
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql.append(" AND LOWER(m.item_name) LIKE LOWER(?)");
            parameters.add("%" + searchTerm.trim() + "%");
        }
        
         // Add category search condition
        if (categoryId != null && categoryId > 0) {
            sql.append(" AND m.category_id = ?");
            parameters.add(categoryId);
        }
        
         // Add price range search condition
        if (minPrice != null) {
            sql.append(" AND m.current_price >= ?");
            parameters.add(minPrice);
        }
        
        if (maxPrice != null) {
            sql.append(" AND m.current_price <= ?");
            parameters.add(maxPrice);
        }
        
        sql.append(" ORDER BY c.name, m.item_name");
        
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            // Set parameters
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MenuItem menuItem = new MenuItem(
                        rs.getInt("item_id"),
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getString("item_name"),
                        rs.getBigDecimal("current_price"),
                        rs.getBoolean("is_active")
                    );
                    menuItems.add(menuItem);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in comprehensive menu search: " + e.getMessage());
        }
        
        return menuItems;
    }
    
    /**
     * Get single menu item by ID
     */
    public MenuItem getMenuItemById(int itemId) {
        String sql = """
            SELECT m.item_id, m.category_id, c.name as category_name, 
                   m.item_name, m.current_price, m.is_active
            FROM MenuItem m
            JOIN Category c ON m.category_id = c.category_id
            WHERE m.item_id = ?
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new MenuItem(
                        rs.getInt("item_id"),
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getString("item_name"),
                        rs.getBigDecimal("current_price"),
                        rs.getBoolean("is_active")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching menu item by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get menu statistics information
     */
    public void printMenuStatistics() {
        String sql = """
            SELECT 
                c.name as category_name,
                COUNT(m.item_id) as item_count,
                AVG(m.current_price) as avg_price,
                MIN(m.current_price) as min_price,
                MAX(m.current_price) as max_price
            FROM Category c
            LEFT JOIN MenuItem m ON c.category_id = m.category_id AND m.is_active = TRUE
            GROUP BY c.category_id, c.name
            ORDER BY c.name
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
             System.out.println("\n=== Menu Statistics ===");
            System.out.printf("%-15s %-8s %-12s %-12s %-12s%n", 
                             "Category", "Count", "Avg Price", "Min Price", "Max Price");
            System.out.println("-".repeat(65));
            
            while (rs.next()) {
                String categoryName = rs.getString("category_name");
                int itemCount = rs.getInt("item_count");
                BigDecimal avgPrice = rs.getBigDecimal("avg_price");
                BigDecimal minPrice = rs.getBigDecimal("min_price");
                BigDecimal maxPrice = rs.getBigDecimal("max_price");
                
                System.out.printf("%-15s %-8d %-12s %-12s %-12s%n",
                    categoryName,
                    itemCount,
                    avgPrice != null ? String.format("$%.2f", avgPrice) : "N/A",
                    minPrice != null ? String.format("$%.2f", minPrice) : "N/A",
                    maxPrice != null ? String.format("$%.2f", maxPrice) : "N/A"
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching menu statistics: " + e.getMessage());
        }
    }
}
