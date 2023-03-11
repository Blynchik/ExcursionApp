insert into users(name, email, phone_number, password, role)
values ('User', 'user@yandex.ru', '9006005040', '{noop}password', 'ROLE_USER'),
       ('Admin', 'admin@mail.ru', '9007008060', '{noop}password', 'ROLE_ADMIN');

insert into excursion(name, date, description, price)
values ('Памуккале', current_timestamp, '', '450'),
       ('Мира', current_timestamp, '', '350');

insert into users_excursion(users_id, excursion_id)
values (1, 1),
       (1, 2),
       (2, 1);

insert into comment(message, created_at, users_id, excursion_id)
values ('Понравилось', current_date + 10, 1, 1),
       ('Не понравилось', current_date + 13, 2, 2);