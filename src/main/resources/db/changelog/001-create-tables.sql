--liquibase formatted sql

--changeset author:admin@localhost:1-create-users-table
CREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(100) NOT NULL,
    email       VARCHAR(200) NOT NULL,
    city        VARCHAR(100),
    created_at  TIMESTAMP DEFAULT NOW()
);

--changeset author:admin@localhost:2-create-products-table
CREATE TABLE products (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    category    VARCHAR(100),
    price       NUMERIC(10, 2) NOT NULL
);

--changeset author:admin@localhost:3-create-orders-table
CREATE TABLE orders (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT REFERENCES users(id),
    status      VARCHAR(20) NOT NULL,
    created_at  TIMESTAMP DEFAULT NOW()
);

--changeset author:admin@localhost:4-create-order-items-table
CREATE TABLE order_items (
    id          BIGSERIAL PRIMARY KEY,
    order_id    BIGINT REFERENCES orders(id),
    product_id  BIGINT REFERENCES products(id),
    quantity    INT NOT NULL,
    total_price NUMERIC(10, 2) NOT NULL
);
