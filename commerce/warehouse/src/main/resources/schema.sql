DROP TABLE IF EXISTS product_in_warehouse;
DROP TABLE IF EXISTS order_booking;
DROP TABLE IF EXISTS order_booking_items;

CREATE TABLE product_in_warehouse
(
    id         BIGSERIAL PRIMARY KEY,
    product_id UUID UNIQUE      NOT NULL,
    weight     DOUBLE PRECISION NOT NULL,
    fragile    BOOLEAN                   DEFAULT FALSE,
    width      DOUBLE PRECISION NOT NULL,
    height     DOUBLE PRECISION NOT NULL,
    depth      DOUBLE PRECISION NOT NULL,
    quantity   BIGINT           NOT NULL DEFAULT 0
);

CREATE TABLE order_booking
(
    order_id    UUID PRIMARY KEY,
    delivered   BOOLEAN NOT NULL DEFAULT FALSE,
    delivery_id UUID
);

CREATE TABLE order_booking_items
(
    order_id   UUID   NOT NULL REFERENCES order_booking (order_id) ON DELETE CASCADE,
    product_id UUID   NOT NULL,
    quantity   BIGINT NOT NULL,

    PRIMARY KEY (order_id, product_id)
);

