INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO USER_ROLES (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (name)
VALUES ('Sakhalin'),
       ('Terrassa'),
       ('Mansarda'),
       ('Katyusha');

INSERT INTO DISH (name, price, restaurant_id)
VALUES ('Sakhalin fish soup', 900, 1),
       ('Grilled dorado', 1050, 1),
       ('Crab with avocado, cucumbers and tomatoes', 900, 1),
       ('Mango and grilled tuna salad', 780, 2),
       ('Andalusian gazpacho', 860, 2),
       ('Duck breast with yams', 1300, 2),
       ('Greek salad', 890, 3),
       ('Ribeye', 1500, 3),
       ('Tom Yum with seafood', 990, 3),
       ('Okroshka with kvass', 800, 4),
       ('Russian salad', 750, 4),
       ('Chicken with potatoes', 950, 4);