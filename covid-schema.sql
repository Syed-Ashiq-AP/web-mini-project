CREATE DATABASE IF NOT EXISTS covid_tracker;
USE covid_tracker;

CREATE TABLE IF NOT EXISTS patients (
    patient_id VARCHAR(50) PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    status VARCHAR(50) NOT NULL,
    temperature DECIMAL(4,1) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_first_name ON patients(first_name);
CREATE INDEX idx_last_name ON patients(last_name);
CREATE INDEX idx_status ON patients(status);
CREATE INDEX idx_email ON patients(email);
