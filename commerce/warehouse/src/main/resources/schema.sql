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
