create table users(
    id int auto_increment primary key,
    username varchar(255) unique not null,
    password varchar(255) not null,
    type varchar(50) not null,
    check (type in ('WEB_USER', 'ADMIN'))
);

create table carts(
    id int auto_increment primary key,
    user_id int not null,
    foreign key (user_id) references users(id)
);
-- maybe add an "active" attr. later if you want to record past carts

create table carts_products(
    cart_id int,
    product_id int,
    count int,
    primary key (cart_id, product_id, count),
    check (count > 0)
);

create table products(
    id int auto_increment primary key,
    name varchar(255),
    price decimal(15, 3) not null,
    count int not null,
    check (price > 0 and count > 0)
);

insert into users(ID, USERNAME, PASSWORD, TYPE) values (0, 'admin', 'admin', 'admin');