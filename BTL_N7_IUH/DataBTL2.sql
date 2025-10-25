-- ===========================================
-- BƯỚC 1: CÀI ĐẶT DATABASE VÀ BẢNG
-- ===========================================

-- 1. TẠO VÀ CHỌN DATABASE
CREATE DATABASE IF NOT EXISTS frubana_db;
USE frubana_db;

-- 2. Bảng ROLES (Phân quyền)
CREATE TABLE Roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE 
);

-- 3. Bảng ACCOUNTS (Tài khoản người dùng)
CREATE TABLE Accounts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role_id BIGINT,
    is_enabled BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (role_id) REFERENCES Roles(id)
);

-- 4. Bảng ACCOUNT_DETAILS (Thông tin chi tiết)
CREATE TABLE Account_Details (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_id BIGINT UNIQUE,
    full_name VARCHAR(255),
    phone_number VARCHAR(15),
    address VARCHAR(255),
    FOREIGN KEY (account_id) REFERENCES Accounts(id)
);

-- 5. Bảng CATEGORIES (Danh mục sản phẩm)
CREATE TABLE Categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    slug VARCHAR(100) UNIQUE,
    description TEXT
);

-- 6. Bảng PRODUCTS (Sản phẩm)
CREATE TABLE Products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    sale_price DECIMAL(10, 2),
    image_url VARCHAR(255),
    description TEXT,
    category_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES Categories(id)
);

-- 7. Bảng ORDER_STATUSES (Trạng thái đơn hàng)
CREATE TABLE Order_Statuses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    status_name VARCHAR(50) NOT NULL UNIQUE
);

-- 8. Bảng ORDERS (Đơn hàng)
CREATE TABLE Orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_id BIGINT,
    order_status_id BIGINT,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    shipping_address VARCHAR(255),
    payment_method VARCHAR(50),
    FOREIGN KEY (account_id) REFERENCES Accounts(id),
    FOREIGN KEY (order_status_id) REFERENCES Order_Statuses(id)
);

-- 9. Bảng ORDER_DETAILS (Chi tiết đơn hàng)
CREATE TABLE Order_Details (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT,
    product_id BIGINT,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(id),
    FOREIGN KEY (product_id) REFERENCES Products(id)
);

-- 10. Bảng COMMENTS (Bình luận/Đánh giá sản phẩm)
CREATE TABLE Comments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT,
    account_id BIGINT,
    content TEXT,
    rating INT, -- Từ 1 đến 5
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES Products(id),
    FOREIGN KEY (account_id) REFERENCES Accounts(id)
);

-- 11. Bảng CONTACTS (Liên hệ từ khách hàng)
CREATE TABLE Contacts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(255),
    email VARCHAR(100),
    subject VARCHAR(255),
    message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 12. Bảng BANNERS (Quản lý banner/slide)
CREATE TABLE Banners (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    image_url VARCHAR(255) NOT NULL,
    title VARCHAR(255),
    link_url VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE
);


-- ===========================================
-- BƯỚC 2: CHÈN DỮ LIỆU MẪU (DUMMY DATA)
-- ===========================================

-- 1. Dữ liệu Roles
INSERT INTO Roles (id, name) VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_USER');

-- 2. Dữ liệu Order Statuses
INSERT INTO Order_Statuses (id, status_name) VALUES 
(1, 'PENDING'), 
(2, 'SHIPPED'), 
(3, 'DELIVERED'), 
(4, 'CANCELLED');

-- 3. Dữ liệu Accounts (Sử dụng mật khẩu đã mã hóa 'admin' và 'user')
-- Password 'admin' (BCrypt: $2a$10$wK1Wd4sF4vj5t8M0Vq9qUe1T8nN2M.3K2Q9vO5hP0r.H8S2gJ6k.a)
INSERT INTO Accounts (id, username, password, email, role_id) VALUES 
(1, 'admin', '$2a$10$wK1Wd4sF4vj5t8M0Vq9qUe1T8nN2M.3K2Q9vO5hP0r.H8S2gJ6k.a', 'admin@frubana.com', 1),
(2, 'user', '$2a$10$wK1Wd4sF4vj5t8M0Vq9qUe1T8nN2M.3K2Q9vO5hP0r.H8S2gJ6k.a', 'user@frubana.com', 2);

