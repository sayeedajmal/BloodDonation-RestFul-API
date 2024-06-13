-- init.sql
CREATE USER IF NOT EXISTS 'BloodBank'@'%' IDENTIFIED BY 'BloodBank';
GRANT ALL PRIVILEGES ON *.* TO 'BloodBank'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

CREATE DATABASE IF NOT EXISTS BloodBank;

USE BloodBank;

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

INSERT INTO staff (staff_id, staff_name, position, contact_number, email, address, password, enabled, created_at, updated_at)
VALUES (2,'Admin','Manager','0000000000','Admin@gmail.com','AdminAddress','$2a$12$trsHDy6FeI6LiM/UAG6kUuFtY8dTA7ImKmU7AhXk56bthbXAeYIwa', true, NOW(), null);
