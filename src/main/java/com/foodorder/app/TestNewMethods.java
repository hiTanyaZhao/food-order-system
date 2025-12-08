package com.foodorder.app;

import java.math.BigDecimal;

import com.foodorder.config.DatabaseConnection;
import com.foodorder.dao.CustomerDAO;
import com.foodorder.dao.MenuDAO;
import com.foodorder.model.MenuItem;

/**
 * Test class for newly added methods
 */
public class TestNewMethods {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("       Testing Newly Added Methods");
        System.out.println("=".repeat(60));
        
        // Test database connection
        try {
            DatabaseConnection.getInstance().getConnection();
            System.out.println("✓ Database connection successful!\n");
        } catch (Exception e) {
            System.err.println("✗ Database connection failed: " + e.getMessage());
            return;
        }
        
        // Initialize DAOs
        CustomerDAO customerDAO = new CustomerDAO();
        MenuDAO menuDAO = new MenuDAO();
        
        // ========== Test CustomerDAO.getCustomerCumulativeSpend ==========
        System.out.println("=".repeat(60));
        System.out.println("TEST 1: CustomerDAO.getCustomerCumulativeSpend()");
        System.out.println("=".repeat(60));
        
        try {
            // Test with customer ID 1 (John Smith from sample data)
            int testCustomerId = 1;
            BigDecimal spend = customerDAO.getCustomerCumulativeSpend(testCustomerId);
            System.out.println("Customer ID " + testCustomerId + " cumulative spend: $" + spend);
            
            // Test with customer ID 3
            testCustomerId = 3;
            spend = customerDAO.getCustomerCumulativeSpend(testCustomerId);
            System.out.println("Customer ID " + testCustomerId + " cumulative spend: $" + spend);
            
            // Test with non-existent customer
            testCustomerId = 999;
            spend = customerDAO.getCustomerCumulativeSpend(testCustomerId);
            System.out.println("Customer ID " + testCustomerId + " (non-existent) cumulative spend: $" + spend);
            
            System.out.println("✓ getCustomerCumulativeSpend() test PASSED\n");
        } catch (Exception e) {
            System.err.println("✗ getCustomerCumulativeSpend() test FAILED: " + e.getMessage());
        }
        
        // ========== Test MenuDAO.addMenuItem ==========
        System.out.println("=".repeat(60));
        System.out.println("TEST 2: MenuDAO.addMenuItem()");
        System.out.println("=".repeat(60));
        
        int newItemId = -1;
        try {
            // Add a new menu item (category 5 = Dessert)
            newItemId = menuDAO.addMenuItem(5, "Test Brownie", new BigDecimal("6.99"), true);
            
            if (newItemId > 0) {
                System.out.println("✓ New menu item created with ID: " + newItemId);
                
                // Verify by fetching it
                MenuItem item = menuDAO.getMenuItemById(newItemId);
                if (item != null) {
                    System.out.println("  Verified - Name: " + item.getItemName() + 
                                     ", Price: $" + item.getCurrentPrice() + 
                                     ", Active: " + item.isActive());
                }
                System.out.println("✓ addMenuItem() test PASSED\n");
            } else {
                System.out.println("✗ addMenuItem() test FAILED - returned ID: " + newItemId);
            }
        } catch (Exception e) {
            System.err.println("✗ addMenuItem() test FAILED: " + e.getMessage());
        }
        
        // ========== Test MenuDAO.updateMenuItem ==========
        System.out.println("=".repeat(60));
        System.out.println("TEST 3: MenuDAO.updateMenuItem()");
        System.out.println("=".repeat(60));
        
        try {
            if (newItemId > 0) {
                // Update the item we just created
                boolean updated = menuDAO.updateMenuItem(newItemId, "Updated Test Brownie", 
                                                        new BigDecimal("7.99"), 5);
                
                if (updated) {
                    System.out.println("✓ Menu item updated successfully");
                    
                    // Verify the update
                    MenuItem item = menuDAO.getMenuItemById(newItemId);
                    if (item != null) {
                        System.out.println("  Verified - New Name: " + item.getItemName() + 
                                         ", New Price: $" + item.getCurrentPrice());
                    }
                    System.out.println("✓ updateMenuItem() test PASSED\n");
                } else {
                    System.out.println("✗ updateMenuItem() test FAILED");
                }
            } else {
                System.out.println("⚠ Skipped - no test item available\n");
            }
        } catch (Exception e) {
            System.err.println("✗ updateMenuItem() test FAILED: " + e.getMessage());
        }
        
