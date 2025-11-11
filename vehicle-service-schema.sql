-- Vehicle Service Management System Database Schema

CREATE DATABASE IF NOT EXISTS vehicle_service_db;
USE vehicle_service_db;

-- Table for storing vehicle service records
CREATE TABLE IF NOT EXISTS vehicles (
    vehicle_id VARCHAR(50) PRIMARY KEY,
    owner_name VARCHAR(100) NOT NULL,
    vehicle_model VARCHAR(100) NOT NULL,
    registration_number VARCHAR(50) NOT NULL UNIQUE,
    contact_number VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    service_type VARCHAR(50) NOT NULL,
    mileage INT NOT NULL,
    service_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Indexes for optimized queries
CREATE INDEX idx_owner_name ON vehicles(owner_name);
CREATE INDEX idx_vehicle_model ON vehicles(vehicle_model);
CREATE INDEX idx_registration_number ON vehicles(registration_number);
CREATE INDEX idx_service_type ON vehicles(service_type);
CREATE INDEX idx_email ON vehicles(email);
