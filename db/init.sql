create table if not exists users(
    id uuid primary key,
    username varchar(255) unique not null,
    password varchar(255) not null
);

create table if not exists carts(
    id uuid primary key,
    user_id uuid not null,
    foreign key (user_id) references users(id)
);
-- maybe add an "active" attr. if you want to record past carts

create table if not exists carts_products(
    cart_id uuid,
    product_id uuid,
    count int,
    primary key (cart_id, product_id, count),
    check (count > 0)
);

create table if not exists products(
    id uuid primary key,
    name varchar(255),
    price decimal(15, 3) not null,
    count int not null,
    check (price > 0 and count > 0)
);