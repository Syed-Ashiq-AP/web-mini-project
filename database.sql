-- ============================================
-- Inventory Management System Database Setup
-- ============================================

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS inventory_management;

-- Use the database
USE inventory_management;

-- Drop existing table if it exists (for fresh setup)
DROP TABLE IF EXISTS products;

-- Create products table
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id VARCHAR(50) UNIQUE NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    price DECIMAL(10, 2) NOT NULL,
    supplier VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_product_id (product_id),
    INDEX idx_category (category),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample data
INSERT INTO products (product_id, product_name, category, quantity, price, supplier, status) VALUES
('P001', 'Dell Laptop XPS 15', 'Electronics', 25, 1299.99, 'Dell Inc.', 'In Stock'),
('P002', 'HP Wireless Mouse', 'Electronics', 150, 29.99, 'HP Supplies', 'In Stock'),
('P003', 'Office Chair Executive', 'Furniture', 12, 249.99, 'Furniture World', 'In Stock'),
('P004', 'LED Monitor 27 inch', 'Electronics', 8, 349.99, 'Samsung Electronics', 'Low Stock'),
('P005', 'Ergonomic Keyboard', 'Electronics', 45, 79.99, 'Logitech', 'In Stock'),
('P006', 'Standing Desk', 'Furniture', 3, 599.99, 'Furniture World', 'Low Stock'),
('P007', 'USB-C Hub', 'Electronics', 0, 49.99, 'Anker', 'Out of Stock'),
('P008', 'Desk Lamp LED', 'Furniture', 20, 39.99, 'IKEA', 'In Stock'),
('P009', 'Wireless Headphones', 'Electronics', 35, 199.99, 'Sony', 'In Stock'),
('P010', 'Filing Cabinet', 'Furniture', 6, 179.99, 'Furniture World', 'Low Stock');

-- Display all products
SELECT * FROM products;

-- ============================================
-- Useful Queries for Inventory Management
-- ============================================

-- View all products
-- SELECT * FROM products ORDER BY product_id;

-- View products by category
-- SELECT * FROM products WHERE category = 'Electronics';

-- View low stock products
-- SELECT * FROM products WHERE status = 'Low Stock' OR status = 'Out of Stock';

-- View products with quantity less than 10
-- SELECT * FROM products WHERE quantity < 10;

-- Update product quantity
-- UPDATE products SET quantity = 50 WHERE product_id = 'P007';

-- Update product status based on quantity
-- UPDATE products 
-- SET status = CASE 
--     WHEN quantity = 0 THEN 'Out of Stock'
--     WHEN quantity < 10 THEN 'Low Stock'
--     ELSE 'In Stock'
-- END;

-- Delete a product
-- DELETE FROM products WHERE product_id = 'P001';

-- Get total inventory value
-- SELECT SUM(quantity * price) AS total_value FROM products;

-- Get inventory value by category
-- SELECT category, SUM(quantity * price) AS category_value 
-- FROM products 
-- GROUP BY category;

-- Count products by status
-- SELECT status, COUNT(*) AS count 
-- FROM products 
-- GROUP BY status;
