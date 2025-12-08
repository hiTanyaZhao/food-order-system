package com.foodorder.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.foodorder.dao.CustomerDAO;
import com.foodorder.dao.EmployeeDAO;
import com.foodorder.dao.OrderDAO;
import com.foodorder.dao.OrderItemDAO;
import com.foodorder.model.Customer;
import com.foodorder.model.Employee;
import com.foodorder.model.Order;
import com.foodorder.model.OrderItem;

/**
 * Business logic layer
 * HandleManagementValidate
 */
public class OrderService {
    
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private EmployeeDAO employeeDAO;
    private CustomerDAO customerDAO;
    
    // order status
    private static final List<String> VALID_STATUSES = Arrays.asList(
        "PENDING", "ACCEPTED", "PREPARING", "COMPLETED", "CANCELLED"
    );
    
    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.orderItemDAO = new OrderItemDAO();
        this.employeeDAO = new EmployeeDAO();
        this.customerDAO = new CustomerDAO();
    }
    
    /**
     * Create new
     */
    public int createOrder(int customerId, int employeeId) {
        // Validateexists
        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Order does not exist");
        }
        
        // Validateexistsavailable
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Order does not exist");
        }
        
        if (!employee.isAvailable()) {
            throw new IllegalArgumentException("available");
        }
        
        // Business logic
        Order order = new Order(customerId, employeeId, "PENDING");
        order.setOrderTime(Timestamp.valueOf(LocalDateTime.now()));
        order.setTotalAmount(BigDecimal.ZERO);
        
        return orderDAO.createOrder(order);
    }
    
    /**
     * auto assign
     */
    public int createOrderWithAutoAssignment(int customerId) {
        // Validateexists
        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Order does not exist");
        }
        
        // auto assignavailable
        Employee availableEmployee = employeeDAO.getRandomAvailableEmployee();
        if (availableEmployee == null) {
            throw new IllegalArgumentException("available");
        }
        
        return createOrder(customerId, availableEmployee.getEmployeeId());
    }
    
    /**
     * Get by ID
     */
    public Order getOrderById(int orderId) {
        if (orderId <= 0) {
            throw new IllegalArgumentException("order IDmust be greater than0");
        }
        
        Order order = orderDAO.getOrderById(orderId);
        if (order != null) {
            // Business logic
            List<OrderItem> orderItems = orderItemDAO.getOrderItemsByOrderId(orderId);
            order.setOrderItems(orderItems);
        }
        
        return order;
    }
    
    /**
     * Get all
     */
    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }
    
    /**
     * Get by
     */
    public List<Order> getOrdersByCustomerId(int customerId) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("customer IDmust be greater than0");
        }
        return orderDAO.getOrdersByCustomerId(customerId);
    }
    
    /**
     * Get by
     */
    public List<Order> getOrdersByEmployeeId(int employeeId) {
        if (employeeId <= 0) {
            throw new IllegalArgumentException("employee IDmust be greater than0");
        }
        return orderDAO.getOrdersByEmployeeId(employeeId);
    }
    
    /**
     * Get by
     */
    public List<Order> getOrdersByStatus(String status) {
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("order status: " + status);
        }
        return orderDAO.getOrdersByStatus(status);
    }
    
    /**
     * Update order status
     */
    public boolean updateOrderStatus(int orderId, String newStatus) {
        if (orderId <= 0) {
            throw new IllegalArgumentException("order IDmust be greater than0");
        }
        
        if (!isValidStatus(newStatus)) {
            throw new IllegalArgumentException("order status: " + newStatus);
        }
        
        // Check ifexists
        Order order = orderDAO.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order does not exist");
        }
        
        // Validate
        if (!isValidStatusTransition(order.getCurrentStatus(), newStatus)) {
            throw new IllegalArgumentException(
                String.format(" '%s'  '%s'", 
                    getStatusDescription(order.getCurrentStatus()),
                    getStatusDescription(newStatus))
            );
        }
        
        return orderDAO.updateOrderStatus(orderId, newStatus);
    }
    
    /**
     * Add order items - uses database triggers for automatic total calculation
     */
    public boolean addOrderItem(int orderId, int itemId, int quantity) {
        if (orderId <= 0) {
            throw new IllegalArgumentException("Order ID must be greater than 0");
        }
        
        if (itemId <= 0) {
            throw new IllegalArgumentException("Menu item ID must be greater than 0");
        }
        
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        
        // Check if menu item is available using database function
        if (!isMenuItemAvailable(itemId)) {
            throw new IllegalArgumentException("Menu item is not available");
        }
        
        // Check if order exists and can be modified
        Order order = orderDAO.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order does not exist");
        }
        
        if (!isOrderModifiable(order.getCurrentStatus())) {
            throw new IllegalArgumentException("Cannot modify order with status '" + 
                order.getCurrentStatus() + "'. Only PENDING orders can be modified.");
        }
        
        // Check if item already exists in order
        if (orderItemDAO.orderItemExists(orderId, itemId)) {
            // If already exists, update quantity
            OrderItem existingItem = orderItemDAO.getOrderItem(orderId, itemId);
            int newQuantity = existingItem.getQuantity() + quantity;
            return updateOrderItemQuantity(orderId, itemId, newQuantity);
        } else {
            // Add new item - trigger will automatically update order total
            OrderItem orderItem = new OrderItem(orderId, itemId, quantity);
            return orderItemDAO.addOrderItem(orderItem);
            // No need to manually call recalculateOrderTotal() - trigger handles it!
        }
    }
    
    /**
     * Check if menu item is available using database function
     */
    private boolean isMenuItemAvailable(int itemId) {
        return orderDAO.isMenuItemAvailable(itemId);
    }
    
    /**
     * Get order total using database function
     */
    public BigDecimal getOrderTotal(int orderId) {
        return orderDAO.calculateOrderTotalUsingFunction(orderId);
    }
    
    /**
     * Get available employees count using database function
     */
    public int getAvailableEmployeesCount() {
        return employeeDAO.getAvailableEmployeesCount();
    }
    
    /**
     * Auto assign employee using database procedure
     */
    public boolean autoAssignEmployee(int orderId) {
        return orderDAO.assignEmployeeToOrder(orderId);
    }
    
    /**
     * Update order item quantity - trigger will automatically update total
     */
    public boolean updateOrderItemQuantity(int orderId, int itemId, int newQuantity) {
        if (orderId <= 0) {
            throw new IllegalArgumentException("order IDmust be greater than0");
        }
        
        if (itemId <= 0) {
            throw new IllegalArgumentException("Menu item ID must be greater than 0");
        }
        
        if (newQuantity < 0) {
            throw new IllegalArgumentException("quantitycannot be less than0");
        }
        
        // Check ifmodify
        Order order = orderDAO.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order does not exist");
        }
        
        if (!isOrderModifiable(order.getCurrentStatus())) {
            throw new IllegalArgumentException("order status '" + 
                getStatusDescription(order.getCurrentStatus()) + "'modify");
        }
        
        // Update quantity - trigger will automatically update order total
        return orderItemDAO.updateOrderItemQuantity(orderId, itemId, newQuantity);
        // No need to manually recalculate - trigger handles it automatically!
    }
    
    /**
     * Delete
     */
    public boolean removeOrderItem(int orderId, int itemId) {
        return updateOrderItemQuantity(orderId, itemId, 0);
    }
    
    /**
     * Delete
     */
    public boolean deleteOrder(int orderId) {
        if (orderId <= 0) {
            throw new IllegalArgumentException("order IDmust be greater than0");
        }
        
        // Check ifexists
        Order order = orderDAO.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order does not exist");
        }
        
        // HandleDelete
        if (!"PENDING".equals(order.getCurrentStatus())) {
            throw new IllegalArgumentException("Cannot delete order with status other than PENDING");
        }
        
        return orderDAO.deleteOrder(orderId);
    }
    
    /**
     * Cancel order
     */
    public boolean cancelOrder(int orderId) {
        return updateOrderStatus(orderId, "CANCELLED");
    }
    
    /**
     * complete
     */
    public boolean completeOrder(int orderId) {
        return updateOrderStatus(orderId, "COMPLETED");
    }
    
    /**
     * Calculatetotal amount
     */
    public boolean recalculateOrderTotal(int orderId) {
        return orderDAO.recalculateOrderTotal(orderId);
    }
    
    /**
     * Validateorder status
     */
    private boolean isValidStatus(String status) {
        return status != null && VALID_STATUSES.contains(status.toUpperCase());
    }
    
    /**
     * Validate
     */
    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        if (currentStatus.equals(newStatus)) {
            return true;
        }
        
        return switch (currentStatus) {
            case "PENDING" -> Arrays.asList("ACCEPTED", "CANCELLED").contains(newStatus);
            case "ACCEPTED" -> Arrays.asList("PREPARING", "CANCELLED").contains(newStatus);
            case "PREPARING" -> Arrays.asList("COMPLETED", "CANCELLED").contains(newStatus);
            case "COMPLETED", "CANCELLED" -> false; // Business logic
            default -> false;
        };
    }
    
    /**
     * Check if order can be modified
     */
    private boolean isOrderModifiable(String status) {
        return Arrays.asList("PENDING", "ACCEPTED").contains(status);
    }
    
    /**
     * Get status description
     */
    private String getStatusDescription(String status) {
        return switch (status) {
            case "PENDING" -> "Handle";
            case "ACCEPTED" -> "accepted";
            case "PREPARING" -> "preparing";
            case "COMPLETED" -> "complete";
            case "CANCELLED" -> "cancel";
            default -> status;
        };
    }
    
    /**
     * FormatDisplay
     */
    public String formatOrdersForDisplay(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return "No orders found";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-4s %-15s %-15s %-20s %-10s %-8s%n", 
                "ID", "Customer", "Employee", "Order Time", "Total Amount", "Status"));
        sb.append("-".repeat(80)).append("\n");
        
        for (Order order : orders) {
            sb.append(String.format("%-4d %-15s %-15s %-20s %-10s %-8s%n",
                order.getOrderId(),
                order.getCustomerName(),
                order.getEmployeeName(),
                order.getFormattedOrderTime(),
                order.getFormattedTotalAmount(),
                getStatusDescription(order.getCurrentStatus())
            ));
        }
        
        return sb.toString();
    }
    
    /**
     * statisticsinformation
     */
    public void printOrderStatistics() {
        orderDAO.printOrderStatistics();
    }
    
    /**
     * statistics
     */
    public void printTodayOrderStatistics() {
        orderDAO.printTodayOrderStatistics();
    }
    
    /**
     * Get order detailed information
     */
    public String getOrderDetailInfo(int orderId) {
        Order order = getOrderById(orderId);
        if (order == null) {
            return "Order does not exist";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== Order Detailed Information ===\n");
        sb.append("Order ID: ").append(order.getOrderId()).append("\n");
        sb.append("Customer: ").append(order.getCustomerName()).append("\n");
        sb.append("Employee: ").append(order.getEmployeeName()).append("\n");
        sb.append("Order Time: ").append(order.getFormattedOrderTime()).append("\n");
        sb.append("Order Status: ").append(getStatusDescription(order.getCurrentStatus())).append("\n");
        sb.append("Total Amount: ").append(order.getFormattedTotalAmount()).append("\n");
        
        if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            sb.append("\n--- Order Items ---\n");
            sb.append(String.format("%-25s %-8s %-12s %-12s%n", "Menu Item", "Quantity", "Unit Price", "Subtotal"));
            sb.append("-".repeat(65)).append("\n");
            
            for (OrderItem item : order.getOrderItems()) {
                sb.append(String.format("%-25s %-8d %-12s %-12s%n",
                    item.getItemName(),
                    item.getQuantity(),
                    item.getFormattedItemPrice(),
                    item.getFormattedSubtotal()
                ));
            }
        }
        
        return sb.toString();
    }
    
    /**
     * 
     */
    public int getTotalOrderCount() {
        return orderDAO.getAllOrders().size();
    }
    
    /**
     * quantity
     */
    public int getOrderCountByStatus(String status) {
        return orderDAO.getOrdersByStatus(status).size();
    }
}
