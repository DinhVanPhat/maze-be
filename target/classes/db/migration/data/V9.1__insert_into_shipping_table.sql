-- Thêm 5 dữ liệu vào bảng shipping
INSERT INTO shipping (order_id, address, city, postal_code, tracking_number, carrier, shipped_at, delivered_at) VALUES
    (1, '123 Đường ABC', 'Hà Nội', '100000', 'GHN123456', 'GHN', NOW(), NULL),
    (2, '456 Đường DEF', 'TP.HCM', '700000', 'GHN654321', 'GHN', NOW(), NOW()),
    (3, '789 Đường GHI', 'Đà Nẵng', '550000', 'GHN789456', 'GHN', NOW(), NULL),
    (4, '321 Đường JKL', 'Hải Phòng', '180000', 'GHN321789', 'GHN', NOW(), NOW()),
    (5, '654 Đường MNO', 'Cần Thơ', '900000', 'GHN987123', 'GHN', NOW(), NULL);