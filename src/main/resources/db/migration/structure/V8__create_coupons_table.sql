-- 8. Bảng coupons (Mã giảm giá)
CREATE TABLE coupons (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    discount DECIMAL(10, 2) NOT NULL,
    min_order_value DECIMAL(10, 2),
    max_uses INT DEFAULT 1,
    expires_at DATETIME,
    status ENUM('active', 'inactive') DEFAULT 'active'
);