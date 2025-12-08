package com.foodorder.app;

import java.math.BigDecimal;

import com.foodorder.config.DatabaseConnection;
import com.foodorder.dao.CustomerDAO;
import com.foodorder.dao.MenuDAO;
import com.foodorder.model.MenuItem;
import com.foodorder.service.CustomerService;
import com.foodorder.service.MenuService;

/**
 * Test class for newly added DAO and Service methods
 */
public class TestNewMethods {
    
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("         Testing DAO and Service Layer Methods");
        System.out.println("=".repeat(70));
        
        // Test database connection
        try {
            DatabaseConnection.getInstance().getConnection();
            System.out.println("✓ Database connection successful!\n");
        } catch (Exception e) {
            System.err.println("✗ Database connection failed: " + e.getMessage());
            return;
        }
        
        // Initialize DAOs and Services
        CustomerDAO customerDAO = new CustomerDAO();
        MenuDAO menuDAO = new MenuDAO();
        CustomerService customerService = new CustomerService();
        MenuService menuService = new MenuService();
        
        int testItemId = -1;
        
        // ==================== PART 1: DAO LAYER TESTS ====================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                    PART 1: DAO LAYER TESTS");
        System.out.println("=".repeat(70));
        
        // TEST 1: CustomerDAO.getCustomerCumulativeSpend
        System.out.println("\n--- TEST 1: CustomerDAO.getCustomerCumulativeSpend() ---");
        try {
            BigDecimal spend1 = customerDAO.getCustomerCumulativeSpend(1);
            System.out.println("  Customer 1 cumulative spend: $" + spend1);
            
            BigDecimal spend3 = customerDAO.getCustomerCumulativeSpend(3);
            System.out.println("  Customer 3 cumulative spend: $" + spend3);
            
            BigDecimal spend999 = customerDAO.getCustomerCumulativeSpend(999);
            System.out.println("  Customer 999 (non-existent) cumulative spend: $" + spend999);
            
            if (spend999.compareTo(BigDecimal.ZERO) == 0) {
                System.out.println("✓ TEST 1 PASSED");
                testsPassed++;
            } else {
                System.out.println("✗ TEST 1 FAILED - non-existent customer should return $0");
                testsFailed++;
            }
        } catch (Exception e) {
            System.err.println("✗ TEST 1 FAILED: " + e.getMessage());
            testsFailed++;
        }
        
        // TEST 2: MenuDAO.addMenuItem
        System.out.println("\n--- TEST 2: MenuDAO.addMenuItem() ---");
        try {
            testItemId = menuDAO.addMenuItem(5, "DAO Test Item", new BigDecimal("9.99"), true);
            
            if (testItemId > 0) {
                MenuItem item = menuDAO.getMenuItemById(testItemId);
                if (item != null && "DAO Test Item".equals(item.getItemName())) {
                    System.out.println("  Created item ID: " + testItemId);
                    System.out.println("  Name: " + item.getItemName() + ", Price: $" + item.getCurrentPrice());
                    System.out.println("✓ TEST 2 PASSED");
                    testsPassed++;
                } else {
                    System.out.println("✗ TEST 2 FAILED - item not found or name mismatch");
                    testsFailed++;
                }
            } else {
                System.out.println("✗ TEST 2 FAILED - returned invalid ID");
                testsFailed++;
            }
        } catch (Exception e) {
            System.err.println("✗ TEST 2 FAILED: " + e.getMessage());
            testsFailed++;
        }
        
        // TEST 3: MenuDAO.updateMenuItem
        System.out.println("\n--- TEST 3: MenuDAO.updateMenuItem() ---");
        try {
            if (testItemId > 0) {
                boolean updated = menuDAO.updateMenuItem(testItemId, "DAO Updated Item", new BigDecimal("12.99"), 5);
                MenuItem item = menuDAO.getMenuItemById(testItemId);
                
                if (updated && item != null && "DAO Updated Item".equals(item.getItemName())) {
                    System.out.println("  Updated name: " + item.getItemName());
                    System.out.println("  Updated price: $" + item.getCurrentPrice());
                    System.out.println("✓ TEST 3 PASSED");
                    testsPassed++;
                } else {
                    System.out.println("✗ TEST 3 FAILED - update verification failed");
                    testsFailed++;
                }
            }
        } catch (Exception e) {
            System.err.println("✗ TEST 3 FAILED: " + e.getMessage());
            testsFailed++;
        }
        
