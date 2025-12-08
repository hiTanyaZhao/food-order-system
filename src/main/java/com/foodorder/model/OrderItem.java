package com.foodorder.model;

import java.math.BigDecimal;

/**
 * Order item model class
 */
public class OrderItem {
    private int orderId;
    private int itemId;
    private int quantity;
    
    // Associated object information for display
    private String itemName;
    private BigDecimal itemPrice;
    private String categoryName;
    
    // Constructors
    public OrderItem() {}
    
    public OrderItem(int orderId, int itemId, int quantity) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
    }
    
    public OrderItem(int orderId, int itemId, int quantity, String itemName, BigDecimal itemPrice) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }
    
    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public int getItemId() {
        return itemId;
    }
    
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public String getItemName() {
        return itemName;
    }
    
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
    public BigDecimal getItemPrice() {
        return itemPrice;
    }
    
    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    /**
     * Calculate order item subtotal (quantity  unit price)
     */
    public BigDecimal getSubtotal() {
        if (itemPrice == null) return BigDecimal.ZERO;
        return itemPrice.multiply(BigDecimal.valueOf(quantity));
    }
    
    /**
     * Get formatted unit price
     */
    public String getFormattedItemPrice() {
        return itemPrice != null ? String.format("$%.2f", itemPrice) : "$0.00";
    }
    
    /**
     * Get formatted subtotal
     */
    public String getFormattedSubtotal() {
        return String.format("$%.2f", getSubtotal());
    }
    
    /**
     * Validate order item information completeness
     */
    public boolean isValid() {
        return orderId > 0 && itemId > 0 && quantity > 0;
    }
    
    /**
     * Increase quantity
     */
    public void increaseQuantity(int amount) {
        if (amount > 0) {
            this.quantity += amount;
        }
    }
    
    /**
     * Decrease quantity
     */
    public void decreaseQuantity(int amount) {
        if (amount > 0 && this.quantity > amount) {
            this.quantity -= amount;
        }
    }
    
    @Override
    public String toString() {
        return String.format("OrderItem{orderId=%d, itemId=%d, itemName='%s', quantity=%d, price=%s, subtotal=%s}", 
                           orderId, itemId, itemName, quantity, getFormattedItemPrice(), getFormattedSubtotal());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        OrderItem orderItem = (OrderItem) obj;
        return orderId == orderItem.orderId && itemId == orderItem.itemId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(orderId) * 31 + Integer.hashCode(itemId);
    }
}
