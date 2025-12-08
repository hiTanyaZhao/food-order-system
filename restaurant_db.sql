DROP TABLE IF EXISTS OrderItem;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS MenuItem;
DROP TABLE IF EXISTS Category;
DROP TABLE IF EXISTS Employee;
DROP TABLE IF EXISTS Customer;



CREATE TABLE Category (
    category_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE MenuItem (
    item_id SERIAL PRIMARY KEY,
    category_id INT REFERENCES Category(category_id),
    item_name VARCHAR(100) NOT NULL,
    current_price NUMERIC(10, 2) NOT NULL CHECK (current_price >= 0),
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE Customer (
    customer_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20)
);

CREATE TABLE Employee (
    employee_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    availability_status BOOLEAN DEFAULT TRUE
);

CREATE TABLE Orders (
    order_id SERIAL PRIMARY KEY,
    customer_id INT REFERENCES Customer(customer_id),
    employee_id INT REFERENCES Employee(employee_id),
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount NUMERIC(10, 2) DEFAULT 0.00,
    current_status VARCHAR(20) NOT NULL
        CHECK (current_status IN ('PENDING', 'ACCEPTED', 'PREPARING', 'COMPLETED', 'CANCELLED'))
);

CREATE TABLE OrderItem (
    order_id INT REFERENCES Orders(order_id),
    item_id INT REFERENCES MenuItem(item_id),
    quantity INT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (order_id, item_id)
);


DELETE FROM OrderItem;
DELETE FROM Orders;
DELETE FROM MenuItem;
DELETE FROM Employee;
DELETE FROM Customer;
DELETE FROM Category;

ALTER SEQUENCE category_category_id_seq RESTART WITH 1;
ALTER SEQUENCE menuitem_item_id_seq RESTART WITH 1;
ALTER SEQUENCE customer_customer_id_seq RESTART WITH 1;
ALTER SEQUENCE employee_employee_id_seq RESTART WITH 1;
ALTER SEQUENCE orders_order_id_seq RESTART WITH 1;

INSERT INTO Category (name) VALUES 
('Appetizer'),
('Main Course'),
('Seafood'),
('Vegetarian'),
('Dessert'),
('Beverage'),
('Coffee & Tea'),
('Alcohol');

SELECT * FROM Category;

INSERT INTO MenuItem (category_id, item_name, current_price, is_active) VALUES 
(1, 'Caesar Salad', 8.99, TRUE),
(1, 'French Onion Soup', 7.50, TRUE),
(1, 'Bruschetta', 6.99, TRUE),
(1, 'Chicken Wings', 9.99, TRUE),
(2, 'Grilled Steak', 24.99, TRUE),
(2, 'Roasted Chicken', 18.99, TRUE),
(2, 'Beef Burger', 12.50, TRUE),
(2, 'BBQ Ribs', 22.99, TRUE),
(2, 'Lamb Chops', 26.99, TRUE),
(3, 'Grilled Salmon', 21.99, TRUE),
(3, 'Fish and Chips', 15.99, TRUE),
(3, 'Lobster Tail', 34.99, TRUE),
(3, 'Shrimp Scampi', 19.99, TRUE),
(3, 'Tuna Steak', 23.99, TRUE),
(4, 'Veggie Pizza', 13.99, TRUE),
(4, 'Mushroom Risotto', 16.99, TRUE),
(4, 'Quinoa Bowl', 12.99, TRUE),
(4, 'Caprese Sandwich', 10.99, TRUE),
(5, 'Chocolate Cake', 7.99, TRUE),
(5, 'Tiramisu', 8.50, TRUE),
(5, 'Cheesecake', 7.50, TRUE),
(5, 'Ice Cream Sundae', 6.99, TRUE),
(6, 'Coca Cola', 2.50, TRUE),
(6, 'Fresh Orange Juice', 4.50, TRUE),
(6, 'Lemonade', 3.50, TRUE),
(6, 'Sparkling Water', 2.00, TRUE),
(7, 'Espresso', 3.50, TRUE),
(7, 'Cappuccino', 4.50, TRUE),
(7, 'Green Tea', 3.00, TRUE),
(7, 'Latte', 4.99, TRUE);

INSERT INTO Customer (name, email, phone) VALUES 
('John Smith', 'john.smith@email.com', '555-0101'),
('Emma Johnson', 'emma.j@email.com', '555-0102'),
('Michael Brown', 'm.brown@email.com', '555-0103'),
('Sarah Davis', 'sarah.d@email.com', '555-0104'),
('David Wilson', 'david.w@email.com', '555-0105'),
('Lisa Anderson', 'lisa.a@email.com', '555-0106'),
('James Taylor', 'james.t@email.com', '555-0107'),
('Jennifer White', 'jennifer.w@email.com', '555-0108'),
('Robert Martinez', 'robert.m@email.com', '555-0109'),
('Mary Garcia', 'mary.g@email.com', '555-0110'),
('William Lee', 'william.l@email.com', '555-0111'),
('Patricia Rodriguez', 'patricia.r@email.com', '555-0112'),
('Thomas Clark', 'thomas.c@email.com', '555-0113'),
('Linda Hall', 'linda.h@email.com', '555-0114'),
('Christopher Allen', 'chris.a@email.com', '555-0115');

INSERT INTO Employee (name, phone, availability_status) VALUES 
('Chef Marco Rossi', '555-1001', TRUE),
('Chef Maria Chen', '555-1002', TRUE),
('Waiter Alex Johnson', '555-1003', TRUE),
('Waitress Sophie Martin', '555-1004', TRUE),
('Waiter Daniel Kim', '555-1005', FALSE),
('Waitress Emma Wilson', '555-1006', TRUE),
('Bartender Jake Brown', '555-1007', TRUE),
('Host Manager Sarah Lee', '555-1008', TRUE),
('Sous Chef Tom Anderson', '555-1009', FALSE),
('Server Manager Lisa Taylor', '555-1010', TRUE);

INSERT INTO Orders (customer_id, employee_id, order_time, total_amount, current_status) VALUES 
(1, 3, '2024-12-07 11:30:00', 45.48, 'COMPLETED'),
(2, 4, '2024-12-07 12:15:00', 28.98, 'COMPLETED'),
(3, 3, '2024-12-07 12:45:00', 67.97, 'COMPLETED'),
(4, 6, '2024-12-07 13:20:00', 52.47, 'COMPLETED'),
(5, 4, '2024-12-07 14:00:00', 89.95, 'PREPARING'),
(6, 3, '2024-12-07 14:30:00', 34.98, 'PREPARING'),
(7, 6, '2024-12-07 15:00:00', 125.96, 'ACCEPTED'),
(8, 4, '2024-12-07 15:30:00', 41.97, 'ACCEPTED'),
(9, 3, '2024-12-07 16:00:00', 78.46, 'PENDING'),
(10, 6, '2024-12-07 16:30:00', 55.98, 'PENDING'),
(11, 4, '2024-12-07 17:00:00', 93.95, 'COMPLETED'),
(12, 3, '2024-12-07 17:30:00', 62.47, 'COMPLETED'),
(13, 6, '2024-12-07 18:00:00', 156.93, 'PREPARING'),
(14, 4, '2024-12-07 18:30:00', 44.98, 'PREPARING'),
(15, 3, '2024-12-07 19:00:00', 71.96, 'ACCEPTED'),
(1, 6, '2024-12-07 19:30:00', 38.49, 'ACCEPTED'),
(2, 4, '2024-12-07 20:00:00', 85.97, 'PENDING'),
(3, 3, '2024-12-07 20:30:00', 112.95, 'COMPLETED'),
(4, 6, '2024-12-07 21:00:00', 58.48, 'CANCELLED'),
(5, 4, '2024-12-07 21:30:00', 96.96, 'COMPLETED');


INSERT INTO OrderItem (order_id, item_id, quantity) VALUES 
(1, 7, 2),  -- 2x Beef Burger
(1, 23, 2), -- 2x Coca Cola
(1, 19, 1), -- 1x Chocolate Cake
(2, 1, 1),  -- Caesar Salad
(2, 17, 1), -- Quinoa Bowl
(2, 24, 2), -- Orange Juice
 3
(3, 5, 1),  -- Grilled Steak
(3, 10, 1), -- Grilled Salmon
(3, 28, 1), -- Cappuccino
(3, 20, 1), -- Tiramisu
 4
(4, 8, 2),  -- BBQ Ribs
(4, 2, 1),  -- French Onion Soup
(4, 25, 1), -- Lemonade
 5
(5, 12, 1), -- Lobster Tail
(5, 5, 1),  -- Grilled Steak
(5, 1, 1),  -- Caesar Salad
(5, 21, 1), -- Cheesecake
(5, 27, 2), -- Espresso
 6
(6, 15, 2), -- Veggie Pizza
(6, 23, 2), -- Coca Cola
(6, 22, 1), -- Ice Cream Sundae
 7
(7, 12, 2), -- Lobster Tail
(7, 9, 1),  -- Lamb Chops
(7, 3, 2),  -- Bruschetta
(7, 28, 2), -- Cappuccino
 8
(8, 16, 1), -- Mushroom Risotto
(8, 11, 1), -- Fish and Chips
(8, 26, 2), -- Sparkling Water
(8, 29, 1), -- Green Tea
 9
(9, 6, 2),  -- Roasted Chicken
(9, 4, 1),  -- Chicken Wings
(9, 24, 2), -- Orange Juice
(9, 19, 2), -- Chocolate Cake
0
(10, 13, 1), -- Shrimp Scampi
(10, 14, 1), -- Tuna Steak
(10, 1, 1),  -- Caesar Salad
(10, 30, 2), -- Latte
1
(11, 5, 2),  -- Grilled Steak
(11, 2, 1),  -- French Onion Soup
(11, 20, 2), -- Tiramisu
(11, 27, 2), -- Espresso
2
(12, 10, 1), -- Grilled Salmon
(12, 8, 1),  -- BBQ Ribs
(12, 25, 2), -- Lemonade
(12, 21, 1), -- Cheesecake
3
(13, 12, 3), -- Lobster Tail
(13, 3, 2),  -- Bruschetta
(13, 28, 3), -- Cappuccino
4
(14, 7, 2),  -- Beef Burger
(14, 23, 3), -- Coca Cola
(14, 22, 2), -- Ice Cream Sundae
5
(15, 15, 1), -- Veggie Pizza
(15, 16, 1), -- Mushroom Risotto
(15, 17, 1), -- Quinoa Bowl
(15, 24, 2), -- Orange Juice
6
(16, 18, 2), -- Caprese Sandwich
(16, 29, 2), -- Green Tea
(16, 19, 1), -- Chocolate Cake
7
(17, 5, 1),  -- Grilled Steak
(17, 10, 1), -- Grilled Salmon
(17, 9, 1),  -- Lamb Chops
(17, 30, 2), -- Latte
8
(18, 12, 2), -- Lobster Tail
(18, 14, 1), -- Tuna Steak
(18, 1, 1),  -- Caesar Salad
(18, 20, 2), -- Tiramisu
9
(19, 6, 2),  -- Roasted Chicken
(19, 4, 1),  -- Chicken Wings
(19, 23, 3), -- Coca Cola
0
(20, 8, 2),  -- BBQ Ribs
(20, 13, 1), -- Shrimp Scampi
(20, 21, 2), -- Cheesecake
(20, 28, 2); -- Cappuccino

-- DATABASE PROGRAMMING OBJECTS

-- Indexes
CREATE INDEX idx_menuitem_category ON MenuItem(category_id);
CREATE INDEX idx_orders_customer ON Orders(customer_id);
CREATE INDEX idx_orders_employee ON Orders(employee_id);
CREATE INDEX idx_orders_status ON Orders(current_status);
CREATE INDEX idx_orders_time ON Orders(order_time);
CREATE INDEX idx_orderitem_composite ON OrderItem(order_id, item_id);
CREATE INDEX idx_customer_email ON Customer(email);

-- Functions
CREATE OR REPLACE FUNCTION calculate_order_total(p_order_id INT)
RETURNS NUMERIC(10,2) AS $$
DECLARE
    total_amount NUMERIC(10,2) := 0;
BEGIN
    SELECT COALESCE(SUM(oi.quantity * mi.current_price), 0)
    INTO total_amount
    FROM OrderItem oi
    JOIN MenuItem mi ON oi.item_id = mi.item_id
    WHERE oi.order_id = p_order_id;
    
    RETURN total_amount;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_available_employees_count()
RETURNS INTEGER AS $$
DECLARE
    emp_count INTEGER;
BEGIN
    SELECT COUNT(*)
    INTO emp_count
    FROM Employee
    WHERE availability_status = TRUE;
    
    RETURN emp_count;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_customer_order_count(p_customer_id INT)
RETURNS INTEGER AS $$
DECLARE
    order_count INTEGER;
BEGIN
    SELECT COUNT(*)
    INTO order_count
    FROM Orders
    WHERE customer_id = p_customer_id;
    
    RETURN order_count;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION is_menu_item_available(p_item_id INT)
RETURNS BOOLEAN AS $$
DECLARE
    is_available BOOLEAN := FALSE;
BEGIN
    SELECT is_active
    INTO is_available
    FROM MenuItem
    WHERE item_id = p_item_id;
    
    RETURN COALESCE(is_available, FALSE);
END;
$$ LANGUAGE plpgsql;

-- Stored Procedures
CREATE OR REPLACE PROCEDURE update_order_total(p_order_id INT)
LANGUAGE plpgsql AS $$
DECLARE
    new_total NUMERIC(10,2);
BEGIN
    new_total := calculate_order_total(p_order_id);
    
    UPDATE Orders 
    SET total_amount = new_total
    WHERE order_id = p_order_id;
    
    RAISE NOTICE 'Order % total updated to %', p_order_id, new_total;
END;
$$;

CREATE OR REPLACE PROCEDURE assign_employee_to_order(p_order_id INT)
LANGUAGE plpgsql AS $$
DECLARE
    available_employee_id INT;
BEGIN
    SELECT employee_id
    INTO available_employee_id
    FROM Employee
    WHERE availability_status = TRUE
    ORDER BY employee_id
    LIMIT 1;
    
    IF available_employee_id IS NOT NULL THEN
        UPDATE Orders
        SET employee_id = available_employee_id
        WHERE order_id = p_order_id;
        
        RAISE NOTICE 'Employee % assigned to order %', available_employee_id, p_order_id;
    ELSE
        RAISE EXCEPTION 'No available employees found';
    END IF;
END;
$$;

-- Triggers
CREATE OR REPLACE FUNCTION trigger_update_order_total()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        CALL update_order_total(OLD.order_id);
        RETURN OLD;
    ELSE
        CALL update_order_total(NEW.order_id);
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_orderitem_insert_update_total
    AFTER INSERT ON OrderItem
    FOR EACH ROW
    EXECUTE FUNCTION trigger_update_order_total();

CREATE TRIGGER tr_orderitem_update_update_total
    AFTER UPDATE ON OrderItem
    FOR EACH ROW
    EXECUTE FUNCTION trigger_update_order_total();

CREATE TRIGGER tr_orderitem_delete_update_total
    AFTER DELETE ON OrderItem
    FOR EACH ROW
    EXECUTE FUNCTION trigger_update_order_total();

CREATE OR REPLACE FUNCTION validate_order_status_transition()
RETURNS TRIGGER AS $$
BEGIN
    IF OLD.current_status = 'COMPLETED' AND NEW.current_status != 'COMPLETED' THEN
        RAISE EXCEPTION 'Cannot change status of completed order';
    END IF;
    
    IF OLD.current_status = 'CANCELLED' AND NEW.current_status != 'CANCELLED' THEN
        RAISE EXCEPTION 'Cannot change status of cancelled order';
    END IF;
    
    RAISE NOTICE 'Order % status changed from % to %', NEW.order_id, OLD.current_status, NEW.current_status;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_orders_status_validation
    BEFORE UPDATE ON Orders
    FOR EACH ROW
    WHEN (OLD.current_status IS DISTINCT FROM NEW.current_status)
    EXECUTE FUNCTION validate_order_status_transition();

-- Views
CREATE OR REPLACE VIEW order_summary AS
SELECT 
    o.order_id,
    c.name AS customer_name,
    c.email AS customer_email,
    e.name AS employee_name,
    o.order_time,
    o.total_amount,
    o.current_status,
    COUNT(oi.item_id) AS item_count
FROM Orders o
JOIN Customer c ON o.customer_id = c.customer_id
LEFT JOIN Employee e ON o.employee_id = e.employee_id
LEFT JOIN OrderItem oi ON o.order_id = oi.order_id
GROUP BY o.order_id, c.name, c.email, e.name, o.order_time, o.total_amount, o.current_status
ORDER BY o.order_time DESC;

CREATE OR REPLACE VIEW menu_with_category AS
SELECT 
    mi.item_id,
    mi.item_name,
    mi.current_price,
    mi.is_active,
    c.name AS category_name,
    c.category_id
FROM MenuItem mi
JOIN Category c ON mi.category_id = c.category_id
ORDER BY c.name, mi.item_name;

CREATE OR REPLACE VIEW employee_workload AS
SELECT 
    e.employee_id,
    e.name AS employee_name,
    e.availability_status,
    COUNT(o.order_id) AS total_orders,
    COALESCE(SUM(o.total_amount), 0) AS total_revenue,
    COUNT(CASE WHEN o.current_status = 'COMPLETED' THEN 1 END) AS completed_orders
FROM Employee e
LEFT JOIN Orders o ON e.employee_id = o.employee_id
GROUP BY e.employee_id, e.name, e.availability_status
ORDER BY total_orders DESC;

-- Foreign Key Constraints
ALTER TABLE MenuItem 
DROP CONSTRAINT IF EXISTS menuitem_category_id_fkey,
ADD CONSTRAINT menuitem_category_id_fkey 
    FOREIGN KEY (category_id) REFERENCES Category(category_id) 
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE Orders 
DROP CONSTRAINT IF EXISTS orders_customer_id_fkey,
ADD CONSTRAINT orders_customer_id_fkey 
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) 
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE Orders 
DROP CONSTRAINT IF EXISTS orders_employee_id_fkey,
ADD CONSTRAINT orders_employee_id_fkey 
    FOREIGN KEY (employee_id) REFERENCES Employee(employee_id) 
    ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE OrderItem 
DROP CONSTRAINT IF EXISTS orderitem_order_id_fkey,
ADD CONSTRAINT orderitem_order_id_fkey 
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) 
    ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE OrderItem 
DROP CONSTRAINT IF EXISTS orderitem_item_id_fkey,
ADD CONSTRAINT orderitem_item_id_fkey 
    FOREIGN KEY (item_id) REFERENCES MenuItem(item_id) 
    ON DELETE RESTRICT ON UPDATE CASCADE;

-- Test Functions
SELECT 'Testing Functions:' AS test_section;
SELECT calculate_order_total(1) AS order_1_total;
SELECT get_available_employees_count() AS available_employees;
SELECT get_customer_order_count(1) AS customer_1_orders;
SELECT is_menu_item_available(1) AS item_1_available;

-- Test Views
SELECT 'Testing Views:' AS test_section;
SELECT * FROM order_summary LIMIT 5;
SELECT * FROM menu_with_category WHERE category_name = 'Main Course' LIMIT 3;
SELECT * FROM employee_workload LIMIT 5;

-- Test Procedures
CALL update_order_total(1);
CALL update_order_total(2);

COMMIT;
