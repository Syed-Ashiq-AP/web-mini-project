-- Student Management System Database Schema
-- MySQL Script

-- Create Database
CREATE DATABASE IF NOT EXISTS student_management;
USE student_management;

-- Create Students Table
CREATE TABLE IF NOT EXISTS students (
    roll_number VARCHAR(50) PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    course VARCHAR(100) NOT NULL,
    gpa DECIMAL(3,2) NOT NULL,
    date_of_birth DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create Index for faster searches
CREATE INDEX idx_first_name ON students(first_name);
CREATE INDEX idx_last_name ON students(last_name);
CREATE INDEX idx_course ON students(course);
CREATE INDEX idx_email ON students(email);

-- Sample Data (Optional)
INSERT INTO students (roll_number, first_name, last_name, email, phone, course, gpa, date_of_birth) 
VALUES ('001', 'John', 'Doe', 'john.doe@email.com', '9876543210', 'Computer Science', 3.5, '2002-01-15');

INSERT INTO students (roll_number, first_name, last_name, email, phone, course, gpa, date_of_birth) 
VALUES ('002', 'Jane', 'Smith', 'jane.smith@email.com', '9876543211', 'Information Technology', 3.8, '2001-05-20');

INSERT INTO students (roll_number, first_name, last_name, email, phone, course, gpa, date_of_birth) 
VALUES ('003', 'Robert', 'Johnson', 'robert.j@email.com', '9876543212', 'Electronics', 3.2, '2002-03-10');

-- Verify data
SELECT * FROM students;
