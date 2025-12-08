package com.foodorder.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Order model class
 */
public class Order {
    private int orderId;
    private int customerId;
    private int employeeId;
    private Timestamp orderTime;
    private BigDecimal totalAmount;
    private String currentStatus;
    
    // Associated object information for display
    private String customerName;
    private String employeeName;
    private List<OrderItem> orderItems;
    
    // Order status enumeration
    public enum OrderStatus {
        PENDING("PENDING", "Pending"),
        ACCEPTED("ACCEPTED", "Accepted"),
        PREPARING("PREPARING", "Preparing"),
        COMPLETED("COMPLETED", "Completed"),
        CANCELLED("CANCELLED", "Cancelled");
        
        private final String code;
        private final String description;
        
        OrderStatus(String code, String description) {
            this.code = code;
            this.description = description;
        }
        
        public String getCode() { return code; }
        public String getDescription() { return description; }
        
        public static OrderStatus fromCode(String code) {
            for (OrderStatus status : values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            return PENDING;
        }
    }
    
    // Constructors
    public Order() {}
    
    public Order(int orderId, int customerId, int employeeId, Timestamp orderTime, 
                BigDecimal totalAmount, String currentStatus) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.orderTime = orderTime;
        this.totalAmount = totalAmount;
        this.currentStatus = currentStatus;
    }
    
    public Order(int customerId, int employeeId, String currentStatus) {
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.currentStatus = currentStatus;
        this.totalAmount = BigDecimal.ZERO;
        this.orderTime = Timestamp.valueOf(LocalDateTime.now());
    }
    
    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public int getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    
    public Timestamp getOrderTime() {
        return orderTime;
    }
    
    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getCurrentStatus() {
        return currentStatus;
    }
    
    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getEmployeeName() {
        return employeeName;
    }
    
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    
    /**
     * Get formatted order time
     */
    public String getFormattedOrderTime() {
        if (orderTime == null) return "";
        return orderTime.toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    /**
     * Get formatted total amount
     */
    public String getFormattedTotalAmount() {
        return String.format("$%.2f", totalAmount);
    }
    
    /**
     * Get order status description
     */
    public String getStatusDescription() {
        return OrderStatus.fromCode(currentStatus).getDescription();
    }
    
    /**
     * Check if order can be cancelled
     */
    public boolean isCancellable() {
        return "PENDING".equals(currentStatus) || "ACCEPTED".equals(currentStatus);
    }
    
    /**
     * Check if order is completed
     */
    public boolean isCompleted() {
        return "COMPLETED".equals(currentStatus);
    }
    
    /**
     * Check if order is cancelled
     */
    public boolean isCancelled() {
        return "CANCELLED".equals(currentStatus);
    }
    
    /**
     * Validate order information completeness
     */
    public boolean isValid() {
        return customerId > 0 && employeeId > 0 && 
               currentStatus != null && !currentStatus.trim().isEmpty() &&
               totalAmount != null && totalAmount.compareTo(BigDecimal.ZERO) >= 0;
    }
    
    /**
     * Calculate total item count
     */
    public int getTotalItemCount() {
        if (orderItems == null) return 0;
        return orderItems.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }
    
    @Override
    public String toString() {
        return String.format("Order{id=%d, customer='%s', employee='%s', time='%s', total=%s, status='%s'}", 
                           orderId, customerName, employeeName, getFormattedOrderTime(), 
                           getFormattedTotalAmount(), getStatusDescription());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Order order = (Order) obj;
        return orderId == order.orderId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(orderId);
    }
}
