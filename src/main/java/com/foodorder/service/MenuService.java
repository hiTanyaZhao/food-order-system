package com.foodorder.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.foodorder.dao.MenuDAO;
import com.foodorder.model.Category;
import com.foodorder.model.MenuItem;

/**
 * Business logic layer
 * HandleValidateFormat
 */
public class MenuService {
    
    private MenuDAO menuDAO;
    
    public MenuService() {
        this.menuDAO = new MenuDAO();
    }
    
    /**
     * Get allcategory
     */
    public List<Category> getAllCategories() {
        return menuDAO.getAllCategories();
    }
    
    /**
     * Get allavailable
     */
    public List<MenuItem> getAllAvailableMenuItems() {
        return menuDAO.getAllAvailableMenuItems();
    }
    
    /**
     * Get by
     */
    public List<MenuItem> getMenuItemsByCategory(int categoryId) {
        if (categoryId <= 0) {
            throw new IllegalArgumentException("categoryIDmust be greater than0");
        }
        return menuDAO.getMenuItemsByCategory(categoryId);
    }
    
    /**
     * Search by name
     */
    public List<MenuItem> searchMenuItemsByName(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Searchcannot be empty");
        }
        return menuDAO.searchMenuItemsByName(searchTerm.trim());
    }
    
    /**
     * Search by price range
     */
    public List<MenuItem> searchMenuItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        // Validateprice
        if (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("pricecannot be less than0");
        }
        if (maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("pricecannot be less than0");
        }
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Min and maxprice");
        }
        
        return menuDAO.searchMenuItemsByPriceRange(minPrice, maxPrice);
    }
    
    /**
     * Comprehensive search
     */
    public List<MenuItem> searchMenuItems(String searchTerm, Integer categoryId, 
                                        BigDecimal minPrice, BigDecimal maxPrice) {
        // Validate
        if (categoryId != null && categoryId <= 0) {
            throw new IllegalArgumentException("categoryIDmust be greater than0");
        }
        
        if (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("pricecannot be less than0");
        }
        
        if (maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("pricecannot be less than0");
        }
        
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Min and maxprice");
        }
        
        return menuDAO.searchMenuItems(searchTerm, categoryId, minPrice, maxPrice);
    }
    
    /**
     * Get by ID
     */
    public MenuItem getMenuItemById(int itemId) {
        if (itemId <= 0) {
            throw new IllegalArgumentException("menu item IDmust be greater than0");
        }
        return menuDAO.getMenuItemById(itemId);
    }
    
    /**
     * categoryDisplay
     */
    public Map<String, List<MenuItem>> getMenuItemsGroupedByCategory() {
        List<MenuItem> allItems = menuDAO.getAllAvailableMenuItems();
        return allItems.stream()
                .collect(Collectors.groupingBy(MenuItem::getCategoryName));
    }
    
    /**
     * pricestatistics
     */
    public Map<String, Long> getPriceRangeStatistics() {
        List<MenuItem> allItems = menuDAO.getAllAvailableMenuItems();
        
        return allItems.stream()
                .collect(Collectors.groupingBy(item -> {
                    BigDecimal price = item.getCurrentPrice();
                    if (price.compareTo(new BigDecimal("10")) < 0) {
                        return " ($0-$9.99)";
                    } else if (price.compareTo(new BigDecimal("20")) < 0) {
                        return " ($10-$19.99)";
                    } else if (price.compareTo(new BigDecimal("30")) < 0) {
                        return " ($20-$29.99)";
                    } else {
                        return " ($30+)";
                    }
                }, Collectors.counting()));
    }
    
    /**
     * price
     */
    public List<MenuItem> getRecommendedItems() {
        // price$10-$25
        return menuDAO.searchMenuItemsByPriceRange(
            new BigDecimal("10.00"), 
            new BigDecimal("25.00")
        );
    }
    
    /**
     * Validateexistsavailable
     */
    public boolean isMenuItemAvailable(int itemId) {
        MenuItem item = menuDAO.getMenuItemById(itemId);
        return item != null && item.isAvailable();
    }
    
    /**
     * FormatDisplay
     */
    public String formatMenuItemsForDisplay(List<MenuItem> items) {
        if (items == null || items.isEmpty()) {
            return "found";
        }
        
        StringBuilder sb = new StringBuilder();
        String currentCategory = "";
        
        for (MenuItem item : items) {
            // categoryDisplaycategory
            if (!item.getCategoryName().equals(currentCategory)) {
                currentCategory = item.getCategoryName();
                if (sb.length() > 0) {
                    sb.append("\n");
                }
                sb.append("=== ").append(currentCategory).append(" ===\n");
            }
            
            sb.append(String.format("%-3d %-25s %8s%n", 
                item.getItemId(),
                item.getItemName(),
                item.getFormattedPrice()
            ));
        }
        
        return sb.toString();
    }
    
    /**
     * Menu statisticsinformation
     */
    public void printMenuStatistics() {
        menuDAO.printMenuStatistics();
    }
    
    /**
     * 
     */
    public int getTotalMenuItemCount() {
        return menuDAO.getAllAvailableMenuItems().size();
    }
    
    /**
     * category
     */
    public int getTotalCategoryCount() {
        return menuDAO.getAllCategories().size();
    }
    
    /**
     * Search
     */
    public List<String> getSearchSuggestions(String partialName) {
        if (partialName == null || partialName.trim().length() < 2) {
            return List.of();
        }
        
        List<MenuItem> items = menuDAO.searchMenuItemsByName(partialName);
        return items.stream()
                .map(MenuItem::getItemName)
                .distinct()
                .limit(5)  // quantity
                .collect(Collectors.toList());
    }
    
    
    /**
     * Add new menu item
     */
    public int addMenuItem(int categoryId, String itemName, BigDecimal price, boolean isActive) {
        // Validate category ID
        if (categoryId <= 0) {
            throw new IllegalArgumentException("Category ID must be greater than 0");
        }
        
        // Validate item name
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be empty");
        }
        
        // Validate price
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be non-negative");
        }
        
        // Check if category exists
        List<Category> categories = menuDAO.getAllCategories();
        boolean categoryExists = categories.stream()
                .anyMatch(c -> c.getCategoryId() == categoryId);
        if (!categoryExists) {
            throw new IllegalArgumentException("Category does not exist");
        }
        
        return menuDAO.addMenuItem(categoryId, itemName.trim(), price, isActive);
    }
    
    /**
     * Update menu item
     */
    public boolean updateMenuItem(int itemId, String itemName, BigDecimal price, int categoryId) {
        // Validate item ID
        if (itemId <= 0) {
            throw new IllegalArgumentException("Item ID must be greater than 0");
        }
        
        // Validate item name
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be empty");
        }
        
        // Validate price
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be non-negative");
        }
        
        // Validate category ID
        if (categoryId <= 0) {
            throw new IllegalArgumentException("Category ID must be greater than 0");
        }
        
        // Check if item exists
        MenuItem existingItem = menuDAO.getMenuItemById(itemId);
        if (existingItem == null) {
            throw new IllegalArgumentException("Menu item does not exist");
        }
        
        return menuDAO.updateMenuItem(itemId, itemName.trim(), price, categoryId);
    }
    
    /**
     * Activate or deactivate menu item
     */
    public boolean setMenuItemActive(int itemId, boolean isActive) {
        // Validate item ID
        if (itemId <= 0) {
            throw new IllegalArgumentException("Item ID must be greater than 0");
        }
        
        // Check if item exists
        MenuItem existingItem = menuDAO.getMenuItemById(itemId);
        if (existingItem == null) {
            throw new IllegalArgumentException("Menu item does not exist");
        }
        
        return menuDAO.setMenuItemActive(itemId, isActive);
    }
    
    /**
     * Update menu item price
     */
    public boolean updateMenuItemPrice(int itemId, BigDecimal newPrice) {
        // Validate item ID
        if (itemId <= 0) {
            throw new IllegalArgumentException("Item ID must be greater than 0");
        }
        
        // Validate price
        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be non-negative");
        }
        
        // Check if item exists
        MenuItem existingItem = menuDAO.getMenuItemById(itemId);
        if (existingItem == null) {
            throw new IllegalArgumentException("Menu item does not exist");
        }
        
        return menuDAO.updateMenuItemPrice(itemId, newPrice);
    }
}
