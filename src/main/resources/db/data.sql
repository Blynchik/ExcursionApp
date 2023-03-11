insert into users(name, email, phone_number, password, role)
values ('User', 'user@yandex.ru', '9006005040', '{noop}password', 'ROLE_USER'),
       ('Admin', 'admin@mail.ru', '9007008060', '{noop}password', 'ROLE_ADMIN');

insert into excursion(name, date, description, price)
values ('Памуккале', current_date + 10, '', '450'),
       ('Мира', current_date + 13, '', '350');

insert into users_excursion(users_id, excursion_id)
values (1, 1),
       (1, 2),
       (2, 1);