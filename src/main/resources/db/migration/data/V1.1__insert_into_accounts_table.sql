-- Thêm người dùng mẫu
INSERT INTO accounts (fullname, password, email, gender, birth_date, phone_number, role) 
VALUES ('Phát User', '123456', 'phatuser@example.com', 1, '2004-01-01', '0987654321', 'user'),
    ('Phát Admin', '123456', 'phatadmin@example.com', 1, '2004-01-01','0702819466', 'admin'),
    ('Seller 1', '123456', 'seller1@example.com', 0, '2004-01-01','0987654323', 'seller'),
    ('Seller 2', '123456', 'seller2@example.com', 1, '2004-01-01','0987654324', 'seller'),
    ('Seller 3', '123456', 'seller3@example.com', 0, '2004-01-01','0987654325', 'seller'),
    ('Seller 4', '123456', 'seller4@example.com', 1, '2004-01-01','0987654326', 'seller'),
    ('Seller 5', '123456', 'seller5@example.com', 0, '2004-01-01','0987654327', 'seller');