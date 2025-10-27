DROP TABLE IF EXISTS products;

CREATE TABLE products
(
    product_id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_name     VARCHAR(255) NOT NULL,
    description      TEXT         NOT NULL,
    image_src        VARCHAR(512),
    quantity_state   VARCHAR(16)  NOT NULL,
    product_state    VARCHAR(16)  NOT NULL,
    product_category VARCHAR(16)  NOT NULL,
    price            FLOAT        NOT NULL
);