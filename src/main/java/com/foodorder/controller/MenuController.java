package com.foodorder.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.foodorder.model.Category;
import com.foodorder.model.MenuItem;
import com.foodorder.service.MenuService;

/**
 * Menu Controller - Full Version with CRUD operations
 * Handle menu browsing, search and management functionality
 */
public class MenuController {
    
    private MenuService menuService;
    private Scanner scanner;
    
    public MenuController() {
        this.menuService = new MenuService();
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Display menu management main interface
     */
    public void displayMenuInterface() {
        while (true) {
            try {
                displayMenuOptions();
                System.out.print("Please select operation (1-9): ");
                
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1 -> viewAllMenuItems();
                    case 2 -> browseMenuByCategory();
                    case 3 -> searchMenuByName();
                    case 4 -> addMenuItem();
                    case 5 -> updateMenuItem();
                    case 6 -> updateMenuItemPrice();
                    case 7 -> toggleMenuItemStatus();
                    case 8 -> viewMenuItemDetails();
                    case 9 -> {
                        System.out.println("Returning to main menu...");
                        return;
                    }
                    default -> System.out.println("Invalid selection, please try again.");
                }
                
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
                
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error occurred: " + e.getMessage());
            }
        }
    }
    
    /**
     * Display menu management options
     */
    private void displayMenuOptions() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           Menu Management System");
        System.out.println("=".repeat(50));
        System.out.println("--- Browse ---");
        System.out.println("1. Browse all menu items");
        System.out.println("2. Browse by category");
        System.out.println("3. Search by name");
        System.out.println("--- Management ---");
        System.out.println("4. Add new menu item");
        System.out.println("5. Update menu item");
        System.out.println("6. Update price");
        System.out.println("7. Activate/Deactivate item");
        System.out.println("8. View item details");
        System.out.println("---");
        System.out.println("9. Return to main menu");
        System.out.println("=".repeat(50));
    }
    
    /**
     * Browse all menu items
     */
    private void viewAllMenuItems() {
        System.out.println("\nAll Menu Items");
        System.out.println("=".repeat(60));
        
        try {
            Map<String, List<MenuItem>> groupedItems = menuService.getMenuItemsGroupedByCategory();
            
            if (groupedItems.isEmpty()) {
                System.out.println("No menu items available.");
                return;
            }
            
            // Display by category
            groupedItems.forEach((categoryName, items) -> {
                System.out.println("\n=== " + categoryName + " ===");
                System.out.printf("%-4s %-30s %-10s%n", "ID", "Item Name", "Price");
                System.out.println("-".repeat(50));
                
                items.forEach(item -> 
                    System.out.printf("%-4d %-30s %-10s%n", 
                        item.getItemId(),
                        item.getItemName(),
                        item.getFormattedPrice())
                );
            });
            
        } catch (Exception e) {
            System.err.println("Error loading menu items: " + e.getMessage());
        }
    }
    
    /**
     * Browse menu by category
     */
    private void browseMenuByCategory() {
        System.out.println("\nBrowse by Category");
        System.out.println("=".repeat(40));
        
        try {
            // Get all categories
            List<Category> categories = menuService.getAllCategories();
            
            if (categories.isEmpty()) {
                System.out.println("No categories available.");
                return;
            }
            
            // Display categories
            System.out.println("Available categories:");
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i).getName());
            }
            
            System.out.print("Select category (1-" + categories.size() + "): ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            if (choice < 1 || choice > categories.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Category selectedCategory = categories.get(choice - 1);
            List<MenuItem> items = menuService.getMenuItemsByCategory(selectedCategory.getCategoryId());
            
            System.out.println("\n=== " + selectedCategory.getName() + " Items ===");
            
            if (items.isEmpty()) {
                System.out.println("No items available in this category.");
            } else {
                System.out.printf("%-4s %-30s %-10s%n", "ID", "Item Name", "Price");
                System.out.println("-".repeat(50));
                
                items.forEach(item -> 
                    System.out.printf("%-4d %-30s %-10s%n", 
                        item.getItemId(),
                        item.getItemName(),
                        item.getFormattedPrice())
                );
            }
            
        } catch (Exception e) {
            System.err.println("Error browsing by category: " + e.getMessage());
        }
    }
    
    /**
     * Search menu by name
     */
    private void searchMenuByName() {
        System.out.println("\nSearch Menu Items");
        System.out.println("=".repeat(40));
        
        try {
            System.out.print("Enter search term: ");
            String searchTerm = scanner.nextLine().trim();
            
            if (searchTerm.isEmpty()) {
                System.out.println("Search term cannot be empty.");
                return;
            }
            
            List<MenuItem> items = menuService.searchMenuItemsByName(searchTerm);
            
            System.out.println("\nSearch Results for: " + searchTerm);
            System.out.println("-".repeat(50));
            
            if (items.isEmpty()) {
                System.out.println("No items found matching '" + searchTerm + "'.");
            } else {
                System.out.printf("%-4s %-20s %-30s %-10s%n", "ID", "Category", "Item Name", "Price");
                System.out.println("-".repeat(70));
                
                items.forEach(item -> 
                    System.out.printf("%-4d %-20s %-30s %-10s%n", 
                        item.getItemId(),
                        item.getCategoryName(),
                        item.getItemName(),
                        item.getFormattedPrice())
                );
            }
            
        } catch (Exception e) {
            System.err.println("Error searching menu: " + e.getMessage());
        }
    }
    
    /**
     * Add new menu item
     */
    private void addMenuItem() {
        System.out.println("\nAdd New Menu Item");
        System.out.println("=".repeat(40));
        
        try {
            // Display categories for selection
            List<Category> categories = menuService.getAllCategories();
            if (categories.isEmpty()) {
                System.out.println("No categories available. Please add categories first.");
                return;
            }
            
            System.out.println("Available categories:");
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i).getName());
            }
            
            System.out.print("Select category (1-" + categories.size() + "): ");
            int categoryChoice = Integer.parseInt(scanner.nextLine().trim());
            
            if (categoryChoice < 1 || categoryChoice > categories.size()) {
                System.out.println("Invalid category selection.");
                return;
            }
            
            int categoryId = categories.get(categoryChoice - 1).getCategoryId();
            
            System.out.print("Enter item name: ");
            String itemName = scanner.nextLine().trim();
            
            if (itemName.isEmpty()) {
                System.out.println("Item name cannot be empty.");
                return;
            }
            
            System.out.print("Enter price: $");
            BigDecimal price = new BigDecimal(scanner.nextLine().trim());
            
            System.out.print("Is active? (Y/n): ");
            String activeInput = scanner.nextLine().trim().toLowerCase();
            boolean isActive = activeInput.isEmpty() || "y".equals(activeInput) || "yes".equals(activeInput);
            
            int newItemId = menuService.addMenuItem(categoryId, itemName, price, isActive);
            
            if (newItemId > 0) {
                System.out.println("✓ Menu item added successfully! Item ID: " + newItemId);
            } else {
                System.out.println("✗ Failed to add menu item.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numbers.");
        } catch (Exception e) {
            System.out.println("Error adding menu item: " + e.getMessage());
        }
    }
    
    /**
     * Update menu item
     */
    private void updateMenuItem() {
        System.out.println("\nUpdate Menu Item");
        System.out.println("=".repeat(40));
        
        try {
            System.out.print("Enter item ID to update: ");
            int itemId = Integer.parseInt(scanner.nextLine().trim());
            
            // Get current item info
            MenuItem existingItem = menuService.getMenuItemById(itemId);
            if (existingItem == null) {
                System.out.println("Menu item not found.");
                return;
            }
            
            System.out.println("\nCurrent item information:");
            System.out.println("Name: " + existingItem.getItemName());
            System.out.println("Category: " + existingItem.getCategoryName());
            System.out.println("Price: " + existingItem.getFormattedPrice());
            System.out.println("Status: " + (existingItem.isActive() ? "Active" : "Inactive"));
            
            System.out.println("\nEnter new information (press Enter to keep current value):");
            
            // Get new name
            System.out.print("New name [" + existingItem.getItemName() + "]: ");
            String newName = scanner.nextLine().trim();
            if (newName.isEmpty()) newName = existingItem.getItemName();
            
            // Get new price
            System.out.print("New price [" + existingItem.getCurrentPrice() + "]: $");
            String priceInput = scanner.nextLine().trim();
            BigDecimal newPrice = priceInput.isEmpty() ? existingItem.getCurrentPrice() : new BigDecimal(priceInput);
            
            // Get new category
            List<Category> categories = menuService.getAllCategories();
            System.out.println("\nAvailable categories:");
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i).getName());
            }
            System.out.print("Select new category (Enter to keep current): ");
            String categoryInput = scanner.nextLine().trim();
            int newCategoryId = existingItem.getCategoryId();
            if (!categoryInput.isEmpty()) {
                int categoryChoice = Integer.parseInt(categoryInput);
                if (categoryChoice >= 1 && categoryChoice <= categories.size()) {
                    newCategoryId = categories.get(categoryChoice - 1).getCategoryId();
                }
            }
            
            boolean success = menuService.updateMenuItem(itemId, newName, newPrice, newCategoryId);
            
            if (success) {
                System.out.println("✓ Menu item updated successfully!");
            } else {
                System.out.println("✗ Failed to update menu item.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numbers.");
        } catch (Exception e) {
            System.out.println("Error updating menu item: " + e.getMessage());
        }
    }
    
    /**
     * Update menu item price only
     */
    private void updateMenuItemPrice() {
        System.out.println("\nUpdate Menu Item Price");
        System.out.println("=".repeat(40));
        
        try {
            System.out.print("Enter item ID: ");
            int itemId = Integer.parseInt(scanner.nextLine().trim());
            
            // Get current item info
            MenuItem existingItem = menuService.getMenuItemById(itemId);
            if (existingItem == null) {
                System.out.println("Menu item not found.");
                return;
            }
            
            System.out.println("Item: " + existingItem.getItemName());
            System.out.println("Current price: " + existingItem.getFormattedPrice());
            
            System.out.print("Enter new price: $");
            BigDecimal newPrice = new BigDecimal(scanner.nextLine().trim());
            
            boolean success = menuService.updateMenuItemPrice(itemId, newPrice);
            
            if (success) {
                System.out.println("✓ Price updated successfully!");
                System.out.println("New price: $" + newPrice);
            } else {
                System.out.println("✗ Failed to update price.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numbers.");
        } catch (Exception e) {
            System.out.println("Error updating price: " + e.getMessage());
        }
    }
    
    /**
     * Toggle menu item active status
     */
    private void toggleMenuItemStatus() {
        System.out.println("\nActivate/Deactivate Menu Item");
        System.out.println("=".repeat(40));
        
        try {
            System.out.print("Enter item ID: ");
            int itemId = Integer.parseInt(scanner.nextLine().trim());
            
            // Get current item info
            MenuItem existingItem = menuService.getMenuItemById(itemId);
            if (existingItem == null) {
                System.out.println("Menu item not found.");
                return;
            }
            
            System.out.println("Item: " + existingItem.getItemName());
            System.out.println("Current status: " + (existingItem.isActive() ? "Active" : "Inactive"));
            
            System.out.print("Set as active? (Y/n): ");
            String activeInput = scanner.nextLine().trim().toLowerCase();
            boolean newStatus = activeInput.isEmpty() || "y".equals(activeInput) || "yes".equals(activeInput);
            
            boolean success = menuService.setMenuItemActive(itemId, newStatus);
            
            if (success) {
                System.out.println("✓ Status updated successfully!");
                System.out.println("New status: " + (newStatus ? "Active" : "Inactive"));
            } else {
                System.out.println("✗ Failed to update status.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid item ID.");
        } catch (Exception e) {
            System.out.println("Error updating status: " + e.getMessage());
        }
    }
    
    /**
     * View menu item details
     */
    private void viewMenuItemDetails() {
        System.out.println("\nMenu Item Details");
        System.out.println("=".repeat(40));
        
        try {
            System.out.print("Enter item ID: ");
            int itemId = Integer.parseInt(scanner.nextLine().trim());
            
            MenuItem item = menuService.getMenuItemById(itemId);
            if (item == null) {
                System.out.println("Menu item not found.");
                return;
            }
            
            System.out.println("\n=== Menu Item Details ===");
            System.out.println("ID: " + item.getItemId());
            System.out.println("Name: " + item.getItemName());
            System.out.println("Category: " + item.getCategoryName());
            System.out.println("Price: " + item.getFormattedPrice());
            System.out.println("Status: " + (item.isActive() ? "Active" : "Inactive"));
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid item ID.");
        } catch (Exception e) {
            System.out.println("Error viewing item details: " + e.getMessage());
        }
    }
}