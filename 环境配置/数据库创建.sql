CREATE TABLE user
(
    user_id       INT AUTO_INCREMENT PRIMARY KEY,
    openid        VARCHAR(50)                NOT NULL UNIQUE,             -- 微信用户唯一标识
    nickname      VARCHAR(50)                NOT NULL,                    -- 微信用户昵称
    avatar_url    VARCHAR(255),                                           -- 微信用户头像URL
    role          ENUM ('admin', 'customer') NOT NULL DEFAULT 'customer', -- 区分用户角色
    phone_number  VARCHAR(20),                                             -- 用户手机号
    register_date TIMESTAMP                           DEFAULT CURRENT_TIMESTAMP,
    last_login    TIMESTAMP                           DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE category
(
    category_id   INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL,
    description   TEXT
);

CREATE TABLE dish
(
    dish_id     INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT,
    dish_name   VARCHAR(100)   NOT NULL,
    price       DECIMAL(10, 2) NOT NULL,
    description TEXT,
    image_url   VARCHAR(255),
    status      ENUM ('available', 'unavailable') DEFAULT 'available',
    FOREIGN KEY (category_id) REFERENCES category (category_id)
);

CREATE TABLE `order`
(
    order_id     INT AUTO_INCREMENT PRIMARY KEY,
    user_id      INT,
    order_date   TIMESTAMP                                          DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    order_status ENUM ('pending', 'paid', 'completed', 'cancelled') DEFAULT 'pending',
    FOREIGN KEY (user_id) REFERENCES user (user_id)
);

CREATE TABLE order_detail
(
    order_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id        INT,
    dish_id         INT,
    quantity        INT            NOT NULL, -- 购买数量
    subtotal        DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES `order` (order_id),
    FOREIGN KEY (dish_id) REFERENCES dish (dish_id)
);

CREATE TABLE feedback
(
    feedback_id      INT AUTO_INCREMENT PRIMARY KEY,
    user_id          INT,
    order_id         INT,
    feedback_content TEXT NOT NULL,
    feedback_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    rating           INT CHECK (rating >= 1 AND rating <= 5), -- 评分字段，1到5分
    FOREIGN KEY (user_id) REFERENCES user (user_id),
    FOREIGN KEY (order_id) REFERENCES `order` (order_id)
);

CREATE TABLE carousel
(
    carousel_id INT AUTO_INCREMENT PRIMARY KEY,
    image_url   VARCHAR(255) NOT NULL,
    title       VARCHAR(100),
    link        VARCHAR(255),
    `order`     INT          NOT NULL -- 轮播图的显示顺序
);