        // TEST 4: MenuDAO.setMenuItemActive
        System.out.println("\n--- TEST 4: MenuDAO.setMenuItemActive() ---");
        try {
            if (testItemId > 0) {
                // Deactivate
                menuDAO.setMenuItemActive(testItemId, false);
                MenuItem item = menuDAO.getMenuItemById(testItemId);
                boolean deactivateSuccess = (item != null && !item.isActive());
                System.out.println("  After deactivate: isActive = " + item.isActive());
                
                // Reactivate
                menuDAO.setMenuItemActive(testItemId, true);
                item = menuDAO.getMenuItemById(testItemId);
                boolean reactivateSuccess = (item != null && item.isActive());
                System.out.println("  After reactivate: isActive = " + item.isActive());
                
                if (deactivateSuccess && reactivateSuccess) {
                    System.out.println("✓ TEST 4 PASSED");
                    testsPassed++;
                } else {
                    System.out.println("✗ TEST 4 FAILED - status toggle failed");
                    testsFailed++;
                }
            }
        } catch (Exception e) {
            System.err.println("✗ TEST 4 FAILED: " + e.getMessage());
            testsFailed++;
        }
        
        // TEST 5: MenuDAO.updateMenuItemPrice
        System.out.println("\n--- TEST 5: MenuDAO.updateMenuItemPrice() ---");
        try {
            if (testItemId > 0) {
                BigDecimal newPrice = new BigDecimal("15.99");
                menuDAO.updateMenuItemPrice(testItemId, newPrice);
                MenuItem item = menuDAO.getMenuItemById(testItemId);
                
                if (item != null && item.getCurrentPrice().compareTo(newPrice) == 0) {
                    System.out.println("  New price: $" + item.getCurrentPrice());
                    System.out.println("✓ TEST 5 PASSED");
                    testsPassed++;
                } else {
                    System.out.println("✗ TEST 5 FAILED - price update failed");
                    testsFailed++;
                }
            }
        } catch (Exception e) {
            System.err.println("✗ TEST 5 FAILED: " + e.getMessage());
            testsFailed++;
        }
        
        // Cleanup DAO test item
        cleanupTestItem(testItemId);
        testItemId = -1;
        
        // ==================== PART 2: SERVICE LAYER TESTS ====================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                  PART 2: SERVICE LAYER TESTS");
        System.out.println("=".repeat(70));
        
        // TEST 6: CustomerService.getCustomerCumulativeSpend
        System.out.println("\n--- TEST 6: CustomerService.getCustomerCumulativeSpend() ---");
        try {
            BigDecimal spend = customerService.getCustomerCumulativeSpend(1);
            System.out.println("  Customer 1 cumulative spend via Service: $" + spend);
            
            // Test validation - should throw exception for invalid ID
            try {
                customerService.getCustomerCumulativeSpend(0);
                System.out.println("✗ TEST 6 FAILED - should throw exception for ID=0");
                testsFailed++;
            } catch (IllegalArgumentException e) {
                System.out.println("  Correctly rejected invalid ID (0): " + e.getMessage());
                System.out.println("✓ TEST 6 PASSED");
                testsPassed++;
            }
        } catch (Exception e) {
            System.err.println("✗ TEST 6 FAILED: " + e.getMessage());
            testsFailed++;
        }
        