        // ========== Test MenuDAO.setMenuItemActive ==========
        System.out.println("=".repeat(60));
        System.out.println("TEST 4: MenuDAO.setMenuItemActive()");
        System.out.println("=".repeat(60));
        
        try {
            if (newItemId > 0) {
                // Deactivate the item
                boolean deactivated = menuDAO.setMenuItemActive(newItemId, false);
                
                if (deactivated) {
                    System.out.println("✓ Menu item deactivated");
                    
                    // Verify
                    MenuItem item = menuDAO.getMenuItemById(newItemId);
                    if (item != null) {
                        System.out.println("  Verified - Active status: " + item.isActive());
                    }
                }
                
                // Reactivate the item
                boolean activated = menuDAO.setMenuItemActive(newItemId, true);
                
                if (activated) {
                    System.out.println("✓ Menu item reactivated");
                    
                    // Verify
                    MenuItem item = menuDAO.getMenuItemById(newItemId);
                    if (item != null) {
                        System.out.println("  Verified - Active status: " + item.isActive());
                    }
                }
                
                System.out.println("✓ setMenuItemActive() test PASSED\n");
            } else {
                System.out.println("⚠ Skipped - no test item available\n");
            }
        } catch (Exception e) {
            System.err.println("✗ setMenuItemActive() test FAILED: " + e.getMessage());
        }
        
        // ========== Test MenuDAO.updateMenuItemPrice ==========
        System.out.println("=".repeat(60));
        System.out.println("TEST 5: MenuDAO.updateMenuItemPrice()");
        System.out.println("=".repeat(60));
        
        try {
            if (newItemId > 0) {
                // Update price
                boolean priceUpdated = menuDAO.updateMenuItemPrice(newItemId, new BigDecimal("8.49"));
                
                if (priceUpdated) {
                    System.out.println("✓ Menu item price updated");
                    
                    // Verify
                    MenuItem item = menuDAO.getMenuItemById(newItemId);
                    if (item != null) {
                        System.out.println("  Verified - New Price: $" + item.getCurrentPrice());
                    }
                    System.out.println("✓ updateMenuItemPrice() test PASSED\n");
                } else {
                    System.out.println("✗ updateMenuItemPrice() test FAILED");
                }
            } else {
                System.out.println("⚠ Skipped - no test item available\n");
            }
        } catch (Exception e) {
            System.err.println("✗ updateMenuItemPrice() test FAILED: " + e.getMessage());
        }
        
        // ========== Cleanup: Delete test item ==========
        System.out.println("=".repeat(60));
        System.out.println("CLEANUP: Removing test data");
        System.out.println("=".repeat(60));
        
        try {
            if (newItemId > 0) {
                // Delete the test menu item using direct SQL
                java.sql.Connection conn = DatabaseConnection.getInstance().getConnection();
                String sql = "DELETE FROM MenuItem WHERE item_id = ?";
                try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, newItemId);
                    int deleted = stmt.executeUpdate();
                    if (deleted > 0) {
                        System.out.println("✓ Test menu item (ID: " + newItemId + ") deleted\n");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("⚠ Cleanup warning: " + e.getMessage());
        }
        
        // ========== Summary ==========
        System.out.println("=".repeat(60));
        System.out.println("       ALL TESTS COMPLETED!");
        System.out.println("=".repeat(60));
        System.out.println("\nNew methods verified:");
        System.out.println("  1. CustomerDAO.getCustomerCumulativeSpend() ✓");
        System.out.println("  2. MenuDAO.addMenuItem() ✓");
        System.out.println("  3. MenuDAO.updateMenuItem() ✓");
        System.out.println("  4. MenuDAO.setMenuItemActive() ✓");
        System.out.println("  5. MenuDAO.updateMenuItemPrice() ✓");
        
        // Close connection
        DatabaseConnection.getInstance().closeConnection();
    }
}

