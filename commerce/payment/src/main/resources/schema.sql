DROP TABLE IF EXISTS payment;

CREATE TABLE payments
(
    payment_id     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id       UUID,
    total_payment  DOUBLE PRECISION,
    delivery_total DOUBLE PRECISION,
    fee_total      DOUBLE PRECISION,
    payment_state  VARCHAR(32)
);