        // TEST 7: MenuService.addMenuItem with validation
        System.out.println("\n--- TEST 7: MenuService.addMenuItem() with validation ---");
        try {
            // Test successful creation
            testItemId = menuService.addMenuItem(5, "Service Test Item", new BigDecimal("19.99"), true);
            
            if (testItemId > 0) {
                MenuItem item = menuService.getMenuItemById(testItemId);
                System.out.println("  Created via Service - ID: " + testItemId + ", Name: " + item.getItemName());
            }
            
            // Test validation - empty name
            try {
                menuService.addMenuItem(5, "", new BigDecimal("10.00"), true);
                System.out.println("✗ TEST 7 FAILED - should reject empty name");
                testsFailed++;
            } catch (IllegalArgumentException e) {
                System.out.println("  Correctly rejected empty name: " + e.getMessage());
            }
            
            // Test validation - negative price
            try {
                menuService.addMenuItem(5, "Bad Item", new BigDecimal("-5.00"), true);
                System.out.println("✗ TEST 7 FAILED - should reject negative price");
                testsFailed++;
            } catch (IllegalArgumentException e) {
                System.out.println("  Correctly rejected negative price: " + e.getMessage());
            }
            
            // Test validation - invalid category
            try {
                menuService.addMenuItem(999, "Bad Category Item", new BigDecimal("10.00"), true);
                System.out.println("✗ TEST 7 FAILED - should reject invalid category");
                testsFailed++;
            } catch (IllegalArgumentException e) {
                System.out.println("  Correctly rejected invalid category: " + e.getMessage());
            }
            
            System.out.println("✓ TEST 7 PASSED");
            testsPassed++;
        } catch (Exception e) {
            System.err.println("✗ TEST 7 FAILED: " + e.getMessage());
            testsFailed++;
        }
        
        // TEST 8: MenuService.updateMenuItem with validation
        System.out.println("\n--- TEST 8: MenuService.updateMenuItem() with validation ---");
        try {
            if (testItemId > 0) {
                // Successful update
                boolean updated = menuService.updateMenuItem(testItemId, "Service Updated Item", new BigDecimal("24.99"), 5);
                MenuItem item = menuService.getMenuItemById(testItemId);
                
                if (updated && "Service Updated Item".equals(item.getItemName())) {
                    System.out.println("  Updated via Service - Name: " + item.getItemName() + ", Price: $" + item.getCurrentPrice());
                }
                
                // Test validation - non-existent item
                try {
                    menuService.updateMenuItem(99999, "Ghost Item", new BigDecimal("10.00"), 5);
                    System.out.println("✗ TEST 8 FAILED - should reject non-existent item");
                    testsFailed++;
                } catch (IllegalArgumentException e) {
                    System.out.println("  Correctly rejected non-existent item: " + e.getMessage());
                }
                
                System.out.println("✓ TEST 8 PASSED");
                testsPassed++;
            }
        } catch (Exception e) {
            System.err.println("✗ TEST 8 FAILED: " + e.getMessage());
            testsFailed++;
        }
        
        // TEST 9: MenuService.setMenuItemActive with validation
        System.out.println("\n--- TEST 9: MenuService.setMenuItemActive() with validation ---");
        try {
            if (testItemId > 0) {
                // Deactivate
                menuService.setMenuItemActive(testItemId, false);
                MenuItem item = menuService.getMenuItemById(testItemId);
                System.out.println("  After deactivate via Service: isActive = " + item.isActive());
                
                // Reactivate
                menuService.setMenuItemActive(testItemId, true);
                item = menuService.getMenuItemById(testItemId);
                System.out.println("  After reactivate via Service: isActive = " + item.isActive());
                
                // Test validation - non-existent item
                try {
                    menuService.setMenuItemActive(99999, false);
                    System.out.println("✗ TEST 9 FAILED - should reject non-existent item");
                    testsFailed++;
                } catch (IllegalArgumentException e) {
                    System.out.println("  Correctly rejected non-existent item: " + e.getMessage());
                    System.out.println("✓ TEST 9 PASSED");
                    testsPassed++;
                }
            }
        } catch (Exception e) {
            System.err.println("✗ TEST 9 FAILED: " + e.getMessage());
            testsFailed++;
        }
        
