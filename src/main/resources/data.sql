insert into rating (id, name, description) values
    (1, 'G', 'Все возрастные группы допущены. Материал подходит для детей.'),
    (2, 'PG', 'Рекомендуется просмотр с родителями. Некоторые материалы могут не подойти для детей.'),
    (3, 'PG-13', 'Настоятельное предостережение родителям. Некоторые материалы могут быть не подходят для детей до 13 лет.'),
    (4, 'R', 'Лица, не достигшие 17 лет, допускаются на фильм только в сопровождении одного из родителей или законного представителя.'),
    (5, 'NC-17', 'Лица 17 лет и младше на фильм не допускаются. Это строго взрослый рейтинг.');

insert into genres (id, name) values
    (1, 'Комедия'),
    (2, 'Драма'),
    (3, 'Мультфильм'),
    (4, 'Триллер'),
    (5, 'Документальный'),
    (6, 'Боевик');

insert into users (email, login, name, birthday) values
    ('admintema.leshkin@gmail.com', 'temlesk', 'Tema', '2005-05-14'),
    ('usertema.leshkingmail.com', 'userTema', 'Meme', '2000-01-01'),
    ('usertema.leshkingmail1.com', 'userTema1', 'Tema1', '2005-01-01'),
    ('usertema.leshkingmail2.com', 'userTema2', 'Tema2', '2004-01-01');

insert into films (name, description, release_date, duration, rating_id) values
    ('Terminator', 'XZ', '2005-05-14', 60, 3),
    ('Java', 'db', '2025-04-22', 120, 1);

insert into film_genres (film_id, genre_id) values
    (1, 2), (1, 3),
    (2, 4), (2, 5);