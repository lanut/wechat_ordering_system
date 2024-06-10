INSERT INTO user (openid, nickname, avatar_url, role)
VALUES ('wx1234567890', '张三', 'http://example.com/avatar1.jpg', 'customer'),
       ('wx0987654321', '李四', 'http://example.com/avatar2.jpg', 'customer'),
       ('wx1122334455', '王五', 'http://example.com/avatar3.jpg', 'admin');

INSERT INTO category (category_name, description)
VALUES ('主食', '各种主食类菜品'),
       ('小吃', '各种小吃类菜品'),
       ('饮料', '各种饮料');

INSERT INTO dish (category_id, dish_name, price, description, image_url, status)
VALUES (1, '红烧牛肉面', 25.00, '美味的红烧牛肉面', 'http://example.com/dish1.jpg', 'available'),
       (1, '扬州炒饭', 18.00, '经典的扬州炒饭', 'http://example.com/dish2.jpg', 'available'),
       (2, '炸鸡翅', 15.00, '香脆的炸鸡翅', 'http://example.com/dish3.jpg', 'available'),
       (3, '可乐', 5.00, '冰爽的可乐', 'http://example.com/dish4.jpg', 'available');

INSERT INTO `order` (user_id, total_amount, order_status)
VALUES (1, 43.00, 'paid'),
       (2, 20.00, 'completed');

INSERT INTO order_detail (order_id, dish_id, quantity, subtotal)
VALUES (1, 1, 1, 25.00),
       (1, 2, 1, 18.00),
       (2, 3, 1, 15.00),
       (2, 4, 1, 5.00);

INSERT INTO feedback (user_id, order_id, feedback_content, rating)
VALUES (1, 1, '菜品非常美味，配送也很快！', 5),
       (2, 2, '小吃不错，但饮料有点温了。', 4);

INSERT INTO carousel (image_url, title, link, `order`)
VALUES ('http://example.com/carousel1.jpg', '夏日特惠', 'http://example.com/promo1', 1),
       ('http://example.com/carousel2.jpg', '新品上市', 'http://example.com/promo2', 2);
