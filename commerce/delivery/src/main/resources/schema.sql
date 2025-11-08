
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS deliveries;

create TABLE addresses
(
    address_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    country    VARCHAR NOT NULL,
    city       VARCHAR NOT NULL,
    street     VARCHAR NOT NULL,
    house      VARCHAR NOT NULL,
    flat       VARCHAR NOT NULL
);

create TABLE deliveries
(
    delivery_id     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    from_address_id UUID REFERENCES addresses(address_id),
    to_address_id   UUID REFERENCES addresses(address_id),
    delivery_state  VARCHAR NOT NULL,
    order_id        UUID    NOT NULL
);
