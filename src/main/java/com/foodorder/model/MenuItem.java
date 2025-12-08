package com.foodorder.model;

import java.math.BigDecimal;

/**
 * Menu item model class
 */
public class MenuItem {
    private int itemId;
    private int categoryId;
    private String categoryName;  // For displaying category name
    private String itemName;
    private BigDecimal currentPrice;
    private boolean isActive;
    
    // Constructors
    public MenuItem() {}
    
    public MenuItem(int itemId, int categoryId, String itemName, BigDecimal currentPrice, boolean isActive) {
        this.itemId = itemId;
        this.categoryId = categoryId;
        this.itemName = itemName;
        this.currentPrice = currentPrice;
        this.isActive = isActive;
    }
    
    public MenuItem(int itemId, int categoryId, String categoryName, String itemName, 
                   BigDecimal currentPrice, boolean isActive) {
        this.itemId = itemId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.itemName = itemName;
        this.currentPrice = currentPrice;
        this.isActive = isActive;
    }
    
    // Getters and Setters
    public int getItemId() {
        return itemId;
    }
    
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getItemName() {
        return itemName;
    }
    
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }
    
    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    /**
     * Format price for display
     */
    public String getFormattedPrice() {
        return String.format("$%.2f", currentPrice);
    }
    
    /**
     * Check if menu item is available
     */
    public boolean isAvailable() {
        return isActive;
    }
    
    @Override
    public String toString() {
        return String.format("MenuItem{id=%d, name='%s', category='%s', price=%s, active=%s}", 
                           itemId, itemName, categoryName, getFormattedPrice(), isActive);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MenuItem menuItem = (MenuItem) obj;
        return itemId == menuItem.itemId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(itemId);
    }
}
