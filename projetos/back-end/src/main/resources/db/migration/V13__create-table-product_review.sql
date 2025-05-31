CREATE TABLE product_review (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    score INT CHECK (score BETWEEN 1 AND 5),
    comment TEXT,
    product_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES produto(id) ON DELETE CASCADE
);