-- 1. Bảng users (Người mua/người bán)
CREATE TABLE accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fullname NVARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    gender TINYINT(1),
    birth_date DATETIME,
    phone_number VARCHAR(15) UNIQUE NOT NULL,
    role ENUM('user', 'seller', 'admin') DEFAULT 'user',
    avatar_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);