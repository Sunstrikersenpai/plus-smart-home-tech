DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS order_products;

CREATE TABLE orders
(
    order_id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    shopping_cart_id UUID,
    username         VARCHAR(100),
    payment_id       UUID,
    delivery_id      UUID,
    state            VARCHAR(32) NOT NULL,
    delivery_weight  DOUBLE PRECISION,
    delivery_volume  DOUBLE PRECISION,
    fragile          BOOLEAN DEFAULT FALSE,
    total_price      DOUBLE PRECISION,
    delivery_price   DOUBLE PRECISION,
    product_price    DOUBLE PRECISION
);

CREATE TABLE order_products
(
    order_id   UUID REFERENCES orders (order_id) ON DELETE CASCADE,
    product_id UUID,
    quantity   BIGINT,
    PRIMARY KEY (order_id, product_id)
)