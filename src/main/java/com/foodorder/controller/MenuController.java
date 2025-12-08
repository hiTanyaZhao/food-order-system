package com.foodorder.controller;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.foodorder.model.Category;
import com.foodorder.model.MenuItem;
import com.foodorder.service.MenuService;

/**
 * Menu Controller - Simplified Version
 * Handle menu browsing and search functionality
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
                System.out.print("Please select operation (1-4): ");
                
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                
                switch (choice) {
                    case 1 -> viewAllMenuItems();
                    case 2 -> browseMenuByCategory();
                    case 3 -> searchMenuByName();
                    case 4 -> {
                        System.out.println("Returning to main menu...");
                        return;
                    }
                    default -> System.out.println("Invalid selection, please try again.");
                }
                
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
                
            } catch (Exception e) {
                System.out.println("Error occurred: " + e.getMessage());
                scanner.nextLine(); // clear invalid input
            }
        }
    }
    
    /**
     * Display menu management options
     */
    private void displayMenuOptions() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("             Menu Browse System");
        System.out.println("=".repeat(50));
        System.out.println("1. Browse all menu items");
        System.out.println("2. Browse by category");
        System.out.println("3. Search by name");
        System.out.println("4. Return to main menu");
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
    
}