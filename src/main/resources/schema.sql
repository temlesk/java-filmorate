create table  if not exists users (
    user_id bigint auto_increment primary key,
    email varchar(255) not null ,
    login varchar(100) not null ,
    name varchar(255),
    birthday date not null
);

create table  if not exists films (
    id bigint auto_increment primary key,
    name varchar(255) not null ,
    description varchar(200),
    release_date date,
    duration int check (duration > 0)
);

create table if not exists genres (
    genre_id bigint auto_increment primary key,
    genre_name varchar(100)
);

create table if not exists rating (
    rating_id bigint auto_increment primary key,
    rating_name varchar(100),
    rating_description varchar(150)
);

create table if not exists film_genres (
    genre_id bigint auto_increment primary key,
    film_id int
);

create table if not exists friendships (
    user_id bigint auto_increment primary key not null ,
    friend_id bigint auto_increment primary key not null,
    confirmed boolean default false,
    foreign key (user_id) references users(user_id)
);

create table if not exists film_likes (
    film_likes_id bigint auto_increment primary key not null ,
    film_id int not null,
    user_id int not null,
    liked_at date not null
);

insert into users (user_id, email, login, name, birthday)
values ( 1,'admintema.leshkin@gmail.com', 'temlesk', 'Tema', '2005-05-14'),
       (2, 'usertema.leshkingmail.com', 'userTema', 'Meme', '2000-01-01');

insert into films (id, name, description, release_date, duration)
values ( 1, 'Terminator', 'XZ', '2005-05-14', 60),
       (2, 'Java', 'db', 2025-22-04);

insert into rating (rating_id, rating_name, rating_description)
values (1, 'G', 'Все возрастные группы допущены. Материал подходит для детей.'),
       (2, 'PG', 'Рекомендуется просмотр с родителями. Некоторые материалы могут не подойти для детей.'),
       (3, 'PG-13', ' Настоятельное предостережение родителям. Некоторые материалы могут быть не подходят для детей до 13 лет.'),
       (4, 'R', ' Лица, не достигшие 17 лет, допускаются на фильм только в сопровождении одного из родителей или законного представителя.'),
       (5, 'NC-17', 'Лица 17 лет и младше на фильм не допускаются. Это строго взрослый рейтинг.');

insert into genres (genre_id, genre_name)
values (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Боевик'),
       (4, 'Триллер'),
       (5, 'Ужасы'),
       (6, 'Фантастика'),
       (7, 'Фэнтези'),
       (8, 'Мелодрама'),
       (9, 'Детектив'),
       (10, 'Приключения');

insert into film_genres (film_id, genre_id)
values (1,2 ), (1, 3),
       (2, 4), (2, 5);