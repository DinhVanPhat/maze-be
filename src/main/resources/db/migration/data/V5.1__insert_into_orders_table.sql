-- Tạo đơn hàng mẫu
INSERT INTO orders (accounts_id, total_price, status, payment_id) 
VALUES 
    (1, 58000000, 'pending', 1),
    (2, 45000000, 'processing', 2),
    (3, 32000000, 'shipped', 4),
    (4, 15000000, 'delivered', 4),
    (5, 27000000, 'pending', 3),
    (1, 62000000, 'cancelled', 1);