        // TEST 10: MenuService.updateMenuItemPrice with validation
        System.out.println("\n--- TEST 10: MenuService.updateMenuItemPrice() with validation ---");
        try {
            if (testItemId > 0) {
                // Successful price update
                menuService.updateMenuItemPrice(testItemId, new BigDecimal("29.99"));
                MenuItem item = menuService.getMenuItemById(testItemId);
                System.out.println("  New price via Service: $" + item.getCurrentPrice());
                
                // Test validation - negative price
                try {
                    menuService.updateMenuItemPrice(testItemId, new BigDecimal("-10.00"));
                    System.out.println("✗ TEST 10 FAILED - should reject negative price");
                    testsFailed++;
                } catch (IllegalArgumentException e) {
                    System.out.println("  Correctly rejected negative price: " + e.getMessage());
                }
                
                // Test validation - null price
                try {
                    menuService.updateMenuItemPrice(testItemId, null);
                    System.out.println("✗ TEST 10 FAILED - should reject null price");
                    testsFailed++;
                } catch (IllegalArgumentException e) {
                    System.out.println("  Correctly rejected null price: " + e.getMessage());
                }
                
                System.out.println("✓ TEST 10 PASSED");
                testsPassed++;
            }
        } catch (Exception e) {
            System.err.println("✗ TEST 10 FAILED: " + e.getMessage());
            testsFailed++;
        }
        
        // TEST 11: CustomerService.getCustomerDetailInfo (should include cumulative spend)
        System.out.println("\n--- TEST 11: CustomerService.getCustomerDetailInfo() ---");
        try {
            String detailInfo = customerService.getCustomerDetailInfo(1);
            System.out.println("  Customer 1 Detail Info:");
            for (String line : detailInfo.split("\n")) {
                System.out.println("    " + line);
            }
            
            if (detailInfo.contains("Cumulative Spend")) {
                System.out.println("✓ TEST 11 PASSED - Detail info includes cumulative spend");
                testsPassed++;
            } else {
                System.out.println("✗ TEST 11 FAILED - Missing cumulative spend in detail info");
                testsFailed++;
            }
        } catch (Exception e) {
            System.err.println("✗ TEST 11 FAILED: " + e.getMessage());
            testsFailed++;
        }
        
        // Cleanup Service test item
        cleanupTestItem(testItemId);
        
        // ==================== SUMMARY ====================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                       TEST SUMMARY");
        System.out.println("=".repeat(70));
        System.out.println("\n  Total Tests: " + (testsPassed + testsFailed));
        System.out.println("  ✓ Passed: " + testsPassed);
        System.out.println("  ✗ Failed: " + testsFailed);
        System.out.println("\n  Methods Tested:");
        System.out.println("  --- DAO Layer ---");
        System.out.println("    1. CustomerDAO.getCustomerCumulativeSpend()");
        System.out.println("    2. MenuDAO.addMenuItem()");
        System.out.println("    3. MenuDAO.updateMenuItem()");
        System.out.println("    4. MenuDAO.setMenuItemActive()");
        System.out.println("    5. MenuDAO.updateMenuItemPrice()");
        System.out.println("  --- Service Layer ---");
        System.out.println("    6. CustomerService.getCustomerCumulativeSpend()");
        System.out.println("    7. MenuService.addMenuItem()");
        System.out.println("    8. MenuService.updateMenuItem()");
        System.out.println("    9. MenuService.setMenuItemActive()");
        System.out.println("   10. MenuService.updateMenuItemPrice()");
        System.out.println("   11. CustomerService.getCustomerDetailInfo()");
        
        if (testsFailed == 0) {
            System.out.println("\n  🎉 ALL TESTS PASSED! 🎉");
        } else {
            System.out.println("\n  ⚠️  Some tests failed. Please review.");
        }
        
        System.out.println("=".repeat(70));
        
        // Close connection
        DatabaseConnection.getInstance().closeConnection();
    }
    
    /**
     * Helper method to cleanup test items
     */
    private static void cleanupTestItem(int itemId) {
        if (itemId > 0) {
            try {
                java.sql.Connection conn = DatabaseConnection.getInstance().getConnection();
                String sql = "DELETE FROM MenuItem WHERE item_id = ?";
                try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, itemId);
                    int deleted = stmt.executeUpdate();
                    if (deleted > 0) {
                        System.out.println("\n  [Cleanup] Deleted test item ID: " + itemId);
                    }
                }
            } catch (Exception e) {
                System.err.println("  [Cleanup] Warning: " + e.getMessage());
            }
        }
    }
}
