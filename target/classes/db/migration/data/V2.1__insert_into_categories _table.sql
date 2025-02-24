INSERT INTO categories (name, description, parent_id) VALUES
('Điện tử', 'Danh mục các thiết bị điện tử', NULL),
('Thời trang', 'Danh mục các sản phẩm thời trang', NULL),
('Gia dụng', 'Danh mục các sản phẩm gia dụng', NULL),
('Sách', 'Danh mục các loại sách', NULL),
('Thể thao', 'Danh mục các sản phẩm thể thao', NULL),

-- Điện tử (id = 1)
('Điện thoại', 'Các loại điện thoại thông minh', 1),
('Laptop', 'Máy tính xách tay các hãng', 1),
('Tablet', 'Máy tính bảng', 1),
('Tivi', 'Tivi các loại', 1),
('Âm thanh', 'Thiết bị âm thanh', 1),

-- Thời trang (id = 2)
('Nam', 'Thời trang nam', 2),
('Nữ', 'Thời trang nữ', 2),
('Trẻ em', 'Thời trang trẻ em', 2),
('Giày dép', 'Các loại giày dép', 2),
('Phụ kiện', 'Phụ kiện thời trang', 2),

-- Gia dụng (id = 3)
('Nhà bếp', 'Dụng cụ nhà bếp', 3),
('Điện gia dụng', 'Thiết bị điện gia dụng', 3),
('Nội thất', 'Sản phẩm nội thất', 3),
('Vệ sinh', 'Dụng cụ vệ sinh nhà cửa', 3),
('Ngoài trời', 'Sản phẩm sử dụng ngoài trời', 3),

-- Sách (id = 4)
('Văn học', 'Sách văn học', 4),
('Khoa học', 'Sách khoa học', 4),
('Giáo trình', 'Sách giáo trình học tập', 4),
('Kinh tế', 'Sách kinh tế', 4),
('Thiếu nhi', 'Sách thiếu nhi', 4),

-- Thể thao (id = 5)
('Dụng cụ', 'Dụng cụ thể thao', 5),
('Thời trang thể thao', 'Quần áo thể thao', 5),
('Thể thao ngoài trời', 'Sản phẩm thể thao ngoài trời', 5),
('Thể thao trong nhà', 'Sản phẩm thể thao trong nhà', 5),
('Phụ kiện thể thao', 'Phụ kiện hỗ trợ thể thao', 5);