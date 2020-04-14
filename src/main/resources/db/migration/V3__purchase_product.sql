CREATE SCHEMA IF NOT EXISTS public;

DROP TABLE IF EXISTS purchase_product;

CREATE TABLE purchase_product (
    purchase UUID NOT NULL CONSTRAINT FK_purchase_product_purchase REFERENCES purchase,
    product UUID NOT NULL CONSTRAINT FK_purchase_product_product REFERENCES product
);