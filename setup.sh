#!/bin/bash

# Install MySQL server
sudo apt-get install mysql-common

# Start MySQL service
sudo service mysql start

# Log in to MySQL as root and execute SQL scripts
sudo mysql -u root <<'END_SQL'
-- Create a user
CREATE USER IF NOT EXISTS 'BloodBank'@'localhost' IDENTIFIED BY 'BloodBank';
GRANT ALL PRIVILEGES ON *.* TO 'BloodBank'@'localhost' WITH GRANT OPTION;
FLUSH PRIVILEGES;

-- Create the database
CREATE DATABASE IF NOT EXISTS BloodBank;
USE BloodBank;

-- Create the staff table
CREATE TABLE IF NOT EXISTS staff (
    staff_id INT PRIMARY KEY,
    staff_name VARCHAR(255) NOT NULL,
    position VARCHAR(255),
    contact_number VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    address VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME
);

-- Insert an initial admin into the staff table
INSERT INTO staff (staff_id, staff_name, position, contact_number, email, address, password, enabled, created_at, updated_at)
VALUES (2,'Admin','Manager','0000000000','Admin@gmail.com','AdminAddress','$2a$12$trsHDy6FeI6LiM/UAG6kUuFtY8dTA7ImKmU7AhXk56bthbXAeYIwa', true, NOW(), null);
END_SQL

# Restart MySQL service to apply changes
sudo service mysql restart

# Run database migrations or initialize the schema
# Adjust this based on your project structure and build tools
mvn clean install

# Start your application
mvn spring-boot:run