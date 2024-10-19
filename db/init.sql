create table users(
    id int auto_increment primary key,
    username varchar(256) unique not null,
    password varchar(512) not null,
    type varchar(64) not null,
    check (type in ('WEB_USER', 'ADMIN'))
);

create table carts(
    id int auto_increment primary key,
    user_id int not null,
    process_time datetime,
    foreign key (user_id) references users(id)
);

create table products(
     id int auto_increment primary key,
     name varchar(256) unique,
     price decimal(15, 3) not null,
     count int not null,
     check (price > 0),
     check (count > 0)
);

create table carts_products(
    cart_id int,
    product_id int,
    count int,
    foreign key(cart_id) references carts(id),
    foreign key(product_id) references products(id) on delete cascade,
    primary key (cart_id, product_id),
    check (count > 0)
);