-- 4. Dữ liệu Account Details
INSERT INTO Account_Details (account_id, full_name, phone_number, address) VALUES
(1, 'Nguyễn Văn Admin', '0987654321', '235 Hoàng Quốc Việt, Hà Nội'),
(2, 'Trần Thị Khách', '0912345678', '45 Lê Lợi, TP.HCM');

-- 5. Dữ liệu Categories
INSERT INTO Categories (id, name, slug) VALUES 
(1, 'Trái cây', 'trai-cay'), 
(2, 'Thịt Tươi', 'thit-tuoi'), 
(3, 'Rau Hữu Cơ', 'rau-huu-co'),
(4, 'Đồ khô', 'do-kho');

-- 6. Dữ liệu Products (Dữ liệu hiển thị trên trang chủ)
INSERT INTO Products (id, name, price, sale_price, image_url, description, category_id) VALUES
(1, 'Dâu tây Đà Lạt', 65000.00, 59000.00, 'dautay.jpg', 'Dâu tây là loại trái cây mọng nước, có hình tròn hoặc oval, thường mọc thành chùm. Vị ngọt, có lúc chua nhẹ.', 1),
(2, 'Nho Tươi Đen Mỹ', 78000.00, 70000.00, 'nhotuoi.jpg', 'Nho là loại trái cây mọng nước, có hình tròn hoặc oval, thường mọc thành chùm. Vị ngọt, thường có màu tím hoặc xanh.', 1),
(3, 'Xoài Cát Chu', 76000.00, NULL, 'xoai.jpg', 'Xoài là loại trái cây nhiệt đới có hình bầu dục, vỏ ngoài mỏng với màu xanh, vàng hoặc đỏ tùy thị.', 1),
(4, 'Quýt Ngọt Úc', 76000.00, 72000.00, 'quytngot.jpg', 'Quýt là loại trái cây có vỏ mỏng, dễ bóc, thường có màu cam sáng và mùi thơm dịu nhẹ.', 1),
(5, 'Cà Chua Hữu Cơ', 35000.00, NULL, 'cachua.jpg', 'Cà chua được trồng hữu cơ, đảm bảo an toàn thực phẩm.', 3),
(6, 'Thịt Bò Thăn Nội', 250000.00, 235000.00, 'thitbo.jpg', 'Thịt bò thăn nội tươi, mềm, thích hợp làm bít tết.', 2);

-- 7. Dữ liệu Banners
INSERT INTO Banners (image_url, title, link_url, is_active) VALUES
('banner1.jpg', '100% Thực phẩm hữu cơ', '/client/products', TRUE),
('banner2.jpg', 'Khuyến mãi cuối tuần', '/client/sale', TRUE);

-- 8. Dữ liệu Orders (Đơn hàng mẫu)
INSERT INTO Orders (account_id, order_status_id, total_amount, shipping_address, payment_method) VALUES
(2, 3, 139000.00, '45 Lê Lợi, TP.HCM', 'COD'), -- Đơn hàng đã giao
(2, 1, 70000.00, '45 Lê Lợi, TP.HCM', 'VNPAY');  -- Đơn hàng chờ xử lý

-- 9. Dữ liệu Order Details (Chi tiết đơn hàng)
INSERT INTO Order_Details (order_id, product_id, quantity, price) VALUES
(1, 1, 1, 59000.00), -- 1 Dâu Tây
(1, 4, 1, 72000.00), -- 1 Quýt Ngọt
(2, 2, 1, 70000.00); -- 1 Nho Tươi

-- 10. Dữ liệu Comments
INSERT INTO Comments (product_id, account_id, content, rating) VALUES
(1, 2, 'Dâu rất tươi và ngọt. Giao hàng nhanh!', 5),
(2, 2, 'Nho ngon, nhưng hơi bị dập nhẹ một vài chùm.', 4);