INSERT INTO user (username, password, role, email, phone_number)
VALUES ('admin1', 'password123', 'admin', 'admin1@example.com', '1234567890'),
       ('customer1', 'password123', 'customer', 'customer1@example.com', '0987654321'),
       ('customer2', 'password123', 'customer', 'customer2@example.com', '1122334455');

INSERT INTO category (category_name, description)
VALUES ('Appetizers', 'Starter dishes to begin your meal'),
       ('Main Courses', 'Hearty and filling main dishes'),
       ('Desserts', 'Sweet treats to end your meal'),
       ('Beverages', 'Refreshing drinks to accompany your meal');

INSERT INTO dish (category_id, dish_name, price, description, image_url, status)
VALUES (1, 'Spring Rolls', 5.99, 'Crispy rolls filled with vegetables', 'http://example.com/spring_rolls.jpg',
        'available'),
       (1, 'Garlic Bread', 3.99, 'Toasted bread with garlic and butter', 'http://example.com/garlic_bread.jpg',
        'available'),
       (2, 'Grilled Chicken', 12.99, 'Juicy grilled chicken with herbs', 'http://example.com/grilled_chicken.jpg',
        'available'),
       (2, 'Steak', 19.99, 'Tender steak cooked to perfection', 'http://example.com/steak.jpg', 'available'),
       (3, 'Cheesecake', 6.99, 'Creamy cheesecake with a graham cracker crust', 'http://example.com/cheesecake.jpg',
        'available'),
       (3, 'Ice Cream', 4.99, 'Rich and creamy ice cream', 'http://example.com/ice_cream.jpg', 'available'),
       (4, 'Lemonade', 2.99, 'Refreshing lemonade', 'http://example.com/lemonade.jpg', 'available'),
       (4, 'Coffee', 1.99, 'Hot brewed coffee', 'http://example.com/coffee.jpg', 'available');

INSERT INTO `order` (user_id, total_amount, order_status)
VALUES (2, 25.97, 'completed'),
       (3, 19.98, 'pending');

INSERT INTO order_detail (order_id, dish_id, quantity, subtotal)
VALUES (1, 1, 1, 5.99),
       (1, 3, 2, 25.98),
       (2, 4, 1, 19.99);

INSERT INTO feedback (user_id, dish_id, feedback_content)
VALUES (2, 1, 'The spring rolls were delicious and crispy!'),
       (3, 4, 'The steak was cooked perfectly and very tender.');

INSERT INTO carousel (image_url, title, link, `order`)
VALUES ('http://example.com/carousel1.jpg', 'Welcome to Our Restaurant', 'http://example.com', 1),
       ('http://example.com/carousel2.jpg', 'Try Our New Dishes', 'http://example.com/new', 2);
