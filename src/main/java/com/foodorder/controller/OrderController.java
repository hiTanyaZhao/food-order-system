package com.foodorder.controller;

import java.util.List;
import java.util.Scanner;

import com.foodorder.model.Customer;
import com.foodorder.model.Employee;
import com.foodorder.model.MenuItem;
import com.foodorder.model.Order;
import com.foodorder.service.CustomerService;
import com.foodorder.service.EmployeeService;
import com.foodorder.service.MenuService;
import com.foodorder.service.OrderService;

/**
 * Controller
 * HandleManagementrelated user interface interactions
 */
public class OrderController {
    
    private OrderService orderService;
    private MenuService menuService;
    private CustomerController customerController;
    private EmployeeController employeeController;
    private Scanner scanner;
    
    public OrderController() {
        this.orderService = new OrderService();
        this.menuService = new MenuService();
        this.customerController = new CustomerController();
        this.employeeController = new EmployeeController();
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * DisplayManagement
     */
    public void showOrderInterface() {
        while (true) {
            displayOrderOptions();
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1 -> createOrder();
                    case 2 -> viewAllOrders();
                    case 3 -> viewOrdersByStatus();
                    case 4 -> viewOrderDetails();
                    case 5 -> updateOrderStatus();
                    case 6 -> addOrderItems();
                    case 7 -> updateOrderItems();
                    case 8 -> viewCustomerOrders();
                    case 9 -> viewEmployeeOrders();
                    case 10 -> cancelOrder();
                    case 11 -> deleteOrder();
                    case 12 -> viewOrderStatistics();
                    case 13 -> {
                        System.out.println("Return to main menu...");
                        return;
                    }
                    default -> System.out.println("Invalid selection, please try again");
                }
                
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
                
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number selection");
            } catch (Exception e) {
                System.out.println("Error occurred: " + e.getMessage());
            }
        }
    }
    
    /**
     * DisplayManagement
     */
    private void displayOrderOptions() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("             Order Management System");
        System.out.println("=".repeat(50));
        System.out.println("1. Create new order");
        System.out.println("2. View all orders");
        System.out.println("3. View orders by status");
        System.out.println("4. View order details");
        System.out.println("5. Update order status");
        System.out.println("6. Add order items");
        System.out.println("7. Modify order items");
        System.out.println("8. View customer orders");
        System.out.println("9. View employee orders");
        System.out.println("10. Cancel order");
        System.out.println("11. Delete order");
        System.out.println("12. View order statistics");
        System.out.println("13. Return to main menu");
        System.out.println("=".repeat(50));
        System.out.print("Please select operation (1-13): ");
    }
    
    /**
     * Create new order
     */
    private void createOrder() {
        System.out.println("\nCreate New Order");
        System.out.println("=".repeat(40));
        
        try {
            // Step 1: Select customer
            System.out.println("Step 1: Select Customer");
            CustomerService customerService = new CustomerService();
            List<Customer> customers = customerService.getAllCustomers();
            
            if (customers.isEmpty()) {
                System.out.println("No customers found. Please add customers first.");
                return;
            }
            
            System.out.println("Available customers:");
            for (Customer customer : customers) {
                System.out.println(customer.getCustomerId() + ". " + customer.getName() + 
                                 " (" + customer.getEmail() + ")");
            }
            
            System.out.print("Enter customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine().trim());
            
            // Find selected customer
            Customer selectedCustomer = null;
            for (Customer customer : customers) {
                if (customer.getCustomerId() == customerId) {
                    selectedCustomer = customer;
                    break;
                }
            }
            
            if (selectedCustomer == null) {
                System.out.println("Invalid customer ID.");
                return;
            }
            
            // Step 2: Employee assignment (simplified - just auto assign)
            System.out.println("\nStep 2: Creating order with auto-assigned employee...");
            int orderId = orderService.createOrderWithAutoAssignment(customerId);
            
            if (orderId > 0) {
                System.out.println("\n✓ Order created successfully!");
                System.out.println("Order ID: " + orderId);
                System.out.println("Customer: " + selectedCustomer.getName());
                
                // Ask if user wants to add items
                System.out.print("\nWould you like to add items to this order? (Y/n): ");
                String addItems = scanner.nextLine().trim().toLowerCase();
                
                if (addItems.equals("y") || addItems.equals("yes") || addItems.isEmpty()) {
                    addOrderItemsToOrder(orderId);
                }
                
            } else {
                System.out.println("Failed to create order. No available employees.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Error creating order: " + e.getMessage());
        }
    }
    
    /**
     * View all orders
     */
    private void viewAllOrders() {
        System.out.println("\nAll Orders");
        System.out.println("=".repeat(80));
        
        try {
            List<Order> orders = orderService.getAllOrders();
            
            if (orders.isEmpty()) {
                System.out.println("No orders found.");
            } else {
                System.out.printf("%-4s %-15s %-15s %-20s %-12s %-10s%n", 
                                "ID", "Customer", "Employee", "Order Time", "Total", "Status");
                System.out.println("-".repeat(80));
                
                for (Order order : orders) {
                    System.out.printf("%-4d %-15s %-15s %-20s %-12s %-10s%n",
                        order.getOrderId(),
                        order.getCustomerName(),
                        order.getEmployeeName(),
                        order.getFormattedOrderTime(),
                        order.getFormattedTotalAmount(),
                        order.getCurrentStatus());
                }
                
                System.out.println("\n total: " + orders.size() + " orders");
            }
            
        } catch (Exception e) {
            System.out.println(" informationfailed: " + e.getMessage());
        }
    }
    
    /**
     * View orders by status
     */
    private void viewOrdersByStatus() {
        System.out.println("\n View orders by status");
        System.out.println("=".repeat(35));
        
        try {
            System.out.println("order status:");
            System.out.println("1. Handle (PENDING)");
            System.out.println("2. accepted (ACCEPTED)");
            System.out.println("3. preparing (PREPARING)");
            System.out.println("4. complete (COMPLETED)");
            System.out.println("5. cancel (CANCELLED)");
            System.out.print(": ");
            
            int choice = Integer.parseInt(scanner.nextLine().trim());
            String status = switch (choice) {
                case 1 -> "PENDING";
                case 2 -> "ACCEPTED";
                case 3 -> "PREPARING";
                case 4 -> "COMPLETED";
                case 5 -> "CANCELLED";
                default -> null;
            };
            
            if (status == null) {
                System.out.println(" ");
                return;
            }
            
            List<Order> orders = orderService.getOrdersByStatus(status);
            
            System.out.println("\n " + getStatusDescription(status) + " ");
            System.out.println("=".repeat(50));
            
            if (orders.isEmpty()) {
                System.out.println(" " + getStatusDescription(status) + " ");
            } else {
                System.out.printf("%-4s %-15s %-15s %-20s %-10s%n", 
                                "ID", "", "", "order time", "total amount");
                System.out.println("-".repeat(70));
                
                orders.forEach(order -> 
                    System.out.printf("%-4d %-15s %-15s %-20s %-10s%n",
                        order.getOrderId(),
                        order.getCustomerName(),
                        order.getEmployeeName(),
                        order.getFormattedOrderTime(),
                        order.getFormattedTotalAmount())
                );
                
                System.out.println("\n found " + orders.size() + " orders");
            }
            
        } catch (NumberFormatException e) {
            System.out.println(" Please enter");
        } catch (Exception e) {
            System.out.println(" failed: " + e.getMessage());
        }
    }
    
    /**
     * View order details
     */
    private void viewOrderDetails() {
        System.out.println("\nView Order Details");
        System.out.println("=".repeat(40));
        
        try {
            System.out.print("Please enter order ID: ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());
            
            String detailInfo = orderService.getOrderDetailInfo(orderId);
            if (detailInfo != null && !detailInfo.trim().isEmpty()) {
                System.out.println("\n" + detailInfo);
            } else {
                System.out.println("Order not found or no details available.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid order ID.");
        } catch (Exception e) {
            System.out.println("Error viewing order details: " + e.getMessage());
        }
    }
    
    /**
     * Update order status
     */
    private void updateOrderStatus() {
        System.out.println("\nUpdate Order Status");
        System.out.println("=".repeat(40));
        
        try {
            System.out.print("Please enterorder ID: ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());
            
            // Get order information
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                System.out.println("Order does not exist.");
                return;
            }
            
            System.out.println("Current order status: " + order.getCurrentStatus());
            
            System.out.println("\nSelect new status:");
            System.out.println("1. Pending (PENDING)");
            System.out.println("2. Accepted (ACCEPTED)");
            System.out.println("3. Preparing (PREPARING)");
            System.out.println("4. Completed (COMPLETED)");
            System.out.println("5. Cancelled (CANCELLED)");
            System.out.print("Choose option (1-5): ");
            
            int choice = Integer.parseInt(scanner.nextLine().trim());
            String newStatus = switch (choice) {
                case 1 -> "PENDING";
                case 2 -> "ACCEPTED";
                case 3 -> "PREPARING";
                case 4 -> "COMPLETED";
                case 5 -> "CANCELLED";
                default -> null;
            };
            
            if (newStatus == null) {
                System.out.println("Invalid selection.");
                return;
            }
            
            boolean success = orderService.updateOrderStatus(orderId, newStatus);
            
            if (success) {
                System.out.println("✓ Order status updated successfully!");
                System.out.println("New status: " + newStatus);
            } else {
                System.out.println("✗ Failed to update order status.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numbers.");
        } catch (Exception e) {
            System.out.println("Error updating order status: " + e.getMessage());
        }
    }
    
    /**
     * Add order items
     */
    private void addOrderItems() {
        System.out.println("\nAdd Order Items");
        System.out.println("=".repeat(40));
        
        try {
            System.out.print("Please enter order ID: ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());
            
            // Check if order exists
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                System.out.println("Order does not exist.");
                return;
            }
            
            System.out.println("Order Information: " + order.getCustomerName() + " - " + 
                             order.getCurrentStatus());
            
            addOrderItemsToOrder(orderId);
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid order ID.");
        } catch (Exception e) {
            System.out.println("Failed to add order items: " + e.getMessage());
        }
    }
    
    /**
     * Add items to order
     */
    private void addOrderItemsToOrder(int orderId) {
        while (true) {
            System.out.println("\n--- Add Items to Order ---");
            System.out.println("1. Browse menu and add item");
            System.out.println("2. Add item by ID");
            System.out.println("3. Finish adding items");
            System.out.print("Choose option (1-3): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1 -> {
                        // Display menu
                        List<MenuItem> menuItems = menuService.getAllAvailableMenuItems();
                        System.out.println("\nAvailable Menu Items:");
                        System.out.printf("%-4s %-25s %-15s %-10s%n", "ID", "Item Name", "Category", "Price");
                        System.out.println("-".repeat(60));
                        
                        for (MenuItem item : menuItems) {
                            System.out.printf("%-4d %-25s %-15s %-10s%n",
                                item.getItemId(),
                                item.getItemName(),
                                item.getCategoryName(),
                                item.getFormattedPrice());
                        }
                        
                        System.out.print("\nEnter menu item ID: ");
                        int itemId = Integer.parseInt(scanner.nextLine().trim());
                        
                        System.out.print("Enter quantity: ");
                        int quantity = Integer.parseInt(scanner.nextLine().trim());
                        
                        boolean success = orderService.addOrderItem(orderId, itemId, quantity);
                        if (success) {
                            System.out.println("✓ Item added successfully!");
                        } else {
                            System.out.println("✗ Failed to add item.");
                        }
                    }
                    case 2 -> {
                        System.out.print("Enter menu item ID: ");
                        int itemId = Integer.parseInt(scanner.nextLine().trim());
                        
                        System.out.print("Enter quantity: ");
                        int quantity = Integer.parseInt(scanner.nextLine().trim());
                        
                        boolean success = orderService.addOrderItem(orderId, itemId, quantity);
                        if (success) {
                            System.out.println("✓ Item added successfully!");
                        } else {
                            System.out.println("✗ Failed to add item.");
                        }
                    }
                    case 3 -> {
                        // Recalculate total amount
                        orderService.recalculateOrderTotal(orderId);
                        System.out.println("✓ Order items added. Total amount updated.");
                        return;
                    }
                    default -> System.out.println("Invalid selection. Please try again.");
                }
                
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    /**
     * Modify order items
     */
    private void updateOrderItems() {
        System.out.println("\nModify Order Items");
        System.out.println("=".repeat(40));
        
        try {
            System.out.print("Please enter order ID: ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());
            
            // Display order details
            String orderDetail = orderService.getOrderDetailInfo(orderId);
            System.out.println("\n" + orderDetail);
            
            System.out.print("\nEnter menu item ID to modify: ");
            int itemId = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.print("Enter new quantity (0 to delete): ");
            int newQuantity = Integer.parseInt(scanner.nextLine().trim());
            
            boolean success = orderService.updateOrderItemQuantity(orderId, itemId, newQuantity);
            
            if (success) {
                if (newQuantity == 0) {
                    System.out.println("✓ Item deleted successfully!");
                } else {
                    System.out.println("✓ Item quantity updated successfully!");
                }
                // Recalculate total amount
                orderService.recalculateOrderTotal(orderId);
                System.out.println("✓ Order total updated.");
            } else {
                System.out.println("✗ Failed to modify item.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numbers.");
        } catch (Exception e) {
            System.out.println("Error modifying order: " + e.getMessage());
        }
    }
    
    /**
     * View customer orders
     */
    private void viewCustomerOrders() {
        System.out.println("\nView Customer Orders");
        System.out.println("=".repeat(40));
        
        try {
            // First show all customers
            System.out.println("Available customers:");
            CustomerService customerService = new CustomerService();
            List<Customer> customers = customerService.getAllCustomers();
            
            if (customers.isEmpty()) {
                System.out.println("No customers found.");
                return;
            }
            
            // Display customers
            for (Customer customer : customers) {
                System.out.println(customer.getCustomerId() + ". " + customer.getName() + 
                                 " (" + customer.getEmail() + ")");
            }
            
            System.out.print("\nEnter customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine().trim());
            
            // Find the selected customer
            Customer selectedCustomer = null;
            for (Customer customer : customers) {
                if (customer.getCustomerId() == customerId) {
                    selectedCustomer = customer;
                    break;
                }
            }
            
            if (selectedCustomer == null) {
                System.out.println("Invalid customer ID.");
                return;
            }
            
            // Get orders for this customer
            List<Order> orders = orderService.getOrdersByCustomerId(customerId);
            
            System.out.println("\nOrders for: " + selectedCustomer.getName());
            System.out.println("=".repeat(70));
            
            if (orders.isEmpty()) {
                System.out.println("No orders found for this customer.");
            } else {
                System.out.printf("%-6s %-15s %-20s %-12s %-10s%n", 
                                "Order ID", "Employee", "Order Time", "Total", "Status");
                System.out.println("-".repeat(70));
                
                for (Order order : orders) {
                    System.out.printf("%-6d %-15s %-20s %-12s %-10s%n",
                        order.getOrderId(),
                        order.getEmployeeName(),
                        order.getFormattedOrderTime(),
                        order.getFormattedTotalAmount(),
                        order.getCurrentStatus());
                }
                
                System.out.println("\nTotal orders: " + orders.size());
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid customer ID.");
        } catch (Exception e) {
            System.out.println("Error viewing customer orders: " + e.getMessage());
        }
    }
    
    /**
     * View employee orders
     */
    private void viewEmployeeOrders() {
        System.out.println("\nView Employee Orders");
        System.out.println("=".repeat(40));
        
        try {
            // First show all employees
            System.out.println("Available employees:");
            EmployeeService employeeService = new EmployeeService();
            List<Employee> employees = employeeService.getAllEmployees();
            
            if (employees.isEmpty()) {
                System.out.println("No employees found.");
                return;
            }
            
            // Display employees
            for (Employee employee : employees) {
                System.out.println(employee.getEmployeeId() + ". " + employee.getName() + 
                                 " (" + employee.getAvailabilityStatusText() + ")");
            }
            
            System.out.print("\nEnter employee ID: ");
            int employeeId = Integer.parseInt(scanner.nextLine().trim());
            
            // Find the selected employee
            Employee selectedEmployee = null;
            for (Employee employee : employees) {
                if (employee.getEmployeeId() == employeeId) {
                    selectedEmployee = employee;
                    break;
                }
            }
            
            if (selectedEmployee == null) {
                System.out.println("Invalid employee ID.");
                return;
            }
            
            // Get orders for this employee
            List<Order> orders = orderService.getOrdersByEmployeeId(employeeId);
            
            System.out.println("\nOrders handled by: " + selectedEmployee.getName());
            System.out.println("=".repeat(70));
            
            if (orders.isEmpty()) {
                System.out.println("No orders found for this employee.");
            } else {
                System.out.printf("%-6s %-15s %-20s %-12s %-10s%n", 
                                "Order ID", "Customer", "Order Time", "Total", "Status");
                System.out.println("-".repeat(70));
                
                for (Order order : orders) {
                    System.out.printf("%-6d %-15s %-20s %-12s %-10s%n",
                        order.getOrderId(),
                        order.getCustomerName(),
                        order.getFormattedOrderTime(),
                        order.getFormattedTotalAmount(),
                        order.getCurrentStatus());
                }
                
                System.out.println("\nTotal orders handled: " + orders.size());
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid employee ID.");
        } catch (Exception e) {
            System.out.println("Error viewing employee orders: " + e.getMessage());
        }
    }
    
    /**
     * Cancel order
     */
    private void cancelOrder() {
        System.out.println("\nCancel Order");
        System.out.println("=".repeat(40));
        
        try {
            System.out.print("Please enter order ID to cancel: ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());
            
            // Get order information and confirm
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                System.out.println("Order does not exist.");
                return;
            }
            
            System.out.println("Order Information:");
            System.out.println("Customer: " + order.getCustomerName());
            System.out.println("Status: " + order.getCurrentStatus());
            System.out.println("Total Amount: " + order.getFormattedTotalAmount());
            
            if (!order.isCancellable()) {
                System.out.println("Cannot cancel order with current status.");
                return;
            }
            
            System.out.print("Confirm cancel order? (y/N): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            
            if ("y".equals(confirm) || "yes".equals(confirm)) {
                boolean success = orderService.cancelOrder(orderId);
                
                if (success) {
                    System.out.println(" cancelsuccessful");
                } else {
                    System.out.println(" cancelfailed");
                }
            } else {
                System.out.println("cancel");
            }
            
        } catch (NumberFormatException e) {
            System.out.println(" Please enterorder ID");
        } catch (Exception e) {
            System.out.println(" Cancel orderfailed: " + e.getMessage());
        }
    }
    
    /**
     * Delete
     */
    private void deleteOrder() {
        System.out.println("\nDelete Order");
        System.out.println("=".repeat(40));
        
        try {
            System.out.print("Please enter order ID to delete: ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());
            
            // Get order information and confirm
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                System.out.println("Order does not exist.");
                return;
            }
            
            System.out.println("Order Information:");
            System.out.println("Customer: " + order.getCustomerName());
            System.out.println("Status: " + order.getCurrentStatus());
            System.out.println("Total Amount: " + order.getFormattedTotalAmount());
            
            System.out.println("\n⚠️  Warning: This will permanently delete the order and all its items.");
            System.out.print("Confirm delete? (y/N): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            
            if ("y".equals(confirm) || "yes".equals(confirm)) {
                boolean success = orderService.deleteOrder(orderId);
                
                if (success) {
                    System.out.println(" Deletesuccessful");
                } else {
                    System.out.println(" Deletefailed");
                }
            } else {
                System.out.println("cancelDelete");
            }
            
        } catch (NumberFormatException e) {
            System.out.println(" Please enterorder ID");
        } catch (Exception e) {
            System.out.println(" Deletefailed: " + e.getMessage());
        }
    }
    
    /**
     * View order statistics
     */
    private void viewOrderStatistics() {
        System.out.println("\n statisticsinformation");
        System.out.println("=".repeat(35));
        
        try {
            System.out.println(" basicstatistics:");
            System.out.println("   order count: " + orderService.getTotalOrderCount());
            System.out.println("   Handle: " + orderService.getOrderCountByStatus("PENDING"));
            System.out.println("   complete: " + orderService.getOrderCountByStatus("COMPLETED"));
            
            orderService.printOrderStatistics();
            orderService.printTodayOrderStatistics();
            
        } catch (Exception e) {
            System.out.println(" statisticsinformationfailed: " + e.getMessage());
        }
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
}
