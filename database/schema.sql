-- ============================================================
-- Bank Management System - Database Schema (MySQL)
-- ============================================================
-- NOTE: You do NOT have to run this file by hand.
-- The Spring Boot app uses "spring.jpa.hibernate.ddl-auto=update"
-- which automatically creates these tables the first time it runs.
--
-- This file is provided just so you can see the table structure,
-- or run it manually if you prefer to create tables yourself.
-- ============================================================

CREATE DATABASE IF NOT EXISTS bank_db;
USE bank_db;

-- Login credentials + role
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL -- 'ADMIN' or 'USER'
);

-- Personal profile details
CREATE TABLE IF NOT EXISTS customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(50),
    address VARCHAR(255),
    user_id BIGINT UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- The actual bank account & balance
CREATE TABLE IF NOT EXISTS accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(50) NOT NULL UNIQUE,
    account_type VARCHAR(50) NOT NULL,
    balance DOUBLE NOT NULL DEFAULT 0,
    customer_id BIGINT UNIQUE,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- Deposit / Withdraw history
CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(20) NOT NULL, -- 'DEPOSIT' or 'WITHDRAW'
    amount DOUBLE NOT NULL,
    balance_after DOUBLE NOT NULL,
    transaction_date DATETIME NOT NULL,
    account_id BIGINT NOT NULL,
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);

-- A default admin user is auto-created by the app on first run:
--   username: admin
--   password: admin123
-- (you can change these in backend/src/main/resources/application.properties)
