-- ENUMS

create table cart_status(
    enum_value varchar(64) primary key
);
insert into cart_status values ('ACTIVE');
insert into cart_status values ('PROCESSED');
insert into cart_status values ('CANCELLED');

create table payment_status(
    enum_value varchar(64) primary key
);
insert into payment_status values ('SUCCESSFUL');
insert into payment_status values ('PENDING');
insert into payment_status values ('FAILED');

create table product_status(
    enum_value varchar(64) primary key
);
insert into product_status values ('AVAILABLE');
insert into product_status values ('RESERVED');
insert into product_status values ('SOLD');

create table product_type(
    enum_value varchar(64) primary key
);
insert into product_type values ('PRODUCT');
insert into product_type values ('DESKTOP');
insert into product_type values ('LAPTOP');
insert into product_type values ('MOBILE');

create table user_type(
    enum_value varchar(64) primary key
);
insert into user_type values ('WEB_USER');
insert into user_type values ('ADMIN');

-- TABLES

create table users(
    id int auto_increment primary key,
    username varchar(256) unique not null,
    password varchar(512) not null,
    user_type varchar(64),
    foreign key (user_type) references user_type (enum_value)
);

create table carts(
    id int auto_increment primary key,
    user_id int not null,
    last_update_time timestamp not null,
    cart_status varchar(64),
    foreign key (user_id) references users(id),
    foreign key (cart_status) references cart_status (enum_value)
);

create table products(
    id int auto_increment primary key,
    name varchar(256) not null,
    price decimal(15, 3) not null,
    product_type varchar(64),
    product_status varchar(64),
    check (price > 0),
    foreign key (product_type) references product_type (enum_value),
    foreign key (product_status) references product_status (enum_value)
);

create table carts_products(
    cart_id int,
    product_id int,
    foreign key(cart_id) references carts(id),
    foreign key(product_id) references products(id),
    primary key (cart_id, product_id)
);

create table payments(
    id int auto_increment primary key,
    user_id int not null,
    cart_id int not null,
    amount decimal(15, 3) not null,
    last_update_time timestamp not null,
    payment_status varchar(64),
    foreign key (user_id) references users(id),
    foreign key (cart_id) references carts(id),
    foreign key (payment_status) references payment_status (enum_value)
);

create table cards(
    id int auto_increment primary key,
    number varchar(16) unique not null,
    password varchar(6) not null,
    expiry_date date not null,
    balance decimal(15, 3) not null
);

-- Initial Data
insert into users values (0, 'admin', 'nn9EysQOvC9Dm5LwpdhgBkrLj6eUwm0E+gxvTU8ZSaA=:4ZFmj0Gd+Ly8zb/okH5AM+kFpraRJrY4qIBT9PKP164=', 'ADMIN');