create table users(
    id int auto_increment primary key,
    username varchar(256) unique not null,
    password varchar(512) not null,
    user_type varchar(64),
    balance decimal(15, 3) not null,
    check (user_type in ('WEB_USER', 'ADMIN'))
);

create table carts(
    id int auto_increment primary key,
    user_id int not null,
    last_update_time timestamp not null,
    cart_status varchar(64),
    foreign key (user_id) references users(id),
    check (cart_status in ('ACTIVE', 'PROCESSED'))
);

create table products(
     id int auto_increment primary key,
     name varchar(256) unique,
     price decimal(15, 3) not null,
     product_type varchar(64),
     product_status varchar(64),
     check (price > 0),
     check (product_type in ('PRODUCT', 'DESKTOP', 'LAPTOP', 'MOBILE')),
     check (product_status in ('AVAILABLE', 'RESERVED', 'SOLD'))
);

create table carts_products(
    cart_id int,
    product_id int,
    foreign key(cart_id) references carts(id),
    foreign key(product_id) references products(id),
    primary key (cart_id, product_id)
);